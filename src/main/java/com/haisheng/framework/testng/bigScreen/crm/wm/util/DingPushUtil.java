package com.haisheng.framework.testng.bigScreen.crm.wm.util;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumDingTalkWebHook;
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

import java.util.Date;

public class DingPushUtil {
    private static final String WEBHOOK_TOKEN = EnumDingTalkWebHook.BA.getWebHook();
    private static final Logger logger = LoggerFactory.getLogger(DingPushUtil.class);

    public static void sendTxt(String msg, String sql) {
        try {
            String date = DateTimeUtil.getFormat(new Date(), "yyyy-MM-dd HH:mm:ss");
            HttpClient httpclient = HttpClients.createDefault();
            HttpPost httppost = new HttpPost(WEBHOOK_TOKEN);
            httppost.addHeader("Content-Type", "application/json; charset=utf-8");
            JSONObject object = new JSONObject();
            JSONObject markdown = new JSONObject();
            object.put("msgtype", "markdown");
            object.put("markdown", markdown);
            markdown.put("title", "balabala");
            markdown.put("text", "### " + "**" + "拉垮了，有空看一下" + "**" + "\n"
                    + "\n" + date + "\n"
                    + "\n" + "SQL错误：" + msg + "\n"
                    + "\n" + "SQL语句：" + sql + "\n");
            StringEntity se = new StringEntity(JSONObject.toJSONString(object), "utf-8");
            httppost.setEntity(se);
            HttpResponse response = httpclient.execute(httppost);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                String result = EntityUtils.toString(response.getEntity(), "utf-8");
                logger.info(result);
            }
        } catch (Exception e) {
            logger.error(e.toString());
        }
    }
}
