package com.haisheng.framework.testng.defence.online;

import com.google.common.base.Preconditions;
import com.haisheng.framework.model.bean.Case;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.*;

public class DefenceConsistencyOnline {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    //    工具类变量
    DefenceOnline defence = new DefenceOnline();

    //    入库相关变量
    private String failReason = "";
    private Case aCase = new Case();

    @Test
    public void alarmLogPageEqualsStatistics() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "告警记录(分页查询)中的告警条数==告警统计中的次数";

        logger.info("\n\n" + caseName + "\n");

        try {

            String[] devices = {defence.deviceIdJinmen, defence.deviceIdYinshuiji};

            for (int i = 0; i < devices.length; i++) {

//            告警记录(分页查询)
                int total = defence.alarmLogPage(devices[i], 1, 1).getJSONObject("data").getInteger("total");

//            告警统计
                int alarmCount = defence.deviceAlarmStatistic(devices[i]).getJSONObject("data").getInteger("alarm_count");

                if (total != alarmCount) {
                    throw new Exception("设备id=" + devices[i] + "，告警记录（分页查询）中的记录条数=" + total + "，告警统计中的总数=" + alarmCount);
                }
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
    public void alarmLogPageDevices() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "告警记录-各个设备的告警记录累计和==不选择设备时的告警记录总和";

        logger.info("\n\n" + caseName + "\n");

        try {

            String[] devices = {defence.deviceIdJinmen, defence.deviceIdYinshuiji};

            int deviceTotal = 0;

            for (int i = 0; i < devices.length; i++) {

//            告警记录(分页查询)
                deviceTotal += defence.alarmLogPage(devices[i], 1, 1).getJSONObject("data").getInteger("total");
            }

//            不选择设备
            int NoDeviceTotal = defence.alarmLogPage("", 1, 1).getJSONObject("data").getInteger("total");
            Preconditions.checkArgument(deviceTotal == NoDeviceTotal, "告警记录中各个设备的累计记录和=" + deviceTotal +
                    "!=不选择设备时的记录数=" + NoDeviceTotal);

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
    public void captureEqualsFaceTrace() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "人脸识别记录中的人脸数==轨迹查询中的人脸数";

        logger.info("\n\n" + caseName + "\n");

        try {

            String[] faces = {defence.liaoFaceUrlNew, defence.yuFaceUrlNew, defence.xueqingFaceUrl, defence.huaFaceUrlNew, defence.qiaoFaceUrlNew};

            String[] similaritys = {"LOW", "HIGH", ""};

            for (int j = 0; j < faces.length; j++) {
                for (int i = 0; i < similaritys.length; i++) {
//            人脸识别记录分页查询
                    int total1 = defence.customerHistoryCapturePage(faces[j], "", "", "", similaritys[i], 0, 0, 1, 1).
                            getJSONObject("data").getInteger("total");

//            轨迹查询
                    int total2 = defence.customerFaceTraceList(faces[j], 0, 0, similaritys[i]).
                            getJSONObject("data").getInteger("total");

                    Preconditions.checkArgument(total1 == total2, "人脸识别记录分页查询中返回的结果数=" + total1 +
                            "!=轨迹查询中返回的结果数=" + total2 + "，similarity=" + similaritys[i] + "，faceUrl=" + faces[j]);
                }
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

        String caseDesc = "人脸识别记录分页查询-相似度高+相似度低==不选择相似度";

        logger.info("\n\n" + caseName + "\n");

        try {

            String faceUrl = defence.liaoFaceUrlNew;
            String customerId = "";
            String namePhone = "";
            String device_id = "";

            long startTime = System.currentTimeMillis() - 24 * 60 * 60 * 1000;
            long endTime = System.currentTimeMillis();

//            人脸识别记录分页查询
            String similarity = "HIGH";
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

        String caseDesc = "人脸识别记录分页查询-各个设备的累加和==不选择设备";

        logger.info("\n\n" + caseName + "\n");

        try {

            String faceUrl = "";
            String[] devices = {defence.deviceIdJinmen, defence.deviceIdYinshuiji};

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

        String caseDesc = "人脸识别记录分页查询-前（48-24）小时的数据+前24小时的数据==前48小时的数据";

        logger.info("\n\n" + caseName + "\n");

        try {

            String faceUrl = defence.liaoFaceUrlNew;
            String customerId = "";
            String deviceId = "";
            String namePhone = "";
            int page = 1;
            int size = 10;
            long startTime = System.currentTimeMillis() - 2 * 24 * 60 * 60 * 1000;
            long endTime = System.currentTimeMillis() - 24 * 60 * 60 * 1000;

            String[] similaritys = {"HIGH", "LOW", ""};

            for (int i = 0; i < similaritys.length; i++) {

                long now = System.currentTimeMillis();

                int total1 = defence.customerHistoryCapturePage(faceUrl, customerId, deviceId, namePhone, similaritys[i], startTime, endTime, page, size).
                        getJSONObject("data").getInteger("total");

//            昨天的数据
                long startTime1 = now - 24 * 60 * 60 * 1000;
                long endTime1 = now;
                int total2 = defence.customerHistoryCapturePage(faceUrl, customerId, deviceId, namePhone, similaritys[i], startTime1, endTime1, page, size).
                        getJSONObject("data").getInteger("total");

//            昨天+前天的数据
                int total3 = defence.customerHistoryCapturePage(faceUrl, customerId, deviceId, namePhone, similaritys[i], startTime, endTime1, page, size).
                        getJSONObject("data").getInteger("total");

                Preconditions.checkArgument(total1 + total2 == total3, "人脸识别记录分页查询，前48-24小时的数据条数=" + total1 +
                        "+前24-现在的数据条数=" + total2 + "！=时间选择前48h-现在的数据条数=" + total3 + "，similarity=" + similaritys[i]);

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
    public void customerFaceTraceListSimilarity() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "轨迹查询(人脸搜索)-相似度高+相似度低==不选择相似度";

        logger.info("\n\n" + caseName + "\n");

        try {

            String picUrl = defence.liaoFaceUrlNew;
            long startTime = System.currentTimeMillis() - 24 * 60 * 60 * 1000;
            long endTime = System.currentTimeMillis();

//            轨迹查询(人脸搜索)
            String similarity = "HIGH";
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

        String caseDesc = "轨迹查询(人脸搜索)-前（48-24）小时的数据+前24小时的数据==前48小时的数据";

        logger.info("\n\n" + caseName + "\n");

        try {

            String picUrl = defence.liaoFaceUrlNew;
//            前天的数据
            long now = System.currentTimeMillis();

            long startTime = now - 48 * 60 * 60 * 1000;
            long endTime = now - 24 * 60 * 60 * 1000;

            String[] similaritys = {"HIGH", "LOW", ""};

            for (int i = 0; i < similaritys.length; i++) {

                int total1 = defence.customerFaceTraceList(picUrl, startTime, endTime, similaritys[i], 1, 100).getJSONObject("data").getInteger("total");

//            昨天的数据
                long startTime1 = now - 24 * 60 * 60 * 1000;
                long endTime1 = now;
                int total2 = defence.customerFaceTraceList(picUrl, startTime1, endTime1, similaritys[i], 1, 100).getJSONObject("data").getInteger("total");

//            昨天+前天的数据
                int total3 = defence.customerFaceTraceList(picUrl, startTime, endTime1, similaritys[i], 1, 100).getJSONObject("data").getInteger("total");

                Preconditions.checkArgument(total1 + total2 == total3, "轨迹查询（人脸搜索），前48-24小时的数据条数=" + total1 +
                        "+前24-现在的数据条数=" + total2 + "！=时间选择前48h-现在的数据条数=" + total3 + "， similarity=" + similaritys[i]);

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
    public void customerSearchListTime() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "结构化检索-前（48-24）小时的数据+前24小时的数据==前48小时的数据";

        logger.info("\n\n" + caseName + "\n");

        try {


            String deviceId = "";
            String sex = "MALE";//MALE/FEMALE
            String age = "";
            String hair = "";
            String clothes = "";
            String clothesColour = "";
            String trousers = "";
            String trousersColour = "";
            String hat = "";
            String knapsack = "";

//            前天的数据
            long now = System.currentTimeMillis();

            long startTime = now - 48 * 60 * 60 * 1000;
            long endTime = now - 24 * 60 * 60 * 1000;

            int total1 = defence.customerSearchList(deviceId, startTime, endTime,
                    sex, age, hair, clothes, clothesColour, trousers, trousersColour, hat, knapsack, 1, 100).getJSONObject("data").getInteger("total");

//            昨天的数据
            long startTime1 = now - 24 * 60 * 60 * 1000;
            long endTime1 = now;
            int total2 = defence.customerSearchList(deviceId, startTime1, endTime1,
                    sex, age, hair, clothes, clothesColour, trousers, trousersColour, hat, knapsack, 1, 100).getJSONObject("data").getInteger("total");

//            昨天+前天的数据
            int total3 = defence.customerSearchList(deviceId, startTime, endTime1,
                    sex, age, hair, clothes, clothesColour, trousers, trousersColour, hat, knapsack, 1, 100).getJSONObject("data").getInteger("total");

            Preconditions.checkArgument(total1 + total2 == total3, "结构化检索，前48-24小时的数据条数=" + total1 +
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

    @Test(dataProvider = "AGE_GRP_NAME")
    public void customerSearchListAgeGrpEqualsAges(String ageStr,String agegrp,String grpName) {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "结构化检索-age=某一个年龄组的所有年龄==查询条件=该ageGroup的结果条数";

        logger.info("\n\n" + caseName + "\n");

        try {

            String[] ages = ageStr.split(",");

            int ageTotal = 0;
            int grpTotal = 0;

            for (int i = 0; i < ages.length; i++) {
                ageTotal += defence.customerSearchList(ages[i], "", 1, 1).getJSONObject("data").getInteger("total");
            }

            grpTotal = defence.customerSearchList("", agegrp, 1, 1).getJSONObject("data").getInteger("total");

            Preconditions.checkArgument(ageTotal == grpTotal, "结构化检索，选择年龄的总数=" + ageTotal +
                    "!=选择年龄组的总数=" + grpTotal);

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

    @Test(dataProvider = "AGE_GRP_NAME")
    public void customerSearchListAgeGrpEqualsAges1(String ageStr,String agegrp,String grpName) {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "结构化检索-age=某一个年龄组的所有年龄==查询条件=该ageGroup的结果条数";

        logger.info("\n\n" + caseName + "\n");

        try {

            String[] ages = ageStr.split(",");

            int ageTotal = 0;
            int grpTotal = 0;

            for (int i = 0; i < ages.length; i++) {
                ageTotal += defence.customerSearchList(ages[i], agegrp, 1, 1).getJSONObject("data").getInteger("total");
            }

            grpTotal = defence.customerSearchList("", agegrp, 1, 1).getJSONObject("data").getInteger("total");

            Preconditions.checkArgument(ageTotal == grpTotal, "结构化检索，选择年龄的总数=" + ageTotal +
                    "!=选择年龄组的总数=" + grpTotal);

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

    @DataProvider(name = "AGE_GRP_NAME")
    public Object[][] ageGrpName() {

        return new Object[][]{

                new Object[]{

                        "0,1,2,3,4,5,6,7,8,9,10,11,12", "CHILD","[0,12]"
                },

                new Object[]{

                        "13,14,15,16,17,18", "TEENAGER","[13,18]"
                },

                new Object[]{

                        "19,20,21,22,23,24,25,26,27,28,29", "YOUNG","[19,29]"
                },

                new Object[]{

                        "30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45", "PUBER","[30,45]"
                },

                new Object[]{

                        "46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65", "MIDDLE_AGED","[46,65]"
                },

                new Object[]{

                        "66,67,68,69,70,71,72,73,74,75,76,77,78,79,80", "OLD","[66,200]"
                }
        };
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

