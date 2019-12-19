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

public class FeidanMiniApiOnline {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private String failReason = "";
    private String response = "";
    private boolean FAIL = false;
    private Case aCase = new Case();

    DateTimeUtil dateTimeUtil = new DateTimeUtil();
    private QADbUtil qaDbUtil = new QADbUtil();
    private int APP_ID = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
    private int CONFIG_ID = ChecklistDbInfo.DB_SERVICE_ID_FEIDAN_ONLINE_SERVICE;

    private String CI_CMD = "curl -X POST http://qarobot:qarobot@192.168.50.2:8080/job/feidan-online-test/buildWithParameters?case_name=";

    private String DEBUG = System.getProperty("DEBUG", "true");

    private String authorization = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiLotornp4DmtYvor5XotKblj7ciLCJ1aWQiOiJ1aWRfZWY2ZDJkZTUiLCJsb2dpblRpbWUiOjE1NzQyNDE5NDIxNjV9.lR3Emp8iFv5xMZYryi0Dzp94kmNT47hzk2uQP9DbqUU";

    private HttpConfig config;

    String channelId = "19";

    String genderMale = "MALE";
    String genderFemale = "FEMALE";

    private String getIpPort() {
        return "http://store.winsenseos.com";
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
        String json = "{\"username\":\"demo@winsense.ai\",\"passwd\":\"f2064e9d2477a6bc75c132615fe3294c\"}";
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

        saveData(aCase, caseName, caseName, "登录获取authentication");
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
            saveData(aCase, caseName, caseName, "校验shop");
        }

    }

    private Object getShopId() {
        return "97";
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
            "\"gender\":\"${gender}\",\"customer_name\":\"${customerName}\",\"phone\":\"${phone}\"}";

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
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName + "-" + searchType;

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
            saveData(aCase, ciCaseName, caseName, "顾客列表与顾客详情中的信息一致");
        }
    }

    @Test
    public void dealListEqualsDetail() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        try {
            // 订单列表
            JSONArray list = orderList(1, pageSize);
            for (int i = 0; i < list.size() && i <= 20; i++) {
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
            saveData(aCase, ciCaseName, caseName, "订单详情与订单列表中信息是否一致");
        }
    }

    @Test(dataProvider = "ALL_DEAL_PHONE", priority = 1)
    public void dealLogEqualsDetail(String phone) {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName + "-" + phone;


        try {
            // 订单列表
            JSONArray list = orderListWithPhone(phone, 1, 5);
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
            saveData(aCase, ciCaseName, caseName, "订单详情与订单跟进详情中信息是否一致");
        }
    }

    /**channel == 渠道， 渠道报备总数 == 渠道所有业务员报备总数**/
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
            saveData(aCase, caseName, caseName, "渠道的累计报备数==各个业务员的累计报备数之和");
        }
    }

    /**渠道订单数 == 已签约的订单数，已签约: CHECKED**/
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
            saveData(aCase, caseName, caseName, "顾客查询中的签约顾客数==渠道中的签约顾客数");
        }
    }

    /**REPORT: 报备**/
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
            saveData(aCase, caseName, caseName, "顾客查询中的报备顾客数==渠道中的报备顾客数");
        }
    }

    /**订单列表数 == 正常订单数 + 风险订单数**/
    @Test
    public void orderListDiffType() {
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        try {
            JSONArray totalList = orderList(1, 10000);
            int totalNum = totalList.size();

//            获取正常订单数
            JSONArray normalList = orderListWithStatus("1", 1, 10000);//1是正常，3是风险
            int normalNum = normalList.size();

//            获取风险订单数
            JSONArray riskList = orderListWithStatus("3", 1, 10000);//1是正常，3是风险
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
            saveData(aCase, caseName, caseName, "订单列表中，风险+正常的订单数==订单列表总数");
        }
    }

    /**
     * 创建10个置业顾问，查看列表是否正确，然后清除新建的置业顾问
     **/
    @Test
    public void addStaffTestPage() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;
        ArrayList<String> phones = new ArrayList<>();

        try {

            String dirPath = "src/main/java/com/haisheng/framework/testng/bigScreen/feidanImages";
            int pageSizeTemp = 10;
            File file = new File(dirPath);
            File[] files = file.listFiles();

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

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            try {
                JSONArray staffList = staffList(1, pageSize);
                ArrayList<String> ids = getIdsByPhones(staffList, phones);
                for (int i = 0; i < ids.size(); i++) {
                    deleteStaff(ids.get(i));
                }
            } catch (AssertionError e) {
                logger.error(e.toString());
            } catch (Exception e) {
                logger.error(e.toString());
            } finally {
                saveData(aCase, ciCaseName, caseName, "员工列表每页显示核查");
            }

        }
    }

    /**
     * 渠道业务员列表每页显示核查
     * 注册一个渠道业务员并查看渠道列表
     **/
    @Test
    public void addChannelStaffTestPage() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;
        ArrayList<String> phones = new ArrayList<>();
        try {
            int pageSizeTemp = 10;

            for (int i=0; i<1; i++) {
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


        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);

        } finally {
            try {
                JSONArray staffList = channelStaffList(channelId, 1, pageSize);
                ArrayList<String> ids = getIdsByPhones(staffList, phones);

                for (int i = 0; i < ids.size(); i++) {
                    changeChannelStaffState(ids.get(i));
                }
            } catch (Exception e) {
                logger.info(e.toString());
            } finally {
                saveData(aCase, ciCaseName, caseName, "渠道业务员列表每页显示核查");
            }

        }
    }

    /**
     * 人脸注册渠道员工，期望成功
     **/
    @Test
    public void addChannelStaffWithPicCheck() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

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
            saveData(aCase, ciCaseName, caseName, "人脸注册渠道员工，期望成功");
        }
    }

    /**
     * 机会顾客列表每页显示是否正常
     **/
    @Test
    public void newCustomerTestPage() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;
        String gong = "69";
        String zhangjunmi = "66";

        try {
            int pageSizeTemp = 10;
            String serachType = "CHANCE";

            for (int i = 0; i < 10; i++) {
                String phoneNum = genPhoneNum();
                newCustomer(channelId, gong, zhangjunmi, phoneNum, "customer-testpage", genderMale);
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
            saveData(aCase, ciCaseName, caseName, "机会顾客列表每页显示是否正常");
        }
    }



    /**签约+机会顾客 >= 报备顾客**/
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
            saveData(aCase, caseName, caseName, "签约顾客+机会顾客≥报备顾客");
        }
    }

    /**员工类型总数 == 各个员工类型数量之和**/
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
            saveData(aCase, caseName, caseName, "员工管理中，各类型员工数量统计是否正确");
        }
    }

    /**相同人脸新建渠道员工，新建失败**/
    @Test
    public void initThenRegChannelStaffSamePic() {

        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        try {
            String dirPath = "src/main/java/com/haisheng/framework/testng/bigScreen/feidanForbid/changestate.jpg";

            String namePhone = "宫先生";

//            保证业务员处于启用状态
            JSONObject staffListWithPhone = channelStaffListWithPhone(channelId, namePhone, 1, pageSize);
            JSONObject single = staffListWithPhone.getJSONArray("list").getJSONObject(0);

            boolean isDelete = single.getBooleanValue("is_delete");

            if (isDelete) {
                changeChannelStaffState("69");
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
            saveData(aCase, caseName, caseName, "业务员处于启用状态，不能新建一个与此业务员相似人脸的业务员");
        }
    }

    /**相同人脸新建置业顾问，新建失败**/
    @Test
    public void initThenRegStaffSamePic() {

        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        try {
            String dirPath = "src/main/java/com/haisheng/framework/testng/bigScreen/feidanForbid/changestate.jpg";

            String namePhone = "宫先生";

//            保证业务员处于启用状态
            JSONObject staffListWithPhone = channelStaffListWithPhone(channelId, namePhone, 1, pageSize);
            JSONObject single = staffListWithPhone.getJSONArray("list").getJSONObject(0);

            boolean isDelete = single.getBooleanValue("is_delete");

            if (isDelete) {
                changeChannelStaffState("69");
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
            saveData(aCase, caseName, caseName, "业务员处于启用状态，不能新建一个与此业务员相似人脸的售楼处员工");
        }
    }

    /**用已存在的渠道员工手机号，新建渠道员工，新建失败**/
    @Test
    public void initThenRegChannelStaffSamePhone() {

        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        try {
            String namePhone = "宫先生";
            String phone = "17610248107";

//            保证业务员“宫二”处于启用状态
            JSONObject staffListWithPhone = channelStaffListWithPhone(channelId, namePhone, 1, pageSize);
            JSONObject single = staffListWithPhone.getJSONArray("list").getJSONObject(0);

            boolean isDelete = single.getBooleanValue("is_delete");

            if (isDelete) {
                changeChannelStaffState("69");
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
            saveData(aCase, caseName, caseName, "业务员处于启用状态，不能新建一个与此业务员相同手机号的业务员");
        }
    }

    /**用已存在的渠道员工手机号，新建置业顾问，新建失败**/
    @Test
    public void initThenRegStaffSamePhone() {

        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        try {
            String dirPath = "src/main/java/com/haisheng/framework/testng/bigScreen/feidanForbid/changestate.jpg";

            String namePhone = "宫先生";

            String phone = "17610248107";

//            保证业务员处于启用状态
            JSONObject staffListWithPhone = channelStaffListWithPhone(channelId, namePhone, 1, pageSize);
            JSONObject single = staffListWithPhone.getJSONArray("list").getJSONObject(0);

            boolean isDelete = single.getBooleanValue("is_delete");

            if (isDelete) {
                changeChannelStaffState("69");
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
            saveData(aCase, caseName, caseName, "业务员处于启用状态，不能新建一个与此业务员相同手机号的售楼处员工");
        }
    }

    /**用已存在的渠道员工图片，编辑渠道员工，编辑失败**/
    @Test
    public void initThenEditChannelStaffSamePic() {

        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        try {
            String dirPath = "src/main/java/com/haisheng/framework/testng/bigScreen/feidanForbid/makun.jpg";

            String namePhone = "宫先生";

//            保证业务员“宫二”处于启用状态
            JSONObject staffListWithPhone = channelStaffListWithPhone(channelId, namePhone, 1, pageSize);
            JSONObject single = staffListWithPhone.getJSONArray("list").getJSONObject(0);

            boolean isDelete = single.getBooleanValue("is_delete");

            if (isDelete) {
                changeChannelStaffState("69");
            }

//           编辑一个业务员，用宫二的图片
            dirPath = dirPath.replace("/", File.separator);

            String faceUrl = uploadImage(dirPath).getString("face_url");


//          phone：17610248107是宫先生的手机号，id：86是待编辑的渠道员工
            String id = "86";
            String phone = "17610248107";
            String name = "黄鑫";
            String response = editChannelStaffPic(id, name, channelId, phone, faceUrl);

            checkCode(response, StatusCode.BAD_REQUEST, "编辑业务员");

        } catch (AssertionError e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, caseName, caseName, "业务员处于启用状态，不能编辑另一业务员为相似人脸");
        }
    }

    /**用已存在的渠道员工图片，编辑置业顾问，编辑失败**/
    @Test
    public void initThenEditStaffSamePic() {

        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        try {
            String dirPath = "src/main/java/com/haisheng/framework/testng/bigScreen/feidanForbid/changestate.jpg";

            String namePhone = "宫先生";

//            保证业务员处于启用状态
            JSONObject staffListWithPhone = channelStaffListWithPhone(channelId, namePhone, 1, pageSize);
            JSONObject single = staffListWithPhone.getJSONArray("list").getJSONObject(0);

            boolean isDelete = single.getBooleanValue("is_delete");

            if (isDelete) {
                changeChannelStaffState("69");
            }

//           编辑一个置业顾问，用宫二的图片
            dirPath = dirPath.replace("/", File.separator);

            String faceUrl = uploadImage(dirPath).getString("face_url");

//            这是一个已经建好的置业顾问
            String id = "70";
            String phone = "15511111112";
            String name = "徐峥";
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
            saveData(aCase, caseName, caseName, "业务员处于启用状态，不能编辑另一售楼处员工为相似人脸");
        }
    }

    /**用已存在的渠道员工手机号，编辑渠道员工，编辑失败**/
    @Test
    public void initThenEditChannelStaffSamePhone() {

        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        try {
            String namePhone = "宫先生";

//            保证业务员处于启用状态
            JSONObject staffListWithPhone = channelStaffListWithPhone(channelId, namePhone, 1, pageSize);
            JSONObject single = staffListWithPhone.getJSONArray("list").getJSONObject(0);

            boolean isDelete = single.getBooleanValue("is_delete");

            if (isDelete) {
                changeChannelStaffState("69");
            }

//           编辑一个置业顾问，用宫的手机号 17610248107
//           这是一个已经建好的置业顾问
            String id = "86";
            String phone = "17610248107";
            String name = "黄鑫";
            String response = editChannelStaffPhone(id, name, channelId, phone);

            checkCode(response, StatusCode.BAD_REQUEST, "编辑业务员");

        } catch (AssertionError e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, caseName, caseName, "业务员处于启用状态，不能编辑另一业务员为相同手机号");
        }
    }

    /**用已存在的渠道员工手机号，编辑置业顾问，编辑失败**/
    @Test
    public void initThenEditStaffSamePhone() {

        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        try {
            String dirPath = "src/main/java/com/haisheng/framework/testng/bigScreen/feidanForbid/changestate.jpg";

            String namePhone = "宫先生";

//            保证业务员处于启用状态
            JSONObject staffListWithPhone = channelStaffListWithPhone(channelId, namePhone, 1, pageSize);
            JSONObject single = staffListWithPhone.getJSONArray("list").getJSONObject(0);

            boolean isDelete = single.getBooleanValue("is_delete");

            if (isDelete) {
                changeChannelStaffState("69");
            }


//           编辑一个置业顾问，用宫的手机号 17610248107
//           这是一个已经建好的置业顾问
            String id = "70";
            String phone = "17610248107";
            String name = "徐峥";
            String staffType = "PROPERTY_CONSULTANT";
            String faceUrl = uploadImage(dirPath).getString("face_url");
            String response = editStaffRes(id, name, staffType, phone, faceUrl);

            checkCode(response, StatusCode.BAD_REQUEST, "编辑售楼处员工");

        } catch (AssertionError e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, caseName, caseName, "业务员处于启用状态，不能编辑另一售楼处员工为相同手机号");
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
            saveData(aCase, caseName, caseName, "订单列表按照新建时间倒排");
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
            saveData(aCase, caseName, caseName, "员工列表按照新建时间倒排");
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
            saveData(aCase, caseName, caseName, "渠道列表按照新建时间倒排");
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
            saveData(aCase, caseName, caseName, "渠道员工列表按照新建时间倒排");
        }
    }

    /**案场二维码不为空**/
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
            } else if (!url.contains(".com")) {
                throw new Exception("案场二维码中【url】不是线上url， url: " + url);
            }


        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, caseName, caseName, "渠道员工列表按照新建时间倒排");
        }
    }

    /**设置订单状态从认购到成交**/
    @Test(dataProvider = "ALL_DEAL_PHONE")
    public void sign2deal(String phone) {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName + "-" + phone;

        try {

            JSONArray orders = orderListWithPhone(phone, 1, pageSize);
            for (int i = 0, num = 0; i < orders.size() && num < 2; i++) {
                JSONObject oneOrder = orders.getJSONObject(i);
                String orderId = oneOrder.getString("order_id");
                String dealTime = oneOrder.getString("deal_time");
                if (dealTime == null || "".equals(dealTime)) {
                    num++;
                    orderEdit(orderId, phone, "DEAL");
                }
                checkEditOrderOfList(orderId, phone);
                checkEditOrderOfDetail(orderId);
                checkEditOrderOfStepLog(orderId);
            }

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "");
        }
    }

    /**订单环节异常设置**/
    @Test
    public void stepRiskAudit() {
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        try {
            JSONArray orders = orderListWithPhone("19811111111", 1, 1);
            String orderId = orders.getJSONObject(0).getString("order_id");

            checkStepRiskAudit(orderId);

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, caseName, caseName, "");
        }
    }

    private void checkStepRiskAudit(String orderId) throws Exception {
        JSONArray orderStepLog = orderStepLog(orderId);

        String auditResult = "";//1是正常，2是风险
        String originalStatus = "";//true表示本来是风险，false表示本来是正常
        for (int i = 0; i < orderStepLog.size(); i++) {

            JSONObject oneStep = orderStepLog.getJSONObject(i);

            String isInRisk = oneStep.getString("is_in_risk");
            if (isInRisk == null || "".equals(isInRisk) || "false".equals(isInRisk)) {
                auditResult = "2";
//                originalStatus = "false";
            } else {
                auditResult = "1";
            }

            String typeIndex = oneStep.getString("type_index");
            String stepType = oneStep.getString("step_type");
            String stepName = oneStep.getString("step_name");
            String desc = "测试 !@#$%^&*()_";

            orderStepAudit(orderId, auditResult, typeIndex, stepType, desc);

            checkStepAuditLog(orderId, stepName, desc);

        }
    }

    private void checkStepAuditLog(String orderId, String itemName, String desc) throws Exception {
        JSONArray auditLogs = orderStepAuditLog(orderId);

        boolean isExist = false;

        for (int i = auditLogs.size() - 1; i >= 0; i--) {
            JSONObject oneLog = auditLogs.getJSONObject(i);
            String itemNameRes = oneLog.getString("item_name");
            String stepIndexRes = oneLog.getString("step_index");
            if (itemName.equals(itemNameRes)) {

                isExist = true;

                String descRes = oneLog.getString("desc");

                if (!desc.equals(descRes)) {
                    throw new Exception("订单操作记录中desc[" + descRes + "],订单跟进详情中[" + desc + "],不一致！");
                }
            }

            if (!isExist) {
                throw new Exception("订单操作记录中没有item_name为[" + itemName + "]的操作记录！");
            }
        }
    }

    private void checkEditOrderOfList(String orderId, String phone) throws Exception {
        boolean isExist = false;
        JSONArray orderList = orderListWithPhone(phone, 1, pageSize);
        for (int i = 0; i < orderList.size(); i++) {
            JSONObject oneOrder = orderList.getJSONObject(i);
            String orderIdRes = oneOrder.getString("order_id");
            if (orderId.equals(orderIdRes)) {
                isExist = true;
                String dealTime = oneOrder.getString("deal_time");
                if (dealTime == null || "".equals(dealTime)) {
                    throw new Exception("认购订单改为成交订单后，列表中deal_time为空！");
                }
                break;
            }
        }

        if (!isExist) {
            throw new Exception("不存在该订单");
        }
    }

    private void checkEditOrderOfDetail(String orderId) throws Exception {

        JSONObject orderDetail = orderDetail(orderId);

        String dealTime = orderDetail.getString("deal_time");

        if (dealTime == null || "".equals(dealTime)) {
            throw new Exception("认购订单改为成交订单后，订单详情中deal_time为空！");
        }
    }

    private void checkEditOrderOfStepLog(String orderId) throws Exception {

        JSONArray orderStepLog = orderStepLog(orderId);
        boolean isExist = false;

        for (int i = 0; i < orderStepLog.size(); i++) {
            JSONObject oneStep = orderStepLog.getJSONObject(i);
            String stepNameContent = oneStep.getString("step_name_content");
            if ("成交".equals(stepNameContent)) {
                isExist = true;
                String dealTime = oneStep.getString("time_str");
                compareOrderTimeValue(orderDetail(orderId), "deal_time", orderId, dealTime, "订单跟进详情", "订单详情");

                break;
            }
        }

        if (!isExist) {
            throw new Exception("认购订单改为成交订单后，订单跟进详情中“成交”环节为空！");
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
                break;
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

    public String getIdByPhone(JSONArray staffList, String phone) {
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

    public String getIdByPhoneAndStatus(JSONArray staffList, String phone, boolean status) {
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

    public JSONArray orderEdit(String orderId, String phone, String orderStage) throws Exception {
        String url = "/risk/order/edit";
        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + ",\n" +
                        "    \"order_id\":\"" + orderId + "\"," +
                        "    \"phone\":\"" + phone + "\"," +
                        "    \"order_stage\":\"" + orderStage + "\"" +
                        "}";
        String res = httpPostWithCheckCode(url, json, new String[0]);

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

    public void newCustomer(String channelId, String channelStaffId, String adviserId, String phone, String customerName, String gender) throws Exception {

        String json = StrSubstitutor.replace(CUSTOMER_INSERT_JSON, ImmutableMap.builder()
                .put("shopId", getShopId())
                .put("channelId", channelId) //测试【勿动】
                .put("channelStaffId", channelStaffId)//宫二
                .put("adviserId", adviserId)
                .put("phone", phone)
                .put("customerName", customerName)
                .put("gender", gender)
                .build()
        );

        String res = httpPost(CUSTOMER_INSERT, json, new String[0]);
        int codeRes = JSON.parseObject(res).getInteger("code");

        if (codeRes == 2002) {
            phone = genPhoneNum();
            newCustomer(channelId, channelStaffId, adviserId, phone, customerName, gender);
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

    public void orderStepAudit(String orderId, String auditResult, String typeIndex, String stepType, String desc) throws Exception {
        String url = "/risk/order/step/audit";
        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + ",\n" +
                        "    \"order_id\":\"" + orderId + "\"," +
                        "    \"audit_result\":\"" + auditResult + "\",\n" +//1是正常，2是风险
//                        "    \"original_status\":\"" + originalStatus +  "\"," +//true表示本来是风险，false表示本来是正常
                        "    \"type_index\":\"" + typeIndex + "\",\n" +//表示相同的环节的序号，如第一次修改姓名是0，第二次是1
                        "    \"step_type\":\"" + stepType + "\",\n" +
                        "    \"desc\":\"" + desc + "\"" +
                        "}";

        httpPostWithCheckCode(url, json, new String[0]);//订单详情与订单跟进详情入参json一样

    }

    public JSONArray orderStepAuditLog(String orderId) throws Exception {
        String url = "/risk/order/step/audit/log";
        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + ",\n" +
                        "    \"order_id\":\"" + orderId + "\"" +
                        "}";

        String res = httpPostWithCheckCode(url, json, new String[0]);//订单详情与订单跟进详情入参json一样

        return JSON.parseObject(res).getJSONObject("data").getJSONArray("list");
    }

    /**
     * 华成裕，报备-到场-成交，订单状态：正常 ，核验状态：未核验，修改状态为正常，查询订单详情和订单列表，该订单状态为：正常，已核验
     * 无异常环节
     * 18831111111
     * 333333333333333333
     */
    @Test
    public void normal2Normal() {
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        try {
            // 创建订单

            JSONObject result = createOrder("333333333333333333", "18831111111", "SIGN");
            String orderId = JSONPath.eval(result, "$.data.order_id").toString();

            result = orderDetail(orderId);

            checkOrder(result, 1, false);

            // 审核订单

            orderAudit(orderId, 1, 1, 1);

            // 查询订单
            result = orderDetail(orderId);

            checkOrder(result, 1, true);

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, caseName, caseName, "报备-到场-成交，订单状态：正常 ，核验状态：已核验");
        }
    }

    /**
     * 华成裕，报备-到场-成交，订单状态：正常 ，核验状态：未核验，修改状态为风险，查询订单详情和订单列表，该订单状态为：风险，已核验
     * 无异常环节
     * 18831111111
     * 333333333333333333
     */
    @Test
    public void normal2risk() {
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        try {

            // 创建订单
            JSONObject result = createOrder("333333333333333333", "18831111111", "SIGN");
            String orderId = JSONPath.eval(result, "$.data.order_id").toString();

            // 查询订单
            result = orderDetail(orderId);

            checkOrder(result, 1, false);

            // 审核订单
            orderAudit(orderId, 0, 2, 0);

            // 查询订单
            result = orderDetail(orderId);

            checkOrder(result, 3, true);

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, caseName, caseName, "报备-到场-成交，订单状态：正常 ，核验状态：已核验");
        }
    }

    /**
     * 傅天宇，现场报备-成交，订单状态：风险，核验状态：未核验。修改状态为风险，查询订单详情和订单列表，该订单状态为：风险，已核验
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
            checkRiskStep(orderId, "REPORT");

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, caseName, caseName, "傅天宇，现场报备-成交，订单状态：风险");
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
            String phone = "14111111135";
            JSONObject result = createOrder("111111111111111114", phone, "SIGN");
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
            checkRiskStepPhoneChng(orderId, phone);

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, caseName, caseName, "报备-到场-修改报备手机号-创单，订单状态：风险 ，核验状态：未核验");
        }
    }

    /**廖祥茹, 19811111111 报备男，到场为女，成单后报环节异常，订单状态：正常**/
    @Test
    public void diffGender() {

        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        try {
            // 创建订单
            JSONObject result = createOrder("222222222222222222", "19811111111", "SIGN");
            String orderId = JSONPath.eval(result, "$.data.order_id").toString();

            // 校验订单的风险状态
            result = orderDetail(orderId);

            checkOrder(result, 1, false);

            //校验顾客性别冲突时，环节异常
            JSONArray logSteps = orderStepLog(orderId);
            checkConflict(logSteps, orderId, true);

//            校验环节异常
            checkRiskStep(orderId, "GENDER_AUDIT");

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, caseName, caseName, "报备时性别和身份证性别不一致！");
        }
    }

    /**新建订单的首次到访时间与该订单的第一笔订单时间一致**/
    @Test(dataProvider = "ALL_DEAL_IDCARD_PHONE")
    public void orderFirstAppearTimeEquals(String phone, String idCard, String customerName, String firstAppearTime) {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName + "-" + phone;
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
            saveData(aCase, ciCaseName, caseName, "同一个人的不同订单的首次到访时间是否一致");
        }
    }

    /**正常订单，报备时间 < 到场时间**/
    @Test
    public void normalOrderTimeTest() {
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        try {
            // 创建订单
            String phone = "17800000002";
            JSONObject result = createOrder("111111111111111111", phone, "SIGN");
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
            saveData(aCase, caseName, caseName, "正常订单的首次出现时间<报备时间<签约时间");
        }
    }

    /**报备风险订单，报备时间 > 到场时间**/
    @Test
    public void riskOrderTimeTest() {
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        try {
            // 创建订单
            String phone = "18888811111";
            JSONObject result = createOrder("333333333333333335", phone, "SIGN");
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
            saveData(aCase, caseName, caseName, "正常订单的首次出现时间<报备时间<签约时间");
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

    private void checkRiskStep(String orderId, String stepType) throws Exception {
        JSONArray steps = orderStepLog(orderId);
        for (int i = 0; i < steps.size(); i++) {
            JSONObject oneStep = steps.getJSONObject(i);
            if (stepType.equals(oneStep.getString("step_type"))) {
                if (!oneStep.getBooleanValue("is_in_risk")) {
                    String stepName = oneStep.getString("step_name");
                    String stepNameContent = oneStep.getString("step_name_content");
                    throw new Exception("orderId[" + orderId + "],没有将[" + stepName + "]环节标记为异常。异常信息[" + stepNameContent + "]");
                }
            }
        }
    }

    private void checkRiskStepPhoneChng(String orderId, String phone) throws Exception {
        JSONArray steps = orderStepLog(orderId);
        boolean isLast = true;
        for (int i = steps.size() - 1; i >= 0; i--) {
            JSONObject oneStep = steps.getJSONObject(i);
            if ("CUSTOMER_PHONE_CHANGE".equals(oneStep.getString("step_type"))) {
                if (!oneStep.getBooleanValue("is_in_risk")) {
                    String stepName = oneStep.getString("step_name");
                    String stepNameContent = oneStep.getString("step_name_content");
                    throw new Exception("orderId[" + orderId + "],没有将[" + stepName + "]环节标记为异常。异常信息[" + stepNameContent + "]");
                }

                if (isLast) {
                    String stepNameContent = oneStep.getString("step_name_content");
                    String lastPhone = stepNameContent.substring(stepNameContent.indexOf(">") + 1);
                    if (!phone.equals(lastPhone)) {
                        throw new Exception("最后一个更新手机号的环节显示的手机号[" + lastPhone + "],与当前手机号[" + phone + "]不符！");
                    }
                    isLast = false;
                }
            }
        }
    }


    private void checkRiskStepName(String orderId, String name) throws Exception {
        JSONArray steps = orderStepLog(orderId);

        boolean isLast = true;

        for (int i = steps.size() - 1; i >= 0; i--) {
            JSONObject oneStep = steps.getJSONObject(i);
            String stepNameContent = oneStep.getString("step_name_content");

            if (stepNameContent.contains("顾客姓名多次修改")) {
                if (!oneStep.getBooleanValue("is_in_risk")) {
                    throw new Exception("orderId[" + orderId + "]没有将“顾客姓名多次修改”环节标记为异常！");
                }

                if (isLast) {
                    int index = stepNameContent.indexOf(">");
                    String lastName = stepNameContent.substring(index + 1, index + 1 + name.length());
                    if (!name.equals(lastName)) {
                        throw new Exception("最后一个更新姓名的环节显示的姓名[" + lastName + "],与当前姓名[" + name + "]不符！");
                    }
                    isLast = false;
                }
            }
        }
    }

    private void checkRiskStepAdviser(String orderId, String adviser) throws Exception {
        JSONArray steps = orderStepLog(orderId);

        boolean isLast = true;

        for (int i = steps.size() - 1; i >= 0; i--) {
            JSONObject oneStep = steps.getJSONObject(i);
            String stepNameContent = oneStep.getString("step_name_content");

            if (stepNameContent.contains("置业顾问多次修改")) {
                if (!oneStep.getBooleanValue("is_in_risk")) {
                    throw new Exception("orderId[" + orderId + "]没有将“置业顾问多次修改”环节标记为异常！");
                }

                if (isLast) {
                    int index = stepNameContent.indexOf(">");
                    String lastAdviser = stepNameContent.substring(index + 1, index + 1 + adviser.length());
                    if (!adviser.equals(lastAdviser)) {
                        throw new Exception("最后一个更新置业顾问的环节显示的置业顾问为[" + lastAdviser + "],与当前置业顾问[" + adviser + "]不符！");
                    }
                    isLast = false;
                }
            }

        }

    }

//    private void checkRiskStep1(String orderId) throws Exception {
//        JSONArray steps = orderStepLog(orderId);
//        for (int i = 0; i < steps.size(); i++) {
//            JSONObject oneStep = steps.getJSONObject(i);
//            String stepNameContent = oneStep.getString("step_name_content");
//            String stepName = oneStep.getString("step_name");
//
//            if (stepName.contains("顾客手机号更新")) {
//                if (!oneStep.getBooleanValue("is_in_risk")) {
//                    throw new Exception("orderId[" + orderId + "]没有将“顾客手机号更新”环节标记为异常！");
//                }
//            } else if (stepName.contains("顾客从未出现")) {
//                if (!oneStep.getBooleanValue("is_in_risk")) {
//                    throw new Exception("orderId[" + orderId + "]没有将“顾客从未出现”环节标记为异常！");
//                }
//            } else if (stepName.contains("顾客性别信息冲突")) {
//                if (!oneStep.getBooleanValue("is_in_risk")) {
//                    throw new Exception("orderId[" + orderId + "]没有将“顾客性别信息冲突”环节标记为异常！");
//                }
//            } else if (stepNameContent.contains("报备时间晚于首次到访时间")) {
//                if (!oneStep.getBooleanValue("is_in_risk")) {
//                    throw new Exception("orderId[" + orderId + "]没有将“报备时间晚于首次到访时间”环节标记为异常！");
//                }
//            } else if (stepNameContent.contains("置业顾问多次修改")) {
//                if (!oneStep.getBooleanValue("is_in_risk")) {
//                    throw new Exception("orderId[" + orderId + "]没有将“置业顾问多次修改”环节标记为异常！");
//                }
//            } else if (stepNameContent.contains("顾客姓名多次修改")) {
//                if (!oneStep.getBooleanValue("is_in_risk")) {
//                    throw new Exception("orderId[" + orderId + "]没有将“顾客姓名多次修改”环节标记为异常！");
//                }
//            }
//
//        }
//    }

    private void setBasicParaToDB(Case aCase, String ciCaseName, String caseName, String caseDesc) {
        aCase.setApplicationId(APP_ID);
        aCase.setConfigId(CONFIG_ID);
        aCase.setCaseName(caseName);
        aCase.setCaseDescription(caseDesc);
        aCase.setCiCmd(CI_CMD + ciCaseName);
        aCase.setQaOwner("于海生");
        aCase.setExpect("code==1000");
        aCase.setResponse(response);

        if (!StringUtils.isEmpty(failReason) || !StringUtils.isEmpty(aCase.getFailReason())) {
            aCase.setFailReason(failReason);
        } else {
            aCase.setResult("PASS");
        }
    }

    private void saveData(Case aCase, String ciCaseName, String caseName, String caseDescription) {
        setBasicParaToDB(aCase, ciCaseName, caseName, caseDescription);
        qaDbUtil.saveToCaseTable(aCase);
        if (!StringUtils.isEmpty(aCase.getFailReason())) {
            logger.error(aCase.getFailReason());
            dingPush("飞单日常 \n" + aCase.getCaseDescription() + " \n" + aCase.getFailReason());
        }
    }

    private void dingPush(String msg) {
        if (DEBUG.trim().toLowerCase().equals("false")) {
            AlarmPush alarmPush = new AlarmPush();

            alarmPush.setDingWebhook(DingWebhook.ONLINE_OPEN_MANAGEMENT_PLATFORM_GRP);

            alarmPush.onlineRgn(msg);
            this.FAIL = true;
        }
        Assert.assertNull(aCase.getFailReason());
    }

    private void dingPushFinal() {
        if (DEBUG.trim().toLowerCase().equals("false") && FAIL) {
            AlarmPush alarmPush = new AlarmPush();

            alarmPush.setDingWebhook(DingWebhook.ONLINE_OPEN_MANAGEMENT_PLATFORM_GRP);

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

    @DataProvider(name = "ALL_DEAL_IDCARD_PHONE")
    private static Object[][] dealIdCardPhone() {
        return new Object[][]{
                new Object[]{
                        "17800000002", "111111111111111111", "于海生", "2019-12-13 13:44:26"
                },
                new Object[]{
                        "19811111111", "222222222222222222", "廖祥茹", "2019-12-13 13:40:53"
                },
                new Object[]{
                        "18831111111", "333333333333333333", "华成裕", "2019-12-13 15:27:22"
                },
                new Object[]{
                        "18888811111", "333333333333333335", "傅天宇", "2019-12-13 15:05:53"
                },
                new Object[]{
                        "14111111135", "111111111111111114", "李俊延", "2019-12-17 16:51:31"
                }
        };
    }

    @DataProvider(name = "ALL_DEAL_PHONE")
    private static Object[] dealPhone() {
        return new Object[]{
                "17800000002",
                "19811111111",
                "18831111111",
                "18888811111",
                "14111111135"
        };
    }
}

