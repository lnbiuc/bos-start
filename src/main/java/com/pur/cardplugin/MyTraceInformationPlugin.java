package com.pur.cardplugin;

import kd.bos.dataentity.resource.ResManager;
import kd.bos.entity.datamodel.events.ChangeData;
import kd.bos.entity.datamodel.events.PropertyChangedArgs;
import kd.bos.form.chart.*;
import kd.bos.form.control.Label;
import kd.bos.form.control.events.ClickListener;
import kd.tmc.mon.formplugin.index.TraceInformationPlugin;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

/**
 * 交易信息
 */
public class MyTraceInformationPlugin extends TraceInformationPlugin implements ClickListener
{
    private static final String[] colors = new String[]{"#098bff", "#00cccc", "#87db3b", "#ffd72b"};

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
            this.renderData(newQueryCycle);
        }

    }

    @Override
    public void afterCreateNewData(EventObject e)
    {
        this.renderData("month");
    }

    private void renderData(String queryCycle)
    {
        // 交易信息
        Label summoney = this.getView().getControl("tpv_summoney");
        summoney.setText("￥1600万元");
        Label intotal = this.getView().getControl("tpv_intotal");
        intotal.setText("收入资金(笔)：10");
        Label outtotal = this.getView().getControl("tpv_outtotal");
        outtotal.setText("支出资金(笔)：20");
        Label inticket = this.getView().getControl("tpv_inticket");
        inticket.setText("收入票据(张)：30");
        Label outticket = this.getView().getControl("tpv_outticket");
        outticket.setText("支出票据(张)：40");
        Label intotalsum = this.getView().getControl("tpv_intotalsum");
        intotalsum.setText("1001.00万元");
        Label outtotalsum = this.getView().getControl("tpv_outtotalsum");
        outtotalsum.setText("1002.00万元");
        Label inticketsum = this.getView().getControl("tpv_inticketsum");
        inticketsum.setText("1003.00万元");
        Label outticketsum = this.getView().getControl("tpv_outticketsum");
        outticketsum.setText("1004.00万元");
        // 饼图
        PieChart pieChart = this.getControl("tpv_piechartap");
        pieChart.clearData();
        pieChart.setShowTooltip(true);
        pieChart.setShowLegend(true);
        pieChart.setTitleAlign(XAlign.center, YAlign.center);
        PieSeries series = pieChart.createPieSeries("piechartap");
        series.setPropValue("label", new Object[]{Boolean.FALSE, "center"});
        List<ItemValue> itemList = new ArrayList<>();
        switch (queryCycle) {
            case "month":
                itemList.add(new ItemValue("D", 50, colors[0]));
                itemList.add(new ItemValue("A", 10, colors[1]));
                itemList.add(new ItemValue("B", 10, colors[2]));
                itemList.add(new ItemValue("C", 30, colors[3]));
                break;
            case "day":
                itemList.add(new ItemValue("一", 50, colors[0]));
                itemList.add(new ItemValue("二", 10, colors[1]));
                itemList.add(new ItemValue("三", 10, colors[2]));
                break;
        }
        ItemValue[] items = new ItemValue[itemList.size()];
        itemList.toArray(items);
        series.setRadius("45%", "60%");
        series.setCenter("25%", "38%");
        series.setData(items);
        pieChart.addTooltip("formatter", (new StringBuilder("{b0}: ")).append("{c0}(").append(ResManager.loadKDString("个", "CommonMethodsPlugin_2", "tpv_app_deal_trace", new Object[0])).append(")<br/>{d0}%"));
        pieChart.setLegendPropValue("top", "20");
        pieChart.setLegendPropValue("orient", "vertical");
        pieChart.setLegendPropValue("left", 160);
        pieChart.setLegendPropValue("icon", "circle");
        pieChart.setLegendPropValue("itemGap", 30);
        pieChart.addTooltip("position", new String[]{"15%", "50%"});
        pieChart.refresh();
    }
}
