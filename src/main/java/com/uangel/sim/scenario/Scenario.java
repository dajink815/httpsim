package com.uangel.sim.scenario;

import com.uangel.sim.command.CliInfo;
import com.uangel.sim.scenario.nodes.MsgNode;
import lombok.Data;

import java.util.List;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @author dajin kim
 */
@Data
public class Scenario {

    private final String name;
    private final List<MsgNode> msgNodes;

    private CliInfo cmdInfo;
    private ScheduledExecutorService executorService;
    private ScenarioRunner scenarioRunner;

    private boolean isTestEnded;

    public Scenario(String name, List<MsgNode> msgNodes) {
        this.name = name;
        this.msgNodes = msgNodes;
    }
}
