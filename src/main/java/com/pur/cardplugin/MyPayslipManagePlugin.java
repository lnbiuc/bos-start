package com.pur.cardplugin;

import kd.bos.dataentity.resource.ResManager;
import kd.bos.form.chart.*;
import kd.bos.form.control.Html;
import kd.swc.hspp.formplugin.web.view.PayslipManagePlugin;

import java.text.MessageFormat;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Map;

public class MyPayslipManagePlugin extends PayslipManagePlugin
{
    @Override
    public void beforeBindData(EventObject e)
    {
        super.beforeBindData(e);
        this.initCustomCharTap();
        this.initLapDes();
        this.initPieChart();
        this.initPieChart1();
        this.initPieChart2();
    }

    private void initCustomCharTap()
    {
        Chart chart = (Chart) this.getControl("customchartap");
        Axis xAxis = chart.createXAxis(ResManager.loadKDString("X轴", "PayslipManagePlugin_0", "swc-hspp-formplugin", new Object[0]), AxisType.category);
        Map<String, Boolean> xAxisTick = new HashMap<>();
        xAxisTick.put("show", Boolean.FALSE);
        xAxis.setPropValue("axisTick", xAxisTick);
        xAxis.setCategorys(new String[]{"2011", "2012", "2013", "2014", "2015", "2016", ResManager.loadKDString("2017(年份)", "PayslipManagePlugin_13", "swc-hspp-formplugin", new Object[0])});
        Axis yAxis = chart.createYAxis(ResManager.loadKDString("人数", "PayslipManagePlugin_1", "swc-hspp-formplugin", new Object[0]), AxisType.value);
        Map<String, Boolean> yAxisLine = new HashMap<>();
        yAxisLine.put("show", Boolean.FALSE);
        Map<String, Boolean> yAxisTick = new HashMap<>();
        yAxisTick.put("show", Boolean.FALSE);
        yAxis.setPropValue("axisLine", yAxisLine);
        yAxis.setPropValue("axisTick", yAxisTick);
        Series series = chart.createBarSeries(ResManager.loadKDString("已发布工资条数量", "PayslipManagePlugin_2", "swc-hspp-formplugin", new Object[0]));
        series.setType(ChartType.line);
        series.setItemColor("#FF9400");
        series.addData(new ItemValue(20, (String) null));
        series.addData(new ItemValue(20, (String) null));
        series.addData(new ItemValue(20, (String) null));
        series.addData(new ItemValue(20, (String) null));
        series.addData(new ItemValue(30, (String) null));
        series.addData(new ItemValue(40, (String) null));
        series.addData(new ItemValue(50, (String) null));
        Series series1 = chart.createBarSeries(ResManager.loadKDString("已查看工资条数量", "PayslipManagePlugin_3", "swc-hspp-formplugin", new Object[0]));
        series1.setType(ChartType.line);
        series1.setItemColor("#655DD4");
        series1.addData(new ItemValue(10, (String) null));
        series1.addData(new ItemValue(10, (String) null));
        series1.addData(new ItemValue(10, (String) null));
        series1.addData(new ItemValue(10, (String) null));
        series1.addData(new ItemValue(20, (String) null));
        series1.addData(new ItemValue(30, (String) null));
        series1.addData(new ItemValue(40, (String) null));
        chart.setLegendAlign(XAlign.right, YAlign.top);
    }

    private void initLapDes()
    {
        Html content = (Html) this.getControl("htmlap");
        content.setConent(MessageFormat.format(ResManager.loadKDString("{0}该期间内，您总共发布了{1}条工资条，产生了{2}条问询反馈，{3}名员工操作了确认。{4}其中，您提醒了{5}名员工操作查询，催办了{6}名员工的问询反馈。{7}", "PayslipManagePlugin_14", "swc-hspp-formplugin", new Object[0]), "<div style='overflow:auto;word-wrap:break-word;word-break:break-all;width:100%'>", "<span style='font-size:18px;color:#308AF0'>18.2w</span>", "<span style='font-size:18px;color:#308AF0'>59</span>", "<span style='font-size:18px;color:#FF9400'>1028</span>", "<div style='height:5px'></div>", "<span style='font-size:18px;color:#18BC71'>128</span>", "<span style='font-size:18px;color:#FF736C'>28</span>", "</div>"));
    }

