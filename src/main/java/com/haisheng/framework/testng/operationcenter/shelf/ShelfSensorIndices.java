package com.haisheng.framework.testng.operationcenter.shelf;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.model.bean.IShelfSensorIndices;
import com.haisheng.framework.util.DateTimeUtil;
import com.haisheng.framework.util.FileUtil;
import com.haisheng.framework.util.QADbUtil;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ShelfSensorIndices {

    private QADbUtil qaDbUtil = new QADbUtil();

    public void writeTocsv(String unitCode, String type, int standardWeight, int size, double avg, double val, double dev, double max, double min) {
        try {
            String filePath = "src\\main\\java\\com\\haisheng\\framework\\testng\\operationcenter\\result.csv";
            filePath = filePath.replace("\\", File.separator);
            File csv = new File(filePath);//CSV文件
            BufferedWriter bw = new BufferedWriter(new FileWriter(csv,true));
            //新增一行数据
            bw.newLine();
            bw.write(unitCode + "," + type + "," + standardWeight + "," + size + "," + avg + "," + val + "," + dev + "," + max + "," + min);
            bw.close();
        } catch (FileNotFoundException e) {
            //捕获File对象生成时的异常
            e.printStackTrace();
        } catch (IOException e) {
            //捕获BufferedWriter对象关闭时的异常
            e.printStackTrace();
        }
    }

    @Test
    public void test() throws Exception {
        String filePath = "src\\main\\java\\com\\haisheng\\framework\\testng\\operationcenter\\sensortest_045g.txt";
        filePath = filePath.replace("\\", File.separator);
        extratctSensorPickPutData(filePath);
    }

    public void extratctSensorPickPutData(String filePath) throws Exception {
        FileUtil fileUtil = new FileUtil();
        filePath = filePath.replace("\\", File.separator);
        int standardWeight = 0;
        String fdf = filePath.substring(filePath.length() - 19);

        String standardWeightStr = fdf.substring(11, 14);
        standardWeight = Integer.valueOf(standardWeightStr);

        String key = "app_id";
        String noKey = "camera";
        String line;

        List<String> lines = fileUtil.findLinesByKey(filePath, key, noKey);
        ConcurrentHashMap<MySensor, List<Double>> hashMap = new ConcurrentHashMap<>();

        for (int step = 0; step < lines.size(); step++) {
            line = lines.get(step);
            int StartValue = line.indexOf("{");
            line = line.substring(StartValue);
            JSONObject bizDataJo = JSON.parseObject(line).getJSONObject("data").getJSONObject("biz_data");
            String type = bizDataJo.getString("type");

            String position = bizDataJo.getJSONObject("data").getString("position");
            position = position.replace(",", "-");
            double weightChange = bizDataJo.getJSONObject("data").getDouble("weight_change");

            saveAsHm(hashMap, position, type, weightChange);
        }

        Iterator<Map.Entry<MySensor, List<Double>>> iterator = hashMap.entrySet().iterator();
        while (iterator.hasNext()) {
            double actual = 0d;
            HashMap.Entry<MySensor, List<Double>> entry = iterator.next();
            MySensor s1 = entry.getKey();
            List<Double> valueList = entry.getValue();
            calIndice(s1.unitCode, s1.type, standardWeight, valueList, actual);
        }
    }

    private void calIndice(String unitCode, String action, int standardWeight, List<Double> valueList, double actual) {
        int repeatTimes = valueList.size();
        double diffForAvg = 0d;
        double avg = 0d;

        for (Double aDouble : valueList) {
            avg += Math.abs(aDouble) - standardWeight;
        }

        avg = diffForAvg / repeatTimes;

        double diffForVal = 0d;

        for (double aDouble : valueList) {
            diffForVal += Math.pow(Math.abs(aDouble) - actual - avg, 2);
        }

        double variance = diffForVal / repeatTimes;

        double dev = Math.sqrt(variance);

        double max = 0d;
        double min = 1000d;

        for (Double aDouble : valueList) {
            if (Math.abs(max) < Math.abs(Math.abs(aDouble) - actual)) {
                max = Math.abs(aDouble) - actual;
            } else if (Math.abs(Math.abs(aDouble) - actual) < Math.abs(min)) {
                min = Math.abs(aDouble) - actual;
            }
        }

        IShelfSensorIndices iShelfSensorIndices = new IShelfSensorIndices();
        iShelfSensorIndices.setUnitCode(unitCode);
        iShelfSensorIndices.setAction(action);
        iShelfSensorIndices.setStandardWeight(standardWeight);
        iShelfSensorIndices.setRepeatTimes(repeatTimes);
        iShelfSensorIndices.setAvg(avg);
        iShelfSensorIndices.setVariance(variance);
        iShelfSensorIndices.setStdDeviation(dev);
        iShelfSensorIndices.setMax(max);
        iShelfSensorIndices.setMin(min);
        DateTimeUtil dateTimeUtil = new DateTimeUtil();

        iShelfSensorIndices.setDate(dateTimeUtil.getHistoryDate(0));

        writeTocsv(unitCode, action, standardWeight, repeatTimes, avg, variance, dev, max, min);

        qaDbUtil.saveDataToDb(iShelfSensorIndices);
    }

    public void saveAsHm(ConcurrentHashMap<MySensor, List<Double>> hashMap, String position, String type, double weightChange) {
        MySensor mySensor = new MySensor(position, type);

        if (hashMap.containsKey(mySensor)) {
            hashMap.get(mySensor).add(weightChange);
        } else {
            List<Double> list = new ArrayList<>();
            list.add(weightChange);
            hashMap.put(mySensor, list);
        }
    }

    @BeforeSuite
    public void initial(){
        qaDbUtil.openConnection();
    }

    @AfterSuite
    public void clean(){
        qaDbUtil.closeConnection();
    }
}
