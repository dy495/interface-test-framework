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

public class shelvesMana {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private LogMine logMine = new LogMine(logger);
    private static String UID = "uid_87803c0c";
    private static String APP_CODE = "7485a90349a2";
    private static String AK = "8da9aeabd74198b1";
    private static String SK = "ec44b94f9b3cf4333c5d000781cb0289";

    private static long SHOP_ID = 640;
    private String genAuthURL = "http://39.106.253.190/administrator/login";
    String PLATE_CODE = "123";

    String response;
    String authorization;
    HashMap<String, String> header = new HashMap();
    ApiResponse apiResponse;

    private String failReason = "";
    private QADbUtil qaDbUtil = new QADbUtil();
    private int APP_ID = ChecklistDbInfo.DB_APP_ID_SHELF_SERVICE;
    private int CONFIG_ID = ChecklistDbInfo.DB_SERVICE_ID_SHELF_SERVICE;
    private String CI_CMD = "curl -X POST http://shelf:shelf@192.168.50.2:8080/job/commodity-management/buildWithParameters?case_name=";

    private String createFloor(String floorNum, String floorName, Case acase, int step) throws Exception {
        logger.info("\n");
        logger.info("------------create floor!-----------------------");

        String url = "http://39.106.253.190/admin/commodity/floor/create";

        //组织参数
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
//        sendResAndReqIdToDb(response, acase, step);
        return response;
    }

    private String listFloor(String floorNum, Case acase, int step) throws Exception {
        logger.info("\n");
        logger.info("------------list floor!-----------------------");

        String url = "http://39.106.253.190/admin/commodity/floor/list";

        //组织参数
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
//        sendResAndReqIdToDb(response, acase, step);
        return response;
    }

    private String updateFloor(String id, String floorName, String floorMap, Case acase, int step) throws Exception {
        logger.info("\n");
        logger.info("------------update floor!-----------------------");

        String url = "http://39.106.253.190/admin/commodity/floor/update";

        //组织参数
        StringBuffer jsonBF= new StringBuffer();
        jsonBF.append("{\n").
                append("    \"id\":\"").append(id).append("\",\n").
                append("    \"subject_id\":\"").append(SHOP_ID).append("\",\n").
                append( "    \"floor_name\":\"").append(floorName).append("\",\n").
                append("    \"floor_map\":\"").append(floorMap).append("\"\n").
                append("}\n");

        try {
            response = sendRequestWithUrl(url, jsonBF.toString(), header);
        } catch (Exception e) {
            failReason = e.toString();
            throw e;
        }
//        sendResAndReqIdToDb(response, acase, step);
        return response;
    }

    private String deleteFloor(String id,Case acase, int step) throws Exception {
        logger.info("\n");
        logger.info("------------delete floor!-----------------------");

        String url = "http://39.106.253.190/admin/commodity/floor/delete";

        //组织参数
        StringBuffer jsonBF = new StringBuffer();
        jsonBF.append("{\n").
                append("    \"subject_id\":\"").append(SHOP_ID).append( "\",\n").
                append("    \"id\":\"").append(id).append("\"\n").
                append("}\n");

        try {
            response = sendRequestWithUrl(url, jsonBF.toString(), header);
        } catch (Exception e) {
            failReason = e.toString();
            throw e;
        }
//        sendResAndReqIdToDb(response, acase, step);
        return response;
    }

    private String deleteFloor(String id) throws Exception {
        logger.info("\n");
        logger.info("------------delete floor!-----------------------");

        String url = "http://39.106.253.190/admin/commodity/floor/delete";

        //组织参数
        String json =
                "{\n" +
                        "    \"subject_id\":\""+ SHOP_ID + "\",\n" +
                        "    \"id\":\""+ id + "\"\n" +
                        "}\n";
        try {
            response = sendRequestWithUrl(url, json, header);
        } catch (Exception e) {
            failReason = e.toString();
            throw e;
        }
//        sendResAndReqIdToDb(response, acase, step);
        return response;
    }

    private String detailFloor(String id, String floorName, String floorMap, Case acase, int step) throws Exception {
        logger.info("\n");
        logger.info("------------detail floor!-----------------------");

        String url = "http://39.106.253.190/admin/commodity/floor/detail";

        //组织参数
        String json =
                "{\n" +
                        "    \"id\":\""+ id + "\"\n" +
                        "}\n";
        try {
            response = sendRequestWithUrl(url, json, header);
        } catch (Exception e) {
            failReason = e.toString();
            throw e;
        }
//        sendResAndReqIdToDb(response, acase, step);
        return response;
    }

    @Test
    public void testIni() throws Exception {
        iniUnit("QA-TEST-1【勿动】","912",new Case(),1);
    }

