package com.pur.cardplugin;

import kd.bos.dataentity.resource.ResManager;
import kd.bos.entity.datamodel.events.ChangeData;
import kd.bos.entity.datamodel.events.PropertyChangedArgs;
import kd.bos.entity.report.FilterInfo;
import kd.bos.entity.report.ReportQueryParam;
import kd.bos.form.ShowType;
import kd.bos.form.control.Control;
import kd.bos.form.control.Label;
import kd.bos.form.control.events.ClickListener;
import kd.bos.orm.query.QFilter;
import kd.bos.report.ReportShowParameter;
import kd.tmc.fbp.common.helper.TmcOrgDataHelper;
import kd.tmc.fbp.common.util.DateUtils;
import kd.tmc.mon.common.helper.OrgHelper;
import kd.tmc.mon.formplugin.index.TraceInformationPlugin;

import java.math.BigDecimal;
import java.util.*;

/**
 * 交易信息
 */
public class MyTraceInformationPlugin extends TraceInformationPlugin implements ClickListener
{
    private final String cardName = ResManager.loadKDString("交易信息", "TraceInformationPlugin_0", "tpv_app_deal_trace", new Object[0]);


    /**
     * intotalsum 收入资金
     * outtotalsum 支出资金
     * inticketsum 收入票据
     * outticketsum 支出票据
     */
    @Override
    public void registerListener(EventObject e)
    {
        super.registerListener(e);
        this.addClickListeners("tpv_summoney");
        this.getView().getFormShowParameter().setListentimerElapsed(true);
    }

    @Override
    public void propertyChanged(PropertyChangedArgs e)
    {
        super.propertyChanged(e);
        String propName = e.getProperty().getName();
        ChangeData[] valueSet = e.getChangeSet();
        String newValue = (String) valueSet[0].getNewValue();
        if (propName.equals("tpv_querycycle")) {
            Date beginDate = this.getDateByQueryCycle(newValue);
            this.getSumMoney(beginDate, newValue);
        }

    }

    @Override
    public void afterCreateNewData(EventObject e)
    {
        super.afterCreateNewData(e);
        Date beginDate = this.getDateByQueryCycle("month");
        this.getSumMoney(beginDate, (String) null);
    }

