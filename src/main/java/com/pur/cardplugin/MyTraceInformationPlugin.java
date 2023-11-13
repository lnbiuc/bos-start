package com.pur.cardplugin;

import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.entity.datamodel.events.ChangeData;
import kd.bos.entity.datamodel.events.PropertyChangedArgs;
import kd.bos.entity.report.FilterInfo;
import kd.bos.entity.report.ReportQueryParam;
import kd.bos.form.ShowType;
import kd.bos.form.chart.*;
import kd.bos.form.control.Control;
import kd.bos.form.control.Label;
import kd.bos.form.control.events.ClickListener;
import kd.bos.orm.query.QFilter;
import kd.bos.report.ReportShowParameter;
import kd.bos.servicehelper.BusinessDataServiceHelper;
import kd.tmc.fbp.common.helper.TmcOrgDataHelper;
import kd.tmc.fbp.common.util.DateUtils;
import kd.tmc.mon.common.helper.BaseDataHelper;
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
        this.addClickListeners("tpv_summoney");
        this.getView().getFormShowParameter().setListentimerElapsed(true);
    }

    @Override
    public void propertyChanged(PropertyChangedArgs e)
    {
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
                    drawPiechart(currency, sumMoneyMainCurrency, currencyPk, sumMoneyNotMainCurrency, sumMoneyUnit, "trace", this.cardName);
                    BigDecimal incount = (BigDecimal) returnData.get("incount");
                    BigDecimal outcount = (BigDecimal) returnData.get("outcount");
                    BigDecimal inTotal = (BigDecimal) returnData.get("inTotal");
                    BigDecimal outTotal = (BigDecimal) returnData.get("outTotal");
                    Label intotal = this.getView().getControl("tpv_intotal");
                    Label outtotal = this.getView().getControl("tpv_outtotal");
                    Label inticket = this.getView().getControl("tpv_inticket");
                    Label outticket = this.getView().getControl("tpv_outticket");
                    Label[] labels = new Label[]{intotal, outtotal, inticket, outticket};
                    Label intotalsum = this.getView().getControl("tpv_intotalsum");
                    Label outtotalsum = this.getView().getControl("tpv_outtotalsum");
                    Label inticketsum = this.getView().getControl("tpv_inticketsum");
                    Label outticketsum = this.getView().getControl("tpv_outticketsum");
                    Label[] labelsums = new Label[]{intotalsum, outtotalsum, inticketsum, outticketsum};
                    String unit = ResManager.loadKDString("张", "TraceInformationPlugin_2", "tpv_app_deal_trace", new Object[0]);
                    String unitCapital = ResManager.loadKDString("笔", "TraceInformationPlugin_3", "tpv_app_deal_trace", new Object[0]);
                    int flag = 0;
                    if (incount != null && incount.compareTo(BigDecimal.ZERO) != 0) {
                        labels[flag].setText(ResManager.loadKDString("收入资金", "TraceInformationPlugin_4", "tpv_app_deal_trace", new Object[0]) + "(" + incount + unitCapital + ")");
                        labelsums[flag].setText(this.getCurrencyName(currency, new String[]{"sign"}) + this.getNumberFormat(inTotal.divide(new BigDecimal(10000), 2, 4)));
                        ++flag;
                    }

                    if (outcount != null && outcount.compareTo(BigDecimal.ZERO) != 0) {
                        labels[flag].setText(ResManager.loadKDString("支出资金", "TraceInformationPlugin_5", "tpv_app_deal_trace", new Object[0]) + "(" + outcount + unitCapital + ")");
                        labelsums[flag].setText(this.getCurrencyName(currency, new String[]{"sign"}) + this.getNumberFormat(outTotal.divide(new BigDecimal(10000), 2, 4)));
                        ++flag;
                    }

                    HashMap<String, BigDecimal> dataMap = this.inOutMoneyCountBill(orgFilter, currency, beginDate);
                    BigDecimal payCount = (BigDecimal) dataMap.get("payCount");
                    BigDecimal recCount = (BigDecimal) dataMap.get("recCount");
                    BigDecimal payTotal = (BigDecimal) dataMap.get("payTotal");
                    BigDecimal recTotal = (BigDecimal) dataMap.get("recTotal");
                    if (payCount != null && payCount.compareTo(BigDecimal.ZERO) != 0) {
                        labels[flag].setText(ResManager.loadKDString("收入票据", "TraceInformationPlugin_6", "tpv_app_deal_trace", new Object[0]) + "(" + payCount + unit + ")");
                        labelsums[flag].setText(this.getCurrencyName(currency, new String[]{"sign"}) + this.getNumberFormat(payTotal.divide(new BigDecimal(10000), 2, 4)));
                        ++flag;
                    }

                    if (recCount != null && recCount.compareTo(BigDecimal.ZERO) != 0) {
                        labels[flag].setText(ResManager.loadKDString("支出票据", "TraceInformationPlugin_7", "tpv_app_deal_trace", new Object[0]) + "(" + recCount + unit + ")");
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
                this.getView().showTipNotification(ResManager.loadKDString("请设置组织本位币别", "TraceInformationPlugin_1", "tpv_app_deal_trace", new Object[0]));
            }
        }
    }

    private HashMap<String, BigDecimal> inOutMoneyCountBill(QFilter orgFilter, Long currency, Date beginDate)
    {
        QFilter begQf = (new QFilter("issuedate", ">", beginDate)).or(new QFilter("issuedate", "=", beginDate));
        QFilter endQf = new QFilter("issuedate", "<", this.getCalendarEndTime());
        DynamicObject[] payablebills = BusinessDataServiceHelper.load("cdm_payablebill", "id,currency,amount", new QFilter[]{orgFilter, begQf.and(endQf)});
        HashMap<Long, BigDecimal> payTotalMoney = new HashMap();
        HashMap<Long, BigDecimal> recTotalMoney = new HashMap();
        BigDecimal recCount = BigDecimal.ZERO;
        BigDecimal payCount = BigDecimal.ZERO;
        if (payablebills != null && payablebills.length > 0) {
            DynamicObject[] var11 = payablebills;
            int var12 = payablebills.length;

            for (int var13 = 0; var13 < var12; ++var13) {
                DynamicObject payablebill = var11[var13];
                DynamicObject currencyData = payablebill.getDynamicObject("currency");
                if (currencyData != null) {
                    Long currencyId = (Long) payablebill.getDynamicObject("currency").getPkValue();
                    BigDecimal amount = payablebill.getBigDecimal("amount");
                    BigDecimal sumMoney = (BigDecimal) payTotalMoney.get(currencyId);
                    if (sumMoney == null) {
                        payTotalMoney.put(currencyId, amount);
                    } else {
                        sumMoney = sumMoney.add(amount);
                        payTotalMoney.put(currencyId, sumMoney);
                    }

                    payCount = payCount.add(BigDecimal.ONE);
                }
            }
        }

        QFilter receiveQf = new QFilter("draftbillstatus", "=", "endorsed");
        DynamicObject[] receivebills = BusinessDataServiceHelper.load("cdm_receivablebill", "id,currency,amount", new QFilter[]{orgFilter, receiveQf, begQf.and(endQf)});
        if (receivebills != null && receivebills.length > 0) {
            DynamicObject[] var23 = receivebills;
            int var25 = receivebills.length;

            for (int var27 = 0; var27 < var25; ++var27) {
                DynamicObject receivebill = var23[var27];
                DynamicObject currencyData = receivebill.getDynamicObject("currency");
                if (currencyData != null) {
                    Long currencyId = (Long) receivebill.getDynamicObject("currency").getPkValue();
                    BigDecimal amount = receivebill.getBigDecimal("amount");
                    BigDecimal sumMoney = (BigDecimal) recTotalMoney.get(currencyId);
                    if (sumMoney == null) {
                        recTotalMoney.put(currencyId, amount);
                    } else {
                        sumMoney = sumMoney.add(amount);
                        recTotalMoney.put(currencyId, sumMoney);
                    }

                    recCount = recCount.add(BigDecimal.ONE);
                }
            }
        }

        BigDecimal payTotal = this.diffCurrencyToMainCurrency(payTotalMoney, currency, this.cardName);
        BigDecimal recTotal = this.diffCurrencyToMainCurrency(recTotalMoney, currency, this.cardName);
        HashMap<String, BigDecimal> returnData = new HashMap();
        returnData.put("payCount", payCount);
        returnData.put("recCount", recCount);
        returnData.put("payTotal", payTotal);
        returnData.put("recTotal", recTotal);
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
        BigDecimal total = BigDecimal.ZERO;
        BigDecimal mainCurrencyMoney = BigDecimal.ZERO;
        QFilter bankaccount = OrgHelper.getQFilterOrg((DynamicObjectCollection) null, "mon_fundflow", "org");
        QFilter begindateQf = null;
        QFilter enddateQf = null;
        Object querycycle = this.getModel().getValue("tpv_querycycle");
        if (querycycle != null && "date".equals((String) querycycle)) {
            begindateQf = new QFilter("createtime", ">=", beginDate);
            enddateQf = new QFilter("createtime", "<", this.getCalendarEndTime());
        } else {
            begindateQf = new QFilter("bookdate", ">=", beginDate);
            enddateQf = new QFilter("bookdate", "<", this.getCalendarEndTime());
        }

        DynamicObject[] bankjournals = BusinessDataServiceHelper.load("cas_bankjournal", "id,accountbank,currency,debitamount,creditamount", new QFilter[]{bankaccount, begindateQf.and(enddateQf)});
        HashMap<Long, BigDecimal> currencyTotalMoney = new HashMap();
        HashMap<Long, BigDecimal> notMaincurrencyTotalMoney = new HashMap();
        HashMap<Long, BigDecimal> inTotalMoney = new HashMap();
        HashMap<Long, BigDecimal> outTotalMoney = new HashMap();
        BigDecimal inCount = BigDecimal.ZERO;
        BigDecimal outCount = BigDecimal.ZERO;
        DynamicObject[] var18 = bankjournals;
        int var19 = bankjournals.length;

        BigDecimal debitamount;
        BigDecimal creditamount;
        BigDecimal totalMoney;
        for (int var20 = 0; var20 < var19; ++var20) {
            DynamicObject bankjournal = var18[var20];
            DynamicObject currencyData = bankjournal.getDynamicObject("currency");
            if (currencyData != null) {
                Long currencyId = (Long) bankjournal.getDynamicObject("currency").getPkValue();
                debitamount = bankjournal.getBigDecimal("debitamount");
                creditamount = bankjournal.getBigDecimal("creditamount");
                totalMoney = (BigDecimal) currencyTotalMoney.get(currencyId);
                if (debitamount == null || debitamount.compareTo(BigDecimal.ZERO) < 0) {
                    debitamount = BigDecimal.ZERO;
                }

                if (creditamount == null || creditamount.compareTo(BigDecimal.ZERO) < 0) {
                    creditamount = BigDecimal.ZERO;
                }

                if (totalMoney == null) {
                    totalMoney = debitamount.add(creditamount);
                    currencyTotalMoney.put(currencyId, totalMoney);
                } else {
                    totalMoney = totalMoney.add(debitamount).add(creditamount);
                    currencyTotalMoney.put(currencyId, totalMoney);
                }

                BigDecimal sumAcctOutMoney;
                if (currencyId.compareTo(aimCurrency) == 0) {
                    mainCurrencyMoney = mainCurrencyMoney.add(debitamount.add(creditamount));
                } else {
                    sumAcctOutMoney = (BigDecimal) notMaincurrencyTotalMoney.get(currencyId);
                    if (sumAcctOutMoney == null) {
                        sumAcctOutMoney = debitamount.add(creditamount);
                        notMaincurrencyTotalMoney.put(currencyId, sumAcctOutMoney);
                    } else {
                        sumAcctOutMoney = sumAcctOutMoney.add(debitamount).add(creditamount);
                        notMaincurrencyTotalMoney.put(currencyId, sumAcctOutMoney);
                    }
                }

                if (debitamount.compareTo(BigDecimal.ZERO) > 0) {
                    sumAcctOutMoney = (BigDecimal) outTotalMoney.get(currencyId);
                    if (sumAcctOutMoney == null) {
                        outTotalMoney.put(currencyId, debitamount);
                    } else {
                        sumAcctOutMoney = sumAcctOutMoney.add(debitamount);
                        outTotalMoney.put(currencyId, sumAcctOutMoney);
                    }

                    outCount = outCount.add(BigDecimal.ONE);
                }

                if (creditamount.compareTo(BigDecimal.ZERO) > 0) {
                    sumAcctOutMoney = (BigDecimal) inTotalMoney.get(currencyId);
                    if (sumAcctOutMoney == null) {
                        inTotalMoney.put(currencyId, creditamount);
                    } else {
                        sumAcctOutMoney = sumAcctOutMoney.add(creditamount);
                        inTotalMoney.put(currencyId, sumAcctOutMoney);
                    }

                    inCount = inCount.add(BigDecimal.ONE);
                }
            }
        }

        ArrayList<Long> longRates = new ArrayList(currencyTotalMoney.size());
        Iterator var29 = currencyTotalMoney.entrySet().iterator();

        while (var29.hasNext()) {
            Map.Entry<Long, BigDecimal> entry = (Map.Entry) var29.next();
            longRates.add(entry.getKey());
        }

        boolean flagExist = BaseDataHelper.verifyExchangeRateCard(longRates, aimCurrency, TmcOrgDataHelper.getCurrentOrgId(), new Date(), this.cardName, this);
        if (!flagExist) {
            return null;
        } else {
            Iterator var32 = currencyTotalMoney.entrySet().iterator();

            BigDecimal inTotal;
            while (var32.hasNext()) {
                Map.Entry<Long, BigDecimal> entry = (Map.Entry) var32.next();
                Long id = (Long) entry.getKey();
                inTotal = BaseDataHelper.getExchangeRate(id, aimCurrency, TmcOrgDataHelper.getCurrentOrgId(), new Date());
                if (null != inTotal) {
                    debitamount = (BigDecimal) entry.getValue();
                    debitamount = debitamount.multiply(inTotal);
                    total = total.add(debitamount);
                }
            }

            BigDecimal maxMoney = null;
            Long currencyMaxId = null;
            Iterator var37 = notMaincurrencyTotalMoney.entrySet().iterator();

            while (var37.hasNext()) {
                Map.Entry<Long, BigDecimal> entry = (Map.Entry) var37.next();
                Long id = (Long) entry.getKey();
                creditamount = BaseDataHelper.getExchangeRate(id, aimCurrency, TmcOrgDataHelper.getCurrentOrgId(), new Date());
                if (null != creditamount) {
                    totalMoney = (BigDecimal) entry.getValue();
                    totalMoney = totalMoney.multiply(creditamount);
                    if (maxMoney == null) {
                        maxMoney = totalMoney;
                        currencyMaxId = id;
                    }

                    maxMoney = maxMoney.compareTo(totalMoney) > 0 ? maxMoney : totalMoney;
                    currencyMaxId = maxMoney.compareTo(totalMoney) > 0 ? currencyMaxId : id;
                }
            }

            BigDecimal maxCurrencyMoney = (BigDecimal) currencyTotalMoney.get(currencyMaxId);
            returnMap.put("currencyMaxId", currencyMaxId);
            returnMap.put("maxCurrencyMoney", maxCurrencyMoney);
            returnMap.put("sumMoney", total);
            returnMap.put("MainCurrencyMoney", mainCurrencyMoney);
            inTotal = this.diffCurrencyToMainCurrency(inTotalMoney, aimCurrency, this.cardName);
            debitamount = this.diffCurrencyToMainCurrency(outTotalMoney, aimCurrency, this.cardName);
            returnMap.put("incount", outCount);
            returnMap.put("outcount", inCount);
            returnMap.put("inTotal", debitamount);
            returnMap.put("outTotal", inTotal);
            return returnMap;
        }
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
//        super.click(evt);
        Control c = (Control) evt.getSource();
        String key = c.getKey().toLowerCase();
        if ("tpv_summoney".equals(key)) {
            this.showFormCapitalSumrpt();
        }

    }

    private static String[] colors = new String[]{"#098bff", "#00cccc", "#87db3b", "#ffd72b"};

    public void drawPiechart(Object mainCurrency, BigDecimal mainCurrencyMoney, Object notMainCurrencyId, BigDecimal notMainCurrency, BigDecimal total, String type, String cardName)
    {
        PieChart pieChart = (PieChart) this.getControl("tpv_piechartap");
        pieChart.clearData();
        pieChart.setShowTooltip(true);
        pieChart.setShowLegend(true);
        pieChart.setTitleAlign(XAlign.center, YAlign.center);
        List<ItemValue> itemList = new ArrayList(10);
        this.getView().setVisible(Boolean.TRUE, new String[]{"flexpanelap113", "flexpanelap112", "flexpanelap11"});
        if (mainCurrencyMoney == null) {
            mainCurrencyMoney = BigDecimal.ZERO;
        }

        String valueName = type.equals("account") ? String.format(ResManager.loadKDString("%s", "CommonMethodsPlugin_5", "CommonMethodsPlugin_6", new Object[0]), this.getCurrencyName(mainCurrency)) : ResManager.loadKDString(this.getCurrencyName(mainCurrency), "CommonMethodsPlugin_2", "CommonMethodsPlugin_6", new Object[0]);
        ItemValue otherItemMain = type.equals("account") ? new ItemValue(valueName, mainCurrencyMoney, colors[0]) : new ItemValue(valueName, mainCurrencyMoney.divide(new BigDecimal(10000), 2, 4), colors[0]);
        itemList.add(otherItemMain);
        Label mainrate = (Label) this.getView().getControl("tpv_mainrate");
        if (total != null && total.compareTo(BigDecimal.ZERO) != 0) {
            mainrate.setText(mainCurrencyMoney.multiply(BigDecimal.valueOf(100L)).divide(total, 2, 4) + "%");
        } else {
            total = BigDecimal.ZERO;
            mainrate.setText("0.00%");
        }

        Label label = (Label) this.getView().getControl("tpv_maincurrcymoney1");
        String moneyMain = type.equals("account") ? this.getNumberFormatAccount(mainCurrencyMoney) : this.getCurrencyName(mainCurrency, "sign") + this.getNumberFormat(mainCurrencyMoney.divide(new BigDecimal(10000), 2, 4));
        label.setText(moneyMain);
        Label notmainrate = (Label) this.getView().getControl("tpv_notmainrate");
        Label notmain = (Label) this.getView().getControl("tpv_nobussnessmoney");
        Label otherrate = (Label) this.getView().getControl("tpv_otherrate");
        Label otherLabel = (Label) this.getView().getControl("tpv_other");
        BigDecimal notMainToMain = BigDecimal.ZERO;
        if (total == null) {
            total = BigDecimal.ZERO;
        }

        if (notMainCurrencyId != null && BigDecimal.ZERO.compareTo(notMainCurrency) != 0) {
            HashMap<Long, BigDecimal> currencyHashMap = new HashMap();
            currencyHashMap.put((Long) notMainCurrencyId, notMainCurrency);
            if (!type.equals("account")) {
                notMainToMain = this.diffCurrencyToMainCurrency(currencyHashMap, (Long) mainCurrency, cardName);
            }

            if (notMainToMain == null) {
                return;
            }

            String value = this.getCurrencyName(notMainCurrencyId);
            ItemValue otherItem = type.equals("account") ? new ItemValue(value, notMainCurrency, colors[1]) : new ItemValue(value, notMainToMain.divide(new BigDecimal(10000), 2, 4), colors[1]);
            itemList.add(otherItem);
            if (total.compareTo(BigDecimal.ZERO) == 0) {
                notmainrate.setText("0.00%");
            } else if (type.equals("account")) {
                notmainrate.setText(notMainCurrency.multiply(BigDecimal.valueOf(100L)).divide(total, 2, 4) + "%");
            } else {
                notmainrate.setText(notMainToMain.multiply(BigDecimal.valueOf(100L)).divide(total, 2, 4) + "%");
            }

            String money = type.equals("account") ? this.getNumberFormatAccount(notMainCurrency) : this.getCurrencyName(notMainCurrencyId, "sign") + this.getNumberFormat(notMainCurrency.divide(new BigDecimal(10000), 2, 4));
            notmain.setText(money);
            BigDecimal other = BigDecimal.ZERO;
            if (type.equals("account")) {
                other = total.subtract(mainCurrencyMoney).subtract(notMainCurrency);
            } else {
                other = total.subtract(mainCurrencyMoney).subtract(notMainToMain);
            }

            if (BigDecimal.ZERO.compareTo(other) != 0) {
                ItemValue otherItem1 = type.equals("account") ? new ItemValue(ResManager.loadKDString("其他", "CommonMethodsPlugin_4", "tpv_app_deal_trace", new Object[0]), other, colors[2]) : new ItemValue(ResManager.loadKDString("其他(折算)", "CommonMethodsPlugin_1", "tpv_app_deal_trace", new Object[0]), other.divide(new BigDecimal(10000), 2, 4), colors[2]);
                itemList.add(otherItem1);
                if (total.compareTo(BigDecimal.ZERO) == 0) {
                    otherrate.setText("0.00%");
                } else {
                    otherrate.setText(other.multiply(BigDecimal.valueOf(100L)).divide(total, 2, 4) + "%");
                }

                String otherValue = type.equals("account") ? this.getNumberFormatAccount(other) : this.getCurrencyName(mainCurrency, "sign") + this.getNumberFormat(other.divide(new BigDecimal(10000), 2, 4));
                otherLabel.setText(otherValue);
            } else {
                this.getView().setVisible(Boolean.FALSE, new String[]{"flexpanelap113"});
            }
        } else {
            BigDecimal other = BigDecimal.ZERO;
            if (type.equals("account")) {
                other = total.subtract(mainCurrencyMoney).subtract(notMainCurrency);
            } else {
                other = total.subtract(mainCurrencyMoney).subtract(notMainToMain);
            }

            if (BigDecimal.ZERO.compareTo(other) != 0) {
                ItemValue otherItem = type.equals("account") ? new ItemValue(ResManager.loadKDString("其他", "CommonMethodsPlugin_4", "tpv_app_deal_trace", new Object[0]), other, colors[1]) : new ItemValue(ResManager.loadKDString("其他(折算)", "CommonMethodsPlugin_1", "tpv_app_deal_trace", new Object[0]), other.divide(new BigDecimal(10000), 2, 4), colors[1]);
                itemList.add(otherItem);
                if (total.compareTo(BigDecimal.ZERO) == 0) {
                    notmainrate.setText("0.00%");
                } else {
                    notmainrate.setText(other.multiply(BigDecimal.valueOf(100L)).divide(total, 2, 4) + "%");
                }

                String otherValue = type.equals("account") ? this.getNumberFormat(other) : this.getCurrencyName(mainCurrency, "sign") + this.getNumberFormat(other.divide(new BigDecimal(10000), 2, 4));
                notmain.setText(otherValue);
                this.getView().setVisible(Boolean.FALSE, new String[]{"flexpanelap113"});
            } else {
                this.getView().setVisible(Boolean.FALSE, new String[]{"flexpanelap113", "flexpanelap112", "flexpanelap11"});
            }
        }

        PieSeries series = pieChart.createPieSeries("piechartap");
        series.setPropValue("label", new Object[]{Boolean.FALSE, "center"});
        ItemValue[] items = new ItemValue[itemList.size()];
        itemList.toArray(items);
        series.setRadius("45%", "60%");
        series.setCenter("25%", "38%");
        series.setData(items);
        this.getView().setVisible(Boolean.TRUE, new String[]{"piechartap"});
        if (type.equals("account")) {
            pieChart.addTooltip("formatter", (new StringBuilder("{b0}: ")).append("{c0}(").append(ResManager.loadKDString("个", "CommonMethodsPlugin_2", "tpv_app_deal_trace", new Object[0])).append(")<br/>{d0}%"));
        } else {
            pieChart.addTooltip("formatter", (new StringBuilder("{b0}: ")).append("{c0}(").append(ResManager.loadKDString("万元", "CommonMethodsPlugin_3", "tpv_app_deal_trace", new Object[0])).append(")<br/>{d0}%"));
        }

        pieChart.setLegendPropValue("top", "20");
        pieChart.setLegendPropValue("orient", "vertical");
        pieChart.setLegendPropValue("left", 160);
        pieChart.setLegendPropValue("icon", "circle");
        pieChart.setLegendPropValue("itemGap", 30);
        pieChart.addTooltip("position", new String[]{"15%", "50%"});
        pieChart.refresh();
    }
}
