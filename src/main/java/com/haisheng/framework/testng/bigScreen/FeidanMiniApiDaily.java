package com.haisheng.framework.testng.bigScreen;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.arronlong.httpclientutil.HttpClientUtil;
import com.arronlong.httpclientutil.builder.HCB;
import com.arronlong.httpclientutil.common.HttpConfig;
import com.arronlong.httpclientutil.common.HttpHeader;
import com.arronlong.httpclientutil.exception.HttpProcessException;
import com.google.common.collect.ImmutableMap;
import com.haisheng.framework.model.bean.Case;
import com.haisheng.framework.testng.CommonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.CommonDataStructure.DingWebhook;
import com.haisheng.framework.util.AlarmPush;
import com.haisheng.framework.util.DateTimeUtil;
import com.haisheng.framework.util.QADbUtil;
import com.haisheng.framework.util.StatusCode;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.File;
import java.io.FileInputStream;
import java.time.LocalDate;
import java.util.*;

/**
 * @author : huachengyu
 * @date :  2019/11/21  14:55
 */

public class FeidanMiniApiDaily {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private String failReason = "";
    private String response = "";
    private boolean FAIL = false;
    private Case aCase = new Case();

    DateTimeUtil dateTimeUtil = new DateTimeUtil();
    private QADbUtil qaDbUtil = new QADbUtil();
    private int APP_ID = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
    private int CONFIG_ID = ChecklistDbInfo.DB_SERVICE_ID_FEIDAN_DAILY_SERVICE;

    private String CI_CMD = "curl -X POST http://qarobot:qarobot@192.168.50.2:8080/job/feidan-daily-test/buildWithParameters?case_name=";

    private String DEBUG = System.getProperty("DEBUG", "true");

    private String authorization = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiLotornp4DmtYvor5XotKblj7ciLCJ1aWQiOiJ1aWRfZWY2ZDJkZTUiLCJsb2dpblRpbWUiOjE1NzQyNDE5NDIxNjV9.lR3Emp8iFv5xMZYryi0Dzp94kmNT47hzk2uQP9DbqUU";

    private HttpConfig config;

    String channelId = "5";
    String gongErId = "12";
    String anShengId = "15";

    private String getIpPort() {
        return "http://dev.store.winsenseos.cn";
    }

    private void checkResult(String result, String... checkColumnNames) {
        logger.info("result = {}", result);
        JSONObject res = JSONObject.parseObject(result);
        if (!res.getInteger("code").equals(1000)) {
            throw new RuntimeException("result code is " + res.getInteger("code") + " not success code");
        }
        for (String checkColumn : checkColumnNames) {
            Object column = res.getJSONObject("data").get(checkColumn);
            logger.info("{} : {}", checkColumn, column);
            if (column == null) {
                throw new RuntimeException("result does not contains column " + checkColumn);
            }
        }
    }

    private void initHttpConfig() {
        HttpClient client;
        try {
            client = HCB.custom()
                    .pool(50, 10)
                    .retry(3).build();
        } catch (HttpProcessException e) {
            failReason = "初始化http配置异常" + "\n" + e;
            return;
            //throw new RuntimeException("初始化http配置异常", e);
        }
        //String authorization = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIyIiwidXNlcm5hbWUiOiJ5dWV4aXUiLCJleHAiOjE1NzE0NzM1OTh9.QYK9oGRG48kdwzYlYgZIeF7H2svr3xgYDV8ghBtC-YUnLzfFpP_sDI39D2_00wiVONSelVd5qQrjtsXNxRUQ_A";
        String userAgent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36";
        Header[] headers = HttpHeader.custom().contentType("application/json; charset=utf-8")
                .other("shop_id", getShopId().toString())
                .userAgent(userAgent)
                .authorization(authorization)
                .build();

        config = HttpConfig.custom()
                .headers(headers)
                .client(client);
    }

    private String httpPostWithCheckCode(String path, String json, String... checkColumnNames) throws Exception {
        initHttpConfig();
        String queryUrl = getIpPort() + path;
        config.url(queryUrl).json(json);
        logger.info("{} json param: {}", path, json);
        long start = System.currentTimeMillis();

        response = HttpClientUtil.post(config);

        logger.info("{} time used {} ms", path, System.currentTimeMillis() - start);
        checkResult(response, checkColumnNames);
        return response;
    }

    private String httpPost(String path, String json, String... checkColumnNames) throws Exception {
        initHttpConfig();
        String queryUrl = getIpPort() + path;
        config.url(queryUrl).json(json);
        logger.info("{} json param: {}", path, json);
        long start = System.currentTimeMillis();

        response = HttpClientUtil.post(config);

        logger.info("{} time used {} ms", path, System.currentTimeMillis() - start);
        return response;
    }

    private void checkCode(String response, int expect, String message) throws Exception {
        JSONObject resJo = JSON.parseObject(response);

        if (resJo.containsKey("code")) {
            int code = resJo.getInteger("code");

            message += resJo.getString("message");

            if (expect != code) {
                throw new Exception(message + " expect code: " + expect + ",actual: " + code);
            }
        } else {
            int status = resJo.getInteger("status");
            String path = resJo.getString("path");
            throw new Exception("接口调用失败，status：" + status + ",path:" + path);
        }
    }

    /**
     * 获取登录信息 如果上述初始化方法（initHttpConfig）使用的authorization 过期，请先调用此方法获取
     *
     * @ 异常
     */
    @BeforeSuite
    public void login() {
        qaDbUtil.openConnection();
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

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
            aCase.setFailReason("http post 调用异常，url = " + loginUrl + "\n" + e);
            logger.error(aCase.getFailReason());
            logger.error(e.toString());
        }
        logger.info("{} time used {} ms", path, System.currentTimeMillis() - start);

        saveData(aCase, caseName, "登录获取authentication");
    }

    @AfterSuite
    public void clean() {
        qaDbUtil.closeConnection();
        dingPushFinal();
    }

    @BeforeMethod
    public void initialVars() {
        failReason = "";
        response = "";
        aCase = new Case();
    }

    @Test
    public void testShopList() {
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        try {
            String path = "/risk/shop/list";
            String json = "{}";
            String checkColumnName = "list";
            httpPostWithCheckCode(path, json, checkColumnName);

        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, caseName, "校验shop");
        }

    }

    private Object getShopId() {
        return "4116";
    }

    private final int pageSize = 15;

    private static final String ADD_ORDER = "/risk/order/createOrder";
    private static final String ORDER_DETAIL = "/risk/order/detail";
    private static final String AUDIT_ORDER = "/risk/order/status/audit";
    private static final String CUSTOMER_LIST = "/risk/customer/list";
    private static final String CUSTOMER_DETAIL = "/risk/customer/detail";
    private static final String CUSTOMER_APPEAR_LIST = "/risk/customer/day/appear/list";
    private static final String ORDER_LIST = "/risk/order/list";
    private static final String ORDER_STEP_LOG = "/risk/order/step/log";
    private static final String CHANNEL_STAFF_PAGE = "/risk/channel/staff/page";
    private static final String CUSTOMER_INSERT = "/risk/customer/insert";
    private static final String CHANNEL_LIST = "/risk/channel/page";
    private static final String STAFF_LIST = "/risk/staff/page";
    private static final String STAFF_TYPE_LIST = "/risk/staff/type/list";
    private static final String ADD_CHANNEL = "/risk/channel/add";
    private static final String ADD_CHANNEL_STAFF = "/risk/channel/staff/register";
    private static final String EDIT_CHANNEL_STAFF = "/risk/channel/staff/edit/";
    private static final String ADD_STAFF = "/risk/staff/add";
    private static final String DELETE_STAFF = "/risk/staff/delete/";
    private static final String EDIT_STAFF = "/risk/staff/edit/";

    private static String CREATE_ORDER_JSON = "{\"request_id\":\"${requestId}\"," +
            "\"shop_id\":${shopId},\"id_card\":\"${idCard}\",\"phone\":\"${phone}\"," +
            "\"order_stage\":\"${orderStage}\"}";

    private static String AUDIT_ORDER_JSON = "{\"is_customer_introduced\":${isCustomerIntroduced}," +
            "\"introduce_checked_person\":\"${introduceCheckedPerson}\"," +
            "\"is_channel_staff_show_dialog\":${isChannelStaffShowDialog}," +
            "\"dialog_path\":\"FEIDAN/undefined/30830a3179a3d75c634335a7104553fa\"," +
            "\"shop_id\":${shopId}," +
            "\"order_id\":\"${orderId}\"}";

    private static String CUSTOMER_DETAIL_JSON = "{\"cid\":\"${cid}\"," +
            "\"shop_id\":${shopId}}";

    private static String CUSTOMER_LIST_JSON = "{\"search_type\":\"${searchType}\"," +
            "\"shop_id\":${shopId},\"page\":\"${page}\",\"size\":\"${pageSize}\"}";

    private static String CUSTOMER_LIST_WITH_CHANNEL_JSON = "{\"search_type\":\"${searchType}\"," +
            "\"shop_id\":${shopId},\"channel_id\":\"${channelId}\",\"page\":\"${page}\",\"size\":\"${pageSize}\"}";
