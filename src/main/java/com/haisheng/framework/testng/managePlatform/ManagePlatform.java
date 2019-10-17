package com.haisheng.framework.testng.managePlatform;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.model.bean.Case;
import com.haisheng.framework.testng.CommonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.util.HttpExecutorUtil;
import com.haisheng.framework.util.QADbUtil;
import com.haisheng.framework.util.StatusCode;
import okhttp3.*;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

public class ManagePlatform {

    private org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());
    boolean IS_DEBUG = false;

    private String DeviceUrl = "rtsp://admin:winsense2018@192.168.50.155";
    private String UID = "uid_04e816df";
    private String APP_ID = "0d28ec728799";
    private String BRAND_ID = "638";
    private String SHOP_Id = "640";
    private static String DEVICE_ID_1 = "6857175479387136";
    private static String DEVICE_ID_2 = "6857180815393792";
    private static String DEVICE_ID_MAPPING = "6862780298232832";
    private static String DEVICE_NAME_1 = "管理后台专用-1【勿动】";
    private static String DEVICE_NAME_2 = "管理后台专用-2【勿动】";

    private static String REGION_DEVICE_1 = "6863510172828672";
    private static String REGION_DEVICE_2 = "6863561660236800";

    private String regionTypeGeneral = "GENERAL";
    private String regionTypeAttention = "ATTENTION";


    private int LAYOUT_ID = 3238;
    private int LAYOUT_ID_MAPPING = 3243;

    private String REGION_ID = "3441";
    private String ENTRANCE_ID = "662";

    private String deviceTypeFaceCamera = "FACE_CAMERA";
    private String deviceTypeWebCamera = "WEB_CAMERA";

    private String subjectTypeMarket = "3";

    private String DingDingUrl = "https://oapi.dingtalk.com/robot/send?access_token=f9b712af64398d3b3234e1657069b9784f7a9360e7afc085211b194841056dca";
    private String Email = "liaoxiangru@winsense.ai";

    private String genAuthURL = "http://39.106.253.190/administrator/login";
    private String authorization = null;
    private HashMap<String, String> header = new HashMap();

    private String failReason = "";
    private QADbUtil qaDbUtil = new QADbUtil();
    private int APP_ID_DB = ChecklistDbInfo.DB_APP_ID_MANAGE_PORTAL_SERVICE;
    private int CONFIG_ID = ChecklistDbInfo.DB_SERVICE_ID_MANAGEMENT_PLATFORM_SERVICE;

    private String CI_CMD = "curl -X POST http://liaoxiangru:liaoxiangru@192.168.50.2:8080/job/management-platform/buildWithParameters?case_name=";

    private String URL_prefix = "http://39.106.253.190";

