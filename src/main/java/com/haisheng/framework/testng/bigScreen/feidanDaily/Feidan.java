package com.haisheng.framework.testng.bigScreen.feidanDaily;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.arronlong.httpclientutil.HttpClientUtil;
import com.arronlong.httpclientutil.builder.HCB;
import com.arronlong.httpclientutil.common.HttpConfig;
import com.arronlong.httpclientutil.common.HttpHeader;
import com.arronlong.httpclientutil.exception.HttpProcessException;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.haisheng.framework.model.bean.Case;
import com.haisheng.framework.model.bean.ProtectTime;
import com.haisheng.framework.model.bean.ReportTime;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.util.*;
import org.apache.commons.lang.text.StrSubstitutor;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.testng.Assert;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author : huachengyu
 * @date :  2019/11/21  14:55
 */

public class Feidan {

    public Logger logger = LoggerFactory.getLogger(this.getClass());
    public String failReason = "";
    public String response = "";
    public boolean FAIL = false;
    public Case aCase = new Case();

    StringUtil stringUtil = new StringUtil();
    DateTimeUtil dateTimeUtil = new DateTimeUtil();
    CheckUtil checkUtil = new CheckUtil();
    public QADbUtil qaDbUtil = new QADbUtil();
    public int APP_ID = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
    public int CONFIG_ID = ChecklistDbInfo.DB_SERVICE_ID_FEIDAN_DAILY_SERVICE;

    public String CI_CMD = "curl -X POST http://qarobot:qarobot@192.168.50.2:8080/job/feidan-daily-test/buildWithParameters?case_name=";

    public String DEBUG = System.getProperty("DEBUG", "true");

    public String authorization = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiLotornp4DmtYvor5XotKblj7ciLCJ1aWQiOiJ1aWRfZWY2ZDJkZTUiLCJsb2dpblRpbWUiOjE1NzQyNDE5NDIxNjV9.lR3Emp8iFv5xMZYryi0Dzp94kmNT47hzk2uQP9DbqUU";

    public HttpConfig config;

    DateTimeUtil dt = new DateTimeUtil();

    //  -----------------------------------------??????------------------------------------------
    String wudongChannelIdStr = "5";
    String lianjiaChannelStr = "1";

//  ------------------------------------------?????????-----------------------------------------------------

    String lianjiaToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiLmtYvor5XjgJDli7_liqjjgJEiLCJ1aWQiOjIxMzYsImxvZ2luVGltZSI6MT" +
            "U3ODk5OTY2NjU3NH0.kQsEw_wGVmPQ4My1p-FNZ556FJC7W177g7jfjFarTu4";
    String lianjiaFreezeStaffIdStr = "2136";
    int lianjiaFreezeStaffIdInt = 2136;


    String wudongToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiLjgJDli7_li" +
            "qjjgJExIiwidWlkIjoyMDk4LCJsb2dpblRpbWUiOjE1Nzg1NzQ2MjM4NDB9.exDJ6avJKJd3ezQkYc4fmUkHvXaukqfgjThkpoYgnAw";

    String wudongStaffIdStr = "2098";

//    -------------------------------------------????????????-----------------------------------------------------

    String zhangName = "?????????";
    String zhangPhone = "19111311116";

    long lianjiaReportTime = 1547014265000L;//2019-01-09 14:11:05
    long noChannelReportTime = 1547034265000L;//2019-01-09 19:44:25

    long firstAppearTime = 1609733247344L;

//    *****************************************************3?????????************************************************************

    public JSONObject customerEditPC(String cid, String customerName, String phone, String adviserName, String
            adviserPhone) throws Exception {
        String url = "/risk/customer/edit/" + cid;
        String json =
                "{\n" +
                        "\"customer_name\":\"" + customerName + "\"," +
                        "\"phone\":\"" + phone + "\"," +
                        "\"adviser_name\":\"" + adviserName + "\"," +
                        "\"adviser_phone\":\"" + adviserPhone + "\"," +
                        "\"shop_id\":" + getShopId() +
                        "}";

        String res = httpPostWithCheckCode(url, json);

        Thread.sleep(1000);

        return JSON.parseObject(res).getJSONObject("data");
    }

    public void newCustomer(int channelId, String channelStaffName, String channelStaffPhone, String
            adviserName, String adviserPhone, String phone, String customerName, String gender) throws Exception {

        String url = "/risk/customer/insert";

        String json =
                "{\n" +
                        "    \"customer_name\":\"" + customerName + "\"," +
                        "    \"phone\":\"" + phone + "\",";
        if (!"".equals(adviserName)) {
            json += "    \"adviser_name\":\"" + adviserName + "\",";
            json += "    \"adviser_phone\":\"" + adviserPhone + "\",";
        }

        if (channelId != -1) {
            json += "    \"channel_id\":" + channelId + "," +
                    "    \"channel_staff_name\":\"" + channelStaffName + "\"," +
                    "    \"channel_staff_phone\":\"" + channelStaffPhone + "\",";
        }

        json +=
                "    \"gender\":\"" + gender + "\"," +
                        "    \"shop_id\":" + getShopId() + "" +
                        "}";

        httpPostWithCheckCode(url, json);
    }

    public String newCustomerNoCheckCode(int channelId, String channelStaffName, String channelStaffPhone, String
            adviserName,
                                         String adviserPhone, String phone, String customerName, String gender) {

        String url = "/risk/customer/insert";

        String res = "";

        String json =
                "{\n" +
                        "    \"customer_name\":\"" + customerName + "\"," +
                        "    \"phone\":\"" + phone + "\",";
        if (!"".equals(adviserName)) {
            json += "    \"adviser_name\":\"" + adviserName + "\",";
            json += "    \"adviser_phone\":\"" + adviserPhone + "\",";
        }

        if (channelId != -1) {
            json += "    \"channel_id\":" + channelId + "," +
                    "    \"channel_staff_name\":\"" + channelStaffName + "\"," +
                    "    \"channel_staff_phone\":\"" + channelStaffPhone + "\",";
        }

        json +=
                "    \"gender\":\"" + gender + "\"," +
                        "    \"shop_id\":" + getShopId() + "" +
                        "}";

        try {
            res = httpPost(url, json);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return res;
    }

    /**
     * 3.4 ????????????
     */
    public JSONObject customerList(String phone, String channel, String adviser, int page, int pageSize) throws
            Exception {

        String url = "/risk/customer/list";

        String json =
                "{\n";

        if (!"".equals(phone)) {
            json += "    \"phone_or_name\":\"" + phone + "\",";
        }

        if (!"".equals(channel)) {
            json += "    \"channel_id\":" + channel + ",";
        }

        if (!"".equals(adviser)) {
            json += "    \"adviser_id\":" + adviser + ",";
        }

        json += "    \"shop_id\":" + getShopId() + "," +
                "    \"page\":" + page + "," +
                "    \"size\":" + pageSize +
                "}";


        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * 3.10 ??????????????????
     */
    public String customerEditPCNoCheckCode(String cid, String customerName, String phone, String
            adviserName, String adviserPhone) throws Exception {
        String url = "/risk/customer/edit/" + cid;
        String json =
                "{\n" +
                        "\"customer_name\":\"" + customerName + "\"," +
                        "\"phone\":\"" + phone + "\"," +
                        "\"adviser_name\":\"" + adviserName + "\"," +
                        "\"adviser_phone\":\"" + adviserPhone + "\"," +
                        "\"shop_id\":" + getShopId() +
                        "}";

        String res = httpPost(url, json);

        return res;
    }


    //**********************************************************4?????????**************************************************************


    /**
     * 4.4 ????????????
     */
    public JSONObject createOrder(String phone, String orderId, String faceUrl, int channelId, String smsCode) throws
            Exception {

        String url = "/risk/order/createOrder";

        String json =
                "{" +
                        "    \"shop_id\":" + getShopId() + "," +
                        "    \"phone\":\"" + phone + "\"," +
                        "    \"face_url\":\"" + faceUrl + "\"," +
                        "    \"order_id\":\"" + orderId + "\",";
        if (channelId != -1) {
            json += "    \"channel_id\":\"" + channelId + "\",";
        }

        json += "    \"sms_code\":\"" + smsCode + "\"" +
                "}";
        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res);
    }

    public JSONObject createOrderNOCode(String phone, String orderId, String faceUrl, int channelId) throws
            Exception {

        String url = "/risk/order/createOrder";

        String json =
                "{" +
                        "    \"shop_id\":" + getShopId() + "," +
                        "    \"phone\":\"" + phone + "\"," +
                        "    \"face_url\":\"" + faceUrl + "\"," ;
        if (channelId != -1) {
            json += "    \"channel_id\":\"" + channelId + "\",";
        }
        json = json+ "    \"order_id\":\"" + orderId + "\""+
                "}";
        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res);
    }

    public String createOrderNoCode(String phone, String orderId, String faceUrl, int channelId, String smsCode) throws Exception {

        String url = "/risk/order/createOrder";

        String json =
                "{" +
                        "    \"shop_id\":" + getShopId() + "," +
                        "    \"phone\":\"" + phone + "\"," +
                        "    \"face_url\":\"" + faceUrl + "\"," +
                        "    \"order_id\":\"" + orderId + "\",";
        if (channelId != -1) {
            json += "    \"channel_id\":\"" + channelId + "\",";
        }

        json += "    \"sms_code\":\"" + smsCode + "\"" +
                "}";
        String res = httpPost(url, json);

        return res;
    }

