package com.haisheng.framework.testng.defence;

import ai.winsense.model.ApiResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.model.bean.Case;
import com.haisheng.framework.util.CheckUtil;
import com.haisheng.framework.util.DateTimeUtil;
import com.haisheng.framework.util.StatusCode;
import com.haisheng.framework.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.*;

public class DefenceConsistencyDaily {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    //    工具类变量
    StringUtil stringUtil = new StringUtil();
    DateTimeUtil dt = new DateTimeUtil();
    CheckUtil checkUtil = new CheckUtil();
    Defence defence = new Defence();

    //    入库相关变量
    private String failReason = "";
    private Case aCase = new Case();

    //    case相关变量
    public String CUSTOMER_REGISTER_ROUTER = "/business/defence/CUSTOMER_REGISTER/v1.0";
    public String CUSTOMER_DELETE_ROUTER = "/business/defence/CUSTOMER_DELETE/v1.0";

    public final long VILLAGE_ID = 8;

//    ------------------------------------------------------非创单验证（其他逻辑）-------------------------------------


    @Test
    public void alarmLogPageOperateTest() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "告警记录(分页查询)中的告警条数==告警统计中的次数";

        logger.info("\n\n" + caseName + "\n");

        try {

//            String deviceId = defence.device1Caiwu;
//            String deviceId = defence.deviceXieduimen;
            String deviceId = defence.deviceDongbeijiao;

//            告警记录(分页查询)
            int total = defence.alarmLogPage(deviceId, 1, 100).getJSONObject("data").getInteger("total");

//            告警统计
            int alarmCount = defence.deviceAlarmStatistic(deviceId).getJSONObject("data").getInteger("alarm_count");

            if (total != alarmCount) {
                throw new Exception("告警记录（分页查询）中的记录条数=" + total + "，告警统计中的总数=" + alarmCount);
            }

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            defence.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    @Test
    public void customerHistoryCapturePageSimilarity() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "人脸识别记录分页查询";

        logger.info("\n\n" + caseName + "\n");

        try {

            String faceUrl = defence.liaoFaceUrlNew;
            String customerId = "";
            String namePhone = "";
            String similarity = "HIGH";
            String device_id = "";

            long startTime = System.currentTimeMillis() - 24 * 60 * 60 * 1000;
            long endTime = System.currentTimeMillis();

//            人脸识别记录分页查询

            int high = defence.customerHistoryCapturePage(faceUrl, customerId, device_id, namePhone, similarity,
                    startTime, endTime, 1, 10).getJSONObject("data").getInteger("total");

            similarity = "LOW";
            int low = defence.customerHistoryCapturePage(faceUrl, customerId, device_id, namePhone, similarity,
                    startTime, endTime, 1, 10).getJSONObject("data").getInteger("total");

            similarity = "";

            int all = defence.customerHistoryCapturePage(faceUrl, customerId, device_id, namePhone, similarity,
                    startTime, endTime, 1, 10).getJSONObject("data").getInteger("total");

            Preconditions.checkArgument(high + low <= all, "人脸识别记录分页查询，相似度高的结果=" + high +
                    "+相似度低的结果=" + low + "，!=不选择相似度的结果=" + all);

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            defence.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    @Test
    public void customerHistoryCapturePageTestDeviceId() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "人脸识别记录分页查询";

        logger.info("\n\n" + caseName + "\n");

        try {

            String faceUrl = "";
            String customerId = "";
            String namePhone = "";
            String similarity = "";
            String[] devices = {defence.device1Caiwu, defence.device1Huiyi, defence.deviceYilaoshi,
                    defence.deviceXieduimen, defence.deviceChukou, defence.deviceDongbeijiao};

            long startTime = System.currentTimeMillis() - 24 * 60 * 60 * 1000;
            long endTime = System.currentTimeMillis();

            int total = 0;

            for (int i = 0; i < devices.length; i++) {

//            人脸识别记录分页查询
                total += defence.customerHistoryCapturePage(faceUrl, devices[i], startTime, endTime, 1, 10).
                        getJSONObject("data").getInteger("total");
            }


            int total1 = defence.customerHistoryCapturePage(faceUrl, "", startTime, endTime, 1, 10).
                    getJSONObject("data").getInteger("total");

            Preconditions.checkArgument(total1 == total, "所有设备的累计记录数=" + total +
                    "，不选择设备时的记录数=" + total1);


        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            defence.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    @Test
    public void customerHistoryCapturePageTime() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "人脸识别记录分页查询";

        logger.info("\n\n" + caseName + "\n");

        try {

            String faceUrl = defence.liaoFaceUrlNew;
            String customerId = "";
            String namePhone = "";
            String similarity = "";

            long startTime = System.currentTimeMillis() - 2 * 24 * 60 * 60 * 1000;
            long endTime = System.currentTimeMillis() - 24 * 60 * 60 * 1000;

            int total = 0;

            long now = System.currentTimeMillis();

            int total1 = defence.customerHistoryCapturePage(faceUrl, "", startTime, endTime, 1, 10).
                    getJSONObject("data").getInteger("total");

//            昨天的数据
            long startTime1 = now - 24 * 60 * 60 * 1000;
            long endTime1 = now;
            int total2 = defence.customerHistoryCapturePage(faceUrl, "", startTime1, endTime1, 1, 10).
                    getJSONObject("data").getInteger("total");

//            昨天+前天的数据
            int total3 = defence.customerHistoryCapturePage(faceUrl, "", startTime, endTime1, 1, 10).
                    getJSONObject("data").getInteger("total");

            Preconditions.checkArgument(total1 + total2 == total3, "人脸识别记录分页查询，前48-24小时的数据条数=" + total1 +
                    "+前24-现在的数据条数=" + total2 + "！=时间选择前48h-现在的数据条数=" + total3);

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            defence.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }


    @Test
    public void customerFaceTraceListSimilarity() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "轨迹查询(人脸搜索)";

        logger.info("\n\n" + caseName + "\n");

        try {

            String picUrl = defence.liaoFaceUrlNew;
            String similarity = "HIGH";
            long startTime = System.currentTimeMillis() - 24 * 60 * 60 * 1000;
            long endTime = System.currentTimeMillis();

//            轨迹查询(人脸搜索)
            int high = defence.customerFaceTraceList(picUrl, startTime, endTime, similarity).getJSONObject("data").getInteger("total");

            similarity = "LOW";
            int low = defence.customerFaceTraceList(picUrl, startTime, endTime, similarity).getJSONObject("data").getInteger("total");

            similarity = "";
            int all = defence.customerFaceTraceList(picUrl, startTime, endTime, similarity).getJSONObject("data").getInteger("total");

            Preconditions.checkArgument(high + low <= all, "轨迹查询（人脸搜索），相似度高的结果=" + high +
                    "+相似度低的结果=" + low + "，!=不选择相似度的结果=" + all);

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            defence.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    @Test
    public void customerFaceTraceListTime() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "轨迹查询(人脸搜索)";

        logger.info("\n\n" + caseName + "\n");

        try {

            String picUrl = defence.liaoFaceUrlNew;
            String similarity = "HIGH";
//            前天的数据
            long now = System.currentTimeMillis();

            long startTime = now - 48 * 60 * 60 * 1000;
            long endTime = now - 24 * 60 * 60 * 1000;

            int total1 = defence.customerFaceTraceList(picUrl, startTime, endTime, similarity, 1, 100).getJSONObject("data").getInteger("total");

//            昨天的数据
            long startTime1 = now - 24 * 60 * 60 * 1000;
            long endTime1 = now;
            int total2 = defence.customerFaceTraceList(picUrl, startTime1, endTime1, similarity, 1, 100).getJSONObject("data").getInteger("total");

//            昨天+前天的数据
            int total3 = defence.customerFaceTraceList(picUrl, startTime, endTime1, similarity, 1, 100).getJSONObject("data").getInteger("total");

            Preconditions.checkArgument(total1 + total2 == total3, "轨迹查询（人脸搜索），前48-24小时的数据条数=" + total1 +
                    "+前24-现在的数据条数=" + total2 + "！=时间选择前48h-现在的数据条数=" + total3);


        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            defence.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    //    @Test
    public void customerSearchListSimilarity() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "结构化检索(分页查询)，查询条件similarity=HIGH+LOW的==不填写similarity的结果条数";

        logger.info("\n\n" + caseName + "\n");

        try {

            String deviceId = "";
            long startTime = System.currentTimeMillis() - 24 * 60 * 60 * 1000;
            long endTime = System.currentTimeMillis();
            String sex = "";//MALE/FEMALE
            String age = "";
            String hair = "";//SHORT \| LONG
            String clothes = "";
            String clothesColour = "";
            String trousers = "";
            String trousersColour = "";
            String hat = "";
            String knapsack = "";
            String similarity = "HIGH";

//            结构化检索(分页查询)
            int high = defence.customerSearchList(deviceId, startTime, endTime,
                    sex, age, hair, clothes, clothesColour, trousers, trousersColour, hat, knapsack, similarity, 1, 100).getJSONObject("data").getInteger("total");

            similarity = "LOW";
            int low = defence.customerSearchList(deviceId, startTime, endTime,
                    sex, age, hair, clothes, clothesColour, trousers, trousersColour, hat, knapsack, similarity, 1, 100).getJSONObject("data").getInteger("total");

            similarity = "";
            int all = defence.customerSearchList(deviceId, startTime, endTime,
                    sex, age, hair, clothes, clothesColour, trousers, trousersColour, hat, knapsack, similarity, 1, 100).getJSONObject("data").getInteger("total");

            Preconditions.checkArgument(high + low <= all, "结构化检索，相似度高的结果=" + high +
                    "+相似度低的结果=" + low + "，!=不选择相似度的结果=" + all);

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            defence.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    @BeforeClass
    public void initial() {
        defence.initial();
    }

    @AfterClass
    public void clean() {
        defence.clean();
    }

    @BeforeMethod
    public void initialVars() {
        failReason = "";
        aCase = new Case();
    }
}