//----------------------------------------------------------模块一、设备管理------------------------------------------------------------------

    public String enumDeviceType() throws Exception {
        String url = URL_prefix + "/admin/data/deviceType/list";

        String response = getRequest(url, header);

        return response;
    }

    public String enumDeviceStatus() throws Exception {
        String url = URL_prefix + "/admin/data/device/status/enum";

        String response = getRequest(url, header);

        return response;
    }

    public String addDevice(String name, String deviceType, String subjectId, Case aCase, int step) throws Exception {

        String url = URL_prefix + "/admin/data/device/";

        String json =
                "{\n" +
                        "    \"name\":\"" + name + "\",\n" +
                        "    \"device_type\":\"" + deviceType + "\",\n" +
                        "    \"scene_type\":\"COMMON\",\n" +
                        "    \"cloud_scene_type\":\"DEFAULT\",\n" +
                        "    \"url\":\"rtsp://admin:winsense2018@192.168.50.155\",\n" +
                        "    \"subject_id\":\"" + subjectId + "\"\n" +
                        "}";

        String response = postRequest(url, json, header);
        sendResAndReqIdToDb(response, aCase, step);
        checkCode(response, StatusCode.SUCCESS, "");

        return response;
    }

    public String updateDevice(String name, String deviceId, String deviceType, int interval, int startTime, int endTime,
                               String ding, String email, String videoUrl, Case aCase, int step) throws Exception {

        String url = URL_prefix + "/admin/data/device/" + deviceId;

        String json =
                "{\n" +
                        "    \"name\":\"" + name + "\",\n" +
                        "    \"device_type\":\"" + deviceType + "\",\n" +
                        "    \"scene_type\":\"COMMON\",\n" +
                        "    \"cloud_scene_type\":\"MALL_ENTRANCE_SNAP\",\n" +
                        "    \"monitor_config\":{\n" +
                        "        \"open\":true,\n" +
                        "        \"interval\":" + interval + ",\n" +
                        "        \"time\":[\n" +
                        "            " + startTime + ",\n" +
                        "            " + endTime + "\n" +
                        "        ],\n" +
                        "        \"ding_ding\":[\n" +
                        "            \"" + ding + "\"\n" +
                        "        ],\n" +
                        "        \"email\":[\n" +
                        "            \"" + email + "\"\n" +
                        "        ]\n" +
                        "    },\n" +
                        "    \"url\":\"" + videoUrl + "\"\n" +
                        "}";

        String response = putRequest(url, json, header);
        sendResAndReqIdToDb(response, aCase, step);
        checkCode(response, StatusCode.SUCCESS, "");

        return response;
    }

    public String removeDevice(String deviceId, Case aCase, int step) throws Exception {

        String url = URL_prefix + "/admin/device/remove/" + deviceId;

        String json = "{}";

        String response = deleteRequest(url, json, header);
        sendResAndReqIdToDb(response, aCase, step);
        checkCode(response, StatusCode.SUCCESS, "");

        return response;
    }

    public String removeDevice(String deviceId) throws Exception {

        String url = URL_prefix + "/admin/device/remove/" + deviceId;

        String json = "{}";

        String response = deleteRequest(url, json, header);
        checkCode(response, StatusCode.SUCCESS, "");

        return response;
    }

    public String getDevice(String deviceId, Case aCase, int step) throws Exception {

        String url = URL_prefix + "/admin/data/device/" + deviceId;

        String response = getRequest(url, header);
        sendResAndReqIdToDb(response, aCase, step);
        checkCode(response, StatusCode.SUCCESS, "");

        return response;
    }

    public String listDevice(String subjectId, Case aCase, int step) throws Exception {

        String url = URL_prefix + "/admin/data/device/list";

        String json =
                "{\n" +
                        "    \"subject_id\":\"" + subjectId + "\",\n" +
                        "    \"level\":2,\n" +
                        "    \"page\":1,\n" +
                        "    \"size\":20\n" +
                        "}";

        String response = postRequest(url, json, header);
        sendResAndReqIdToDb(response, aCase, step);
        checkCode(response, StatusCode.SUCCESS, "");

        return response;
    }

    public String listDeviceDiffCondition(String subjectId, String condition, Case aCase, int step) throws Exception {

        String url = URL_prefix + "/admin/data/device/list";
        StringBuffer json = new StringBuffer();

        json.append("{\n").
                append("    \"subject_id\":\"" + subjectId + "\",\n").
                append("    \"level\":2,\n").
                append("    \"page\":1,\n").
                append("    \"size\":20,\n").
                append(condition).
                append("}");

        String response = postRequest(url, json.toString(), header);
        sendResAndReqIdToDb(response, aCase, step);
        checkCode(response, StatusCode.SUCCESS, "");

        return response;
    }

    public String startDevice(String deviceId, Case aCase, int step) throws Exception {

        String url = URL_prefix + "/admin/device/start/" + deviceId;

        String json =
                "{}";

        String response = postRequest(url, json, header);
        sendResAndReqIdToDb(response, aCase, step);
        checkCode(response, StatusCode.SUCCESS, "");

        return response;
    }

    public String stopDevice(String deviceId, Case aCase, int step) throws Exception {

        String url = URL_prefix + "/admin/device/stop/" + deviceId;

        String json =
                "{}";

        String response = postRequest(url, json, header);
        sendResAndReqIdToDb(response, aCase, step);
        checkCode(response, StatusCode.SUCCESS, "");

        return response;
    }

    public String batchStartDevice(String deviceId1, String deviceId2, Case aCase, int step) throws Exception {

        String url = URL_prefix + "/admin/batch/device/start";

        String json =
                "{\n" +
                        "    \"device_list\":[\n" +
                        "        \"" + deviceId1 + "\",\n" +
                        "        \"" + deviceId2 + "\"\n" +
                        "    ]\n" +
                        "}";

        String response = postRequest(url, json, header);
        sendResAndReqIdToDb(response, aCase, step);
        checkCode(response, StatusCode.SUCCESS, "");

        return response;
    }

    public String batchStopDevice(String deviceId1, String deviceId2, Case aCase, int step) throws Exception {

        String url = URL_prefix + "/admin/batch/device/stop";

        String json =
                "{\n" +
                        "    \"stop_list\":[\n" +
                        "        {\n" +
                        "            \"device_id\":\"" + deviceId1 + "\"\n" +
                        "        },\n" +
                        "        {\n" +
                        "            \"device_id\":\"" + deviceId2 + "\"\n" +
                        "        }\n" +
                        "    ]\n" +
                        "}";

        String response = postRequest(url, json, header);
        sendResAndReqIdToDb(response, aCase, step);
        checkCode(response, StatusCode.SUCCESS, "");

        return response;
    }

    public String batchRemoveDevice(String deviceId1, String deviceId2, Case aCase, int step) throws Exception {

        String url = URL_prefix + "/admin/batch/device/remove";

        String json =
                "{\n" +
                        "    \"remove_list\":[\n" +
                        "        {\n" +
                        "            \"device_id\":\"" + deviceId1 + "\"\n" +
                        "        },\n" +
                        "        {\n" +
                        "            \"device_id\":\"" + deviceId2 + "\"\n" +
                        "        }\n" +
                        "    ]\n" +
                        "}";

        String response = postRequest(url, json, header);
        sendResAndReqIdToDb(response, aCase, step);
        checkCode(response, StatusCode.SUCCESS, "");

        return response;
    }

    public String batchMonitorDevice(String deviceId1, String deviceId2, int interval, int startTime, int endTime,
                                     String ding, String email, Case aCase, int step) throws Exception {

        String url = URL_prefix + "/admin/batch/device/monitorConfig";

        String json =
                "{\n" +
                        "    \"list\":[\n" +
                        "        \"" + deviceId1 + "\",\n" +
                        "        \"" + deviceId2 + "\"\n" +
                        "    ],\n" +
                        "    \"monitor_config\":{\n" +
                        "        \"open\":true,\n" +
                        "        \"interval\":" + interval + ",\n" +
                        "        \"time\":[\n" +
                        "            " + startTime + "," +
                        "            " + endTime +
                        "        ],\n" +
                        "        \"ding_ding\":[\n" +
                        "            \"" + ding + "\"\n" +
                        "        ],\n" +
                        "        \"email\":[\n" +
                        "            \"" + email + "\"\n" +
                        "        ]\n" +
                        "    }\n" +
                        "}";

        String response = postRequest(url, json, header);
        sendResAndReqIdToDb(response, aCase, step);
        checkCode(response, StatusCode.SUCCESS, "");

        return response;
    }

    @Test
    public void addDeviceCheck() throws Exception {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();

        failReason = "";
        Case aCase = new Case();
        int step = 0;

        String caseName = ciCaseName;
        String caseDesc = "验证增加设备是否成功";
        logger.info(caseDesc + "-----------------------------------------------------------------------------------");

        String name = ciCaseName;

        String type = null;
        String deviceId = null;
        try {
            aCase.setRequestData("1、增加设备-2、查询设备列表-3、查询设备详情" + "\n\n");
            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);

//            1、增加设备
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            type = getOneDeviceType();
            String response = addDevice(name, type, SHOP_Id, aCase, step);
            deviceId = getDeviceId(response);

//            2、查询设备列表
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            response = listDevice(SHOP_Id, aCase, step);

            checkisExistByListDevice(response, deviceId, true);

//            3、查询设备详情
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            response = getDevice(deviceId, aCase, step);

            checkisExistByGetDevice(response, deviceId, true);

            aCase.setResult("PASS");
        } catch (AssertionError e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
        } finally {
            removeDevice(deviceId);
            if (!IS_DEBUG) {
                qaDbUtil.saveToCaseTable(aCase);
            }
        }
    }

    @Test
    public void updateDeviceCheck() throws Exception {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();

        failReason = "";
        Case aCase = new Case();
        int step = 0;

        String caseName = ciCaseName;
        String caseDesc = "验证更新设备是否成功";
        logger.info(caseDesc + "-----------------------------------------------------------------------------------");

        String name = ciCaseName;

        String deviceType = null;
        String deviceId = null;

        try {

            aCase.setRequestData("1、增加设备-2、更新设备-3、查询设备详情" + "\n\n");
            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);

//            1、增加设备
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            deviceType = getOneDeviceType();
            String response = addDevice(name, deviceType, SHOP_Id, aCase, step);
            deviceId = getDeviceId(response);

            String newDeviceType = getOneDeviceType();
            String newName = ciCaseName + "-new";
            int interval = 350;
            int startTime = 9;
            int endTime = 9;
            String ding = "newDing";
            String email = "new@winsense.ai";
            String videoUrl = "newVideoUrl";

//            2、更新设备
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            updateDevice(newName, deviceId, newDeviceType, interval, startTime, endTime, ding, email, videoUrl, aCase, step);

//            3、查询设备详情
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            response = getDevice(deviceId, aCase, step);

            checkUpdateResult(response, deviceId, newName, newDeviceType, interval, startTime, endTime, ding, email, videoUrl);

            aCase.setResult("PASS");
        } catch (AssertionError e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
        } finally {
            removeDevice(deviceId);
            if (!IS_DEBUG) {
                qaDbUtil.saveToCaseTable(aCase);
            }
        }
    }

    @Test
    public void deleteDeviceCheck() throws Exception {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        failReason = "";
        Case aCase = new Case();
        int step = 0;

        String caseName = ciCaseName;
        String caseDesc = "验证删除设备是否成功";
        logger.info(caseDesc + "-----------------------------------------------------------------------------------");

        String name = ciCaseName;

        String deviceType = null;
        String deviceId = null;
        try {
            aCase.setRequestData("1、增加设备-2、查询设备列表-3、删除设备-4、查询设备列表" + "\n\n");
            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);

//            1、增加设备
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            deviceType = getOneDeviceType();
            String response = addDevice(name, deviceType, SHOP_Id, aCase, step);

            deviceId = getDeviceId(response);

//            2、查询设备列表
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            response = listDevice(SHOP_Id, aCase, step);
            checkisExistByListDevice(response, deviceId, true);

//            3、删除设备
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            removeDevice(deviceId, aCase, step);

//            4、查询设备列表
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            response = listDevice(SHOP_Id, aCase, step);
            checkisExistByListDevice(response, deviceId, false);

            aCase.setResult("PASS");
        } catch (AssertionError e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
        } finally {
            if (!IS_DEBUG) {
                qaDbUtil.saveToCaseTable(aCase);
            }
        }

    }

    @Test
    public void startDeviceCheck() throws Exception {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        failReason = "";
        Case aCase = new Case();
        int step = 0;

        String caseName = ciCaseName;
        String caseDesc = "验证启动设备是否成功";
        logger.info(caseDesc + "-----------------------------------------------------------------------------------");

        String name = ciCaseName;

        String deviceType = null;
        String deviceId = null;
        try {
            aCase.setRequestData("1、增加设备-2、启动设备-3、查询设备列表-4、停止设备；5、查询设备列表" + "\n\n");
            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);

//            1、增加设备
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            deviceType = getOneDeviceType();
            String response = addDevice(name, deviceType, SHOP_Id, aCase, step);

            deviceId = getDeviceId(response);

//            2、启动设备
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            startDevice(deviceId, aCase, step);
            Thread.sleep(60 * 1000);

//            3、查询设备列表
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            response = listDevice(SHOP_Id, aCase, step);
            checkDeviceStatus(response, deviceId, "RUNNING");

//            4、停止设备
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            stopDevice(deviceId, aCase, step);

//            5、查询设备列表
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            response = listDevice(SHOP_Id, aCase, step);
            checkDeviceStatus(response, deviceId, "UN_DEPLOYMENT");

            aCase.setResult("PASS");
        } catch (AssertionError e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
        } finally {
            removeDevice(deviceId);
            if (!IS_DEBUG) {
                qaDbUtil.saveToCaseTable(aCase);
            }
        }
    }

    @Test
    public void batchStartDeviceCheck() {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        failReason = "";
        Case aCase = new Case();
        int step = 0;

        String caseName = ciCaseName;
        String caseDesc = "验证批量启动/停止设备是否成功";
        logger.info(caseDesc + "-----------------------------------------------------------------------------------");

        String response = null;
        try {

            aCase.setRequestData("1、批量启动设备-2、查询设备列表-3、批量停止设备-4、查询设备列表" + "\n\n");
            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);

//            1、批量启动设备
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            response = batchStartDevice(DEVICE_ID_1, DEVICE_ID_2, aCase, step);
            String[] success = {DEVICE_ID_1, DEVICE_ID_2};
            String[] fail = new String[0];
            checkBatchResult(response, success, fail, "批量启动设备");

            Thread.sleep(120 * 1000);

//            2、查询设备列表
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            response = listDevice(SHOP_Id, aCase, step);
            checkDeviceStatus(response, DEVICE_ID_1, "RUNNING");
            checkDeviceStatus(response, DEVICE_ID_2, "RUNNING");

//            3、批量停止设备
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            response = batchStopDevice(DEVICE_ID_1, DEVICE_ID_2, aCase, step);
            checkBatchResult(response, success, fail, "批量停止设备");

//            4、查询设备列表
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            response = listDevice(SHOP_Id, aCase, step);
            checkDeviceStatus(response, DEVICE_ID_1, "UN_DEPLOYMENT");
            checkDeviceStatus(response, DEVICE_ID_2, "UN_DEPLOYMENT");

            aCase.setResult("PASS");
        } catch (AssertionError e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
        } finally {
            if (!IS_DEBUG) {
                qaDbUtil.saveToCaseTable(aCase);
            }
        }
    }

    @Test
    public void batchMonitorDeviceCheck() throws Exception {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        failReason = "";
        Case aCase = new Case();
        int step = 0;

        String caseName = ciCaseName;
        String caseDesc = "验证批量设置告警是否成功";
        logger.info(caseDesc + "-----------------------------------------------------------------------------------");

        int interval = 340;
        int startTime = 8;
        int endTime = 8;
        String ding = "dingdingAlarm";
        String email = "testMonitor@winsense.ai";

        String name1 = ciCaseName + "-1";
        String name2 = ciCaseName + "-2";

        String deviceType = null;
        String deviceId1 = null;
        String deviceId2 = null;

        try {
            aCase.setRequestData("1、增加设备（2个）-2、批量设置告警-3、查询设备详情-4、查询设备详情" + "\n\n");
            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);

//            1、增加设备
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            deviceType = getOneDeviceType();
            String response = addDevice(name1, deviceType, SHOP_Id, aCase, step);
            deviceId1 = getDeviceId(response);

//            2、增加设备
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            response = addDevice(name2, deviceType, SHOP_Id, aCase, step);
            deviceId2 = getDeviceId(response);

//            3、批量设置告警
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            response = batchMonitorDevice(deviceId1, deviceId2, interval, startTime, endTime, ding, email, aCase, step);
            String[] success = {deviceId1, deviceId2};
            String[] fail = new String[0];
            checkBatchResult(response, success, fail, "批量设置告警");

//            4、查询设备详情
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            response = getDevice(deviceId1, aCase, step);
            checkMonitorResult(response, deviceId1, interval, startTime, endTime, ding, email);

//            5、查询设备详情
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            response = getDevice(deviceId2, aCase, step);
            checkMonitorResult(response, deviceId2, interval, startTime, endTime, ding, email);

            aCase.setResult("PASS");
        } catch (AssertionError e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
        } finally {
            removeDevice(deviceId1);
            removeDevice(deviceId2);
            if (!IS_DEBUG) {
                qaDbUtil.saveToCaseTable(aCase);
            }
        }
    }

    @Test
    public void batchRemoveDeviceCheck() throws Exception {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();

        failReason = "";
        Case aCase = new Case();
        int step = 0;

        String caseName = ciCaseName;
        String caseDesc = "验证批量删除设备是否成功";
        logger.info(caseDesc + "-----------------------------------------------------------------------------------");

        String name1 = ciCaseName + "-1";
        String name2 = ciCaseName + "-2";

        String deviceType = null;
        String deviceId1 = null;
        String deviceId2 = null;

        try {
            aCase.setRequestData("1，2、增加设备（2个）-3，4、查询设备列表-5、批量删除设备-6，7、查询设备列表" + "\n\n");
            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);

//            1、增加设备
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            deviceType = getOneDeviceType();
            String response = addDevice(name1, deviceType, SHOP_Id, aCase, step);
            deviceId1 = getDeviceId(response);

//            2、增加设备
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            response = addDevice(name2, deviceType, SHOP_Id, aCase, step);
            deviceId2 = getDeviceId(response);

//            3、查询设备列表
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            response = listDevice(SHOP_Id, aCase, step);
            checkisExistByListDevice(response, deviceId1, true);

//            4、查询设备列表
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            response = listDevice(SHOP_Id, aCase, step);
            checkisExistByListDevice(response, deviceId2, true);

//            5、批量删除设备
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            response = batchRemoveDevice(deviceId1, deviceId2, aCase, step);

            String[] success = {deviceId1, deviceId2};
            String[] fail = new String[0];
            checkBatchResult(response, success, fail, "批量删除设备");

//            6、查询设备列表
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            response = listDevice(SHOP_Id, aCase, step);
            checkisExistByListDevice(response, deviceId1, false);

//            7、查询设备列表
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            response = listDevice(SHOP_Id, aCase, step);
            checkisExistByListDevice(response, deviceId2, false);

            aCase.setResult("PASS");
        } catch (AssertionError e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
        } finally {
            if (!IS_DEBUG) {
                qaDbUtil.saveToCaseTable(aCase);
            }
        }
    }

    @Test
    public void listDeviceDSCheck() throws Exception {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        failReason = "";
        Case aCase = new Case();
        int step = 0;

        String caseName = ciCaseName;
        String caseDesc = "验证查询设备列表必返回参数";
        logger.info(caseDesc + "-----------------------------------------------------------------------------------");

        try {
            aCase.setRequestData("1、启动设备-2、查询设备列表-3、停止设备" + "\n\n");
            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);

//            1、启动设备
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            startDevice(DEVICE_ID_1, aCase, step);
            Thread.sleep(60 * 1000);

//            2、查询设备列表
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            String response = listDevice(SHOP_Id, aCase, step);
            checkListDeviceDs(response, DEVICE_ID_1, true);
            checkListDeviceDs(response, DEVICE_ID_2, false);

//            3、停止设备
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            stopDevice(DEVICE_ID_1, aCase, step);

            aCase.setResult("PASS");

        } catch (AssertionError e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
        } finally {
            if (!IS_DEBUG) {
                qaDbUtil.saveToCaseTable(aCase);
            }
        }
    }

    @Test(dataProvider = "CONDITION")
    public void listDeviceDiffConditionCheck(String id, String condition) throws Exception {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        failReason = "";
        Case aCase = new Case();
        int step = 0;

        String caseName = ciCaseName + id;
        String caseDesc = "验证查询设备列表不同的搜索条件";
        logger.info(caseDesc + "-----------------------------------------------------------------------------------");

        try {
            aCase.setRequestData("1、启动设备-2、查询设备列表-3、停止设备" + "\n\n");
            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);

//            1、启动设备
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            startDevice(DEVICE_ID_1, aCase, step);

            if (condition.contains("RUNNING")) {
                Thread.sleep(60 * 1000);
            }

//            2、查询设备列表
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            String response = listDeviceDiffCondition(SHOP_Id, condition, aCase, step);

            checkisExistByListDevice(response, DEVICE_ID_1, true);
            checkisExistByListDevice(response, DEVICE_ID_2, false);

//            3、停止设备
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            stopDevice(DEVICE_ID_1, aCase, step);

            aCase.setResult("PASS");

        } catch (AssertionError e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
        } finally {
            if (!IS_DEBUG) {
                qaDbUtil.saveToCaseTable(aCase);
            }
        }
    }

    private void checkListDeviceDs(String response, String deviceId, boolean isStart) throws Exception {

        boolean isExist = false;
        JSONObject resJo = JSON.parseObject(response);

        if (!resJo.containsKey("data")) {
            throw new Exception("查询设备列表---“data字段为空！");
        }
        JSONObject data = resJo.getJSONObject("data");


        if (!data.containsKey("list")) {
            throw new Exception("查询设备列表---“data-list字段为空！");
        }
        JSONArray list = data.getJSONArray("list");

        for (int i = 0; i < list.size(); i++) {
            JSONObject singleDevice = list.getJSONObject(i);
            String deviceIdRes = singleDevice.getString("device_id");
            if (deviceId.equals(deviceIdRes)) {
                isExist = true;

                if (!singleDevice.containsKey("name")) {
                    throw new Exception("查询设备列表---没有返回“name”字段！");
                }
                String name = singleDevice.getString("name");
                if (name == null || "".equals(name)) {
                    throw new Exception("查询设备列表---“name”字段值为空！");
                }

                if (!singleDevice.containsKey("app_id")) {
                    throw new Exception("查询设备列表---没有返回“app_id”字段！");
                }
                String appId = singleDevice.getString("app_id");
                if (appId == null || "".equals(appId)) {
                    throw new Exception("查询设备列表---“app_id”字段值为空！");
                }

                if (!singleDevice.containsKey("subject_id")) {
                    throw new Exception("查询设备列表---没有返回“subject_id”字段！");
                }
                String subjectId = singleDevice.getString("subject_id");
                if (subjectId == null || "".equals(subjectId)) {
                    throw new Exception("查询设备列表---“subject_id”字段值为空！");
                }

                if (!singleDevice.containsKey("device_type")) {
                    throw new Exception("查询设备列表---没有返回“device_type”字段！");
                }
                String deviceType = singleDevice.getString("device_type");
                if (deviceType == null || "".equals(deviceType)) {
                    throw new Exception("查询设备列表---“device_type”字段值为空！");
                }

                if (!singleDevice.containsKey("type_name")) {
                    throw new Exception("查询设备列表---没有返回“type_name”字段！");
                }
                String typeName = singleDevice.getString("type_name");
                if (typeName == null || "".equals(typeName)) {
                    throw new Exception("查询设备列表---“type_name”字段值为空！");
                }

                if (!singleDevice.containsKey("device_status")) {
                    throw new Exception("查询设备列表---没有返回“device_status”字段！");
                }
                String deviceStatus = singleDevice.getString("device_status");
                if (deviceStatus == null || "".equals(deviceStatus)) {
                    throw new Exception("查询设备列表---“device_status”字段值为空！");
                }

                if (!singleDevice.containsKey("scene_name")) {
                    throw new Exception("查询设备列表---没有返回“scene_name”字段！");
                }
                String sceneName = singleDevice.getString("scene_name");
                if (sceneName == null || "".equals(sceneName)) {
                    throw new Exception("查询设备列表---“scene_name”字段值为空！");
                }

                if (!singleDevice.containsKey("scene_type")) {
                    throw new Exception("查询设备列表---没有返回“scene_type”字段！");
                }
                String sceneType = singleDevice.getString("scene_type");
                if (sceneType == null || "".equals(sceneType)) {
                    throw new Exception("查询设备列表---“scene_type”字段值为空！");
                }

                if (!singleDevice.containsKey("cloud_scene_name")) {
                    throw new Exception("查询设备列表---没有返回“cloud_scene_name”字段！");
                }
                String cloudSceneName = singleDevice.getString("cloud_scene_name");
                if (cloudSceneName == null || "".equals(cloudSceneName)) {
                    throw new Exception("查询设备列表---“cloud_scene_name”字段值为空！");
                }

                if (!singleDevice.containsKey("cloud_scene_type")) {
                    throw new Exception("查询设备列表---没有返回“cloud_scene_type”字段！");
                }
                String cloudSceneType = singleDevice.getString("cloud_scene_type");
                if (cloudSceneType == null || "".equals(cloudSceneType)) {
                    throw new Exception("查询设备列表---“cloud_scene_type”字段值为空！");
                }

                if (!singleDevice.containsKey("deploy_time")) {
                    throw new Exception("查询设备列表---没有返回“deploy_time”字段！");
                }
                String deployTime = singleDevice.getString("deploy_time");

                if (isStart) {
                    if (deployTime == null || "".equals(deployTime)) {
                        throw new Exception("设备启动，但是“compute_id”字段为空！");
                    }
                } else {
                    if (deployTime != null) {
                        throw new Exception("设备未启动，但是“compute_id”字段不为空！");
                    }
                }

                if (!singleDevice.containsKey("creator")) {
                    throw new Exception("查询设备列表---没有返回“creator”字段！");
                }

                JSONObject creator = singleDevice.getJSONObject("creator");
                if (!creator.containsKey("name")) {
                    throw new Exception("查询设备列表---没有返回“creator-name”字段！");
                }

                String creatorName = creator.getString("name");
                if (creatorName == null || "".equals(creatorName)) {
                    throw new Exception("查询设备列表---“creator-name”字段值为空！");
                }

                if (!singleDevice.containsKey("gmt_create")) {
                    throw new Exception("查询设备列表---没有返回“gmt_create”字段！");
                }
                String createTime = singleDevice.getString("gmt_create");
                if (createTime == null || "".equals(createTime)) {
                    throw new Exception("查询设备列表---“gmt_create”字段值为空！");
                }

                if (!singleDevice.containsKey("compute_id")) {
                    throw new Exception("查询设备列表---没有返回“compute_id”字段！");
                }
                String computeId = singleDevice.getString("compute_id");
                if (isStart) {
                    if (computeId == null || "".equals(computeId)) {
                        throw new Exception("设备启动，但是“compute_id”字段为空！");
                    }
                } else {
                    if (computeId != null) {
                        throw new Exception("设备未启动，但是“compute_id”字段不为空！");
                    }
                }

                if (!singleDevice.containsKey("deployment_id")) {
                    throw new Exception("查询设备列表---没有返回“deployment_id”字段！");
                }
                String deploymentId = singleDevice.getString("deployment_id");
                if (isStart) {
                    if (deploymentId == null || "".equals(deploymentId)) {
                        throw new Exception("设备启动，但是“deployment_id”字段为空！");
                    }
                } else {
                    if (deploymentId != null) {
                        throw new Exception("设备未启动，但是“deployment_id”字段不为空！");
                    }
                }
            }
        }

        if (!isExist) {
            throw new Exception("未查询到该设备！");
        }
    }


    private void checkMonitorResult(String response, String deviceId, int interval, int startTime, int endTime, String ding, String email) throws Exception {
        JSONObject data = JSON.parseObject(response).getJSONObject("data");

        if (!data.containsKey("device_id")) {
            throw new Exception("设备详情---“device_id”字段为空");
        }
        String deviceIdRes = data.getString("device_id");
        Assert.assertEquals(deviceIdRes, deviceId, "deviceId is not Exist,expect: " + deviceId + ",actual: " + deviceIdRes);

        if (!data.containsKey("monitor_config")) {
            throw new Exception("设备详情---“monitor_config”字段为空");
        }
        JSONObject monitorConfig = data.getJSONObject("monitor_config");

        if (!monitorConfig.containsKey("interval")) {
            throw new Exception("设备详情---“monitor_config-interval”字段为空");
        }
        int intervalRes = monitorConfig.getInteger("interval");
        Assert.assertEquals(intervalRes, interval, "interval is not Exist,expect: " + interval + ",actual: " + intervalRes);

        if (!monitorConfig.containsKey("time")) {
            throw new Exception("设备详情---“monitor_config-time”字段为空");
        }
        int startTimeRes = monitorConfig.getJSONArray("time").getInteger(0);
        Assert.assertEquals(startTimeRes, startTime, "startTime is not Exist,expect: " + startTime + ",actual: " + startTimeRes);

        int endTimeRes = monitorConfig.getJSONArray("time").getInteger(1);
        Assert.assertEquals(endTimeRes, endTime, "endTime is not Exist,expect: " + endTime + ",actual: " + endTimeRes);

        if (!monitorConfig.containsKey("email")) {
            throw new Exception("设备详情---“monitor_config-email”字段为空");
        }
        String emailRes = monitorConfig.getJSONArray("email").getString(0);
        Assert.assertEquals(emailRes, email, "email is not Exist,expect: " + email + ",actual: " + emailRes);

        if (!monitorConfig.containsKey("ding_ding")) {
            throw new Exception("设备详情---“monitor_config-ding”字段为空");
        }
        String dingRes = monitorConfig.getJSONArray("ding_ding").getString(0);
        Assert.assertEquals(dingRes, ding, "ding_ding is not Exist,expect: " + ding + ",actual: " + dingRes);

    }

    private void checkBatchResult(String response, String[] successIds, String[] failIds, String message) {

        JSONObject data = JSON.parseObject(response).getJSONObject("data");
        JSONArray successs = data.getJSONArray("success");

        System.out.println(JSON.parseObject(response) + "");

        String[] successIdsRes = new String[successs.size()];
        for (int i = 0; i < successs.size(); i++) {
            successIdsRes[i] = successs.getString(i);
        }

        Assert.assertEquals(successIdsRes, successIds, message + "失败！成功ID与实际不符");

        JSONArray fail = data.getJSONArray("fail");
        String[] failIdsRes = new String[fail.size()];
        for (int i = 0; i < fail.size(); i++) {
            failIdsRes[i] = fail.getString(i);
        }

        Assert.assertEquals(failIdsRes, failIds, message + "失败！失败ID与实际不符");
    }

    private void checkDeviceStatus(String response, String deviceId, String status) throws Exception {

        JSONArray list = JSON.parseObject(response).getJSONObject("data").getJSONArray("list");

        boolean isExist = false;

        for (int i = 0; i < list.size(); i++) {
            JSONObject singleDevice = list.getJSONObject(i);
            String deviceIdRes = singleDevice.getString("device_id");

            if (deviceId.equals(deviceIdRes)) {
                isExist = true;
                String statusRes = singleDevice.getString("device_status");
                Assert.assertEquals(statusRes, status, "device_status is wrong, expect: " + status + ",actaul: " + statusRes);
            }
        }

        if (!isExist) {
            throw new Exception("该设备不存在！");
        }

    }

    private void checkUpdateResult(String response, String deviceId, String name, String deviceType, int interval, int startTime, int endTime,
                                   String ding, String email, String videoUrl) throws Exception {

        JSONObject resJo = JSON.parseObject(response);
        if (!resJo.containsKey("data")) {
            throw new Exception("设备详情---“data”字段为空");
        }
        JSONObject data = JSON.parseObject(response).getJSONObject("data");

        checkMonitorResult(response, deviceId, interval, startTime, endTime, ding, email);

        if (!data.containsKey("device_type")) {
            throw new Exception("设备详情---“device_type”字段为空");
        }
        String deviceTypeRes = data.getString("device_type");
        Assert.assertEquals(deviceTypeRes, deviceType, "deviceType is not Exist,expect: " + deviceType + ",actual: " + deviceTypeRes);

        if (!data.containsKey("name")) {
            throw new Exception("设备详情---“name”字段为空");
        }
        String nameRes = data.getString("name");
        Assert.assertEquals(nameRes, name, "name is not Exist,expect: " + name + ",actual: " + nameRes);

        if (!data.containsKey("url")) {
            throw new Exception("设备详情---“url”字段为空");
        }
        String videoUrlRes = data.getString("url");
        Assert.assertEquals(videoUrlRes, videoUrl, "videoUrl is not Exist,expect: " + videoUrl + ",actual: " + videoUrlRes);
    }

    private String getDeviceId(String response) {
        JSONObject data = JSON.parseObject(response).getJSONObject("data");
        String deviceId = data.getString("device_id");

        return deviceId;
    }

    private void checkisExistByListDevice(String response, String deivceId, boolean isExist) {
        boolean isExistRes = false;

        JSONObject data = JSON.parseObject(response).getJSONObject("data");
        JSONArray list = data.getJSONArray("list");

        for (int i = 0; i < list.size(); i++) {
            JSONObject singleDevice = list.getJSONObject(i);
            String deviceIdRes = singleDevice.getString("device_id");
            if (deviceIdRes.equals(deivceId)) {
                isExistRes = true;
            }
        }

        Assert.assertEquals(isExistRes, isExist, "expect exist :" + isExist + ",actual: " + isExistRes);
    }

    private void checkisExistByGetDevice(String response, String deivceId, boolean isExist) {
        boolean isExistRes = false;

        JSONObject data = JSON.parseObject(response).getJSONObject("data");

        String deviceIdRes = data.getString("device_id");

        if (deviceIdRes.equals(deivceId)) {
            isExistRes = true;
        }

        Assert.assertEquals(isExistRes, isExist, "expect exist :" + isExist + ",actual: " + isExistRes);
    }


