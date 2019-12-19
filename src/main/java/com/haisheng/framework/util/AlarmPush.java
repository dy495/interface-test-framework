package com.haisheng.framework.util;


import com.haisheng.framework.model.bean.BaiguoyuanBindMetrics;
import com.haisheng.framework.model.bean.EdgePvAccuracy;
import com.haisheng.framework.model.bean.Shelf;
import com.haisheng.framework.testng.CommonDataStructure.ConstantVar;
import com.haisheng.framework.testng.CommonDataStructure.DingWebhook;

import java.text.DecimalFormat;
import java.util.Comparator;
import java.util.List;

public class AlarmPush {
    private String hostPort = "http://192.168.50.2:7777";
    private String algorithomBugLink  = "http://192.168.50.2:8081/bug-browse-2.html";
    private String allBugLink  = "http://192.168.50.2:8081/qa/";
    private String grafanaLink = ConstantVar.GRAPH_DASHBORD;
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

        String summary = "云端算法每日回归&缺陷简报";
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
                + "\n>请 *@17610248107、@18600872221、@13259979249、@18810332354* 关注"
                + "\n\n>失败用例信息点击链接->云端服务->用例管理[详情链接](" + hostPort + ")"
                + "\n>Bug信息查看[详情链接](" + algorithomBugLink +")";

//        DingChatbot.sendMarkdown(msg);

