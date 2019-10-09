package com.haisheng.framework.testng.custemorGateTest.online;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.model.bean.OnlinePVUV;
import com.haisheng.framework.testng.CommonDataStructure.DingWebhook;
import com.haisheng.framework.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.util.UUID;

public class PVUVMonitor {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    QADbUtil qaDbUtil = new QADbUtil();
    DateTimeUtil dt = new DateTimeUtil();


    final String LOCAL_DEBUG_HOST  = "http://39.105.225.20";
    final String DAILY_LB          = "http://10.0.15.226";
    final String ONLINE_LB         = "http://10.0.16.17";

    String HOUR = "all";

    boolean DEBUG = false;

    //@Test
    public void getRealTimeDataYingshiDaily() {
        String shopId = "8";
        String appId  = "097332a388c2";
        String com    = "赢识日常";
        String router = "/business/customer/QUERY_CURRENT_CUSTOMER_STATISTICS/v1.1";
        String url    = DAILY_LB + router;

        if (DEBUG) {
            url = LOCAL_DEBUG_HOST + router;
        }

        realTimeMonitor(url, shopId, appId, com);

    }

    //@Test
    public void getRealTimeDataYingshiOnline() {
        String shopId = "97";
        String appId  = "111112a388c2";
        String com    = "赢识线上";
        String router = "/business/customer/QUERY_CURRENT_CUSTOMER_STATISTICS/v1.1";
        String url    = ONLINE_LB + router;

        realTimeMonitor(url, shopId, appId, com);

    }

    @Test
    public void getHistoryDataYingshiDaily() throws Exception {
        String shopId = "8";
        String appId  = "097332a388c2";
        String com    = "赢识日常";

        if (DEBUG) {
            getHistoryDataByShop(LOCAL_DEBUG_HOST, shopId, appId, com);
        } else {
            getHistoryDataByShop(DAILY_LB, shopId, appId, com);
        }
    }

    @Test
    public void getHistoryDataYingshiOnline() throws Exception {
        String shopId = "97";
        String appId  = "111112a388c2";
        String com    = "赢识线上";
        getHistoryDataByShop(ONLINE_LB, shopId, appId, com);

    }

    @Test
    public void getHistoryDataBaihuaOnline() throws Exception {
        String shopId = "149";
        String appId  = "77d07cf38e8e";
        String com    = "百花时代广场(东莞)线上";
        getHistoryDataByShop(ONLINE_LB, shopId, appId, com);

    }

    @Test
    public void getHistoryDataHumenOnline() throws Exception {
        String shopId = "251";
        String appId  = "77d07cf38e8e";
        String com    = "虎门国际购物中心线上";
        getHistoryDataByShop(ONLINE_LB, shopId, appId, com);

    }

    @Test
    public void getHistoryDataLiyingOnline() throws Exception {
        String shopId = "373";
        String appId  = "77d07cf38e8e";
        String com    = "丽影广场线上";
        getHistoryDataByShop(ONLINE_LB, shopId, appId, com);

    }

    @Test
    public void getHistoryDataFengkeOnline() throws Exception {
        String shopId = "242";
        String appId  = "5f20ed10b9cb";
        String com    = "万达广场丰科店线上";
        getHistoryDataByShop(ONLINE_LB, shopId, appId, com);

    }

    @Test
    public void getHistoryDataBaiguoyuanOnline() throws Exception {
        String shopId = "246";
        String appId  = "2cf019f4c443";
        String com    = "百果园-测试店线上";
        getHistoryDataByShop(ONLINE_LB, shopId, appId, com);

    }



    private void realTimeMonitor(String url, String shopId, String appId, String com) {
        try {
            String requestId = UUID.randomUUID().toString();
            String date = dt.getHistoryDate(0);
            HOUR = "all";

            String json = "{" +
                    "\"data\":{\"shop_id\":" + shopId + "}," +
                    "\"request_id\":\"" + requestId + "\"," +
                    "\"system\":{\"app_id\":\"" + appId + "\",\"source\":\"BIZ\"}" +
                    "}";

            String response = sendRequest(url, json);
            checkCode(response, StatusCode.SUCCESS, com + "环境监测-实时统计查询-返回值异常");

            saveData(response, com, date);

        } catch (Exception e) {
            dingPush(com + "-巡检代码异常: " + e.toString());
        }
    }

    private void getHistoryDataByShop(String lb, String shopId, String appId, String com) throws Exception {
        String router = "/business/customer/QUERY_CURRENT_CUSTOMER_STATISTICS/v1.1";
        String url    = lb + router;
        String endTime   = dt.getHourBegin(0);
        String startTime = dt.getHourBegin(-1);
        HOUR = dt.getCurrentHour();

        if (HOUR.equals("0")) {
            HOUR = "24";
        }
        historyMonitor(url, startTime, endTime, shopId, appId, com);

        //get one day pvuv data
        if (HOUR.equals("24")) {
            HOUR = "all";
            endTime   = String.valueOf(dt.getHistoryDateTimestamp(0));
            startTime = String.valueOf(dt.getHistoryDateTimestamp(-1));
            historyMonitor(url, startTime, endTime, shopId, appId, com);
        }
    }

