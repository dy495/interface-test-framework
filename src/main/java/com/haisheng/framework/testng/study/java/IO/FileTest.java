package com.haisheng.framework.testng.study.java.IO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;


public class FileTest {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

//    1、创建文件
    @Test
    public void testFiele(){
//        new出来的file对象只是一个实例，并非一个文件

//        法1>
        String pathName = "D:\\study\\aaa.txt";
        File file = new File(pathName);

        logger.info(file +"");

//        法2>
        String parent = "d:\\study\\parent";
        String child = "child.txt";

        File file1 = new File(parent,child);

//        法3>
        String parentFile= "d:study";
        File file2 = new File(new java.io.File(parentFile), child);
    }


@Test
    public void separator(){
    /*static String pathSeparator 与系统有关的路径分隔符，为了方便，它被表示为一个字符串。
    static char pathSeparatorChar 与系统有关的路径分隔符。

    static String separator 与系统有关的默认名称分隔符，为了方便，它被表示为一个字符串。
    static char separatorChar 与系统有关的默认名称分隔符。*/

//        路径分隔符              win:分号;  linux: 冒号:
        String pathSeparator = java.io.File.pathSeparator;
        logger.info(pathSeparator);

//        文件名称分隔符   win:反斜杠\   linux:正斜杠/
        String separator = java.io.File.separator;
        logger.info(separator);
    }

    @Test
    public void fileMethod(){
        String path = "d:\\study\\test\\a.txt";
        File file = new File(path);
        System.out.println(file.getAbsolutePath());

//        这俩等价，都是传递的path是什么样就返回什么样
        System.out.println(file.getPath());
        System.out.println(file.toString());

//        返回路径的结尾部分（文件or文件夹）
        System.out.println(file.getName());

        System.out.println(file.length());

        System.out.println("======================================================================");


        String path1 = "src\\main\\java\\com\\haisheng\\framework\\testng\\study\\java\\IO\\FileTest.txt";
        File file1 = new File(path1);

        System.out.println(file1.getAbsolutePath());

//        这俩等价，都是传递的path是什么样就返回什么样
        System.out.println(file1.getPath());
        System.out.println(file1.toString());

//        返回路径的结尾部分（文件or文件夹）
        System.out.println(file1.getName());
        System.out.println(file1.length());
        System.out.println(file1.exists());

        System.out.println("========================================================================");



        File file2 = new File("C:\\Users\\Shine\\Desktop\\控制台坑位管理.pages");

//        如果构造方法中的路径中的文件是存在的，返回的就是该文件的大小；不存在则返回0；
//        如果路径中是一个存在的文件夹，那么也是返回0，因为文件夹没有大小的概念
        System.out.println(file2.length());

//        判断当前路径是否存在，存在：true，不存在：false
        System.out.println(file2.exists());

        if(file2.exists()){
//            是文件夹返回true，不是文件夹或者路径不存在时返回false
            System.out.println(file2.isDirectory());
            System.out.println(file2.isFile());
        }

    }

    @Test
    public void createNewFileMethod() throws IOException {
        String path = "src\\main\\java\\com\\haisheng\\framework\\testng\\study\\java\\IO\\FileTest.txt";
        File file = new File(path);

        //只能创建文件，不能创建文件夹；
        boolean isSuccess = file.createNewFile();
        System.out.println("isSuccess:" + isSuccess);

//        路径不存在时抛出ioException
        File file1 = new File("src\\main\\java\\com\\haisheng\\framework\\testng\\study\\jav\\IO\\FileTest.txt");
        file1.createNewFile();


    }

    @Test
    public void mkdirMethod(){

        String path = "src\\main\\java\\com\\haisheng\\framework\\testng\\study\\java\\IO\\testfile";
        File f1 = new File(path);

//        只能创建单级文件夹，路径不存在不会创建，但是不会抛异常
        boolean isSuccess = f1.mkdir();
        System.out.println("isSuccess: " + isSuccess);

        f1.delete();


        String path1 = "src\\main\\java\\com\\haisheng\\framework\\testng\\study\\java\\IO\\111\\testfile";

        File file = new File(path1);
        isSuccess = file.mkdir();
        System.out.println("用mkdir创建多级文件夹： " + isSuccess);

        isSuccess = file.mkdirs();
        System.out.println("用mkdirs创建多级文件夹： " + isSuccess);

//        delete只能删除单个文件或文件夹，要是想删除多级文件夹，要用递归

//	      true：文件/文件夹删除成功  false:文件夹中有内容（存在子文件或文件夹）或构造方法中路径不存在
        System.out.println(file.delete());

    }

    @Test
    public void listFile(){
        String path = "src\\main\\java\\com\\haisheng\\framework\\testng\\study";
        File file = new File(path);

//        即Linux中的ls， 构造方法中路径不存在或路径不是一个目录（是一个文件），会抛出空指针异常
//        隐藏文件和文件夹也能获取到
        String[] arrFile = file.list();
        System.out.println(Arrays.toString(arrFile));

//        两方法只是返回值不同
        File[] files = file.listFiles();
        for (File f : files) {
            System.out.println(f);
        }

    }
}
