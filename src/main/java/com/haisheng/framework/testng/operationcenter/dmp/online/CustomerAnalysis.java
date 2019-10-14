package com.haisheng.framework.testng.operationcenter.dmp.online;

import com.alibaba.fastjson.JSON;
import com.haisheng.framework.testng.CommonDataStructure.DingWebhook;
import com.haisheng.framework.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.net.URLEncoder;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class CustomerAnalysis {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    QADbUtil qaDbUtil = new QADbUtil();
    DateTimeUtil dt = new DateTimeUtil();

    final String DAILY_LB   = "http://10.0.16.26";
    final String ONLINE_LB  = "http://10.0.16.17";

    boolean DEBUG = false;




    @Test
    public void realTimeCustomerData() {
        String uid = "uid_7fc78d24";
        String nodeId = "55";
        String shopId = "8";
        String com    = "赢识日常";

        realTimeMonitor(DAILY_LB, uid, nodeId, shopId, com);
    }


    private void realTimeMonitor(String lb, String uid, String nodeId, String shopId, String com) {
        String router = "/dashboard/customer/real/statistics";
        String url    = lb + router;
        String json = "{" +
                "\"subject_id\":" + shopId +
                "}";

        ConcurrentHashMap<String, Object> headers = new ConcurrentHashMap();
        headers.put("request_id", UUID.randomUUID().toString());
        headers.put("api_source", "DMP");
        String creator = "{\"uid\":\"" + uid + "\",\"name\":\"yuhaisheng\",\"source\":\"DMP\"}";
        try {
            headers.put("creator", URLEncoder.encode(creator, "UTF-8"));
        } catch (Exception e) {
            dingPush(com+"运营中心-实时统计查询-测试代码异常: URLEncoder.encode(creator, \"UTF-8\")");
        }
        headers.put("uid", uid);
        headers.put("node_id", nodeId);
        String response = sendRequest(com+"运营中心-实时统计查询-接口请求异常", url, json, headers);
        checkCode(response, StatusCode.SUCCESS, com + "运营中心-实时统计查询-返回值异常");

    }

    private String sendRequest(String dingMsg, String url, String json, Map<String, Object> headers) {
        try {
            HttpExecutorUtil executorUtil = new HttpExecutorUtil();
            executorUtil.doPostJsonWithHeaders(url, json, headers);

            return executorUtil.getResponse();
        } catch (Exception e) {
            dingPush(dingMsg + e.toString());
        }

        return "";

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
