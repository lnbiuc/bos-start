package com.pur.formplugin;

import com.kingdee.util.StringUtils;
import kd.bos.bill.AbstractBillPlugIn;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.entity.datamodel.IDataModel;
import kd.bos.form.FormShowParameter;
import kd.bos.form.ShowType;
import kd.bos.form.control.events.ItemClickEvent;
import kd.bos.workflow.form.operate.flowchart.ViewFlowchartConstant;

import java.util.HashMap;

public class ShowGraph extends AbstractBillPlugIn
{
    /**
     * 打开动态弹窗点击事件
     *
     * @param e 点击事件
     */
    @Override
    public void itemClick(ItemClickEvent e)
    {
        super.itemClick(e);
        String key = e.getItemKey();
        IDataModel model = this.getModel();
        if (StringUtils.equals("tpv_showgraph", key)) {
            DynamicObjectCollection treeData = model.getEntryEntity("tpv_treeentity");
            if (treeData != null && !treeData.isEmpty()) {
                FormShowParameter parameter = new FormShowParameter();
                parameter.setFormId("tpv_app_showgraph");
                HashMap<String, Object> map = new HashMap<>();
                map.put("entity", treeData);
                parameter.setCustomParams(map);
                parameter.setClientParam(ViewFlowchartConstant.PROCINSTID, treeData);
                parameter.getOpenStyle().setShowType(ShowType.MainNewTabPage);
                this.getView().showForm(parameter);
            }
        }
    }
}
