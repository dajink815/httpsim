package com.uangel.sim.scenario;

import org.junit.Test;
import org.xml.sax.SAXException;

import java.io.IOException;

/**
 * @author dajin kim
 */
public class ScenarioBuilderTest {

    @Test
    public void buildScenario() throws IOException, SAXException {
        String scenarioPath = "./src/test/resources/scenario/easy_cms_basic.xml";
        Scenario scenario = ScenarioBuilder.fromXMLFileName(scenarioPath);
        System.out.println(scenario);
    }


}
