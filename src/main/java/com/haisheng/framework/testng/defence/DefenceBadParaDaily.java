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

public class DefenceBadParaDaily {

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

    private String boundaryDeviceId = "153";
    private String blackDeviced = "150";
    private String NumDeviced = "155";

    public final long VILLAGE_ID = 8;

//    ------------------------------------------------------非创单验证（其他逻辑）-------------------------------------


    @Test
    public void customerDeleteNonExistVillage() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "注册-删除（不存在的Village）-删除";

        logger.info("\n\n" + caseName + "\n");

        try {

//            注册
            String faceUrl1 = defence.kangLinFaceUrlNew;
            String userId1 = defence.genRandom();
            String name1 = "name1";
            String phone1 = "17610248107";
            String type1 = "RESIDENT";
            String cardKey1 = defence.genRandom();
            String age1 = "20";
            String sex1 = "MALE";
            String address1 = "address";
            String birthday1 = "birthday1";

            defence.customerReg(faceUrl1, userId1, name1, phone1, type1, cardKey1,
                    age1, sex1, address1, birthday1);

//            删除注册(不存在的Village)
            defence.customerDelete(-1, userId1, StatusCode.BAD_REQUEST);

//            再次删除
            defence.customerDelete(userId1);

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
    public void customerDeleteNonExistUserId() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "注册-删除（不存在的UserId）-删除";

        logger.info("\n\n" + caseName + "\n");

        try {

//            注册
            String faceUrl1 = defence.kangLinFaceUrlNew;
            String userId1 = defence.genRandom();
            String name1 = "name1";
            String phone1 = "17610248107";
            String type1 = "RESIDENT";
            String cardKey1 = defence.genRandom();
            String age1 = "20";
            String sex1 = "MALE";
            String address1 = "address";
            String birthday1 = "birthday1";

            defence.customerReg(faceUrl1, userId1, name1, phone1, type1, cardKey1,
                    age1, sex1, address1, birthday1);

//            删除注册(不存在的Village)
            defence.customerDelete(8, userId1 + "nonexist", StatusCode.BAD_REQUEST);

//            再次删除
            defence.customerDelete(userId1);

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
    public void blackDeleteNonExistUserId() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "注册-black删除（不存在的alarm_customer_id）-删除";

        logger.info("\n\n" + caseName + "\n");

        try {

//            注册
            String faceUrl1 = defence.kangLinFaceUrlNew;
            String userId1 = defence.genRandom();
            String name1 = "name1";
            String phone1 = "17610248107";
            String type1 = "RESIDENT";
            String cardKey1 = defence.genRandom();
            String age1 = "20";
            String sex1 = "MALE";
            String address1 = "address";
            String birthday1 = "birthday1";

            defence.customerReg(faceUrl1, userId1, name1, phone1, type1, cardKey1,
                    age1, sex1, address1, birthday1);

//            删除注册(不存在的Village)
            defence.customerDelete(8, userId1 + "nonexist", StatusCode.BAD_REQUEST);

//            再次删除
            defence.customerDelete(userId1);

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
    public void alarmLogPageOperate1025() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "告警记录(分页查询)-告警记录处理(optResult有1025个字符)";

        logger.info("\n\n" + caseName + "\n");

        try {

            String deviceId = "";
            String operator = "sophie";
            String optResult = defence.genCharLen(1025);

//            告警记录(分页查询)
            String alarmId = "";
            JSONArray list = defence.alarmLogPage(deviceId, 1, 10).getJSONObject("data").getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                JSONObject single = list.getJSONObject(i);
                String optStatus = single.getString("opt_status");
                if ("未处理".equals(optStatus)) {
                    alarmId = single.getString("id");

//                    告警记录处理
                    ApiResponse apiResponse = defence.alarmLogOperate(alarmId, operator, optResult, StatusCode.BAD_REQUEST);

                    String message = JSON.parseObject(JSON.toJSONString(apiResponse)).getString("message");

                    String expectMessage = "[opt_result] length should less than  equal to 1024 varchar";
                    Preconditions.checkArgument(expectMessage.equals(message),
                            "opt_result有1025个字符时，提示信息=" + message + "，期待=" + expectMessage);

                    break;
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
    public void boundaryAlarmLT3Point() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "设置周界报警";

        logger.info("\n\n" + caseName + "\n");

        try {

            String deviceId = defence.deviceDongbeijiao;

//            设置周界
            defence.boundaryAlarmAdd(deviceId, 1, StatusCode.BAD_REQUEST);
            defence.boundaryAlarmAdd(deviceId, 2, StatusCode.BAD_REQUEST);
            defence.boundaryAlarmAdd(deviceId, 10, StatusCode.SUCCESS);

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
    public void boundaryAlarmYZ() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "设置周界报警";

        logger.info("\n\n" + caseName + "\n");

        try {

            String deviceId = defence.deviceDongbeijiao;

//            设置周界
            ApiResponse res = defence.boundaryAlarmAdd(deviceId, StatusCode.BAD_REQUEST);

            defence.checkMessage("设置周界告警-", res, "设置周界时，点的坐标填y，z---");

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
    public void blackRegBadFaceUrl() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "黑名单注册-奇怪的图片";

        logger.info("\n\n" + caseName + "\n");

        try {

            String[] faces = {defence.fengjing1FaceUrlNew, defence.fengjingFaceUrlNew, defence.cheliangFaceUrlNew, defence.cheliang1FaceUrlNew,
                    defence.beiyingFaceUrlNew, defence.maoFaceUrlNew, defence.mao1FaceUrlNew, defence.roll90FaceUrlNew, defence.roll180FaceUrlNew,
                    defence.roll270FaceUrlNew, defence.multiFaceUrlNew, defence.celianFaceUrlNew};

            for (int i = 0; i < faces.length; i++) {
                ApiResponse res = defence.customerRegBlackNewUser(faces[i], StatusCode.BAD_REQUEST);
                String message = "人脸图片不符合要求";

                defence.checkMessage("社区人员注册-奇怪的图片", res, message, false);
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
    public void customerRegBadFaceUrl() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "社区人员注册-奇怪的图片";

        logger.info("\n\n" + caseName + "\n");

        try {

            String[] faces = {defence.fengjing1FaceUrlNew, defence.fengjingFaceUrlNew, defence.cheliangFaceUrlNew, defence.cheliang1FaceUrlNew,
                    defence.beiyingFaceUrlNew, defence.maoFaceUrlNew, defence.mao1FaceUrlNew, defence.roll90FaceUrlNew, defence.roll180FaceUrlNew,
                    defence.roll270FaceUrlNew, defence.multiFaceUrlNew, defence.celianFaceUrlNew};

            for (int i = 0; i < faces.length; i++) {
                ApiResponse res = defence.customerReg(faces[i], defence.genRandom(), StatusCode.BAD_REQUEST);
                String message = "人脸图片不符合要求";

                defence.checkMessage("社区人员注册-奇怪的图片", res, message, false);
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
    public void customerFaceTraceFaceUrl() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "轨迹查询（人脸搜索）-奇怪的图片";

        logger.info("\n\n" + caseName + "\n");

        try {

//            defence.celianFaceUrlNew
//            侧脸图片可以查出来

            String[] faces = {defence.fengjing1FaceUrlNew, defence.fengjingFaceUrlNew, defence.cheliangFaceUrlNew, defence.cheliang1FaceUrlNew,
                    defence.beiyingFaceUrlNew, defence.maoFaceUrlNew, defence.mao1FaceUrlNew, defence.roll90FaceUrlNew, defence.roll180FaceUrlNew,
                    defence.roll270FaceUrlNew};

            String similarity = "HIGH";
            long startTime = System.currentTimeMillis() - 48 * 60 * 60 * 1000;
            long endTime = System.currentTimeMillis() - 24 * 60 * 60 * 1000;

            for (int i = 0; i < faces.length; i++) {

//            轨迹查询(人脸搜索)
                ApiResponse res = defence.customerFaceTraceList(faces[i], startTime, endTime, similarity, StatusCode.BAD_REQUEST);

                String message = "人脸图片不符合要求";

                defence.checkMessage("轨迹查询(人脸搜索)--奇怪的图片", res, message, false);
            }

            ApiResponse res = defence.customerFaceTraceList(defence.multiFaceUrlNew, startTime, endTime, similarity, StatusCode.BAD_REQUEST);

            String message = "请勿上传包含多张人脸图片";

            defence.checkMessage("轨迹查询(人脸搜索)--奇怪的图片", res, message, false);

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
    public void customerHistoryCapturePageBadFaceUrl() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "人脸识别记录分页查询-奇怪的图片";

        logger.info("\n\n" + caseName + "\n");

        try {
//            defence.celianFaceUrlNew
//            侧脸图片可以查出来

            String[] faces = {defence.fengjing1FaceUrlNew, defence.fengjingFaceUrlNew, defence.cheliangFaceUrlNew, defence.cheliang1FaceUrlNew,
                    defence.beiyingFaceUrlNew, defence.maoFaceUrlNew, defence.mao1FaceUrlNew, defence.roll90FaceUrlNew, defence.roll180FaceUrlNew,
                    defence.roll270FaceUrlNew};

            for (int i = 0; i < faces.length; i++) {

//            轨迹查询(人脸搜索)
                ApiResponse res = defence.customerHistoryCapturePage(faces[i], StatusCode.BAD_REQUEST);

                String message = "人脸图片不符合要求";

                defence.checkMessage("人脸识别记录分页查询-奇怪的图片", res, message, false);
            }

            ApiResponse res = defence.customerHistoryCapturePage(defence.multiFaceUrlNew, StatusCode.BAD_REQUEST);

            String message = "请勿上传包含多张人脸图片";

            defence.checkMessage("人脸识别记录分页查询-奇怪的图片", res, message, false);

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