//    顾客列表中是size参数控制一页显示的条数，订单列表中是pageSize控制

    private static String CUSTOMER_APPEAR_LIST_JSON = "{\"start_time\":\"${startTime}\",\"end_time\":\"${endTime}\",\"cid\":\"${cid}\"," +
            "\"shop_id\":${shopId}}";

    //    顾客列表中是size参数控制一页显示的条数，订单列表中是pageSize控制
    private static String ORDER_LIST_JSON = "{\"shop_id\":${shopId},\"page\":\"${page}\",\"page_size\":\"${pageSize}\"}";
    private static String ORDER_LIST_WITH_CHANNEL_JSON = "{\"shop_id\":${shopId},\"channel_id\":\"${channelId}\",\"page\":\"1\",\"page_size\":\"10000\"}";
    private static String ORDER_LIST_WITH_STATUS_JSON = "{\"shop_id\":${shopId},\"status\":\"${status}\",\"page\":\"1\",\"page_size\":\"10000\"}";
    private static String ORDER_LIST_WITH_PHONE_JSON = "{\"shop_id\":${shopId},\"customer_name\":\"${customerName}\",\"page\":\"1\",\"page_size\":\"10000\"}";

    private static String ORDER_DETAIL_JSON = "{\"order_id\":\"${orderId}\"," +
            "\"shop_id\":${shopId}}";

    private static String ORDER_STEP_LOG_JSON = ORDER_DETAIL_JSON;

    private static String CHANNEL_STAFF_LIST_JSON = "{\"channel_id\":\"${channelId}\"," +
            "\"shop_id\":${shopId},\"page\":\"${page}\",\"size\":\"${pageSize}\"}";
    private static String CHANNEL_STAFF_LIST_PHOEN_JSON = "{\"channel_id\":\"${channelId}\"," +
            "\"shop_id\":${shopId},\"name_phone\":\"${namePhone}\",\"page\":\"${page}\",\"size\":\"${pageSize}\"}";

    private static String CUSTOMER_INSERT_JSON = "{\"shop_id\":\"${shopId}\",\"channel_id\":${channelId}," +
            "\"channel_staff_id\":\"${channelStaffId}\",\"adviser_id\":\"${adviserId}\"," +
            "\"customer_name\":\"${customerName}\",\"phone\":\"${phone}\"}";

    private static String CHANNEL_LIST_JSON = "{\"shop_id\":${shopId},\"page\":\"${page}\",\"page_size\":\"${pageSize}\"}";

    private static String STAFF_TYPE_LIST_JSON = "{\"shop_id\":${shopId}}";

    private static String STAFF_LIST_JSON = "{\"shop_id\":${shopId},\"page\":\"${page}\",\"size\":\"${pageSize}\"}";

    private static String STAFF_LIST_WITH_TYPE_JSON = "{\"shop_idaddStaffTestPage\":${shopId},\"staff_type\":\"${staffType}\",\"page\":\"${page}\",\"size\":\"${pageSize}\"}";

    private static String ADD_CHANNEL_JSON = "{\"shop_id\":${shopId},\"channel_name\":\"${channelName}\"," +
            "\"owner_principal\":\"${owner}\",\"phone\":\"${phone}\",\"contract_code\":\"${contractCode}\"}";

    private static String ADD_CHANNEL_STAFF_JSON = "{\"shop_id\":${shopId},\"staff_name\":\"${staffName}\"," +
            "\"channel_id\":\"${channelId}\",\"phone\":\"${phone}\"}";

    private static String ADD_CHANNEL_STAFF_WITH_PIC_JSON = "{\"shop_id\":${shopId},\"staff_name\":\"${staffName}\"," +
            "\"channel_id\":\"${channelId}\",\"phone\":\"${phone}\",\"face_url\":\"${faceUrl}\"}";

    private static String EDIT_CHANNEL_STAFF_WITH_PIC_JSON = "{\"shop_id\":${shopId},\"staff_name\":\"${staffName}\"," +
            "\"channel_id\":\"${channelId}\",\"face_url\":\"${faceUrl}\",\"phone\":\"${phone}\"}";

    private static String EDIT_CHANNEL_STAFF_JSON = "{\"shop_id\":${shopId},\"staff_name\":\"${staffName}\"," +
            "\"channel_id\":\"${channelId}\",\"phone\":\"${phone}\"}";

    private static String ADD_STAFF_JSON = "{\"shop_id\":${shopId},\"staff_name\":\"${staffName}\"," +
            "\"staff_type\":\"${staffType}\",\"phone\":\"${phone}\",\"face_url\":\"${faceUrl}\"}";

    private static String EDIT_STAFF_JSON = "{\"shop_id\":${shopId},\"staff_name\":\"${staffName}\"," +
            "\"staff_type\":\"${staffType}\",\"phone\":\"${phone}\",\"face_url\":\"${faceUrl}\"}";

    @Test(dataProvider = "SEARCH_TYPE")
    public void customerListEqualsDetail(String searchType) {
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName() + "-" + searchType;

        try {

            JSONArray list = customerList(searchType, 1, pageSize);
            for (int i = 0; i < list.size(); i++) {
                JSONObject listData = list.getJSONObject(i);
                String cidOfList = listData.getString("cid");

                String ageGroupList = getValue(listData, "age_group");
                String adviserNameList = getValue(listData, "adviser_name");
                String customerNameList = getValue(listData, "customer_name");
                String firstAppearTimeList = getValue(listData, "first_appear_time");
                String lastVisitTimeList = getValue(listData, "last_visit_time");
                String phoneList = getValue(listData, "phone");

                JSONObject data = customerDetail(cidOfList);

                compareValue(data, "顾客", cidOfList, "adviser_name", adviserNameList, "置业顾问");
                compareValue(data, "顾客", cidOfList, "customer_name", customerNameList, "顾客姓名");
                compareValue(data, "顾客", cidOfList, "age_group", ageGroupList, "年龄段");
                compareValue(data, "顾客", cidOfList, "first_appear_time", firstAppearTimeList, "首次出现时间");
                compareValue(data, "顾客", cidOfList, "last_appear_time", lastVisitTimeList, "最后出现时间");
                compareValue(data, "顾客", cidOfList, "phone", phoneList, "顾客手机号码");
            }
        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, caseName, "顾客列表与顾客详情中的信息一致");
        }
    }

    @Test(dataProvider = "SEARCH_TYPE")
    public void appearListEqualsDetail(String searchType) {
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName() + "-" + searchType;

        try {
            //顾客列表

            JSONArray list = customerList(searchType, 1, pageSize);
            for (int i = 0; i < list.size(); i++) {
                JSONObject data = list.getJSONObject(i);
                String cidOfList = data.getString("cid");

                //顾客详情
                data = customerDetail(cidOfList);

                String firstAppearTime = data.getString("first_appear_time");

                String lastAppearTime = data.getString("last_appear_time");


                JSONArray appearList = customerAppearList(cidOfList, LocalDate.now().minusDays(3000).toString(), LocalDate.now().toString());

                if (appearList != null && appearList.size() != 0) {
                    if (firstAppearTime == null || "".equals(firstAppearTime)) {
                        throw new Exception("cid：【" + cidOfList + "】,到访记录列表不为空，顾客详情中的【first_appear_time】为空！");
                    }
                    if (lastAppearTime == null || "".equals(lastAppearTime)) {
                        throw new Exception("cid：【" + cidOfList + "】,到访记录列表不为空，顾客详情中的【last_appear_time】为空！");
                    }
                }

                //未到场顾客没有出现时间
                if (firstAppearTime != null && !"".equals(firstAppearTime)) {
                    long firstAppearTimeList = Long.valueOf(firstAppearTime);
                    String firstDate = dateTimeUtil.timestampToDate("yyyy-MM-dd", firstAppearTimeList);
                    String firstMinute = dateTimeUtil.timestampToDate("HH:mm", firstAppearTimeList);

                    long lastAppearTimeList = Long.valueOf(getValue(data, "last_appear_time"));
                    String lastDate = dateTimeUtil.timestampToDate("yyyy-MM-dd", lastAppearTimeList);
                    String lastMinute = dateTimeUtil.timestampToDate("HH:mm", lastAppearTimeList);

                    if (appearList == null || appearList.size() == 0) {
                        throw new Exception("cid:" + cidOfList + ", 有最早最晚出现时间，没有到访记录！");
                    }

                    JSONObject first = appearList.getJSONObject(0);
                    String firstdateAppearList = getValue(first, "date");
                    if (!firstdateAppearList.equals(firstDate)) {
                        throw new Exception("cid：" + cidOfList + ",到访记录列表中最早出现日期：" + firstdateAppearList + ",详情中：" + firstDate);
                    }
                    String firstMinuteAppearList = dateTimeUtil.timestampToDate("HH:mm", Long.valueOf(getValue(first, "first_appear_timestamp")));
                    if (!firstMinute.equals(firstMinuteAppearList)) {
                        throw new Exception("cid：" + cidOfList + ",到访记录列表中最早出现时间：" + firstDate + " " + firstMinuteAppearList +
                                ",详情中：" + firstDate + " " + firstMinute);
                    }

                    JSONObject last = appearList.getJSONObject(appearList.size() - 1);
                    String lastdateAppearList = getValue(last, "date");
                    if (!lastdateAppearList.equals(lastDate)) {
                        throw new Exception("cid：" + cidOfList + ",到访记录列表中最晚出现日期：" + lastdateAppearList + ",详情中：" + lastDate);
                    }
                    String lastMinuteAppearList = dateTimeUtil.timestampToDate("HH:mm", Long.valueOf(getValue(last, "last_appear_timestamp")));
                    if (!lastMinute.equals(lastMinuteAppearList)) {
                        throw new Exception("cid：" + cidOfList + ",到访记录列表中最晚出现时间：" + lastDate + " " + lastMinuteAppearList +
                                ",详情中：" + lastDate + " " + lastMinute);
                    }
                }
            }

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, caseName, "顾客到访记录列表与顾客详情中的信息一致");
        }
    }

    @Test(dataProvider = "SEARCH_TYPE")
    public void appearDayEquals(String searchType) {
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName() + "-" + searchType;

        try {
            //顾客列表

            JSONArray list = customerList(searchType, 1, pageSize);
            for (int i = 0; i < list.size(); i++) {
                JSONObject detailData = list.getJSONObject(i);
                String cidOfList = detailData.getString("cid");

                //顾客详情
                detailData = customerDetail(cidOfList);

                JSONArray appearList = customerAppearList(cidOfList, LocalDate.now().minusDays(3000).toString(), LocalDate.now().toString());
                int stayTimes = detailData.getInteger("stay_times");

                if (appearList != null && appearList.size() != 0) {
                    if (stayTimes == 0) {
                        throw new Exception("cid【" + cidOfList + "】，到访记录列表不为空，详情中到场天数为空。");
                    }
                }

                if (stayTimes > 0) {
                    if (appearList == null || appearList.size() == 0) {
                        throw new Exception("cid：" + cidOfList + ",到访记录列表中有【" + appearList.size() + "】个日期，详情中到场天数是【" + stayTimes + "】");
                    }
                }
            }

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, caseName, "顾客到访记录列表的天数与顾客详情中的累计到场天数一致");
        }
    }

    @Test(dataProvider = "SEARCH_TYPE")
    public void appearDayNotNull(String searchType) {
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName() + "-" + searchType;

        try {
            //顾客列表

            JSONArray list = customerList(searchType, 1, pageSize);
            for (int i = 0; i < list.size(); i++) {
                JSONObject detailData = list.getJSONObject(i);
                String cidOfList = detailData.getString("cid");

                //顾客详情
                detailData = customerDetail(cidOfList);

                int stayTimes = detailData.getInteger("stay_times");

                String firstAppearTime = detailData.getString("first_appear_time");

                String lastAppearTime = detailData.getString("last_appear_time");

                if (firstAppearTime != null && !"".equals(firstAppearTime)) {
                    if (lastAppearTime == null || "".equals(lastAppearTime)) {
                        throw new Exception("cid：" + cidOfList + ",首次出现时间不为空，最后出现时间为空！");
                    }
                }

                if (lastAppearTime != null && !"".equals(lastAppearTime)) {
                    if (firstAppearTime == null || "".equals(firstAppearTime)) {
                        throw new Exception("cid：" + cidOfList + ",最后出现时间不为空，首次出现时间为空！");
                    }
                }

                if (firstAppearTime != null && !"".equals(firstAppearTime)) {
                    if (stayTimes == 0) {
                        throw new Exception("cid：" + cidOfList + ",首次出现时间不为空，累计到场天数为0！");
                    }
                }
            }

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, caseName, "首次出现时间时，到场天数不能为空");
        }
    }

    @Test
    public void dealListEqualsDetail() {
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        try {
            // 订单列表
            JSONArray list = orderList(1, pageSize);
            for (int i = 0; i < list.size() && i<=20; i++) {
                JSONObject single = list.getJSONObject(i);
                String orderId = getValue(single, "order_id");
                String customerName = getValue(single, "customer_name");
                String adviserName = getValue(single, "adviser_name");
                String channelName = getValue(single, "channel_name");
                String firstAppearTime = getValue(single, "first_appear_time");
                String reportTime = getValue(single, "report_time");
                String signTime = getValue(single, "sign_time");
                String dealTime = getValue(single, "deal_time");
                String status = getValue(single, "status");
                String isAudited = getValue(single, "is_audited");

                if ("3".equals(orderId)) {
                    System.out.println("fdsfsd");
                }


                if ("3".equals(orderId)) {

                    JSONObject data = orderDetail(orderId);
                    compareValue(data, "订单", orderId, "customer_name", customerName, "顾客姓名");
                    compareValue(data, "订单", orderId, "adviser_name", adviserName, "置业顾问");
                    compareValue(data, "订单", orderId, "channel_name", channelName, "渠道名称");
                    compareValue(data, "订单", orderId, "order_status", status, "订单状态");
                    compareValue(data, "订单", orderId, "is_audited", isAudited, "是否审核");

                    compareOrderTimeValue(data, "first_appear_time", orderId, firstAppearTime, "订单列表", "订单详情");
                    compareOrderTimeValue(data, "report_time", orderId, reportTime, "订单列表", "订单详情");
                    compareOrderTimeValue(data, "sign_time", orderId, signTime, "订单列表", "订单详情");
                    compareOrderTimeValue(data, "deal_time", orderId, dealTime, "订单列表", "订单详情");
                }


            }
        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, caseName, "订单详情与订单列表中信息是否一致");
        }
    }

    @Test
    public void dealLogEqualsDetail() {
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        try {
            // 订单列表
            JSONArray list = orderList(1, pageSize);
            for (int i = 0; i < list.size(); i++) {
                String orderId = getValue(list.getJSONObject(i), "order_id");

                JSONArray logList = orderStepLog(orderId);

                String firstAppearTime = "";
                String reportTime = "";
                String signTime = "";
                String dealTime = "";

                for (int j = 0; j < logList.size(); j++) {
                    JSONObject singleStep = logList.getJSONObject(j);
                    String stepType = singleStep.getString("step_type");
                    String stepNameContent = singleStep.getString("step_name_content");

                    if ("FIRST_APPEAR".equals(stepType)) {
                        firstAppearTime = singleStep.getString("time_str");
                    } else if ("REPORT".equals(stepType)) {
                        reportTime = singleStep.getString("time_str");
                    } else if ("CREATE_ORDER".equals(stepType)) {
                        if ("认购认筹".equals(stepNameContent)) {
                            signTime = singleStep.getString("time_str");
                        } else if ("成交".equals(stepNameContent)) {
                            dealTime = singleStep.getString("time_str");
                        } else {
                            throw new Exception("步骤名称出错--返回名称为【" + stepNameContent + "】,期待：认购/认筹或成交");
                        }
                    } else if ("STAGE_CHANGE".equals(stepType)) {
                        dealTime = singleStep.getString("time_str");
                    }
                }

                JSONObject detailData = orderDetail(orderId);

                compareOrderTimeValue(detailData, "first_appear_time", orderId, firstAppearTime, "订单跟进详情", "订单详情");
                compareOrderTimeValue(detailData, "report_time", orderId, reportTime, "订单跟进详情", "订单详情");
                compareOrderTimeValue(detailData, "sign_time", orderId, signTime, "订单跟进详情", "订单详情");
                compareOrderTimeValue(detailData, "deal_time", orderId, dealTime, "订单跟进详情", "订单详情");
            }

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, caseName, "订单详情与订单跟进详情中信息是否一致");
        }
    }

    //    新建报备,测试报备数量
    @Test
    public void channelStaffReportNum() {
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        try {
            //取出渠道员工宫二的报备数
            int channelStaffTotalReportBefore =
                    getChannelStaffReportNum(channelStaffList(channelId, 1, pageSize));//"5"是测试【勿动】

//            新建报备
            String phoneNum = genPhoneNum();

            newCustomer(channelId,  //测试【勿动】
                    gongErId,  //宫二
                    anShengId,  //"矮大紧"
                    phoneNum,
                    "测试数量");

            //取出渠道员工宫二的报备数
            int channelStaffTotalReportAfter =
                    getChannelStaffReportNum(channelStaffList("5", 1, pageSize));//"5"是测试【勿动】

            //比较报备前后渠道员工的报备数
            if (channelStaffTotalReportAfter - 1 != channelStaffTotalReportBefore) {
                throw new Exception("新建报备后渠道员工--宫二的累计报备数没有加1。报备前：" +
                        channelStaffTotalReportBefore + ",报备后：" + channelStaffTotalReportAfter + "。顾客手机号:" + phoneNum);
            }

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, caseName, "新建报备后，业务员累计报备数是否增加");
        }
    }

    @Test
    public void channelTotalEqualsStaffTotal() {
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        try {
            JSONArray channelList = channelList(1, pageSize);

            for (int i = 0; i < channelList.size(); i++) {
                JSONObject singleChannel = channelList.getJSONObject(i);
                int channelNum = singleChannel.getInteger("total_customers");
                String channelId = singleChannel.getString("channel_id");
                if ("1".equals(channelId)) {
                    channelNum -= 4;
                }
                String channelName = singleChannel.getString("channel_name");

                JSONArray staffList = channelStaffList(channelId, 1, pageSize);
                int staffNum = 0;
                for (int j = 0; j < staffList.size(); j++) {
                    JSONObject singleStaff = staffList.getJSONObject(j);
                    staffNum += singleStaff.getInteger("total_report");
                }

                if (staffNum != channelNum) {
                    throw new Exception("渠道【" + channelName + "】,渠道累计报备数：" + channelNum + "，业务员累计报备数之和：" + staffNum);
                }
            }
        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, caseName, "渠道的累计报备数==各个业务员的累计报备数之和");
        }
    }

    @Test
    public void customerOrderEqualschannelOrder() {
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        try {
            //查询渠道列表，获取channel_id
            JSONArray channelList = channelList(1, pageSize);

            for (int i = 0; i < channelList.size(); i++) {
                JSONObject singleChannel = channelList.getJSONObject(i);
                String channelId = singleChannel.getString("channel_id");
                String channelName = singleChannel.getString("channel_name");

                int customerListSize = customerListWithChannel("CHECKED", channelId, 1, pageSize).size();

                JSONArray orderList = orderListWithChannel(channelId, 1, pageSize);
                HashMap<String, Integer> hm = new HashMap<>();
                int channelListSize = 0;
                for (int j = 0; j < orderList.size(); j++) {
                    JSONObject singleOrder = orderList.getJSONObject(j);
                    String customerPhone = singleOrder.getString("customer_phone");
                    if (!hm.containsKey(customerPhone)) {
                        hm.put(customerPhone, 1);
                        channelListSize++;
                    }
                }

                if (customerListSize != channelListSize) {
                    throw new Exception("渠道：" + channelName + ",顾客列表中的订单数：" + customerListSize + ", 渠道详情中的订单数：" + channelListSize);
                }
            }
        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, caseName, "顾客查询中的签约顾客数==渠道中的签约顾客数");
        }
    }

    @Test
    public void customerReportEqualsChannelReport() {
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        try {
            //查询渠道列表，获取channel_id
            JSONArray channelList = channelList(1, pageSize);

            for (int i = 0; i < channelList.size(); i++) {
                JSONObject singleChannel = channelList.getJSONObject(i);
                String channelId = singleChannel.getString("channel_id");
                String channelName = singleChannel.getString("channel_name");
                Integer channelReportNum = singleChannel.getInteger("total_customers");

                JSONArray customerList = customerListWithChannel("REPORTED", channelId, 1, pageSize);

                int customerListReportNum = customerList.size();

                if (channelReportNum != customerListReportNum) {
                    throw new Exception("渠道【" + channelName + "】,顾客列表中的报备数：" + customerListReportNum + ", 渠道列表中的报备数：" + channelReportNum);
                }
            }
        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, caseName, "顾客查询中的报备顾客数==渠道中的报备顾客数");
        }
    }

    @Test
    public void customerListCHECKEDNotCHANCE() {
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        try {
            JSONArray customerListChance = customerList("CHANCE", 1, pageSize);

            JSONArray customerListChecked = customerList("CHECKED", 1, pageSize);

            HashMap<String, Integer> hm = new HashMap<>();
            for (int i = 0; i < customerListChecked.size(); i++) {
                hm.put(customerListChecked.getJSONObject(i).getString("phone"), 1);
            }

            for (int i = 0; i < customerListChance.size(); i++) {
                String phone = customerListChance.getJSONObject(i).getString("phone");
                if (hm.containsKey(phone)) {
                    throw new Exception("手机号为【" + phone + "】顾客，同时出现在签约顾客列表和机会顾客列表中。");
                }
            }
        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, caseName, "签约顾客不能出现在机会顾客中");
        }
    }

    @Test
    public void orderListDiffType() {
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        try {
            JSONArray totalList = orderList(1, pageSize);
            int totalNum = totalList.size();

//            获取正常订单数
            JSONArray normalList = orderListWithStatus("1", 1, pageSize);//1是正常，3是风险
            int normalNum = normalList.size();

//            获取风险订单数
            JSONArray riskList = orderListWithStatus("3", 1, pageSize);//1是正常，3是风险
            int riskNum = riskList.size();

            if (normalNum + riskNum != totalNum) {
                throw new Exception("订单列表总数：" + totalNum + ",正常订单数：" + normalNum + ",风险订单数：" + riskNum);
            }
        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, caseName, "订单列表中，风险+正常的订单数==订单列表总数");
        }
    }

    @Test
    public void customerListDiffTypeNum() {
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        try {
            int customerCheckedNum = customerList("CHECKED", 1, pageSize).size();

            int customerChanceNum = customerList("CHANCE", 1, pageSize).size();

            int customerReportedNum = customerList("REPORTED", 1, pageSize).size();

            if (customerCheckedNum + customerChanceNum < customerReportedNum) {
                throw new Exception("顾客列表中，签约顾客数：" + customerCheckedNum + " + 机会顾客数：" + customerChanceNum + " 小于 报备顾客数：" + customerReportedNum);
            }

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, caseName, "签约顾客+机会顾客≥报备顾客");
        }
    }

    @Test
    public void staffTypeNum() {
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        try {

//            1、获取员工类型
            JSONArray staffTypeList = staffTypeList();

            HashMap<String, String> staffTypes = new HashMap<>();

            for (int i = 0; i < staffTypeList.size(); i++) {

                JSONObject singleType = staffTypeList.getJSONObject(i);

                staffTypes.put(singleType.getString("staff_type"), singleType.getString("type_name"));
            }

//            2、查询员工总体中各类型的员工数
            JSONArray totalList = staffList(1, pageSize);

            HashMap<String, Integer> staffNumHm = new HashMap<>();

            for (String key : staffTypes.keySet()) {
                staffNumHm.put(key, 0);
            }

            for (int j = 0; j < totalList.size(); j++) {
                String staffType = totalList.getJSONObject(j).getString("staff_type");
                staffNumHm.put(staffType, staffNumHm.get(staffType) + 1);
            }

//            3、查询各个类型的员工列表
            for (Map.Entry<String, String> entry : staffTypes.entrySet()) {
                String staffType = entry.getKey();

                JSONArray array = staffListWithType(staffType, 1, pageSize);
                int size = 0;
                if (array != null) {
                    size = array.size();
                }

                if (size != staffNumHm.get(staffType)) {
                    throw new Exception("不选员工类型时，列表返回结果中【" + staffTypes.get(staffType) + "】的数量为：" + staffNumHm.get(staffType) +
                            ", 选择类型查询时，查询结果中该类型员工数为：" + array.size());
                }

            }

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, caseName, "员工管理中，各类型员工数量统计是否正确");
        }
    }

    @Test
    public void addStaffTestPage() {
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        try {

            String dirPath = "src/main/java/com/haisheng/framework/testng/bigScreen/feidanImages";

            int pageSizeTemp = 10;

            File file = new File(dirPath);
            File[] files = file.listFiles();

            ArrayList<String> phones = new ArrayList<>();

            for (int i = 0; i < files.length; i++) {

                String imagePath = dirPath + "/" + files[i].getName();

                imagePath = imagePath.replace("/", File.separator);

                JSONObject uploadImage = uploadImage(imagePath);

                String phoneNum = genPhoneNum();

                phones.add(phoneNum);

                addStaff("staff-" + i, getOneStaffType(), phoneNum, uploadImage.getString("face_url"));

                int totalPage = getTotalPage(staffListReturnData(1, pageSizeTemp));

                for (int j = 1; j <= totalPage; j++) {
                    JSONArray staffList = staffList(j, pageSizeTemp);

                    if (j < totalPage) {
                        if (staffList.size() != 10) {
                            throw new Exception("员工列表，第【" + j + "】页不是最后一页，仅有【" + staffList.size() + "】条记录。");
                        }
                    } else {
                        if (staffList.size() == 0) {
                            throw new Exception("员工列表，第【" + j + "】页显示为空");
                        }
                    }
                }
            }

            JSONArray staffList = staffList(1, pageSize);
            ArrayList<String> ids = getIdsByPhones(staffList, phones);
            for (int i = 0; i < ids.size(); i++) {
                deleteStaff(ids.get(i));
            }
        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, caseName, "员工列表每页显示核查");
        }
    }

    @Test
    public void addChannelStaffTestPage() {
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        try {
            String channelId = "5";
            int pageSizeTemp = 10;

            ArrayList<String> phones = new ArrayList<>();

            for (int i = 0; i < 10; i++) {
                String phoneNum = genPhoneNum();
                addChannelStaff("change-test-" + i, channelId, phoneNum);
                JSONObject temp = channelStaffListReturnData(channelId, 1, pageSizeTemp);

                int totalPage = getTotalPage(temp);
                for (int j = 1; j <= totalPage; j++) {
                    JSONArray singlePage = channelStaffList(channelId, j, pageSizeTemp);

                    if (j < totalPage) {
                        if (singlePage.size() != 10) {
                            throw new Exception("渠道业务员列表，第【" + j + "】页不是最后一页，仅有【" + singlePage.size() + "】条记录。");
                        }
                    } else {
                        if (singlePage.size() == 0) {
                            throw new Exception("渠道业务员列表，第【" + j + "】页显示为空");
                        }
                    }
                }

                phones.add(phoneNum);
            }

            JSONArray staffList = channelStaffList(channelId, 1, pageSize);
            ArrayList<String> ids = getIdsByPhones(staffList, phones);

            for (int i = 0; i < ids.size(); i++) {
                changeChannelStaffState(ids.get(i));
            }
        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, caseName, "渠道业务员列表每页显示核查");
        }
    }

    @Test
    public void addChannelStaffWithPicCheck() {
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        try {

            String dirPath = "src/main/java/com/haisheng/framework/testng/bigScreen/feidanImages";

            String channelId = "5";

            File file = new File(dirPath);
            File[] files = file.listFiles();

            ArrayList<String> phones = new ArrayList<>();

//            只注册一张，用于测试用人脸注册渠道员工是否成功！
            for (int i = 0; i <= 1; i++) {

                String imagePath = dirPath + "/" + files[i].getName();

                imagePath = imagePath.replace("/", File.separator);

                JSONObject uploadImage = uploadImage(imagePath);

                String phoneNum = genPhoneNum();

                phones.add(phoneNum);

                addChannelStaffWithPic("staff-" + i, channelId, phoneNum, uploadImage.getString("face_url"));
            }

            JSONArray staffList = channelStaffList(channelId, 1, pageSize);
            ArrayList<String> ids = getIdsByPhones(staffList, phones);
            if (ids.size() == 0) {
                throw new Exception("用人脸注册渠道员工失败！");
            }
            for (int i = 0; i < ids.size(); i++) {
                changeChannelStaffState(ids.get(i));
            }
        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, caseName, "人脸注册渠道员工，期望成功");
        }
    }

    @Test
    public void addChannelTestPage() {
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        try {
            int pageSizeTemp = 10;

            for (int i = 0; i < 10; i++) {
                String phoneNum = genPhoneNum();
                addChannel("channel-" + System.currentTimeMillis(), "于老师", phoneNum, "test");
                JSONObject temp = channelListReturnData(1, pageSizeTemp);

                int totalPage = getTotalPage(temp);
                for (int j = 1; j <= totalPage; j++) {
                    JSONArray singlePage = channelList(j, pageSizeTemp);

                    if (j < totalPage) {
                        if (singlePage.size() != 10) {
                            throw new Exception("渠道列表，第【" + j + "】页不是最后一页，仅有【" + singlePage.size() + "】条记录。");
                        }
                    } else {
                        if (singlePage.size() == 0) {
                            throw new Exception("渠道列表，第【" + j + "】页显示为空");
                        }
                    }
                }
            }
        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, caseName, "渠道列表每页显示是否正常");
        }
    }

    @Test
    public void newCustomerTestPage() {
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        try {
            int pageSizeTemp = 10;
            String serachType = "CHANCE";

            for (int i = 0; i < 10; i++) {
                String phoneNum = genPhoneNum();
                newCustomer(channelId, gongErId, anShengId, phoneNum, "customer-testpage");
                JSONObject temp = customerListReturnData(serachType, 1, pageSizeTemp);

                int totalPage = getCustomerTotalPage(temp);
                for (int j = 1; j <= totalPage; j++) {
                    JSONArray singlePage = customerList(serachType, j, pageSizeTemp);
                    if (j < totalPage) {
                        if (singlePage.size() != 10) {
                            throw new Exception("机会顾客列表，第【" + j + "】页不是最后一页，仅有【" + singlePage.size() + "】条记录。");
                        }
                    } else {
                        if (singlePage.size() == 0) {
                            throw new Exception("机会顾客列表，第【" + j + "】页显示为空");
                        }
                    }
                }
            }
        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, caseName, "机会顾客列表每页显示是否正常");
        }
    }

    @Test
    public void initThenRegChannelStaffSamePic() {

        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        try {
            String dirPath = "src/main/java/com/haisheng/framework/testng/bigScreen/feidanForbid/changestate.jpg";

            String channelId = "5";
            String namePhone = "宫二";

//            保证业务员“宫二”处于启用状态
            JSONObject staffListWithPhone = channelStaffListWithPhone(channelId, namePhone, 1, pageSize);
            JSONObject single = staffListWithPhone.getJSONArray("list").getJSONObject(0);

            boolean isDelete = single.getBooleanValue("is_delete");

            if (isDelete) {
                changeChannelStaffState("12");
            }

//            新建一个相同人脸的业务员
            dirPath = dirPath.replace("/", File.separator);

            String faceUrl = uploadImage(dirPath).getString("face_url");

            String response = addChannelStaffWithPicRes("change-state-test", channelId, genPhoneNum(), faceUrl);
            checkCode(response, StatusCode.BAD_REQUEST, "添加业务员");

        } catch (AssertionError e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, caseName, "业务员处于启用状态，不能新建一个与此业务员相似人脸的业务员");
        }
    }

    @Test
    public void initThenRegStaffSamePic() {

        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        try {
            String dirPath = "src/main/java/com/haisheng/framework/testng/bigScreen/feidanForbid/changestate.jpg";

            String channelId = "5";
            String namePhone = "宫二";

//            保证业务员“宫二”处于启用状态
            JSONObject staffListWithPhone = channelStaffListWithPhone(channelId, namePhone, 1, pageSize);
            JSONObject single = staffListWithPhone.getJSONArray("list").getJSONObject(0);

            boolean isDelete = single.getBooleanValue("is_delete");

            if (isDelete) {
                changeChannelStaffState("12");
            }

//            新建一个相同人脸的业务员
            dirPath = dirPath.replace("/", File.separator);

            String faceUrl = uploadImage(dirPath).getString("face_url");

            String response = addStaffRes("change-state-test", getOneStaffType(), genPhoneNum(), faceUrl);
            checkCode(response, StatusCode.BAD_REQUEST, "添加业务员");

        } catch (AssertionError e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, caseName, "业务员处于启用状态，不能新建一个与此业务员相似人脸的售楼处员工");
        }
    }

    @Test
    public void initThenRegChannelStaffSamePhone() {

        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        try {
            String channelId = "5";
            String namePhone = "宫二";
            String phone = "17610248107";

//            保证业务员“宫二”处于启用状态
            JSONObject staffListWithPhone = channelStaffListWithPhone(channelId, namePhone, 1, pageSize);
            JSONObject single = staffListWithPhone.getJSONArray("list").getJSONObject(0);

            boolean isDelete = single.getBooleanValue("is_delete");

            if (isDelete) {
                changeChannelStaffState("12");
            }

//            新建一个相同手机号的业务员
            String response = addChannelStaffRes(caseName, channelId, phone);
            checkCode(response, StatusCode.BAD_REQUEST, "添加业务员");

        } catch (AssertionError e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, caseName, "业务员处于启用状态，不能新建一个与此业务员相同手机号的业务员");
        }
    }

    @Test
    public void initThenRegStaffSamePhone() {

        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        try {
            String dirPath = "src/main/java/com/haisheng/framework/testng/bigScreen/feidanForbid/changestate.jpg";

            String channelId = "5";
            String namePhone = "宫二";

            String phone = "17610248107";

//            保证业务员“宫二”处于启用状态
            JSONObject staffListWithPhone = channelStaffListWithPhone(channelId, namePhone, 1, pageSize);
            JSONObject single = staffListWithPhone.getJSONArray("list").getJSONObject(0);

            boolean isDelete = single.getBooleanValue("is_delete");

            if (isDelete) {
                changeChannelStaffState("12");
            }

//            新建一个相同手机号的售楼处员工
            dirPath = dirPath.replace("/", File.separator);

            String faceUrl = uploadImage(dirPath).getString("face_url");

            String response = addStaffRes(caseName, channelId, phone, faceUrl);
            checkCode(response, StatusCode.BAD_REQUEST, "添加售楼处员工");

        } catch (AssertionError e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, caseName, "业务员处于启用状态，不能新建一个与此业务员相同手机号的售楼处员工");
        }
    }

    @Test
    public void initThenEditChannelStaffSamePic() {

        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        try {
            String dirPath = "src/main/java/com/haisheng/framework/testng/bigScreen/feidanForbid/changestate.jpg";

            String channelId = "5";
            String namePhone = "宫二";

//            保证业务员“宫二”处于启用状态
            JSONObject staffListWithPhone = channelStaffListWithPhone(channelId, namePhone, 1, pageSize);
            JSONObject single = staffListWithPhone.getJSONArray("list").getJSONObject(0);

            boolean isDelete = single.getBooleanValue("is_delete");

            if (isDelete) {
                changeChannelStaffState("12");
            }

//           编辑一个业务员，用宫二的图片
            dirPath = dirPath.replace("/", File.separator);

            String faceUrl = uploadImage(dirPath).getString("face_url");


//            这是一个已经建好的业务员，phone：17771434896，id：733
            String id = "733";
            String phone = "17771434896";
            String name = "测试【勿动】";
            String response = editChannelStaffPic(id, name, channelId, phone, faceUrl);

            checkCode(response, StatusCode.BAD_REQUEST, "编辑业务员");

        } catch (AssertionError e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, caseName, "业务员处于启用状态，不能编辑另一业务员为相似人脸");
        }
    }

    @Test
    public void initThenEditStaffSamePic() {

        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        try {
            String dirPath = "src/main/java/com/haisheng/framework/testng/bigScreen/feidanForbid/changestate.jpg";

            String channelId = "5";
            String namePhone = "宫二";

//            保证业务员“宫二”处于启用状态
            JSONObject staffListWithPhone = channelStaffListWithPhone(channelId, namePhone, 1, pageSize);
            JSONObject single = staffListWithPhone.getJSONArray("list").getJSONObject(0);

            boolean isDelete = single.getBooleanValue("is_delete");

            if (isDelete) {
                changeChannelStaffState("12");
            }

//           编辑一个业务员，用宫二的图片
            dirPath = dirPath.replace("/", File.separator);

            String faceUrl = uploadImage(dirPath).getString("face_url");

//            这是一个已经建好的业务员
            String id = "15";
            String phone = "16622222222";
            String name = "安生";
            String staffType = "PROPERTY_CONSULTANT";
            String response = editStaffRes(id, name, staffType, phone, faceUrl);

            checkCode(response, StatusCode.BAD_REQUEST, "编辑售楼处员工");

        } catch (AssertionError e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, caseName, "业务员处于启用状态，不能编辑另一售楼处员工为相似人脸");
        }
    }

    @Test
    public void initThenEditChannelStaffSamePhone() {

        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        try {
            String channelId = "5";
            String namePhone = "宫二";

//            保证业务员“宫二”处于启用状态
            JSONObject staffListWithPhone = channelStaffListWithPhone(channelId, namePhone, 1, pageSize);
            JSONObject single = staffListWithPhone.getJSONArray("list").getJSONObject(0);

            boolean isDelete = single.getBooleanValue("is_delete");

            if (isDelete) {
                changeChannelStaffState("12");
            }

//           编辑一个业务员，用宫二的手机号

//            这是一个已经建好的业务员
            String id = "733";
            String phone = "17610248107";
            String name = "测试【勿动】";
            String response = editChannelStaffPhone(id, name, channelId, phone);

            checkCode(response, StatusCode.BAD_REQUEST, "编辑业务员");

        } catch (AssertionError e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, caseName, "业务员处于启用状态，不能编辑另一业务员为相同手机号");
        }
    }

    @Test
    public void initThenEditStaffSamePhone() {

        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        try {
            String dirPath = "src/main/java/com/haisheng/framework/testng/bigScreen/feidanForbid/ansheng.jpg";

            String channelId = "5";
            String namePhone = "宫二";

//            保证业务员“宫二”处于启用状态
            JSONObject staffListWithPhone = channelStaffListWithPhone(channelId, namePhone, 1, pageSize);
            JSONObject single = staffListWithPhone.getJSONArray("list").getJSONObject(0);

            boolean isDelete = single.getBooleanValue("is_delete");

            if (isDelete) {
                changeChannelStaffState("12");
            }

//           编辑一个业务员，用宫二的手机号
            dirPath = dirPath.replace("/", File.separator);

            String faceUrl = uploadImage(dirPath).getString("face_url");

//            这是一个已经建好的业务员
            String id = "15";
            String phone = "17610248107";
            String name = "安生";
            String staffType = "PROPERTY_CONSULTANT";
            String response = editStaffRes(id, name, staffType, phone, faceUrl);

            checkCode(response, StatusCode.BAD_REQUEST, "编辑售楼处员工");

        } catch (AssertionError e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, caseName, "业务员处于启用状态，不能编辑另一售楼处员工为相同手机号");
        }
    }

    @Test
    public void initThenInitChannelStaffSamePhone() {

        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        try {
            String channelId = "5";
            String namePhone = "宫二";

//            保证业务员“宫二”处于启用状态
            JSONObject staffListWithPhone = channelStaffListWithPhone(channelId, namePhone, 1, pageSize);
            JSONObject single = staffListWithPhone.getJSONArray("list").getJSONObject(0);

            boolean isDelete = single.getBooleanValue("is_delete");

            if (isDelete) {
                changeChannelStaffState("12");
            }

//           编辑一个业务员，用宫二的手机号

//            这是一个已经建好的业务员
            String id = "533";
//            String name = "相同手机号【勿动】";
            String response = changeChannelStaffStateRes(id);

            checkCode(response, StatusCode.BAD_REQUEST, "启动业务员");

        } catch (AssertionError e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, caseName, "业务员处于启用状态，不能启动另一有相同人脸图片的业务员");
        }
    }

    @Test
    public void initThenInitChannelStaffSamePic() {

        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        try {

            String channelId = "5";
            String namePhone = "宫二";

//            保证业务员“宫二”处于启用状态
            JSONObject staffListWithPhone = channelStaffListWithPhone(channelId, namePhone, 1, pageSize);
            JSONObject single = staffListWithPhone.getJSONArray("list").getJSONObject(0);

            boolean isDelete = single.getBooleanValue("is_delete");

            if (isDelete) {
                changeChannelStaffState("12");
            }

//            启动一个已经相同人脸图片的业务员，这是一个已经建好的业务员，phone：17771434896，id：751
            String id = "751";
//            String phone = "17708829844";
//            String name = "change-state-test";
            String response = changeChannelStaffStateRes(id);

            checkCode(response, StatusCode.BAD_REQUEST, "启动业务员");

        } catch (AssertionError e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, caseName, "业务员处于启用状态，不能启动另一有相同手机号的业务员");
        }
    }

    @Test
    public void forbidThenRegChannelStaffSamePic() {

        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        try {
            String dirPath = "src/main/java/com/haisheng/framework/testng/bigScreen/feidanForbid/makun.jpg";

            String channelId = "5";
            String namePhone = "马锟";
            String id = "47";

//            保证业务员“马锟”处于禁用状态
            JSONObject staffListWithPhone = channelStaffListWithPhone(channelId, namePhone, 1, pageSize);
            JSONObject single = staffListWithPhone.getJSONArray("list").getJSONObject(0);

            boolean isDelete = single.getBooleanValue("is_delete");

            if (!isDelete) {
                changeChannelStaffState(id);
            }

//            新建一个相同人脸的业务员
            dirPath = dirPath.replace("/", File.separator);

            String faceUrl = uploadImage(dirPath).getString("face_url");

            String phone = genPhoneNum();
            String response = addChannelStaffWithPicRes(caseName, channelId, phone, faceUrl);
            checkCode(response, StatusCode.SUCCESS, "添加业务员");

            JSONObject data = channelStaffListWithPhone(channelId, phone, 1, pageSize);

            String idNew = getIdByPhone(data.getJSONArray("list"), phone);

            changeChannelStaffState(idNew);
        } catch (AssertionError e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, caseName, "业务员处于禁用状态，可以新建一个与此业务员相似人脸的业务员");
        }
    }

    @Test
    public void forbidThenRegStaffSamePic() {

        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        try {
            String dirPath = "src/main/java/com/haisheng/framework/testng/bigScreen/feidanForbid/makun.jpg";

            String channelId = "5";
            String namePhone = "马锟";
            String id = "47";

//            保证业务员“马锟”处于禁用状态
            JSONObject staffListWithPhone = channelStaffListWithPhone(channelId, namePhone, 1, pageSize);
            JSONObject single = staffListWithPhone.getJSONArray("list").getJSONObject(0);

            boolean isDelete = single.getBooleanValue("is_delete");

            if (!isDelete) {
                changeChannelStaffState(id);
            }

//            新建一个相同人脸的售楼处员工
            dirPath = dirPath.replace("/", File.separator);

            String faceUrl = uploadImage(dirPath).getString("face_url");
            String phone = genPhoneNum();

            String response = addStaffRes(caseName, getOneStaffType(), phone, faceUrl);
            checkCode(response, StatusCode.SUCCESS, "添加售楼处员工");

            JSONArray staffList = staffList(1, pageSize);

            String idNew = getIdByPhone(staffList, phone);

            deleteStaff(idNew);

        } catch (AssertionError e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, caseName, "业务员处于禁用状态，可以新建一个与此业务员相似人脸的售楼处员工");
        }
    }

    @Test
    public void forbidThenRegChannelStaffSamePhone() {

        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        try {
            String channelId = "5";
            String namePhone = "马锟";
            String phone = "16567676767";
            String id = "47";

//            保证业务员“马锟”处于禁用状态
            JSONObject staffListWithPhone = channelStaffListWithPhone(channelId, namePhone, 1, pageSize);
            JSONObject single = staffListWithPhone.getJSONArray("list").getJSONObject(0);

            boolean isDelete = single.getBooleanValue("is_delete");

            if (!isDelete) {
                changeChannelStaffState(id);
            }

//            新建一个相同手机号的业务员
            String response = addChannelStaffRes(caseName, channelId, phone);
            checkCode(response, StatusCode.SUCCESS, "添加业务员");

            JSONObject data = channelStaffListWithPhone(channelId, phone, 1, pageSize);

            String idNew = getIdByPhoneAndStatus(data.getJSONArray("list"), phone, false);

            changeChannelStaffState(idNew);

        } catch (AssertionError e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, caseName, "业务员处于禁用状态，可以新建一个与此业务员相同手机号的业务员");
        }
    }

    @Test
    public void forbidThenRegStaffSamePhone() {

        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        try {
            String dirPath = "src/main/java/com/haisheng/framework/testng/bigScreen/feidanForbid/makun.jpg";

            String channelId = "5";
            String namePhone = "马锟";
            String maphone = "16567676767";
            String id = "47";

//            保证业务员“马锟”处于禁用状态
            JSONObject staffListWithPhone = channelStaffListWithPhone(channelId, namePhone, 1, pageSize);
            JSONObject single = staffListWithPhone.getJSONArray("list").getJSONObject(0);

            boolean isDelete = single.getBooleanValue("is_delete");

            if (!isDelete) {
                changeChannelStaffState(id);
            }
//            新建一个相同手机号的售楼处员工
            dirPath = dirPath.replace("/", File.separator);

            String faceUrl = uploadImage(dirPath).getString("face_url");
            String phone = genPhoneNum();

            String response = addStaffRes(caseName, getOneStaffType(), phone, faceUrl);
            checkCode(response, StatusCode.SUCCESS, "添加售楼处员工");

            JSONArray staffList = staffList(1, pageSize);

            String idNew = getIdByPhone(staffList, phone);

            deleteStaff(idNew);

        } catch (AssertionError e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, caseName, "业务员处于禁用状态，可以新建一个与此业务员相同手机号的售楼处员工");
        }
    }

    @Test(dataProvider = "DEAL_PHONE")
    public void orderFirstAppearTimeEquals(String phone, String idCard, String customerName, String firstAppearTime) {
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName() + "-" + phone;

        try {
//            创建订单
            JSONObject result = createOrder(idCard, phone, "DEAL");
            String orderId = JSONPath.eval(result, "$.data.order_id").toString();

            // 查询订单
            Long firstAppearTimeL = orderDetail(orderId).getLong("first_appear_time");

            String firstAppearTimeA = dateTimeUtil.timestampToDate("yyyy-MM-dd HH:mm:ss", firstAppearTimeL);

            if (!firstAppearTime.equals(firstAppearTimeA)) {
                throw new Exception("订单顾客姓名【" + customerName + "】，手机号【" + phone + "】，订单id【" + orderId + "】首次到访时间：【" +
                        firstAppearTimeA + "】，最初订单的首次到访时间：【" + firstAppearTime + "】");
            }
        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, caseName, "同一个人的不同订单的首次到访时间是否一致");
        }
    }

    @Test
    public void adviserFreezeAfterDeal() {
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        try {
            // 创建订单
            JSONObject result = createOrder("666666666666666666", "12111111115", "SIGN");
            String orderId = JSONPath.eval(result, "$.data.order_id").toString();

            // 查询订单
            JSONObject resultB = orderDetail(orderId);

            String adviserNameB = resultB.getString("adviser_name");

//            更改置业顾问
            String cid = "REGISTER-8d69f6ed-7824-48c7-9350-7b3a1d87c791";
            String zhangZhenId = "11";
            String jinChengWuId = "6";

            if ("张震".equals(adviserNameB)) {
                customerEdit(cid, jinChengWuId);
                String adviserNameA = orderDetail(orderId).getString("adviser_name");
                if (!"张震".equals(adviserNameA)) {
                    throw new Exception("成单置业顾问改变，变更前【" + adviserNameB + "】，变更后【" + adviserNameA + "】");
                }
            } else {
                customerEdit(cid, zhangZhenId);
                String adviserNameA = orderDetail(orderId).getString("adviser_name");
                if (!"金城武".equals(adviserNameA)) {
                    throw new Exception("成单置业顾问改变，变更前【" + adviserNameB + "】，变更后【" + adviserNameA + "】");
                }
            }

//            校验异常环节
            checkRiskStep(orderId);

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, caseName, "更改置业顾问，成单置业顾问不变");
        }
    }

    @Test
    public void normalOrderTimeTest() {
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        try {
            // 创建订单
            String phone = "12111111119";
            JSONObject result = createOrder("111111111111111113", phone, "SIGN");
            String orderId = JSONPath.eval(result, "$.data.order_id").toString();

            // 查询订单
            JSONObject resultB = orderDetail(orderId);
            Long firstAppearTime = resultB.getLong("first_appear_time");
//            dateTimeUtil.timestampToDate("yyyy-MM-dd HH:mm:ss",firstAppearTime)
            Long reportTime = resultB.getLong("report_time");
            Long signTime = resultB.getLong("sign_time");

            if (firstAppearTime < reportTime) {
                throw new Exception("正常订单，手机号【" + phone + "】，订单号【" + orderId + "】，首次到访时间【" +
                        firstAppearTime + "】早于报备时间【" + reportTime + "】");
            }

            if (reportTime > signTime) {
                throw new Exception("正常订单，手机号【" + phone + "】，订单号【" + orderId + "】，报备时间【" +
                        reportTime + "】晚于签约时间【" + signTime + "】");
            }
        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, caseName, "正常订单的首次出现时间<报备时间<签约时间");
        }
    }

    @Test
    public void riskOrderTimeTest() {
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        try {
            // 创建订单
            String phone = "12111111311";
            JSONObject result = createOrder("111111111111111115", phone, "SIGN");
            String orderId = JSONPath.eval(result, "$.data.order_id").toString();

            // 查询订单
            JSONObject resultB = orderDetail(orderId);
            Long firstAppearTime = resultB.getLong("first_appear_time");
//            dateTimeUtil.timestampToDate("yyyy-MM-dd HH:mm:ss",firstAppearTime)
            Long reportTime = resultB.getLong("report_time");

            if (firstAppearTime > reportTime) {
                throw new Exception("风险订单，手机号【" + phone + "】，订单号【" + orderId + "】，首次到访时间【" +
                        firstAppearTime + "】晚于报备时间【" + reportTime + "】");
            }
        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, caseName, "正常订单的首次出现时间<报备时间<签约时间");
        }
    }

    @Test
    public void orderListRank() {
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        try {

            // 订单列表
            JSONArray jsonArray = orderList(1, pageSize);
            checkRank(jsonArray, "customer_phone", "订单列表>>>");

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, caseName, "订单列表按照新建时间倒排");
        }
    }

    @Test
    public void staffListRank() {
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        try {

            // 员工列表
            JSONArray jsonArray = staffList(1, pageSize);
            checkRank(jsonArray, "phone", "员工列表>>>");

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, caseName, "员工列表按照新建时间倒排");
        }
    }

    @Test
    public void channelListRank() {
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        try {

            // 渠道列表
            JSONArray jsonArray = channelList(1, pageSize);
            checkRank(jsonArray, "phone", "渠道列表>>>");
        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, caseName, "渠道列表按照新建时间倒排");
        }
    }

    @Test
    public void channelStaffListRank() {
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        try {

            // 渠道列表
            JSONArray jsonArray = channelStaffList(channelId, 1, pageSize);
            checkRank(jsonArray, "phone", "渠道员工列表>>>");
        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, caseName, "渠道员工列表按照新建时间倒排");
        }
    }

    @Test
    public void registerQrCodeNotNull() {
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        try {

            JSONObject data = registerQrCode();

            String qrcode = data.getString("qrcode");
            if (qrcode == null || "".equals(qrcode.trim())) {
                throw new Exception("案场二维码中【qrcode】为空！");
            }
            String url = data.getString("url");
            if (url == null || "".equals(url.trim())) {
                throw new Exception("案场二维码中【url】为空！");
            }


        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, caseName, "渠道员工列表按照新建时间倒排");
        }
    }

    private void checkRank(JSONArray list, String key, String function) throws Exception {
        for (int i = 0; i < list.size() - 1; i++) {
            JSONObject singleB = list.getJSONObject(i);
            long gmtCreateB = singleB.getLongValue("gmt_create");
            JSONObject singleA = list.getJSONObject(i + 1);
            long gmtCreateA = singleA.getLongValue("gmt_create");

            if (gmtCreateB < gmtCreateA) {
                String phoneB = singleB.getString(key);
                String phoneA = singleA.getString(key);

                throw new Exception(function + "没有按照创建时间倒排！前一条,phone:【" + phoneB + ",gmt_create【" + gmtCreateB +
                        "】，后一条phone【" + phoneA + ",gmt_create【" + gmtCreateA + "】");
            }
        }
    }

    public int getChannelStaffReportNum(JSONArray list) {
        int reportNum = 0;

        for (int i = 0; i < list.size(); i++) {
            JSONObject singleStaff = list.getJSONObject(i);
            String staffId = singleStaff.getString("id");
            if ("12".equals(staffId)) {//宫二的员工id
                reportNum = singleStaff.getInteger("total_report");
            }
        }

        return reportNum;
    }

    public void compareOrderTimeValue(JSONObject data, String key, String orderId, String valueExpect, String function1, String function2) throws Exception {
        String valueStr = data.getString(key);
        if (valueStr != null && !"".equals(valueStr)) {
            String firstStr = dateTimeUtil.timestampToDate("yyyy-MM-dd HH:mm:ss", Long.valueOf(valueStr));
            if (!firstStr.equals(valueExpect)) {
                throw new Exception("订单id：" + orderId + ",【" + key + "】在" + function1 + "中是：" + valueExpect + ",在" + function2 + "中是：" + firstStr);
            }
        }
    }

    public void compareValue(JSONObject data, String function, String cid, String key, String valueExpect, String comment) throws Exception {

        String value = getValue(data, key);

        if (!valueExpect.equals(value)) {
            throw new Exception(function + "id：" + cid + ",列表中" + comment + "：" + valueExpect + ",详情中：" + value);
        }
    }

    public String getValue(JSONObject data, String key) {
        String value = data.getString(key);

        if (value == null || "".equals(value)) {
            value = "";
        }

        return value;
    }

