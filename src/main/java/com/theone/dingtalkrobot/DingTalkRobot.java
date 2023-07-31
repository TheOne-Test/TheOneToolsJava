package com.theone.dingtalkrobot;

import com.alibaba.fastjson.JSONObject;
import com.theone.utils.HttpClientUtils;
import org.apache.hc.core5.http.ParseException;

import java.io.IOException;

/**
 * @author mhsu
 * @version 1.0
 * Description:
 * Created 2023-07-18 17:21
 */
public class DingTalkRobot {

    HttpClientUtils client;

    public DingTalkRobot() {
        this.client = new HttpClientUtils();
    }

    /**
     * 测试群提醒：更新飞书文档【测试组任务状态】
     */
    public void updateTaskToast() {
        String url = "https://oapi.dingtalk.com/robot/send?access_token=518fe33e849298d6a88b9992c687cdb82f15db1a521f1350a6f2fc7ddb5a7411";
        String markdown = "{\n" +
                "     \"msgtype\": \"markdown\",\n" +
                "     \"markdown\": {\n" +
                "         \"title\":\"测试任务状态\",\n" +
                "         \"text\": \"## 请及时更新测试任务状态 @18867521753 \\n > #### [任务链接：https://zy5211r7q3.feishu.cn/base/bascnahD7T3IvJ9fIfs0U0EwFDf?table=tblzmHzrC9o7Lf40&view=vewiubvZmF](https://zy5211r7q3.feishu.cn/base/bascnahD7T3IvJ9fIfs0U0EwFDf?table=tblzmHzrC9o7Lf40&view=vewiubvZmF) \\n\"\n" +
                "     },\n" +
                "    \"at\": {\n" +
                "        \"atMobiles\": [\"18867521753\"],\n" +
                "        \"atUserIds\": [],\n" +
                "        \"isAtAll\": false\n" +
                "    }\n" +
                " }";
        try {
            JSONObject jsonRes = client.doPost(url, markdown);
            System.out.println("updateTaskToastResult = " + jsonRes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                client.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 测试群提醒：创建周会文档，预定周会会议室
     * @param phone 手机号
     */
    public void weeklyMeetingToast(String phone) {
        String url = "https://oapi.dingtalk.com/robot/send?access_token=518fe33e849298d6a88b9992c687cdb82f15db1a521f1350a6f2fc7ddb5a7411";
        String markdown = String.format("{\n" +
                "   \"msgtype\": \"markdown\",\n" +
                "   \"markdown\": {\n" +
                "       \"title\":\"测试任务状态\",\n" +
                "       \"text\": \"## 温馨提醒：创建周会文档，预定周会会议室 @%s \\n\"\n" +
                "   },\n" +
                "    \"at\": {\n" +
                "        \"atMobiles\": [\"%s\"],\n" +
                "        \"atUserIds\": [],\n" +
                "        \"isAtAll\": false\n" +
                "    }\n" +
                "}", phone, phone);
        try {
            JSONObject jsonRes = client.doPost(url, markdown);
            System.out.println("weeklyMeetingToastResult = " + jsonRes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                client.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
