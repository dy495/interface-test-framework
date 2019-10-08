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
import java.util.Random;
import java.util.UUID;

public class CustomerTest {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private LogMine logMine = new LogMine(logger);

    String scope = "2466";
    String appId = "232a40e4d37c";
    String deviceId = "6761965967213568";
    String grpName = "vipGrp";

    String updateCustomerUrl = "http://47.95.69.163/gate/manage/updateCustomer";
    String updateImageUrl = "http://47.95.69.163/gate/manage/updateCustomerImageOne";
    String queryCustomerUrl = "http://47.95.69.163/gate/manage/queryCustomer";

    private String strangerPerfix = "STRANGER@@";
    private String specialPerfix = "SPECIAL@@vipGrp@@";

    private static DateTimeUtil dateTimeUtil = new DateTimeUtil();
    OssClientUtil ossClientUtil = new OssClientUtil();
    private static int num = 0;

    @Test(dataProvider = "PLZLJH")
    public void testplzljh(String picUrl, String userId) throws Exception {
        userId = strangerPerfix + userId;
        long startTime = System.currentTimeMillis() - 5;
        long endTime = System.currentTimeMillis();
        register(picUrl, userId, startTime, endTime, GroupType.DEFAULT, genQuality());

//        String response = queryUser(userId);
//        String faceId = getFaceId(response, picUrl);

//        long updateTimeStamp = System.currentTimeMillis();
//
//        updateCustomertime(updateCustomerUrl, userId, updateTimeStamp);
//        updateImagetime(userId, faceId, updateTimeStamp);
    }

    //    ----------------人物清理------1、以天周月年为维度清理用户------------------

    @Test(dataProvider = "TIME_ID")
    public void testclearLimit(String time, String customerId) throws Exception {
        String picUrl = "liao_1";
        String userId = strangerPerfix + customerId;
//        String userId = customerId;
        long startTime = System.currentTimeMillis() - 100;
        long endTime = System.currentTimeMillis();
        GroupType type = GroupType.DEFAULT;

        register(picUrl, userId, startTime, endTime, type, genQuality());

        String response = queryUser(userId);

        String faceId = getFaceId(response, picUrl);

        time = time.replace("-", "/") + ":000";

        System.out.println(Long.valueOf(dateTimeUtil.dateToTimestamp(time)));

        long timestamp = Long.valueOf(dateTimeUtil.dateToTimestamp(time)) + 24 * 60 * 60 * 1000;

        updateCustomertime(updateCustomerUrl, userId, timestamp);
        updateImagetime(userId, faceId, timestamp);
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

    private String sendRequestWithUrl(String url, String json) throws Exception {
        HttpExecutorUtil executor = new HttpExecutorUtil();
        executor.doPostJson(url, json);
        return executor.getResponse();
    }

    @Test
    public void initDateByDay() {

        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        Long today = c.getTimeInMillis();

        System.out.println(today);

    }

    private static Object[][] allMode() {
        return new Object[][]{
                new Object[]{dateTimeUtil.initDateByDay() + 1, "12"},
                new Object[]{dateTimeUtil.initDateByDay() + 1, "12"},
                new Object[]{dateTimeUtil.initDateByDay() + 1, "12"},
                new Object[]{dateTimeUtil.initDateByDay() + 1, "12"},
                new Object[]{dateTimeUtil.initDateByDay() + 1, "12"},
//                new Object[]{dateTimeUtil.initDateByDay() + ,"12"},
                new Object[]{dateTimeUtil.initDateByDay() - 7 * 24 * 60 * 60 * 1000 - 1, "7"},
                new Object[]{dateTimeUtil.initDateByDay() - 7 * 24 * 60 * 60 * 1000, "8"},
                new Object[]{dateTimeUtil.initDateByDay() - 7 * 24 * 60 * 60 * 1000 + 1, "9"},
                new Object[]{dateTimeUtil.initDateByDay() - 1, "10"},
                new Object[]{dateTimeUtil.initDateByDay(), "11"},
                new Object[]{dateTimeUtil.initDateByDay() + 1, "12"},
                new Object[]{dateTimeUtil.initDateByDay() + 2 * 60 * 60 * 1000, "13"},
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
//                new Object[]{System.currentTimeMillis()-8*24*60*60*1000, "yu_16"},
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

    @DataProvider(name = "ZLJL_2_STRANGER_1_SPECIAL_6_PIC")
    private static Object[][] zljl2() throws Exception {
        return new Object[][]{
                new Object[]{"stranger", "zljl1_1", "yu_1"},
                new Object[]{"stranger", "zljl1_2", "yu_1"},
                new Object[]{"special", "zljl1_3", "yu_1"},
                new Object[]{"stranger", "zljl1_1", "yu_2"},
                new Object[]{"stranger", "zljl1_2", "yu_2"},
                new Object[]{"special", "zljl1_3", "yu_2"},
                new Object[]{"stranger", "zljl1_1", "yu_3"},
                new Object[]{"stranger", "zljl1_2", "yu_3"},
                new Object[]{"special", "zljl1_3", "yu_3"},
                new Object[]{"stranger", "zljl1_1", "yu_4"},
                new Object[]{"stranger", "zljl1_2", "yu_4"},
                new Object[]{"special", "zljl1_3", "yu_4"},
                new Object[]{"stranger", "zljl1_1", "yu_5"},
                new Object[]{"stranger", "zljl1_2", "yu_5"},
                new Object[]{"special", "zljl1_3", "yu_5"},
                new Object[]{"stranger", "zljl1_1", "yu_6"},
                new Object[]{"stranger", "zljl1_2", "yu_6"},
                new Object[]{"special", "zljl1_3", "yu_6"},

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
