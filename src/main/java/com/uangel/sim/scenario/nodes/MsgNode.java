package com.uangel.sim.scenario.nodes;

import com.uangel.sim.http.HttpMethodType;
import com.uangel.sim.scenario.type.AttrName;
import com.uangel.sim.scenario.type.NodeName;
import com.uangel.sim.scenario.xml.XmlParser;
import lombok.Getter;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.List;

/**
 * @author dajin kim
 */
@Getter
public class MsgNode extends XmlParser {

    private final HttpMethodType method;
    private final String url;
    private HeaderNode headerNode;
    private BodyNode bodyNode;

    public MsgNode(Node xmlNode) {
        super(xmlNode);

        String methodStr = getStrAttrValue(AttrName.METHOD.getValue());
        this.method = HttpMethodType.getMethodType(methodStr);
        this.url = getStrAttrValue(AttrName.URL.getValue());

        NodeList childNodes = xmlNode.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node childNode = childNodes.item(i);
            if (isElementNode(childNode)) {
                // Field List
                List<FieldNode> fieldNodes = createFieldInfos(childNode.getChildNodes());

                if (NodeName.BODY.getValue().equals(childNode.getNodeName())) {

                    if (bodyNode == null) {
                        //System.out.println("Body FieldInfos : " + fieldNodes);
                        bodyNode = new BodyNode(childNode, fieldNodes);
                    }

                } else if (NodeName.HEADER.getValue().equals(childNode.getNodeName())) {

                    if (headerNode == null) {
                        System.out.println("Header FieldInfos : " + fieldNodes);
                        headerNode = new HeaderNode(childNode);
                    }

                }

            }
        }
    }

    @Override
    public String toString() {
        if (headerNode == null)
            return "MSG(" + method + ", url=" + url
                    + ", body=" + bodyNode + ")";
        else
            return "MSG(" + method + ", url=" + url
                    + ", header=" + headerNode
                    + ", body=" + bodyNode + ")";
    }
}
