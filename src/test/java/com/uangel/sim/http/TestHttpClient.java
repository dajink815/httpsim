package com.uangel.sim.http;

import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * @author dajin kim
 */
@Slf4j
public class TestHttpClient {

    private final HttpClient client = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .build();

    protected TestHttpClient() {
        // nothing
    }

    protected HttpResponse<String> send(String uri, String reqBody) {
        return send(URI.create(uri), reqBody);
    }

    protected HttpResponse<String> send(URI uri, String reqBody) {
        try {
            HttpRequest.Builder reqBuilder = HttpRequest.newBuilder(uri)
                    .timeout(Duration.ofSeconds(5))
                    .header(HttpEnums.CONTENT_TYPE.getStr(), HttpEnums.APPLICATION_JSON.getStr());

            // Set Body
            HttpRequest request;
            if (reqBody != null) {
                request = reqBuilder.POST(HttpRequest.BodyPublishers.ofString(reqBody)).build();
            } else {
                request = reqBuilder.build();   // Default Method : GET
            }

            log.debug("[HTTP CLIENT] send [{}] -> [{}]", request, reqBody);

            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            log.error("[HTTP CLIENT] TestHttpClient.Exception ", e);
        }

        return null;
    }

}
