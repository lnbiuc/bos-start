package com.pur.formplugin;

import com.pur.model.NodeModel;
import com.pur.model.Relation;
import kd.bos.bill.AbstractBillPlugIn;
import kd.bos.workflow.design.plugin.IWorkflowDesigner;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;

public class GetGraphData extends AbstractBillPlugIn implements IWorkflowDesigner
{
    @Override
    public Map<String, Object> getDesignerInitData(Map<String, Object> map)
    {
//        BpmnModel bpmnModel = new BpmnModel();
//        FormShowParameter parameter = new FormShowParameter();
//        DynamicObjectCollection treeData = parameter.getCustomParam(ViewFlowchartConstant.PROCINSTID);
//        List<Relation> relationList = new ArrayList<>();
//        Relation relation = new Relation(1L, "APPLY-231106-0010", "tpv_app_proc_req", "采购申请", 1L, 0L, null, false, 157, 518, null);
//        Relation relation1 = new Relation(2L, "ORDER-231106-0011", "tpv_app_proc_order", "采购订单", 1L, 1L, "tpv_app_proc_req", false, 453, 518, null);
//        relationList.add(relation);
//        relationList.add(relation1);
//        String spliceXml = spliceXml(relationList, new StringBuilder());
//        StringBuilder xml = new StringBuilder();
//        for (Relation r : relationList) {
//            xml.append(spliceModel(r));
//            if (StringUtils.isNotEmpty(r.getParentEntityNumber())) {
//                xml.append(spliceLine(r));
//            }
//            List<Relation> targets = r.getTargets();
//            if (!targets.isEmpty()) {
//                spliceXml(targets, xml);
//            }
//            System.out.println(r);
//        }
//        System.out.println(spliceXml);
//        BpmnModel bpmnModel = new BpmnModel();
//        GraphCodecContext context = new GraphCodecContext(bpmnModel);
//        Node genXml = GraphCodecUtils.convertBpmnModelToNode(bpmnModel, context);
//        String genXmlStr = genXml.asXML();
//        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
//                "<mxGraphModel grid=\"0\"" +
//                "<root>" +
//                "<mxCell id=\"node_0\"/>" +
//                "<mxCell id=\"node_1\" parent=\"node_0\" type=\"Diagram\" group=\"ProcessControl\"/>" +
//                "<Object as=\"properties\"/>" +
////                "</mxCell>" + spliceXml + "</root>" +
//                "</mxGraphModel>";
//        NodeModel nodeModel = new NodeModel();
//        nodeModel.addNode(new Node("node_1", "Diagram", "node_0"));
//        nodeModel.addNode(new Node("bill_circulaterelation_billCard_1", "billCard", "node_1"));
//        String xml = convertModelToXml(nodeModel);
        

        String twoNode = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<mxGraphModel grid=\"0\"><root><mxCell id=\"node_0\"/><mxCell id=\"node_1\" type=\"Diagram\" group=\"ProcessControl\" parent=\"node_0\"><Object process_id=\"bill_circulaterelation\" as=\"properties\"/></mxCell><mxCell id=\"bill_circulaterelation_billCard_1\" value=\"\" style=\"shape=billCard;whiteSpace=wrap;spacingLeft=50;spacingRight=10;overflow=hidden;resizable=0;\" type=\"billCard\" parent=\"node_1\" vertex=\"1\" showRecords=\"false\"><mxGeometry width=\"216.0\" height=\"132.0\" x=\"157.0\" y=\"518.0\" as=\"geometry\"/><Object as=\"properties\" entityName=\"采购申请\" subtitle=\"APPLY-231106-0010\" entityNumber=\"tpv_app_proc_req\" businessKey=\"1812376428591713280\" name=\"\" title=\"采购申请\" department=\"\" activityInstId=\"1812376431510947840\" status=\"\"/></mxCell><mxCell id=\"bill_circulaterelation_billCard_1_child\" parent=\"bill_circulaterelation_billCard_1\" vertex=\"1\" style=\"shape=ierp.billrelation.Location;\"><mxGeometry width=\"32.0\" height=\"32.0\" x=\"0.0\" y=\"0.0\" relative=\"true\" as=\"geometry\"><mxPoint x=\"-16.0\" y=\"-16.0\" as=\"offset\"/></mxGeometry></mxCell><mxCell id=\"bill_circulaterelation_billCard_2\" value=\"\" style=\"shape=billCard;whiteSpace=wrap;spacingLeft=50;spacingRight=10;overflow=hidden;resizable=0;\" type=\"billCard\" parent=\"node_1\" vertex=\"1\" showRecords=\"false\"><mxGeometry width=\"216.0\" height=\"132.0\" x=\"453.0\" y=\"518.0\" as=\"geometry\"/><Object as=\"properties\" entityName=\"采购订单\" subtitle=\"ORDER-231106-0011\" entityNumber=\"tpv_app_proc_order\" businessKey=\"1812376827520355328\" name=\"\" title=\"采购订单\" department=\"\" status=\"\"/></mxCell><mxCell id=\"bill_circulaterelation_SequenceFlow3\" style=\"edgeStyle=orthogonalEdgeStyle;rounded=1;html=1;jettySize=auto;orthogonalLoop=1;entryX=0;entryY=0.5;strokeColor=#A1CFFF!important;;\" type=\"SequenceFlow\" parent=\"node_1\" edge=\"1\" source=\"bill_circulaterelation_billCard_1\" target=\"bill_circulaterelation_billCard_2\"><mxGeometry relative=\"1\" as=\"geometry\"/></mxCell><mxCell id=\"bill_circulaterelation_SequenceFlow3_child\" parent=\"bill_circulaterelation_SequenceFlow3\" vertex=\"1\" style=\"shape=ierp.billrelation.IconComplete;\"><Object entityNumber=\"tpv_app_proc_req\" businessKey=\"1812376428591713280\" type=\"complete\" title=\"下推已完成\" as=\"properties\"/><mxGeometry width=\"24.0\" height=\"24.0\" x=\"0.0\" y=\"0.0\" relative=\"true\" as=\"geometry\"><mxPoint x=\"-32.0\" y=\"-12.0\" as=\"offset\"/></mxGeometry></mxCell></root></mxGraphModel>";
        String threeNode = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><mxGraphModel grid=\"0\"><root><mxCell id=\"node_0\"/><mxCell id=\"node_1\" type=\"Diagram\" group=\"ProcessControl\" parent=\"node_0\"><Object process_id=\"bill_circulaterelation\" as=\"properties\"/></mxCell><mxCell id=\"bill_circulaterelation_billCard_1\" value=\"\" style=\"shape=billCard;whiteSpace=wrap;spacingLeft=50;spacingRight=10;overflow=hidden;resizable=0;\" type=\"billCard\" parent=\"node_1\" vertex=\"1\" showRecords=\"false\"><mxGeometry width=\"216.0\" height=\"132.0\" x=\"157.0\" y=\"518.0\" as=\"geometry\"/><Object as=\"properties\" entityName=\"采购申请\" subtitle=\"APPLY-231106-0010\" entityNumber=\"tpv_app_proc_req\" businessKey=\"1812376428591713280\" name=\"\" title=\"采购申请\" department=\"\" activityInstId=\"1812376431510947840\" status=\"\"/></mxCell><mxCell id=\"bill_circulaterelation_billCard_1_child\" parent=\"bill_circulaterelation_billCard_1\" vertex=\"1\" style=\"shape=ierp.billrelation.Location;\"><mxGeometry width=\"32.0\" height=\"32.0\" x=\"0.0\" y=\"0.0\" relative=\"true\" as=\"geometry\"><mxPoint x=\"-16.0\" y=\"-16.0\" as=\"offset\"/></mxGeometry></mxCell><mxCell id=\"bill_circulaterelation_billCard_2\" value=\"\" style=\"shape=billCard;whiteSpace=wrap;spacingLeft=50;spacingRight=10;overflow=hidden;resizable=0;\" type=\"billCard\" parent=\"node_1\" vertex=\"1\" showRecords=\"false\"><mxGeometry width=\"216.0\" height=\"132.0\" x=\"453.0\" y=\"518.0\" as=\"geometry\"/><Object as=\"properties\" entityName=\"采购订单\" subtitle=\"ORDER-231106-0011\" entityNumber=\"tpv_app_proc_order\" businessKey=\"1812376827520355328\" name=\"\" title=\"采购订单\" department=\"\" status=\"\"/></mxCell><mxCell id=\"bill_circulaterelation_SequenceFlow3\" style=\"edgeStyle=orthogonalEdgeStyle;rounded=1;html=1;jettySize=auto;orthogonalLoop=1;entryX=0;entryY=0.5;strokeColor=#A1CFFF!important;;\" type=\"SequenceFlow\" parent=\"node_1\" edge=\"1\" source=\"bill_circulaterelation_billCard_1\" target=\"bill_circulaterelation_billCard_2\"><mxGeometry relative=\"1\" as=\"geometry\"/></mxCell><mxCell id=\"bill_circulaterelation_SequenceFlow3_child\" parent=\"bill_circulaterelation_SequenceFlow3\" vertex=\"1\" style=\"shape=ierp.billrelation.IconComplete;\"><Object entityNumber=\"tpv_app_proc_req\" businessKey=\"1812376428591713280\" type=\"complete\" title=\"下推已完成\" as=\"properties\"/><mxGeometry width=\"24.0\" height=\"24.0\" x=\"0.0\" y=\"0.0\" relative=\"true\" as=\"geometry\"><mxPoint x=\"-32.0\" y=\"-12.0\" as=\"offset\"/></mxGeometry></mxCell><mxCell id=\"bill_circulaterelation_billCard_3\" value=\"\" style=\"shape=billCard;whiteSpace=wrap;spacingLeft=50;spacingRight=10;overflow=hidden;resizable=0;\" type=\"billCard\" parent=\"node_1\" vertex=\"1\" showRecords=\"false\"><mxGeometry width=\"216.0\" height=\"132.0\" x=\"453.0\" y=\"518.0\" as=\"geometry\"/><Object as=\"properties\" entityName=\"采购订单--3\" subtitle=\"ORDER-231106-0011\" entityNumber=\"tpv_app_proc_order\" businessKey=\"1812376827520355328\" name=\"\" title=\"采购订单\" department=\"\" status=\"\"/></mxCell><mxCell id=\"bill_circulaterelation_SequenceFlow3\" style=\"edgeStyle=orthogonalEdgeStyle;rounded=1;html=1;jettySize=auto;orthogonalLoop=1;entryX=0;entryY=0.5;strokeColor=#A1CFFF!important;;\" type=\"SequenceFlow\" parent=\"node_1\" edge=\"1\" source=\"bill_circulaterelation_billCard_1\" target=\"bill_circulaterelation_billCard_2\"><mxGeometry relative=\"1\" as=\"geometry\"/></mxCell><mxCell id=\"bill_circulaterelation_SequenceFlow3_child\" parent=\"bill_circulaterelation_SequenceFlow3\" vertex=\"1\" style=\"shape=ierp.billrelation.IconComplete;\"><Object entityNumber=\"tpv_app_proc_req\" businessKey=\"1812376428591713280\" type=\"complete\" title=\"下推已完成\" as=\"properties\"/><mxGeometry width=\"24.0\" height=\"24.0\" x=\"0.0\" y=\"0.0\" relative=\"true\" as=\"geometry\"><mxPoint x=\"-32.0\" y=\"-12.0\" as=\"offset\"/></mxGeometry></mxCell></root></mxGraphModel>";
        String fourNode = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><mxGraphModel grid=\"0\"><root><mxCell id=\"node_0\"/><mxCell id=\"node_1\" type=\"Diagram\" group=\"ProcessControl\" parent=\"node_0\"><Object process_id=\"bill_circulaterelation\" as=\"properties\"/></mxCell><mxCell id=\"bill_circulaterelation_billCard_1\" value=\"\" style=\"shape=billCard;whiteSpace=wrap;spacingLeft=50;spacingRight=10;overflow=hidden;resizable=0;\" type=\"billCard\" parent=\"node_1\" vertex=\"1\" showRecords=\"false\"><mxGeometry width=\"216.0\" height=\"132.0\" x=\"-139.0\" y=\"518.0\" as=\"geometry\"/><Object as=\"properties\" entityName=\"采购合同\" subtitle=\"HNJYHNJY01--HC231103001C\" entityNumber=\"pm_purorderbill\" businessKey=\"1810097047714420736\" name=\"采购组织：海宁旌云商贸有限公司\" title=\"采购合同\" department=\"采购员：\" activityInstId=\"1810097464250758144\" status=\"订单日期：2023-11-03\"/></mxCell><mxCell id=\"bill_circulaterelation_billCard_1_child\" parent=\"bill_circulaterelation_billCard_1\" vertex=\"1\" style=\"shape=ierp.billrelation.Location;\"><mxGeometry width=\"32.0\" height=\"32.0\" x=\"0.0\" y=\"0.0\" relative=\"true\" as=\"geometry\"><mxPoint x=\"-16.0\" y=\"-16.0\" as=\"offset\"/></mxGeometry></mxCell><mxCell id=\"bill_circulaterelation_billCard_2\" value=\"\" style=\"shape=billCard;whiteSpace=wrap;spacingLeft=50;spacingRight=10;overflow=hidden;resizable=0;\" type=\"billCard\" parent=\"node_1\" vertex=\"1\" showRecords=\"false\"><mxGeometry width=\"216.0\" height=\"132.0\" x=\"157.0\" y=\"518.0\" as=\"geometry\"/><Object as=\"properties\" entityName=\"付款申请单\" subtitle=\"FKSQ-00009026\" entityNumber=\"ap_payapply\" businessKey=\"1812269294617125888\" name=\"申请人：钟婧婧\" title=\"付款申请单\" department=\"申请金额：￥2,910,000.00\" activityInstId=\"1812269651569173504\" status=\"申请事由：\"/></mxCell><mxCell id=\"bill_circulaterelation_SequenceFlow3\" style=\"edgeStyle=orthogonalEdgeStyle;rounded=1;html=1;jettySize=auto;orthogonalLoop=1;entryX=0;entryY=0.5;strokeColor=#A1CFFF!important;;\" type=\"SequenceFlow\" parent=\"node_1\" edge=\"1\" source=\"bill_circulaterelation_billCard_1\" target=\"bill_circulaterelation_billCard_2\"><mxGeometry relative=\"1\" as=\"geometry\"/></mxCell><mxCell id=\"bill_circulaterelation_SequenceFlow3_child\" parent=\"bill_circulaterelation_SequenceFlow3\" vertex=\"1\" style=\"shape=ierp.billrelation.IconComplete;\"><Object entityNumber=\"pm_purorderbill\" businessKey=\"1810097047714420736\" type=\"complete\" title=\"下推已完成\" as=\"properties\"/><mxGeometry width=\"24.0\" height=\"24.0\" x=\"0.0\" y=\"0.0\" relative=\"true\" as=\"geometry\"><mxPoint x=\"-32.0\" y=\"-12.0\" as=\"offset\"/></mxGeometry></mxCell><mxCell id=\"bill_circulaterelation_billCard_3\" value=\"\" style=\"shape=billCard;whiteSpace=wrap;spacingLeft=50;spacingRight=10;overflow=hidden;resizable=0;\" type=\"billCard\" parent=\"node_1\" vertex=\"1\" showRecords=\"false\"><mxGeometry width=\"216.0\" height=\"132.0\" x=\"453.0\" y=\"518.0\" as=\"geometry\"/><Object as=\"properties\" entityName=\"付款处理\" subtitle=\"PV-202311-009403\" entityNumber=\"cas_paybill\" businessKey=\"1812448679882166272\" name=\"单据编号：PV-202311-009403\" title=\"付款处理\" department=\"单据状态：已付款\" status=\"付款金额：￥2,910,000.00\"/></mxCell><mxCell id=\"bill_circulaterelation_SequenceFlow5\" style=\"edgeStyle=orthogonalEdgeStyle;rounded=1;html=1;jettySize=auto;orthogonalLoop=1;entryX=0;entryY=0.5;strokeColor=#A1CFFF!important;;\" type=\"SequenceFlow\" parent=\"node_1\" edge=\"1\" source=\"bill_circulaterelation_billCard_2\" target=\"bill_circulaterelation_billCard_3\"><mxGeometry relative=\"1\" as=\"geometry\"/></mxCell><mxCell id=\"bill_circulaterelation_SequenceFlow5_child\" parent=\"bill_circulaterelation_SequenceFlow5\" vertex=\"1\" style=\"shape=ierp.billrelation.IconComplete;\"><Object entityNumber=\"ap_payapply\" businessKey=\"1812269294617125888\" type=\"complete\" title=\"下推已完成\" as=\"properties\"/><mxGeometry width=\"24.0\" height=\"24.0\" x=\"0.0\" y=\"0.0\" relative=\"true\" as=\"geometry\"><mxPoint x=\"-32.0\" y=\"-12.0\" as=\"offset\"/></mxGeometry></mxCell><mxCell id=\"bill_circulaterelation_billCard_4\" value=\"\" style=\"shape=billCard;whiteSpace=wrap;spacingLeft=50;spacingRight=10;overflow=hidden;resizable=0;\" type=\"billCard\" parent=\"node_1\" vertex=\"1\" showRecords=\"false\"><mxGeometry width=\"216.0\" height=\"132.0\" x=\"749.0\" y=\"442.0\" as=\"geometry\"/><Object as=\"properties\" entityName=\"银行日记账\" subtitle=\"PV-202311-009403\" entityNumber=\"cas_bankjournal\" businessKey=\"1812452750009598976\" name=\"\" title=\"银行日记账\" department=\"\" status=\"\"/></mxCell><mxCell id=\"bill_circulaterelation_SequenceFlow7\" style=\"edgeStyle=orthogonalEdgeStyle;rounded=1;html=1;jettySize=auto;orthogonalLoop=1;entryX=0;entryY=0.5;strokeColor=#A1CFFF!important;;\" type=\"SequenceFlow\" parent=\"node_1\" edge=\"1\" source=\"bill_circulaterelation_billCard_3\" target=\"bill_circulaterelation_billCard_4\"><mxGeometry relative=\"1\" as=\"geometry\"/></mxCell><mxCell id=\"bill_circulaterelation_billCard_5\" value=\"\" style=\"shape=billCard;whiteSpace=wrap;spacingLeft=50;spacingRight=10;overflow=hidden;resizable=0;\" type=\"billCard\" parent=\"node_1\" vertex=\"1\" showRecords=\"false\"><mxGeometry width=\"216.0\" height=\"132.0\" x=\"749.0\" y=\"594.0\" as=\"geometry\"/><Object as=\"properties\" entityName=\"凭证\" subtitle=\"HNJY记-0011\" entityNumber=\"gl_voucher\" businessKey=\"1812457776270552065\" name=\"\" title=\"凭证\" department=\"\" status=\"\"/></mxCell><mxCell id=\"bill_circulaterelation_SequenceFlow9\" style=\"edgeStyle=orthogonalEdgeStyle;rounded=1;html=1;jettySize=auto;orthogonalLoop=1;entryX=0;entryY=0.5;strokeColor=#A1CFFF!important;;\" type=\"SequenceFlow\" parent=\"node_1\" edge=\"1\" source=\"bill_circulaterelation_billCard_3\" target=\"bill_circulaterelation_billCard_5\"><mxGeometry relative=\"1\" as=\"geometry\"/></mxCell><mxCell id=\"bill_circulaterelation_SequenceFlow9_child\" parent=\"bill_circulaterelation_SequenceFlow9\" vertex=\"1\" style=\"shape=ierp.billrelation.IconComplete;\"><Object entityNumber=\"cas_paybill\" businessKey=\"1812448679882166272\" type=\"complete\" title=\"下推已完成\" as=\"properties\"/><mxGeometry width=\"24.0\" height=\"24.0\" x=\"0.0\" y=\"0.0\" relative=\"true\" as=\"geometry\"><mxPoint x=\"-32.0\" y=\"-50.0\" as=\"offset\"/></mxGeometry></mxCell></root></mxGraphModel>";
        map.put("graph_xml", fourNode);
        return map;
    }


