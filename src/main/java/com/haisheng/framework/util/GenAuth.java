package com.haisheng.framework.util;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class GenAuth {
    public static void main(String[] args) {
        String signStr = "uid_e0d1ebec.a4d4d18741a8.e0709358d368ee13./business/customer/QUERY_CUSTOMER_FIND_HISTORY/v1.1.1553926584000.1234567";
        String sk = "ef4e751487888f4a7d5331e8119172a3";
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
