package com.haisheng.framework.util;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;

public class Base64Util {
    // 加密
    public static String Encode(String str) {
        byte[] b;
        String s;
        b = str.getBytes(StandardCharsets.UTF_8);
        s = new BASE64Encoder().encode(b);
        return s;
    }

    // 解密
    public static String Decode(String s) {
        byte[] b;
        String result = null;
        if (s != null) {
            BASE64Decoder decoder = new BASE64Decoder();
            try {
                b = decoder.decodeBuffer(s);
                result = new String(b, StandardCharsets.UTF_8);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static String encodeBase64File(String path) {
        File file = new File(path);
        try {
            FileInputStream inputFile = new FileInputStream(file);
            byte[] buffer = new byte[(int) file.length()];
            inputFile.read(buffer);
            inputFile.close();
            return new BASE64Encoder().encode(buffer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