    private void getSumMoney(Date beginDate, String queryCycle)
    {
        List<Long> orgList = this.getOrgList();
        if (orgList != null) {
            QFilter orgFilter = new QFilter("company", "in", orgList.toArray());
            // 获取组织本位币
            Long currency = OrgHelper.getMainCurrency(TmcOrgDataHelper.getCurrentOrgId());
            if (currency != null && !"0".equals(String.valueOf(currency))) {
                HashMap<String, Object> returnData = this.sumMoney(orgList, currency, beginDate);
                if (returnData != null) {
                    BigDecimal sumMoneyUnit = (BigDecimal) returnData.get("tpv_summoney");
                    BigDecimal sumMoney = null;
                    if (null != sumMoneyUnit && sumMoneyUnit.compareTo(BigDecimal.ZERO) != 0) {
                        sumMoney = sumMoneyUnit.divide(new BigDecimal(10000), 2, 4);
                    }

                    if (sumMoney == null) {
                        sumMoneyUnit = sumMoney = BigDecimal.ZERO;
                    }

                    Label label = (Label) this.getView().getControl("tpv_summoney");
                    if (sumMoney == null) {
                        sumMoney = BigDecimal.ZERO;
                    }

                    label.setText(this.getCurrencyName(currency, new String[]{"sign"}) + this.getNumberFormat(sumMoney));
                    Object mainCurrencyMoney = returnData.get("MainCurrencyMoney");
                    if (mainCurrencyMoney == null) {
                        mainCurrencyMoney = BigDecimal.ZERO;
                    }

                    BigDecimal sumMoneyMainCurrency = (BigDecimal) mainCurrencyMoney;
                    BigDecimal sumMoneyNotMainCurrency = (BigDecimal) returnData.get("maxCurrencyMoney");
                    if (sumMoneyNotMainCurrency == null) {
                        sumMoneyNotMainCurrency = BigDecimal.ZERO;
                    }

                    Object currencyPk = returnData.get("currencyMaxId");
                    // 绘制饼图
                    this.drawPiechart(currency, sumMoneyMainCurrency, currencyPk, sumMoneyNotMainCurrency, sumMoneyUnit, "trace", this.cardName);
                    BigDecimal incount = (BigDecimal) returnData.get("incount");
                    BigDecimal outcount = (BigDecimal) returnData.get("outcount");
                    BigDecimal inTotal = (BigDecimal) returnData.get("inTotal");
                    BigDecimal outTotal = (BigDecimal) returnData.get("outTotal");
                    Label intotal = (Label) this.getView().getControl("intotal");
                    Label outtotal = (Label) this.getView().getControl("outtotal");
                    Label inticket = (Label) this.getView().getControl("inticket");
                    Label outticket = (Label) this.getView().getControl("outticket");
                    Label[] labels = new Label[]{intotal, outtotal, inticket, outticket};
                    Label intotalsum = (Label) this.getView().getControl("intotalsum");
                    Label outtotalsum = (Label) this.getView().getControl("outtotalsum");
                    Label inticketsum = (Label) this.getView().getControl("inticketsum");
                    Label outticketsum = (Label) this.getView().getControl("outticketsum");
                    Label[] labelsums = new Label[]{intotalsum, outtotalsum, inticketsum, outticketsum};
                    String unit = ResManager.loadKDString("张", "TraceInformationPlugin_2", "tmc-mon-formplugin", new Object[0]);
                    String unitCapital = ResManager.loadKDString("笔", "TraceInformationPlugin_3", "tmc-mon-formplugin", new Object[0]);
                    int flag = 0;
                    if (incount != null && incount.compareTo(BigDecimal.ZERO) != 0) {
                        labels[flag].setText(ResManager.loadKDString("收入资金", "TraceInformationPlugin_4", "tmc-mon-formplugin", new Object[0]) + "(" + incount + unitCapital + ")");
                        labelsums[flag].setText(this.getCurrencyName(currency, new String[]{"sign"}) + this.getNumberFormat(inTotal.divide(new BigDecimal(10000), 2, 4)));
                        ++flag;
                    }

                    if (outcount != null && outcount.compareTo(BigDecimal.ZERO) != 0) {
                        labels[flag].setText(ResManager.loadKDString("支出资金", "TraceInformationPlugin_5", "tmc-mon-formplugin", new Object[0]) + "(" + outcount + unitCapital + ")");
                        labelsums[flag].setText(this.getCurrencyName(currency, new String[]{"sign"}) + this.getNumberFormat(outTotal.divide(new BigDecimal(10000), 2, 4)));
                        ++flag;
                    }

                    HashMap<String, BigDecimal> dataMap = this.inOutMoneyCountBill(orgFilter, currency, beginDate);
                    BigDecimal payCount = (BigDecimal) dataMap.get("payCount");
                    BigDecimal recCount = (BigDecimal) dataMap.get("recCount");
                    BigDecimal payTotal = (BigDecimal) dataMap.get("payTotal");
                    BigDecimal recTotal = (BigDecimal) dataMap.get("recTotal");
                    if (payCount != null && payCount.compareTo(BigDecimal.ZERO) != 0) {
                        labels[flag].setText(ResManager.loadKDString("收入票据", "TraceInformationPlugin_6", "tmc-mon-formplugin", new Object[0]) + "(" + payCount + unit + ")");
                        labelsums[flag].setText(this.getCurrencyName(currency, new String[]{"sign"}) + this.getNumberFormat(payTotal.divide(new BigDecimal(10000), 2, 4)));
                        ++flag;
                    }

                    if (recCount != null && recCount.compareTo(BigDecimal.ZERO) != 0) {
                        labels[flag].setText(ResManager.loadKDString("支出票据", "TraceInformationPlugin_7", "tmc-mon-formplugin", new Object[0]) + "(" + recCount + unit + ")");
                        labelsums[flag].setText(this.getCurrencyName(currency, new String[]{"sign"}) + this.getNumberFormat(recTotal.divide(new BigDecimal(10000), 2, 4)));
                        ++flag;
                    }

                    while (flag <= 3) {
                        labels[flag].setText((String) null);
                        labelsums[flag].setText((String) null);
                        ++flag;
                    }

                }
            } else {
                this.getView().showTipNotification(ResManager.loadKDString("请设置组织本位币别", "TraceInformationPlugin_1", "tmc-mon-formplugin", new Object[0]));
            }
        }
    }

