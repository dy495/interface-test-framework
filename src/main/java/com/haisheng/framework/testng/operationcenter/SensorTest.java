package com.haisheng.framework.testng.operationcenter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class SensorTest {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    String typeDrop = "DROP";
    String typePick = "PICK";

    public void writeTocsv(String unitCode, String type, int standardWeight, int size, double avg, double val, double dev, double max, double min) {
        try {
            String filePath = "src\\main\\java\\com\\haisheng\\framework\\testng\\operationcenter\\result.csv";
            filePath = filePath.replace("\\", File.separator);
            File csv = new File(filePath);//CSV文件
            BufferedWriter bw = new BufferedWriter(new FileWriter(csv));
            //新增一行数据
            bw.newLine();
            bw.write(unitCode + "," + type + "," + standardWeight +"," + size + "," + avg + "," + val + "," + dev + "," + max + "," + min);
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
        String filePath = "src\\main\\java\\com\\haisheng\\framework\\testng\\operationcenter\\sensortest_45g.txt";
        extratctSensorPickPutData(filePath);
    }

    public void extratctSensorPickPutData(String filePath) throws Exception {
        FileUtil fileUtil = new FileUtil();
        filePath = filePath.replace("\\", File.separator);
        int standardWeight = 0;
        if (filePath.endsWith("sensortest_45g.txt")) {
            standardWeight = 45;
        }
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

    private void calIndice(String unitCode, String type, int standardWeight, List<Double> valueList, double actual) {
        int size = valueList.size();
        double diffForAvg = 0d;
        double avg = 0d;

        for (Double aDouble : valueList) {
            avg += Math.abs(aDouble) - standardWeight;
        }

        avg =  diffForAvg/size;

        double diffForVal = 0d;

        for (Double aDouble : valueList) {
            diffForVal += Math.pow(Math.abs(aDouble) - actual - avg, 2);
        }

        double val = diffForVal / size;

        double dev = Math.sqrt(val);

        double max = 0d;
        double min = 1000d;

        for (Double aDouble : valueList) {
            if (Math.abs(max) < Math.abs(Math.abs(aDouble) - actual)) {
                max = Math.abs(aDouble) - actual;
            } else if (Math.abs(Math.abs(aDouble) - actual) < Math.abs(min)) {
                min = Math.abs(aDouble) - actual;
            }
        }

        writeTocsv(unitCode, type, standardWeight, size, avg, val, dev, max, min);
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

}
