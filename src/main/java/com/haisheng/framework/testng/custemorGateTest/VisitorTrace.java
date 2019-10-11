package com.haisheng.framework.testng.custemorGateTest;

import ai.winsense.retail.scenario.gate.client.ScenarioGateClient;
import ai.winsense.retail.scenario.gate.domain.enumeration.GroupType;
import ai.winsense.retail.scenario.gate.domain.object.DetectImage;
import ai.winsense.retail.scenario.gate.domain.object.Visitor;
import ai.winsense.retail.scenario.gate.domain.response.visitor.GateVisitorRegisterResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.CommonDataStructure.LogMine;
import com.haisheng.framework.util.DateTimeUtil;
import com.haisheng.framework.util.HttpExecutorUtil;
import com.haisheng.framework.util.OssClientUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

public class VisitorTrace {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private LogMine logMine = new LogMine(logger);

    String scope = "3064";
    String appId = "0d28ec728799";
    String deviceId = "6840208903439360";
    String grpName = "vipGrp";

    private String updateCustomerUrl = "http://47.95.69.163/gate/manage/updateCustomer";
    private String updateImageUrl = "http://47.95.69.163/gate/manage/updateCustomerImageOne";
    private String queryCustomerUrl = "http://47.95.69.163/gate/manage/queryCustomer";
    private String genAuthURL = "http://39.106.253.190/administrator/login";

    private String strangerPerfix = "STRANGER@@";
    private String specialPerfix = "SPECIAL@@vipGrp@@";

    private static DateTimeUtil dateTimeUtil = new DateTimeUtil();
    OssClientUtil ossClientUtil = new OssClientUtil();
    private static int num = 0;

    String authorization;
    HashMap<String, String> header = new HashMap();

    public VisitorTrace() throws Exception {
    }

    public void splitModeConfig(String splitMode) throws Exception {

        String url = "http://39.106.253.190/admin/data/nodeServiceConfig/389";

        String json =
                "{\n" +
                        "    \"cloud_config\":{\n" +
                        "        \"merge_interval\":60000,\n" +
                        "        \"auto_merge\":true,\n" +
                        "        \"auto_inc_merge\":true,\n" +
                        "        \"group\":[\n" +
                        "            {\n" +
                        "                \"group_name\":\"vipGrp\",\n" +
                        "                \"threshold\":0.8,\n" +
                        "                \"type\":\"DEFINE\",\n" +
                        "                \"node_id\":\"" + scope + "\"\n" +
                        "            },\n" +
                        "            {\n" +
                        "                \"survival_times\":\"3\",\n" +
                        "                \"split_mode\":\"" + splitMode + "\",\n" +
                        "                \"visitor_best_threshold\":0.83,\n" +
                        "                \"group_name\":\"DEFAULT\",\n" +
                        "                \"threshold\":0.8,\n" +
                        "                \"type\":\"DEFAULT\",\n" +
                        "                \"node_id\":\"" + scope + "\"\n" +
                        "            }\n" +
                        "        ]\n" +
                        "    },\n" +
                        "    \"node_id\":\"3064\",\n" +
                        "    \"service_id\":13\n" +
                        "}";

        genAuth();
        String response = putRequestWithHeader(url, json, header);

        System.out.println(response);
    }

    //    ----------------人物清理------------------------

    long clearLimitTime[] = {
            dateTimeUtil.initLastYear() - 1,
            dateTimeUtil.initLastYear(),
            dateTimeUtil.initLastYear() + 1,
            dateTimeUtil.initLastMonth() - 1,
            dateTimeUtil.initLastMonth(),
            dateTimeUtil.initLastMonth() + 1,
            dateTimeUtil.initLastWeek() - 1,
            dateTimeUtil.initLastWeek(),
            dateTimeUtil.initLastWeek() + 1,
            dateTimeUtil.initDateByDay() - 1,
            dateTimeUtil.initDateByDay(),
            dateTimeUtil.initDateByDay() + 1,
            System.currentTimeMillis(),
    };

//    ---------------------------1、以YEAR为维度清理人物---------------------------------------

    @Test
    public void clearLimitWithYear() throws Exception {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();

        String picUrl = "liao_1";
        long startTime = System.currentTimeMillis() - 100;
        long endTime = System.currentTimeMillis();
        GroupType type = GroupType.DEFAULT;

//        1、修改模式为“年”
        splitModeConfig("YEAR");

//        2、注册
        for (int i = 1; i <= clearLimitTime.length; i++) {
            String userId = strangerPerfix + ciCaseName + "-" + i;
            register(picUrl, userId, startTime, endTime, type, genQuality());

            String response = queryUser(userId);
            String faceId = getFaceId(response, picUrl);
            updateImagetime(userId, faceId, clearLimitTime[i]);

            updateCustomertime(updateCustomerUrl, userId, clearLimitTime[i]);
        }

//        3、清理


//        4、查询
        for (int i = 1; i <= 1; i++) {
            String userId = strangerPerfix + ciCaseName + "-" + i;
            String response = queryUser(userId);
            checkQueryResult(response, false);
        }

        for (int i = 2; i <= 13; i++) {
            String userId = strangerPerfix + ciCaseName + "-" + i;
            String response = queryUser(userId);
            checkQueryResult(response, true);
        }
    }

    //    -----------------------------------2、以MONYH为维度清理人物-------------------------------------------
    @Test
    public void clearLimitWithMon() throws Exception {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();

        String picUrl = "liao_1";
        long startTime = System.currentTimeMillis() - 100;
        long endTime = System.currentTimeMillis();
        GroupType type = GroupType.DEFAULT;

//        1、修改模式为“月”
        splitModeConfig("MONTH");

//        2、注册
        for (int i = 1; i <= clearLimitTime.length; i++) {
            String userId = strangerPerfix + ciCaseName + "-" + i;
            register(picUrl, userId, startTime, endTime, type, genQuality());

            String response = queryUser(userId);
            String faceId = getFaceId(response, picUrl);
            updateImagetime(userId, faceId, clearLimitTime[i]);

            updateCustomertime(updateCustomerUrl, userId, clearLimitTime[i]);
        }

//        3、清理


//        4、查询
        for (int i = 1; i <= 4; i++) {
            String userId = strangerPerfix + ciCaseName + "-" + i;
            String response = queryUser(userId);
            checkQueryResult(response, false);
        }

        for (int i = 5; i <= 13; i++) {
            String userId = strangerPerfix + ciCaseName + "-" + i;
            String response = queryUser(userId);
            checkQueryResult(response, true);
        }
    }

    //    ----------------------------------------3、以WEEK为维度清理人物-------------------------------
    @Test
    public void clearLimitWithWeek() throws Exception {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();

        String picUrl = "liao_1";
        long startTime = System.currentTimeMillis() - 100;
        long endTime = System.currentTimeMillis();
        GroupType type = GroupType.DEFAULT;

//        1、修改模式为“周”
        splitModeConfig("WEEK");

//        2、注册
        for (int i = 1; i <= clearLimitTime.length; i++) {
            String userId = strangerPerfix + ciCaseName + "-" + i;
            register(picUrl, userId, startTime, endTime, type, genQuality());
            String response = queryUser(userId);

            String faceId = getFaceId(response, picUrl);
            updateImagetime(userId, faceId, clearLimitTime[i]);

            updateCustomertime(updateCustomerUrl, userId, clearLimitTime[i]);

        }

//        3、清理


//        4、查询
        for (int i = 1; i <= 7; i++) {
            String userId = strangerPerfix + ciCaseName + "-" + i;
            String response = queryUser(userId);
            checkQueryResult(response, false);
        }

        for (int i = 8; i <= 13; i++) {
            String userId = strangerPerfix + ciCaseName + "-" + i;
            String response = queryUser(userId);
            checkQueryResult(response, true);
        }
    }

    //    ---------------------------------------4、以DAY为维度清理人物------------------------------
    @Test
    public void clearLimitWithDay() throws Exception {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();

        String picUrl = "liao_1";
        long startTime = System.currentTimeMillis() - 100;
        long endTime = System.currentTimeMillis();
        GroupType type = GroupType.DEFAULT;

//        1、修改模式为“天”
        splitModeConfig("DAY");

//        2、注册
        for (int i = 1; i <= clearLimitTime.length; i++) {
            String userId = strangerPerfix + ciCaseName + "-" + i;
            register(picUrl, userId, startTime, endTime, type, genQuality());

            String response = queryUser(userId);
            String faceId = getFaceId(response, picUrl);
            updateImagetime(userId, faceId, clearLimitTime[i]);

            updateCustomertime(updateCustomerUrl, userId, clearLimitTime[i]);
        }

//        3、清理


//        4、查询
        for (int i = 1; i <= 10; i++) {
            String userId = strangerPerfix + ciCaseName + "-" + i;
            String response = queryUser(userId);
            checkQueryResult(response, false);
        }

        for (int i = 11; i <= 13; i++) {
            String userId = strangerPerfix + ciCaseName + "-" + i;
            String response = queryUser(userId);
            checkQueryResult(response, true);
        }
    }

    //    ----------------------5、DAY维度，同一customerId，图片均在时间窗口外（stranger）------------------------------
    @Test(dataProvider = "ALL_TIME_OUT_OF_DATE")
    public void clearAllOutOfDateStranger(long time, String picUrl) throws Exception {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();

        long startTime = System.currentTimeMillis() - 100;
        long endTime = System.currentTimeMillis();
        GroupType type = GroupType.DEFAULT;

//        1、修改模式为“天”
        splitModeConfig("DAY");

//        2、注册
        String userId = strangerPerfix + ciCaseName;

        register(picUrl, userId, startTime, endTime, type, genQuality());

        String response = queryUser(userId);
        String faceId = getFaceId(response, picUrl);
        updateImagetime(userId, faceId, time);

        updateCustomertime(updateCustomerUrl, userId, time);

        if ("yu_10".equals(picUrl)) {
//            3、清理

//            4、查询
            response = queryUser(userId);
            checkQueryResult(response, false);

        }
    }

    //    ---------------------------6、DAY维度，同一customerId，图片均在时间窗口外（special）---------------------
    @Test(dataProvider = "ALL_TIME_OUT_OF_DATE")
    public void clearAllOutOfDateSpecial(long time, String picUrl) throws Exception {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();

        long startTime = System.currentTimeMillis() - 100;
        long endTime = System.currentTimeMillis();
        GroupType type = GroupType.DEFINE;

//        1、修改模式为“天”
        splitModeConfig("DAY");

//        2、注册
        String userId = specialPerfix + ciCaseName;

        register(picUrl, userId, startTime, endTime, type, genQuality());

        String response = queryUser(userId);
        String faceId = getFaceId(response, picUrl);
        updateImagetime(userId, faceId, time);

        updateCustomertime(updateCustomerUrl, userId, time);

        if ("yu_10".equals(picUrl)) {
//            3、清理

//            4、查询
            response = queryUser(userId);
            checkQueryResult(response, false);

        }
    }

