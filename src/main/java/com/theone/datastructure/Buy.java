package com.theone.datastructure;

import com.alibaba.fastjson.JSONObject;
import com.theone.utils.FileUtils;
import com.theone.utils.HttpClientUtils;
import org.apache.hc.core5.http.ParseException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author mhsu
 * @version 1.0
 * Description:
 * Created 2023-06-27 16:14
 */
public class Buy {

    static String url = "https://qa-api.theone.art";

    public static String buyTreasure(HttpClientUtils client, String token, String commodityId, int num) throws IOException, ParseException, InterruptedException {


        // 设置头
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", token);
//        headers.put("Content-Type", "application/json");

        // 根据commodityUuid获取saleRecordUuid
        String saleRecordUuidUrl = url + "/market/api/saleRecord/list/v2";
        String saleRecordUuidJsonBody = String.format("{\n" +
                "    \"commodityId\": \"%s\",\n" +
                "    \"pageCount\": 1,\n" +
                "    \"pageSize\": 20\n" +
                "}", commodityId);
        JSONObject saleRecordUuidJson = client.doPost(saleRecordUuidUrl, saleRecordUuidJsonBody, headers);
        JSONObject record = saleRecordUuidJson.getJSONObject("data").getJSONArray("records").getJSONObject(0);
        String saleRecordUuid = record.getString("id");
        System.out.println("saleRecordUuid = " + saleRecordUuid);
        // 获取价格
//        System.out.println("price = " + record.getString("price"));
        float price = Float.parseFloat(record.getString("price"));
        int amountPay = (int) (price * 100 * num);
        System.out.println("amountPay = " + amountPay);

        // 根据saleRecordUuid获取orderNo
        String orderNoUrl = url + "/order/api/orderBuy/add";
        String orderNoJsonBody = String.format("[\n" +
                "    {\n" +
                "        \"saleRecordAmount\": %s,\n" +
                "        \"saleRecordId\": \"%s\",\n" +
                "        \"amountPay\": \"%s\"\n" +
                "    }\n" +
                "]", num, saleRecordUuid, amountPay);
        JSONObject orderNoJson = client.doPost(orderNoUrl, orderNoJsonBody, headers);
        String orderNo = orderNoJson.getString("data");
        System.out.println("orderNo = " + orderNo);

        // 购买
        String buyUrl = url + "/pay/api/v3/pay";
        String buyJsonBody = String.format("{\n" +
                "    \"orderNo\": \"%s\",\n" +
                "    \"payChannel\": \"balance\",\n" +
                "    \"clientType\": 1,\n" +
                "    \"payPw\": \"uunUa0tzpcBzZKios4nJhWV26OQPRk2nUu7GUCgyqJ3VVeECkERGte2izxrKV/9og17rfmpqlyVkTiqhqP636cOv8keVO4ZLfPwjct2VW/+tvPLw/Lvc3q1E1jYwzqEUGv2QsvZCDfwcmf60BFBbhYI+Ffzf1OieQSgwExiVqEqkgMxuYG99Emd0NJGv8471qY/Xc3u65nl3loGCALDzv/qWDOPzWwnHFOTMTs55cxtsly9b+GqSgSPjWJLeiGR0m7auXHUGCIZKusY1DnTWMgjxCJAPCF4bDJgYn88YkfMCYqBb98HU33Avh6qgNKGAaNQx+CbuesVShEmW+dmVFQ==\",\n" +
                "    \"amountPay\": \"%s\"\n" +
                "}", orderNo, amountPay);
        JSONObject buyJson = client.doPost(buyUrl, buyJsonBody, headers);
        System.out.println("buyJson = " + buyJson);

        Thread.sleep(5000);

        // 获取treasureSkuUuid
        String treasureSkuUuidUrl = url + "/goods/api/treasureSku/listMaterial";
        String treasureSkuUuidJsonBody = String.format("{\n" +
                "    \"commodityIdList\": [\"%s\"],\n" +
                "    \"pageCount\": 1,\n" +
                "    \"pageSize\": 20\n" +
                "}", commodityId);
        JSONObject treasureSkuUuidJson = client.doPost(treasureSkuUuidUrl, treasureSkuUuidJsonBody, headers);
        String treasureSkuUuid = treasureSkuUuidJson.getJSONObject("data").getJSONArray("records").getJSONObject(0).getString("id");
        System.out.println("treasureSkuUuid = " + treasureSkuUuid);
        return treasureSkuUuid;
    }

    public static void writeTokenAndTreasureSkuUuidToFile(String fileName, String phone, String token, String treasureSkuUuid) throws IOException {
        String content = phone + "," + token + "," + treasureSkuUuid + "\n";
        FileUtils.writeStr2File(fileName, content);
    }

    public static void main(String[] args) throws IOException, ParseException, InterruptedException {
        HttpClientUtils client = new HttpClientUtils();
        try {
            // 初始手机号
            long phone = 16170000001L;

            for (int i = 0; i < 2; i++) {
                String phoneStr = Long.toString(phone);
                System.out.println("phone = " + phoneStr);
                // 登录获取token
                String token = GetToken.getToken(client, phoneStr);
                // 获取treasureSkuUuid
                String treasureSkuUuid = buyTreasure(client, token, "8fbde021edadd3ad96a7befb0b4fcc02", 1);

                // 将token和treasureSkuUuid写入文件
                writeTokenAndTreasureSkuUuidToFile("/Users/suminghui/Desktop/test.csv", phoneStr, token, treasureSkuUuid);
                phone += 1;
                System.out.println("");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            client.close();
        }

    }
}
