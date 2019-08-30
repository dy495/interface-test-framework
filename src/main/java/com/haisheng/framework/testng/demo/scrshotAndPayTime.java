package com.haisheng.framework.testng.demo;

import org.testng.annotations.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

//    对比结账记录与截图，将结账记录中有截图中没有的筛选出来。
public class scrshotAndPayTime {

    String screenshotPath = "src\\main\\java\\com\\haisheng\\framework\\testng\\demo\\screenshot_trans_contrast\\screenshot";

    String transPath = "src\\main\\java\\com\\haisheng\\framework\\testng\\demo\\screenshot_trans_contrast\\trans";

    @Test
    public void contrast() throws Exception {
        File file = new File(transPath);
        File[] csvFiles = null;
        if (file.exists()) {
            csvFiles = file.listFiles();//如果这里面有文件夹的话，就会报拒绝访问
        }

        file = new File(screenshotPath);

        File[] shotFiles = null;
        if (file.exists()) {
        }






        BufferedReader br;

        String line;
        ArrayList<String> csvPhoneNums = new ArrayList<>();

        for (int i = 0; i < csvFiles.length; i++) {
            File file1 = csvFiles[i];
            String path = file1.getPath();
//            System.out.println(path);

            br = new BufferedReader(new FileReader(path));
            while ((line = br.readLine()) != null) {
                String phoneNum = line.substring(0, line.indexOf(","));
                csvPhoneNums.add(phoneNum);
            }


        }
    }


////    @Test
//    public void contrast1() {
////        String[] scrshots = getFileName();
//
////        String csvsPath = "src\\main\\java\\com\\haisheng\\framework\\testng\\demo\\screenshot_trans_contrast\\trans";
////        String[] csvs = readCsv(transPath, scrshots);
//
////        System.out.println(Arrays.toString(scrshots));
//
////        System.out.println("======================================================");
//
////        System.out.println(Arrays.toString(csvs));
//
////        for (int i = 0; i < csvs.length; i++) {
////            String temp = csvs[i].substring(csvs[i].indexOf("-"));
////            for (int step = 0; step < scrshots.length; step++) {
////                if (!scrshots[step].contains(temp)){
////                    System.out.println(csvs[i]);
////                }
////            }
////        }
//    }
//
//    public String[] getFileName(String filePath) {
//        File file = new File(filePath);
//        File[] array = file.listFiles();
//        String[] fileNameArray = new String[array.length];
//
//        for (int i = 0; i < array.length; i++) {
//            if (array[i].isFile()) {
////                System.out.println("only take file name:" + array[i].getName());
////                System.out.println("take file path and name:" + array[i]);
////                System.out.println("take file path and name:" + array[i].getPath());
//                fileNameArray[i] = array[i].getName();
//            } else if (array[i].isDirectory()) {
//                getFileName(array[i].getPath());
//            }
//        }
//
//        return fileNameArray;
//    }
//
//    public String[] readCsv(String filePath, String[] scrshots) {
//        int ArrayLength = 0;
//        String[] temp = new String[300];
//        String[] ff = new String[0];
//        try {
//            String[] fileNameArray = getFileName(filePath);
//            for (int i = 0; i < fileNameArray.length; i++) {
//                String newFilePath = fileNameArray[i] + "_";
//                File newFile = new File( "\\" + newFilePath);
//                if (!newFile.exists()) {
//                    newFile.createNewFile();
//                }
////                String ss = fileNameArray[i];
////                System.out.println(ss);
//
//                BufferedReader reader = new BufferedReader(new FileReader( "\\" + fileNameArray[i]));//换成你的文件名
////            reader.readLine();//第一行信息，为标题信息，不用，如果需要，注释掉
//                String line;
//                while ((line = reader.readLine()) != null) {
//                    String item[] = line.split(",");//CSV格式文件为逗号分隔符文件，这里根据逗号切分
//                    String phoneNum = item[2];
//                    temp[ArrayLength] = fileNameArray[i] + "-" + phoneNum;
//
//
//                    ArrayLength++;
//                }
//            }
//
//            ff = Arrays.copyOf(temp, ArrayLength);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return ff;
//    }
}
