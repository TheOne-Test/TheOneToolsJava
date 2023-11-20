package com.theone.utils;

import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * @author mhsu
 * @version 1.0
 * Description:
 * Created 2023-06-20 16:27
 */
public class RSAUtils {

    /**
     * RSA公钥加密
     *
     * @param str       加密字符串
     * @param publicKey 公钥
     * @return 密文
     * @throws Exception 加密过程中的异常信息
     */
    public static String encrypt(String str, String publicKey) throws Exception {
        //base64编码的公钥,    用base64处理下主要是将字符串内的不可见字符转换成可见字符，防止不同机器处理错误
        byte[] decoded = Base64.getDecoder().decode(publicKey);
        RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
        //RSA加密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        //这里没用像解密  new String  主要还是这个是要传输的，所以用base64编码的，防止错误
        String outStr = Base64.getEncoder().encodeToString(cipher.doFinal(str.getBytes("UTF-8")));
        return outStr;
    }

    public static void main(String[] args) {
        String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAzfNjtXLDyN4yt9gz3yepU3thRLHjWeLyC1aCovbd8Zoh9a5gzXiucoVEqf1ITmPikW8+qurimRQsP6t/zEs4cOKQhdKZVRAKNipP2BtRgeHdsypiIxof9ixL1Qa/nmaT4ACGbB54Dx1CPtBTENAEMkVwe6WFPp983l3jUAxp95BmbETiIVAXjedfivvW+rdbZlQgG1a2K7ve0KZxRideq9peSosFiAmJ6la9zWqU8vxXbgpA98AXa93YAQQ1i1kfMp8Hz7TuzpzNz54Bztf6vZa9zO2g9Tk5X6Kx/5Ky02bS47DL55irdmb32UTi6xzdZRzcNppX3VdahlgLY4CuWQIDAQAB";
        try {
            String encrypt = encrypt("000000", publicKey);
            System.out.println("encrypt = " + encrypt);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