//    --------------------------------------------------------接口方法-------------------------------------------------------

    public JSONObject addChannel(String channelName, String owner, String phone, String contractCode) throws Exception {
        String json = StrSubstitutor.replace(ADD_CHANNEL_JSON, ImmutableMap.builder()
                .put("shopId", getShopId())
                .put("channelName", channelName)
                .put("owner", owner)
                .put("phone", phone)
                .put("contractCode", contractCode)
                .build()
        );
        String res = httpPostWithCheckCode(ADD_CHANNEL, json, new String[0]);

        return JSON.parseObject(res).getJSONObject("data");
    }

    public ArrayList<String> getIdsByPhones(JSONArray staffList, ArrayList<String> phones) throws Exception {
        ArrayList<String> ids = new ArrayList<>();
        for (int i = 0; i < phones.size(); i++) {
            String id = getIdByPhone(staffList, phones.get(i));
            ids.add(id);
        }

        return ids;
    }

    public String getIdByPhone(JSONArray staffList, String phone) throws Exception {
        String id = "";
        for (int j = 0; j < staffList.size(); j++) {
            JSONObject singleStaff = staffList.getJSONObject(j);
            String phoneRes = singleStaff.getString("phone");
            if (phone.equals(phoneRes)) {
                id = singleStaff.getString("id");
            }
        }

        return id;
    }

    public String getIdByPhoneAndStatus(JSONArray staffList, String phone, boolean status) throws Exception {
        String id = "";
        for (int j = 0; j < staffList.size(); j++) {
            JSONObject singleStaff = staffList.getJSONObject(j);
            String phoneRes = singleStaff.getString("phone");
            boolean isDelete = singleStaff.getBooleanValue("is_delete");
            if (phone.equals(phoneRes) && (isDelete == status)) {
                id = singleStaff.getString("id");
            }
        }

        return id;
    }


    public String editChannelStaffPic(String staffId, String staffName, String channelId, String phone, String faceUrl) throws Exception {
        String json = StrSubstitutor.replace(EDIT_CHANNEL_STAFF_WITH_PIC_JSON, ImmutableMap.builder()
                .put("shopId", getShopId())
                .put("staffName", staffName)
                .put("channelId", channelId)
                .put("phone", phone)
                .put("faceUrl", faceUrl)
                .build()
        );
        String res = httpPost(EDIT_CHANNEL_STAFF + staffId, json, new String[0]);

        JSONObject result = JSON.parseObject(res);
        int codeRes = result.getInteger("code");
        String message = result.getString("message");

        if (codeRes == 1001) {
            if ("当前手机号已被使用".equals(message)) {
                phone = genPhoneNum();
                res = editChannelStaffPic(staffId, staffName, channelId, phone, faceUrl);
            }
        }

        return res;
    }

    public String editChannelStaffPhone(String staffId, String staffName, String channelId, String phone) throws Exception {
        String json = StrSubstitutor.replace(EDIT_CHANNEL_STAFF_JSON, ImmutableMap.builder()
                .put("shopId", getShopId())
                .put("staffName", staffName)
                .put("channelId", channelId)
                .put("phone", phone)
                .build()
        );
        String res = httpPost(EDIT_CHANNEL_STAFF + staffId, json, new String[0]);

        return res;
    }

    public JSONObject addChannelStaff(String staffName, String channelId, String phone) throws Exception {
        String json = StrSubstitutor.replace(ADD_CHANNEL_STAFF_JSON, ImmutableMap.builder()
                .put("shopId", getShopId())
                .put("staffName", staffName)
                .put("channelId", channelId)
                .put("phone", phone)
                .build()
        );
        String res = httpPostWithCheckCode(ADD_CHANNEL_STAFF, json, new String[0]);

        JSONObject result = JSON.parseObject(res);
        int codeRes = result.getInteger("code");
        String message = result.getString("message");

        if (codeRes == 1001) {
            if ("当前手机号已被使用".equals(message)) {
                phone = genPhoneNum();
                addChannelStaff(staffName, channelId, phone);
            } else {
                String id = getIdOfStaff(res);

                if (!"".equals(id)) {
                    changeChannelStaffState(id);
                    deleteStaff(id);
                    addChannelStaff(staffName, channelId, phone);
                }
            }
        }

        return JSON.parseObject(res).getJSONObject("data");
    }

    public String addChannelStaffRes(String staffName, String channelId, String phone) throws Exception {
        String json = StrSubstitutor.replace(ADD_CHANNEL_STAFF_JSON, ImmutableMap.builder()
                .put("shopId", getShopId())
                .put("staffName", staffName)
                .put("channelId", channelId)
                .put("phone", phone)
                .build()
        );
        String res = httpPost(ADD_CHANNEL_STAFF, json, new String[0]);

        return res;
    }

    public JSONObject addChannelStaffWithPic(String staffName, String channelId, String phone, String pic) throws Exception {
        String json = StrSubstitutor.replace(ADD_CHANNEL_STAFF_WITH_PIC_JSON, ImmutableMap.builder()
                .put("shopId", getShopId())
                .put("staffName", staffName)
                .put("channelId", channelId)
                .put("phone", phone)
                .put("faceUrl", pic)
                .build()
        );
        String res = httpPost(ADD_CHANNEL_STAFF, json, new String[0]);

        JSONObject result = JSON.parseObject(res);
        int codeRes = result.getInteger("code");
        String message = result.getString("message");

        if (codeRes == 1001) {
            if ("当前手机号已被使用".equals(message)) {
                phone = genPhoneNum();
                addChannelStaffWithPic(staffName, channelId, phone, pic);
            } else {
                String id = getIdOfStaff(res);

                if (!"".equals(id)) {
                    deleteStaff(id);
                    changeChannelStaffState(id);
                    addChannelStaffWithPic(staffName, channelId, phone, pic);
                }
            }
        }

        return JSON.parseObject(res).getJSONObject("data");
    }

    public String addChannelStaffWithPicRes(String staffName, String channelId, String phone, String pic) throws Exception {
        String json = StrSubstitutor.replace(ADD_CHANNEL_STAFF_WITH_PIC_JSON, ImmutableMap.builder()
                .put("shopId", getShopId())
                .put("staffName", staffName)
                .put("channelId", channelId)
                .put("phone", phone)
                .put("faceUrl", pic)
                .build()
        );
        String res = httpPost(ADD_CHANNEL_STAFF, json, new String[0]);

        return res;
    }

    public String getIdOfStaff(String res) {

        JSONObject resJo = JSON.parseObject(res);

        Integer resCode = resJo.getInteger("code");

        String id = "";

        if (resCode == StatusCode.BAD_REQUEST) {

            String message = resJo.getString("message");

            id = message.substring(message.indexOf(":") + 1, message.indexOf(")")).trim();
        }

        return id;
    }

    public int getTotalPage(JSONObject data) {
        return data.getInteger("pages");
    }

    public int getCustomerTotalPage(JSONObject data) {
        double total = data.getDoubleValue("total");

        return (int) Math.ceil(total / (double) 10.0);
    }

    public String getOneStaffType() throws Exception {
        JSONArray staffTypeList = staffTypeList();
        Random random = new Random();
        return staffTypeList.getJSONObject(random.nextInt(3)).getString("staff_type");
    }

    public JSONObject uploadImage(String imagePath) {
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

            builder.addTextBody("path", "shopStaff", ContentType.TEXT_PLAIN);

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

    public JSONObject addStaff(String staffName, String staffType, String phone, String faceUrl) throws Exception {
        String json = StrSubstitutor.replace(ADD_STAFF_JSON, ImmutableMap.builder()
                .put("shopId", getShopId())
                .put("staffName", staffName)
                .put("staffType", staffType)
                .put("phone", phone)
                .put("faceUrl", faceUrl)
                .build()
        );
        String res = httpPost(ADD_STAFF, json, new String[0]);

        JSONObject result = JSON.parseObject(res);
        int codeRes = result.getInteger("code");
        String message = result.getString("message");
        if (codeRes == 1001) {
            if ("当前手机号已被使用".equals(message)) {
                phone = genPhoneNum();
                addStaff(staffName, staffType, phone, faceUrl);
            } else {
                String id = getIdOfStaff(res);

                if (!"".equals(id)) {
                    deleteStaff(id);
                    addStaff(staffName, staffType, phone, faceUrl);
                }
            }
        }

        return JSON.parseObject(res).getJSONObject("data");
    }

    public String editStaffRes(String id, String staffName, String staffType, String phone, String faceUrl) throws Exception {
        String json = StrSubstitutor.replace(EDIT_STAFF_JSON, ImmutableMap.builder()
                .put("shopId", getShopId())
                .put("staffName", staffName)
                .put("staffType", staffType)
                .put("phone", phone)
                .put("faceUrl", faceUrl)
                .build()
        );
        String res = httpPost(EDIT_STAFF + id, json, new String[0]);

        return res;
    }

    public String addStaffRes(String staffName, String staffType, String phone, String faceUrl) throws Exception {
        String json = StrSubstitutor.replace(ADD_STAFF_JSON, ImmutableMap.builder()
                .put("shopId", getShopId())
                .put("staffName", staffName)
                .put("staffType", staffType)
                .put("phone", phone)
                .put("faceUrl", faceUrl)
                .build()
        );
        String res = httpPost(ADD_STAFF, json, new String[0]);

        return res;
    }

    public void deleteStaff(String staffId) throws Exception {
        String json = "{}";

        httpPostWithCheckCode(DELETE_STAFF + staffId, json, new String[0]);
    }

    public void changeChannelStaffState(String staffId) throws Exception {
        String json = "{}";

        httpPostWithCheckCode("/risk/channel/staff/state/change/" + staffId, json, new String[0]);
    }

    public String changeChannelStaffStateRes(String staffId) throws Exception {
        String json = "{}";

        String response = httpPost("/risk/channel/staff/state/change/" + staffId, json, new String[0]);

        return response;
    }

    public JSONArray channelList(int page, int pageSize) throws Exception {
        return channelListReturnData(page, pageSize).getJSONArray("list");
    }

    public JSONObject channelListReturnData(int page, int pageSize) throws Exception {
        String json = StrSubstitutor.replace(CHANNEL_LIST_JSON, ImmutableMap.builder()
                .put("shopId", getShopId())
                .put("page", page)
                .put("pageSize", pageSize)
                .build()
        );
        String res = httpPostWithCheckCode(CHANNEL_LIST, json, new String[0]);

        return JSON.parseObject(res).getJSONObject("data");
    }

    public JSONArray channelStaffList(String channelId, int page, int pageSize) throws Exception {

        return channelStaffListReturnData(channelId, page, pageSize).getJSONArray("list");
    }

    public JSONObject channelStaffListReturnData(String channelId, int page, int pageSize) throws Exception {
        String json = StrSubstitutor.replace(CHANNEL_STAFF_LIST_JSON, ImmutableMap.builder()
                .put("shopId", getShopId())
                .put("channelId", channelId)
                .put("page", page)
                .put("pageSize", pageSize)
                .build()
        );
        String res = httpPostWithCheckCode(CHANNEL_STAFF_PAGE, json, new String[0]);
        return JSON.parseObject(res).getJSONObject("data");
    }

    public JSONObject channelStaffListWithPhone(String channelId, String namePhone, int page, int pageSize) throws Exception {
        String json = StrSubstitutor.replace(CHANNEL_STAFF_LIST_PHOEN_JSON, ImmutableMap.builder()
                .put("shopId", getShopId())
                .put("channelId", channelId)
                .put("namePhone", namePhone)
                .put("page", page)
                .put("pageSize", pageSize)
                .build()
        );
        String res = httpPostWithCheckCode(CHANNEL_STAFF_PAGE, json, new String[0]);
        return JSON.parseObject(res).getJSONObject("data");
    }

    public JSONArray customerList(String searchType, int page, int pageSize) throws Exception {
        return customerListReturnData(searchType, page, pageSize).getJSONArray("list");
    }

    public JSONObject customerListReturnData(String searchType, int page, int pageSize) throws Exception {
        String json = StrSubstitutor.replace(
                CUSTOMER_LIST_JSON, ImmutableMap.builder()
                        .put("shopId", getShopId())
                        .put("searchType", searchType)
                        .put("page", page)
                        .put("pageSize", pageSize)
                        .build()
        );

        String res = httpPostWithCheckCode(CUSTOMER_LIST, json, new String[0]);

        return JSON.parseObject(res).getJSONObject("data");
    }

    public JSONArray customerListWithChannel(String searchType, String channelId, int page, int pageSize) throws Exception {
        String json = StrSubstitutor.replace(CUSTOMER_LIST_WITH_CHANNEL_JSON, ImmutableMap.builder()
                .put("shopId", getShopId())
                .put("searchType", searchType)
                .put("channelId", channelId)
                .put("page", page)
                .put("pageSize", pageSize)
                .build()
        );
        String res = httpPostWithCheckCode(CUSTOMER_LIST, json, new String[0]);

        return JSON.parseObject(res).getJSONObject("data").getJSONArray("list");
    }

    public JSONArray customerAppearList(String cid, String startTime, String endTime) throws Exception {
        String json = StrSubstitutor.replace(
                CUSTOMER_APPEAR_LIST_JSON, ImmutableMap.builder()
                        .put("shopId", getShopId())
                        .put("cid", cid)
                        .put("startTime", startTime)
                        .put("endTime", endTime)
                        .build()
        );

        String res = httpPostWithCheckCode(CUSTOMER_APPEAR_LIST, json, new String[0]);

        return JSON.parseObject(res).getJSONObject("data").getJSONArray("list");
    }

    public JSONObject customerDetail(String cid) throws Exception {
        String json = StrSubstitutor.replace(
                CUSTOMER_DETAIL_JSON, ImmutableMap.builder()
                        .put("shopId", getShopId())
                        .put("cid", cid)
                        .build()
        );

        String res = httpPostWithCheckCode(CUSTOMER_DETAIL, json, new String[0]);

        return JSON.parseObject(res).getJSONObject("data");
    }

    public JSONObject customerEdit(String cid, String adviserId) throws Exception {
        String json =
                "{\n" +
                        "    \"customer_name\":\"更改置业顾问\",\n" +
                        "    \"phone\":\"12111111115\",\n" +
                        "    \"adviser_id\":" + adviserId + ",\n" +
                        "    \"shop_id\":" + getShopId() +
                        "}";

        String res = httpPostWithCheckCode(CUSTOMER_DETAIL, json, new String[0]);

        return JSON.parseObject(res).getJSONObject("data");
    }

    public JSONArray orderList(int page, int pageSize) throws Exception {
        String json = StrSubstitutor.replace(ORDER_LIST_JSON, ImmutableMap.builder()
                .put("shopId", getShopId())
                .put("page", page)
                .put("pageSize", pageSize)
                .build()
        );
        String[] checkColumnNames = {};
        String res = httpPostWithCheckCode(ORDER_LIST, json, checkColumnNames);

        return JSON.parseObject(res).getJSONObject("data").getJSONArray("list");
    }

    public JSONArray orderListWithStatus(String status, int page, int pageSize) throws Exception {
        String json = StrSubstitutor.replace(ORDER_LIST_WITH_STATUS_JSON, ImmutableMap.builder()
                .put("shopId", getShopId())
                .put("status", status)
                .put("page", page)
                .put("pageSize", pageSize)
                .build()
        );

        String res = httpPostWithCheckCode(ORDER_LIST, json, new String[0]);

        return JSON.parseObject(res).getJSONObject("data").getJSONArray("list");
    }

    public JSONArray orderListWithChannel(String channelId, int page, int pageSize) throws Exception {
        String json = StrSubstitutor.replace(ORDER_LIST_WITH_CHANNEL_JSON, ImmutableMap.builder()
                .put("shopId", getShopId())
                .put("channelId", channelId)
                .put("page", page)
                .put("pageSize", pageSize)
                .build()
        );
        String res = httpPostWithCheckCode(ORDER_LIST, json, new String[0]);

        return JSON.parseObject(res).getJSONObject("data").getJSONArray("list");
    }

    public JSONArray orderListWithPhone(String customerName, int page, int pageSize) throws Exception {
        String json = StrSubstitutor.replace(ORDER_LIST_WITH_PHONE_JSON, ImmutableMap.builder()
                .put("shopId", getShopId())
                .put("customerName", customerName)
                .put("page", page)
                .put("pageSize", pageSize)
                .build()
        );
        String res = httpPostWithCheckCode(ORDER_LIST, json, new String[0]);

        return JSON.parseObject(res).getJSONObject("data").getJSONArray("list");
    }

    public JSONArray staffList(int page, int pageSize) throws Exception {
        return staffListReturnData(page, pageSize).getJSONArray("list");
    }

    public JSONObject staffListReturnData(int page, int pageSize) throws Exception {
        String json = StrSubstitutor.replace(STAFF_LIST_JSON, ImmutableMap.builder()
                .put("shopId", getShopId())
                .put("page", page)
                .put("pageSize", pageSize)
                .build()
        );

        String res = httpPostWithCheckCode(STAFF_LIST, json, new String[0]);

        return JSON.parseObject(res).getJSONObject("data");
    }

    public JSONArray staffListWithType(String staffType, int page, int pageSize) throws Exception {
        String json = StrSubstitutor.replace(STAFF_LIST_WITH_TYPE_JSON, ImmutableMap.builder()
                .put("shopId", getShopId())
                .put("staffType", staffType)
                .put("page", page)
                .put("pageSize", pageSize)
                .build()
        );

        String res = httpPostWithCheckCode(STAFF_LIST, json, new String[0]);

        return JSON.parseObject(res).getJSONObject("data").getJSONArray("list");
    }

    public JSONArray staffTypeList() throws Exception {
        String json = StrSubstitutor.replace(STAFF_TYPE_LIST_JSON, ImmutableMap.builder()
                .put("shopId", getShopId())
                .build()
        );

        String res = httpPostWithCheckCode(STAFF_TYPE_LIST, json, new String[0]);

        return JSON.parseObject(res).getJSONObject("data").getJSONArray("list");
    }

    public String genPhoneNum() {
        Random random = new Random();
        long num = 17700000000L + random.nextInt(99999999);

        return String.valueOf(num);
    }

    public void newCustomer(String channelId, String channelStaffId, String adviserId, String phone, String customerName) throws Exception {

        String json = StrSubstitutor.replace(CUSTOMER_INSERT_JSON, ImmutableMap.builder()
                .put("shopId", getShopId())
                .put("channelId", channelId) //测试【勿动】
                .put("channelStaffId", channelStaffId)//宫二
                .put("adviserId", adviserId)
                .put("phone", phone)
                .put("customerName", customerName)
                .build()
        );

        String res = httpPost(CUSTOMER_INSERT, json, new String[0]);
        int codeRes = JSON.parseObject(res).getInteger("code");

        if (codeRes == 2002) {
            phone = genPhoneNum();
            newCustomer(channelId, channelStaffId, adviserId, phone, customerName);
        }
    }

    public JSONObject registerQrCode() throws Exception {

        String requestUrl = "/risk/shop/self-register/qrcode";

        String json = "{\"shop_id\":" + getShopId() + "}";

        String res = httpPostWithCheckCode(requestUrl, json, new String[0]);
        JSONObject data = JSON.parseObject(res).getJSONObject("data");

        return data;
    }

    public JSONObject createOrder(String idCard, String phone, String orderStage) throws Exception {

        String json = StrSubstitutor.replace(CREATE_ORDER_JSON, ImmutableMap.builder()
                .put("requestId", UUID.randomUUID().toString())
                .put("shopId", getShopId())
                .put("idCard", idCard)
                .put("phone", phone)
                .put("orderStage", orderStage)
                .build()
        );
        String res = httpPostWithCheckCode(ADD_ORDER, json, new String[0]);

        return JSON.parseObject(res);
    }

    public JSONObject orderDetail(String orderId) throws Exception {
        String json = StrSubstitutor.replace(ORDER_DETAIL_JSON, ImmutableMap.builder()
                .put("shopId", getShopId())
                .put("orderId", orderId)
                .build()
        );
        String res = httpPostWithCheckCode(ORDER_DETAIL, json, new String[0]);

        return JSON.parseObject(res).getJSONObject("data");
    }

    public void orderAudit(String orderId, int isCustomerIntroduced, int introduceCheckedPerson, int isChannelStaffShowDialog) throws Exception {
        String json = StrSubstitutor.replace(AUDIT_ORDER_JSON, ImmutableMap.builder()
                .put("requestId", UUID.randomUUID().toString())
                .put("shopId", getShopId())
                .put("orderId", orderId)
                .put("isCustomerIntroduced", isCustomerIntroduced)
                .put("introduceCheckedPerson", introduceCheckedPerson)
                .put("isChannelStaffShowDialog", isChannelStaffShowDialog)
                .build()
        );
        httpPostWithCheckCode(AUDIT_ORDER, json, new String[0]);
    }

    public JSONArray orderStepLog(String orderId) throws Exception {
        String json = StrSubstitutor.replace(ORDER_STEP_LOG_JSON, ImmutableMap.builder()
                .put("shopId", getShopId())
                .put("orderId", orderId)
                .build()
        );

        String res = httpPostWithCheckCode(ORDER_STEP_LOG, json, new String[0]);//订单详情与订单跟进详情入参json一样

        return JSON.parseObject(res).getJSONObject("data").getJSONArray("list");
    }

    /**
     * 于海生，现场自然成交，订单状态：正常 ，核验状态：无需核验
     * 18811111111
     * 111111111111111111
     */
    @Test
    public void noChannelDeal() {
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        try {
            // 创建订单
            JSONObject result = createOrder("111111111111111111", "18811111111", "SIGN");
            String orderId = JSONPath.eval(result, "$.data.order_id").toString();

            // 查询订单
            result = orderDetail(orderId);

            checkOrder(result, 1, false);

//            校验环节异常
            checkRiskStep(orderId);
        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, caseName, "现场自然成交，订单状态：正常 ，核验状态：无需核验");
        }
    }

    /**
     * 刘峤，报备-到场-成交，订单状态：正常 ，核验状态：未核验，修改状态为正常，查询订单详情和订单列表，该订单状态为：正常，已核验
     * 12111111119
     * 111111111111111113
     */
    @Test
    public void normal2Normal() {
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        try {
            // 创建订单

            JSONObject result = createOrder("111111111111111113", "12111111119", "SIGN");
            String orderId = JSONPath.eval(result, "$.data.order_id").toString();

            result = orderDetail(orderId);

            checkOrder(result, 1, false);

            // 审核订单

            orderAudit(orderId, 1, 1, 1);

            // 查询订单
            result = orderDetail(orderId);

            checkOrder(result, 1, true);

//            校验环节异常
            checkRiskStep(orderId);
        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, caseName, "报备-到场-成交，订单状态：正常 ，核验状态：未核验");
        }
    }

    /**
     * 刘峤，报备-到场-成交，订单状态：正常 ，核验状态：未核验，修改状态为正常，查询订单详情和订单列表，该订单状态为：风险，已核验
     * 12111111119
     * 111111111111111113
     */
    @Test
    public void normal2risk() {
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        try {

            // 创建订单
            JSONObject result = createOrder("111111111111111113", "12111111119", "SIGN");
            String orderId = JSONPath.eval(result, "$.data.order_id").toString();

            // 查询订单
            result = orderDetail(orderId);

            checkOrder(result, 1, false);

            // 审核订单
            orderAudit(orderId, 0, 2, 0);

            // 查询订单
            result = orderDetail(orderId);

            checkOrder(result, 3, true);

//            校验环节异常
            checkRiskStep(orderId);
        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, caseName, "报备-到场-成交，订单状态：正常 ，核验状态：未核验");
        }
    }

    /**
     * 创单报备，现场报备-成交，订单状态：风险，核验状态：未核验。修改状态为风险，查询订单详情和订单列表，该订单状态为：风险，已核验
     * 18888811111
     * 333333333333333335
     */
    @Test
    public void dealReport() {
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        try {
            // 创建订单
            JSONObject result = createOrder("333333333333333335", "18888811111", "SIGN");
            String orderId = JSONPath.eval(result, "$.data.order_id").toString();

            // 查询订单
            result = orderDetail(orderId);

            checkOrder(result, 3, false);

            // 审核订单
            orderAudit(orderId, 0, 2, 0);

            // 查询订单

            result = orderDetail(orderId);

            checkOrder(result, 3, true);

            //校验环节异常

            checkRiskStep(orderId);

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, caseName, "创单报备，现场报备-成交，订单状态：风险");
        }
    }

    /**
     * 李俊延，报备-到场-修改报备手机号-创单，订单状态：风险 ，核验状态：未核验。修改状态为风险，查询订单详情和订单列表，该订单状态为：风险，已核验
     * 14111111135
     * 111111111111111114
     */
    @Test
    public void risk2risk() {
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        try {
            // 创建订单
            JSONObject result = createOrder("111111111111111114", "14111111135", "SIGN");
            String orderId = JSONPath.eval(result, "$.data.order_id").toString();

            // 查询订单
            result = orderDetail(orderId);

            checkOrder(result, 3, false);

            // 审核订单
            orderAudit(orderId, 0, 2, 0);

            // 查询订单
            result = orderDetail(orderId);

            checkOrder(result, 3, true);

            //校验异常环节
            checkRiskStep(orderId);

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, caseName, "报备-到场-修改报备手机号-创单，订单状态：风险 ，核验状态：未核验");
        }
    }

    /**
     * 谢志东，顾客到场-H5报备-成交 ，订单状态：风险 ，核验状态：未核验。修改状态为正常，查询订单详情和订单列表，该订单状态为：正常，已核验
     * 12111111311
     * 111111111111111115
     */
    @Test
    public void arrivalBoforeReport() {
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        try {
            // 创建订单
            JSONObject result = createOrder("111111111111111115", "12111111311", "SIGN");
            String orderId = JSONPath.eval(result, "$.data.order_id").toString();

            // 查询订单
            result = orderDetail(orderId);

            checkOrder(result, 3, false);

            // 审核订单
            orderAudit(orderId, 1, 1, 1);

            // 查询订单
            result = orderDetail(orderId);

            checkOrder(result, 1, true);

//            校验异常环节
            checkRiskStep(orderId);

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, caseName, "顾客到场-H5报备-成交 ，订单状态：风险 ，核验状态：未核验");
        }
    }

    /**
     * 刘博，未到场-自然成交，订单状态：正常 ，核验状态：无需核验
     * 16600000003
     * 111111111111111116
     */
    @Test
    public void nochannelNoArrival() {
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        try {
            // 创建订单
            JSONObject result = createOrder("111111111111111116", "16600000003", "SIGN");
            String orderId = JSONPath.eval(result, "$.data.order_id").toString();

            // 查询订单
            result = orderDetail(orderId);

            checkOrder(result, 1, false);

//            校验环节异常
            checkRiskStep(orderId);

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, caseName, "未到场-自然成交，订单状态：正常");
        }
    }

    /**
     * 未到场B，未到场-报备-成交，订单状态：风险 ，核验状态：未核验
     * 16600000002
     * 111111111111111117
     */
    @Test
    public void channelNoArrival() {

        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        try {
            // 创建订单
            JSONObject result = createOrder("111111111111111117", "16600000002", "SIGN");
            String orderId = JSONPath.eval(result, "$.data.order_id").toString();

            // 查询订单
            result = orderDetail(orderId);

            checkOrder(result, 3, false);

//            校验异常环节
            checkRiskStep(orderId);

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, caseName, "未到场-报备-成交，订单状态：风险 ，核验状态：未核验");
        }
    }

    @Test
    public void diffGender() {

        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        try {
            // 创建订单
            JSONObject result = createOrder("555555555555555565", "19811111111", "SIGN");
            String orderId = JSONPath.eval(result, "$.data.order_id").toString();

            // 校验订单的风险状态
            result = orderDetail(orderId);

            checkOrder(result, 1, false);

            //校验顾客性别冲突时，环节异常
            JSONArray logSteps = orderStepLog(orderId);
            checkConflict(logSteps, orderId, true);

//            校验环节异常
            checkRiskStep(orderId);

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, caseName, "报备时性别和身份证性别不一致！");
        }
    }

    @Test
    public void sameGender() {

        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        try {
            // 创建订单
            JSONObject result = createOrder("555555555555555555", "18831111111", "SIGN");
            String orderId = JSONPath.eval(result, "$.data.order_id").toString();

            // 校验订单的风险状态
            result = orderDetail(orderId);

            checkOrder(result, 1, false);

            //校验顾客性别冲突时，环节异常
            JSONArray logSteps = orderStepLog(orderId);
            checkConflict(logSteps, orderId, false);

//            校验环节异常
            checkRiskStep(orderId);

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, caseName, "报备时性别和身份证性别一致！");
        }
    }

    private void checkConflict(JSONArray logSteps, String orderId, boolean isExist) throws Exception {
        boolean isExistRes = false;
        for (int i = 0; i < logSteps.size(); i++) {
            JSONObject oneStep = logSteps.getJSONObject(i);
            String stepType = oneStep.getString("step_type");

            if ("GENDER_AUDIT".equals(stepType)) {
                isExistRes = true;
                if (!oneStep.getBooleanValue("is_in_risk")) {
                    throw new Exception("orderId[" + orderId + "],性别冲突时，环节没有标记为异常！");
                }
            }
        }

        if (!isExistRes == isExist) {
            throw new Exception("orderId[" + orderId + "],是否期待有“信息冲突”环节，期待：" + isExist + "，系统返回：" + isExistRes);
        }
    }

    public void checkOrder(JSONObject result, int expectStatus, boolean expectNeedAudit) {
        Object orderStatus = JSONPath.eval(result, "$.order_status");
        Assert.assertEquals(orderStatus, expectStatus, "订单状态不正常");

        Object isNeedAudit = JSONPath.eval(result, "$.is_audited");
        Assert.assertEquals(isNeedAudit, expectNeedAudit, "核验状态不正常");

    }

