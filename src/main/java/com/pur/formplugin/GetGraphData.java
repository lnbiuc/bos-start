package com.pur.formplugin;

import com.pur.model.FlowNodeModel;
import kd.bos.bill.AbstractBillPlugIn;
import kd.bos.workflow.design.plugin.IWorkflowDesigner;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GetGraphData extends AbstractBillPlugIn implements IWorkflowDesigner
{
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
        List<FlowNodeModel> flowNodeModels = calcPosition(nodelList);
        String genXml = convertNodeToXml(flowNodeModels);
//        String xml = readFileToString("D:/Code/bos/src/main/java/com/pur/xml/fourNode.xml");
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
