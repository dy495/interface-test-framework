package com.haisheng.framework.testng.defence.online;

import com.google.common.base.Preconditions;
import com.haisheng.framework.model.bean.Case;
import com.haisheng.framework.testng.defence.online.DefenceOnline;
import com.haisheng.framework.util.CheckUtil;
import com.haisheng.framework.util.DateTimeUtil;
import com.haisheng.framework.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class DefenceConsistencyOnline {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    //    工具类变量
    StringUtil stringUtil = new StringUtil();
    DateTimeUtil dt = new DateTimeUtil();
    CheckUtil checkUtil = new CheckUtil();
    DefenceOnline defenceOnline = new DefenceOnline();

    //    入库相关变量
    private String failReason = "";
    private Case aCase = new Case();

    //    case相关变量
    public String CUSTOMER_REGISTER_ROUTER = "/business/defenceOnline/CUSTOMER_REGISTER/v1.0";
    public String CUSTOMER_DELETE_ROUTER = "/business/defenceOnline/CUSTOMER_DELETE/v1.0";

    public final long VILLAGE_ID = 8;


    @Test
    public void alarmLogPageEqualsAlarm() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "告警记录(分页查询)中的告警条数==告警统计中的次数";

        logger.info("\n\n" + caseName + "\n");

        try {

//            String deviceId = defenceOnline.device1Caiwu;
//            String deviceId = defenceOnline.deviceXieduimen;
            String deviceId = defenceOnline.deviceIdJinmen;

//            告警记录(分页查询)
            int total = defenceOnline.alarmLogPage(deviceId, 1, 100).getJSONObject("data").getInteger("total");

//            告警统计
            int alarmCount = defenceOnline.deviceAlarmStatistic(deviceId).getJSONObject("data").getInteger("alarm_count");

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
            defenceOnline.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
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

            String faceUrl = defenceOnline.liaoFaceUrlNew;
            String customerId = "";
            String namePhone = "";
            String similarity = "HIGH";
            String device_id = "";

            long startTime = System.currentTimeMillis() - 24 * 60 * 60 * 1000;
            long endTime = System.currentTimeMillis();

//            人脸识别记录分页查询

            int high = defenceOnline.customerHistoryCapturePage(faceUrl, customerId, device_id, namePhone, similarity,
                    startTime, endTime, 1, 10).getJSONObject("data").getInteger("total");

            similarity = "LOW";
            int low = defenceOnline.customerHistoryCapturePage(faceUrl, customerId, device_id, namePhone, similarity,
                    startTime, endTime, 1, 10).getJSONObject("data").getInteger("total");

            similarity = "";

            int all = defenceOnline.customerHistoryCapturePage(faceUrl, customerId, device_id, namePhone, similarity,
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
            defenceOnline.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
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
            String[] devices = {defenceOnline.deviceIdJinmen, defenceOnline.deviceIdYinshuiji};

            long startTime = System.currentTimeMillis() - 24 * 60 * 60 * 1000;
            long endTime = System.currentTimeMillis();

            int total = 0;

            for (int i = 0; i < devices.length; i++) {

//            人脸识别记录分页查询
                total += defenceOnline.customerHistoryCapturePage(faceUrl, devices[i], startTime, endTime, 1, 10).
                        getJSONObject("data").getInteger("total");
            }


            int total1 = defenceOnline.customerHistoryCapturePage(faceUrl, "", startTime, endTime, 1, 10).
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
            defenceOnline.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
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

            String faceUrl = defenceOnline.liaoFaceUrlNew;
            String customerId = "";
            String namePhone = "";
            String similarity = "";

            long startTime = System.currentTimeMillis() - 2 * 24 * 60 * 60 * 1000;
            long endTime = System.currentTimeMillis() - 24 * 60 * 60 * 1000;

            int total = 0;

            long now = System.currentTimeMillis();

            int total1 = defenceOnline.customerHistoryCapturePage(faceUrl, "", startTime, endTime, 1, 10).
                    getJSONObject("data").getInteger("total");

//            昨天的数据
            long startTime1 = now - 24 * 60 * 60 * 1000;
            long endTime1 = now;
            int total2 = defenceOnline.customerHistoryCapturePage(faceUrl, "", startTime1, endTime1, 1, 10).
                    getJSONObject("data").getInteger("total");

//            昨天+前天的数据
            int total3 = defenceOnline.customerHistoryCapturePage(faceUrl, "", startTime, endTime1, 1, 10).
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
            defenceOnline.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
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

            String picUrl = defenceOnline.liaoFaceUrlNew;
            String similarity = "HIGH";
            long startTime = System.currentTimeMillis() - 24 * 60 * 60 * 1000;
            long endTime = System.currentTimeMillis();

//            轨迹查询(人脸搜索)
            int high = defenceOnline.customerFaceTraceList(picUrl, startTime, endTime, similarity).getJSONObject("data").getInteger("total");

            similarity = "LOW";
            int low = defenceOnline.customerFaceTraceList(picUrl, startTime, endTime, similarity).getJSONObject("data").getInteger("total");

            similarity = "";
            int all = defenceOnline.customerFaceTraceList(picUrl, startTime, endTime, similarity).getJSONObject("data").getInteger("total");

            Preconditions.checkArgument(high + low <= all, "轨迹查询（人脸搜索），相似度高的结果=" + high +
                    "+相似度低的结果=" + low + "，!=不选择相似度的结果=" + all);

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            defenceOnline.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
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

            String picUrl = defenceOnline.liaoFaceUrlNew;
            String similarity = "HIGH";
//            前天的数据
            long now = System.currentTimeMillis();

            long startTime = now - 48 * 60 * 60 * 1000;
            long endTime = now - 24 * 60 * 60 * 1000;

            int total1 = defenceOnline.customerFaceTraceList(picUrl, startTime, endTime, similarity, 1, 100).getJSONObject("data").getInteger("total");

//            昨天的数据
            long startTime1 = now - 24 * 60 * 60 * 1000;
            long endTime1 = now;
            int total2 = defenceOnline.customerFaceTraceList(picUrl, startTime1, endTime1, similarity, 1, 100).getJSONObject("data").getInteger("total");

//            昨天+前天的数据
            int total3 = defenceOnline.customerFaceTraceList(picUrl, startTime, endTime1, similarity, 1, 100).getJSONObject("data").getInteger("total");

            Preconditions.checkArgument(total1 + total2 == total3, "轨迹查询（人脸搜索），前48-24小时的数据条数=" + total1 +
                    "+前24-现在的数据条数=" + total2 + "！=时间选择前48h-现在的数据条数=" + total3);


        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            defenceOnline.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    @Test
    public void alarmLogEqualsStatistics() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "告警记录中的条数==告警统计中的条数";

        logger.info("\n\n" + caseName + "\n");

        try {

            String[] devices = {defenceOnline.deviceIdYinshuiji, defenceOnline.deviceIdJinmen};


//            String deviceId = defenceOnline.device1Caiwu;
//            String deviceId = defenceOnline.device1Huiyi;
//            String deviceId = defenceOnline.deviceYilaoshi;
//            String deviceId = defenceOnline.deviceXieduimen;
//            String deviceId = defenceOnline.deviceChukou;
//            String deviceId = defenceOnline.deviceDongbeijiao;

            for (int i = 0; i < devices.length; i++) {
//                告警记录
                Integer total = defenceOnline.alarmLogPage(devices[i], 1, 1).getJSONObject("data").getInteger("total");

//                报警统计
                Integer total1 = defenceOnline.deviceAlarmStatistic(devices[i]).getJSONObject("data").getInteger("alarm_count");

                Preconditions.checkArgument(total == total1, "告警记录中的总条数=" + total + "！=报警统计中的总条数=" + total1
                        + "，deviceId=" + devices[i]);
            }

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            defenceOnline.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    @Test
    public void captureEqualsFaceTrace() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "告警记录中的条数==告警统计中的条数";

        logger.info("\n\n" + caseName + "\n");

        try {

            String faceUrl = defenceOnline.liaoFaceUrlNew;
//        String faceUrl = defenceOnline.yuFaceUrlNew;
//        String faceUrl = defenceOnline.xuyanFaceUrlNew;
//        String faceUrl = defenceOnline.huaFaceUrlNew;
//            String faceUrl = defenceOnline.qiaoFaceUrlNew;

            String[] faces = {defenceOnline.liaoFaceUrlNew, defenceOnline.yuFaceUrlNew, defenceOnline.xueqingFaceUrl, defenceOnline.huaFaceUrlNew, defenceOnline.qiaoFaceUrlNew};

            String[] similaritys = {"LOW", "HIGH", ""};

            for (int j = 0; j < faces.length; j++) {
                for (int i = 0; i < similaritys.length; i++) {
//            人脸识别记录分页查询
                    int total1 = defenceOnline.customerHistoryCapturePage(faces[j], "", "", "", similaritys[i], 0, 0, 1, 1).
                            getJSONObject("data").getInteger("total");

//            轨迹查询
                    int total2 = defenceOnline.customerFaceTraceList(faces[j], 0, 0, similaritys[i]).
                            getJSONObject("data").getInteger("total");

                    Preconditions.checkArgument(total1 == total2, "人脸识别记录分页查询中返回的结果数=" + total1 +
                            "!=轨迹查询中返回的结果数=" + total2 + "，similarity=" + similaritys[i] +"，faceUrl=" + faces[j]);
                }
            }

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            defenceOnline.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    @BeforeClass
    public void initial() {
        defenceOnline.initial();
    }

    @AfterClass
    public void clean() {
        defenceOnline.clean();
    }

    @BeforeMethod
    public void initialVars() {
        failReason = "";
        aCase = new Case();
    }
}

