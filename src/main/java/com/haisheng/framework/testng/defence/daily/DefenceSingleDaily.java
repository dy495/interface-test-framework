package com.haisheng.framework.testng.defence.daily;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.model.bean.Case;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.*;

public class DefenceSingleDaily {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    //    工具类变量
    Defence defence = new Defence();

    //    入库相关变量
    private String failReason = "";
    private Case aCase = new Case();

    //    case相关变量
//    -----------------------------------------------------非创单验证（其他逻辑）-------------------------------------

    @Test
    public void customerReg() throws Exception {
        String faceUrl = defence.tingtingFaceUrlNew;
        String userId = defence.genRandom();
        defence.customerReg(faceUrl, userId);
    }

    @Test
    public void cusotmerDelete() throws Exception {
        String userId = "5ffef83e-f837-4aee-9124-611b254eec76";
        defence.customerDelete(userId);
    }

    @Test
    public void blackDelete() throws Exception {

//        String blackId = "e22e9777-104a-4323-aebc-99a5e8d11783";
//        String blackId = "7afae737-6467-4adf-b1e2-2f46bfbccb98";
//        String blackId = "05d10e2c-cd05-4343-b333-764b5021fbfc";
        String blackId = "3a281254-918f-4af6-900e-1f406b2cae69";

        defence.customerDeleteBlack(blackId);
    }

    @Test
    public void blackRegUserId() throws Exception {

        String blackId = "";
        String level = "level";
        String label = "label";

        defence.customerRegBlackUserId(blackId, label, level);
    }

    @Test
    public void blackRegNewUser() throws Exception {
        String faceUrl = defence.wanghuanFaceUrlNew;
        String level = "level";
        String label = "label";

        JSONObject data = defence.customerRegBlackNewUser(faceUrl, label, level).getJSONObject("data");
        String alarmCustomerId = data.getString("alarm_customer_id");

    }

    @Test
    public void boundaryAlarmList() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "设置周界报警-获取";

        logger.info("\n\n" + caseName + "\n");

