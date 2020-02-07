package com.haisheng.framework.testng.operationcenter.dmp.online;

import com.alibaba.fastjson.JSON;
import com.haisheng.framework.testng.CommonDataStructure.DingWebhook;
import com.haisheng.framework.testng.CommonDataStructure.DmpScope;
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

public class CustomerAnalysisMonitor {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    QADbUtil qaDbUtil = new QADbUtil();
    DateTimeUtil dt = new DateTimeUtil();

    final String DAILY_LB   = "http://10.0.16.26";
    final String ONLINE_LB  = "http://10.0.16.44";

    boolean DEBUG = false;




//    @Test(dataProvider = "DMP_DAILY_SCOPE", dataProviderClass = DmpScope.class)
    public void dailyCustomerDashboard(String uid, String nodeId, String shopId, String com) {
//        String uid = "uid_7fc78d24";
//        String nodeId = "55";
//        String shopId = "8";
//        String com    = "赢识日常";

        customerRealStatistic(DAILY_LB, uid, nodeId, shopId, com);
    }


    @Test(dataProvider = "DMP_ONLINE_SCOPE", dataProviderClass = DmpScope.class)
    public void onlineCustomerDashboard(String uid, String nodeId, String shopId, String com) {
        customerRealStatistic(ONLINE_LB, uid, nodeId, shopId, com);
    }


    private void customerRealStatistic(String lb, String uid, String nodeId, String shopId, String com) {
        String router = "/dashboard/customer/real/statistics";
        String url    = lb + router;
        String json = "{" +
                "\"subject_id\":" + shopId +
                "}";

        ConcurrentHashMap<String, Object> headers = getRequestHeader(uid, nodeId, com);
        String response = sendRequest(com+"运营中心-实时统计查询-接口请求异常: "+router, url, json, headers);
        checkCode(response, StatusCode.SUCCESS, com + "运营中心-实时统计查询-response返回值异常: " + router);
        saveCustomerData(response, com);
    }

    private void saveCustomerData(String response, String com) {

    }

    private ConcurrentHashMap<String, Object> getRequestHeader(String uid, String nodeId, String com) {
        ConcurrentHashMap<String, Object> headers = new ConcurrentHashMap();
        headers.put("request_id", UUID.randomUUID().toString());
        headers.put("api_source", "DMP");
        String creator = "{\"uid\":\"" + uid + "\",\"name\":\"yuhaisheng\",\"source\":\"DMP\"}";
        try {
            headers.put("creator", URLEncoder.encode(creator, "UTF-8"));
        } catch (Exception e) {
            dingPush(com+"运营中心-测试代码异常: URLEncoder.encode(creator, \"UTF-8\")");
        }
        headers.put("uid", uid);
        headers.put("node_id", nodeId);

        return  headers;
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
        try {
            int code = JSON.parseObject(response).getInteger("code");

            if (expect != code) {
                dingPush(message + " expect code: " + expect + ", actual: " + code);
            }
        } catch (Exception e) {
            dingPush(message + "\n\n" + e.toString());
        }

    }

    private void dingPush(String msg) {
        AlarmPush alarmPush = new AlarmPush();

        if (DEBUG) {
            alarmPush.setDingWebhook(DingWebhook.QA_TEST_GRP);
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
