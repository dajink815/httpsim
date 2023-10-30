package com.uangel.sim.http;

import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    protected HttpResponse<String> sendMultipart(String uriStr, String filePath, String reqBody) {
        try {
            URI uri = URI.create(uriStr);

            // Prepare the multipart/form-data request
            HttpRequest.Builder reqBuilder = HttpRequest.newBuilder(uri)
                    .timeout(Duration.ofSeconds(5))
                    .header(HttpEnums.CONTENT_TYPE.getStr(), HttpEnums.MULTIPART.getStr());

            // Set the file path of the file to be uploaded
            Path file = Paths.get(filePath);

            // Set Body
            HttpRequest request;
            if (reqBody != null) {
                request = reqBuilder
                        .POST(HttpRequest.BodyPublishers.ofString(reqBody))
                        .POST(HttpRequest.BodyPublishers.ofFile(file))
                        //.method("post",HttpRequest.BodyProcessor.fromString("foo"))
                        //.method("post", HttpRequest.BodyProcessor.fromFile(Paths.get("/path/to/your/file")))
                        .build();
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
