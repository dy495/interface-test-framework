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
import com.haisheng.framework.util.QADbUtil;
import com.haisheng.framework.util.StatusCode;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class shelfServer {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private LogMine logMine = new LogMine(logger);
    private static String UID = "uid_87803c0c";
    private static String APP_CODE = "7485a90349a2";
    private static String AK = "8da9aeabd74198b1";
    private static String SK = "ec44b94f9b3cf4333c5d000781cb0289";

    private static long SHOP_ID = 640;
    private String genAuthURL = "http://39.106.253.190/administrator/login";
    String PLATE_CODE = "123";

    private String UNIT_NAME_POSI = "";

    String response;
    String authorization;
    HashMap<String, String> header = new HashMap();
    ApiResponse apiResponse;

    private String failReason = "";
    private QADbUtil qaDbUtil = new QADbUtil();
    private int APP_ID = ChecklistDbInfo.DB_APP_ID_SHELF_SERVICE;
    private int CONFIG_ID = ChecklistDbInfo.DB_SERVICE_ID_SHELF_SERVICE;
    private String CI_CMD = "curl -X POST http://shelf:shelf@192.168.50.2:8080/job/commodity-management/buildWithParameters?case_name=";


    public void sendResAndReqIdToDb(String response, Case acase, int step) {

        if (response != null && response.trim().length() > 0) {
            String requestId = JSON.parseObject(response).getString("request_id");
            String requestDataBefore = acase.getRequestData();
            if (requestDataBefore != null && requestDataBefore.trim().length() > 0) {
                acase.setRequestData(requestDataBefore + "(" + step + ") " + requestId + "\n");
            } else {
                acase.setRequestData("(" + step + ") " + requestId + "\n");
            }

//            ???response???????????????
            String responseBefore = acase.getResponse();
            if (responseBefore != null && responseBefore.trim().length() > 0) {
                acase.setResponse(responseBefore + "(" + step + ") " + JSON.parseObject(response) + "\n\n");
            } else {

                acase.setResponse(JSON.parseObject(response) + "\n\n");
            }
        }
    }

    public void sendResAndReqIdToDbApi(ApiResponse response, Case acase, int step) {

        if (response != null) {
//            ???requestId???????????????
            String requestId = response.getRequestId();
            String requestDataBefore = acase.getRequestData();
            if (requestDataBefore != null && requestDataBefore.trim().length() > 0) {
                acase.setRequestData(requestDataBefore + "(" + step + ") " + requestId + "\n");
            } else {
                acase.setRequestData("(" + step + ") " + requestId + "\n");
            }
        }
    }

    private String createFloor(String floorNum, String floorName, Case acase, int step) throws Exception {
        logger.info("\n");
        logger.info("------------create floor!-----------------------");

        String url = "http://39.106.253.190/admin/commodity/floor/create";

        //????????????
        StringBuffer jsonBF = new StringBuffer();
        jsonBF.append("{").
                append("    \"subject_id\":\"").append(SHOP_ID).append("\",\n").
                append("    \"floor_num\":\"").append(floorNum).append("\",\n").
                append("    \"floor_name\":\"").append(floorName).append("\"\n").
                append("}\n");
        try {
            response = sendRequestWithUrl(url, jsonBF.toString(), header);
        } catch (Exception e) {
            failReason = e.toString();
            throw e;
        }
        sendResAndReqIdToDb(response, acase, step);
        return response;
    }

    private String listFloor(String floorNum, Case acase, int step) throws Exception {
        logger.info("\n");
        logger.info("------------list floor!-----------------------");

        String url = "http://39.106.253.190/admin/commodity/floor/list";

        //????????????
        StringBuffer jsonBF = new StringBuffer();
        jsonBF.append("{\n").
                append("    \"page\":1,\n").
                append("    \"size\":10,\n").
                append("    \"subject_id\":\"").append(SHOP_ID).append("\",\n").
                append("    \"floor_num\":\"").append(floorNum).append("\"\n").
                append("}\n");

        try {
            response = sendRequestWithUrl(url, jsonBF.toString(), header);
        } catch (Exception e) {
            failReason = e.toString();
            throw e;
        }
        sendResAndReqIdToDb(response, acase, step);
        return response;
    }

    private String updateFloor(String id, String floorName, String floorMap, Case acase, int step) throws Exception {
        logger.info("\n");
        logger.info("------------update floor!-----------------------");

        String url = "http://39.106.253.190/admin/commodity/floor/update";

        //????????????
        StringBuffer jsonBF = new StringBuffer();
        jsonBF.append("{\n").
                append("    \"id\":\"").append(id).append("\",\n").
                append("    \"subject_id\":\"").append(SHOP_ID).append("\",\n").
                append("    \"floor_name\":\"").append(floorName).append("\",\n").
                append("    \"floor_map\":\"").append(floorMap).append("\"\n").
                append("}\n");

        try {
            response = sendRequestWithUrl(url, jsonBF.toString(), header);
        } catch (Exception e) {
            failReason = e.toString();
            throw e;
        }
        sendResAndReqIdToDb(response, acase, step);
        return response;
    }

    private String deleteFloor(String id) throws Exception {
        logger.info("\n");
        logger.info("------------delete floor!-----------------------");

        String url = "http://39.106.253.190/admin/commodity/floor/delete";

        //????????????
        StringBuffer jsonBF = new StringBuffer();
        jsonBF.append("{\n").
                append("    \"subject_id\":\"").append(SHOP_ID).append("\",\n").
                append("    \"id\":\"").append(id).append("\"\n").
                append("}\n");
        try {
            response = sendRequestWithUrl(url, jsonBF.toString(), header);
        } catch (Exception e) {
            failReason = e.toString();
            throw e;
        }
        return response;
    }

    private String detailFloor(String id, Case acase, int step) throws Exception {
        logger.info("\n");
        logger.info("------------detail floor!-----------------------");

        String url = "http://39.106.253.190/admin/commodity/floor/detail";

        //????????????
        String json =
                "{\n" +
                        "    \"id\":\"" + id + "\"\n" +
                        "}\n";
        try {
            response = sendRequestWithUrl(url, json, header);
        } catch (Exception e) {
            failReason = e.toString();
            throw e;
        }
        sendResAndReqIdToDb(response, acase, step);
        return response;
    }


    private String iniUnit(String unitCode, String plateCode, Case acase, int step) throws Exception {
        logger.info("\n");
        logger.info("------------init unit!-----------------------");

        String router = "/commodity/external/INIT_UNIT/v1.0";

        //????????????
        StringBuffer jsonBF = new StringBuffer();
        jsonBF.append("{\n").
                append("    \"plate_list\":[\n").
                append("        {\n");

        String json =
                "{\n" +
                        "    \"plate_list\":[\n" +
                        "        {\n" +
                        "            \"col_num\":4,\n" +
                        "            \"plate_code\":\"" + plateCode + "\",\n" +
                        "            \"row_num\":1,\n" +
                        "            \"sensors\":[\n" +
                        "                {\n" +
                        "                    \"sensor_id\":0,\n" +
                        "                    \"k\":1.231313,\n" +
                        "                    \"zero\":123213.123123\n" +
                        "                },\n" +
                        "                {\n" +
                        "                    \"sensor_id\":1,\n" +
                        "                    \"k\":1.231313,\n" +
                        "                    \"zero\":123213.123123\n" +
                        "                }\n" +
                        "            ]\n" +
                        "        }\n" +
                        "    ],\n" +
                        "    \"unit_code\": \"" + unitCode + "\"," +
                        "}\n";
        try {
            apiResponse = sendRequestWithGate(router, null, json);
        } catch (Exception e) {
            failReason = e.toString();
            throw e;
        }
        sendResAndReqIdToDbApi(apiResponse, acase, step);
        return response;
    }

    //-------------------------0.1 ??????unit---------------------------------
    private String createUnit(String unitCode, int height, int width, int depth, Case acase, int step) throws Exception {
        logger.info("\n");
        logger.info("------------create unit!-----------------------");

        String url = "http://39.106.253.190/admin/commodity/unit/create";

        //????????????
        String json =
                "{\n" +
                        "    \"unit_code\": \"" + unitCode + "\"," +
                        "    \"height\": \"" + height + "\"," +
                        "    \"width\": \"" + width + "\"," +
                        "    \"depth\": \"" + depth + "\"" +
                        "}";
        try {
            response = sendRequestWithUrl(url, json, header);
        } catch (Exception e) {
            failReason = e.toString();
            throw e;
        }
        sendResAndReqIdToDb(response, acase, step);
        return response;
    }

    //-------------------------??????????????????--unit---------------------------------
    private String listUnit(long subjectId, Case acase, int step) throws Exception {
        logger.info("\n");
        logger.info("------------list unit!-----------------------");

        String url = "http://39.106.253.190/admin/commodity/unit/list";

        //????????????
        String json =
                "{\n" +
                        "    \"page\":1,\n" +
                        "    \"size\":10,\n" +
                        "    \"subject_id\": \"" + subjectId + "\"" +
                        "}\n";
        try {
            response = sendRequestWithUrl(url, json, header);
        } catch (Exception e) {
            failReason = e.toString();
            throw e;
        }
        sendResAndReqIdToDb(response, acase, step);
        return response;
    }

    private String updateUnitWithoutPosi(String unitCode, String unitName, int height, int width, int depth, String floorId, Case acase, int step) throws Exception {
        logger.info("\n");
        logger.info("------------update unit!-----------------------");

        String url = "http://39.106.253.190/admin/commodity/unit/update";

        //????????????
        String json =
                "{" +
                        "    \"unit_code\": \"" + unitCode + "\"," +
                        "    \"unit_name\": \"" + unitName + "\"," +
                        "    \"floor_id\": \"" + floorId + "\"," +
                        "    \"height\": \"" + height + "\"," +
                        "    \"width\": \"" + width + "\"," +
                        "    \"depth\": \"" + depth + "\"" +
                        "}";
        try {
            response = sendRequestWithUrl(url, json, header);
        } catch (Exception e) {
            failReason = e.toString();
            throw e;
        }
        sendResAndReqIdToDb(response, acase, step);
        return response;
    }

    private String updateUnitWithPosi(String unitCode, String unitName, String floorId, double x, double y, Case acase, int step) throws Exception {
        logger.info("\n");
        logger.info("------------update unit!-----------------------");

        String url = "http://39.106.253.190/admin/commodity/unit/update";

        //????????????
        String json =
                "{" +
                        "    \"unit_code\": \"" + unitCode + "\"," +
                        "    \"unit_name\": \"" + unitName + "\"," +
                        "    \"floor_id\": \"" + floorId + "\"," +
                        "    \"position\":{" +
                        "        \"x\":" + x + "," +
                        "        \"y\":" + y +
                        "    }" +
                        "}";
        try {
            response = sendRequestWithUrl(url, json, header);
        } catch (Exception e) {
            failReason = e.toString();
            throw e;
        }
        sendResAndReqIdToDb(response, acase, step);
        return response;
    }

    private String updateUnitRmPosi(String unitCode, String unitName, String floorId, String posi, Case acase, int step) throws Exception {
        logger.info("\n");
        logger.info("------------update unit!-----------------------");

        String url = "http://39.106.253.190/admin/commodity/unit/update";

        //????????????
        String json =
                "{" +
                        "    \"unit_code\": \"" + unitCode + "\"," +
                        "    \"unit_name\": \"" + unitName + "\"," +
                        "    \"floor_id\": \"" + floorId + "\"," +
                        "    \"position\":\"" + posi + "\"" +
                        "}";
        try {
            response = sendRequestWithUrl(url, json, header);
        } catch (Exception e) {
            failReason = e.toString();
            throw e;
        }
        sendResAndReqIdToDb(response, acase, step);
        return response;
    }

    private String bindUnit(String unitCode, String unitName, String floorId, Case acase, int step) throws Exception {
        logger.info("\n");
        logger.info("------------bind unit!-----------------------");

        String url = "http://39.106.253.190/admin/commodity/unit/binding";

        //????????????
        String json =
                "{" +
                        "    \"unit_code\": \"" + unitCode + "\"," +
                        "    \"unit_name\": \"" + unitName + "\"," +
                        "    \"subject_id\": \"" + SHOP_ID + "\"," +
                        "    \"floor_id\": \"" + floorId + "\"," +
                        "    \"position\":{" +
                        "        \"x\":0.23," +
                        "        \"y\":0.45" +
                        "    }" +
                        "}";
        try {
            response = sendRequestWithUrl(url, json, header);
        } catch (Exception e) {
            failReason = e.toString();
            throw e;
        }
        sendResAndReqIdToDb(response, acase, step);
        return response;
    }

    private String unbindUnit(String unitCode, Case acase, int step) throws Exception {
        logger.info("\n");
        logger.info("------------unbind unit!-----------------------");

        String url = "http://39.106.253.190/admin/commodity/unit/unbinding";

        //????????????
        String json =
                "{" +
                        "    \"unit_code\": \"" + unitCode + "\"," +
                        "    \"subject_id\": \"" + SHOP_ID + "\"" +
                        "}";
        try {
            response = sendRequestWithUrl(url, json, header);
        } catch (Exception e) {
            failReason = e.toString();
            throw e;
        }
        sendResAndReqIdToDb(response, acase, step);
        return response;
    }

    private void deleteUnit(String unitCode, String shelvesCode) throws Exception {
        logger.info("\n");
        logger.info("------------delete unit!-----------------------");

        String url = "http://dev.app.winsenseos.cn/operation/app/COMMODITY/3576";

        HashMap<String, String> openPlatformHeader = new HashMap<>();
        openPlatformHeader.put("Authorization", genOpenPlatformAuth());

        //????????????
        String json =
                "{" +
                        "    \"shelves_code\": \"" + shelvesCode + "\"," +
                        "    \"unit_code\": \"" + unitCode + "\"" +
                        "}";
        try {
            response = sendRequestWithUrl(url, json, openPlatformHeader);
        } catch (Exception e) {
            throw e;
        }
    }

    public String genOpenPlatformAuth() {
        String url = "http://dev.sso.winsenseos.cn/sso/login";

        String json =
                "{\n" +
                        "  \"email\": \"demo@winsense.ai\",\n" +
                        "  \"password\": \"fe01ce2a7fbac8fafaed7c982a04e229\"\n" +
                        "}";

        String platformAuth = "";
        try {
            response = sendRequestWithUrl(url, json, new HashMap<>());
            logger.info("\n");
            JSONObject data = JSON.parseObject(response).getJSONObject("data");
            platformAuth = data.getString("token");


        } catch (Exception e) {
            e.printStackTrace();
        }

        return platformAuth;
    }


    private String createPlate(String plateCode, int width, double k0, double k1, Case acase, int step) throws Exception {
        logger.info("\n");
        logger.info("------------create plate!-----------------------");

        String url = "http://39.106.253.190/admin/commodity/plate/create";

        //????????????
        String json =
                "{\n" +
                        "    \"plate_code\": \"" + plateCode + "\"," +
                        "    \"sensors\":[\n" +
                        "        {\n" +
                        "            \"k\":\"" + k0 + "\",\n" +
                        "            \"plate_code\": \"" + plateCode + "\"," +
                        "            \"sensor_id\":0,\n" +
                        "            \"status\":\"NORMAL\",\n" +
                        "            \"zero\":0" +
                        "        },\n" +
                        "        {\n" +
                        "            \"k\":\"" + k1 + "\",\n" +
                        "            \"plate_code\": \"" + plateCode + "\"," +
                        "            \"sensor_id\":1,\n" +
                        "            \"status\":\"NORMAL\",\n" +
                        "            \"zero\":0" +
                        "        }\n" +
                        "    ],\n" +
                        "    \"width\": \"" + width + "\"" +
                        "}\n";
        try {
            response = sendRequestWithUrl(url, json, header);
        } catch (Exception e) {
            failReason = e.toString();
            throw e;
        }
        sendResAndReqIdToDb(response, acase, step);
        return response;
    }

    private String modifyConfig(String plateCode, int width, double k0, double k1, Case acase, int step) throws Exception {
        logger.info("\n");
        logger.info("------------modify plate!-----------------------");

        String url = "http://39.106.253.190/admin/commodity/plate/config/modify";

        //????????????
        String json =
                "{\n" +
                        "    \"plate_code\": \"" + plateCode + "\"," +
                        "    \"sensors\":[\n" +
                        "        {\n" +
                        "            \"k\":\"" + k0 + "\",\n" +
                        "            \"plate_code\": \"" + plateCode + "\"," +
                        "            \"sensor_id\":0,\n" +
                        "            \"status\":\"NORMAL\",\n" +
                        "            \"zero\":0" +
                        "        },\n" +
                        "        {\n" +
                        "            \"k\":\"" + k1 + "\",\n" +
                        "            \"plate_code\": \"" + plateCode + "\"," +
                        "            \"sensor_id\":1,\n" +
                        "            \"status\":\"NORMAL\",\n" +
                        "            \"zero\":0" +
                        "        }\n" +
                        "    ],\n" +
                        "    \"width\": \"" + width + "\"" +
                        "}\n";
        try {
            response = sendRequestWithUrl(url, json, header);
        } catch (Exception e) {
            failReason = e.toString();
            throw e;
        }
        sendResAndReqIdToDb(response, acase, step);
        return response;
    }

    private String listPlate(String plateCode, Case acase, int step) throws Exception {
        logger.info("\n");
        logger.info("------------list unit!-----------------------");

        String url = "http://39.106.253.190/admin/commodity/plate/list";

        //????????????
        String json =
                "{\n" +
                        "    \"page\":1,\n" +
                        "    \"size\":10,\n" +
                        "    \"plate_code\": \"" + plateCode + "\"," +
                        "}\n";
        try {
            response = sendRequestWithUrl(url, json, header);
        } catch (Exception e) {
            failReason = e.toString();
            throw e;
        }
        sendResAndReqIdToDb(response, acase, step);
        return response;
    }

    public String layoutPicUpload() throws IOException {

        String url = "http://39.106.253.190/admin/data/layout/layoutPicUpload";

        String filePath = "src/main/java/com/haisheng/framework/testng/operationcenter/shelf/experimentLayout";

        filePath = filePath.replace("/", File.separator);
        File file = new File(filePath);

        OkHttpClient client = new OkHttpClient();

        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);

//        builder.addFormDataPart("file_path", "console/layout/");

        builder.addFormDataPart("layout_pic", file.getName(), RequestBody.create
                (MediaType.parse("application/octet-stream"), file));

        //??????
        MultipartBody multipartBody = builder.build();

        //??????Request
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", authorization)
                .post(multipartBody)
                .build();

        Response response = client.newCall(request).execute();

        return response.body().string();

    }

