package com.haisheng.framework.testng.operationcenter;

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
import com.haisheng.framework.testng.CommonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.CommonDataStructure.LogMine;
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
    String customerId = "d409302b-9346-4fe7-a37d-a157e0a4";
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private LogMine logMine = new LogMine(logger);

    private String failReason = "";
    private QADbUtil qaDbUtil = new QADbUtil();
    private int APP_ID_DB = ChecklistDbInfo.DB_APP_ID_SHELF_SERVICE;
    private int CONFIG_ID = ChecklistDbInfo.DB_SERVICE_ID_SHELF_SERVICE;
    private String CI_CMD = "curl -X POST http://liaoxiangru:liaoxiangru@192.168.50.2:8080/job/commodity-management/buildWithParameters?case_name=";

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

    public String filePath1 = "src\\main\\java\\com\\haisheng\\framework\\testng\\operationcenter\\multiPickJson1";
    public String filePath2 = "src\\main\\java\\com\\haisheng\\framework\\testng\\operationcenter\\multiPickJson2";

    private void customerMessage(String json, String[] secKey, Case acase, int step) throws Exception {
        logger.info("\n");
        logger.info("------------customer message!-----------------------");
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

    private ApiResponse leaveShop(long msgTime,String personId) throws Exception {
        logger.info("\n");
        logger.info("------------leave shop!-----------------------");
        String router = "/commodity/external/CUSTOMER_TRACE/v1.0";
        String[] secKey = new String[]{};
        String message = "";

        try {

            String json =
                    "{\n" +
                            "        \"shop_id\":\"" + SHOP_ID + "\",\n" +
                            "        \"msg_time\":" + msgTime + "," +
                            "        \"customer\":{\n" +
                            "            \"customer_id\":\"" + customerId + "\"\n" +
                            "        },\n" +
                            "        \"region\":[\n" +
                            "            {\n" +
                            "                \"leave_entrance_type\":\"STORE\",\n" +
                            "                \"position_status\":\"LEAVE\"\n" +
                            "            }\n" +
                            "        ]\n" +
                            "}";

            String json1 =
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
                            "        \"" + personId +  "\"\n" +
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

    private void checkResult(ApiResponse apiResponse, String[] goodsIds, int size) throws Exception {

        String responseStr = JSON.toJSONString(apiResponse);
        logger.info(responseStr);
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
            ApiClient apiClient = new ApiClient("http://dev.api.winsenseos.com/retail/api/data/device", credential);
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
        long currentTime = System.currentTimeMillis();

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

    @Test
    public void readCsv() throws Exception {
        File file = new File(filePath1);
        File[] files = file.listFiles();

        System.out.println("files.length: " + files.length);

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

        file = new File(filePath2);
        files = file.listFiles();

        for (int i = 0; i < files.length; i++) {
            BufferedReader bd = new BufferedReader(new FileReader(files[i]));
            String fileName = files[i].getName();
            String[] temp = fileName.split("-");
            long timeShift = Long.valueOf(temp[0]) + 266000;
            String type = temp[1].substring(0, temp[1].lastIndexOf("."));

            String oldJson = bd.readLine();

            VideoJson videoJson = new VideoJson();

            videoJson.setType(type);
            videoJson.setTimeSift(timeShift);
            videoJson.setJson(oldJson);

            jsonList.add(videoJson);
        }

        sort(jsonList);

        System.out.println("jsonList.size():" + jsonList.size());

        for (int i = 0; i < jsonList.size(); i++) {
            VideoJson videoJson = (VideoJson) jsonList.get(i);
            setNewJsonAndTimestamp(videoJson.getJson(), videoJson.getType(), videoJson.getTimeSift(), videoJson);
        }

        jsonList.add(0, null);
    }


    @BeforeSuite
    public void initial() throws Exception {
//        init();
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