    private void historyMonitor(String url, String startTime, String endTime, String shopId, String appId, String com) {
        try {
            String requestId = UUID.randomUUID().toString();
            String date = dt.getHistoryDate(0);
            String json = "{" +
                    "\"data\":{" +
                    "\"shop_id\":" + shopId + "," +
                    "\"start_time\":" + startTime + "," +
                    "\"end_time\":" + endTime +
                    "}," +
                    "\"request_id\":\"" + requestId + "\"," +
                    "\"system\":{\"app_id\":\"" + appId + "\",\"source\":\"BIZ\"}" +
                    "}";

            if (HOUR.equals("all")) {
                //全天历史数据统计的是昨天的流量
                date = dt.getHistoryDate(-1);
                json = "{" +
                        "\"data\":{" +
                        "\"shop_id\":" + shopId + "," +
                        "\"start_time\":" + startTime + "," +
                        "\"end_time\":" + endTime + "," +
                        "\"time_statistics_type\":" + "\"DAY\"" +
                        "}," +
                        "\"request_id\":\"" + requestId + "\"," +
                        "\"system\":{\"app_id\":\"" + appId + "\",\"source\":\"BIZ\"}" +
                        "}";
            }
            String response = sendRequest(url, json);
            checkCode(response, StatusCode.SUCCESS, com + "环境监测-历史统计查询-返回值异常");
            saveData(response, com, date);

        } catch (Exception e) {
            dingPush(com + "-巡检代码异常: " + e.toString());
        }
    }


    private void saveData(String response, String com, String date) throws Exception {
        OnlinePVUV onlinePVUV = new OnlinePVUV();
        JSONObject responseJo = JSON.parseObject(response);
        JSONArray statistics  = responseJo.getJSONArray("statistics");

        if (statistics != null) {
            int size = statistics.size();
            if (size == 0) {
                dingPush(com + "-实时统计查询接口-返回数据为空");
            }
        }

        JSONObject statisticsSingle = statistics.getJSONObject(0);

        JSONObject gender = statisticsSingle.getJSONObject("gender");
        JSONObject age = statisticsSingle.getJSONObject("age");

        String uvLeaveStr = statisticsSingle.getJSONObject("person_number").getString("entrance_leave_total");
        int uvLeave = Integer.parseInt(uvLeaveStr);

        String uvEnterStr = statisticsSingle.getJSONObject("person_number").getString("entrance_enter_total");
        int uvEnter = Integer.parseInt(uvEnterStr);

        String pvLeaveStr = statisticsSingle.getJSONObject("passing_times").getString("entrance_leave_total");
        int pvLeave = Integer.parseInt(pvLeaveStr);

        String pvEnterStr = statisticsSingle.getJSONObject("passing_times").getString("entrance_enter_total");
        int pvEnter = Integer.parseInt(pvEnterStr);

        onlinePVUV.setCom(com);
        onlinePVUV.setDate(date);
        onlinePVUV.setHour(HOUR);
        onlinePVUV.setGender(gender.toJSONString());
        onlinePVUV.setAge(age.toJSONString());
        onlinePVUV.setPvEnter(pvEnter);
        onlinePVUV.setPvLeave(pvLeave);
        onlinePVUV.setUvEnter(uvEnter);
        onlinePVUV.setUvLeave(uvLeave);
        onlinePVUV.setUpdateTime(dt.currentDateToTimestamp());

        qaDbUtil.saveOnlinePvUv(onlinePVUV);

    }

    private String sendRequest(String url, String json) throws Exception {
        HttpExecutorUtil executorUtil = new HttpExecutorUtil();
        executorUtil.doPostJson(url, json);

        return executorUtil.getResponse();
    }

    private void checkCode(String response, int expect, String message) {
        int code = JSON.parseObject(response).getInteger("code");

        if (expect != code) {
            dingPush(message + " expect code: " + expect + ", actual: " + code);
        }
    }

    private void dingPush(String msg) {
        AlarmPush alarmPush = new AlarmPush();

        if (DEBUG) {
            alarmPush.setDingWebhook(DingWebhook.AD_GRP);
        } else {
            alarmPush.setDingWebhook(DingWebhook.PV_UV_ACCURACY_GRP);
        }

        alarmPush.onlineMonitorPvuvAlarm(msg);
        Assert.assertTrue(false);

    }



    @BeforeSuite
    public void initial(){
        qaDbUtil.openConnection();
    }

    @AfterSuite
    public void clean() {
        qaDbUtil.closeConnection();
    }
}
