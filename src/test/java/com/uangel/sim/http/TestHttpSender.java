package com.uangel.sim.http;

import com.uangel.sim.command.CliInfo;
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

    public void testSend(String uri) {
        String reqBody = "{value:test}";
        String url = baseUrl.append(uri).toString();
        HttpResponse<String> response = send(url, reqBody);

        String resUri = response.uri().toString();

        log.debug("[HTTP CLIENT] received [{}({})] <- [{}]", resUri, response.statusCode(), response);
        log.debug("[HTTP CLIENT] Headers [{}]", response.headers().map());
        log.debug("[HTTP CLIENT] body [{}]", response.body());
    }


}
