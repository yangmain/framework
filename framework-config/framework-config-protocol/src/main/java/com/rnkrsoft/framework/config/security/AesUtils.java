package com.rnkrsoft.framework.config.security;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;

@Slf4j
public class AesUtils {
    /**
     * 加密
     *
     * @param content  需要加密的内容
     * @param password 加密密码
     * @return 加密的字节数组
     */
    public static byte[] encrypt(byte[] content, String password) {
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            //需要对密码进行SHA填充
            SecureRandom random=SecureRandom.getInstance("SHA1PRNG");
            random.setSeed(password.getBytes("UTF-8"));
            kgen.init(128, random);
            SecretKey secretKey = kgen.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
            Cipher cipher = Cipher.getInstance("AES");// 创建密码器
            cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化
            byte[] result = cipher.doFinal(content);
            return result; // 加密
        } catch (Exception e) {
            log.error("使用AES加密发生错误", e);
            return null;
        }
    }

    /**
     * 加密
     * @param plaintext 需要加密的内容
     * @param password 密码
     * @param encoding 编码集
     * @return 加密的字符串
     */
    public static String encrypt(String plaintext, String password, String encoding) {
        byte[] input = new byte[0];
        try {
            input = Base64.encodeBase64(plaintext.getBytes(encoding));
        } catch (UnsupportedEncodingException e) {
            return null;
        }
        byte[] results = encrypt(input, password);
        byte[] base64bytes = Base64.encodeBase64(results);
        String ciphertext = new String(base64bytes);
        log.debug("plaintext:{} ciphertext:{}", plaintext, ciphertext);
        return ciphertext;
    }
    /**
     * 解密
     *
     * @param content  待解密内容
     * @param password 解密密钥
     * @return 解密的字节数组
     */
    public static byte[] decrypt(byte[] content, String password) {
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            //需要对密码进行SHA填充
            SecureRandom random=SecureRandom.getInstance("SHA1PRNG");
            random.setSeed(password.getBytes("UTF-8"));
            kgen.init(128, random);
            SecretKey secretKey = kgen.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
            Cipher cipher = Cipher.getInstance("AES");// 创建密码器
            cipher.init(Cipher.DECRYPT_MODE, key);// 初始化
            byte[] result = cipher.doFinal(content);
            return result; // 解密
        } catch (Exception e) {
            log.error("使用AES解密发生错误", e);
            return null;
        }
    }

    /**
     *
     * @param ciphertext 待解密内容
     * @param password 解密密钥
     * @param encoding 编码集
     * @return 加密的字符串
     */
    public static String decrypt(String ciphertext, String password, String encoding){
        byte[] input = new byte[0];
        try {
            input = Base64.decodeBase64(ciphertext.getBytes(encoding));
        } catch (UnsupportedEncodingException e) {
            return null;
        }
        byte[] results = decrypt(input, password);
        byte[] base64bytes = Base64.decodeBase64(results);
        if(base64bytes == null){
            throw new RuntimeException("解密数据失败");
        }
        String plaintext = new String(base64bytes);
        log.debug("ciphertext:{} plaintext:{}", ciphertext, plaintext);
        return plaintext;
    }

}
