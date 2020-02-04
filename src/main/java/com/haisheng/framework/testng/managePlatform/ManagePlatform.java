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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

public class ManagePlatform {

    private org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());
    boolean IS_DEBUG = false;

    private String UID = "uid_04e816df";
    private String APP_ID = "0d28ec728799";
    private String BRAND_ID = "638";
    private String SHOP_Id = "640";
    private String CLUSTER_NODE_Id = "463";
    private String ALIAS = "测试专用机";
    private static String BATCH_START_DEVICE_ID_1 = "6857175479387136";//batchStartDeviceCheck,listDeviceDiffConditionCheck
    private static String BATCH_START_DEVICE_ID_2 = "6857180815393792";//batchStartDeviceCheck,listDeviceDiffConditionCheck
    private static String DEVICE_ID_MAPPING = "6869660253094912";//layoutMapping

    private static String REGION_DEVICE_1 = "6869668371661824";//已经添加到REGION_ID=3674上
    private static String REGION_DEVICE_2 = "6866177136657408";//已经添加到REGION_ID=3674上
    private static String ENTRANCE_DEVICE_ID_1 = "6866521499632640";
    private static String ENTRANCE_DEVICE_ID_2 = "6866522640647168";
    private static String DEVICE_NAME_1 = "批量启动-管理后台-回归-1【勿动】";//验证设备列表不同搜索条件时用


    private static String REGION_ID_DEVICE = "5974";//设备绑定出入口专用
    private static String ENTRANCE_ID_DEVICE_1 = "5975";//设备绑定出入口专用
    private static String ENTRANCE_ID_DEVICE_2 = "5976";//设备绑定出入口专用
    private static String DEVICE_ID_DEVICE = "6987412810728448";//设备绑定出入口专用
    private static String DEVICE_NAME_DEVICE = "设备-出入口【勿动】";//设备绑定出入口专用
    private static String ENTRANCECE_ID_NON_DEVICE = "5979";//非所属平面出入口，在楼层区域:平面映射【勿动】（3436）下

    private String regionTypeGeneral = "GENERAL";

    //    平面用
    private int LAYOUT_ID = 3238;//floorDeviceCheck
    private int LAYOUT_ID_MAPPING = 3243;//listLayoutDSCheck,layoutMappingCheck

    //    区域用
    private int LAYOUT_ID_REGION = 3308;//listLayoutDSCheck,layoutMappingCheck
    private String REGION_ID = "3674";

    //    进出口用
    private String ENTRANCE_REGION_ID = "3685";
    private String ENTRANCE_ID = "3686";

    private String serviceAcquaintance = "13";
    private String servicePassenger = "3";

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

    public String removeDevice(String deviceId, int expectCode, Case aCase, int step) throws Exception {

        String url = URL_prefix + "/admin/device/remove/" + deviceId;

        String json = "{}";

        String response = deleteRequest(url, json, header);
        sendResAndReqIdToDb(response, aCase, step);
        checkCode(response, expectCode, "");

        return response;
    }

    public String removeDevice(String deviceId) {

        String url = URL_prefix + "/admin/device/remove/" + deviceId;

        String json = "{}";

        String response = null;
        try {
            response = deleteRequest(url, json, header);
            checkCode(response, StatusCode.SUCCESS, "");
        } catch (Exception e) {
            e.printStackTrace();
        }

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


    public String findDetailOfEntrance(String deviceId, Case aCase, int step) throws Exception {

        String url = URL_prefix + "/admin/data/layout/findDetail/device/" + deviceId;

        String response = getRequest(url, header);
        sendResAndReqIdToDb(response, aCase, step);
        checkCode(response, StatusCode.SUCCESS, "");

        return response;
    }

    @Test
    public void addDeviceCheck() {
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
    public void updateDeviceCheck() {
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
    public void deleteDeviceCheck() {
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
            removeDevice(deviceId, StatusCode.SUCCESS, aCase, step);

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
    public void deleteRunningDeviceCheck() {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        failReason = "";
        Case aCase = new Case();
        int step = 0;

        String caseName = ciCaseName;
        String caseDesc = "删除一个已经启动的设备";
        logger.info(caseDesc + "-----------------------------------------------------------------------------------");

        String name = ciCaseName;

        String deviceType = null;
        String deviceId = null;
        try {
            aCase.setRequestData("1、增加设备-2、启动设备-3、删除设备-4、停止设备-5、删除设备" + "\n\n");
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
//            Thread.sleep(10 * 1000);

//            3、删除设备
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            removeDevice(deviceId, StatusCode.UNKNOWN_ERROR, aCase, step);

//            4、停止设备
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            stopDevice(deviceId, aCase, step);

//            5、删除设备
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            removeDevice(deviceId, StatusCode.SUCCESS, aCase, step);

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
    public void startDeviceCheck() {
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
            response = batchStartDevice(BATCH_START_DEVICE_ID_1, BATCH_START_DEVICE_ID_2, aCase, step);
            String[] success = {BATCH_START_DEVICE_ID_1, BATCH_START_DEVICE_ID_2};
            String[] fail = new String[0];
            checkBatchResult(response, success, fail, "批量启动设备");

            Thread.sleep(60 * 1000);

//            2、查询设备列表
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            response = listDevice(SHOP_Id, aCase, step);
            checkDeviceStatus(response, BATCH_START_DEVICE_ID_1, "RUNNING");
            checkDeviceStatus(response, BATCH_START_DEVICE_ID_2, "RUNNING");

//            3、批量停止设备
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            response = batchStopDevice(BATCH_START_DEVICE_ID_1, BATCH_START_DEVICE_ID_2, aCase, step);
            checkBatchResult(response, success, fail, "批量停止设备");

//            4、查询设备列表
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            response = listDevice(SHOP_Id, aCase, step);
            checkDeviceStatus(response, BATCH_START_DEVICE_ID_1, "UN_DEPLOYMENT");
            checkDeviceStatus(response, BATCH_START_DEVICE_ID_2, "UN_DEPLOYMENT");

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
    public void batchMonitorDeviceCheck() {
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
            aCase.setRequestData("1，2、增加设备（2个）-3、批量设置告警-4、查询设备详情-5、查询设备详情" + "\n\n");
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
    public void batchRemoveDeviceCheck() {
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
            aCase.setRequestData("1，2、增加设备（2个）-3、查询设备列表-4、批量删除设备-5、查询设备列表" + "\n\n");
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
            checkisExistByListDevice(response, deviceId2, true);

//            4、批量删除设备
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            response = batchRemoveDevice(deviceId1, deviceId2, aCase, step);

            String[] success = {deviceId1, deviceId2};
            String[] fail = new String[0];
            checkBatchResult(response, success, fail, "批量删除设备");

//            5、查询设备列表
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            response = listDevice(SHOP_Id, aCase, step);
            checkisExistByListDevice(response, deviceId1, false);
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
    public void listDeviceDSCheck() {
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

        String deviceId_1 = "";
        String deviceId_2 = "";
        String computerId = "17";
        String deploymentId = "19";
        String deviceStatus_1 = "RUNNING";
        String deviceStatus_2 = "UN_DEPLOYMENT";
        String sceneType = "COMMON";
        String cloudSceneType = "DEFAULT";
        String creatorName = "廖祥茹";
        String deployTime = "";
        String createTime = "";


        try {
            aCase.setRequestData("1、新建设备-2、启动设备-3、查询设备列表-4、停止设备" + "\n\n");
            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);

//            1、增加设备
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            String deviceType = getOneDeviceType();
            String deviceName_1 = ciCaseName + "-1";
            String response = addDevice(deviceName_1, deviceType, SHOP_Id, aCase, step);
            deviceId_1 = getDeviceId(response);

//            2、增加设备
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            String deviceName_2 = ciCaseName + "-2";
            response = addDevice(deviceName_2, deviceType, SHOP_Id, aCase, step);
            deviceId_2 = getDeviceId(response);

//            2、启动设备
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            startDevice(deviceId_1, aCase, step);
            Thread.sleep(30 * 1000);

//            3、查询设备列表
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            response = listDevice(SHOP_Id, aCase, step);
            checkListDeviceDs(response, deviceId_1, deviceName_1, APP_ID, computerId, SHOP_Id, deviceType, deploymentId,
                    deviceStatus_1, sceneType, cloudSceneType, deployTime, creatorName, createTime, true);

            checkListDeviceDs(response, deviceId_2, deviceName_2, APP_ID, computerId, SHOP_Id, deviceType, deploymentId,
                    deviceStatus_2, sceneType, cloudSceneType, deployTime, creatorName, createTime, false);

//            4、停止设备
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            stopDevice(deviceId_1, aCase, step);

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
            removeDevice(deviceId_1);
            removeDevice(deviceId_2);
            if (!IS_DEBUG) {
                qaDbUtil.saveToCaseTable(aCase);
            }
        }
    }

    @Test(dataProvider = "CONDITION")
    public void listDeviceDiffConditionCheck(String id, String condition) {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        failReason = "";
        Case aCase = new Case();
        int step = 0;

        String caseName = "listDeviceDiffCondition-" + id;
        String caseDesc = "验证查询设备列表不同的搜索条件";
        logger.info(caseDesc + "-----------------------------------------------------------------------------------");

        try {
            aCase.setRequestData("1、查询设备列表" + "\n\n");
            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);

//            查询设备列表
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            String response = listDeviceDiffCondition(SHOP_Id, condition, aCase, step);

            checkisExistByListDevice(response, BATCH_START_DEVICE_ID_1, true);
            checkisExistByListDevice(response, BATCH_START_DEVICE_ID_2, false);

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
    public void deviceEntranceBind() {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        failReason = "";
        Case aCase = new Case();
        int step = 0;

        String caseName = ciCaseName;
        String caseDesc = "设备详情绑定/解绑所属平面的出入口";
        logger.info(caseDesc + "-----------------------------------------------------------------------------------");

        try {
            aCase.setRequestData("1、查询设备的出入口绑定列表-2、出入口列表-3、设备绑定出入口-4、出入口列表" +
                    "5、查询设备的出入口绑定列表-6、出入口解绑设备-7、查询设备的出入口绑定列表-8、出入口列表" + "\n\n");
            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);

//            1、查询设备的出入口绑定列表
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            String response = findDetailOfEntrance(DEVICE_ID_DEVICE, aCase, step);

            checkFindDetail(response, new String[]{ENTRANCE_ID_DEVICE_1});

//            2、出入口列表
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            String listEntrance = listEntrance(REGION_ID_DEVICE, aCase, step);
            checkBindedEntrance(listEntrance, ENTRANCE_DEVICE_ID_1, DEVICE_ID_DEVICE, true);
            checkBindedEntrance(listEntrance, ENTRANCE_DEVICE_ID_2, DEVICE_ID_DEVICE, false);

//            3、设备绑定出入口
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            bindEntranceDeviceCheckCode(ENTRANCE_ID_DEVICE_2, DEVICE_ID_DEVICE, true, false, aCase, step);

//            4、出入口列表
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            listEntrance = listEntrance(REGION_ID_DEVICE, aCase, step);
            checkBindedEntrance(listEntrance, ENTRANCE_DEVICE_ID_1, DEVICE_ID_DEVICE, true);
            checkBindedEntrance(listEntrance, ENTRANCE_DEVICE_ID_2, DEVICE_ID_DEVICE, true);

//            5、查询设备的出入口绑定列表
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            response = findDetailOfEntrance(DEVICE_ID_DEVICE, aCase, step);
            checkFindDetail(response, new String[]{ENTRANCE_ID_DEVICE_1, ENTRANCE_ID_DEVICE_2});

//            6、出入口解绑设备
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            unbindEntranceDevice(ENTRANCE_ID_DEVICE_2, DEVICE_ID_DEVICE, aCase, step);

//            7、查询设备的出入口绑定列表
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            response = findDetailOfEntrance(DEVICE_ID_DEVICE, aCase, step);

            checkFindDetail(response, new String[]{ENTRANCE_ID_DEVICE_1});

//            8、出入口列表
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            listEntrance = listEntrance(REGION_ID_DEVICE, aCase, step);
            checkBindedEntrance(listEntrance, ENTRANCE_DEVICE_ID_1, DEVICE_ID_DEVICE, true);
            checkBindedEntrance(listEntrance, ENTRANCE_DEVICE_ID_2, DEVICE_ID_DEVICE, false);

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
    public void deviceNonEntranceBind() {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        failReason = "";
        Case aCase = new Case();
        int step = 0;

        String caseName = ciCaseName;
        String caseDesc = "设备详情是否能添加别的平面的出入口";
        logger.info(caseDesc + "-----------------------------------------------------------------------------------");

        try {
            aCase.setRequestData("1、选一个别的区域的出入口列表绑定" + "\n\n");
            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);

//            1、绑定非所属平面的出入口
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            String bindEntranceDevice = bindEntranceDevice(ENTRANCECE_ID_NON_DEVICE, DEVICE_ID_DEVICE, true, false, aCase, step);

            checkInvalidBind(bindEntranceDevice);

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

    private void checkInvalidBind(String response) throws Exception {

        JSONObject resJo = JSON.parseObject(response);

        if (1009 != resJo.getInteger("code")) {
            throw new Exception("绑定不是所属平面的出入口时，返回的状态码是：" + resJo.getInteger("code") + ",期待：1009");
        }

        if (!"校验失败:当前设备已绑定其它平面楼层,与此区域平面楼层不一致".equals(resJo.getString("message"))) {
            throw new Exception("绑定不是所属平面的出入口时，返回的message是：【" + resJo.getString("message") +
                    "】,期待：【校验失败:当前设备已绑定其它平面楼层,与此区域平面楼层不一致】");
        }
    }

    private void checkBindedEntrance(String response, String entranceId, String deviceId, boolean expectBind) throws Exception {
        JSONArray list = JSON.parseObject(response).getJSONObject("data").getJSONArray("list");
        boolean isExist = false;
        for (int i = 0; i < list.size(); i++) {
            JSONObject singleEntrance = list.getJSONObject(i);
            String entranceIdRes = singleEntrance.getString("entrance_id");

            if (entranceId.equals(entranceIdRes)) {
                JSONArray bindDevice = singleEntrance.getJSONArray("bind_device");
                for (int j = 0; j < bindDevice.size(); j++) {
                    JSONObject singleDevice = bindDevice.getJSONObject(i);
                    if (deviceId.equals(singleDevice.getString("device_id"))) {
                        isExist = true;
                    }
                }

                if (expectBind ^ isExist) {
                    throw new Exception("设备【" + deviceId + "】，出入口【" + entranceId + "】，期待绑定：" + expectBind + ",系统是否绑定：" + isExist);
                }
            }
        }
    }

    private void checkFindDetail(String response, String[] entranceIds) {

        JSONArray regionList = JSON.parseObject(response).getJSONObject("data").getJSONObject("layout").getJSONArray("region_list");

        for (int i = 0; i < regionList.size(); i++) {
            JSONObject singleRegion = regionList.getJSONObject(i);
            String reiognIdRes = singleRegion.getString("region_id");
            if (REGION_ID_DEVICE.equals(reiognIdRes)) {
                JSONArray entranceList = singleRegion.getJSONArray("entrance_List");
                String[] entranceIdsRes = new String[entranceList.size()];
                for (int j = 0; j < entranceList.size(); j++) {
                    JSONObject singleEntrance = entranceList.getJSONObject(j);
                    entranceIdsRes[j] = singleEntrance.getString("entrance_id");
                }

                Assert.assertEqualsNoOrder(entranceIdsRes, entranceIds, "设备【" + DEVICE_NAME_DEVICE + "】实际绑定的出入口列表："
                        + Arrays.toString(entranceIdsRes) + ",期待：" + Arrays.toString(entranceIds));
            }
        }
    }

    private void checkListDeviceDs(String response, String deviceId, String name, String appId, String computerId,
                                   String subjectId, String deviceType, String deploymentId, String deviceStatus,
                                   String sceneType, String cloudSceneType, String deployTime, String creatorName,
                                   String createTime, boolean isStart) throws Exception {

        boolean isExist = false;
        JSONObject resJo = JSON.parseObject(response);

        String function = "查询设备列表";

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

                checkKeyValue(function, singleDevice, "name", name, true);

                checkKeyValue(function, singleDevice, "app_id", appId, true);

                checkKeyValue(function, singleDevice, "subject_id", subjectId, true);

                checkKeyValue(function, singleDevice, "device_type", deviceType, true);

                checkKeyValue(function, singleDevice, "device_status", deviceStatus, true);

                checkKeyValue(function, singleDevice, "scene_type", sceneType, true);

                checkKeyValue(function, singleDevice, "cloud_scene_type", cloudSceneType, true);

                checkKeyKeyValue(function, singleDevice, "creator_name", creatorName, true);

                checkKeyValue(function, singleDevice, "gmt_create", createTime, false);

                if (isStart) {
                    checkKeyValue(function, singleDevice, "compute_id", computerId, false);
                    checkKeyValue(function, singleDevice, "deployment_id", deploymentId, false);
                    checkKeyValue(function, singleDevice, "deploy_time", deployTime, false);

                } else {
//                    checkKeyValue(function,singleDevice,"deploy_time",null,false);
//                    checkKeyValue(function,singleDevice,"deploy_time",null,false);
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

    public String deleteLayout(int layoutId) {

        logger.info("\n");
        logger.info("------------------------4、finally平面删除----------------------------");

        String url = URL_prefix + "/admin/data/layout/" + layoutId;
        String json =
                "{}";

        String response = null;
        try {
            response = deleteRequest(url, json, header);
            checkCode(response, StatusCode.SUCCESS, "");
        } catch (Exception e) {
            e.printStackTrace();
        }

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
    public void addFloorCheck() {

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
    public void reAddFloorCheck() {

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
    public void deleteFloorRegionCheck() {

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
            response = listRegion(SHOP_Id, layoutId, true, aCase, step);
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
    public void updateFloorCheck() {

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
    public void layoutPicCheck() {

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

        String deviceId_1 = "";
        String deviceId_2 = "";

        try {
            aCase.setRequestData("1,2、新增设备-3,4、平面设备新增-5、平面所属设备列表-6,7、平面设备删除-8、平面所属设备列表" + "\n\n");
            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);

//            1,2、新增设备
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            String deviceType = getOneDeviceType();
            String deviceName_1 = ciCaseName + "-1";
            String deviceName_2 = ciCaseName + "-2";
            String response = addDevice(deviceName_1, deviceType, SHOP_Id, aCase, step);
            deviceId_1 = getDeviceId(response);
            response = addDevice(deviceName_2, deviceType, SHOP_Id, aCase, step);
            deviceId_2 = getDeviceId(response);

//            3,4、平面设备新增
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            addLayoutDevice(LAYOUT_ID, deviceId_1, aCase, step);
            addLayoutDevice(LAYOUT_ID, deviceId_2, aCase, step);

//            5、平面所属设备列表
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            response = listLayoutDevice(LAYOUT_ID, aCase, step);
            checkLayoutDevice(response, deviceId_1, true);
            checkLayoutDevice(response, deviceId_2, true);

//            6,7、平面设备删除
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            deleteLayoutDevice(LAYOUT_ID, deviceId_1, aCase, step);
            deleteLayoutDevice(LAYOUT_ID, deviceId_2, aCase, step);

//            8、平面所属设备列表
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            response = listLayoutDevice(LAYOUT_ID, aCase, step);
            checkLayoutDevice(response, deviceId_1, false);
            checkLayoutDevice(response, deviceId_2, false);

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
            removeDevice(deviceId_1);
            removeDevice(deviceId_2);
            if (!IS_DEBUG) {
                qaDbUtil.saveToCaseTable(aCase);
            }
        }
    }

    @Test
    public void layoutMappingCheck() {

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
    public void listLayoutDSCheck() {

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
        if (expectExactValue) {
            Assert.assertEquals(valueRes, value, key + "字段值不相符：列表返回：" + valueRes + ", 实际：" + value);
        } else {
            if (valueRes == null || "".equals(valueRes)) {
                throw new Exception(key + "对应的字段值为空！");
            }
        }
    }

    private void checkKeyArrValue(String function, JSONObject jo, String key, int minSize) throws Exception {

        if (!jo.containsKey(key)) {
            throw new Exception(function + "---没有返回" + key + "字段！");
        }

        JSONArray valueRes = jo.getJSONArray(key);

        if (valueRes == null || valueRes.size() < minSize) {
            throw new Exception(function + "---" + key + "字段值为空或少于最小size数！");
        }
    }

    private void checkKeyObjectValue(String function, JSONObject jo, String key, int minSize) throws Exception {

        if (!jo.containsKey(key)) {
            throw new Exception(function + "---没有返回" + key + "字段！");
        }

        JSONObject valueRes = jo.getJSONObject(key);

        if (valueRes == null || valueRes.size() < minSize) {
            throw new Exception(function + "---" + key + "字段值为空或少于最小size数！");
        }
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
    public String addRegion(String regionName, String regionType, int layoutId, boolean isLayoutRegion, Case aCase, int step) throws Exception {
        String url = URL_prefix + "/admin/data/region/";

        String json =
                "{\n" +
                        "    \"region_name\":\"" + regionName + "\",\n" +
                        "    \"region_type\":\"" + regionType + "\",\n";

        if (isLayoutRegion) {
            json += "    \"layout_id\":" + layoutId + ",\n";
        }

        json += "    \"subject_id\":\"" + SHOP_Id + "\"\n" +
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

    public String deleteRegion(String regionId) {
        String url = URL_prefix + "/admin/data/region/" + regionId;

        String json =
                "{}";

        String response = null;
        try {
            response = deleteRequest(url, json, header);
            checkCode(response, StatusCode.SUCCESS, "创建新区域");
        } catch (Exception e) {
            e.printStackTrace();
        }

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
    public String listRegion(String subjectId, int layoutId, boolean isLayoutRegion, Case aCase, int step) throws Exception {
        String url = URL_prefix + "/admin/data/region/list";

        String json =
                "{\n" +
                        "    \"subject_id\":\"" + subjectId + "\",\n";

        if (isLayoutRegion) {
            json += "    \"layout_id\":\"" + layoutId + "\",\n";
        }

        json += "    \"page\":1,\n" +
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

    public String addRegionDeviceCheckCode(String regionId, String deviceId, int expectCode, Case aCase, int step) throws Exception {
        String url = URL_prefix + "/admin/data/regionDevice/";

        String json =
                "{\n" +
                        "    \"region_id\":" + regionId + ",\n" +
                        "    \"device_id\":\"" + deviceId + "\"\n" +
                        "}";


        String response = postRequest(url, json, header);
        sendResAndReqIdToDb(response, aCase, step);
        checkCode(response, expectCode, "区域列表");
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

    @Test(dataProvider = "ADD_REGION")
    public void addRegionCheck(boolean isLayoutRegion) {

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
            String response = addRegion(regionName, regionType, LAYOUT_ID_REGION, isLayoutRegion, aCase, step);
            regionId = getRegionId(response);

//            2、区域列表
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            response = listRegion(SHOP_Id, LAYOUT_ID_REGION, isLayoutRegion, aCase, step);
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
    public void deleteRegionCheck() {

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
            String response = addRegion(regionName, regionType, LAYOUT_ID, true, aCase, step);
            String regionId = getRegionId(response);

//            2、区域列表
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            response = listRegion(SHOP_Id, LAYOUT_ID, true, aCase, step);
            checkListRegion(response, regionId, regionName, true);

//            3、区域删除
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            deleteRegion(regionId);

//            4、区域列表
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            response = listRegion(SHOP_Id, LAYOUT_ID, true, aCase, step);
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
    public void regionDeviceCheck() {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        failReason = "";
        Case aCase = new Case();
        int step = 0;

        String caseName = ciCaseName;
        String caseDesc = "验证新增/删除多个设备";
        logger.info(caseDesc + "-----------------------------------------------------------------------------------");

        try {
            aCase.setRequestData("1,2、新增区域设备-3、区域所属设备列表-4,5、删除区域设备-6、区域所属设备列表" + "\n\n");
            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);

//            1、区域设备新增
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            addRegionDevice(REGION_ID, REGION_DEVICE_1, aCase, step);

//            2、区域设备新增
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            addRegionDevice(REGION_ID, REGION_DEVICE_2, aCase, step);

//            3、区域所属设备列表
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            String response = listRegionDevice(REGION_ID, aCase, step);
            checkLayoutDevice(response, REGION_DEVICE_1, true);
            checkLayoutDevice(response, REGION_DEVICE_2, true);

//            4、删除区域设备
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            deleteRegionDevice(REGION_ID, REGION_DEVICE_1, aCase, step);

//            5、删除区域设备
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            deleteRegionDevice(REGION_ID, REGION_DEVICE_2, aCase, step);

//            6、区域所属设备列表
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            response = listRegionDevice(REGION_ID, aCase, step);
            checkLayoutDevice(response, REGION_DEVICE_1, false);
            checkLayoutDevice(response, REGION_DEVICE_2, false);

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
    public void updateRegionCheck() {

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
            String response = addRegion(regionName, regionType, LAYOUT_ID_REGION, true, aCase, step);
            regionId = getRegionId(response);

//            2、区域编辑
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            updateRegion(regionId, regionName, false, aCase, step);

//            3、区域列表
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            response = listRegion(SHOP_Id, LAYOUT_ID_REGION, true, aCase, step);
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
    public void regionDrawCheck() {

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
            String response = addRegion(regionName, regionType, LAYOUT_ID_REGION, true, aCase, step);
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
            aCase.setRequestData("1、新增区域设备-2、区域设备列表-3、平面设备列表-4、区域设备删除" + "\n\n");
            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);

//            1、新增区域设备
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            addRegionDevice(REGION_ID, REGION_DEVICE_2, aCase, step);

//            2、区域设备列表
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            String response = listRegionDevice(REGION_ID, aCase, step);
            checkRegionDeviceLocation(response, REGION_DEVICE_2, true, true);

//            3、平面设备列表
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            response = listLayoutDevice(LAYOUT_ID_REGION, aCase, step);
            checkLayoutDeviceLocation(response, REGION_DEVICE_2);
            checkLayoutDeviceLocation(response, REGION_DEVICE_2);

//            4、区域设备删除
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            deleteRegionDevice(REGION_ID, REGION_DEVICE_2, aCase, step);

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
    public void deleteLayoutDeviceCheck() {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        failReason = "";
        Case aCase = new Case();
        int step = 0;

        String caseName = ciCaseName;
        String caseDesc = "平面区域绑定设备后，平面设备解绑，区域设备要跟着解绑";
        logger.info(caseDesc + "-----------------------------------------------------------------------------------");

        String deviceId = "";

        try {
            aCase.setRequestData("1、新增设备-2、绑定平面设备-3、绑定区域设备-4、区域设备列表-5、解绑平面设备-6、区域设备列表" + "\n\n");
            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);

//            1、新建设备
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            String name = ciCaseName;
            String deviceType = getOneDeviceType();
            String response = addDevice(name, deviceType, SHOP_Id, aCase, step);
            deviceId = getDeviceId(response);

//            2、绑定平面设备
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            addLayoutDevice(LAYOUT_ID_REGION, deviceId, aCase, step);

//            3、绑定区域设备
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            addRegionDevice(REGION_ID, deviceId, aCase, step);

//            4、区域设备列表
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            response = listRegionDevice(REGION_ID, aCase, step);
            checkRegionDeviceLocation(response, deviceId, false, true);

//            5、解绑平面设备
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            deleteLayoutDevice(LAYOUT_ID_REGION, deviceId, aCase, step);

//            6、区域设备列表
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            response = listRegionDevice(REGION_ID, aCase, step);
            checkRegionDeviceLocation(response, REGION_DEVICE_2, false, false);

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

    @Test(dataProvider = "NON_LAYOUT_DEVICE")//ADD_REGION       NON_LAYOUT_DEVICE
    public void layoutRegionBindNonLayoutDeviceCheck(String deviceId, int expect) {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        failReason = "";
        Case aCase = new Case();
        int step = 0;

        System.out.println(deviceId);

        String caseName = ciCaseName;
        String caseDesc = "平面区域不可绑定非此平面设备";
        logger.info(caseDesc + "-----------------------------------------------------------------------------------");

        try {
            aCase.setRequestData("1、绑定区域设备-2、解绑区域设备" + "\n\n");
            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);

//            1、绑定区域设备
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            addRegionDeviceCheckCode(REGION_ID, deviceId, expect, aCase, step);

//            2、解绑区域设备
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            deleteRegionDevice(REGION_ID, deviceId, aCase, step);

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


    private void checkRegionDeviceLocation(String response, String deviceId, boolean isMapping, boolean isExist) throws Exception {
        JSONObject data = JSON.parseObject(response).getJSONObject("data");

        JSONArray list = data.getJSONArray("list");

        boolean isExistRes = false;

        for (int i = 0; i < list.size(); i++) {
            JSONObject regionDevice = list.getJSONObject(i);
            String deviceIdRes = regionDevice.getString("device_id");
            if (deviceId.equals(deviceIdRes)) {
                isExistRes = true;
                JSONObject deviceLocation = regionDevice.getJSONObject("device_location");
                if (isMapping) {
                    if (deviceLocation == null || deviceLocation.size() == 0) {
                        throw new Exception("映射后，区域设备位置为空！");
                    }
                } else {
                    if (!(deviceLocation == null || deviceLocation.size() == 0)) {
                        throw new Exception("没有映射，区域设备位置不为空！");
                    }
                }
            }
        }

        Assert.assertEquals(isExistRes, isExist, "是否期待存在该设备，期待：" + isExist + ", 实际：" + isExistRes);
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

//    -------------------------------------------出入口管理模块--------------------------------------------------------

    //    1、新增出入口
    public String addEntrance(String name, String entranceType, String regionId, Case aCase, int step) throws Exception {
        String url = URL_prefix + "/admin/data/entrance/";
        String json =
                "{\n" +
                        "    \"entrance_name\":\"" + name + "\",\n" +
                        "    \"entrance_type\":\"" + entranceType + "\",\n" +
                        "    \"region_id\":\"" + regionId + "\"\n" +
                        "}";

        String response = postRequest(url, json, header);
        sendResAndReqIdToDb(response, aCase, step);
        checkCode(response, StatusCode.SUCCESS, "");
        return response;
    }

    //    2、删除出入口
    public String deleteEntrance(String entranceId, Case aCase, int step) throws Exception {
        String url = URL_prefix + "/admin/data/entrance/" + entranceId;
        String json =
                "{}";

        String response = deleteRequest(url, json, header);
        sendResAndReqIdToDb(response, aCase, step);
        checkCode(response, StatusCode.SUCCESS, "");
        return response;
    }

    public String deleteEntrance(String entranceId) {
        String url = URL_prefix + "/admin/data/entrance/" + entranceId;
        String json =
                "{}";

        String response = null;
        try {
            response = deleteRequest(url, json, header);
            checkCode(response, StatusCode.SUCCESS, "");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    //    3、出入口编辑
    public String updateEntrance(String entranceId, String entranceName, String entranceType, Case aCase, int step) throws Exception {
        String url = URL_prefix + "/admin/data/entrance/" + entranceId;
        String json =
                "{\n" +
                        "    \"entrance_name\":\"" + entranceName + "\",\n" +
                        "    \"entrance_type\":\"" + entranceType + "\"\n" +
                        "}";

        String response = putRequest(url, json, header);
        sendResAndReqIdToDb(response, aCase, step);
        checkCode(response, StatusCode.SUCCESS, "");
        return response;
    }

    //    4、出入口详情
    public String getEntrance(String entranceId, Case aCase, int step) throws Exception {
        String url = URL_prefix + "/admin/data/entrance/" + entranceId;

        String response = getRequest(url, header);
        sendResAndReqIdToDb(response, aCase, step);
        checkCode(response, StatusCode.SUCCESS, "");
        return response;
    }

    //    5、出入口列表
    public String listEntrance(String regionId, Case aCase, int step) throws Exception {
        String url = URL_prefix + "/admin/data/entrance/list";
        String json =
                "{\n" +
                        "    \"region_id\":\"" + regionId + "\",\n" +
                        "    \"page\":1,\n" +
                        "    \"size\":50\n" +
                        "}";

        String response = postRequest(url, json, header);
        sendResAndReqIdToDb(response, aCase, step);
        checkCode(response, StatusCode.SUCCESS, "");
        return response;
    }

    public String genEntranceDeviceJson(String entraneId, String deviceId, boolean isDraw, boolean isRegion) {

        String json =
                "{\n";
        if (!isDraw) {
            json += "    \"entrance_dp_location_1\":null,\n" +
                    "    \"entrance_location_1\":null,\n" +
                    "    \"entrance_point_1\":null,\n";
        } else {
            if (isRegion) {
                json += "    \"entrance_location\":[\n" +
                        "        {\n" +
                        "            \"x\":0.22142857142857145,\n" +
                        "            \"y\":0.3707936507936508\n" +
                        "        },\n" +
                        "        {\n" +
                        "            \"x\":0.89,\n" +
                        "            \"y\":0.3352380952380953\n" +
                        "        }\n" +
                        "    ],\n" +
                        "    \"entrance_point\":{\n" +
                        "        \"x\":0.48428571428571426,\n" +
                        "        \"y\":0.7238095238095238\n" +
                        "    },\n";

            } else {
                json += "    \"entrance_dp_location\":[\n" +
                        "        {\n" +
                        "            \"x\":0.3164285714285714,\n" +
                        "            \"y\":0.2615873015873016\n" +
                        "        },\n" +
                        "        {\n" +
                        "            \"x\":0.6321428571428572,\n" +
                        "            \"y\":0.259047619047619\n" +
                        "        },\n" +
                        "        {\n" +
                        "            \"x\":0.685,\n" +
                        "            \"y\":0.5638095238095238\n" +
                        "        },\n" +
                        "        {\n" +
                        "            \"x\":0.15214285714285714,\n" +
                        "            \"y\":0.7365079365079366\n" +
                        "        }\n" +
                        "    ],\n";
            }

        }

        json += "    \"entrance_id\":" + entraneId + ",\n" +
                "    \"device_id\":\"" + deviceId + "\"\n" +
                "}";


        return json;
    }

    //    6、绑定出入口设备
    public String bindEntranceDeviceCheckCode(String entranceId, String deviceId, boolean isDraw, boolean isRegion, Case aCase, int step) throws Exception {
        String url = URL_prefix + "/admin/data/entranceDevice/";

        String json = genEntranceDeviceJson(entranceId, deviceId, isDraw, isRegion);

        String response = postRequest(url, json, header);
        sendResAndReqIdToDb(response, aCase, step);
        checkCode(response, StatusCode.SUCCESS, "绑定出入口设备");
        return response;
    }

    public String bindEntranceDevice(String entranceId, String deviceId, boolean isDraw, boolean isRegion, Case aCase, int step) throws Exception {
        String url = URL_prefix + "/admin/data/entranceDevice/";

        String json = genEntranceDeviceJson(entranceId, deviceId, isDraw, isRegion);

        String response = postRequest(url, json, header);
        sendResAndReqIdToDb(response, aCase, step);
        return response;
    }

    //    7、出入口设备编辑
    public String updateEntranceDevice(String entranceId, String deviceId, boolean isDraw, boolean isRegion, Case aCase, int step) throws Exception {
        String url = URL_prefix + "/admin/data/entranceDevice/";

        String json = genEntranceDeviceJson(entranceId, deviceId, isDraw, isRegion);

        String response = putRequest(url, json, header);
        sendResAndReqIdToDb(response, aCase, step);
        checkCode(response, StatusCode.SUCCESS, "");
        return response;
    }

    //    8、出入口设备解绑
    public String unbindEntranceDevice(String entranceId, String deviceId, Case aCase, int step) throws Exception {
        String url = URL_prefix + "/admin/data/entranceDevice/" + entranceId + "/" + deviceId;
        String json =
                "{}";

        String response = deleteRequest(url, json, header);
        sendResAndReqIdToDb(response, aCase, step);
        checkCode(response, StatusCode.SUCCESS, "出入口设备解绑");
        return response;
    }

    //    9、出入口所属设备列表（不分页）
    public String listEntranceDevice(String entranceId, Case aCase, int step) throws Exception {
        String url = URL_prefix + "/admin/data/entranceDevice/list";
        String json =
                "{\n" +
                        "    \"entrance_id\":\"" + entranceId + "\"\n" +
                        "}";

        String response = postRequest(url, json, header);
        sendResAndReqIdToDb(response, aCase, step);
        checkCode(response, StatusCode.SUCCESS, "");
        return response;
    }

    //    10、出入口类型
    public String enumEntranceType() throws Exception {
        String url = URL_prefix + "/admin/data/entrance/entranceEnum/list";

        String response = getRequest(url, header);
        checkCode(response, StatusCode.SUCCESS, "");
        return response;
    }

    @Test
    public void addEntranceCheck() {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        failReason = "";
        Case aCase = new Case();
        int step = 0;

        String caseName = ciCaseName;
        String caseDesc = "验证新增出入口是否成功";
        logger.info(caseDesc + "-----------------------------------------------------------------------------------");

        String entranceId = "";
        try {

            aCase.setRequestData("1、新增出入口-2、进出口列表-3、进出口详情" + "\n\n");
            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);

//            1、新增出入口
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            String[] entranceTypes = getEntranceType();
            String entranceType = entranceTypes[0];
            String name = ciCaseName;

            String response = addEntrance(name, entranceType, ENTRANCE_REGION_ID, aCase, step);
            entranceId = getEntranceId(response);

//            2、进出口列表
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            response = listEntrance(ENTRANCE_REGION_ID, aCase, step);
            checkListEntrance(response, entranceId, name, entranceType, true);

//            3、进出口详情
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            response = getEntrance(entranceId, aCase, step);
            checkGetEntrance(response, entranceType, name);

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
            deleteEntrance(entranceId);
            if (!IS_DEBUG) {
                qaDbUtil.saveToCaseTable(aCase);
            }
        }
    }

    @Test
    public void deleteEntranceCheck() {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        failReason = "";
        Case aCase = new Case();
        int step = 0;

        String caseName = ciCaseName;
        String caseDesc = "验证删除出入口是否成功";
        logger.info(caseDesc + "-----------------------------------------------------------------------------------");

        String entranceId = "";
        try {

            aCase.setRequestData("1、新增出入口-2、进出口列表-3、删除进出口-4、进出口列表" + "\n\n");
            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);

//            1、新增出入口
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            String[] entranceTypes = getEntranceType();
            String entranceType = entranceTypes[0];
            String name = ciCaseName;

            String response = addEntrance(name, entranceType, ENTRANCE_REGION_ID, aCase, step);
            entranceId = getEntranceId(response);

//            2、进出口列表
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            response = listEntrance(ENTRANCE_REGION_ID, aCase, step);
            checkListEntrance(response, entranceId, name, entranceType, true);

//            3、进出口详情
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            deleteEntrance(entranceId);

//            4、进出口列表
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            response = listEntrance(ENTRANCE_REGION_ID, aCase, step);
            checkListEntrance(response, entranceId, name, entranceType, false);

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
            deleteEntrance(entranceId);
            if (!IS_DEBUG) {
                qaDbUtil.saveToCaseTable(aCase);
            }
        }
    }

    @Test
    public void updateEntranceCheck() {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        failReason = "";
        Case aCase = new Case();
        int step = 0;

        String caseName = ciCaseName;
        String caseDesc = "验证更新出入口是否成功";
        logger.info(caseDesc + "-----------------------------------------------------------------------------------");

        String entranceId = "";
        try {

            aCase.setRequestData("1、新增出入口-2、进出口编辑-3、进出口列表-4、进出口详情" + "\n\n");
            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);

//            1、新增出入口
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            String[] entranceTypes = getEntranceType();
            String entranceType = entranceTypes[0];
            String entranceTypeNew = entranceTypes[1];
            String name = ciCaseName;
            String nameNew = ciCaseName + "-new";

            String response = addEntrance(name, entranceType, ENTRANCE_REGION_ID, aCase, step);
            entranceId = getEntranceId(response);

//            2、进出口编辑
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            updateEntrance(entranceId, nameNew, entranceTypeNew, aCase, step);

//            3、进出口列表
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            response = listEntrance(ENTRANCE_REGION_ID, aCase, step);
            checkListEntrance(response, entranceId, nameNew, entranceTypeNew, true);

//            4、进出口详情
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            response = getEntrance(entranceId, aCase, step);
            checkGetEntrance(response, entranceTypeNew, nameNew);

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
            deleteEntrance(entranceId);
            if (!IS_DEBUG) {
                qaDbUtil.saveToCaseTable(aCase);
            }
        }
    }

    @Test(dataProvider = "ADD_UPDATE_ENTRANCE")
    public void entranceDeviceCheck(String entranceType, boolean AddIsDraw, boolean updateIsDraw, boolean isRegion) {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        failReason = "";
        Case aCase = new Case();
        int step = 0;

        String caseName = ciCaseName;
        String caseDesc = "验证出入口设备绑定/解绑/编辑";
        logger.info(caseDesc + "-----------------------------------------------------------------------------------");

        String entranceId = "";

        try {

            aCase.setRequestData("1、新建出入口；-2、绑定出入口设备-3、进出口所属设备列表-4、进出口设备编辑-5、进出口所属设备列表-" +
                    "6、进出口设备解绑-7、进出口所属设备列表" + "\n\n");
            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);

//            updateEntranceDevice()

            String entranceName = ciCaseName;

//            1、新建出入口
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            String response = addEntrance(entranceName, entranceType, ENTRANCE_REGION_ID, aCase, step);
            entranceId = getEntranceId(response);

//            2、绑定出入口设备
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            bindEntranceDeviceCheckCode(entranceId, ENTRANCE_DEVICE_ID_1, AddIsDraw, isRegion, aCase, step);

//            3、进出口所属设备列表
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            response = listEntranceDevice(entranceId, aCase, step);
            checkListEntrnceDevice(response, ENTRANCE_DEVICE_ID_1, AddIsDraw, isRegion, true);

//            4、进出口设备编辑
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            updateEntranceDevice(entranceId, ENTRANCE_DEVICE_ID_1, updateIsDraw, isRegion, aCase, step);

//            5、进出口所属设备列表
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            response = listEntranceDevice(entranceId, aCase, step);
            checkListEntrnceDevice(response, ENTRANCE_DEVICE_ID_1, updateIsDraw, isRegion, true);

//            6、进出口设备解绑
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            unbindEntranceDevice(entranceId, ENTRANCE_DEVICE_ID_1, aCase, step);

//            7、进出口所属设备列表
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            response = listEntranceDevice(entranceId, aCase, step);
            checkListEntrnceDevice(response, ENTRANCE_DEVICE_ID_1, updateIsDraw, isRegion, false);

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
            deleteEntrance(entranceId);
            if (!IS_DEBUG) {
                qaDbUtil.saveToCaseTable(aCase);
            }
        }
    }

    @Test()
    public void getEntranceDSCheck() {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        failReason = "";
        Case aCase = new Case();
        int step = 0;

        String caseName = ciCaseName;
        String caseDesc = "验证出入口详情返回的数据结构";
        logger.info(caseDesc + "-----------------------------------------------------------------------------------");

        try {

            aCase.setRequestData("1、出入口详情" + "\n\n");
            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);

            String entranceName = "管理后台-出入口-回归【勿动】";
            String entranceType = "REGION";

//            1、出入口详情
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            String response = getEntrance(ENTRANCE_ID, aCase, step);

            CheckGetEntranceDS(response, ENTRANCE_ID, entranceType, entranceName);

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

    @Test()
    public void listEntranceDSCheck() {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        failReason = "";
        Case aCase = new Case();
        int step = 0;

        String caseName = ciCaseName;
        String caseDesc = "验证出入口列表返回的数据结构";
        logger.info(caseDesc + "-----------------------------------------------------------------------------------");

        try {

            aCase.setRequestData("1、出入口列表" + "\n\n");
            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);

            String entranceName = "管理后台-出入口-回归【勿动】";
            String entranceType = "REGION";

//            1、出入口列表
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            String response = listEntrance(ENTRANCE_REGION_ID, aCase, step);

            checkListEntranceDS(response, ENTRANCE_ID, entranceType, entranceName);

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

    @Test()
    public void listEntranceDeviceDSCheck() {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        failReason = "";
        Case aCase = new Case();
        int step = 0;

        String caseName = ciCaseName;
        String caseDesc = "验证出入口设备列表返回的数据结构";
        logger.info(caseDesc + "-----------------------------------------------------------------------------------");

        try {

            aCase.setRequestData("1、出入口设备列表" + "\n\n");
            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);

            String deviceName = "出入口设备-1【勿动】";
            String deviceType = "WEB_CAMERA";

//            1、出入口设备
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            String response = listEntranceDevice(ENTRANCE_ID, aCase, step);

            checkListEntranceDeviceDS(response, ENTRANCE_DEVICE_ID_1, deviceType, deviceName);

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

    private void checkListEntranceDeviceDS(String response, String deviceId, String deviceType, String deviceName) throws Exception {

        JSONObject data = JSON.parseObject(response).getJSONObject("data");

        JSONArray list = data.getJSONArray("list");

        boolean isExist = false;

        String function = "出入口设备列表";

        for (int i = 0; i < list.size(); i++) {
            JSONObject single = list.getJSONObject(i);
            String deviceIdRes = single.getString("device_id");
            if (deviceId.equals(deviceIdRes)) {
                isExist = true;
                checkKeyValue(function, single, "device_type", deviceType, true);

                checkKeyValue(function, single, "name", deviceName, true);
                checkKeyValue(function, single, "url", "", false);
                checkKeyValue(function, single, "device_pic_oss", "", false);

                checkKeyArrValue(function, single, "entrance_location", 2);
                checkKeyObjectValue(function, single, "entrance_point", 2);

                checkKeyObjectValue(function, single, "device_location", 2);
            }
        }

        if (!isExist) {
            throw new Exception("该出入口不存在！");
        }

    }

    private void checkListEntranceDS(String response, String entranceId, String entranceType, String entranceName) throws Exception {
        JSONObject data = JSON.parseObject(response).getJSONObject("data");

        JSONArray list = data.getJSONArray("list");

        boolean isExist = false;

        String function = "出入口列表";

        for (int i = 0; i < list.size(); i++) {
            JSONObject single = list.getJSONObject(i);
            String entranceIdRes = single.getString("entrance_id");
            if (entranceId.equals(entranceIdRes)) {
                isExist = true;
                checkKeyValue(function, single, "entrance_type", entranceType, true);

                checkKeyValue(function, single, "entrance_name", entranceName, true);
                checkKeyValue(function, single, "gmt_create", "", false);
                checkKeyValue(function, single, "stair", "", false);
                checkKeyValue(function, single, "both_dir", "", false);
                checkKeyValue(function, single, "use_line", "", false);
                checkKeyValue(function, single, "is_stair", "", false);
            }
        }

        if (!isExist) {
            throw new Exception("该出入口不存在！");
        }
    }

    private void CheckGetEntranceDS(String response, String entranceId, String entranceType, String entranceName) throws Exception {

        String function = "出入口详情";

        JSONObject data = JSON.parseObject(response).getJSONObject("data");
        checkKeyValue(function, data, "gmt_create", "", false);
        checkKeyValue(function, data, "entrance_id", entranceId, true);
        checkKeyValue(function, data, "entrance_type", entranceType, true);
        checkKeyValue(function, data, "entrance_name", entranceName, true);
        checkKeyKeyValue(function, data, "creator_name", "", false);
        checkKeyKeyValue(function, data, "creator_id", "", false);
        checkKeyValue(function, data, "gmt_modified", "", false);
        checkKeyValue(function, data, "entrance_type", entranceType, true);

        checkKeyArrValue(function, data, "entrance_map_location", 3);
    }

    private void checkListEntrnceDevice(String response, String deviceId, boolean isDraw, boolean isRegion, boolean isExist) throws Exception {

        JSONObject data = JSON.parseObject(response).getJSONObject("data");

        JSONArray list = data.getJSONArray("list");

        boolean isExistRes = false;

        for (int i = 0; i < list.size(); i++) {
            JSONObject single = list.getJSONObject(i);
            String deviceIdRes = single.getString("device_id");
            if (deviceId.equals(deviceIdRes)) {
                isExistRes = true;
                if (isRegion) {
                    JSONArray entranceLocation = single.getJSONArray("entrance_location");
                    JSONObject entrancePoint = single.getJSONObject("entrance_point");
                    if (isDraw) {
                        if (entranceLocation == null || entranceLocation.size() < 2) {
                            throw new Exception("该出入口画完映射以后，entrance_location为空！");
                        }

                        if (entrancePoint == null || entrancePoint.size() < 2) {
                            throw new Exception("该出入口画完映射以后，entrance_point为空！");
                        }
                    } else {
                        if (!(entranceLocation == null || entranceLocation.size() == 0)) {
                            throw new Exception("删除该出入口映射以后，entrance_location没有清空！");
                        }
                        if (!(entrancePoint == null || entrancePoint.size() == 0)) {
                            throw new Exception("删除该出入口映射以后，entrance_point没有清空！");
                        }
                    }
                } else {
                    JSONArray entranceDpLocation = single.getJSONArray("entrance_dp_location");
                    if (isDraw) {
                        if (entranceDpLocation == null || entranceDpLocation.size() < 3) {
                            throw new Exception("该区域画完映射以后，entrance_dp_location为空！");
                        }
                    } else {
                        if (!(entranceDpLocation == null || entranceDpLocation.size() == 0)) {
                            throw new Exception("删除该出入口映射以后，entrance_dp_location没有清空！");
                        }
                    }

                }

            }
        }

        Assert.assertEquals(isExistRes, isExist, "是否期待该出入口设备存在，期待：" + isExist + ",实际：" + isExistRes);
    }

    private void checkGetEntrance(String response, String entranceType, String entranceName) {

        JSONObject data = JSON.parseObject(response).getJSONObject("data");

        String entranceNameRes = data.getString("entrance_name");
        Assert.assertEquals(entranceNameRes, entranceName, "出入口详情--出入口名称不符，期待：" + entranceName + ", 实际：" + entranceNameRes);

        String entranceTypeRes = data.getString("entrance_type");
        Assert.assertEquals(entranceTypeRes, entranceType, "出入口详情--出入口类型不符，期待：" + entranceType + ", 实际：" + entranceTypeRes);


    }

    private void checkListEntrance(String response, String entranceId, String entranceName, String entranceType, boolean isExist) {
        JSONObject data = JSON.parseObject(response).getJSONObject("data");
        JSONArray list = data.getJSONArray("list");

        boolean isExistRes = false;

        for (int i = 0; i < list.size(); i++) {
            JSONObject single = list.getJSONObject(i);
            String entranceIdRes = single.getString("entrance_id");
            if (entranceId.equals(entranceIdRes)) {
                isExistRes = true;
                String entranceTypeRes = single.getString("entrance_type");
                Assert.assertEquals(entranceTypeRes, entranceType, "出入口列表--出入口类型不符，期待：" + entranceType + ", 实际：" + entranceTypeRes);

                String entranceNameRes = single.getString("entrance_name");
                Assert.assertEquals(entranceNameRes, entranceName, "出入口列表--出入口名称不符，期待：" + entranceType + ", 实际：" + entranceTypeRes);


            }
        }

        Assert.assertEquals(isExistRes, isExist, "出入口列表--是否期待存在：" + isExist + ", 实际：" + isExistRes);
    }

    private String getEntranceId(String response) {

        JSONObject data = JSON.parseObject(response).getJSONObject("data");
        String entranceId = data.getString("entrance_id");

        return entranceId;
    }

    public String[] getEntranceType() throws Exception {
        String response = enumEntranceType();
        JSONObject data = JSON.parseObject(response).getJSONObject("data");
        JSONArray list = data.getJSONArray("list");

        String[] types = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            JSONObject single = list.getJSONObject(i);
            String type = single.getString("entrance_type");
            types[i] = type;
        }

        return types;
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

    public String deleteSubject(String subjectId, Case aCase, int step) throws Exception {
        String url = URL_prefix + "/admin/data/subject/" + subjectId;
        String json = "{}";

        String response = deleteRequest(url, json, header);
        sendResAndReqIdToDb(response, aCase, step);
        checkCode(response, StatusCode.SUCCESS, "");
        return response;
    }

    public void deleteSubject(String subjectId) {
        String url = URL_prefix + "/admin/data/subject/" + subjectId;
        String json = "{}";

        try {
            deleteRequest(url, json, header);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String updateSubject(String subjectId, String subjectName, String country, String area, String province,
                                String city, String district, String local, String manager, String phone, Case aCase, int step) throws Exception {
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
        sendResAndReqIdToDb(response, aCase, step);
        checkCode(response, StatusCode.SUCCESS, "");
        return response;
    }

    public String detailSubject(String subjectId, Case aCase, int step) throws Exception {
        String url = URL_prefix + "/admin/data/subject/" + subjectId;

        String response = getRequest(url, header);

        sendResAndReqIdToDb(response, aCase, step);
        checkCode(response, StatusCode.SUCCESS, "");
        return response;
    }

    public String listSubject(Case aCase, int step) throws Exception {
        String url = URL_prefix + "/admin/data/subject/list";
        String json =
                "{\n" +
                        "    \"app_id\":\"" + APP_ID + "\",\n" +
                        "    \"brand_id\":\"" + BRAND_ID + "\",\n" +
                        "    \"page\":1,\n" +
                        "    \"size\":10\n" +
                        "}";

        String response = postRequest(url, json, header);
        sendResAndReqIdToDb(response, aCase, step);
        checkCode(response, StatusCode.SUCCESS, "");
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
        checkCode(response, StatusCode.SUCCESS, "");

        return response;
    }

    public String getSubjectIdByList(String subjectName) throws Exception {
        String response = listSubject();

        String subjectId = checkIsExistByListSubject(response, subjectName, true);

        return subjectId;
    }


    //    业务节点解除关联机器，即解绑定机器
    public String removeNode(String nodeId, String clusterNodeId, Case aCase, int step) throws Exception {
        String url = URL_prefix + "/admin/cluster/node/remove";
        String json =
                "{\n" +
                        "    \"node_id\":\"" + nodeId + "\",\n" +
                        "    \"cluster_node_id\":\"" + clusterNodeId + "\"" +
                        "}";

        String response = deleteRequest(url, json, header);
        sendResAndReqIdToDb(response, aCase, step);
        checkCode(response, StatusCode.SUCCESS, "");
        return response;
    }

    //    查询业务节点是否有绑定集群
//    即已绑定机器列表
    public String bindedList(String nodeId, Case aCase, int step) throws Exception {
        String url = URL_prefix + "/admin/cluster/bind/list";
        String json =
                "{\n" +
                        "    \"node_id\":\"" + nodeId + "\",\n" +
                        "    \"page\":1,\n" +
                        "    \"size\":10\n" +
                        "}";

        String response = postRequest(url, json, header);
        sendResAndReqIdToDb(response, aCase, step);
        checkCode(response, StatusCode.SUCCESS, "");
        return response;
    }

    //    根据业务节点，获取可绑定机器列表
    public String unbindedList(String nodeId, Case aCase, int step) throws Exception {
        String url = URL_prefix + "/admin/cluster/node/un_bind/list";
        String json =
                "{\n" +
                        "    \"node_id\":\"" + nodeId + "\",\n" +
                        "    \"page\":1,\n" +
                        "    \"size\":10\n" +
                        "}";

        String response = postRequest(url, json, header);
        sendResAndReqIdToDb(response, aCase, step);
        checkCode(response, StatusCode.SUCCESS, "");
        return response;
    }

    //    业务节点关联机器，即绑定机器
    public String bindNode(String nodeId, String clusterNodeId, String alias, Case aCase, int step) throws Exception {
        String url = URL_prefix + "/admin/cluster/node/bind";
        String json =
                "{\n" +
                        "    \"node_id\":\"" + nodeId + "\",\n" +
                        "    \"cluster_node_id\":" + clusterNodeId + ",\n" +
                        "    \"alias\":\"" + alias + "\"\n" +
                        "}";

        String response = postRequest(url, json, header);
        sendResAndReqIdToDb(response, aCase, step);
        checkCode(response, StatusCode.SUCCESS, "");
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

////    查询服务列表
//    public String nodeServiceConfig(int serviceId, String nodeId, Case aCase, int step) throws Exception {
//        String url = URL_prefix + "/admin/data/service/list";
//        String json =
//                "{\n" +
//                        "    \"page\":1,\n" +
//                        "    \"size\":1000\n" +
//                        "}";
//
//        String response = postRequest(url, json, header);
//        sendResAndReqIdToDb(response, aCase, step);
//        checkCode(response, StatusCode.SUCCESS, "");
//        return response;
//    }

    //    1、删除服务配置
    public String deleteConfig(String serviceId, Case aCase, int step) throws Exception {
        String url = URL_prefix + "/admin/data/nodeServiceConfig/" + serviceId;
        String json =
                "{}";

        String response = deleteRequest(url, json, header);
        sendResAndReqIdToDb(response, aCase, step);
        checkCode(response, StatusCode.SUCCESS, "");
        return response;
    }

    //    2、根据节点ID查询服务详情
    public String queryByNodeId(String nodeId, Case aCase, int step) throws Exception {
        String url = URL_prefix + "/admin/data/nodeServiceConfig/queryByNodeId/" + nodeId;

        String response = getRequest(url, header);
        sendResAndReqIdToDb(response, aCase, step);
        checkCode(response, StatusCode.SUCCESS, "");
        return response;
    }

    //    3、增加节点对应服务&配置
    public String nodeServiceConfig(String serviceId, String nodeId, Case aCase, int step) throws Exception {
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
        sendResAndReqIdToDb(response, aCase, step);
        checkCode(response, StatusCode.SUCCESS, "");
        return response;
    }

    //4、更新服务配置
    public String updateConfig(String serviceId, boolean autoMerge, boolean autoIncMerge, String nodeId, String grpName,
                               String type, String mode, Case aCase, int step) throws Exception {
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
        sendResAndReqIdToDb(response, aCase, step);
        checkCode(response, StatusCode.SUCCESS, "");
        return response;
    }


    //    查询服务配置详情
    public String getConfig(int serviceId, Case aCase, int step) throws Exception {
        String url = URL_prefix + "/admin/data/nodeServiceConfig/" + serviceId;

        String response = getRequest(url, header);
        sendResAndReqIdToDb(response, aCase, step);
        checkCode(response, StatusCode.SUCCESS, "");
        return response;
    }

    @Test
    public void addSubjectCheck() {
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
            subjectId = getSubjectIdByList(subjectName);

//            2、主体列表
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            String response = listSubject(aCase, step);
            checkIsExistByListSubject(response, subjectName, true);

//            3、主体详情
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            String detailSubject = detailSubject(subjectId, aCase, step);
            checkIsExistByGetSubject(detailSubject, subjectName, true);

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

    @Test
    public void updateSubjectCheck() {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        failReason = "";
        Case aCase = new Case();
        int step = 0;

        String caseName = ciCaseName;
        String caseDesc = "验证更新主体是否成功";
        logger.info(caseDesc + "-----------------------------------------------------------------------------------");

        String subjectName = caseName;
        int subjectType = 3;
        String subjectId = "";

        String subjectNameNew = ciCaseName + "-new";
        String country = "新中国";
        String area = "新华北";
        String province = "新北京";
        String city = "新北京";
        String district = "新海淀";
        String local = "新中关村soho";
        String manager = "new sophie";
        String phone = "17610248107";
        try {

            aCase.setRequestData("1、增加主体-2、更新主体-3、主体列表-4、主体详情" + "\n\n");
            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);

//            1、新增主体
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            addSubject(subjectName, subjectType, aCase, step);
            subjectId = getSubjectIdByList(subjectName);

//            2、更新主体
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            updateSubject(subjectId, subjectNameNew, country, area, province, city, district, local, manager, phone, aCase, step);

//            3、主体列表
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            String response = listSubject(aCase, step);
            checkListSubject(response, subjectId, subjectType, subjectNameNew, area, city,
                    country, district, province, local, phone, true);

//            4、主体详情
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            String detailSubject = detailSubject(subjectId, aCase, step);
            checkGetSubject(detailSubject, subjectId, subjectNameNew, phone, area,
                    country, province, city, district, local);

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

    @Test
    public void deleteSubjectCheck() {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        failReason = "";
        Case aCase = new Case();
        int step = 0;

        String caseName = ciCaseName;
        String caseDesc = "验证更新主体是否成功";
        logger.info(caseDesc + "-----------------------------------------------------------------------------------");

        String subjectName = caseName;
        int subjectType = 0;
        String subjectId = "";

        try {

            aCase.setRequestData("1、增加主体-2、主体列表-3、删除主体-4、主体列表" + "\n\n");
            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);

//            1、新增主体
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            subjectType = getOneSUbjectType();
            addSubject(subjectName, subjectType, aCase, step);
            subjectId = getSubjectIdByList(subjectName);

//            2、主体列表
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            String response = listSubject(aCase, step);
            checkIsExistByListSubject(response, subjectName, true);

//            3、删除主体
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            deleteSubject(subjectId, aCase, step);

//            4、主体列表
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            response = listSubject(aCase, step);
            checkIsExistByListSubject(response, subjectName, false);

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
    public void getbindableListCheck() {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        failReason = "";
        Case aCase = new Case();
        int step = 0;

        String caseName = ciCaseName;
        String caseDesc = "绑定/解绑边缘节点";
        logger.info(caseDesc + "-----------------------------------------------------------------------------------");

        try {

            aCase.setRequestData("1、解绑机器-2、查询业务节点是否有绑定集群-3、根据业务节点，获取可绑定机器列表-4、绑定机器-" +
                    "5、查询业务节点是否有绑定集群-6、根据业务节点，获取可绑定机器列表" + "\n\n");
            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);

//            1、解绑机器
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            removeNode(SHOP_Id, CLUSTER_NODE_Id, aCase, step);

//            2、查询业务节点是否有绑定集群
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            String response = bindedList(SHOP_Id, aCase, step);
            checkBindedList(response, CLUSTER_NODE_Id, false);

//            3、根据业务节点，获取可绑定机器列表
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            response = unbindedList(SHOP_Id, aCase, step);
            checkUnbindedList(response, CLUSTER_NODE_Id, true);

//            4、绑定机器
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            bindNode(SHOP_Id, CLUSTER_NODE_Id, ALIAS, aCase, step);

//            5、查询业务节点是否有绑定集群
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            response = bindedList(SHOP_Id, aCase, step);
            checkBindedList(response, CLUSTER_NODE_Id, true);

//            6、根据业务节点，获取可绑定机器列表
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            response = unbindedList(SHOP_Id, aCase, step);
            checkUnbindedList(response, CLUSTER_NODE_Id, false);

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
    public void NodeConfigCheck() {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        failReason = "";
        Case aCase = new Case();
        int step = 0;

        String caseName = ciCaseName;
        String caseDesc = "增加/删除节点对应服务";
        logger.info(caseDesc + "-----------------------------------------------------------------------------------");

        String subjectName = ciCaseName;
        int subjectType = 0;

        String subjectId = "";

        try {

            aCase.setRequestData("1、主体新增-2、根据节点ID查询服务详情-3、删除服务配置-4、根据节点ID查询服务详情-" +
                    "5、增加节点对应服务&配置-6、根据节点ID查询服务详情-7、更新服务配置-8、根据节点ID查询服务详情" + "\n\n");
            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);

//            1、主体新增
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            subjectType = getOneSUbjectType();
            addSubject(subjectName, subjectType, aCase, step);
            subjectId = getSubjectIdByList(subjectName);

//            2、根据节点ID查询服务详情
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            String response = queryByNodeId(subjectId, aCase, step);
            String acquaintanceId = getIdByQueryService(response, serviceAcquaintance);
            String passengerId = getIdByQueryService(response, servicePassenger);

//            3、删除服务配置
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            deleteConfig(acquaintanceId, aCase, step);

//            4、根据节点ID查询服务详情
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            response = queryByNodeId(subjectId, aCase, step);
            checkSeviceList(response, serviceAcquaintance, false);
            checkSeviceList(response, servicePassenger, true);

//            5、增加节点对应服务&配置
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            nodeServiceConfig(serviceAcquaintance, subjectId, aCase, step);

//            6、根据节点ID查询服务详情
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            response = queryByNodeId(subjectId, aCase, step);
            checkSeviceList(response, serviceAcquaintance, true);
            checkSeviceList(response, servicePassenger, true);

//            7、更新服务配置
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            updateConfig(serviceAcquaintance, false, false, subjectId, "vipGrp", "DEFINE", "WEEK", aCase, step);

//            8、根据节点ID查询服务详情
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            checkSeviceList(response, serviceAcquaintance, true);
            checkSeviceList(response, servicePassenger, true);

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

    public String getIdByQueryService(String response, String serviceId) throws Exception {
        JSONArray list = JSON.parseObject(response).getJSONObject("data").getJSONArray("list");

        String id = "";
        boolean isExist = false;
        for (int i = 0; i < list.size(); i++) {
            JSONObject single = list.getJSONObject(i);
            String serviceIdRes = single.getString("service_id");
            if (serviceId.equals(serviceIdRes)) {
                isExist = true;
                id = single.getString("id");
            }
        }

        if (!isExist) {
            throw new Exception("不存在该服务！");
        }

        return id;
    }

    private void checkSeviceList(String response, String serviceId, boolean isExist) {
        JSONArray list = JSON.parseObject(response).getJSONObject("data").getJSONArray("list");

        boolean isExistRes = false;

        for (int i = 0; i < list.size(); i++) {
            JSONObject single = list.getJSONObject(i);
            String serviceIdRes = single.getString("service_id");
            if (serviceId.equals(serviceIdRes)) {
                isExistRes = true;
            }
        }

        Assert.assertEquals(isExistRes, isExist, "是否期待存在该服务，期待：" + isExist + ", 实际：" + isExistRes);
    }

    private void checkUnbindedList(String response, String id, boolean isExist) {
        JSONArray list = JSON.parseObject(response).getJSONObject("data").getJSONArray("list");

        boolean isExistRes = false;

        for (int i = 0; i < list.size(); i++) {
            JSONObject single = list.getJSONObject(i);

            String idRes = single.getString("id");
            if (id.equals(idRes)) {
                isExistRes = true;
            }
        }
        Assert.assertEquals(isExistRes, isExist, "是否期待存在该边缘节点，期待：" + isExist + ", 实际：" + isExistRes);
    }

    private void checkBindedList(String response, String id, boolean isExist) throws Exception {

        JSONObject clusterNodeList = JSON.parseObject(response).getJSONObject("data").getJSONObject("cluster_node_list");

        JSONArray list = clusterNodeList.getJSONArray("list");

        String function = "查询业务节点是否有绑定集群";
        boolean isExistRes = false;

        for (int i = 0; i < list.size(); i++) {
            JSONObject single = list.getJSONObject(i);
            String idRes = single.getString("id");
            if (id.equals(idRes)) {
                isExistRes = true;
                checkKeyValue(function, single, "memory", "", false);
                checkKeyValue(function, single, "free_cpu", "", false);
                checkKeyValue(function, single, "ip", "", false);
                checkKeyValue(function, single, "free_memory", "", false);
                checkKeyValue(function, single, "node_name", "", false);
                checkKeyValue(function, single, "cpu", "", false);
                checkKeyValue(function, single, "agent_type", "", false);
                checkKeyValue(function, single, "cluster_id", "", false);
                checkKeyValue(function, single, "alias", "", false);
                checkKeyValue(function, single, "status", "", false);
            }
        }

        Assert.assertEquals(isExistRes, isExist, "是否期待存在该边缘节点，期待：" + isExist + ", 实际：" + isExistRes);
    }

    private void checkListSubject(String response, String subjectId, int subjectType, String subjectName, String area, String city,
                                  String country, String district, String province, String local, String phone, boolean isExist) throws Exception {

        JSONObject data = JSON.parseObject(response).getJSONObject("data");

        JSONArray list = data.getJSONArray("list");

        String function = "主体列表";
        boolean isExistRes = false;

        for (int i = 0; i < list.size(); i++) {
            JSONObject single = list.getJSONObject(i);
            String subjectIdRes = single.getString("subject_id");
            if (subjectId.equals(subjectIdRes)) {
                isExistRes = true;
                checkKeyValue(function, single, "subject_name", subjectName, true);
                checkKeyValue(function, single, "type_id", subjectType, true);
                checkKeyValue(function, single, "local", local, true);
                checkKeyValue(function, single, "telephone", phone, true);
                checkKeyValue(function, single, "app_id", APP_ID, true);

                String regionRes = single.getString("region");
                JSONObject regionJo = JSON.parseObject(regionRes);
                checkKeyValue(function, regionJo, "area", area, true);
                checkKeyValue(function, regionJo, "city", city, true);
                checkKeyValue(function, regionJo, "country", country, true);
                checkKeyValue(function, regionJo, "district", district, true);
                checkKeyValue(function, regionJo, "province", province, true);
            }
        }

        Assert.assertEquals(isExistRes, isExist, "是否期待存在该主体，期待：" + isExist + ", 实际：" + isExistRes);
    }

    private void checkGetSubject(String response, String subjectId, String subjectName, String phone, String area,
                                 String country, String province, String city, String district, String local) throws Exception {
        JSONObject data = JSON.parseObject(response).getJSONObject("data");

        String function = "主体详情";
        if (data == null || data.size() == 0) {
            throw new Exception("主体详情信息为空！");
        }

        checkKeyValue(function, data, "subject_id", subjectId, true);
        checkKeyValue(function, data, "subject_name", subjectName, true);
        checkKeyValue(function, data, "local", local, true);
        checkKeyValue(function, data, "telephone", phone, true);

        JSONObject region = data.getJSONObject("region");
        checkKeyValue(function, region, "area", area, true);
        checkKeyValue(function, region, "city", city, true);
        checkKeyValue(function, region, "country", country, true);
        checkKeyValue(function, region, "district", district, true);
        checkKeyValue(function, region, "province", province, true);
    }

    public String checkIsExistByListSubject(String response, String subjectName, boolean isExist) {
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

    public void checkIsExistByGetSubject(String response, String subjectName, boolean isExist) {
        String subjectId = "";
        boolean isExistRes = false;
        JSONObject data = JSON.parseObject(response).getJSONObject("data");

        String subjectNameRes = data.getString("subject_name");
        if (subjectName.equals(subjectNameRes)) {
            isExistRes = true;
        }

        Assert.assertEquals(isExistRes, isExist, "expect isExist: " + isExist + ", acatual: " + isExistRes);
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

    //    1、增加应用
    public String addApp(String appName, Case aCase, int step) throws Exception {
        String url = URL_prefix + "/admin/data/app/";
        String json =
                "{\n" +
                        "    \"name\":\"" + appName + "\",\n" +
                        "    \"uid\":\"" + UID + "\"\n" +
                        "}";

        String response = postRequest(url, json, header);
        sendResAndReqIdToDb(response, aCase, step);
        checkCode(response, StatusCode.SUCCESS, "");
        return response;
    }

    //    2、更新应用
    public String updateApp(String appId, String appName, String phone, Case aCase, int step) throws Exception {
        String url = URL_prefix + "/admin/data/app/" + appId;
        String json =
                "{\n" +
                        "    \"name\":\"" + appName + "\",\n" +
                        "    \"telephone\":\"" + phone + "\"\n" +
                        "}";

        String response = putRequest(url, json, header);
        sendResAndReqIdToDb(response, aCase, step);
        checkCode(response, StatusCode.SUCCESS, "");
        return response;
    }

    //    3、删除应用
    public String deleteApp(String appId, Case aCase, int step) throws Exception {
        String url = URL_prefix + "/admin/data/app/" + appId;

        String json = "{}";
        String response = deleteRequest(url, json, header);
        sendResAndReqIdToDb(response, aCase, step);
        checkCode(response, StatusCode.SUCCESS, "");
        return response;
    }

    public String deleteApp(String appId) {
        String url = URL_prefix + "/admin/data/app/" + appId;

        String json = "{}";
        String response = null;
        try {
            response = deleteRequest(url, json, header);
            checkCode(response, StatusCode.SUCCESS, "");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    //    4、查询应用详情
    public String getApp(String appId, Case aCase, int step) throws Exception {
        String url = URL_prefix + "/admin/data/app/" + appId;

        String response = getRequest(url, header);
        sendResAndReqIdToDb(response, aCase, step);
        checkCode(response, StatusCode.SUCCESS, "");
        return response;
    }


    //    5、查询应用列表
    public String listApp(Case aCase, int step) throws Exception {
        String url = URL_prefix + "/admin/data/app/list";
        String json =
                "{\n" +
                        "    \"page\":1,\n" +
                        "    \"size\":100\n" +
                        "}";

        String response = postRequest(url, json, header);
        sendResAndReqIdToDb(response, aCase, step);
        checkCode(response, StatusCode.SUCCESS, "");
        return response;
    }

    @Test
    public void addAppCheck() {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        failReason = "";
        Case aCase = new Case();
        int step = 0;

        String caseName = ciCaseName;
        String caseDesc = "验证新增应用是否成功";
        logger.info(caseDesc + "-----------------------------------------------------------------------------------");

        String appName = caseName;
        String appId = "";
        try {

            aCase.setRequestData("1、增加应用-2、查询应用列表-3、查询应用详情" + "\n\n");
            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);

//            1、新增应用
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            String response = addApp(appName, aCase, step);
            appId = getAppId(response);

//            2、应用列表
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            response = listApp(aCase, step);
            checkIsExistListApp(response, appId, true);

//            3、应用详情
            response = getApp(appId, aCase, step);
            checkGetApp(response, appId, "", true);

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
            deleteApp(appId);

            if (!IS_DEBUG) {
                qaDbUtil.saveToCaseTable(aCase);
            }
        }
    }

    @Test
    public void updateAppCheck() {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        failReason = "";
        Case aCase = new Case();
        int step = 0;

        String caseName = ciCaseName;
        String caseDesc = "验证编辑应用是否成功";
        logger.info(caseDesc + "-----------------------------------------------------------------------------------");

        String appName = caseName;
        String appId = "";
        try {

            aCase.setRequestData("1、增加应用-2、编辑应用-3、查询应用详情" + "\n\n");
            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);

//            1、新增应用
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            String response = addApp(appName, aCase, step);
            appId = getAppId(response);

//            2、应用列表
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            response = listApp(aCase, step);
            checkIsExistListApp(response, appId, true);

//            3、删除应用
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            deleteApp(appId);

//            2、应用列表
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            response = listApp(aCase, step);
            checkIsExistListApp(response, appId, false);

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
    public void deleteAppCheck() {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        failReason = "";
        Case aCase = new Case();
        int step = 0;

        String caseName = ciCaseName;
        String caseDesc = "验证删除应用是否成功";
        logger.info(caseDesc + "-----------------------------------------------------------------------------------");

        String appName = caseName;
        String appNameNew = caseName + "-new";
        String phone = "17610248107";
        String appId = "";
        try {

            aCase.setRequestData("1、增加应用-2、编辑应用-3、查询应用详情" + "\n\n");
            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);

//            1、新增应用
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            String response = addApp(appName, aCase, step);
            appId = getAppId(response);

//            2、编辑应用
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            updateApp(appId, appNameNew, phone, aCase, step);

//            3、应用详情
            response = getApp(appId, aCase, step);
            checkGetApp(response, appId, phone, true);

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
            deleteApp(appId);

            if (!IS_DEBUG) {
                qaDbUtil.saveToCaseTable(aCase);
            }
        }
    }

    @Test
    public void getAppDSCheck() {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        failReason = "";
        Case aCase = new Case();
        int step = 0;

        String caseName = ciCaseName;
        String caseDesc = "验证应用详情数据结构";
        logger.info(caseDesc + "-----------------------------------------------------------------------------------");

        String appId = APP_ID;
        String appName = "【接口测试】专用应用";
        String uidName = "test";
        String company = "winsense";
        String createTime = "1561968247000";
        String creatorName = "廖祥茹";
        String phone = "17610248107";
        try {

            aCase.setRequestData("1、应用详情" + "\n\n");
            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);

//            1、应用详情
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            String response = getApp(appId, aCase, step);
            checkGetAppDS(response, appId, appName, uidName, company, createTime, creatorName, phone);

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

    private void checkGetAppDS(String response, String appId, String appName, String uidName, String company,
                               String createTime, String creatorName, String phone) throws Exception {
        String function = "应用详情";

        JSONObject data = JSON.parseObject(response).getJSONObject("data");

        checkKeyValue(function, data, "name", appName, true);
        checkKeyValue(function, data, "uid_name", uidName, true);
        checkKeyValue(function, data, "company", company, true);
        checkKeyValue(function, data, "gmt_create", createTime, true);
        checkKeyValue(function, data, "telephone", phone, true);
        checkKeyValue(function, data, "ak", "", false);
        checkKeyValue(function, data, "sk", "", false);
        checkKeyKeyValue(function, data, "creator_name", creatorName, true);
    }

    private void checkGetApp(String response, String appId, String phone, boolean isExist) throws Exception {
        JSONObject data = JSON.parseObject(response).getJSONObject("data");

        String function = "应用详情";
        boolean isExistRes = false;
        String appIdRes = data.getString("app_id");
        if (appId.equals(appIdRes)) {
            isExistRes = true;

            if (!"".equals(phone)) {
                checkKeyValue(function, data, "telephone", phone, true);
            }
        }
        Assert.assertEquals(isExistRes, isExist, "是否期待存在该应用，期待：" + isExist + ", 实际：" + isExistRes);
    }

    private void checkIsExistListApp(String response, String appId, boolean isExist) {
        JSONObject data = JSON.parseObject(response).getJSONObject("data");
        JSONArray list = data.getJSONArray("list");

        boolean isExistRes = false;
        for (int i = 0; i < list.size(); i++) {
            JSONObject single = list.getJSONObject(i);
            String appIdRes = single.getString("app_id");
            if (appId.equals(appIdRes)) {
                isExistRes = true;
            }
        }

        Assert.assertEquals(isExistRes, isExist, "是否期待存在该应用，期待：" + isExist + ", 实际：" + isExistRes);

    }

    private String getAppId(String response) {
        JSONObject data = JSON.parseObject(response).getJSONObject("data");

        String appId = data.getString("app_id");

        return appId;
    }

    //    1、新建品牌
    public String addBrand(String appId, String brandName, String manager, String phone, Case aCase, int step) throws Exception {
        String url = URL_prefix + "/admin/data/brand/";
        String json =
                "{\n" +
                        "    \"brand_name\":\"" + brandName + "\",\n" +
                        "    \"app_id\":\"" + appId + "\",\n" +
                        "    \"manager\":\"" + manager + "\",\n" +
                        "    \"telephone\":\"" + phone + "\"\n" +
                        "}";

        String response = postRequest(url, json, header);
        sendResAndReqIdToDb(response, aCase, step);
        checkCode(response, StatusCode.SUCCESS, "");
        return response;
    }

    //    2、删除品牌
    public String deleteBrand(String brandId, Case aCase, int step) throws Exception {
        String url = URL_prefix + "/admin/data/brand/" + brandId;
        String json =
                "{}";

        String response = deleteRequest(url, json, header);
        sendResAndReqIdToDb(response, aCase, step);
        checkCode(response, StatusCode.SUCCESS, "");
        return response;
    }

    public String deleteBrand(String brandId) {
        String url = URL_prefix + "/admin/data/brand/" + brandId;
        String json =
                "{}";

        String response = null;
        try {
            response = deleteRequest(url, json, header);
            checkCode(response, StatusCode.SUCCESS, "");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }


    //    3、更新品牌
    public String updateBrand(String brandId, String brandName, String manager, String phone, Case aCase, int step) throws Exception {
        String url = URL_prefix + "/admin/data/brand/" + brandId;
        String json =
                "{\n" +
                        "    \"brand_name\":\"" + brandName + "\",\n" +
                        "    \"manager\":\"" + manager + "\",\n" +
                        "    \"telephone\":\"" + phone + "\"\n" +
                        "}";

        String response = putRequest(url, json, header);
        sendResAndReqIdToDb(response, aCase, step);
        checkCode(response, StatusCode.SUCCESS, "");
        return response;
    }

    //    4、品牌详情
    public String getBrand(String brandId, Case aCase, int step) throws Exception {
        String url = URL_prefix + "/admin/data/brand/" + brandId;

        String response = getRequest(url, header);
        sendResAndReqIdToDb(response, aCase, step);
        checkCode(response, StatusCode.SUCCESS, "");
        return response;
    }

    //    5、品牌列表
    public String listBrand(String appId, Case aCase, int step) throws Exception {
        String url = URL_prefix + "/admin/data/brand/list";

        String json = genListBrandJson(appId);
        String response = postRequest(url, json, header);
        sendResAndReqIdToDb(response, aCase, step);
        checkCode(response, StatusCode.SUCCESS, "");
        return response;
    }

    private String genListBrandJson(String appId) {
        String json =
                "{\n" +
                        "    \"app_id\":\"" + appId + "\",\n" +
                        "    \"page\":1,\n" +
                        "    \"size\":100\n" +
                        "}";
        return json;
    }

    public String listBrandForBrandId(String appId) throws Exception {
        String url = URL_prefix + "/admin/data/brand/list";

        String json = genListBrandJson(appId);
        String response = postRequest(url, json, header);
        checkCode(response, StatusCode.SUCCESS, "");
        return response;
    }

    @Test
    public void addBrandCheck() throws Exception {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        failReason = "";
        Case aCase = new Case();
        int step = 0;

        String caseName = ciCaseName;
        String caseDesc = "验证新增品牌是否成功";
        logger.info(caseDesc + "-----------------------------------------------------------------------------------");

        String brandName = caseName;
        String manager = "sophie";
        String phone = "17610248107";

        String brandId = "";
        try {

            aCase.setRequestData("1、增加品牌-2、品牌列表-3、品牌详情" + "\n\n");
            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);

//            1、新增品牌
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            addBrand(APP_ID, brandName, manager, phone, aCase, step);

//            2、品牌列表
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            String response = listBrand(APP_ID, aCase, step);
            brandId = getBrandIdBylist(response, brandName, true);

//            3、品牌详情
            response = getBrand(brandId, aCase, step);
            checkIsExistByGetBrand(response, brandId, true);

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
            deleteBrand(brandId);

            if (!IS_DEBUG) {
                qaDbUtil.saveToCaseTable(aCase);
            }
        }
    }

    @Test
    public void updateBrandCheck() throws Exception {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        failReason = "";
        Case aCase = new Case();
        int step = 0;

        String caseName = ciCaseName;
        String caseDesc = "验证编辑品牌是否成功";
        logger.info(caseDesc + "-----------------------------------------------------------------------------------");

        String brandName = caseName;
        String manager = "sophie";
        String phone = "15165153865";

        String brandNameNew = caseName + "-new";
        String managerNew = "sophie" + "-new";
        String phoneNew = "17610248107";

        String brandId = "";
        try {

            aCase.setRequestData("1、增加品牌-2、编辑品牌-3、品牌列表-4、品牌详情" + "\n\n");
            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);

//            1、新增品牌
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            addBrand(APP_ID, brandName, manager, phone, aCase, step);

            String response = listBrandForBrandId(APP_ID);
            brandId = getBrandIdBylist(response, brandName, true);

//            2、编辑品牌
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            updateBrand(brandId, brandNameNew, managerNew, phoneNew, aCase, step);

//            3、品牌列表
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            response = listBrand(APP_ID, aCase, step);
            CheckListBrand(response, brandId, brandNameNew, managerNew, phoneNew);

//            4、品牌详情
            response = getBrand(brandId, aCase, step);
            checkGetBrand(response, brandId, brandNameNew, managerNew, phoneNew);

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
            deleteBrand(brandId);

            if (!IS_DEBUG) {
                qaDbUtil.saveToCaseTable(aCase);
            }
        }
    }

    @Test
    public void deleteBrandCheck() throws Exception {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        failReason = "";
        Case aCase = new Case();
        int step = 0;

        String caseName = ciCaseName;
        String caseDesc = "验证删除品牌是否成功";
        logger.info(caseDesc + "-----------------------------------------------------------------------------------");

        String brandName = caseName;
        String manager = "sophie";
        String phone = "15165153865";

        String brandId = "";
        try {

            aCase.setRequestData("1、增加品牌-2、品牌列表-3、删除品牌-4、品牌列表" + "\n\n");
            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);

//            1、新增品牌
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            addBrand(APP_ID, brandName, manager, phone, aCase, step);

//            2、品牌列表
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            String response = listBrand(APP_ID, aCase, step);
            brandId = getBrandIdBylist(response, brandName, true);

//            3、删除品牌
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            deleteBrand(brandId, aCase, step);

//            4、品牌列表
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            response = listBrand(APP_ID, aCase, step);
            brandId = getBrandIdBylist(response, brandName, false);

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
    public void getBrandDSCheck() throws Exception {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        failReason = "";
        Case aCase = new Case();
        int step = 0;

        String caseName = ciCaseName;
        String caseDesc = "验证品牌详情返回的数据结构";
        logger.info(caseDesc + "-----------------------------------------------------------------------------------");

        String brandId = "638";
        String brandName = "【接口测试】专用品牌";
        String createTime = "1561968876000";
        String creatorName = "管理员";
        String manager = "索菲";
        String phone = "17610248107";

        try {

            aCase.setRequestData("1、品牌详情" + "\n\n");
            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);

//            1、品牌详情
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            String response = getBrand(brandId, aCase, step);
            checkGetBrandDS(response, brandId, brandName, createTime, creatorName, manager, phone);

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

    private void checkGetBrandDS(String response, String brandId, String brandName, String createTime, String creatorName,
                                 String manager, String phone) throws Exception {
        String function = "品牌详情";
        JSONObject data = JSON.parseObject(response).getJSONObject("data");
        checkGetBrand(response, brandId, brandName, manager, phone);
        checkKeyValue(function, data, "gmt_create", createTime, true);
        checkKeyKeyValue(function, data, "creator_name", creatorName, true);

    }

    private void checkGetBrand(String response, String brandId, String brandName, String manager, String phone) throws Exception {

        String function = "品牌详情";
        JSONObject data = JSON.parseObject(response).getJSONObject("data");
        checkKeyValue(function, data, "brand_id", brandId, true);
        checkKeyValue(function, data, "brand_name", brandName, true);
        checkKeyValue(function, data, "manager", manager, true);
        checkKeyValue(function, data, "telephone", phone, true);
    }

    private void CheckListBrand(String response, String brandId, String brandName, String manager, String phone) throws Exception {
        JSONArray list = JSON.parseObject(response).getJSONObject("data").getJSONArray("list");

        String function = "品牌列表";
        boolean isExist = false;

        for (int i = 0; i < list.size(); i++) {
            JSONObject single = list.getJSONObject(i);
            String brandIdRes = single.getString("brand_id");
            if (brandId.equals(brandIdRes)) {
                isExist = true;
                checkKeyValue(function, single, "brand_name", brandName, true);
                checkKeyValue(function, single, "manager", manager, true);
                checkKeyValue(function, single, "telephone", phone, true);
            }
        }

        if (!isExist) {
            throw new Exception("该品牌不存在！");
        }

    }

    private void checkIsExistByGetBrand(String response, String brandId, boolean isExist) {
        JSONObject data = JSON.parseObject(response).getJSONObject("data");
        String brandIdRes = data.getString("brand_id");

        boolean isExistRes = false;
        if (brandId.equals(brandIdRes)) {
            isExistRes = true;
        }

        Assert.assertEquals(isExistRes, isExist, "是否期待存在该品牌，期待：" + isExist + ", 实际：" + isExistRes);
    }

    private String getBrandIdBylist(String response, String brandName, boolean isExist) {
        JSONObject data = JSON.parseObject(response).getJSONObject("data");
        JSONArray list = data.getJSONArray("list");
        String brandId = "";

        boolean isExistRes = false;
        for (int i = 0; i < list.size(); i++) {
            JSONObject single = list.getJSONObject(i);

            String brandNameRes = single.getString("brand_name");
            if (brandName.equals(brandNameRes)) {
                isExistRes = true;
                brandId = single.getString("brand_id");
            }
        }

        Assert.assertEquals(isExistRes, isExist, "是否期待存在该品牌，期待：" + isExist + ", 实际：" + isExistRes);

        return brandId;
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
                        "deviceId", "\"device_id\":\"" + BATCH_START_DEVICE_ID_1 + "\""
                },
                new Object[]{
                        "name", "\"name\":\"" + DEVICE_NAME_1 + "\""
                },
                new Object[]{
                        "sceneType", "\"scene_type\":\"" + "COMMON" + "\""
                }
        };
    }

    @DataProvider(name = "ADD_UPDATE_ENTRANCE")
    public static Object[][] AddUpdateEntrance() {
        //entranceType,addIsdraw,updateIsDraw, isRegion

        return new Object[][]{
                new Object[]{
                        "REGION_ENTER", false, false, false
                },
                new Object[]{
                        "REGION", false, false, true
                },
                new Object[]{
                        "REGION_ENTER", false, true, false
                },
                new Object[]{
                        "REGION", false, true, true
                },
                new Object[]{
                        "REGION_ENTER", true, false, false
                },
                new Object[]{
                        "REGION", true, false, true
                },
                new Object[]{
                        "REGION_ENTER", true, true, false
                },
                new Object[]{
                        "REGION", true, true, true
                }
        };
    }

    @DataProvider(name = "ADD_REGION")
    public static Object[] addRegion() {
        return new Object[]{
                true, false
        };
    }

    @DataProvider(name = "NON_LAYOUT_DEVICE")
    public static Object[][] nonLayoutDevice() {
        //entranceType,addIsdraw,updateIsDraw, isRegion

        return new Object[][]{
                new Object[]{
                        DEVICE_ID_MAPPING, StatusCode.UNKNOWN_ERROR
                },
                new Object[]{
                        REGION_DEVICE_2, StatusCode.SUCCESS
                },

                new Object[]{
                        "6872037729895424", StatusCode.SUCCESS//没有绑定任何平面的设备
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