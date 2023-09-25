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

    private final StringBuilder baseUrl;

    public TestHttpSender(CliInfo cliInfo) {
        this.baseUrl = new StringBuilder("http://").append(cliInfo.getHttpIp())
                .append(":").append(cliInfo.getHttpPort()).append("/");
    }

    public void msgSend(String uri) {
        String reqBody = "{\"value\":\"test\"}";
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