//    -------------------------------(1)----------------------------------------

//	1. ???????????????2.???????????????3.???????????????

    @Test(priority = 1)
    public void testFloorsCreate() throws Exception {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName;
        String caseDesc = "??????????????????????????????";
        logger.info(caseDesc + "--------------------");

        Case aCase = new Case();
        failReason = "";

        int step = 0;

        String response;

        String floorNameOld = caseName + "-old";

        String floorNum = "1";

        String floorMapOld = null;

        String id = "";

        try {
            aCase.setRequestData("1. ???????????????2.???????????????3.???????????????" + "\n\n");

//            1???????????????
            logger.info("\n\n");
            logger.info("--------------------------------???" + (++step) + ")------------------------------");
            response = createFloor(floorNum, floorNameOld, aCase, step);
            checkCode(response, StatusCode.SUCCESS, "create floor ");

            id = JSON.parseObject(response).getJSONObject("data").getString("floor_id");

//            2???????????????
            logger.info("\n\n");
            logger.info("--------------------------------???" + (++step) + ")------------------------------");
            response = detailFloor(id, aCase, step);
            checkFloorDetail(response, id, floorNum, floorNameOld, floorMapOld);

//            3???????????????
            logger.info("\n\n");
            logger.info("--------------------------------???" + (++step) + ")------------------------------");
            response = listFloor(floorNum, aCase, step);
            checkFloorList(response, id, floorNameOld, floorMapOld, true);

            aCase.setResult("PASS");
        } catch (AssertionError e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
            e.printStackTrace();
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
            e.printStackTrace();
        } finally {
            deleteFloor(id);
            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);

            qaDbUtil.saveToCaseTable(aCase);
        }
    }

    //--------------------------------------???2???-??????????????????????????????---------------------------------------

