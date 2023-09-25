package com.uangel.sim.scenario;

import com.uangel.sim.command.CliInfo;
import com.uangel.sim.command.CliManager;
import com.uangel.sim.http.HttpServer;
import com.uangel.sim.util.SleepUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;


/**
 * @author dajin kim
 */
@Slf4j
@Getter
public class ScenarioRunner {

    private Scenario scenario;
    private HttpServer httpServer;

    public ScenarioRunner() {
        // nothing
    }

    public void run(String[] args) {

        // Parse Command Line
        CliInfo cliInfo = CliManager.parseCommandLine(args);
        if (cliInfo == null) {
            return;
        }

        try {
            // Build Scenario
            scenario = ScenarioBuilder.fromXMLFileName(cliInfo.getScenarioFile());
            if (scenario == null) {
                log.error("ScenarioRunner Scenario Build Fail");
                return;
            }

            String scenarioName = scenario.getName();
            log.info("[{}] Scenario Parsing Completed", scenarioName);
            log.info("Parse Scenario Success [{}]", scenario);

            scenario.setCliInfo(cliInfo);

            // Init HTTP Server
            // 파싱한 시나리오 정보 이용해 HTTP Handler 미리 준비
            httpServer = new HttpServer(scenario, cliInfo);
            httpServer.init();

            // set ScenarioRunner
            scenario.setScenarioRunner(this);

            // Keyword


            Thread.currentThread().setName(scenarioName);

            while (!scenario.isEndFlag()) {
                int maxTransaction = scenario.getMaxTrans();
                int transCnt = scenario.getCurTransCnt();
                if (maxTransaction <= 0 || transCnt >= maxTransaction) {
                    log.info("Max Transaction count is over (max:{}, curTrans:{})", maxTransaction, transCnt);
                    stop("Scenario Ended");
                } else {
                    SleepUtil.trySleep(500);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stop(String reason) {
        if (scenario == null || scenario.isEndFlag()) return;
        log.info("Stop Scenario Runner ({})", reason);

        scenario.setEndFlag(true);

        if (httpServer != null)
            httpServer.stop();

    }

}
