package com.uangel.sim.command;

import lombok.Getter;
import lombok.ToString;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

/**
 * @author dajin kim
 */
@Getter
@ToString
public class CliInfo {
    private static Options opts;

    // Service
    private String scenarioFile;

    // HTTP
    private String httpIp;
    private int httpPort;

    // Performance
    private int threadSize;
    private int maxTransCnt;

    public CliInfo(CommandLine cmd) {
        loadServiceConfig(cmd);
        loadHttpConfig(cmd);
        loadPerfConfig(cmd);
    }

    public void loadServiceConfig(CommandLine cmd) {
        this.scenarioFile = cmd.getOptionValue("sf");
    }

    public void loadHttpConfig(CommandLine cmd) {
        this.httpIp = cmd.getOptionValue("hh");
        this.httpPort = Integer.parseInt(cmd.getOptionValue("hp", "5672"));
    }

    public void loadPerfConfig(CommandLine cmd) {
        this.threadSize = Integer.parseInt(cmd.getOptionValue("ts", "10"));
        this.maxTransCnt = Integer.parseInt(cmd.getOptionValue("mt", "10"));
    }

    public static Options createOptions() {
        if(opts != null) return opts;
        opts = new Options();

        // Service
        opts.addOption(new Option("h", "display help text"));
        opts.addOption(Option.builder("sf").argName("scenario_file").hasArg().desc("The XML scenario file").build());

        // HTTP
        opts.addOption(Option.builder("hh").argName("http_host").hasArg().desc("Local HTTP server host").build());
        opts.addOption(Option.builder("hp").argName("http_port").hasArg().desc("Local HTTP server port").build());

        // Performance
        opts.addOption(Option.builder("ts").argName("thread_size").hasArg().desc("Thread size").build());
        opts.addOption(Option.builder("mt").argName("max_transactions").hasArg().desc("Max Transactions Number").build());

        return opts;
    }

}