        try {

            String deviceId = defence.deviceXieduimen;

//            周界列表
            JSONArray axis = defence.boundaryAlarmInfo(deviceId).getJSONObject("data").getJSONArray("boundary_axis");

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
//            defence.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    @Test
    public void boundaryAlarmAdd() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;


        logger.info("\n\n" + caseName + "\n");

        try {

//            注册周界
            defence.boundaryAlarmAdd(defence.device1Caiwu);
            defence.boundaryAlarmAdd(defence.device1Huiyi);
            defence.boundaryAlarmAdd(defence.deviceYilaoshi);
            defence.boundaryAlarmAdd(defence.deviceXieduimen);
            defence.boundaryAlarmAdd(defence.deviceChukou);
            defence.boundaryAlarmAdd(defence.deviceDongbeijiao);

//            周界列表
//            JSONArray axis = defence.boundaryAlarmInfo(deviceId).getJSONObject("data").getJSONArray("boundary_axis");

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
//            defence.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    @Test
    public void boundaryAlarmDelete() throws Exception {

//        defence.boundaryAlarmDelete(defence.device1Caiwu);
//        defence.boundaryAlarmDelete(defence.device1Huiyi);
//        defence.boundaryAlarmDelete(defence.deviceYilaoshi);
//        defence.boundaryAlarmDelete(defence.deviceXieduimen);
//        defence.boundaryAlarmDelete(defence.deviceChukou);
        defence.boundaryAlarmDelete(defence.deviceDongbeijiao);
    }

    @Test
    public void alarmLogPageOperate() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "告警记录(分页查询)-告警记录处理，验证code==1000";

        logger.info("\n\n" + caseName + "\n");

        try {

            String deviceId = defence.deviceDongbeijiao;
            String operator = "sophie";
            String optResult = "有不明人员进入与周界，目前没有确定是具体的那个人，继续观察";

//            告警记录(分页查询)
            JSONObject data = defence.alarmLogPage(deviceId, 1, 1).getJSONObject("data");
            String alarmId = data.getString("id");

//            告警记录处理
            defence.alarmLogOperate(alarmId, operator, optResult);

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
//            defence.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    @Test
    public void deivceCustomerNumAlarmAdd() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "设备画面人数告警设置，验证code==1000";

        logger.info("\n\n" + caseName + "\n");

        try {

            int threshold = 1;

//            设备画面人数告警设置
            defence.deviceCustomerNumAlarmAdd(defence.device1Caiwu, threshold);
            defence.deviceCustomerNumAlarmAdd(defence.device1Huiyi, threshold);
            defence.deviceCustomerNumAlarmAdd(defence.deviceYilaoshi, threshold);
            defence.deviceCustomerNumAlarmAdd(defence.deviceXieduimen, threshold);
            defence.deviceCustomerNumAlarmAdd(defence.deviceChukou, threshold);
            defence.deviceCustomerNumAlarmAdd(defence.deviceDongbeijiao, threshold);

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
//            defence.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    @Test
    public void deivceCustomerNumAlarmDelete() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "设备画面人数告警设置，验证code==1000";

        logger.info("\n\n" + caseName + "\n");

        try {

            defence.deviceCustomerNumAlarmDelete(defence.device1Caiwu);
            defence.deviceCustomerNumAlarmDelete(defence.device1Huiyi);
            defence.deviceCustomerNumAlarmDelete(defence.deviceYilaoshi);
            defence.deviceCustomerNumAlarmDelete(defence.deviceXieduimen);
            defence.deviceCustomerNumAlarmDelete(defence.deviceChukou);
            defence.deviceCustomerNumAlarmDelete(defence.deviceDongbeijiao);

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
//            defence.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    @Test
    public void customerInfo() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "人物详情信息，验证code==1000";

        logger.info("\n\n" + caseName + "\n");

        try {

            String faceUrl = defence.yuFaceUrl;
            String userId = defence.genRandom();

//            社区人员注册
            String customerId = defence.customerReg(faceUrl, userId).getJSONObject("data").getString("customer_id");

//            人物详情信息
            JSONObject data = defence.customerInfo(userId, customerId).getJSONObject("data").getJSONObject("info");

//            删除社区人员
            defence.customerDelete(userId);

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
//            defence.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    @Test
    public void customerSearchListTest() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "结构化检索(分页查询)";

        logger.info("\n\n" + caseName + "\n");

        try {

            long startTime = System.currentTimeMillis() - 24 * 60 * 60 * 1000;
            long endTime = System.currentTimeMillis();

            String deviceId = defence.device1Caiwu;
//            String deviceId = defence.device1Huiyi;
//            String deviceId = defence.deviceYilaoshi;
//            String deviceId = defence.deviceXieduimen;
//            String deviceId = defence.deviceChukou;
//            String deviceId = defence.deviceDongbeijiao;

            String sex = "FEMALE";
            String age = "";
            String hair = "LONG";
            String clothes = "LONG_SLEEVES";
            String clothesColour = "YELLOW";
            String trousers = "";
            String trousersColour = "";
            String hat = "";
            String knapsack = "";
            String similarity = "LOW";


//            结构化检索(分页查询)
            defence.customerSearchList(deviceId, startTime, endTime,
                    sex, age, hair, clothes, clothesColour, trousers, trousersColour, hat, knapsack, similarity);

            defence.customerSearchList(deviceId, startTime, endTime);


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
    public void customerSearchListHat() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "结构化检索(分页查询)，验证code==1000";

        logger.info("\n\n" + caseName + "\n");

        try {

            String deviceId = defence.deviceXieduimen;
            long startTime = System.currentTimeMillis() - 24 * 60 * 60 * 1000;
            long endTime = System.currentTimeMillis();

//            结构化检索(分页查询)
            JSONObject data = defence.customerSearchList(deviceId, startTime, endTime).getJSONObject("data");

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
    public void customerHistoryCapturePageTestVilllage() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "人脸识别记录分页查询";

        logger.info("\n\n" + caseName + "\n");

        try {

//            String faceUrl = "";
//            String faceUrl = defence.celianFaceUrlNew;
//            String faceUrl = defence.roll90FaceUrlNew;
//            String faceUrl = defence.roll180FaceUrlNew;
//            String faceUrl = defence.roll270FaceUrlNew;
//            String faceUrl = defence.fengjing1FaceUrlNew;
//            String faceUrl = defence.mao1FaceUrlNew;
//            String faceUrl = defence.multiFaceUrlNew;
//            String faceUrl = defence.beiyingFaceUrlNew;
//            String faceUrl = defence.liaoFaceUrlNew;
//            String faceUrl = defence.zhidongFaceUrl;
            String faceUrl = defence.tingtingFaceUrlNew;
            String customerId = "";
//            String namePhone = "17775184194";
            String namePhone = "";
            String similarity = "";
            String deviceId = "";

//            long startTime = System.currentTimeMillis() - 24 * 60 * 60 * 1000;
//            long endTime = System.currentTimeMillis();

            long startTime = 0;
            long endTime = 0;

//            人脸识别记录分页查询
            defence.customerHistoryCapturePage(faceUrl, customerId, deviceId, namePhone, similarity, startTime, endTime, 1, 10).getJSONObject("data");

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
    public void customerHistoryCapturePageCustomerId() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "人脸识别记录分页查询";

        logger.info("\n\n" + caseName + "\n");

        try {

            String faceUrl = defence.liaoFaceUrlNew;
//            String customerId = "e49e8685-d7e3-4a84-89ea-f11072484e83";
            String customerId = "";
            String namePhone = "";
            String similarity = "";
            String device_id = "";

            long startTime = System.currentTimeMillis() - 60 * 60 * 1000;
            long endTime = System.currentTimeMillis();

//            人脸识别记录分页查询
            defence.customerHistoryCapturePage(faceUrl, customerId, device_id, namePhone, similarity, startTime, endTime, 1, 100).getJSONObject("data");

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
    public void customerHistoryCapturePageTestNamePhone() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "人脸识别记录分页查询";

        logger.info("\n\n" + caseName + "\n");

        try {

            String faceUrl = defence.liaoFaceUrlNew;
            String customerId = "";
            String namePhone = "";
//            String namePhone = "17747246350";
            String similarity = "";
            String device_id = "";

            long startTime = System.currentTimeMillis() - 24 * 60 * 60 * 1000;
            long endTime = System.currentTimeMillis();

//            人脸识别记录分页查询
            defence.customerHistoryCapturePage(faceUrl, customerId, device_id, namePhone, similarity,
                    startTime, endTime, 1, 100).getJSONObject("data");
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
    public void customerFaceTraceListTest() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "轨迹查询(人脸搜索)，验证code==1000";

        logger.info("\n\n" + caseName + "\n");

        try {

//            String picUrl = "";
//            String picUrl = defence.liaoFaceUrlNew;
            String picUrl = defence.xuyanFaceUrlNew;
            String similarity = "HIGH";
            long startTime = System.currentTimeMillis() - 48 * 60 * 60 * 1000;
            long endTime = System.currentTimeMillis() - 24 * 60 * 60 * 1000;
//
//            long startTime = System.currentTimeMillis() - 24 * 60 * 60 * 1000;
//            long endTime = System.currentTimeMillis();

//            轨迹查询(人脸搜索)
            JSONObject data = defence.customerFaceTraceList(picUrl, startTime, endTime, similarity, 1, 100).getJSONObject("data");

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

    @AfterClass
    public void clean() {
        defence.clean();
    }

    @BeforeClass
    public void initial() {
        defence.initial();
    }

    @BeforeMethod
    public void initialVars() {
        failReason = "";
        aCase = new Case();
    }
}

