package com.haisheng.framework.testng.bigScreen.itemXundian.common.util;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumDingTalkWebHook;
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
import java.util.Map;

public class DingPushUtil {
    private final Logger logger = LoggerFactory.getLogger(DingPushUtil.class);
    private String WEBHOOK_TOKEN = EnumDingTalkWebHook.KLL.getWebHook();

    public void changeWeHook(String webhookToken) {
        WEBHOOK_TOKEN = webhookToken;
    }

    public void send(String messageDetail) {
        String nowTime = DateTimeUtil.getFormat(new Date(), "yyyy-MM-dd HH:mm:ss");
        Map<String, Object> map = new HashMap<>();
        map.put("title", "巡检");
        StringBuilder sb = new StringBuilder();
        sb.append("## **").append("线上巡检数据提醒").append("**").append("\n")
                .append("### **").append(nowTime).append("**").append("\n")
                .append(messageDetail).append("\n");
        map.put("text", sb);
        send(map);
    }

    public void send(Map<String, Object> map) {
        try {
            String title = map.get("title").toString();
            String text = map.get("text").toString();
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

    private void send(JSONObject object) throws IOException {
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

    public void alarmTo(String[] who) {
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
