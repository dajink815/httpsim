package com.uangel.sim.scenario.nodes;

import com.uangel.sim.scenario.type.AttrName;
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
    // 중첩구조?

    public BodyNode(Node xmlNode, List<FieldNode> fieldNodes) {
        super(xmlNode);
        super.fieldNodes = fieldNodes;

        this.statusCode = getIntAttrWithDefault(AttrName.STATUS.getValue(), 200);
    }

    @Override
    public String toString() {
        return "(code:" + statusCode + ", fields=" + fieldNodes + ")";
    }
}