    private String iniUnit(String unitCode, String plateCode, Case acase, int step) throws Exception {
        logger.info("\n");
        logger.info("------------init unit!-----------------------");

        String router = "/commodity/external/INIT_UNIT/v1.0";

        //组织参数
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
            apiResponse = sendRequestWithGate(router,null,json);
        } catch (Exception e) {
            failReason = e.toString();
            throw e;
        }
//        sendResAndReqIdToDb(response, acase, step);
        return response;
    }

    //-------------------------0.1 创建unit---------------------------------
    private String createUnit(String unitCode, int height, int width, int depth, Case acase, int step) throws Exception {
        logger.info("\n");
        logger.info("------------create unit!-----------------------");

        String url = "http://39.106.253.190/admin/commodity/unit/create";

        //组织参数
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
//        sendResAndReqIdToDb(response, acase, step);
        return response;
    }

    //-------------------------查询单元列表--unit---------------------------------
    private String listUnit(long subjectId, Case acase, int step) throws Exception {
        logger.info("\n");
        logger.info("------------list unit!-----------------------");

        String url = "http://39.106.253.190/admin/commodity/unit/list";

        //组织参数
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
//        sendResAndReqIdToDb(response, acase, step);
        return response;
    }

    private String updateUnitWithoutPosi(String unitCode, String unitName, int height, int width, int depth, String floorId, Case acase, int step) throws Exception {
        logger.info("\n");
        logger.info("------------update unit!-----------------------");

        String url = "http://39.106.253.190/admin/commodity/unit/update";

        //组织参数
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
//        sendResAndReqIdToDb(response, acase, step);
        return response;
    }

    private String updateUnitWithPosi(String unitCode, String unitName, String floorId, double x, double y,Case acase, int step) throws Exception {
        logger.info("\n");
        logger.info("------------update unit!-----------------------");

        String url = "http://39.106.253.190/admin/commodity/unit/update";

        //组织参数
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
//        sendResAndReqIdToDb(response, acase, step);
        return response;
    }

    private String updateUnitRmPosi(String unitCode, String unitName, String floorId, String posi,Case acase, int step) throws Exception {
        logger.info("\n");
        logger.info("------------update unit!-----------------------");

        String url = "http://39.106.253.190/admin/commodity/unit/update";

        //组织参数
        String json =
                "{" +
                        "    \"unit_code\": \"" + unitCode + "\"," +
                        "    \"unit_name\": \"" + unitName + "\"," +
                        "    \"floor_id\": \"" + floorId + "\"," +
                        "    \"position\":\"" +posi + "\"" +
                        "}";
        try {
            response = sendRequestWithUrl(url, json, header);
        } catch (Exception e) {
            failReason = e.toString();
            throw e;
        }
//        sendResAndReqIdToDb(response, acase, step);
        return response;
    }

    private String bindUnit(String unitCode, String unitName, String floorId, Case acase, int step) throws Exception {
        logger.info("\n");
        logger.info("------------bind unit!-----------------------");

        String url = "http://39.106.253.190/admin/commodity/unit/binding";

        //组织参数
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
//        sendResAndReqIdToDb(response, acase, step);
        return response;
    }

    private String unbindUnit(String unitCode, Case acase, int step) throws Exception {
        logger.info("\n");
        logger.info("------------unbind unit!-----------------------");

        String url = "http://39.106.253.190/admin/commodity/unit/unbinding";

        //组织参数
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
//        sendResAndReqIdToDb(response, acase, step);
        return response;
    }

    private void deleteUnit(String unitCode,String shelvesCode) throws Exception {
        logger.info("\n");
        logger.info("------------delete unit!-----------------------");

        String url = "http://dev.app.winsenseos.com/operation/app/COMMODITY/3576";

        HashMap<String,String> openPlatformHeader = new HashMap<>();
        openPlatformHeader.put("Authorization", genOpenPlatformAuth());

        //组织参数
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
        String url = "http://dev.sso.winsenseos.com/sso/login";

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

        //组织参数
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
//        sendResAndReqIdToDb(response, acase, step);
        return response;
    }

    private String modifyConfig(String plateCode, int width, double k0, double k1, Case acase, int step) throws Exception {
        logger.info("\n");
        logger.info("------------modify plate!-----------------------");

        String url = "http://39.106.253.190/admin/commodity/plate/config/modify";

        //组织参数
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
//        sendResAndReqIdToDb(response, acase, step);
        return response;
    }

    private String listPlate(String plateCode, Case acase, int step) throws Exception {
        logger.info("\n");
        logger.info("------------list unit!-----------------------");

        String url = "http://39.106.253.190/admin/commodity/plate/list";

        //组织参数
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
//        sendResAndReqIdToDb(response, acase, step);
        return response;
    }

    public String layoutPicUpload() throws IOException {

        String url = "http://39.106.253.190/admin/data/layout/layoutPicUpload";

        String filePath = "src\\main\\java\\com\\haisheng\\framework\\testng\\operationcenter\\experimentLayout";
        filePath = filePath.replace("\\",File.separator);
        File file = new File(filePath);

        OkHttpClient client = new OkHttpClient();

        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);

//        builder.addFormDataPart("file_path", "console/layout/");

        builder.addFormDataPart("layout_pic", file.getName(), RequestBody.create
                (MediaType.parse("application/octet-stream"), file));

        //构建
        MultipartBody multipartBody = builder.build();

        //创建Request
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", authorization)
                .post(multipartBody)
                .build();

        Response response = client.newCall(request).execute();

        return response.body().string();

    }