//    -----------------------------------------平面管理模块----------------------------------------

    public String enumFloorList() throws Exception {
        System.out.println("dfdfd");

        String url = URL_prefix + "/admin/data/layout/floorEnum/list";

        String response = getRequest(url, header);
        checkCode(response, StatusCode.SUCCESS, "获取楼层");

        return response;
    }

    public String addLayout(String name, String description, String subjectId, int floorId, int expectCode, Case aCase, int step) throws Exception {

        logger.info("\n");
        logger.info("------------------------1、创建新平面----------------------------");

        String url = URL_prefix + "/admin/data/layout/";

        String json =
                "{\n" +
                        "    \"name\":\"" + name + "\",\n" +
                        "    \"description\":\"" + description + "\",\n" +
                        "    \"subject_id\":\"" + subjectId + "\",\n" +
                        "    \"floor_id\":" + floorId +
                        "}";

        String response = postRequest(url, json, header);
        sendResAndReqIdToDb(response, aCase, step);
        checkCode(response, expectCode, "新建平面");

        return response;
    }

    public String layoutPicUpload() throws IOException {

        logger.info("\n");
        logger.info("------------------------2、平面图片上传----------------------------");

        String url = URL_prefix + "/admin/data/layout/layoutPicUpload";

        String filePath = "src\\main\\java\\com\\haisheng\\framework\\testng\\managePlatform\\experimentLayout";
        filePath = filePath.replace("\\", File.separator);
        File file = new File(filePath);

        OkHttpClient client = new OkHttpClient();

        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);

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

        JSONObject data = JSON.parseObject(response.body().string()).getJSONObject("data");

        String layoutPic = data.getString("url");

        return layoutPic;

    }

    public String deleteLayoutPic(int layoutId, Case aCase, int step) throws Exception {

        logger.info("\n");
        logger.info("------------------------3、平面图片删除----------------------------");

        String url = URL_prefix + "/admin/data/layout/layoutPicUpload/" + layoutId;

        String json = "{}";

        String response = deleteRequest(url, json, header);
        sendResAndReqIdToDb(response, aCase, step);
        checkCode(response, StatusCode.SUCCESS, "平面图片删除");

        return response;
    }

    public String deleteLayout(int layoutId, Case aCase, int step) throws Exception {

        logger.info("\n");
        logger.info("------------------------4、平面删除----------------------------");

        String url = URL_prefix + "/admin/data/layout/" + layoutId;
        String json =
                "{}";

        String response = deleteRequest(url, json, header);
        sendResAndReqIdToDb(response, aCase, step);
        checkCode(response, StatusCode.SUCCESS, "");
        return response;
    }

    public String deleteLayout(int layoutId) throws Exception {

        logger.info("\n");
        logger.info("------------------------4、finally平面删除----------------------------");

        String url = URL_prefix + "/admin/data/layout/" + layoutId;
        String json =
                "{}";

        String response = deleteRequest(url, json, header);
        checkCode(response, StatusCode.SUCCESS, "");
        return response;
    }

    public String updateLayout(int layoutId, String name, String description, String layoutPic, int floorId, Case aCase, int step) throws Exception {

        logger.info("\n");
        logger.info("------------------------5、平面编辑----------------------------");

        String url = URL_prefix + "/admin/data/layout/" + layoutId;

        String json =
                "{\n" +
                        "    \"name\":\"" + name + "\",\n" +
                        "    \"description\":\"" + description + "\",\n" +
                        "    \"layout_pic_oss\":\"" + layoutPic + "\",\n" +
                        "    \"floor_id\":" + floorId +
                        "}";

        String response = putRequest(url, json, header);
        sendResAndReqIdToDb(response, aCase, step);
        checkCode(response, StatusCode.SUCCESS, "平面编辑");

        return response;
    }

    public String getLayout(int layoutId, Case aCase, int step) throws Exception {
        logger.info("\n");
        logger.info("------------------------6、平面详情----------------------------");

        String url = URL_prefix + "/admin/data/layout/" + layoutId;

        String response = getRequest(url, header);
        sendResAndReqIdToDb(response, aCase, step);
        checkCode(response, StatusCode.SUCCESS, "平面详情");
        return response;
    }

    public String listLayout(String subjectId, Case aCase, int step) throws Exception {
        logger.info("\n");
        logger.info("------------------------7、平面列表（分页）----------------------------");

        String url = URL_prefix + "/admin/data/layout/list";
        String json =
                "{\n" +
                        "    \"subject_id\":\"" + subjectId + "\",\n" +
                        "    \"page\":1,\n" +
                        "    \"size\":50\n" +
                        "}";

        String response = postRequest(url, json, header);
        sendResAndReqIdToDb(response, aCase, step);
        checkCode(response, StatusCode.SUCCESS, "平面列表（分页）");
        return response;
    }

    public String listAllLayout(String subjectId, Case aCase, int step) throws Exception {
        logger.info("\n");
        logger.info("------------------------7、平面列表（不分页）----------------------------");

        String url = URL_prefix + "/admin/data/layout/listAll";
        String json =
                "{\n" +
                        "    \"subject_id\":\"" + subjectId + "\"" +
                        "}";

        String response = postRequest(url, json, header);
        sendResAndReqIdToDb(response, aCase, step);
        checkCode(response, StatusCode.SUCCESS, "平面列表（不分页）");
        return response;
    }

    public String addLayoutDevice(int layoutId, String deviceId, Case aCase, int step) throws Exception {
        logger.info("\n");
        logger.info("------------------------8、平面设备新增----------------------------");

        String url = URL_prefix + "/admin/data/layoutDevice/" + deviceId + "/" + layoutId;
        String json =
                "{}";

        String response = postRequest(url, json, header);
        sendResAndReqIdToDb(response, aCase, step);
        checkCode(response, StatusCode.SUCCESS, "平面所属设备列表");
        return response;
    }

    //    平面设备删除
    public String deleteLayoutDevice(int layoutId, String deviceId, Case aCase, int step) throws Exception {
        logger.info("\n");
        logger.info("------------------------9、平面设备删除----------------------------");

        String url = URL_prefix + "/admin/data/layoutDevice/" + deviceId + "/" + layoutId;
        String json =
                "{}";

        String response = deleteRequest(url, json, header);
        sendResAndReqIdToDb(response, aCase, step);
        checkCode(response, StatusCode.SUCCESS, "平面设备删除");
        return response;
    }

    public String listLayoutDevice(int layoutId, Case aCase, int step) throws Exception {
        logger.info("\n");
        logger.info("------------------------11、平面所属设备列表----------------------------");

        String url = URL_prefix + "/admin/data/layoutDevice/pagelist";
        String json =
                "{\n" +
                        "    \"layout_id\":\"" + layoutId + "\",\n" +
                        "    \"page\":1,\n" +
                        "    \"size\":50\n" +
                        "}";

        String response = postRequest(url, json, header);
        sendResAndReqIdToDb(response, aCase, step);
        checkCode(response, StatusCode.SUCCESS, "平面所属设备列表");
        return response;
    }

    public String genMappingJson(String method, String deviceId, int layoutId) {

        String json =
                "{\n" +
                        "    \"device_id\":\"" + deviceId + "\",\n" +
                        "    \"layout_id\":\"" + layoutId + "\",\n" +
                        "    \"device_location\":{\n" +
                        "        \"x\":0.6482204361875191,\n" +
                        "        \"y\":0.6309841031580149,\n" +
                        "        \"degree\":0\n" +
                        "    },\n" +
                        "    \"layout_mapping\":{\n" +
                        "        \"a\":{\n" +
                        "            \"x\":0.886133611007495,\n" +
                        "            \"y\":0.622972179426388\n" +
                        "        },\n" +
                        "        \"b\":{\n" +
                        "            \"x\":0.8821125432640587,\n" +
                        "            \"y\":0.8704516013588629\n" +
                        "        },\n" +
                        "        \"c\":{\n" +
                        "            \"x\":0.6395081227434074,\n" +
                        "            \"y\":0.8704516013588629\n" +
                        "        },\n" +
                        "        \"d\":{\n" +
                        "            \"x\":0.6435291904868436,\n" +
                        "            \"y\":0.6514590193610613\n" +
                        "        }\n" +
                        "    },\n" +
                        "    \"device_mapping\":{\n" +
                        "        \"a\":{\n" +
                        "            \"x\":0.9476324751895118,\n" +
                        "            \"y\":0.05416666666666667\n" +
                        "        },\n" +
                        "        \"b\":{\n" +
                        "            \"x\":0.9523199179697514,\n" +
                        "            \"y\":0.9166666666666666\n" +
                        "        },\n" +
                        "        \"c\":{\n" +
                        "            \"x\":0.047643461383528034,\n" +
                        "            \"y\":0.9416666666666667\n" +
                        "        },\n" +
                        "        \"d\":{\n" +
                        "            \"x\":0.045299739993408285,\n" +
                        "            \"y\":0.041666666666666664\n" +
                        "              }";
        if ("analysisMatrix".equals(method)) {
            json += "}";
        } else if ("layoutMapping".equals(method) || "updateLayoutMapping".equals(method)) {
            json += "    },\n" +
                    "    \"matrix\":[\n" +
                    "        0.11883674069475003,\n" +
                    "        -0.004101835356844864,\n" +
                    "        0.6334838431213622,\n" +
                    "        -0.1387884257457738,\n" +
                    "        0.24273284991074442,\n" +
                    "        0.6427397244425025,\n" +
                    "        -0.1671104142576048,\n" +
                    "        0.001438933074163663,\n" +
                    "        1\n" +
                    "    ]\n";
        }

        json += "}";

        return json;
    }

    public String layoutMapping(String deviceId, int layoutId, Case aCase, int step) throws Exception {
        logger.info("\n");
        logger.info("------------------------12、平面映射----------------------------");

        String url = URL_prefix + "/admin/data/layoutMapping/";
        String json = genMappingJson("layoutMapping", deviceId, layoutId);

        String response = postRequest(url, json, header);
        sendResAndReqIdToDb(response, aCase, step);
        checkCode(response, StatusCode.SUCCESS, "平面映射");
        return response;
    }

    public String updateLayoutMapping(String deviceId, int layoutId, Case aCase, int step) throws Exception {
        logger.info("\n");
        logger.info("------------------------13、平面映射编辑----------------------------");

        String url = URL_prefix + "/admin/data/layoutMapping/";
        String json = genMappingJson("updateLayoutMapping", deviceId, layoutId);

        String response = putRequest(url, json, header);
        sendResAndReqIdToDb(response, aCase, step);
        checkCode(response, StatusCode.SUCCESS, "平面映射编辑");
        return response;
    }

    public String getLayoutMapping(String deviceId, int layoutId, Case aCase, int step) throws Exception {
        logger.info("\n");
        logger.info("------------------------14、平面映射详情----------------------------");

        String url = URL_prefix + "/admin/data/layoutMapping/" + deviceId + "/" + layoutId;

        String response = getRequest(url, header);
        sendResAndReqIdToDb(response, aCase, step);
        checkCode(response, StatusCode.SUCCESS, "映射详情");
        return response;
    }

    public String deleteLayoutMapping(String deviceId, int layoutId, Case aCase, int step) throws Exception {
        logger.info("\n");
        logger.info("------------------------15、平面映射删除----------------------------");

        String url = URL_prefix + "/admin/data/layoutMapping/" + deviceId + "/" + layoutId;
        String json = "{}";

        String response = deleteRequest(url, json, header);
        sendResAndReqIdToDb(response, aCase, step);
        checkCode(response, StatusCode.SUCCESS, "平面映射删除");
        return response;
    }

    //    测试模式的时候会调用这个接口
    public String analysisMatrix(String deviceId, int layoutId, Case aCase, int step) throws Exception {
        logger.info("\n");
        logger.info("------------------------15、平面映射矩阵解析----------------------------");

        String url = URL_prefix + "/admin/data/layoutMapping/analysisMatrix";
        String json = genMappingJson("analysisMatrix", deviceId, layoutId);

        String response = postRequest(url, json, header);
        sendResAndReqIdToDb(response, aCase, step);
        checkCode(response, StatusCode.SUCCESS, "平面映射矩阵解析");
        return response;
    }


    @Test
    public void addFloorCheck() throws Exception {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();

        failReason = "";
        Case aCase = new Case();
        int step = 0;

        String caseName = ciCaseName;
        String caseDesc = "验证添加新平面是否成功";
        logger.info(caseDesc + "-----------------------------------------------------------------------------------");

        int layoutId = 100;
        try {
            aCase.setRequestData("1、添加新平面-2、查询平面列表-3、查询平面详情" + "\n\n");
            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);

            String name = ciCaseName;
            String desc = name + "-desc";

            int[] allFloorType = getAllFloorType();
            int floorId = allFloorType[0];

//            1、添加新平面
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            String response = addLayout(name, desc, SHOP_Id, floorId, StatusCode.SUCCESS, aCase, step);
            layoutId = getLayoutId(response);

//            2、平面列表
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            String listLayout = listLayout(SHOP_Id, aCase, step);
            checkLayoutList(listLayout, layoutId, name, desc, floorId);

//            3、平面详情
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            String getLayout = getLayout(layoutId, aCase, step);
            checkGetLayout(getLayout, name, desc, "", floorId);

            aCase.setResult("PASS");
        } catch (AssertionError e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
        } finally {
            deleteLayout(layoutId);
            if (!IS_DEBUG) {
                qaDbUtil.saveToCaseTable(aCase);
            }
        }
    }

    @Test
    public void reAddFloorCheck() throws Exception {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();

        failReason = "";
        Case aCase = new Case();
        int step = 0;

        String caseName = ciCaseName;
        String caseDesc = "验证重复添加新平面是否成功";
        logger.info(caseDesc + "-----------------------------------------------------------------------------------");

        int layoutId = 100;
        try {
            aCase.setRequestData("1、添加新平面-2、添加新平面" + "\n\n");
            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);

            String name = ciCaseName;
            String desc = name + "-desc";

            int[] allFloorType = getAllFloorType();
            int floorId = allFloorType[0];

//            1、添加新平面
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            String response = addLayout(name, desc, SHOP_Id, floorId, StatusCode.SUCCESS, aCase, step);
            layoutId = getLayoutId(response);

//            2、添加新平面
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            String message = "当前楼层已存在:请重新选择";
            response = addLayout(name, desc, SHOP_Id, floorId, StatusCode.UNKNOWN_ERROR, aCase, step);
            checkReAddLayout(response, message);

            aCase.setResult("PASS");
        } catch (AssertionError e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
        } finally {
            deleteLayout(layoutId);
            if (!IS_DEBUG) {
                qaDbUtil.saveToCaseTable(aCase);
            }
        }
    }

    @Test
    public void deleteFloorRegionCheck() throws Exception {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();

        failReason = "";
        Case aCase = new Case();
        int step = 0;

        String caseName = ciCaseName;
        String caseDesc = "验证平面的创建和删除是否会创建删除响应区域";
        logger.info(caseDesc + "-----------------------------------------------------------------------------------");

        int layoutId = 100;
        try {
            aCase.setRequestData("1、添加新平面-2、平面列表-3、区域列表-4、删除平面-5、平面列表-6、区域列表" + "\n\n");
            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);

            String name = ciCaseName;
            String desc = name + "-desc";

            int[] allFloorType = getAllFloorType();
            int floorId = allFloorType[0];

//            1、添加新平面
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            String response = addLayout(name, desc, SHOP_Id, floorId, StatusCode.SUCCESS, aCase, step);
            layoutId = getLayoutId(response);

//            2、平面列表
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            String listLayout = listLayout(SHOP_Id, aCase, step);
            checkLayoutList(listLayout, layoutId, name, desc, floorId);

//            3、区域详情
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            response = listRegion(SHOP_Id, layoutId, aCase, step);
            String regionName = "楼层区域:" + name;
            String layoutType = "DEFAULT";
            checkIsCreateFloorRegion(response, layoutId, regionName, layoutType);

//            4、删除平面
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            deleteLayout(layoutId);

//            5、平面列表
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            response = listLayout(SHOP_Id, aCase, step);
            if (checkLayoutList(response, layoutId, name, desc, floorId)) {
                throw new Exception("删除平面失败！");
            }

//            6、区域列表

//            判断区域是否被删除

            aCase.setResult("PASS");
        } catch (AssertionError e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
        } finally {
            deleteLayout(layoutId);
            if (!IS_DEBUG) {
                qaDbUtil.saveToCaseTable(aCase);
            }
        }
    }

    @Test
    public void updateFloorCheck() throws Exception {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();

        failReason = "";
        Case aCase = new Case();
        int step = 0;

        String caseName = ciCaseName;
        String caseDesc = "验证编辑新平面是否成功";
        logger.info(caseDesc + "-----------------------------------------------------------------------------------");

        int layoutId = 100;
        try {
            aCase.setRequestData("1、添加新平面-2、平面编辑-3、平面列表-4、平面详情" + "\n\n");
            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);

            String name = ciCaseName;
            String desc = name + "-desc";

            int[] allFloorType = getAllFloorType();
            int floorId = allFloorType[0];

//            1、添加新平面
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            String response = addLayout(name, desc, SHOP_Id, floorId, StatusCode.SUCCESS, aCase, step);
            layoutId = getLayoutId(response);

//            2、平面编辑
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            String nameNew = ciCaseName + "-new";
            String descNew = name + "-desc" + "-new";
            String layoutPic = layoutPicUpload();
            updateLayout(layoutId, nameNew, descNew, layoutPic, floorId, aCase, step);

//            3、平面列表
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            String listLayout = listLayout(SHOP_Id, aCase, step);
            checkLayoutList(listLayout, layoutId, nameNew, descNew, floorId);

//            4、平面详情
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            String getLayout = getLayout(layoutId, aCase, step);
            checkGetLayout(getLayout, nameNew, descNew, layoutPic, floorId);

            aCase.setResult("PASS");
        } catch (AssertionError e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
        } finally {
            deleteLayout(layoutId);
            if (!IS_DEBUG) {
                qaDbUtil.saveToCaseTable(aCase);
            }
        }
    }

    @Test
    public void layoutPicCheck() throws Exception {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();

        failReason = "";
        Case aCase = new Case();
        int step = 0;

        String caseName = ciCaseName;
        String caseDesc = "验证添加/删除平面图";
        logger.info(caseDesc + "-----------------------------------------------------------------------------------");

        int layoutId = 100;
        try {
            aCase.setRequestData("1、添加新平面-2、平面图片上传-3、平面详情-4、平面图片删除-5、平面详情" + "\n\n");
            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);

            String name = ciCaseName;
            String desc = name + "-desc";

            int[] allFloorType = getAllFloorType();
            int floorId = allFloorType[0];

//            1、添加新平面
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            String response = addLayout(name, desc, SHOP_Id, floorId, StatusCode.SUCCESS, aCase, step);
            layoutId = getLayoutId(response);

//            2、平面图片上传（编辑平面）
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            String nameNew = ciCaseName + "-new";
            String descNew = name + "-desc" + "-new";
            String layoutPic = layoutPicUpload();
            updateLayout(layoutId, nameNew, descNew, layoutPic, floorId, aCase, step);

//            3、平面详情
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            String getLayout = getLayout(layoutId, aCase, step);
            checkGetLayout(getLayout, nameNew, descNew, layoutPic, floorId);

//            4、平面图片删除
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            deleteLayoutPic(layoutId, aCase, step);

//            5、平面详情
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            getLayout = getLayout(layoutId, aCase, step);
            checkGetLayout(getLayout, nameNew, descNew, "", floorId);

            aCase.setResult("PASS");
        } catch (AssertionError e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
        } finally {
            deleteLayout(layoutId);
            if (!IS_DEBUG) {
                qaDbUtil.saveToCaseTable(aCase);
            }
        }
    }

    @Test
    public void floorDeviceCheck() {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();

        failReason = "";
        Case aCase = new Case();
        int step = 0;

        String caseName = ciCaseName;
        String caseDesc = "验证新增/删除平面设备";
        logger.info(caseDesc + "-----------------------------------------------------------------------------------");

        try {
            aCase.setRequestData("1,2、平面设备新增-3,4、平面所属设备列表-5,6、平面设备删除-7,8平面所属设备列表" + "\n\n");
            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);

//            1,2、平面设备新增
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            addLayoutDevice(LAYOUT_ID, DEVICE_ID_1, aCase, step);
            addLayoutDevice(LAYOUT_ID, DEVICE_ID_2, aCase, step);

//            3,4、平面所属设备列表
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            String response = listLayoutDevice(LAYOUT_ID, aCase, step);
            checkLayoutDevice(response, DEVICE_ID_1, true);
            checkLayoutDevice(response, DEVICE_ID_2, true);

//            5,6、平面设备删除
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            deleteLayoutDevice(LAYOUT_ID, DEVICE_ID_1, aCase, step);
            deleteLayoutDevice(LAYOUT_ID, DEVICE_ID_2, aCase, step);

//            7,8、平面所属设备列表
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            response = listLayoutDevice(LAYOUT_ID, aCase, step);
            checkLayoutDevice(response, DEVICE_ID_1, false);
            checkLayoutDevice(response, DEVICE_ID_2, false);

            aCase.setResult("PASS");
        } catch (AssertionError e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
        } finally {
            if (!IS_DEBUG) {
                qaDbUtil.saveToCaseTable(aCase);
            }
        }
    }

    @Test
    public void layoutMapping() {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();

        failReason = "";
        Case aCase = new Case();
        int step = 0;

        String caseName = ciCaseName;
        String caseDesc = "验证平面映射";
        logger.info(caseDesc + "-----------------------------------------------------------------------------------");

        try {
            aCase.setRequestData("1、平面映射矩阵解析-2、平面映射-3、平面映射详情-4、平面映射编辑-5、平面映射矩阵解析-6、平面映射详情" +
                    "-7、删除平面映射-8、平面映射详情" + "\n\n");
            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);

//            1、平面映射矩阵解析
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            analysisMatrix(DEVICE_ID_MAPPING, LAYOUT_ID_MAPPING, aCase, step);

//            2、平面映射
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            layoutMapping(DEVICE_ID_MAPPING, LAYOUT_ID_MAPPING, aCase, step);

//            3、平面映射详情
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            String response = getLayoutMapping(DEVICE_ID_MAPPING, LAYOUT_ID_MAPPING, aCase, step);
            checkLayoutMapping(response, true);

//            4、平面映射编辑
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            updateLayoutMapping(DEVICE_ID_MAPPING, LAYOUT_ID_MAPPING, aCase, step);

//            5、平面映射矩阵解析
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            analysisMatrix(DEVICE_ID_MAPPING, LAYOUT_ID_MAPPING, aCase, step);

//            6、平面映射详情
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            response = getLayoutMapping(DEVICE_ID_MAPPING, LAYOUT_ID_MAPPING, aCase, step);
            checkLayoutMapping(response, true);

//            7、平面映射删除
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            deleteLayoutMapping(DEVICE_ID_MAPPING, LAYOUT_ID_MAPPING, aCase, step);

//            8、平面映射详情
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            response = getLayoutMapping(DEVICE_ID_MAPPING, LAYOUT_ID_MAPPING, aCase, step);
            checkLayoutMapping(response, false);

            aCase.setResult("PASS");
        } catch (AssertionError e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
        } finally {
            if (!IS_DEBUG) {
                qaDbUtil.saveToCaseTable(aCase);
            }
        }
    }

    @Test
    public void listLayoutDSCheck() throws Exception {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();

        failReason = "";
        Case aCase = new Case();
        int step = 0;

        String caseName = ciCaseName;
        String caseDesc = "验证添加/删除平面图";
        logger.info(caseDesc + "-----------------------------------------------------------------------------------");

        try {
            aCase.setRequestData("1、平面列表" + "\n\n");
            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);

            String subjectId = SHOP_Id;
            int layoutId = LAYOUT_ID_MAPPING;
            String floorName = "L9";
            String LayoutName = "平面映射【勿动】";
            long createDate = 1571189046000L;
            String createor_name = "廖祥茹";

//            1、平面列表
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            String listLayout = listLayout(subjectId, aCase, step);
            checkListLayoutDS(listLayout, subjectId, layoutId, floorName, LayoutName, createDate, createor_name);

            aCase.setResult("PASS");
        } catch (AssertionError e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
        } finally {
            if (!IS_DEBUG) {
                qaDbUtil.saveToCaseTable(aCase);
            }
        }
    }

    private void checkListLayoutDS(String response, String subjectId, int layoutId, String floorName, String layoutName,
                                   long createDate, String creator_name) throws Exception {

        JSONObject data = JSON.parseObject(response).getJSONObject("data");
        JSONArray list = data.getJSONArray("list");
        String function = "获取楼层列表";

        boolean isExist = false;
        for (int i = 0; i < list.size(); i++) {
            JSONObject singleList = list.getJSONObject(i);

            int layoutIdRes = singleList.getInteger("layout_id");
            if (layoutIdRes == layoutId) {
                isExist = true;
                checkKeyValue(function, singleList, "subject_id", subjectId, true);
                checkKeyValue(function, singleList, "floor_name", floorName, true);
                checkKeyValue(function, singleList, "name", layoutName, true);
                checkKeyValue(function, singleList, "gmt_create", createDate, true);
                checkKeyKeyValue(function, singleList, "creator_name", creator_name, true);

            }
        }

        Assert.assertEquals(isExist, true, "不存在该楼层！");

    }

    private void checkKeyKeyValue(String function, JSONObject jo, String key, String value, boolean expectExactValue) throws Exception {

        String[] keys = key.split("_");
        String parentKey = keys[0];
        String childKey = keys[1];

        if (!jo.containsKey(parentKey)) {
            throw new Exception(function + "---没有返回" + parentKey + "字段！");
        }

        JSONObject values = jo.getJSONObject(parentKey);
        if (!values.containsKey(childKey)) {
            throw new Exception(function + "---没有返回" + key + "字段！");
        }

        String valueRes = values.getString(childKey);
        Assert.assertEquals(valueRes, value, key + "字段值不相符：列表返回：" + valueRes + ", 实际：" + value);
    }

    private void checkKeyValue(String function, JSONObject jo, String key, String value, boolean expectExactValue) throws Exception {

        if (!jo.containsKey(key)) {
            throw new Exception(function + "---没有返回" + key + "字段！");
        }

        String valueRes = jo.getString(key);

        if (expectExactValue) {
            Assert.assertEquals(valueRes, value, key + "字段值不相符：列表返回：" + valueRes + ", 实际：" + value);
        } else {
            if (valueRes == null || "".equals(valueRes)) {
                throw new Exception(function + "---" + key + "字段值为空！");
            }
        }
    }

    private void checkKeyValue(String function, JSONObject jo, String key, int value, boolean expectExactValue) throws Exception {

        if (!jo.containsKey(key)) {
            throw new Exception(function + "---没有返回" + key + "字段！");
        }

        int valueRes = jo.getInteger(key);

        if (expectExactValue) {
            Assert.assertEquals(valueRes, value, key + "字段值不相符：列表返回：" + valueRes + ", 实际：" + value);
        }
    }

    private void checkKeyValue(String function, JSONObject jo, String key, boolean value, boolean expectExactValue) throws Exception {

        if (!jo.containsKey(key)) {
            throw new Exception(function + "---没有返回" + key + "字段！");
        }

        boolean valueRes = jo.getBoolean(key);

        if (expectExactValue) {
            Assert.assertEquals(valueRes, value, key + "字段值不相符：列表返回：" + valueRes + ", 实际：" + value);
        }
    }

    private void checkKeyValue(String function, JSONObject jo, String key, long value, boolean expectExactValue) throws Exception {

        if (!jo.containsKey(key)) {
            throw new Exception(function + "---没有返回" + key + "字段！");
        }

        long valueRes = jo.getLong(key);

        if (expectExactValue) {
            Assert.assertEquals(valueRes, value, key + "字段值不相符：列表返回：" + valueRes + ", 实际：" + value);
        }
    }

    private void checkLayoutMapping(String response, boolean isMapping) throws Exception {

        JSONObject data = JSON.parseObject(response).getJSONObject("data");
        boolean isMappingRes = data.getBoolean("is_mapping");
        Assert.assertEquals(isMappingRes, isMapping, "映射状态与映射详情中状态不符，是否映射：" + isMapping + ",映射详情返回：" + isMappingRes);

        JSONObject deviceMapping = data.getJSONObject("device_mapping");
        if (isMapping) {
            if (!(deviceMapping.containsKey("a") && deviceMapping.containsKey("b") && deviceMapping.containsKey("c") && deviceMapping.containsKey("d"))) {
                throw new Exception("映射后，设备上没有包含四个坐标点！");
            }
        } else {
            if (!(deviceMapping == null || deviceMapping.size() == 0)) {
                throw new Exception("删除映射后device_mapping没有清空！");
            }
        }

        JSONArray layoutMatrix = data.getJSONArray("layout_matrix");
        if (isMapping) {
            if (layoutMatrix == null || layoutMatrix.size() != 9) {
                throw new Exception("画完映射后，layout_matrix为null或是没有设显示9个点！");
            }
        } else {
            if (!(layoutMatrix == null || layoutMatrix.size() == 0)) {
                throw new Exception("删除映射后，layout_matrix没有置空！");
            }
        }

        JSONObject deviceLocation = data.getJSONObject("device_location");
        if (isMapping) {
            if (!(deviceLocation.containsKey("x") && deviceLocation.containsKey("y") && deviceLocation.containsKey("degree"))) {
                throw new Exception("映射后，设备坐标未显示");
            }
        } else {
            if (!(deviceLocation == null || deviceLocation.size() == 0)) {
                throw new Exception("删除映射后，device_location没有清空！");
            }
        }

        JSONObject layoutMapping = data.getJSONObject("layout_mapping");
        if (isMapping) {
            if (!(layoutMapping.containsKey("a") && layoutMapping.containsKey("b") && layoutMapping.containsKey("c") && layoutMapping.containsKey("d"))) {
                throw new Exception("映射后，平面图上没有包含四个坐标点！");
            }
        } else {
            if (!(layoutMatrix == null || layoutMatrix.size() == 0)) {
                throw new Exception("删除映射后，layout_matrix没有清空！");
            }
        }
    }

    private void checkLayoutDevice(String response, String deviceId, boolean isExist) {
        JSONObject data = JSON.parseObject(response).getJSONObject("data");
        JSONArray list = data.getJSONArray("list");

        boolean isExistRes = false;
        for (int i = 0; i < list.size(); i++) {
            JSONObject singleList = list.getJSONObject(i);
            String deviceIdRes = singleList.getString("device_id");
            if (deviceIdRes.equals(deviceId)) {
                isExistRes = true;
            }
        }

        Assert.assertEquals(isExistRes, isExist, "平面所属设备列表，是否期待有该设备：" + isExist + ", 实际上：" + isExistRes);
    }

    private void checkIsCreateFloorRegion(String response, int layoutId, String regionName, String layoutType) throws Exception {
        JSONObject data = JSON.parseObject(response).getJSONObject("data");
        JSONArray list = data.getJSONArray("list");
        JSONObject region = list.getJSONObject(0);
        if (!region.containsKey("layout_id")) {
            throw new Exception("新建平面后，没有创建对应区域!");
        }
        int layoutIdRes = region.getInteger("layout_id");
        if (layoutIdRes != layoutId) {
            throw new Exception("新建平面后，没有创建对应区域。");
        }


        String regionNameRes = region.getString("region_name");
        Assert.assertEquals(regionNameRes, regionName, "新建的楼层区域名称不符合规范--楼层区域：平面名称");


    }

    private void checkReAddLayout(String response, String message) {
        JSONObject resJo = JSON.parseObject(response);
        String messageRes = resJo.getString("message");
        Assert.assertEquals(messageRes, message,
                "重复创建新平面的报错信息与期待不相符，实际：" + messageRes + ",期待：" + message);
    }

    private void checkGetLayout(String response, String name, String desc, String layoutPic, int floorId) throws Exception {

        JSONObject data = JSON.parseObject(response).getJSONObject("data");

        String nameRes = data.getString("name");
        Assert.assertEquals(nameRes, name, "详情的name与注册的name不一致。注册：" + name + ", 详情：" + nameRes);

        String descRes = data.getString("description");
        Assert.assertEquals(descRes, desc, "详情的desc与注册的desc不一致。注册：" + desc + ", 详情：" + descRes);

        int floorIdRes = data.getInteger("floor_id");
        Assert.assertEquals(floorIdRes, floorId, "详情的floor_id与注册的floor_id不一致。注册：" + floorId + ", 详情：" + floorIdRes);

        String layoutPicRes = data.getString("layout_pic_oss");
        if (!"".equals(layoutPic)) {
            if (!layoutPicRes.contains(layoutPic)) {
                throw new Exception("平面详情返回的平面图不正确，实际：" + layoutPicRes + ",期待：" + layoutPic);
            }
        } else {
            if (layoutPicRes != null) {
                throw new Exception("平面详情--删除平面图后，平面图仍存在-" + layoutPicRes);
            }
        }
    }

    private boolean checkLayoutList(String response, int layoutId, String name, String desc, int floorId) throws Exception {

        JSONObject data = JSON.parseObject(response).getJSONObject("data");
        JSONArray list = data.getJSONArray("list");

        boolean isExist = false;

        for (int i = 0; i < list.size(); i++) {

            JSONObject singleList = list.getJSONObject(i);
            int layoutIdRes = singleList.getInteger("layout_id");
            if (layoutId == layoutIdRes) {
                isExist = true;
                String nameRes = singleList.getString("name");
                if (!name.equals(nameRes)) {
                    throw new Exception("返回的name与注册name不一样。注册：" + name + ",列表返回：" + nameRes);
                }
                String descRes = singleList.getString("description");
                if (!desc.equals(descRes)) {
                    throw new Exception("返回的desc与注册desc不一样。注册：" + desc + ",列表返回：" + descRes);
                }
                int floorIdRes = singleList.getInteger("floor_id");
                if (floorId != floorIdRes) {
                    throw new Exception("返回的floor与注册的floor不一样。注册：" + floorId + ",列表返回：" + floorIdRes);
                }
            }
        }

        return isExist;
    }

    private int getLayoutId(String response) {
        JSONObject data = JSON.parseObject(response).getJSONObject("data");
        int layoutId = data.getInteger("layout_id");

        return layoutId;
    }

    private int[] getAllFloorType() throws Exception {
        String floorList = enumFloorList();

        JSONObject data = JSON.parseObject(floorList).getJSONObject("data");
        JSONArray list = data.getJSONArray("list");

        if (list == null || list.size() == 0) {
            throw new Exception("楼层类型列表为空！");
        }

        int[] flooList = new int[list.size()];

        for (int i = 0; i < list.size(); i++) {
            JSONObject singleList = list.getJSONObject(i);
            int floorId = singleList.getInteger("floor_id");
            flooList[i] = floorId;
        }

        Arrays.sort(flooList);

        return flooList;
    }

    //    ---------------------------------------------区域模块-------------------------------------------------

    //    1、创建新区域
    public String addRegion(String regionName, String regionType, int layoutId, Case aCase, int step) throws Exception {
        String url = URL_prefix + "/admin/data/region/";

        String json =
                "{\n" +
                        "    \"region_name\":\"" + regionName + "\",\n" +
                        "    \"region_type\":\"" + regionType + "\",\n" +
                        "    \"layout_id\":" + layoutId + ",\n" +
                        "    \"subject_id\":\"" + SHOP_Id + "\"\n" +
                        "}";

        String response = postRequest(url, json, header);
        sendResAndReqIdToDb(response, aCase, step);
        checkCode(response, StatusCode.SUCCESS, "创建新区域");
        return response;
    }

    //    2、删除区域
    public String deleteRegion(String regionId, Case aCase, int step) throws Exception {
        String url = URL_prefix + "/admin/data/region/" + regionId;

        String json =
                "{}";

        String response = deleteRequest(url, json, header);
        sendResAndReqIdToDb(response, aCase, step);
        checkCode(response, StatusCode.SUCCESS, "创建新区域");
        return response;
    }

    public String deleteRegion(String regionId) throws Exception {
        String url = URL_prefix + "/admin/data/region/" + regionId;

        String json =
                "{}";

        String response = deleteRequest(url, json, header);
        checkCode(response, StatusCode.SUCCESS, "创建新区域");
        return response;
    }

    //    3、编辑区域
    public String updateRegion(String regionId, String regionName, boolean ifLayoutDraw, Case aCase, int step) throws Exception {
        String url = URL_prefix + "/admin/data/region/" + regionId;

        String json =
                "{\n" +
                        "    \"region_name\":\"" + regionName;
        if (!ifLayoutDraw) {
            json += "\"";
        } else {
            json += "\",\n";
            json += "    \"region_location\":[\n" +
                    "        {\n" +
                    "            \"x\":0.630831565063552,\n" +
                    "            \"y\":0.6728163489787377\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"x\":0.8757361025238644,\n" +
                    "            \"y\":0.6651231437494662\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"x\":0.8732539619414964,\n" +
                    "            \"y\":0.8673445383474563\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"x\":0.6564803510813549,\n" +
                    "            \"y\":0.8376707467488382\n" +
                    "        }\n" +
                    "    ]\n";
        }

        json += "}";

        String response = putRequest(url, json, header);
        sendResAndReqIdToDb(response, aCase, step);
        checkCode(response, StatusCode.SUCCESS, "编辑区域");
        return response;
    }

    //    4、区域详情
    public String getRegion(String regionId, Case aCase, int step) throws Exception {
        String url = URL_prefix + "/admin/data/region/" + regionId;

        String response = getRequest(url, header);
        sendResAndReqIdToDb(response, aCase, step);
        checkCode(response, StatusCode.SUCCESS, "区域详情");
        return response;
    }

    //    5、区域列表
    public String listRegion(String subjectId, int layoutId, Case aCase, int step) throws Exception {
        String url = URL_prefix + "/admin/data/region/list";

        String json =
                "{\n" +
                        "    \"subject_id\":\"" + subjectId + "\",\n" +
                        "    \"layout_id\":\"" + layoutId + "\",\n" +
                        "    \"page\":1,\n" +
                        "    \"size\":50\n" +
                        "}";


        String response = postRequest(url, json, header);
        sendResAndReqIdToDb(response, aCase, step);
        checkCode(response, StatusCode.SUCCESS, "区域列表");
        return response;
    }

    //    6、新增区域设备
    public String addRegionDevice(String regionId, String deviceId, Case aCase, int step) throws Exception {
        String url = URL_prefix + "/admin/data/regionDevice/";

        String json =
                "{\n" +
                        "    \"region_id\":" + regionId + ",\n" +
                        "    \"device_id\":\"" + deviceId + "\"\n" +
                        "}";


        String response = postRequest(url, json, header);
        sendResAndReqIdToDb(response, aCase, step);
        checkCode(response, StatusCode.SUCCESS, "区域列表");
        return response;
    }

    //    7、区域设备删除
    public String deleteRegionDevice(String regionId, String deviceId, Case aCase, int step) throws Exception {
        String url = URL_prefix + "/admin/data/regionDevice/" + regionId + "/" + deviceId;

        String json =
                "{}";

        String response = deleteRequest(url, json, header);
        sendResAndReqIdToDb(response, aCase, step);
        checkCode(response, StatusCode.SUCCESS, "区域设备删除");
        return response;
    }

    //    8、区域所属设备列表
    public String listRegionDevice(String regionId, Case aCase, int step) throws Exception {
        String url = URL_prefix + "/admin/data/regionDevice/list";

        String json =
                "{\n" +
                        "    \"region_id\":" + regionId + "\n" +
                        "}";

        String response = postRequest(url, json, header);
        sendResAndReqIdToDb(response, aCase, step);
        checkCode(response, StatusCode.SUCCESS, "区域所属设备列表");
        return response;
    }

    //    9、区域类型
    public String enumRegionType() throws Exception {
        String url = URL_prefix + "/admin/data/region/regionEnum/list";

        String response = getRequest(url, header);
        checkCode(response, StatusCode.SUCCESS, "区域类型");
        return response;
    }

    private String[] getAllRegionType() throws Exception {
        String response = enumRegionType();
        JSONObject data = JSON.parseObject(response).getJSONObject("data");
        JSONArray list = data.getJSONArray("list");

        String[] regionTypes = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            JSONObject singleList = list.getJSONObject(i);
            String regionType = singleList.getString("region_type");
            regionTypes[i] = regionType;
        }

        return regionTypes;
    }

    @Test
    public void addRegionCheck() throws Exception {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        failReason = "";
        Case aCase = new Case();
        int step = 0;

        String caseName = ciCaseName;
        String caseDesc = "验证新增区域是否成功";
        logger.info(caseDesc + "-----------------------------------------------------------------------------------");

        String regionId = "";

        try {
            aCase.setRequestData("1、新增区域-2、区域列表-3、区域详情" + "\n\n");
            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);

            String regionName = caseName;
            String regionType = regionTypeGeneral;

//            1、新增区域
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            String response = addRegion(regionName, regionType, LAYOUT_ID, aCase, step);
            regionId = getRegionId(response);

//            2、区域列表
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            response = listRegion(SHOP_Id, LAYOUT_ID, aCase, step);
            checkListRegion(response, regionId, regionName, true);

//            3、区域详情
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            response = getRegion(regionId, aCase, step);
            checkgetRegion(response, regionName, false);

            aCase.setResult("PASS");
        } catch (AssertionError e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
        } finally {
            deleteRegion(regionId);
            if (!IS_DEBUG) {
                qaDbUtil.saveToCaseTable(aCase);
            }
        }
    }

    @Test
    public void deleteRegionCheck() throws Exception {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        failReason = "";
        Case aCase = new Case();
        int step = 0;

        String caseName = ciCaseName;
        String caseDesc = "验证删除区域是否成功";
        logger.info(caseDesc + "-----------------------------------------------------------------------------------");

        try {
            aCase.setRequestData("1、新增区域-2、区域列表-3、删除区域-4、区域列表" + "\n\n");
            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);

            String regionName = caseName;
            String regionType = regionTypeGeneral;

//            1、新增区域
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            String response = addRegion(regionName, regionType, LAYOUT_ID, aCase, step);
            String regionId = getRegionId(response);

//            2、区域列表
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            response = listRegion(SHOP_Id, LAYOUT_ID, aCase, step);
            checkListRegion(response, regionId, regionName, true);

//            3、区域删除
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            deleteRegion(regionId);

//            4、区域列表
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            response = listRegion(SHOP_Id, LAYOUT_ID, aCase, step);
            checkListRegion(response, regionId, regionName, false);

            aCase.setResult("PASS");
        } catch (AssertionError e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
        } finally {
            if (!IS_DEBUG) {
                qaDbUtil.saveToCaseTable(aCase);
            }
        }
    }

    @Test
    public void updateRegionCheck() throws Exception {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        failReason = "";
        Case aCase = new Case();
        int step = 0;

        String caseName = ciCaseName;
        String caseDesc = "验证更新区域是否成功";
        logger.info(caseDesc + "-----------------------------------------------------------------------------------");

        String regionId = "";

        try {
            aCase.setRequestData("1、新增区域-2、区域编辑-3、区域列表-4、区域详情" + "\n\n");
            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);

            String regionName = caseName;
            String regionType = regionTypeGeneral;

//            1、新增区域
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            String response = addRegion(regionName, regionType, LAYOUT_ID, aCase, step);
            regionId = getRegionId(response);

//            2、区域编辑
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            updateRegion(regionId, regionName, false, aCase, step);

//            3、区域列表
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            response = listRegion(SHOP_Id, LAYOUT_ID, aCase, step);
            checkListRegion(response, regionId, regionName, true);

//            4、区域详情
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            response = getRegion(regionId, aCase, step);
            checkgetRegion(response, regionName, false);

            aCase.setResult("PASS");
        } catch (AssertionError e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
        } finally {
            deleteRegion(regionId);
            if (!IS_DEBUG) {
                qaDbUtil.saveToCaseTable(aCase);
            }
        }
    }

    @Test
    public void regionDrawCheck() throws Exception {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        failReason = "";
        Case aCase = new Case();
        int step = 0;

        String caseName = ciCaseName;
        String caseDesc = "验证绘制/删除区域是否成功";
        logger.info(caseDesc + "-----------------------------------------------------------------------------------");

        String regionId = "";

        try {
            aCase.setRequestData("1、新增区域-2、区域编辑-3、区域详情-4、编辑区域-5、区域详情" + "\n\n");
            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);

            String regionName = caseName;
            String regionType = regionTypeGeneral;

//            1、新增区域
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            String response = addRegion(regionName, regionType, LAYOUT_ID, aCase, step);
            regionId = getRegionId(response);

//            2、区域编辑
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            updateRegion(regionId, regionName, true, aCase, step);

//            3、区域详情
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            response = getRegion(regionId, aCase, step);
            checkgetRegion(response, regionName, true);

//            4、区域编辑
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            updateRegion(regionId, regionName, false, aCase, step);

//            5、区域详情
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            response = getRegion(regionId, aCase, step);
            checkgetRegion(response, regionName, false);

            aCase.setResult("PASS");
        } catch (AssertionError e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
        } finally {
            deleteRegion(regionId);
            if (!IS_DEBUG) {
                qaDbUtil.saveToCaseTable(aCase);
            }
        }
    }

    @Test
    public void regionDeviceDisplayCheck() {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        failReason = "";
        Case aCase = new Case();
        int step = 0;

        String caseName = ciCaseName;
        String caseDesc = "验证区域详情-设备列表-平面图是否显示设备位置";
        logger.info(caseDesc + "-----------------------------------------------------------------------------------");

        try {
            aCase.setRequestData("1、新增区域设备-2、区域设备列表-3、平面设备列表" + "\n\n");
            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);

//            1、新增区域设备
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            addRegionDevice(REGION_ID, REGION_DEVICE_1, aCase, step);

//            2、区域设备列表
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            String response = listRegionDevice(REGION_ID, aCase, step);
            checkRegionDeviceLocation(response, REGION_DEVICE_1);

//            3、平面设备列表
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            response = listLayoutDevice(LAYOUT_ID, aCase, step);
            checkLayoutDeviceLocation(response, REGION_DEVICE_1);
            checkLayoutDeviceLocation(response, REGION_DEVICE_2);

            aCase.setResult("PASS");
        } catch (AssertionError e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
        } finally {
            if (!IS_DEBUG) {
                qaDbUtil.saveToCaseTable(aCase);
            }
        }
    }

    private void checkRegionDeviceLocation(String response, String deviceId) throws Exception {
        JSONObject data = JSON.parseObject(response).getJSONObject("data");

        JSONArray list = data.getJSONArray("list");

        boolean isExist = false;

        for (int i = 0; i < list.size(); i++) {
            JSONObject regionDevice = list.getJSONObject(i);
            String deviceIdRes = regionDevice.getString("device_id");
            if (deviceId.equals(deviceIdRes)) {
                isExist = true;
                JSONObject deviceLocation = regionDevice.getJSONObject("device_location");
                if (deviceLocation == null || deviceLocation.size() == 0) {
                    throw new Exception("区域设备位置为空！");
                }
            }
        }

        if (!isExist) {
            throw new Exception("该区域设备不存在！");
        }
    }

    private void checkLayoutDeviceLocation(String response, String deviceId) throws Exception {
        JSONObject data = JSON.parseObject(response).getJSONObject("data");

        JSONArray list = data.getJSONArray("list");

        boolean isExist = false;

        for (int i = 0; i < list.size(); i++) {
            JSONObject regionDevice = list.getJSONObject(i);
            String deviceIdRes = regionDevice.getString("device_id");
            if (deviceId.equals(deviceIdRes)) {
                isExist = true;
                JSONObject deviceLocation = regionDevice.getJSONObject("device_location");
                if (deviceLocation == null || deviceLocation.size() == 0) {
                    throw new Exception("平面设备位置为空！");
                }
            }
        }

        if (!isExist) {
            throw new Exception("该平面设备不存在！");
        }
    }

    private void checkgetRegion(String response, String regionName, boolean isMapping) throws Exception {
        JSONObject data = JSON.parseObject(response).getJSONObject("data");

        String regionNameRes = data.getString("region_name");
        Assert.assertEquals(regionNameRes, regionName, "区域详情--region_name与期待不相符，期待：" + regionName + ",实际：" + regionNameRes);

        JSONArray regionLocation = data.getJSONArray("region_location");
        if (isMapping) {
            if (regionLocation == null || regionLocation.size() <= 2) {
                throw new Exception("区域详情--映射后，region_location为空或少于两个点！");
            }
        } else {
            if (!(regionLocation == null || regionLocation.size() == 0)) {
                throw new Exception("区域详情--删除映射后，region_location信息不为空！");
            }
        }
    }

    private String getRegionId(String response) {
        JSONObject data = JSON.parseObject(response).getJSONObject("data");
        String regionId = data.getString("region_id");

        return regionId;
    }

    private void checkListRegion(String response, String regionId, String regionName, boolean isExist) {
        JSONObject data = JSON.parseObject(response).getJSONObject("data");

        JSONArray list = data.getJSONArray("list");

        boolean isExistRes = false;

        for (int i = 0; i < list.size(); i++) {
            JSONObject singleList = list.getJSONObject(i);
            String regionIdRes = singleList.getString("region_id");

            if (regionId.equals(regionIdRes)) {
                isExistRes = true;
                String regionNameRes = singleList.getString("region_name");
                Assert.assertEquals(regionNameRes, regionName, "区域列表--区域名称不相符，期待：" + regionName + ", 实际：" + regionNameRes);
            }
        }

        Assert.assertEquals(isExistRes, isExist, "是否期待该区域存在，期待：" + isExist + ", 实际：" + isExistRes);

    }


