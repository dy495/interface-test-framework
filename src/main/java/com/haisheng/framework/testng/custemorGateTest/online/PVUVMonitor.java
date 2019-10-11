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
    public void getHistoryDataYingshiDaily() {
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
    public void getHistoryDataYingshiOnline() {
        String shopId = "97";
        String appId  = "111112a388c2";
        String com    = "赢识线上";
        getHistoryDataByShop(ONLINE_LB, shopId, appId, com);

    }

    @Test
    public void getHistoryDataBaihuaOnline() {
        String shopId = "149";
        String appId  = "77d07cf38e8e";
        String com    = "百花时代广场(东莞)线上";
        getHistoryDataByShop(ONLINE_LB, shopId, appId, com);

    }

    @Test
    public void getHistoryDataHumenOnline() {
        String shopId = "251";
        String appId  = "77d07cf38e8e";
        String com    = "虎门国际购物中心线上";
        getHistoryDataByShop(ONLINE_LB, shopId, appId, com);

    }

    @Test
    public void getHistoryDataLiyingOnline() {
        String shopId = "373";
        String appId  = "77d07cf38e8e";
        String com    = "丽影广场线上";
        getHistoryDataByShop(ONLINE_LB, shopId, appId, com);

    }

    @Test
    public void getHistoryDataFengkeOnline() {
        String shopId = "242";
        String appId  = "5f20ed10b9cb";
        String com    = "万达广场丰科店线上";
        getHistoryDataByShop(ONLINE_LB, shopId, appId, com);

    }

    @Test
    public void getHistoryDataBaiguoyuanOnline() {
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

            String response = sendRequest(com, url, json);
            checkCode(response, StatusCode.SUCCESS, com + "环境监测-实时统计查询-返回值异常");

            saveData(response, com, date);

        } catch (Exception e) {
            dingPush(com + "-巡检代码异常: " + e.toString());
        }
    }

    private void getHistoryDataByShop(String lb, String shopId, String appId, String com) {
        String router = "/business/customer/QUERY_CUSTOMER_STATISTICS/v1.1";
        String url    = lb + router;
        String startTime = null, endTime = null;

        try {
            startTime = dt.getHourBegin(-1);
            endTime = dt.getHourBegin(0);
            HOUR = dt.getCurrentHour();
        } catch (Exception e) {
            dingPush(com + "-巡检代码获取当前小时整点时间戳异常: " + e.toString());
        }

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
        String requestId = UUID.randomUUID().toString();
        String date = dt.getHistoryDate(0);

        if (HOUR.equals("24")) {
            //0点后获取昨天24点的数据，数据日期为昨天
            date = dt.getHistoryDate(-1);
        }
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
        String response = sendRequest(com, url, json);
        checkCode(response, StatusCode.SUCCESS, com + "环境监测-历史统计查询-返回值异常");
        saveData(response, com, date);
    }


    private void saveData(String response, String com, String date) {

        try {
            OnlinePVUV onlinePVUV = new OnlinePVUV();
            JSONObject responseJo = JSON.parseObject(response);
            JSONArray statistics  = responseJo.getJSONArray("statistics");

            String gender = null, age = null;
            int pvEnter = 0, pvLeave = 0, uvEnter = 0, uvLeave = 0;

            if (statistics != null) {
                int size = statistics.size();
                if (size > 0) {
                    JSONObject statisticsSingle = statistics.getJSONObject(0);

                    gender = statisticsSingle.getString("gender");
                    age = statisticsSingle.getString("age");

                    String uvLeaveStr = statisticsSingle.getJSONObject("person_number").getString("entrance_leave_total");
                    uvLeave = Integer.parseInt(uvLeaveStr);

                    String uvEnterStr = statisticsSingle.getJSONObject("person_number").getString("entrance_enter_total");
                    uvEnter = Integer.parseInt(uvEnterStr);

                    String pvLeaveStr = statisticsSingle.getJSONObject("passing_times").getString("entrance_leave_total");
                    pvLeave = Integer.parseInt(pvLeaveStr);

                    String pvEnterStr = statisticsSingle.getJSONObject("passing_times").getString("entrance_enter_total");
                    pvEnter = Integer.parseInt(pvEnterStr);
                }
            }

            onlinePVUV.setCom(com);
            onlinePVUV.setDate(date);
            onlinePVUV.setHour(HOUR);
            onlinePVUV.setGender(gender);
            onlinePVUV.setAge(age);
            onlinePVUV.setPvEnter(pvEnter);
            onlinePVUV.setPvLeave(pvLeave);
            onlinePVUV.setUvEnter(uvEnter);
            onlinePVUV.setUvLeave(uvLeave);
            onlinePVUV.setUpdateTime(dt.currentDateToTimestamp());

            qaDbUtil.saveOnlinePvUv(onlinePVUV);
        } catch (Exception e) {
            dingPush(com + "-历史统计查询接口返回response数据异常: " + e.toString() + "\n" + "response: " + response);
        }


    }

    private String sendRequest(String com, String url, String json) {
        try {
            HttpExecutorUtil executorUtil = new HttpExecutorUtil();
            executorUtil.doPostJson(url, json);

            return executorUtil.getResponse();
        } catch (Exception e) {
            dingPush(com + "-历史统计查询接口请求异常: " + e.toString());
        }

        return null;

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
