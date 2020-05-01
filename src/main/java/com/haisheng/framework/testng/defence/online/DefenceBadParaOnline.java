package com.haisheng.framework.testng.defence.online;

import ai.winsense.model.ApiResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.model.bean.Case;
import com.haisheng.framework.testng.defence.online.DefenceOnline;
import com.haisheng.framework.util.CheckUtil;
import com.haisheng.framework.util.DateTimeUtil;
import com.haisheng.framework.util.StatusCode;
import com.haisheng.framework.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class DefenceBadParaOnline {

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
            String faceUrl1 = defenceOnline.kangLinFaceUrlNew;
            String userId1 = defenceOnline.genRandom();
            String name1 = "name1";
            String phone1 = "17610248107";
            String type1 = "RESIDENT";
            String cardKey1 = defenceOnline.genRandom();
            String age1 = "20";
            String sex1 = "MALE";
            String address1 = "address";
            String birthday1 = "birthday1";

            defenceOnline.customerReg(faceUrl1, userId1, name1, phone1, type1, cardKey1,
                    age1, sex1, address1, birthday1);

//            删除注册(不存在的Village)
            defenceOnline.customerDelete(-1, userId1, StatusCode.BAD_REQUEST);

//            再次删除
            defenceOnline.customerDelete(userId1);

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
    public void customerDeleteNonExistUserId() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "注册-删除（不存在的UserId）-删除";

        logger.info("\n\n" + caseName + "\n");

        try {

//            注册
            String faceUrl1 = defenceOnline.kangLinFaceUrlNew;
            String userId1 = defenceOnline.genRandom();
            String name1 = "name1";
            String phone1 = "17610248107";
            String type1 = "RESIDENT";
            String cardKey1 = defenceOnline.genRandom();
            String age1 = "20";
            String sex1 = "MALE";
            String address1 = "address";
            String birthday1 = "birthday1";

            defenceOnline.customerReg(faceUrl1, userId1, name1, phone1, type1, cardKey1,
                    age1, sex1, address1, birthday1);

//            删除注册(不存在的Village)
            defenceOnline.customerDelete(8, userId1 + "nonexist", StatusCode.BAD_REQUEST);

//            再次删除
            defenceOnline.customerDelete(userId1);

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
    public void blackDeleteNonExistUserId() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "注册-black删除（不存在的alarm_customer_id）-删除";

        logger.info("\n\n" + caseName + "\n");

        try {

//            注册
            String faceUrl1 = defenceOnline.kangLinFaceUrlNew;
            String userId1 = defenceOnline.genRandom();
            String name1 = "name1";
            String phone1 = "17610248107";
            String type1 = "RESIDENT";
            String cardKey1 = defenceOnline.genRandom();
            String age1 = "20";
            String sex1 = "MALE";
            String address1 = "address";
            String birthday1 = "birthday1";

            defenceOnline.customerReg(faceUrl1, userId1, name1, phone1, type1, cardKey1,
                    age1, sex1, address1, birthday1);

//            删除注册(不存在的Village)
            defenceOnline.customerDelete(8, userId1 + "nonexist", StatusCode.BAD_REQUEST);

//            再次删除
            defenceOnline.customerDelete(userId1);

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
    public void alarmLogPageOperate1025() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "告警记录(分页查询)-告警记录处理(optResult有1025个字符)";

        logger.info("\n\n" + caseName + "\n");

        try {

            String deviceId = "";
            String operator = "sophie";
            String optResult = defenceOnline.genCharLen(1025);

//            告警记录(分页查询)
            String alarmId = "";
            JSONArray list = defenceOnline.alarmLogPage(deviceId, 1, 10).getJSONObject("data").getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                JSONObject single = list.getJSONObject(i);
                String optStatus = single.getString("opt_status");
                if ("未处理".equals(optStatus)) {
                    alarmId = single.getString("id");

//                    告警记录处理
                    ApiResponse apiResponse = defenceOnline.alarmLogOperate(alarmId, operator, optResult, StatusCode.BAD_REQUEST);

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
            defenceOnline.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
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

            String deviceId = defenceOnline.deviceIdJinmen;

//            设置周界
            defenceOnline.boundaryAlarmAdd(deviceId, 1, StatusCode.BAD_REQUEST);
            defenceOnline.boundaryAlarmAdd(deviceId, 2, StatusCode.BAD_REQUEST);
            defenceOnline.boundaryAlarmAdd(deviceId, 10, StatusCode.SUCCESS);

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
    public void boundaryAlarmYZ() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "设置周界报警";

        logger.info("\n\n" + caseName + "\n");

        try {

            String deviceId = defenceOnline.deviceIdJinmen;

//            设置周界
            ApiResponse res = defenceOnline.boundaryAlarmAdd(deviceId, StatusCode.BAD_REQUEST);

            defenceOnline.checkMessage("设置周界告警-", res, "设置周界时，点的坐标填y，z---");

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
    public void blackRegBadFaceUrl() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "黑名单注册-奇怪的图片";

        logger.info("\n\n" + caseName + "\n");

        try {

            String[] faces = {defenceOnline.fengjing1FaceUrlNew, defenceOnline.fengjingFaceUrlNew, defenceOnline.cheliangFaceUrlNew, defenceOnline.cheliang1FaceUrlNew,
                    defenceOnline.beiyingFaceUrlNew, defenceOnline.maoFaceUrlNew, defenceOnline.mao1FaceUrlNew, defenceOnline.roll90FaceUrlNew, defenceOnline.roll180FaceUrlNew,
                    defenceOnline.roll270FaceUrlNew, defenceOnline.multiFaceUrlNew, defenceOnline.celianFaceUrlNew};

            for (int i = 0; i < faces.length; i++) {
                ApiResponse res = defenceOnline.customerRegBlackNewUser(faces[i], StatusCode.BAD_REQUEST);
                String message = "人脸图片不符合要求";

                defenceOnline.checkMessage("社区人员注册-奇怪的图片", res, message, false);
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
    public void customerRegBadFaceUrl() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "社区人员注册-奇怪的图片";

        logger.info("\n\n" + caseName + "\n");

        try {

            String[] faces = {defenceOnline.fengjing1FaceUrlNew, defenceOnline.fengjingFaceUrlNew, defenceOnline.cheliangFaceUrlNew, defenceOnline.cheliang1FaceUrlNew,
                    defenceOnline.beiyingFaceUrlNew, defenceOnline.maoFaceUrlNew, defenceOnline.mao1FaceUrlNew, defenceOnline.roll90FaceUrlNew, defenceOnline.roll180FaceUrlNew,
                    defenceOnline.roll270FaceUrlNew, defenceOnline.multiFaceUrlNew, defenceOnline.celianFaceUrlNew};

            for (int i = 0; i < faces.length; i++) {
                ApiResponse res = defenceOnline.customerReg(faces[i], defenceOnline.genRandom(), StatusCode.BAD_REQUEST);
                String message = "人脸图片不符合要求";

                defenceOnline.checkMessage("社区人员注册-奇怪的图片", res, message, false);
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
    public void customerFaceTraceFaceUrl() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "轨迹查询（人脸搜索）-奇怪的图片";

        logger.info("\n\n" + caseName + "\n");

        try {

//            defenceOnline.celianFaceUrlNew
//            侧脸图片可以查出来

            String[] faces = {defenceOnline.fengjing1FaceUrlNew, defenceOnline.fengjingFaceUrlNew, defenceOnline.cheliangFaceUrlNew, defenceOnline.cheliang1FaceUrlNew,
                    defenceOnline.beiyingFaceUrlNew, defenceOnline.maoFaceUrlNew, defenceOnline.mao1FaceUrlNew, defenceOnline.roll90FaceUrlNew, defenceOnline.roll180FaceUrlNew,
                    defenceOnline.roll270FaceUrlNew};

            String similarity = "HIGH";
            long startTime = System.currentTimeMillis() - 48 * 60 * 60 * 1000;
            long endTime = System.currentTimeMillis() - 24 * 60 * 60 * 1000;

            for (int i = 0; i < faces.length; i++) {

//            轨迹查询(人脸搜索)
                ApiResponse res = defenceOnline.customerFaceTraceList(faces[i], startTime, endTime, similarity, StatusCode.BAD_REQUEST);

                String message = "人脸图片不符合要求";

                defenceOnline.checkMessage("轨迹查询(人脸搜索)--奇怪的图片", res, message, false);
            }

            ApiResponse res = defenceOnline.customerFaceTraceList(defenceOnline.multiFaceUrlNew, startTime, endTime, similarity, StatusCode.BAD_REQUEST);

            String message = "请勿上传包含多张人脸图片";

            defenceOnline.checkMessage("轨迹查询(人脸搜索)--奇怪的图片", res, message, false);

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
    public void customerHistoryCapturePageBadFaceUrl() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "人脸识别记录分页查询-奇怪的图片";

        logger.info("\n\n" + caseName + "\n");

        try {
//            defenceOnline.celianFaceUrlNew
//            侧脸图片可以查出来

            String[] faces = {defenceOnline.fengjing1FaceUrlNew, defenceOnline.fengjingFaceUrlNew, defenceOnline.cheliangFaceUrlNew, defenceOnline.cheliang1FaceUrlNew,
                    defenceOnline.beiyingFaceUrlNew, defenceOnline.maoFaceUrlNew, defenceOnline.mao1FaceUrlNew, defenceOnline.roll90FaceUrlNew, defenceOnline.roll180FaceUrlNew,
                    defenceOnline.roll270FaceUrlNew};

            for (int i = 0; i < faces.length; i++) {

//            轨迹查询(人脸搜索)
                ApiResponse res = defenceOnline.customerHistoryCapturePage(faces[i], StatusCode.BAD_REQUEST);

                String message = "人脸图片不符合要求";

                defenceOnline.checkMessage("人脸识别记录分页查询-奇怪的图片", res, message, false);
            }

            ApiResponse res = defenceOnline.customerHistoryCapturePage(defenceOnline.multiFaceUrlNew, StatusCode.BAD_REQUEST);

            String message = "请勿上传包含多张人脸图片";

            defenceOnline.checkMessage("人脸识别记录分页查询-奇怪的图片", res, message, false);

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

    @AfterClass
    public void clean() {
        defenceOnline.clean();
    }

    @BeforeClass
    public void initial() {
        defenceOnline.initial();
    }

    @BeforeMethod
    public void initialVars() {
        failReason = "";
        aCase = new Case();
    }
}



