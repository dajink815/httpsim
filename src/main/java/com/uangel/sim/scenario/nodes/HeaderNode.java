package com.uangel.sim.scenario.nodes;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Node;

/**
 * @author dajin kim
 */
@Getter
@Slf4j
public class HeaderNode extends ChildNode {

    public HeaderNode(Node xmlNode) {
        super(xmlNode);
    }

    @Override
    public String toString() {
        return "(fields=" + fieldNodes + ")";
    }
}
