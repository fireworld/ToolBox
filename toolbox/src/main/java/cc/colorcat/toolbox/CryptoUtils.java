package cc.colorcat.toolbox;

import android.support.annotation.NonNull;
import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by cxx on 16-4-19.
 * xx.ch@outlook.com
 */
public final class CryptoUtils {
    // IvParameterSpec 为线程安全的类
    private static IvParameterSpec SPEC = new IvParameterSpec("1234567890123456".getBytes());

    /**
     * 解密，如果解密失败则原样返回
     */
    public static String aesDecrypt(final String data, final String key) {
        String result = data;
        if (data != null) {
            String res = data;
            try {
                // 判断是否需要URLDecode
                if (res.contains("%2B") || res.contains("%3D") || res.contains("%2F")) {
                    res = urlDecode(res);
                }
                byte[] base = base64Decode(res.getBytes("UTF-8"));
                SecretKeySpec sks = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                cipher.init(Cipher.DECRYPT_MODE, sks, SPEC);
                byte[] decryptedData = cipher.doFinal(base);
                result = new String(decryptedData, "UTF-8");
            } catch (Exception e) {
                L.e(e);
            }
        }
        return result;
    }

    /**
     * 加密，如果加密失败则原样返回
     */
    public static String aesEncrypt(final String resource, final String key) {
        String result = resource;
        if (resource != null) {
            try {
                SecretKeySpec sks = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");// 算法/模式/补码方式
                cipher.init(Cipher.ENCRYPT_MODE, sks, SPEC);
                byte[] encryptedData = cipher.doFinal(resource.getBytes("UTF-8"));
                String base64Str = new String(base64Encode(encryptedData), "UTF-8");
                result = urlEncode(base64Str);
            } catch (Exception e) {
                L.e(e);
            }
        }
        return result;
    }

    private static byte[] base64Decode(byte[] input) {
        return Base64.decode(input, Base64.NO_WRAP);
    }

    public static byte[] base64Encode(byte[] input) {
        return Base64.encode(input, Base64.NO_WRAP);
    }

    private static String urlDecode(String s) throws UnsupportedEncodingException {
        return URLDecoder.decode(s, "UTF-8");
    }

    private static String urlEncode(String s) throws UnsupportedEncodingException {
        return URLEncoder.encode(s, "UTF-8");
    }

    public static String sha256(String text) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.reset();
            byte[] data = md.digest(text.getBytes());
            return String.format("%0" + (data.length * 2) + "X", new BigInteger(1, data));
        } catch (NoSuchAlgorithmException e) {
            L.e(e);
            return text;
        }
    }

    /**
     * md5 加密，如果加密失败则原样返回
     */
    public static String md5(@NonNull String resource) {
        String result = resource;
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.reset();
            digest.update(resource.getBytes());
            byte[] bytes = digest.digest();
            int len = bytes.length << 1;
            StringBuilder sb = new StringBuilder(len);
            for (byte b : bytes) {
                sb.append(Character.forDigit((b & 0xf0) >> 4, 16));
                sb.append(Character.forDigit(b & 0x0f, 16));
            }
            result = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            L.e(e);
        }
        return result;
    }
}
