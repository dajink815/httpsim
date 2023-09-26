package com.uangel.sim.scenario;

import com.uangel.sim.command.CliManagerTest;
import com.uangel.sim.http.TestHttpSender;
import com.uangel.sim.util.SleepUtil;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author dajin kim
 */
public class ScenarioRunnerTest extends CliManagerTest {

    private CompletableFuture<Void> f;

    @Test
    public void runTest() throws ExecutionException, InterruptedException {
        prepareOptions();

        ScenarioRunner runner = new ScenarioRunner();
        f = CompletableFuture.runAsync(() -> runner.run(cliList.toArray(new String[0])));

        SleepUtil.trySleep(3000);

        Scenario scenario = runner.getScenario();
        TestHttpSender httpSender = new TestHttpSender(scenario.getCliInfo());

        httpSender.apiMsgSend("/aica/v1.0/internal/SelectBotCodeApi");
        SleepUtil.trySleep(2000);

        httpSender.apiMsgSend("/aica/v1.0/internal/SelectVbSetApi");
        SleepUtil.trySleep(2000);

        httpSender.easyCmsMsgSend("/gateway/voice/uangelChannel/v1/start");
        SleepUtil.trySleep(2000);

        httpSender.easyCmsMsgSend("/gateway/voice/uangelChannel/v1/talk");
        SleepUtil.trySleep(2000);

        httpSender.easyCmsMsgSend("/gateway/voice/uangelChannel/v1/stop");
        SleepUtil.trySleep(1000);

        Assert.assertNull(f.get());
    }

}