    public static String convertModelToXml(NodeModel nodeModel)
    {
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();

            Element mxGraphModel = document.createElement("mxGraphModel");
            mxGraphModel.setAttribute("grid", "0");
            document.appendChild(mxGraphModel);

            Element root = document.createElement("root");
            mxGraphModel.appendChild(root);

            Element mxCellNode = document.createElement("mxCell");
            mxCellNode.setAttribute("id", "node_0");
            root.appendChild(mxCellNode);

            // Create other mxCell elements based on your NodeModel here
            // You can loop through the nodes in your NodeModel and create corresponding XML elements

            // Example:
            Element mxCellElement = document.createElement("mxCell");
            mxCellElement.setAttribute("id", "bill_circulaterelation_billCard_1");
            // Add more attributes as needed
            mxCellNode.appendChild(mxCellElement);

            // Repeat the above process for other nodes in your model

            // Finally, transform the XML document to a string
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(document), new StreamResult(writer));

            return writer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    public String spliceXml(List<Relation> relations, StringBuilder xml)
    {
        relations.forEach(relation ->
        {
            xml.append(spliceModel(relation));
            if (StringUtils.isNotEmpty(relation.getParentEntityNumber())) {
                xml.append(spliceLine(relation));
            }
            List<Relation> targets = relation.getTargets();
            if (!targets.isEmpty()) {
                spliceXml(targets, xml);
            }
        });
        return xml.toString();
    }

