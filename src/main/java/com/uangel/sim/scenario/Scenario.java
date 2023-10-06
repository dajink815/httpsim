package com.uangel.sim.scenario;

import com.uangel.sim.command.CliInfo;
import com.uangel.sim.scenario.handler.KeywordMapper;
import com.uangel.sim.scenario.nodes.MsgNode;
import lombok.Data;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author dajin kim
 */
@Data
public class Scenario {

    private final String name;
    private final List<MsgNode> msgNodes;
    private final List<String> uriList;

    private CliInfo cliInfo;
    private ScenarioRunner scenarioRunner;
    private KeywordMapper keywordMapper;

    private int maxTrans;
    private final AtomicInteger transCnt = new AtomicInteger();

    private boolean endFlag;

    public Scenario(String name, List<MsgNode> msgNodes) {
        this.name = name;
        this.msgNodes = msgNodes;
        this.uriList = msgNodes.stream().map(MsgNode::getUri).collect(Collectors.toList());
    }

    public void setCliInfo(CliInfo cliInfo) {
        this.cliInfo = cliInfo;
        this.maxTrans = cliInfo.getMaxTransCnt();
    }

    public void increaseTrans() {
        transCnt.incrementAndGet();
    }

    public int getCurTransCnt() {
        return transCnt.get();
    }
}
