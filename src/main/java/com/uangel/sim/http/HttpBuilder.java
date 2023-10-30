package com.uangel.sim.http;

import com.uangel.sim.scenario.Scenario;
import com.uangel.sim.scenario.nodes.*;
import com.uangel.sim.scenario.type.FieldType;
import com.uangel.sim.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.json.simple.JSONObject;
import spark.Request;
import spark.Response;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.apache.http.client.methods.HttpPost;

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

        int curTotalTrans = scenario.getCurTransCnt();
        scenario.increaseTrans();

        int curUriTrans = msgNode.getTransCnt();
        msgNode.increaseCnt();

        log.debug("[HTTP] Recv Request - ({}) Body: {} (Total:{}->{}) (UriCnt:{}->{})",
                req.uri(), req.body(), curTotalTrans, scenario.getCurTransCnt(), curUriTrans, msgNode.getTransCnt());

        Map<String, String> fieldsMap;
        if (req.contentType().equals(HttpEnums.APPLICATION_JSON.getStr())) {
            fieldsMap = JsonUtil.getAllJsonFields(req.body());
        }
        // JSON 포맷 외의 Body 도 처리
        else {
            try {
                log.debug("[HTTP] ContentType: [{}]", req.headers(HttpEnums.CONTENT_TYPE.getStr()));
                // JSON 추출
                String bodyStr = req.body();
                int startIndex = bodyStr.indexOf("{");
                int endIndex = bodyStr.lastIndexOf("}");
                String jsonMessage = bodyStr.substring(startIndex, endIndex + 1);
                log.debug("[HTTP] Json: [{}]", jsonMessage);
                fieldsMap = JsonUtil.getAllJsonFields(jsonMessage);
            } catch (Exception e) {
                log.error("[HTTP] HttpBuilder Parse Json Exception", e);
                fieldsMap = Collections.emptyMap();
            }
        }

        // Header 처리
        if (header != null) {
            List<FieldNode> headerFields = header.getFieldNodes();
            for (FieldNode fieldNode : headerFields) {
                try {
                    if (FieldType.STR.equals(fieldNode.getType()))  {
                        log.debug("[HTTP] Add Header - name:{}, value:{}", fieldNode.getName(), fieldNode.getValue());
                        res.header(fieldNode.getName(), fieldNode.getValue());
                    } else {
                        log.info("[HTTP] Check Header Field Type - name:{}, value:{}, type:{}",
                                fieldNode.getName(), fieldNode.getValue(), fieldNode.getType());
                    }
                } catch (Exception e) {
                    log.error("[HTTP] HttpBuilder.handleHttpMsg.Exception - Check Header Field ({})", fieldNode, e);
                }
            }
        }

        // content-type : 기본 JSON
        res.type(HttpEnums.APPLICATION_JSON.getStr());
        // Status Code
        res.status(body.getStatusCode());

        // Body 처리
        String result = "";
        JSONObject jsonObject = JsonUtil.buildJsonMsg(body, fieldsMap, scenario.getKeywordMapper());
        if (!jsonObject.isEmpty()) {
            result = jsonObject.toJSONString();
            log.debug("[HTTP] Response Body: {}", JsonUtil.buildPretty(result));
        }

        // Response body 전송 시
        // return Json ? res.body(Json) ?

        return result;
    }

    // spark.Request -> org.apache.http.client.methods.HttpPost 변환
    public static HttpPost convertRequestToHttpPost(Request request, String endpoint) {
        HttpPost httpPost = new HttpPost(endpoint);

        // HTTP 요청 헤더 설정
        request.headers().forEach(header -> httpPost.setHeader(header, request.headers(header)));

        // JSON 데이터 추출
        String body = request.body();

        // JSON 데이터를 HttpEntity로 변환
        HttpEntity entity = new StringEntity(body, ContentType.APPLICATION_JSON);

        // HttpEntity를 HttpPost에 설정
        httpPost.setEntity(entity);

        return httpPost;
    }


}
