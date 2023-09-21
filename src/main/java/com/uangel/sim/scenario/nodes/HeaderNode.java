package com.uangel.sim.scenario.nodes;

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
public class HeaderNode extends XmlParser {

    private List<FieldNode> fieldNodes;

    public HeaderNode(Node xmlNode) {
        super(xmlNode);
    }
}
