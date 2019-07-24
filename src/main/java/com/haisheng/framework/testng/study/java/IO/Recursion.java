package com.haisheng.framework.testng.study.java.IO;

import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;

public class Recursion {

//    要有停止条件
    @Test
    public void stackOverFlow(){
        a();
    }

    private void a() {
        a();
    }

//    递归次数不能太多
    @Test
    public void testTimesLimit(){
        b(1);
    }

    private void b(int i) {
        System.out.println(i);
        if (i==20000){
            return;
        }

        b(++i);
    }

//    构造方法禁止递归，因为这样会导致内存中有无数个对象
//    public Recursion(){
//        Recursion();
//    }


//    注意递归的结束条件，和递归的目的:获取下一个被加的数字
//    使用条件：调用者不变，方法参数发生变化
    @Test
    public void testSum(){

        System.out.println(add(5));
    }

    public int add(int n){

        if (n>1){
            return n + add(n-1);
        }else {
            return 1;
        }
    }

    @Test
    public void testFactorial(){

        System.out.println(factorial(5));
    }

    private int factorial(int n) {
        if (n==1){
            return 1;
        }

        return n*factorial(n-1);
    }

    @Test
    public void createFile() throws IOException {
       String path = "c:\\abc";
        File file = new File(path);
        System.out.println(file.mkdir());

       path = "c:\\abc\\abc.txt";
       file = new File(path);
        System.out.println(file.createNewFile());

        path = "c:\\abc\\abc.java";
        file = new File(path);
        System.out.println(file.createNewFile());

        path = "c:\\abc\\a";
        file = new File(path);
        System.out.println(file.mkdir());

        path = "c:\\abc\\b";
        file = new File(path);
        System.out.println(file.mkdir());

        path = "c:\\abc\\a\\a.jpg";
        file = new File(path);
        System.out.println(file.createNewFile());

        path = "c:\\abc\\a\\a.java";
        file = new File(path);
        System.out.println(file.createNewFile());

        path = "c:\\abc\\b\\b.java";
        file = new File(path);
        System.out.println(file.createNewFile());

        path = "c:\\abc\\b\\b.txt";
        file = new File(path);
        System.out.println(file.createNewFile());

    }

    @Test
    public void testTraverseDir(){

        traverseDir1(new File("c:\\abc"));

        System.out.println("=======================================");

        traverseDir2(new File("c:\\abc"));
    }

    private void traverseDir1(File dir) {
        File[] files = dir.listFiles();
        for (File f:files){
            System.out.println(f);
            if(f.isDirectory()){
                traverseDir1(f);
            }
        }
    }

    private void traverseDir2(File dir) {
        System.out.println(dir);
        File[] files = dir.listFiles();
        for (File f : files) {
            if (f.isDirectory()) {
                traverseDir2(f);
            }else {
                System.out.println(f);
            }
        }
    }
}
