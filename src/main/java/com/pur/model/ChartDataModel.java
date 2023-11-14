package com.pur.model;

import kd.bos.form.chart.ChartType;
import kd.bos.form.chart.ItemValue;

import java.util.List;

public class ChartDataModel
{
    // 名称
    private String name;

    /**
     * 图表类型
     *
     * @see kd.bos.form.chart.ChartType
     * line：折线图
     * bar：柱状图
     */
    private ChartType type;
    private String color;
    // 图表数据
    private List<ItemValue> data;

    public ChartDataModel()
    {
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public ChartType getType()
    {
        return type;
    }

    public void setType(ChartType type)
    {
        this.type = type;
    }

    public String getColor()
    {
        return color;
    }

    public void setColor(String color)
    {
        this.color = color;
    }

    public List<ItemValue> getData()
    {
        return data;
    }

    public void setData(List<ItemValue> data)
    {
        this.data = data;
    }
}
