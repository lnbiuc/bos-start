package com.pur.formplugin;

import kd.bos.bill.AbstractBillPlugIn;
import kd.bos.bill.OperationStatus;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.entity.datamodel.IDataModel;
import kd.bos.form.CloseCallBack;
import kd.bos.form.FormShowParameter;
import kd.bos.form.ShowType;
import kd.bos.form.control.events.ItemClickEvent;
import kd.bos.form.events.ClosedCallBackEvent;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 采购申请单附件回填功能实现 采购申请单界面插件， 打开动态表单
 */
public class AttachmentbackfillEdit extends AbstractBillPlugIn
{

    /**
     * order 1
     * 打开
     * 从单据中获取需要在动态表单显示的内容，将其以参数传入动态表单
     */
    @Override
    public void itemClick(ItemClickEvent evt)
    {
        super.itemClick(evt);
        String key = evt.getItemKey();
        // 判断是否为添加附件（回填）按钮
        if (StringUtils.equals("tpv_backfill", key)) {
            // 创建弹出动态表单页面对象
            FormShowParameter showParameter = new FormShowParameter();
            // 设置弹出页面的标识
            showParameter.setFormId("tpv_app_back_dialog");
            DynamicObject dataEntity = this.getModel().getDataEntity(true);
            Map<String, Object> map = new HashMap<>();
            // 获取申请人
            DynamicObject applier = dataEntity.getDynamicObject("tpv_applier");

            if (applier != null) {
                map.put("tpv_applier", applier.getLong("id"));
            } else {
                this.getView().showMessage("申请人不能为空");
                return;
            }
            // 获取申请单号
            String billno = dataEntity.getString("billno");
            if (billno != null) {
                map.put("tpv_billno", billno);
            } else {
                this.getView().showMessage("申请单号不能为空");
                return;
            }
            // 获取附件字段的值
            DynamicObjectCollection attachment = dataEntity.getDynamicObjectCollection("tpv_attachment");
            if (attachment != null && !attachment.isEmpty()) {
                // 获取源附件字段附件对象id集合
                List<Long> attachmentIdSet = new ArrayList<>();
                attachment.forEach(attach ->
                {
                    attachmentIdSet.add(attach.getDynamicObject("fbasedataId").getLong("id"));
                });
                // 判断附件数据是否为空
                if (!attachmentIdSet.isEmpty()) {
                    map.put("tpv_attachment", attachmentIdSet);
                }
            }

            // 存入获取到的动态表单数据
            showParameter.setCustomParams(map);
            // 状态
            showParameter.setStatus(OperationStatus.ADDNEW);
            // 设置页面关闭回调方法
            showParameter.setCloseCallBack(new CloseCallBack(this, "tpv_backfill"));
            // 设置弹出页面打开方式
            showParameter.getOpenStyle().setShowType(ShowType.Modal);
            // 打开动态表单
            this.getView().showForm(showParameter);
        }
    }

    /**
     * Order 4
     * 动态表单关闭回调事件
     */
    @Override
    public void closedCallBack(ClosedCallBackEvent e)
    {
        Object returnData = e.getReturnData();
        // 判断标识是否匹配，并验证返回值不为空，不验证返回值可能会报空指针
        if (StringUtils.equals(e.getActionId(), "tpv_backfill")
                && null != e.getReturnData()) {
            @SuppressWarnings("unchecked")
            HashMap<String, Object> values = (HashMap<String, Object>) returnData;
            this.setInwardInfo(values);
        }
    }

    /**
     * Order 5
     * 动态表单通过 returnDataToParent(map) 返回的数据设置到源单据中
     */
    private void setInwardInfo(Map<String, Object> map)
    {
        IDataModel model = this.getModel();
        if (map.containsKey("tpv_attachment") && map.get("tpv_attachment") != null) {
            @SuppressWarnings("unchecked")
            List<Long> attachIdSet = (List<Long>) map.get("tpv_attachment");
            if (!attachIdSet.isEmpty()) {
                model.setValue("tpv_attachment", attachIdSet.toArray());
            }
            // 设置附件数量
            model.setValue("tpv_attachmentcount", attachIdSet.size());
        } else {
            model.setValue("tpv_attachment", null);
            model.setValue("tpv_attachmentcount", 0);
        }
        this.getView().invokeOperation("save");
    }
}