//    -------------------------------(1)----------------------------------------

    @Test(priority = 1)
    public void testFloorsCreate() throws Exception {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName;
        String caseDesc = "测试创建楼层是否成功";
        logger.info(caseDesc + "--------------------");

        Case acase = new Case();
        int step = 0;

        String response;

        String floorNameOld = caseName + "-old";

        String floorNum = "1";

        String floorMapOld = null;

        String id = "";

        try {

//            1、创建楼层
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = createFloor(floorNum,floorNameOld, acase,step);
            checkCode(response, StatusCode.SUCCESS,"create floor ");

            id = JSON.parseObject(response).getJSONObject("data").getString("floor_id");

//            2、楼层详情
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = detailFloor(id,floorNameOld,floorMapOld,acase,step);
            checkFloorDetail(response, id, floorNum,floorNameOld,floorMapOld);

//            3、楼层列表
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = listFloor(floorNum,acase,step);
            checkFloorList(response, id, floorNameOld,floorMapOld,true);

        }catch (AssertionError e){
            failReason+=e.getMessage();
            Assert.fail(failReason);
            e.printStackTrace();
        }
        catch (Exception e) {
            failReason+=e.getMessage();
            Assert.fail(failReason);
            e.printStackTrace();
        }finally {
            deleteFloor(id);
        }
    }

    //--------------------------------------（2）----------------------------------------
    @Test(priority = 1)
    public void testFloorsUpdate() throws Exception {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName;
        String caseDesc = "测试更新楼层是否成功";
        logger.info(caseDesc + "--------------------");

        Case acase = new Case();
        int step = 0;

        String response;

        String floorNameOld = caseName + "-old";
        String floorNameNew = caseName + "-new";

        String floorNum = "1";

        String floorMapOld = null;
        String floorMapNew = "";

        String id = "";


        try {

//            1、创建楼层
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = createFloor(floorNum,floorNameOld, acase,step);
            checkCode(response, StatusCode.SUCCESS,"create floor ");

            id = JSON.parseObject(response).getJSONObject("data").getString("floor_id");

            response = layoutPicUpload();
            floorMapNew = JSON.parseObject(response).getJSONObject("data").getString("url");

//            2、更新楼层
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = updateFloor(id,floorNameNew,floorMapNew,acase,step);
            checkCode(response,StatusCode.SUCCESS,"updateFloor--");

//            3、楼层详情
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = detailFloor(id,floorNameOld,floorMapOld,acase,step);
            checkFloorDetail(response, id, floorNum,floorNameNew,floorMapNew);

//            4、楼层列表
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = listFloor(floorNum,acase,step);
            checkFloorList(response, id, floorNameNew,floorMapNew,true);

//            5、更新楼层
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = updateFloor(id,floorNameNew,"",acase,step);
            checkCode(response,StatusCode.SUCCESS,"updateFloor--");

//            6、楼层列表
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = listFloor(floorNum,acase,step);
            checkFloorList(response, id, floorNameNew,null,true);

        }catch (AssertionError e){
            failReason+=e.getMessage();
            Assert.fail(failReason);
            e.printStackTrace();
        }
        catch (Exception e) {
            failReason+=e.getMessage();
            Assert.fail(failReason);
            e.printStackTrace();
        }finally {
//            deleteFloor(id);
        }
    }

//    --------------------------------------(3)----删除楼层------------------------------------------

    @Test(priority = 1)
    public void testFloorsDelete() throws Exception {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName;
        String caseDesc = "测试删除楼层是否成功";
        logger.info(caseDesc + "--------------------");

        Case acase = new Case();
        int step = 0;

        String response;

        String floorNameOld = caseName + "-old";
        String floorNameNew = caseName + "-new";

        String floorNum = "1";

        String floorMapNew = "";

        String id = "";


        try {

//            1、创建楼层
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = createFloor(floorNum,floorNameOld, acase,step);
            checkCode(response, StatusCode.SUCCESS,"create floor ");

            id = JSON.parseObject(response).getJSONObject("data").getString("floor_id");

//            2、删除楼层
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = deleteFloor(id);
            checkCode(response,StatusCode.SUCCESS,"deleteFloor--");

//            3、楼层列表
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = listFloor(floorNum,acase,step);
            checkFloorList(response, id, floorNameNew,floorMapNew,false);

        }catch (AssertionError e){
            failReason+=e.getMessage();
            Assert.fail(failReason);
            e.printStackTrace();
        }
        catch (Exception e) {
            failReason+=e.getMessage();
            Assert.fail(failReason);
            e.printStackTrace();
        }finally {
            deleteFloor(id);
        }
    }

//    -------------------------------------------------(4)测试创建单元是否成功----------------------------------------

    @Test(priority = 1)
    public void testUnitCreate() throws Exception {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName;
        String caseDesc = "测试创建单元是否成功";
        logger.info(caseDesc + "--------------------");

        Case acase = new Case();
        int step = 0;

        String response;

        String unitCode = caseName;
        String shelvesCode = unitCode;
        int height = 140;
        int width = 100;
        int depth = 40;

        try {
            deleteUnit(unitCode,shelvesCode);
//            1、创建单元
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = createUnit(unitCode, height, width, depth, acase,step);
            checkCode(response, StatusCode.SUCCESS,"createUnit");

//            2、单元列表
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = listUnit(SHOP_ID,acase,step);
            checkCode(response, StatusCode.SUCCESS,"listUnit");
            checkUnitList(response,unitCode,height,width,depth);

        }catch (AssertionError e){
            failReason+=e.getMessage();
            Assert.fail(failReason);
            e.printStackTrace();
        } catch (Exception e) {
            failReason+=e.getMessage();
            Assert.fail(failReason);
            e.printStackTrace();
        }finally {
            deleteUnit(unitCode,shelvesCode);
        }
    }

