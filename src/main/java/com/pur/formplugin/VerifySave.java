package com.pur.formplugin;

import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.entity.plugin.AbstractOperationServicePlugIn;
import kd.bos.entity.plugin.args.BeforeOperationArgs;

import java.util.Date;


public class VerifySave extends AbstractOperationServicePlugIn
{

    @Override
    public void beforeExecuteOperationTransaction(BeforeOperationArgs e)
    {
        super.beforeExecuteOperationTransaction(e);
        String operationKey = e.getOperationKey();
        //点提交按钮
        if (operationKey.equals("bar_submit")) {
            verifySaveDate(e);
        }
    }

    public void verifySaveDate(BeforeOperationArgs e)
    {
        DynamicObject[] entities = e.getDataEntities();
        for (DynamicObject bill : entities) {

            if (bill.get("tpv_usage") == null) {
                e.setCancel(true);
                e.setCancelMessage("用途不能为空");
                return;
            }
            if (bill.get("tpv_org") == null) {
                e.setCancel(true);
                e.setCancelMessage("采购组织不能为空");
                return;
            }
            if (bill.get("tpv_applyorg") == null) {
                e.setCancel(true);
                e.setCancelMessage("申请部门不能为空");
                return;
            }
            if (bill.get("tpv_applier") == null) {
                e.setCancel(true);
                e.setCancelMessage("申请人不能为空");
                return;
            }
            if (bill.get("tpv_exratetable") == null) {
                e.setCancel(true);
                e.setCancelMessage("汇率表不能为空");
                return;
            }
            if (bill.get("tpv_tocurr") == null) {
                e.setCancel(true);
                e.setCancelMessage("结算币不能为空");
                return;
            }
            if (bill.get("tpv_exratedate") == null) {
                e.setCancel(true);
                e.setCancelMessage("汇率日期不能为空");
                return;
            }

            DynamicObjectCollection entries = bill.getDynamicObjectCollection("tpv_app_proc_req_fl");
            if (entries.isEmpty() || entries.get(0).get("tpv_materia") == null) {
                e.setCancel(true);
                e.setCancelMessage("采购分录不能为空");
                return;
            }
            //查询当前日期
            Date currentDate = new Date();
            for (DynamicObject entity : entries) {
                if (entity.get("tpv_materia") == null) {
                    e.setCancel(true);
                    e.setCancelMessage("采购分录物料不能为空");
                    return;
                }
                //需求日期
                Date demandDate = entity.getDate("tpv_requiredate");
                //判断需求日期是否为空
                if (demandDate != null) {
                    //判断需求日期是否小于当前日期
                    if (demandDate.before(currentDate)) {
                        e.setCancel(true);
                        e.setCancelMessage("采购分录需求日期不能小于当前日期");
                    }
                }
            }
        }
    }
}