//	1. ????????????-2.?????????????????????map???-3.????????????-4.????????????-5.?????????????????????map???-6.????????????-7.????????????


    @Test(priority = 1)
    public void testFloorsUpdate() throws Exception {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName;
        String caseDesc = "??????????????????????????????";
        logger.info(caseDesc + "--------------------");

        Case aCase = new Case();
        failReason = "";
        int step = 0;

        String response;

        String floorNameOld = caseName + "-old";
        String floorNameNew = caseName + "-new";

        String floorNum = "1";

        String floorMapNew = "";

        String id = "";


        try {
            aCase.setRequestData("1. ????????????-2.?????????????????????map???-3.????????????-4.????????????-5.?????????????????????map???-6.????????????-7.????????????" + "\n\n");

//            1???????????????
            logger.info("\n\n");
            logger.info("--------------------------------???" + (++step) + ")------------------------------");
            response = createFloor(floorNum, floorNameOld, aCase, step);
            checkCode(response, StatusCode.SUCCESS, "create floor ");

            id = JSON.parseObject(response).getJSONObject("data").getString("floor_id");

            response = layoutPicUpload();
            floorMapNew = JSON.parseObject(response).getJSONObject("data").getString("url");

//            2???????????????
            logger.info("\n\n");
            logger.info("--------------------------------???" + (++step) + ")------------------------------");
            response = updateFloor(id, floorNameNew, floorMapNew, aCase, step);
            checkCode(response, StatusCode.SUCCESS, "updateFloor--");

//            3???????????????
            logger.info("\n\n");
            logger.info("--------------------------------???" + (++step) + ")------------------------------");
            response = detailFloor(id, aCase, step);
            checkFloorDetail(response, id, floorNum, floorNameNew, floorMapNew);

//            4???????????????
            logger.info("\n\n");
            logger.info("--------------------------------???" + (++step) + ")------------------------------");
            response = listFloor(floorNum, aCase, step);
            checkFloorList(response, id, floorNameNew, floorMapNew, true);

//            5???????????????
            logger.info("\n\n");
            logger.info("--------------------------------???" + (++step) + ")------------------------------");
            response = updateFloor(id, floorNameNew, "", aCase, step);
            checkCode(response, StatusCode.SUCCESS, "updateFloor--");

//            6???????????????
            logger.info("\n\n");
            logger.info("--------------------------------???" + (++step) + ")------------------------------");
            response = listFloor(floorNum, aCase, step);
            checkFloorList(response, id, floorNameNew, null, true);

            aCase.setResult("PASS");

        } catch (AssertionError e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
            e.printStackTrace();
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
            e.printStackTrace();
        } finally {
            deleteFloor(id);
            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);

            qaDbUtil.saveToCaseTable(aCase);
        }
    }

//    --------------------------------------(3)----????????????------------------------------------------
//	1. ????????????-2.????????????-3.????????????


    @Test(priority = 1)
    public void testFloorsDelete() throws Exception {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName;
        String caseDesc = "??????????????????????????????";
        logger.info(caseDesc + "--------------------");

        Case aCase = new Case();
        failReason = "";
        int step = 0;

        String response;

        String floorNameOld = caseName + "-old";
        String floorNameNew = caseName + "-new";

        String floorNum = "1";

        String floorMapNew = "";

        String id = "";


        try {
            aCase.setRequestData("1. ????????????-2.????????????-3.????????????" + "\n\n");

//            1???????????????
            logger.info("\n\n");
            logger.info("--------------------------------???" + (++step) + ")------------------------------");
            response = createFloor(floorNum, floorNameOld, aCase, step);
            checkCode(response, StatusCode.SUCCESS, "create floor ");

            id = JSON.parseObject(response).getJSONObject("data").getString("floor_id");

//            2???????????????
            logger.info("\n\n");
            logger.info("--------------------------------???" + (++step) + ")------------------------------");
            response = deleteFloor(id);
            checkCode(response, StatusCode.SUCCESS, "deleteFloor--");

//            3???????????????
            logger.info("\n\n");
            logger.info("--------------------------------???" + (++step) + ")------------------------------");
            response = listFloor(floorNum, aCase, step);
            checkFloorList(response, id, floorNameNew, floorMapNew, false);

            aCase.setResult("PASS");

        } catch (AssertionError e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
            e.printStackTrace();
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
            e.printStackTrace();
        } finally {
            deleteFloor(id);
            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);

            qaDbUtil.saveToCaseTable(aCase);
        }
    }

//    -------------------------------------------------(4)??????????????????????????????----------------------------------------

//	1. ????????????-2.????????????

    @Test(priority = 1)
    public void testUnitCreate() throws Exception {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName;
        String caseDesc = "??????????????????????????????";
        logger.info(caseDesc + "--------------------");

        Case aCase = new Case();
        failReason = "";
        int step = 0;

        String response;

        String unitCode = caseName;
        String shelvesCode = unitCode;
        int height = 140;
        int width = 100;
        int depth = 40;

        try {
            aCase.setRequestData("1. ????????????-2.????????????" + "\n\n");

            deleteUnit(unitCode, shelvesCode);
//            1???????????????
            logger.info("\n\n");
            logger.info("--------------------------------???" + (++step) + ")------------------------------");
            response = createUnit(unitCode, height, width, depth, aCase, step);
            checkCode(response, StatusCode.SUCCESS, "createUnit");

//            2???????????????
            logger.info("\n\n");
            logger.info("--------------------------------???" + (++step) + ")------------------------------");
            response = listUnit(SHOP_ID, aCase, step);
            checkCode(response, StatusCode.SUCCESS, "listUnit");
            checkUnitList(response, unitCode, height, width, depth);

            aCase.setResult("PASS");

        } catch (AssertionError e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
            e.printStackTrace();
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
            e.printStackTrace();
        } finally {
            deleteUnit(unitCode, shelvesCode);
            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);

            qaDbUtil.saveToCaseTable(aCase);
        }
    }

//    ----------------------------------------???5?????????????????????????????????????????????----------------------------------

