package com.haisheng.framework.testng.bigScreen.xundian.util;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.xundian.bean.DetailMessage;
import com.haisheng.framework.testng.bigScreen.xundian.bean.PvUvInfo;
import com.haisheng.framework.testng.bigScreen.xundian.bean.ShopInfo;
import com.haisheng.framework.util.CommonUtil;
import com.haisheng.framework.util.DateTimeUtil;
import com.haisheng.framework.util.DingChatbot;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DingPushUtil {
    private static boolean isAt;
    public static final Logger logger = LoggerFactory.getLogger(com.haisheng.framework.testng.bigScreen.crm.wm.util.DingPushUtil.class);
    private static final String WEBHOOK_TOKEN = "https://oapi.dingtalk.com/robot/send?access_token=30334438d6943ac6a34ed2708a9ef16b15be3e502e5591ffc95224bdcacf1ac2";

    public static void sendMessage(List<ShopInfo> shopInfos) {
        StringBuilder sb = new StringBuilder();
        shopInfos.forEach(shopInfo -> {
            String name = shopInfo.getShopName();
            sb.append("\n").append("### **").append("门店：").append(name).append("**").append("\n");
            shopInfo.getRealTimeShopPvUvBeanList().forEach(realTimeShopPvUvBean -> sb.append("\n").append("##### **").append("时间段：").append(realTimeShopPvUvBean.getTime()).append("**").append("\n")
                    .append("###### 昨日人次：").append(realTimeShopPvUvBean.getYesterdayPv()).append("        昨日人数：").append(realTimeShopPvUvBean.getYesterdayUv()).append("\n"));
            CommonUtil.valueView(shopInfo.getPvSum(), shopInfo.getUvSum());
            isAt = shopInfo.getPvSum() == 0 || shopInfo.getUvSum() == 0;
        });
        send(sb.toString());
        if (isAt) {
            alarmTo(new String[]{"15321527989"});
        }
    }

    public static void sendPVUVMessage(PvUvInfo pvUvInfo) {
        List<DetailMessage> detailMessages = pvUvInfo.getDetailMessages();
        StringBuilder sb = new StringBuilder();
        sb.append("\n").append("### **").append("门店：").append(pvUvInfo.getShopId()).append("**").append("\n");
        detailMessages.forEach(detailMessage -> sb.append("\n").append("##### **").append("数据：").append(detailMessage.getSourceName()).append("**").append("\n")
                .append("###### 去重数据：").append(detailMessage.getHasReception()).append("      不去重数据：").append(detailMessage.getNoReception()).append("\n"));
//        System.err.println(sb);
        send(sb.toString());
    }

    public static void send(String messageDetail) {
        Map<String, String> map = new HashMap<>();
        map.put("title", "巡检");
        String text = "## **" + "线上巡检数据提醒：" + "**" + "\n"
                + "\n" + "### **" + DateTimeUtil.getFormat(new Date(), "yyyy-MM-dd HH:mm:ss") + "**" + "\n"
                + "\n" + messageDetail + "\n";
        map.put("text", text);
        send(map);
    }

    public static void send_PV_UV(String messageDetail) {
        Map<String, String> map = new HashMap<>();
        map.put("title", "购物中心数据监控");
        String text = "## **" + "流量巡检：" + "**" + "\n"
                + "\n" + "### **" + DateTimeUtil.getFormat(new Date(), "yyyy-MM-dd HH:mm:ss") + "**" + "\n"
                + "\n" + messageDetail + "\n";
        map.put("text", text);
        send(map);
    }

    public static void send(Map<String, String> map) {
        try {
            String text = map.get("text");
            String title = map.get("title");
            JSONObject object = new JSONObject();
            JSONObject markdown = new JSONObject();
            object.put("msgtype", "markdown");
            object.put("markdown", markdown);
            markdown.put("title", title);
            markdown.put("text", text);
            send(object);
        } catch (Exception e) {
            logger.error(e.toString());
        }
    }

    private static void send(JSONObject object) throws IOException {
        HttpClient httpclient = HttpClients.createDefault();
        HttpPost httppost = new HttpPost(WEBHOOK_TOKEN);
        httppost.addHeader("Content-Type", "application/json");
        StringEntity se = new StringEntity(JSONObject.toJSONString(object), "utf-8");
        httppost.setEntity(se);
        HttpResponse response = httpclient.execute(httppost);
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            String result = EntityUtils.toString(response.getEntity(), "utf-8");
            logger.info(result);
        }
    }

    public static void alarmTo(String[] who) {
        DingChatbot.WEBHOOK_TOKEN = WEBHOOK_TOKEN;
        String msg = "#### 自动巡检发现门店数据为空" + "请 XXX及时查看" + "\n";
        StringBuilder toAt = new StringBuilder();
        for (String rd : who) {
            toAt.append("@").append(rd).append(" ");
        }
        msg = msg.replace("XXX", toAt.toString());
        DingChatbot.sendMarkdown(msg, who, false);
    }
}
