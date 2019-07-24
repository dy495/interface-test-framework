package com.haisheng.framework.testng.study.java.IO.bytestream;

import org.testng.annotations.Test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class DemoOutputStream {
//    写入数据的原理（内存-->硬盘）
//        java代码-->JVM-->OS-->OS调用自己的方法将数据写入文件

//    使用步骤（3步，重点）

    @Test
    public void testOutputStream() throws IOException {
//        1、创建对象
        FileOutputStream fos = new FileOutputStream("c:\\abc\\demo.txt");
//        2、写数据
        fos.write(97);
//        3、关闭
        fos.close();
    }

//    ------------------------------------------使用我的电脑打开写入的文件，老是乱码，不知道为啥--------------------------
    @Test
    public void testOutputStream1() throws IOException {
//        1、创建对象
        FileOutputStream fos = new FileOutputStream("c:\\abc\\demo.txt");
//        2、写数据
        byte[] bytes = {65,66,67,68,69};//第一个字节是正（0-127），显示时查询ASCII表
        fos.write(bytes);

        //第一个字节为负，那么第一个字节和第二个字节会组成一个中文显示，查询默认码表（GBK）
        byte[] bytes1 = {-65,-66,-67,68,69};
        fos.write(bytes1);

//        把字节数数组的一部分写入文件
        byte[] bytes2 = {65,66,67,68,69};
        fos.write(bytes2,1,2);

//        把String转成字节数组，写入字符串
        byte[] bytes3 = "你好".getBytes();
        System.out.println(Arrays.toString(bytes3));
        fos.write(bytes3);

//        3、关闭
        fos.close();
    }


    @Test
    public void appendWrite() throws IOException {
//        追加写
        FileOutputStream fos = new FileOutputStream("c:\\abc\\demoappend.txt", true);
        fos.write(65);
        fos.write("你好".getBytes());

//        追加并换行
        fos.write("\r\n".getBytes());//注意要getBytes
        fos.write("你好".getBytes());
        fos.close();

    }
}
