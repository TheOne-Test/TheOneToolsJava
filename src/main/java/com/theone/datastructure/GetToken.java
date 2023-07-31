package com.theone.datastructure;

import com.theone.utils.FileUtils;
import com.theone.utils.HttpClientUtils;
import org.apache.hc.core5.http.ParseException;

import java.io.IOException;

/**
 * @author mhsu
 * @version 1.0
 * Description:
 * Created 2023-06-26 20:13
 */
public class GetToken {
    /**
     * 根据手机号获取token。只支持固定url和固定验证码（000000）的账号
     * 批量修改验证码，可使用RedisUtils的 multiModifyLoginCode 方法
     * @param client HttpClients对象
     * @param phone 手机号
     * @return token
     * @throws IOException
     * @throws ParseException
     */
    public static String getToken(HttpClientUtils client, String phone) throws IOException, ParseException {
        String url = "https://qa-api.theone.art/auth/api/auth/authCodeLoginV2";
        String jsonBody = String.format("{\n" +
                "    \"phone\": \"%s\",\n" +
                "    \"authCode\": \"000000\",\n" +
                "    \"loginType\": 4\n" +
                "}", phone);
        // 登录
        client.doPost(url, jsonBody);
        return client.headers.get("Authorization");
    }

    public static void main(String[] args) throws IOException, ParseException {
        HttpClientUtils client = new HttpClientUtils();
        try {
            // 初始手机号
            long phone = 16170000001L;

            for (int i = 0; i < 2000; i++) {
                String token = GetToken.getToken(client, Long.toString(phone));
                System.out.println("token = " + token);
                String content = phone + "," + token + "\n";
                FileUtils.writeStr2File("/Users/suminghui/Desktop/token.csv", content);
                phone += 1;
                Thread.sleep(100);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            client.close();
        }
    }
}