//    ----------------------------------------（5）测试更新单元的长宽高是否成功----------------------------------

    @Test(priority = 1)
    public void testUnitUpdate3D() throws Exception {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName;
        String caseDesc = "测试更新单元的长宽高是否成功";
        logger.info(caseDesc + "--------------------");

        Case acase = new Case();
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
            deleteUnit(unitCode,shelvesCode);
//            1、创建单元
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = createUnit(unitCode, heightOld, widthOld, depthOld, acase,step);
            checkCode(response, StatusCode.SUCCESS,"createUnit");

//            2、创建楼层
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = createFloor("1",floorName,acase,step);
            checkCode(response, StatusCode.SUCCESS,"create Floor");

            floorId = JSON.parseObject(response).getJSONObject("data").getString("floor_id");

//            3、更新单元
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = updateUnitWithoutPosi(unitCode,unitName,heightNew,widthNew,depthNew,floorId,acase,step);
            checkCode(response, StatusCode.SUCCESS,"update Floor");

//            4、单元列表
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = listUnit(SHOP_ID,acase,step);
            checkCode(response, StatusCode.SUCCESS,"listUnit");
            checkUnitListAUpdate3D(response,unitCode,heightNew,widthNew,depthNew);

        }catch (AssertionError e){
            failReason+=e.getMessage();
            Assert.fail(failReason);
            e.printStackTrace();
        } catch (Exception e) {
            failReason+=e.getMessage();
            Assert.fail(failReason);
            e.printStackTrace();
        }finally {
            deleteUnit(unitCode,shelvesCode);
            deleteFloor(floorId);
        }
    }

//    -----------------------（6）测试更新单元时先加入位置信息，再删除位置信息------------------------------

    @Test(priority = 1)
    public void testUnitUpdatePosi() throws Exception {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName;
        String caseDesc = "测试更新单元时先加入位置信息，再删除位置信息";
        logger.info(caseDesc + "--------------------");

        Case acase = new Case();
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

        String floorId="";

        try {
            deleteUnit(unitCode,shelvesCode);
//            1、创建单元
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = createUnit(unitCode, height, width, depth, acase,step);
            checkCode(response, StatusCode.SUCCESS,"createUnit");

//            2、创建楼层
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = createFloor("1",floorName,acase,step);
            checkCode(response, StatusCode.SUCCESS,"create Floor");

            floorId = JSON.parseObject(response).getJSONObject("data").getString("floor_id");

            response = layoutPicUpload();
            String floorMap = JSON.parseObject(response).getJSONObject("data").getString("url");

//            3、更新楼层
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = updateFloor(floorId,floorName,floorMap,acase,step);
            checkCode(response,StatusCode.SUCCESS,"updateFloor--");

//            4、绑定单元
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = bindUnit(unitCode,unitName, floorId, acase,step);
            checkCode(response,StatusCode.SUCCESS,"bindUnit--");

//            5、初始化单元
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = iniUnit(unitCode, PLATE_CODE,acase,step);
            checkCode(response,StatusCode.SUCCESS,"iniUnit--");

//            6、更新单元
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = updateUnitWithPosi(unitCode,unitName,floorId,x,y,acase,step);
            checkCode(response, StatusCode.SUCCESS,"update Floor");

//            7、单元列表
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = listUnit(SHOP_ID,acase,step);
            checkCode(response, StatusCode.SUCCESS,"listUnit");
            checkUnitListAUpdatePosi(response,unitCode,unitName,floorId,x,y);

//            8、更新单元（去掉位置信息）
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = updateUnitRmPosi(unitCode,unitName,floorId,"",acase,step);
            checkCode(response, StatusCode.SUCCESS,"update Floor");

//            9、单元列表
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = listUnit(SHOP_ID,acase,step);
            checkCode(response, StatusCode.SUCCESS,"listUnit");
            checkUnitListRmPosi(response,unitCode,unitName,floorId);

        }catch (AssertionError e){
            failReason+=e.getMessage();
            Assert.fail(failReason);
            e.printStackTrace();
        } catch (Exception e) {
            failReason+=e.getMessage();
            Assert.fail(failReason);
            e.printStackTrace();
        }finally {
            deleteUnit(unitCode,shelvesCode);
            deleteFloor(floorId);
        }
    }

