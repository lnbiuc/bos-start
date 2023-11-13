package com.pur.cardplugin;

import com.pur.model.TraceInfoModel;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.entity.datamodel.events.ChangeData;
import kd.bos.entity.datamodel.events.PropertyChangedArgs;
import kd.bos.form.chart.*;
import kd.bos.form.control.Label;
import kd.bos.form.control.events.ClickListener;
import kd.bos.orm.query.QCP;
import kd.bos.orm.query.QFilter;
import kd.bos.servicehelper.BusinessDataServiceHelper;
import kd.tmc.fbp.common.helper.TmcOrgDataHelper;
import kd.tmc.mon.common.helper.OrgHelper;
import kd.tmc.mon.formplugin.index.TraceInformationPlugin;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

/**
 * 交易信息
 */
public class MyTraceInformationPlugin extends TraceInformationPlugin implements ClickListener
{
    private static final String[] colors = new String[]{"#098bff", "#00cccc", "#87db3b", "#ffd72b"};
    private static final String cardId = "tpv_app_deal_trace";

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
        String newQueryCycle = (String) valueSet[0].getNewValue();
        if (propName.equals("tpv_querycycle")) {
            this.queryCycleChange(newQueryCycle);
        }

    }

    @Override
    public void afterCreateNewData(EventObject e)
    {
        this.queryCycleChange("month");
    }

    private void queryCycleChange(String queryCycle)
    {
        Long currencyId = OrgHelper.getMainCurrency(TmcOrgDataHelper.getCurrentOrgId());
        DynamicObject[] currency2 = BusinessDataServiceHelper.load("bd_currency", "id", new QFilter[]{new QFilter("number", QCP.equals, "HKD")});
        Long currencyId2 = null;
        for (DynamicObject object : currency2) {
            currencyId2 = object.getLong("id");
        }
        DynamicObject[] currency3 = BusinessDataServiceHelper.load("bd_currency", "id", new QFilter[]{new QFilter("number", QCP.equals, "JPY")});
        Long currencyId3 = null;
        for (DynamicObject object : currency3) {
            currencyId3 = object.getLong("id");
        }

        // Month
        TraceInfoModel monthModel = new TraceInfoModel();
        monthModel.setMainCurrencyId(currencyId);
        monthModel.setSumMoney(new BigDecimal("160540000.65"));
        monthModel.setInTotal(10);
        monthModel.setOutTotal(20);
        monthModel.setInTicket(30);
        monthModel.setOutTicket(40);
        monthModel.setInTotalSum(new BigDecimal("473243.23"));
        monthModel.setOutTotalSum(new BigDecimal("3453424345.23"));
        monthModel.setInTicketSum(new BigDecimal("648734565.23"));
        monthModel.setOutTicketSum(new BigDecimal("3245452346.23"));
        List<ItemValue> itemList = new ArrayList<>();
        itemList.add(new ItemValue(currencyId.toString(), 2133.123, colors[0]));
        if (currencyId2 != null) {
            itemList.add(new ItemValue(currencyId2.toString(), 11235.34, colors[1]));
        }
        if (currencyId3 != null) {
            itemList.add(new ItemValue(currencyId3.toString(), 3432.34, colors[2]));
        }
        monthModel.setPieData(itemList);
        // Day
        TraceInfoModel datModel = new TraceInfoModel();
        switch (queryCycle) {
            case "month":
                renderData(monthModel);
                break;
            case "day":
                monthModel.setSumMoney(new BigDecimal("143554300.65"));
                monthModel.setInTotal(10);
                monthModel.setOutTotal(20);
                monthModel.setInTicket(30);
                monthModel.setOutTicket(40);
                monthModel.setInTotalSum(new BigDecimal("234654.23"));
                monthModel.setOutTotalSum(new BigDecimal("234543453.23"));
                monthModel.setInTicketSum(new BigDecimal("423344565.23"));
                monthModel.setOutTicketSum(new BigDecimal("1356452346.23"));
                List<ItemValue> itemListMonth = new ArrayList<>();
                if (currencyId3 != null) {
                    itemListMonth.add(new ItemValue(currencyId3.toString(), 7653.32, colors[1]));
                }
                if (currencyId2 != null) {
                    itemListMonth.add(new ItemValue(currencyId2.toString(), 5235.34, colors[1]));
                }
                itemListMonth.add(new ItemValue(currencyId.toString(), 9032.34, colors[2]));
                monthModel.setPieData(itemListMonth);
                renderData(monthModel);
                break;
        }
    }

    private void renderData(TraceInfoModel monthModel)
    {
        // 交易信息
        Label summoneyLabel = this.getView().getControl("tpv_summoney");
        Label intotalLabel = this.getView().getControl("tpv_intotal");
        Label outtotalLabel = this.getView().getControl("tpv_outtotal");
        Label inticketLabel = this.getView().getControl("tpv_inticket");
        Label outticketLabel = this.getView().getControl("tpv_outticket");
        Label intotalsumLabel = this.getView().getControl("tpv_intotalsum");
        Label outtotalsumLabel = this.getView().getControl("tpv_outtotalsum");
        Label inticketsumLabel = this.getView().getControl("tpv_inticketsum");
        Label outticketsumLabel = this.getView().getControl("tpv_outticketsum");
        Long currencyId = monthModel.getMainCurrencyId();
        // 设置总金额
        BigDecimal sumMoneyUnit = monthModel.getSumMoney();
        BigDecimal sumMoney = null;
        if (null != sumMoneyUnit && sumMoneyUnit.compareTo(BigDecimal.ZERO) != 0) {
            sumMoney = sumMoneyUnit.divide(new BigDecimal(10000), 2, RoundingMode.HALF_UP);
        }

        if (sumMoney == null) {
            sumMoney = BigDecimal.ZERO;
        }
        summoneyLabel.setText(this.getCurrencyName(currencyId, "sign") + this.getNumberFormat(sumMoney));

        String unit = ResManager.loadKDString("张", "TraceInformationPlugin_2", cardId, new Object[0]);
        String unitCapital = ResManager.loadKDString("笔", "TraceInformationPlugin_3", cardId, new Object[0]);

        // 收入资金
        Integer inTotal = monthModel.getInTotal();
        if (inTotal != null && inTotal.compareTo(0) != 0) {
            intotalLabel.setText(ResManager.loadKDString("收入资金", "TraceInformationPlugin_6", cardId, new Object[0]) + "(" + inTotal + unitCapital + ")");
            intotalsumLabel.setText(this.getCurrencyName(currencyId, "sign") + this.getNumberFormat(monthModel.getInTotalSum().divide(new BigDecimal(10000), 2, RoundingMode.HALF_UP)));
        }

        // 支出资金
        Integer outTotal = monthModel.getOutTotal();
        if (outTotal != null && outTotal.compareTo(0) != 0) {
            outtotalLabel.setText(ResManager.loadKDString("支出资金", "TraceInformationPlugin_5", cardId, new Object[0]) + "(" + outTotal + unitCapital + ")");
            outtotalsumLabel.setText(this.getCurrencyName(currencyId, new String[]{"sign"}) + this.getNumberFormat(monthModel.getOutTotalSum().divide(new BigDecimal(10000), 2, RoundingMode.HALF_UP)));
        }

        // 收入票据
        Integer inTicket = monthModel.getInTicket();
        if (inTicket != null && inTicket.compareTo(0) != 0) {
            inticketLabel.setText(ResManager.loadKDString("收入票据", "TraceInformationPlugin_6", cardId, new Object[0]) + "(" + inTicket + unit + ")");
            inticketsumLabel.setText(this.getCurrencyName(currencyId, new String[]{"sign"}) + this.getNumberFormat(monthModel.getInTicketSum().divide(new BigDecimal(10000), 2, RoundingMode.HALF_UP)));
        }

        // 支出票据
        Integer outTicket = monthModel.getOutTicket();
        if (outTicket != null && outTicket.compareTo(0) != 0) {
            outticketLabel.setText(ResManager.loadKDString("支出票据", "TraceInformationPlugin_7", cardId, new Object[0]) + "(" + outTicket + unit + ")");
            outticketsumLabel.setText(this.getCurrencyName(currencyId, new String[]{"sign"}) + this.getNumberFormat(monthModel.getOutTicketSum().divide(new BigDecimal(10000), 2, RoundingMode.HALF_UP)));
        }
        renderPieChart(monthModel);
    }

    private void renderPieChart(TraceInfoModel monthModel)
    {
        List<ItemValue> itemList = monthModel.getPieData();
        PieChart pieChart = this.getControl("tpv_piechartap");
        pieChart.clearData();
        pieChart.setShowTooltip(true);
        pieChart.setShowLegend(true);
        pieChart.setTitleAlign(XAlign.center, YAlign.center);
        PieSeries series = pieChart.createPieSeries("piechartap");
        series.setPropValue("label", new Object[]{Boolean.FALSE, "center"});
        series.setRadius("45%", "60%");
        series.setCenter("25%", "38%");
        ItemValue[] items = new ItemValue[itemList.size()];
        setPieChartValue(itemList);
        series.setData(itemList.toArray(items));
        pieChart.addTooltip("formatter", (new StringBuilder("{b0}: ")).append("{c0}(").append(ResManager.loadKDString("个", "CommonMethodsPlugin_2", "tpv_app_deal_trace", new Object[0])).append(")<br/>{d0}%"));
        pieChart.setLegendPropValue("top", "20");
        pieChart.setLegendPropValue("orient", "vertical");
        pieChart.setLegendPropValue("left", 160);
        pieChart.setLegendPropValue("icon", "circle");
        pieChart.setLegendPropValue("itemGap", 30);
        pieChart.addTooltip("position", new String[]{"15%", "50%"});
        pieChart.refresh();
    }

    private void setPieChartValue(List<ItemValue> itemList)
    {
        Label mainrate = this.getView().getControl("tpv_mainrate");
        Label maincurrcymoney = this.getView().getControl("tpv_maincurrcymoney1");
        Label notmainrate = this.getView().getControl("tpv_notmainrate");
        Label notmain = this.getView().getControl("tpv_nobussnessmoney");
        Label otherrate = this.getView().getControl("tpv_otherrate");
        Label other = this.getView().getControl("tpv_other");
        // 计算总和
        double total = itemList.stream().mapToDouble(item -> ((Number) item.getValue()).doubleValue()).sum();
        // 只有在 itemList 大于 2 时才进行计算和合并
        if (itemList.size() > 2) {
            // 计算百分比，并按百分比大小重新排列 itemList
            itemList.sort((item1, item2) -> Double.compare(((Number) item2.getValue()).doubleValue() / total, ((Number) item1.getValue()).doubleValue() / total));
            // 合并其他项
            double otherValue = 0.0;
            for (int i = 2; i < itemList.size(); i++) {
                otherValue += ((Number) itemList.get(i).getValue()).doubleValue();
            }
            ItemValue otherItem = new ItemValue("其他(折算)", otherValue, colors[3]);
            // 移除其他项之后的项
            itemList.subList(2, itemList.size()).clear();
            itemList.add(otherItem);

            mainrate.setText(String.format("%.2f", ((Number) itemList.get(0).getValue()).doubleValue() / total * 100) + "%");
            maincurrcymoney.setText(this.getCurrencyName(Long.valueOf(itemList.get(0).getName()), "sign") + itemList.get(0).getValue());

            notmainrate.setText(String.format("%.2f", ((Number) itemList.get(1).getValue()).doubleValue() / total * 100) + "%");
            notmain.setText(this.getCurrencyName(Long.valueOf(itemList.get(1).getName()), "sign") + itemList.get(1).getValue());

            otherrate.setText(String.format("%.2f", ((Number) itemList.get(2).getValue()).doubleValue() / total * 100) + "%");
            other.setText(this.getCurrencyName(Long.valueOf(itemList.get(0).getName()), "sign") + itemList.get(0).getValue());
        } else {
            if (itemList.size() == 2) {
                mainrate.setText(String.format("%.2f", ((Number) itemList.get(0).getValue()).doubleValue() / total * 100) + "%");
                maincurrcymoney.setText(this.getCurrencyName(Long.valueOf(itemList.get(0).getName()), "sign") + itemList.get(0).getValue());
                notmainrate.setText(String.format("%.2f", ((Number) itemList.get(1).getValue()).doubleValue() / total * 100) + "%");
                notmain.setText(this.getCurrencyName(Long.valueOf(itemList.get(1).getName()), "sign") + itemList.get(1).getValue());
            } else if (itemList.size() == 1) {
                mainrate.setText("100%");
                maincurrcymoney.setText(this.getCurrencyName(Long.valueOf(itemList.get(0).getName()), "sign") + itemList.get(0).getValue());
            }
        }
        for (ItemValue itemValue : itemList) {
            if (!"其他(折算)".equals(itemValue.getName())) {
                String currencyName = this.getCurrencyName(Long.valueOf(itemValue.getName()), "name");
                itemValue.setName(currencyName);
            }
        }
    }
}