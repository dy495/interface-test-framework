package com.haisheng.framework.testng.custemorGateTest;


import com.alibaba.fastjson.JSON;
import com.haisheng.framework.dao.ICaseDao;
import com.haisheng.framework.model.bean.Case;
import com.haisheng.framework.testng.CommonDataStructure.*;
import com.haisheng.framework.util.*;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ai.winsense.ApiClient;
import ai.winsense.common.Credential;
import ai.winsense.constant.SdkConstant;
import ai.winsense.model.ApiRequest;
import ai.winsense.model.ApiResponse;

import java.sql.Timestamp;
import java.util.*;


import java.io.File;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @author yuhaisheng
 * @date 2019-03-14
 **/

public class PVTestCloud {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private LogMine logMine       = new LogMine(logger);
    private SqlSession sqlSession = null;
    private String START_TIME     = "123456789";
    private String END_TIME       = "987654321";
    private String UID            = "uid_e0d1ebec";
    private String APP_ID         = "a4d4d18741a8";
    private String SHOP_ID        = "134";
    private String RE_ID          = "144";
    private String DEVICE_ID      = "6254834559910912";
    private int SLEEP_MS          = 3*1000;
    private int SLEEP_LONG        = 5*1000;
    private ICaseDao caseDao      = null;
    private String request        = "";
    private String response       = "";
    private PvInfo expect         = null;
    private boolean IS_SUCCESS    = true;


//    @Test
    public void testQUERY_CUSTOMER_STATISTICS() throws Exception{
        String requestId = "";
        boolean result = true;
        String caseName = "testQUERY_CUSTOMER_STATISTICS";

        try {
            logMine.logCase(caseName);
            String jsonDir = "src/main/resources/test-res-repo/pv-post/cloud-pv-valid-scenario";
            FileUtil fileUtil = new FileUtil();
            List<File> files = fileUtil.getFiles(jsonDir, ".json");
            for (File file : files) {
                logger.debug("file: " + file.getName());
                String startTime = getCurrentHourBegin();
                String startDayTime = getCurrentDayBegin();
                String endTime = getCurrentHourEnd();
                requestId = UUID.randomUUID().toString();

                PvInfo existedBefore = apiCustomerRequest("/business/customer/QUERY_CUSTOMER_STATISTICS/v1.1", startDayTime, endTime);
                PvInfo currentAdd = getCurrentTestPvInfo(file, startTime, endTime, requestId);

                apiSdkPostToCloud(file, "/scenario/who/ANALYSIS_PERSON/v1.0", startTime, requestId);
                Thread.sleep(SLEEP_MS);

                PvInfo resultPvInfo = apiCustomerRequest("/business/customer/QUERY_CUSTOMER_STATISTICS/v1.1", startDayTime, endTime);
//                PvInfo resultPvInfo = apiCustomerRequest("/business/customer/QUERY_CURRENT_CUSTOMER_STATISTICS/v1.1", startTime, endTime);
                result = checkTestResult(existedBefore, currentAdd, resultPvInfo);
                String expectStr = "QA-CUSTOMIZED: " + JSON.toJSONString(expect);
                String actualStr = JSON.toJSONString(resultPvInfo);
                saveCaseToDb(caseName, request, response, expectStr, result);
                if (!result) {
                    String msg = "request id: " + requestId + "\nPV数据统计测试, 上传的数据和最新获取的PV数据不同"
                            + "\nExpect: " + expectStr
                            + "\nActual: " + actualStr;
                    throw new Exception(msg);
                }
            }

            logger.info("get " + files.size() + " files");
        } catch (Exception e) {
            logger.error(e.toString());
            IS_SUCCESS = false;
            throw e;
        }
    }

//    @Test
    public void testQUERY_CURRENT_CUSTOMER_STATISTICS() throws Exception{
        String requestId = "";
        boolean result = true;
        String caseName = "testQUERY_CURRENT_CUSTOMER_STATISTICS";


        try {
            logMine.logCase(caseName);
            String jsonDir = "src/main/resources/test-res-repo/pv-post/cloud-pv-valid-scenario";
            FileUtil fileUtil = new FileUtil();
            List<File> files = fileUtil.getFiles(jsonDir, ".json");
            for (File file : files) {
                logger.debug("file: " + file.getName());
                String startTime = getCurrentHourBegin();
                String startDayTime = getCurrentDayBegin();
                String endTime = getCurrentHourEnd();
                requestId = UUID.randomUUID().toString();

                PvInfo existedBefore = apiCustomerRequest("/business/customer/QUERY_CURRENT_CUSTOMER_STATISTICS/v1.1");
                PvInfo currentAdd = getCurrentTestPvInfo(file, startTime, endTime, requestId);

                //apiSdkPostToCloud(file, "/scenario/who/ANALYSIS_PERSON/v1.0", startTime, requestId);
//                Thread.sleep(SLEEP_MS);

//                PvInfo resultPvInfo = apiCustomerRequest("/business/customer/QUERY_CURRENT_CUSTOMER_STATISTICS/v1.1", startDayTime, endTime);
////                PvInfo resultPvInfo = apiCustomerRequest("/business/customer/QUERY_CURRENT_CUSTOMER_STATISTICS/v1.1", startTime, endTime);
//                result = checkTestResult(existedBefore, currentAdd, resultPvInfo);
//                String expectStr = "QA-CUSTOMIZED: " + JSON.toJSONString(expect);
//                String actualStr = JSON.toJSONString(resultPvInfo);
//                saveCaseToDb(caseName, request, response, expectStr, result);
//                if (!result) {
//                    String msg = "request id: " + requestId + "\nPV数据统计测试, 上传的数据和最新获取的PV数据不同"
//                            + "\nExpect: " + expectStr
//                            + "\nActual: " + actualStr;
//                    throw new Exception(msg);
//                }
            }

            logger.info("get " + files.size() + " files");
        } catch (Exception e) {
            logger.error(e.toString());
            IS_SUCCESS = false;
            throw e;
        }
    }