    public JSONObject orderList(String namePhone, int pageSize) throws Exception {

        String url = "/risk/order/list";
        String json =
                "{" +
                        "    \"shop_id\":" + getShopId() + "," +
                        "    \"customer_name\":\"" + namePhone + "\"," +
                        "    \"page\":1" + "," +
                        "    \"size\":" + pageSize +
                        "}";
        String res = httpPost(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    public JSONObject searchReportInfoByPhone(String orderId, String phone) throws Exception {

        String url = "/risk/order/searchReportInfoByPhone";

        String json =
                "{\n" +
                        "    \"shop_id\":\"" + getShopId() + "\"," +
                        "    \"order_id\":\"" + orderId + "\"," +
                        "    \"phone\":\"" + phone + "\"" +
                        "}\n";

        String s = httpPostWithCheckCode(url, json);
        return JSON.parseObject(s).getJSONObject("data");
    }

    /**
     * 4.5 ????????????
     */
    public static String ORDER_DETAIL_JSON = "{\"order_id\":\"${orderId}\"," +
            "\"shop_id\":${shopId}}";

    public JSONObject orderDetail(String orderId) throws Exception {
        String url = "/risk/order/detail";

        String json = StrSubstitutor.replace(ORDER_DETAIL_JSON, ImmutableMap.builder()
                .put("shopId", getShopId())
                .put("orderId", orderId)
                .build()
        );
        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * 4.6 ????????????????????????
     */
    public JSONObject orderLinkList(String orderId) throws Exception {
        String url = "/risk/order/link/list";
        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + "," +
                        "    \"orderId\":\"" + orderId + "\"," +
                        "    \"page\":\"" + 1 + "\"," +
                        "    \"size\":\"" + 1000 + "\"" +
                        "}";

        String res = httpPostWithCheckCode(url, json);//???????????????????????????????????????json??????

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * 4.8 ????????????
     */
    public JSONObject orderList(int status, String namePhone, int pageSize) throws Exception {

        String url = "/risk/order/list";
        String json =
                "{" +
                        "    \"shop_id\":" + getShopId() + "," +
                        "    \"page\":1" + ",";
        if (status != -1) {
            json += "    \"status\":\"" + status + "\",";
        }

        if (!"".equals(namePhone)) {
            json += "    \"customer_name\":\"" + namePhone + "\",";
        }

        json += "    \"size\":" + pageSize + "" +
                "}";
        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }


    public JSONObject orderList(String channelId, String status, String isAudited, String namePhone, int pageSize) throws Exception {

        String url = "/risk/order/list";
        String json =
                "{" +
                        "    \"shop_id\":" + getShopId() + "," +
                        "    \"page\":1" + ",";
        if (!"".equals(status)) {
            json += "    \"status\":\"" + status + "\",";
        }

        if (!"".equals(namePhone)) {
            json += "    \"customer_name\":\"" + namePhone + "\",";
        }

        if (!"".equals(isAudited)) {
            json += "    \"is_audited\":\"" + isAudited + "\",";
        }

        if (!"".equals(channelId)) {
            json += "    \"channel_id\":\"" + channelId + "\",";
        }

        json += "    \"size\":" + pageSize + "" +
                "}";
        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * 4.15 ????????????
     */
    public JSONObject orderAudit(String orderId, String visitor) throws Exception {
        String url = "/risk/order/status/audit";
        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + "," +
                        "    \"orderId\":\"" + orderId + "\"," +
                        "    \"visitor\":\"" + visitor + "\"" +
                        "}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * 15.2 ???????????????
     */
    public JSONObject reportCreate(String orderId) throws Exception {
        String url = "/risk/evidence/risk-report/download";
        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + ",\n" +
                        "    \"orderId\":\"" + orderId + "\"" +
                        "}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    public JSONObject uploadImage(String imagePath, String pathText) {
        String url = "http://dev.store.winsenseos.cn/risk/imageUpload";
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);

        httpPost.addHeader("authorization", authorization);
        httpPost.addHeader("shop_id", String.valueOf(getShopId()));
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();

        File file = new File(imagePath);
        try {
            builder.addBinaryBody(
                    "img_file",
                    new FileInputStream(file),
                    ContentType.IMAGE_JPEG,
                    file.getName()
            );

            builder.addTextBody("path", pathText, ContentType.MULTIPART_FORM_DATA);

            HttpEntity multipart = builder.build();
            httpPost.setEntity(multipart);
            CloseableHttpResponse response = httpClient.execute(httpPost);
            HttpEntity responseEntity = response.getEntity();
            this.response = EntityUtils.toString(responseEntity, "UTF-8");

            checkCode(this.response, StatusCode.SUCCESS, file.getName() + ">>>");
            logger.info("response: " + this.response);

        } catch (Exception e) {
            failReason = e.toString();
            e.printStackTrace();
        }

        return JSON.parseObject(this.response).getJSONObject("data");
    }


    //    ****************************************************************6?????????***************************************************************************

    /**
     * ????????????
     */
    public void channelEdit(String channelId, String channelName, String owner_id, String phone, String ruleId) throws
            Exception {

        String url = "/risk/channel/edit/" + channelId;
        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + "," +
                        "    \"channel_name\":\"" + channelName + "\"," +
                        "    \"owner_id\":\"" + owner_id + "\"," +
//                        "    \"phone\":\"" + phone + "\"," +
                        "    \"rule_id\":\"" + ruleId + "\"" +
                        "}";

        httpPostWithCheckCode(url, json);
    }

    public void channelEditFinally(String channelId, String channelName, String owner_id, String phone, String ruleId) {

        String url = "/risk/channel/edit/" + channelId;
        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + "," +
                        "    \"channel_name\":\"" + channelName + "\"," +
                        "    \"owner_id\":\"" + owner_id + "\"," +
//                        "    \"phone\":\"" + phone + "\"," +
                        "    \"rule_id\":\"" + ruleId + "\"" +
                        "}";

        try {
            httpPostWithCheckCode(url, json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public JSONObject channelStaffList(String namePhone, String channelId, int page, int size) throws Exception {
        String url = "/risk/channel/staff/page";
        String json =
                "{\n" +
                        "\"name_phone\":\"" + namePhone + "\"," +
                        "\"channel_id\":\"" + channelId + "\"," +
                        "\"page\":\"" + page + "\"," +
                        "\"size\":\"" + size + "\"," +
                        "\"shop_id\":" + getShopId() +
                        "}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    public String changeState(String id) throws Exception {
        String url = "/risk/channel/staff/state/change/" + id;
        String json =
                "{}";

        String res = httpPostWithCheckCode(url, json);

        return res;
    }

    public String changeStateNocode(String id) throws Exception {
        String url = "/risk/channel/staff/state/change/" + id;
        String json =
                "{}";

        String res = httpPost(url, json);

        return res;
    }


    public JSONObject addChannelStaff(String channelId, String staffName, String phone) throws Exception {

        String url = "/risk/channel/staff/register";

        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + "," +
                        "    \"staff_name\":\"" + staffName + "\"," +
                        "    \"channel_id\":\"" + channelId + "\"," +
                        "    \"phone\":\"" + phone + "\"" +
                        "}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    public String addChannelStaffNoCode(String channelId, String staffName, String phone) throws Exception {

        String url = "/risk/channel/staff/register";

        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + "," +
                        "    \"staff_name\":\"" + staffName + "\"," +
                        "    \"channel_id\":\"" + channelId + "\"," +
                        "    \"phone\":\"" + phone + "\"" +
                        "}";

        String res = httpPost(url, json);

        return res;
    }

    public JSONObject editChannelStaff(String id, String channelId, String staffName, String phone, String faceUrl) throws Exception {

        String url = "/risk/channel/staff/edit/" + id;

        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + "," +
                        "    \"staff_name\":\"" + staffName + "\"," +
                        "    \"channel_id\":\"" + channelId + "\"," +
                        "    \"face_url\":\"" + faceUrl + "\"," +
                        "    \"phone\":\"" + phone + "\"" +
                        "}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    public JSONObject channelStaffList(String channelId, int page, int size) throws Exception {

        String url = "/risk/channel/staff/page";

        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + "," +
                        "    \"channel_id\":\"" + channelId + "\"," +
                        "    \"page\":\"" + page + "\"," +
                        "    \"size\":\"" + size + "\"" +
                        "}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    public void changeChannelStaffState(String staffId) throws Exception {
        String json = "{}";

        httpPostWithCheckCode("/risk/channel/staff/state/change/" + staffId, json);
    }


    public String addAdviserNoCode(String staffName, String phone, String faceUrl) throws Exception {

        String url = "/risk/staff/add";

        String json =
                "{\n" +
                        "    \"staff_name\":\"" + staffName + "\"," +
                        "    \"phone\":\"" + phone + "\"," +
                        "    \"face_url\":\"" + faceUrl + "\"," +
                        "\"shop_id\":" + getShopId() +
                        "}";

        String res = httpPost(url, json);

        return res;
    }

    public JSONObject adviserEdit(String id, String staffName, String phone, String faceUrl) throws Exception {

        String url = "/risk/staff/edit/" + id;

        String json =
                "{\n" +
                        "    \"staff_name\":\"" + staffName + "\"," +
                        "    \"phone\":\"" + phone + "\"," +
                        "    \"face_url\":\"" + faceUrl + "\"," +
                        "\"shop_id\":" + getShopId() +
                        "}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }


    public JSONObject adviserDelete(String id) throws Exception {
        String url = "/risk/staff/delete/" + id;
        String json =
                "{}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    public JSONObject adviserList(String namePhone, int page, int size) throws Exception {
        String url = "/risk/staff/page";
        String json =
                "{\n" +
                        "\"name_phone\":\"" + namePhone + "\"," +
                        "\"page\":\"" + page + "\"," +
                        "\"size\":\"" + size + "\"," +
                        "\"shop_id\":" + getShopId() +
                        "}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    public JSONObject addAdviser(String staffName, String phone, String faceUrl) throws Exception {

        String url = "/risk/staff/add";

        String json =
                "{\n" +
                        "    \"staff_name\":\"" + staffName + "\"," +
                        "    \"phone\":\"" + phone + "\"," +
                        "    \"face_url\":\"" + faceUrl + "\"," +
                        "\"shop_id\":" + getShopId() +
                        "}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }


    public void addChannel(String channelName, String owner_id, String phone, String ruleId) throws Exception {
        String url = "/risk/channel/add";
        String json =
                "{\n" +
                        "    \"channel_name\":\"" + channelName + "\"," +
                        "    \"owner_id\":\"" + owner_id + "\"," +
//                        "    \"phone\":\"" + phone + "\"," +
                        "    \"rule_id\":\"" + ruleId + "\"," +
                        "\"shop_id\":" + getShopId() +
                        "}";

        String res = httpPostWithCheckCode(url, json);
    }
    public String creatQDzlr () throws Exception {
        String name= ""+System.currentTimeMillis();
        String email=System.currentTimeMillis()+"@qq.com";
        JSONArray arr = new JSONArray();
        JSONObject obj = new JSONObject();

        obj.put("role_id",1062);
        obj.put("role_name","?????????????????????-??????");
        JSONObject obj1 = new JSONObject();
        obj1.put("shop_id",4116);
        obj1.put("shop_name","???????????????(????????????/??????)");
        JSONArray a = new JSONArray();
        a.add(obj1);

        obj.put("shop_list",a);
        arr.add(obj);
        //??????
        accountAdd(name,phonenum(),email,arr);
        String asscountid = accountPage(1,10,name,null,email,null,null).getJSONArray("list").getJSONObject(0).getString("id");
        return  asscountid;
    }
    public String[] creatQDzlr2 () throws Exception {
        String asscountid[]=new String[3];
        String name= ""+System.currentTimeMillis();
        String email=System.currentTimeMillis()+"@qq.com";
        JSONArray arr = new JSONArray();
        JSONObject obj = new JSONObject();

        obj.put("role_id",1062);
        obj.put("role_name","?????????????????????-??????");
        JSONObject obj1 = new JSONObject();
        obj1.put("shop_id",4116);
        obj1.put("shop_name","???????????????(????????????/??????)");
        JSONArray a = new JSONArray();
        a.add(obj1);

        obj.put("shop_list",a);
        arr.add(obj);
        //??????
        accountAdd(name,phonenum(),email,arr);
        JSONObject dd = accountPage(1,10,name,null,email,null,null).getJSONArray("list").getJSONObject(0);
        asscountid[0] = dd.getString("id");
        asscountid[1] = dd.getString("name");
        asscountid[2] = dd.getString("phone");
        return  asscountid;
    }

    //????????????
    public JSONObject accountAdd(String name, String phone, String email,JSONArray role_list) throws Exception {
        String url = "/risk/account/add";

        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + "," +
                        "    \"name\":\"" + name + "\"," +
                        "    \"phone\":\"" + phone + "\"," +
                        "    \"email\":\"" + email + "\"," +

                        "    \"role_list\":" + role_list  +
                        "}";


        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }
    public String phonenum(){
        String phone = "1380110"+Integer.toString((int)((Math.random()*9+1)*1000));
        return phone;
    }

    //??????????????????
    public JSONObject accountPage(int page, int size, String name, String phone, String email, String rolename, String shopname) throws Exception {
        String url = "/risk/account/page";
        JSONObject json = new JSONObject();
        json.put("page", page);
        json.put("size", size);
        json.put("name", name);
//        json.put("phone", phone);
        json.put("email", email);
        json.put("role_name", rolename);
        json.put("shop_name", shopname);
        String result = httpPostWithCheckCode(url, json.toJSONString());
        return JSON.parseObject(result).getJSONObject("data");
    }

    public JSONObject channelList(int page, int size) throws Exception {
        String url = "/risk/channel/page";
        String json =
                "{\n" +
                        "    \"page\":\"" + page + "\"," +
                        "    \"size\":\"" + size + "\"," +
                        "\"shop_id\":" + getShopId() +
                        "}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }


//    **********************************************9???H5**********************************************************

    //    H5?????????????????????
    public JSONObject customerListH5(int page, int size, String token) throws
            Exception {
        String url = "/external/channel/customer/list";
        String json =
                "{\n" +
                        "    \"page\":\"" + page + "\"," +
                        "    \"size\":\"" + size + "\"," +
                        "    \"shop_id\":" + getShopId() + "," +
                        "    \"token\":\"" + token + "\"" +
                        "}\n";

        String response = httpPostWithCheckCode(url, json);

        return JSON.parseObject(response).getJSONObject("data");
    }

    public void customerEditH5(String cid, String customerName, String phone, String token) throws Exception {

        String url = "/external/channel/customer/edit";

        String json =
                "{\n" +
                        "    \"shop_id\":\"" + getShopId() + "\"," +
                        "    \"token\":\"" + token + "\"," +
                        "    \"cid\":\"" + cid + "\"," +
                        "    \"customer_name\":\"" + customerName + "\"," +
                        "    \"phone\":\"" + phone + "\"" +
                        "}\n";

        Thread.sleep(1000);

        httpPostWithCheckCode(url, json);
    }


    public String customerEditH5NoCode(String cid, String customerName, String phone, String token) throws Exception {

        String url = "/external/channel/customer/edit";

        String json =
                "{\n" +
                        "    \"shop_id\":\"" + getShopId() + "\"," +
                        "    \"token\":\"" + token + "\"," +
                        "    \"cid\":\"" + cid + "\"," +
                        "    \"customer_name\":\"" + customerName + "\"," +
                        "    \"phone\":\"" + phone + "\"" +
                        "}\n";

        String s = httpPost(url, json);

        return s;
    }

    /**
     * ??????????????????H5
     */
    public String customerReportH5(String staffId, String customerName, String phone, String gender, String token) throws Exception {
        String url = "/external/channel/customer/report";
        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + "," +
                        "    \"id\":\"" + staffId + "\"," +
                        "    \"customer_name\":\"" + customerName + "\"," +
                        "    \"customer_phone\":\"" + phone + "\"," +
                        "    \"gender\":\"" + gender + "\"," +
                        "    \"token\":\"" + token + "\"" +
                        "}\n";

        String response = httpPostWithCheckCode(url, json);

        return response;
    }

    public String customerReportH5NoCheckCode(String staffId, String customerName, String phone, String
            gender, String token) throws Exception {
        String url = "/external/channel/customer/report";
        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + "," +
                        "    \"id\":\"" + staffId + "\"," +
                        "    \"customer_name\":\"" + customerName + "\"," +
                        "    \"customer_phone\":\"" + phone + "\"," +
                        "    \"gender\":\"" + gender + "\"," +
                        "    \"token\":\"" + token + "\"" +
                        "}\n";

        String response = httpPost(url, json);

        return response;
    }

    /**
     * 9.6 ????????????
     */
    public JSONObject selfRegister(String customerName, String phone, String verifyCode, String adviserId, String
            hotPoints, String gender) throws Exception {
        String url = "/external/self-register/confirm";

        String json =
                "{\n" +
                        "    \"name\":\"" + customerName + "\"," +
                        "    \"gender\":\"" + gender + "\"," +
                        "    \"phone\":\"" + phone + "\"," +
                        "    \"verify_code\":\"" + verifyCode + "\",";
        if (!"".equals(adviserId)) {
            json += "    \"adviser_id\":\"" + adviserId + "\",";
        }
        if (!"".equals(hotPoints)) {
            json += "    \"hot_points\":[10],";
        }

        json += "    \"shop_id\":" + getShopId() + "}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }


    public JSONObject selfRegisterHot(String customerName, String phone, String verifyCode, String adviserId, int
            hotPoints, String gender) throws Exception {
        String url = "/external/self-register/confirm";

        String json =
                "{\n" +
                        "    \"name\":\"" + customerName + "\"," +
                        "    \"gender\":\"" + gender + "\"," +
                        "    \"phone\":\"" + phone + "\"," +
                        "    \"verify_code\":\"" + verifyCode + "\",";
        if (!"".equals(adviserId)) {
            json += "    \"adviser_id\":\"" + adviserId + "\",";
        }

        if (hotPoints == 0) {
            json += "    \"hot_points\":[],";
        } else if (hotPoints == 1) {
            json += "    \"hot_points\":[10],";
        } else if (hotPoints == 2) {
            json += "    \"hot_points\":[10,11],";
        } else if (hotPoints == 3) {
            json += "    \"hot_points\":[10,11,12],";
        }

        json += "    \"shop_id\":" + getShopId() + "}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    public String selfRegisterHotNoCode(String customerName, String phone, String verifyCode, String adviserId, int
            hotPoints, String gender) throws Exception {
        String url = "/external/self-register/confirm";

        String json =
                "{\n" +
                        "    \"name\":\"" + customerName + "\"," +
                        "    \"gender\":\"" + gender + "\"," +
                        "    \"phone\":\"" + phone + "\"," +
                        "    \"verify_code\":\"" + verifyCode + "\",";
        if (!"".equals(adviserId)) {
            json += "    \"adviser_id\":\"" + adviserId + "\",";
        }

        if (hotPoints == 0) {
            json += "    \"hot_points\":[],";
        } else if (hotPoints == 1) {
            json += "    \"hot_points\":[10],";
        } else if (hotPoints == 2) {
            json += "    \"hot_points\":[10,11],";
        } else if (hotPoints == 3) {
            json += "    \"hot_points\":[10,11,12],";
        }

        json += "    \"shop_id\":" + getShopId() + "}";

        String res = httpPost(url, json);

        return res;
    }


//    *************************************************************??????????????????*******************************************************

    public String witnessUpload(String cardId, String personName) throws Exception {
        String router = "/risk-inner/witness/upload";
        String json =
                "{\n" +
                        "    \"data\":{\n" +
                        "        \"person_name\":\"" + personName + "\"," +
                        "        \"capture_pic\":\"@1\"," +
                        "        \"is_pass\":true," +
                        "        \"card_pic\":\"@0\"," +
                        "        \"card_id\":\"" + cardId + "\"" +
                        "    },\n" +
                        "    \"request_id\":\"" + UUID.randomUUID() + "\"," +
                        "    \"resource\":[" +
                        "        \"http://retail-huabei2.oss-cn-beijing.aliyuncs.com/BUSINESS_RISK_DAILY/witness/100000000000235625/d020e3fe-8050-47bb-9c16-49a2aebdc8f0?OSSAccessKeyId=LTAIlYpjA39n18Yr&Expires=37614851253&Signature=sha3ensWL7oYP5lVgOy28TD6bXQ%3D\"," +
                        "        \"http://retail-huabei2.oss-cn-beijing.aliyuncs.com/BUSINESS_RISK_DAILY/witness/100000000000235625/d020e3fe-8050-47bb-9c16-49a2aebdc8f0?OSSAccessKeyId=LTAIlYpjA39n18Yr&Expires=37614851253&Signature=sha3ensWL7oYP5lVgOy28TD6bXQ%3D\"" +
                        "    ],\n" +
                        "    \"system\":{" +
                        "        \"app_id\":\"49998b971ea0\"," +
                        "        \"device_id\":\"6934268400763904\"," +
//                        "        \"device_id\":\"6798257327342592\"," + //shop 2606de
                        "        \"scope\":[" +
                        "            \"4116\"" +
                        "        ]," +
//                        "        \"scope\":[" +
//                        "            \"2606\"" +
//                        "        ]," +
                        "        \"service\":\"/business/risk/WITNESS_UPLOAD/v1.0\"," +
                        "        \"source\":\"DEVICE\"" +
                        "    }" +
                        "}";

        Thread.sleep(3000);

        return httpPostWithCheckCode(router, json);
    }

    public String witnessUploadFail(String cardId, String personName) throws Exception {
        String router = "/risk-inner/witness/upload";
        String json =
                "{\n" +
                        "    \"data\":{\n" +
                        "        \"person_name\":\"" + personName + "\"," +
                        "        \"capture_pic\":\"@1\"," +
                        "        \"is_pass\":true," +
                        "        \"card_pic\":\"@0\"," +
                        "        \"card_id\":\"" + cardId + "\"" +
                        "    },\n" +
                        "    \"request_id\":\"" + UUID.randomUUID() + "\"," +
                        "    \"resource\":[" +
                        "        \"https://retail-huabei2.oss-cn-beijing.aliyuncs.com/BUSINESS_RISK_DAILY/witness/100000000000962662/1c32c393-21c2-48b2-afeb-11c197436194?Expires=1580882241&OSSAccessKeyId=TMP.hj3MfDhaCX3aSbKjRM9Rx1WScRdTdWZN3cLj2fsLxnAkxXHTnRz9BXDebaX6qhG2x15xP2zULU6q3mRT7JgZ3aCbSs4RtyXfHAnXCZUAY6oRAaDx9iaE5eCeGmv2P5.tmp&Signature=LTQnJJ5jKkh45rImVZDCZzpotLI%3D\"," +
                        "        \"https://retail-huabei2.oss-cn-beijing.aliyuncs.com/BUSINESS_RISK_DAILY/witness/100000000000962662/1c32c393-21c2-48b2-afeb-11c197436194?Expires=1580882241&OSSAccessKeyId=TMP.hj3MfDhaCX3aSbKjRM9Rx1WScRdTdWZN3cLj2fsLxnAkxXHTnRz9BXDebaX6qhG2x15xP2zULU6q3mRT7JgZ3aCbSs4RtyXfHAnXCZUAY6oRAaDx9iaE5eCeGmv2P5.tmp&Signature=LTQnJJ5jKkh45rImVZDCZzpotLI%3D\"" +
                        "    ],\n" +
                        "    \"system\":{" +
                        "        \"app_id\":\"49998b971ea0\"," +
                        "        \"device_id\":\"6934268400763904\"," +
                        "        \"scope\":[" +
                        "            \"4116\"" +
                        "        ]," +
                        "        \"service\":\"/business/risk/WITNESS_UPLOAD/v1.0\"," +
                        "        \"source\":\"DEVICE\"" +
                        "    }" +
                        "}";

        Thread.sleep(3000);

        return httpPostWithCheckCode(router, json);
    }

    //    *****************************************************14?????????????????????******************************************************

    public String addActivity(String name, String type, String contrastS, String contrastE, String startDate, String endDate,
                              String influenceS, String influenceE) throws Exception {
        String url = "/risk/manage/activity/add";
        String json =
                "{\n" +
                        "    \"activity_name\":\"" + name + "\"," +
                        "    \"activity_type\":\"" + type + "\"," +
                        "    \"contrast_start\":\"" + contrastS + "\"," +
                        "    \"contrast_end\":\"" + contrastE + "\"," +
                        "    \"start_date\":\"" + startDate + "\"," +
                        "    \"end_date\":\"" + endDate + "\"," +
                        "    \"influence_start\":\"" + influenceS + "\"," +
                        "    \"influence_end\":\"" + influenceE + "\"," +
                        "    \"shop_id\":\"" + getShopId() + "\"" +
                        "}";

        String res = httpPost(url, json);

        return res;
    }

    public String deleteActivity(String id) throws Exception {
        String url = "/risk/manage/activity/delete";
        String json =
                "{\n" +
                        "    \"id\":\"" + id + "\"," +
                        "    \"shop_id\":\"" + getShopId() + "\"" +
                        "}";

        String res = httpPostWithCheckCode(url, json);

        return res;
    }

    public JSONObject listActivity(String name, int page, int pageSize) throws Exception {
        String url = "/risk/manage/activity/list";
        String json =
                "{\n" +
                        "    \"activity_name\":\"" + name + "\"," +
                        "    \"page\":\"" + page + "\"," +
                        "    \"size\":\"" + pageSize + "\"," +
                        "    \"shop_id\":\"" + getShopId() + "\"" +
                        "}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }


//    **************************************************15????????????????????????************************************************

    public JSONObject catchList(String customerType, String deviceId, String startTime, String endTime, int page,
                                int size) throws Exception {
        String url = "/risk/evidence/person-catch/page";
        String json =
                "{\n";
        if (!"".equals(customerType)) {
            json += "\"person_type\":\"" + customerType + "\",";
        }

        if (!"".equals(deviceId)) {
            json += "\"device_id\":\"" + deviceId + "\",";
        }

        if (!"".equals(startTime)) {
            json += "\"start_time\":\"" + startTime + "\"," +
                    "\"end_time\":\"" + endTime + "\",";
        }

        json += "\"page\":\"" + page + "\"," +
                "\"size\":\"" + size + "\"," +
                "\"shop_id\":" + getShopId() +
                "}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    public JSONObject fetchDeviceList() throws Exception {
        String url = "/risk/evidence/face-catch/devices";
        String json =
                "{\n" +
                        "    \"shop_id\":\"" + getShopId() + "\"" +
                        "}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }


    public JSONObject deviceList(int page, int pageSize) throws Exception {
        String url = "/risk/device/page";
        String json =
                "{\n" +
                        "    \"page\":\"" + page + "\"," +
                        "    \"page_size\":\"" + pageSize + "\"," +
                        "    \"shop_id\":\"" + getShopId() + "\"" +
                        "}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    public JSONObject faceTraces(String showUrl) throws Exception {
        String url = "/risk/evidence/face/traces";
        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + ",\n" +
                        "    \"show_url\":\"" + showUrl + "\"" +
                        "}";


        String res = httpPost(url, json);

        return JSON.parseObject(res);
    }

    public void visitor2Staff(String customerId) throws Exception {
        String url = "/risk/evidence/person-catch/toStaff";
        String json =
                "{\n" +
                        "    \"shop_id\":4116," +
                        "    \"customer_ids\":[" +
                        "        \"" + customerId + "\"" +
                        "    ]" +
                        "}";

        String res = httpPostWithCheckCode(url, json);
    }

    public JSONObject personTypeList() throws Exception {
        String url = "/risk/evidence/person-type/list";
        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + "\"" +
                        "}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

//    ***********************************************************16???????????????*************************************************

    /**
     * 16.1 ??????????????????
     */
    public String addRiskRuleNoCheckCode(String name, String aheadReportTime, String reportProtect) throws
            Exception {

        String url = "/risk/rule/add";
        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + "," +
                        "    \"name\":\"" + name + "\"," +
                        "    \"ahead_report_time\":\"" + aheadReportTime + "\"," +
                        "    \"report_protect\":\"" + reportProtect + "\"" +
                        "}";

        return httpPost(url, json);
    }

    /**
     * 16.1 ??????????????????
     */
    public void addRiskRule(String name, String aheadReportTime, String reportProtect) throws Exception {

        String url = "/risk/rule/add";
        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + "," +
                        "    \"name\":\"" + name + "\"," +
                        "    \"ahead_report_time\":\"" + aheadReportTime + "\"," +
                        "    \"report_protect\":\"" + reportProtect + "\"" +
                        "}";

        httpPostWithCheckCode(url, json);
    }

    /**
     * 16.1 ??????????????????
     */
    public JSONObject riskRuleList() throws Exception {

        String url = "/risk/rule/list";
        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + "," +
                        "    \"page\":\"" + 1 + "\"," +
                        "    \"size\":\"" + 100 + "\"" +
                        "}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");


    }

    /**
     * 16.1 ??????????????????
     */
    public void deleteRiskRule(String id) throws Exception {

        String url = "/risk/rule/delete";
        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + "," +
                        "    \"id\":\"" + id + "\"" +
                        "}";

        httpPostWithCheckCode(url, json);
    }

    public String deleteRiskRuleNoCheckCode(String id) throws Exception {

        String url = "/risk/rule/delete";
        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + "," +
                        "    \"id\":\"" + id + "\"" +
                        "}";

        String s = httpPost(url, json);
        return s;
    }

    //    ***********************************************************17???OCR**********************************************************

    public String refreshQrcodeNoCheckCode() throws Exception {

        String url = "/risk/shop/ocr/qrcode/refresh";
        String json =
                "{}";

        String res = httpPost(url, json);

        return res;
    }

    public String confirmQrcodeNoCheckCode(String code) throws Exception {

        String url = "/external/ocr/code/confirm";
        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + "," +
                        "    \"code\":\"" + code + "\"" +
                        "}";


        String res = httpPost(url, json);

        return res;

    }

    /**
     * 17.3 OCR???????????????-H5
     */
    public static String OCR_PIC_UPLOAD_JSON = "{\"shop_id\":${shopId},\"token\":\"${token}\"," +
            "\"identity_card\":\"${idCard}\",\"face\":\"${face}\"}";

    public String ocrPicUpload(String token, String idCard, String face) throws Exception {

        String url = "/external/ocr/pic/upload";
        String json = StrSubstitutor.replace(OCR_PIC_UPLOAD_JSON, ImmutableMap.builder()
                .put("shopId", getShopId())
                .put("token", token)
                .put("idCard", idCard)
                .put("face", face)
                .build()
        );

        String res = httpPostNoPrintPara(url, json);

        return res;
    }

    //    *************************************************18???????????????***************************************************************

    /**
     * ??????????????????(2020.02.12)
     */
    public JSONObject historyRuleDetail() throws Exception {
        String url = "/risk/history/rule/detail";
        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + "\n" +
                        "}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }


    /**
     * ?????????????????? (2020-03-02) ????????????
     */
    public JSONObject channelReptstatistics() throws Exception {
        String url = "/risk/channel/report/statistics";
        String json = "{\n" +
                "    \"shop_id\":" + getShopId() + "\n}";
        String res = httpPostWithCheckCode(url, json);
        return JSON.parseObject(res).getJSONObject("data");
    }


//    #########################################################??????????????????########################################################


//    #########################################################??????????????????########################################################


    public void checkDevice() throws Exception {

        JSONArray list = deviceList(1, 100).getJSONArray("list");

        for (int i = 0; i < list.size(); i++) {
            JSONObject single = list.getJSONObject(i);
            String statusName = single.getString("status_name");
            if (!"?????????".equals(statusName)) {
                String typeName = single.getString("type_name");
                Preconditions.checkArgument(single.getBooleanValue("error") == true, "????????????=" + typeName + "?????????=" + statusName + "????????????????????????");

            }
        }
    }

    public void updateReportTimeChannel(String phone, String customerName, int channelId, int staffId, long repTime) throws
            Exception {
        ReportTime reportTime = new ReportTime();
        reportTime.setShopId(4116);
        reportTime.setChannelId(channelId);
        reportTime.setChannelStaffId(staffId);
        reportTime.setPhone(phone);
        reportTime.setCustomerName(customerName);
        long timestamp = repTime;
        reportTime.setReportTime(String.valueOf(timestamp));
        reportTime.setGmtCreate(dateTimeUtil.changeDateToSqlTimestamp(timestamp));

        qaDbUtil.updateReportTime(reportTime);
    }

    public void updateReportTime_PCF(String phone, String customerName, long repTime) throws Exception {
        ReportTime reportTime = new ReportTime();
        reportTime.setShopId(4116);
        reportTime.setChannelId(-1);
        reportTime.setChannelStaffId(0);
        reportTime.setPhone(phone);
        reportTime.setCustomerName(customerName);
        long timestamp = repTime;
        reportTime.setReportTime(String.valueOf(timestamp));
        reportTime.setGmtCreate(dateTimeUtil.changeDateToSqlTimestamp(timestamp));

        qaDbUtil.updateReportTime(reportTime);
    }

    public void updateReportTime_S(String phone, String customerName, long repTime) throws Exception {
        ReportTime reportTime = new ReportTime();
        reportTime.setShopId(4116);
        reportTime.setChannelId(0);
        reportTime.setChannelStaffId(0);
        reportTime.setPhone(phone);
        reportTime.setCustomerName(customerName);
        long timestamp = repTime;
        reportTime.setReportTime(String.valueOf(timestamp));
        reportTime.setGmtCreate(dateTimeUtil.changeDateToSqlTimestamp(timestamp));

        qaDbUtil.updateReportTime(reportTime);
    }


    public void checkNotCode(String response, int expectNot, String message) throws Exception {
        JSONObject resJo = JSON.parseObject(response);

        if (resJo.containsKey("code")) {
            int code = resJo.getInteger("code");

            if (expectNot == code) {
                Assert.assertNotEquals(code, expectNot, message + resJo.getString("message"));
            }
        } else {
            int status = resJo.getInteger("status");
            String path = resJo.getString("path");
            throw new Exception("?????????????????????status???" + status + ",path:" + path);
        }
    }

    public Object getShopId() {
        return "4116";
    }

    public String httpPostWithCheckCode(String path, String json) throws Exception {
        initHttpConfig();
        String queryUrl = getIpPort() + path;
        config.url(queryUrl).json(json);
        logger.info("{} json param: {}", path, json);
        long start = System.currentTimeMillis();

        response = HttpClientUtil.post(config);

        logger.info("response: {}", response);

        checkCode(response, StatusCode.SUCCESS, path);

        logger.info("{} time used {} ms", path, System.currentTimeMillis() - start);
        return response;
    }

    public String httpPost(String path, String json) throws Exception {
        initHttpConfig();
        String queryUrl = getIpPort() + path;
        config.url(queryUrl).json(json);
        logger.info("{} json param: {}", path, json);
        long start = System.currentTimeMillis();

        response = HttpClientUtil.post(config);

        logger.info("response: {}", response);

        logger.info("{} time used {} ms", path, System.currentTimeMillis() - start);
        return response;
    }

    public String httpPostNoPrintPara(String path, String json) throws Exception {
        initHttpConfig();
        String queryUrl = getIpPort() + path;
        config.url(queryUrl).json(json);
        long start = System.currentTimeMillis();


        response = HttpClientUtil.post(config);

        logger.info("response: {}", response);

        logger.info("{} time used {} ms", path, System.currentTimeMillis() - start);
        return response;
    }

    public void checkCode(String response, int expect, String message) throws Exception {
        JSONObject resJo = JSON.parseObject(response);

        if (resJo.containsKey("code")) {
            int code = resJo.getInteger("code");

            if (expect != code) {
                if (code != 1000) {
                    message += resJo.getString("message");
                }
                Assert.assertEquals(code, expect, message);
            }
        } else {
            int status = resJo.getInteger("status");
            String path = resJo.getString("path");
            throw new Exception("?????????????????????status???" + status + ",path:" + path);
        }
    }

    public void checkMessage(String function, String response, String message) throws Exception {

        String messageRes = JSON.parseObject(response).getString("message");
        if (!message.equals(messageRes)) {
            throw new Exception(function + "???????????????????????????????????????=" + message + "?????????=" + messageRes);
        }
    }


    public void checkAdviserList(String name, String phone, boolean hasPic) throws Exception {

        JSONObject staff = adviserList(phone, 1, 1).getJSONArray("list").getJSONObject(0);

        if (staff == null || staff.size() == 0) {
            throw new Exception("?????????????????????????????????=" + name + "????????????=" + phone);
        } else {
            checkUtil.checkKeyValue("????????????????????????", staff, "staff_name", name, true);
            checkUtil.checkKeyValue("????????????????????????", staff, "phone", phone, true);

            if (hasPic) {
                checkUtil.checkNotNull("????????????????????????", staff, "face_url");
            } else {
                checkUtil.checkNull("????????????????????????", staff, "face_url");
            }
        }
    }

    public void checkChannelStaffList(String channelId, String name, String phone, boolean hasPic) throws Exception {

        JSONObject staff = channelStaffList(channelId, 1, 1).getJSONArray("list").getJSONObject(0);

        if (staff == null || staff.size() == 0) {
            throw new Exception("??????????????????????????????????????????????????????=" + name + "????????????=" + phone);
        } else {
            checkUtil.checkKeyValue("???????????????????????????", staff, "staff_name", name, true);
            checkUtil.checkKeyValue("???????????????????????????", staff, "phone", phone, true);
            checkUtil.checkNotNull("???????????????????????????", staff, "total_report");

            if (hasPic) {
                checkUtil.checkNotNull("???????????????????????????", staff, "face_url");
            } else {
                checkUtil.checkNull("???????????????????????????", staff, "face_url");
            }
        }
    }

//    ----------------------------------------??????????????????--------------------------------------------------------------------


    public String genPhoneNum() {
        Random random = new Random();
        String num = "177" + (random.nextInt(89999999) + 10000000);

        return num;
    }

    public String genCardId() {
        Random random = new Random();
        long num = 100000000000000000L + random.nextInt(99999999);

        return String.valueOf(num);
    }

    public void checkDetail(String orderId, String customerName, String phone, String adviserName, String
            channelName,
                            String channelStaffName, String orderStatusTips, String faceUrl, String firstAppearTime,
                            String reportTime, JSONObject orderDetail) throws Exception {

        String function = "????????????-orderId=" + orderId + "???";

        if (!"-".equals(customerName)) {

            checkUtil.checkKeyValue(function, orderDetail, "customer_name", customerName, true);
        } else {
            checkUtil.checkNull(function, orderDetail, "customer_name");
        }

        if (!"-".equals(phone)) {

            checkUtil.checkKeyValue(function, orderDetail, "phone", phone, true);
        } else {
            checkUtil.checkNull(function, orderDetail, "phone");
        }

        if (!"-".equals(adviserName)) {

            checkUtil.checkKeyValue(function, orderDetail, "adviser_name", adviserName, true);
        } else {
            checkUtil.checkNull(function, orderDetail, "adviser_name");
        }

        if (!"-".equals(channelName)) {

            checkUtil.checkKeyValue(function, orderDetail, "channel_name", channelName, true);
        } else {
            checkUtil.checkNull(function, orderDetail, "channel_name");
        }

        if (!"-".equals(channelStaffName)) {

            checkUtil.checkKeyValue(function, orderDetail, "channel_staff_name", channelStaffName, true);
        } else {
            checkUtil.checkNull(function, orderDetail, "channel_staff_name");
        }

        if (!"-".equals(orderStatusTips)) {

            checkUtil.checkKeyValue(function, orderDetail, "order_status_tips", orderStatusTips, true);
        } else {
            checkUtil.checkNull(function, orderDetail, "order_status_tips");
        }

        if (!"-".equals(faceUrl)) {
            checkUtil.checkKeyValue(function, orderDetail, "face_url", faceUrl, true);
        } else {
            checkUtil.checkNull(function, orderDetail, "face_url");
        }

        if (!"-".equals(firstAppearTime)) {
            checkUtil.checkKeyValue(function, orderDetail, "first_appear_time", firstAppearTime, true);
        } else {
            checkUtil.checkNull(function, orderDetail, "first_appear_time");
        }

        if (!"-".equals(reportTime)) {
            checkUtil.checkKeyValue(function, orderDetail, "report_time", reportTime, true);
        } else {
            checkUtil.checkNull(function, orderDetail, "report_time");
        }

        checkUtil.checkKeyValue(function, orderDetail, "deal_time", "", false);
    }


    public void checkNormalOrderLink(String orderId, JSONObject data) throws Exception {

        JSONArray linkLists = data.getJSONArray("list");

        for (int i = 0; i < linkLists.size(); i++) {
            JSONObject link = linkLists.getJSONObject(i);
            Integer linkStatus = link.getInteger("link_status");
            String linkName = link.getString("link_name");
            if (linkStatus != 1) {
                throw new Exception("order_id=" + orderId + "?????????=" + linkName + "?????????????????????????????????????????????!");
            }
        }
    }

    public void checkFirstVisitAndTrace(String orderId, JSONObject data, boolean expectExist) throws Exception {
        JSONArray linkLists = data.getJSONArray("list");

        boolean isExist = false;


        String functionPre = "orderId=" + orderId + "???";

        for (int i = 0; i < linkLists.size(); i++) {
            String function;
            JSONObject link = linkLists.getJSONObject(i);
            String linkKey = link.getString("link_key");
            String id = link.getString("id");

            function = functionPre + "??????id=" + id + "???";


            if ("FIRST_APPEAR".equals(linkKey) || "TRACE".equals(linkKey)) {
                isExist = true;

                checkUtil.checkNotNull(function, link, "link_point");
                checkUtil.checkNotNull(function, link.getJSONObject("link_note"), "face_url");
                checkUtil.checkNotNull(function, link, "link_time");
                checkUtil.checkNotNull(function, link, "link_time");
                checkUtil.checkNotNull(function, link, "link_name");

                Integer linkStatus = link.getInteger("link_status");
                String linkName = link.getString("link_name");
                if (linkStatus != 1) {
                    throw new Exception("order_id=" + orderId + "?????????=" + linkName + "?????????id=" + id + "?????????????????????????????????????????????!");
                }
            }
        }

        if (isExist != expectExist) {
            throw new Exception("order_id=" + orderId + "?????????????????????????????????????????????" + expectExist + ",?????????" + isExist);
        }

    }

    public void checkReport(String orderId, String orderType, int riskNum, String customerType, JSONObject orderDetail) throws Exception {

        String txtPath = "src/main/java/com/haisheng/framework/testng/bigScreen/checkOrderFile/riskReport.txt";
        txtPath = txtPath.replace("/", File.separator);
        String pdfPath = "src/main/java/com/haisheng/framework/testng/bigScreen/checkOrderFile/riskReport.pdf";
        pdfPath = pdfPath.replace("/", File.separator);

        String pdfUrl = reportCreate(orderId).getString("file_url");

        File pdfFile = new File(pdfPath);
        File txtFile = new File(txtPath);
        pdfFile.delete();
        txtFile.delete();

//        ??????pdf
        String currentTime1 = dt.timestampToDate("yyyy???MM???dd??? HH:mm", System.currentTimeMillis());
        downLoadPdf(pdfUrl);
        String currentTime = dt.timestampToDate("yyyy???MM???dd??? HH:mm", System.currentTimeMillis());

//        pdf???txt
        readPdf(pdfPath);

//        ??????????????????
        String noSpaceStr = removebreakStr(txtPath);

//        ????????????????????????
        Link[] links = getLinkMessage(orderId);
//
        String message = "";

//            1.1????????????????????????
        DateTimeUtil dt = new DateTimeUtil();

        currentTime = stringUtil.trimStr(currentTime);
        currentTime1 = stringUtil.trimStr(currentTime1);

//        if (!(noSpaceStr.contains(currentTime) || noSpaceStr.contains(currentTime1))) {
//            message += "?????????????????????????????????????????????,????????????????????????????????????\n\n";
//        }

//            1.2???????????????
        if (!noSpaceStr.contains("??????????????????")) {
            message += "???????????????????????????????????????????????????????????????\n\n";
        }

//            2?????????????????????
        String s = "";

        if ("??????".equals(orderType)) {
            s = "????????????" + riskNum + "???????????????" + customerType;
        } else {
            s = "??????" + riskNum + "??????????????????" + customerType;
        }
        if (!noSpaceStr.contains(s)) {
            message += "??????????????????????????????????????????\n\n";
        }

//            ????????????
        String customerName = orderDetail.getString("customer_name");
        String phone = orderDetail.getString("phone");
        String adviserName = orderDetail.getString("adviser_name");
        if (adviserName == null) {
            adviserName = "-";
        }
        String channelName = orderDetail.getString("channel_name");
        if (channelName == null) {
            channelName = "-";
        }
        String channelStaffName = orderDetail.getString("channel_staff_name");
        if (channelStaffName == null) {
            channelStaffName = "-";
        }
        String firstAppearTime = "-";
        if (orderDetail.getLong("first_appear_time") != null) {
            firstAppearTime = dt.timestampToDate("yyyy/MM/dd HH:mm:ss", orderDetail.getLong("first_appear_time"));
        }
        String reportTime = "-";
        if (orderDetail.getLong("report_time") != null) {

            reportTime = dt.timestampToDate("yyyy/MM/dd HH:mm:ss", orderDetail.getLong("report_time"));
        }

        String dealTime = "-";
        if (orderDetail.getLong("deal_time") != null) {
            dealTime = dt.timestampToDate("yyyy/MM/dd HH:mm:ss", orderDetail.getLong("deal_time"));
        }

        s = "??????" + customerName + "????????????" + phone + "??????????????????" + adviserName + "????????????" + channelName + "???????????????" + channelStaffName + "????????????" + reportTime + "????????????" + firstAppearTime + "????????????" + dealTime + "??????????????????" + orderType;
        String tem = stringUtil.trimStr(s);
        if (!noSpaceStr.contains(tem)) {
            message += "?????????????????????????????????????????????";
        }

//            3???????????????
        for (int i = 0; i < links.length; i++) {
            Link link = links[i];
            s = stringUtil.trimStr(link.linkTime + link.linkName + link.content + link.linkPoint);
            if (!noSpaceStr.contains(s)) {

                message += "???????????????" + links[i].linkName +
                        "???????????????????????????????????????" + links[i].linkTime + "?????????????????????" + links[i].linkPoint + "???\n\n";
            }
        }

//            4?????????????????????
//        if (noSpaceStr.contains("??????")) {
//            message += "????????????\n\n";
//        }

        if (!"".equals(message)) {
            throw new Exception("orderId=" + orderId + "??????????????????????????????\n\n" + message);
        }
    }

    public Link[] getLinkMessage(String orderId) throws Exception {
        JSONArray list = orderLinkList(orderId).getJSONArray("list");
        Link[] links = new Link[list.size()];
        for (int i = 0; i < list.size(); i++) {
            JSONObject single = list.getJSONObject(i);
            Link link = new Link();
            link.linkName = single.getString("link_name");
            JSONObject linkNote = single.getJSONObject("link_note");
            if (!linkNote.getBooleanValue("is_pic")) {
                link.content = linkNote.getString("content");
            }
            if (link.content == null || "".equals(link.content.trim())) {
                link.content = "";
            }
            String linkPoint = single.getString("link_point");
            link.linkPoint = linkPoint.replace("\n", " ");
            link.linkTime = dt.timestampToDate("yyyy-MM-dd HH:mm:ss", single.getLong("link_time"));
            links[i] = link;
        }

        return links;
    }

    public String removebreakStr(String fileName) {

        StringBuffer noSpaceStr = new StringBuffer();

        try {
            File srcFile = new File(fileName);
            boolean b = srcFile.exists();
            if (b) {    //??????????????????????????????????????????????????????

                if (!srcFile.getName().endsWith("txt")) {    //???????????????TXT??????
                    System.out.println(srcFile.getName() + "??????TXT?????????");
                }
//                Runtime.getRuntime().exec("notepad " + srcFile.getAbsolutePath());//?????????????????????,?????????????????????????????????
                String str = null;
                String REGEX = "\\s+";    //?????????????????????????????????,\s??????????????????????????????????????????????????????????????????

                InputStreamReader stream = new InputStreamReader(new FileInputStream(srcFile), "UTF-8");    //???????????????????????????????????????UTF-8
                BufferedReader reader = new BufferedReader(stream);    ////??????????????????????????????????????????????????????????????????????????????????????????

//                File newFile = new File(srcFile.getParent(),
//                 "new" + srcFile.getName());    //???????????????????????????????????????

//                OutputStreamWriter outstream = new OutputStreamWriter(new FileOutputStream(newFile), "UTF-8");    //???????????????
//                BufferedWriter writer = new BufferedWriter(outstream);
                Pattern patt = Pattern.compile(REGEX);    //??????Pattern??????????????????????????????

                while ((str = reader.readLine()) != null) {
                    Matcher mat = patt.matcher(str);    //?????????????????????????????????
                    str = mat.replaceAll("");

                    if (str == "") {    //??????????????????????????????????????????????????????????????????
                        continue;
                    } else {
                        noSpaceStr.append(str);
//                        writer.write(str);    //???????????????????????????????????????str+"\r\n" ???????????????????????????
                    }
                }
//                writer.close();
                reader.close();

                //????????????????????????
//                Runtime.getRuntime().exec("notepad " + newFile.getAbsolutePath());
//                System.out.println("?????????????????????");
            } else {
//                System.out.println("????????????????????????????????????????????????????????????");
                System.exit(0);
            }
        } catch (IOException e) {
            System.out.println(e);
        }

        return noSpaceStr.toString();
    }

    public static String readPdf(String fileStr) throws Exception {
        // ????????????
        boolean sort = false;
        File pdfFile = new File(fileStr);
        // ????????????????????????
        String textFile = null;
        // ????????????
        String encoding = "UTF-8";
        // ??????????????????
        int startPage = 1;
        // ??????????????????
        int endPage = Integer.MAX_VALUE;
        // ????????????????????????????????????
        Writer output = null;
        // ??????????????????PDF Document
        PDDocument document = null;
        try {

            //???????????????File
            document = PDDocument.load(pdfFile);

            // ?????????PDF??????????????????????????????txt??????
            textFile = fileStr.replace(".pdf", ".txt");

            // ?????????????????????????????????textFile
            output = new OutputStreamWriter(new FileOutputStream(textFile), encoding);
            // PDFTextStripper???????????????
            PDFTextStripper stripper = null;
            stripper = new PDFTextStripper();
            // ??????????????????
            stripper.setSortByPosition(sort);
            // ???????????????
            stripper.setStartPage(startPage);
            // ???????????????
            stripper.setEndPage(endPage);
            // ??????PDFTextStripper???writeText?????????????????????
            stripper.writeText(document, output);

            return textFile;
        } finally {
            if (output != null) {
                output.close();
            }
            if (document != null) {
                // ??????PDF Document
                document.close();
            }
        }
    }

    public void downLoadPdf(String pdfUrl) throws IOException {

        String downloadImagePath = "src/main/java/com/haisheng/framework/testng/bigScreen/checkOrderFile/riskReport.pdf";

        URL url = new URL(pdfUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        // ???????????????
        InputStream inputStream = conn.getInputStream();
        // ??????????????????
        byte[] getData = readInputStream(inputStream);
        // ??????????????????
        File file = new File(downloadImagePath);
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(getData);
        if (fos != null) {
            fos.close();
        }
        if (inputStream != null) {
            inputStream.close();
        }
    }

    public static byte[] readInputStream(InputStream inputStream)
            throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while ((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }


    public String getNamePro() {

        String tmp = UUID.randomUUID() + "";

        return tmp.substring(tmp.length() - 7);
    }

    public void detailListLinkConsist(String orderId, String phone) throws Exception {

        JSONArray list = orderList(3, phone, 100).getJSONArray("list");

        for (int i = 0; i < list.size(); i++) {
            JSONObject single = list.getJSONObject(i);
            String orderIdRes = single.getString("order_id");

            if (orderId.equals(orderIdRes)) {

                JSONObject orderDetail = orderDetail(orderId);
                String function = "????????????>>>";

                String customerName = single.getString("customer_name");
                checkUtil.checkKeyValue(function, orderDetail, "customer_name", customerName, true);

                String adviserName = single.getString("adviser_name");
                if (adviserName == null || "".equals(adviserName)) {
                    String adviserNameDetail = orderDetail.getString("adviser_name");
                    if (adviserNameDetail != null && !"".equals(adviserNameDetail)) {
                        throw new Exception("orderId=" + orderId + ",adviser_name?????????????????????????????????????????????=" + adviserNameDetail);
                    }
                } else {
                    checkUtil.checkKeyValue(function, orderDetail, "adviser_name", adviserName, true);
                }

                String channelName = single.getString("channel_name");
                if (channelName == null || "".equals(channelName)) {
                    String channelNameDetail = orderDetail.getString("channel_name");
                    if (channelNameDetail != null && !"".equals(channelNameDetail)) {
                        throw new Exception("orderId=" + orderId + ",channel_name?????????????????????????????????????????????=" + channelNameDetail);
                    }
                } else {
                    checkUtil.checkKeyValue(function, orderDetail, "channel_name", channelName, true);
                }

                String channelStaffName = single.getString("channel_staff_name");
                if (channelStaffName == null || "".equals(channelStaffName)) {
                    String channelStaffNameDetail = orderDetail.getString("channel_staff_name");
                    if (channelStaffNameDetail != null && !"".equals(channelStaffNameDetail)) {
                        throw new Exception("orderId=" + orderId + ",channel_staff_name?????????????????????????????????????????????=" + channelStaffNameDetail);
                    }
                } else {
                    checkUtil.checkKeyValue(function, orderDetail, "channel_staff_name", channelStaffName, true);
                }

                String firstAppearTime = single.getString("first_appear_time");
                if (firstAppearTime == null || "".equals(firstAppearTime)) {
                    String firstAppearTimeDetail = orderDetail.getString("first_appear_time");
                    if (firstAppearTimeDetail != null && !"".equals(firstAppearTimeDetail)) {
                        throw new Exception("orderId=" + orderId + ",first_appear_time?????????????????????????????????????????????=" + firstAppearTimeDetail);
                    }
                } else {
                    checkUtil.checkKeyValue(function, orderDetail, "first_appear_time", firstAppearTime, true);
                }

                String dealTime = single.getString("deal_time");
                if (dealTime == null || "".equals(dealTime)) {
                    String dealTimeDetail = orderDetail.getString("deal_time");
                    if (dealTimeDetail != null && !"".equals(dealTimeDetail)) {
                        throw new Exception("orderId=" + orderId + ",deal_time?????????????????????????????????????????????=" + dealTimeDetail);
                    }
                } else {
                    checkUtil.checkKeyValue(function, orderDetail, "deal_time", dealTime, true);
                }

                String reportTime = single.getString("report_time");
                if (reportTime == null || "".equals(reportTime)) {
                    String reportTimeDetail = orderDetail.getString("report_time");
                    if (reportTimeDetail != null && !"".equals(reportTimeDetail)) {
                        throw new Exception("orderId=" + orderId + ",report_time?????????????????????????????????????????????=" + reportTimeDetail);
                    }
                } else {
                    checkUtil.checkKeyValue(function, orderDetail, "report_time", reportTime, true);
                }

                String isAudited = single.getString("is_audited");
                if (isAudited == null || "".equals(isAudited)) {
                    String isAuditedDetail = orderDetail.getString("report_time");
                    if (isAuditedDetail != null && !"".equals(isAuditedDetail)) {
                        throw new Exception("orderId=" + orderId + ",is_audited?????????????????????????????????????????????=" + isAuditedDetail);
                    }
                } else {
                    checkUtil.checkKeyValue(function, orderDetail, "is_audited", isAudited, true);
                }

                break;
            }
        }
    }

    public String importFile(String imagePath) {
        String url = "http://dev.store.winsenseos.cn/risk/customer/file/import";
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);

        httpPost.addHeader("authorization", authorization);
        httpPost.addHeader("shop_id", getShopId() + "");
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();

        File file = new File(imagePath);

        String res = "";
        try {
            builder.addBinaryBody(
                    "file",
                    new FileInputStream(file),
                    ContentType.IMAGE_JPEG,
                    file.getName()
            );

            HttpEntity multipart = builder.build();
            httpPost.setEntity(multipart);
            CloseableHttpResponse response = httpClient.execute(httpPost);
            HttpEntity responseEntity = response.getEntity();
            res = EntityUtils.toString(responseEntity, "UTF-8");

            logger.info("response: " + res);


        } catch (Exception e) {
            failReason = e.getMessage();
            e.printStackTrace();
        }

        return res;
    }


    public void updateProtectTime(String phone, String customerName, int channelId, int staffId, long pretectTime) {
        ProtectTime protectTime = new ProtectTime();
        protectTime.setShopId(4116);
        protectTime.setChannelId(channelId);
        protectTime.setChannelStaffId(staffId);
        protectTime.setPhone(phone);
        protectTime.setCustomerName(customerName);
        long timestamp = pretectTime;
        protectTime.setProtectTime(String.valueOf(timestamp));

        qaDbUtil.updateProtectTime(protectTime);
    }

    public void checkOrderRiskLinkMess(String orderId, JSONObject data, String linkKey, String content, String
            linkPoint) throws Exception {

        JSONArray linkLists = data.getJSONArray("list");

        boolean isExistLinkKey = false;
        boolean isExistLinkKeyContent = false;

        for (int i = 0; i < linkLists.size(); i++) {
            JSONObject link = linkLists.getJSONObject(i);

            String linkKeyRes = link.getString("link_key");
            if (linkKey.equals(linkKeyRes)) {
                isExistLinkKey = true;

                String contentRes = link.getJSONObject("link_note").getString("content");

                if ("".equals(content) || content.equals(contentRes)) {
                    isExistLinkKeyContent = true;

                    int linkStatus = link.getInteger("link_status");
                    if (linkStatus != 0) {
                        throw new Exception("order_id=" + orderId + "?????????=" + linkKey + "????????????????????????????????????????????????");
                    }

                    String linkPointRes = link.getString("link_point");

                    if (!linkPoint.equals(linkPointRes)) {
                        throw new Exception("order_id=" + orderId + "?????????=" + linkKey + "???????????????????????????" + linkPoint + "????????????????????????" + linkPointRes + "???");
                    }

                    break;
                }
            }
        }

        if (!isExistLinkKey) {
            throw new Exception("order_id=" + orderId + "??????????????????=" + linkKey);
        }

        if (!isExistLinkKeyContent) {
            throw new Exception("order_id=" + orderId + "?????????=" + linkKey + "??????????????????=" + content + "????????????");
        }
    }

    public void checkOrderRiskLinkNum(String orderId, JSONObject data, int num) throws Exception {

        JSONArray list = data.getJSONArray("list");

        int riskNum = 0;

        for (int i = 0; i < list.size(); i++) {
            JSONObject single = list.getJSONObject(i);
            int linkStatus = single.getInteger("link_status");
            if (linkStatus == 0) {
                riskNum++;
            }
        }

        if (riskNum != num) {
            throw new Exception("order_id=" + orderId + "????????????????????????=" + num + "??????????????????????????????=" + riskNum);
        }
    }

    public void checkReportInfo(String orderId, String phone, String[] descs) throws Exception {

        JSONArray list = searchReportInfoByPhone(orderId, phone).getJSONArray("list");

        String[] descsRes = new String[list.size()];

        for (int i = 0; i < list.size(); i++) {
            JSONObject single = list.getJSONObject(i);
            descsRes[i] = single.getString("desc");
        }

        Assert.assertEqualsNoOrder(descsRes, descs, "orderId=" + orderId + "???phone=" + phone + "?????????????????????????????????????????????????????????" +
                Arrays.toString(descs) + ",???????????????" + Arrays.toString(descsRes));
    }


    public void login() {
        qaDbUtil.openConnection();
        qaDbUtil.openConnectionRdDaily();
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        logger.info("\n\n" + caseName + "\n");

        initHttpConfig();
        String path = "/risk-login";
        String loginUrl = getIpPort() + path;
        String json = "{\"username\":\"yuexiu@test.com\",\"passwd\":\"f5b3e737510f31b88eb2d4b5d0cd2fb4\"}";
        config.url(loginUrl)
                .json(json);
        logger.info("{} json param: {}", path, json);
        long start = System.currentTimeMillis();
        try {
            response = HttpClientUtil.post(config);
            this.authorization = JSONObject.parseObject(response).getJSONObject("data").getString("token");
            logger.info("authorization: {}", this.authorization);
        } catch (Exception e) {
            aCase.setFailReason("http post ???????????????url = " + loginUrl + "\n" + e);
            logger.error(aCase.getFailReason());
            logger.error(e.toString());
        }
        logger.info("{} time used {} ms", path, System.currentTimeMillis() - start);

        saveData(aCase, ciCaseName, caseName, failReason, "????????????authentication");
    }

    public void clean() {
        qaDbUtil.closeConnection();
        qaDbUtil.closeConnectionRdDaily();
        dingPushFinal();
    }

    public void dingPushFinal() {
        if (DEBUG.trim().toLowerCase().equals("false") && FAIL) {
            AlarmPush alarmPush = new AlarmPush();

            alarmPush.setDingWebhook(DingWebhook.QA_TEST_GRP);
//            alarmPush.setDingWebhook(DingWebhook.CAR_OPEN_MANAGEMENT_PLATFORM_GRP);

            //15898182672 ?????????
            //18513118484 ??????
            //15011479599 ?????????
            //18600872221 ?????????
            String[] rd = {"18513118484", //??????
                    "15011479599", //?????????
                    "15898182672"}; //?????????
            alarmPush.alarmToRd(rd);
        }

        FAIL = false;
    }

    public void saveData(Case aCase, String ciCaseName, String caseName, String failReason, String caseDescription) {
        setBasicParaToDB(aCase, ciCaseName, caseName, failReason, caseDescription);
        qaDbUtil.saveToCaseTable(aCase);
        if (!StringUtils.isEmpty(aCase.getFailReason())) {
            logger.error(aCase.getFailReason());

            failReason = aCase.getFailReason();

            String message = "???????????? \n" +
                    "?????????" + aCase.getCaseDescription() +
                    " \n\n" + failReason;

            dingPush(aCase, message);
        }
    }

    public void dingPush(Case aCase, String msg) {
        AlarmPush alarmPush = new AlarmPush();
        if (DEBUG.trim().toLowerCase().equals("false")) {
//            alarmPush.setDingWebhook(DingWebhook.QA_TEST_GRP);
            alarmPush.setDingWebhook(DingWebhook.CAR_OPEN_MANAGEMENT_PLATFORM_GRP);
        } else {
            alarmPush.setDingWebhook(DingWebhook.QA_TEST_GRP);
        }
        alarmPush.dailyRgn(msg);
        this.FAIL = true;
        Assert.assertNull(aCase.getFailReason());
    }

    public void setBasicParaToDB(Case aCase, String ciCaseName, String caseName, String failReason, String caseDesc) {
        aCase.setApplicationId(APP_ID);
        aCase.setConfigId(CONFIG_ID);
        aCase.setCaseName(caseName);
        aCase.setCaseDescription(caseDesc);
        aCase.setCiCmd(CI_CMD + ciCaseName);
        aCase.setQaOwner("?????????");
        aCase.setExpect("code==1000");
        aCase.setResponse(response);

        if (!StringUtils.isEmpty(failReason) || !StringUtils.isEmpty(aCase.getFailReason())) {
            if (failReason.contains("java.lang.Exception:")) {
                failReason = failReason.replace("java.lang.Exception", "??????");
            } else if (failReason.contains("java.lang.AssertionError")) {
                failReason = failReason.replace("java.lang.AssertionError", "??????");
            } else if (failReason.contains("java.lang.IllegalArgumentException")) {
                failReason = failReason.replace("java.lang.IllegalArgumentException", "??????");
            }
            aCase.setFailReason(failReason);
        } else {
            aCase.setResult("PASS");
        }
    }

    public void initHttpConfig() {
        HttpClient client;
        try {
            client = HCB.custom()
                    .pool(50, 10)
                    .retry(3).build();
        } catch (HttpProcessException e) {
            failReason = "?????????http????????????" + "\n" + e;
            return;
        }
        String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.132 Safari/537.36";
        Header[] headers = HttpHeader.custom().contentType("application/json; charset=utf-8")
                .other("shop_id", getShopId().toString())
                .userAgent(userAgent)
                .authorization(authorization)
                .build();

        config = HttpConfig.custom()
                .headers(headers)
                .client(client);
    }

    public String getIpPort() {
        return "http://dev.store.winsenseos.cn";
    }

}