//---------------------------（7）-------测试更新单元时，先不加入位置信息，再加入位置信息-------------------------------------
    @Test(priority = 1)
    public void testUnitUpdatePosi1() throws Exception {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName;
        String caseDesc = "测试更新单元时先不加入位置信息，再加入位置信息";
        logger.info(caseDesc + "--------------------");

        Case acase = new Case();
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

        String floorId="";

        try {
            deleteUnit(unitCode,shelvesCode);

//            1、创建单元
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = createUnit(unitCode, height, width, depth, acase,step);
            checkCode(response, StatusCode.SUCCESS,"createUnit");

//            2、创建楼层
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = createFloor("1",floorName,acase,step);
            checkCode(response, StatusCode.SUCCESS,"create Floor");

            floorId = JSON.parseObject(response).getJSONObject("data").getString("floor_id");

            response = layoutPicUpload();
            String floorMap = JSON.parseObject(response).getJSONObject("data").getString("url");

//            3、绑定单元
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = bindUnit(unitCode,unitName, floorId, acase,step);
            checkCode(response,StatusCode.SUCCESS,"bindUnit--");

//            4、初始化单元
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = iniUnit(unitCode, PLATE_CODE,acase,step);
            checkCode(response,StatusCode.SUCCESS,"iniUnit--");

//            5、更新楼层
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = updateFloor(floorId,floorName,floorMap,acase,step);
            checkCode(response,StatusCode.SUCCESS,"updateFloor--");

//            6、更新单元（不增加位置信息）
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = updateUnitRmPosi(unitCode,unitName,floorId,"", acase,step);
            checkCode(response, StatusCode.SUCCESS,"update unit");

//            7、单元列表
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = listUnit(SHOP_ID,acase,step);
            checkCode(response, StatusCode.SUCCESS,"listUnit");
            checkUnitListRmPosi(response,unitCode,unitName,floorId);

//            8、更新单元，添加位置信息
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = updateUnitWithPosi(unitCode,unitName,floorId,x,y,acase,step);
            checkCode(response, StatusCode.SUCCESS,"update Floor");

//            9、单元列表
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = listUnit(SHOP_ID,acase,step);
            checkCode(response, StatusCode.SUCCESS,"listUnit");
            checkUnitListAUpdatePosi(response,unitCode,unitName,floorId,x,y);

        }catch (AssertionError e){
            failReason+=e.getMessage();
            Assert.fail(failReason);
            e.printStackTrace();
        } catch (Exception e) {
            failReason+=e.getMessage();
            Assert.fail(failReason);
            e.printStackTrace();
        }finally {
            deleteUnit(unitCode,shelvesCode);
            deleteFloor(floorId);
        }
    }

//    ----------------------（8）---------------------测试新建时先增加位置信息，然后更新位置信息是否成功-----------------
    @Test(priority = 1)
    public void testUnitUpdatePosi2() throws Exception {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName;
        String caseDesc = "测试新建时先增加位置信息，然后更新位置信息是否成功";
        logger.info(caseDesc + "--------------------");

        Case acase = new Case();
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

        String floorId="";

        try {
            deleteUnit(unitCode,shelvesCode);

//            1、创建单元
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = createUnit(unitCode, height, width, depth, acase,step);
            checkCode(response, StatusCode.SUCCESS,"createUnit");

//            2、创建楼层
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = createFloor("1",floorName,acase,step);
            checkCode(response, StatusCode.SUCCESS,"create Floor");

            floorId = JSON.parseObject(response).getJSONObject("data").getString("floor_id");

            response = layoutPicUpload();
            String floorMap = JSON.parseObject(response).getJSONObject("data").getString("url");

//            3、绑定单元
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = bindUnit(unitCode,unitName, floorId, acase,step);
            checkCode(response,StatusCode.SUCCESS,"bindUnit--");

//            4、初始化单元
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = iniUnit(unitCode, PLATE_CODE,acase,step);
            checkCode(response,StatusCode.SUCCESS,"iniUnit--");

//            5、更新楼层
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = updateFloor(floorId,floorName,floorMap,acase,step);
            checkCode(response,StatusCode.SUCCESS,"updateFloor--");

//            6、更新单元（增加位置信息）
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = updateUnitWithPosi(unitCode,unitName,floorId,xOld,yOld, acase,step);
            checkCode(response, StatusCode.SUCCESS,"update unit");

//            7、单元列表
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = listUnit(SHOP_ID,acase,step);
            checkCode(response, StatusCode.SUCCESS,"listUnit");
            checkUnitListAUpdatePosi(response,unitCode,unitName,floorId,xOld,yOld);

//            8、更新单元，添加位置信息
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = updateUnitWithPosi(unitCode,unitName,floorId,xNew,yNew,acase,step);
            checkCode(response, StatusCode.SUCCESS,"update Floor");

//            9、单元列表
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = listUnit(SHOP_ID,acase,step);
            checkCode(response, StatusCode.SUCCESS,"listUnit");
            checkUnitListAUpdatePosi(response,unitCode,unitName,floorId,xNew,yNew);

        }catch (AssertionError e){
            failReason+=e.getMessage();
            Assert.fail(failReason);
            e.printStackTrace();
        } catch (Exception e) {
            failReason+=e.getMessage();
            Assert.fail(failReason);
            e.printStackTrace();
        }finally {
            deleteUnit(unitCode,shelvesCode);
            deleteFloor(floorId);
        }
    }