    //    @Test(dataProvider = "REID", priority = 0)
    public void invalidRegionAllTest(String regionID) throws Exception{
        //region id 错误，entrance id 正确，pv统计算法可以根据entrance id找到region id并进行记录
        String requestId = "";
        boolean result = true;
        try {
            String caseName = "invalidRegionAllTest-region_id-"+regionID;
            logMine.logCase(caseName);
            String jsonDir = "src/main/resources/test-res-repo/pv-post/cloud-pv-invalid-scenario";
            String fileName = "pv-post-invalid-region.json";
            File file = new File(jsonDir + "/" + fileName);
            logger.info("file: " + file.getName());
            String startTime = getCurrentHourBegin();
            String endTime = getCurrentHourEnd();
            requestId = UUID.randomUUID().toString();

            PvInfo existedBefore = apiCustomerRequest("/business/customer/QUERY_CUSTOMER_STATISTICS/v1.1", startTime, endTime);
            PvInfo currentAdd = getCurrentTestPvInfo(file, startTime, endTime, requestId);

            apiSdkPostToCloud(file, regionID, "/scenario/who/ANALYSIS_PERSON/v1.0", startTime, requestId);
            Thread.sleep(SLEEP_MS);

            PvInfo resultPvInfo = apiCustomerRequest("/business/customer/QUERY_CUSTOMER_STATISTICS/v1.1", startTime, endTime);
            result = checkTestResult(existedBefore, currentAdd, resultPvInfo);
            String expectStr = "QA-CUSTOMIZED: " + JSON.toJSONString(expect);
            String actualStr = JSON.toJSONString(resultPvInfo);
            saveCaseToDb(caseName, request, response, expectStr, result);
            if (!result) {
//                dingdingAlarm("PV数据统计测试", "region id 错误，entrance id 正确。\n\n期望: PV数统计增加 \n结果：最新的PV数与期望不符", requestId, "@刘峤");
                String msg = "request id: " + requestId
                        + "\nregion id 错误，entrance id 正确。\n期望: PV数统计增加, 结果：最新的PV数与期望不符"
                        + "\nExpect: " + expectStr
                        + "\nActual: " + actualStr;
                throw new Exception(msg);
            }
        } catch (Exception e) {
            logger.error(e.toString());
            IS_SUCCESS = false;
//            if (result) {
//                //exception NOT be caused by final result data checking
//                dingdingAlarm("PV数据统计测试", "region id 错误，entrance id 正确。出现Exception", requestId, "@刘峤");
//            }
            throw e;
        }
    }

//    @Test(dataProvider = "REID", priority = 1)
    public void invalidEntranceAllTest(String entranceId) throws Exception{
        //region id 正确，entrance id 错误，pv统计算法将该结果丢弃
        String reIdOrigin = RE_ID;
        String requestId = "";
        boolean result = true;
        try {
            RE_ID = "145";
            String caseName = "invalidEntranceAllTest-entrance_id-"+entranceId;
            logMine.logCase(caseName);
            String jsonDir = "src/main/resources/test-res-repo/pv-post/cloud-pv-invalid-scenario";
            String fileName = "pv-post-invalid-entrance.json";
            File file = new File(jsonDir + "/" + fileName);
            logger.info("file: " + file.getName());
            String startTime = getCurrentHourBegin();
            String endTime = getCurrentHourEnd();
            requestId = UUID.randomUUID().toString();

            PvInfo existedBefore = apiCustomerRequest("/business/customer/QUERY_CUSTOMER_STATISTICS/v1.1", startTime, endTime);

            apiSdkPostToCloud(file, entranceId, "/scenario/who/ANALYSIS_PERSON/v1.0", startTime, requestId);
            Thread.sleep(SLEEP_MS);

            PvInfo resultPvInfo = apiCustomerRequest("/business/customer/QUERY_CUSTOMER_STATISTICS/v1.1", startTime, endTime);
            result = checkTestResult(existedBefore, resultPvInfo);
            String expectStr = "QA-CUSTOMIZED: " + JSON.toJSONString(existedBefore);
            String actualStr = JSON.toJSONString(resultPvInfo);
            saveCaseToDb(caseName, request, response, expectStr, result);
            if (!result) {
//                dingdingAlarm("PV数据统计测试", "region id 正确，entrance id 错误。\n\n期望: pv统计算法将该结果丢弃 \n结果：最新的PV数与期望不符", requestId, "@刘峤");
                String msg = "request id: " + requestId
                        + "\nregion id 正确，entrance id 错误。\n期望: pv统计算法将该结果丢弃, 结果：最新的PV数与期望不符"
                        + "\nExpect: " + expectStr
                        + "\nActual: " + actualStr;
                throw new Exception(msg);
            }
        } catch (Exception e) {
            RE_ID = reIdOrigin;
            logger.error(e.toString());
            IS_SUCCESS = false;
//            if (result) {
//                //exception NOT be caused by final result data checking
//                dingdingAlarm("PV数据统计测试", "region id 正确，entrance id 错误。出现Exception", requestId, "@刘峤");
//            }
            throw e;
        }

        RE_ID = reIdOrigin;
    }

//    @Test(dataProvider = "REID", priority = 1)
    public void invalidAppId(String appId) throws Exception{
        String requestId = "";
        boolean result = true;
        try {
            RE_ID = "145";
            String caseName = "invalidAppId-app_id-"+appId;
            logMine.logCase(caseName);
            String jsonDir = "src/main/resources/test-res-repo/pv-post/cloud-pv-invalid-scenario";
            String fileName = "pv-post-invalid-app.json";
            File file = new File(jsonDir + "/" + fileName);
            logger.info("file: " + file.getName());
            String startTime = getCurrentHourBegin();
            String endTime = getCurrentHourEnd();
            requestId = UUID.randomUUID().toString();

            PvInfo existedBefore = apiCustomerRequest("/business/customer/QUERY_CUSTOMER_STATISTICS/v1.1", startTime, endTime);

            apiSdkPostToCloudInvalidAppid(appId, file, "/scenario/who/ANALYSIS_PERSON/v1.0", startTime, requestId);
            Thread.sleep(SLEEP_MS);

            PvInfo resultPvInfo = apiCustomerRequest("/business/customer/QUERY_CUSTOMER_STATISTICS/v1.1", startTime, endTime);
            result = checkTestResult(existedBefore, resultPvInfo);
            String expectStr = "QA-CUSTOMIZED: StatusCode.UN_AUTHORIZED and data same as before: " + JSON.toJSONString(existedBefore);
            String actualStr = JSON.toJSONString(resultPvInfo);
            saveCaseToDb(caseName, request, response, expectStr, result);
            if (!result) {
//                dingdingAlarm("PV上传非法参数测试", "非法appid。\n\n期望: 数据被丢弃，PV数据无变化 \n结果：最新的PV数与期望不符", requestId, "@刘峤");
                String msg = "request id: " + requestId
                        + "\nPV上传非法参数测试，非法appid。\n期望: 数据被丢弃，PV数据无变化, 结果：最新的PV数与期望不符"
                        + "\nExpect: " + expectStr
                        + "\nActual: " + actualStr;
                throw new Exception(msg);
            }
        } catch (Exception e) {
            logger.error(e.toString());
            IS_SUCCESS = false;
//            if (result) {
//                //exception NOT be caused by final result data checking
//                dingdingAlarm("PV上传非法参数测试", "非法appid。\n\n期望: 数据被丢弃，PV数据无变化 \n结果：出现Exception", requestId, "@刘峤");
//            }
            throw e;
        }
    }

