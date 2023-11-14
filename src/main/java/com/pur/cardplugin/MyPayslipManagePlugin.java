package com.pur.cardplugin;

import com.pur.model.ChartDataModel;
import com.pur.model.StatisticalChartModel;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.entity.datamodel.events.PropertyChangedArgs;
import kd.bos.form.chart.*;
import kd.bos.form.control.Html;
import kd.swc.hspp.formplugin.web.view.PayslipManagePlugin;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class MyPayslipManagePlugin extends PayslipManagePlugin
{
    @Override
    public void beforeBindData(EventObject e)
    {
        // 获取当前日期
        LocalDate endDate = LocalDate.now();

        // 获取30天之前的日期
        LocalDate startDate = endDate.minusDays(30);
        Date end = Date.from(endDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        Date start = Date.from(startDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        dateChange(start, end);
    }

    @Override
    public void propertyChanged(PropertyChangedArgs e)
    {
        Date startDate;
        Date endDate;
        final String startId = "tpv_daterange_startdate";
        final String endId = "tpv_daterange_enddate";
        String propName = e.getProperty().getName();

        if (startId.equals(propName)) {
            startDate = (Date) e.getChangeSet()[0].getNewValue();
            endDate = (Date) this.getView().getModel().getValue(endId);
            if (startDate != null && endDate != null) {
                dateChange(startDate, endDate);
            }
        }

        if (endId.equals(propName)) {
            startDate = (Date) this.getView().getModel().getValue(startId);
            endDate = (Date) e.getChangeSet()[0].getNewValue();
            if (endDate != null) {
                dateChange(startDate, endDate);
            }
        }
    }

    private void dateChange(Date startDate, Date endDate)
    {
        // Search Data by Date
        StatisticalChartModel chartModel = new StatisticalChartModel();
        chartModel.setChartId("tpv_customchartap");
        chartModel.setXDataType(AxisType.category);
        String[] xItems = {"1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月(月份)"};
        chartModel.setXItems(xItems);
        chartModel.setYDataType(AxisType.value);
        chartModel.setXFieldName("X轴");
        chartModel.setYFieldName("Y轴");
        ChartDataModel data1 = new ChartDataModel();
        data1.setName("数据1");
        data1.setType(ChartType.line);
        data1.setColor("#FF9400");
        data1.setData(Arrays.asList(
                new ItemValue(null, 34),
                new ItemValue(null, 13),
                new ItemValue(null, 75),
                new ItemValue(null, 32),
                new ItemValue(null, 87),
                new ItemValue(null, 34),
                new ItemValue(null, 65),
                new ItemValue(null, 34),
                new ItemValue(null, 13),
                new ItemValue(null, 75),
                new ItemValue(null, 32),
                new ItemValue(null, 87)
        ));
        ChartDataModel data2 = new ChartDataModel();
        data2.setName("数据2");
        data2.setType(ChartType.line);
        data2.setColor("#655DD4");
        data2.setData(Arrays.asList(
                new ItemValue(null, 23),
                new ItemValue(null, 23),
                new ItemValue(null, 56),
                new ItemValue(null, 34),
                new ItemValue(null, 32),
                new ItemValue(null, 75),
                new ItemValue(null, 13),
                new ItemValue(null, 87),
                new ItemValue(null, 34),
                new ItemValue(null, 87),
                new ItemValue(null, 65),
                new ItemValue(null, 32)
        ));
        chartModel.setSeriesData(Arrays.asList(data1, data2));
        renderData(chartModel);
    }

    private void renderData(StatisticalChartModel chartModel)
    {
        this.initStatisticalChart(chartModel);
        this.initLapDes();
        this.initPieChart();
        this.initPieChart1();
        this.initPieChart2();
    }

    private void initStatisticalChart(StatisticalChartModel chartModel)
    {
        Chart chart = this.getControl(chartModel.getChartId());
        if (chart == null) {
            return;
        }
        Axis xAxis = chart.createXAxis(ResManager.loadKDString(chartModel.getXFieldName(), "PayslipManagePlugin_0", "swc-hspp-formplugin", new Object[0]), chartModel.getXDataType());
        if (AxisType.category.equals(chartModel.getXDataType())) {
            xAxis.setCategorys(chartModel.getXItems());
        }
        Axis yAxis = chart.createYAxis(ResManager.loadKDString(chartModel.getYFieldName(), "PayslipManagePlugin_0", "swc-hspp-formplugin", new Object[0]), chartModel.getYDataType());
        if (AxisType.category.equals(chartModel.getYDataType())) {
            yAxis.setCategorys(chartModel.getYItems());
        }
        List<ChartDataModel> seriesData = chartModel.getSeriesData();
        if (seriesData == null || seriesData.isEmpty()) {
            return;
        }
        for (ChartDataModel seriesDatum : seriesData) {
            Series series = chart.createBarSeries(seriesDatum.getName());
            series.setType(seriesDatum.getType());
            series.setItemColor(seriesDatum.getColor());
            List<ItemValue> data = seriesDatum.getData();
            series.setData(data.toArray(new ItemValue[0]));
        }
    }

    private void initCustomCharTap()
    {
        // 统计图
        Chart chart = this.getControl("tpv_customchartap");
        // 设置X轴属性，AxisType.category 离散的类目数据
        Axis xAxis = chart.createXAxis(ResManager.loadKDString("X轴", "PayslipManagePlugin_0", "swc-hspp-formplugin", new Object[0]), AxisType.category);
        // 设置X轴数据
        xAxis.setCategorys(new String[]{"2011", "2012", "2013", "2014", "2015", "2016", ResManager.loadKDString("2017(年份)", "PayslipManagePlugin_13", "swc-hspp-formplugin", new Object[0])});
        // 设置Y轴属性，AxisType.value 连续的数值数据
        chart.createYAxis(ResManager.loadKDString("人数", "PayslipManagePlugin_1", "swc-hspp-formplugin", new Object[0]), AxisType.value);

        // 创建条形图
        Series series = chart.createBarSeries(ResManager.loadKDString("已发布工资条数量", "PayslipManagePlugin_2", "swc-hspp-formplugin", new Object[0]));
        // 使用折线图
        series.setType(ChartType.line);
        // 设置条形图颜色
        series.setItemColor("#FF9400");
        // 设置数据
        series.addData(new ItemValue(34, null));
        series.addData(new ItemValue(13, null));
        series.addData(new ItemValue(75, null));
        series.addData(new ItemValue(32, null));
        series.addData(new ItemValue(87, null));
        series.addData(new ItemValue(34, null));
        series.addData(new ItemValue(65, null));

        // 创建条形图
        Series series1 = chart.createBarSeries(ResManager.loadKDString("已查看工资条数量", "PayslipManagePlugin_3", "swc-hspp-formplugin", new Object[0]));
        series1.setType(ChartType.line);
        series1.setItemColor("#655DD4");
        series1.addData(new ItemValue(23, null));
        series1.addData(new ItemValue(56, null));
        series1.addData(new ItemValue(23, null));
        series1.addData(new ItemValue(78, null));
        series1.addData(new ItemValue(34, null));
        series1.addData(new ItemValue(12, null));
        series1.addData(new ItemValue(54, null));
        // 设置统计图位置
        chart.setLegendAlign(XAlign.right, YAlign.top);
    }

    private void initLapDes()
    {
        Html content = (Html) this.getControl("tpv_htmlap");
        String contentStr = MessageFormat.format(
                ResManager.loadKDString(
                        "{0}该期间内，您总共发布了{1}条工资条，产生了{2}条问询反馈，{3}名员工操作了确认。{4}其中，您提醒了{5}名员工操作查询，催办了{6}名员工的问询反馈。{7}"
                        , "PayslipManagePlugin_14"
                        , "swc-hspp-formplugin"
                        , new Object[0])
                , "<div style='overflow:auto;word-wrap:break-word;word-break:break-all;width:100%'>"
                , "<span style='font-size:18px;color:#308AF0'>18.2w</span>"
                , "<span style='font-size:18px;color:#308AF0'>59</span>"
                , "<span style='font-size:18px;color:#FF9400'>1028</span>"
                , "<div style='height:5px'></div>"
                , "<span style='font-size:18px;color:#18BC71'>128</span>"
                , "<span style='font-size:18px;color:#FF736C'>28</span>"
                , "</div>");
        content.setConent(contentStr);
    }

    private void initPieChart()
    {
        PieChart pieChart = (PieChart) this.getControl("tpv_piechartap_query");
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
        pieSeries.setPropValue("hoverAnimation", Boolean.TRUE);
        Label label = new Label();
        label.setShow(false);
        label.setColor("#5f00bf");
        pieSeries.setLabel(label);
        pieSeries.addData(new ItemValue("data", 90, "#18BC71"));
        pieSeries.addData(new ItemValue("empty", 10, "#EEEEEE"));
        kd.bos.form.control.Label labelap11 = (kd.bos.form.control.Label) this.getControl("tpv_labelap11");
        labelap11.setText("■");
        kd.bos.form.control.Label queryrate = (kd.bos.form.control.Label) this.getControl("tpv_queryrate");
        queryrate.setText(ResManager.loadKDString("查询率", "PayslipManagePlugin_10", "swc-hspp-formplugin", new Object[0]));
    }

    private void initPieChart1()
    {
        PieChart pieChart = (PieChart) this.getControl("tpv_piechartap_confirm");
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
        kd.bos.form.control.Label labelap12 = (kd.bos.form.control.Label) this.getControl("tpv_labelap12");
        labelap12.setText("■");
        kd.bos.form.control.Label confirmrate = (kd.bos.form.control.Label) this.getControl("tpv_confirmrate");
        confirmrate.setText(ResManager.loadKDString("确认率", "PayslipManagePlugin_11", "swc-hspp-formplugin", new Object[0]));
    }

    private void initPieChart2()
    {
        PieChart pieChart = (PieChart) this.getControl("tpv_piechartap_response");
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
        kd.bos.form.control.Label labelap13 = (kd.bos.form.control.Label) this.getControl("tpv_labelap13");
        labelap13.setText("■");
        kd.bos.form.control.Label responserate = (kd.bos.form.control.Label) this.getControl("tpv_responserate");
        responserate.setText(ResManager.loadKDString("答复率", "PayslipManagePlugin_12", "swc-hspp-formplugin", new Object[0]));
    }
}
