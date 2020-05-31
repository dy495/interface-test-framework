package com.haisheng.framework.testng.operationcenter.shelf;

import ai.winsense.ApiClient;
import ai.winsense.common.Credential;
import ai.winsense.constant.SdkConstant;
import ai.winsense.exception.SdkClientException;
import ai.winsense.model.ApiRequest;
import ai.winsense.model.ApiResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.model.bean.Case;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.LogMine;
import com.haisheng.framework.util.QADbUtil;
import com.haisheng.framework.util.StatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

public class SinglePutBinding {

    String UID = "uid_7fc78d24";
    String APP_ID = "097332a388c2";
    String SHOP_ID = "8";
    String AK = "77327ffc83b27f6d";
    String SK = "7624d1e6e190fbc381d0e9e18f03ab81";
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private LogMine logMine = new LogMine(logger);

    private String failReason = "";
    private QADbUtil qaDbUtil = new QADbUtil();
    private int APP_ID_DB = ChecklistDbInfo.DB_APP_ID_SHELF_SERVICE;
    private int CONFIG_ID = ChecklistDbInfo.DB_SERVICE_ID_SHELF_SERVICE;
    private String CI_CMD = "curl -X POST http://liaoxiangru:liaoxiangru@192.168.50.2:8080/job/person-commodity-binding-test/buildWithParameters?case_name=";

    ArrayList jsonList = new ArrayList<VideoJson>();
    ApiResponse apiResponse;

    String goods1_2 = "6919892992000";
    String goods1_4 = "6911988009890";
    String goods1_5 = "6906907101229";
    String goods1_7 = "6926892566087";
    String goods1_8 = "6907992513652";
    String goods2_2 = "6903252710793";
    String goods2_4 = "6922266446726";
    String goods3_2 = "6920698400378";
    String goods3_3 = "6903252000771";
    String goods3_4 = "6924743915060";
    String goods3_5 = "6911988009784";
    String goods4_2 = "6923450657935";
    String goods4_4 = "6906907101229";
    String goods4_5 = "6920584471215";
    String goods4_6 = "6926892566087";
    String goods4_9 = "6920907800302";
    String goods4_10 = "6907992513652";

    private boolean IS_DEBUG = true;

    public String filePath = "src/main/java/com/haisheng/framework/testng/operationcenter/shelf/singlePutJson";

    private long currentTime = System.currentTimeMillis() + 24 * 60 * 60 * 1000;

    private long waitTime = 0L;
    private long beforeTime = 0L;

    private void customerMessage(String json, String[] secKey, Case acase, int step) throws Exception {
        String router = "/commodity/external/CUSTOMER_MESSAGE/v1.0";
        String message = "";

        //暂时不需要测试type为ENTER和LEAVE的，人物相关的都不支持；PICK和DROP不要传入face和person_id

        try {
            apiResponse = sendRequestWithGate(router, secKey, json);
            message = apiResponse.getMessage();

            sendResAndReqIdToDbApi(apiResponse, acase, step);

            checkCodeApi(apiResponse, router, StatusCode.SUCCESS);

        } catch (Exception e) {
            failReason = e.toString();
            Assert.fail(message);
            throw e;
        }
    }

    private ApiResponse customerGoods(long timestamp, Case acase, int step) throws Exception {
        String router = "/commodity/external/SHELVES_CUSTOMER_LIST/v1.0";
        String[] secKey = new String[]{};
        String message = "";

        //暂时不需要测试type为ENTER和LEAVE的，人物相关的都不支持；PICK和DROP不要传入face和person_id

//        last_notify_time：顾客拿起商品时间
        try {

            String json =
                    "{\"subject_id\":\"" + SHOP_ID + "\" ,\"last_notify_time\":" + timestamp + "}";
            apiResponse = sendRequestWithGate(router, secKey, json);
            message = apiResponse.getMessage();

            sendResAndReqIdToDbApi(apiResponse, acase, step);

            checkCodeApi(apiResponse, router, StatusCode.SUCCESS);

        } catch (Exception e) {
            failReason = e.toString();
            Assert.fail(message);
            throw e;
        }
        return apiResponse;
    }

    private ApiResponse leaveShop(long msgTime, String personId, String customerId) throws Exception {
        logger.info("\n");
        logger.info("------------leave shop!-----------------------");
        String router = "/commodity/external/CUSTOMER_TRACE/v1.0";
        String[] secKey = new String[]{};
        String message = "";

        try {
            String json =
                    "{\n" +
                            "    \"shop_id\":\"" + SHOP_ID + "\",\n" +
                            "    \"msg_time\":" + msgTime + "," +
                            "    \"region\":[\n" +
                            "        {\n" +
                            "            \"position_status\":\"LEAVE\",\n" +
                            "            \"leave_entrance_type\":\"STORE\"\n" +
                            "        }\n" +
                            "    ],\n" +
                            "    \"person_ids\":[\n" +
                            "        \"" + personId + "\"\n" +
                            "    ],\n" +
                            "    \"customer\":{\n" +
                            "            \"customer_id\":\"" + customerId + "\"\n" +
                            "    }\n" +
                            "}";
            apiResponse = sendRequestWithGate(router, secKey, json);
            message = apiResponse.getMessage();

            checkCodeApi(apiResponse, router, StatusCode.SUCCESS);

        } catch (Exception e) {
            failReason = e.toString();
            Assert.fail(message);
            throw e;
        }
        return apiResponse;
    }

    private void init() throws Exception {
        logger.info("\n");
        logger.info("------------init!-----------------------");
        String router = "/commodity/external/INIT_TEST_ENV/v1.0";
        String[] resource = new String[]{};
        String message = "";

        //暂时不需要测试type为ENTER和LEAVE的，人物相关的都不支持；PICK和DROP不要传入face和person_id

        try {

            String json =
                    "{\"stock\":100}";
            apiResponse = sendRequestWithGate(router, resource, json);
            message = apiResponse.getMessage();

            checkCodeApi(apiResponse, router, StatusCode.SUCCESS);

        } catch (Exception e) {
            failReason = e.toString();
            Assert.fail(message);
            throw e;
        }
    }