    private String getCurrentDayBegin() throws Exception {
        DateTime dateTime = new DateTime();
        int year = dateTime.getYear();
        int month = dateTime.getMonthOfYear();
        int day = dateTime.getDayOfMonth();
        int hour = dateTime.getHourOfDay();
        DateTimeUtil dt = new DateTimeUtil();
        String time = year + "/" + month + "/" + day + " " + "00:00:00:000";
        return dt.dateToTimestamp(time);
    }

    private String getCurrentHourBegin() throws Exception {
        DateTime dateTime = new DateTime();
        int year = dateTime.getYear();
        int month = dateTime.getMonthOfYear();
        int day = dateTime.getDayOfMonth();
        int hour = dateTime.getHourOfDay();
        DateTimeUtil dt = new DateTimeUtil();
        String time = year + "/" + month + "/" + day + " " + hour +":00:00:000";
        return dt.dateToTimestamp(time);
    }

    private String getCurrentHourEnd() throws Exception {
        DateTime dateTime = new DateTime();
        int year = dateTime.getYear();
        int month = dateTime.getMonthOfYear();
        int day = dateTime.getDayOfMonth();
        int hour = dateTime.getHourOfDay()+1;
        DateTimeUtil dt = new DateTimeUtil();
        String time = year + "/" + month + "/" + day + " " + hour +":00:00:000";
        return dt.dateToTimestamp(time);
    }