//---------------------------（9）-----测试绑定单元是否成功------------------------------------------------
    @Test(priority = 1)
    public void testUnitBind() throws Exception {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName;
        String caseDesc = "测试绑定单元是否成功";
        logger.info(caseDesc + "--------------------");

        Case acase = new Case();
        int step = 0;

        String response;

        String unitCode = caseName;
        String shelvesCode = unitCode;
        String floorName = caseName;
        String unitName = "unitBind";
        int height = 140;
        int width = 100;
        int depth = 40;

        String floorId="";

        try {
            deleteUnit(unitCode,shelvesCode);

//            1、创建单元
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = createUnit(unitCode, height, width, depth, acase,step);
            checkCode(response, StatusCode.SUCCESS,"createUnit");

//            2、创建楼层
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = createFloor("1",floorName,acase,step);
            checkCode(response, StatusCode.SUCCESS,"create Floor");

            floorId = JSON.parseObject(response).getJSONObject("data").getString("floor_id");

//            2、绑定单元
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = bindUnit(unitCode,unitName, floorId, acase,step);
            checkCode(response,StatusCode.SUCCESS,"bindUnit--");

//            3、单元列表
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = listUnit(SHOP_ID,acase,step);
            checkCode(response, StatusCode.SUCCESS,"listUnit");
            checkUnitListABind(response,unitCode,floorId);

        }catch (AssertionError e){
            failReason+=e.getMessage();
            Assert.fail(failReason);
            e.printStackTrace();
        } catch (Exception e) {
            failReason+=e.getMessage();
            Assert.fail(failReason);
            e.printStackTrace();
        }finally {
            deleteUnit(unitCode,shelvesCode);
            deleteFloor(floorId);
        }
    }

    //---------------------------（10）-----测试解绑单元是否成功------------------------------------------------
    @Test(priority = 1)
    public void testUnitUnbind() throws Exception {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName;
        String caseDesc = "测试解绑单元是否成功";
        logger.info(caseDesc + "--------------------");

        Case acase = new Case();
        int step = 0;

        String response;

        String unitCode = caseName;
        String shelvesCode = unitCode;
        String floorName = caseName;
        String unitName = "unbindUnit";
        int height = 140;
        int width = 100;
        int depth = 40;

        String floorId="";

        try {
            deleteUnit(unitCode,shelvesCode);

//            1、创建单元
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = createUnit(unitCode, height, width, depth, acase,step);
            checkCode(response, StatusCode.SUCCESS,"createUnit");

//            2、创建楼层
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = createFloor("1",floorName,acase,step);
            checkCode(response, StatusCode.SUCCESS,"create Floor");

            floorId = JSON.parseObject(response).getJSONObject("data").getString("floor_id");

//            3、绑定单元
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = bindUnit(unitCode,unitName, floorId, acase,step);
            checkCode(response,StatusCode.SUCCESS,"bindUnit--");

//            4、解绑单元
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = unbindUnit(unitCode,acase,step);
            checkCode(response, StatusCode.SUCCESS,"unbindUnit");

//            5、单元列表
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = listUnit(SHOP_ID,acase,step);
            checkCode(response, StatusCode.SUCCESS,"listUnit");
            checkUnitListAUnbind(response,unitCode);

        }catch (AssertionError e){
            failReason+=e.getMessage();
            Assert.fail(failReason);
            e.printStackTrace();
        } catch (Exception e) {
            failReason+=e.getMessage();
            Assert.fail(failReason);
            e.printStackTrace();
        }finally {
            deleteUnit(unitCode,shelvesCode);
            deleteFloor(floorId);
        }
    }

//------------------------------------测试解绑后重新绑定---------------------------------------
    @Test(priority = 1)
    public void testUnitRebind() throws Exception {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName;
        String caseDesc = "测试解绑后重新绑定";
        logger.info(caseDesc + "--------------------");

        Case acase = new Case();
        int step = 0;

        String response;

        String unitCode = caseName;
        String shelvesCode = unitCode;
        String floorName = caseName;
        String unitName = "rebindUnit";
        int height = 140;
        int width = 100;
        int depth = 40;

        String floorId="";

        try {
            deleteUnit(unitCode,shelvesCode);

//            1、创建单元
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = createUnit(unitCode, height, width, depth, acase,step);
            checkCode(response, StatusCode.SUCCESS,"createUnit");

//            2、创建楼层
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = createFloor("1",floorName,acase,step);
            checkCode(response, StatusCode.SUCCESS,"create Floor");

            floorId = JSON.parseObject(response).getJSONObject("data").getString("floor_id");

//            3、绑定单元
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = bindUnit(unitCode,unitName, floorId, acase,step);
            checkCode(response,StatusCode.SUCCESS,"bindUnit--");

//            4、解绑单元
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = unbindUnit(unitCode,acase,step);
            checkCode(response, StatusCode.SUCCESS,"unbindUnit");

//            5、绑定单元
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = bindUnit(unitCode,unitName, floorId, acase,step);
            checkCode(response,StatusCode.SUCCESS,"bindUnit--");

//            6、单元列表
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = listUnit(SHOP_ID,acase,step);
            checkCode(response, StatusCode.SUCCESS,"listUnit");
            checkUnitListABind(response,unitCode,floorId);

        }catch (AssertionError e){
            failReason+=e.getMessage();
            Assert.fail(failReason);
            e.printStackTrace();
        } catch (Exception e) {
            failReason+=e.getMessage();
            Assert.fail(failReason);
            e.printStackTrace();
        }finally {
            deleteUnit(unitCode,shelvesCode);
            deleteFloor(floorId);
        }
    }

