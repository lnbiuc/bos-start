package com.pur.model;

import java.util.List;

public class Relation
{
    private Long id;
    private String billno;
    private String entityNumber;
    private String entityName;
    private Long billnoType;
    private Long parentId;
    private String parentEntityNumber;
    private Boolean virtual;

    private Integer x;
    private Integer y;

    private List<Relation> targets;

    public Relation()
    {
    }

    public Relation(Long id, String billno, String entityNumber, String entityName, Long billnoType, Long parentId, String parentEntityNumber, Boolean virtual, Integer x, Integer y, List<Relation> targets)
    {
        this.id = id;
        this.billno = billno;
        this.entityNumber = entityNumber;
        this.entityName = entityName;
        this.billnoType = billnoType;
        this.parentId = parentId;
        this.parentEntityNumber = parentEntityNumber;
        this.virtual = virtual;
        this.x = x;
        this.y = y;
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

    public String getBillno()
    {
        return billno;
    }

    public void setBillno(String billno)
    {
        this.billno = billno;
    }

    public String getEntityNumber()
    {
        return entityNumber;
    }

    public void setEntityNumber(String entityNumber)
    {
        this.entityNumber = entityNumber;
    }

    public String getEntityName()
    {
        return entityName;
    }

    public void setEntityName(String entityName)
    {
        this.entityName = entityName;
    }

    public Long getBillnoType()
    {
        return billnoType;
    }

    public void setBillnoType(Long billnoType)
    {
        this.billnoType = billnoType;
    }

    public Long getParentId()
    {
        return parentId;
    }

    public void setParentId(Long parentId)
    {
        this.parentId = parentId;
    }

    public String getParentEntityNumber()
    {
        return parentEntityNumber;
    }

    public void setParentEntityNumber(String parentEntityNumber)
    {
        this.parentEntityNumber = parentEntityNumber;
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
        return 195;
    }

    public Integer getHeight()
    {
        return 65;
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