    private PvInfo getCurrentTestPvInfo(File file, String startTime, String endTime, String requestId) {
        logMine.logStep("get current add pv info");
        PvInfo pvInfo = new PvInfo();
        try {
            pvInfo.setStartTime(startTime);
            pvInfo.setEndTime(endTime);
            pvInfo.setRequestId(requestId);
            pvInfo.setUid(UID);
            ConcurrentHashMap<String, Integer> stayHm = new ConcurrentHashMap<>();
            ConcurrentHashMap<RegionEntranceUnit, Integer> unitHm = new ConcurrentHashMap<>();
            pvInfo.setStayHm(stayHm);
            pvInfo.setUnitHm(unitHm);
            String content= null;
            content = FileUtils.readFileToString(file,"UTF-8");
            JSONArray trace = new JSONObject(content).getJSONArray("trace");

            for (int i=0; i<trace.length(); i++) {
                JSONArray region = trace.getJSONObject(i).getJSONObject("position").getJSONArray("region");
                for (int j=0; j<region.length(); j++) {
                    String status = region.getJSONObject(j).getString("status");
                    if (!RegionStatus.ENTER.equals(status) && !RegionStatus.LEAVE.equals(status)) {
                        //不处理除ENTER和LEAVE之外的状态
                        //STAY状态由LEAVE个数-ENTER个数计算得到
                        continue;
                    }
                    String regionId = region.getJSONObject(j).getString("region_id");
                    String entranceId = region.getJSONObject(j).getString("entrance_id");
                    saveEnterLeaveInfo(regionId, entranceId, status, unitHm, 1);
                    //saveStayInfo(regionId, status, stayHm);
                }
            }

            int total = 0;
            for (int value : unitHm.values()) {
                total += value;
            }
            pvInfo.setTotal(total);
            logger.info("current test json data get done.");
        } catch (Exception e) {
            logger.error(e.toString());
            Assert.assertTrue(false);
        }
        return pvInfo;
    }

    private void saveEnterLeaveInfo(String regionId, String entranceId, String status,
                                    ConcurrentHashMap<RegionEntranceUnit, Integer> unitHm,
                                    int num
    ) {
        //save enter/leave info
        RegionEntranceUnit regionEntranceUnit = new RegionEntranceUnit(regionId,entranceId,status);
        if (unitHm.containsKey(regionEntranceUnit)) {
            unitHm.put(regionEntranceUnit, unitHm.get(regionEntranceUnit)+num);
        } else {
            unitHm.put(regionEntranceUnit, num);
        }
    }

    private void saveStayInfo(String regionId, String status,
                            ConcurrentHashMap<String, Integer> stayHm
    ) {
        //calc and save stay info
        if (RegionStatus.ENTER.equals(status)) {
            //stay +1
            if (stayHm.containsKey(regionId)) {
                stayHm.put(regionId, stayHm.get(regionId)+1);
            } else {
                stayHm.put(regionId, 1);
            }
        } else {
            //stay -1
            if (stayHm.containsKey(regionId)) {
                stayHm.put(regionId, stayHm.get(regionId)-1);
            } else {
                stayHm.put(regionId, -1);
            }
        }
    }

