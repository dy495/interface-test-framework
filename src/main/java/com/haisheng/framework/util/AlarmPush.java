package com.haisheng.framework.util;


import com.haisheng.framework.model.bean.BaiguoyuanBindMetrics;
import com.haisheng.framework.model.bean.Shelf;

import java.util.List;

public class AlarmPush {
    private String hostPort = "http://192.168.50.2:7777";
    private String algorithomBugLink  = "http://192.168.50.2:8081/bug-browse-2.html";
    private String allBugLink  = "http://192.168.50.2:8081/qa/";
    private String grafanaLink = "http://192.168.50.4:7000/d/81PXtnnWk/qa-portal?orgId=1&from=now-7d&to=now";
    private String dingWebhook = "";

    public void setDingWebhook(String webhook) {
        this.dingWebhook = webhook;
    }

    public void cloudAlarm(String[] bodyPassRate,
                           String[] facePassRate,
                           String[] customerApiPassRate,
                           String[] cloudBugInfo) {

        DingChatbot.WEBHOOK_TOKEN = this.dingWebhook;
        this.algorithomBugLink  = "http://192.168.50.2:8081/bug-browse-2.html";
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
                + "\n\n>##### **云端服务 缺陷清除率**：" + cloudBugInfo[0]
                + "\n>##### **云端服务 未关闭缺陷**：" + cloudBugInfo[1]
                + "\n>请 *@蔡思明、@黄鑫、@刘峤* 关注"
                + "\n\n>失败用例信息点击链接->云端服务->用例管理[详情链接](" + hostPort + ")"
                + "\n>Bug信息查看[详情链接](" + algorithomBugLink +")";

        DingChatbot.sendMarkdown(msg);
    }

    public void openPlatformAlarm(String[] adPassRate,
                           String[] consolePassRate,
                           String[] bugInfo) {

        DingChatbot.WEBHOOK_TOKEN = this.dingWebhook;
        this.algorithomBugLink = "http://192.168.50.2:8081/bug-browse-4.html";
        DateTimeUtil dt = new DateTimeUtil();

        String summary = "每日回归&缺陷简报";
        String msg = "### " + summary + "\n";
        String today = dt.getHistoryDate(0);

        msg += "\n\n#### " + today + " 记录信息\n";
        msg +=  "\n\n>##### **模块：广告接口，RD：马琨**"
                + "\n>##### 通过率：" + adPassRate[0] + "，FAIL：" + adPassRate[1] + "，TOTAL：" + adPassRate[2]
                + "\n\n>##### **模块：控制中心，RD：黄鑫、杨航**"
                + "\n>##### 通过率：" + consolePassRate[0] + "，FAIL：" + consolePassRate[1] + "，TOTAL：" + consolePassRate[2]
                + "\n\n>##### **开放平台 缺陷清除率**：" + bugInfo[0]
                + "\n>##### **开放平台 未关闭缺陷**：" + bugInfo[1]
                + "\n>请 *@廖祥茹、@马琨、@黄鑫、@杨航* 关注"
                + "\n\n>失败用例信息点击链接->开放平台->用例管理[详情链接](" + hostPort + ")"
                + "\n>Bug信息查看[详情链接](" + algorithomBugLink +")";

        DingChatbot.sendMarkdown(msg);
    }

    public void shelfAlarm(String[] shelfPassRate,
                           List<Shelf> accuracyList,
                           String[] bugInfo) {

        DingChatbot.WEBHOOK_TOKEN = this.dingWebhook;
        this.algorithomBugLink = "http://192.168.50.2:8081/bug-browse-8.html";
        DateTimeUtil dt = new DateTimeUtil();

        String summary = "每日回归&缺陷简报";
        String msg = "### " + summary + "\n";
        String today = dt.getHistoryDate(0);

        msg += "\n\n#### " + today + " 记录信息\n";

        //parse accuracy list
        String accuracyStr = "";
        for (Shelf shelf : accuracyList) {
            accuracyStr += "\n>##### " + shelf.getType() + "算法准确率：" + shelf.getAccuracy()*100 + "%";
        }


        msg +=  "\n\n>##### **模块：货架接口，RD：谢志东、李俊延**"
                + "\n>##### 通过率：" + shelfPassRate[0] + "，FAIL：" + shelfPassRate[1] + "，TOTAL：" + shelfPassRate[2]
                + accuracyStr
                + "\n\n>##### **缺陷清除率**：" + bugInfo[0]
                + "\n>##### **未关闭缺陷**：" + bugInfo[1]
                + "\n>请 *@廖祥茹、@谢志东、@李俊延* 关注"
                + "\n\n>失败用例信息点击链接->开放平台->用例管理[详情链接](" + hostPort + ")"
                + "\n>Bug信息查看[详情链接](" + algorithomBugLink +")";

        DingChatbot.sendMarkdown(msg);
    }


    public void appCloudAlarm(String[] openPlatformBugInfo,
                              String[] bigScreenBugInfo,
                              String[] shelfBugInfo
    ) {

        DingChatbot.WEBHOOK_TOKEN = this.dingWebhook;
        DateTimeUtil dt = new DateTimeUtil();

        String summary = "每日回归&缺陷简报";
        String msg = "### " + summary + "\n";
        String today = dt.getHistoryDate(0);

        msg += "\n\n#### " + today + " 记录信息\n";
        msg +=  "\n\n>##### **模块：开放平台，RD：华成裕，徐艳，杨航，马锟**"
                + "\n>##### 缺陷清除率：" + openPlatformBugInfo[0]
                + "\n>##### 未关闭缺陷：" + openPlatformBugInfo[1]
                + "\n\n>##### **模块：大屏展示，RD：杨航，徐艳，华成裕**"
                + "\n>##### 缺陷清除率：" +  bigScreenBugInfo[0]
                + "\n>##### 未关闭缺陷：" + bigScreenBugInfo[1]
                + "\n\n>##### **模块：货架商品，RD：徐艳，杨航，谢志东，李俊延**"
                + "\n>##### 缺陷清除率：" +  shelfBugInfo[0]
                + "\n>##### 未关闭缺陷：" + shelfBugInfo[1]
                + "\n\n>请 相关同学 关注"
                + "\n>Bug信息查看[详情链接](" + allBugLink +")";

        DingChatbot.sendMarkdown(msg);
    }

    public void baiguoyuanAlarm(List<BaiguoyuanBindMetrics> accuracyList) {

        DingChatbot.WEBHOOK_TOKEN = this.dingWebhook;
        this.algorithomBugLink = "http://192.168.50.2:8081/bug-browse-8.html";
        DateTimeUtil dt = new DateTimeUtil();

        String summary = "百果园绑定率";
        String msg = "### " + summary + "\n";
        String today = dt.getHistoryDate(0);

        msg += "\n\n#### " + today + " 记录信息\n";

        //parse accuracy list
        for (BaiguoyuanBindMetrics bindMetrics : accuracyList) {
            msg += "\n>##### " + bindMetrics.getMetrics() + " 绑定率：" + bindMetrics.getAccuracy()*100 + "%\n";
        }
        msg += "\n>曲线图[详情链接](" + grafanaLink +")";

        DingChatbot.sendMarkdown(msg);
    }
}
