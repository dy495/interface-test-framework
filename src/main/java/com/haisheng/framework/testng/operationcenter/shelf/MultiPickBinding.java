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
import com.haisheng.framework.util.HttpExecutorUtil;
import com.haisheng.framework.util.OssClientUtil;
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

public class MultiPickBinding {

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
    String goods3_3 = "6903252000771";
    String goods3_5 = "6911988009784";
    String goods4_2 = "6923450657935";
    String goods4_4 = "6906907101229";
    String goods4_5 = "6920584471215";
    String goods4_6 = "6926892566087";
    String goods4_9 = "6920907800302";
    String goods4_10 = "6907992513652";

    private boolean IS_DEBUG = true;

    public String filePath1 = "src/main/java/com/haisheng/framework/testng/operationcenter/shelf/multiPickJson1";
    public String filePath2 = "src/main/java/com/haisheng/framework/testng/operationcenter/shelf/multiPickJson2";

    ArrayList<String> customerIds = new ArrayList();

    private long currentTime = System.currentTimeMillis() + 24 * 60 * 60 * 1000;

    private long waitTime = 0L;
    private long beforeTime = 0L;

    private void customerMessage(String json, String[] secKey, Case acase, int step) throws Exception {
        logger.info("\n");
        logger.info("------------customer message!-----------------------");
        String router = "/commodity/external/CUSTOMER_MESSAGE/v1.0";
        String message = "";

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
        logger.info("\n");
        logger.info("------------customer goods!-----------------------");
        String router = "/commodity/external/SHELVES_CUSTOMER_LIST/v1.0";
        String[] secKey = new String[]{};
        String message = "";

        //暂时不需要测试type为ENTER和LEAVE的，人物相关的都不支持；PICK和DROP不要传入face和person_id

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

    private String analysisPersonSync(long startTime,long endTime,JSONArray faces,String person) throws Exception {
        logger.info("\n");
        logger.info("------------ANALYSIS_PERSON_SYNC!-----------------------");
        String url = "http://60.205.83.160/scenario/who/ANALYSIS_PERSON_SYNC/v1.0";
        String message = "";
        String resStr;

        try {
            String json =
                    "{\n" +
                            "    \"app_id\":\"" + APP_ID + "\",\n" +
                            "    \"device_id\":\"8_test_733203be-9e4b-11e9-a23e-00163e0ae160\",\n" +
                            "    \"end_time\":"  +   endTime + "," +
                            "    \"face_data\":[\n" +
                            "        {\n" +
                            "            \"axis_str\":\"[1050,432,1224,660]\",\n" +
                            "            \"blur\":0,\n" +
                            "            \"face_url\":\"http://retail-huabei2.oss-cn-beijing-internal.aliyuncs.com/commodity_file_dev/customer/72f24c1f-5681-40a5-933f-26676d765449_1566371214933?Expires=1567583893&OSSAccessKeyId=LTAIlYpjA39n18Yr&Signature=dwMcBqbYX8kPAFsP2%2Bk5RWoIfUQ%3D\",\n" +
                            "            \"frame_time\":1566371214933,\n" +
                            "            \"illumination\":0,\n" +
                            "            \"mask\":0,\n" +
                            "            \"pitch\":-6.1709704,\n" +
                            "            \"quality\":0.7161318,\n" +
                            "            \"roll\":8.871774,\n" +
                            "            \"sunglasses\":0,\n" +
                            "            \"yaw\":3.1207001\n" +
                            "        }\n" +
                            "    ],\n" +
                            "    \"image_data\":{\n" +
                            "        \"height\":112,\n" +
                            "        \"width\":96\n" +
                            "    },\n" +
                            "    \"request_id\":\"" + UUID.randomUUID() + "\",\n" +
                            "    \"scene_name\":\"FACE_FRONT\",\n" +
                            "    \"scopes\":[\n" +
                            "        \"" + SHOP_ID  + "\"\n" +
                            "    ],\n" +
                            "    \"start_time\":" + startTime +
                            "}";
            JSONObject jsonJo = JSON.parseObject(json);

            for (int i = 0; i < faces.size(); i++) {
                JSONObject singleFace = faces.getJSONObject(i);
                OssClientUtil ossClientUtil = new OssClientUtil();
                singleFace.put("face_url",ossClientUtil.genUrl("QA_TEST/" + person + "_1.jpg"));
                singleFace.remove("axis_str");
                singleFace.put("axis_str","[1050,432,1224,660]");
                singleFace.remove("image");
            }

            jsonJo.put("face_data",faces);

            json = jsonJo.toJSONString();

            resStr = sendRequestWithUrl(url, json);

        } catch (Exception e) {
            failReason = e.toString();
            Assert.fail(message);
            throw e;
        }

        return resStr;
    }

    @Test(priority = 1)
    public void multiPickTest1() {
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
            aCase.setRequestData("1、enter(yu)；2、enter(xie)；3、pick 4-2 一瓶益达(yu)；4、pick 4-9一个蛋黄派(xie)；" +
                    "5、leave(yu)；6、leave(xie)；7、查询绑定结果" + "\n\n");

            videoJson = (VideoJson) jsonList.get(1);

            long msgTime = videoJson.getTimestamp() - 1;
            json = videoJson.getJson();
            String personId = getEnterPersonId(json);

            videoJson = (VideoJson) jsonList.get(5);
            json = videoJson.getJson();
            JSONObject jo = JSON.parseObject(json);

            String resStr = analysisPersonSync(jo.getLong("start_time"),jo.getLong("end_time"),jo.getJSONArray("face_list"),"yu");

            String yuCustomerId = getCustomerIdFromSync(resStr);
            customerIds.add(yuCustomerId);

            videoJson = (VideoJson) jsonList.get(6);
            json = videoJson.getJson();
            jo = JSON.parseObject(json);

            resStr = analysisPersonSync(jo.getLong("start_time"),jo.getLong("end_time"),jo.getJSONArray("face_list"),"xie");

            String xieCustomerId = getCustomerIdFromSync(resStr);
            customerIds.add(yuCustomerId);


            for (int i = 0; i < customerIds.size(); i++) {
                leaveShop(msgTime,personId,customerIds.get(i));
            }

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----1、enter----------------------------");
            videoJson = (VideoJson) jsonList.get(1);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----2、enter----------------------------");
            videoJson = (VideoJson) jsonList.get(2);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("-------------" + (++step) + "---3、pick 4-2 一瓶益达----------------");
            videoJson = (VideoJson) jsonList.get(3);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("-------------" + (++step) + "---4、pick 4-9一个蛋黄派----------------");
            videoJson = (VideoJson) jsonList.get(4);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("------------" + (++step) + "-----5、leave----------------------------");
            videoJson = (VideoJson) jsonList.get(5);
            long timestamp = videoJson.getTimestamp() - 1000;
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("------------" + (++step) + "-----6、leave----------------------------");
            videoJson = (VideoJson) jsonList.get(6);
            beforeTime = videoJson.getTimeSift();
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

//            因为云端查询的是>json传递时间的时间，太小又会查多了，所以只－1ms
            logger.info("\n\n");
            logger.info("----------" + (++step) + "----7、查询绑定结果----------------------------");
            apiResponse = customerGoods(timestamp, aCase, step);

            addCustomerIds(apiResponse);

            String[] yuGoodsIds = {goods4_2};
            String[] xieGoodsIds = {goods4_9};
            checkResult(apiResponse, yuGoodsIds, yuCustomerId, "yu");
            checkResult(apiResponse, xieGoodsIds, xieCustomerId, "xie");

            msgTime = videoJson.getTimestamp() + 1;

            for (int i = 0; i < customerIds.size(); i++) {
                leaveShop(msgTime, personId, customerIds.get(i));
            }
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
    public void multiPickTest2() {
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
            aCase.setRequestData("1、enter(yu)；2、pick 4-4 一瓶可乐(yu)；3、enter(xie)；4、pick 4-6一个黑米粥；" +
                    "5、leave(xie)；6、leave(yu)；7、查询绑定结果" + "\n\n");

            videoJson = (VideoJson) jsonList.get(7);
            json = videoJson.getJson();

            long msgTime = videoJson.getTimestamp() - 1;
            String personId = getEnterPersonId(json);

            waitTime = videoJson.getTimeSift() - beforeTime;
            Thread.sleep(waitTime);

            videoJson = (VideoJson) jsonList.get(11);
            json = videoJson.getJson();
            JSONObject jo = JSON.parseObject(json);

            String resStr = analysisPersonSync(jo.getLong("start_time"),jo.getLong("end_time"),jo.getJSONArray("face_list"),"xie");

            String xieCustomerId = getCustomerIdFromSync(resStr);
            customerIds.add(xieCustomerId);

            videoJson = (VideoJson) jsonList.get(12);
            json = videoJson.getJson();
            jo = JSON.parseObject(json);

            resStr = analysisPersonSync(jo.getLong("start_time"),jo.getLong("end_time"),jo.getJSONArray("face_list"),"yu");

            String yuCustomerId = getCustomerIdFromSync(resStr);
            customerIds.add(yuCustomerId);

            for (int i = 0; i < customerIds.size(); i++) {
                leaveShop(msgTime,personId,customerIds.get(i));
            }

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----1、enter(yu)----------------------------");
            videoJson = (VideoJson) jsonList.get(7);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----2、pick 4-4 一瓶可乐----------------------------");
            videoJson = (VideoJson) jsonList.get(8);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("-------------" + (++step) + "---3、enter----------------");
            videoJson = (VideoJson) jsonList.get(9);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("-------------" + (++step) + "---4、pick 4-6一个黑米粥----------------");
            videoJson = (VideoJson) jsonList.get(10);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("------------" + (++step) + "-----5、leave----------------------------");
            videoJson = (VideoJson) jsonList.get(11);
            long timestamp = videoJson.getTimestamp() - 1;
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("------------" + (++step) + "-----6、leave----------------------------");
            videoJson = (VideoJson) jsonList.get(12);
            beforeTime = videoJson.getTimeSift();
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----7、查询绑定结果----------------------------");
            apiResponse = customerGoods(timestamp, aCase, step);

            String[] yuGoodsId = {goods4_4};
            String[] xieGoodsId = {goods4_6};

            checkResult(apiResponse, yuGoodsId,yuCustomerId,"yu");
            checkResult(apiResponse, xieGoodsId,xieCustomerId,"xie");

            addCustomerIds(apiResponse);

            msgTime = videoJson.getTimestamp() + 1;

            for (int i = 0; i < customerIds.size(); i++) {
                leaveShop(msgTime, personId, customerIds.get(i));
            }
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
    public void multiPickTest3() {
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
            aCase.setRequestData("1、enter(yu)；2、enter(xie)；3、pick 4-6一个黑米粥；" +
                    "4、leave(xie)；5、leave(yu)；6、查询绑定结果" + "\n\n");

            videoJson = (VideoJson) jsonList.get(13);
            json = videoJson.getJson();

            long msgTime = videoJson.getTimestamp() - 1;
            String personId = getEnterPersonId(json);

            waitTime = videoJson.getTimeSift() - beforeTime;
            Thread.sleep(waitTime);

            videoJson = (VideoJson) jsonList.get(16);
            json = videoJson.getJson();
            JSONObject jo = JSON.parseObject(json);

            String resStr = analysisPersonSync(jo.getLong("start_time"),jo.getLong("end_time"),jo.getJSONArray("face_list"),"xie");

            String xieCustomerId = getCustomerIdFromSync(resStr);

            customerIds.add(xieCustomerId);

            for (int i = 0; i < customerIds.size(); i++) {
                leaveShop(msgTime, personId, customerIds.get(i));
            }

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----1、enter(yu)----------------------------");
            videoJson = (VideoJson) jsonList.get(13);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----2、enter(xie)----------------------------");
            videoJson = (VideoJson) jsonList.get(14);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("-------------" + (++step) + "---4、pick 4-6一个黑米粥----------------");
            videoJson = (VideoJson) jsonList.get(15);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("------------" + (++step) + "-----5、leave(xie)----------------------------");
            videoJson = (VideoJson) jsonList.get(16);
            long timestamp = videoJson.getTimestamp() - 1;
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("------------" + (++step) + "-----6、leave(yu)----------------------------");
            videoJson = (VideoJson) jsonList.get(17);
            beforeTime = videoJson.getTimeSift();
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----7、查询绑定结果----------------------------");
            apiResponse = customerGoods(timestamp, aCase, step);

            addCustomerIds(apiResponse);

            String[] xieGoodsId = {goods4_6};

            checkResult(apiResponse, xieGoodsId,xieCustomerId,"xie");

            msgTime = videoJson.getTimestamp() + 1;

            for (int i = 0; i < customerIds.size(); i++) {
                leaveShop(msgTime, personId, customerIds.get(i));
            }
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
    public void multiPickTest4() {
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
            aCase.setRequestData("1、enter(yu)；2、enter(xie)；3、pick 4-4 一瓶可乐；4、pick 4-9一个蛋黄派；" +
                    "5、leave(xie)；6、leave(yu)；7、查询绑定结果" + "\n\n");

            videoJson = (VideoJson) jsonList.get(18);
            json = videoJson.getJson();

            long msgTime = videoJson.getTimestamp() - 1;
            String personId = getEnterPersonId(json);

            waitTime = videoJson.getTimeSift() - beforeTime;
            Thread.sleep(waitTime);

            videoJson = (VideoJson) jsonList.get(22);
            json = videoJson.getJson();
            JSONObject jo = JSON.parseObject(json);

            String resStr = analysisPersonSync(jo.getLong("start_time"),jo.getLong("end_time"),jo.getJSONArray("face_list"),"xie");

            String xieCustomerId = getCustomerIdFromSync(resStr);
            customerIds.add(xieCustomerId);

            for (int i = 0; i < customerIds.size(); i++) {
                leaveShop(msgTime, personId, customerIds.get(i));
            }

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----1、enter(yu)----------------------------");
            videoJson = (VideoJson) jsonList.get(18);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----2、enter(xie)----------------------------");
            videoJson = (VideoJson) jsonList.get(19);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----3、pick 4-4 一瓶可乐----------------------------");
            videoJson = (VideoJson) jsonList.get(20);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("-------------" + (++step) + "---4、pick 4-9一个蛋黄派----------------");
            videoJson = (VideoJson) jsonList.get(21);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("------------" + (++step) + "-----5、leave(xie)----------------------------");
            videoJson = (VideoJson) jsonList.get(22);
            long timestamp = videoJson.getTimestamp() - 1;
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("------------" + (++step) + "-----6、leave(yu)----------------------------");
            videoJson = (VideoJson) jsonList.get(23);
            beforeTime = videoJson.getTimeSift();
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----7、查询绑定结果----------------------------");
            apiResponse = customerGoods(timestamp, aCase, step);

            addCustomerIds(apiResponse);

            String[] xieGoodsIds = {goods4_4,goods4_9};
            checkResult(apiResponse, xieGoodsIds, xieCustomerId,"xie");

            msgTime = videoJson.getTimestamp() + 1;

            for (int i = 0; i < customerIds.size(); i++) {
                leaveShop(msgTime, personId, customerIds.get(i));
            }

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
    public void multiPickTest5() {
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
            aCase.setRequestData("1、enter(yu)；2、enter(xie)；3、pick 4-2 一瓶益达；4、pick 4-10一个优酸乳；" +
                    "5、pick 4-4一个可乐；6、leave(xie)；7、leave(yu)；8、查询绑定结果" + "\n\n");

            videoJson = (VideoJson) jsonList.get(24);
            json = videoJson.getJson();

            long msgTime = videoJson.getTimestamp() - 1;
            String personId = getEnterPersonId(json);

            waitTime = videoJson.getTimeSift() - beforeTime;
            Thread.sleep(waitTime);

            videoJson = (VideoJson) jsonList.get(29);
            json = videoJson.getJson();
            JSONObject jo = JSON.parseObject(json);

            String resStr = analysisPersonSync(jo.getLong("start_time"),jo.getLong("end_time"),jo.getJSONArray("face_list"),"xie");

            String xieCustomerId = getCustomerIdFromSync(resStr);

            videoJson = (VideoJson) jsonList.get(30);
            json = videoJson.getJson();
            jo = JSON.parseObject(json);

            resStr = analysisPersonSync(jo.getLong("start_time"),jo.getLong("end_time"),jo.getJSONArray("face_list"),"yu");

            String yuCustomerId = getCustomerIdFromSync(resStr);

            customerIds.add(xieCustomerId);
            customerIds.add(yuCustomerId);

            for (int i = 0; i < customerIds.size(); i++) {
                leaveShop(msgTime, personId, customerIds.get(i));
            }

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----1、enter(yu)----------------------------");
            videoJson = (VideoJson) jsonList.get(24);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);


            logger.info("\n\n");
            logger.info("----------" + (++step) + "----2、enter(xie)----------------------------");
            videoJson = (VideoJson) jsonList.get(25);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----3、pick 4-2 一瓶益达----------------------------");
            videoJson = (VideoJson) jsonList.get(26);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("-------------" + (++step) + "---4、pick 4-10一个优酸乳----------------");
            videoJson = (VideoJson) jsonList.get(27);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("-------------" + (++step) + "---5、pick 4-4一个可乐----------------");
            videoJson = (VideoJson) jsonList.get(28);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("------------" + (++step) + "-----6、leave(xie)----------------------------");
            videoJson = (VideoJson) jsonList.get(29);
            long timestamp = videoJson.getTimestamp() - 1;
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("------------" + (++step) + "-----7、leave(yu)----------------------------");
            videoJson = (VideoJson) jsonList.get(30);
            beforeTime = videoJson.getTimeSift();
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----8、查询绑定结果----------------------------");
            apiResponse = customerGoods(timestamp, aCase, step);

            addCustomerIds(apiResponse);

            String[] xieGoodsIds = {goods4_4,goods4_10};
            checkResult(apiResponse, xieGoodsIds, xieCustomerId,"xie");

            String[] yuGoodsIds = {goods4_2};
            checkResult(apiResponse, yuGoodsIds, yuCustomerId,"yu");

            msgTime = videoJson.getTimestamp() + 1;

            for (int i = 0; i < customerIds.size(); i++) {
                leaveShop(msgTime, personId, customerIds.get(i));
            }
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
    public void multiPickTest6() {
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
            aCase.setRequestData("1、enter(yu)；2、pick 4-9 一个蛋黄派；3、enter(xie)；4、pick 4-10一个优酸乳；" +
                    "5、leave(xie)；6、leave(yu)；7、查询绑定结果" + "\n\n");

            videoJson = (VideoJson) jsonList.get(31);
            json = videoJson.getJson();

            long msgTime = videoJson.getTimestamp() - 1;
            String personId = getEnterPersonId(json);

            waitTime = videoJson.getTimeSift() - beforeTime;
            Thread.sleep(waitTime);

            videoJson = (VideoJson) jsonList.get(35);
            json = videoJson.getJson();
            JSONObject jo = JSON.parseObject(json);

            String resStr = analysisPersonSync(jo.getLong("start_time"),jo.getLong("end_time"),jo.getJSONArray("face_list"),"xie");

            String xieCustomerId = getCustomerIdFromSync(resStr);

            videoJson = (VideoJson) jsonList.get(36);
            json = videoJson.getJson();
            jo = JSON.parseObject(json);

            resStr = analysisPersonSync(jo.getLong("start_time"),jo.getLong("end_time"),jo.getJSONArray("face_list"),"yu");

            String yuCustomerId = getCustomerIdFromSync(resStr);

            customerIds.add(xieCustomerId);
            customerIds.add(yuCustomerId);

            for (int i = 0; i < customerIds.size(); i++) {
                leaveShop(msgTime, personId, customerIds.get(i));
            }

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----1、enter(yu)----------------------------");
            videoJson = (VideoJson) jsonList.get(31);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----2、pick 4-9 一个蛋黄派----------------------------");
            videoJson = (VideoJson) jsonList.get(32);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----3、enter(xie)----------------------------");
            videoJson = (VideoJson) jsonList.get(33);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("-------------" + (++step) + "---4、pick 4-10一个优酸乳----------------");
            videoJson = (VideoJson) jsonList.get(34);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("------------" + (++step) + "-----5、leave(xie)----------------------------");
            videoJson = (VideoJson) jsonList.get(35);
            long timestamp = videoJson.getTimestamp() - 1;
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("------------" + (++step) + "-----6、leave(yu)----------------------------");
            videoJson = (VideoJson) jsonList.get(36);
            beforeTime = videoJson.getTimeSift();
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----7、查询绑定结果----------------------------");
            apiResponse = customerGoods(timestamp, aCase, step);

            addCustomerIds(apiResponse);

            String[] xieGoodsIds = {goods4_10};
            checkResult(apiResponse, xieGoodsIds, xieCustomerId,"xie");

            String[] yuGoodsIds = {goods4_9};
            checkResult(apiResponse, yuGoodsIds, yuCustomerId,"yu");

            msgTime = videoJson.getTimestamp() + 1;

            for (int i = 0; i < customerIds.size(); i++) {
                leaveShop(msgTime, personId, customerIds.get(i));
            }
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
    public void multiPickTest7() {
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
            aCase.setRequestData("1、enter(yu)；2、enter(xie)；3、enter(liao)；4、pick 4-10 一个优酸乳；" +
                    "5、pick 3-3 小牛肉面；6、pick 4-2一个益达；7、leave(liao)；8、leave(xie)；9、leave(yu)；10、查询绑定结果" + "\n\n");

            videoJson = (VideoJson) jsonList.get(37);
            json = videoJson.getJson();

            long msgTime = videoJson.getTimestamp() - 1;
            String personId = getEnterPersonId(json);

            waitTime = videoJson.getTimeSift() - beforeTime;
            Thread.sleep(waitTime);

            videoJson = (VideoJson) jsonList.get(43);
            json = videoJson.getJson();
            JSONObject jo = JSON.parseObject(json);

            String resStr = analysisPersonSync(jo.getLong("start_time"),jo.getLong("end_time"),jo.getJSONArray("face_list"),"liao");

            String liaoCustomerId = getCustomerIdFromSync(resStr);

            videoJson = (VideoJson) jsonList.get(44);
            json = videoJson.getJson();
            jo = JSON.parseObject(json);

            resStr = analysisPersonSync(jo.getLong("start_time"),jo.getLong("end_time"),jo.getJSONArray("face_list"),"xie");

            String xieCustomerId = getCustomerIdFromSync(resStr);

            videoJson = (VideoJson) jsonList.get(45);
            json = videoJson.getJson();
            jo = JSON.parseObject(json);

            resStr = analysisPersonSync(jo.getLong("start_time"),jo.getLong("end_time"),jo.getJSONArray("face_list"),"yu");

            String yuCustomerId = getCustomerIdFromSync(resStr);

            customerIds.add(xieCustomerId);
            customerIds.add(yuCustomerId);
            customerIds.add(liaoCustomerId);

            for (int i = 0; i < customerIds.size(); i++) {
                leaveShop(msgTime, personId, customerIds.get(i));
            }

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----1、enter(yu)----------------------------");
            videoJson = (VideoJson) jsonList.get(37);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----2、enter(xie)----------------------------");
            videoJson = (VideoJson) jsonList.get(38);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----3、enter(liao)----------------------------");
            videoJson = (VideoJson) jsonList.get(39);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----4、pick 4-10 一个优酸乳----------------------------");
            videoJson = (VideoJson) jsonList.get(40);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----5、pick 3-3小牛肉面----------------------------");
            videoJson = (VideoJson) jsonList.get(41);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("-------------" + (++step) + "---6、pick 4-2 一个益达----------------");
            videoJson = (VideoJson) jsonList.get(42);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("-------------" + (++step) + "---7、leave(liao)----------------");
            videoJson = (VideoJson) jsonList.get(43);
            long timestamp = videoJson.getTimestamp() - 1;
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("------------" + (++step) + "-----8、leave(xie)----------------------------");
            videoJson = (VideoJson) jsonList.get(44);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("------------" + (++step) + "-----9、leave(yu)----------------------------");
            videoJson = (VideoJson) jsonList.get(45);
            beforeTime = videoJson.getTimeSift();
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----10、查询绑定结果----------------------------");
            apiResponse = customerGoods(timestamp, aCase, step);

            addCustomerIds(apiResponse);

            String[] xieGoodsIds = {goods3_3};
            checkResult(apiResponse, xieGoodsIds, xieCustomerId,"xie");

            String[] yuGoodsIds = {goods4_10};
            checkResult(apiResponse, yuGoodsIds, yuCustomerId,"yu");

            String[] liaoGoodsIds = {goods4_2};
            checkResult(apiResponse, liaoGoodsIds, liaoCustomerId,"liao");

            msgTime = videoJson.getTimestamp() + 1;

            for (int i = 0; i < customerIds.size(); i++) {
                leaveShop(msgTime, personId, customerIds.get(i));
            }
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
    public void multiPickTest8() {
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
            aCase.setRequestData("1、enter(yu)；2、pick 1-8 一个优酸乳；3、leave(xie)；4、pick 1-2 一个3+2；" +
                    "5、leave(yu)；6、查询绑定结果" + "\n\n");

            videoJson = (VideoJson) jsonList.get(46);
            json = videoJson.getJson();

            long msgTime = videoJson.getTimestamp() - 1;
            String personId = getEnterPersonId(json);

            waitTime = videoJson.getTimeSift() - beforeTime;
            Thread.sleep(waitTime);

            videoJson = (VideoJson) jsonList.get(48);
            json = videoJson.getJson();
            JSONObject jo = JSON.parseObject(json);

            String resStr = analysisPersonSync(jo.getLong("start_time"),jo.getLong("end_time"),jo.getJSONArray("face_list"),"xie");

            String xieCustomerId = getCustomerIdFromSync(resStr);

            customerIds.add(xieCustomerId);

            videoJson = (VideoJson) jsonList.get(50);
            json = videoJson.getJson();
            jo = JSON.parseObject(json);

            resStr = analysisPersonSync(jo.getLong("start_time"),jo.getLong("end_time"),jo.getJSONArray("face_list"),"yu");

            String yuCustomerId = getCustomerIdFromSync(resStr);

            customerIds.add(yuCustomerId);

            for (int i = 0; i < customerIds.size(); i++) {
                leaveShop(msgTime, personId, customerIds.get(i));
            }

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----1、enter(yu)----------------------------");
            videoJson = (VideoJson) jsonList.get(46);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----2、pick 1-8 一个优酸乳----------------------------");
            videoJson = (VideoJson) jsonList.get(47);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----3、leave(佚名)----------------------------");
            videoJson = (VideoJson) jsonList.get(48);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----4、pick 1-2 一个3+2----------------------------");
            videoJson = (VideoJson) jsonList.get(49);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----5、leave(yu)----------------------------");
            videoJson = (VideoJson) jsonList.get(50);
            long timestamp = videoJson.getTimestamp() - 1;
            beforeTime = videoJson.getTimeSift();
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----6、查询绑定结果----------------------------");
            apiResponse = customerGoods(timestamp, aCase, step);

            addCustomerIds(apiResponse);

            String[] xieGoodsIds = {goods1_8};
            checkResult(apiResponse, xieGoodsIds, xieCustomerId,"xie");

            String[] yuGoodsIds = {goods1_2};
            checkResult(apiResponse, yuGoodsIds, yuCustomerId,"yu");

            msgTime = videoJson.getTimestamp() + 1;

            for (int i = 0; i < customerIds.size(); i++) {
                leaveShop(msgTime, personId, customerIds.get(i));
            }
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
    public void multiPickTest9() {
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
            aCase.setRequestData("1、enter(yu)；2、pick 1-4 一个好吃点；3、pick 1-8 一个优酸乳；" +
                    "4、leave(yu)；5、leave(xie)；6、查询绑定结果" + "\n\n");

            videoJson = (VideoJson) jsonList.get(51);
            json = videoJson.getJson();

            long msgTime = videoJson.getTimestamp() - 1;
            String personId = getEnterPersonId(json);

            waitTime = videoJson.getTimeSift() -beforeTime;
            Thread.sleep(waitTime);

            videoJson = (VideoJson) jsonList.get(54);
            json = videoJson.getJson();
            JSONObject jo = JSON.parseObject(json);

            String resStr = analysisPersonSync(jo.getLong("start_time"),jo.getLong("end_time"),jo.getJSONArray("face_list"),"xie");

            String xieCustomerId = getCustomerIdFromSync(resStr);

            customerIds.add(xieCustomerId);

            videoJson = (VideoJson) jsonList.get(55);
            json = videoJson.getJson();
            jo = JSON.parseObject(json);

            resStr = analysisPersonSync(jo.getLong("start_time"),jo.getLong("end_time"),jo.getJSONArray("face_list"),"yu");

            String yuCustomerId = getCustomerIdFromSync(resStr);

            customerIds.add(yuCustomerId);

            for (int i = 0; i < customerIds.size(); i++) {
                leaveShop(msgTime, personId, customerIds.get(i));
            }

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----1、enter(yu)----------------------------");
            videoJson = (VideoJson) jsonList.get(51);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----2、pick 1-4 一个好吃点----------------------------");
            videoJson = (VideoJson) jsonList.get(52);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----3、pick 1-8 一个优酸乳----------------------------");
            videoJson = (VideoJson) jsonList.get(53);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----4、leave(yu)----------------------------");
            videoJson = (VideoJson) jsonList.get(54);
            long timestamp = videoJson.getTimestamp() - 1;
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----5、leave(xie)----------------------------");
            videoJson = (VideoJson) jsonList.get(55);
            beforeTime = videoJson.getTimeSift();
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----6、查询绑定结果----------------------------");
            apiResponse = customerGoods(timestamp, aCase, step);

            addCustomerIds(apiResponse);

            String[] yuGoodsIds = {goods1_4};
            checkResult(apiResponse, yuGoodsIds, yuCustomerId,"yu");

            String[] xieGoodsIds = {goods1_8};
            checkResult(apiResponse, xieGoodsIds, xieCustomerId,"xie");

            msgTime = videoJson.getTimestamp() + 1;

            for (int i = 0; i < customerIds.size(); i++) {
                leaveShop(msgTime, personId, customerIds.get(i));
            }
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
    public void multiPickTest10() {
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
            aCase.setRequestData("1、enter(xie)；2、pick 1-7 一个黑米粥；3、leave(xie)；" +
                    "4、leave(yu)；5、查询绑定结果" + "\n\n");

            videoJson = (VideoJson) jsonList.get(56);
            json = videoJson.getJson();

            long msgTime = videoJson.getTimestamp() - 1;
            String personId = getEnterPersonId(json);

            for (int i = 0; i < customerIds.size(); i++) {
                leaveShop(msgTime, personId, customerIds.get(i));
            }

            waitTime = videoJson.getTimeSift() -beforeTime;
            Thread.sleep(waitTime);

            videoJson = (VideoJson) jsonList.get(58);
            json = videoJson.getJson();
            JSONObject jo = JSON.parseObject(json);

            String resStr = analysisPersonSync(jo.getLong("start_time"),jo.getLong("end_time"),jo.getJSONArray("face_list"),"xie");

            String xieCustomerId = getCustomerIdFromSync(resStr);

            customerIds.add(xieCustomerId);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----1、enter(xie)----------------------------");
            videoJson = (VideoJson) jsonList.get(56);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "---2、pick 1-7 一个黑米粥----------------------------");
            videoJson = (VideoJson) jsonList.get(57);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----3、leave(xie)----------------------------");
            videoJson = (VideoJson) jsonList.get(58);
            long timestamp = videoJson.getTimestamp() - 1;
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----4、leave(yu)----------------------------");
            videoJson = (VideoJson) jsonList.get(59);
            beforeTime = videoJson.getTimeSift();
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----5、查询绑定结果----------------------------");

            apiResponse = customerGoods(timestamp, aCase, step);

            addCustomerIds(apiResponse);

            String[] xieGoodsIds = {goods1_7};
            checkResult(apiResponse, xieGoodsIds, xieCustomerId,"xie");

            msgTime = videoJson.getTimestamp() + 1;

            for (int i = 0; i < customerIds.size(); i++) {
                leaveShop(msgTime, personId, customerIds.get(i));
            }
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
    public void multiPickTest11() {
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
            aCase.setRequestData("1、enter(xie)；2、pick 1-2 一个3+2；3、pick 1-4 一个好吃点；4、pick 1-8 一个优酸乳；" +
                    "5、leave(xie)；6、leave(yu)；7、查询绑定结果" + "\n\n");

            videoJson = (VideoJson) jsonList.get(60);
            json = videoJson.getJson();

            long msgTime = videoJson.getTimestamp() - 1;
            String personId = getEnterPersonId(json);

            videoJson = (VideoJson) jsonList.get(64);
            json = videoJson.getJson();
            JSONObject jo = JSON.parseObject(json);

            String resStr = analysisPersonSync(jo.getLong("start_time"),jo.getLong("end_time"),jo.getJSONArray("face_list"),"xie");

            String xieCustomerId = getCustomerIdFromSync(resStr);

            customerIds.add(xieCustomerId);

            videoJson = (VideoJson) jsonList.get(65);
            json = videoJson.getJson();
            jo = JSON.parseObject(json);

            resStr = analysisPersonSync(jo.getLong("start_time"),jo.getLong("end_time"),jo.getJSONArray("face_list"),"yu");

            String yuCustomerId = getCustomerIdFromSync(resStr);

            customerIds.add(yuCustomerId);

            for (int i = 0; i < customerIds.size(); i++) {
                leaveShop(msgTime, personId, customerIds.get(i));
            }

            waitTime = videoJson.getTimeSift() -beforeTime;
            Thread.sleep(waitTime);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----1、enter(xie)----------------------------");
            videoJson = (VideoJson) jsonList.get(60);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "---2、pick 1-2 一个3+2----------------------------");
            videoJson = (VideoJson) jsonList.get(61);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----3、pick 1-4 一个好吃点----------------------------");
            videoJson = (VideoJson) jsonList.get(62);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----4、pick 1-8 一个优酸乳----------------------------");
            videoJson = (VideoJson) jsonList.get(63);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----5、leave(xie)----------------------------");
            videoJson = (VideoJson) jsonList.get(64);
            long timestamp = videoJson.getTimestamp() - 1;
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----6、leave(yu)----------------------------");
            videoJson = (VideoJson) jsonList.get(65);
            beforeTime = videoJson.getTimeSift();
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----7、查询绑定结果----------------------------");
            apiResponse = customerGoods(timestamp, aCase, step);

            addCustomerIds(apiResponse);

            String[] yuGoodsIds = {goods1_2};
            checkResult(apiResponse, yuGoodsIds, yuCustomerId,"yu");

            String[] xieGoodsIds = {goods1_4,goods1_8};
            checkResult(apiResponse, xieGoodsIds, xieCustomerId,"xie");

            msgTime = videoJson.getTimestamp() + 1;

            for (int i = 0; i < customerIds.size(); i++) {
                leaveShop(msgTime, personId, customerIds.get(i));
            }
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
    public void multiPickTest12() {
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
            aCase.setRequestData("1、pick 1-7 一个黑米粥；2、pick 1-8 一个优酸乳；3、enter(xie)；" +
                    "4、leave(xie)；5、leave(yu)；6、查询绑定结果" + "\n\n");

            videoJson = (VideoJson) jsonList.get(66);
            json = videoJson.getJson();

            long msgTime = videoJson.getTimestamp() - 1;
            String personId = getEnterPersonId(json);

            waitTime = videoJson.getTimeSift() -beforeTime;
            Thread.sleep(waitTime);

            videoJson = (VideoJson) jsonList.get(69);
            json = videoJson.getJson();
            JSONObject jo = JSON.parseObject(json);

            String resStr = analysisPersonSync(jo.getLong("start_time"),jo.getLong("end_time"),jo.getJSONArray("face_list"),"xie");

            String xieCustomerId = getCustomerIdFromSync(resStr);
            customerIds.add(xieCustomerId);

            videoJson = (VideoJson) jsonList.get(70);
            json = videoJson.getJson();
            jo = JSON.parseObject(json);

            resStr = analysisPersonSync(jo.getLong("start_time"),jo.getLong("end_time"),jo.getJSONArray("face_list"),"yu");

            String yuCustomerId = getCustomerIdFromSync(resStr);

            customerIds.add(yuCustomerId);

            for (int i = 0; i < customerIds.size(); i++) {
                leaveShop(msgTime, personId, customerIds.get(i));
            }

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----1、pick 1-7 一个黑米粥----------------------------");
            videoJson = (VideoJson) jsonList.get(66);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();

            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "---2、pick 1-8 一个优酸乳----------------------------");
            videoJson = (VideoJson) jsonList.get(67);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----3、enter(xie)----------------------------");
            videoJson = (VideoJson) jsonList.get(68);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----4、leave(xie)----------------------------");
            videoJson = (VideoJson) jsonList.get(69);
            long timestamp = videoJson.getTimestamp() - 1;
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----5、leave(yu)----------------------------");
            videoJson = (VideoJson) jsonList.get(70);
            beforeTime = videoJson.getTimeSift();
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "---6、查询绑定结果----------------------------");
            apiResponse = customerGoods(timestamp, aCase, step);

            addCustomerIds(apiResponse);

            String[] yuGoodsIds = {goods1_7};
            checkResult(apiResponse, yuGoodsIds, yuCustomerId,"yu");

            String[] xieGoodsIds = {goods1_8};
            checkResult(apiResponse, xieGoodsIds, xieCustomerId,"xie");

            msgTime = videoJson.getTimestamp() + 1;

            for (int i = 0; i < customerIds.size(); i++) {
                leaveShop(msgTime, personId, customerIds.get(i));
            }

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
    public void multiPickTest14() {
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
            aCase.setRequestData("1、enter(yu)；2、enter(xie)；3、pick 4-4 一个可乐；" +
                    "4、leave(xie)；5、leave(yu)；6、查询绑定结果" + "\n\n");

            videoJson = (VideoJson) jsonList.get(71);
            json = videoJson.getJson();

            long msgTime = videoJson.getTimestamp() - 1;
            String personId = getEnterPersonId(json);

            waitTime = videoJson.getTimeSift() -beforeTime;
            Thread.sleep(waitTime);

            videoJson = (VideoJson) jsonList.get(75);
            json = videoJson.getJson();
            JSONObject jo = JSON.parseObject(json);

            String resStr = analysisPersonSync(jo.getLong("start_time"),jo.getLong("end_time"),jo.getJSONArray("face_list"),"yu");

            String yuCustomerId = getCustomerIdFromSync(resStr);
            customerIds.add(yuCustomerId);

            for (int i = 0; i < customerIds.size(); i++) {
                leaveShop(msgTime, personId, customerIds.get(i));
            }

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----1、enter(yu)----------------------------");
            videoJson = (VideoJson) jsonList.get(71);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----2、enter(xie)----------------------------");
            videoJson = (VideoJson) jsonList.get(72);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----3、pick 4-4 一个可乐----------------------------");
            videoJson = (VideoJson) jsonList.get(73);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----4、leave(xie)----------------------------");
            videoJson = (VideoJson) jsonList.get(74);
            long timestamp = videoJson.getTimestamp() - 1;
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----5、leave(yu)----------------------------");
            videoJson = (VideoJson) jsonList.get(75);
            beforeTime = videoJson.getTimeSift();
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "---6、查询绑定结果----------------------------");
            apiResponse = customerGoods(timestamp, aCase, step);

            addCustomerIds(apiResponse);

            String[] yuGoodsIds = {goods4_4};
            checkResult(apiResponse, yuGoodsIds, yuCustomerId,"yu");

            msgTime = videoJson.getTimestamp() + 1;

            for (int i = 0; i < customerIds.size(); i++) {
                leaveShop(msgTime, personId, customerIds.get(i));
            }
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
    public void multiPickTest15() {
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
            aCase.setRequestData("1、enter(yu)；2、enter(xie)；3、pick 4-2 一个益达；" +
                    "4、leave(yu)；5、leave(xie)；6、查询绑定结果" + "\n\n");

            videoJson = (VideoJson) jsonList.get(76);
            json = videoJson.getJson();

            long msgTime = videoJson.getTimestamp() - 1;
            String personId = getEnterPersonId(json);

            waitTime = videoJson.getTimeSift() -beforeTime;
            Thread.sleep(waitTime);

            videoJson = (VideoJson) jsonList.get(79);
            json = videoJson.getJson();
            JSONObject jo = JSON.parseObject(json);

            String resStr = analysisPersonSync(jo.getLong("start_time"),jo.getLong("end_time"),jo.getJSONArray("face_list"),"yu");

            String yuCustomerId = getCustomerIdFromSync(resStr);
            customerIds.add(yuCustomerId);

            for (int i = 0; i < customerIds.size(); i++) {
                leaveShop(msgTime,personId,customerIds.get(i));
            }

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----1、enter(yu)----------------------------");
            videoJson = (VideoJson) jsonList.get(76);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----2、enter(xie)----------------------------");
            videoJson = (VideoJson) jsonList.get(77);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----3、pick 4-2 一个益达----------------------------");
            videoJson = (VideoJson) jsonList.get(78);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----4、leave(yu)----------------------------");
            videoJson = (VideoJson) jsonList.get(79);
            long timestamp = videoJson.getTimestamp() - 1;
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----5、leave(xie)----------------------------");
            videoJson = (VideoJson) jsonList.get(80);
            beforeTime = videoJson.getTimeSift();
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "---6、查询绑定结果----------------------------");
            apiResponse = customerGoods(timestamp, aCase, step);

            addCustomerIds(apiResponse);

            String[] yuGoodsIds = {goods4_2};
            checkResult(apiResponse, yuGoodsIds, yuCustomerId,"yu");

            msgTime = videoJson.getTimestamp() + 1;

            for (int i = 0; i < customerIds.size(); i++) {
                leaveShop(msgTime, personId, customerIds.get(i));
            }
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
    public void multiPickTest16() {
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
            aCase.setRequestData("1、enter(yu)；2、enter(xie)；3、pick 1-4 一个好吃点；" +
                    "4、leave(yu)；5、leave(xie)；6、查询绑定结果" + "\n\n");

            videoJson = (VideoJson) jsonList.get(81);
            json = videoJson.getJson();

            long msgTime = videoJson.getTimestamp() - 1;
            String personId = getEnterPersonId(json);

            waitTime = videoJson.getTimeSift() - beforeTime;
            Thread.sleep(waitTime);

            videoJson = (VideoJson) jsonList.get(84);
            json = videoJson.getJson();
            JSONObject jo = JSON.parseObject(json);

            String resStr = analysisPersonSync(jo.getLong("start_time"),jo.getLong("end_time"),jo.getJSONArray("face_list"),"yu");

            String yuCustomerId = getCustomerIdFromSync(resStr);
            customerIds.add(yuCustomerId);

            for (int i = 0; i < customerIds.size(); i++) {
                leaveShop(msgTime,personId,customerIds.get(i));
            }

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----1、enter(yu)----------------------------");
            videoJson = (VideoJson) jsonList.get(81);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----2、enter(xie)----------------------------");
            videoJson = (VideoJson) jsonList.get(82);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----3、pick 1-4 一个好吃点----------------------------");
            videoJson = (VideoJson) jsonList.get(83);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----4、leave(yu)----------------------------");
            videoJson = (VideoJson) jsonList.get(84);
            long timestamp = videoJson.getTimestamp() - 1;
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----5、leave(xie)----------------------------");
            videoJson = (VideoJson) jsonList.get(85);
            beforeTime = videoJson.getTimeSift();
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "---6、查询绑定结果----------------------------");
            apiResponse = customerGoods(timestamp, aCase, step);

            addCustomerIds(apiResponse);

            String[] yuGoodsIds = {goods1_4};
            checkResult(apiResponse, yuGoodsIds, yuCustomerId,"yu");

            msgTime = videoJson.getTimestamp() + 1;

            for (int i = 0; i < customerIds.size(); i++) {
                leaveShop(msgTime, personId, customerIds.get(i));
            }
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
    public void multiPickTest17() {
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
            aCase.setRequestData("1、enter(yu)；2、enter(xie)；3、pick 1-5 一个可乐；" +
                    "4、leave(xie)；5、leave(yu)；6、查询绑定结果" + "\n\n");

            videoJson = (VideoJson) jsonList.get(86);
            json = videoJson.getJson();

            long msgTime = videoJson.getTimestamp() - 1;
            String personId = getEnterPersonId(json);

            waitTime = videoJson.getTimeSift() - beforeTime;
            Thread.sleep(waitTime);

            videoJson = (VideoJson) jsonList.get(90);
            json = videoJson.getJson();
            JSONObject jo = JSON.parseObject(json);

            String resStr = analysisPersonSync(jo.getLong("start_time"),jo.getLong("end_time"),jo.getJSONArray("face_list"),"yu");

            String yuCustomerId = getCustomerIdFromSync(resStr);
            customerIds.add(yuCustomerId);

            for (int i = 0; i < customerIds.size(); i++) {
                leaveShop(msgTime,personId,customerIds.get(i));
            }

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----1、enter(yu)----------------------------");
            videoJson = (VideoJson) jsonList.get(86);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----2、enter(xie)----------------------------");
            videoJson = (VideoJson) jsonList.get(87);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----3、pick 1-5 一个可乐----------------------------");
            videoJson = (VideoJson) jsonList.get(88);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----4、leave(xie)----------------------------");
            videoJson = (VideoJson) jsonList.get(89);
            long timestamp = videoJson.getTimestamp() - 1;
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----5、leave(yu)----------------------------");
            videoJson = (VideoJson) jsonList.get(90);
            beforeTime = videoJson.getTimeSift();
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "---6、查询绑定结果----------------------------");
            apiResponse = customerGoods(timestamp, aCase, step);

            addCustomerIds(apiResponse);

            String[] yuGoodsIds = {goods1_5};
            checkResult(apiResponse, yuGoodsIds, yuCustomerId,"yu");

            msgTime = videoJson.getTimestamp() + 1;

            for (int i = 0; i < customerIds.size(); i++) {
                leaveShop(msgTime, personId, customerIds.get(i));
            }
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
    public void multiPickTest18() {
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
            aCase.setRequestData("1、enter(yu)；2、pick 4-4 一个可乐；3、enter(xie)；" +
                    "4、pick 1-4 一个好吃点；5、leave(xie)；6、leave(yu)；7、查询绑定结果" + "\n\n");

            videoJson = (VideoJson) jsonList.get(91);
            json = videoJson.getJson();

            long msgTime = videoJson.getTimestamp() - 1;
            String personId = getEnterPersonId(json);

            waitTime = videoJson.getTimeSift() - beforeTime;
            Thread.sleep(waitTime);

            videoJson = (VideoJson) jsonList.get(96);
            json = videoJson.getJson();
            JSONObject jo = JSON.parseObject(json);

            String resStr = analysisPersonSync(jo.getLong("start_time"),jo.getLong("end_time"),jo.getJSONArray("face_list"),"yu");

            String yuCustomerId = getCustomerIdFromSync(resStr);
            customerIds.add(yuCustomerId);

            for (int i = 0; i < customerIds.size(); i++) {
                leaveShop(msgTime,personId,customerIds.get(i));
            }

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----1、enter(yu)----------------------------");
            videoJson = (VideoJson) jsonList.get(91);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----2、pick 4-4 一个可乐----------------------------");
            videoJson = (VideoJson) jsonList.get(92);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----3、enter(xie)----------------------------");
            videoJson = (VideoJson) jsonList.get(93);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----4、pick 1-4 一个好吃点----------------------------");
            videoJson = (VideoJson) jsonList.get(94);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----5、leave(xie)----------------------------");
            videoJson = (VideoJson) jsonList.get(95);
            long timestamp = videoJson.getTimestamp() - 1;
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----6、leave(yu)----------------------------");
            videoJson = (VideoJson) jsonList.get(96);
            beforeTime = videoJson.getTimeSift();
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "---7、查询绑定结果----------------------------");
            apiResponse = customerGoods(timestamp, aCase, step);

            addCustomerIds(apiResponse);

            String[] yuGoodsIds = {goods4_4, goods1_4};
            checkResult(apiResponse, yuGoodsIds, yuCustomerId,"yu");

            msgTime = videoJson.getTimestamp() + 1;

            for (int i = 0; i < customerIds.size(); i++) {
                leaveShop(msgTime, personId, customerIds.get(i));
            }
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
    public void multiPickTest19() {
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
            aCase.setRequestData("1、enter(yu)；2、enter(xie)；3、pick 4-5 一个旺仔；" +
                    "4、pick 1-5 一个可乐；5、leave(xie)；6、leave(yu)；7、查询绑定结果" + "\n\n");

            videoJson = (VideoJson) jsonList.get(97);
            json = videoJson.getJson();

            long msgTime = videoJson.getTimestamp() - 1;
            String personId = getEnterPersonId(json);

            waitTime = videoJson.getTimeSift() - beforeTime;
            Thread.sleep(waitTime);

            videoJson = (VideoJson) jsonList.get(101);
            json = videoJson.getJson();
            JSONObject jo = JSON.parseObject(json);

            String resStr = analysisPersonSync(jo.getLong("start_time"),jo.getLong("end_time"),jo.getJSONArray("face_list"),"yu");

            String yuCustomerId = getCustomerIdFromSync(resStr);
            customerIds.add(yuCustomerId);

            for (int i = 0; i < customerIds.size(); i++) {
                leaveShop(msgTime,personId,customerIds.get(i));
            }

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----1、enter(yu)----------------------------");
            videoJson = (VideoJson) jsonList.get(97);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----2、pick 4-4 一个可乐----------------------------");
            videoJson = (VideoJson) jsonList.get(98);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----3、enter(xie)----------------------------");
            videoJson = (VideoJson) jsonList.get(99);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----4、pick 1-4 一个好吃点----------------------------");
            videoJson = (VideoJson) jsonList.get(100);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----5、leave(xie)----------------------------");
            videoJson = (VideoJson) jsonList.get(101);
            long timestamp = videoJson.getTimestamp() - 1;
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----6、leave(yu)----------------------------");
            videoJson = (VideoJson) jsonList.get(102);
            beforeTime = videoJson.getTimeSift();
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "---7、查询绑定结果----------------------------");
            apiResponse = customerGoods(timestamp, aCase, step);

            addCustomerIds(apiResponse);

            String[] yuGoodsIds = {goods4_5, goods1_5};
            checkResult(apiResponse, yuGoodsIds, yuCustomerId,"yu");

            msgTime = videoJson.getTimestamp() + 1;

            for (int i = 0; i < customerIds.size(); i++) {
                leaveShop(msgTime, personId, customerIds.get(i));
            }
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
    public void multiPickTest20() {
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
            aCase.setRequestData("1、enter(yu)；2、enter(xie)；3、pick 4-5 一个旺仔；" +
                    "4、pick 1-5 一个可乐；5、leave(xie)；6、leave(yu)；7、查询绑定结果" + "\n\n");

            videoJson = (VideoJson) jsonList.get(103);
            json = videoJson.getJson();

            long msgTime = videoJson.getTimestamp() - 1;
            String personId = getEnterPersonId(json);

            waitTime = videoJson.getTimeSift() - beforeTime;
            Thread.sleep(waitTime);

            videoJson = (VideoJson) jsonList.get(107);
            json = videoJson.getJson();
            JSONObject jo = JSON.parseObject(json);

            String resStr = analysisPersonSync(jo.getLong("start_time"),jo.getLong("end_time"),jo.getJSONArray("face_list"),"yu");

            String yuCustomerId = getCustomerIdFromSync(resStr);
            customerIds.add(yuCustomerId);

            for (int i = 0; i < customerIds.size(); i++) {
                leaveShop(msgTime,personId,customerIds.get(i));
            }

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----1、enter(yu)----------------------------");
            videoJson = (VideoJson) jsonList.get(103);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----2、pick 4-4 一个可乐----------------------------");
            videoJson = (VideoJson) jsonList.get(104);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----3、enter(xie)----------------------------");
            videoJson = (VideoJson) jsonList.get(105);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----4、pick 1-4 一个好吃点----------------------------");
            videoJson = (VideoJson) jsonList.get(106);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----5、leave(xie)----------------------------");
            videoJson = (VideoJson) jsonList.get(107);
            long timestamp = videoJson.getTimestamp() - 1;
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----6、leave(yu)----------------------------");
            videoJson = (VideoJson) jsonList.get(108);
            beforeTime = videoJson.getTimeSift();
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "---7、查询绑定结果----------------------------");
            apiResponse = customerGoods(timestamp, aCase, step);

            addCustomerIds(apiResponse);

            String[] yuGoodsIds = {goods4_5, goods1_2};
            checkResult(apiResponse, yuGoodsIds, yuCustomerId,"yu");

            msgTime = videoJson.getTimestamp() + 1;

            for (int i = 0; i < customerIds.size(); i++) {
                leaveShop(msgTime, personId, customerIds.get(i));
            }
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
    public void multiPickTest21(){
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
            aCase.setRequestData("1、enter(yu)；2、enter(xie)；3、pick 4-6 一个黑米粥；" +
                    "4、pick 1-2 一个3+2；5、leave(xie)；6、leave(yu)；7、查询绑定结果" + "\n\n");

            videoJson = (VideoJson) jsonList.get(109);
            json = videoJson.getJson();

            long msgTime = videoJson.getTimestamp() - 1;
            String personId = getEnterPersonId(json);

            waitTime = videoJson.getTimeSift() - beforeTime;
            Thread.sleep(waitTime);

            videoJson = (VideoJson) jsonList.get(114);
            json = videoJson.getJson();
            JSONObject jo = JSON.parseObject(json);

            String resStr = analysisPersonSync(jo.getLong("start_time"),jo.getLong("end_time"),jo.getJSONArray("face_list"),"yu");

            String yuCustomerId = getCustomerIdFromSync(resStr);
            customerIds.add(yuCustomerId);

            for (int i = 0; i < customerIds.size(); i++) {
                leaveShop(msgTime,personId,customerIds.get(i));
            }

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----1、enter(yu)----------------------------");
            videoJson = (VideoJson) jsonList.get(109);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----2、enter(xie)----------------------------");
            videoJson = (VideoJson) jsonList.get(110);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----3、pick 4-6 一个黑米粥----------------------------");
            videoJson = (VideoJson) jsonList.get(111);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----4、pick 1-2 一个3+2----------------------------");
            videoJson = (VideoJson) jsonList.get(112);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----5、leave(xie)----------------------------");
            videoJson = (VideoJson) jsonList.get(113);
            long timestamp = videoJson.getTimestamp() - 1;
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----6、leave(yu)----------------------------");
            videoJson = (VideoJson) jsonList.get(114);
            beforeTime = videoJson.getTimeSift();
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "---7、查询绑定结果----------------------------");
            apiResponse = customerGoods(timestamp, aCase, step);

            addCustomerIds(apiResponse);

            String[] yuGoodsIds = {goods4_6, goods1_2};
            checkResult(apiResponse, yuGoodsIds, yuCustomerId,"yu");

            msgTime = videoJson.getTimestamp() + 1;

            for (int i = 0; i < customerIds.size(); i++) {
                leaveShop(msgTime, personId, customerIds.get(i));
            }
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
    public void multiPickTest22(){
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
            aCase.setRequestData("1、enter(yu)；2、enter(xie)；3、pick 1-2 一个3+2；" +
                    "4、pick 4-2 一个益达；5、leave(yu)；6、leave(xie)；7、查询绑定结果" + "\n\n");

            videoJson = (VideoJson) jsonList.get(115);
            json = videoJson.getJson();

            long msgTime = videoJson.getTimestamp() - 1;
            String personId = getEnterPersonId(json);

            waitTime = videoJson.getTimeSift() - beforeTime;
            Thread.sleep(waitTime);

            videoJson = (VideoJson) jsonList.get(119);
            json = videoJson.getJson();
            JSONObject jo = JSON.parseObject(json);

            String resStr = analysisPersonSync(jo.getLong("start_time"),jo.getLong("end_time"),jo.getJSONArray("face_list"),"yu");

            String yuCustomerId = getCustomerIdFromSync(resStr);
            customerIds.add(yuCustomerId);

            customerIds.add("4eb4c30b-0cb8-4374-bff0-1812f1e9df64");

            for (int i = 0; i < customerIds.size(); i++) {
                leaveShop(msgTime,personId,customerIds.get(i));
            }

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----1、enter(yu)----------------------------");
            videoJson = (VideoJson) jsonList.get(115);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----2、enter(xie)----------------------------");
            videoJson = (VideoJson) jsonList.get(116);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----3、pick 1-2 一个3+2----------------------------");
            videoJson = (VideoJson) jsonList.get(117);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);


            logger.info("\n\n");
            logger.info("----------" + (++step) + "----4、pick 4-2 一个益达----------------------------");
            videoJson = (VideoJson) jsonList.get(118);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----5、leave(yu)----------------------------");
            videoJson = (VideoJson) jsonList.get(119);
            long timestamp = videoJson.getTimestamp() - 1;
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----6、leave(xie)----------------------------");
            videoJson = (VideoJson) jsonList.get(120);
            beforeTime = videoJson.getTimeSift();
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "---7、查询绑定结果----------------------------");
            apiResponse = customerGoods(timestamp, aCase, step);

            addCustomerIds(apiResponse);

            String[] yuGoodsIds = {goods1_2, goods4_2};
            checkResult(apiResponse, yuGoodsIds, yuCustomerId,"yu");

            msgTime = videoJson.getTimestamp() + 1;

            for (int i = 0; i < customerIds.size(); i++) {
                leaveShop(msgTime, personId, customerIds.get(i));
            }
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
    public void multiPickTest23(){
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
            aCase.setRequestData("1、enter(yu)；2、pick 1-7 一个黑米粥；3、pick 4-6 一个黑米粥；" +
                    "4、enter(xie)；5、leave(yu)；6、leave(xie)；7、查询绑定结果" + "\n\n");

            videoJson = (VideoJson) jsonList.get(121);
            json = videoJson.getJson();

            long msgTime = videoJson.getTimestamp() - 1;
            String personId = getEnterPersonId(json);

            waitTime = videoJson.getTimeSift() - beforeTime;
            Thread.sleep(waitTime);

            videoJson = (VideoJson) jsonList.get(125);
            json = videoJson.getJson();
            JSONObject jo = JSON.parseObject(json);

            String resStr = analysisPersonSync(jo.getLong("start_time"),jo.getLong("end_time"),jo.getJSONArray("face_list"),"yu");

            String yuCustomerId = getCustomerIdFromSync(resStr);
            customerIds.add(yuCustomerId);

            for (int i = 0; i < customerIds.size(); i++) {
                leaveShop(msgTime,personId,customerIds.get(i));
            }

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----1、enter(yu)----------------------------");
            videoJson = (VideoJson) jsonList.get(121);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----2、pick 1-7 一个黑米粥----------------------------");
            videoJson = (VideoJson) jsonList.get(122);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----3、pick 4-6 一个黑米粥----------------------------");
            videoJson = (VideoJson) jsonList.get(123);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----4、enter(xie)----------------------------");
            videoJson = (VideoJson) jsonList.get(124);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----5、leave(yu)----------------------------");
            videoJson = (VideoJson) jsonList.get(125);
            long timestamp = videoJson.getTimestamp() - 1;
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----6、leave(xie)----------------------------");
            videoJson = (VideoJson) jsonList.get(126);
            beforeTime = videoJson.getTimeSift();
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "---7、查询绑定结果----------------------------");
            apiResponse = customerGoods(timestamp, aCase, step);

            addCustomerIds(apiResponse);

            String[] yuGoodsIds = {goods1_7, goods4_6};
            checkResult(apiResponse, yuGoodsIds, yuCustomerId,"yu");

            msgTime = videoJson.getTimestamp() + 1;

            for (int i = 0; i < customerIds.size(); i++) {
                leaveShop(msgTime, personId, customerIds.get(i));
            }
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
    public void multiPickTest24(){
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
            aCase.setRequestData("1、enter(yu)；2、pick 4-4 一个可乐；3、pick 1-8 一个优酸乳；" +
                    "4、leave(xie)；5、leave(yu)；6、查询绑定结果" + "\n\n");

            videoJson = (VideoJson) jsonList.get(127);
            json = videoJson.getJson();

            long msgTime = videoJson.getTimestamp() - 1;
            String personId = getEnterPersonId(json);

            waitTime = videoJson.getTimeSift() - beforeTime;
            Thread.sleep(waitTime);

            videoJson = (VideoJson) jsonList.get(130);
            json = videoJson.getJson();
            JSONObject jo = JSON.parseObject(json);

            String resStr = analysisPersonSync(jo.getLong("start_time"),jo.getLong("end_time"),jo.getJSONArray("face_list"),"xie");

            String xieCustomerId = getCustomerIdFromSync(resStr);
            customerIds.add(xieCustomerId);

            videoJson = (VideoJson) jsonList.get(131);
            json = videoJson.getJson();
            jo = JSON.parseObject(json);

            resStr = analysisPersonSync(jo.getLong("start_time"),jo.getLong("end_time"),jo.getJSONArray("face_list"),"yu");

            String yuCustomerId = getCustomerIdFromSync(resStr);
            customerIds.add(yuCustomerId);

            for (int i = 0; i < customerIds.size(); i++) {
                leaveShop(msgTime,personId,customerIds.get(i));
            }

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----1、enter(yu)----------------------------");
            videoJson = (VideoJson) jsonList.get(127);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----2、pick 4-4 一个可乐----------------------------");
            videoJson = (VideoJson) jsonList.get(128);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----3、pick 1-8 一个优酸乳----------------------------");
            videoJson = (VideoJson) jsonList.get(129);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----4、leave(佚名)----------------------------");
            videoJson = (VideoJson) jsonList.get(130);
            long timestamp = videoJson.getTimestamp() - 1;
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----5、leave(佚名)----------------------------");
            videoJson = (VideoJson) jsonList.get(131);
            beforeTime = videoJson.getTimeSift();
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "---6、查询绑定结果----------------------------");
            apiResponse = customerGoods(timestamp, aCase, step);

            addCustomerIds(apiResponse);

            String[] yuGoodsIds = {goods4_4};
            checkResult(apiResponse, yuGoodsIds, yuCustomerId,"yu");

            String[] xieGoodsIds = {goods1_8};
            checkResult(apiResponse, xieGoodsIds, xieCustomerId,"xie");

            msgTime = videoJson.getTimestamp() + 1;

            for (int i = 0; i < customerIds.size(); i++) {
                leaveShop(msgTime, personId, customerIds.get(i));
            }
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
    public void multiPickTest25(){
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
            aCase.setRequestData("1、enter(yu)；2、pick 4-5 一个旺仔；3、pick 2-2 一个鱼板面；" +
                    "4、pick 1-7 一个黑米粥；5、leave(xie)；6、leave(yu)；7、查询绑定结果" + "\n\n");

            videoJson = (VideoJson) jsonList.get(132);
            json = videoJson.getJson();

            long msgTime = videoJson.getTimestamp() - 1;
            String personId = getEnterPersonId(json);

            waitTime = videoJson.getTimeSift() - beforeTime;
            Thread.sleep(waitTime);

            videoJson = (VideoJson) jsonList.get(136);
            json = videoJson.getJson();
            JSONObject jo = JSON.parseObject(json);

            String resStr = analysisPersonSync(jo.getLong("start_time"),jo.getLong("end_time"),jo.getJSONArray("face_list"),"xie");

            String xieCustomerId = getCustomerIdFromSync(resStr);
            customerIds.add(xieCustomerId);

            videoJson = (VideoJson) jsonList.get(137);
            json = videoJson.getJson();
            jo = JSON.parseObject(json);

            resStr = analysisPersonSync(jo.getLong("start_time"),jo.getLong("end_time"),jo.getJSONArray("face_list"),"yu");

            String yuCustomerId = getCustomerIdFromSync(resStr);
            customerIds.add(yuCustomerId);

            for (int i = 0; i < customerIds.size(); i++) {
                leaveShop(msgTime, personId, customerIds.get(i));
            }

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----1、enter(yu)----------------------------");
            videoJson = (VideoJson) jsonList.get(132);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----2、pick 4-5 一个旺仔----------------------------");
            videoJson = (VideoJson) jsonList.get(133);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----3、pick 2-2 一个鱼板面----------------------------");
            videoJson = (VideoJson) jsonList.get(134);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----4、pick 1-7 一个黑米粥----------------------------");
            videoJson = (VideoJson) jsonList.get(135);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----5、leave(佚名)----------------------------");
            videoJson = (VideoJson) jsonList.get(136);
            long timestamp = videoJson.getTimestamp() - 1;
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----5、leave(yu)----------------------------");
            videoJson = (VideoJson) jsonList.get(137);
            beforeTime = videoJson.getTimeSift();
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "---6、查询绑定结果----------------------------");
            apiResponse = customerGoods(timestamp, aCase, step);

            addCustomerIds(apiResponse);

            String[] xieGoodsIds = {goods1_7};
            checkResult(apiResponse, xieGoodsIds, xieCustomerId,"xie");

            String[] yuGoodsIds = {goods4_5, goods2_2};
            checkResult(apiResponse, yuGoodsIds, yuCustomerId,"yu");

            msgTime = videoJson.getTimestamp() + 1;

            for (int i = 0; i < customerIds.size(); i++) {
                leaveShop(msgTime, personId, customerIds.get(i));
            }

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

    @Test(priority = 26)
    public void multiPickTest26(){
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
            aCase.setRequestData("1、enter(yu)；2、enter(xie)；3、pick 4-4 一个可乐；4、pick 4-9 一个蛋黄派；" +
                    "5、pick 2-2 一个鱼板面；6、leave(xie)；7、leave(yu)；8、查询绑定结果" + "\n\n");

            videoJson = (VideoJson) jsonList.get(138);
            json = videoJson.getJson();

            long msgTime = videoJson.getTimestamp() - 1;
            String personId = getEnterPersonId(json);

            waitTime = videoJson.getTimeSift() - beforeTime;
            Thread.sleep(waitTime);

            videoJson = (VideoJson) jsonList.get(143);
            json = videoJson.getJson();
            JSONObject jo = JSON.parseObject(json);

            String resStr = analysisPersonSync(jo.getLong("start_time"),jo.getLong("end_time"),jo.getJSONArray("face_list"),"xie");

            String xieCustomerId = getCustomerIdFromSync(resStr);
            customerIds.add(xieCustomerId);

            videoJson = (VideoJson) jsonList.get(144);
            json = videoJson.getJson();
            jo = JSON.parseObject(json);

            resStr = analysisPersonSync(jo.getLong("start_time"),jo.getLong("end_time"),jo.getJSONArray("face_list"),"yu");

            String yuCustomerId = getCustomerIdFromSync(resStr);
            customerIds.add(yuCustomerId);

            for (int i = 0; i < customerIds.size(); i++) {
                leaveShop(msgTime, personId, customerIds.get(i));
            }

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----1、enter(yu)----------------------------");
            videoJson = (VideoJson) jsonList.get(138);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----2、enter(xie)----------------------------");
            videoJson = (VideoJson) jsonList.get(139);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----3、pick 4-4 一个可乐----------------------------");
            videoJson = (VideoJson) jsonList.get(140);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----4、pick 4-9 一个蛋黄派----------------------------");
            videoJson = (VideoJson) jsonList.get(141);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----5、pick 2-2 一个鱼板面----------------------------");
            videoJson = (VideoJson) jsonList.get(142);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----6、leave(xie)----------------------------");
            videoJson = (VideoJson) jsonList.get(143);
            long timestamp = videoJson.getTimestamp() - 1;
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----7、leave(yu)----------------------------");
            videoJson = (VideoJson) jsonList.get(144);
            beforeTime = videoJson.getTimeSift();
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "---8、查询绑定结果----------------------------");
            apiResponse = customerGoods(timestamp, aCase, step);

            addCustomerIds(apiResponse);

            String[] yuGoodsIds = {goods4_4, goods2_2};
            checkResult(apiResponse, yuGoodsIds, yuCustomerId,"yu");

            String[] xieGoodsIds = {goods4_9};
            checkResult(apiResponse, xieGoodsIds, xieCustomerId,"xie");

            msgTime = videoJson.getTimestamp() + 1;

            for (int i = 0; i < customerIds.size(); i++) {
                leaveShop(msgTime, personId, customerIds.get(i));
            }
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

    @Test(priority = 27)
    public void multiPickTest27(){
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
            aCase.setRequestData("1、enter(yu)；2、enter(xie)；3、pick 1-8 一个优酸乳；4、pick 2-2 一个鱼板面；" +
                    "5、leave(yu)；6、leave(xie)；7、查询绑定结果" + "\n\n");

            videoJson = (VideoJson) jsonList.get(145);
            json = videoJson.getJson();

            long msgTime = videoJson.getTimestamp() - 1;
            String personId = getEnterPersonId(json);

            waitTime = videoJson.getTimeSift() - beforeTime;
            Thread.sleep(waitTime);

            videoJson = (VideoJson) jsonList.get(149);
            json = videoJson.getJson();
            JSONObject jo = JSON.parseObject(json);

            String resStr = analysisPersonSync(jo.getLong("start_time"),jo.getLong("end_time"),jo.getJSONArray("face_list"),"yu");

            String yuCustomerId = getCustomerIdFromSync(resStr);
            customerIds.add(yuCustomerId);

            videoJson = (VideoJson) jsonList.get(150);
            json = videoJson.getJson();
            jo = JSON.parseObject(json);

            resStr = analysisPersonSync(jo.getLong("start_time"),jo.getLong("end_time"),jo.getJSONArray("face_list"),"xie");

            String xieCustomerId = getCustomerIdFromSync(resStr);
            customerIds.add(xieCustomerId);

            for (int i = 0; i < customerIds.size(); i++) {
                leaveShop(msgTime, personId, customerIds.get(i));
            }

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----1、enter(yu)----------------------------");
            videoJson = (VideoJson) jsonList.get(145);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----2、enter(xie)----------------------------");
            videoJson = (VideoJson) jsonList.get(146);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----3、pick 1-8 一个优酸乳----------------------------");
            videoJson = (VideoJson) jsonList.get(147);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----4、pick 2-2 一个鱼板面----------------------------");
            videoJson = (VideoJson) jsonList.get(148);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----5、leave(yu)----------------------------");
            videoJson = (VideoJson) jsonList.get(149);
            long timestamp = videoJson.getTimestamp() - 1;
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----6、leave(xie)----------------------------");
            videoJson = (VideoJson) jsonList.get(150);
            beforeTime = videoJson.getTimeSift();
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "---7、查询绑定结果----------------------------");
            apiResponse = customerGoods(timestamp, aCase, step);

            addCustomerIds(apiResponse);

            String[] yuGoodsIds = {goods2_2};
            checkResult(apiResponse, yuGoodsIds, yuCustomerId,"yu");

            String[] xieGoodsIds = {goods1_8};
            checkResult(apiResponse, xieGoodsIds, xieCustomerId,"xie");

            msgTime = videoJson.getTimestamp() + 1;

            for (int i = 0; i < customerIds.size(); i++) {
                leaveShop(msgTime, personId, customerIds.get(i));
            }
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

    @Test(priority = 28)
    public void multiPickTest28(){
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
            aCase.setRequestData("1、enter(yu)；2、enter(xie)；3、pick 4-9 一个蛋黄派；4、pick 1-4 一个好吃点；" +
                    "5、leave(yu)；6、leave(xie)；7、查询绑定结果" + "\n\n");

            videoJson = (VideoJson) jsonList.get(151);
            json = videoJson.getJson();

            long msgTime = videoJson.getTimestamp() - 1;
            String personId = getEnterPersonId(json);

            waitTime = videoJson.getTimeSift() - beforeTime;
            Thread.sleep(waitTime);

            videoJson = (VideoJson) jsonList.get(155);
            json = videoJson.getJson();
            JSONObject jo = JSON.parseObject(json);

            String resStr = analysisPersonSync(jo.getLong("start_time"),jo.getLong("end_time"),jo.getJSONArray("face_list"),"yu");

            String yuCustomerId = getCustomerIdFromSync(resStr);
            customerIds.add(yuCustomerId);

            videoJson = (VideoJson) jsonList.get(156);
            json = videoJson.getJson();
            jo = JSON.parseObject(json);

            resStr = analysisPersonSync(jo.getLong("start_time"),jo.getLong("end_time"),jo.getJSONArray("face_list"),"xie");

            String xieCustomerId = getCustomerIdFromSync(resStr);
            customerIds.add(xieCustomerId);

            for (int i = 0; i < customerIds.size(); i++) {
                leaveShop(msgTime, personId, customerIds.get(i));
            }

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----1、enter(yu)----------------------------");
            videoJson = (VideoJson) jsonList.get(151);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----2、enter(xie)----------------------------");
            videoJson = (VideoJson) jsonList.get(152);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----3、pick 4-9 一个蛋黄派----------------------------");
            videoJson = (VideoJson) jsonList.get(153);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----4、pick 1-4 一个好吃点----------------------------");
            videoJson = (VideoJson) jsonList.get(154);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----5、leave(yu)----------------------------");
            videoJson = (VideoJson) jsonList.get(155);
            long timestamp = videoJson.getTimestamp() - 1;
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----6、leave(xie)----------------------------");
            videoJson = (VideoJson) jsonList.get(156);
            beforeTime = videoJson.getTimeSift();
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);
            logger.info("\n\n");
            logger.info("----------" + (++step) + "---7、查询绑定结果----------------------------");
            apiResponse = customerGoods(timestamp, aCase, step);

            addCustomerIds(apiResponse);

            String[] yuGoodsIds = {goods1_4};
            checkResult(apiResponse, yuGoodsIds, yuCustomerId,"yu");

            String[] xieGoodsIds = {goods4_9};
            checkResult(apiResponse, xieGoodsIds, xieCustomerId,"xie");

            msgTime = videoJson.getTimestamp() + 1;

            for (int i = 0; i < customerIds.size(); i++) {
                leaveShop(msgTime, personId, customerIds.get(i));
            }

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

    @Test(priority = 29)
    public void multiPickTest29(){
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
            aCase.setRequestData("1、enter(yu)；2、pick 1-7 一个黑米粥；3、enter(xie)；4、pick 2-2 一个鱼板面；" +
                    "5、leave(yu)；6、leave(xie)；7、查询绑定结果" + "\n\n");

            videoJson = (VideoJson) jsonList.get(157);
            json = videoJson.getJson();

            long msgTime = videoJson.getTimestamp() - 1;
            String personId = getEnterPersonId(json);

            waitTime = videoJson.getTimeSift() - beforeTime;
            Thread.sleep(waitTime);

            videoJson = (VideoJson) jsonList.get(161);
            json = videoJson.getJson();
            JSONObject jo = JSON.parseObject(json);

            String resStr = analysisPersonSync(jo.getLong("start_time"),jo.getLong("end_time"),jo.getJSONArray("face_list"),"yu");

            String yuCustomerId = getCustomerIdFromSync(resStr);
            customerIds.add(yuCustomerId);

            videoJson = (VideoJson) jsonList.get(162);
            json = videoJson.getJson();
            jo = JSON.parseObject(json);

            resStr = analysisPersonSync(jo.getLong("start_time"),jo.getLong("end_time"),jo.getJSONArray("face_list"),"xie");

            String xieCustomerId = getCustomerIdFromSync(resStr);
            customerIds.add(xieCustomerId);

            for (int i = 0; i < customerIds.size(); i++) {
                leaveShop(msgTime, personId, customerIds.get(i));
            }

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----1、enter(yu)----------------------------");
            videoJson = (VideoJson) jsonList.get(157);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----2、pick 1-7 一个黑米粥----------------------------");
            videoJson = (VideoJson) jsonList.get(158);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----3、enter(xie)----------------------------");
            videoJson = (VideoJson) jsonList.get(159);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----4、pick 2-2 一个鱼板面----------------------------");
            videoJson = (VideoJson) jsonList.get(160);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----5、leave(yu)----------------------------");
            videoJson = (VideoJson) jsonList.get(161);
            long timestamp = videoJson.getTimestamp() - 1;
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----6、leave(xie)----------------------------");
            videoJson = (VideoJson) jsonList.get(162);
            beforeTime = videoJson.getTimeSift();
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "---7、查询绑定结果----------------------------");
            apiResponse = customerGoods(timestamp, aCase, step);

            addCustomerIds(apiResponse);

            String[] yuGoodsIds = {goods2_2};
            checkResult(apiResponse, yuGoodsIds, yuCustomerId,"yu");

            String[] xieGoodsIds = {goods1_7};
            checkResult(apiResponse, xieGoodsIds, xieCustomerId,"xie");

            msgTime = videoJson.getTimestamp() + 1;

            for (int i = 0; i < customerIds.size(); i++) {
                leaveShop(msgTime, personId, customerIds.get(i));
            }
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

    @Test(priority = 31)
    public void multiPickTest31(){
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
            aCase.setRequestData("1、enter(yu)；2、enter(xie)；3、pick 4-2 一个益达；4、leave(xie)；" +
                    "5、pick 2-2 一个鱼板面；6、pick 2-4 一个抽纸；7、leave(yu)；8、leave(xie)；9、查询绑定结果" + "\n\n");

            videoJson = (VideoJson) jsonList.get(163);
            json = videoJson.getJson();

            long msgTime = videoJson.getTimestamp() - 1;
            String personId = getEnterPersonId(json);

            waitTime = videoJson.getTimeSift() - beforeTime;
            Thread.sleep(waitTime);

            videoJson = (VideoJson) jsonList.get(169);
            json = videoJson.getJson();
            JSONObject jo = JSON.parseObject(json);

            String resStr = analysisPersonSync(jo.getLong("start_time"),jo.getLong("end_time"),jo.getJSONArray("face_list"),"yu");

            String yuCustomerId = getCustomerIdFromSync(resStr);
            customerIds.add(yuCustomerId);

            videoJson = (VideoJson) jsonList.get(170);
            json = videoJson.getJson();
            jo = JSON.parseObject(json);

            resStr = analysisPersonSync(jo.getLong("start_time"),jo.getLong("end_time"),jo.getJSONArray("face_list"),"xie");

            String xieCustomerId = getCustomerIdFromSync(resStr);
            customerIds.add(xieCustomerId);

            for (int i = 0; i < customerIds.size(); i++) {
                leaveShop(msgTime, personId, customerIds.get(i));
            }

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----1、enter(yu)----------------------------");
            videoJson = (VideoJson) jsonList.get(163);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----2、enter(xie)----------------------------");
            videoJson = (VideoJson) jsonList.get(164);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----3、pick 4-2 一个益达----------------------------");
            videoJson = (VideoJson) jsonList.get(165);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----4、leave(xie)----------------------------");
            videoJson = (VideoJson) jsonList.get(166);
            long timestamp = videoJson.getTimestamp() - 1;
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----5、pick 2-2 一个鱼板面----------------------------");
            videoJson = (VideoJson) jsonList.get(167);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----6、pick 2-4 一个抽纸----------------------------");
            videoJson = (VideoJson) jsonList.get(168);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);


            logger.info("\n\n");
            logger.info("----------" + (++step) + "----7、leave(佚名)----------------------------");
            videoJson = (VideoJson) jsonList.get(169);
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "----8、leave(yu)----------------------------");
            videoJson = (VideoJson) jsonList.get(170);
            beforeTime = videoJson.getTimeSift();
            json = videoJson.getJson();
            secKey = videoJson.getSecKey();
            customerMessage(json, secKey, aCase, step);

            logger.info("\n\n");
            logger.info("----------" + (++step) + "---9、查询绑定结果----------------------------");
            apiResponse = customerGoods(timestamp, aCase, step);

            addCustomerIds(apiResponse);

            String[] yuGoodsIds = {goods2_2};
            checkResult(apiResponse, yuGoodsIds, yuCustomerId,"yu");

            String[] xieGoodsIds = {goods4_2,goods2_4};
            checkResult(apiResponse, xieGoodsIds, xieCustomerId,"xie");

            msgTime = videoJson.getTimestamp() + 1;

            for (int i = 0; i < customerIds.size(); i++) {
                leaveShop(msgTime, personId, customerIds.get(i));
            }
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

    private void checkResult(ApiResponse apiResponse, String[] goodsIds, String customerId,String message) throws Exception {

        boolean isExist = false;

        String responseStr = JSON.toJSONString(apiResponse);
        logger.info(responseStr);
        JSONObject responseJo = JSON.parseObject(responseStr);

        JSONArray activeCustomerList = responseJo.getJSONObject("data").getJSONArray("active_customer_list");

        if (activeCustomerList==null || activeCustomerList.size()==0){
            throw new Exception(message + "-绑定列表为空！");
        }

        for (int i = 0; i < activeCustomerList.size(); i++) {
            JSONObject singleCustomer = activeCustomerList.getJSONObject(i);
            String customerIdRes = singleCustomer.getString("customer_id");

            JSONArray attentionGoods = singleCustomer.getJSONArray("attention_goods");

            String[] attentionGoodsArr = new String[attentionGoods.size()];

            for (int step = 0; step < attentionGoods.size(); step++) {
                JSONObject singleGoods = attentionGoods.getJSONObject(step);
                attentionGoodsArr[step] = singleGoods.getString("goods_id");
            }

            if (customerId.equals(customerIdRes)){
                isExist = true;
                Assert.assertEqualsNoOrder(attentionGoodsArr,goodsIds,message + "-绑定错误！expect：" +
                        Arrays.toString(goodsIds) + ",actual: " + Arrays.toString(attentionGoodsArr));
            }
        }

        if (!isExist){
            throw new Exception(message + "绑定列表为空！");
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

    private String sendRequestWithUrl(String url, String json) throws Exception {
        HttpExecutorUtil executor = new HttpExecutorUtil();
        executor.doPostJson(url, json);
        return executor.getResponse();
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

    public String getEnterPersonId(String json){
        JSONObject jsonJo = JSON.parseObject(json);
        String personId = jsonJo.getString("person_id");
        return personId;
    }

    public String getPickPersonId(String json) throws Exception {
        JSONObject jsonJo = JSON.parseObject(json);
        JSONArray faceListja = jsonJo.getJSONObject("data").getJSONArray("face_list");

        if (faceListja==null || faceListja.size()==0){
            throw new Exception("face_list为空！");
        }else {
            JSONObject singleFaceJo = faceListja.getJSONObject(0);
            String personId = singleFaceJo.getString("person_id");

            return personId;
        }
    }

    public  String getCustomerIdFromSync(String json){
        JSONObject resJo = JSON.parseObject(json);
        JSONObject visitorJo = resJo.getJSONObject("visitor");
        String customerId = visitorJo.getString("customer_id");

        customerIds.add(customerId);

        return customerId;
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

    private void addCustomerIds(ApiResponse apiResponse) {
        String responseStr = JSON.toJSONString(apiResponse);
        JSONArray customerListJa = JSON.parseObject(responseStr).getJSONObject("data").getJSONArray("active_customer_list");
        for (int i = 0; i < customerListJa.size(); i++) {
            JSONObject singleCustomer = customerListJa.getJSONObject(i);
            String customerId = singleCustomer.getString("customer_id");

            customerIds.add(customerId);
        }
    }

    @Test
    public void readCsv() throws Exception {
        File file = new File(filePath1.replace("/",File.separator));

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

        file = new File(filePath2.replace("/",File.separator));
        files = file.listFiles();

        for (int i = 0; i < files.length; i++) {
            BufferedReader bd = new BufferedReader(new FileReader(files[i]));
            String fileName = files[i].getName();
            String[] temp = fileName.split("-");
            long timeShift = Long.valueOf(temp[0]) + 249000;
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
            System.out.println(videoJson.getId()+"=="+videoJson.getTimeSift()+ "=="+ videoJson.getType());
            setNewJsonAndTimestamp(videoJson.getJson(), videoJson.getType(), videoJson.getTimeSift(), videoJson);
        }

//        for (int i = 0; i < jsonList.size(); i++) {
//            VideoJson videoJson = (VideoJson) jsonList.get(i);
//            System.out.println(videoJson.getId()+"==" + videoJson.getTimeSift()+ "=="+ videoJson.getType() + "\n" +
//                    JSON.parseObject(videoJson.getJson())  + "\n\n");
//        }

        jsonList.add(0, null);
    }


    @BeforeSuite
    public void initial() throws Exception {
        init();
//        readCsv();
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
