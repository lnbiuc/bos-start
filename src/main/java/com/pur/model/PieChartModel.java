package com.pur.model;

import kd.bos.form.chart.ItemValue;

public class PieChartModel
{
    // 标识
    private String chartId;
    // 标题
    private String title;
    // 饼图数据
    private ItemValue[] data;
    // 图例图标标识
    private String labelIconId;
    // 图例图标
    private String labelIcon;
    // 图例名称标识
    private String labelTextId;
    // 图例名称
    private String labelText;

    public PieChartModel()
    {
    }

    public String getLabelIconId()
    {
        return labelIconId;
    }

    public void setLabelIconId(String labelIconId)
    {
        this.labelIconId = labelIconId;
    }

    public String getLabelTextId()
    {
        return labelTextId;
    }

    public void setLabelTextId(String labelTextId)
    {
        this.labelTextId = labelTextId;
    }

    public String getChartId()
    {
        return chartId;
    }

    public void setChartId(String chartId)
    {
        this.chartId = chartId;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public ItemValue[] getData()
    {
        return data;
    }

    public void setData(ItemValue[] data)
    {
        this.data = data;
    }

    public String getLabelIcon()
    {
        return labelIcon;
    }

    public void setLabelIcon(String labelIcon)
    {
        this.labelIcon = labelIcon;
    }

    public String getLabelText()
    {
        return labelText;
    }

    public void setLabelText(String labelText)
    {
        this.labelText = labelText;
    }
}
