package ru.scrait.parser.utils;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONObject;

import java.io.IOException;

public class HttpUtils {

    public static String initParserOnBalancer(String ipPort, String query) {
        final CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        try {
            final HttpPost request = new HttpPost("http://" + query + "/initParser");

            final StringEntity params = new StringEntity(ipPort);
            //request.addHeader("content-type", "application/json");
            request.setEntity(params);
            final HttpResponse response = httpClient.execute(request);
            final String body = new String(response.getEntity().getContent().readAllBytes());
            try {
                httpClient.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return body;

        } catch (Exception ex) {
            return "ebal";
        }
    }

}