    //    -----------------------7、DAY维度，同一customerId，图片有2张在时间窗口外，13张在时间窗口内（stranger）-----------------
    @Test(dataProvider = "2_OUT_OF_DATE_13_IN")
    public void clear2OutOfDate13InStranger(long time, String picUrl) throws Exception {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();

        long startTime = System.currentTimeMillis() - 100;
        long endTime = System.currentTimeMillis();
        GroupType type = GroupType.DEFAULT;

//        1、修改模式为“天”
        splitModeConfig("DAY");

//        2、注册
        String userId = strangerPerfix + ciCaseName;

        register(picUrl, userId, startTime, endTime, type, genQuality());

        String response = queryUser(userId);
        String faceId = getFaceId(response, picUrl);
        updateImagetime(userId, faceId, time);

        updateCustomertime(updateCustomerUrl, userId, time);

        if ("yu_15".equals(picUrl)) {
//            3、清理

//            4、验证，这个怎么验证呢？查询出来，然后检查图片的时间戳和图片的数量吗？
            response = queryUser(userId);

        }
    }

    //    -----------------------8、DAY维度，同一customerId，图片有2张在时间窗口外，13张在时间窗口内-------------------
    @Test(dataProvider = "2_OUT_OF_DATE_13_IN")
    public void clear2OutOfDate13InSpecial(long time, String picUrl) throws Exception {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();

        long startTime = System.currentTimeMillis() - 100;
        long endTime = System.currentTimeMillis();
        GroupType type = GroupType.DEFINE;

//        1、修改模式为“天”
        splitModeConfig("DAY");

//        2、注册
        String userId = specialPerfix + ciCaseName;

        register(picUrl, userId, startTime, endTime, type, genQuality());

        String response = queryUser(userId);
        String faceId = getFaceId(response, picUrl);
        updateImagetime(userId, faceId, time);

        updateCustomertime(updateCustomerUrl, userId, time);

        if ("yu_15".equals(picUrl)) {
//            3、清理

//            4、验证，这个怎么验证呢？查询出来，然后检查图片的时间戳和图片的数量吗？
            response = queryUser(userId);

        }
    }

    //   ------------------------------9、DAY维度，同一customerId，15张图片均在时间窗口内（stranger）----------------------
    @Test(dataProvider = "ALL_IN_DATE")
    public void clearAllInDateStranger(long time, String picUrl) throws Exception {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();

        long startTime = System.currentTimeMillis() - 100;
        long endTime = System.currentTimeMillis();
        GroupType type = GroupType.DEFAULT;

//        1、修改模式为“天”
        splitModeConfig("DAY");

//        2、注册
        String userId = strangerPerfix + ciCaseName;

        register(picUrl, userId, startTime, endTime, type, genQuality());

        String response = queryUser(userId);
        String faceId = getFaceId(response, picUrl);
        updateImagetime(userId, faceId, time);

        updateCustomertime(updateCustomerUrl, userId, time);

        if ("yu_15".equals(picUrl)) {
//            3、清理

//            4、验证，这个怎么验证呢？查询出来，然后检查图片的时间戳和图片的数量吗？
            response = queryUser(userId);

        }
    }

    //    ----------------------------10、DAY维度，同一customerId，15张图片均在时间窗口内（special）---------------------
    @Test(dataProvider = "ALL_IN_DATE")
    public void clearAllInDateSpecial(long time, String picUrl) throws Exception {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();

        long startTime = System.currentTimeMillis() - 100;
        long endTime = System.currentTimeMillis();
        GroupType type = GroupType.DEFINE;

//        1、修改模式为“天”
        splitModeConfig("DAY");

//        2、注册
        String userId = specialPerfix + ciCaseName;

        register(picUrl, userId, startTime, endTime, type, genQuality());

        String response = queryUser(userId);
        String faceId = getFaceId(response, picUrl);
        updateImagetime(userId, faceId, time);

        updateCustomertime(updateCustomerUrl, userId, time);

        if ("yu_15".equals(picUrl)) {
//            3、清理

//            4、验证，这个怎么验证呢？查询出来，然后检查图片的时间戳和图片的数量吗？
            response = queryUser(userId);

        }
    }


    //    ------------------------------------------------人物图片化简----------------------------------------------

    //    1、同一个customerId，用同一个人的图片注册16张图片，用其他人图片注册3张图片，时间都在昨天（stranger）
    @Test(dataProvider = "YU_16_DUAN_3_PIC")
    public void reduceYu16Duan3Stranger(String picUrl) throws Exception {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();

        long startTime = System.currentTimeMillis() - 100;
        long endTime = System.currentTimeMillis();

        String userId = strangerPerfix + ciCaseName;

//        1、注册
        register(picUrl, userId, startTime, endTime, GroupType.DEFAULT, genQuality());

        String response = queryUser(userId);
        String faceId = getFaceId(response, picUrl);

        long updateTimeStamp = System.currentTimeMillis() - 24 * 60 * 60 * 1000;

        updateCustomertime(updateCustomerUrl, userId, updateTimeStamp);
        updateImagetime(userId, faceId, updateTimeStamp);

        if ("duan_3".equals(picUrl)) {
//            2、化简

//            3、验证（怎么验证呢？查询该人的图片是否小于11张？）
        }
    }

    //    2、同一个customerId，用同一个人的图片注册16张图片，用其他人图片注册3张图片，时间都在昨天（special）
    @Test(dataProvider = "YU_16_DUAN_3_PIC")
    public void reduceYu16Duan3Special(String picUrl) throws Exception {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();

        long startTime = System.currentTimeMillis() - 100;
        long endTime = System.currentTimeMillis();

        String userId = specialPerfix + ciCaseName;

//        1、注册
        register(picUrl, userId, startTime, endTime, GroupType.DEFINE, genQuality());

        String response = queryUser(userId);
        String faceId = getFaceId(response, picUrl);

        long updateTimeStamp = System.currentTimeMillis() - 24 * 60 * 60 * 1000;

        updateCustomertime(updateCustomerUrl, userId, updateTimeStamp);
        updateImagetime(userId, faceId, updateTimeStamp);

        if ("duan_3".equals(picUrl)) {
//            2、化简

//            3、验证（怎么验证呢？查询该人的图片是否小于11张？）
        }
    }

    //    3、同一个customerId，用同一个人的图片注册13张图片（时间在昨天），再用该人图片注册3张图片（时间在一周前），stranger
    @Test(dataProvider = "YU_13_IN_3_OUT_PIC")
    public void reduce13In3OutStranger(long picTime, String picUrl) throws Exception {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();

        long startTime = System.currentTimeMillis() - 100;
        long endTime = System.currentTimeMillis();

        String userId = strangerPerfix + ciCaseName;
        register(picUrl, userId, startTime, endTime, GroupType.DEFAULT, genQuality());

        String response = queryUser(userId);
        String faceId = getFaceId(response, picUrl);

        long updateTimeStamp = System.currentTimeMillis() - 24 * 60 * 60 * 1000;

        updateCustomertime(updateCustomerUrl, userId, updateTimeStamp);
        updateImagetime(userId, faceId, picTime);

        if ("yu_16".equals(picUrl)) {
//            2、化简

//            3、验证（怎么验证呢？）


        }
    }

    //    4、同一个customerId，用同一个人的图片注册15张图片（时间在昨天），再用该人图片注册3张图片（时间在一周前），specail
    @Test(dataProvider = "YU_13_IN_3_OUT_PIC")
    public void reduce13In3OutSpecial(long picTime, String picUrl) throws Exception {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();

        long startTime = System.currentTimeMillis() - 100;
        long endTime = System.currentTimeMillis();

        String userId = specialPerfix + ciCaseName;
        register(picUrl, userId, startTime, endTime, GroupType.DEFINE, genQuality());

        String response = queryUser(userId);
        String faceId = getFaceId(response, picUrl);

        long updateTimeStamp = System.currentTimeMillis() - 24 * 60 * 60 * 1000;

        updateCustomertime(updateCustomerUrl, userId, updateTimeStamp);
        updateImagetime(userId, faceId, picTime);

        if ("yu_16".equals(picUrl)) {
//            2、化简

//            3、验证（怎么验证呢？）


        }
    }

    //    5、同一个customerId，用同一个人的图片注册16张图片，时间在一周前，类型为stranger
    @Test(dataProvider = "YU_16_OUT")
    public void reduce16outStranger(long picTime, String picUrl) throws Exception {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();

        long startTime = System.currentTimeMillis() - 100;
        long endTime = System.currentTimeMillis();

        String userId = strangerPerfix + ciCaseName;
        register(picUrl, userId, startTime, endTime, GroupType.DEFAULT, genQuality());

        String response = queryUser(userId);
        String faceId = getFaceId(response, picUrl);

        long updateTimeStamp = System.currentTimeMillis() - 24 * 60 * 60 * 1000;

        updateCustomertime(updateCustomerUrl, userId, updateTimeStamp);
        updateImagetime(userId, faceId, picTime);

        if ("yu_16".equals(picUrl)) {
//            2、化简

//            3、验证(期待将该人物删除)
            response = queryUser(userId);
            checkQueryResult(response, false);
        }
    }

    //    6、同一个customerId，用同一个人的图片注册16张图片，时间在一周前，类型为special
    @Test(dataProvider = "YU_16_OUT")
    public void reduce16outSpecial(long picTime, String picUrl) throws Exception {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();

        long startTime = System.currentTimeMillis() - 100;
        long endTime = System.currentTimeMillis();

        String userId = specialPerfix + ciCaseName;
        register(picUrl, userId, startTime, endTime, GroupType.DEFINE, genQuality());

        String response = queryUser(userId);
        String faceId = getFaceId(response, picUrl);

        long updateTimeStamp = System.currentTimeMillis() - 24 * 60 * 60 * 1000;

        updateCustomertime(updateCustomerUrl, userId, updateTimeStamp);
        updateImagetime(userId, faceId, picTime);

        if ("yu_16".equals(picUrl)) {
//            2、化简

//            3、验证(期待将该人物删除)
            response = queryUser(userId);
            checkQueryResult(response, false);
        }
    }

    @Test(dataProvider = "YU_ORIGIN")
    public void reduceChangeOriginStranger(long picTime, String picUrl) throws Exception {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();

        long startTime = System.currentTimeMillis() - 100;
        long endTime = System.currentTimeMillis();

        String userId = strangerPerfix + ciCaseName;
        register(picUrl, userId, startTime, endTime, GroupType.DEFAULT, genQuality());

        String response = queryUser(userId);
        String faceId = getFaceId(response, picUrl);

        long updateTimeStamp = System.currentTimeMillis() - 24 * 60 * 60 * 1000;

        updateCustomertime(updateCustomerUrl, userId, updateTimeStamp);
        updateImagetime(userId, faceId, picTime);

        if ("yu_16".equals(picUrl)) {
//          2、更新ORINGIN图片时间在一周前

            response = queryUser(userId);

            faceId = getFaceIdOfOrigin(response);

            long imageTimeStamp = System.currentTimeMillis() - 8 * 24 * 60 * 60 * 1000;
            updateImagetime(userId, faceId, imageTimeStamp);

//            3、化简

//            4、验证
            response = queryUser(userId);
            checkChangeOriginResult(response, faceId);
        }
    }

