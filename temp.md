```java
package com.pur.workflow;

import com.alibaba.fastjson.JSON;
import kd.bos.bill.BillOperationStatus;
import kd.bos.bill.BillShowParameter;
import kd.bos.bill.OperationStatus;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.form.FormShowParameter;
import kd.bos.form.IFormView;
import kd.bos.form.control.events.ItemClickEvent;
import kd.bos.form.events.BeforeClosedEvent;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.mservice.monitor.healthmanage.indicator.IndicatorInfo;
import kd.bos.workflow.bizflow.graph.model.BillRelationGraphData;
import kd.bos.workflow.bizflow.graph.util.BizFlowGraphUtil;
import kd.bos.workflow.bpmn.graph.codec.GraphCodecContext;
import kd.bos.workflow.bpmn.graph.codec.GraphCodecUtils;
import kd.bos.workflow.bpmn.model.BpmnModel;
import kd.bos.workflow.component.WorkflowDesigner;
import kd.bos.workflow.design.plugin.IWorkflowDesigner;
import kd.bos.workflow.design.plugin.WorkflowViewBPMFlowchartPlugin;
import kd.bos.workflow.engine.WfUtils;
import kd.bos.workflow.engine.WorkflowDevopsService;
import kd.bos.workflow.engine.extitf.ExtItfCallerType;
import kd.bos.workflow.engine.extitf.ExternalInterfaceUtil;
import kd.bos.workflow.engine.impl.persistence.entity.design.ModelType;
import org.dom4j.Node;

import java.util.EventObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BpmFlowViewBillRelationGraphPlugin extends WorkflowViewBPMFlowchartPlugin implements IWorkflowDesigner
{
    private static final String KEY_ID = "id";
    private static final String KEY_RELOAD = "reload";
    private static final String KEY_RELATION_GRAPH = "relation_graph";
    private static final String KEY_LAYOUT_HORIZONTAL = "horizontal";
    private static final String KEY_STACKEDBILLIDSMAP = "stackedBillIds";
    private static final String KEY_ISBILLRELATIONGRAPH = "isBillRelationGraph";
    private static final String PROCINSTID = "procinstid";
    private static final String TOOLBAR = "toolbarap";
    private static final String BTN_CLOSE = "close";
    private static final String BTN_REFRESH = "refresh";
    private static final String BTN_GOBACK = "goback";
    private static final String WORKFLOW_DESIGNER = "workflowdesigner";
    private Log log;

    public BpmFlowViewBillRelationGraphPlugin()
    {
        this.log = LogFactory.getLog(this.getClass());
    }

    public void initialize()
    {
        super.initialize();
        this.addItemClickListeners(new String[]{"toolbarap"});
    }

    public void afterCreateNewData(EventObject e)
    {
        super.afterCreateNewData(e);
        this.initConfigParams();
        this.setIsRelationGraph(true);
    }

    private void initConfigParams()
    {
        HashMap<String, String> config = new HashMap<String, String>(2);
        config.put("horizontal", "true");
        this.getPageCache().put(config);
    }

    public void itemClick(ItemClickEvent evt)
    {
        String key;
        switch (key = evt.getItemKey()) {
            case "refresh": {
                this.refresh();
                break;
            }
            case "goback": {
                this.goback();
                break;
            }
            default: {
                super.itemClick(evt);
            }
        }
    }

    private void switchLayout(Map<String, Object> param)
    {
        boolean horizontal = Boolean.TRUE.equals(param.get("horizontal"));
        if (horizontal) {
            this.getPageCache().put("horizontal", "true");
        } else {
            this.getPageCache().put("horizontal", "false");
        }
        this.refresh();
        HashMap<String, Boolean> params = new HashMap<String, Boolean>();
        params.put("block", false);
        this.getDesigner().notify("showBlock", params, this.getView());
    }

    private void goback()
    {
        this.getDesigner().cleanRecordsState();
        this.hideNodeDetailPanel();
        this.setIsRelationGraph(true);
        String procInstId = this.getPageCache().get("id");
        if (procInstId != null) {
            this.getModel().setValue("procinstid", (Object) Long.valueOf(procInstId));
        } else {
            this.getModel().setValue("procinstid", (Object) null);
        }
        this.getView().setVisible(Boolean.FALSE, BUTTONS);
        this.getView().setVisible(Boolean.FALSE,
                new String[]{"goback", "barprecomputator", "viewbillrelation", "barviewapprovalrecord"});
        this.reloadBillRelationGraph();
    }

    private void refresh()
    {
        String isRelation = this.getPageCache().get("relation_graph");
        if ("true".equals(isRelation)) {
            this.reloadBillRelationGraph();
            this.hideNodeDetailPanel();
        } else {
            super.refreshPage();
        }
    }

    private void hideNodeDetailPanel()
    {
        this.getView().setVisible(Boolean.FALSE, new String[]{"summaryandapproval"});
    }

    private void reloadBillRelationGraph()
    {
        WorkflowDesigner designer = this.getDesigner();
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("reload", Boolean.TRUE);
        params.put("procInstId", this.getModel().getValue("procinstid"));
        FormShowParameter parameter = this.getView().getFormShowParameter();
        params.put("billId", parameter.getCustomParam("billId"));
        params.put("entityNumber", parameter.getCustomParam("entityNumber"));
        try {
            Map<String, Object> initData = this.getDesignerInitData(params);
            if (initData != null) {
                initData.put("time", String.valueOf(WfUtils.now().getTime()));
                designer.open("WorkflowModel", initData);
            }
        } catch (Exception e) {
            this.showLoadErrorNotification(e);
        }
    }

    private void showLoadErrorNotification(Exception e)
    {
        this.getView()
                .showErrorNotification(ResManager.loadKDString((String) "计算上下游单据关系失败，暂不支持当前业务场景。",
                        (String) "BpmFlowViewBillRelationGraphPlugin_7", (String) "bos-wf-formplugin",
                        (Object[]) new Object[0]));
        this.log.error(WfUtils.getExceptionStacktrace((Throwable) e));
    }

    private WorkflowDesigner getDesigner()
    {
        return (WorkflowDesigner) this.getControl("workflowdesigner");
    }

    public void afterLoadInitData(Map<String, Object> params)
    {
        try {
            String id = (String) params.get("procInstId");
            if (WfUtils.isEmpty((String) id)) {
                boolean horizontal;
                String appId = this.getView().getFormShowParameter().getAppId();
                String businessKey = (String) params.get("billId");
                String entityNumber = (String) params.get("entityNumber");
                BillRelationGraphData graphData = BizFlowGraphUtil.createBillBOTPRelationModel((String) entityNumber,
                        (String) businessKey, (String) appId,
                        (boolean) (horizontal = "true".equals(this.getPageCache().get("horizontal"))), (boolean) true);
                if (graphData != null) {
                    if (WfUtils.isNotEmpty((String) graphData.getTip())) {
                        this.getView().showTipNotification(graphData.getTip());
                    }
                    BpmnModel bpmnModel = graphData.getBpmnModel();
                    this.getPageCache().put("stackedBillIds",
                            JSON.toJSONString((Object) graphData.getBusinessKeysMap()));
                    Map<String, Object> initData = this.getInitData(bpmnModel);
                    initData.put("time", String.valueOf(WfUtils.now().getTime()));
                    this.getDesigner().open("WorkflowModel", initData);
                }
            }
        } catch (Exception e) {
            this.showLoadErrorNotification(e);
        }
    }

    public Map<String, Object> getDesignerInitData(Map<String, Object> params)
    {
        this.getPageCache().put("showAdminButtons", "true".equals(params.get("showButtons")) ? "true" : "false");
        String isRelation = this.getPageCache().get("relation_graph");
        if ("false".equals(isRelation)) {
            return super.getDesignerInitData(params);
        }
        this.getView().setVisible(Boolean.TRUE, new String[]{"refresh", "close"});
        String id = (String) params.get("procInstId");
        String businessKey = (String) params.get("billId");
        String entityNumber = (String) params.get("entityNumber");
        boolean horizontal = "true".equals(this.getPageCache().get("horizontal"));
        String appId = this.getView().getFormShowParameter().getAppId();
        BpmnModel bpmnModel = null;
        BillRelationGraphData graphData = null;
        try {
            if (WfUtils.isEmpty((String) id)) {
                graphData = BizFlowGraphUtil.createBillBOTPRelationModel((String) entityNumber, (String) businessKey,
                        (String) appId, (boolean) horizontal, (boolean) Boolean.TRUE.equals(params.get("reload")));
                WorkflowDevopsService.create()
                        .exceutionDataCollection(new IndicatorInfo("billRelationGraph", (Object) "botp"));
            } else {
                if (this.getPageCache().get("id") == null) {
                    this.getPageCache().put("id", id);
                }
                Long procInstId = Long.valueOf(id);
                this.getModel().setValue("procinstid", (Object) procInstId);
                graphData = BizFlowGraphUtil.createBillRelationModel((Long) procInstId, (String) businessKey,
                        (String) appId, (boolean) horizontal);
                WorkflowDevopsService.create()
                        .exceutionDataCollection(new IndicatorInfo("billRelationGraph", (Object) "bizflow"));
            }
        } catch (Exception e) {
            this.showLoadErrorNotification(e);
        }
        if (graphData == null) {
            bpmnModel = BizFlowGraphUtil.createBpmnModel((String) ModelType.BizFlow.name(),
                    (String) "bill_circulaterelation");
            this.getView()
                    .showTipNotification(ResManager.loadKDString((String) "该流程暂无单据关系数据。",
                            (String) "BpmFlowViewBillRelationGraphPlugin_8", (String) "bos-wf-formplugin",
                            (Object[]) new Object[0]));
            return this.getInitData(bpmnModel);
        }
        bpmnModel = graphData.getBpmnModel();
        this.getPageCache().put("stackedBillIds", JSON.toJSONString((Object) graphData.getBusinessKeysMap()));
        if (WfUtils.isNotEmpty((String) graphData.getTip())) {
            this.getView().showTipNotification(graphData.getTip());
        }
        return this.getInitData(bpmnModel);
    }

    private Map<String, Object> getInitData(BpmnModel bpmnModel)
    {
        GraphCodecContext context = new GraphCodecContext(bpmnModel);
        context.setBillRelation(true);
        HashMap<String, Object> data = new HashMap<String, Object>();
        Node xml = GraphCodecUtils.convertBpmnModelToNode((BpmnModel) bpmnModel, (GraphCodecContext) context);
        data.put("graph_xml", xml.asXML());
        data.put("flowType", ModelType.BizFlow.name());
        data.put("isBillRelationGraph", context.isBillRelation());
        return data;
    }

    public void updateProcInstId(String procInstId, Map<String, Object> data)
    {
        this.setIsRelationGraph(false);
        this.getModel().setValue("procinstid", (Object) procInstId);
        this.getView().setVisible(Boolean.TRUE, new String[]{"goback"});
        super.refreshPage();
    }

    public void handleCustomEvent(String type, Map<String, Object> param)
    {
        switch (type) {
            case "showBill": {
                this.showBill(param);
                break;
            }
            case "showStackedBills": {
                this.showStackedBills(param);
                break;
            }
            case "selectCard": {
                this.selectCard(param);
                break;
            }
            case "deselectCard": {
                this.deselectCard(param);
                break;
            }
            case "afterShowIconTip": {
                this.afterShowIconTip(param);
                break;
            }
            case "switchLayout": {
                this.switchLayout(param);
                break;
            }
        }
    }

    private void afterShowIconTip(Map<String, Object> param)
    {
        try {
            WorkflowDesigner designer = (WorkflowDesigner) this.getView().getControl("workflowdesigner");
            String entityNumber = (String) param.get("entityNumber");
            String businessKey = (String) param.get("businessKey");
            String plugin = (String) param.get("plugin");
            if (!ExternalInterfaceUtil.isValidValue((String) plugin)) {
                return;
            }
            Object result = ExternalInterfaceUtil.executeExtItf((ExtItfCallerType) ExtItfCallerType.GETPUSHSTATUS,
                    (String) plugin, (Object[]) new Object[]{entityNumber, businessKey});
            if (result instanceof Object[]) {
                Object[] infos = (Object[]) result;
                String info = this.getStringInfo(infos, 0);
                if (info != null) {
                    param.put("first", info);
                }
                if ((info = this.getStringInfo(infos, 1)) != null) {
                    param.put("second", info);
                }
                if ((info = this.getStringInfo(infos, 2)) != null) {
                    param.put("third", info);
                }
                designer.renderTooltipData(param);
            }
        } catch (Exception e) {
            this.log.error(WfUtils.getExceptionStacktrace((Throwable) e));
            this.getView().showErrorNotification(e.getMessage());
        }
    }

    private String getStringInfo(Object[] infos, int index)
    {
        Object info;
        int length = infos.length;
        if (length > index && (info = infos[index]) instanceof String) {
            return (String) info;
        }
        return null;
    }

    private void selectCard(Map<String, Object> params)
    {
        String businessKey = (String) params.get("businessKey");
        Boolean lock = (Boolean) params.get("lock");
        if (WfUtils.isEmpty((String) businessKey) || Boolean.TRUE.equals(lock)) {
            return;
        }
        String activityInstId = (String) params.get("activityInstId");
        String entityNumber = (String) params.get("entityNumber");
        if (WfUtils.isNotEmpty((String) activityInstId) && !"0".equals(activityInstId)
                || WfUtils.isNotEmpty((String) entityNumber)) {
            this.updateBillSummaryAndApprovalRecored(activityInstId, entityNumber, businessKey, true);
        } else {
            this.hideNodeDetailPanel();
        }
    }

    private void deselectCard(Map<String, Object> param)
    {
        this.hideNodeDetailPanel();
    }

    private void showBill(Map<String, Object> param)
    {
        String entityName = (String) param.get("entityName");
        String entityNumber = (String) param.get("entityNumber");
        String businessKey = (String) param.get("businessKey");
        BillShowParameter showParameter = new BillShowParameter();
        showParameter.setBillStatus(BillOperationStatus.VIEW);
        showParameter.setStatus(OperationStatus.VIEW);
        showParameter.setFormId(entityNumber);
        showParameter.setParentPageId(this.getView().getPageId());
        showParameter.setPkId((Object) businessKey);
        showParameter.setCustomParam("hasright", (Object) Boolean.TRUE);
        showParameter.setCustomParam("isIgnoreLicense", (Object) Boolean.TRUE);
        ViewBillRelationGraphUtil.showBillForm((FormShowParameter) showParameter, (String) entityName,
                (IFormView) this.getView());
    }

    private void showStackedBills(Map<String, Object> param)
    {
        String entityNumber = (String) param.get("entityNumber");
        String billIds = this.getPageCache().get("stackedBillIds");
        Map billIdsMap = new HashMap();
        if (WfUtils.isNotEmpty((String) billIds)) {
            billIdsMap = (Map) JSON.parseObject((String) billIds, Map.class);
        }
        String itemId = (String) param.get("itemId");
        List ids = (List) billIdsMap.get(itemId);
        Object title = param.get("title");
        ViewBillRelationGraphUtil.showStackedBills((String) entityNumber, (List) ids, (Object) title,
                (IFormView) this.getView(), (String) this.getView().getPageId());
    }

    private void setIsRelationGraph(boolean isRelationGraph)
    {
        this.getPageCache().put("relation_graph", isRelationGraph ? "true" : "false");
    }

    protected void switchButtonVisibility(Long procInstId)
    {
        super.switchButtonVisibility(procInstId);
        if (this.getView().getFormShowParameter().getCustomParam("billId") != null) {
            this.getView().setVisible(Boolean.FALSE, BUTTONS);
        }
    }

    public void beforeClosed(BeforeClosedEvent e)
    {
        super.beforeClosed(e);
        this.getPageCache().remove("id");
        this.getPageCache().remove("relation_graph");
        this.getPageCache().remove("horizontal");
        this.getPageCache().remove("stackedBillIds");
    }
}
```