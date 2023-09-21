package com.uangel.sim.scenario.nodes;

import com.uangel.sim.scenario.type.AttrName;
import com.uangel.sim.scenario.type.NodeName;
import com.uangel.sim.scenario.xml.XmlParser;
import lombok.ToString;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dajin kim
 */
@ToString
public class MsgNode extends XmlParser {

    private final String method;
    private final String url;
    private final List<HeaderNode> headerNodes = new ArrayList<>();
    private final List<BodyNode> bodyNodes = new ArrayList<>();

    public MsgNode(Node xmlNode) {
        super(xmlNode);

        // todo default get
        this.method = getStrAttrValue(AttrName.METHOD.getValue());
        this.url = getStrAttrValue(AttrName.URL.getValue());

        NodeList childNodes = xmlNode.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node childNode = childNodes.item(i);
            if (isElementNode(childNode)) {
                // Field List
                List<FieldNode> fieldNodes = createFieldInfos(childNode.getChildNodes());

                if (NodeName.BODY.getValue().equals(childNode.getNodeName())) {

                    System.out.println("Body FieldInfos : " + fieldNodes);
                    BodyNode bodyNode = new BodyNode(childNode, fieldNodes);
                    bodyNodes.add(bodyNode);
                } else if (NodeName.HEADER.getValue().equals(childNode.getNodeName())) {
                    System.out.println("Header FieldInfos : " + fieldNodes);

                }

            }
        }
    }
}
