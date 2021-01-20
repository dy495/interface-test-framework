package com.haisheng.framework.testng.demo;

import org.testng.annotations.Test;

import java.io.*;
import java.util.Arrays;

public class DeteleData {


    //    将没有电话号码和付款方式不为线下的去掉
    @Test
    public void delete(){
        try {
            String fromFilePath = "src/main/java/com/haisheng/framework/testng/demo/orifile/from.trans";
            fromFilePath= fromFilePath.replace("/",File.separator);
            String toFilePath = "src/main/java/com/haisheng/framework/testng/demo/orifile/to.trans";
            toFilePath= toFilePath.replace("/",File.separator);
            File toFile = new File(toFilePath);

            BufferedReader br = new BufferedReader(new FileReader(fromFilePath));

            BufferedWriter bw = new BufferedWriter(new FileWriter(toFile,true));
            String line;

//            while ((line = br.readLine()) != null){
//                String[] item = line.split(",");
//                String phoneNum = item[5];
////                String payWay = item[6];
//                if (!(phoneNum==null || phoneNum.trim().length()==0) && !"18012345678".equals(phoneNum)){
//                    bw.newLine();
////                    bw.write(item[0]+ ","+item[1]+ ","+item[2]+ ","+item[3]+ ","+item[4]+ ","+item[5] +","+item[6]);
//                    bw.write(item[2]+ ","+item[3]+ ","+item[4]+ ","+item[5] +","+item[6]);
//                }
//            }

            while ((line = br.readLine()) != null){
                String[] item = line.split(",");
                String phoneNum = item[4];
                String payWay = item[5];


                if(!(phoneNum==null || phoneNum.trim().length()==0) && !"18012345678".equals(phoneNum)&&!("APP").equals(payWay)){
                    bw.newLine();
                    bw.write(item[2]+ ","+item[3]+ ","+item[4]+ ","+item[5]);
                }

            }

            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public static void insertSort(int[] a) {
        int i, j, insertNote;// 要插入的数据
        for (i = 1; i < a.length; i++) {// 从数组的第二个元素开始循环将数组中的元素插入
            insertNote = a[i];// 设置数组中的第2个元素为第一次循环要插入的数据
            j = i - 1;
            while (j >= 0 && insertNote < a[j]) {
                a[j + 1] = a[j];// 如果要插入的元素小于第j个元素,就将第j个元素向后移动
                j--;
            }
            a[j + 1] = insertNote;// 直到要插入的元素不小于第j个元素,将insertNote插入到数组中
        }
    }

    @Test
    public void testInsertSort() {
        int a[] = { 38,65,97,76,13,27,49 };
        insertSort(a);
        System.out.println(Arrays.toString(a));
    }
}
