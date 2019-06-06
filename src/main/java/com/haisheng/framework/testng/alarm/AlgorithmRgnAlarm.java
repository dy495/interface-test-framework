package com.haisheng.framework.testng.alarm;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.CommonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.util.DateTimeUtil;
import com.haisheng.framework.util.DingChatbot;
import com.haisheng.framework.util.HttpExecutorUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import java.io.IOException;
import java.text.DecimalFormat;

public class AlgorithmRgnAlarm {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private boolean IS_SUCCESS    = true;
    HttpExecutorUtil executor = new HttpExecutorUtil();
    //    private String hostPort = "http://192.168.50.3:7777";
    private String hostPort = "http://localhost:8080";
    private String bugLink  = "http://192.168.50.3:8081/bug-browse-2.html";
    private int CLOUD_APP_ID = ChecklistDbInfo.DB_APP_ID_CLOUD_SERVICE;
    private int BODY_SERVICE_ID = ChecklistDbInfo.DB_SERVICE_ID_BODY_SERVICE;
    private int FACE_SERVICE_ID = ChecklistDbInfo.DB_SERVICE_ID_FACE_SERVICE;
    private int CUSTOMER_API_SERVICE_ID = ChecklistDbInfo.DB_SERVICE_ID_CUSTOMER_DATA_SERVICE;




    @Test
    public void qaPush() {
        cloudAlarm();
    }

    private String[] getPassRate(int appId, int configId) {
        String[] passRate = new String[]{"", "", ""};
        String URL = hostPort + "/application/"
                + appId
                + "/config/"
                + configId
                +"/config";
        try {
            executor.doGet(URL);
            JSONObject jsonObject = JSONObject.parseObject(executor.getResponse());
            JSONObject data = jsonObject.getJSONObject("data");
            int caseTotal = data.getInteger("caseTotal");
            int passTotal = data.getInteger("passTotal");
            DecimalFormat decimalFormat=new DecimalFormat(".00");
            passRate[0] = decimalFormat.format((float)passTotal*100/caseTotal) + "%";
            passRate[1] = String.valueOf(caseTotal - passTotal);
            passRate[2] = String.valueOf(caseTotal);


        } catch (IOException e) {
            e.printStackTrace();
        }

        return passRate;
    }

    private String[] getBugInfo(int appId) {
        String[] bugInfo = new String[]{"", "", ""};
        DateTimeUtil dt = new DateTimeUtil();
        String today = dt.getHistoryDate(0).replace("-", "");
        String URL = hostPort + "/"
                + appId
                + "/bug"
                + "?periodUnit=month"
                + "&start="+ today
                + "&end=" + today;
        try {
            executor.doGet(URL);
            JSONObject jsonObject = JSONObject.parseObject(executor.getResponse());
            JSONArray data = jsonObject.getJSONArray("data");
            int bugTotal = data.getJSONObject(0).getInteger("allTotalNum");
            int notFixTotal = data.getJSONObject(0).getInteger("openTotalNum");
            DecimalFormat decimalFormat=new DecimalFormat(".00");
            bugInfo[0] = decimalFormat.format((float)(bugTotal-notFixTotal)*100/bugTotal) + "%";
            bugInfo[1] = String.valueOf(notFixTotal);
            bugInfo[2] = String.valueOf(bugTotal);


        } catch (IOException e) {
            e.printStackTrace();
        }


        return bugInfo;
    }

    private void cloudAlarm() {

        String[] bodyPassRate = getPassRate(CLOUD_APP_ID, BODY_SERVICE_ID);
        String[] facePassRate = getPassRate(CLOUD_APP_ID, FACE_SERVICE_ID);
        String[] customerApiPassRate = getPassRate(CLOUD_APP_ID, CUSTOMER_API_SERVICE_ID);
        String[] cloudBugInfo = getBugInfo(CLOUD_APP_ID);

        DingChatbot.WEBHOOK_TOKEN = "https://oapi.dingtalk.com/robot/send?access_token=0732a60532e16e85c37dcbbd350d461d51e5b877b6e4cd7aba498acffdf1c175";
        DateTimeUtil dt = new DateTimeUtil();

        String summary = "每日回归&缺陷简报";
        String msg = "### " + summary + "\n";
        String today = dt.getHistoryDate(0);

        msg += "\n\n#### " + today + " 记录信息\n";
        msg +=  "\n\n>##### **模块：消费者接口，RD：黄鑫**"
                + "\n>##### 通过率：" + customerApiPassRate[0] + "，FAIL：" + customerApiPassRate[1] + "，TOTAL：" + customerApiPassRate[2]
                + "\n\n>##### **模块：人体算法，RD：蔡思明**"
                + "\n>##### 通过率：" + bodyPassRate[0] + "，FAIL：" + bodyPassRate[1] + "，TOTAL：" + bodyPassRate[2]
                + "\n\n>##### **模块：人脸算法，RD：蔡思明**"
                + "\n>##### 通过率：" + facePassRate[0] + "，FAIL：" + facePassRate[1] + "，TOTAL：" + facePassRate[2]
                + "\n\n>##### 云端服务整体**缺陷清除率**：" + cloudBugInfo[0]
                + "\n>##### 云端服务整体**未关闭缺陷**：" + cloudBugInfo[1]
                + "\n>请 *@蔡思明、@黄鑫、@刘峤* 关注"
                + "\n>失败用例信息点击链接->用例管理[详情链接](" + hostPort + ")"
                + "\n>Bug信息查看[详情链接](" + bugLink +")";

        DingChatbot.sendMarkdown(msg);
    }
}