//    ------------------------------------------主体管理模块----------------------------------------

    public String addSubject(String subjectName, int subjectTypeId, Case aCase, int step) throws Exception {
        String url = URL_prefix + "/admin/data/subject/";
        String json =
                "{\n" +
                        "    \"brand_id\":\"" + BRAND_ID + "\",\n" +
                        "    \"subject_type\":" + subjectTypeId + ",\n" +
                        "    \"subject_name\":\"" + subjectName + "\",\n" +
                        "    \"region\":{\n" +
                        "        \"country\":\"中国\",\n" +
                        "        \"area\":\"华北区\",\n" +
                        "        \"province\":\"北京市\",\n" +
                        "        \"city\":\"北京市\",\n" +
                        "        \"district\":\"海淀区\"\n" +
                        "    },\n" +
                        "    \"local\":\"中关村soho\",\n" +
                        "    \"manager\":\"娜乌西卡\",\n" +
                        "    \"telephone\":\"17666666666\"\n" +
                        "}";

        String response = postRequest(url, json, header);
        sendResAndReqIdToDb(response, aCase, step);
        checkCode(response, StatusCode.SUCCESS, "");
        return response;
    }

    public String deleteSubject(String subjectId) throws Exception {
        String url = URL_prefix + "/admin/data/subject/" + subjectId;
        String json = "{}";

        String response = deleteRequest(url, json, header);
        return response;
    }

    public String updateSubject(String subjectId, String subjectName, String country, String area, String province,
                                String city, String district, String local, String manager, String phone) throws Exception {
        String url = URL_prefix + "/admin/data/subject/" + subjectId;
        String json =
                "{\n" +
                        "    \"subject_name\":\"" + subjectName + "\",\n" +
                        "    \"region\":{\n" +
                        "        \"country\":\"" + country + "\",\n" +
                        "        \"area\":\"" + area + "\",\n" +
                        "        \"province\":\"" + province + "\",\n" +
                        "        \"city\":\"" + city + "\",\n" +
                        "        \"district\":\"" + district + "\"\n" +
                        "    },\n" +
                        "    \"local\":\"" + local + "\",\n" +
                        "    \"manager\":\"" + manager + "\",\n" +
                        "    \"telephone\":\"" + phone + "\"\n" +
                        "}";

        String response = putRequest(url, json, header);
        return response;
    }

    public String detailSubject(String subjectId) throws Exception {
        String url = URL_prefix + "/admin/data/subject/" + subjectId;

        String response = getRequest(url, header);

        return response;
    }

    public String listSubject() throws Exception {
        String url = URL_prefix + "/admin/data/subject/list";
        String json =
                "{\n" +
                        "    \"app_id\":\"" + APP_ID + "\",\n" +
                        "    \"brand_id\":\"" + BRAND_ID + "\",\n" +
                        "    \"page\":1,\n" +
                        "    \"size\":10\n" +
                        "}";

        String response = postRequest(url, json, header);
        return response;
    }

    //    查询业务节点是否有绑定集群
