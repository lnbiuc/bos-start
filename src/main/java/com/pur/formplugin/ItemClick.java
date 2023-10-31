package com.pur.formplugin;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import com.alibaba.druid.util.StringUtils;

import kd.bos.bill.AbstractBillPlugIn;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.form.control.events.BeforeItemClickEvent;

public class ItemClick extends AbstractBillPlugIn
{

    @Override
    public void beforeItemClick(BeforeItemClickEvent evt)
    {
        super.beforeItemClick(evt);
        String itemKey = evt.getItemKey();
        // 判断是否为保存按钮
        if (StringUtils.equals("bar_submit", itemKey)) {
            this.submitIncident(evt);
        }
    }

    public void submitIncident(BeforeItemClickEvent evt)
    {
        // 拿到单据用途字段
        String materiel = (String) this.getView().getModel().getValue("tpv_usage");
        // 获取到采购组织
        DynamicObject billno = (DynamicObject) this.getView().getModel().getValue("tpv_org");
        // 获取申请部门
        DynamicObject applyorg = (DynamicObject) this.getView().getModel().getValue("tpv_applyorg");
        // 获取申请人
        DynamicObject applier = (DynamicObject) this.getView().getModel().getValue("tpv_applier");
        // 获取汇率表
        DynamicObject exrateTable = (DynamicObject) this.getView().getModel().getValue("tpv_exratetable");
        // 获取结算币
        DynamicObject tocurr = (DynamicObject) this.getView().getModel().getValue("tpv_tocurr");
        // 获取汇率日期
        Date exratedate = (Date) this.getView().getModel().getValue("tpv_exratedate");
        List<String> list = new ArrayList<>();
        // 判断用途是否为空
        if (materiel.isEmpty()) {
            list.add("提交时用途不可为空");
        }
        // 判断采购组织是否为空
        if (billno == null) {
            String billnoString = "必填项采购组织为空";
            list.add(billnoString);
        }
        // 判断申请部门是否为空
        if (applyorg == null) {
            String applyorgString = "必填项申请部门为空";
            list.add(applyorgString);
        }
        // 判断申请人是否为空
        if (applier == null) {
            String applierString = "必填项申请人为空";
            list.add(applierString);
        }
        // 判断汇率表是否为空
        if (exrateTable == null) {
            String exrateTableString = "必填项汇率表为空";
            list.add(exrateTableString);
        }
        // 判断结算币是否为空
        if (tocurr == null) {
            String tocurrString = "必填项结算币为空";
            list.add(tocurrString);
        }
        // 判断汇率日期是否为空
        if (exratedate == null) {
            String exratedateString = "必填项汇率日期为空";
            list.add(exratedateString);
        }
        // 判断采购分录是否为空
        DynamicObjectCollection entries = this.getModel().getEntryEntity("tpv_app_proc_req_fl");
        if (entries.isEmpty() || entries.get(0).get("tpv_materia") == null) {
            String exratedateString = "采购分录不能为空";
            list.add(exratedateString);
        }

        String result = String.join(", ", list);
        if (!list.isEmpty()) {
            this.getView().showMessage(result);
            evt.setCancel(true);
        }
    }
}