    @Test(dataProvider = "YU_ORIGIN")
    public void reduceChangeOriginSpecial(long picTime, String picUrl) throws Exception {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();

        long startTime = System.currentTimeMillis() - 100;
        long endTime = System.currentTimeMillis();

        String userId = specialPerfix + ciCaseName;
        register(picUrl, userId, startTime, endTime, GroupType.DEFINE, genQuality());

        String response = queryUser(userId);
        String faceId = getFaceId(response, picUrl);

        long updateTimeStamp = System.currentTimeMillis() - 24 * 60 * 60 * 1000;

        updateCustomertime(updateCustomerUrl, userId, updateTimeStamp);
        updateImagetime(userId, faceId, picTime);

        if ("yu_16".equals(picUrl)) {
//          2、更新ORINGIN图片时间在一周前

            response = queryUser(userId);

            faceId = getFaceIdOfOrigin(response);

            long imageTimeStamp = System.currentTimeMillis() - 8 * 24 * 60 * 60 * 1000;
            updateImagetime(userId, faceId, imageTimeStamp);

//            3、化简

//            4、验证
            response = queryUser(userId);
            checkChangeOriginResult(response, faceId);
        }
    }


    //    ------------------------------------------------全量聚类------------------------------
    @Test(dataProvider = "QLJL_3_STRANGER_6_PIC")
    public void qljl3Stranger6Pic(String typeStr, String id, String picUrl) throws Exception {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();

        long startTime = System.currentTimeMillis() - 100;
        long endTime = System.currentTimeMillis();

        String userId;

        if ("stranger".equals(typeStr)) {
            userId = strangerPerfix + ciCaseName + "-" + id;
            register(picUrl, id, startTime, endTime, GroupType.DEFAULT, genQuality());
        } else if ("special".equals(typeStr)) {
            userId = specialPerfix + ciCaseName + "-" + id;
            register(picUrl, id, startTime, endTime, GroupType.DEFINE, genQuality());
        } else {
            throw new Exception("typeStr is wrong");
        }

        String response = queryUser(userId);
        String faceId = getFaceId(response, picUrl);

        long updateTimeStamp = System.currentTimeMillis() - 24 * 60 * 60 * 1000;

        updateCustomertime(updateCustomerUrl, userId, updateTimeStamp);
        updateImagetime(id, faceId, updateTimeStamp);

        if ("3".equals(id) && "yu_6".equals(picUrl)){
//           2、化简

//            3、验证
            response = queryUser(userId);
//            checkIsMerge();
        }
    }

    @Test(dataProvider = "QLJL_2_STRANGER_1_SPECIAL_6_PIC")
    public void qljl2Stranger1special6Pic(String typeStr, String id, String picUrl) throws Exception {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();

        long startTime = System.currentTimeMillis() - 100;
        long endTime = System.currentTimeMillis();

        String userId;

        if ("stranger".equals(typeStr)) {
            userId = strangerPerfix + ciCaseName + "-" + id;
            register(picUrl, id, startTime, endTime, GroupType.DEFAULT, genQuality());
        } else if ("special".equals(typeStr)) {
            userId = specialPerfix + ciCaseName + "-" + id;
            register(picUrl, id, startTime, endTime, GroupType.DEFINE, genQuality());
        } else {
            throw new Exception("typeStr is wrong");
        }

        String response = queryUser(userId);
        String faceId = getFaceId(response, picUrl);

        long updateTimeStamp = System.currentTimeMillis() - 24 * 60 * 60 * 1000;

        updateCustomertime(updateCustomerUrl, userId, updateTimeStamp);
        updateImagetime(id, faceId, updateTimeStamp);

        if ("3".equals(id) && "yu_6".equals(picUrl)){
//           2、化简

//            3、验证
            for (int i = 1;i<=3; i++){
                userId = specialPerfix + ciCaseName + "-" + i;

                if (i==1 || i==2){
                    response = queryUser(userId);
                    boolean isExistRes = checkIsMerge(response);
                    Assert.assertEquals(isExistRes,false, "合并后stranger仍能查出");
                }

                if (i==3){
                    response = queryUser(userId);
                    checkPicCount(response);
                }
            }

        }

    }

    @Test(dataProvider = "BOTH_OUT_OF_TIME_STRANGER")
    public void testBothOutOfTimeStranger(String updateTimeStr, String picUrl) throws Exception {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();

        String userId = strangerPerfix + ciCaseName;
        long startTime = System.currentTimeMillis() - 100;
        long endTime = System.currentTimeMillis();

        register(picUrl, userId, startTime, endTime, GroupType.DEFAULT, genQuality());
        String response = queryUser(userId);

        String faceId = getFaceId(response, picUrl);

        System.out.println(updateTimeStr);
        long updateTime = Long.valueOf(updateTimeStr);
        updateImagetime(userId, faceId, updateTime);
    }

    @Test(dataProvider = "BOTH_OUT_OF_TIME_SPECIAL")
    public void testBothOutOfTimeSpecial(String updateTimeStr, String picUrl) throws Exception {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();

        String userId = specialPerfix + ciCaseName;
        long startTime = System.currentTimeMillis() - 100;
        long endTime = System.currentTimeMillis();

        register(picUrl, userId, startTime, endTime, GroupType.DEFINE, genQuality());
        String response = queryUser(userId);

        String faceId = getFaceId(response, picUrl);

        System.out.println(updateTimeStr);

        long updateTime = Long.valueOf(updateTimeStr);
        updateImagetime(userId, faceId, updateTime);
    }

    //    天维度
//3、同一customerId，图片有一张在时间窗口内，其他在时间窗口外（stranger）
    @Test(dataProvider = "ONE_IN_TIME")
    public void testOneInTimeStranger(long updateTimeStr, String picUrl) throws Exception {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();

        String userId = strangerPerfix + ciCaseName;
        long startTime = System.currentTimeMillis() - 100;
        long endTime = System.currentTimeMillis();

        register(picUrl, userId, startTime, endTime, GroupType.DEFAULT, genQuality());
        String response = queryUser(userId);

        String faceId = getFaceId(response, picUrl);
        System.out.println(updateTimeStr);
        long timestamp = Long.valueOf(updateTimeStr);
        updateImagetime(userId, faceId, timestamp);
    }

    //    天维度
//4、同一customerId，图片有一张在时间窗口内，其他在时间窗口外（special）
    @Test(dataProvider = "ONE_IN_TIME")
    public void testOneInTimeSpecial(long updateTimeStr, String picUrl) throws Exception {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();

        String userId = specialPerfix + ciCaseName;
        long startTime = System.currentTimeMillis() - 100;
        long endTime = System.currentTimeMillis();

        register(picUrl, userId, startTime, endTime, GroupType.DEFINE, genQuality());
        String response = queryUser(userId);

        String faceId = getFaceId(response, picUrl);

//        updateCustomertime(updateCustomerUrl, userId, Long.valueOf(dateTimeUtil.dateToTimestamp(date)));
        long timestamp = Long.valueOf(updateTimeStr);
        System.out.println(timestamp);
        updateImagetime(userId, faceId, timestamp);
    }

    //    天维度
//5、同一customerId，图片均在时间窗口内（stranger）
    @Test(dataProvider = "ALL_IN_TIME")
    public void testAllInTimeStranger(long timastamp, String picUrl) throws Exception {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();

        String userId = strangerPerfix + ciCaseName;
        long startTime = System.currentTimeMillis() - 100;
        long endTime = System.currentTimeMillis();

        register(picUrl, userId, startTime, endTime, GroupType.DEFAULT, genQuality());

        String response = queryUser(userId);

        String faceId = getFaceId(response, picUrl);

        updateCustomertime(updateCustomerUrl, userId, timastamp);
        updateImagetime(userId, faceId, timastamp);
    }

    //    天维度
//6、同一customerId，图片均在时间窗口内（special）
    @Test(dataProvider = "ALL_IN_TIME")
    public void testAllInTimeSpecial(long timestamp, String picUrl) throws Exception {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();

        String userId = specialPerfix + ciCaseName;
        long startTime = System.currentTimeMillis() - 100;
        long endTime = System.currentTimeMillis();

        register(picUrl, userId, startTime, endTime, GroupType.DEFINE, genQuality());
        String response = queryUser(userId);

        String faceId = getFaceId(response, picUrl);

        updateCustomertime(updateCustomerUrl, userId, timestamp);
        updateImagetime(userId, faceId, timestamp);
    }

    // 人物图片化简-------1、同一个customerId，用同一个人（original中绝对存在的）的图片注册15张图片，
// 用其他人图片注册3张图片，时间都在昨天(stranger)
    @Test(dataProvider = "YU_16_DUAN_3_PIC")
    public void testReduceImage1(String picUrl) throws Exception {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();

        long startTime = System.currentTimeMillis() - 100;
        long endTime = System.currentTimeMillis();

        String userId = strangerPerfix + ciCaseName;
        register(picUrl, userId, startTime, endTime, GroupType.DEFAULT, genQuality());

        String response = queryUser(userId);
        String faceId = getFaceId(response, picUrl);

        long updateTimeStamp = System.currentTimeMillis() - 24 * 60 * 60 * 1000;

        updateCustomertime(updateCustomerUrl, userId, updateTimeStamp);
        updateImagetime(userId, faceId, updateTimeStamp);
    }

    //2、同一个customerId，用同一个人（original中绝对存在的）的图片注册15张图片，用其他人图片注册3张图片，时间都在昨天（special）
    @Test(dataProvider = "YU_16_DUAN_3_PIC")
    public void testReduceImage2(String picUrl) throws Exception {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();

        long startTime = System.currentTimeMillis() - 100;
        long endTime = System.currentTimeMillis();

        String userId = specialPerfix + ciCaseName;
        register(picUrl, userId, startTime, endTime, GroupType.DEFINE, genQuality());

        String response = queryUser(userId);
        String faceId = getFaceId(response, picUrl);

        long updateTimeStamp = System.currentTimeMillis() - 24 * 60 * 60 * 1000;

        updateCustomertime(updateCustomerUrl, userId, updateTimeStamp);
        updateImagetime(userId, faceId, updateTimeStamp);
    }

