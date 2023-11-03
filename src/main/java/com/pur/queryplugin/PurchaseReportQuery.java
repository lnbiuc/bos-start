package com.pur.queryplugin;

import com.kingdee.util.StringUtils;
import kd.bos.algo.DataSet;
import kd.bos.entity.report.AbstractReportListDataPlugin;
import kd.bos.entity.report.FilterInfo;
import kd.bos.entity.report.ReportQueryParam;
import kd.bos.orm.ORM;
import kd.bos.orm.query.QCP;
import kd.bos.orm.query.QFilter;
import kd.sdk.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

/**
 * 报表取数
 */
public class PurchaseReportQuery extends AbstractReportListDataPlugin implements Plugin
{
    @Override
    public DataSet query(ReportQueryParam param, Object o) throws Throwable
    {
        // 获取过滤条件
        FilterInfo filterInfo = param.getFilter();
        String billno = filterInfo.getString("tpv_billno_query");//申请单号
        String status = filterInfo.getString("tpv_billstatus_query");//单据状态
        List<QFilter> list = new ArrayList<>();
        if (!StringUtils.isEmpty(billno)) {
            list.add(new QFilter("billno", QCP.equals, billno));
        }
        if (!StringUtils.isEmpty(status)) {
            list.add(new QFilter("billstatus", QCP.equals, status));
        }

        // 采购申请
        String procReqFields =
                "to_char(id) tpv_id,"
                        + "billno tpv_billno,"
                        + "'采购' tpv_type,"
                        + "billstatus tpv_billstatus,"
                        + "tpv_applyorg tpv_applyorg,"
                        + "tpv_applier tpv_applier,"
                        + "tpv_applydate tpv_date,"
                        + "tpv_app_proc_req_fl.tpv_materia tpv_materia,"
                        + "tpv_app_proc_req_fl.tpv_unit tpv_unit,"
                        + "tpv_app_proc_req_fl.tpv_price tpv_price,"
                        + "tpv_app_proc_req_fl.tpv_applyqty tpv_applyqty,"
                        + "tpv_app_proc_req_fl.tpv_orderedqty tpv_orderedqty,"
                        + "tpv_app_proc_req_fl.tpv_amount tpv_amount";

        DataSet procReqDataSet = ORM.create().queryDataSet(this.getClass().getName(), "tpv_app_proc_req", procReqFields,
                list.toArray(new QFilter[0]));

        // 销售申请
        String salesReqFields =
                "to_char(id) tpv_id,"
                        + "billno tpv_billno,"
                        + "'销售' tpv_type,"
                        + "billstatus tpv_billstatus,"
                        + "tpv_applyorg tpv_applyorg,"
                        + "tpv_applier tpv_applier,"
                        + "tpv_applydate tpv_date,"
                        + "tpv_app_sale_req_fl.tpv_materia tpv_materia,"
                        + "tpv_app_sale_req_fl.tpv_unit tpv_unit,"
                        + "tpv_app_sale_req_fl.tpv_price tpv_price,"
                        + "tpv_app_sale_req_fl.tpv_applyqty tpv_applyqty,"
                        + "tpv_app_sale_req_fl.tpv_salesquantity tpv_orderedqty,"
                        + "tpv_app_sale_req_fl.tpv_amount tpv_amount";

        DataSet salesReqDataSet = ORM.create().queryDataSet(this.getClass().getName(), "tpv_app_proc_sales", salesReqFields,
                list.toArray(new QFilter[0]));
        // 合并采购申请和销售申请
        DataSet union = procReqDataSet.union(salesReqDataSet);

        String procOrderFields =
                // 采购申请单id
                "to_char(tpv_sourcedocid1) tpv_id,"
                        // 采购申请单号
                        + "billno tpv_billno,"
                        // 采购员
                        + "tpv_purchaser tpv_purchaser,"
                        // 已订货数量
                        + "tpv_app_proc_order_fl.tpv_applyqty tpv_orderedquantity";

        DataSet procOrderDataSet = ORM.create().queryDataSet(this.getClass().getName(), "tpv_app_proc_order", procOrderFields,
                list.toArray(new QFilter[0]));

        return union.leftJoin(procOrderDataSet).on("tpv_id", "tpv_id")
                .select("tpv_billno",
                        "tpv_type",
                        "tpv_billstatus",
                        "tpv_applyorg",
                        "tpv_applier",
                        "tpv_date",
                        "tpv_materia",
                        "tpv_unit",
                        "tpv_price",
                        "tpv_applyqty",
                        "tpv_orderedqty",
                        "tpv_amount",
                        "tpv_orderedquantity",
                        "tpv_purchaser")
                .finish();
    }
}