        //add @ following rds
        //17610248107 廖祥茹
        //13581630214 马琨
        //18513118484 杨航
        //13259979249 黄鑫
        //15898182672 华成裕
        //18810332354 刘峤
        String[] atArray = {"17610248107", "13259979249", "18600872221", "18810332354"};
        DingChatbot.sendMarkdown(msg, atArray, false);
    }

    public void openPlatformAlarm(String[] adPassRate,
                           String[] consolePassRate,
                           String[] dmpPassRate,
                           String[] bugInfo) {

        DingChatbot.WEBHOOK_TOKEN = this.dingWebhook;
        this.algorithomBugLink = "http://192.168.50.2:8081/bug-browse-4.html";
        DateTimeUtil dt = new DateTimeUtil();

        String summary = "开放平台每日回归&缺陷简报";
        String msg = "### " + summary + "\n";
        String today = dt.getHistoryDate(0);

        msg += "\n\n#### " + today + " 记录信息\n";
        msg +=  "\n\n>##### **模块：广告接口，RD：马琨**"
                + "\n>##### 通过率：" + adPassRate[0] + "，FAIL：" + adPassRate[1] + "，TOTAL：" + adPassRate[2]
                + "\n\n>##### **模块：控制中心，RD：黄鑫、杨航**"
                + "\n>##### 通过率：" + consolePassRate[0] + "，FAIL：" + consolePassRate[1] + "，TOTAL：" + consolePassRate[2]
                + "\n\n>##### **模块：运营中心，RD：杨航、华成裕**"
                + "\n>##### 通过率：" + dmpPassRate[0] + "，FAIL：" + dmpPassRate[1] + "，TOTAL：" + dmpPassRate[2]
                + "\n\n>##### **开放平台 缺陷清除率**：" + bugInfo[0]
                + "\n>##### **开放平台 未关闭缺陷**：" + bugInfo[1]
//                + "\n>请 *@廖祥茹、@马琨、@黄鑫、@杨航、@华成裕* 关注"
                + "\n>请 *@17610248107、@13581630214、@13259979249、@18513118484、@15898182672* 关注"
                + "\n\n>失败用例信息点击链接->开放平台->用例管理[详情链接](" + hostPort + ")"
                + "\n>Bug信息查看[详情链接](" + algorithomBugLink +")";

        //DingChatbot.sendMarkdown(msg);

        //add @ following rds
        //17610248107 廖祥茹
        //13581630214 马琨
        //18513118484 杨航
        //13259979249 黄鑫
        //15898182672 华成裕
        String[] atArray = {"17610248107", "13581630214", "18513118484", "13259979249", "15898182672"};
        DingChatbot.sendMarkdown(msg, atArray, false);
    }

    public void shelfAlarm(String[] shelfPassRate,
                           List<Shelf> accuracyList,
                           String[] bugInfo) {

        DingChatbot.WEBHOOK_TOKEN = this.dingWebhook;
        this.algorithomBugLink = "http://192.168.50.2:8081/bug-browse-8.html";
        DateTimeUtil dt = new DateTimeUtil();

        String summary = "货架每日回归&缺陷简报";
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
                + "\n>请 *@17610248107、@15011479599、@17600739322* 关注"
                + "\n\n>失败用例信息点击链接->开放平台->用例管理[详情链接](" + hostPort + ")"
                + "\n>Bug信息查看[详情链接](" + algorithomBugLink +")";

//        DingChatbot.sendMarkdown(msg);

        //add @ following rds
        //17610248107 廖祥茹
        //13581630214 马琨
        //18513118484 杨航
        //13259979249 黄鑫
        //15898182672 华成裕
        //18810332354 刘峤
        //15011479599 谢志东
        //17600739322 李俊延
        String[] atArray = {"17610248107", "15011479599", "17600739322"};
        DingChatbot.sendMarkdown(msg, atArray, false);
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
                + "\n\n>请 @17610248107、@18513118484、@15011479599、@15898182672 关注"
                + "\n>Bug信息查看[详情链接](" + allBugLink +")";

//        DingChatbot.sendMarkdown(msg);

        //add @ following rds
        //17610248107 廖祥茹
        //13581630214 马琨
        //18513118484 杨航
        //13259979249 黄鑫
        //15898182672 华成裕
        //18810332354 刘峤
        //15011479599 谢志东
        //17600739322 李俊延
        String[] atArray = {"17610248107", "18513118484", "15011479599", "15898182672"};
        DingChatbot.sendMarkdown(msg, atArray, false);
    }

    public void baiguoyuanAlarm(List<BaiguoyuanBindMetrics> accuracyList, boolean agrregate) {

        if (null == accuracyList || accuracyList.size() == 0) {
            return;
        }

        DingChatbot.WEBHOOK_TOKEN = this.dingWebhook;
        this.algorithomBugLink = "http://192.168.50.2:8081/bug-browse-8.html";
        DateTimeUtil dt = new DateTimeUtil();


        accuracyList.sort((m1, m2) -> {
            if(m1.getVideo() == m2.getVideo()){
                return 0;
            }
            return m1.getVideo().compareTo(m2.getVideo());
        });

        String summary = "百果园绑定指标";
        String msg = "### " + summary + "\n";
        String today = dt.getHistoryDate(0);

        msg += "\n\n#### " + today + " 记录信息\n";

        String lastVideo = "lastvideo";
        int accuracyExpectTotal = 0;
        int accuracyActualTotal = 0;
        int sucAccuracyExpectTotal = 0;
        int sucAccuracyActualTotal = 0;
        boolean addAggreTotal = false;
        if (accuracyList.size() > 2 && agrregate) {
            addAggreTotal = true;
        }
        //parse accuracy list
        for (BaiguoyuanBindMetrics bindMetrics : accuracyList) {

            //parse video
            if (! bindMetrics.getVideo().equals(lastVideo)) {
                msg += "\n>##### 样本: " + bindMetrics.getVideo() + "\n";
                lastVideo = bindMetrics.getVideo();
            }

            if (bindMetrics.getMetrics().trim().equals("bind_accuracy")) {
                bindMetrics.setMetrics("绑定率");
                accuracyExpectTotal += bindMetrics.getExpectNum();
                accuracyActualTotal += bindMetrics.getActualNum();
            } else if (bindMetrics.getMetrics().trim().equals("bind_success_accuracy")) {
                bindMetrics.setMetrics("绑定正确率");
                sucAccuracyExpectTotal += bindMetrics.getExpectNum();
                sucAccuracyActualTotal += bindMetrics.getActualNum();
            }
            msg += "\n>###### " + bindMetrics.getMetrics() + ": " + bindMetrics.getAccuracy()*100 + "%\n";
        }
        //accuracy total aggregate
        if (addAggreTotal) {
            DecimalFormat df = new DecimalFormat("#.00");
            String accuracy = df.format(((float)accuracyActualTotal/accuracyExpectTotal)*100) + "%";
            String sucAccuracy = df.format(((float)sucAccuracyActualTotal/sucAccuracyExpectTotal)*100) + "%";
            msg += "\n>##### 所有样本数据统计: \n";
            msg += "\n>###### 绑定率: " + accuracy + "\n";
            msg += "\n>###### 绑定正确率: " + sucAccuracy + "\n";
        }
        msg += "\n>曲线图[详情链接](" + grafanaLink +")";

        DingChatbot.sendMarkdown(msg);
    }


    public void edgeRgnAlarm(List<EdgePvAccuracy> accuracyList) {

        DingChatbot.WEBHOOK_TOKEN = this.dingWebhook;
        DateTimeUtil dt = new DateTimeUtil();

        String summary = "边缘端PV准确率回归简报";
        String msg = "### " + summary + "\n";
        String lastDay = "2019-01-01";
        String lastVideo = "none";

        for ( EdgePvAccuracy item : accuracyList) {
            String day = item.getUpdateTime().substring(0,10);
            if (! day.equals(lastDay)) {
                msg += "\n\n#### " + day + " 记录信息\n";
                lastDay = day;
            }

            if (! item.getVideo().contains(lastVideo)) {
                //new video
                msg += "\n>##### 样本：" + item.getVideo()+"\n";
                lastVideo = item.getVideo();
            }

            msg += "\n>###### >>" + item.getStatus() + "\n";
            msg += "\n>###### ----->准确率：" + item.getPvAccuracyRate() + "\n";
        }
        msg += "\n##### 准确率历史信息请点击[链接](" + grafanaLink +")";;
        DingChatbot.sendMarkdown(msg);
    }

    public void onlineMonitorPvuvAlarm(String content) {
        DingChatbot.WEBHOOK_TOKEN = this.dingWebhook;
        DateTimeUtil dt = new DateTimeUtil();

        String msg = "### " + "线上巡检发现异常，请及时查看" + "\n";
        msg += "\n\n#### " + dt.getHistoryDate(0) + " " + dt.getCurrentHourMinutesSec() +"\n";
        msg += "\n\n#### " + content + "\n";

        DingChatbot.sendMarkdown(msg);
    }

    public void onlineMonitorPvuvAlarm(String content, String[] atUsers) {
        DingChatbot.WEBHOOK_TOKEN = this.dingWebhook;
        DateTimeUtil dt = new DateTimeUtil();

        String msg = "### " + "线上巡检发现异常，请及时查看" + "\n";
        msg += "\n\n#### " + dt.getHistoryDate(0) + " " + dt.getCurrentHourMinutesSec() +"\n";
        msg += "\n\n#### " + content + "\n";

        DingChatbot.sendMarkdown(msg, atUsers, false);
    }

    public void alarmToRd(String[] who) {
        DingChatbot.WEBHOOK_TOKEN = this.dingWebhook;
        DateTimeUtil dt = new DateTimeUtil();
        String msg = "#### " + "请 XXX及时查看" + "\n";
        String toAt = "";

        for (String rd : who) {
            toAt += "@" + rd + " ";
        }

        msg = msg.replace("XXX", toAt);


        DingChatbot.sendMarkdown(msg, who, false);
    }

    public void dailyRgn(String content) {
        DingChatbot.WEBHOOK_TOKEN = this.dingWebhook;
        DateTimeUtil dt = new DateTimeUtil();

        String msg = "### " + "日常回归发现异常，请及时查看" + "\n";
        msg += "\n\n#### " + dt.getHistoryDate(0) + " " + dt.getCurrentHourMinutesSec() +"\n";
        msg += "\n\n#### " + content + "\n";

        DingChatbot.sendMarkdown(msg);
    }

    public void onlineRgn(String content) {
        DingChatbot.WEBHOOK_TOKEN = this.dingWebhook;
        DateTimeUtil dt = new DateTimeUtil();

        String msg = "### " + "线上回归发现异常，请及时查看" + "\n";
        msg += "\n\n#### " + dt.getHistoryDate(0) + " " + dt.getCurrentHourMinutesSec() +"\n";
        msg += "\n\n#### " + content + "\n";

        DingChatbot.sendMarkdown(msg);
    }


    public void bigScreenAlarm(String[] yuexiuOnlinePassRate,
                               String[] yuexiuDailyPassRate,
                                  String[] bugInfo) {

        DingChatbot.WEBHOOK_TOKEN = this.dingWebhook;
        this.algorithomBugLink = "http://192.168.50.2:8081/bug-browse-7-0-unclosed.html";
        DateTimeUtil dt = new DateTimeUtil();

        String summary = "独立项目每日回归&缺陷简报";
        String msg = "### " + summary + "\n";
        String today = dt.getHistoryDate(0);

        msg += "\n\n#### " + today + " 记录信息\n";
        msg +=  "\n\n>##### **模块：越秀售楼处，RD：谢志东**"
                + "\n>##### 【线上】通过率：" + yuexiuOnlinePassRate[0] + "，FAIL：" + yuexiuOnlinePassRate[1] + "，TOTAL：" + yuexiuOnlinePassRate[2]
                + "\n>##### 【日常】通过率：" + yuexiuDailyPassRate[0] + "，FAIL：" + yuexiuDailyPassRate[1] + "，TOTAL：" + yuexiuDailyPassRate[2]
                + "\n\n>##### **大屏独立项目 缺陷清除率**：" + bugInfo[0]
                + "\n>##### **大屏独立项目 未关闭缺陷**：" + bugInfo[1]
                + "\n>请 * @17610248107 @15011479599 @15898182672 * 关注"
                + "\n\n>失败用例信息点击链接->开放平台->用例管理[详情链接](" + hostPort + ")"
                + "\n>Bug信息查看[详情链接](" + algorithomBugLink +")";

        //add @ following rds
        //17610248107 廖祥茹
        //13581630214 马琨
        //18513118484 杨航
        //13259979249 黄鑫
        //15898182672 华成裕
        //18810332354 刘峤
        //15011479599 谢志东
        //17600739322 李俊延
        String[] atArray = {"17610248107", "15011479599", "15898182672"};
        DingChatbot.sendMarkdown(msg, atArray, false);
        DingChatbot.sendMarkdown(msg);
    }

    public void managementPlatformAlarm(String[] passRate,
                                  String[] bugInfo) {

        DingChatbot.WEBHOOK_TOKEN = this.dingWebhook;
        this.algorithomBugLink = "http://192.168.50.2:8081/bug-browse-3.html";
        DateTimeUtil dt = new DateTimeUtil();

        String summary = "管理后台每日回归&缺陷简报";
        String msg = "### " + summary + "\n";
        String today = dt.getHistoryDate(0);

        msg += "\n\n#### " + today + " 记录信息\n";
        msg +=  "\n\n>##### **模块：管理后台，RD：杨航、华成裕**"
                + "\n>##### 通过率：" + passRate[0] + "，FAIL：" + passRate[1] + "，TOTAL：" + passRate[2]
                + "\n\n>##### **开放平台 缺陷清除率**：" + bugInfo[0]
                + "\n>##### **开放平台 未关闭缺陷**：" + bugInfo[1]
                + "\n>请 *@17610248107、@18513118484、@15898182672* 关注"
                + "\n\n>失败用例信息点击链接->开放平台->用例管理[详情链接](" + hostPort + ")"
                + "\n>Bug信息查看[详情链接](" + algorithomBugLink +")";

//        DingChatbot.sendMarkdown(msg);

        //add @ following rds
        //17610248107 廖祥茹
        //13581630214 马琨
        //18513118484 杨航
        //13259979249 黄鑫
        //15898182672 华成裕
        //18810332354 刘峤
        //15011479599 谢志东
        //17600739322 李俊延
        String[] atArray = {"17610248107", "18513118484", "15898182672"};
        DingChatbot.sendMarkdown(msg, atArray, false);

    }
}