    private PvInfo mergeExpectInfo(PvInfo existedBefore, PvInfo currentAdd) {
        PvInfo expect = null;
        try {
            logMine.logStep("merge current add and existed pv info");
            int expectTotal = existedBefore.getTotal() + currentAdd.getTotal();
            ConcurrentHashMap<String, Integer> stayExistedHm =  existedBefore.getStayHm();
            ConcurrentHashMap<String, Integer> stayCurrentHm =  currentAdd.getStayHm();
            ConcurrentHashMap<RegionEntranceUnit, Integer> unitExistedHm = existedBefore.getUnitHm();
            ConcurrentHashMap<RegionEntranceUnit, Integer> unitCurrentHm = currentAdd.getUnitHm();


            //merge total in existedBefore to currentAdd
            currentAdd.setTotal(expectTotal);
            //merge stayHM in existedBefore to currentAdd's stayHm
            for(Map.Entry<String, Integer> entry : stayExistedHm.entrySet()) {
                String regionId = entry.getKey();
                if (stayCurrentHm.containsKey(regionId)) {
                    //有相同的region id, 新加和已存在的num相加
                    stayCurrentHm.put(regionId, stayCurrentHm.get(regionId)+stayExistedHm.get(regionId));
                } else {
                    //无相同的region id，已存在的num存入
                    stayCurrentHm.put(regionId, stayExistedHm.get(regionId));
                }
            }

            //merge unitHm
            for(Map.Entry<RegionEntranceUnit, Integer> entry : unitExistedHm.entrySet()){
                RegionEntranceUnit unit = entry.getKey();
                if (unitCurrentHm.containsKey(unit)) {
                    //有相同的出入口数据
                    unitCurrentHm.put(unit, unitCurrentHm.get(unit)+unitExistedHm.get(unit));
                } else {
                    //无相同的出入口数据
                    unitCurrentHm.put(unit, unitExistedHm.get(unit));
                }
            }

            expect = currentAdd;
        } catch (Exception e) {
            logger.error(e.toString());
            Assert.assertTrue(false);
        }

        return expect;
    }

    private boolean checkTestResult(PvInfo existedBefore, PvInfo currentAdd, PvInfo result) {
        expect = mergeExpectInfo(existedBefore, currentAdd);
        return checkTestResult(expect, result);
    }


    private boolean checkTestResult(PvInfo expect, PvInfo result) {
        boolean isSuccess = true;
        logMine.logStep("check pv info");
        try {
            ConcurrentHashMap<String, Integer> stayExpectHm =  expect.getStayHm();
            ConcurrentHashMap<RegionEntranceUnit, Integer> unitExpectHm = expect.getUnitHm();

            logMine.printImportant("Expect: " + JSON.toJSONString(expect));
            logMine.printImportant("Actual: " + JSON.toJSONString(result));

            if (expect.getStartTime().equals(result.getStartTime())
                    && expect.getEndTime().equals(result.getEndTime())
                    && expect.getUid().equals(result.getUid())
                    && expect.getTotal() == result.getTotal()
            ) {
                logger.info("basic info correct");
                //check stay in result's stayHm
                int actualStaySize = result.getStayHm().size();
                int expectStaySize = stayExpectHm.size();
                if (actualStaySize != expectStaySize) {
                    logger.error("result's stay num NOT correct, expect: "
                            + expectStaySize
                            + ", actual: "
                            + actualStaySize);
                    return false;
                }
                for(Map.Entry<String, Integer> entry : result.getStayHm().entrySet()) {
                    String regionId = entry.getKey();
                    if (stayExpectHm.containsKey(regionId)) {
                        if (stayExpectHm.get(regionId) != entry.getValue()) {
                            logger.error("stay num of region id " + regionId
                                    + "result not equal to set."
                                    + " actual: " + entry.getValue()
                                    + ", expect: " + stayExpectHm.get(regionId));
                            return false;
                        }
                    } else {
                        logger.error("result get an unknown region id in stay info. region id: " + regionId);
                        return false;
                    }
                }

                //check enter/leave unit
                int actualUnitSize = result.getUnitHm().size();
                int expectUnitSize = unitExpectHm.size();
                if (actualUnitSize != expectUnitSize) {
                    logger.error("result's leave/enter num NOT correct, actual: "
                            + actualUnitSize
                            + ", expect: "
                            + expectUnitSize);
                    return false;
                }
                for (Map.Entry<RegionEntranceUnit, Integer> entry : result.getUnitHm().entrySet()) {
                    RegionEntranceUnit regionEntranceUnit = entry.getKey();
                    if(unitExpectHm.containsKey(regionEntranceUnit)) {
                        if (unitExpectHm.get(regionEntranceUnit) != entry.getValue()) {
                            logger.info(JSON.toJSONString(regionEntranceUnit));
                            logger.error("[result]: " + entry.getValue()
                                    + ", expect: " + unitExpectHm.get(regionEntranceUnit));
                            return false;
                        }
                    } else {
                        logger.error("result get an unknown enter/leave info: " + JSON.toJSONString(regionEntranceUnit));
                        return false;
                    }
                }

            } else {
                logger.error("basic info NOT correct");
                return false;
            }

            logger.info("expect and result all info same");

        } catch (Exception e) {
            logger.error(e.toString());
            Assert.assertTrue(false);
        }

        return isSuccess;

    }