    //    3、同一个customerId，用同一个人（original中绝对存在的）的图片注册15张图片（时间在1昨天），
//    再用该人图片注册3张图片（时间在一周前），类型为stranger
    @Test(dataProvider = "YU_13_IN_3_OUT_PIC")
    public void testReduceImage3(long picTime, String picUrl) throws Exception {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();

        long startTime = System.currentTimeMillis() - 100;
        long endTime = System.currentTimeMillis();

        String userId = strangerPerfix + ciCaseName;
        register(picUrl, userId, startTime, endTime, GroupType.DEFAULT, genQuality());

        String response = queryUser(userId);
        String faceId = getFaceId(response, picUrl);

        long updateTimeStamp = System.currentTimeMillis() - 24 * 60 * 60 * 1000;

        updateCustomertime(updateCustomerUrl, userId, updateTimeStamp);
        updateImagetime(userId, faceId, picTime);
    }

    //    4、同一个customerId，用同一个人（original中绝对存在的）的图片注册15张图片（时间在1昨天），再用该人图片注册3张图片（时间在一周前），类型为specail
    @Test(dataProvider = "YU_13_IN_3_OUT_PIC")
    public void testReduceImage4(long picTime, String picUrl) throws Exception {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();

        long startTime = System.currentTimeMillis() - 100;
        long endTime = System.currentTimeMillis();

        String userId = specialPerfix + ciCaseName;
        register(picUrl, userId, startTime, endTime, GroupType.DEFINE, genQuality());

        String response = queryUser(userId);
        String faceId = getFaceId(response, picUrl);

        long updateTimeStamp = System.currentTimeMillis() - 24 * 60 * 60 * 1000;

        updateCustomertime(updateCustomerUrl, userId, updateTimeStamp);
        updateImagetime(userId, faceId, picTime);
    }

    //    5、同一个customerId，用同一个人的图片注册15张图片，时间在一周前，类型为stranger
    @Test(dataProvider = "YU_ORIGIN")
    public void testReduceImage5(long picTime, String picUrl) throws Exception {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();

        long startTime = System.currentTimeMillis() - 100;
        long endTime = System.currentTimeMillis();

        String userId = strangerPerfix + ciCaseName;
        register(picUrl, userId, startTime, endTime, GroupType.DEFAULT, genQuality());

        String response = queryUser(userId);
        String faceId = getFaceId(response, picUrl);

        long updateTimeStamp = System.currentTimeMillis() - 24 * 60 * 60 * 1000;

        updateCustomertime(updateCustomerUrl, userId, updateTimeStamp);
        updateImagetime(userId, faceId, picTime);
    }

    //6、同一个customerId，用同一个人的图片注册15张图片，时间在一周前，类型为special
    @Test(dataProvider = "YU_ORIGIN")
    public void testReduceImage6(long picTime, String picUrl) throws Exception {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();

        long startTime = System.currentTimeMillis() - 100;
        long endTime = System.currentTimeMillis();

        String userId = specialPerfix + ciCaseName;

        register(picUrl, userId, startTime, endTime, GroupType.DEFINE, genQuality());

        String response = queryUser(userId);
        String faceId = getFaceId(response, picUrl);

        long updateTimeStamp = System.currentTimeMillis() - 24 * 60 * 60 * 1000;

        updateCustomertime(updateCustomerUrl, userId, updateTimeStamp);
        updateImagetime(userId, faceId, picTime);
    }

    //    7、
    @Test(dataProvider = "YU_16_PIC")
    public void testReduceImage7(String picUrl) throws Exception {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();

        long startTime = System.currentTimeMillis() - 100;
        long endTime = System.currentTimeMillis();

        String userId = specialPerfix + "testReduceImage6";
//        String userId = specialPerfix + ciCaseName;

        register(picUrl, userId, startTime, endTime, GroupType.DEFINE, genQuality());

        String response = queryUser(userId);
        String faceId = getFaceId(response, picUrl);

        long updateTimeStamp = System.currentTimeMillis() - 24 * 60 * 60 * 1000;

        updateCustomertime(updateCustomerUrl, userId, updateTimeStamp);
        updateImagetime(userId, faceId, updateTimeStamp);
    }

    @Test(dataProvider = "YU_6_PIC")
    public void testzljl1(String picUrl) throws Exception {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();

        long startTime = System.currentTimeMillis() - 100;
        long endTime = System.currentTimeMillis();

        String userId1 = strangerPerfix + ciCaseName + "-1";
        register(picUrl, userId1, startTime, endTime, GroupType.DEFAULT, genQuality());

        String userId2 = strangerPerfix + ciCaseName + "-2";
        register(picUrl, userId2, startTime, endTime, GroupType.DEFAULT, genQuality());
    }

    @Test(dataProvider = "YU_6_PIC")
    public void testzljl2(String picUrl) throws Exception {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();

        long startTime = System.currentTimeMillis() - 100;
        long endTime = System.currentTimeMillis();

//        String userId1 = strangerPerfix + ciCaseName + "-1";
//        register(picUrl, userId1, startTime, endTime, GroupType.DEFAULT,genQuality());

        String userId2 = specialPerfix + ciCaseName + "-2";
        register(picUrl, userId2, startTime, endTime, GroupType.DEFINE, genQuality());
    }

    //    增量聚类-----------------1、customerId：3个，图片：6张；
//    将这6张图片注册给这3个人，都是stranger，每个人都有这6张图片；
//    图片和人物的时间都在昨天
    @Test(dataProvider = "ZLJL_3_STRANGER_6_PIC")
    public void testqljl1(String typeStr, String userId, String picUrl) throws Exception {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();

        long startTime = System.currentTimeMillis() - 100;
        long endTime = System.currentTimeMillis();

        if ("stranger".equals(typeStr)) {
            userId = strangerPerfix + userId;
            register(picUrl, userId, startTime, endTime, GroupType.DEFAULT, genQuality());
        } else if ("special".equals(typeStr)) {
            userId = specialPerfix + userId;
            register(picUrl, userId, startTime, endTime, GroupType.DEFINE, genQuality());
        } else {
            throw new Exception("typestr is wrong");
        }

        String response = queryUser(userId);
        String faceId = getFaceId(response, picUrl);

        long updateTimeStamp = System.currentTimeMillis() - 24 * 60 * 60 * 1000;

        updateCustomertime(updateCustomerUrl, userId, updateTimeStamp);
        updateImagetime(userId, faceId, updateTimeStamp);
    }

    //    增量聚类-----------------2、customerId：3个，图片：6张；
//    将这6张图片注册给这3个人，两人是stranger，一人是special，每个人都有这6张图片；
//    图片和人物的时间都在昨天
    @Test(dataProvider = "ZLJL_2_STRANGER_1_SPECIAL_6_PIC")
    public void testqljl2(String typeStr, String userId, String picUrl) throws Exception {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();

        long startTime = System.currentTimeMillis() - 100;
        long endTime = System.currentTimeMillis();

        if ("stranger".equals(typeStr)) {
            userId = strangerPerfix + userId;
            register(picUrl, userId, startTime, endTime, GroupType.DEFAULT, genQuality());
        } else if ("special".equals(typeStr)) {
            userId = specialPerfix + userId;
            register(picUrl, userId, startTime, endTime, GroupType.DEFINE, genQuality());
        } else {
            throw new Exception("typestr is wrong");
        }

        String response = queryUser(userId);
        String faceId = getFaceId(response, picUrl);

        long updateTimeStamp = System.currentTimeMillis() - 24 * 60 * 60 * 1000;

        updateCustomertime(updateCustomerUrl, userId, updateTimeStamp);
        updateImagetime(userId, faceId, updateTimeStamp);
    }

//    3、customerId：2个，图片：5张；
//    将这5张图片注册给这2个人，都是stranger，每个人都有这5张图片；
//    一人的图片和人物的时间都在昨天，另一人的图片和人物的时间都在一周外

    @Test(dataProvider = "ZLJL_2_STRANGER_1_IN_1_OUT")
    public void testqljl3(String typeStr, String userId, String picUrl, String timeRange) throws Exception {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();

        long startTime = System.currentTimeMillis() - 100;
        long endTime = System.currentTimeMillis();

        if ("stranger".equals(typeStr)) {
            userId = strangerPerfix + userId;
            register(picUrl, userId, startTime, endTime, GroupType.DEFAULT, genQuality());
        } else if ("special".equals(typeStr)) {
            userId = specialPerfix + userId;
            register(picUrl, userId, startTime, endTime, GroupType.DEFINE, genQuality());
        } else {
            throw new Exception("typestr is wrong");
        }

        long imageTime;

        if ("in".equals(timeRange)) {
            imageTime = System.currentTimeMillis() - 24 * 60 * 60 * 1000;
        } else if ("out".equals(timeRange)) {
            imageTime = System.currentTimeMillis() - 8 * 24 * 60 * 60 * 1000;
        } else {
            throw new Exception("timeRange is wrong!");
        }

        String response = queryUser(userId);
        String faceId = getFaceId(response, picUrl);

        long updateTimeStamp = System.currentTimeMillis() - 24 * 60 * 60 * 1000;

        updateCustomertime(updateCustomerUrl, userId, updateTimeStamp);
        updateImagetime(userId, faceId, imageTime);
    }

    //    4、customerId：2个，图片：6张；
//    将这6张图片注册给这2个人，一个stranger，一个special，每个人都有这6张图片；
//    stranger的图片和人物的时间都在昨天，special的图片和人物的时间都在一周外
    @Test(dataProvider = "ZLJL_1_STRANGER_IN_1_SPECIAL_OUT")
    public void testqljl4(String typeStr, String userId, String picUrl, String timeRange) throws Exception {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();

        long startTime = System.currentTimeMillis() - 100;
        long endTime = System.currentTimeMillis();

        if ("stranger".equals(typeStr)) {
            userId = strangerPerfix + userId;
            register(picUrl, userId, startTime, endTime, GroupType.DEFAULT, genQuality());
        } else if ("special".equals(typeStr)) {
            userId = specialPerfix + userId;
            register(picUrl, userId, startTime, endTime, GroupType.DEFINE, genQuality());
        } else {
            throw new Exception("typestr is wrong");
        }

        long imageTime;

        if ("in".equals(timeRange)) {
            imageTime = System.currentTimeMillis() - 24 * 60 * 60 * 1000;
        } else if ("out".equals(timeRange)) {
            imageTime = System.currentTimeMillis() - 8 * 24 * 60 * 60 * 1000;
        } else {
            throw new Exception("timeRange is wrong!");
        }

        String response = queryUser(userId);
        String faceId = getFaceId(response, picUrl);

        long updateTimeStamp = System.currentTimeMillis() - 24 * 60 * 60 * 1000;

        updateCustomertime(updateCustomerUrl, userId, updateTimeStamp);
        updateImagetime(userId, faceId, imageTime);
    }

