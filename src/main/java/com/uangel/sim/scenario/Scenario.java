package com.uangel.sim.scenario;

import com.uangel.sim.command.CliInfo;
import com.uangel.sim.scenario.nodes.MsgNode;
import lombok.Data;

import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

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
    private int maxTrans;
    private final AtomicInteger transCnt = new AtomicInteger();

    private boolean endFlag;

    public Scenario(String name, List<MsgNode> msgNodes) {
        this.name = name;
        this.msgNodes = msgNodes;
    }

    public void setCmdInfo(CliInfo cmdInfo) {
        this.cmdInfo = cmdInfo;
        this.maxTrans = cmdInfo.getMaxTransCnt();
    }

    public void increaseTrans() {
        transCnt.incrementAndGet();
    }

    public boolean isTestEnd() {
        if (maxTrans <= 0) return true;
        return transCnt.get() >= maxTrans;
    }

    public int getCurTransCnt() {
        return transCnt.get();
    }
}
