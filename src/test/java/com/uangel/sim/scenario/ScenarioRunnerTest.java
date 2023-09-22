package com.uangel.sim.scenario;

import com.uangel.sim.command.CliManagerTest;
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

        f = CompletableFuture.runAsync(() -> new ScenarioRunner().run(cliList.toArray(new String[0])));

        Assert.assertNull(f.get());
    }

}