//	1. ????????????-2.-????????????????????????????????????-3.????????????


    @Test(priority = 1)
    public void testUnitUpdate3D() throws Exception {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName;
        String caseDesc = "??????????????????????????????????????????";
        logger.info(caseDesc + "--------------------");

        Case aCase = new Case();
        failReason = "";
        int step = 0;

        String response;

        String unitCode = caseName;
        String shelvesCode = unitCode;
        String floorName = caseName;
        String unitName = "unitNameUpdate";
        int heightOld = 140;
        int heightNew = 141;
        int widthOld = 100;
        int widthNew = 101;
        int depthOld = 40;
        int depthNew = 41;

        String floorId = "";

        try {
            aCase.setRequestData("1. ????????????-2.-????????????????????????????????????-3.????????????" + "\n\n");

            deleteUnit(unitCode, shelvesCode);
//            1???????????????
            logger.info("\n\n");
            logger.info("--------------------------------???" + (++step) + ")------------------------------");
            response = createUnit(unitCode, heightOld, widthOld, depthOld, aCase, step);
            checkCode(response, StatusCode.SUCCESS, "createUnit");

//            2???????????????
            logger.info("\n\n");
            logger.info("--------------------------------???" + (++step) + ")------------------------------");
            response = createFloor("1", floorName, aCase, step);
            checkCode(response, StatusCode.SUCCESS, "create Floor");

            floorId = JSON.parseObject(response).getJSONObject("data").getString("floor_id");

//            3???????????????
            logger.info("\n\n");
            logger.info("--------------------------------???" + (++step) + ")------------------------------");
            response = updateUnitWithoutPosi(unitCode, unitName, heightNew, widthNew, depthNew, floorId, aCase, step);
            checkCode(response, StatusCode.SUCCESS, "update Floor");

//            4???????????????
            logger.info("\n\n");
            logger.info("--------------------------------???" + (++step) + ")------------------------------");
            response = listUnit(SHOP_ID, aCase, step);
            checkCode(response, StatusCode.SUCCESS, "listUnit");
            checkUnitListAUpdate3D(response, unitCode, heightNew, widthNew, depthNew);

            aCase.setResult("PASS");

        } catch (AssertionError e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
            e.printStackTrace();
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
            e.printStackTrace();
        } finally {
            deleteUnit(unitCode, shelvesCode);
            deleteFloor(floorId);
            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);

            qaDbUtil.saveToCaseTable(aCase);
        }
    }

//    -----------------------???6?????????????????????????????????????????????????????????????????????------------------------------

//	1. ????????????-2.????????????-3.???????????????????????????-4.????????????-5.???????????????-6.?????????????????????????????????-
//	7.????????????-8.??????????????????????????????-9.????????????


    @Test(priority = 1)
    public void testUnitUpdatePosi() throws Exception {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName;
        String caseDesc = "??????????????????????????????????????????????????????????????????";
        logger.info(caseDesc + "--------------------");

        Case aCase = new Case();
        failReason = "";
        int step = 0;

        String response;

        String unitCode = caseName;
        String shelvesCode = unitCode;
        String floorName = caseName;
        String unitName = "unitNameUpdatePosi";
        int height = 140;
        int width = 100;
        int depth = 40;

        double x = 5.5D;
        double y = 5.5D;

        String floorId = "";

        try {
            aCase.setRequestData("1. ????????????-2.????????????-3.???????????????????????????-4.????????????-5.???????????????-6.?????????????????????????????????" +
                    "-7.????????????-8.??????????????????????????????-9.????????????" + "\n\n");

            deleteUnit(unitCode, shelvesCode);
//            1???????????????
            logger.info("\n\n");
            logger.info("--------------------------------???" + (++step) + ")------------------------------");
            response = createUnit(unitCode, height, width, depth, aCase, step);
            checkCode(response, StatusCode.SUCCESS, "createUnit");

//            2???????????????
            logger.info("\n\n");
            logger.info("--------------------------------???" + (++step) + ")------------------------------");
            response = createFloor("1", floorName, aCase, step);
            checkCode(response, StatusCode.SUCCESS, "create Floor");

            floorId = JSON.parseObject(response).getJSONObject("data").getString("floor_id");

            response = layoutPicUpload();
            String floorMap = JSON.parseObject(response).getJSONObject("data").getString("url");

//            3???????????????
            logger.info("\n\n");
            logger.info("--------------------------------???" + (++step) + ")------------------------------");
            response = updateFloor(floorId, floorName, floorMap, aCase, step);
            checkCode(response, StatusCode.SUCCESS, "updateFloor--");

//            4???????????????
            logger.info("\n\n");
            logger.info("--------------------------------???" + (++step) + ")------------------------------");
            response = bindUnit(unitCode, unitName, floorId, aCase, step);
            checkCode(response, StatusCode.SUCCESS, "bindUnit--");

//            5??????????????????
            logger.info("\n\n");
            logger.info("--------------------------------???" + (++step) + ")------------------------------");
            response = iniUnit(unitCode, PLATE_CODE, aCase, step);
            checkCode(response, StatusCode.SUCCESS, "iniUnit--");

//            6???????????????
            logger.info("\n\n");
            logger.info("--------------------------------???" + (++step) + ")------------------------------");
            response = updateUnitWithPosi(unitCode, unitName, floorId, x, y, aCase, step);
            checkCode(response, StatusCode.SUCCESS, "update Floor");

//            7???????????????
            logger.info("\n\n");
            logger.info("--------------------------------???" + (++step) + ")------------------------------");
            response = listUnit(SHOP_ID, aCase, step);
            checkCode(response, StatusCode.SUCCESS, "listUnit");
            checkUnitListAUpdatePosi(response, unitCode, unitName, floorId, x, y);

//            8???????????????????????????????????????
            logger.info("\n\n");
            logger.info("--------------------------------???" + (++step) + ")------------------------------");
            response = updateUnitRmPosi(unitCode, unitName, floorId, "", aCase, step);
            checkCode(response, StatusCode.SUCCESS, "update Floor");

//            9???????????????
            logger.info("\n\n");
            logger.info("--------------------------------???" + (++step) + ")------------------------------");
            response = listUnit(SHOP_ID, aCase, step);
            checkCode(response, StatusCode.SUCCESS, "listUnit");
            checkUnitListRmPosi(response, unitCode, unitName, floorId);

            aCase.setResult("PASS");

        } catch (AssertionError e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
            e.printStackTrace();
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
            e.printStackTrace();
        } finally {
            deleteUnit(unitCode, shelvesCode);
            deleteFloor(floorId);

            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);

            qaDbUtil.saveToCaseTable(aCase);
        }
    }

    //---------------------------???7???-------????????????????????????????????????????????????????????????????????????-------------------------------------

//	1. ????????????-2.????????????-3.?????????????????????????????????-4.???????????????-5.????????????-6.????????????
//	7. ???????????????????????????-8.??????????????????????????????-9.????????????

    @Test(priority = 1)
    public void testUnitUpdatePosi1() throws Exception {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName;
        String caseDesc = "?????????????????????????????????????????????????????????????????????";
        logger.info(caseDesc + "--------------------");

        Case aCase = new Case();
        failReason = "";
        int step = 0;

        String response;

        String unitCode = caseName;
        String shelvesCode = unitCode;
        String floorName = caseName;
        String unitName = "unitNameUpdatePosi";
        int height = 140;
        int width = 100;
        int depth = 40;

        double x = 5.5D;
        double y = 5.5D;

        String floorId = "";

        try {

            aCase.setRequestData("1. ????????????-2.????????????-3.?????????????????????????????????-4.???????????????-5.????????????-6.????????????-" +
                    "7. ???????????????????????????-8.??????????????????????????????-9.????????????" + "\n\n");

            deleteUnit(unitCode, shelvesCode);

//            1???????????????
            logger.info("\n\n");
            logger.info("--------------------------------???" + (++step) + ")------------------------------");
            response = createUnit(unitCode, height, width, depth, aCase, step);
            checkCode(response, StatusCode.SUCCESS, "createUnit");

//            2???????????????
            logger.info("\n\n");
            logger.info("--------------------------------???" + (++step) + ")------------------------------");
            response = createFloor("1", floorName, aCase, step);
            checkCode(response, StatusCode.SUCCESS, "create Floor");

            floorId = JSON.parseObject(response).getJSONObject("data").getString("floor_id");

            response = layoutPicUpload();
            String floorMap = JSON.parseObject(response).getJSONObject("data").getString("url");

//            3???????????????
            logger.info("\n\n");
            logger.info("--------------------------------???" + (++step) + ")------------------------------");
            response = bindUnit(unitCode, unitName, floorId, aCase, step);
            checkCode(response, StatusCode.SUCCESS, "bindUnit--");

//            4??????????????????
            logger.info("\n\n");
            logger.info("--------------------------------???" + (++step) + ")------------------------------");
            response = iniUnit(unitCode, PLATE_CODE, aCase, step);
            checkCode(response, StatusCode.SUCCESS, "iniUnit--");

//            5???????????????
            logger.info("\n\n");
            logger.info("--------------------------------???" + (++step) + ")------------------------------");
            response = updateFloor(floorId, floorName, floorMap, aCase, step);
            checkCode(response, StatusCode.SUCCESS, "updateFloor--");

//            6??????????????????????????????????????????
            logger.info("\n\n");
            logger.info("--------------------------------???" + (++step) + ")------------------------------");
            response = updateUnitRmPosi(unitCode, unitName, floorId, "", aCase, step);
            checkCode(response, StatusCode.SUCCESS, "update unit");

//            7???????????????
            logger.info("\n\n");
            logger.info("--------------------------------???" + (++step) + ")------------------------------");
            response = listUnit(SHOP_ID, aCase, step);
            checkCode(response, StatusCode.SUCCESS, "listUnit");
            checkUnitListRmPosi(response, unitCode, unitName, floorId);

//            8????????????????????????????????????
            logger.info("\n\n");
            logger.info("--------------------------------???" + (++step) + ")------------------------------");
            response = updateUnitWithPosi(unitCode, unitName, floorId, x, y, aCase, step);
            checkCode(response, StatusCode.SUCCESS, "update Floor");

//            9???????????????
            logger.info("\n\n");
            logger.info("--------------------------------???" + (++step) + ")------------------------------");
            response = listUnit(SHOP_ID, aCase, step);
            checkCode(response, StatusCode.SUCCESS, "listUnit");
            checkUnitListAUpdatePosi(response, unitCode, unitName, floorId, x, y);

            aCase.setResult("PASS");

        } catch (AssertionError e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
            e.printStackTrace();
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
            e.printStackTrace();
        } finally {
            deleteUnit(unitCode, shelvesCode);
            deleteFloor(floorId);

            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);

            qaDbUtil.saveToCaseTable(aCase);
        }
    }

    //    ----------------------???8???---------------------???????????????????????????????????????????????????????????????????????????-----------------

