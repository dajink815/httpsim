package com.uangel.sim.http;

import com.uangel.sim.scenario.Scenario;
import com.uangel.sim.scenario.nodes.*;
import com.uangel.sim.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;

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



    }

    public static void makePostRoute(MsgNode msgNode, Scenario scenario) {
        String uri = msgNode.getUri();
        BodyNode body = msgNode.getBodyNode();
        HeaderNode header = msgNode.getHeaderNode();
        log.info("[HTTP] Created POST method handler (uri:{})", uri);

        post(uri, (req, res) -> {
            log.debug("[HTTP] URI : {}", req.uri());
            log.debug("[HTTP] URL : {}", req.url());
            log.debug("[HTTP] Headers : {}", req.headers());
            log.debug("[HTTP] Body : {}", req.body());

            int curTrans = scenario.getCurTransCnt();
            scenario.increaseTrans();
            log.debug("[HTTP] Prev:{} -> Cur:{}", curTrans, scenario.getCurTransCnt());

            // 기본 JSON header
            res.header(HttpEnums.CONTENT_TYPE.getStr(), HttpEnums.APPLICATION_JSON.getStr());
            if (header != null) {
                // Header 처리
            }

            // Body 처리
            String result = "";
            JSONObject jsonObject = JsonUtil.buildJsonMsg(body);
            if (!jsonObject.isEmpty())
                result = jsonObject.toJSONString();

            return result;
        });
    }


}
