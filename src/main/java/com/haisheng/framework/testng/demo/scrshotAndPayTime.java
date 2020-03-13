package com.haisheng.framework.testng.demo;

import org.testng.annotations.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

//    对比结账记录与截图，将结账记录中有截图中没有的筛选出来。
public class scrshotAndPayTime {

    String screenshotPath = "src/main/java/com/haisheng/framework/testng/demo/screenshot_trans_contrast/screenshot";

    String transPath = "src/main/java/com/haisheng/framework/testng/demo/screenshot_trans_contrast/trans";

    @Test
    public void contrast() throws Exception {

        screenshotPath = screenshotPath.replace("/",File.separator);
        transPath = transPath.replace("/",File.separator);

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
}
