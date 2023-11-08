package com.pur.formplugin;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.pur.model.FlowNodeModel;
import kd.bos.bill.AbstractBillPlugIn;
import kd.bos.workflow.design.plugin.IWorkflowDesigner;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class GetGraphData extends AbstractBillPlugIn implements IWorkflowDesigner
{


    public List<FlowNodeModel> entityToNodeList(JSONArray entity)
    {
        List<FlowNodeModel> nodeList = new ArrayList<>();
        createFlowNodeModels(entity, "0", nodeList);
        return nodeList;
    }

    private void createFlowNodeModels(JSONArray entity, String parentId, List<FlowNodeModel> nodeList)
    {
        for (int i = 0; i < entity.size(); i++) {
            JSONObject jsonObject = entity.getJSONObject(i);
            String id = jsonObject.getString("id");
            String pid = "";
            if (jsonObject.get("pid") != null) {
                pid = jsonObject.getString("pid");
            } else {
                pid = "0";
            }
            if (pid.equals(String.valueOf(parentId))) {
                FlowNodeModel node = new FlowNodeModel();
                node.setNodeId(id);

                if (hasChildNodes(entity, id)) {
                    node.setSourceNodeId(id);
                    node.setTargetNodeId(getChildNodeIds(entity, id));
                }

                // Calculate the level of the node
                int level = calculateLevel(entity, id);
                node.setLevel(level);

                nodeList.add(node);

                // Recursively process child nodes
                createFlowNodeModels(entity, id, nodeList);
            }
        }
    }

    private boolean hasChildNodes(JSONArray entity, String id)
    {
        for (int i = 0; i < entity.size(); i++) {
            JSONObject jsonObject = entity.getJSONObject(i);
            String pid = "";
            if (jsonObject.get("pid") != null) {
                pid = jsonObject.getString("pid");
            } else {
                pid = "0";
            }
            if (pid.equals(id)) {
                return true;
            }
        }
        return false;
    }

    private List<String> getChildNodeIds(JSONArray entity, String id)
    {
        List<String> childIds = new ArrayList<>();
        for (int i = 0; i < entity.size(); i++) {
            JSONObject jsonObject = entity.getJSONObject(i);
            String pid = "";
            if (jsonObject.get("pid") != null) {
                pid = jsonObject.getString("pid");
            } else {
                pid = "0";
            }
            if (pid.equals(id)) {
                String childId = jsonObject.getString("id");
                childIds.add(childId);
            }
        }
        return childIds;
    }

    private int calculateLevel(JSONArray entity, String id)
    {
        int level = 0;
        String parentId = id;
        while (!parentId.equals("0")) {
            for (int i = 0; i < entity.size(); i++) {
                JSONObject jsonObject = entity.getJSONObject(i);
                String idValue = jsonObject.getString("id");
                String pid = "";
                if (jsonObject.get("pid") != null) {
                    pid = jsonObject.getString("pid");
                } else {
                    pid = "0";
                }
                if (idValue.equals(parentId)) {
                    parentId = pid;
                    level++;
                    break;
                }
            }
        }
        return level;
    }


    @Override
    public Map<String, Object> getDesignerInitData(Map<String, Object> map)
    {


        List<FlowNodeModel> nodelList = new ArrayList<>();
        List<String> targetList = new ArrayList<>();
        targetList.add("nodeId-2");
        targetList.add("nodeId-3");
        targetList.add("nodeId-4");
        List<String> targetList2 = new ArrayList<>();
        targetList2.add("nodeId-5");
        nodelList.add(new FlowNodeModel("nodeId-1", "node1Title", "node1SubTitle", "info1", "info2", "info3", null, targetList, 1));
        nodelList.add(new FlowNodeModel("nodeId-2", "node2Title", "node2SubTitle", "info1", "info2", "info3", "nodeId-1", targetList2, 2));
        nodelList.add(new FlowNodeModel("nodeId-3", "node3Title", "node3SubTitle", "info1", "info2", "info3", "nodeId-1", null, 2));
        nodelList.add(new FlowNodeModel("nodeId-4", "node4Title", "node4SubTitle", "info1", "info2", "info3", "nodeId-1", null, 2));
        nodelList.add(new FlowNodeModel("nodeId-5", "node5Title", "node5SubTitle", "info1", "info2", "info3", "nodeId-2", null, 3));
//        List<FlowNodeModel> flowNodeModels = calcPosition(nodelList);
//        List<FlowNodeModel> flowNodeModels = calcPosition(this.nodeList);
//        System.out.println("this.nodeList = " + this.nodeList);
//        String genXml = convertNodeToXml(nodelList);
////        String xml = readFileToString("D:/Code/bos/src/main/java/com/pur/xml/fourNode.xml");
//        System.out.println("genXml = " + genXml);
//        map.put("graph_xml", genXml);

        JSONArray entity = this.getView().getFormShowParameter().getCustomParam("entity");
        List<FlowNodeModel> nodeList = entityToNodeList(entity);
        for (FlowNodeModel flowNodeModel : nodeList) {
            for (Object object : entity) {
                JSONObject jsonObject = (JSONObject) object;
                if (jsonObject.getString("id").equals(flowNodeModel.getNodeId())) {
                    flowNodeModel.setTitle("title");
                    flowNodeModel.setSubTitle("subtitle");
                    flowNodeModel.setInfo1("name");
                    flowNodeModel.setInfo2("department");
                    flowNodeModel.setInfo3("status");
                }
            }
        }
        List<FlowNodeModel> flowNodeModels = calcPosition(nodeList);
        System.out.println("this.nodeList = " + nodeList);
        String genXml = convertNodeToXml(flowNodeModels);
//        String xml = readFileToString("D:/Code/bos/src/main/java/com/pur/xml/fourNode.xml");
        System.out.println("genXml = " + genXml);
        map.put("graph_xml", genXml);
        return map;
    }

    private String convertNodeToXml(List<FlowNodeModel> nodeList)
    {
        StringBuilder xml = new StringBuilder();
        // start
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        xml.append("<mxGraphModel grid=\"0\">\n" +
                "    <root>\n" +
                "        <mxCell id=\"node_0\"/>\n" +
                "        <mxCell id=\"node_1\" type=\"Diagram\" group=\"ProcessControl\" parent=\"node_0\">\n" +
                "            <Object process_id=\"bill_circulaterelation\" as=\"properties\"/>\n" +
                "        </mxCell>");

        nodeList.forEach(node ->
        {
            xml.append("<mxCell id=\"" + node.getNodeId() + "\" value=\"\"\n" +
                    "                style=\"shape=billCard;whiteSpace=wrap;spacingLeft=50;spacingRight=10;overflow=hidden;resizable=0;\"\n" +
                    "                type=\"billCard\" parent=\"node_1\" vertex=\"1\" showRecords=\"false\">\n" +
                    "            <mxGeometry width=\"216.0\" height=\"132.0\" " + "x=\"" + node.getX() + "\" " + "y=\"" + node.getY() + "\" as=\"geometry\"/>\n" +
                    "            <Object as=\"properties\"\n" +
                    "                    title=\"" + node.getTitle() + "\"\n" +
                    "                    subtitle=\"" + node.getSubTitle() + "\"\n" +
                    "                    name=\"" + node.getInfo1() + "\"\n" +
                    "                    department=\"" + node.getInfo2() + "\"\n" +
                    "                    status=\"" + node.getInfo3() + "\"\n" +
                    "                    />\n" +
                    "        </mxCell>");
            if (node.getTargetNodeId() != null && !node.getTargetNodeId().isEmpty()) {
                for (int i = 0; i < node.getTargetNodeId().size(); i++) {
                    xml.append(
                            "<mxCell id=\"node_line_" + node.getNodeId() + i + "\"\n" +
                                    "                style=\"edgeStyle=orthogonalEdgeStyle;rounded=1;html=1;jettySize=auto;orthogonalLoop=1;entryX=0;entryY=0.5;strokeColor=#A1CFFF!important;;\"\n" +
                                    "                type=\"SequenceFlow\" \n" +
                                    "                parent=\"node_1\" \n" +
                                    "                edge=\"1\" \n" +
                                    "                source=\"" + node.getNodeId() + "\"\n" +
                                    "                target=\"" + node.getTargetNodeId().get(i) + "\">\n" +
                                    "            <mxGeometry relative=\"1\" as=\"geometry\"/>\n" +
                                    "        </mxCell>");
                }
            }
        });

        // end
        xml.append("</root>\n" +
                "</mxGraphModel>");
        return xml.toString();
    }

    private List<FlowNodeModel> calcPosition(List<FlowNodeModel> nodeList)
    {
        int currentX = 0;
        int currentY = 0;
        int currentLevel = 1;

        for (FlowNodeModel node : nodeList) {
            int level = node.getLevel();

            if (level > currentLevel) {
                // 进入下一层，重置 x 和 y
                currentLevel = level;
                currentX += 300;
                currentY = 0;
            }

            node.setX(currentX);
            node.setY(currentY);

            currentY += 200;
        }

        return nodeList;
    }


    private int getNextLevelNode(List<FlowNodeModel> nodeList, int startLevel)
    {
        int nextLevel = Integer.MAX_VALUE;

        for (FlowNodeModel node : nodeList) {
            int level = node.getLevel();
            if (level > startLevel && level < nextLevel) {
                nextLevel = level;
            }
        }

        return nextLevel;
    }

    public static String readFileToString(String filePath)
    {
        try {
            byte[] bytes = Files.readAllBytes(Paths.get(filePath));
            return new String(bytes);
        } catch (IOException e) {
            return null;
        }
    }
}
