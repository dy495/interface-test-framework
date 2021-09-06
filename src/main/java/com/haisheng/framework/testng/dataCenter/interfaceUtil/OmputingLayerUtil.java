package com.haisheng.framework.testng.dataCenter.interfaceUtil;

import lombok.SneakyThrows;

import java.util.Base64;
public class OmputingLayerUtil {
    @SneakyThrows
    public static String base64Convert(String url) {
        byte[] imgBytes = HttpConnector.get(url, null);
        return Base64.getEncoder().encodeToString(imgBytes);
    }
}
