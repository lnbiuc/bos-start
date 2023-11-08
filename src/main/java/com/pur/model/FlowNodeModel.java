package com.pur.model;

import java.util.List;

public class FlowNodeModel
{
    private String nodeId;

    private String title;

    private String subTitle;

    private String info1;

    private String info2;

    private String info3;

    private Integer x;

    private Integer y;

    private String sourceNodeId;

    private List<String> targetNodeId;

    private Integer level;

    public FlowNodeModel()
    {
    }

    public FlowNodeModel(String nodeId, String title, String subTitle, String info1, String info2, String info3, String sourceNodeId, List<String> targetNodeId, Integer level)
    {
        this.nodeId = nodeId;
        this.title = title;
        this.subTitle = subTitle;
        this.info1 = info1;
        this.info2 = info2;
        this.info3 = info3;
        this.sourceNodeId = sourceNodeId;
        this.targetNodeId = targetNodeId;
        this.level = level;
    }

    public String getNodeId()
    {
        return nodeId;
    }

    public void setNodeId(String nodeId)
    {
        this.nodeId = nodeId;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getSubTitle()
    {
        return subTitle;
    }

    public void setSubTitle(String subTitle)
    {
        this.subTitle = subTitle;
    }

    public String getInfo1()
    {
        return info1;
    }

    public void setInfo1(String info1)
    {
        this.info1 = info1;
    }

    public String getInfo2()
    {
        return info2;
    }

    public void setInfo2(String info2)
    {
        this.info2 = info2;
    }

    public String getInfo3()
    {
        return info3;
    }

    public void setInfo3(String info3)
    {
        this.info3 = info3;
    }

    public Integer getX()
    {
        return x;
    }

    public void setX(Integer x)
    {
        this.x = x;
    }

    public Integer getY()
    {
        return y;
    }

    public void setY(Integer y)
    {
        this.y = y;
    }

    public String getSourceNodeId()
    {
        return sourceNodeId;
    }

    public void setSourceNodeId(String sourceNodeId)
    {
        this.sourceNodeId = sourceNodeId;
    }

    public List<String> getTargetNodeId()
    {
        return targetNodeId;
    }

    public void setTargetNodeId(List<String> targetNodeId)
    {
        this.targetNodeId = targetNodeId;
    }

    public Integer getLevel()
    {
        return level;
    }

    public void setLevel(Integer level)
    {
        this.level = level;
    }
}