//	1. ????????????-2.????????????-3.?????????????????????????????????-4.???????????????-5.????????????-6.????????????-7.???????????????????????????-8.??????????????????????????????-9.????????????

    @Test(priority = 1)
    public void testUnitUpdatePosi2() throws Exception {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName;
        String caseDesc = "???????????????????????????????????????????????????????????????????????????";
        logger.info(caseDesc + "--------------------");

        Case aCase = new Case();
        failReason = "";
        int step = 0;

        String response;

        String unitCode = caseName;
        String shelvesCode = unitCode;
        String floorName = caseName;
        String unitName = "unitNameUpdatePosi";
        int height = 140;
        int width = 100;
        int depth = 40;

        double xOld = 3.2D;
        double yOld = 5.5D;

        double xNew = 6.8D;
        double yNew = 7.5D;

        String floorId = "";

        try {
            aCase.setRequestData("1. ????????????-2.????????????-3.?????????????????????????????????-4.???????????????-5.????????????-" +
                    "6.????????????-7.???????????????????????????-8.??????????????????????????????-9.????????????" + "\n\n");

            deleteUnit(unitCode, shelvesCode);

//            1???????????????
            logger.info("\n\n");
            logger.info("--------------------------------???" + (++step) + ")------------------------------");
            response = createUnit(unitCode, height, width, depth, aCase, step);
            checkCode(response, StatusCode.SUCCESS, "createUnit");

//            2???????????????
            logger.info("\n\n");
            logger.info("--------------------------------???" + (++step) + ")------------------------------");
            response = createFloor("1", floorName, aCase, step);
            checkCode(response, StatusCode.SUCCESS, "create Floor");

            floorId = JSON.parseObject(response).getJSONObject("data").getString("floor_id");

            response = layoutPicUpload();
            String floorMap = JSON.parseObject(response).getJSONObject("data").getString("url");

//            3???????????????
            logger.info("\n\n");
            logger.info("--------------------------------???" + (++step) + ")------------------------------");
            response = bindUnit(unitCode, unitName, floorId, aCase, step);
            checkCode(response, StatusCode.SUCCESS, "bindUnit--");

//            4??????????????????
            logger.info("\n\n");
            logger.info("--------------------------------???" + (++step) + ")------------------------------");
            response = iniUnit(unitCode, PLATE_CODE, aCase, step);
            checkCode(response, StatusCode.SUCCESS, "iniUnit--");

//            5???????????????
            logger.info("\n\n");
            logger.info("--------------------------------???" + (++step) + ")------------------------------");
            response = updateFloor(floorId, floorName, floorMap, aCase, step);
            checkCode(response, StatusCode.SUCCESS, "updateFloor--");

//            6???????????????????????????????????????
            logger.info("\n\n");
            logger.info("--------------------------------???" + (++step) + ")------------------------------");
            response = updateUnitWithPosi(unitCode, unitName, floorId, xOld, yOld, aCase, step);
            checkCode(response, StatusCode.SUCCESS, "update unit");

//            7???????????????
            logger.info("\n\n");
            logger.info("--------------------------------???" + (++step) + ")------------------------------");
            response = listUnit(SHOP_ID, aCase, step);
            checkCode(response, StatusCode.SUCCESS, "listUnit");
            checkUnitListAUpdatePosi(response, unitCode, unitName, floorId, xOld, yOld);

//            8????????????????????????????????????
            logger.info("\n\n");
            logger.info("--------------------------------???" + (++step) + ")------------------------------");
            response = updateUnitWithPosi(unitCode, unitName, floorId, xNew, yNew, aCase, step);
            checkCode(response, StatusCode.SUCCESS, "update Floor");

//            9???????????????
            logger.info("\n\n");
            logger.info("--------------------------------???" + (++step) + ")------------------------------");
            response = listUnit(SHOP_ID, aCase, step);
            checkCode(response, StatusCode.SUCCESS, "listUnit");
            checkUnitListAUpdatePosi(response, unitCode, unitName, floorId, xNew, yNew);

            aCase.setResult("PASS");

        } catch (AssertionError e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
            e.printStackTrace();
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
            e.printStackTrace();
        } finally {
            deleteUnit(unitCode, shelvesCode);
            deleteFloor(floorId);

            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);

            qaDbUtil.saveToCaseTable(aCase);
        }
    }

//--------------------------------------------------(9) ??????????????????????????????unit???position???????????????-----------------------------------

//	1. ????????????-2.????????????-3.????????????-4.???????????????-5.???????????????????????????6.??????????????????????????????7.?????????????????????????????????8.????????????

    @Test(priority = 1)
    public void testUnitDeleteLayout() throws Exception {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName;
        String caseDesc = "??????????????????????????????unit???position???????????????";
        logger.info(caseDesc + "--------------------");

        Case aCase = new Case();
        failReason = "";
        int step = 0;

        String response;

        String unitCode = caseName;
        String shelvesCode = unitCode;
        String floorName = caseName;
        String unitName = "unitNameUpdatePosi";
        int height = 140;
        int width = 100;
        int depth = 40;

        double xOld = 3.2D;
        double yOld = 5.5D;

        String floorId = "";

        try {
            aCase.setRequestData("1. ????????????-2.????????????-3.????????????-4.???????????????-5.???????????????????????????6.??????????????????????????????" +
                    "7.?????????????????????????????????8.????????????" + "\n\n");

            deleteUnit(unitCode, shelvesCode);

//            1???????????????
            logger.info("\n\n");
            logger.info("--------------------------------???" + (++step) + ")------------------------------");
            response = createUnit(unitCode, height, width, depth, aCase, step);
            checkCode(response, StatusCode.SUCCESS, "createUnit");

//            2???????????????
            logger.info("\n\n");
            logger.info("--------------------------------???" + (++step) + ")------------------------------");
            response = createFloor("1", floorName, aCase, step);
            checkCode(response, StatusCode.SUCCESS, "create Floor");

            floorId = JSON.parseObject(response).getJSONObject("data").getString("floor_id");

            response = layoutPicUpload();
            String floorMap = JSON.parseObject(response).getJSONObject("data").getString("url");

//            3???????????????
            logger.info("\n\n");
            logger.info("--------------------------------???" + (++step) + ")------------------------------");
            response = bindUnit(unitCode, unitName, floorId, aCase, step);
            checkCode(response, StatusCode.SUCCESS, "bindUnit--");

//            4??????????????????
            logger.info("\n\n");
            logger.info("--------------------------------???" + (++step) + ")------------------------------");
            response = iniUnit(unitCode, PLATE_CODE, aCase, step);
            checkCode(response, StatusCode.SUCCESS, "iniUnit--");

//            5???????????????
            logger.info("\n\n");
            logger.info("--------------------------------???" + (++step) + ")------------------------------");
            response = updateFloor(floorId, floorName, floorMap, aCase, step);
            checkCode(response, StatusCode.SUCCESS, "updateFloor--");

//            6???????????????????????????????????????
            logger.info("\n\n");
            logger.info("--------------------------------???" + (++step) + ")------------------------------");
            response = updateUnitWithPosi(unitCode, unitName, floorId, xOld, yOld, aCase, step);
            checkCode(response, StatusCode.SUCCESS, "update unit");

//            7????????????????????????????????????
            logger.info("\n\n");
            logger.info("--------------------------------???" + (++step) + ")------------------------------");
            response = updateFloor(floorId, floorName, "", aCase, step);
            checkCode(response, StatusCode.SUCCESS, "updateFloor--");

//            8???????????????
            logger.info("\n\n");
            logger.info("--------------------------------???" + (++step) + ")------------------------------");
            response = listUnit(SHOP_ID, aCase, step);
            checkCode(response, StatusCode.SUCCESS, "listUnit");
            checkUnitListRmPosi(response, unitCode, unitName, floorId);

            aCase.setResult("PASS");

        } catch (AssertionError e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
            e.printStackTrace();
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
            e.printStackTrace();
        } finally {
            deleteUnit(unitCode, shelvesCode);
            deleteFloor(floorId);

            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);

            qaDbUtil.saveToCaseTable(aCase);
        }
    }


    //--------------------------------------------------(10) ??????????????????????????????unit???position???????????????-----------------------------------

