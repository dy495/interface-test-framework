package com.haisheng.framework.testng.custemorGateTest.online;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.model.bean.OnlinePVUV;
import com.haisheng.framework.testng.CommonDataStructure.DingWebhook;
import com.haisheng.framework.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.util.UUID;

public class PVUVMonitor {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    QADbUtil qaDbUtil = new QADbUtil();
    DateTimeUtil dt = new DateTimeUtil();


    String LOCAL_DEBUG_HOST  = "http://39.105.225.20";
    String DAILY_LB          = "http://10.0.15.226";
    String ONLINE_LB         = "http://10.0.16.17";

    boolean DEBUG = false;

    @Test
    public void getRealTimeDataYingshiDaily() {
        String shopId = "8";
        String appId  = "097332a388c2";
        String com    = "赢识日常";
        String router = "/business/customer/QUERY_CURRENT_CUSTOMER_STATISTICS/v1.1";
        String url = DAILY_LB + router;
        String requestId = UUID.randomUUID().toString();
        String json = "{" +
                "\"data\":{\"shop_id\":" + shopId + "}," +
                "\"request_id\":\"" + requestId + "\"," +
                "\"system\":{\"app_id\":\"" + appId + "\",\"source\":\"BIZ\"}" +
                "}";

        try {
            if (DEBUG) {
                url = LOCAL_DEBUG_HOST + router;
            }

            String response = sendRequest(url, json);
            checkCode(response, StatusCode.SUCCESS, com + "环境监测-实时统计查询-返回值异常");
            getRealTimeData(response, com);

        } catch (Exception e) {
            dingPush(com + "-巡检代码异常: " + e.toString());
        }

    }

    @Test
    public void getRealTimeDataYingshiOnline() {
        String shopId = "97";
        String appId  = "111112a388c2";
        String com    = "赢识线上";
        String router = "/business/customer/QUERY_CURRENT_CUSTOMER_STATISTICS/v1.1";
        String url = ONLINE_LB + router;
        String requestId = UUID.randomUUID().toString();
        String json = "{" +
                "\"data\":{\"shop_id\":" + shopId + "}," +
                "\"request_id\":\"" + requestId + "\"," +
                "\"system\":{\"app_id\":\"" + appId + "\",\"source\":\"BIZ\"}" +
                "}";

        try {
            String response = sendRequest(url, json);
            checkCode(response, StatusCode.SUCCESS, com + "环境监测-实时统计查询-返回值异常");
            getRealTimeData(response, com);

        } catch (Exception e) {
            dingPush(com + "-巡检代码异常: " + e.toString());
        }

    }


    private void getRealTimeData(String response, String com) throws Exception {
        DateTimeUtil dateTimeUtil = new DateTimeUtil();
        OnlinePVUV onlinePVUV = new OnlinePVUV();
        JSONObject responseJo = JSON.parseObject(response);
        JSONArray statistics = responseJo.getJSONArray("statistics");

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
        String date = dateTimeUtil.getHistoryDate(0);
        onlinePVUV.setDate(date);
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
