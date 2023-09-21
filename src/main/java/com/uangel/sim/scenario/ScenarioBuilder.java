package com.uangel.sim.scenario;

import com.uangel.sim.scenario.nodes.MsgNode;
import com.uangel.sim.scenario.type.AttrName;
import com.uangel.sim.scenario.type.NodeName;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author dajin kim
 */
@Slf4j
public class ScenarioBuilder {
    public static final DocumentBuilder DOCUMENT_BUILDER = getDefaultDocumentBuilder();

    private ScenarioBuilder() {
        // nothing
    }

    // 파일 이름
    public static Scenario fromXMLFileName(String filename) throws SAXException, IOException {
        if (DOCUMENT_BUILDER == null) return null;

        File fXmlFile = new File(filename);
        Document doc;
        synchronized (DOCUMENT_BUILDER) {
            doc = DOCUMENT_BUILDER.parse(fXmlFile);
        }
        return fromXMLDocument(doc);
    }

    public static Scenario fromXMLDocument(Document doc) {
        Element scenarioEle = doc.getDocumentElement();
        NamedNodeMap attr = scenarioEle.getAttributes();
        Node nameAttr = attr.getNamedItem(AttrName.NAME.getValue());
        String name = "Unnamed Scenario";
        if (nameAttr != null) {
            name = nameAttr.getTextContent();
        }

        List<MsgNode> msgNodeList = new ArrayList<>();

        for (Node m = scenarioEle.getFirstChild(); m != null; m = m.getNextSibling()) {
            if (NodeName.MSG.getValue().equals(m.getNodeName())) {
                msgNodeList.add(new MsgNode(m));
            } else if (m.getNodeType() == Node.ELEMENT_NODE){
                log.warn("ScenarioBuilder - Check NodeName [{}]", m.getNodeName());
            }
        }

        return new Scenario(name, msgNodeList);
    }

    private static DocumentBuilder getDefaultDocumentBuilder() {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            dbFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            dbFactory.setValidating(true);
            dbFactory.setNamespaceAware(true);
            dbFactory.setFeature("http://xml.org/sax/features/namespaces", false);
            dbFactory.setFeature("http://xml.org/sax/features/validation", false);
            dbFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
            dbFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
            dbFactory.setIgnoringElementContentWhitespace(true);
            dbFactory.setIgnoringComments(true);
            dbFactory.setCoalescing(true);
            return dbFactory.newDocumentBuilder();
        } catch (Exception e) {
            log.warn("Fail to build DocumentBuilder", e);
            return null;
        }
    }
}
