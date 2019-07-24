package com.haisheng.framework.testng.study.java.IO.bytestream;

import org.testng.annotations.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class DemoInputStream {

    @Test
    public void readOne() throws IOException {
        File file = new File("c:\\abc\\inputstream.txt");
        file.createNewFile();

        FileOutputStream fos = new FileOutputStream(file, true);
        fos.write("你好".getBytes());
        fos.close();

        FileInputStream fis = new FileInputStream(file);
//        一次读取一个字节
        int b1 = fis.read();
        System.out.println(b1);

        int len=0;
        while ((len = fis.read())!=-1){
            System.out.println((char) len);
        }

        fis.close();
    }

    @Test
    public void readSome() throws IOException {
        File file = new File("c:\\abc\\readSome.txt");
//        file.createNewFile();

//        FileOutputStream fos = new FileOutputStream(file, true);
//        fos.write("abcdefg".getBytes());
//        fos.close();

        FileInputStream fis = new FileInputStream(file);
//        byte[] bytes = new byte[2];

//        一次读取多个字节。
//        方法的参数bytes的作用是缓冲，存储每次读到的字节数，数组的长度一般定义为1024或其整数倍；
//        方法返回值是读取的有效字节个数
//        int len = fis.read(bytes);
//        System.out.println(len);
//        System.out.println(new String(bytes));

        int len  =0;
//        声明为1024，但是只有7个字符，所以后面都用空格填充；
//        如果将bytes长度设为2，那么双击输出内容只会选中一行的两个字符，三击才会选中整行。
//        如果是1024，那么双击会选中整行，说明后面都是空格。
        byte[] bytes = new byte[1024];
//        bytes每次读取时都会被更新，缓存新的读到的数据，
//        所以byte[] bytes = new byte[2]只是声明了数组的长度，read的时候才真正赋值
        while ((len = fis.read(bytes))!=-1){
            System.out.println(new String(bytes));
        }

        fis.close();

    }


}
