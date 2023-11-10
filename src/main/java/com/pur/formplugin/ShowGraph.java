package com.pur.formplugin;

import com.kingdee.util.StringUtils;
import kd.bos.bill.AbstractBillPlugIn;
import kd.bos.dataentity.entity.DynamicObjectCollection;
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
        if (StringUtils.equals("tpv_showgraph", key)) {
            FormShowParameter parameter = new FormShowParameter();
            // 设置动态表单id
            parameter.setFormId("tpv_app_showgraph");
            // 获取树型分录数据
            DynamicObjectCollection treeData = this.getModel().getEntryEntity("tpv_treeentryentity");
            if (treeData != null && !treeData.isEmpty()) {
                // 加载数据
                HashMap<String, Object> map = new HashMap<>();
                map.put("entity", treeData);
                parameter.setCustomParams(map);
                // 设置客户端参数
                parameter.setClientParam(ViewFlowchartConstant.PROCINSTID, treeData);
                // 设置打开方式，新标签页打开
                parameter.getOpenStyle().setShowType(ShowType.MainNewTabPage);
                this.getView().showForm(parameter);
            } else {
                // 如果树型分录数据为空，显示信息
                this.getView().showTipNotification("树形单据体内容不能为空");
            }
        }
    }
}