    @Test(dataProvider = "ZLJL_1_STRANGER_OUT_1_SPECIAL_IN")
    public void testqljl5(String typeStr, String userId, String picUrl, String timeRange) throws Exception {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();

        long startTime = System.currentTimeMillis() - 100;
        long endTime = System.currentTimeMillis();

        if ("stranger".equals(typeStr)) {
            userId = strangerPerfix + userId;
            register(picUrl, userId, startTime, endTime, GroupType.DEFAULT, genQuality());
        } else if ("special".equals(typeStr)) {
            userId = specialPerfix + userId;
            register(picUrl, userId, startTime, endTime, GroupType.DEFINE, genQuality());
        } else {
            throw new Exception("typestr is wrong");
        }

        long imageTime;

        if ("in".equals(timeRange)) {
            imageTime = System.currentTimeMillis() - 24 * 60 * 60 * 1000;
        } else if ("out".equals(timeRange)) {
            imageTime = System.currentTimeMillis() - 8 * 24 * 60 * 60 * 1000;
        } else {
            throw new Exception("timeRange is wrong!");
        }

        String response = queryUser(userId);
        String faceId = getFaceId(response, picUrl);

        long updateTimeStamp = System.currentTimeMillis() - 24 * 60 * 60 * 1000;

        updateCustomertime(updateCustomerUrl, userId, updateTimeStamp);
        updateImagetime(userId, faceId, imageTime);
    }

    @Test(dataProvider = "ZLJL_2_STRANGER_OUT")
    public void testqljl6(String typeStr, String userId, String picUrl, String timeRange) throws Exception {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();

        long startTime = System.currentTimeMillis() - 100;
        long endTime = System.currentTimeMillis();

        if ("stranger".equals(typeStr)) {
            userId = strangerPerfix + userId;
            register(picUrl, userId, startTime, endTime, GroupType.DEFAULT, genQuality());
        } else if ("special".equals(typeStr)) {
            userId = specialPerfix + userId;
            register(picUrl, userId, startTime, endTime, GroupType.DEFINE, genQuality());
        } else {
            throw new Exception("typestr is wrong");
        }

        long imageTime;

        if ("in".equals(timeRange)) {
            imageTime = System.currentTimeMillis() - 24 * 60 * 60 * 1000;
        } else if ("out".equals(timeRange)) {
            imageTime = System.currentTimeMillis() - 8 * 24 * 60 * 60 * 1000;
        } else {
            throw new Exception("timeRange is wrong!");
        }

        String response = queryUser(userId);
        String faceId = getFaceId(response, picUrl);

        long updateTimeStamp = System.currentTimeMillis() - 24 * 60 * 60 * 1000;

        updateCustomertime(updateCustomerUrl, userId, updateTimeStamp);
        updateImagetime(userId, faceId, imageTime);
    }

    @Test(dataProvider = "ZLJL_1_STRANGER_OUT_1_SPECIAL_OUT")
    public void testqljl7(String typeStr, String userId, String picUrl, String timeRange) throws Exception {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();

        long startTime = System.currentTimeMillis() - 100;
        long endTime = System.currentTimeMillis();

        if ("stranger".equals(typeStr)) {
            userId = strangerPerfix + userId;
            register(picUrl, userId, startTime, endTime, GroupType.DEFAULT, genQuality());
        } else if ("special".equals(typeStr)) {
            userId = specialPerfix + userId;
            register(picUrl, userId, startTime, endTime, GroupType.DEFINE, genQuality());
        } else {
            throw new Exception("typestr is wrong");
        }

        long imageTime;

        if ("in".equals(timeRange)) {
            imageTime = System.currentTimeMillis() - 24 * 60 * 60 * 1000;
        } else if ("out".equals(timeRange)) {
            imageTime = System.currentTimeMillis() - 8 * 24 * 60 * 60 * 1000;
        } else {
            throw new Exception("timeRange is wrong!");
        }

        String response = queryUser(userId);
        String faceId = getFaceId(response, picUrl);

        long updateTimeStamp = System.currentTimeMillis() - 24 * 60 * 60 * 1000;

        updateCustomertime(updateCustomerUrl, userId, updateTimeStamp);
        updateImagetime(userId, faceId, imageTime);
    }


    public void register(String picUrl, String userId, long startTime, long endTime, GroupType type, float quality) throws Exception {

        DetectImage image = new DetectImage();
        int[] axis = {1494, 395, 1628, 570};
        image.setAxis(axis);
        image.setBlur(0f);
        image.setFaceUrl(ossClientUtil.genUrl("QA_TEST/" + picUrl + ".jpg").trim());
        System.out.println(ossClientUtil.genUrl("QA_TEST/" + picUrl + ".jpg").trim());
        image.setIllumination(0f);
        image.setMask(0f);
        image.setPitch(-17.179115f);
        image.setQuality(quality);
        image.setRoll(14.50518f);
        image.setSunglasses(0f);
        image.setYaw(20.501074f);

        Visitor visitor = new Visitor();
        visitor.setFaceUrl("http://retail-huabei2.oss-cn-beijing-internal.aliyuncs.com/who_DAILY/1397%25%252019-09-05/c5a7b0b1-e8b3-4c60-a2af-d759606c/c5a7b0b1-e8b3-4c60-a2af-d759606c%25%25face.jpg?Expires=3038896801&OSSAccessKeyId=LTAIlYpjA39n18Yr&Signature=JOzEzJmoVgPM%2FjPvZdlSWFP%2BcVI%3D");
        visitor.setUserId(userId);
        visitor.setAge(20);
        visitor.setGender(0.9f);
        visitor.setStartTime(startTime);
        visitor.setEndTime(endTime);
        visitor.setQuality(quality);
        String requestId = UUID.randomUUID().toString();
        System.out.println("requestId:" + requestId);
        visitor.setRequestId(requestId);
        visitor.setScope(scope);
        visitor.setDeviceId(deviceId);

        if (type.equals(GroupType.DEFINE)) {
            visitor.setGroupName(grpName);
            visitor.setType(type);
        } else if (type.equals(GroupType.DEFAULT)) {
            visitor.setType(type);
        } else {
            throw new Exception("type is wrong!");
        }

        String[] scopes = new String[]{scope};
        try {

            GateVisitorRegisterResponse registerResponse = ScenarioGateClient
                    .getVisitorRegisterClient("47.95.69.163")
                    .setRequestId(UUID.randomUUID().toString())
                    .setVisitor(visitor)
                    .setImage(image)
                    .setScopes(scopes)
                    .setAppId(appId)
                    .setRegisterTemp(false)
                    .execute();

            System.out.println("registerResponse:" + JSON.toJSONString(registerResponse));
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }
    }

    float genQuality() {

        float quality = 0f;
        while (quality < 0.6) {
            quality = new Random().nextFloat();
        }

        return quality;
    }

    String getFaceId(String response, String faceUrl) throws Exception {
        boolean isExist = false;
        String faceId = "";
        JSONObject responseJo = JSON.parseObject(response);
        JSONArray images = responseJo.getJSONArray("images");
        for (int i = 0; i < images.size(); i++) {
            JSONObject singleFace = images.getJSONObject(i);
            String faceUrlRes = singleFace.getString("faceUrl");
            if (faceUrlRes.contains(faceUrl)) {
                isExist = true;
                faceId = singleFace.getString("faceId");
            }
        }

        if (!isExist) {
            throw new Exception("faceUrl不存在！");
        }

        return faceId;
    }


    String queryUser(String customerId) {

        String response = "";

        String json =
                "{\n" +
                        "    \"customer_id\":\"" + customerId + "\",\n" +
                        "    \"scope\":\"2466\"\n" +
                        "}";
        try {
            response = sendRequestWithUrl(queryCustomerUrl, json);

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }

        return response;
    }

    void updateCustomertime(String url, String customerId, long updateGmt) {

        String json =
                "{\n" +
                        "    \"customer_id\":\"" + customerId + "\",\n" +
                        "    \"scope\":\"" + scope + "\",\n" +
                        "    \"update_gmt\":" + updateGmt + "" +
                        "}";
        try {
            sendRequestWithUrl(updateCustomerUrl, json);

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }
    }

    void updateImagetime(String customerId, String faceId, long createGmt) {

        String json =
                "{\n" +
                        "    \"customer_id\":\"" + customerId + "\",\n" +
                        "    \"face_id\":\"" + faceId + "\",\n" +
                        "    \"scope\":\"" + scope + "\",\n" +
                        "    \"create_gmt\":" + createGmt + "" +

                        "}";
        try {
            sendRequestWithUrl(updateImageUrl, json);

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }
    }

    private void checkQueryResult(String response, boolean isExist) {
        JSONObject resJo = JSON.parseObject(response);
        boolean isExistRes = resJo.containsKey("customer_info");
        Assert.assertEquals(isExistRes, isExist, "expect find : " + isExist + ", actual: " + isExistRes);
    }

    private String getFaceIdOfOrigin(String response) throws Exception {
        String faceId = "";
        boolean isOriginExist = false;
        JSONObject resJo = JSON.parseObject(response);
        boolean isExistRes = resJo.containsKey("customer_info");
        if (!isExistRes) {
            throw new Exception("化简完以后，该人物不存在！");
        }

        JSONObject customerInfo = resJo.getJSONObject("customer_info");
        if (!customerInfo.containsKey("images")) {
            throw new Exception("查询结果中不包含images字段");
        }
        JSONArray images = customerInfo.getJSONArray("images");

        for (int i = 0; i < images.size(); i++) {

            JSONObject singleImage = images.getJSONObject(i);
            String imageType = singleImage.getString("image_type");
            if ("ORIGIN".equals(imageType)) {
                isOriginExist = true;
                faceId = singleImage.getString("face_id");
            }
        }

        if (!isOriginExist) {
            throw new Exception("ORINGIN 图片不存在！");
        }

        return faceId;

    }

    private void checkChangeOriginResult(String response, String faceId) throws Exception {
        JSONObject resJo = JSON.parseObject(response);
        boolean isExistRes = resJo.containsKey("customer_info");

        if (!isExistRes) {
            throw new Exception("化简完以后，该人物不存在！");
        }

        JSONObject customerInfo = resJo.getJSONObject("customer_info");
        if (!customerInfo.containsKey("images")) {
            throw new Exception("查询结果中不包含images字段");
        }
        JSONArray images = customerInfo.getJSONArray("images");

        for (int i = 0; i < images.size(); i++) {

            JSONObject singleImage = images.getJSONObject(i);
            String imageType = singleImage.getString("image_type");
            if ("ORIGIN".equals(imageType)) {
                String faceIdNewOrigin = singleImage.getString("face_id");
                Assert.assertNotEquals(faceIdNewOrigin, faceId, "未变更ORINGIN图片");
            }
        }
    }

    private boolean checkIsMerge(String response) {
        JSONObject resJo = JSON.parseObject(response);
        boolean isExistRes = resJo.containsKey("customer_info");

        return isExistRes;
    }

    private int checkPicCount(String response) throws Exception {
        JSONObject resJo = JSON.parseObject(response);
        boolean isExistRes = resJo.containsKey("customer_info");

        if (!isExistRes) {
            throw new Exception("化简完以后，该人物不存在！");
        }

        JSONObject customerInfo = resJo.getJSONObject("customer_info");
        if (!customerInfo.containsKey("images")) {
            throw new Exception("查询结果中不包含images字段");
        }

        JSONArray images = customerInfo.getJSONArray("images");
        int size = images.size();

        return size;
    }