    private PvInfo apiCustomerRequest(String router, String beginTime, String endTime) throws Exception {
        logMine.logStep("get latest pv info from cloud");
        PvInfo pvInfo = null;
        try {
            String json = "{" +
                    "\"shop_id\":\""+SHOP_ID+"\"," +
                    "\"start_time\":" + beginTime + "," +
                    "\"end_time\":"+ endTime +
                    "}";

            Credential credential = new Credential("e0709358d368ee13", "ef4e751487888f4a7d5331e8119172a3");
            // 封装request对象
            String requestId = UUID.randomUUID().toString();
            ApiRequest apiRequest = new ApiRequest.Builder()
                    .uid(UID)
                    .appId(APP_ID)
                    .version(SdkConstant.API_VERSION)
                    .requestId(requestId)
                    .router(router)
                    .dataResource(new String[]{})
                    .dataBizData(JSON.parseObject(json))
                    .build();

            // client 请求
            ApiClient apiClient = new ApiClient("http://dev.api.winsenseos.cn/retail/api/data/biz", credential);
            ApiResponse apiResponse = apiClient.doRequest(apiRequest);
            logMine.printImportant(JSON.toJSONString(apiResponse));
            if(! apiResponse.isSuccess()) {
                String msg = "request id: " + requestId + ", gateway: /retail/api/data/biz, router: " + router + "\nresponse: " + JSON.toJSONString(apiResponse);
                throw new Exception(msg);
            }
            pvInfo = getExistedPvInfo(JSON.toJSONString(apiResponse));
        } catch (Exception e) {
            throw e;
        }

        return pvInfo;
    }

    private PvInfo apiCustomerRequest(String router) throws Exception {
        logMine.logStep("get latest pv info from cloud");
        PvInfo pvInfo = null;
        try {
            String json = "{" +
                    "\"shop_id\":\""+SHOP_ID+"\"" +
                    "}";

            Credential credential = new Credential("e0709358d368ee13", "ef4e751487888f4a7d5331e8119172a3");
            // 封装request对象
            String requestId = UUID.randomUUID().toString();
            ApiRequest apiRequest = new ApiRequest.Builder()
                    .uid(UID)
                    .appId(APP_ID)
                    .version(SdkConstant.API_VERSION)
                    .requestId(requestId)
                    .router(router)
                    .dataResource(new String[]{})
                    .dataBizData(JSON.parseObject(json))
                    .build();

            // client 请求
            ApiClient apiClient = new ApiClient("http://dev.api.winsenseos.cn/retail/api/data/biz", credential);
            ApiResponse apiResponse = apiClient.doRequest(apiRequest);
            logMine.printImportant(JSON.toJSONString(apiResponse));
            if(! apiResponse.isSuccess()) {
                String msg = "request id: " + requestId + ", gateway: /retail/api/data/biz, router: " + router + "\nresponse: " + JSON.toJSONString(apiResponse);
                throw new Exception(msg);
            }
            pvInfo = getExistedPvInfo(JSON.toJSONString(apiResponse));
        } catch (Exception e) {
            throw e;
        }

        return pvInfo;
    }

    private PvInfo getExistedPvInfo(String response) throws Exception {
        PvInfo pvInfo = new PvInfo();
        ConcurrentHashMap<String, Integer> stayHm = new ConcurrentHashMap<>();
        ConcurrentHashMap<RegionEntranceUnit, Integer> unitHm = new ConcurrentHashMap<>();
        pvInfo.setStayHm(stayHm);
        pvInfo.setUnitHm(unitHm);

        com.alibaba.fastjson.JSONObject resJson = JSON.parseObject(response);
        pvInfo.setUid(resJson.getString("uid"));
        pvInfo.setRequestId(resJson.getString("request_id"));
        com.alibaba.fastjson.JSONArray statisticArray = resJson.getJSONObject("data").getJSONArray("statistics");
        if (statisticArray.size() > 0) {
            com.alibaba.fastjson.JSONObject statistics = statisticArray.getJSONObject(0);
            String startTime = statistics.getString("start_time");
            String endTime = statistics.getString("end_time");
            pvInfo.setStartTime(startTime);
            pvInfo.setEndTime(endTime);

            //get stay info
//            com.alibaba.fastjson.JSONArray stayArray = statistics.getJSONArray("stay_number");
//            for (int i=0; i<stayArray.size(); i++) {
//                String regionId = stayArray.getJSONObject(i).getString("region_id");
//                int num = stayArray.getJSONObject(i).getInteger("num");
//                stayHm.put(regionId, num);
//            }
            com.alibaba.fastjson.JSONObject passTimes = statistics.getJSONObject("passing_times");

            //get enter/leave info
//            int total = passTimes.getInteger("total");
//            pvInfo.setTotal(total);

            filterEnterLeaveFromResponse(passTimes, "leave", unitHm);
            filterEnterLeaveFromResponse(passTimes, "enter", unitHm);
        }

        return pvInfo;
    }

