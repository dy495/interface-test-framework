package com.haisheng.framework.testng.defence.daily;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.model.bean.Case;
import com.haisheng.framework.util.CheckUtil;
import com.haisheng.framework.util.DateTimeUtil;
import com.haisheng.framework.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.*;

public class DefenceSingleDaily {

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
    private String xueqingFaceUrl = "https://retail-huabei2.oss-cn-beijing.aliyuncs.com/BUSINESS_RISK_DAILY/qa_test/AI/xueqing.jpg?OSSAccessKeyId=LTAILRdMUAwTZdPh&Expires=1903005023&Signature=Hv9x9LsKtFJCGjV6e%2F1RXfuB02s%3D";
    private String xueqingMaskFaceUrl = "https://retail-huabei2.oss-cn-beijing.aliyuncs.com/BUSINESS_RISK_DAILY/qa_test/AI/xueqingMask.jpg?OSSAccessKeyId=LTAILRdMUAwTZdPh&Expires=1903005047&Signature=oBUSxN8rLPxtcj3JDIHnHoOfmgM%3D";
    private String yuFaceUrl = "https://retail-huabei2.oss-cn-beijing.aliyuncs.com/BUSINESS_RISK_DAILY/qa_test/AI/yu_7.jpg?OSSAccessKeyId=LTAILRdMUAwTZdPh&Expires=1903005104&Signature=ASaweFXsYZsmrVRXC2MLUAwqArA%3D";

    private String boundaryDeviceId = "153";
    private String blackDeviced = "150";
    private String NumDeviced = "155";
//    ------------------------------------------------------非创单验证（其他逻辑）-------------------------------------

    @Test
    public void customerReg() throws Exception {
//        defence.customerDelete("6ef2cae9-4f97-4ec6-85ff-eff1c722d4b3");

//        String faceUrl = defence.zhidongFaceUrl;
        String faceUrl = defence.tingtingFaceUrlNew;
//        String faceUrl = defence.liaoFaceUrlNew;
//        String faceUrl = defence.yuFaceUrlNew;
//        String faceUrl = defence.xuyanFaceUrlNew;
//        String faceUrl = defence.huaFaceUrlNew;
//        String faceUrl = defence.qiaoFaceUrlNew;
//        String faceUrl = defence.wanghuanFaceUrlNew;
//        String userId = "e6596a78-c244-41cb-ad2e-79e16339fabd";
        String userId = defence.genRandom();
        defence.customerReg(faceUrl, userId);
    }

    @Test
    public void cusotmerDelete() throws Exception {
//        String userId = "0000";
//        String userId = "e49e8685-d7e3-4a84-89ea-f11072484e83";
//        String userId = "5318b438-3a08-4444ba3-9833-442bdc8daad9";
//        String userId = "de0f89d4-a8db-4a11-a08b-b30935543f0a";
//        String userId = "dcf766f6-fd19-4a72-857c-811626032248";
//        String userId = "5e5e1178-af9a-4e2d-a55f-928de414c019";
//        String userId = "d438fc3b-5473-460a-83cb-2f240097b005";
//        String userId = "7834c296-8edd-411b-9719-a4193b50ce96";
//        String userId = "1cebcf7d-7bcf-4c9e-9a4d-a6888a9e37d6";
//        String userId = "f05bc49f-1191-48f6-a120-d772155d0c2f";
//        String userId = "459a9857-1872-4984-aa2c-00739e23970c";
//        String userId = "b985ff3a-97fa-4ba1-a406-ea697c99c971";
//        String userId = "50b54fb9-d9c1-4207-8a3d-3173d8b7e460";
        String userId = "5ffef83e-f837-4aee-9124-611b254eec76";
        defence.customerDelete(userId);
    }

    @Test
    public void blackDelete() throws Exception {

//        String blackId = "9832dbf4-350d-4ac7-b8bb-650acffdb67b";
//        String blackId = "732dd77a-91b7-4717-87c1-310555c15be1";
//        String blackId = "de0f89d4-a8db-4a11-a08b-b30935543f0a";
//        String blackId = "5e5e1178-af9a-4e2d-a55f-928de414c019";
//        String blackId = "d6cf6a19-7021-4d87-99f7-8e7a4b950871";
//        String blackId = "308ed3f9-f5d7-4fe3-9dec-ba59ae7ef1f8";
//        String blackId = "9996c9a1-01da-409f-aa23-8b7f80da2bfd";
//        String blackId = "b985ff3a-97fa-4ba1-a406-ea697c99c971";
//        String blackId = "5ffef83e-f837-4aee-9124-611b254eec76";
        String blackId = "e22e9777-104a-4323-aebc-99a5e8d11783";

        defence.customerDeleteBlack(blackId);
    }

    @Test
    public void blackRegUserId() throws Exception {
//        defence.customerDelete("6ef2cae9-4f97-4ec6-85ff-eff1c722d4b3");

        String blackId = "";
        String level = "level";
        String label = "label";

        defence.customerRegBlackUserId(blackId, label, level);
    }

    @Test
    public void blackRegNewUser() throws Exception {
//        defence.customerDelete("6ef2cae9-4f97-4ec6-85ff-eff1c722d4b3");

//        String faceUrl = xueqingFaceUrl;
//        String faceUrl = "";
//        String faceUrl = defence.beiyingFaceUrlNew;
        String faceUrl = defence.wanghuanFaceUrlNew;
//        String faceUrl = hangGoodFaceUrl;
//        String faceUrl = hangFaceUrl;
//        String faceUrl = liaoFaceUrl;
//        String faceUrl = liaoFaceUrl;
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

            String deviceId = boundaryDeviceId;

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
//        defence.boundaryAlarmDelete(boundaryDeviceId);

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

//            String deviceId = defence.device1Caiwu;
//            String deviceId = defence.device1Huiyi;
//            String deviceId = defence.deviceYilaoshi;
//            String deviceId = defence.deviceXieduimen;
//            String deviceId = defence.deviceChukou;
//            String deviceId = defence.deviceDongbeijiao;

            int threshold = 1;

//            设备画面人数告警设置
            defence.deviceCustomerNumAlarmAdd(defence.device1Caiwu, threshold);
            defence.deviceCustomerNumAlarmAdd(defence.device1Huiyi, threshold);
            defence.deviceCustomerNumAlarmAdd(defence.deviceYilaoshi, threshold);
            defence.deviceCustomerNumAlarmAdd(defence.deviceXieduimen, threshold);
            defence.deviceCustomerNumAlarmAdd(defence.deviceChukou, threshold);
            defence.deviceCustomerNumAlarmAdd(defence.deviceDongbeijiao, threshold);


//            String[] devices = {defence.device1Caiwu,defence.device1Huiyi,defence.deviceYilaoshi,
//                    defence.deviceXieduimen,defence.deviceChukou,defence.deviceDongbeijiao};

//            for (int i = 0; i < devices.length; i++) {
//                defence.deviceCustomerNumAlarmAdd(deviceId, threshold);
//            }


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

            String faceUrl = yuFaceUrl;
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

            String deviceId = boundaryDeviceId;
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

