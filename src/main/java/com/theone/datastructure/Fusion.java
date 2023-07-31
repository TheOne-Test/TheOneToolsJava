package com.theone.datastructure;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.theone.utils.HttpClientUtils;
import org.apache.hc.core5.http.ParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author mhsu
 * @version 1.0
 * Description: 概率合成
 * Created 2023-07-08 15:23
 */
public class Fusion {

    HttpClientUtils client;
    Map<String, String> headers;
    String token;
    String url;
    public Fusion(HttpClientUtils client, String url, String token) {
        this.client = client;
        this.url = url;
        this.token = token;
        // 设置头
        headers = new HashMap<>();
        headers.put("Authorization", token);
    }

    /**
     * 合成活动详情
     * @param uuid 活动UUID
     * @return
     * @throws IOException
     * @throws ParseException
     * @throws InterruptedException
     */
    public JSONObject detail(String uuid) throws IOException, ParseException {
        String path = "/om-goods/api/fusion/detail";
        String body = String.format("{\"uuid\":\"%s\"}", uuid);

        JSONObject response = client.doPost(url + path, body, headers);
        return response;
    }

    /**
     * 获取指定数量的treasureSkuUuid列表
     * @param body 请求体
     * @param treasureSkuUuidNum 指定数量
     * @return
     * @throws IOException
     * @throws ParseException
     */
    public List<String> listMaterial(String body, Integer treasureSkuUuidNum) throws IOException, ParseException {
        if (treasureSkuUuidNum < 1) {
            throw new IOException("treasureSkuUuidNum 不能小于1");
        }

        String path = "/goods/api/treasureSku/listMaterial";
        JSONObject response = client.doPost(url+path, body, headers);
        JSONArray records = response.getJSONObject("data").getJSONArray("records");
        System.out.println("records = " + records);
        System.out.println("records.size() = " + records.size());
        if (records.size() < 1) {
            throw new IOException("无可用消耗物");
        }

        List<String> uuidList = new ArrayList<>();
        int listLen = Math.min(records.size(), treasureSkuUuidNum);
        for (int i=0; i<listLen; i++) {
            String uuid = records.getJSONObject(i).getString("id");
            uuidList.add(uuid);
        }
        return uuidList;
    }

    /**
     * 合成
     * @param body
     * @return
     * @throws IOException
     * @throws ParseException
     */
    public JSONObject fusion(String body) throws IOException, ParseException {
        String path = "/om-goods/api/fusion/fusion";
        JSONObject response = client.doPost(url + path, body, headers);
        return response;
    }

    public static void main(String[] args) throws IOException, ParseException {

        HttpClientUtils client = new HttpClientUtils();

        try {
//            String token = GetToken.getToken(client, "18867521753");
//            Fusion fusion = new Fusion(client, "https://qa-api.theone.art", token);
//
//            // 详情页
//            JSONObject dtl = fusion.detail("e5dbb9ea41a58cd4257f93fcbc58f07b");
//            System.out.println("dtl = " + dtl);
//
//            // 消耗物列表
//            String listMaterialBody = """
//                    {"pageCount":1,"pageSize":20,"commodityIdList":["98c20657a606d7fc738b640ace869291"]}
//                    """;
//            List<String> records = fusion.listMaterial(listMaterialBody, 3);
//
//            // 合成
//            String recordsJsonString = JSONObject.toJSONString(records);
//            String fusionBody = String.format("{\n" +
//                    "\t\"channelUuid\": \"3550449f99c1504e2e350663bd72e343\",\n" +
//                    "\t\"conditionList\": [{\n" +
//                    "\t\t\"conditionUuid\": \"44f280e2bd80ae58e90402a3f7db3395\",\n" +
//                    "\t\t\"treasureSkuUuids\": %s\n" +
//                    "\t}],\n" +
//                    "\t\"uuid\": \"e5dbb9ea41a58cd4257f93fcbc58f07b\"\n" +
//                    "}", recordsJsonString);
//
//            while (true) {
//                JSONObject fusionRes = fusion.fusion(fusionBody);
//                System.out.println("fusionRes = " + fusionRes);
//            }
            String url = "http://47.105.36.27";
            String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJVU0VSX0xPR0lOX1JFRElTX0tFWV9QQzpxUE1TTEl0Vzl3NHUwOEF2US80WWdnPT06MTIyLjIzNS44NS45OCIsImlhdCI6MTY4OTIzNjIzMSwiZXhwIjoxNjg5ODQxMDMxfQ.fqRZHlQ7rRWQtb9yFRmTAiYUgss9_Cmcy3p4RBrlJ0uq26kFMqWJpbSXFOE-py0ViqnZo1XBoLWNM1yNkjuOqQ";
            Fusion fusion = new Fusion(client, url, token);
            String body = "{\n" +
                    "    \"channelUuid\": \"e1f7c52454a72fe9306a82b2c0940f59\",\n" +
                    "    \"conditionList\": [\n" +
                    "        {\n" +
                    "            \"conditionUuid\": \"a8db18f9c7bf036f1d8b6df2acbb3298\",\n" +
                    "            \"treasureSkuUuids\": [\n" +
                    "                \"458463805213852d7ebfb271ca4398c9\"\n" +
                    "            ]\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"conditionUuid\": \"f38673a9fb83d484f47cae4082aa4fca\",\n" +
                    "            \"treasureSkuUuids\": [\n" +
                    "                \"d1e0eec4abd249fe222210a246dd8684\"\n" +
                    "            ]\n" +
                    "        }\n" +
                    "    ],\n" +
                    "    \"uuid\": \"72fe7b43cf33d18cc78a7580a1802439\"\n" +
                    "}";
            while (true) {
                JSONObject res = fusion.fusion(body);
                System.out.println("res = " + res);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            client.close();
        }

    }
}