    private HashMap<String, BigDecimal> inOutMoneyCountBill(QFilter orgFilter, Long currency, Date beginDate)
    {
        HashMap<String, BigDecimal> returnData = new HashMap<>();
        returnData.put("payCount", BigDecimal.valueOf(23));
        returnData.put("recCount", BigDecimal.valueOf(213));
        returnData.put("payTotal", BigDecimal.valueOf(345));
        returnData.put("recTotal", BigDecimal.valueOf(345));
        return returnData;
    }

    private Date getCalendarEndTime()
    {
        Object querycycle = this.getModel().getValue("tpv_querycycle");
        Date endDate = null;
        Date currDate = DateUtils.getCurrentDate();
        if ("date".equals(String.valueOf(querycycle))) {
            endDate = DateUtils.getNextDay(currDate, 1);
        } else {
            endDate = DateUtils.getFirstDayOfMonth(DateUtils.getNextMonth(currDate, 1));
        }

        return endDate;
    }

    private HashMap<String, Object> sumMoney(List<Long> orgList, Long aimCurrency, Date beginDate)
    {
        HashMap<String, Object> returnMap = new HashMap();

        returnMap.put("currencyMaxId", 123123);
        returnMap.put("maxCurrencyMoney", 3123143);
        returnMap.put("tpv_summoney", 2342342);
        returnMap.put("MainCurrencyMoney", 3142312);
        returnMap.put("incount", 1232131);
        returnMap.put("outcount", 234234);
        returnMap.put("inTotal", 234234);
        returnMap.put("outTotal", 23423423);
        return returnMap;

    }

    private Date getDateByQueryCycle(String queryCycle)
    {
        Date beginDate = null;
        switch (queryCycle) {
            case "month":
                beginDate = DateUtils.getFirstDayOfCurMonth();
                break;
            default:
                beginDate = DateUtils.getCurrentDate();
        }

        return beginDate;
    }

    private void showFormCapitalSumrpt()
    {
        ReportShowParameter showParameter = new ReportShowParameter();
        showParameter.setFormId("mon_fundflow");
        Map<String, Object> params = new HashMap();
        showParameter.setCustomParams(params);
        showParameter.getOpenStyle().setShowType(ShowType.MainNewTabPage);
        Long currencyId = OrgHelper.getMainCurrency(TmcOrgDataHelper.getCurrentOrgId());
        ReportQueryParam queryParam = new ReportQueryParam();
        FilterInfo filter = new FilterInfo();
        filter.addFilterItem("filter_stadimension", "01");
        String value = (String) this.getModel().getValue("tpv_querycycle");
        if (value.equals("date")) {
            filter.addFilterItem("filter_staperiod", "05");
        } else {
            filter.addFilterItem("filter_staperiod", "04");
        }

        filter.addFilterItem("filter_repcurrency", currencyId);
        queryParam.setFilter(filter);
        showParameter.setQueryParam(queryParam);
        showParameter.setPageId(this.getView().getPageId() + "fromtrace");
        this.getView().showForm(showParameter);
    }

    @Override
    public void click(EventObject evt)
    {
        super.click(evt);
        Control c = (Control) evt.getSource();
        String key = c.getKey().toLowerCase();
        if ("tpv_summoney".equals(key)) {
            this.showFormCapitalSumrpt();
        }

    }
}
