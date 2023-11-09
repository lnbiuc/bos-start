package com.pur.formplugin;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.pur.model.FlowNodeModel;
import kd.bos.bill.AbstractBillPlugIn;
import kd.bos.workflow.design.plugin.IWorkflowDesigner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GetGraphData extends AbstractBillPlugIn implements IWorkflowDesigner
{

    @Override
    public Map<String, Object> getDesignerInitData(Map<String, Object> map)
    {
        // 获取树形单据体数据
        JSONArray treeEntity = this.getView().getFormShowParameter().getCustomParam("entity");
        List<FlowNodeModel> nodeList = entityToNodeList(treeEntity);

        List<FlowNodeModel> nodeModels = calcPosition(nodeList);
        String genXml = convertNodeToXml(nodeModels);
        map.put("graph_xml", genXml);
        return map;
    }

    public List<FlowNodeModel> entityToNodeList(JSONArray jsonArray)
    {
        List<FlowNodeModel> nodeList = new ArrayList<>();
        createFlowNodeModels(jsonArray, "0", nodeList);
        for (FlowNodeModel node : nodeList) {
            for (Object entity : jsonArray) {
                JSONObject object = (JSONObject) entity;
                if (object.getString("id").equals(node.getNodeId())) {
                    if (object.containsKey("tpv_treenuser")) {
                        JSONObject userObject = object.getObject("tpv_treenuser", JSONObject.class);
                        if (userObject.containsKey("name")) {
                            JSONObject nameObject = userObject.getObject("name", JSONObject.class);
                            if (nameObject.containsKey("zh_CN")) {
                                node.setTitle(nameObject.getString("zh_CN"));
                            } else {
                                node.setTitle("user name not found");
                            }
                        } else {
                            node.setTitle("user name not found");
                        }
                    } else {
                        node.setTitle("user name not found");
                    }
                    if (object.containsKey("tpv_treendata")) {
                        node.setSubTitle(object.get("tpv_treendata").toString());
                    } else {
                        node.setSubTitle("data not found");
                    }
                    if (object.containsKey("tpv_treentext")) {
                        node.setInfo1(object.get("tpv_treentext").toString());
                    } else {
                        node.setInfo1("text not found");
                    }
                    if (object.containsKey("tpv_treenint")) {
                        node.setInfo2(object.get("tpv_treenint").toString());
                    } else {
                        node.setInfo2("int not found");
                    }
                    if (object.containsKey("seq")) {
                        node.setInfo3(object.get("seq").toString());
                    } else {
                        node.setInfo3("seq not found");
                    }
                }
            }
        }
        return nodeList;
    }


    private void createFlowNodeModels(JSONArray entity, String parentId, List<FlowNodeModel> nodeList)
    {
        for (int i = 0; i < entity.size(); i++) {
            JSONObject jsonObject = entity.getJSONObject(i);
            String id = jsonObject.getString("id");
            String pid;
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
            String pid;
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
            String pid;
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
                String pid;
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
        int initialX = 0;

        for (FlowNodeModel node : nodeList) {
            int level = node.getLevel();

            if (level == 1) {
                // Reset x value to the initialX when level changes back to 1
                currentX = initialX;

            } else if (level > currentLevel) {
                currentLevel = level;
                currentX += 300;
                currentY = 0;
            }

            node.setX(currentX);
            node.setY(currentY);

            currentY += 200;
        }
        System.out.println("JSONObject.toJSONString(nodeList) = " + JSONObject.toJSONString(nodeList));
        return nodeList;
    }
}