    @Test(priority = 1)
    public void singlePutTest1() {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName;
        String caseDesc = "";
        logger.info(caseDesc + "--------------------");

        Case aCase = new Case();
        failReason = "";

        int step = 0;

        VideoJson videoJson;
        String json;
        String[] secKey;

        try {
            aCase.setRequestData("1、enter；2、pick 4-4 1瓶可乐；3、drop 4-4 1瓶可乐；4、leave；5、查询绑定结果" + "\n\n");

            videoJson = (VideoJson) jsonList.get(1);
            json = videoJson.getJson();
            String personId = getEnterPersonId(json);
            secKey = videoJson.getSecKey();

            logger.info("\n\n");
            logger.info("----------" + (++step) + "-----1、enter----------------------------");
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("-------------" + (++step) + "---2、pick 4-4 1瓶可乐----------------");
            videoJson = (VideoJson) jsonList.get(2);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("-------------" + (++step) + "---3、drop 4-4 1瓶可乐----------------");
            videoJson = (VideoJson) jsonList.get(3);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("------------" + (++step) + "-----leave----------------------------");
            videoJson = (VideoJson) jsonList.get(4);
            beforeTime = videoJson.getTimeSift();
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----查询绑定结果----------------------------");
            long timestamp = videoJson.getTimestamp() - 1;
            ApiResponse customerGoodsRes = customerGoods(timestamp, aCase, step);

            String customerId = getCustomerId(customerGoodsRes);

            long msgTime = videoJson.getTimestamp() + 1;
            leaveShop(msgTime, personId, customerId);

            String[] goodsIds = new String[0];
            checkResult(customerGoodsRes, goodsIds, 0);

            aCase.setResult("PASS"); //FAIL, PASS
        } catch (AssertionError e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
            e.printStackTrace();
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
        } finally {

            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);

            if (!IS_DEBUG) {
                qaDbUtil.saveToCaseTable(aCase);
            }
        }
    }

    @Test(priority = 2)
    public void singlePutTest2() {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName;
        String caseDesc = "";
        logger.info(caseDesc + "--------------------");

        Case aCase = new Case();
        failReason = "";

        int step = 0;

        VideoJson videoJson;
        String json;
        String[] secKey;

        try {
            aCase.setRequestData("1、enter；2、pick 3-3 2桶牛肉面；3、leave；4、drop 3-3 1桶牛肉面；5、leave；6、查询绑定结果" + "\n\n");

            videoJson = (VideoJson) jsonList.get(5);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            String personId = getEnterPersonId(json);

            waitTime = videoJson.getTimeSift() - beforeTime;
            Thread.sleep(waitTime);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----1、enter----------------------------");
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----2、pick 3-3 2桶牛肉面---------------------------");
            videoJson = (VideoJson) jsonList.get(6);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----3、leave----------------------------");
            videoJson = (VideoJson) jsonList.get(7);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----4、drop 3-3 1桶牛肉面----------------------------");
            videoJson = (VideoJson) jsonList.get(8);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----5、leave----------------------------");
            videoJson = (VideoJson) jsonList.get(9);
            beforeTime = videoJson.getTimeSift();
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----6、查询绑定结果----------------------------");
            long timestamp = videoJson.getTimestamp() - 1;
            ApiResponse customerGoodsRes = customerGoods(timestamp, aCase, step);

            String customerId = getCustomerId(customerGoodsRes);

            long msgTime = videoJson.getTimestamp() + 1;
            leaveShop(msgTime, personId, customerId);

            String[] goodsIds = {goods3_3, "", ""};
            checkResult(customerGoodsRes, goodsIds, 1);

            aCase.setResult("PASS"); //FAIL, PASS
        } catch (AssertionError e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
            e.printStackTrace();
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
        } finally {

            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);

            if (!IS_DEBUG) {
                qaDbUtil.saveToCaseTable(aCase);
            }
        }
    }

    @Test(priority = 3)
    public void singlePutTest3() {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName;
        String caseDesc = "";
        logger.info(caseDesc + "--------------------");

        Case aCase = new Case();
        failReason = "";

        int step = 0;

        VideoJson videoJson;
        String json;
        String[] secKey;

        try {
            aCase.setRequestData("1、enter；2、leave；3、pick 4-6 1罐黑米粥；4、drop 4-6 1罐黑米粥；" +
                    "5、pick 4-6 1罐黑米粥；6、leave；7、查询绑定结果" + "\n\n");

            videoJson = (VideoJson) jsonList.get(10);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();

            String personId = getEnterPersonId(json);

            waitTime = videoJson.getTimeSift() - beforeTime;
            Thread.sleep(waitTime);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----1、enter----------------------------");
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----2、leave----------------------------");
            videoJson = (VideoJson) jsonList.get(11);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----3、pick 4-6 1罐黑米粥----------------------------");
            videoJson = (VideoJson) jsonList.get(12);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----4、drop 4-6 1罐黑米粥----------------------------");
            videoJson = (VideoJson) jsonList.get(13);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----5、pick 4-6 1罐黑米粥----------------------------");
            videoJson = (VideoJson) jsonList.get(14);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----6、leave----------------------------");
            videoJson = (VideoJson) jsonList.get(15);
            beforeTime = videoJson.getTimeSift();
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----7、查询绑定结果----------------------------");
            long timestamp = videoJson.getTimestamp() - 1;
            ApiResponse customerGoodsRes = customerGoods(timestamp, aCase, step);

            String customerId = getCustomerId(customerGoodsRes);

            long msgTime = videoJson.getTimestamp() + 1;
            leaveShop(msgTime, personId, customerId);

            String[] goodsIds = {goods4_6, "", ""};
            checkResult(customerGoodsRes, goodsIds, 1);

            aCase.setResult("PASS"); //FAIL, PASS

        } catch (AssertionError e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
            e.printStackTrace();
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
        } finally {

            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);

            if (!IS_DEBUG) {
                qaDbUtil.saveToCaseTable(aCase);
            }
        }
    }

    @Test(priority = 4)
    public void singlePutTest4() {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName;
        String caseDesc = "";
        logger.info(caseDesc + "--------------------");

        Case aCase = new Case();
        failReason = "";

        int step = 0;

        VideoJson videoJson;
        String json;
        String[] secKey;

        try {
            aCase.setRequestData("1、pick 3-2 1个大食桶；2、drop 3-2 1个大食桶；3、pick 4-6 1罐黑米粥；4、leave；5、查询绑定结果" + "\n\n");

            videoJson = (VideoJson) jsonList.get(16);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();

            String personId = getEnterPersonId(json);

            waitTime = videoJson.getTimeSift() - beforeTime;
            Thread.sleep(waitTime);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----1、pick 3-2 1个大食桶----------------------------");
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----2、drop 3-2 1个大食桶----------------------------");
            videoJson = (VideoJson) jsonList.get(17);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----3、pick 4-6 1罐黑米粥----------------------------");
            videoJson = (VideoJson) jsonList.get(18);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----4、leave----------------------------");
            videoJson = (VideoJson) jsonList.get(19);
            beforeTime = videoJson.getTimeSift();
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----5、查询绑定结果----------------------------");
            long timestamp = videoJson.getTimestamp() - 1;
            ApiResponse customerGoodsRes = customerGoods(timestamp, aCase, step);

            String customerId = getCustomerId(customerGoodsRes);

            long msgTime = videoJson.getTimestamp() + 1;
            leaveShop(msgTime, personId, customerId);

            String[] goodsIds = {goods4_6, "", ""};
            checkResult(customerGoodsRes, goodsIds, 1);

            aCase.setResult("PASS"); //FAIL, PASS

        } catch (AssertionError e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
            e.printStackTrace();
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
        } finally {

            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);

            if (!IS_DEBUG) {
                qaDbUtil.saveToCaseTable(aCase);
            }
        }
    }

    @Test(priority = 5)
    public void singlePutTest5() {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName;
        String caseDesc = "";
        logger.info(caseDesc + "--------------------");

        Case aCase = new Case();
        failReason = "";

        int step = 0;

        VideoJson videoJson;
        String json;
        String[] secKey;

        try {
            aCase.setRequestData("1、enter；2、pick 3-4 1个乐事；3、drop 3-5 1个乐事；4、leave；5、查询绑定结果" + "\n\n");

            videoJson = (VideoJson) jsonList.get(20);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();

            String personId = getEnterPersonId(json);

            waitTime = videoJson.getTimeSift() - beforeTime;
            Thread.sleep(waitTime);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----1、enter----------------------------");
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----2、pick 3-4 1个乐事----------------------------");
            videoJson = (VideoJson) jsonList.get(21);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----3、drop 3-5 1个乐事----------------------------");
            videoJson = (VideoJson) jsonList.get(22);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----4、leave----------------------------");
            videoJson = (VideoJson) jsonList.get(23);
            beforeTime = videoJson.getTimeSift();
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----5、查询绑定结果----------------------------");
            long timestamp = videoJson.getTimestamp() - 1;
            ApiResponse customerGoodsRes = customerGoods(timestamp, aCase, step);

            String customerId = getCustomerId(customerGoodsRes);

            long msgTime = videoJson.getTimestamp() + 1;
            leaveShop(msgTime, personId, customerId);

            String[] goodsIds = new String[0];
            checkResult(customerGoodsRes, goodsIds, 0);

            aCase.setResult("PASS"); //FAIL, PASS

        } catch (AssertionError e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
            e.printStackTrace();
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
        } finally {

            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);

            if (!IS_DEBUG) {
                qaDbUtil.saveToCaseTable(aCase);
            }
        }
    }

    @Test(priority = 6)
    public void singlePutTest6() {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName;
        String caseDesc = "";
        logger.info(caseDesc + "--------------------");

        Case aCase = new Case();
        failReason = "";

        int step = 0;

        VideoJson videoJson;
        String json;
        String[] secKey;

        try {
            aCase.setRequestData("1、enter；2、leave；3、pick 4-5 2个旺仔；4、leave；5、drop 4-6 1个旺仔；6、leave；7、查询绑定结果" + "\n\n");

            videoJson = (VideoJson) jsonList.get(24);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();

            String personId = getEnterPersonId(json);

            waitTime = videoJson.getTimeSift() - beforeTime;
            Thread.sleep(waitTime);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----1、enter----------------------------");
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----2、leave----------------------------");
            videoJson = (VideoJson) jsonList.get(25);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----3、pick 4-5 2个旺仔----------------------------");
            videoJson = (VideoJson) jsonList.get(26);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----4、leave----------------------------");
            videoJson = (VideoJson) jsonList.get(27);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----5、drop 4-6 1个旺仔----------------------------");
            videoJson = (VideoJson) jsonList.get(28);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----6、leave----------------------------");
            videoJson = (VideoJson) jsonList.get(29);
            beforeTime = videoJson.getTimeSift();
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----7、查询绑定结果----------------------------");
            long timestamp = videoJson.getTimestamp() - 1;
            ApiResponse customerGoodsRes = customerGoods(timestamp, aCase, step);

            String customerId = getCustomerId(customerGoodsRes);

            long msgTime = videoJson.getTimestamp() + 1;
            leaveShop(msgTime, personId, customerId);

            String[] goodsIds = {goods4_5, "", ""};
            checkResult(customerGoodsRes, goodsIds, 1);

            aCase.setResult("PASS"); //FAIL, PASS

        } catch (AssertionError e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
            e.printStackTrace();
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
        } finally {

            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);

            if (!IS_DEBUG) {
                qaDbUtil.saveToCaseTable(aCase);
            }
        }
    }

    @Test(priority = 7)
    public void singlePutTest7() {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName;
        String caseDesc = "";
        logger.info(caseDesc + "--------------------");

        Case aCase = new Case();
        failReason = "";

        int step = 0;

        VideoJson videoJson;
        String json;
        String[] secKey;

        try {
            aCase.setRequestData("1、enter；2、pick 4-9 2个蛋黄派；3、leave；4、drop 3-3 1个蛋黄派；5、drop 3-2 1个蛋黄派；6、pick 3-3 1桶牛肉面；" +
                    "7、pick 3-2 1桶大食桶；8、leave；9、查询绑定结果" + "\n\n");

            videoJson = (VideoJson) jsonList.get(30);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();

            String personId = getEnterPersonId(json);

            waitTime = videoJson.getTimeSift() - beforeTime;
            Thread.sleep(waitTime);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----1、enter----------------------------");
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----2、pick 4-9 2个蛋黄派----------------------------");
            videoJson = (VideoJson) jsonList.get(31);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----3、leave----------------------------");
            videoJson = (VideoJson) jsonList.get(32);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----4、drop 3-3 1个蛋黄派----------------------------");
            videoJson = (VideoJson) jsonList.get(33);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----5、drop 3-2 1个蛋黄派----------------------------");
            videoJson = (VideoJson) jsonList.get(34);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----6、pick 3-3 1桶牛肉面----------------------------");
            videoJson = (VideoJson) jsonList.get(35);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----7、pick 3-2 1桶大食桶----------------------------");
            videoJson = (VideoJson) jsonList.get(36);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----8、leave----------------------------");
            videoJson = (VideoJson) jsonList.get(37);
            beforeTime = videoJson.getTimeSift();
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----9、查询绑定结果----------------------------");
            long timestamp = videoJson.getTimestamp() - 1;
             ApiResponse customerGoodsRes = customerGoods(timestamp, aCase, step);

            String customerId = getCustomerId(customerGoodsRes);

            long msgTime = videoJson.getTimestamp() + 1;
            leaveShop(msgTime, personId, customerId);

            String[] goodsIds = {goods3_2, goods3_3, ""};
            checkResult(customerGoodsRes, goodsIds, 2);

            aCase.setResult("PASS"); //FAIL, PASS

        } catch (AssertionError e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
            e.printStackTrace();
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
        } finally {

            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);

            if (!IS_DEBUG) {
                qaDbUtil.saveToCaseTable(aCase);
            }
        }
    }

    @Test(priority = 8)
    public void singlePutTest8() {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName;
        String caseDesc = "";
        logger.info(caseDesc + "--------------------");

        Case aCase = new Case();
        failReason = "";

        int step = 0;

        VideoJson videoJson;
        String json;
        String[] secKey;

        try {
            aCase.setRequestData("1、enter；2、pick 4-2 1个益达；3、pick 4-4 1个可乐；4、drop 4-6 1个益达；5、leave；6、查询绑定结果" + "\n\n");

            videoJson = (VideoJson) jsonList.get(38);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();

            String personId = getEnterPersonId(json);

            waitTime = videoJson.getTimeSift() - beforeTime;
            Thread.sleep(waitTime);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----1、enter----------------------------");
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----2、pick 4-2 1个益达----------------------------");
            videoJson = (VideoJson) jsonList.get(39);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----3、pick 4-4 1个可乐----------------------------");
            videoJson = (VideoJson) jsonList.get(40);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----4、drop 4-6 1个益达----------------------------");
            videoJson = (VideoJson) jsonList.get(41);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----5、leave----------------------------");
            videoJson = (VideoJson) jsonList.get(42);
            beforeTime = videoJson.getTimeSift();
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----6、查询绑定结果----------------------------");
            long timestamp = videoJson.getTimestamp() - 1;
            ApiResponse customerGoodsRes = customerGoods(timestamp, aCase, step);

            String customerId = getCustomerId(customerGoodsRes);

            long msgTime = videoJson.getTimestamp() + 1;
            leaveShop(msgTime, personId, customerId);

            String[] goodsIds = {goods4_4, "", ""};
            checkResult(customerGoodsRes, goodsIds, 1);

            aCase.setResult("PASS"); //FAIL, PASS

        } catch (AssertionError e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
            e.printStackTrace();
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
        } finally {

            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);

            if (!IS_DEBUG) {
                qaDbUtil.saveToCaseTable(aCase);
            }
        }
    }

    @Test(priority = 9)
    public void singlePutTest9() {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName;
        String caseDesc = "";
        logger.info(caseDesc + "--------------------");

        Case aCase = new Case();
        failReason = "";

        int step = 0;

        VideoJson videoJson;
        String json;
        String[] secKey;

        try {
            aCase.setRequestData("1、pick 1-4 1个好吃点消化饼；2、drop 1-4 1个好吃点消化饼；3、enter；4、leave；5、查询绑定结果" + "\n\n");

            videoJson = (VideoJson) jsonList.get(43);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();

            String personId = getEnterPersonId(json);

            waitTime = videoJson.getTimeSift() - beforeTime;
            Thread.sleep(waitTime);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----1、pick 1-4 1个好吃点消化饼----------------------------");
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----2、drop 1-4 1个好吃点消化饼----------------------------");
            videoJson = (VideoJson) jsonList.get(44);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----3、enter----------------------------");
            videoJson = (VideoJson) jsonList.get(45);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----4、leave----------------------------");
            videoJson = (VideoJson) jsonList.get(46);
            beforeTime = videoJson.getTimeSift();
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----5、查询绑定结果----------------------------");
            long timestamp = videoJson.getTimestamp() - 1;
            ApiResponse customerGoodsRes = customerGoods(timestamp, aCase, step);

            String customerId = getCustomerId(customerGoodsRes);

            long msgTime = videoJson.getTimestamp() + 1;
            leaveShop(msgTime, personId, customerId);

            String[] goodsIds = new String[0];
            checkResult(customerGoodsRes, goodsIds, 0);

            aCase.setResult("PASS"); //FAIL, PASS

        } catch (AssertionError e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
            e.printStackTrace();
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
        } finally {

            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);

            if (!IS_DEBUG) {
                qaDbUtil.saveToCaseTable(aCase);
            }
        }
    }

    @Test(priority = 10)
    public void singlePutTest10() {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName;
        String caseDesc = "";
        logger.info(caseDesc + "--------------------");

        Case aCase = new Case();
        failReason = "";

        int step = 0;

        VideoJson videoJson;
        String json;
        String[] secKey;

        try {
            aCase.setRequestData("1、pick 1-8 2个优酸乳；2、drop 1-8 1个优酸乳；3、leave；4、查询绑定结果" + "\n\n");

            videoJson = (VideoJson) jsonList.get(47);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();

            String personId = getEnterPersonId(json);

            waitTime = videoJson.getTimeSift() - beforeTime;
            Thread.sleep(waitTime);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----1、pick 1-8 2个优酸乳----------------------------");
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----2、drop 1-8 1个优酸乳----------------------------");
            videoJson = (VideoJson) jsonList.get(48);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----3、leave----------------------------");
            videoJson = (VideoJson) jsonList.get(49);
            beforeTime = videoJson.getTimeSift();
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----4、查询绑定结果----------------------------");
            long timestamp = videoJson.getTimestamp() - 1;
            ApiResponse customerGoodsRes = customerGoods(timestamp, aCase, step);

            String customerId = getCustomerId(customerGoodsRes);

            long msgTime = videoJson.getTimestamp() + 1;
            leaveShop(msgTime, personId, customerId);

            String[] goodsIds = {goods1_8, "", ""};
            checkResult(customerGoodsRes, goodsIds, 1);

            aCase.setResult("PASS"); //FAIL, PASS

        } catch (AssertionError e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
            e.printStackTrace();
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
        } finally {

            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);

            if (!IS_DEBUG) {
                qaDbUtil.saveToCaseTable(aCase);
            }
        }
    }

    @Test(priority = 11)
    public void singlePutTest11() {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName;
        String caseDesc = "";
        logger.info(caseDesc + "--------------------");

        Case aCase = new Case();
        failReason = "";

        int step = 0;

        VideoJson videoJson;
        String json;
        String[] secKey;

        try {
            aCase.setRequestData("1、pick 1-2 1个3+2；2、drop 1-2 1个3+2；3、pick 1-2 1个3+2；4、enter；5、leave；6、查询绑定结果" + "\n\n");

            videoJson = (VideoJson) jsonList.get(50);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();

            String personId = getEnterPersonId(json);

            waitTime = videoJson.getTimeSift() - beforeTime;
            Thread.sleep(waitTime);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----1、pick 1-2 1个3+2----------------------------");
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----2、drop pick 1-2 1个3+2----------------------------");
            videoJson = (VideoJson) jsonList.get(51);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----3、pick 1-2 1个3+2----------------------------");
            videoJson = (VideoJson) jsonList.get(52);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----4、enter----------------------------");
            videoJson = (VideoJson) jsonList.get(53);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----5、leave----------------------------");
            videoJson = (VideoJson) jsonList.get(54);
            beforeTime = videoJson.getTimeSift();
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----6、查询绑定结果----------------------------");
            long timestamp = videoJson.getTimestamp() - 1;
            ApiResponse customerGoodsRes = customerGoods(timestamp, aCase, step);

            String customerId = getCustomerId(customerGoodsRes);

            long msgTime = videoJson.getTimestamp() + 1;
            leaveShop(msgTime, personId, customerId);

            String[] goodsIds = {goods1_2, "", ""};
            checkResult(customerGoodsRes, goodsIds, 1);

            aCase.setResult("PASS"); //FAIL, PASS

        } catch (AssertionError e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
            e.printStackTrace();
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
        } finally {

            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);

            if (!IS_DEBUG) {
                qaDbUtil.saveToCaseTable(aCase);
            }
        }
    }

    @Test(priority = 12)
    public void singlePutTest12() {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName;
        String caseDesc = "";
        logger.info(caseDesc + "--------------------");

        Case aCase = new Case();
        failReason = "";

        int step = 0;

        VideoJson videoJson;
        String json;
        String[] secKey;

        try {
            aCase.setRequestData("1、pick 1-7 1个黑米粥；2、pick 1-5 1个可乐；3、drop 1-7 1个黑米粥；4、leave；5、leave；6、查询绑定结果" + "\n\n");

            videoJson = (VideoJson) jsonList.get(55);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();

            String personId = getEnterPersonId(json);

            waitTime = videoJson.getTimeSift() - beforeTime;
            Thread.sleep(waitTime);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----1、pick 1-7 1个黑米粥----------------------------");
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----2、pick 1-5 1个可乐----------------------------");
            videoJson = (VideoJson) jsonList.get(56);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----3、drop 1-7 1个黑米粥----------------------------");
            videoJson = (VideoJson) jsonList.get(57);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----4、leave----------------------------");
            videoJson = (VideoJson) jsonList.get(58);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----5、leave----------------------------");
            videoJson = (VideoJson) jsonList.get(59);
            beforeTime = videoJson.getTimeSift();
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----6、查询绑定结果----------------------------");
            long timestamp = videoJson.getTimestamp() - 1;
            ApiResponse customerGoodsRes = customerGoods(timestamp, aCase, step);

            String customerId = getCustomerId(customerGoodsRes);

            long msgTime = videoJson.getTimestamp() + 1;
            leaveShop(msgTime, personId, customerId);

            String[] goodsIds = {goods1_5, "", ""};
            checkResult(customerGoodsRes, goodsIds, 1);

            aCase.setResult("PASS"); //FAIL, PASS

        } catch (AssertionError e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
            e.printStackTrace();
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
        } finally {

            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);

            if (!IS_DEBUG) {
                qaDbUtil.saveToCaseTable(aCase);
            }
        }
    }

    @Test(priority = 13)
    public void singlePutTest13() {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName;
        String caseDesc = "";
        logger.info(caseDesc + "--------------------");

        Case aCase = new Case();
        failReason = "";

        int step = 0;

        VideoJson videoJson;
        String json;
        String[] secKey;

        try {
            aCase.setRequestData("1、pick 1-7 1个黑米粥；2、drop 1-5 1个黑米粥；3、leave；4、leave；5、leave；6、查询绑定结果" + "\n\n");

            videoJson = (VideoJson) jsonList.get(60);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();

            String personId = getEnterPersonId(json);

            waitTime = videoJson.getTimeSift() - beforeTime;
            Thread.sleep(waitTime);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----1、pick 1-7 1个黑米粥----------------------------");
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----2、drop 1-5 1个黑米粥----------------------------");
            videoJson = (VideoJson) jsonList.get(61);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----3、leave----------------------------");
            videoJson = (VideoJson) jsonList.get(62);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----4、leave----------------------------");
            videoJson = (VideoJson) jsonList.get(63);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----5、leave----------------------------");
            videoJson = (VideoJson) jsonList.get(64);
            beforeTime = videoJson.getTimeSift();
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----6、查询绑定结果----------------------------");
            long timestamp = videoJson.getTimestamp() - 1;
            ApiResponse customerGoodsRes = customerGoods(timestamp, aCase, step);

            String customerId = getCustomerId(customerGoodsRes);

            long msgTime = videoJson.getTimestamp() + 1;
            leaveShop(msgTime, personId, customerId);

            String[] goodsIds = new String[0];
            checkResult(customerGoodsRes, goodsIds, 0);

            aCase.setResult("PASS"); //FAIL, PASS

        } catch (AssertionError e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
            e.printStackTrace();
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
        } finally {

            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);

            if (!IS_DEBUG) {
                qaDbUtil.saveToCaseTable(aCase);
            }
        }
    }

    @Test(priority = 14)
    public void singlePutTest14() {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName;
        String caseDesc = "";
        logger.info(caseDesc + "--------------------");

        Case aCase = new Case();
        failReason = "";

        int step = 0;

        VideoJson videoJson;
        String json;
        String[] secKey;

        try {
            aCase.setRequestData("1、pick 1-8 2个优酸乳；2、drop 1-8 1个优酸乳；3、leave；4、查询绑定结果" + "\n\n");

            videoJson = (VideoJson) jsonList.get(65);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();

            String personId = getEnterPersonId(json);

            waitTime = videoJson.getTimeSift() - beforeTime;
            Thread.sleep(waitTime);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----1、pick 1-8 2个优酸乳----------------------------");
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----2、drop 1-8 1个优酸乳----------------------------");
            videoJson = (VideoJson) jsonList.get(66);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----3、leave----------------------------");
            videoJson = (VideoJson) jsonList.get(67);
            beforeTime = videoJson.getTimeSift();
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----4、查询绑定结果----------------------------");
            long timestamp = videoJson.getTimestamp() - 1;

            ApiResponse customerGoodsRes = customerGoods(timestamp, aCase, step);

            String customerId = getCustomerId(customerGoodsRes);

            long msgTime = videoJson.getTimestamp() + 1;
            leaveShop(msgTime, personId, customerId);

            String[] goodsIds = {goods1_8, "", ""};
            checkResult(customerGoodsRes, goodsIds, 1);

            aCase.setResult("PASS"); //FAIL, PASS

        } catch (AssertionError e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
            e.printStackTrace();
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
        } finally {

            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);

            if (!IS_DEBUG) {
                qaDbUtil.saveToCaseTable(aCase);
            }
        }
    }

    @Test(priority = 15)
    public void singlePutTest15() {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName;
        String caseDesc = "";
        logger.info(caseDesc + "--------------------");

        Case aCase = new Case();
        failReason = "";

        int step = 0;

        VideoJson videoJson;
        String json;
        String[] secKey;

        try {
            aCase.setRequestData("1、enter；2、pick 2-2 2个鱼板面；3、drop 2-4 1个鱼板面；4、drop 1-4 1个鱼板面；" +
                    "5、leave；6、leave；7、查询绑定结果" + "\n\n");

            videoJson = (VideoJson) jsonList.get(68);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();

            String personId = getEnterPersonId(json);

            waitTime = videoJson.getTimeSift() - beforeTime;
            Thread.sleep(waitTime);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----1、enter----------------------------");
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----2、pick 2-2 2个鱼板面----------------------------");
            videoJson = (VideoJson) jsonList.get(69);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----3、drop 2-4 1个鱼板面----------------------------");
            videoJson = (VideoJson) jsonList.get(70);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----4、drop 1-4 1个鱼板面----------------------------");
            videoJson = (VideoJson) jsonList.get(71);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----5、leave----------------------------");
            videoJson = (VideoJson) jsonList.get(72);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----6、leave----------------------------");
            videoJson = (VideoJson) jsonList.get(73);
            beforeTime = videoJson.getTimeSift();
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----7、查询绑定结果----------------------------");
            long timestamp = videoJson.getTimestamp() - 1;
            ApiResponse customerGoodsRes = customerGoods(timestamp, aCase, step);

            String customerId = getCustomerId(customerGoodsRes);

            long msgTime = videoJson.getTimestamp() + 1;
            leaveShop(msgTime, personId, customerId);

            String[] goodsIds = new String[0];
            checkResult(customerGoodsRes, goodsIds, 0);

            aCase.setResult("PASS"); //FAIL, PASS

        } catch (AssertionError e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
            e.printStackTrace();
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
        } finally {

            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);

            if (!IS_DEBUG) {
                qaDbUtil.saveToCaseTable(aCase);
            }
        }
    }

    @Test(priority = 16)
    public void singlePutTest16() {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName;
        String caseDesc = "";
        logger.info(caseDesc + "--------------------");

        Case aCase = new Case();
        failReason = "";

        int step = 0;

        VideoJson videoJson;
        String json;
        String[] secKey;

        try {
            aCase.setRequestData("1、pick 1-5 1个黑米粥；2、pick 1-7 1个优酸乳；3、drop 1-2 1个优酸乳；4、leave；5、查询绑定结果" + "\n\n");

            videoJson = (VideoJson) jsonList.get(74);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();

            String personId = getEnterPersonId(json);

            waitTime = videoJson.getTimeSift() - beforeTime;
            Thread.sleep(waitTime);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----1、pick 1-5 1个黑米粥----------------------------");
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----2、pick 1-7 1个优酸乳----------------------------");
            videoJson = (VideoJson) jsonList.get(75);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----3、drop 1-2 1个优酸乳----------------------------");
            videoJson = (VideoJson) jsonList.get(76);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----4、leave----------------------------");
            videoJson = (VideoJson) jsonList.get(77);
            beforeTime = videoJson.getTimeSift();
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----5、查询绑定结果----------------------------");
            long timestamp = videoJson.getTimestamp() - 1;
            ApiResponse customerGoodsRes = customerGoods(timestamp, aCase, step);

            String customerId = getCustomerId(customerGoodsRes);

            long msgTime = videoJson.getTimestamp() + 1;
            leaveShop(msgTime, personId, customerId);

            String[] goodsIds = {goods1_7, "", ""};
            checkResult(customerGoodsRes, goodsIds, 1);

            aCase.setResult("PASS"); //FAIL, PASS

        } catch (AssertionError e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
            e.printStackTrace();
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
        } finally {

            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);

            if (!IS_DEBUG) {
                qaDbUtil.saveToCaseTable(aCase);
            }
        }
    }

    @Test(priority = 17)
    public void singlePutTest17() {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName;
        String caseDesc = "";
        logger.info(caseDesc + "--------------------");

        Case aCase = new Case();
        failReason = "";

        int step = 0;

        VideoJson videoJson;
        String json;
        String[] secKey;

        try {
            aCase.setRequestData("1、enter；2、pick 4-10 1个优酸乳；3、drop 1-7 1个优酸乳；4、leave；5、查询绑定结果" + "\n\n");

            videoJson = (VideoJson) jsonList.get(78);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();

            String personId = getEnterPersonId(json);

            waitTime = videoJson.getTimeSift() - beforeTime;
            Thread.sleep(waitTime);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----1、enter----------------------------");
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----2、pick 4-10 1个优酸乳----------------------------");
            videoJson = (VideoJson) jsonList.get(79);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----3、drop 1-7 1个优酸乳----------------------------");
            videoJson = (VideoJson) jsonList.get(80);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----4、leave----------------------------");
            videoJson = (VideoJson) jsonList.get(81);
            beforeTime = videoJson.getTimeSift();
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----5、查询绑定结果----------------------------");
            long timestamp = videoJson.getTimestamp() - 1;
            ApiResponse customerGoodsRes = customerGoods(timestamp, aCase, step);

            String customerId = getCustomerId(customerGoodsRes);

            long msgTime = videoJson.getTimestamp() + 1;
            leaveShop(msgTime, personId, customerId);

            String[] goodsIds = new String[0];
            checkResult(customerGoodsRes, goodsIds, 0);

            aCase.setResult("PASS"); //FAIL, PASS

        } catch (AssertionError e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
            e.printStackTrace();
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
        } finally {

            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);

            if (!IS_DEBUG) {
                qaDbUtil.saveToCaseTable(aCase);
            }
        }
    }

    @Test(priority = 18)
    public void singlePutTest18() {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName;
        String caseDesc = "";
        logger.info(caseDesc + "--------------------");

        Case aCase = new Case();
        failReason = "";

        int step = 0;

        VideoJson videoJson;
        String json;
        String[] secKey;

        try {
            aCase.setRequestData("1、enter；2、pick 4-4 1个可乐；3、leave；4、drop 2-2 1个可乐；5、leave；6、查询绑定结果" + "\n\n");

            videoJson = (VideoJson) jsonList.get(82);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();

            String personId = getEnterPersonId(json);

            waitTime = videoJson.getTimeSift() - beforeTime;
            Thread.sleep(waitTime);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----1、enter----------------------------");
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----2、pick 4-4 1个可乐----------------------------");
            videoJson = (VideoJson) jsonList.get(83);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----3、leave----------------------------");
            videoJson = (VideoJson) jsonList.get(84);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "---4、drop 2-2 1个可乐----------------------------");
            videoJson = (VideoJson) jsonList.get(85);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "---5、leave----------------------------");
            videoJson = (VideoJson) jsonList.get(86);
            beforeTime = videoJson.getTimeSift();
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----6、查询绑定结果----------------------------");
            long timestamp = videoJson.getTimestamp() - 1;
            ApiResponse customerGoodsRes = customerGoods(timestamp, aCase, step);

            String customerId = getCustomerId(customerGoodsRes);

            long msgTime = videoJson.getTimestamp() + 1;
            leaveShop(msgTime, personId, customerId);

            String[] goodsIds = {goods4_4, "", ""};
            checkResult(customerGoodsRes, goodsIds, 1);

            aCase.setResult("PASS"); //FAIL, PASS

        } catch (AssertionError e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
            e.printStackTrace();
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
        } finally {

            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);

            if (!IS_DEBUG) {
                qaDbUtil.saveToCaseTable(aCase);
            }
        }
    }

    @Test(priority = 19)
    public void singlePutTest19() {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName;
        String caseDesc = "";
        logger.info(caseDesc + "--------------------");

        Case aCase = new Case();
        failReason = "";

        int step = 0;

        VideoJson videoJson;
        String json;
        String[] secKey;

        try {
            aCase.setRequestData("1、enter；2、pick 4-10 1个优酸乳；3、leave；4、drop 1-5 1个优酸乳；5、drop 4-10 1个优酸乳;" +
                    "6、leave；7、查询绑定结果" + "\n\n");

            videoJson = (VideoJson) jsonList.get(87);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();

            String personId = getEnterPersonId(json);

            waitTime = videoJson.getTimeSift() - beforeTime;
            Thread.sleep(waitTime);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----1、enter----------------------------");
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----2、pick 4-4 1个可乐----------------------------");
            videoJson = (VideoJson) jsonList.get(88);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----3、leave----------------------------");
            videoJson = (VideoJson) jsonList.get(89);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "---4、drop 2-2 1个可乐----------------------------");
            videoJson = (VideoJson) jsonList.get(90);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "---5、leave----------------------------");
            videoJson = (VideoJson) jsonList.get(91);
            beforeTime = videoJson.getTimeSift();
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----6、查询绑定结果----------------------------");
            long timestamp = videoJson.getTimestamp() - 1;
            ApiResponse customerGoodsRes = customerGoods(timestamp, aCase, step);

            String customerId = getCustomerId(customerGoodsRes);

            long msgTime = videoJson.getTimestamp() + 1;
            leaveShop(msgTime, personId, customerId);

            String[] goodsIds = new String[0];
            checkResult(customerGoodsRes, goodsIds, 0);

            aCase.setResult("PASS"); //FAIL, PASS

        } catch (AssertionError e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
            e.printStackTrace();
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
        } finally {

            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);

            if (!IS_DEBUG) {
                qaDbUtil.saveToCaseTable(aCase);
            }
        }
    }

    @Test(priority = 20)
    public void singlePutTest20() {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName;
        String caseDesc = "";
        logger.info(caseDesc + "--------------------");

        Case aCase = new Case();
        failReason = "";

        int step = 0;

        VideoJson videoJson;
        String json;
        String[] secKey;

        try {
            aCase.setRequestData("1、enter；2、pick 4-4 1个旺仔；3、pick 4-5 1个旺仔；4、drop 1-5 1个旺仔；5、leave；6、查询绑定结果" + "\n\n");

            videoJson = (VideoJson) jsonList.get(93);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();

            String personId = getEnterPersonId(json);

            waitTime = videoJson.getTimeSift() - beforeTime;
            Thread.sleep(waitTime);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----1、enter----------------------------");
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----2、pick 4-4 1个旺仔----------------------------");
            videoJson = (VideoJson) jsonList.get(94);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----3、pick 4-5 1个旺仔----------------------------");
            videoJson = (VideoJson) jsonList.get(95);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "---4、drop 1-5 1个旺仔----------------------------");
            videoJson = (VideoJson) jsonList.get(96);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "---5、leave----------------------------");
            videoJson = (VideoJson) jsonList.get(97);
            beforeTime = videoJson.getTimeSift();
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----6、查询绑定结果----------------------------");
            long timestamp = videoJson.getTimestamp() - 1;
            ApiResponse customerGoodsRes = customerGoods(timestamp, aCase, step);

            String customerId = getCustomerId(customerGoodsRes);

            long msgTime = videoJson.getTimestamp() + 1;
            leaveShop(msgTime, personId, customerId);

            String[] goodsIds = {goods4_4, "", ""};
            checkResult(customerGoodsRes, goodsIds, 1);

            aCase.setResult("PASS"); //FAIL, PASS

        } catch (AssertionError e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
            e.printStackTrace();
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
        } finally {

            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);

            if (!IS_DEBUG) {
                qaDbUtil.saveToCaseTable(aCase);
            }
        }
    }

    @Test(priority = 21)
    public void singlePutTest21() {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName;
        String caseDesc = "";
        logger.info(caseDesc + "--------------------");

        Case aCase = new Case();
        failReason = "";

        int step = 0;

        VideoJson videoJson;
        String json;
        String[] secKey;

        try {
            aCase.setRequestData("1、pick 1-5 1个优酸乳；2、enter；3、leave；4、drop 4-10 1个优酸乳；5、leave；6、查询绑定结果" + "\n\n");

            videoJson = (VideoJson) jsonList.get(98);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();

            String personId = getEnterPersonId(json);

            waitTime = videoJson.getTimeSift() - beforeTime;
            Thread.sleep(waitTime);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----1、enter----------------------------");
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----2、pick 4-4 1个旺仔----------------------------");
            videoJson = (VideoJson) jsonList.get(99);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----3、pick 4-5 1个旺仔----------------------------");
            videoJson = (VideoJson) jsonList.get(100);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "---4、drop 1-5 1个旺仔----------------------------");
            videoJson = (VideoJson) jsonList.get(101);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "---5、leave----------------------------");
            videoJson = (VideoJson) jsonList.get(102);
            beforeTime = videoJson.getTimeSift();
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----6、查询绑定结果----------------------------");
            long timestamp = videoJson.getTimestamp() - 1;
            ApiResponse customerGoodsRes = customerGoods(timestamp, aCase, step);

            String customerId = getCustomerId(customerGoodsRes);

            long msgTime = videoJson.getTimestamp() + 1;
            leaveShop(msgTime, personId, customerId);

            String[] goodsIds = new String[0];
            checkResult(customerGoodsRes, goodsIds, 0);

            aCase.setResult("PASS"); //FAIL, PASS

        } catch (AssertionError e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
            e.printStackTrace();
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
        } finally {

            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);

            if (!IS_DEBUG) {
                qaDbUtil.saveToCaseTable(aCase);
            }
        }
    }

    @Test(priority = 22)
    public void singlePutTest22() {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName;
        String caseDesc = "";
        logger.info(caseDesc + "--------------------");

        Case aCase = new Case();
        failReason = "";

        int step = 0;

        VideoJson videoJson;
        String json;
        String[] secKey;

        try {
            aCase.setRequestData("1、pick 1-5 2个可乐；2、enter；3、drop 4-6 1个可乐；4、leave；5、查询绑定结果" + "\n\n");

            videoJson = (VideoJson) jsonList.get(98);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();

            String personId = getEnterPersonId(json);

            waitTime = videoJson.getTimeSift() - beforeTime;
            Thread.sleep(waitTime);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----1、pick 1-5 2个可乐----------------------------");
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----2、enter----------------------------");
            videoJson = (VideoJson) jsonList.get(99);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----3、drop 4-6 1个可乐----------------------------");
            videoJson = (VideoJson) jsonList.get(100);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "---4、leave----------------------------");
            videoJson = (VideoJson) jsonList.get(102);
            beforeTime = videoJson.getTimeSift();
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----5、查询绑定结果----------------------------");
            long timestamp = videoJson.getTimestamp() - 1;
            ApiResponse customerGoodsRes = customerGoods(timestamp, aCase, step);

            String customerId = getCustomerId(customerGoodsRes);

            long msgTime = videoJson.getTimestamp() + 1;
            leaveShop(msgTime, personId, customerId);

            String[] goodsIds = {goods1_5, "", ""};
            checkResult(customerGoodsRes, goodsIds, 1);

            aCase.setResult("PASS"); //FAIL, PASS

        } catch (AssertionError e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
            e.printStackTrace();
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
        } finally {

            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);

            if (!IS_DEBUG) {
                qaDbUtil.saveToCaseTable(aCase);
            }
        }
    }

    @Test(priority = 23)
    public void singlePutTest23() {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName;
        String caseDesc = "";
        logger.info(caseDesc + "--------------------");

        Case aCase = new Case();
        failReason = "";

        int step = 0;

        VideoJson videoJson;
        String json;
        String[] secKey;

        try {
            aCase.setRequestData("1、leave；2、pick 1-2 2个3+2；3、enter；4、drop 4-4 1个3+2；5、drop 1-2 1个3+2；6、leave；7、查询绑定结果" + "\n\n");

            videoJson = (VideoJson) jsonList.get(107);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();

            String personId = getEnterPersonId(json);

            waitTime = videoJson.getTimeSift() - beforeTime;
            Thread.sleep(waitTime);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----1、leave----------------------------");
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----2、pick 1-2 2个3+2----------------------------");
            videoJson = (VideoJson) jsonList.get(108);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----3、enter----------------------------");
            videoJson = (VideoJson) jsonList.get(109);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "---4、drop 4-4 1个3+2----------------------------");
            videoJson = (VideoJson) jsonList.get(110);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "---5、drop 1-2 1个3+2----------------------------");
            videoJson = (VideoJson) jsonList.get(111);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "---6、leave----------------------------");
            videoJson = (VideoJson) jsonList.get(112);
            beforeTime = videoJson.getTimeSift();
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----7、查询绑定结果----------------------------");
            long timestamp = videoJson.getTimestamp() - 1;
            ApiResponse customerGoodsRes = customerGoods(timestamp, aCase, step);

            String customerId = getCustomerId(customerGoodsRes);

            long msgTime = videoJson.getTimestamp() + 1;
            leaveShop(msgTime, personId, customerId);

            String[] goodsIds = new String[0];
            checkResult(customerGoodsRes, goodsIds, 0);

            aCase.setResult("PASS"); //FAIL, PASS

        } catch (AssertionError e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
            e.printStackTrace();
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
        } finally {

            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);

            if (!IS_DEBUG) {
                qaDbUtil.saveToCaseTable(aCase);
            }
        }
    }

    @Test(priority = 24)
    public void singlePutTest24() {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName;
        String caseDesc = "";
        logger.info(caseDesc + "--------------------");

        Case aCase = new Case();
        failReason = "";

        int step = 0;

        VideoJson videoJson;
        String json;
        String[] secKey;

        try {
            aCase.setRequestData("1、leave；2、pick 1-2 2个3+2；3、pick 1-7 1个黑米粥；4、enter；5、drop 4-5 1个黑米粥；" +
                    "6、leave；7、查询绑定结果" + "\n\n");

            videoJson = (VideoJson) jsonList.get(113);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();

            String personId = getEnterPersonId(json);

            waitTime = videoJson.getTimeSift() - beforeTime;
            Thread.sleep(waitTime);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----1、leave----------------------------");
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----2、pick 1-2 2个3+2----------------------------");
            videoJson = (VideoJson) jsonList.get(114);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----3、pick 1-7 1个黑米粥----------------------------");
            videoJson = (VideoJson) jsonList.get(115);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "---4、enter----------------------------");
            videoJson = (VideoJson) jsonList.get(116);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "---5、drop 4-5 1个黑米粥----------------------------");
            videoJson = (VideoJson) jsonList.get(117);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "---6、leave----------------------------");
            videoJson = (VideoJson) jsonList.get(118);
            beforeTime = videoJson.getTimeSift();
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----7、查询绑定结果----------------------------");
            long timestamp = videoJson.getTimestamp() - 1;
            ApiResponse customerGoodsRes = customerGoods(timestamp, aCase, step);

            String customerId = getCustomerId(customerGoodsRes);

            long msgTime = videoJson.getTimestamp() + 1;
            leaveShop(msgTime, personId, customerId);

            String[] goodsIds = {goods1_7, goods1_7, goods1_4};
            checkResult(customerGoodsRes, goodsIds, 3);

            aCase.setResult("PASS"); //FAIL, PASS

        } catch (AssertionError e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
            e.printStackTrace();
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
        } finally {

            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);

            if (!IS_DEBUG) {
                qaDbUtil.saveToCaseTable(aCase);
            }
        }
    }

    @Test(priority = 25)
    public void singlePutTest25() {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName;
        String caseDesc = "";
        logger.info(caseDesc + "--------------------");

        Case aCase = new Case();
        failReason = "";

        int step = 0;

        VideoJson videoJson;
        String json;
        String[] secKey;

        try {
            aCase.setRequestData("1、pick 1-4 2个好吃点；2、pick 1-2 1个优酸乳；3、pick 1-7 1个优酸乳；4、leave；5、查询绑定结果" + "\n\n");

            videoJson = (VideoJson) jsonList.get(119);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();

            String personId = getEnterPersonId(json);

            waitTime = videoJson.getTimeSift() - beforeTime;
            Thread.sleep(waitTime);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----1、pick 1-4 2个好吃点----------------------------");
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----2、pick 1-2 1个优酸乳----------------------------");
            videoJson = (VideoJson) jsonList.get(120);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----3、pick 1-7 1个优酸乳----------------------------");
            videoJson = (VideoJson) jsonList.get(121);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "---4、leave----------------------------");
            videoJson = (VideoJson) jsonList.get(122);
            beforeTime = videoJson.getTimeSift();
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----5、查询绑定结果----------------------------");
            long timestamp = videoJson.getTimestamp() - 1;
            ApiResponse customerGoodsRes = customerGoods(timestamp, aCase, step);

            String customerId = getCustomerId(customerGoodsRes);

            long msgTime = videoJson.getTimestamp() + 1;
            leaveShop(msgTime, personId, customerId);

            String[] goodsIds = {goods4_2, goods4_5, ""};
            checkResult(customerGoodsRes, goodsIds, 2);

            aCase.setResult("PASS"); //FAIL, PASS

        } catch (AssertionError e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
            e.printStackTrace();
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
        } finally {

            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);

            if (!IS_DEBUG) {
                qaDbUtil.saveToCaseTable(aCase);
            }
        }
    }

    private String getCustomerId(ApiResponse apiResponse) {
        String responseStr = JSON.toJSONString(apiResponse);
        JSONArray customerListJa = JSON.parseObject(responseStr).getJSONObject("data").getJSONArray("active_customer_list");
        String customerId = "";
        for (int i = 0; i < customerListJa.size(); i++) {
            JSONObject singleCustomer = customerListJa.getJSONObject(i);
            customerId = singleCustomer.getString("customer_id");
        }

        return customerId;
    }

    private void checkResult(ApiResponse apiResponse, String[] goodsIds, int size) throws Exception {

        String responseStr = JSON.toJSONString(apiResponse);
        JSONObject responseJo = JSON.parseObject(responseStr);

        JSONArray activeCustomerList = responseJo.getJSONObject("data").getJSONArray("active_customer_list");
        if (size == 0) {
            if (!(activeCustomerList == null || activeCustomerList.size() == 0)) {
                throw new Exception("绑定列表不为空！");
            }
        } else {
            if (activeCustomerList == null || activeCustomerList.size() == 0) {
                throw new Exception("绑定列表为空！");
            }
            JSONObject singleCustormer = activeCustomerList.getJSONObject(0);

            JSONArray attentionGoods = singleCustormer.getJSONArray("attention_goods");
            if (attentionGoods == null) {
                throw new Exception("attention_goods is null");
            }

            Assert.assertEquals(attentionGoods.size(), size, "绑定商品数不符，expect：" + size + "个，actual：" + attentionGoods.size());

            if (size == 1) {
                String goodIdRes = attentionGoods.getJSONObject(0).getString("goods_id");
                Assert.assertEquals(goodIdRes, goodsIds[0], "绑定商品不符，expect：" + goodsIds[0] + "，actual：" + goodIdRes);
            } else if (size == 2) {
                String goodIdRes0 = attentionGoods.getJSONObject(0).getString("goods_id");
                String goodIdRes1 = attentionGoods.getJSONObject(1).getString("goods_id");

                String[] resGoodsIds = {goodIdRes0, goodIdRes1, ""};

                Assert.assertEqualsNoOrder(resGoodsIds, goodsIds, "绑定商品错误，expect：" + Arrays.toString(goodsIds)
                        + "，actual：" + Arrays.toString(resGoodsIds));
            } else if (size == 3) {
                String goodIdRes0 = attentionGoods.getJSONObject(0).getString("goods_id");
                String goodIdRes1 = attentionGoods.getJSONObject(1).getString("goods_id");
                String goodIdRes2 = attentionGoods.getJSONObject(2).getString("goods_id");

                String[] resGoodsIds = {goodIdRes0, goodIdRes1, goodIdRes2};

                Assert.assertEqualsNoOrder(resGoodsIds, goodsIds, "绑定商品错误，expect：" + Arrays.toString(goodsIds)
                        + "，actual：" + Arrays.toString(resGoodsIds));
            }
        }
    }

    private ApiResponse sendRequestWithGate(String router, String[] secKey, String json) throws Exception {
        try {
            Credential credential = new Credential(AK, SK);
            // 封装request对象
            String requestId = UUID.randomUUID().toString();
            ApiRequest apiRequest = new ApiRequest.Builder()
                    .uid(UID)
                    .appId(APP_ID)
                    .requestId(requestId)
                    .version(SdkConstant.API_VERSION)
                    .router(router)
                    .dataSecKey(secKey)
                    .dataBizData(JSON.parseObject(json))
                    .build();

            // client 请求
            ApiClient apiClient = new ApiClient("http://dev.api.winsenseos.cn/retail/api/data/device", credential);
            apiResponse = apiClient.doRequest(apiRequest);
            logMine.printImportant("apiRequest" + JSON.toJSONString(apiRequest));
            logMine.printImportant("apiResponse" + JSON.toJSONString(apiResponse));
        } catch (SdkClientException e) {
            String msg = e.getMessage();
            throw new Exception(msg);
        } catch (Exception e) {
            throw e;
        }
        return apiResponse;
    }

    private void setNewJsonAndTimestamp(String oldJson, String type, long timeshift, VideoJson videoJson) {

        JSONObject jsonJo = JSON.parseObject(oldJson);

        JSONObject data = jsonJo.getJSONObject("data");
        JSONObject bizData = data.getJSONObject("biz_data");

        String oldType = bizData.getString("type");
        Assert.assertEquals(oldType, type, "类型不符，文件名中：" + type + ", json中：" + oldType);

        String[] secKey = null;
        if ("ENTER".equals(type) || "LEAVE".equals(type)) {
            secKey = jsonArrToStringArr(data.getJSONArray("sec_key_path"));
        }

        long oldEndTime = bizData.getLong("end_time");
        long oldStartTime = bizData.getLong("start_time");
        long oldTimeStamp = bizData.getLong("timestamp");

        long endTime = currentTime + timeshift;
        long startTime = endTime - (oldEndTime - oldStartTime);
        long timestamp = endTime - (oldEndTime - oldTimeStamp);

        bizData.put("start_time", startTime);
        bizData.put("end_time", endTime);
        bizData.put("timestamp", timestamp);

        videoJson.setTimestamp(timestamp);
        videoJson.setJson(bizData + "");
        videoJson.setSecKey(secKey);
    }

    public void sort(ArrayList videoJsonList) {
        long insertNote;
        int i, j;
        for (i = 1; i < videoJsonList.size(); i++) {
            VideoJson temp = (VideoJson) videoJsonList.get(i);
            insertNote = getSingleTimeShift(videoJsonList, i);

            j = i - 1;

            while (j >= 0 && insertNote < getSingleTimeShift(videoJsonList, j)) {
                videoJsonList.set(j + 1, videoJsonList.get(j));
                j--;
            }

            videoJsonList.set(j + 1, temp);

        }

        setIdAfterSort(videoJsonList);
    }

    public long getSingleTimeShift(ArrayList videoJsonList, int i) {

        VideoJson videoJson = (VideoJson) videoJsonList.get(i);
        return videoJson.getTimeSift();
    }

    public void setIdAfterSort(ArrayList videoJsonList) {

        for (int i = 0; i < videoJsonList.size(); i++) {
            VideoJson videoJson = (VideoJson) videoJsonList.get(i);
            videoJson.setId(i + 1);
        }
    }

    public String getEnterPersonId(String json) {
        JSONObject jsonJo = JSON.parseObject(json);
        String personId = jsonJo.getString("person_id");
        return personId;
    }

    public String getPickPersonId(String json) throws Exception {
        JSONObject jsonJo = JSON.parseObject(json);
        JSONArray faceListja = jsonJo.getJSONObject("data").getJSONArray("face_list");
        String personId = "";

        if (faceListja == null || faceListja.size() == 0) {
            personId = "";
        } else {
            JSONObject singleFaceJo = faceListja.getJSONObject(0);
            personId = singleFaceJo.getString("person_id");

        }
        return personId;
    }

    public void sendResAndReqIdToDbApi(ApiResponse response, Case acase, int step) {

        if (response != null) {
//            将requestId存入数据库
            String requestId = response.getRequestId();
            String requestDataBefore = acase.getRequestData();
            if (requestDataBefore != null && requestDataBefore.trim().length() > 0) {
                acase.setRequestData(requestDataBefore + "(" + step + ") " + requestId + "\n");
            } else {
                acase.setRequestData("(" + step + ") " + requestId + "\n");
            }
        }
    }

    private void checkCodeApi(ApiResponse apiResponse, String router, int expectCode) throws Exception {
        try {
            String requestId = apiResponse.getRequestId();
            if (apiResponse.getCode() != expectCode) {
                String msg = "request id: " + requestId + ", gateway: /retail/api/data/device, router: " + router + ". \nresponse: " + JSON.toJSONString(apiResponse) +
                        "actual code: " + apiResponse.getCode() + " expect code: " + expectCode + ".";
                throw new Exception(msg);
            }
        } catch (Exception e) {
            throw e;
        }
    }

    public void readCsv() throws Exception {
        File file = new File(filePath);
        filePath = filePath.replace("/",File.separator);
        File[] files = file.listFiles();

        for (int i = 0; i < files.length; i++) {
            BufferedReader bd = new BufferedReader(new FileReader(files[i]));
            String fileName = files[i].getName();
            String[] temp = fileName.split("-");
            long timeShift = Long.valueOf(temp[0]);
            String type = temp[1].substring(0, temp[1].lastIndexOf("."));

            String oldJson = bd.readLine();

            VideoJson videoJson = new VideoJson();

            videoJson.setType(type);
            videoJson.setTimeSift(timeShift);
            videoJson.setJson(oldJson);

            jsonList.add(videoJson);

        }

        sort(jsonList);

        for (int i = 0; i < jsonList.size(); i++) {
            VideoJson videoJson = (VideoJson) jsonList.get(i);
            setNewJsonAndTimestamp(videoJson.getJson(), videoJson.getType(), videoJson.getTimeSift(), videoJson);

//            System.out.println(videoJson.getId()+"=" +  videoJson.getTimeSift() + "=" +videoJson.getType());
//            System.out.println(videoJson.getId()+"=" +  videoJson.getTimeSift() + "=" +videoJson.getType()  +"=" + videoJson.getJson());

//            jsonList.add(i, videoJson);
//            不用再add了，因为list中存的是引用，取出来修改以后还是这个引用，所以取出来之后修改的就是list中本来的对象
        }
        jsonList.add(0, null);
    }

    @BeforeSuite
    public void initial() throws Exception {
        init();
        readCsv();
        qaDbUtil.openConnection();
    }

    @AfterSuite
    public void clean() {
        qaDbUtil.closeConnection();
    }

    public void setBasicParaToDB(Case aCase, String caseName, String caseDesc, String ciCaseName) {
        aCase.setApplicationId(APP_ID_DB);
        aCase.setConfigId(CONFIG_ID);
        aCase.setCaseName(caseName);
        aCase.setCaseDescription(caseDesc);
        aCase.setCiCmd(CI_CMD + ciCaseName);
        aCase.setQaOwner("廖祥茹");
        aCase.setExpect("能够绑定成功");
    }


    public String[] jsonArrToStringArr(JSONArray jsonArray) {
        String[] strArr = new String[jsonArray.size()];
        for (int i = 0; i < jsonArray.size(); i++) {
            strArr[i] = jsonArray.getString(i);
        }
        return strArr;
    }

}
