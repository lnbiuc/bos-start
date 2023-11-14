package com.pur.model;

import kd.bos.form.chart.AxisType;

import java.util.List;

public class StatisticalChartModel
{
    // 标识
    private String chartId;

    /**
     * X轴数据类型
     *
     * @see kd.bos.form.chart.AxisType
     * category：类目轴，适用于离散的类目数据
     * value：数值轴，适用于连续数据
     * log：对数轴。适用于对数数据
     */
    private AxisType XDataType;
    // Y轴数据类型
    private AxisType YDataType;
    // X轴名称
    private String XFieldName;
    // Y轴名称
    private String YFieldName;
    // X轴数据
    private String[] XItems;
    // Y轴数据
    private String[] YItems;
    // 图表数据
    private List<ChartDataModel> seriesData;

    public StatisticalChartModel()
    {
    }

    public String getChartId()
    {
        return chartId;
    }

    public void setChartId(String chartId)
    {
        this.chartId = chartId;
    }

    public AxisType getXDataType()
    {
        return XDataType;
    }

    public void setXDataType(AxisType XDataType)
    {
        this.XDataType = XDataType;
    }

    public AxisType getYDataType()
    {
        return YDataType;
    }

    public void setYDataType(AxisType YDataType)
    {
        this.YDataType = YDataType;
    }

    public String getXFieldName()
    {
        return XFieldName;
    }

    public void setXFieldName(String XFieldName)
    {
        this.XFieldName = XFieldName;
    }

    public String getYFieldName()
    {
        return YFieldName;
    }

    public void setYFieldName(String YFieldName)
    {
        this.YFieldName = YFieldName;
    }


    public List<ChartDataModel> getSeriesData()
    {
        return seriesData;
    }

    public void setSeriesData(List<ChartDataModel> seriesData)
    {
        this.seriesData = seriesData;
    }

    public String[] getXItems()
    {
        return XItems;
    }

    public void setXItems(String[] XItems)
    {
        this.XItems = XItems;
    }

    public String[] getYItems()
    {
        return YItems;
    }

    public void setYItems(String[] YItems)
    {
        this.YItems = YItems;
    }
}
