package com.haisheng.framework.testng.demo;

import com.haisheng.framework.testng.service.CsvDataProvider;
import com.haisheng.framework.util.DateTimeUtil;
import com.haisheng.framework.util.FileUtil;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.io.*;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.List;

public class calBaiguoyuanVar {

    @Test(dataProvider = "CsvDataProvider", dataProviderClass = CsvDataProvider.class, priority = 1)
    public void yuhaisheng_baiguoyuan_2019_07_17_12H_recorrect(String left, String right) throws ParseException {
        DateTimeUtil dateTimeUtil = new DateTimeUtil();
        float diff = dateTimeUtil.calTimeDiff(left, right);

        DecimalFormat decimalFormat = new DecimalFormat("0.00");
//        String diffAvg = decimalFormat.format(diff-180.0838);
//        writeTocsv(left, diffAvg);
        writeTocsv(left, String.valueOf(diff));
//        writeTocsv(String.valueOf(diff));
    }

    public void writeTocsv(String left,String diffAvg) {

        try {
            String filePath = "src/main/java/com/haisheng/framework/testng/demo/result.csv";
            filePath = filePath.replace("/", File.separator);
            File csv = new File(filePath);//CSV文件
            BufferedWriter bw = new BufferedWriter(new FileWriter(csv, true));
            //新增一行数据
            bw.newLine();
            bw.write(diffAvg);
//            bw.write(left + "," + diffAvg);
            bw.close();
        } catch (FileNotFoundException e) {
            //捕获File对象生成时的异常
            e.printStackTrace();
        } catch (IOException e) {
            //捕获BufferedWriter对象关闭时的异常
            e.printStackTrace();
        }
    }


    public void writeTocsv(String diffAvg) {

        try {
            String filePath = "src/main/java/com/haisheng/framework/testng/demo/result.csv";
            filePath = filePath.replace("/", File.separator);
            File csv = new File(filePath);//CSV文件
            BufferedWriter bw = new BufferedWriter(new FileWriter(csv, true));
            //新增一行数据
            bw.newLine();
            bw.write(diffAvg);
            bw.close();
        } catch (FileNotFoundException e) {
            //捕获File对象生成时的异常
            e.printStackTrace();
        } catch (IOException e) {
            //捕获BufferedWriter对象关闭时的异常
            e.printStackTrace();
        }
    }

    @Test(priority = 2)
    public void calVar() {
        String filePath = "src/main/java/com/haisheng/framework/testng/demo/result.csv";
        filePath = filePath.replace("/",File.separator);
        FileUtil fileUtil = new FileUtil();
        List list = fileUtil.getFileContent(filePath);
        float diffSumForAvg = 0;

        for (int i = 0; i < list.size(); i++) {
            diffSumForAvg += Float.parseFloat((String) list.get(i));
        }

        System.out.println(diffSumForAvg);
        float avg = diffSumForAvg / list.size();

        System.out.println("average: " + avg);

        float diffSumforVar = 0;
        for (int i = 0; i < list.size(); i++) {
            diffSumforVar += Math.pow(Float.parseFloat((String) list.get(i)) - avg, 2);
        }

        float var = diffSumforVar / list.size();

        System.out.println("var: " + var);
    }

    public void clearFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                if (!file.exists()) {
                    file.createNewFile();
                }

                FileWriter fileWriter = new FileWriter(file);
                fileWriter.write("");
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @BeforeSuite
    public void initial() {
        String filePath = "src/main/java/com/haisheng/framework/testng/demo/result.csv";

        filePath = filePath.replace("/",File.separator);
        clearFile(filePath);
    }
}
