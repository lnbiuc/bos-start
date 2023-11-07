package com.pur.formplugin;

import com.kingdee.util.StringUtils;
import kd.bos.bill.AbstractBillPlugIn;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.form.FormShowParameter;
import kd.bos.form.ShowType;
import kd.bos.form.control.events.ItemClickEvent;
import kd.bos.workflow.form.operate.flowchart.ViewFlowchartConstant;

public class ShowGraph extends AbstractBillPlugIn
{
    @Override
    public void itemClick(ItemClickEvent e)
    {
        super.itemClick(e);
        String key = e.getItemKey();
        if (StringUtils.equals("tpv_showgraph", key)) {
            FormShowParameter parameter = new FormShowParameter();
            parameter.setFormId("tpv_app_showgraph");
            DynamicObjectCollection treeData = this.getModel().getEntryEntity("tpv_treeety");
            parameter.setClientParam(ViewFlowchartConstant.PROCINSTID, treeData);
            parameter.getOpenStyle().setShowType(ShowType.MainNewTabPage);
            this.getView().showForm(parameter);
            // 获取树型单据体数据
            DynamicObjectCollection tree = this.getModel().getEntryEntity("tpv_treeety");
            tree.forEach(t ->
            {

                System.out.println(t);
            });
            //
        }
    }
}