    private void filterEnterLeaveFromResponse(com.alibaba.fastjson.JSONObject passTimes,
                                              String key,
                                              ConcurrentHashMap<RegionEntranceUnit, Integer> unitHm
    ) throws JSONException {
        com.alibaba.fastjson.JSONArray keys = passTimes.getJSONArray(key);
        for (int i=0; i<keys.size(); i++) {
            String regionId = keys.getJSONObject(i).getString("region_id");
            String entranceId = keys.getJSONObject(i).getString("entrance_id");
            int num = keys.getJSONObject(i).getInteger("num");
            saveEnterLeaveInfo(regionId, entranceId, key.toUpperCase(), unitHm, num);

        }
    }

    private void apiSdkPostToCloud(File file, String router, String startTime, String requestId) throws Exception {
        try {
            logMine.logStep("post pv info");
            // 传入签名参数
            Credential credential = new Credential("e0709358d368ee13", "ef4e751487888f4a7d5331e8119172a3");
            String json = FileUtils.readFileToString(file,"UTF-8");
            json = json.replaceAll("\n\\s*", "")
                        .replace(START_TIME, startTime)
                        .replace(END_TIME, String.valueOf(System.currentTimeMillis()));
            // 封装request对象
            ApiRequest apiRequest = new ApiRequest.Builder()
                    .uid(UID)
                    .appId(APP_ID)
                    .version(SdkConstant.API_VERSION)
//                    .requestId(requestId)
                    .requestId("7aebbc3e-a14b-4645-80a6-a329064ee9ad")
                    .dataDeviceId(DEVICE_ID)
                    .router(router)
                    .dataResource(new String[]{})
                    .dataBizData(JSON.parseObject(json))
                    .build();

            // client 请求
            request = JSON.toJSONString(apiRequest);
            logger.info("request json: " + request);
            String gateway = "http://dev.api.winsenseos.cn/retail/api/data/device";
            ApiClient apiClient = new ApiClient(gateway, credential);
            ApiResponse apiResponse = apiClient.doRequest(apiRequest);
            response = JSON.toJSONString(apiResponse);
            logMine.printImportant(response);
            if(! apiResponse.isSuccess()) {
                String msg = "request id: " + requestId + ", gateway: /retail/api/data/device, router: " + router + ". \nresponse: " + JSON.toJSONString(apiResponse);
                throw new Exception(msg);
            }
        } catch (Exception e) {
            throw e;
        }
    }

    private void apiSdkPostToCloud(File file, String id , String router, String startTime, String requestId) throws Exception{
        try {
            logMine.logStep("post pv info");
            // 传入签名参数
            Credential credential = new Credential("e0709358d368ee13", "ef4e751487888f4a7d5331e8119172a3");
            String json = FileUtils.readFileToString(file,"UTF-8");
            json = json.replaceAll("\n\\s*", "")
                    .replaceAll(RE_ID, id)
                    .replace(START_TIME, startTime)
                    .replace(END_TIME, String.valueOf(System.currentTimeMillis()));
            // 封装request对象
            ApiRequest apiRequest = new ApiRequest.Builder()
                    .uid(UID)
                    .appId(APP_ID)
                    .version(SdkConstant.API_VERSION)
                    .requestId(requestId)
                    .dataDeviceId(DEVICE_ID)
                    .router(router)
                    .dataResource(new String[]{})
                    .dataBizData(JSON.parseObject(json))
                    .build();

            // client 请求
            request = JSON.toJSONString(apiRequest);
            logger.info("request json: " + request);
            String gateway = "http://dev.api.winsenseos.cn/retail/api/data/device";
            ApiClient apiClient = new ApiClient(gateway, credential);
            ApiResponse apiResponse = apiClient.doRequest(apiRequest);
            response = JSON.toJSONString(apiResponse);
            logMine.printImportant(response);
            if(! apiResponse.isSuccess()) {
                String msg = "request id: " + requestId + ", gateway: /retail/api/data/device, router: " + router + ". \nresponse: " + JSON.toJSONString(apiResponse);
                throw new Exception(msg);
            }
        } catch (Exception e) {
            throw e;
        }
    }

