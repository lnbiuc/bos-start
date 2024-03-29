package com.pur.formplugin;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.pur.model.Relation;
import com.pur.utils.GraphUtil;
import kd.bos.bill.AbstractBillPlugIn;
import kd.bos.workflow.design.plugin.IWorkflowDesigner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GraphFormPlugin extends AbstractBillPlugIn implements IWorkflowDesigner
{

    @Override
    public Map<String, Object> getDesignerInitData(Map<String, Object> map)
    {
        JSONArray treeEntity = this.getView().getFormShowParameter().getCustomParam("entity");
        // 转换为流程图数据
        List<Relation> relations = convert(treeEntity);
        // 计算位置
        GraphUtil.createRelation(relations);

        StringBuilder relationXml = new StringBuilder();
        // 拼接xml
        String xml = spliceXml(relations, relationXml);
        map.put("graph_xml", xml);
        return map;
    }


    /**
     * 转换为流程图数据
     *
     * @param array 树形单据体数据
     * @return 流程图数据
     */
    public static List<Relation> convert(JSONArray array)
    {
        List<Relation> relations = new ArrayList<>();

        for (int i = 0; i < array.size(); i++) {

            JSONObject obj = array.getJSONObject(i);

            Relation relation = new Relation();
            // 如果为根节点，设置一个虚拟的父节点id
            if (obj.getLong("parentId") == null || obj.getLong("parentId") == 0) {
                relation.setParentId(11111L);
            }
            relation.setId(obj.getLong("id"));
            // 设置需要显示的数据
            if (obj.containsKey("tpv_treenuser")) {
                JSONObject userObject = obj.getObject("tpv_treenuser", JSONObject.class);
                if (userObject.containsKey("name")) {
                    JSONObject nameObject = userObject.getObject("name", JSONObject.class);
                    if (nameObject.containsKey("zh_CN")) {
                        relation.setTitle(nameObject.getString("zh_CN"));
                    } else {
                        relation.setTitle("user name not found");
                    }
                } else {
                    relation.setTitle("user name not found");
                }
            }
            if (obj.containsKey("tpv_treendate")) {
                relation.setSubTitle(obj.getString("tpv_treendate"));
            } else {
                relation.setSubTitle("");
            }
            if (obj.containsKey("tpv_treentext")) {
                relation.setText1(obj.get("tpv_treentext").toString());
            } else {
                relation.setText1("data not found");
            }
            if (obj.containsKey("seq")) {
                relation.setText2(obj.get("seq").toString());
            } else {
                relation.setText2("seq not found");
            }
            if (obj.containsKey("tpv_treenint")) {
                relation.setText3(obj.get("tpv_treenint").toString());
            } else {
                relation.setText3("int not found");
            }

            relation.setParentId(obj.getLong("pid"));
            relation.setTargets(new ArrayList<>());
            relation.setVirtual(false);
            relation.setHeight(132);
            relation.setWidth(216);

            relations.add(relation);
        }

        // 将子节点添加到父节点中
        for (Relation relation : relations) {
            long parentId = relation.getParentId();
            if (parentId != 0) {
                for (Relation parent : relations) {
                    if (parent.getId() == parentId) {
                        parent.getTargets().add(relation);
                        break;
                    }
                }
            }
        }
        // 创建虚拟的根节点
        Relation virtualRoot = new Relation();
        virtualRoot.setId(11111L);
        virtualRoot.setVirtual(true);
        virtualRoot.setWidth(200);
        virtualRoot.setHeight(150);
        ArrayList<Relation> rootNodeTarget = new ArrayList<>();
        for (Relation relation : relations) {
            if (relation.getParentId() == 0) {
                rootNodeTarget.add(relation);
            }
        }
        virtualRoot.setTargets(rootNodeTarget);
        relations.add(0, virtualRoot);
        return relations;
    }

    /**
     * 拼接xml
     *
     * @param relations 流程图数据
     * @param xml       xml
     * @return xml
     */
    private String spliceXml(List<Relation> relations, StringBuilder xml)
    {
        for (Relation relation : relations) {
            // 添加流程节点
            xml.append(spliceModel(relation));
            // 如果有父子级关系，添加节点间连线
            if (relation.getParentId() != null && relation.getParentId() != 0) {
                xml.append(spliceLine(relation));
            }
            List<Relation> targets = relation.getTargets();
            if (!targets.isEmpty()) {
                spliceXml(targets, xml);
            }
        }
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<mxGraphModel grid=\"0\">" +
                "<root>" +
                "    <mxCell id=\"node_0\"/>" +
                "    <mxCell id=\"node_1\" type=\"Diagram\" group=\"ProcessControl\" parent=\"node_0\">" +
                "        <Object process_id=\"bill_circulaterelation\" as=\"properties\"/>" +
                "    </mxCell>" +
                xml.toString() +
                "</root>" +
                "</mxGraphModel>";
    }

    /**
     * 拼接线
     *
     * @param relation 流程图数据
     * @return 线
     */
    private String spliceLine(Relation relation)
    {
        return "<mxCell id=\"node_line_" + relation.getId() + "\"" +
                "        style=\"edgeStyle=orthogonalEdgeStyle;rounded=1;html=1;jettySize=auto;orthogonalLoop=1;strokeColor=#A1CFFF!important;;\"" +
                "        type=\"SequenceFlow\" " +
                "        parent=\"node_1\" " +
                "        edge=\"1\" " +
                "        source=\"" + relation.getParentId() + "\"" +
                "        target=\"" + relation.getId() + "\">" +
                "    <mxGeometry relative=\"1\" as=\"geometry\"/>" +
                "</mxCell>";
    }

    /**
     * 拼接节点
     *
     * @param relation 流程图数据
     * @return 节点
     */
    private String spliceModel(Relation relation)
    {
        // 虚拟节点设置宽高为0，不显示
        if (relation.getVirtual()) {
            relation.setHeight(0);
            relation.setWidth(0);
        }
        String style = "shape=billCard";
        return "<mxCell id=\"" + relation.getId() + "\"" +
                " value=\"" + "\"" +
                " style=\"" + style + ";whiteSpace=wrap;spacingLeft=50;spacingRight=10;overflow=hidden;resizable=0\"" +
                " type=\"billCard\" parent=\"node_1\" vertex=\"1\" showRecords=\"false\" clickable=\"false\">" +
                "<mxGeometry width=\"" + relation.getWidth() + "\"" +
                " height=\"" + relation.getHeight() + "\"" +
                " x=\"" + relation.getX() + "\"" +
                " y=\"" + relation.getY() + "\" as=\"geometry\"/>" +
                "<Object as=\"properties\"" +
                "        title=\"" + relation.getTitle() + "\"" +
                "        subtitle=\"" + relation.getSubTitle() + "\"" +
                "        name=\"" + relation.getText1() + "\"" +
                "        department=\"" + relation.getText2() + "\"" +
                "        status=\"" + relation.getText3() + "\"" +
                "        />" +
                "</mxCell>";
    }
}
