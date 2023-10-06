package com.uangel.sim.scenario.screen;

import com.uangel.sim.scenario.Scenario;
import com.uangel.sim.scenario.ScenarioBuilder;
import org.junit.Test;
import org.xml.sax.SAXException;

import java.io.IOException;

/**
 * @author dajin kim
 */
public class ScenarioScreenTest {

    @Test
    public void printScreenTest() throws IOException, SAXException {
        String scenarioPath = "./src/test/resources/scenario/aica_basic.xml";
        Scenario scenario = ScenarioBuilder.fromXMLFileName(scenarioPath);

        assert scenario != null;
        ScenarioScreen screen = new ScenarioScreen(scenario);
        screen.printScreen();

    }
}
