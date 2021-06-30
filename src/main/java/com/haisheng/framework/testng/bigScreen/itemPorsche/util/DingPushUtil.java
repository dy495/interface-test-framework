package com.haisheng.framework.testng.bigScreen.itemPorsche.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.enumerator.config.EnumDingTalkWebHook;
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
import java.util.Map;

public class DingPushUtil {
    public static final Logger logger = LoggerFactory.getLogger(DingPushUtil.class);
    private static final String WEBHOOK_TOKEN = EnumDingTalkWebHook.XMX.getWebHook();

    public static void sendText(String msg, String sql, String caseName) {
        try {
            String date = DateTimeUtil.getFormat(new Date(), "yyyy-MM-dd HH:mm:ss");
            JSONObject object = new JSONObject();
            JSONObject markdown = new JSONObject();
            object.put("msgtype", "markdown");
            object.put("markdown", markdown);
            markdown.put("title", "balabala");
            markdown.put("text", "### " + "**" + "Ding，有空看一下" + "**" + "\n"
                    + "\n" + date + "\n"
                    + "\n" + "caseName：" + caseName + "\n"
                    + "\n" + "SQL错误：" + msg + "\n"
                    + "\n" + "SQL语句：" + sql + "\n"
                    + "\n" + "@15321527989" + "\n");
            JSONObject at = new JSONObject();
            JSONArray atMobiles = new JSONArray();
            atMobiles.add("15321527989");
            at.put("atMobiles", atMobiles);
            at.put("isAtAll", false);
            object.put("at", at);
            send(object);
        } catch (Exception e) {
            logger.error(e.toString());
        }
    }

    public static void sendText(final String msg) {
        Map<String, String> map = new HashMap<>();
        map.put("title", "balabala");
        map.put("text", "### " + "**" + "Ding，有空看一下" + "**" + "\n"
                + "\n" + DateTimeUtil.getFormat(new Date(), "yyyy-MM-dd HH:mm:ss") + "\n"
                + "\n" + "异常：" + msg + "\n");
        sendText(map);
    }

    public static void sendText(Map<String, String> map) {
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
