package com.haisheng.framework.testng.bigScreen.xundianDaily.wm.util;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumDingTalkWebHook;
import com.haisheng.framework.testng.bigScreen.xundianDaily.wm.bean.ShopInfo;
import com.haisheng.framework.util.DateTimeUtil;
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
    public static final Logger logger = LoggerFactory.getLogger(com.haisheng.framework.testng.bigScreen.crm.wm.util.DingPushUtil.class);
    private static final String WEBHOOK_TOKEN = "https://oapi.dingtalk.com/robot/send?access_token=30334438d6943ac6a34ed2708a9ef16b15be3e502e5591ffc95224bdcacf1ac2";

    public static void sendMessage(List<ShopInfo> shopInfos) {
        StringBuilder sb = new StringBuilder();
        shopInfos.forEach(shopInfo -> {
            String shopName = shopInfo.getShopName();
            sb.append("\n").append("### **").append("门店：").append(shopName).append("**").append("\n");
            shopInfo.getRealTimeShopPvUvBeanList().forEach(realTimeShopPvUvBean -> {
                sb.append("\n").append("##### **").append("时间段：").append(realTimeShopPvUvBean.getTime()).append("**").append("\n")
                        .append("###### 昨日人次：").append(realTimeShopPvUvBean.getYesterdayPv()).append("        昨日人数：").append(realTimeShopPvUvBean.getYesterdayUv()).append("\n");
            });
        });
        send(sb.toString());
    }

    public static void send(String messageDetail) {
        Map<String, String> map = new HashMap<>();
        map.put("title", "巡检");
        String text = "## **" + "线上巡检数据提醒：" + "**" + "\n" +
                "\n" + "### **" + DateTimeUtil.getFormat(new Date(), "yyyy-MM-dd HH:mm:ss") + "**" + "\n" +
                "\n" + messageDetail + "\n";
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
}
