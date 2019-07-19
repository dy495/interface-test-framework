package com.haisheng.framework.testng.operationcenter;

import com.haisheng.framework.testng.service.CsvDataProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

public class SensorTest {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Test(dataProvider = "CsvDataProvider", dataProviderClass = CsvDataProvider.class)
    public void shelf_sensortest(String unitCode, String actual, String action,
                                  String v0, String v1, String v2, String v3, String v4,
                                  String v5, String v6, String v7, String v8, String v9) {

        double fv0 = Double.valueOf(v0);
        double fv1 = Double.valueOf(v1);
        double fv2 = Double.valueOf(v2);
        double fv3 = Double.valueOf(v3);
        double fv4 = Double.valueOf(v4);
        double fv5 = Double.valueOf(v5);
        double fv6 = Double.valueOf(v6);
        double fv7 = Double.valueOf(v7);
        double fv8 = Double.valueOf(v8);
        double fv9 = Double.valueOf(v9);
        double factual = Double.valueOf(actual);

        double testQuantity = 10;

        double[] testValue = {fv0-factual,fv1-factual,fv2-factual,fv3-factual,fv4-factual,
                fv5-factual,fv6-factual,fv7-factual,fv8-factual,fv9-factual};

        double avg, val, dev;
        double max, min;

        double sum = 0d;

        for (int i = 0; i < testQuantity; i++){
            sum +=testValue[i];
        }

//        平均值
        avg = sum / testQuantity;
        logger.info(avg + "");

//        方差
        double diff = 0d;
        for (int i = 0;i<testQuantity; i++){
            diff += Math.pow(testValue[i] - avg,2);
        }

        val = diff/testQuantity;

//        标准差
        dev = Math.sqrt(val);

//        最值
        max = Math.abs(testValue[0]);
        min = Math.abs(testValue[1]);

        for (int i = 0; i<testQuantity-1; i++){
            if( max <Math.abs(testValue[i])){
                max = Math.abs(testValue[i]);
            }else if(Math.abs(testValue[i]) < min){
                min = Math.abs(testValue[i]);
            }
        }

        logger.info("unit_code：" + unitCode);
        logger.info("action：" + action);
        logger.info("avg：" + avg);
        logger.info("val：" + val);
        logger.info("dev：" + dev);
        logger.info("max：" + max);
        logger.info("min：" + min);

    }

//    @Test
    public void test(){

        double[] value = {371-378,366-378,377-378,385-378,374-378,389-378,390-378,374-378,363-378,372-378};

        double avg = 0d;

        for (int i = 0; i<10;i++){
            avg += value[i];
        }

        avg/=10;
        logger.info(avg + "");

        double diff = 0d;

        for (int i = 0; i<10;i++){
            diff +=Math.pow(value[i]-avg,2);
        }

        double val = diff/10;

        logger.info(val + "");

    }
}