//	1. ????????????-2.????????????-3.????????????-4.???????????????-5.???????????????????????????6.??????????????????????????????7.?????????????????????????????????8.????????????

    @Test(priority = 1)
    public void testUnitUpdateLayout() throws Exception {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName;
        String caseDesc = "??????????????????????????????unit???position???????????????";
        logger.info(caseDesc + "--------------------");

        Case aCase = new Case();
        failReason = "";
        int step = 0;

        String response;

        String unitCode = caseName;
        String shelvesCode = unitCode;
        String floorName = caseName;
        String unitName = "unitNameUpdatePosi";
        int height = 140;
        int width = 100;
        int depth = 40;

        double xOld = 3.2D;
        double yOld = 5.5D;

        String floorId = "";

        try {
            aCase.setRequestData("1. ????????????-2.????????????-3.????????????-4.???????????????-5.???????????????????????????6.??????????????????????????????" +
                    "7.?????????????????????????????????8.????????????" + "\n\n");

            deleteUnit(unitCode, shelvesCode);

//            1???????????????
            logger.info("\n\n");
            logger.info("--------------------------------???" + (++step) + ")------------------------------");
            response = createUnit(unitCode, height, width, depth, aCase, step);
            checkCode(response, StatusCode.SUCCESS, "createUnit");

//            2???????????????
            logger.info("\n\n");
            logger.info("--------------------------------???" + (++step) + ")------------------------------");
            response = createFloor("1", floorName, aCase, step);
            checkCode(response, StatusCode.SUCCESS, "create Floor");

            floorId = JSON.parseObject(response).getJSONObject("data").getString("floor_id");

            response = layoutPicUpload();
            String floorMap = JSON.parseObject(response).getJSONObject("data").getString("url");

//            3???????????????
            logger.info("\n\n");
            logger.info("--------------------------------???" + (++step) + ")------------------------------");
            response = bindUnit(unitCode, unitName, floorId, aCase, step);
            checkCode(response, StatusCode.SUCCESS, "bindUnit--");

//            4??????????????????
            logger.info("\n\n");
            logger.info("--------------------------------???" + (++step) + ")------------------------------");
            response = iniUnit(unitCode, PLATE_CODE, aCase, step);
            checkCode(response, StatusCode.SUCCESS, "iniUnit--");

//            5???????????????
            logger.info("\n\n");
            logger.info("--------------------------------???" + (++step) + ")------------------------------");
            response = updateFloor(floorId, floorName, floorMap, aCase, step);
            checkCode(response, StatusCode.SUCCESS, "updateFloor--");

//            6???????????????????????????????????????
            logger.info("\n\n");
            logger.info("--------------------------------???" + (++step) + ")------------------------------");
            response = updateUnitWithPosi(unitCode, unitName, floorId, xOld, yOld, aCase, step);
            checkCode(response, StatusCode.SUCCESS, "update unit");

//            7????????????????????????????????????
            logger.info("\n\n");
            logger.info("--------------------------------???" + (++step) + ")------------------------------");
            response = updateFloor(floorId, floorName, floorMap, aCase, step);
            checkCode(response, StatusCode.SUCCESS, "updateFloor--");

//            8???????????????
            logger.info("\n\n");
            logger.info("--------------------------------???" + (++step) + ")------------------------------");
            response = listUnit(SHOP_ID, aCase, step);
            checkCode(response, StatusCode.SUCCESS, "listUnit");
            checkUnitListRmPosi(response, unitCode, unitName, floorId);

            aCase.setResult("PASS");

        } catch (AssertionError e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
            e.printStackTrace();
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
            e.printStackTrace();
        } finally {
            deleteUnit(unitCode, shelvesCode);
            deleteFloor(floorId);

            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);

            qaDbUtil.saveToCaseTable(aCase);
        }
    }

    //---------------------------???11???-----??????????????????????????????------------------------------------------------

//	1. ????????????-2.????????????-3.????????????

    @Test(priority = 1)
    public void testUnitBind() throws Exception {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName;
        String caseDesc = "??????????????????????????????";
        logger.info(caseDesc + "--------------------");

        Case aCase = new Case();
        failReason = "";
        int step = 0;

        String response;

        String unitCode = caseName;
        String shelvesCode = unitCode;
        String floorName = caseName;
        String unitName = "unitBind";
        int height = 140;
        int width = 100;
        int depth = 40;

        String floorId = "";

        try {
            aCase.setRequestData("1. ????????????-2.????????????-3.????????????" + "\n\n");

            deleteUnit(unitCode, shelvesCode);

//            1???????????????
            logger.info("\n\n");
            logger.info("--------------------------------???" + (++step) + ")------------------------------");
            response = createUnit(unitCode, height, width, depth, aCase, step);
            checkCode(response, StatusCode.SUCCESS, "createUnit");

//            2???????????????
            logger.info("\n\n");
            logger.info("--------------------------------???" + (++step) + ")------------------------------");
            response = createFloor("1", floorName, aCase, step);
            checkCode(response, StatusCode.SUCCESS, "create Floor");

            floorId = JSON.parseObject(response).getJSONObject("data").getString("floor_id");

//            2???????????????
            logger.info("\n\n");
            logger.info("--------------------------------???" + (++step) + ")------------------------------");
            response = bindUnit(unitCode, unitName, floorId, aCase, step);
            checkCode(response, StatusCode.SUCCESS, "bindUnit--");

//            3???????????????
            logger.info("\n\n");
            logger.info("--------------------------------???" + (++step) + ")------------------------------");
            response = listUnit(SHOP_ID, aCase, step);
            checkCode(response, StatusCode.SUCCESS, "listUnit");
            checkUnitListABind(response, unitCode, floorId);

            aCase.setResult("PASS");

        } catch (AssertionError e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
            e.printStackTrace();
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
            e.printStackTrace();
        } finally {
            deleteUnit(unitCode, shelvesCode);
            deleteFloor(floorId);

            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);

            qaDbUtil.saveToCaseTable(aCase);
        }
    }

    //---------------------------???12???-----??????????????????????????????------------------------------------------------

//	1. ????????????-2.????????????-3.????????????-4.????????????

    @Test(priority = 1)
    public void testUnitUnbind() throws Exception {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName;
        String caseDesc = "??????????????????????????????";
        logger.info(caseDesc + "--------------------");

        Case aCase = new Case();
        failReason = "";
        int step = 0;

        String response;

        String unitCode = caseName;
        String shelvesCode = unitCode;
        String floorName = caseName;
        String unitName = "unbindUnit";
        int height = 140;
        int width = 100;
        int depth = 40;

        String floorId = "";

        try {
            aCase.setRequestData("1. ????????????-2.????????????-3.????????????-4.????????????" + "\n\n");

            deleteUnit(unitCode, shelvesCode);

//            1???????????????
            logger.info("\n\n");
            logger.info("--------------------------------???" + (++step) + ")------------------------------");
            response = createUnit(unitCode, height, width, depth, aCase, step);
            checkCode(response, StatusCode.SUCCESS, "createUnit");

//            2???????????????
            logger.info("\n\n");
            logger.info("--------------------------------???" + (++step) + ")------------------------------");
            response = createFloor("1", floorName, aCase, step);
            checkCode(response, StatusCode.SUCCESS, "create Floor");

            floorId = JSON.parseObject(response).getJSONObject("data").getString("floor_id");

//            3???????????????
            logger.info("\n\n");
            logger.info("--------------------------------???" + (++step) + ")------------------------------");
            response = bindUnit(unitCode, unitName, floorId, aCase, step);
            checkCode(response, StatusCode.SUCCESS, "bindUnit--");

//            4???????????????
            logger.info("\n\n");
            logger.info("--------------------------------???" + (++step) + ")------------------------------");
            response = unbindUnit(unitCode, aCase, step);
            checkCode(response, StatusCode.SUCCESS, "unbindUnit");

//            5???????????????
            logger.info("\n\n");
            logger.info("--------------------------------???" + (++step) + ")------------------------------");
            response = listUnit(SHOP_ID, aCase, step);
            checkCode(response, StatusCode.SUCCESS, "listUnit");
            checkUnitListAUnbind(response, unitCode);

            aCase.setResult("PASS");

        } catch (AssertionError e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
            e.printStackTrace();
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
            e.printStackTrace();
        } finally {
            deleteUnit(unitCode, shelvesCode);
            deleteFloor(floorId);

            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);

            qaDbUtil.saveToCaseTable(aCase);
        }
    }

    //------------------------------(13)------???????????????????????????---------------------------------------

