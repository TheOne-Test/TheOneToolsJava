package com.theone.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.classic.methods.HttpUriRequest;
import org.apache.hc.client5.http.entity.mime.FileBody;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.*;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author mhsu
 * @version 1.0
 * Description:
 * Created 2023-06-21 11:40
 */
public class HttpClientUtils {

    private final CloseableHttpClient httpClient;
    public Map<String, String> headers;
//    private CloseableHttpResponse response;

    public HttpClientUtils() {
        httpClient = HttpClients.createDefault();
        headers = new HashMap<>();
    }

    public JSONObject doPost(String url, String jsonBody) throws IOException, ParseException {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(new StringEntity(jsonBody, ContentType.APPLICATION_JSON));
        return executeRequest(httpPost);
    }

    public JSONObject doPost(String url, String jsonBody, Map<String, String> headers) throws IOException, ParseException {
        HttpPost httpPost = new HttpPost(url);
        // 设置请求头
        if (headers != null) {
            for (Map.Entry<String, String> entry: headers.entrySet()) {
                httpPost.setHeader(entry.getKey(), entry.getValue());
            }
        }
        httpPost.setEntity(new StringEntity(jsonBody, ContentType.APPLICATION_JSON));
        return executeRequest(httpPost);
    }

    public JSONObject doPost(String url, String jsonBody, Map<String, String> headers, Map<String, String> parameters, File file) throws IOException, ParseException {
        HttpPost httpPost = new HttpPost(url);
        // 设置请求头
        if (headers != null) {
            for (Map.Entry<String, String> entry: headers.entrySet()) {
                httpPost.setHeader(entry.getKey(), entry.getValue());
            }
        }

        MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();

        // 设置文本参数
        if (parameters != null) {
            for (Map.Entry<String, String> entry: parameters.entrySet()) {
                entityBuilder.addTextBody(entry.getKey(), entry.getValue());
            }
        }

        // 设置文件参数
        if (file != null) {
            entityBuilder.addPart("file", new FileBody(file));
        }

        // 设置JSON请求体
        if (jsonBody != null) {
            entityBuilder.addTextBody("json", jsonBody, ContentType.APPLICATION_JSON);
        }

        httpPost.setEntity(entityBuilder.build());
        return executeRequest(httpPost);
    }

    private JSONObject executeRequest(HttpUriRequest request) throws IOException, ParseException {
        try (CloseableHttpResponse response = httpClient.execute(request)) {
            // 添加响应头信息
            headers.clear();
            Header[] allHeaders = response.getHeaders();
            for (Header header: allHeaders) {
                headers.put(header.getName(), header.getValue());
            }

            // 响应体
            HttpEntity entity = response.getEntity();
            String responseString = EntityUtils.toString(entity, StandardCharsets.UTF_8);
            JSONObject responseJson = JSON.parseObject(responseString);

            // 响应状态码
            int statusCode = response.getCode();
            if (statusCode >= 200 && statusCode < 300) {
                return responseJson;
            } else {
                throw new IOException("Request failed with status code: " + statusCode + ", response: " + responseString);
            }
        }
    }

    public void close() throws IOException {
        httpClient.close();
    }

    public static void main(String[] args) {
//        String url = "https://api.theone.art/goods/api/treasure/list/v2";
//        Map<String, String> headers = new HashMap<>();
//        headers.put("Authorization", "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJVU0VSX0xPR0lOX1JFRElTX0tFWV9QQzpaQTZmZWV3WDM5NzVTUmIxejJqYXJ3PT06MTIyLjIzMy4yMjYuMTUiLCJpYXQiOjE2ODczMTk5NDYsImV4cCI6MTY4NzkyNDc0Nn0.kGJrj5BogU707P9zd9Es_rKkppSuGK0zjjfZr6mOex0zXBCgefKAOiFQ4ogaWgB8BZKY_I8OYH6wxnr226KKWA");
//        headers.put("Content-Type", "application/json");
//        String jsonBody = "{\n" +
//                "    \"pageCount\": 1,\n" +
//                "    \"pageSize\": 16\n" +
//                "}";
//
//        HttpClientUtils client = new HttpClientUtils();
//        try {
//            JSONObject login = client.doPost(url, jsonBody, headers);
//            System.out.println("login = " + login);
//            System.out.println(client.headers);
//            System.out.println(login.getJSONObject("data").getInteger("total"));
//            System.out.println(login.getJSONObject("data").getJSONArray("records"));
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        } catch (ParseException e) {
//            throw new RuntimeException(e);
//        } finally {
//            try {
//                client.close();
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        }

        HttpClientUtils client = new HttpClientUtils();
        try {
            while (true) {
                String url = "http://saas-api.theone.art/om-goods/api/activityTemplate/detailV2";
                String jsonBody = "{\"key\": \"test0702\"}";
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJVU0VSX0xPR0lOX1JFRElTX0tFWV9QQzpaQTZmZWV3WDM5NzVTUmIxejJqYXJ3PT06MTIyLjIzMy4yNDAuMTA3IiwiaWF0IjoxNjg4MzU1NTg1LCJleHAiOjE2ODg5NjAzODV9.jYGjWMD19afsQfadFNyNKEwM9--5fE0fE5mZobKuo856_i-SJgy7m3hkJXCgSR8m8P7V_9K6W3lqLSOoui4oLw");
                JSONObject jsonObject = client.doPost(url, jsonBody, headers);
                System.out.println("jsonObject = " + jsonObject);
            }
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        } finally {
            try {
                client.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
