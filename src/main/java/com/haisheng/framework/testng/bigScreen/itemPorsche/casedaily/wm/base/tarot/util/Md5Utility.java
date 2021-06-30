package com.haisheng.framework.testng.bigScreen.itemPorsche.casedaily.wm.base.tarot.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * md5工具
 *
 * @author wangmin
 * @date 2021-06-17
 */
public class Md5Utility {

    private static char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6',
            '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    private static final Map<String, Integer> digitsHex = new HashMap<>();

    static {
        digitsHex.put("0", 0);
        digitsHex.put("1", 1);
        digitsHex.put("2", 2);
        digitsHex.put("3", 3);
        digitsHex.put("4", 4);
        digitsHex.put("5", 5);
        digitsHex.put("6", 6);
        digitsHex.put("7", 7);
        digitsHex.put("8", 8);
        digitsHex.put("9", 9);
        digitsHex.put("a", 10);
        digitsHex.put("b", 11);
        digitsHex.put("c", 12);
        digitsHex.put("d", 13);
        digitsHex.put("e", 14);
        digitsHex.put("f", 15);
    }

    public static String getMD5String(String s) {
        return getMD5String(s.getBytes());
    }

    public static String getMD5String(byte[] bytes) {
        MessageDigest md = getMd5Digest();
        md.update(bytes);
        return bufferToHex(md.digest());
    }

    private static String bufferToHex(byte bytes[]) {
        return bufferToHex(bytes, 0, bytes.length);
    }

    private static String bufferToHex(byte bytes[], int m, int n) {
        StringBuffer stringbuffer = new StringBuffer(2 * n);
        int k = m + n;
        for (int l = m; l < k; l++) {
            appendHexPair(bytes[l], stringbuffer);
        }

        return stringbuffer.toString();
    }

    private static void appendHexPair(byte bt, StringBuffer stringbuffer) {
        char c0 = hexDigits[(bt & 0xf0) >> 4];
        char c1 = hexDigits[bt & 0xf];
        stringbuffer.append(c0);
        stringbuffer.append(c1);
    }

    private static MessageDigest getMd5Digest() {
        try {
            return MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