//	1. ????????????-2.????????????-3.????????????-4.????????????-5.????????????

    @Test(priority = 1)
    public void testUnitRebind() throws Exception {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName;
        String caseDesc = "???????????????????????????";
        logger.info(caseDesc + "--------------------");

        Case aCase = new Case();
        failReason = "";
        int step = 0;

        String response;

        String unitCode = caseName;
        String shelvesCode = unitCode;
        String floorName = caseName;
        String unitName = "rebindUnit";
        int height = 140;
        int width = 100;
        int depth = 40;

        String floorId = "";

        try {
            aCase.setRequestData("1. ????????????-2.????????????-3.????????????-4.????????????-5.????????????" + "\n\n");

            deleteUnit(unitCode, shelvesCode);

//            1???????????????
            logger.info("\n\n");
            logger.info("--------------------------------???" + (++step) + ")------------------------------");
            response = createUnit(unitCode, height, width, depth, aCase, step);
            checkCode(response, StatusCode.SUCCESS, "createUnit");

//            2???????????????
            logger.info("\n\n");
            logger.info("--------------------------------???" + (++step) + ")------------------------------");
            response = createFloor("1", floorName, aCase, step);
            checkCode(response, StatusCode.SUCCESS, "create Floor");

            floorId = JSON.parseObject(response).getJSONObject("data").getString("floor_id");

//            3???????????????
            logger.info("\n\n");
            logger.info("--------------------------------???" + (++step) + ")------------------------------");
            response = bindUnit(unitCode, unitName, floorId, aCase, step);
            checkCode(response, StatusCode.SUCCESS, "bindUnit--");

//            4???????????????
            logger.info("\n\n");
            logger.info("--------------------------------???" + (++step) + ")------------------------------");
            response = unbindUnit(unitCode, aCase, step);
            checkCode(response, StatusCode.SUCCESS, "unbindUnit");

//            5???????????????
            logger.info("\n\n");
            logger.info("--------------------------------???" + (++step) + ")------------------------------");
            response = bindUnit(unitCode, unitName, floorId, aCase, step);
            checkCode(response, StatusCode.SUCCESS, "bindUnit--");

//            6???????????????
            logger.info("\n\n");
            logger.info("--------------------------------???" + (++step) + ")------------------------------");
            response = listUnit(SHOP_ID, aCase, step);
            checkCode(response, StatusCode.SUCCESS, "listUnit");
            checkUnitListABind(response, unitCode, floorId);

            aCase.setResult("PASS");

        } catch (AssertionError e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
            e.printStackTrace();
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
            e.printStackTrace();
        } finally {
            deleteUnit(unitCode, shelvesCode);
            deleteFloor(floorId);

            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);

            qaDbUtil.saveToCaseTable(aCase);
        }
    }

    //    ----------------------------(14)-?????????????????????????????????--------------------------------------------------------
//	1. ???????????????-2.???????????????

    @Test(priority = 1)
    public void testcreatePlate() throws Exception {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName;
        String caseDesc = "?????????????????????????????????";
        logger.info(caseDesc + "--------------------");

        Case aCase = new Case();
        failReason = "";
        int step = 0;

        String response;

        String plateCode = String.valueOf(System.currentTimeMillis());
        int width = 4;
        double k0 = 0.111D;
        double k1 = -0.112D;

        try {

            aCase.setRequestData("1. ???????????????-2.???????????????" + "\n\n");

//            1??????????????????
            logger.info("\n\n");
            logger.info("--------------------------------???" + (++step) + ")------------------------------");
            response = createPlate(plateCode, width, k0, k1, aCase, step);
            checkCode(response, StatusCode.SUCCESS, "createPlate");

//            3??????????????????
            logger.info("\n\n");
            logger.info("--------------------------------???" + (++step) + ")------------------------------");
            response = listPlate(plateCode, aCase, step);
            //zero?????????????????????0???????????????????????????,
            checkUnitListPlate(response, plateCode, width, k0, k1);

            aCase.setResult("PASS");

        } catch (AssertionError e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
            e.printStackTrace();
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
            e.printStackTrace();
        } finally {
            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);

            qaDbUtil.saveToCaseTable(aCase);
        }
    }

    //---------------------------------------------------???????????????????????????--------------------------------
