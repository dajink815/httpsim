package com.uangel.sim.scenario.nodes;

import com.uangel.sim.scenario.xml.XmlParser;
import lombok.Getter;
import lombok.ToString;
import org.w3c.dom.Node;

import java.util.List;

/**
 * @author dajin kim
 */
@Getter
@ToString
public class ChildNode extends XmlParser {

    protected List<FieldNode> fieldNodes;

    public ChildNode(Node xmlNode) {
        super(xmlNode);
    }
}
