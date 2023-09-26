package com.uangel.sim.scenario.nodes;

import com.uangel.sim.scenario.type.AttrName;
import com.uangel.sim.scenario.xml.XmlParser;
import com.uangel.sim.util.StringUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Node;

import java.util.List;

/**
 * @author dajin kim
 */
@Getter
@Slf4j
public class BodyNode extends ChildNode {

    private final int statusCode;
    private String nodeValue = null;
    // 중첩구조?

    public BodyNode(Node xmlNode, List<FieldNode> fieldNodes) {
        super(xmlNode);
        super.fieldNodes = fieldNodes;

        this.statusCode = getIntAttrWithDefault(AttrName.STATUS.getValue(), 200);

        String rawMessage = xmlNode.getFirstChild().getTextContent();
        if (!rawMessage.isBlank() && nodeValue == null) {
            nodeValue = XmlParser.stripWhitespace(rawMessage);
            //System.out.println("Body Node Node Value : " + nodeValue);
        }
    }

    @Override
    public String toString() {
        if (StringUtil.isNull(nodeValue))
            return "(code:" + statusCode + ", fields=" + fieldNodes + ")";
        else
            return "(code:" + statusCode + ", fields=" + fieldNodes + ", bodyValue=" + nodeValue + ")";
    }
}