    private void initPieChart()
    {
        PieChart pieChart = (PieChart) this.getControl("piechartap_query");
        pieChart.setShowTitle(true);
        pieChart.setTitlePropValue("text", "90%");
        pieChart.setTitleAlign(XAlign.center, YAlign.center);
        Map<String, Object> textStyle = new HashMap<>();
        textStyle.put("fontSize", "20");
        pieChart.setTitlePropValue("textStyle", textStyle);
        Map<String, Object> subTextStyle = new HashMap<>();
        subTextStyle.put("fontSize", "10");
        subTextStyle.put("color", "#999999");
        pieChart.setTitlePropValue("subtextStyle", subTextStyle);
        PieSeries pieSeries = pieChart.createPieSeries("");
        pieSeries.setRadius("60%", "70%");
        pieSeries.setPropValue("hoverAnimation", Boolean.FALSE);
        Label label = new Label();
        label.setShow(false);
        label.setColor("#5f00bf");
        pieSeries.setLabel(label);
        pieSeries.addData(new ItemValue("data", 90, "#18BC71"));
        pieSeries.addData(new ItemValue("empty", 10, "#EEEEEE"));
        kd.bos.form.control.Label labelap11 = (kd.bos.form.control.Label) this.getControl("labelap11");
        labelap11.setText("■");
        kd.bos.form.control.Label queryrate = (kd.bos.form.control.Label) this.getControl("queryrate");
        queryrate.setText(ResManager.loadKDString("查询率", "PayslipManagePlugin_10", "swc-hspp-formplugin", new Object[0]));
    }

    private void initPieChart1()
    {
        PieChart pieChart = (PieChart) this.getControl("piechartap_confirm");
        pieChart.setShowTitle(true);
        pieChart.setTitlePropValue("text", "80%");
        pieChart.setTitleAlign(XAlign.center, YAlign.center);
        Map<String, Object> textStyle = new HashMap<>();
        textStyle.put("fontSize", "20");
        pieChart.setTitlePropValue("textStyle", textStyle);
        PieSeries pieSeries = pieChart.createPieSeries("");
        pieSeries.setRadius("60%", "70%");
        pieSeries.setPropValue("hoverAnimation", Boolean.FALSE);
        Label label = new Label();
        label.setShow(false);
        pieSeries.setLabel(label);
        pieSeries.addData(new ItemValue("data", 80, "#308AF0"));
        pieSeries.addData(new ItemValue("empty", 20, "#EEEEEE"));
        kd.bos.form.control.Label labelap12 = (kd.bos.form.control.Label) this.getControl("labelap12");
        labelap12.setText("■");
        kd.bos.form.control.Label confirmrate = (kd.bos.form.control.Label) this.getControl("confirmrate");
        confirmrate.setText(ResManager.loadKDString("确认率", "PayslipManagePlugin_11", "swc-hspp-formplugin", new Object[0]));
    }

    private void initPieChart2()
    {
        PieChart pieChart = (PieChart) this.getControl("piechartap_response");
        pieChart.setShowTitle(true);
        pieChart.setTitlePropValue("text", "70%");
        pieChart.setTitleAlign(XAlign.center, YAlign.center);
        Map<String, Object> textStyle = new HashMap<>();
        textStyle.put("fontSize", "20");
        pieChart.setTitlePropValue("textStyle", textStyle);
        PieSeries pieSeries = pieChart.createPieSeries("");
        pieSeries.setRadius("60%", "70%");
        pieSeries.setPropValue("hoverAnimation", Boolean.FALSE);
        Label label = new Label();
        label.setShow(false);
        pieSeries.setLabel(label);
        pieSeries.addData(new ItemValue("data", 70, "#FF736C"));
        pieSeries.addData(new ItemValue("empty", 30, "#EEEEEE"));
        kd.bos.form.control.Label labelap13 = (kd.bos.form.control.Label) this.getControl("labelap13");
        labelap13.setText("■");
        kd.bos.form.control.Label responserate = (kd.bos.form.control.Label) this.getControl("responserate");
        responserate.setText(ResManager.loadKDString("答复率", "PayslipManagePlugin_12", "swc-hspp-formplugin", new Object[0]));
    }
}
