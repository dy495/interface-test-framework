package com.haisheng.framework.util;

import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class UrlToIoOutputUtil {

    public static void toIoSave(String urlPath, String outputPath) {
        File file = new File(outputPath);
        //使用url 读取网页内容
        try {
            //url路径（微信安装包的URL）
            URL url = new URL(urlPath);
            //获取到HttpURLConnection 对象
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            //通过连接对象获取到输入流
            InputStream is = connection.getInputStream();
            //文件输出流
            OutputStream output = new FileOutputStream(file);
            //使用char 数组传输    -----字节流byte数组
            byte[] chs = new byte[1024];
            //标记
            int len;
            while ((len = is.read(chs)) != -1) {// read() 方法，读取输入流的下一个字节，返回一个0-255之间的int类型整数。如果到达流的末端，返回-1
                output.write(chs, 0, len);
            }
            close(is, output);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭方法
     */
    private static void close(@NotNull AutoCloseable... ac) {
        for (AutoCloseable autoCloseable : ac) {
            if (autoCloseable != null) {
                try {
                    autoCloseable.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