    public String spliceModel(Relation relation)
    {
        return "<mxCell id=\"" + relation.getEntityNumber() + "_" + relation.getId() + "\"" +
                "value=\"" + "-" + relation.getEntityName() + relation.getBillno() + "\"" +
                "style=\"" + "\"shape=ierp.bpmn.AuditTask_USE\"" + ";whiteSpace=wrap;spacingLeft=50;spacingRight=10;overflow=hidden;resizable=0;\"" +
                "type=\"AuditTask\" parent=\"node_1\" vertex=\"1\" clickable=\"true\">" +
                "<mxGeometry width=\"" + relation.getWidth() + "\"" +
                "height=\"" + relation.getHeight() + "\"" +
                "x=\"" + relation.getX() + "\"" +
                "y=\"" + relation.getY() + "\"" +
                "as=\"geometry\"/>" +
                "</mxCell>";
    }

    public String spliceLine(Relation relation)
    {
        return "<mxCell id=\"" + relation.getEntityNumber() + "_" + relation.getBillno() + "\"" +
                "style=\"edgeStyle=orthogonalEdgeStyle;rounded=1;html=1;jettySize=auto;orthogonalLoop=1;entryX=0;entryY=0.5;strokeColor=#A1CFFF!important;;\"" +
                "type=\"SequenceFlow\" parent=\"node_1\" edge=\"1\" source=\"" + relation.getParentEntityNumber() + "_" + relation.getParentId() + "\"" +
                "target=\"" + relation.getEntityNumber() + "_" + relation.getId() + "\">" +
                "<mxGeometry relative=\"1\" as=\"geometry\"/>" +
                "</mxCell>";
    }
}
