package com.haisheng.framework.testng.operationcenter;

import com.alibaba.fastjson.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Date;

public class TestJsonAyyay {

    Logger logger = LoggerFactory.getLogger(this.getClass());
@Test
    public void TestJsonAyyay1(){

        String json = "[\"57d5b4c9ead8bf2ce10dcf01a91d87a2\", \"7beb2345-66e2-4fae-be31-74c0285a3097\", \"sfsdfsdfsdfsd\"]";

        JSONArray testArray = JSONArray.parseArray(json);

        int size = 3;
        size = testArray.size();


        for(int i = 0;i<size;i++){
            String jso = testArray.getString(i);
            logger.info(jso);
        }
    }

    @Test
    public void arrEquals(){
        String[] arr1 = {"1","2","3"};
        String[] arr2 = {"3","1","2"};
        String[] arr3 = {"3","1","2"};
//        if(Arrays.equals(arr1,arr2)){
//            Assert.assertEqualsNoOrder(arr1,arr2);
//            logger.info("1111");
//        }
//
//        if(Arrays.equals(arr3,arr2)){
//            Assert.assertEqualsNoOrder(arr3,arr2);
//            logger.info("22222");
//
//        }

        Assert.assertEqualsNoOrder(arr1,arr2);
            logger.info("1111");

        Assert.assertEqualsNoOrder(arr3,arr2);
            logger.info("22222");

    }

    @Test
    public void timeStamp(){
        long curTimeStamp = System.currentTimeMillis();
        System.out.println(curTimeStamp);
        System.out.println(new Date(curTimeStamp));

        long deadLine = curTimeStamp+60*1000L;
        System.out.println(deadLine);

        System.out.println(new Date(deadLine));
    }

}
