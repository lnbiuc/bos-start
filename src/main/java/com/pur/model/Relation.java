package com.pur.model;

import java.io.Serializable;
import java.util.List;

public class Relation implements Serializable
{

    // 单据id
    private Long id;

    private String title;

    // 父级id
    private Long parentId;

    // 是否实际节点
    private Boolean virtual;

    // 在设计器的横坐标
    private Integer x;

    // 在设计器的纵坐标
    private Integer y;

    // 节点的宽度
    private Integer width;

    // 节点的高度
    private Integer height;

    // 下级节点集合
    private List<Relation> targets;

    public Relation()
    {
    }

    public Relation(Long id, String title, Long parentId, Boolean virtual, Integer x, Integer y, Integer width, Integer height, List<Relation> targets)
    {
        this.id = id;
        this.title = title;
        this.parentId = parentId;
        this.virtual = virtual;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.targets = targets;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public Long getParentId()
    {
        return parentId;
    }

    public void setParentId(Long parentId)
    {
        this.parentId = parentId;
    }

    public Boolean getVirtual()
    {
        return virtual;
    }

    public void setVirtual(Boolean virtual)
    {
        this.virtual = virtual;
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

    public Integer getWidth()
    {
        return width;
    }

    public void setWidth(Integer width)
    {
        this.width = width;
    }

    public Integer getHeight()
    {
        return height;
    }

    public void setHeight(Integer height)
    {
        this.height = height;
    }

    public List<Relation> getTargets()
    {
        return targets;
    }

    public void setTargets(List<Relation> targets)
    {
        this.targets = targets;
    }
}