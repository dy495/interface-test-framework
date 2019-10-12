package com.haisheng.framework.testng.managePlatform;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.model.bean.Case;
import com.haisheng.framework.testng.CommonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.util.HttpExecutorUtil;
import com.haisheng.framework.util.QADbUtil;
import com.haisheng.framework.util.StatusCode;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Random;

public class ManagePlatform {

    private org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());
    boolean IS_DEBUG = true;

    private String DeviceUrl = "rtsp://admin:winsense2018@192.168.50.155";
    private String UID = "uid_04e816df";
    private String APP_ID = "23824ed85698";
    private String BRAND_ID = "3096";
    private String SHOP_Id = "3097";
    private static String DEVICE_ID_1 = "6849617556603904";
    private static String DEVICE_ID_2 = "6849618737103872";
    private String LAYOUT_ID = "646";
    private String REGION_ID = "648";
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
    public void listDeviceDiffConditionCheck(String condition) throws Exception {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        failReason = "";
        Case aCase = new Case();
        int step = 0;

        String caseName = ciCaseName;
        String caseDesc = "验证查询设备列表不同的搜索条件";
        logger.info(caseDesc + "-----------------------------------------------------------------------------------");

        try {
            aCase.setRequestData("1、启动设备-2、查询设备列表-3、停止设备" + "\n\n");
            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);

//            1、启动设备
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            startDevice(DEVICE_ID_2, aCase, step);

            if (condition.contains("RUNNING")) {
                Thread.sleep(60 * 1000);
            }

//            2、查询设备列表
            logger.info("\n\n");
            logger.info("------------------------------" + (++step) + "--------------------------------------");
            String response = listDeviceDiffCondition(SHOP_Id, condition, aCase, step);

            checkisExistByListDevice(response, DEVICE_ID_2, true);
            checkisExistByListDevice(response, DEVICE_ID_1, false);

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


//    ------------------------------------------主体管理模块----------------------------------------

    public String addSubject(String subjectName, String subjectType, Case aCase, int step) throws Exception {
        String url = URL_prefix + "/admin/data/subject/";
        String json =
                "{\n" +
                        "    \"brand_id\":\"" + BRAND_ID + "\",\n" +
                        "    \"subject_type\":" + subjectType + ",\n" +
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
    private static Object[] condition() {
        return new String[]{
                "\"device_type\":\"AI_CAMERA\" ",
                "\"device_id\":\"" + DEVICE_ID_2 + "\"",
                "\"name\":\"" + "管理后台测试二【勿动】" + "\"",
                "\"device_status\":\"" + "RUNNING" + "\"",
                "\"scene_type\":\"" + "COMMON" + "\"",
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