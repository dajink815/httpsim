package com.uangel.sim.scenario.nodes;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Node;

import java.util.List;

/**
 * @author dajin kim
 */
@Getter
@Slf4j
public class HeaderNode extends ChildNode {

    public HeaderNode(Node xmlNode, List<FieldNode> fieldNodes) {
        super(xmlNode);
        super.fieldNodes = fieldNodes;
    }

    @Override
    public String toString() {
        return "(fields=" + fieldNodes + ")";
    }
}
