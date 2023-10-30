package com.uangel.sim.http;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author dajin kim
 */
@Slf4j
public class TestApacheHttpClient {

    private static RequestConfig reqConfig = RequestConfig.custom().setConnectTimeout(5000)
            .setSocketTimeout(5000).build();

    // Send MultipartMsg
    public String sendMultipart(String reqMsg, String url, String filePath, String fileName) {
        //String filePath = config.getMemoFilePath();
        //String fileName = callInfo.getBizRecoInfo().getFileName();
        String resBody = "";
        try (CloseableHttpClient client = HttpClients.custom().setDefaultRequestConfig(reqConfig).build()) {
            HttpPost postRequest = new HttpPost(url);
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            // JSON
            builder.addTextBody("jsonData", reqMsg, ContentType.APPLICATION_JSON);
            // 녹취파일
            byte[] audioData = Files.readAllBytes(Path.of(filePath).resolve(Path.of(fileName)));
            builder.addBinaryBody("fileMixData", audioData, ContentType.create("audio/mpeg"), fileName);
            // postRequest Entity 추가
            HttpEntity entity = builder.build();
            postRequest.setEntity(entity);
           // log.debug("[HTTP CLIENT] send [{}] -> [{}]", postRequest, reqMsg);
            return sendResponse(postRequest, client);
        } catch (Exception e) {
            log.warn("Failed to sendHTTPRequest Multipart [{}] handle", url, e);
        }

        return resBody;
    }

    // Send PostRequest Receive Response
    public String sendResponse(HttpPost postRequest, CloseableHttpClient client) {
        String resBody = "";
        try {
            // Request
            String reqBody = EntityUtils.toString(postRequest.getEntity());
            log.info("[HTTP CLIENT] Send Request: [{}] [{}]", postRequest, reqBody);
            // Response
            HttpResponse response = client.execute(postRequest);
            resBody = EntityUtils.toString(response.getEntity());
            log.info("[HTTP CLIENT] Recv Response: [{}] [{}]", response, resBody);
        } catch (Exception e) {
            log.warn("Failed to sendHTTPResponse", e);
        }
        return resBody;
    }

}
