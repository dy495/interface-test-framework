package com.haisheng.framework.util;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class GenAuth {
    public static void main(String[] args) {
        String uid = "uid_7fc78d24";
        String app_id = "097332a388c2";
        String ak = "77327ffc83b27f6d";
        String router = "/business/customer/QUERY_CUSTOMER_HISTORY/v1.1";
        String timestamp = "1557222173000";
        String nonce = "1234567";
        String signStr = uid + "." + app_id + "." + ak + "." + router + "." + timestamp + "." + nonce;
        String sk = "7624d1e6e190fbc381d0e9e18f03ab81";
        Mac sha256_HMAC = null;
        SecretKeySpec encodeSecretKey = null;
        byte[] hash = new byte[0];
        try {
            sha256_HMAC = Mac.getInstance("HmacSHA256");
            encodeSecretKey = new SecretKeySpec(sk.getBytes("utf-8"), "HmacSHA256");
            sha256_HMAC.init(encodeSecretKey);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        try {
            hash = sha256_HMAC.doFinal(signStr.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String Authorization = Base64.getEncoder().encodeToString(hash);
        System.out.println(Authorization);
    }
}
