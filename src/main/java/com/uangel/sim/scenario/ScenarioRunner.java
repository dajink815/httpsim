package com.uangel.sim.scenario;

import com.uangel.sim.command.CliInfo;
import com.uangel.sim.command.CliManager;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @author dajin kim
 */
@Slf4j
public class ScenarioRunner {

    private Scenario scenario;
    private CliInfo cliInfo;
    // Executor
    // todo Schedule 일 필요? HTTP Server thread 설정 방법
    private ScheduledExecutorService executorService;

    public ScenarioRunner() {
        // nothing
    }

    public void run(String[] args) {

        // Parse Command Line
        cliInfo = CliManager.parseCommandLine(args);
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

            // Init HTTP Server
            // 파싱한 시나리오 정보 이용해 HTTP Handler 미리 준비


            String scenarioName = scenario.getName();
            scenario.setCmdInfo(cliInfo);
            log.info("[{}] Scenario Parsing Completed", scenarioName);

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
            scenario.setExecutorService(executorService);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // todo 테스트 종료 조건 필요
    public void stop(String reason) {
        if (scenario == null || scenario.isTestEnded()) return;
        scenario.setTestEnded(true);

        if (this.executorService != null) {
            List<Runnable> interruptedTask = this.executorService.shutdownNow();
            if (!interruptedTask.isEmpty())
                log.warn("Main ExecutorService was Terminated, RemainedTask: {}", interruptedTask.size());
        }

        log.info("Stop Scenario Runner ({})", reason);
    }

}
