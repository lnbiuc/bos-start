package com.pur.model;

public class Node
{
    private String id;
    private String type;
    private String parent;
    // Add more attributes as needed

    public Node(String id, String type, String parent)
    {
        this.id = id;
        this.type = type;
        this.parent = parent;
    }

    // Getters and setters for attributes

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getParent()
    {
        return parent;
    }

    public void setParent(String parent)
    {
        this.parent = parent;
    }
}