//    private void checkRiskStep(String orderId, String stepType) throws Exception {
//        JSONArray steps = orderStepLog(orderId);
//        for (int i = 0; i < steps.size(); i++) {
//            JSONObject oneStep = steps.getJSONObject(i);
//            if (stepType.equals(oneStep.getString("step_type"))){
//                if (!oneStep.getBooleanValue("is_in_risk")){
//                    String stepName = oneStep.getString("step_name");
//                    String stepNameContent = oneStep.getString("step_name_content");
//                    throw new Exception("orderId[" + orderId + "],没有将[" + stepName + "]环节标记为异常。异常信息[" + stepNameContent + "]");
//                }
//            }
//        }
//    }

    private void checkRiskStep(String orderId) throws Exception {
        JSONArray steps = orderStepLog(orderId);
        for (int i = 0; i < steps.size(); i++) {
            JSONObject oneStep = steps.getJSONObject(i);
            String stepNameContent = oneStep.getString("step_name_content");
            String stepName = oneStep.getString("step_name");

            if (stepName.contains("顾客手机号更新")) {
                if (!oneStep.getBooleanValue("is_in_risk")) {
                    throw new Exception("orderId[" + orderId + "]没有将“顾客手机号更新”环节标记为异常！");
                }
            } else if (stepName.contains("顾客从未出现")) {
                if (!oneStep.getBooleanValue("is_in_risk")) {
                    throw new Exception("orderId[" + orderId + "]没有将“顾客从未出现”环节标记为异常！");
                }
            } else if (stepName.contains("顾客性别信息冲突")) {
                if (!oneStep.getBooleanValue("is_in_risk")) {
                    throw new Exception("orderId[" + orderId + "]没有将“顾客性别信息冲突”环节标记为异常！");
                }
            } else if (stepNameContent.contains("报备时间晚于首次到访时间")) {
                if (!oneStep.getBooleanValue("is_in_risk")) {
                    throw new Exception("orderId[" + orderId + "]没有将“报备时间晚于首次到访时间”环节标记为异常！");
                }
            } else if (stepNameContent.contains("置业顾问多次修改")) {
                if (!oneStep.getBooleanValue("is_in_risk")) {
                    throw new Exception("orderId[" + orderId + "]没有将“置业顾问多次修改”环节标记为异常！");
                }
            } else if (stepNameContent.contains("顾客姓名多次修改")) {
                if (!oneStep.getBooleanValue("is_in_risk")) {
                    throw new Exception("orderId[" + orderId + "]没有将“顾客姓名多次修改”环节标记为异常！");
                }
            }

        }
    }

    private void setBasicParaToDB(Case aCase, String caseName, String caseDesc) {
        aCase.setApplicationId(APP_ID);
        aCase.setConfigId(CONFIG_ID);
        aCase.setCaseName(caseName);
        aCase.setCaseDescription(caseDesc);
        aCase.setCiCmd(CI_CMD + caseName);
        aCase.setQaOwner("于海生");
        aCase.setExpect("code==1000");
        aCase.setResponse(response);

        if (!StringUtils.isEmpty(failReason) || !StringUtils.isEmpty(aCase.getFailReason())) {
            aCase.setFailReason(failReason);
        } else {
            aCase.setResult("PASS");
        }
    }

    private void saveData(Case aCase, String caseName, String caseDescription) {
        setBasicParaToDB(aCase, caseName, caseDescription);
        qaDbUtil.saveToCaseTable(aCase);
        if (!StringUtils.isEmpty(aCase.getFailReason())) {
            logger.error(aCase.getFailReason());
            dingPush("飞单日常 \n" + aCase.getCaseDescription() + " \n" + aCase.getFailReason());
        }
    }

    private void dingPush(String msg) {
        if (DEBUG.trim().toLowerCase().equals("false")) {
            AlarmPush alarmPush = new AlarmPush();

            alarmPush.setDingWebhook(DingWebhook.OPEN_MANAGEMENT_PLATFORM_GRP);

            alarmPush.dailyRgn(msg);
            this.FAIL = true;
        }
        Assert.assertNull(aCase.getFailReason());
    }

    private void dingPushFinal() {
        if (DEBUG.trim().toLowerCase().equals("false") && FAIL) {
            AlarmPush alarmPush = new AlarmPush();

            alarmPush.setDingWebhook(DingWebhook.OPEN_MANAGEMENT_PLATFORM_GRP);

            //15898182672 华成裕
            //18513118484 杨航
            //15011479599 谢志东
            //18600872221 蔡思明
            String[] rd = {"18513118484", //杨航
                    "15011479599", //谢志东
                    "15898182672"}; //华成裕
            alarmPush.alarmToRd(rd);
        }
    }

    @DataProvider(name = "SEARCH_TYPE")
    private static Object[] searchType() {
        return new Object[]{
                "CHANCE",
//                "CHECKED",
//                "REPORTED"
        };
    }

    @DataProvider(name = "DEAL_PHONE")
    private static Object[][] dealPhone() {
        return new Object[][]{
                new Object[]{
                        "12111111123", "222222222222222221", "傅天宇", "2019-11-18 21:38:50"
                },
                new Object[]{
                        "12111111311", "111111111111111115", "谢志东", "2019-11-19 09:52:48"
                },
                new Object[]{
                        "14311111111", "111111111111111119", "杨航", "2019-11-25 20:33:03"
                },
                new Object[]{
                        "18411112112", "111111111111111112", "廖祥茹", "2019-11-26 08:58:29"
                },
                new Object[]{
                        "12111111119", "111111111111111113", "刘峤", "2019-11-26 10:26:46"
                },
                new Object[]{
                        "12111111115", "666666666666666666", "更改置业顾问", "2019-11-18 21:50:16"
                },
                new Object[]{
                        "14111111135", "111111111111111114", "李俊延", "2019-11-29 17:41:55"
                },
                new Object[]{
                        "16600000005", "222222222222222222", "华成裕", "2019-11-19 10:26:34"
                },
                new Object[]{
                        "18811111111", "111111111111111111", "于海生", "2019-11-18 21:14:05"
                },
                new Object[]{
                        "18888811111", "333333333333333335", "创单报备", "2019-11-19 12:42:40"
                }
        };
    }
}