//    -----------------------------测试感压板创建是否成功--------------------------------------------------------
    @Test(priority = 1)
    public void testcreatePlate() throws Exception {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName;
        String caseDesc = "测试感压板创建是否成功";
        logger.info(caseDesc + "--------------------");

        Case acase = new Case();
        int step = 0;

        String response;

        String plateCode = String.valueOf(System.currentTimeMillis());
        int width = 4;
        double k0 = 0.111D;
        double k1 = -0.112D;

        try {
//            1、创建感压板
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = createPlate(plateCode,width,k0,k1,acase,step);
            checkCode(response, StatusCode.SUCCESS,"createPlate");

//            3、感压板列表
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = listPlate(plateCode,acase,step);
            //zero创建时会默认为0，无论设置成什么值,
            checkUnitListPlate(response,plateCode,width,k0,k1);
        }catch (AssertionError e){
            failReason+=e.getMessage();
            Assert.fail(failReason);
            e.printStackTrace();
        } catch (Exception e) {
            failReason+=e.getMessage();
            Assert.fail(failReason);
            e.printStackTrace();
        }finally {

        }
    }

//---------------------------------------------------测试更改感压板配置--------------------------------
    @Test(priority = 1)
    public void testModifyPlate() throws Exception {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName;
        String caseDesc = "测试更改感压板配置";
        logger.info(caseDesc + "--------------------");

        Case acase = new Case();
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
//            1、创建感压板
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = createPlate(plateCode,width,k0,k1,acase,step);
            checkCode(response, StatusCode.SUCCESS,"createPlate");

//            2、更改感压板配置
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = modifyConfig(plateCode,widthNew,k0New,k1New,acase,step);
            checkCode(response, StatusCode.SUCCESS,"modifyPlate");

//            3、感压板列表
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            response = listPlate(plateCode,acase,step);
            //zero创建时会默认为0，无论设置成什么值,
            checkUnitListPlate(response,plateCode,widthNew,k0New,k1New);
        }catch (AssertionError e){
            failReason+=e.getMessage();
            Assert.fail(failReason);
            e.printStackTrace();
        } catch (Exception e) {
            failReason+=e.getMessage();
            Assert.fail(failReason);
            e.printStackTrace();
        }finally {

        }
    }

    private void checkUnitListAUpdatePosi(String response, String unitCode, String unitName, String floorId,double x,double y) {
        JSONObject data = JSON.parseObject(response).getJSONObject("data");
        JSONArray list = data.getJSONArray("list");
        for (int i = 0; i < list.size(); i++) {
            JSONObject listSingle = list.getJSONObject(i);
            String unitCodeRes = listSingle.getString("unit_code");
            if (unitCode.equals(unitCodeRes)){
                String unitNameRes = listSingle.getString("unit_name");
                Assert.assertEquals(unitNameRes,unitName,"unit_name--expect: " + unitName + ",actual: " + unitNameRes);

                String floorIdRes = listSingle.getString("floor_id");
                Assert.assertEquals(floorIdRes,floorId,"floor_id--expect: " + floorId + ",actual: " + floorIdRes);

                double xRes = listSingle.getJSONObject("position").getDouble("x");
                Assert.assertEquals(xRes,x,"x--expect: " + x + ", actual: " + xRes);

                double yRes = listSingle.getJSONObject("position").getDouble("y");
                Assert.assertEquals(yRes,y,"y--expect: " + y + ", actual: " + yRes);
            }
        }
    }

    private void checkUnitListRmPosi(String response, String unitCode, String unitName, String floorId) {
        JSONObject data = JSON.parseObject(response).getJSONObject("data");
        JSONArray list = data.getJSONArray("list");
        for (int i = 0; i < list.size(); i++) {
            JSONObject listSingle = list.getJSONObject(i);
            String unitCodeRes = listSingle.getString("unit_code");
            if (unitCode.equals(unitCodeRes)){
                String unitNameRes = listSingle.getString("unit_name");
                Assert.assertEquals(unitNameRes,unitName,"unit_name--expect: " + unitName + ",actual: " + unitNameRes);

                String floorIdRes = listSingle.getString("floor_id");
                Assert.assertEquals(floorIdRes,floorId,"floor_id--expect: " + floorId + ",actual: " + floorIdRes);

                String posi = listSingle.getString("position");
                Assert.assertEquals(posi,null,"没有清除位置信息");
            }
        }
    }

    private void checkUnitList(String response, String unitCode, int height, int width, int depth) {

        JSONObject data = JSON.parseObject(response).getJSONObject("data");
        JSONArray list = data.getJSONArray("list");
        for (int i = 0; i < list.size(); i++) {
            JSONObject listSingle = list.getJSONObject(i);
            String unitCodeRes = listSingle.getString("unit_code");
            if (unitCode.equals(unitCodeRes)){
                int heightRes = listSingle.getInteger("height");
                Assert.assertEquals(heightRes,height,"height--expect: " + height + ",actual: " + heightRes);

                int widthRes = listSingle.getInteger("width");
                Assert.assertEquals(widthRes,width,"width--expect: " + width + ",actual: " + widthRes);

                int depthRes = listSingle.getInteger("depth");
                Assert.assertEquals(depthRes,depth,"depth--expect: " + depth + ",actual: " + depthRes);
            }
        }
    }

    private void checkUnitListAUpdate3D(String response, String unitCode, int height, int width, int depth) {

        JSONObject data = JSON.parseObject(response).getJSONObject("data");
        JSONArray list = data.getJSONArray("list");
        for (int i = 0; i < list.size(); i++) {
            JSONObject listSingle = list.getJSONObject(i);
            String unitCodeRes = listSingle.getString("unit_code");
            if (unitCode.equals(unitCodeRes)){
                int heightRes = listSingle.getInteger("height");
                Assert.assertEquals(heightRes,height,"height--expect: " + height + ",actual: " + heightRes);

                int widthRes = listSingle.getInteger("width");
                Assert.assertEquals(widthRes,width,"width--expect: " + width + ",actual: " + widthRes);

                int depthRes = listSingle.getInteger("depth");
                Assert.assertEquals(depthRes,depth,"depth--expect: " + depth + ",actual: " + depthRes);


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
            if (unitCode.equals(unitCodeRes)){
                isExist = true;
                String flooorIdRes = listSingle.getString("floor_id");
                Assert.assertEquals(flooorIdRes,floorId,"floorId-- expect: " + floorId + ", actual: " + flooorIdRes);
            }

            if (!isExist){
                throw new Exception("unitCode: " + unitCode + " 不存在");
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
            if (unitCode.equals(unitCodeRes)){
                isExist = true;
                String flooorIdRes = listSingle.getString("floor_id");
                if (flooorIdRes!=null){
                    throw new Exception("解绑后，floorId不为空" );
                }
            }

            if (isExist){
                throw new Exception("解绑失败");
            }
        }
    }

    private void checkFloorList(String response,String id, String floorName, String floorMap,boolean isExist) throws Exception{
        JSONObject data = JSON.parseObject(response).getJSONObject("data");
        JSONArray list = data.getJSONArray("list");

        if (list == null||list.size()==0){
            throw new Exception("楼层列表为空！");
        }

        boolean isExistRes = false;

        for (int i = 0; i < list.size(); i++) {
            JSONObject listSingle = list.getJSONObject(i);
            String idRes = listSingle.getString("id");
            if (id.equals(idRes)){
                isExistRes = true;
                String floorNameRes = listSingle.getString("floor_name");
                Assert.assertEquals(floorNameRes,floorName,"floor_name-- expect: " + floorName + ", actual " + floorNameRes);

                String floorMapRes = listSingle.getString("floor_map");
                if (floorMap ==null){
                    Assert.assertEquals(floorMapRes,floorMap,"floor_map---expect: " + floorMap + ", actual: " + floorMapRes);
                } else if (!(floorMapRes.contains(floorMap))){
                    throw new Exception("checkFloorList---返回的floor_map错误！");
                }
            }
        }

        Assert.assertEquals(isExistRes,isExist," isExist--expect: " + isExist + ", actual: " + isExistRes);
    }

    private void checkFloorDetail(String response, String id,String floorNum,String floorName,String floorMap) throws Exception{
        JSONObject data = JSON.parseObject(response).getJSONObject("data");
        String idRes = data.getString("id");
        if (id.equals(idRes)){
            String floorNumRes = data.getString("floor_num");
            Assert.assertEquals(floorNumRes,floorNum, "floor_num--expect: " + floorNum + " actual: " + floorNumRes);

            String floorNameRes = data.getString("floor_name");
            Assert.assertEquals(floorNameRes,floorName, "floor_name--expect: " + floorName + " actual: " + floorNameRes);

            String floorMapRes = data.getString("floor_map");
            if (floorMap ==null){
                Assert.assertEquals(floorMapRes,floorMap,"floor_map---expect: " + floorMap + ", actual: " + floorMapRes);
            }else if (!(floorMapRes.contains(floorMap))){
                throw new Exception("checkFloorList---返回的floor_map错误！");
            }
        }

    }

    private void checkUnitListPlate(String response, String plateCode, int width, double k0, double k1 ) throws Exception {

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
            if (plateCode.equals(plateCodeRes)){
                PlateCodeIsExist = true;
                for (int j = 0; j < sensors.size(); j++) {
                    JSONObject singleSensor = sensors.getJSONObject(j);
                    String sensorId = singleSensor.getString("sensor_id");
                    if ("0".equals(sensorId)){
                        KZeroIsExist = true;
                        k0Res = singleSensor.getDouble("k");
                    }else if ("1".equals(sensorId)){
                        KZeroIsExist = true;
                        k1Res = singleSensor.getDouble("k");
                    } else {
                        throw new Exception("不存在该sensor_id：" + sensorId );
                    }
                }
            }

            if (!PlateCodeIsExist){
                throw new Exception("plate_code: " + plateCode + " 不存在");
            } else {
                Assert.assertEquals(widthRes,width,"width--expect: " + width + ", actual: " + widthRes);
            }

            if (KZeroIsExist){
                Assert.assertEquals(k0Res,k0,"sensor_id : 0 , k值--expect：" + k0 + ",actual: " + k0Res);
                Assert.assertEquals(k1Res,k1,"sensor_id : 1 , k值--expect：" + k1 + ",actual: " + k1Res);
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
            // 封装request对象
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

            // client 请求
            ApiClient apiClient = new ApiClient("http://dev.api.winsenseos.com/retail/api/data/biz", credential);
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
        aCase.setQaOwner("廖祥茹");
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