    private String sendRequestWithUrl(String url, String json) throws Exception {
        HttpExecutorUtil executor = new HttpExecutorUtil();
        executor.doPostJson(url, json);
        return executor.getResponse();
    }

    private String putRequestWithHeader(String url, String json, HashMap header) throws Exception {
        HttpExecutorUtil executor = new HttpExecutorUtil();
        executor.doPutJsonWithHeaders(url, json, header);
        return executor.getResponse();
    }

    private String sendRequestWithHeader(String url, String json, HashMap header) throws Exception {
        HttpExecutorUtil executor = new HttpExecutorUtil();
        executor.doPostJsonWithHeaders(url, json, header);
        return executor.getResponse();
    }


    void genAuth() {

        String json =
                "{\n" +
                        "  \"email\": \"liaoxiangru@winsense.ai\"," +
                        "  \"password\": \"e586aee0d9d9fdb16b9982adb74aeb60\"" +
                        "}";
        try {
            String response = sendRequestWithHeader(genAuthURL, json, header);
            logger.info("\n");
            JSONObject data = JSON.parseObject(response).getJSONObject("data");
            authorization = data.getString("token");

            header.put("Authorization", authorization);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    @DataProvider(name = "ALL_TIME_OUT_OF_DATE")
//    private static Object[] allMode() throws Exception {
//
//        return new Object[]{
//                dateTimeUtil.initLastYear() - 1,
//                dateTimeUtil.initLastYear(),
//                dateTimeUtil.initLastYear() + 1,
//
//                dateTimeUtil.initLastMonth() - 1,
//                dateTimeUtil.initLastMonth(),
//                dateTimeUtil.initLastMonth() + 1,
//
//                dateTimeUtil.initLastWeek() - 1,
//                dateTimeUtil.initLastWeek(),
//                dateTimeUtil.initLastWeek() + 1,
//
//                dateTimeUtil.initDateByDay() - 1
//        };
//    }

    @DataProvider(name = "ALL_TIME_OUT_OF_DATE")
    private static Object[][] allTimeOutOfTime() throws Exception {

        return new Object[][]{
                new Object[]{dateTimeUtil.initLastYear() - 1, "yu_1"},
                new Object[]{dateTimeUtil.initLastYear(), "yu_2"},
                new Object[]{dateTimeUtil.initLastYear() + 1, "yu_3"},

                new Object[]{dateTimeUtil.initLastMonth() - 1, "yu_4"},
                new Object[]{dateTimeUtil.initLastMonth(), "yu_5"},
                new Object[]{dateTimeUtil.initLastMonth() + 1, "yu_6"},

                new Object[]{dateTimeUtil.initLastWeek() - 1, "yu_7"},
                new Object[]{dateTimeUtil.initLastWeek(), "yu_8"},
                new Object[]{dateTimeUtil.initLastWeek() + 1, "yu_9"},

                new Object[]{dateTimeUtil.initDateByDay() - 1, "yu_10"}
        };
    }

    @DataProvider(name = "2_OUT_OF_DATE_13_IN")
    private static Object[][] out2In13() throws Exception {

        return new Object[][]{
                new Object[]{dateTimeUtil.initDateByDay() + 20 * 60 * 1000, "yu_1"},
                new Object[]{dateTimeUtil.initDateByDay() + 21 * 60 * 1000, "yu_2"},
                new Object[]{dateTimeUtil.initDateByDay() + 22 * 60 * 1000, "yu_3"},

                new Object[]{dateTimeUtil.initDateByDay() + 23 * 60 * 1000, "yu_4"},
                new Object[]{dateTimeUtil.initDateByDay() + 24 * 60 * 1000, "yu_5"},
                new Object[]{dateTimeUtil.initDateByDay() + 25 * 60 * 1000, "yu_6"},

                new Object[]{dateTimeUtil.initDateByDay() + 26 * 60 * 1000, "yu_7"},
                new Object[]{dateTimeUtil.initDateByDay() + 27 * 60 * 1000, "yu_8"},
                new Object[]{dateTimeUtil.initDateByDay() + 28 * 60 * 1000, "yu_9"},

                new Object[]{dateTimeUtil.initDateByDay() + 29 * 60 * 1000, "yu_10"},
                new Object[]{dateTimeUtil.initDateByDay() + 30 * 60 * 1000, "yu_11"},
                new Object[]{dateTimeUtil.initDateByDay() + 31 * 60 * 1000, "yu_12"},
                new Object[]{dateTimeUtil.initDateByDay() + 32 * 60 * 1000, "yu_13"},
                new Object[]{dateTimeUtil.initDateByDay() + 2 * 33 * 60 * 1000, "yu_14"},
                new Object[]{dateTimeUtil.initDateByDay() + 2 * 34 * 60 * 1000, "yu_15"}
        };
    }

    @DataProvider(name = "ALL_IN_DATE")
    private static Object[][] allInDate() throws Exception {

        return new Object[][]{
                new Object[]{dateTimeUtil.initDateByDay() + 20 * 60 * 1000, "yu_1"},
                new Object[]{dateTimeUtil.initDateByDay() + 21 * 60 * 1000, "yu_2"},
                new Object[]{dateTimeUtil.initDateByDay() + 22 * 60 * 1000, "yu_3"},

                new Object[]{dateTimeUtil.initDateByDay() + 23 * 60 * 1000, "yu_4"},
                new Object[]{dateTimeUtil.initDateByDay() + 24 * 60 * 1000, "yu_5"},
                new Object[]{dateTimeUtil.initDateByDay() + 25 * 60 * 1000, "yu_6"},

                new Object[]{dateTimeUtil.initDateByDay() + 26 * 60 * 1000, "yu_7"},
                new Object[]{dateTimeUtil.initDateByDay() + 27 * 60 * 1000, "yu_8"},
                new Object[]{dateTimeUtil.initDateByDay() + 28 * 60 * 1000, "yu_9"},

                new Object[]{dateTimeUtil.initDateByDay() + 29 * 60 * 1000, "yu_10"},
                new Object[]{dateTimeUtil.initDateByDay() + 30 * 60 * 1000, "yu_11"},
                new Object[]{dateTimeUtil.initDateByDay() + 31 * 60 * 1000, "yu_12"},
                new Object[]{dateTimeUtil.initDateByDay() + 32 * 60 * 1000, "yu_13"},
                new Object[]{dateTimeUtil.initDateByDay() + 33 * 60 * 1000, "yu_14"},
                new Object[]{dateTimeUtil.initDateByDay() + 34 * 60 * 1000, "yu_15"}
        };
    }

    @DataProvider(name = "YU_13_IN_3_OUT_PIC")
    private static Object[][] yu13In3OutPic() throws Exception {
        return new Object[][]{
                new Object[]{System.currentTimeMillis() - 24 * 60 * 60 * 1000 - 14000, "yu_1"},
                new Object[]{System.currentTimeMillis() - 24 * 60 * 60 * 1000 - 13000, "yu_2"},
                new Object[]{System.currentTimeMillis() - 24 * 60 * 60 * 1000 - 12000, "yu_1"},
                new Object[]{System.currentTimeMillis() - 24 * 60 * 60 * 1000 - 11000, "yu_3"},
                new Object[]{System.currentTimeMillis() - 24 * 60 * 60 * 1000 - 10000, "yu_4"},
                new Object[]{System.currentTimeMillis() - 24 * 60 * 60 * 1000 - 9000, "yu_5"},
                new Object[]{System.currentTimeMillis() - 24 * 60 * 60 * 1000 - 8000, "yu_6"},
                new Object[]{System.currentTimeMillis() - 24 * 60 * 60 * 1000 - 7000, "yu_7"},
                new Object[]{System.currentTimeMillis() - 24 * 60 * 60 * 1000 - 6000, "yu_8"},
                new Object[]{System.currentTimeMillis() - 24 * 60 * 60 * 1000 - 5000, "yu_9"},
                new Object[]{System.currentTimeMillis() - 24 * 60 * 60 * 1000 - 4000, "yu_10"},
                new Object[]{System.currentTimeMillis() - 24 * 60 * 60 * 1000 - 3000, "yu_11"},
                new Object[]{System.currentTimeMillis() - 24 * 60 * 60 * 1000 - 2000, "yu_12"},
                new Object[]{System.currentTimeMillis() - 24 * 60 * 60 * 1000 - 1000, "yu_13"},
                new Object[]{System.currentTimeMillis() - 10 * 24 * 60 * 60 * 1000, "yu_14"},
                new Object[]{System.currentTimeMillis() - 9 * 24 * 60 * 60 * 1000, "yu_15"},
                new Object[]{System.currentTimeMillis() - 8 * 24 * 60 * 60 * 1000, "yu_16"},
        };
    }

    @DataProvider(name = "YU_16_OUT")
    private static Object[][] yu16Out() throws Exception {
        return new Object[][]{
                new Object[]{System.currentTimeMillis() - 8 * 24 * 60 * 60 * 1000 - 14000, "yu_1"},
                new Object[]{System.currentTimeMillis() - 8 * 24 * 60 * 60 * 1000 - 13000, "yu_2"},
                new Object[]{System.currentTimeMillis() - 8 * 24 * 60 * 60 * 1000 - 12000, "yu_1"},
                new Object[]{System.currentTimeMillis() - 8 * 24 * 60 * 60 * 1000 - 11000, "yu_3"},
                new Object[]{System.currentTimeMillis() - 8 * 24 * 60 * 60 * 1000 - 10000, "yu_4"},
                new Object[]{System.currentTimeMillis() - 8 * 24 * 60 * 60 * 1000 - 9000, "yu_5"},
                new Object[]{System.currentTimeMillis() - 8 * 24 * 60 * 60 * 1000 - 8000, "yu_6"},
                new Object[]{System.currentTimeMillis() - 8 * 24 * 60 * 60 * 1000 - 7000, "yu_7"},
                new Object[]{System.currentTimeMillis() - 8 * 24 * 60 * 60 * 1000 - 6000, "yu_8"},
                new Object[]{System.currentTimeMillis() - 8 * 24 * 60 * 60 * 1000 - 5000, "yu_9"},
                new Object[]{System.currentTimeMillis() - 8 * 24 * 60 * 60 * 1000 - 4000, "yu_10"},
                new Object[]{System.currentTimeMillis() - 8 * 24 * 60 * 60 * 1000 - 3000, "yu_11"},
                new Object[]{System.currentTimeMillis() - 8 * 24 * 60 * 60 * 1000 - 2000, "yu_12"},
                new Object[]{System.currentTimeMillis() - 8 * 24 * 60 * 60 * 1000 - 1000, "yu_13"},
                new Object[]{System.currentTimeMillis() - 10 * 24 * 60 * 60 * 1000, "yu_14"},
                new Object[]{System.currentTimeMillis() - 9 * 24 * 60 * 60 * 1000, "yu_15"},
                new Object[]{System.currentTimeMillis() - 8 * 24 * 60 * 60 * 1000, "yu_16"},
        };
    }

    @DataProvider(name = "YU_ORIGIN")
    private static Object[][] yuOriginPic() throws Exception {
        return new Object[][]{
                new Object[]{System.currentTimeMillis() - 24 * 60 * 60 * 1000 - 14000, "yu_1"},
                new Object[]{System.currentTimeMillis() - 24 * 60 * 60 * 1000 - 13000, "yu_2"},
                new Object[]{System.currentTimeMillis() - 24 * 60 * 60 * 1000 - 12000, "yu_1"},
                new Object[]{System.currentTimeMillis() - 24 * 60 * 60 * 1000 - 11000, "yu_3"},
                new Object[]{System.currentTimeMillis() - 24 * 60 * 60 * 1000 - 10000, "yu_4"},
                new Object[]{System.currentTimeMillis() - 24 * 60 * 60 * 1000 - 9000, "yu_5"},
                new Object[]{System.currentTimeMillis() - 24 * 60 * 60 * 1000 - 8000, "yu_6"},
                new Object[]{System.currentTimeMillis() - 24 * 60 * 60 * 1000 - 7000, "yu_7"},
                new Object[]{System.currentTimeMillis() - 24 * 60 * 60 * 1000 - 6000, "yu_8"},
                new Object[]{System.currentTimeMillis() - 24 * 60 * 60 * 1000 - 5000, "yu_9"},
                new Object[]{System.currentTimeMillis() - 24 * 60 * 60 * 1000 - 4000, "yu_10"},
                new Object[]{System.currentTimeMillis() - 24 * 60 * 60 * 1000 - 3000, "yu_11"},
                new Object[]{System.currentTimeMillis() - 24 * 60 * 60 * 1000 - 2000, "yu_12"},
                new Object[]{System.currentTimeMillis() - 24 * 60 * 60 * 1000 - 1000, "yu_13"},
                new Object[]{System.currentTimeMillis() - 24 * 60 * 60 * 1000 - 800, "yu_14"},
                new Object[]{System.currentTimeMillis() - 24 * 60 * 60 * 1000 - 500, "yu_15"},
                new Object[]{System.currentTimeMillis() - 24 * 60 * 60 * 1000 - 300, "yu_16"},
        };
    }

    @DataProvider(name = "QLJL_3_STRANGER_6_PIC")
    private static Object[][] qljl1() throws Exception {
        return new Object[][]{
                new Object[]{"stranger", "1", "yu_1"},
                new Object[]{"stranger", "2", "yu_1"},
                new Object[]{"stranger", "3", "yu_1"},
                new Object[]{"stranger", "1", "yu_2"},
                new Object[]{"stranger", "2", "yu_2"},
                new Object[]{"stranger", "3", "yu_2"},
                new Object[]{"stranger", "1", "yu_3"},
                new Object[]{"stranger", "2", "yu_3"},
                new Object[]{"stranger", "3", "yu_3"},
                new Object[]{"stranger", "1", "yu_4"},
                new Object[]{"stranger", "2", "yu_4"},
                new Object[]{"stranger", "3", "yu_4"},
                new Object[]{"stranger", "1", "yu_5"},
                new Object[]{"stranger", "2", "yu_5"},
                new Object[]{"stranger", "3", "yu_5"},
                new Object[]{"stranger", "1", "yu_6"},
                new Object[]{"stranger", "2", "yu_6"},
                new Object[]{"stranger", "3", "yu_6"},

        };
    }

    @DataProvider(name = "QLJL_2_STRANGER_1_SPECIAL_6_PIC")
    private static Object[][] qljl2Stranger1Special() throws Exception {
        return new Object[][]{
                new Object[]{"stranger", "1", "yu_1"},
                new Object[]{"stranger", "2", "yu_1"},
                new Object[]{"special", "3", "yu_1"},
                new Object[]{"stranger", "1", "yu_2"},
                new Object[]{"stranger", "2", "yu_2"},
                new Object[]{"special", "3", "yu_2"},
                new Object[]{"stranger", "1", "yu_3"},
                new Object[]{"stranger", "2", "yu_3"},
                new Object[]{"special", "3", "yu_3"},
                new Object[]{"stranger", "1", "yu_4"},
                new Object[]{"stranger", "2", "yu_4"},
                new Object[]{"special", "3", "yu_4"},
                new Object[]{"stranger", "1", "yu_5"},
                new Object[]{"stranger", "2", "yu_5"},
                new Object[]{"special", "3", "yu_5"},
                new Object[]{"stranger", "1", "yu_6"},
                new Object[]{"stranger", "2", "yu_6"},
                new Object[]{"special", "3", "yu_6"},

        };
    }





















































    @DataProvider(name = "ZLJL_3_STRANGER_6_PIC")
    private static Object[][] zljl1() throws Exception {
        return new Object[][]{
                new Object[]{"stranger", "zljl1_1", "yu_1"},
                new Object[]{"stranger", "zljl1_2", "yu_1"},
                new Object[]{"stranger", "zljl1_3", "yu_1"},
                new Object[]{"stranger", "zljl1_1", "yu_2"},
                new Object[]{"stranger", "zljl1_2", "yu_2"},
                new Object[]{"stranger", "zljl1_3", "yu_2"},
                new Object[]{"stranger", "zljl1_1", "yu_3"},
                new Object[]{"stranger", "zljl1_2", "yu_3"},
                new Object[]{"stranger", "zljl1_3", "yu_3"},
                new Object[]{"stranger", "zljl1_1", "yu_4"},
                new Object[]{"stranger", "zljl1_2", "yu_4"},
                new Object[]{"stranger", "zljl1_3", "yu_4"},
                new Object[]{"stranger", "zljl1_1", "yu_5"},
                new Object[]{"stranger", "zljl1_2", "yu_5"},
                new Object[]{"stranger", "zljl1_3", "yu_5"},
                new Object[]{"stranger", "zljl1_1", "yu_6"},
                new Object[]{"stranger", "zljl1_2", "yu_6"},
                new Object[]{"stranger", "zljl1_3", "yu_6"},

        };
    }

    @DataProvider(name = "ZLJL_2_STRANGER_1_IN_1_OUT")
    private static Object[][] zljl3() throws Exception {
        return new Object[][]{
                new Object[]{"stranger", "zljl1_1", "yu_1", "out"},
                new Object[]{"stranger", "zljl1_2", "yu_1", "in"},
                new Object[]{"stranger", "zljl1_1", "yu_2", "out"},
                new Object[]{"stranger", "zljl1_2", "yu_2", "in"},
                new Object[]{"stranger", "zljl1_1", "yu_3", "out"},
                new Object[]{"stranger", "zljl1_2", "yu_3", "in"},
                new Object[]{"stranger", "zljl1_1", "yu_4", "out"},
                new Object[]{"stranger", "zljl1_2", "yu_4", "in"},
                new Object[]{"stranger", "zljl1_1", "yu_5", "out"},
                new Object[]{"stranger", "zljl1_2", "yu_5", "in"},
                new Object[]{"stranger", "zljl1_1", "yu_6", "out"},
                new Object[]{"stranger", "zljl1_2", "yu_6", "in"},

        };
    }

    @DataProvider(name = "ZLJL_1_STRANGER_IN_1_SPECIAL_OUT")
    private static Object[][] zljl4() throws Exception {
        return new Object[][]{
                new Object[]{"special", "zljl1_1", "yu_1", "out"},
                new Object[]{"stranger", "zljl1_2", "yu_1", "in"},
                new Object[]{"special", "zljl1_1", "yu_2", "out"},
                new Object[]{"stranger", "zljl1_2", "yu_2", "in"},
                new Object[]{"special", "zljl1_1", "yu_3", "out"},
                new Object[]{"stranger", "zljl1_2", "yu_3", "in"},
                new Object[]{"special", "zljl1_1", "yu_4", "out"},
                new Object[]{"stranger", "zljl1_2", "yu_4", "in"},
                new Object[]{"special", "zljl1_1", "yu_5", "out"},
                new Object[]{"stranger", "zljl1_2", "yu_5", "in"},
                new Object[]{"special", "zljl1_1", "yu_6", "out"},
                new Object[]{"stranger", "zljl1_2", "yu_6", "in"},

        };
    }

    @DataProvider(name = "ZLJL_1_STRANGER_OUT_1_SPECIAL_IN")
    private static Object[][] zljl5() throws Exception {
        return new Object[][]{
                new Object[]{"special", "zljl1_1", "yu_1", "in"},
                new Object[]{"stranger", "zljl1_2", "yu_1", "out"},
                new Object[]{"special", "zljl1_1", "yu_2", "in"},
                new Object[]{"stranger", "zljl1_2", "yu_2", "out"},
                new Object[]{"special", "zljl1_1", "yu_3", "in"},
                new Object[]{"stranger", "zljl1_2", "yu_3", "out"},
                new Object[]{"special", "zljl1_1", "yu_4", "in"},
                new Object[]{"stranger", "zljl1_2", "yu_4", "out"},
                new Object[]{"special", "zljl1_1", "yu_5", "in"},
                new Object[]{"stranger", "zljl1_2", "yu_5", "out"},
                new Object[]{"special", "zljl1_1", "yu_6", "in"},
                new Object[]{"stranger", "zljl1_2", "yu_6", "out"},

        };
    }

    @DataProvider(name = "ZLJL_2_STRANGER_OUT")
    private static Object[][] zljl6() throws Exception {
        return new Object[][]{
                new Object[]{"stranger", "zljl1_1", "yu_1", "out"},
                new Object[]{"stranger", "zljl1_2", "yu_1", "out"},
                new Object[]{"stranger", "zljl1_1", "yu_2", "out"},
                new Object[]{"stranger", "zljl1_2", "yu_2", "out"},
                new Object[]{"stranger", "zljl1_1", "yu_3", "out"},
                new Object[]{"stranger", "zljl1_2", "yu_3", "out"},
                new Object[]{"stranger", "zljl1_1", "yu_4", "out"},
                new Object[]{"stranger", "zljl1_2", "yu_4", "out"},
                new Object[]{"stranger", "zljl1_1", "yu_5", "out"},
                new Object[]{"stranger", "zljl1_2", "yu_5", "out"},
                new Object[]{"stranger", "zljl1_1", "yu_6", "out"},
                new Object[]{"stranger", "zljl1_2", "yu_6", "out"},

        };
    }

    @DataProvider(name = "ZLJL_1_STRANGER_OUT_1_SPECIAL_OUT")
    private static Object[][] zljl7() throws Exception {
        return new Object[][]{
                new Object[]{"special", "zljl1_1", "yu_1", "out"},
                new Object[]{"stranger", "zljl1_2", "yu_1", "out"},
                new Object[]{"special", "zljl1_1", "yu_2", "out"},
                new Object[]{"stranger", "zljl1_2", "yu_2", "out"},
                new Object[]{"special", "zljl1_1", "yu_3", "out"},
                new Object[]{"stranger", "zljl1_2", "yu_3", "out"},
                new Object[]{"special", "zljl1_1", "yu_4", "out"},
                new Object[]{"stranger", "zljl1_2", "yu_4", "out"},
                new Object[]{"special", "zljl1_1", "yu_5", "out"},
                new Object[]{"stranger", "zljl1_2", "yu_5", "out"},
                new Object[]{"special", "zljl1_1", "yu_6", "out"},
                new Object[]{"stranger", "zljl1_2", "yu_6", "out"},

        };
    }

    @DataProvider(name = "YU_6_PIC")
    private static Object[] yu6Pic() {
        return new String[]{
                "yu_1", "yu_2", "yu_3",
                "yu_4", "yu_5", "yu_6"
        };
    }

    @DataProvider(name = "YU_16_PIC")
    private static Object[] yu16Pic() {
        return new String[]{
                "yu_11", "yu_2", "yu_3",
                "yu_4", "yu_5", "yu_6", "yu_7", "yu_8",
                "yu_9", "yu_10", "yu_1", "yu_12",
                "yu_13", "yu_14", "yu_15", "yu_16",
        };
    }

    @DataProvider(name = "PLZLJH")
    private static Object[][] plzljh() {
        return new Object[][]{
                new Object[]{"yu_1", "1-1"},
                new Object[]{"yu_1", "1-2"},
                new Object[]{"yu_2", "2-1"},
                new Object[]{"yu_2", "2-2"},
                new Object[]{"yu_3", "3-1"},
                new Object[]{"yu_3", "3-2"},
                new Object[]{"yu_4", "4-1"},
                new Object[]{"yu_4", "4-2"},
                new Object[]{"yu_5", "5-1"},
                new Object[]{"yu_5", "5-2"},
                new Object[]{"yu_6", "6-1"},
                new Object[]{"yu_6", "6-2"},
                new Object[]{"yu_7", "7-1"},
                new Object[]{"yu_7", "7-2"},
                new Object[]{"yu_8", "8-1"},
                new Object[]{"yu_8", "8-2"},
                new Object[]{"yu_9", "9-1"},
                new Object[]{"yu_9", "9-2"},
                new Object[]{"yu_10", "10-1"},
                new Object[]{"yu_10", "10-2"},
                new Object[]{"yu_11", "11-1"},
                new Object[]{"yu_11", "11-2"},
                new Object[]{"yu_12", "12-1"},
                new Object[]{"yu_12", "12-2"},
                new Object[]{"yu_13", "13-1"},
                new Object[]{"yu_13", "13-2"},
                new Object[]{"yu_14", "14-1"},
                new Object[]{"yu_14", "14-2"},
                new Object[]{"yu_15", "15-1"},
                new Object[]{"yu_15", "15-2"},
                new Object[]{"yu_16", "16-1"},
                new Object[]{"yu_16", "16-2"},
        };
    }

    @DataProvider(name = "YU_16_DUAN_3_PIC")
    private static Object[] yu16Duan3Pic() {
        return new String[]{
                "yu_1", "yu_2", "yu_3",
                "yu_4", "yu_5", "yu_6", "yu_7", "yu_8",
                "yu_9", "yu_10", "yu_11", "yu_12",
                "yu_13", "yu_14", "yu_15", "yu_16",
                "duan_1", "duan_2", "duan_3"
        };
    }

    @DataProvider(name = "DUAN_PIC")
    private static Object[] duanPic() {
        return new String[]{
                "duan_1", "duan_2", "duan_3",
                "duan_4", "duan_5", "duan_6", "duan_7", "duan_8",
                "duan_9", "duan_10", "duan_11"
        };
    }

    @DataProvider(name = "YANG_PIC")
    private static Object[] yangPic() {
        return new String[]{
                "yang_1", "yang_2", "yang_3",
                "yang_4", "yang_5", "yang_6", "yang_7", "yang_8",
                "yang_9"
        };
    }

    @DataProvider(name = "LIAO_PIC")
    private static Object[] liaoPic() {
        return new String[]{
                "liao_1", "liao_2", "liao_3",
                "liao_4", "liao_5", "liao_6", "liao_7", "liao_8",
                "liao_9", "liao_10"
        };
    }

    @DataProvider(name = "TIME_ID")
    private static Object[][] timeId() {
//        yyyy-MM-dd HH:mm:ss
        return new Object[][]{
                new Object[]{"2018-08-08 00:00:00", "1"},
                new Object[]{"2018-08-09 23:59:59", "2"},
                new Object[]{"2018-08-10 00:00:00", "3"},
                new Object[]{"2018-08-10 00:00:01", "4"},
                new Object[]{"2018-08-10 23:59:59", "5"},

                new Object[]{"2018-08-11 00:00:00", "6"},
                new Object[]{"2018-08-11 00:00:01", "7"},
                new Object[]{"2018-09-09 23:59:59", "8"},
                new Object[]{"2018-09-10 00:00:00", "9"},
                new Object[]{"2018-09-10 00:00:01", "10"},

                new Object[]{"2019-08-08 00:00:00", "11"},
                new Object[]{"2019-08-09 23:59:59", "12"},
                new Object[]{"2019-08-10 00:00:00", "13"},
                new Object[]{"2019-08-10 10:00:00", "14"},
                new Object[]{"2019-08-10 23:59:59", "15"},

                new Object[]{"2019-08-11 00:00:00", "16"},
                new Object[]{"2019-08-11 00:00:01", "17"},
                new Object[]{"2019-09-02 00:00:00", "18"},
                new Object[]{"2019-09-02 23:59:59", "19"},
                new Object[]{"2019-09-03 00:00:00", "20"},

                new Object[]{"2019-09-03 00:00:01", "21"},
                new Object[]{"2019-09-03 10:00:00", "22"},
                new Object[]{"2019-09-05 10:00:00", "23"},
                new Object[]{"2019-09-08 10:00:00", "24"},
                new Object[]{"2019-09-08 23:59:59", "25"},

                new Object[]{"2019-09-09 00:00:00", "26"},
                new Object[]{"2019-09-09 00:00:01", "27"},
                new Object[]{"2019-09-09 10:00:00", "28"},
                new Object[]{"2019-09-09 23:59:59", "29"},
                new Object[]{"2019-09-10 00:00:00", "30"},

                new Object[]{"2019-09-10 00:00:01", "31"},
                new Object[]{"2019-09-10 10:00:00", "32"}
        };
    }

    @DataProvider(name = "BOTH_OUT_OF_TIME_STRANGER")
    private static Object[][] testbothOutOfDateStranger() throws Exception {
        return new Object[][]{
                new Object[]{dateTimeUtil.dateToTimestamp("2019/09/01 00:00:00:000"), "liao_1"},
                new Object[]{dateTimeUtil.dateToTimestamp("2019/09/02 00:00:00:000"), "liao_2"},
                new Object[]{dateTimeUtil.dateToTimestamp("2019/09/03 00:00:00:000"), "liao_3"},
        };
    }

    @DataProvider(name = "BOTH_OUT_OF_TIME_SPECIAL")
    private static Object[][] testbothOutOfDateSpecial() throws Exception {
        return new Object[][]{
                new Object[]{dateTimeUtil.dateToTimestamp("2019/09/01 00:00:00:000"), "liao_1"},
                new Object[]{dateTimeUtil.dateToTimestamp("2019/09/02 00:00:00:000"), "liao_2"},
                new Object[]{dateTimeUtil.dateToTimestamp("2019/09/03 00:00:00:000"), "liao_3"},
        };
    }

    @DataProvider(name = "ONE_IN_TIME")
    private static Object[][] testOneInDateStranger() throws Exception {
        return new Object[][]{
                new Object[]{System.currentTimeMillis() - 5 * 24 * 60 * 60 * 1000, "yu_15"},
//                new Object[]{System.currentTimeMillis()-3*24*60*60*1000, "yu_1"},
//                new Object[]{System.currentTimeMillis()-4*24*60*60*1000, "yu_2"},
//                new Object[]{System.currentTimeMillis()-49*60*1000, "yu_3"},
//                new Object[]{System.currentTimeMillis()-50*60*1000, "yu_4"},
//                new Object[]{System.currentTimeMillis()-51*60*1000, "yu_5"},
//                new Object[]{System.currentTimeMillis()-52*60*1000, "yu_6"},
//                new Object[]{System.currentTimeMillis()-53*60*1000, "yu_7"},
//                new Object[]{System.currentTimeMillis()-54*60*1000, "yu_8"},
//                new Object[]{System.currentTimeMillis()-55*60*1000, "yu_9"},
//                new Object[]{System.currentTimeMillis()-56*60*1000, "yu_10"},
//                new Object[]{System.currentTimeMillis()-57*60*1000, "yu_11"},
//                new Object[]{System.currentTimeMillis()-58*60*1000, "yu_12"},
//                new Object[]{System.currentTimeMillis()-59*60*1000, "yu_13"},
//                new Object[]{System.currentTimeMillis()-60*60*1000, "yu_14"}
        };
    }

    @DataProvider(name = "ALL_IN_TIME")
    private static Object[][] testAllInDateStranger() throws Exception {
        return new Object[][]{
                new Object[]{System.currentTimeMillis() - 47 * 60 * 1000, "yu_1"},
                new Object[]{System.currentTimeMillis() - 48 * 60 * 1000, "yu_2"},
                new Object[]{System.currentTimeMillis() - 49 * 60 * 1000, "yu_3"},
                new Object[]{System.currentTimeMillis() - 50 * 60 * 1000, "yu_4"},
                new Object[]{System.currentTimeMillis() - 51 * 60 * 1000, "yu_5"},
                new Object[]{System.currentTimeMillis() - 52 * 60 * 1000, "yu_6"},
                new Object[]{System.currentTimeMillis() - 53 * 60 * 1000, "yu_7"},
                new Object[]{System.currentTimeMillis() - 54 * 60 * 1000, "yu_8"},
                new Object[]{System.currentTimeMillis() - 55 * 60 * 1000, "yu_9"},
                new Object[]{System.currentTimeMillis() - 56 * 60 * 1000, "yu_10"},
                new Object[]{System.currentTimeMillis() - 57 * 60 * 1000, "yu_11"},
                new Object[]{System.currentTimeMillis() - 58 * 60 * 1000, "yu_12"},
                new Object[]{System.currentTimeMillis() - 59 * 60 * 1000, "yu_13"},
                new Object[]{System.currentTimeMillis() - 60 * 60 * 1000, "yu_14"}
        };
    }

    public void testCalendar() {
        Calendar c = Calendar.getInstance();


    }


}
