package com.uangel.sim.command;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;

/**
 * @author dajin kim
 */
@Slf4j
public class CliManager {

    private CliManager() {
        // nothing
    }

    public static CliInfo parseCommandLine(String[] args) {
        CommandLineParser parser = new DefaultParser();
        CliInfo cmdInfo;
        try {
            CommandLine cmd = parser.parse(CliInfo.createOptions(), args);
            cmdInfo = new CliInfo(cmd);
            if (cmd.hasOption("h") || (cmdInfo.getScenarioFile() == null)) {
                new HelpFormatter().printHelp("httpsim.jar [OPTIONS] (see -h options)", CliInfo.createOptions());
                return null;
            }

        } catch (Exception e) {
            log.error("ScenarioRunner.run.CliManager.Exception ",  e);
            new HelpFormatter().printHelp("httpsim.jar [OPTIONS] (see -h options)", CliInfo.createOptions());
            return null;
        }
        return cmdInfo;
    }
}
