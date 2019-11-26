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
import org.apache.commons.lang.text.StrSubstitutor;
import org.apache.http.Header;
import org.apache.http.client.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.LocalDate;
import java.util.Random;
import java.util.UUID;

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

    private String getIpPort() {
        return "http://dev.store.winsenseos.com";
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
                .userAgent(userAgent)
                .authorization(authorization)
                .build();

        config = HttpConfig.custom()
                .headers(headers)
                .client(client);
    }

    private String httpPost(String path, String json, String... checkColumnNames) throws Exception {
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
            httpPost(path, json, checkColumnName);

        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, caseName, "校验shop");
        }

    }

    private Object getShopId() {
        return "4116";
    }

    private static final String ADD_ORDER = "/risk/order/createOrder";
    private static final String SEARCH_ORDER = "/risk/order/detail";
    private static final String AUDIT_ORDER = "/risk/order/status/audit";
    private static final String CUSTOMER_LIST = "/risk/customer/list";
    private static final String CUSTOMER_DETAIL = "/risk/customer/detail";
    private static final String CUSTOMER_APPEAR_LIST = "/risk/customer/day/appear/list";
    private static final String ORDER_LIST = "/risk/order/list";
    private static final String ORDER_DETAIL = "/risk/order/detail";
    private static final String ORDER_STEP_LOG = "/risk/order/step/log";
    private static final String CHANNEL_STAFF_PAGE = "/risk/channel/staff/page";
    private static final String CUSTOMER_INSERT = "/risk/customer/insert";
    private static final String CHANNEL_LIST = "/risk/channel/page";

    private static String CREATE_ORDER_JSON = "{\"request_id\":\"${requestId}\"," +
            "\"shop_id\":${shopId},\"id_card\":\"${idCard}\",\"phone\":\"${phone}\"," +
            "\"order_stage\":\"${orderStage}\"}";

    private static String DETAIL_ORDER_JSON = "{\"request_id\":\"${requestId}\"," +
            "\"shop_id\":${shopId},\"order_id\":\"${orderId}\"}";

    private static String AUDIT_ORDER_JSON = "{\"is_customer_introduced\":${isCustomerIntroduced}," +
            "\"introduce_checked_person\":\"${introduceCheckedPerson}\"," +
            "\"is_channel_staff_show_dialog\":${isChannelStaffShowDialog}," +
            "\"dialog_path\":\"FEIDAN/undefined/30830a3179a3d75c634335a7104553fa\"," +
            "\"shop_id\":${shopId}," +
            "\"order_id\":\"${orderId}\"}";

    private static String CUSTOMER_DETAIL_JSON = "{\"cid\":\"${cid}\"," +
            "\"shop_id\":${shopId}}";

    private static String CUSTOMER_LIST_JSON = "{\"search_type\":\"${searchType}\"," +
            "\"shop_id\":${shopId},\"page\":\"1\",\"size\":\"60\"}";

    private static String CUSTOMER_APPEAR_LIST_JSON = "{\"start_time\":\"${startTime}\",\"end_time\":\"${endTime}\",\"cid\":\"${cid}\"," +
            "\"shop_id\":${shopId}}";

    private static String ORDER_LIST_JSON = "{\"shop_id\":${shopId},\"page\":\"1\",\"page_size\":\"500\"}";

    private static String ORDER_DETAIL_JSON = "{\"order_id\":\"${orderId}\"," +
            "\"shop_id\":${shopId}}";

    private static String ORDER_STEP_LOG_JSON = ORDER_DETAIL_JSON;

    private static String CHANNEL_STAFF_PAGE_JSON = "{\"channel_id\":\"${channelId}\"," +
            "\"shop_id\":${shopId},\"page\":\"1\",\"size\":\"60\"}";

    private static String CUSTOMER_INSERT_JSON = "{\"shop_id\":\"${shopId}\",\"channel_id\":${channelId}," +
            "\"channel_staff_id\":\"${channelStaffId}\",\"adviser_id\":\"${adviserId}\"," +
            "\"customer_name\":\"${customerName}\",\"phone\":\"${phone}\"}";

    private static String CHANNEL_LIST_JSON = "{\"shop_id\":${shopId},\"page\":\"1\",\"page_size\":\"500\"}";

    @Test(dataProvider = "SEARCH_TYPE")
    public void customerListEqualsDetail(String searchType) {
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName() + "-" + searchType;

        try {
            String json = StrSubstitutor.replace(
                    CUSTOMER_LIST_JSON, ImmutableMap.builder()
                            .put("shopId", getShopId())
                            .put("searchType", searchType)
                            .build()
            );

            String res = httpPost(CUSTOMER_LIST, json, new String[0]);

            JSONObject result = JSON.parseObject(res);

            JSONArray list = result.getJSONObject("data").getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                JSONObject listData = list.getJSONObject(i);
                String cidList = listData.getString("cid");

                String ageGroupList = getValue(listData, "age_group");
                String adviserNameList = getValue(listData, "adviser_name");
                String customerNameList = getValue(listData, "customer_name");
                String gender = getValue(listData, "gender");
                String firstAppearTimeList = getValue(listData, "first_appear_time");
                String lastVisitTimeList = getValue(listData, "last_visit_time");
                String phoneList = getValue(listData, "phone");

                json = StrSubstitutor.replace(
                        CUSTOMER_DETAIL_JSON, ImmutableMap.builder()
                                .put("shopId", getShopId())
                                .put("cid", cidList)
                                .build()
                );

                res = httpPost(CUSTOMER_DETAIL, json, new String[0]);

                result = JSON.parseObject(res);

                JSONObject data = result.getJSONObject("data");

                compareValue(data, "顾客", cidList, "adviser_name", adviserNameList, "置业顾问");
                compareValue(data, "顾客", cidList, "customer_name", customerNameList, "顾客姓名");
                compareValue(data, "顾客", cidList, "age_group", ageGroupList, "年龄段");
                compareValue(data, "顾客", cidList, "gender", gender, "性别");
                compareValue(data, "顾客", cidList, "first_appear_time", firstAppearTimeList, "首次出现时间");
                compareValue(data, "顾客", cidList, "last_appear_time", lastVisitTimeList, "最后出现时间");
                compareValue(data, "顾客", cidList, "phone", phoneList, "顾客手机号码");
            }

        } catch (AssertionError e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.getMessage();
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
            String json = StrSubstitutor.replace(
                    CUSTOMER_LIST_JSON, ImmutableMap.builder()
                            .put("shopId", getShopId())
                            .put("searchType", searchType)
                            .build()
            );

            String res = httpPost(CUSTOMER_LIST, json, new String[0]);

            JSONObject result = JSON.parseObject(res);

            JSONArray list = result.getJSONObject("data").getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                JSONObject data = list.getJSONObject(i);
                String cidList = data.getString("cid");

                //顾客详情
                json = StrSubstitutor.replace(
                        CUSTOMER_DETAIL_JSON, ImmutableMap.builder()
                                .put("shopId", getShopId())
                                .put("cid", cidList)
                                .build()
                );

                res = httpPost(CUSTOMER_DETAIL, json, new String[0]);

                data = JSON.parseObject(res).getJSONObject("data");

                String firstAppearTime = data.getString("first_appear_time");

                //未到场顾客没有出现时间
                if (firstAppearTime != null && !"".equals(firstAppearTime)) {
                    long firstAppearTimeList = Long.valueOf(firstAppearTime);
                    String firstDate = dateTimeUtil.timestampToDate("yyyy-MM-dd", firstAppearTimeList);
                    String firstMinute = dateTimeUtil.timestampToDate("HH:mm", firstAppearTimeList);

                    long lastAppearTimeList = Long.valueOf(getValue(data, "last_appear_time"));
                    String lastDate = dateTimeUtil.timestampToDate("yyyy-MM-dd", lastAppearTimeList);
                    String lastMinute = dateTimeUtil.timestampToDate("HH:mm", lastAppearTimeList);

                    json = StrSubstitutor.replace(
                            CUSTOMER_APPEAR_LIST_JSON, ImmutableMap.builder()
                                    .put("shopId", getShopId())
                                    .put("cid", cidList)
                                    .put("startTime", LocalDate.now().minusDays(30).toString())
                                    .put("endTime", LocalDate.now().toString())
                                    .build()
                    );

                    res = httpPost(CUSTOMER_APPEAR_LIST, json, new String[0]);

                    data = JSON.parseObject(res);

                    JSONArray appearList = data.getJSONObject("data").getJSONArray("list");
                    JSONObject first = appearList.getJSONObject(0);
                    String firstdateAppearList = getValue(first, "date");
                    if (!firstdateAppearList.equals(firstDate)) {
                        throw new Exception("cid：" + cidList + ",出现日期列表中最早出现日期：" + firstdateAppearList + ",详情中：" + firstDate);
                    }
                    String firstMinuteAppearList = dateTimeUtil.timestampToDate("HH:mm", Long.valueOf(getValue(first, "first_appear_timestamp")));
                    if (!firstMinute.equals(firstMinuteAppearList)) {
                        throw new Exception("cid：" + cidList + ",出现日期列表中最早出现时间：" + firstDate + " " + firstMinuteAppearList +
                                ",详情中：" + firstDate + " " + firstMinute);
                    }

                    JSONObject last = appearList.getJSONObject(appearList.size() - 1);
                    String lastdateAppearList = getValue(last, "date");
                    if (!lastdateAppearList.equals(lastDate)) {
                        throw new Exception("cid：" + cidList + ",出现日期列表中最晚出现日期：" + lastdateAppearList + ",详情中：" + lastDate);
                    }
                    String lastMinuteAppearList = dateTimeUtil.timestampToDate("HH:mm", Long.valueOf(getValue(last, "last_appear_timestamp")));
                    if (!lastMinute.equals(lastMinuteAppearList)) {
                        throw new Exception("cid：" + cidList + ",出现日期列表中最晚出现时间：" + lastDate + " " + lastMinuteAppearList +
                                ",详情中：" + lastDate + " " + lastMinute);
                    }
                }
            }

        } catch (AssertionError e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, caseName, "顾客列表与顾客详情中的信息一致");
        }
    }

    @Test
    public void dealListEqualsDetail() {
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        try {
            // 订单列表
            String json = StrSubstitutor.replace(ORDER_LIST_JSON, ImmutableMap.builder()
                    .put("shopId", getShopId())
                    .build()
            );
            String[] checkColumnNames = {};
            String res = httpPost(ORDER_LIST, json, checkColumnNames);

            JSONArray list = JSON.parseObject(res).getJSONObject("data").getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
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

                json = StrSubstitutor.replace(ORDER_DETAIL_JSON, ImmutableMap.builder()
                        .put("shopId", getShopId())
                        .put("orderId", orderId)
                        .build()
                );
                res = httpPost(ORDER_DETAIL, json, new String[0]);

                JSONObject data = JSON.parseObject(res).getJSONObject("data");
                compareValue(data, "订单", orderId, "customer_name", customerName, "顾客姓名");
                compareValue(data, "订单", orderId, "adviser_name", adviserName, "置业顾问");
                compareValue(data, "订单", orderId, "channel_name", channelName, "渠道名称");
                compareValue(data, "订单", orderId, "order_status", status, "订单状态");
                compareValue(data, "订单", orderId, "is_audited", isAudited, "是否审核");

                compareOrderTimeValue(data, "first_appear_time", orderId, firstAppearTime, "首次出现时间");
                compareOrderTimeValue(data, "report_time", orderId, reportTime, "报备时间");
                compareOrderTimeValue(data, "sign_time", orderId, signTime, "认筹认购时间");
                compareOrderTimeValue(data, "deal_time", orderId, dealTime, "成交时间");
            }
        } catch (AssertionError e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.getMessage();
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
            String json = StrSubstitutor.replace(ORDER_LIST_JSON, ImmutableMap.builder()
                    .put("shopId", getShopId())
                    .build()
            );
            String[] checkColumnNames = {};
            String res = httpPost(ORDER_LIST, json, checkColumnNames);

            JSONArray list = JSON.parseObject(res).getJSONObject("data").getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                String orderId = getValue(list.getJSONObject(i), "order_id");

                json = StrSubstitutor.replace(ORDER_DETAIL_JSON, ImmutableMap.builder()
                        .put("shopId", getShopId())
                        .put("orderId", orderId)
                        .build()
                );

                res = httpPost(ORDER_STEP_LOG, json, new String[0]);//订单详情与订单跟进详情入参json一样

                JSONArray logList = JSON.parseObject(res).getJSONObject("data").getJSONArray("list");

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
                            throw new Exception("步骤名称出错--返回名称为：" + stepNameContent + ",期待：认购/认筹或成交");
                        }
                    } else if ("STAGE_CHANGE".equals(stepType)) {
                        dealTime = singleStep.getString("time_str");
                    }
                }

                res = httpPost(ORDER_DETAIL, json, new String[0]);
                JSONObject detailData = JSON.parseObject(res).getJSONObject("data");

                compareOrderTimeValue(detailData, "first_appear_time", orderId, firstAppearTime, "首次出现时间");
                compareOrderTimeValue(detailData, "report_time", orderId, reportTime, "报备时间");
                compareOrderTimeValue(detailData, "sign_time", orderId, signTime, "认筹认购时间");
                compareOrderTimeValue(detailData, "deal_time", orderId, dealTime, "成交时间");
            }

        } catch (AssertionError e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, caseName, "订单详情与订单列表中信息是否一致");
        }
    }

    //    新建报备,测试报备数量
    @Test
    public void channelStaffReportNum() {
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        try {
            //取出渠道员工宫二的报备数
            String json = StrSubstitutor.replace(CHANNEL_STAFF_PAGE_JSON, ImmutableMap.builder()
                    .put("shopId", getShopId())
                    .put("channelId", "5")      //测试【勿动】
                    .build()
            );
            String res = httpPost(CHANNEL_STAFF_PAGE, json, new String[0]);

            int channelStaffTotalReportBefore = getChannelStaffReportNum(res);

//            新建报备
            Random random = new Random();
            long num = 17700000000L + random.nextInt(99999999);
            json = StrSubstitutor.replace(CUSTOMER_INSERT_JSON, ImmutableMap.builder()
                    .put("shopId", getShopId())
                    .put("channelId", "5") //测试【勿动】
                    .put("channelStaffId", "12")//宫二
                    .put("adviserId", "14")
                    .put("phone", num)
                    .put("customerName", "测试数量")
                    .build()
            );

            httpPost(CUSTOMER_INSERT, json, new String[0]);

            //取出渠道员工宫二的报备数
            json = StrSubstitutor.replace(CHANNEL_STAFF_PAGE_JSON, ImmutableMap.builder()
                    .put("shopId", getShopId())
                    .put("channelId", "5")      //测试【勿动】
                    .build()
            );
            res = httpPost(CHANNEL_STAFF_PAGE, json, new String[0]);

            int channelStaffTotalReportAfter = getChannelStaffReportNum(res);

            //比较报备前后渠道员工的报备数
            if (channelStaffTotalReportAfter - 1 != channelStaffTotalReportBefore) {
                throw new Exception("新建报备后渠道员工--宫二的累计报备数没有加1。报备前：" +
                        channelStaffTotalReportBefore + ",报备后：" + channelStaffTotalReportAfter + "。顾客手机号:" + num);
            }

        } catch (AssertionError e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, caseName, "H5报备后，业务员累计报备数是否增加");
        }
    }

    @Test
    public void channelTotalEqualsStaffTotalS() {
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        try {

            String json = StrSubstitutor.replace(CHANNEL_LIST_JSON, ImmutableMap.builder()
                    .put("shopId", getShopId())
                    .build()
            );
            String res = httpPost(CHANNEL_LIST, json, new String[0]);

            JSONArray channelList = JSON.parseObject(res).getJSONObject("data").getJSONArray("list");

            int channelNum = 0;
            int staffNum = 0;

            for (int i = 0; i < channelList.size(); i++) {
                JSONObject singleChannel = channelList.getJSONObject(i);
                channelNum = singleChannel.getInteger("total_customers");
                String channelName = singleChannel.getString("channel_name");

                json = StrSubstitutor.replace(CHANNEL_STAFF_PAGE_JSON, ImmutableMap.builder()
                        .put("shopId", getShopId())
                        .put("channelId", singleChannel.getString("channel_id"))
                        .build()
                );

                res = httpPost(CHANNEL_STAFF_PAGE, json, new String[0]);
                JSONArray staffList = JSON.parseObject(res).getJSONObject("data").getJSONArray("list");
                for (int j = 0; j < staffList.size(); j++) {
                    JSONObject singleStaff = staffList.getJSONObject(i);
                    staffNum += singleStaff.getInteger("total_report");
                }

                if (staffNum != channelNum) {
                    throw new Exception("渠道: " + channelName + ",渠道累计报备数：" + channelNum + "，业务员累计报备数之和：" + staffNum);
                }
            }
        } catch (AssertionError e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, caseName, "渠道的累计带客数等于各个业务员的累计报备数之和");
        }
    }

    public int getChannelStaffReportNum(String res) {
        int reportNum = 0;

        JSONArray channelStaffList = JSON.parseObject(res).getJSONObject("data").getJSONArray("list");
        for (int i = 0; i < channelStaffList.size(); i++) {
            JSONObject singleStaff = channelStaffList.getJSONObject(i);
            String staffId = singleStaff.getString("id");
            if ("12".equals(staffId)) {//宫二的员工id
                reportNum = singleStaff.getInteger("total_report");
            }
        }

        return reportNum;
    }

    public void compareOrderTimeValue(JSONObject data, String key, String orderId, String valueExpect, String comment) throws Exception {
        String valueStr = data.getString(key);
        if (valueStr != null && !"".equals(valueStr)) {
            String firstStr = dateTimeUtil.timestampToDate("yyyy-MM-dd HH:mm:ss", Long.valueOf(valueStr));
            if (!firstStr.equals(valueExpect)) {
                throw new Exception("订单id：" + orderId + "," + comment + ",列表中：" + valueExpect + ",详情中：" + firstStr);
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
            String json = StrSubstitutor.replace(CREATE_ORDER_JSON, ImmutableMap.builder()
                    .put("requestId", UUID.randomUUID().toString())
                    .put("shopId", getShopId())
                    .put("idCard", "111111111111111111")
                    .put("phone", "18811111111")
                    .put("orderStage", "SIGN")
                    .build()
            );
            String res = httpPost(ADD_ORDER, json, new String[0]);

            // 查询订单
            JSONObject result = JSON.parseObject(res);
            Object id = JSONPath.eval(result, "$.data.order_id");
            json = StrSubstitutor.replace(DETAIL_ORDER_JSON, ImmutableMap.builder()
                    .put("requestId", UUID.randomUUID().toString())
                    .put("shopId", getShopId())
                    .put("orderId", id)
                    .put("orderStage", "SIGN")
                    .build()
            );
            String[] checkColumnNames = new String[]{"order_status", "is_need_audit"};
            res = httpPost(SEARCH_ORDER, json, checkColumnNames);
            result = JSON.parseObject(res);

            Object orderStatus = JSONPath.eval(result, "$.data.order_status");
            Assert.assertEquals(1, orderStatus, "订单状态不正常");

            Object isNeedAudit = JSONPath.eval(result, "$.data.is_need_audit");
            Assert.assertEquals(false, isNeedAudit, "核验状态不正常");

        } catch (AssertionError e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.getMessage();
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
            String json = StrSubstitutor.replace(CREATE_ORDER_JSON, ImmutableMap.builder()
                    .put("requestId", UUID.randomUUID().toString())
                    .put("shopId", getShopId())
                    .put("idCard", "111111111111111113")
                    .put("phone", "12111111119")
                    .put("orderStage", "SIGN")
                    .build()
            );
            String[] checkColumnNames = {"order_id"};
            String res = httpPost(ADD_ORDER, json, checkColumnNames);

            JSONObject result = JSON.parseObject(res);
            Object id = JSONPath.eval(result, "$.data.order_id");

            // 查询订单
            json = StrSubstitutor.replace(DETAIL_ORDER_JSON, ImmutableMap.builder()
                    .put("requestId", UUID.randomUUID().toString())
                    .put("shopId", getShopId())
                    .put("orderId", id)
                    .build()
            );
            checkColumnNames = new String[]{"order_status", "is_audited"};
            res = httpPost(SEARCH_ORDER, json, checkColumnNames);
            result = JSON.parseObject(res);

            Object orderStatus = JSONPath.eval(result, "$.data.order_status");
            Assert.assertEquals(1, orderStatus, "订单状态不正常");

            Object isNeedAudit = JSONPath.eval(result, "$.data.is_audited");
            Assert.assertEquals(false, isNeedAudit, "核验状态不正常");

            // 审核订单
            json = StrSubstitutor.replace(AUDIT_ORDER_JSON, ImmutableMap.builder()
                    .put("requestId", UUID.randomUUID().toString())
                    .put("shopId", getShopId())
                    .put("orderId", id)
                    .put("isCustomerIntroduced", 1)
                    .put("introduceCheckedPerson", 1)
                    .put("isChannelStaffShowDialog", 1)
                    .build()
            );
            checkColumnNames = new String[]{};
            httpPost(AUDIT_ORDER, json, checkColumnNames);

            // 查询订单
            json = StrSubstitutor.replace(DETAIL_ORDER_JSON, ImmutableMap.builder()
                    .put("requestId", UUID.randomUUID().toString())
                    .put("shopId", getShopId())
                    .put("orderId", id)
                    .build()
            );
            checkColumnNames = new String[]{"order_status", "is_audited"};
            res = httpPost(SEARCH_ORDER, json, checkColumnNames);
            result = JSON.parseObject(res);

            orderStatus = JSONPath.eval(result, "$.data.order_status");
            Assert.assertEquals(1, orderStatus, "订单状态不正常");

            isNeedAudit = JSONPath.eval(result, "$.data.is_audited");
            Assert.assertEquals(true, isNeedAudit, "核验状态不正常");
        } catch (AssertionError e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.getMessage();
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
            String json = StrSubstitutor.replace(CREATE_ORDER_JSON, ImmutableMap.builder()
                    .put("requestId", UUID.randomUUID().toString())
                    .put("shopId", getShopId())
                    .put("idCard", "111111111111111113")
                    .put("phone", "12111111119")
                    .put("orderStage", "SIGN")
                    .build()
            );
            String[] checkColumnNames = {"order_id"};
            String res = httpPost(ADD_ORDER, json, checkColumnNames);

            JSONObject result = JSON.parseObject(res);
            Object id = JSONPath.eval(result, "$.data.order_id");

            // 查询订单
            json = StrSubstitutor.replace(DETAIL_ORDER_JSON, ImmutableMap.builder()
                    .put("requestId", UUID.randomUUID().toString())
                    .put("shopId", getShopId())
                    .put("orderId", id)
                    .build()
            );
            checkColumnNames = new String[]{"order_status", "is_audited"};
            res = httpPost(SEARCH_ORDER, json, checkColumnNames);
            result = JSON.parseObject(res);

            Object orderStatus = JSONPath.eval(result, "$.data.order_status");
            Assert.assertEquals(1, orderStatus, "订单状态不正常");

            Object isNeedAudit = JSONPath.eval(result, "$.data.is_audited");
            Assert.assertEquals(false, isNeedAudit, "核验状态不正常");

            // 审核订单
            json = StrSubstitutor.replace(AUDIT_ORDER_JSON, ImmutableMap.builder()
                    .put("requestId", UUID.randomUUID().toString())
                    .put("shopId", getShopId())
                    .put("orderId", id)
                    .put("isCustomerIntroduced", 0)
                    .put("introduceCheckedPerson", 2)
                    .put("isChannelStaffShowDialog", 0)
                    .build()
            );
            checkColumnNames = new String[]{};
            res = httpPost(AUDIT_ORDER, json, checkColumnNames);

            // 查询订单
            json = StrSubstitutor.replace(DETAIL_ORDER_JSON, ImmutableMap.builder()
                    .put("requestId", UUID.randomUUID().toString())
                    .put("shopId", getShopId())
                    .put("orderId", id)
                    .build()
            );
            checkColumnNames = new String[]{"order_status", "is_audited"};
            res = httpPost(SEARCH_ORDER, json, checkColumnNames);
            result = JSON.parseObject(res);

            orderStatus = JSONPath.eval(result, "$.data.order_status");
            Assert.assertEquals(3, orderStatus, "订单状态不正常");

            isNeedAudit = JSONPath.eval(result, "$.data.is_audited");
            Assert.assertEquals(true, isNeedAudit, "核验状态不正常");
        } catch (AssertionError e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, caseName, "报备-到场-成交，订单状态：正常 ，核验状态：未核验");
        }
    }

    /**
     * 创单报备，现场报备-成交，订单状态：风险，核验状态：未核验。修改状态为风险，查询订单详情和订单列表，该订单状态为：风险，已核验
     * 12222222229
     * 222222222222222229
     */
    @Test
    public void dealReport() {
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        try {
            // 创建订单
            String json = StrSubstitutor.replace(CREATE_ORDER_JSON, ImmutableMap.builder()
                    .put("requestId", UUID.randomUUID().toString())
                    .put("shopId", getShopId())
                    .put("idCard", "333333333333333335")
                    .put("phone", "18888811111")
                    .put("orderStage", "SIGN")
                    .build()
            );
            String[] checkColumnNames = {"order_id"};
            String res = httpPost(ADD_ORDER, json, checkColumnNames);

            JSONObject result = JSON.parseObject(res);
            Object id = JSONPath.eval(result, "$.data.order_id");

            // 查询订单
            json = StrSubstitutor.replace(DETAIL_ORDER_JSON, ImmutableMap.builder()
                    .put("requestId", UUID.randomUUID().toString())
                    .put("shopId", getShopId())
                    .put("orderId", id)
                    .build()
            );
            checkColumnNames = new String[]{"order_status", "is_audited"};
            res = httpPost(SEARCH_ORDER, json, checkColumnNames);
            result = JSON.parseObject(res);

            Object orderStatus = JSONPath.eval(result, "$.data.order_status");
            Assert.assertEquals(3, orderStatus, "订单状态不正常");

            Object isNeedAudit = JSONPath.eval(result, "$.data.is_audited");
            Assert.assertEquals(false, isNeedAudit, "核验状态不正常");

            // 审核订单
            json = StrSubstitutor.replace(AUDIT_ORDER_JSON, ImmutableMap.builder()
                    .put("requestId", UUID.randomUUID().toString())
                    .put("shopId", getShopId())
                    .put("orderId", id)
                    .put("isCustomerIntroduced", 0)
                    .put("introduceCheckedPerson", 2)
                    .put("isChannelStaffShowDialog", 0)
                    .build()
            );
            checkColumnNames = new String[]{};
            httpPost(AUDIT_ORDER, json, checkColumnNames);

            // 查询订单
            json = StrSubstitutor.replace(DETAIL_ORDER_JSON, ImmutableMap.builder()
                    .put("requestId", UUID.randomUUID().toString())
                    .put("shopId", getShopId())
                    .put("orderId", id)
                    .build()
            );
            checkColumnNames = new String[]{"order_status", "is_audited"};
            res = httpPost(SEARCH_ORDER, json, checkColumnNames);
            result = JSON.parseObject(res);

            orderStatus = JSONPath.eval(result, "$.data.order_status");
            Assert.assertEquals(3, orderStatus, "订单状态不正常");

            isNeedAudit = JSONPath.eval(result, "$.data.is_audited");
            Assert.assertEquals(true, isNeedAudit, "核验状态不正常");
        } catch (AssertionError e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, caseName, "报备-到场-成交，订单状态：正常 ，核验状态：未核验");
        }
    }

    /**
     * 李俊延，报备-到场-修改报备手机号-创单，订单状态：风险 ，核验状态：未核验。修改状态为风险，查询订单详情和订单列表，该订单状态为：风险，已核验
     * 12111111135
     * 111111111111111114
     */
    @Test
    public void risk2risk() {
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        try {
            // 创建订单
            String json = StrSubstitutor.replace(CREATE_ORDER_JSON, ImmutableMap.builder()
                    .put("requestId", UUID.randomUUID().toString())
                    .put("shopId", getShopId())
                    .put("idCard", "111111111111111114")
                    .put("phone", "12111111135")
                    .put("orderStage", "SIGN")
                    .build()
            );
            String res = httpPost(ADD_ORDER, json, new String[0]);

            // 查询订单
            JSONObject result = JSON.parseObject(res);
            Object id = JSONPath.eval(result, "$.data.order_id");
            json = StrSubstitutor.replace(DETAIL_ORDER_JSON, ImmutableMap.builder()
                    .put("requestId", UUID.randomUUID().toString())
                    .put("shopId", getShopId())
                    .put("orderId", id)
                    .build()
            );
            String[] checkColumnNames = new String[]{"order_status"};
            res = httpPost(SEARCH_ORDER, json, checkColumnNames);
            result = JSON.parseObject(res);

            Object orderStatus = JSONPath.eval(result, "$.data.order_status");
            Assert.assertEquals(3, orderStatus, "订单状态不正常");

            Object isNeedAudit = JSONPath.eval(result, "$.data.is_audited");
            Assert.assertEquals(false, isNeedAudit, "核验状态不正常");

            // 审核订单
            json = StrSubstitutor.replace(AUDIT_ORDER_JSON, ImmutableMap.builder()
                    .put("requestId", UUID.randomUUID().toString())
                    .put("shopId", getShopId())
                    .put("orderId", id)
                    .put("isCustomerIntroduced", 0)
                    .put("introduceCheckedPerson", 2)
                    .put("isChannelStaffShowDialog", 0)
                    .build()
            );
            checkColumnNames = new String[]{};
            httpPost(AUDIT_ORDER, json, checkColumnNames);

            // 查询订单
            json = StrSubstitutor.replace(DETAIL_ORDER_JSON, ImmutableMap.builder()
                    .put("requestId", UUID.randomUUID().toString())
                    .put("shopId", getShopId())
                    .put("orderId", id)
                    .build()
            );
            checkColumnNames = new String[]{"order_status", "is_audited"};
            res = httpPost(SEARCH_ORDER, json, checkColumnNames);
            result = JSON.parseObject(res);

            orderStatus = JSONPath.eval(result, "$.data.order_status");
            Assert.assertEquals(3, orderStatus, "订单状态不正常");

            isNeedAudit = JSONPath.eval(result, "$.data.is_audited");
            Assert.assertEquals(true, isNeedAudit, "核验状态不正常");
        } catch (AssertionError e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.getMessage();
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
            String json = StrSubstitutor.replace(CREATE_ORDER_JSON, ImmutableMap.builder()
                    .put("requestId", UUID.randomUUID().toString())
                    .put("shopId", getShopId())
                    .put("idCard", "111111111111111115")
                    .put("phone", "12111111311")
                    .put("orderStage", "SIGN")
                    .build()
            );
            String[] checkColumnNames = {};
            String res = httpPost(ADD_ORDER, json, checkColumnNames);

            // 查询订单
            JSONObject result = JSON.parseObject(res);
            Object id = JSONPath.eval(result, "$.data.order_id");
            json = StrSubstitutor.replace(DETAIL_ORDER_JSON, ImmutableMap.builder()
                    .put("requestId", UUID.randomUUID().toString())
                    .put("shopId", getShopId())
                    .put("orderId", id)
                    .build()
            );
            checkColumnNames = new String[]{"order_status"};
            res = httpPost(SEARCH_ORDER, json, checkColumnNames);
            result = JSON.parseObject(res);

            Object orderStatus = JSONPath.eval(result, "$.data.order_status");
            Assert.assertEquals(3, orderStatus, "订单状态不正常");

            Object isNeedAudit = JSONPath.eval(result, "$.data.is_audited");
            Assert.assertEquals(false, isNeedAudit, "核验状态不正常");

            // 审核订单
            json = StrSubstitutor.replace(AUDIT_ORDER_JSON, ImmutableMap.builder()
                    .put("requestId", UUID.randomUUID().toString())
                    .put("shopId", getShopId())
                    .put("orderId", id)
                    .put("isCustomerIntroduced", 1)
                    .put("introduceCheckedPerson", 1)
                    .put("isChannelStaffShowDialog", 1)
                    .build()
            );
            checkColumnNames = new String[]{};
            res = httpPost(AUDIT_ORDER, json, checkColumnNames);
            result = JSON.parseObject(res);

            // 查询订单
            json = StrSubstitutor.replace(DETAIL_ORDER_JSON, ImmutableMap.builder()
                    .put("requestId", UUID.randomUUID().toString())
                    .put("shopId", getShopId())
                    .put("orderId", id)
                    .build()
            );
            checkColumnNames = new String[]{"order_status", "is_audited"};
            res = httpPost(SEARCH_ORDER, json, checkColumnNames);
            result = JSON.parseObject(res);

            orderStatus = JSONPath.eval(result, "$.data.order_status");
            Assert.assertEquals(1, orderStatus, "订单状态不正常");

            isNeedAudit = JSONPath.eval(result, "$.data.is_audited");
            Assert.assertEquals(true, isNeedAudit, "核验状态不正常");
        } catch (AssertionError e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, caseName, "顾客到场-H5报备-成交 ，订单状态：风险 ，核验状态：未核验");
        }
    }

    @Test
    public void test() throws Exception {
        String json = StrSubstitutor.replace(ORDER_LIST_JSON, ImmutableMap.builder()
                .put("shopId", getShopId())
                .build()
        );
        String res = httpPost(ORDER_LIST, json, new String[0]);
        JSONObject result = JSON.parseObject(res);

        JSONArray list = result.getJSONObject("data").getJSONArray("list");

        int lianjiaNum = 0;
        int testNum = 0;
        int nonNum = 0;
        int total = 0;

        for (int i = 0; i < list.size(); i++) {
            JSONObject single = list.getJSONObject(i);
            String channelName = single.getString("channel_name");
            total++;
            if ("测试【勿动】".equals(channelName)) {
                testNum++;
            } else if ("链家".equals(channelName)) {
                lianjiaNum++;
            } else {
                nonNum++;
            }
        }

        System.out.println("总数：" + total);

        System.out.println("链家：" + lianjiaNum);
        System.out.println("测试勿动：" + testNum);
        System.out.println("无渠道：" + nonNum);
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
            String json = StrSubstitutor.replace(CREATE_ORDER_JSON, ImmutableMap.builder()
                    .put("requestId", UUID.randomUUID().toString())
                    .put("shopId", getShopId())
                    .put("idCard", "111111111111111116")
                    .put("phone", "16600000003")
                    .put("orderStage", "SIGN")
                    .build()
            );
            String[] checkColumnNames = {};
            String res = httpPost(ADD_ORDER, json, checkColumnNames);

            // 查询订单
            JSONObject result = JSON.parseObject(res);
            Object id = JSONPath.eval(result, "$.data.order_id");
            json = StrSubstitutor.replace(DETAIL_ORDER_JSON, ImmutableMap.builder()
                    .put("requestId", UUID.randomUUID().toString())
                    .put("shopId", getShopId())
                    .put("orderId", id)
                    .build()
            );
            checkColumnNames = new String[]{"order_status"};
            res = httpPost(SEARCH_ORDER, json, checkColumnNames);
            result = JSON.parseObject(res);

            Object orderStatus = JSONPath.eval(result, "$.data.order_status");
            Assert.assertEquals(1, orderStatus, "订单状态不正常");

            Object isNeedAudit = JSONPath.eval(result, "$.data.is_audited");
            Assert.assertEquals(false, isNeedAudit, "核验状态不正常");
        } catch (AssertionError e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.getMessage();
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
            String json = StrSubstitutor.replace(CREATE_ORDER_JSON, ImmutableMap.builder()
                    .put("requestId", UUID.randomUUID().toString())
                    .put("shopId", getShopId())
                    .put("idCard", "111111111111111117")
                    .put("phone", "16600000002")
                    .put("orderStage", "SIGN")
                    .build()
            );
            String[] checkColumnNames = {};
            String res = httpPost(ADD_ORDER, json, checkColumnNames);

            // 查询订单
            JSONObject result = JSON.parseObject(res);
            Object id = JSONPath.eval(result, "$.data.order_id");
            json = StrSubstitutor.replace(DETAIL_ORDER_JSON, ImmutableMap.builder()
                    .put("requestId", UUID.randomUUID().toString())
                    .put("shopId", getShopId())
                    .put("orderId", id)
                    .build()
            );
            checkColumnNames = new String[]{"order_status"};
            res = httpPost(SEARCH_ORDER, json, checkColumnNames);
            result = JSON.parseObject(res);

            Object orderStatus = JSONPath.eval(result, "$.data.order_status");
            Assert.assertEquals(3, orderStatus, "订单状态不正常");

            Object isNeedAudit = JSONPath.eval(result, "$.data.is_audited");
            Assert.assertEquals(false, isNeedAudit, "核验状态不正常");
        } catch (AssertionError e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, caseName, "未到场-报备-成交，订单状态：风险 ，核验状态：未核验");
        }
    }

    private void setBasicParaToDB(Case aCase, String caseName, String caseDesc) {
        aCase.setApplicationId(APP_ID);
        aCase.setConfigId(CONFIG_ID);
        aCase.setCaseName(caseName);
        aCase.setCaseDescription(caseDesc);
        aCase.setCiCmd(CI_CMD + caseName);
        aCase.setQaOwner("于海生");
        aCase.setExpect("订单创建&状态核查");
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
            String[] rd = {"18513118484", "18600872221"};
            alarmPush.alarmToRd(rd);
        }
    }

    @DataProvider(name = "SEARCH_TYPE")
    private static Object[] searchType() {
        return new Object[]{
                "CHANCE", "CHECKED", "REPORTED"
        };
    }
}

