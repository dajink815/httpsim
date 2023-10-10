package com.uangel.sim.scenario.screen;

import com.uangel.sim.command.CliInfo;
import com.uangel.sim.http.HttpMethodType;
import com.uangel.sim.scenario.Scenario;
import com.uangel.sim.scenario.nodes.MsgNode;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author dajin kim
 */
@Slf4j
public class ScenarioScreen {

    private final Scenario scenario;
    private final ScheduledExecutorService screenScheduledService;
    private final StringBuilder sb = new StringBuilder();
    private final long startTime;
    private final String scenarioName;
    private final CliInfo cliInfo;

    public ScenarioScreen(Scenario scenario) {
        this.scenario = scenario;
        this.screenScheduledService = Executors.newSingleThreadScheduledExecutor();
        this.startTime = System.currentTimeMillis();
        this.scenarioName = scenario.getName();
        this.cliInfo = scenario.getCliInfo();
    }

    public void run() {
        screenScheduledService.scheduleAtFixedRate(this::printScreen, 0, 1000, TimeUnit.MILLISECONDS);
    }

    public void stop() {
        if (screenScheduledService != null) {
            screenScheduledService.shutdown();
        }
    }
    public void printScreen() {
        List<MsgNode> msgNodeList = scenario.getMsgNodes();

        try {
            sb.setLength(0);

            long currentTime = System.currentTimeMillis();
            double totalTime = ((double) (currentTime - startTime)) / 1000;
            sb.append("----------------------------------- Scenario Screen -----------------------------------\n");
            sb.append(String.format("%18s%s%n", "Scenario Name : ", this.scenarioName));
            sb.append(String.format("%16s%s (Threads:%d) %n%n", "HTTP Server : ",
                    cliInfo.getHttpIp() + ":" + cliInfo.getHttpPort(), cliInfo.getThreadSize()));

            sb.append(String.format("%15s%s%n", "Total Time : ", totalTime));
            sb.append(String.format("%23s[%s/%s]%n%n", "Total Transactions : ", scenario.getCurTransCnt(), cliInfo.getMaxTransCnt()));

            sb.append(String.format("%10s %7s  URI %n", "Method", "ResCode"));

            for (int i = 1; i <= msgNodeList.size(); i++) {
                MsgNode msgNode = msgNodeList.get(i-1);
                HttpMethodType method = msgNode.getMethod();
                int statusCode = msgNode.getBodyNode().getStatusCode();
                String uri = msgNode.getUri();
                int transCnt = msgNode.getTransCnt();
                sb.append(String.format("%3d) %4s %6d   %s [%d]%n", i, method.name(), statusCode, uri, transCnt));
            }
            sb.append("---------------------------------------------------------------------------------------");

            System.out.print("\033[H\033[2J");
            System.out.flush();
            System.out.println(sb);
        } catch (Exception e) {
            log.warn("Unexpected Exception Occurs while Standard Out", e);
        }
    }



}