//    即已绑定机器列表
    public String bindList(String nodeId) throws Exception {
        String url = URL_prefix + "/admin/cluster/bind/list";
        String json =
                "{\n" +
                        "    \"node_id\":\"" + nodeId + "\",\n" +
                        "    \"page\":1,\n" +
                        "    \"size\":10\n" +
                        "}";

        String response = postRequest(url, json, header);
        return response;
    }

    //    根据业务节点，获取可绑定机器列表
    public String unbindList(String nodeId) throws Exception {
        String url = URL_prefix + "/admin/cluster/node/un_bind/list";
        String json =
                "{\n" +
                        "    \"node_id\":\"" + nodeId + "\",\n" +
                        "    \"page\":1,\n" +
                        "    \"size\":10\n" +
                        "}";

        String response = postRequest(url, json, header);
        return response;
    }

    //    业务节点关联机器，即绑定机器
    public String bindNode(String nodeId, int clusterNodeId, String alias) throws Exception {
        String url = URL_prefix + "/admin/cluster/node/bind";
        String json =
                "{\n" +
                        "    \"node_id\":\"" + nodeId + "\",\n" +
                        "    \"cluster_node_id\":" + clusterNodeId + ",\n" +
                        "    \"alias\":\"" + alias + "\"\n" +
                        "}";

        String response = postRequest(url, json, header);
        return response;
    }

    //    业务节点关联机器，即解绑定机器
    public String removeNode(String nodeId, int clusterNodeId) throws Exception {
        String url = URL_prefix + "/admin/cluster/node/bind";
        String json =
                "{\n" +
                        "    \"node_id\":\"" + nodeId + "\",\n" +
                        "    \"cluster_node_id\":" + clusterNodeId + "\n" +
                        "}";

        String response = postRequest(url, json, header);
        return response;
    }


    public String enumNodeType() throws Exception {
        String url = URL_prefix + "/admin/data/nodeType/list";
        String json =
                "{\n" +
                        "    \"page\":1,\n" +
                        "    \"size\":10\n" +
                        "}";

        String response = postRequest(url, json, header);
        return response;
    }

    //    增加节点对应服务&配置
    public String nodeServiceConfig(int serviceId, String nodeId) throws Exception {
        String url = URL_prefix + "/admin/data/nodeServiceConfig/";
        String json =
                "{\n" +
                        "    \"service_id\":" + serviceId + ",\n" +
                        "    \"node_id\":\"" + nodeId + "\",\n" +
                        "    \"cloud_config\":{\n" +
                        "\n" +
                        "    }\n" +
                        "}";

        String response = postRequest(url, json, header);
        return response;
    }

    public String updateConfig(int serviceId, boolean autoMerge, boolean autoIncMerge, String nodeId, String grpName,
                               String type, String mode) throws Exception {
        String url = URL_prefix + "/admin/data/nodeServiceConfig/" + serviceId;
        String json =
                "{\n" +
                        "    \"cloud_config\":{\n" +
                        "        \"merge_interval\":60000,\n" +
                        "        \"auto_merge\":" + autoMerge + ",\n" +
                        "        \"auto_inc_merge\":" + autoIncMerge + ",\n" +
                        "        \"group\":[\n" +
                        "            {\n" +
                        "                \"group_name\":\"" + grpName + "\",\n" +
                        "                \"threshold\":0.8,\n" +
                        "                \"type\":\"" + type + "\",\n" +
                        "                \"node_id\":\"" + nodeId + "\"\n" +
                        "            },\n" +
                        "            {\n" +
                        "                \"survival_times\":\"3\",\n" +
                        "                \"split_mode\":\"" + mode + "\",\n" +
                        "                \"visitor_best_threshold\":0.83,\n" +
                        "                \"group_name\":\"DEFAULT\",\n" +
                        "                \"threshold\":0.8,\n" +
                        "                \"type\":\"DEFAULT\",\n" +
                        "                \"node_id\":\"" + nodeId + "\"\n" +
                        "            }\n" +
                        "        ]\n" +
                        "    },\n" +
                        "    \"node_id\":\"3097\",\n" +
                        "    \"service_id\":13\n" +
                        "}";

        String response = putRequest(url, json, header);
        return response;
    }

    public String deleteConfig(int serviceId, boolean autoMerge, boolean autoIncMerge, String nodeId, String grpName,
                               String type, String mode) throws Exception {
        String url = URL_prefix + "/admin/data/nodeServiceConfig/" + serviceId;
        String json =
                "{}";

        String response = deleteRequest(url, json, header);
        return response;
    }

    public String getConfig(int serviceId) throws Exception {
        String url = URL_prefix + "/admin/data/nodeServiceConfig/" + serviceId;

        String response = getRequest(url, header);
        return response;
    }

    private String getOneDeviceType() throws Exception {

        String response = enumDeviceType();
        JSONArray listArr = JSON.parseObject(response).getJSONObject("data").getJSONArray("list");
        String type = "";
        boolean operation = false;
        Random random = new Random();

        while (!operation) {
            int temp = random.nextInt(5);
            JSONObject singleType = listArr.getJSONObject(temp);

            type = singleType.getString("type");
            operation = singleType.getBoolean("operation");
        }

        return type;
    }


    private String getOneDeviceStatus() throws Exception {

        String response = enumDeviceStatus();
        JSONArray statusArr = JSON.parseObject(response).getJSONObject("data").getJSONArray("status_enum");

        String status = "";

        Random random = new Random();

        for (int i = 0; i < statusArr.size(); i++) {
            status = statusArr.getString(random.nextInt(statusArr.size()));
        }

        return status;
    }

    @Test
    public void addSubjectCheck() throws Exception {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        failReason = "";
        Case aCase = new Case();
        int step = 0;

        String caseName = ciCaseName;
        String caseDesc = "验证新增主体是否成功";
        logger.info(caseDesc + "-----------------------------------------------------------------------------------");

        String subjectName = caseName;
        int subjectTypeId = 0;
        String subjectId = "";
        try {

            aCase.setRequestData("1、增加主体-2、查询主体列表-3、查询主体详情" + "\n\n");
            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);

//            1、新增主体
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            subjectTypeId = getOneSUbjectType();
            addSubject(subjectName, subjectTypeId, aCase, step);

//            2、主体列表
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            String response = listSubject();
            subjectId = getSubjectIdByList(response, subjectName);
            System.out.println("=========================================================" + subjectId);

//            3、主体详情
            String detailSubject = detailSubject(subjectId);
            checkIsExistBySubjectDetail(detailSubject);

            aCase.setResult("PASS");
        } catch (AssertionError e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
        } finally {
            deleteSubject(subjectId);

            if (!IS_DEBUG) {
                qaDbUtil.saveToCaseTable(aCase);
            }
        }
    }

    private void checkIsExistBySubjectDetail(String response) throws Exception {
        JSONObject data = JSON.parseObject(response).getJSONObject("data");

        if (data == null || data.size() == 0) {
            throw new Exception("主体详情信息为空！");
        }
    }

    public String getSubjectIdByList(String response, String subjectName) {
        String subjectId = checkIsExistByList(response, subjectName, true);
        return subjectId;
    }

    public String checkIsExistByList(String response, String subjectName, boolean isExist) {
        String subjectId = "";
        boolean isExistRes = false;
        JSONArray list = JSON.parseObject(response).getJSONObject("data").getJSONArray("list");
        for (int i = 0; i < list.size(); i++) {
            JSONObject listSingle = list.getJSONObject(i);
            String subjectNameRes = listSingle.getString("subject_name");
            if (subjectName.equals(subjectNameRes)) {
                isExistRes = true;
                subjectId = listSingle.getString("subject_id");
                break;
            }
        }

        Assert.assertEquals(isExistRes, isExist, "expect isExist: " + isExist + ", acatual: " + isExistRes);
        return subjectId;
    }

    public int getOneSUbjectType() throws Exception {
        int[] allSubjectType = getAllSubjectType();

        Random random = new Random();

        return allSubjectType[random.nextInt(allSubjectType.length)];
    }


    public int[] getAllSubjectType() throws Exception {
        String response = enumNodeType();
        JSONArray list = JSON.parseObject(response).getJSONObject("data").getJSONArray("list");
        if (list == null || list.size() == 0) {
            throw new Exception("主体类型列表为空！");
        }

        int[] ids = new int[list.size()];

        for (int i = 0; i < list.size(); i++) {
            JSONObject singleType = list.getJSONObject(i);
            int id = singleType.getInteger("id");
            ids[i] = id;
        }
        return ids;
    }


    public void sendResAndReqIdToDb(String response, Case acase, int step) {

        if (response != null && response.trim().length() > 0) {
            String requestId = JSON.parseObject(response).getString("request_id");
            String requestDataBefore = acase.getRequestData();
            if (requestDataBefore != null && requestDataBefore.trim().length() > 0) {
                acase.setRequestData(requestDataBefore + "(" + step + ") " + requestId + "\n");
            } else {
                acase.setRequestData("(" + step + ") " + requestId + "\n");
            }

//            将response存入数据库
            String responseBefore = acase.getResponse();
            if (responseBefore != null && responseBefore.trim().length() > 0) {
                acase.setResponse(responseBefore + "(" + step + ") " + JSON.parseObject(response) + "\n\n");
            } else {

                acase.setResponse(JSON.parseObject(response) + "\n\n");
            }
        }
    }

    private void checkCode(String response, int expect, String message) throws Exception {
        JSONObject resJo = JSON.parseObject(response);
        int code = resJo.getInteger("code");
        message = resJo.getString("message");

        if (expect != code) {
            throw new Exception(message + " expect code: " + expect + ",actual: " + code);
        }
    }

    private String postRequest(String url, String json, HashMap header) throws Exception {
        HttpExecutorUtil executor = new HttpExecutorUtil();
        executor.doPostJsonWithHeaders(url, json, header);
        return executor.getResponse();
    }

    private String putRequest(String url, String json, HashMap header) throws Exception {
        HttpExecutorUtil executor = new HttpExecutorUtil();
        executor.doPutJsonWithHeaders(url, json, header);
        return executor.getResponse();
    }

    private String getRequest(String url, HashMap header) throws Exception {
        HttpExecutorUtil executor = new HttpExecutorUtil();
        executor.doGetJsonWithHeaders(url, header);
        return executor.getResponse();
    }

    private String deleteRequest(String url, String json, HashMap header) throws Exception {
        HttpExecutorUtil executor = new HttpExecutorUtil();

        executor.doDeleteJsonWithHeaders(url, json, header);
        return executor.getResponse();
    }

    public void setBasicParaToDB(Case aCase, String caseName, String caseDesc, String ciCaseName) {
        aCase.setApplicationId(APP_ID_DB);
        aCase.setConfigId(CONFIG_ID);
        aCase.setCaseName(caseName);
        aCase.setCaseDescription(caseDesc);
        aCase.setCiCmd(CI_CMD + ciCaseName);
        aCase.setQaOwner("廖祥茹");
        aCase.setExpect("code==1000");
    }

    void genAuth() {

        String json =
                "{\n" +
                        "  \"email\": \"liaoxiangru@winsense.ai\"," +
                        "  \"password\": \"e586aee0d9d9fdb16b9982adb74aeb60\"" +
                        "}";
        try {
            String response = postRequest(genAuthURL, json, header);
            logger.info("\n");
            JSONObject data = JSON.parseObject(response).getJSONObject("data");
            authorization = data.getString("token");

            header.put("Authorization", authorization);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @DataProvider(name = "CONDITION")
    private static Object[][] condition() {
        return new Object[][]{
                new Object[]{
                        "deviceType", "\"device_type\":\"AI_CAMERA\" "
                },
                new Object[]{
                        "deviceId", "\"device_id\":\"" + DEVICE_ID_1 + "\""
                },
                new Object[]{
                        "name", "\"name\":\"" + DEVICE_NAME_1 + "\""
                },
                new Object[]{
                        "sceneType", "\"scene_type\":\"" + "COMMON" + "\""
                }
        };
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