//	1. ???????????????-2.????????????-3.??????

    @Test(priority = 1)
    public void testModifyPlate() {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName;
        String caseDesc = "???????????????????????????";
        logger.info(caseDesc + "--------------------");

        Case aCase = new Case();
        failReason = "";
        int step = 0;

        String response;

        String plateCode = String.valueOf(System.currentTimeMillis());
        int width = 4;
        int widthNew = 5;

        double k0 = 0.111D;
        double k1 = -0.112D;

        double k0New = 0.222D;
        double k1New = -0.333D;

        try {

            aCase.setRequestData("1. ???????????????-2.????????????-3.??????" + "\n\n");
//            1??????????????????
            logger.info("\n\n");
            logger.info("--------------------------------???" + (++step) + ")------------------------------");
            response = createPlate(plateCode, width, k0, k1, aCase, step);
            checkCode(response, StatusCode.SUCCESS, "createPlate");

//            2????????????????????????
            logger.info("\n\n");
            logger.info("--------------------------------???" + (++step) + ")------------------------------");
            response = modifyConfig(plateCode, widthNew, k0New, k1New, aCase, step);
            checkCode(response, StatusCode.SUCCESS, "modifyPlate");

//            3??????????????????
            logger.info("\n\n");
            logger.info("--------------------------------???" + (++step) + ")------------------------------");
            response = listPlate(plateCode, aCase, step);
            //zero?????????????????????0???????????????????????????,
            checkUnitListPlate(response, plateCode, widthNew, k0New, k1New);

            aCase.setResult("PASS");

        } catch (AssertionError e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
            e.printStackTrace();
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
            e.printStackTrace();
        } finally {
            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);

            qaDbUtil.saveToCaseTable(aCase);
        }
    }

    private void checkUnitListAUpdatePosi(String response, String unitCode, String unitName, String floorId, double x, double y) {
        JSONObject data = JSON.parseObject(response).getJSONObject("data");
        JSONArray list = data.getJSONArray("list");
        for (int i = 0; i < list.size(); i++) {
            JSONObject listSingle = list.getJSONObject(i);
            String unitCodeRes = listSingle.getString("unit_code");
            if (unitCode.equals(unitCodeRes)) {
                String unitNameRes = listSingle.getString("unit_name");
                Assert.assertEquals(unitNameRes, unitName, "unit_name--expect: " + unitName + ",actual: " + unitNameRes);

                String floorIdRes = listSingle.getString("floor_id");
                Assert.assertEquals(floorIdRes, floorId, "floor_id--expect: " + floorId + ",actual: " + floorIdRes);

                double xRes = listSingle.getJSONObject("position").getDouble("x");
                Assert.assertEquals(xRes, x, "x--expect: " + x + ", actual: " + xRes);

                double yRes = listSingle.getJSONObject("position").getDouble("y");
                Assert.assertEquals(yRes, y, "y--expect: " + y + ", actual: " + yRes);
            }
        }
    }

    private void checkUnitListRmPosi(String response, String unitCode, String unitName, String floorId) {
        JSONObject data = JSON.parseObject(response).getJSONObject("data");
        JSONArray list = data.getJSONArray("list");
        for (int i = 0; i < list.size(); i++) {
            JSONObject listSingle = list.getJSONObject(i);
            String unitCodeRes = listSingle.getString("unit_code");
            if (unitCode.equals(unitCodeRes)) {
                String unitNameRes = listSingle.getString("unit_name");
                Assert.assertEquals(unitNameRes, unitName, "unit_name--expect: " + unitName + ",actual: " + unitNameRes);

                String floorIdRes = listSingle.getString("floor_id");
                Assert.assertEquals(floorIdRes, floorId, "floor_id--expect: " + floorId + ",actual: " + floorIdRes);

                String posi = listSingle.getString("position");
                Assert.assertEquals(posi, null, "????????????????????????");
            }
        }
    }

    private void checkUnitList(String response, String unitCode, int height, int width, int depth) {

        JSONObject data = JSON.parseObject(response).getJSONObject("data");
        JSONArray list = data.getJSONArray("list");
        for (int i = 0; i < list.size(); i++) {
            JSONObject listSingle = list.getJSONObject(i);
            String unitCodeRes = listSingle.getString("unit_code");
            if (unitCode.equals(unitCodeRes)) {
                int heightRes = listSingle.getInteger("height");
                Assert.assertEquals(heightRes, height, "height--expect: " + height + ",actual: " + heightRes);

                int widthRes = listSingle.getInteger("width");
                Assert.assertEquals(widthRes, width, "width--expect: " + width + ",actual: " + widthRes);

                int depthRes = listSingle.getInteger("depth");
                Assert.assertEquals(depthRes, depth, "depth--expect: " + depth + ",actual: " + depthRes);
            }
        }
    }

    private void checkUnitListAUpdate3D(String response, String unitCode, int height, int width, int depth) {

        JSONObject data = JSON.parseObject(response).getJSONObject("data");
        JSONArray list = data.getJSONArray("list");
        for (int i = 0; i < list.size(); i++) {
            JSONObject listSingle = list.getJSONObject(i);
            String unitCodeRes = listSingle.getString("unit_code");
            if (unitCode.equals(unitCodeRes)) {
                int heightRes = listSingle.getInteger("height");
                Assert.assertEquals(heightRes, height, "height--expect: " + height + ",actual: " + heightRes);

                int widthRes = listSingle.getInteger("width");
                Assert.assertEquals(widthRes, width, "width--expect: " + width + ",actual: " + widthRes);

                int depthRes = listSingle.getInteger("depth");
                Assert.assertEquals(depthRes, depth, "depth--expect: " + depth + ",actual: " + depthRes);


            }

        }
    }

    private void checkUnitListABind(String response, String unitCode, String floorId) throws Exception {

        boolean isExist = false;
        JSONObject data = JSON.parseObject(response).getJSONObject("data");
        JSONArray list = data.getJSONArray("list");
        for (int i = 0; i < list.size(); i++) {
            JSONObject listSingle = list.getJSONObject(i);
            String unitCodeRes = listSingle.getString("unit_code");
            if (unitCode.equals(unitCodeRes)) {
                isExist = true;
                String flooorIdRes = listSingle.getString("floor_id");
                Assert.assertEquals(flooorIdRes, floorId, "floorId-- expect: " + floorId + ", actual: " + flooorIdRes);
            }

            if (!isExist) {
                throw new Exception("unitCode: " + unitCode + " ?????????");
            }
        }
    }

    private void checkUnitListAUnbind(String response, String unitCode) throws Exception {

        boolean isExist = false;
        JSONObject data = JSON.parseObject(response).getJSONObject("data");
        JSONArray list = data.getJSONArray("list");
        for (int i = 0; i < list.size(); i++) {
            JSONObject listSingle = list.getJSONObject(i);
            String unitCodeRes = listSingle.getString("unit_code");
            if (unitCode.equals(unitCodeRes)) {
                isExist = true;
                String flooorIdRes = listSingle.getString("floor_id");
                if (flooorIdRes != null) {
                    throw new Exception("????????????floorId?????????");
                }
            }

            if (isExist) {
                throw new Exception("????????????");
            }
        }
    }

    private void checkFloorList(String response, String id, String floorName, String floorMap, boolean isExist) throws Exception {
        JSONObject data = JSON.parseObject(response).getJSONObject("data");
        JSONArray list = data.getJSONArray("list");

        if (list == null || list.size() == 0) {
            throw new Exception("?????????????????????");
        }

        boolean isExistRes = false;

        for (int i = 0; i < list.size(); i++) {
            JSONObject listSingle = list.getJSONObject(i);
            String idRes = listSingle.getString("id");
            if (id.equals(idRes)) {
                isExistRes = true;
                String floorNameRes = listSingle.getString("floor_name");
                Assert.assertEquals(floorNameRes, floorName, "floor_name-- expect: " + floorName + ", actual " + floorNameRes);

                String floorMapRes = listSingle.getString("floor_map");
                if (floorMap == null) {
                    Assert.assertEquals(floorMapRes, floorMap, "floor_map---expect: " + floorMap + ", actual: " + floorMapRes);
                } else if (!(floorMapRes.contains(floorMap))) {
                    throw new Exception("checkFloorList---?????????floor_map?????????");
                }
            }
        }

        Assert.assertEquals(isExistRes, isExist, " isExist--expect: " + isExist + ", actual: " + isExistRes);
    }

    private void checkFloorDetail(String response, String id, String floorNum, String floorName, String floorMap) throws Exception {
        JSONObject data = JSON.parseObject(response).getJSONObject("data");
        String idRes = data.getString("id");
        if (id.equals(idRes)) {
            String floorNumRes = data.getString("floor_num");
            Assert.assertEquals(floorNumRes, floorNum, "floor_num--expect: " + floorNum + " actual: " + floorNumRes);

            String floorNameRes = data.getString("floor_name");
            Assert.assertEquals(floorNameRes, floorName, "floor_name--expect: " + floorName + " actual: " + floorNameRes);

            String floorMapRes = data.getString("floor_map");
            if (floorMap == null) {
                Assert.assertEquals(floorMapRes, floorMap, "floor_map---expect: " + floorMap + ", actual: " + floorMapRes);
            } else if (!(floorMapRes.contains(floorMap))) {
                throw new Exception("checkFloorList---?????????floor_map?????????");
            }
        }

    }

    private void checkUnitListPlate(String response, String plateCode, int width, double k0, double k1) throws Exception {

        boolean PlateCodeIsExist = false;
        boolean KZeroIsExist = false;
        JSONObject data = JSON.parseObject(response).getJSONObject("data");
        JSONArray list = data.getJSONArray("list");

        double k0Res = 0, k1Res = 0;
        int widthRes = 0;

        for (int i = 0; i < list.size(); i++) {
            JSONObject listSingle = list.getJSONObject(i);
            widthRes = listSingle.getInteger("width");
            JSONArray sensors = listSingle.getJSONArray("sensors");
            String plateCodeRes = listSingle.getString("plate_code");
            if (plateCode.equals(plateCodeRes)) {
                PlateCodeIsExist = true;
                for (int j = 0; j < sensors.size(); j++) {
                    JSONObject singleSensor = sensors.getJSONObject(j);
                    String sensorId = singleSensor.getString("sensor_id");
                    if ("0".equals(sensorId)) {
                        KZeroIsExist = true;
                        k0Res = singleSensor.getDouble("k");
                    } else if ("1".equals(sensorId)) {
                        KZeroIsExist = true;
                        k1Res = singleSensor.getDouble("k");
                    } else {
                        throw new Exception("????????????sensor_id???" + sensorId);
                    }
                }
            }

            if (!PlateCodeIsExist) {
                throw new Exception("plate_code: " + plateCode + " ?????????");
            } else {
                Assert.assertEquals(widthRes, width, "width--expect: " + width + ", actual: " + widthRes);
            }

            if (KZeroIsExist) {
                Assert.assertEquals(k0Res, k0, "sensor_id : 0 , k???--expect???" + k0 + ",actual: " + k0Res);
                Assert.assertEquals(k1Res, k1, "sensor_id : 1 , k???--expect???" + k1 + ",actual: " + k1Res);
            }
        }
    }

    private String sendRequestWithUrl(String url, String json, HashMap header) throws Exception {
        HttpExecutorUtil executor = new HttpExecutorUtil();
        executor.doPostJsonWithHeaders(url, json, header);
        return executor.getResponse();
    }

    private ApiResponse sendRequestWithGate(String router, String[] resource, String json) throws Exception {
        try {
            Credential credential = new Credential(AK, SK);
            // ??????request??????
            String requestId = UUID.randomUUID().toString();
            ApiRequest apiRequest = new ApiRequest.Builder()
                    .uid(UID)
                    .appId(APP_CODE)
                    .requestId(requestId)
                    .version(SdkConstant.API_VERSION)
                    .router(router)
                    .dataResource(resource)
                    .dataBizData(JSON.parseObject(json))
                    .build();

            // client ??????
            ApiClient apiClient = new ApiClient("http://dev.api.winsenseos.cn/retail/api/data/biz", credential);
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

    private void checkCode(String response, int expect, String message) {
        int code = JSON.parseObject(response).getInteger("code");
        Assert.assertEquals(code, expect, message + " expect:" + expect + ",actual:" + code);
    }

    void genAuth() {

        String json =
                "{\n" +
                        "  \"email\": \"liaoxiangru@winsense.ai\"," +
                        "  \"password\": \"e586aee0d9d9fdb16b9982adb74aeb60\"" +
                        "}";
        try {
            response = sendRequestWithUrl(genAuthURL, json, header);
            logger.info("\n");
            JSONObject data = JSON.parseObject(response).getJSONObject("data");
            authorization = data.getString("token");

            header.put("Authorization", authorization);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setBasicParaToDB(Case aCase, String caseName, String caseDesc, String ciCaseName) {
        aCase.setApplicationId(APP_ID);
        aCase.setConfigId(CONFIG_ID);
        aCase.setCaseName(caseName);
        aCase.setCaseDescription(caseDesc);
        aCase.setCiCmd(CI_CMD + ciCaseName);
        aCase.setQaOwner("?????????");
        aCase.setExpect("code==1000");
    }

    @BeforeSuite
    public void initial() throws Exception {
        genAuth();
        qaDbUtil.openConnection();
    }

    @AfterSuite
    public void clean() {
        qaDbUtil.closeConnection();
    }
}


