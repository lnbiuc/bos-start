package com.pur.model;

import java.util.List;

public class NodeModel
{
    private List<Node> nodes;

    public List<Node> getNodes()
    {
        return nodes;
    }

    public void setNodes(List<Node> nodes)
    {
        this.nodes = nodes;
    }

    public void addNode(Node node)
    {
        this.nodes.add(node);
    }
}
