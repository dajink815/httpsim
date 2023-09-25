package com.uangel.sim.http;

import com.uangel.sim.scenario.Scenario;
import com.uangel.sim.scenario.nodes.*;
import com.uangel.sim.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import spark.Request;
import spark.Response;

import java.util.Map;

import static spark.Spark.*;

/**
 * @author dajin kim
 */
@Slf4j
public class HttpBuilder {

    private HttpBuilder() {
        // nothing
    }

    // GET, POST, DELETE, PUT
    public static void makeGetRoute(MsgNode msgNode, Scenario scenario) {
        String uri = msgNode.getUri();
        log.info("[HTTP] Created GET method handler (uri:{})", uri);

        get(uri, (req, res) -> handleHttpMsg(msgNode, scenario, req, res));
    }

    public static void makePostRoute(MsgNode msgNode, Scenario scenario) {
        String uri = msgNode.getUri();
        log.info("[HTTP] Created POST method handler (uri:{})", uri);

        post(uri, (req, res) -> handleHttpMsg(msgNode, scenario, req, res));
    }

    public static void makeDeleteRoute(MsgNode msgNode, Scenario scenario) {
        String uri = msgNode.getUri();
        log.info("[HTTP] Created DELETE method handler (uri:{})", uri);

        delete(uri, (req, res) -> handleHttpMsg(msgNode, scenario, req, res));
    }

    public static void makePutRoute(MsgNode msgNode, Scenario scenario) {
        String uri = msgNode.getUri();
        log.info("[HTTP] Created PUT method handler (uri:{})", uri);

        put(uri, (req, res) -> handleHttpMsg(msgNode, scenario, req, res));
    }


    private static String handleHttpMsg(MsgNode msgNode, Scenario scenario, Request req, Response res) {
        BodyNode body = msgNode.getBodyNode();
        HeaderNode header = msgNode.getHeaderNode();

        log.debug("[HTTP] URI : {}", req.uri());
        log.debug("[HTTP] URL : {}", req.url());
        log.debug("[HTTP] Headers : {}", req.headers());
        log.debug("[HTTP] Body : {}", req.body());

        // Body Json 포맷만 고려
        Map<String, String> fieldsMap = JsonUtil.getAllJsonFields(req.body());

        // res 생성시 keyword 처리에 fieldsMap 사용

        int curTrans = scenario.getCurTransCnt();
        scenario.increaseTrans();
        log.debug("[HTTP] Prev:{} -> Cur:{}", curTrans, scenario.getCurTransCnt());

        // Header 처리
/*        if (header != null) {
        }*/

        // content-type : 기본 JSON
        res.type(HttpEnums.APPLICATION_JSON.getStr());
        // Status Code
        res.status(body.getStatusCode());

        // Body 처리
        String result = "";
        JSONObject jsonObject = JsonUtil.buildJsonMsg(body);
        if (!jsonObject.isEmpty())
            result = jsonObject.toJSONString();

        // Response body 전송 시
        // return Json ? res.body(Json) ?

        return result;
    }


}
