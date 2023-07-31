package com.theone.utils;

import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.Map;

/**
 * @author mhsu
 * @version 1.0
 * Description:
 * Created 2023-06-20 13:56
 */
public class RedisUtils {
    private final Jedis jedis;

    /**
     *
     * @param host 服务器地址
     * @param port 端口号
     */
    public RedisUtils(String host, int port) {
        jedis = new Jedis(host, port);
    }

    /**
     *
     * @param host 服务器地址
     * @param port 端口号
     * @param password 密码
     */
    public RedisUtils(String host, int port, String password) {
        // 连接redis
        jedis = new Jedis(host, port);
        // 鉴权
        jedis.auth(password);
    }

    /**
     *
     * @param host 服务器地址
     * @param port 端口号
     * @param password 密码
     * @param instanceId 实例ID，内网访问时需要
     */
    public RedisUtils(String host, int port, String password, String instanceId) {
        // 连接redis
        jedis = new Jedis(host, port);
        // 鉴权
        jedis.auth(instanceId + ":" + password);
    }

    /**
     * 选择数据库
     * @param index DB索引，从0开始
     * @return
     */
    public String selectDB(int index) {
        String select = jedis.select(index);
        return select;
    }

    /**
     * 批量修改登录验证码
     * @param phone 需要修改的第一个手机号
     * @param num 总修改数量
     * @param code 新验证码
     */
    public void multiModifyLoginCode(String phone, int num, String code) {
        final String key = "DEFAULT_ACCOUNT_REDIS_KEY_V2";
        long phoneNum = Long.parseLong(phone);
        Map<String, String> map = new HashMap<>();
        for (int i=1; i<=num; i++) {
            String field = Long.toString(phoneNum);
            String value = String.format("[\"art.theone.auth.biz.entity.dto.innerUser.InnerUserSaveDto\"," +
                    "{\"phone\":\"%s\",\"code\":\"%s\",\"remark\":\"0\"}]", field, code);
            System.out.println("field = " + field);
            System.out.println("value = " + value);
            map.put(field, value);
            jedis.hset(key, map);
            phoneNum += 1;
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void close() {
        jedis.close();
    }

    public static void main(String[] args) {
//        String host = "r-m5efki4ficetk7ky8kpd.redis.rds.aliyuncs.com";
//        int port = 6379;
//        String password = "psI6ny0528LDn5F3";
//        RedisUtils redisUtils = new RedisUtils(host, port, password);
//        redisUtils.selectDB(3);
////        String key01 = redisUtils.hGet("DEFAULT_ACCOUNT_REDIS_KEY_V2", "16170000001");
////        System.out.println("key01 = " + key01);
//
//        redisUtils.multiModifyLoginCode("16170000001", 5, "000000");
    }
}
