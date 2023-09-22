package com.uangel.sim.scenario;

import com.uangel.sim.command.CliInfo;
import com.uangel.sim.command.CliManager;
import com.uangel.sim.http.HttpServer;
import com.uangel.sim.util.SleepUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @author dajin kim
 */
@Slf4j
public class ScenarioRunner {

    private Scenario scenario;
    private HttpServer httpServer;

    // Executor
    // todo Schedule 일 필요? HTTP Server thread 설정 방법
    private ScheduledExecutorService executorService;

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

            scenario.setCmdInfo(cliInfo);

            // Init HTTP Server
            // 파싱한 시나리오 정보 이용해 HTTP Handler 미리 준비
            httpServer = new HttpServer(scenario, cliInfo);
            httpServer.init();

            // set ScenarioRunner
            scenario.setScenarioRunner(this);

            // Keyword


            Thread.currentThread().setName(scenarioName);

            // Thread Pool
            int threadSize = cliInfo.getThreadSize() <= 0 ?
                    Runtime.getRuntime().availableProcessors() : cliInfo.getThreadSize();
/*            this.executorService = new Sche(threadSize,
                    new BasicThreadFactory.Builder()
                            .namingPattern(scenarioName + "-%d")
                            .priority(Thread.MAX_PRIORITY)
                            .build());*/
            log.info("[{}] Scenario Runner Start (CorePool:{})", scenarioName, threadSize);
            //scenario.setExecutorService(executorService);

            while (!scenario.isEndFlag()) {
                if (scenario.isTestEnd()) {
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
        scenario.setEndFlag(true);

        if (httpServer != null)
            httpServer.stop();

/*        if (this.executorService != null) {
            List<Runnable> interruptedTask = this.executorService.shutdownNow();
            if (!interruptedTask.isEmpty())
                log.warn("Main ExecutorService was Terminated, RemainedTask: {}", interruptedTask.size());
        }*/

        log.info("Stop Scenario Runner ({})", reason);
    }

}
