package com.haisheng.framework.util;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DingChatbot {

    private static String WEBHOOK_TOKEN = "https://oapi.dingtalk.com/robot/send?access_token=be30edcb935927a9b07aecac5e1483413b8e5ef340ab1534dc82077aa3ea60ae";
    private static Logger logger = LoggerFactory.getLogger("DingChatbot");

    public static void sendTxt(String msg) {
        try {
            HttpClient httpclient = HttpClients.createDefault();

            HttpPost httppost = new HttpPost(WEBHOOK_TOKEN);
            httppost.addHeader("Content-Type", "application/json; charset=utf-8");

            String textMsg = "{ \"msgtype\": \"text\", " +
                    "\"text\": {\"content\": \"" + msg +"\"}}";
            StringEntity se = new StringEntity(textMsg, "utf-8");
            httppost.setEntity(se);

            HttpResponse response = httpclient.execute(httppost);
            if (response.getStatusLine().getStatusCode()== HttpStatus.SC_OK){
                String result= EntityUtils.toString(response.getEntity(), "utf-8");
                logger.info(result);
            }

        } catch (Exception e) {
            logger.error(e.toString());
        }
    }
    public static void sendLinkTxt(String msg, String linkUrl) {
        try {
            HttpClient httpclient = HttpClients.createDefault();

            HttpPost httppost = new HttpPost(WEBHOOK_TOKEN);
            httppost.addHeader("Content-Type", "application/json; charset=utf-8");

            String linkMsg = "{\n" +
                    "    \"msgtype\": \"link\", " +
                    "    \"link\": {" +
                    "        \"text\":\""+ msg + "\", " +
                    "       \"title\": \"警报\", " +
                    "        \"picUrl\": \"\", " +
                    "        \"messageUrl\": \"" + linkUrl +"\"" +
                    "    }\n" +
                    "}";
            StringEntity se = new StringEntity(linkMsg, "utf-8");
            httppost.setEntity(se);

            HttpResponse response = httpclient.execute(httppost);
            if (response.getStatusLine().getStatusCode()== HttpStatus.SC_OK){
                String result= EntityUtils.toString(response.getEntity(), "utf-8");
                logger.info(result);
            }

        } catch (Exception e) {
            logger.error(e.toString());
        }
    }

    //支持的md语法，atMobiles 多个手机号用逗号分隔
    //    标题
    //# 一级标题
    //## 二级标题
    //### 三级标题
    //#### 四级标题
    //##### 五级标题
    //###### 六级标题
    // 
    //    引用
    //> A man who stands for nothing will fall for anything.
    // 
    //    文字加粗、斜体
    //**bold**
    //            *italic*
    //             
    //    链接
    //[this is a link](https://www.dingtalk.com/)
    //             
    //    图片
    //![](http://name.com/pic.jpg)
    //             
    //    无序列表
    //- item1
    //- item2
    // 
    //    有序列表
    //1. item1
    //2. item2
    public static void sendMarkdown(String msg) {
        try {
            HttpClient httpclient = HttpClients.createDefault();

            HttpPost httppost = new HttpPost(WEBHOOK_TOKEN);
            httppost.addHeader("Content-Type", "application/json; charset=utf-8");

            String textMsg = "{" +
                    "\"msgtype\": \"markdown\"," +
                    "\"markdown\": {\"title\":\"警报\"," +
                                   "\"text\": \"" + msg + "\"" +
                                   "}" +
                    "}";

//                    "\"at\": {" +
//                                "\"atMobiles\": [" +
//                                                    "\"" + atMobiles + "\"" +
//                                                "], " +
//                                "\"isAtAll\": "+ isAtAll +
//                    "}}";
            StringEntity se = new StringEntity(textMsg, "utf-8");
            httppost.setEntity(se);

            HttpResponse response = httpclient.execute(httppost);
            if (response.getStatusLine().getStatusCode()== HttpStatus.SC_OK){
                String result= EntityUtils.toString(response.getEntity(), "utf-8");
                logger.info(result);
            }

        } catch (Exception e) {
            logger.error(e.toString());
        }
    }
    public static String getMarkdown(String summary, String detail, String picPath, String linkUrl, String atPersons) {
        String msg = "#### " + summary + "\n"
                + "> " + detail + "\n\n"
                + "> " + "![screenshot](" + picPath + ")\n"
                + "> " + atPersons + "请及时查看 [详细](" + linkUrl + ")";

        return msg;
    }
}