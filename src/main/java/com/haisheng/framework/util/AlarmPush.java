package com.haisheng.framework.util;


public class AlarmPush {
    private String hostPort = "http://192.168.50.3:7777";
    private String bugLink  = "http://192.168.50.3:8081/bug-browse-2.html";
    private String dingWebhook = "";

    public void setDingWebhook(String webhook) {
        this.dingWebhook = webhook;
    }

    public void cloudAlarm(String[] bodyPassRate,
                           String[] facePassRate,
                           String[] customerApiPassRate,
                           String[] cloudBugInfo) {

        DingChatbot.WEBHOOK_TOKEN = this.dingWebhook;
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
                + "\n\n>失败用例信息点击链接->用例管理[详情链接](" + hostPort + ")"
                + "\n\n>Bug信息查看[详情链接](" + bugLink +")";

        DingChatbot.sendMarkdown(msg);
    }
}
