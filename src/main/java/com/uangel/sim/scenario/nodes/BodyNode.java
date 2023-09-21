package com.uangel.sim.scenario.nodes;

import com.uangel.sim.scenario.type.AttrName;
import com.uangel.sim.scenario.xml.XmlParser;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Node;

import java.util.List;

/**
 * @author dajin kim
 */
@Getter
@Slf4j
@ToString
public class BodyNode extends XmlParser {

    private final int statusCode;
    private final List<FieldNode> fieldNodes;
    // 중첩구조?

    public BodyNode(Node xmlNode, List<FieldNode> fieldNodes) {
        super(xmlNode);
        this.fieldNodes = fieldNodes;

        this.statusCode = getIntAttrWithDefault(AttrName.STATUS.getValue(), 200);
    }
}
