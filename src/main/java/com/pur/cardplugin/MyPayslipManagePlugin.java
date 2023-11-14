package com.pur.cardplugin;

import com.pur.model.ChartDataModel;
import com.pur.model.PieChartModel;
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
        // 根据日期查询数据
        // 设置统计图数据
        StatisticalChartModel chartModel = new StatisticalChartModel();
        // 设置柱状图标识
        chartModel.setChartId("tpv_customchartap");
        // 设置X轴数据类型
        chartModel.setXDataType(AxisType.category);
        // 设置X轴数据
        String[] xItems = {"1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月(月份)"};
        chartModel.setXItems(xItems);
        // 设置Y轴数据类型
        chartModel.setYDataType(AxisType.value);
        // 设置XY轴名称
        chartModel.setXFieldName("X轴");
        chartModel.setYFieldName("Y轴");
        // 设置折线图数据
        ChartDataModel data1 = new ChartDataModel();
        // 图例名称
        data1.setName("数据1");
        // 设置为折线图
        data1.setType(ChartType.line);
        // 设置折线图颜色
        data1.setColor("#FF9400");
        // 设置折线图数据
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

        // 添加第二个折线图数据
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

        // 添加第三个折线图数据
        ChartDataModel data3 = new ChartDataModel();
        data3.setName("数据3");
        data3.setType(ChartType.line);
        data3.setColor("#FF736C");
        data3.setData(Arrays.asList(
                new ItemValue(null, 56),
                new ItemValue(null, 75),
                new ItemValue(null, 34),
                new ItemValue(null, 34),
                new ItemValue(null, 32),
                new ItemValue(null, 87),
                new ItemValue(null, 23),
                new ItemValue(null, 65),
                new ItemValue(null, 32),
                new ItemValue(null, 87),
                new ItemValue(null, 13),
                new ItemValue(null, 23)
        ));
        chartModel.setSeriesData(Arrays.asList(data1, data2, data3));

        // 饼图1
        PieChartModel pieChartModel1 = new PieChartModel();
        // 设置饼图标识
        pieChartModel1.setChartId("tpv_piechartap_confirm");
        // 设置饼图标题（饼图中间的文字）
        pieChartModel1.setTitle("90%");
        // 设置饼图数据
        pieChartModel1.setData(new ItemValue[]{
                new ItemValue("data", 90, "#18BC71"),
                new ItemValue("empty", 10, "#EEEEEE")});
        // 设置图例图标标识
        pieChartModel1.setLabelIconId("tpv_labelap11");
        // 设置图例图标
        pieChartModel1.setLabelIcon("✨");
        // 设置图例名称标识
        pieChartModel1.setLabelTextId("tpv_queryrate");
        // 设置图例名称
        pieChartModel1.setLabelText("查询率");

        // 饼图2
        PieChartModel pieChartModel2 = new PieChartModel();
        pieChartModel2.setChartId("tpv_piechartap_response");
        pieChartModel2.setTitle("80%");
        pieChartModel2.setData(new ItemValue[]{
                new ItemValue("data", 80, "#308AF0"),
                new ItemValue("empty", 20, "#EEEEEE")});
        pieChartModel2.setLabelIconId("tpv_labelap12");
        pieChartModel2.setLabelIcon("✨");
        pieChartModel2.setLabelTextId("tpv_confirmrate");
        pieChartModel2.setLabelText("确认率");

        // 饼图3
        PieChartModel pieChartModel3 = new PieChartModel();
        pieChartModel3.setChartId("tpv_piechartap_query");
        pieChartModel3.setTitle("70%");
        pieChartModel3.setData(new ItemValue[]{
                new ItemValue("data", 70, "#FF736C"),
                new ItemValue("empty", 30, "#EEEEEE")});
        pieChartModel3.setLabelIconId("tpv_labelap13");
        pieChartModel3.setLabelIcon("✨");
        pieChartModel3.setLabelTextId("tpv_responserate");
        pieChartModel3.setLabelText("答复率");

        // 渲染图像
        renderData(chartModel, pieChartModel1, pieChartModel2, pieChartModel3);
    }

    private void renderData(StatisticalChartModel chartModel, PieChartModel pieChartModel1, PieChartModel pieChartModel2, PieChartModel pieChartModel3)
    {
        this.initStatisticalChart(chartModel);
        this.initLapDes();
        this.initPieChart(pieChartModel1);
        this.initPieChart(pieChartModel2);
        this.initPieChart(pieChartModel3);
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

    private void initPieChart(PieChartModel pieChartModel)
    {
        PieChart pieChart = this.getControl(pieChartModel.getChartId());
        pieChart.setShowTitle(true);
        // 设置标题
        pieChart.setTitlePropValue("text", pieChartModel.getTitle());
        // 设置标题位置
        pieChart.setTitleAlign(XAlign.center, YAlign.center);
        // 设置标题样式
        Map<String, Object> textStyle = new HashMap<>();
        textStyle.put("fontSize", "20");
        pieChart.setTitlePropValue("textStyle", textStyle);
        // 设置副标题
        Map<String, Object> subTextStyle = new HashMap<>();
        subTextStyle.put("fontSize", "10");
        subTextStyle.put("color", "#999999");
        // 设置副标题样式
        pieChart.setTitlePropValue("subtextStyle", subTextStyle);
        // 设置图形属性
        PieSeries pieSeries = pieChart.createPieSeries("");
        pieSeries.setRadius("60%", "70%");
        pieSeries.setPropValue("hoverAnimation", Boolean.TRUE);
        // 设置标签
        Label label = new Label();
        label.setShow(false);
        label.setColor("#5f00bf");
        pieSeries.setLabel(label);
        // 设置饼图数据
        for (ItemValue datum : pieChartModel.getData()) {
            pieSeries.addData(datum);
        }
        // 设置图例
        kd.bos.form.control.Label labelap11 = this.getControl(pieChartModel.getLabelIconId());
        labelap11.setText(pieChartModel.getLabelIcon());
        kd.bos.form.control.Label queryrate = this.getControl(pieChartModel.getLabelTextId());
        queryrate.setText(pieChartModel.getLabelText());
    }
}
