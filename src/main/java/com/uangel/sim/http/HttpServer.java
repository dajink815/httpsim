package com.uangel.sim.http;

import com.google.gson.Gson;
import com.uangel.sim.command.CliInfo;
import com.uangel.sim.scenario.Scenario;
import com.uangel.sim.scenario.nodes.MsgNode;
import lombok.extern.slf4j.Slf4j;
import spark.Spark;

import java.util.List;

/**
 * @author dajin kim
 */
@Slf4j
public class HttpServer {

    private final Scenario scenario;
    private final List<MsgNode> msgNodes;
    private final CliInfo cliInfo;

    public HttpServer(Scenario scenario, CliInfo cliInfo) {
        this.scenario = scenario;
        this.msgNodes = scenario.getMsgNodes();
        this.cliInfo = cliInfo;
    }

    public void init() {

        // Thread Pool
        int threadSize = cliInfo.getThreadSize() <= 0 ?
                Runtime.getRuntime().availableProcessors() : cliInfo.getThreadSize();

        log.info("[HTTP] Start HTTP Server (IP:{}, Port:{}, Thread:{})",
                cliInfo.getHttpIp(), cliInfo.getHttpPort(), threadSize);

        // Load Server
        Spark.ipAddress(cliInfo.getHttpIp());
        Spark.port(cliInfo.getHttpPort());
        Spark.threadPool(threadSize);
        Spark.exception(Exception.class, (exception, req, res) -> {
            log.error("[HTTP] Err Occurs while handling HTTP Request - ({}) Body: {}\r\ne=", req.uri(), req.body(), exception);
            res.status(500);
            res.type("application/json");
            res.body(new Gson().toJson("Internal Server Error"));
        });
        Spark.notFound((req, res) -> {
            log.warn("[HTTP] Cannot Handle HTTP Request - ({}) Body: {}", req.uri(), req.body());
            res.status(404);
            res.type("application/json");
            res.body(new Gson().toJson("Not Found"));
            return "Not Found";
        });
        // timeoutMillis

        /*
        * Secure (HTTPS/SSL)
        * You can set the connection to be secure via the secure() method.
        * This has to be done before any route mapping:
        * secure(keystoreFilePath, keystorePassword, truststoreFilePath, truststorePassword);Copy
        * */

        // Ready Handler - 메시지 하나 당 하나의 handler 등록
        for (MsgNode msgNode : msgNodes) {
            if (HttpMethodType.GET.equals(msgNode.getMethod())) {
                HttpBuilder.makeGetRoute(msgNode, scenario);
            } else if (HttpMethodType.POST.equals(msgNode.getMethod())) {
                HttpBuilder.makePostRoute(msgNode, scenario);
            } else if (HttpMethodType.DELETE.equals(msgNode.getMethod())) {
                HttpBuilder.makeDeleteRoute(msgNode, scenario);
            } else if (HttpMethodType.PUT.equals(msgNode.getMethod())) {
                HttpBuilder.makePutRoute(msgNode, scenario);
            }
         }

    }

    public void stop() {
        log.info("[HTTP] Stop HTTP Server (IP:{}, Port:{})", cliInfo.getHttpIp(), cliInfo.getHttpPort());
        Spark.stop();
    }

}