    private void apiSdkPostToCloudInvalidAppid(String appId, File file, String router, String startTime, String requestId) throws Exception{
        String appIdOrigin = APP_ID;
        try {
            logMine.logStep("post pv info with invalid app id: " + appId);
            // 传入签名参数
            Credential credential = new Credential("e0709358d368ee13", "ef4e751487888f4a7d5331e8119172a3");
            String json = FileUtils.readFileToString(file,"UTF-8");
            json = json.replaceAll("\n\\s*", "")
                    .replace(START_TIME, startTime)
                    .replace(END_TIME, String.valueOf(System.currentTimeMillis()));
            // 封装request对象
            ApiRequest apiRequest = new ApiRequest.Builder()
                    .uid(UID)
                    .appId(appId)
                    .version(SdkConstant.API_VERSION)
                    .requestId(requestId)
                    .dataDeviceId(DEVICE_ID)
                    .router(router)
                    .dataResource(new String[]{})
                    .dataBizData(JSON.parseObject(json))
                    .build();

            // client 请求
            request = JSON.toJSONString(apiRequest);
            logger.info("request json: " + request);
            String gateway = "http://dev.api.winsenseos.cn/retail/api/data/device";
            ApiClient apiClient = new ApiClient(gateway, credential);
            ApiResponse apiResponse = apiClient.doRequest(apiRequest);
            response = JSON.toJSONString(apiResponse);
            logMine.printImportant(response);
            if (apiResponse.getCode() != StatusCode.UN_AUTHORIZED) {
                String msg = "request id: " + requestId + ", gateway: /retail/api/data/device, router: " + router + ". \nresponse: " + JSON.toJSONString(apiResponse);
                throw new Exception(msg);
            }
        } catch (Exception e) {
            APP_ID = appIdOrigin;
            //dingdingAlarm("接口/retail/api/data/device测试", "非法appid测试。\n\n期望: 返回2001 \n结果：出现Exception", requestId, "@华成裕");
            throw e;
        }
        APP_ID = appIdOrigin;
    }

    private void dingdingAlarm(String summary, String detail, String requestId, String atPerson) {
        detail = "请求requestid: " + requestId + " \n" + detail;
        //screenshot do not support local pic, must use pic in web
        String bugPic = "http://i01.lw.aliimg.com/media/lALPBbCc1ZhJGIvNAkzNBLA_1200_588.png";
        String linkUrl = "http://192.168.50.2:8080/view/云端测试/job/pv-cloud-test/Test_20Report/";
        String msg = DingChatbot.getMarkdown(summary, detail, bugPic, linkUrl, atPerson);
        DingChatbot.sendMarkdown(msg);
    }


    private void saveCaseToDb(String caseName, String request, String response, String expect, boolean result) {

//        Case checklist = new Case();
//        List<Integer> listId = caseDao.queryCaseByName(ChecklistDbInfo.DB_APP_ID_CLOUD_SERVICE,
//                ChecklistDbInfo.DB_SERVICE_ID_CUSTOMER_DATA_SERVICE,
//                caseName);
//        int id = -1;
//        if (listId.size() > 0) {
//            checklist.setId(listId.get(0));
//        }
//        checklist.setApplicationId(ChecklistDbInfo.DB_APP_ID_CLOUD_SERVICE);
//        checklist.setConfigId(ChecklistDbInfo.DB_SERVICE_ID_CUSTOMER_DATA_SERVICE);
//        checklist.setCanManualRun(false);
//        checklist.setRunByCi(false);
//        checklist.setCaseName(caseName);
//        checklist.setEditTime(new Timestamp(System.currentTimeMillis()));
//        checklist.setQaOwner("于海生");
//        checklist.setRequestData(request);
//        checklist.setResponse(response);
//        checklist.setExpect(expect);
//        if (result) {
//            checklist.setResult("PASS");
//        } else {
//            checklist.setResult("FAIL");
//        }
//        caseDao.insert(checklist);
//        sqlSession.commit();

    }


    @DataProvider(name = "REID")
    public Object[] createInvalidId() {

        return new String[] {
                "-1",
                "0",
                "999999999",
                "abcd",
                "2A!34'\\*a",
                "8c37a20f8ba4" //测试的其他app id
        };
    }


    @BeforeSuite
    public void initial() {
        logger.debug("initial");
        SqlSessionFactory sessionFactory = null;
        String resource = "configuration.xml";
        try {
            sessionFactory = new SqlSessionFactoryBuilder().build(Resources.getResourceAsReader(
                    resource));
            sqlSession = sessionFactory.openSession();
            caseDao = sqlSession.getMapper(ICaseDao.class);
            MQYun.subscribeTopic();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @AfterSuite
    public void clean() {
        logger.info("clean");
        sqlSession.close();
        MQYun.shutdown();
        if (! IS_SUCCESS) {
            //dingdingAlarm("PV数据获取测试失败", "请点击下面详细链接查看log", "", "@刘峤");
        }
    }

}
