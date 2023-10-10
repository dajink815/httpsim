package com.uangel.sim.scenario.screen;

import com.uangel.sim.command.CliInfo;
import com.uangel.sim.command.CliManager;
import com.uangel.sim.command.CliManagerTest;
import com.uangel.sim.scenario.Scenario;
import com.uangel.sim.scenario.ScenarioBuilder;
import org.junit.Test;
import org.xml.sax.SAXException;

import java.io.IOException;

/**
 * @author dajin kim
 */
public class ScenarioScreenTest extends CliManagerTest {

    @Test
    public void printScreenTest() throws IOException, SAXException {
        prepareOptions();
        CliInfo cliInfo = CliManager.parseCommandLine(cliList.toArray(new String[0]));

        String scenarioPath = "./src/test/resources/scenario/aica_basic.xml";
        Scenario scenario = ScenarioBuilder.fromXMLFileName(scenarioPath);
        assert scenario != null;
        scenario.setCliInfo(cliInfo);

        ScenarioScreen screen = new ScenarioScreen(scenario);
        screen.printScreen();

    }
}
