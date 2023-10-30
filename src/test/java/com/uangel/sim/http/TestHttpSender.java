package com.uangel.sim.http;

import com.uangel.sim.command.CliInfo;
import com.uangel.sim.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;

import java.net.http.HttpResponse;

/**
 * @author dajin kim
 */
@Slf4j
public class TestHttpSender extends TestHttpClient {

    private final TestApacheHttpClient apacheHttpClient = new TestApacheHttpClient();
    private final StringBuilder baseUrl;

    public TestHttpSender(CliInfo cliInfo) {
        this.baseUrl = new StringBuilder("http://").append(cliInfo.getHttpIp())
                .append(":").append(cliInfo.getHttpPort());
    }

    public void apiMsgSend(String uri) {
        String reqBody = "{\n" +
                "\"token\":\"eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJlYXN5Q01TIiwicm9sZSI6IkFQSSIsImlhdCI6MTYyMzczMjU3NCwiZXhwIjo0Nzc3MzMyNTc0fQ.JFf9V-2TiK0I925h59VO0nBjMyG05hSS7-NIYDvVqLy2jZyX-6ngYS6FaZ7xEKFggIXiJkp-14rSe4F07xjS1Q\",\n" +
                "\"svcNo\":\"0211111111\"\n" +
                "}";
        msgSend(uri, reqBody);
    }

    public void easyCmsMsgSend(String uri) {
        String reqBody = "{\n" +
                "\"value\":\"test\",\n" +
                "\"sessionKey\":\"0211111111\"\n" +
                "}";
        msgSend(uri, reqBody);
    }

    public void bizRecoSend(String filePath, String fileName) {
        String url = buildUrl("/recs/record/aimemo/upload_1_2");
        String reqBody = "{\n" +
                "\"value\":\"test\",\n" +
                "\"reqNo\":\"20200214123450A001\",\n" +
                "\"sessionKey\":\"0211111111\"\n" +
                "}";

        String body = apacheHttpClient.sendMultipart(reqBody, url, filePath, fileName);
        log.debug("[HTTP CLIENT] received Body {}", body);
    }

    public void msgSend(String uri, String reqBody) {
        String url = buildUrl(uri);

        HttpResponse<String> response = send(url, reqBody);

        log.debug("[HTTP CLIENT] received {}", response);
        log.debug("[HTTP CLIENT] Headers [{}]", response.headers().map());

        String body = response.body();
        if (JsonUtil.isJsonStr(body))
            log.debug("[HTTP CLIENT] body [{}]", JsonUtil.buildPretty(body));
        else
            log.debug("[HTTP CLIENT] body [{}]", body);
    }

    private String buildUrl(String uri) {
        return this.baseUrl.toString() + uri;
    }


}
