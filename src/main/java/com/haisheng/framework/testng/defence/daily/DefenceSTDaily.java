package com.haisheng.framework.testng.defence.daily;

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

public class DefenceSTDaily {

    public Logger logger = LoggerFactory.getLogger(this.getClass());

    //    工具类变量
    StringUtil stringUtil = new StringUtil();
    DateTimeUtil dt = new DateTimeUtil();
    CheckUtil checkUtil = new CheckUtil();
    Defence defence = new Defence();

    //    入库相关变量
    public String failReason = "";
    public Case aCase = new Case();

    //    case相关变量
    public final long VILLAGE_ID = 8;

//    ------------------------------------------------------社区人员验证-------------------------------------

    @Test(dataProvider = "CUSTOMER_REG")
    public void customerRegUnique(String caseNamePro, String faceUrl, String userId, String name, String phone, String type, String cardKey,
                                  String age, String sex, String address, String birthday, int expectCode) {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName + "-" + caseNamePro;

        String caseDesc = "社区人员注册--唯一性测试";

        logger.info("\n\n" + caseName + "\n");

        try {

//            注册
            String faceUrl1 = defence.nalaFaceUrlNew;
            String userId1 = ciCaseName + "-" + defence.genRandom7();
            String name1 = ciCaseName + "-" + defence.genRandom7();
            String phone1 = defence.genPhoneNum();
            String type1 = "RESIDENT";
            String cardKey1 = defence.genRandom();
            String age1 = "20";
            String sex1 = "MALE";
            String address1 = "address1";
            String birthday1 = "birthday1";

            JSONObject res = defence.customerReg(faceUrl1, userId1, name1, phone1, type1, cardKey1,
                    age1, sex1, address1, birthday1);

            Thread.sleep(1000);

            String userIdRes1 = res.getJSONObject("data").getString("user_id");

            Preconditions.checkArgument(userId1.equals(userIdRes1), "社区人员注册，注册时的user_id=" + userId1 +
                    "，返回值中的user_id=" + userIdRes1);

//            用参数注册
            if ("userId".equals(userId)) {
                userId = userId1;
            }

            if ("cardKey".equals(cardKey)) {
                cardKey = cardKey1;
            }

            if ("name".equals(name)) {
                name = name1;
            }

            if ("phone".equals(phone)) {
                phone = phone1;
            }

            ApiResponse apiResponse = defence.customerReg(faceUrl, userId, name, phone, type, cardKey,
                    age, sex, address, birthday, expectCode);

            Thread.sleep(1000);

            if (StatusCode.SUCCESS == expectCode) {

                String userIdRes = JSON.parseObject(JSON.toJSONString(apiResponse)).getJSONObject("data").getString("user_id");

                Preconditions.checkArgument(userId.equals(userIdRes), "社区人员注册，注册时的user_id=" + userId +
                        "，返回值中的user_id=" + userIdRes);
                Thread.sleep(1000);

//                删除参数注册
                defence.customerDelete(userId);
            }

//            删除注册
            Thread.sleep(1000);
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
    public void customerRegAfterDelete() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "社区人员注册-删除-再注册-再删除";

        logger.info("\n\n" + caseName + "\n");

        try {

//            注册
            String faceUrl1 = defence.kangLinFaceUrlNew;
            String userId1 = ciCaseName + "-_" + defence.genRandom7();
//            String userId1 = ciCaseName + "-" + defence.genRandom7();
            String name1 = ciCaseName + "-" + defence.genRandom7();
            String phone1 = "17610248107";
            String type1 = "RESIDENT";
            String cardKey1 = defence.genRandom();
            String age1 = "20";
            String sex1 = "MALE";
            String address1 = "address";
            String birthday1 = "birthday1";

            defence.customerReg(faceUrl1, userId1, name1, phone1, type1, cardKey1,
                    age1, sex1, address1, birthday1);

//            删除注册
            defence.customerDelete(userId1);

//            再次注册
            defence.customerReg(faceUrl1, userId1, name1, phone1, type1, cardKey1,
                    age1, sex1, address1, birthday1);

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
    public void customerRegAfterDeleteBlack() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "社区人员注册-加入黑名单-从黑名单删除-注册社区人员-删除社区人员";

        logger.info("\n\n" + caseName + "\n");

        try {

//            注册
            String faceUrl1 = defence.kangLinFaceUrlNew;
            String userId1 = ciCaseName + "-" + defence.genRandom7();
            String name1 = ciCaseName + "-" + defence.genRandom7();
            String phone1 = "17610232223";
            String type1 = "RESIDENT";
            String cardKey1 = defence.genRandom();
            String age1 = "20";
            String sex1 = "MALE";
            String address1 = "address1";
            String birthday1 = "birthday1";

            defence.customerReg(faceUrl1, userId1, name1, phone1, type1, cardKey1,
                    age1, sex1, address1, birthday1).getJSONObject("data").getString("user_id");

//            加入黑名单
            String level = "level";
            String label = "label";
            String alarmCustomerId = defence.customerRegBlackUserId(userId1, level, label).
                    getJSONObject("data").getString("alarm_customer_id");

//            校验黑名单列表中的信息
            defence.checkBlackListExist(userId1, level, label, faceUrl1, name1, phone1, type1, cardKey1,
                    age1, sex1, address1, true);

            defence.customerBlackPage(1, 1);

//            defence.checkBlackListExist(alarmCustomerId,);

//            删除黑名单
            defence.customerDeleteBlack(alarmCustomerId);

//            再次注册
            defence.customerReg(faceUrl1, userId1, name1, phone1, type1, cardKey1,
                    age1, sex1, address1, birthday1);

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
    public void customerRegAfterDelBlackNewUser() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "注册黑名单-删除黑名单-注册社区人员-删除社区人员";

        logger.info("\n\n" + caseName + "\n");

        try {

//            注册黑名单
            String level = "level";
            String label = "label";
            String faceUrl1 = defence.kangLinFaceUrlNew;
            String name1 = ciCaseName + "-" + defence.genRandom7();
            String phone1 = "14567545675";
            String type1 = "RESIDENT";
            String cardKey1 = defence.genRandom();
            String age1 = "20";
            String sex1 = "MALE";
            String address1 = "address1";

            String alarmCustomerId = defence.customerRegBlackNewUser(level, label, faceUrl1, name1, phone1, type1, cardKey1,
                    age1, sex1, address1).getJSONObject("data").getString("alarm_customer_id");

//            删除黑名单
            defence.customerDeleteBlack(alarmCustomerId);

//            注册社区人员
            String userId = defence.genRandom();
            String birthday = "birthday";

            defence.customerReg(faceUrl1, userId, name1, phone1, type1, cardKey1,
                    age1, sex1, address1, birthday);

//            删除社区人员
            defence.customerDelete(userId);

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
    public void customerRegAfterBlackReg() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "注册黑名单--注册社区人员-删除黑名单";

        logger.info("\n\n" + caseName + "\n");

        try {

//            注册黑名单
            String level = "level";
            String label = "label";
            String faceUrl1 = defence.kangLinFaceUrlNew;
            String name1 = ciCaseName + "-" + defence.genRandom7();
            String phone1 = "15678675678";
            String type1 = "RESIDENT";
            String cardKey1 = defence.genRandom();
            String age1 = "20";
            String sex1 = "MALE";
            String address1 = "address1";

            String alarmCustomerId = defence.customerRegBlackNewUser(level, label, faceUrl1, name1, phone1, type1, cardKey1,
                    age1, sex1, address1).getJSONObject("data").getString("alarm_customer_id");

//            注册社区人员
            String userId = defence.genRandom();
            String birthday = "birthday";

            defence.customerReg(faceUrl1, userId, name1, phone1, type1, cardKey1,
                    age1, sex1, address1, birthday, StatusCode.BAD_REQUEST);

//            删除黑名单
            defence.customerDeleteBlack(alarmCustomerId);

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
    public void customerRegReReg() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "社区人员注册-注册-删除";

        logger.info("\n\n" + caseName + "\n");

        try {

//            注册
            String faceUrl1 = defence.kangLinFaceUrlNew;
            String userId1 = ciCaseName + "-_" + defence.genRandom7();
            String name1 = ciCaseName + "-" + defence.genRandom7();
            String phone1 = "17610248107";
            String type1 = "RESIDENT";
            String cardKey1 = defence.genRandom();
            String age1 = "20";
            String sex1 = "MALE";
            String address1 = "address";
            String birthday1 = "birthday1";

            defence.customerReg(faceUrl1, userId1, name1, phone1, type1, cardKey1,
                    age1, sex1, address1, birthday1);

//            再次注册
            String userId2 = ciCaseName + "-_" + defence.genRandom7();
            String cardKey2 = defence.genRandom();
            String name2 = ciCaseName + "-" + defence.genRandom7();
            String phone2 = "17610248107";
            ApiResponse res = defence.customerReg(faceUrl1, userId2, name2, phone2, type1, cardKey2,
                    age1, sex1, address1, birthday1, StatusCode.BAD_REQUEST);

            defence.checkMessage("用相同的face_url注册社区人员-",res,"此人脸与已被用户" + userId1 + "注册");

            String userIdRes = JSON.parseObject(JSON.toJSONString(res)).getJSONObject("data").getString("user_id");

            if (!userId1.equals(userIdRes)){
                throw new Exception("用相同face_url注册时，返回的userId=" + userIdRes + "，实际拥有该人脸的用户的userId=" + userId1);
            }

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

//    --------------------------------------------------社区人员验证---------------------------------------


//    --------------------------------------------------黑名单验证--------------------------------------

    @Test
    public void customerBlackRegReNewuser() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "注册黑名单-用该人的new_user信息注册黑名单-删除黑名单";

        logger.info("\n\n" + caseName + "\n");

        try {

//            注册黑名单
            String level = "level";
            String label = "label";
            String faceUrl1 = defence.kangLinFaceUrlNew;

            String alarmCustomerId = defence.customerRegBlackNewUser(faceUrl1, level, label).getJSONObject("data").
                    getString("alarm_customer_id");

//            再次用该new_user信息注册
            defence.customerRegBlackNewUser(faceUrl1, level, label, StatusCode.BAD_REQUEST);

//            删除黑名单
            defence.customerDeleteBlack(alarmCustomerId);

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
    public void customerBlackRegSamePara() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "注册社区人员-用相同的参数注册黑名单（new_user）-删除社区人员";

        logger.info("\n\n" + caseName + "\n");

        try {

            String level = "level";
            String label = "label";
            String faceUrl1 = defence.kangLinFaceUrlNew;

            String userId = ciCaseName + "-" + defence.genRandom7();

//            注册社区人员
            defence.customerReg(faceUrl1, userId);

//            用相同参数注册黑名单
            defence.customerRegBlackNewUser(faceUrl1, level, label, StatusCode.BAD_REQUEST);

//            删除社区人员
            defence.customerDelete(userId);

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
    public void customerBlackRegSameFace() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "注册黑名单-用相同的face注册，其他参数均不同-删除黑名单";

        logger.info("\n\n" + caseName + "\n");

        try {

//            注册黑名单
            String level = "level";
            String label = "label";
            String faceUrl1 = defence.kangLinFaceUrlNew;
            String name = ciCaseName + "-" + defence.genRandom7();
            String phone = defence.genPhoneNum();
            String type1 = "RESIDENT";
            String cardKey = ciCaseName + "-cardKey" + defence.genRandom7();
            String age1 = "20";
            String sex1 = "MALE";
            String address1 = "sex1";

            String alarmCustomerId = defence.customerRegBlackNewUser(level, label, faceUrl1, name, phone, type1, cardKey,
                    age1, sex1, address1).getJSONObject("data").
                    getString("alarm_customer_id");

//            再次用该new_user信息注册
            String name1 = ciCaseName + "-" + defence.genRandom7();
            String phone1 = defence.genPhoneNum();
            String cardKey1 = ciCaseName + "-cardKey" + defence.genRandom7();

            ApiResponse res = defence.customerRegBlackNewUser(level, label, faceUrl1, name1, phone1, type1, cardKey1,
                    age1, sex1, address1, StatusCode.BAD_REQUEST);

            defence.checkMessage("用相同的face_url注册社区人员-",res,"此人脸与已被用户" + alarmCustomerId + "注册");

            String userIdRes = JSON.parseObject(JSON.toJSONString(res)).getJSONObject("data").getString("user_id");

            if (!alarmCustomerId.equals(userIdRes)){
                throw new Exception("用相同face_url注册时，返回的userId=" + userIdRes + "，实际拥有该人脸的用户的userId=" + alarmCustomerId);
            }

//            删除黑名单
            defence.customerDeleteBlack(alarmCustomerId);

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
    public void blackRegNewuser() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "注册社区人员-删除-用new_user注册";

        logger.info("\n\n" + caseName + "\n");

        try {

//            注册社区人员
            String faceUrl1 = defence.kangLinFaceUrlNew;
            String userId1 = ciCaseName + "-" + defence.genRandom7();
            String name1 = ciCaseName + "-" + defence.genRandom7();
            String phone1 = "15678675678";
            String type1 = "RESIDENT";
            String cardKey1 = defence.genRandom();
            String age1 = "20";
            String sex1 = "FEMALE";
            String address1 = "";
            String birthday1 = "";

            defence.customerReg(faceUrl1, userId1, name1, phone1, type1, cardKey1,
                    age1, sex1, address1, birthday1).getJSONObject("data").getString("user_id");

//            删除社区人员
            defence.customerDelete(VILLAGE_ID, userId1, StatusCode.SUCCESS);

//            用new_user注册
            String level = "level";
            String label = "label";
            String alarmCustomerId = defence.customerRegBlackNewUser(level, label, faceUrl1, name1, phone1, type1, cardKey1,
                    age1, sex1, address1).getJSONObject("data").getString("alarm_customer_id");

//            删除黑名单
            defence.customerDeleteBlack(alarmCustomerId);

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
    public void customerBlackRegAfterDelete() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "注册黑名单-删除-再用相同的参数注册-删除黑名单";

        logger.info("\n\n" + caseName + "\n");

        try {

//            注册黑名单
            String level = "level";
            String label = "label";
            String faceUrl1 = defence.kangLinFaceUrlNew;
            String name1 = ciCaseName + "-" + defence.genRandom7();
            String phone1 = defence.genPhoneNum();
            String type1 = "RESIDENT";
            String cardKey1 = defence.genRandom();
            String age1 = "20";
            String sex1 = "MALE";
            String address1 = "address1";

            String alarmCustomerId = defence.customerRegBlackNewUser(level, label, faceUrl1, name1, phone1, type1, cardKey1,
                    age1, sex1, address1).getJSONObject("data").getString("alarm_customer_id");

//            删除黑名单
            defence.customerDeleteBlack(alarmCustomerId);

//            再次用该new_user信息注册
            alarmCustomerId = defence.customerRegBlackNewUser(level, label, faceUrl1, name1, phone1, type1, cardKey1,
                    age1, sex1, address1).getJSONObject("data").getString("alarm_customer_id");

//            删除黑名单
            defence.customerDeleteBlack(alarmCustomerId);

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

    @Test(dataProvider = "CUSTOMER_BLACK_REG_USERID_NEWUSER")
    public void customerBlackRegUseridNewUser(String faceUrl) {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "注册社区人员-既填写userId（userId即注册的userId），又填写new_user注册";

        logger.info("\n\n" + caseName + "\n");

        try {

//            注册社区人员
            String faceUrl1 = defence.kangLinFaceUrlNew;
            String userId1 = ciCaseName + "-" + defence.genRandom7();
            String name1 = ciCaseName + "-" + defence.genRandom7();
            String phone1 = "18778656787";
            String type1 = "RESIDENT";
            String cardKey1 = defence.genRandom();
            String age1 = "20";
            String sex1 = "MALE";
            String address1 = "address1";
            String birthday1 = "birthday1";

            defence.customerReg(faceUrl1, userId1, name1, phone1, type1, cardKey1,
                    age1, sex1, address1, birthday1).getJSONObject("data").getString("user_id");

//            既填写userId（userId即注册的userId），又填写new_user注册
            String level = "level";
            String label = "label";
            String alarmCustomerId = defence.customerRegBlack(userId1, level, label, faceUrl, name1, phone1, type1, cardKey1,
                    age1, sex1, address1).getJSONObject("data").getString("alarm_customer_id");

//            删除黑名单
            defence.customerDeleteBlack(alarmCustomerId);

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
    public void customerBlackRegUnexistUserid() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "注册黑名单-填写一个不存在的userid";

        logger.info("\n\n" + caseName + "\n");

        try {

//            注册黑名单
            String level = "level";
            String label = "label";
            String userId1 = ciCaseName + "-" + defence.genRandom7();
            defence.customerRegBlackUserId(userId1, level, label, StatusCode.BAD_REQUEST);

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
    public void customerBlackRegDeletedUserid() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "注册社区人员-删除社区人员-删除后用该userId注册";

        logger.info("\n\n" + caseName + "\n");

        try {

//            注册社区人员
            String faceUrl1 = defence.kangLinFaceUrlNew;
            String userId1 = ciCaseName + "-" + defence.genRandom7();
            String name1 = ciCaseName + "-" + defence.genRandom7();
            String phone1 = "18767567898";
            String type1 = "RESIDENT";
            String cardKey1 = defence.genRandom();
            String age1 = "20";
            String sex1 = "FEMALE";
            String address1 = "";
            String birthday1 = "";

            defence.customerReg(faceUrl1, userId1, name1, phone1, type1, cardKey1,
                    age1, sex1, address1, birthday1).getJSONObject("data").getString("user_id");

//            删除社区人员
            defence.customerDelete(userId1);

//           删除社区人员后，用该user_id注册
            defence.customerRegBlackUserId(userId1, StatusCode.BAD_REQUEST);

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
    public void customerBlackRegDeletedAlarmUserid() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "注册黑名单-用该人的alarm_customer_id注册黑名单-删除黑名单";

        logger.info("\n\n" + caseName + "\n");

        try {

//            注册黑名单
            String level = "level";
            String label = "label";
            String faceUrl1 = defence.kangLinFaceUrlNew;
            String name1 = ciCaseName + "-" + defence.genRandom7();
            String phone1 = "18756478965";
            String type1 = "RESIDENT";
            String cardKey1 = defence.genRandom();
            String age1 = "20";
            String sex1 = "FEMALE";
            String address1 = "";

            String alarmCustomerId = defence.customerRegBlackNewUser(level, label, faceUrl1, name1, phone1, type1, cardKey1,
                    age1, sex1, address1).getJSONObject("data").getString("alarm_customer_id");

//            用alarmCustomerId注册黑名单
            defence.customerRegBlackUserId(alarmCustomerId, level, label, StatusCode.BAD_REQUEST);

//            删除黑名单
            defence.customerDeleteBlack(alarmCustomerId);

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
    public void customerBlackResultTest() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "注册黑名单-查询-删除黑名单-查询";

        logger.info("\n\n" + caseName + "\n");

        try {

//            注册黑名单
            String level = "level";
            String label = "label";
            String faceUrl1 = defence.kangLinFaceUrlNew;
            String name1 = ciCaseName + "-" + defence.genRandom7();
            String phone1 = defence.genPhoneNum();
            String type1 = "RESIDENT";
            String cardKey1 = defence.genRandom();
            String age1 = "20";
            String sex1 = "MALE";
            String address1 = "address1";

            String alarmCustomerId = defence.customerRegBlackNewUser(level, label, faceUrl1, name1, phone1, type1, cardKey1,
                    age1, sex1, address1).getJSONObject("data").getString("alarm_customer_id");

//            查询
            defence.checkBlackListExist(alarmCustomerId, level, label, faceUrl1, name1, phone1, type1, cardKey1,
                    age1, sex1, address1, false);

//            删除黑名单
            defence.customerDeleteBlack(alarmCustomerId);

//            查询
            defence.checkBlackListNonExist(alarmCustomerId);

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
    public void customerBlackDeleteThenReg() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "注册黑名单-删除-注册-查询-删除";

        logger.info("\n\n" + caseName + "\n");

        try {

//            注册黑名单
            String level = "level";
            String label = "label";
            String faceUrl1 = defence.kangLinFaceUrlNew;
            String name1 = ciCaseName + "-" + defence.genRandom7();
            String phone1 = defence.genPhoneNum();
            String type1 = "RESIDENT";
            String cardKey1 = defence.genRandom();
            String age1 = "20";
            String sex1 = "MALE";
            String address1 = "address1";

            String alarmCustomerId = defence.customerRegBlackNewUser(level, label, faceUrl1, name1, phone1, type1, cardKey1,
                    age1, sex1, address1).getJSONObject("data").getString("alarm_customer_id");


//            删除黑名单
            defence.customerDeleteBlack(alarmCustomerId);

//            注册
            String alarmCustomerId1 = defence.customerRegBlackNewUser(level, label, faceUrl1, name1, phone1, type1, cardKey1,
                    age1, sex1, address1).getJSONObject("data").getString("alarm_customer_id");

//            查询
            defence.checkBlackListExist(alarmCustomerId1, level, label, faceUrl1, name1, phone1, type1, cardKey1,
                    age1, sex1, address1, false);

//            删除黑名单
            defence.customerDeleteBlack(alarmCustomerId1);

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

//    ------------------------------------------------------------黑名单验证------------------------------------

    //    --------------------------------------------------------------周界告警验证----------------------------------
    @Test
    public void boundaryAlarmTestResult() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "设置周界报警-获取-设置-获取-删除-获取";

        logger.info("\n\n" + caseName + "\n");

        try {

            String deviceId = defence.deviceDongbeijiao;
            double x1 = defence.genDouble();
            double y1 = defence.genDouble();
            double x2 = defence.genDouble();
            double y2 = defence.genDouble();
            double x3 = defence.genDouble();
            double y3 = defence.genDouble();

//            设置周界
            defence.boundaryAlarmAdd(deviceId, x1, y1, x2, y2, x3, y3);

//            周界列表
            JSONArray axis = defence.boundaryAlarmInfo(deviceId).getJSONObject("data").getJSONArray("boundary_axis");

            JSONObject point1 = axis.getJSONObject(0);
            Preconditions.checkArgument(String.valueOf(x1).equals(point1.getString("x")),
                    "注册时，坐标1的x = " + x1 + "，查询时，坐标1的x=" + point1.getString("x"));
            Preconditions.checkArgument(String.valueOf(y1).equals(point1.getString("y")),
                    "注册时，坐标1的y = " + y1 + "，查询时，坐标1的y=" + point1.getString("y"));

            JSONObject point2 = axis.getJSONObject(1);
            Preconditions.checkArgument(String.valueOf(x2).equals(point2.getString("x")),
                    "注册时，坐标2的x = " + x2 + "，查询时，坐标2的x=" + point2.getString("x"));
            Preconditions.checkArgument(String.valueOf(y2).equals(point2.getString("y")),
                    "注册时，坐标2的y = " + y2 + "，查询时，坐标2的y=" + point2.getString("y"));

            JSONObject point3 = axis.getJSONObject(2);
            Preconditions.checkArgument(String.valueOf(x3).equals(point3.getString("x")),
                    "注册时，坐标3的x = " + x3 + "，查询时，坐标3的x=" + point3.getString("x"));
            Preconditions.checkArgument(String.valueOf(y3).equals(point3.getString("y")),
                    "注册时，坐标3的y = " + y3 + "，查询时，坐标3的y=" + point3.getString("y"));

//            再次设置
            x1 = defence.genDouble();
            y1 = defence.genDouble();
            x2 = defence.genDouble();
            y2 = defence.genDouble();
            x3 = defence.genDouble();
            y3 = defence.genDouble();

//            设置周界
            defence.boundaryAlarmAdd(deviceId, x1, y1, x2, y2, x3, y3);

//            获取
            axis = defence.boundaryAlarmInfo(deviceId).getJSONObject("data").getJSONArray("boundary_axis");

            point1 = axis.getJSONObject(0);
            Preconditions.checkArgument(String.valueOf(x1).equals(point1.getString("x")),
                    "注册时，坐标1的x = " + x1 + "，查询时，坐标1的x=" + point1.getString("x"));
            Preconditions.checkArgument(String.valueOf(y1).equals(point1.getString("y")),
                    "注册时，坐标1的y = " + y1 + "，查询时，坐标1的y=" + point1.getString("y"));

            point2 = axis.getJSONObject(1);
            Preconditions.checkArgument(String.valueOf(x2).equals(point2.getString("x")),
                    "注册时，坐标2的x = " + x2 + "，查询时，坐标2的x=" + point2.getString("x"));
            Preconditions.checkArgument(String.valueOf(y2).equals(point2.getString("y")),
                    "注册时，坐标2的y = " + y2 + "，查询时，坐标2的y=" + point2.getString("y"));

            point3 = axis.getJSONObject(2);
            Preconditions.checkArgument(String.valueOf(x3).equals(point3.getString("x")),
                    "注册时，坐标3的x = " + x3 + "，查询时，坐标3的x=" + point3.getString("x"));
            Preconditions.checkArgument(String.valueOf(y3).equals(point3.getString("y")),
                    "注册时，坐标3的y = " + y3 + "，查询时，坐标3的y=" + point3.getString("y"));

//            删除周界
            defence.boundaryAlarmDelete(deviceId);

//            获取
            ApiResponse res = defence.boundaryAlarmInfo(deviceId, StatusCode.BAD_REQUEST);
            String expectMessage = "the device dose not have boundaryAlarm";
            Preconditions.checkArgument(expectMessage.equals(res.getMessage()), "删除周界告警后，查询结果中message=" + res.getMessage() + "，期待=" + expectMessage + "，设备id=" + deviceId + "，village=" + VILLAGE_ID);

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

        String caseDesc = "设置周界报警-坐标两个点";

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

        String caseDesc = "设置周界报警-坐标y z";

        logger.info("\n\n" + caseName + "\n");

        try {

            String deviceId = defence.deviceDongbeijiao;

//            设置周界
            ApiResponse res = defence.boundaryAlarmAdd(deviceId, StatusCode.BAD_REQUEST);

            defence.checkMessage("设置周界告警-", res, "point x should be exist  and greater than 0 and less than 1");

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
    public void boundaryAlarmReDelete() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "设置周界报警-删除-删除";

        logger.info("\n\n" + caseName + "\n");

        try {

            String deviceId = defence.deviceDongbeijiao;
            double x1 = defence.genDouble();
            double y1 = defence.genDouble();
            double x2 = defence.genDouble();
            double y2 = defence.genDouble();
            double x3 = defence.genDouble();
            double y3 = defence.genDouble();

//            设置周界
            defence.boundaryAlarmAdd(deviceId, x1, y1, x2, y2, x3, y3);

//            删除周界
            defence.boundaryAlarmDelete(deviceId);

//            删除周界
            defence.boundaryAlarmDelete(deviceId, StatusCode.BAD_REQUEST);

//            设置周界
            defence.boundaryAlarmAdd(deviceId, x1, y1, x2, y2, x3, y3);

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

//    --------------------------------------------------告警记录，告警记录处理--------------------------------------

    @Test
    public void alarmLogPageOperatePage0() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "告警记录(分页查询)-返回值中page不为0";

        logger.info("\n\n" + caseName + "\n");

        try {

            String deviceId = "6948414027629568";//酷芯AI相机测试

//            告警记录(分页查询)
            JSONObject res = defence.alarmLogPage(deviceId, 1, 11);

            String requestId = res.getString("request_id");

            int page = res.getJSONObject("data").getInteger("page");

            Preconditions.checkArgument(page > 0, "告警记录-，page不应=0，request_id=" + requestId);


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
    public void alarmLogPageOperateTest() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "告警记录(分页查询)-告警记录处理，验证code==1000";

        logger.info("\n\n" + caseName + "\n");

        try {

            String deviceId = "";
//            String operator = "索菲·索菲";
            String operator = "sophie·sophie";
            String optResult = "[]@-+~！#$^&()={}|;:'<>.?/·！￥……（）——【】、；：”‘《》。？、,%*";

//            告警记录(分页查询)
            String alarmId = "";
            JSONArray list = defence.alarmLogPage(deviceId, 1, 100).getJSONObject("data").getJSONArray("list");
            boolean hasUndo = false;
            for (int i = 0; i < list.size(); i++) {
                JSONObject single = list.getJSONObject(i);
                String optStatus = single.getString("opt_status");
                if ("未处理".equals(optStatus)) {
                    hasUndo = true;
                    alarmId = single.getString("id");

//                    告警记录处理
                    defence.alarmLogOperate(alarmId, operator, optResult);

                    break;
                }
            }

            if (!hasUndo) {
                throw new Exception("告警记录中没有“未处理”的记录");
            }

//            验证处理结果
            list = defence.alarmLogPage(deviceId, 1, 100).getJSONObject("data").getJSONArray("list");

            boolean isExist = false;
            for (int i = 0; i < list.size(); i++) {
                JSONObject single = list.getJSONObject(i);
                String id = single.getString("id");
                if (alarmId.equals(id)) {
                    isExist = true;
                    checkUtil.checkKeyValue("告警记录（分页查询）", single, "opt_status", "已处理", true);
                    checkUtil.checkKeyValue("告警记录（分页查询）", single, "opt_result", optResult, true);
                    checkUtil.checkKeyValue("告警记录（分页查询）", single, "operator", operator, true);
                    break;
                }
            }

            if (!isExist) {
                throw new Exception("处理后结果没有出现在列表中，alarm_id = " + alarmId);
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

//    ----------------------------------------------告警记录/告警记录处理------------------------------------------------------

//    --------------------------------------------------设备画面告警-----------------------------------------------------------

    @Test
    public void deivceCustomerNumAlarmTest() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "设置画面告警人数-设置-删除-删除-设置";

        logger.info("\n\n" + caseName + "\n");

        try {

            String deviceId = defence.deviceDongbeijiao;
            int threshold = 10;

//            设备画面人数告警设置
            defence.deviceCustomerNumAlarmAdd(deviceId, threshold);

//            设备画面人数告警设置
            defence.deviceCustomerNumAlarmAdd(deviceId, threshold);

//            删除
            defence.deviceCustomerNumAlarmDelete(deviceId);

//            删除
            defence.deviceCustomerNumAlarmDelete(deviceId, StatusCode.BAD_REQUEST);

//            设备画面人数告警设置
            defence.deviceCustomerNumAlarmAdd(deviceId, threshold);

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

    @Test(dataProvider = "GOOD_THRESHOLD")
    public void deivceCustomerNumAlarmGoodThreshold(String threshold) {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "设置画面告警人数-threshold=" + threshold;

        logger.info("\n\n" + caseName + "\n");

        try {

            String deviceId = defence.deviceDongbeijiao;

//            设备画面人数告警设置
            defence.deviceCustomerNumAlarmAdd(deviceId, threshold, StatusCode.SUCCESS);

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

    @Test(dataProvider = "BAD_THRESHOLD")
    public void deivceCustomerNumAlarmBadThreshold(String threshold, String message) {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "设置画面告警人数-threshold=" + threshold;

        logger.info("\n\n" + caseName + "\n");

        try {

            String deviceId = defence.deviceDongbeijiao;

//            设备画面人数告警设置
            ApiResponse res = defence.deviceCustomerNumAlarmAdd(deviceId, threshold, StatusCode.BAD_REQUEST);

            defence.checkMessage("设备画面人数告警设置-", res, message);

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


//    ----------------------------------------------------结构化检索------------------------------------------------------

    @Test
    public void customerSearchListSex() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "结构化检索(分页查询)，验证code==1000";

        logger.info("\n\n" + caseName + "\n");

        try {

            String deviceId = "";
            long startTime = 0;
            long endTime = 0;
//            String sex = "MALE";//MALE/FEMALE
            String age = "";
            String hair = "";
            String clothes = "";
            String clothesColour = "";
            String trousers = "";
            String trousersColour = "";
            String hat = "";
            String knapsack = "";

//            结构化检索(分页查询)
            String[] sexes = {"MALE", "FEMALE"};
            for (int j = 0; j < sexes.length; j++) {

                JSONObject res = defence.customerSearchList(deviceId, startTime, endTime,
                        sexes[j], age, hair, clothes, clothesColour, trousers, trousersColour, hat, knapsack, 1, 100);

                String requestId = res.getString("request_id");
                JSONArray list = res.getJSONObject("data").getJSONArray("list");

                for (int i = 0; i < list.size(); i++) {
                    JSONObject single = list.getJSONObject(i);

                    String sexRes = single.getString("sex");

                    Preconditions.checkArgument(sexes[j].equals(sexRes), "结构化检索(分页查询)，查询条件是sex=" + sexes[j] + "，返回结果中sex=" + sexRes +
                            "，request_id=" + requestId + "，customer_id=" + single.getString("customer_id"));
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
    public void customerSearchListAge() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "结构化检索(分页查询)-age=20";

        logger.info("\n\n" + caseName + "\n");

        try {

            String deviceId = "";
            long startTime = 0;
            long endTime = 0;
            String sex = "";//MALE/FEMALE
            String age = "20";
            String hair = "";
            String clothes = "";
            String clothesColour = "";
            String trousers = "";
            String trousersColour = "";
            String hat = "";
            String knapsack = "";

//            结构化检索(分页查询)
            JSONObject res = defence.customerSearchList(deviceId, startTime, endTime,
                    sex, age, hair, clothes, clothesColour, trousers, trousersColour, hat, knapsack, 1, 100);

            String requestId = res.getString("request_id");
            JSONArray list = res.getJSONObject("data").getJSONArray("list");

            for (int i = 0; i < list.size(); i++) {
                JSONObject single = list.getJSONObject(i);

                String ageRes = single.getString("age");

                Preconditions.checkArgument(age.equals(ageRes), "结构化检索(分页查询)，查询条件是age=" + age + "，返回结果中age=" + ageRes +
                        "，request_id=" + requestId + "，customer_id=" + single.getString("customer_id"));
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
    public void customerSearchListHair() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "结构化检索(分页查询)-hair=";

        logger.info("\n\n" + caseName + "\n");

        try {

            String deviceId = "";
            long startTime = 0;
            long endTime = 0;
            String sex = "";//MALE/FEMALE
            String age = "";
//            String hair = "";//SHORT \| LONG
            String clothes = "";
            String clothesColour = "";
            String trousers = "";
            String trousersColour = "";
            String hat = "";
            String knapsack = "";

//            结构化检索(分页查询)
            String[] hairs = {"SHORT", "LONG"};
            for (int j = 0; j < hairs.length; j++) {

                JSONObject res = defence.customerSearchList(deviceId, startTime, endTime,
                        sex, age, hairs[j], clothes, clothesColour, trousers, trousersColour, hat, knapsack, 1, 100);

                String requestId = res.getString("request_id");
                JSONArray list = res.getJSONObject("data").getJSONArray("list");

                for (int i = 0; i < list.size(); i++) {
                    JSONObject single = list.getJSONObject(i);

                    String hairRes = single.getString("hair");

                    Preconditions.checkArgument(hairs[j].equals(hairRes), "结构化检索(分页查询)，查询条件是hair=" + hairs[j] + "，返回结果中hair=" + hairRes +
                            "，request_id=" + requestId + "，customer_id=" + single.getString("customer_id"));
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
    public void customerSearchListClothes() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "结构化检索(分页查询)-clothes=";

        logger.info("\n\n" + caseName + "\n");

        try {

            String deviceId = "";
            long startTime = 0;
            long endTime = 0;
            String sex = "";//MALE/FEMALE
            String age = "";
            String hair = "";//SHORT \| LONG
//            String clothes = "";
            String clothesColour = "";
            String trousers = "";
            String trousersColour = "";
            String hat = "";
            String knapsack = "";

//            结构化检索(分页查询)
            String[] clothes = {"SHORT_SLEEVES", "LONG_SLEEVES"};
            for (int j = 0; j < clothes.length; j++) {

                JSONObject res = defence.customerSearchList(deviceId, startTime, endTime,
                        sex, age, hair, clothes[j], clothesColour, trousers, trousersColour, hat, knapsack, 1, 100);

                String requestId = res.getString("request_id");
                JSONArray list = res.getJSONObject("data").getJSONArray("list");

                for (int i = 0; i < list.size(); i++) {
                    JSONObject single = list.getJSONObject(i);

                    String clothesRes = single.getString("clothes");

                    Preconditions.checkArgument(clothes[j].equals(clothesRes), "结构化检索(分页查询)，查询条件是clothes=" + clothes[j] + "，返回结果中clothes=" + clothesRes +
                            "，request_id=" + requestId + "，customer_id=" + single.getString("customer_id"));
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
    public void customerSearchListTrousers() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "结构化检索(分页查询)-trousers=";

        logger.info("\n\n" + caseName + "\n");

        try {

            String deviceId = "";
            long startTime = 0;
            long endTime = 0;
            String sex = "";//MALE/FEMALE
            String age = "";
            String hair = "";//SHORT \| LONG
            String clothes = "";
            String clothesColour = "";
//            String trousers = "";
            String trousersColour = "";
            String hat = "";
            String knapsack = "";

//            结构化检索(分页查询)
            String[] trousers = {"SKIRT", "TROUSERS", "SHORTS"};
            for (int j = 0; j < trousers.length; j++) {

                JSONObject res = defence.customerSearchList(deviceId, startTime, endTime,
                        sex, age, hair, clothes, clothesColour, trousers[j], trousersColour, hat, knapsack, 1, 100);

                String requestId = res.getString("request_id");
                JSONArray list = res.getJSONObject("data").getJSONArray("list");

                for (int i = 0; i < list.size(); i++) {
                    JSONObject single = list.getJSONObject(i);

                    String trousersRes = single.getString("trousers");

                    Preconditions.checkArgument(trousersRes.contains(trousers[j]), "结构化检索(分页查询)，查询条件是trousers=" + trousers[j] + "，返回结果中trousers=" + trousersRes +
                            "，request_id=" + requestId + "，customer_id=" + single.getString("customer_id"));
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
    public void customerSearchListHat() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "结构化检索(分页查询)-hat=";

        logger.info("\n\n" + caseName + "\n");

        try {

            String deviceId = "";
            long startTime = 0;
            long endTime = 0;
            String sex = "";//MALE/FEMALE
            String age = "";
            String hair = "";//SHORT \| LONG
            String clothes = "";
            String clothesColour = "";
            String trousers = "";
            String trousersColour = "";
//            String hat = "";
            String knapsack = "";

//            结构化检索(分页查询)
            String[] hats = {"YES", "NO"};
            for (int j = 0; j < hats.length; j++) {

                JSONObject res = defence.customerSearchList(deviceId, startTime, endTime,
                        sex, age, hair, clothes, clothesColour, trousers, trousersColour, hats[j], knapsack, 1, 100);

                String requestId = res.getString("request_id");
                JSONArray list = res.getJSONObject("data").getJSONArray("list");

                for (int i = 0; i < list.size(); i++) {
                    JSONObject single = list.getJSONObject(i);

                    String hatRes = single.getString("hat");

                    Preconditions.checkArgument(hats[j].equals(hatRes), "结构化检索(分页查询)，查询条件是hat=" + hats[j] + "，返回结果中hat=" + hatRes +
                            "，request_id=" + requestId + "，customer_id=" + single.getString("customer_id"));
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
    public void customerSearchListKnapsack() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "结构化检索(分页查询)-knapsack=";

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

//            结构化检索(分页查询)
            String[] knapsacks = {"YES", "NO"};
            for (int j = 0; j < knapsacks.length; j++) {

                JSONObject res = defence.customerSearchList(deviceId, startTime, endTime,
                        sex, age, hair, clothes, clothesColour, trousers, trousersColour, hat, knapsacks[j], 1, 100);

                String requestId = res.getString("request_id");
                JSONArray list = res.getJSONObject("data").getJSONArray("list");

                for (int i = 0; i < list.size(); i++) {
                    JSONObject single = list.getJSONObject(i);

                    String knapsackRes = single.getString("knapsack");

                    Preconditions.checkArgument(knapsacks[j].equals(knapsackRes), "结构化检索(分页查询)，查询条件是knapsack=" + knapsacks[j] + "，返回结果中knapsack=" + knapsackRes +
                            "，request_id=" + requestId + "，customer_id=" + single.getString("customer_id"));
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
    public void customerSearchListNoPara() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "结构化检索(分页查询)，不选择任何条件";

        logger.info("\n\n" + caseName + "\n");

        try {

            String deviceId = "";
            long startTime = 0;
            long endTime = 0;
            String sex = "";//MALE/FEMALE
            String age = "";
            String hair = "";//SHORT \| LONG
            String clothes = "";
            String clothesColour = "";
            String trousers = "";
            String trousersColour = "";
            String hat = "";
            String knapsack = "";

//            结构化检索(分页查询)
            JSONObject data = defence.customerSearchList(deviceId, startTime, endTime,
                    sex, age, hair, clothes, clothesColour, trousers, trousersColour, hat, knapsack, 1, 100).getJSONObject("data");

            if (data.getJSONArray("list").size() < 1) {
                throw new Exception("结构化搜索（分页查询），仅传village，page和size时，查询结果为空");
            }

            Object[] objects = customerSearchListNotNull();

            for (int i = 0; i < objects.length; i++) {
                String key = objects[i].toString();
                checkUtil.checkNotNull("结构化检索(分页查询)--", data, key);
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
    public void customerSearchListPage0() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "结构化检索(分页查询)，返回值中page>0";

        logger.info("\n\n" + caseName + "\n");

        try {

            String deviceId = "";
            long startTime = System.currentTimeMillis() - 100000;
            long endTime = startTime + 100;
            String sex = "";//MALE/FEMALE
            String age = "";
            String hair = "";//SHORT \| LONG
            String clothes = "";
            String clothesColour = "";
            String trousers = "";
            String trousersColour = "";
            String hat = "";
            String knapsack = "";

//            结构化检索(分页查询)
            JSONObject res = defence.customerSearchList(deviceId, startTime, endTime,
                    sex, age, hair, clothes, clothesColour, trousers, trousersColour, hat, knapsack, 1, 1);

            String requestId = res.getString("request_id");

            int page = res.getJSONObject("data").getInteger("page");

            Preconditions.checkArgument(page != 0, "结构化检索-，page不应=0，request_id=" + requestId);

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
    public void customerSearchListStartMTEnd() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "结构化检索(分页查询)，开始时间晚于结束时间";

        logger.info("\n\n" + caseName + "\n");

        try {

            long startTime = System.currentTimeMillis() - 100000;
            long endTime = startTime - 200000100;

//            结构化检索(分页查询)
            ApiResponse res = defence.customerSearchList(startTime, endTime, StatusCode.BAD_REQUEST);

            defence.checkMessage("结构化检索(分页查询)-", res, "结束时间不应早于开始时间");

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
    public void customerSearchListStartMTNow() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "结构化检索(分页查询)，开始时间晚于当前时间";

        logger.info("\n\n" + caseName + "\n");

        try {

            long startTime = System.currentTimeMillis() + 100000;
            long endTime = startTime + 200000100;

//            结构化检索(分页查询)
            ApiResponse res = defence.customerSearchList(startTime, endTime, StatusCode.BAD_REQUEST);

            defence.checkMessage("结构化检索(分页查询)-", res, "开始时间不能晚于当前时间");

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

//    -------------------------------------------------------结构化检索--------------------------------------------------------

//    ----------------------------------------------------------轨迹查询---------------------------------------------------------

    @Test
    public void customerFaceTraceSimilarity() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "轨迹查询(人脸搜索)，查询条件=相似度";

        logger.info("\n\n" + caseName + "\n");

        try {

            String picUrl = defence.liaoFaceUrlNew;

//            无数据时
            long startTime = System.currentTimeMillis()-100;
            long endTime = startTime;

            String[] similaritys = {"HIGH", "LOW"};

            for (int j = 0; j < similaritys.length; j++) {

                defence.checkFaceTraceListSimilarity(picUrl, similaritys[j], startTime, endTime);
            }
//            有数据时
            startTime = 0;
            endTime = 0;

            for (int j = 0; j < similaritys.length; j++) {
                defence.checkFaceTraceListSimilarity(picUrl, similaritys[j], startTime, endTime);
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
    public void customerFaceTracePage0() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "轨迹查询(人脸搜索)，页数需大于0";

        logger.info("\n\n" + caseName + "\n");

        try {

            String picUrl = defence.liaoFaceUrlNew;
            long startTime = System.currentTimeMillis() - 10;
            long endTime = startTime + 10;

            JSONObject res = defence.customerFaceTraceList(picUrl, startTime, endTime, "", 1, 1);

            String requestId = res.getString("request_id");

            int page = res.getJSONObject("data").getInteger("page");

            Preconditions.checkArgument(page > 0, "轨迹查询-，page不应=0，request_id=" + requestId);
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
    public void customerFaceTraceStartMTEnd() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "轨迹查询(人脸搜索)-开始时间>结束时间";

        logger.info("\n\n" + caseName + "\n");

        try {

            String picUrl = defence.liaoFaceUrlNew;
            long startTime = System.currentTimeMillis();
            long endTime = System.currentTimeMillis() - 100000;

            ApiResponse res = defence.customerFaceTraceList(picUrl, startTime, endTime, "", StatusCode.BAD_REQUEST);

            defence.checkMessage("轨迹查询(人脸搜索)-", res, "结束时间不应早于开始时间");

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
    public void customerFaceTraceStartMTNow() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "轨迹查询(人脸搜索)-开始时间>当前时间";

        logger.info("\n\n" + caseName + "\n");

        try {

            String picUrl = defence.liaoFaceUrlNew;
            long startTime = System.currentTimeMillis() + 10000;
            long endTime = startTime + 100000;

            ApiResponse res = defence.customerFaceTraceList(picUrl, startTime, endTime, "", StatusCode.BAD_REQUEST);

            defence.checkMessage("轨迹查询(人脸搜索)-", res, "开始时间不能晚于当前时间");

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

//    ------------------------------------------------轨迹查询------------------------------------------------------


    //    -------------------------------------------------人脸识别记录分页查询--------------------------------------------
    @Test
    public void customerHistoryCapturePageSimilarity() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "人脸识别记录分页查询-查询条件是similarity";

        logger.info("\n\n" + caseName + "\n");

        try {

            String faceUrl = defence.liaoFaceUrlNew;
            String customerId = "";
            String namePhone = "";
            String device_id = "";

//            无数据
            long startTime = System.currentTimeMillis()-10;
            long endTime = startTime;

            String[] similaritys = {"HIGH", "LOW"};

            for (int j = 0; j < similaritys.length; j++) {

                defence.checkHisCapturePageSimilarity(faceUrl, customerId, device_id, namePhone, similaritys[j], startTime, endTime);
            }

//            有数据
            startTime = 0;
            endTime = 0;

            for (int j = 0; j < similaritys.length; j++) {

                defence.checkHisCapturePageSimilarity(faceUrl, customerId, device_id, namePhone, similaritys[j], startTime, endTime);
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
    public void customerHistoryCapturePageTestCustomerId() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "人脸识别记录分页查询-查询条件是CustomerId";

        logger.info("\n\n" + caseName + "\n");

        try {

            String faceUrl = "";
//            String faceUrl = defence.liaoFaceUrlNew;
            String namePhone = "";
            String similarity = "";
            String device_id = "";

            long startTime = 0;
            long endTime = 0;

            String[] customerIds =
                    {
                            "b359b23f-fb81-4b9e-9f27-bad21a01f003",
                            "2d305b3f-42e4-4214-b9cc-22b6e34d3fc9",
                    "e6596a78-c244-41cb-ad2e-79e16339fabd",
                    defence.genRandom()};

            for (int j = 0; j < customerIds.length; j++) {

//                人脸识别记录分页查询
                JSONObject res = defence.customerHistoryCapturePage(faceUrl, customerIds[j], device_id, namePhone,
                        similarity, startTime, endTime, 1, 100);

                String requestId = res.getString("request_id");
                JSONArray list = res.getJSONObject("data").getJSONArray("list");

                for (int i = 0; i < list.size(); i++) {
                    JSONObject single = list.getJSONObject(i);

                    String customerIdRes = single.getString("customer_id");

                    Preconditions.checkArgument(customerIds[j].equals(customerIdRes), "人脸识别记录分页查询，查询条件是customer_id=" + customerIds[j] + "，返回结果中customer_id=" + customerIdRes +
                            "，request_id=" + requestId + "，customer_id=" + single.getString("customer_id"));
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
    public void customerHistoryCapturePageDeviceId() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "人脸识别记录分页查询-设备id";

        logger.info("\n\n" + caseName + "\n");

        try {

//            String faceUrl = "";
            String faceUrl = defence.liaoFaceUrlNew;
            String namePhone = "";
            String similarity = "";
            String customerId = "";

            long startTime = 0;
            long endTime = 0;

            String[] deviceIds = {defence.device1Huiyi, defence.deviceYilaoshi, defence.device1Caiwu,
                    defence.deviceXieduimen, defence.deviceChukou, defence.deviceDongbeijiao, "3274"};

            for (int j = 0; j < deviceIds.length; j++) {

//                人脸识别记录分页查询
                JSONObject res = defence.customerHistoryCapturePage(faceUrl, customerId, deviceIds[j], namePhone,
                        similarity, startTime, endTime, 1, 100);

                String requestId = res.getString("request_id");
                JSONArray list = res.getJSONObject("data").getJSONArray("list");

                for (int i = 0; i < list.size(); i++) {
                    JSONObject single = list.getJSONObject(i);

                    String deviceIdRes = single.getString("device_id");

                    Preconditions.checkArgument(deviceIds[j].equals(deviceIdRes), "人脸识别记录分页查询，查询条件是device_id=" + deviceIds[j] + "，返回结果中device_id=" + deviceIdRes +
                            "，request_id=" + requestId + "，customer_id=" + single.getString("customer_id"));
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
    public void customerHistoryCapturePageNamephone() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "人脸识别记录分页查询-namePhone";

        logger.info("\n\n" + caseName + "\n");

        try {

//            String faceUrl = "";
            String faceUrl = defence.zhidongFaceUrl;
            String similarity = "";
            String customerId = "";

            long startTime = 0;
            long endTime = 0;

            String namePhones[] = {"17766331971",//存在
                    "12345456765"//不存在
            };

            for (int j = 0; j < namePhones.length; j++) {

//                人脸识别记录分页查询
                JSONObject res = defence.customerHistoryCapturePage(faceUrl, customerId, "", namePhones[j],
                        similarity, startTime, endTime, 1, 100);

                int total = res.getJSONObject("data").getInteger("total");
                if (total > 0) {

                    String requestId = res.getString("request_id");
                    JSONArray list = res.getJSONObject("data").getJSONArray("list");

                    for (int i = 0; i < list.size(); i++) {
                        JSONObject single = list.getJSONObject(i);

                        String phoneRes = single.getString("phone");

                        Preconditions.checkArgument(namePhones[j].equals(phoneRes), "人脸识别记录分页查询，查询条件是namePhone=" + namePhones[j] + "，返回结果中namePhone=" + phoneRes +
                                "，request_id=" + requestId + "，customer_id=" + single.getString("customer_id"));
                    }

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
    public void customerHistoryCapturePageStartMTEnd() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "人脸识别记录分页查询-开始时间大于结束时间";

        logger.info("\n\n" + caseName + "\n");

        try {

            String faceUrl = defence.zhidongFaceUrl;

            long startTime = System.currentTimeMillis();
            long endTime = startTime - 466546546;

//                人脸识别记录分页查询
            ApiResponse res = defence.customerHistoryCapturePage(faceUrl, "", startTime, endTime, 1, 1, StatusCode.BAD_REQUEST);

            defence.checkMessage("人脸识别记录分页查询-", res, "结束时间不应早于开始时间");

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
    public void customerHistoryCapturePageStartMTNow() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "人脸识别记录分页查询-开始时间在将来";

        logger.info("\n\n" + caseName + "\n");

        try {

            String faceUrl = defence.zhidongFaceUrl;

            long startTime = System.currentTimeMillis() + 32873;
            long endTime = startTime + 583957;

//                人脸识别记录分页查询
            ApiResponse res = defence.customerHistoryCapturePage(faceUrl, "", startTime, endTime, 1, 1, StatusCode.BAD_REQUEST);

            defence.checkMessage("人脸识别记录分页查询-", res, "开始时间不能晚于当前时间");

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
    public void customerHistoryCapturePagePage0() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "人脸识别记录分页查询-page不能等于0";

        logger.info("\n\n" + caseName + "\n");

        try {

//            String faceUrl = "";
            String faceUrl = defence.zhidongFaceUrl;
            String similarity = "";
            String customerId = "";

            long startTime = System.currentTimeMillis() - 100;
            long endTime = System.currentTimeMillis();

            String namePhones[] = {"12378981111", "15478675643", "16789765609", "10990876564"};

            for (int j = 0; j < namePhones.length; j++) {

//                人脸识别记录分页查询
                JSONObject res = defence.customerHistoryCapturePage(faceUrl, customerId, "", namePhones[j],
                        similarity, startTime, endTime, 1, 1);

                String requestId = res.getString("request_id");

                int page = res.getJSONObject("data").getInteger("page");

                Preconditions.checkArgument(page != 0, "人脸识别记录分页查询，page不应=0，request_id=" + requestId);
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

//    -------------------------------------------------人脸识别记录分页查询----------------------------------------------

//    ---------------------------------------------------人物详情信息-------------------------------------------------------

    @Test
    public void customerInfoAllPara() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "人物详情信息，参数都传，验证返回结果";

        logger.info("\n\n" + caseName + "\n");

        String userId = "";

        try {

            String faceUrl = defence.wanghuanFaceUrlNew;
            userId = ciCaseName + "-" + defence.genRandom7();

            String age = "20";
            String sex = "MALE";

//            社区人员注册
            String customerId = defence.customerReg(faceUrl, userId).getJSONObject("data").getString("customer_id");

//            人物详情信息
            JSONObject data = defence.customerInfo(userId, customerId).getJSONObject("data").getJSONObject("info");

            checkUtil.checkKeyValue("人物详细信息-", data, "customer_id", customerId, true);
            checkUtil.checkKeyValue("人物详细信息-", data, "user_id", userId, true);
            checkUtil.checkKeyValue("人物详细信息-", data, "age", age, true);
            checkUtil.checkKeyValue("人物详细信息-", data, "sex", sex, true);

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
//            删除社区人员
            defence.customerDeleteTry(userId);
            defence.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    @Test
    public void customerInfoVillageOnly() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "人物详情信息，仅填写villageId";

        logger.info("\n\n" + caseName + "\n");

        String userId = "";

        try {

            String faceUrl = defence.wanghuanFaceUrlNew;
            userId = ciCaseName + "-" + defence.genRandom7();

//            社区人员注册
            defence.customerReg(faceUrl, userId).getJSONObject("data").getString("customer_id");

//            人物详情信息
            ApiResponse res = defence.customerInfo();

            defence.checkMessage("人物详细信息-", res, "customer_id and user_id all empty");

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
//            删除社区人员
            defence.customerDeleteTry(userId);
            defence.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    @Test
    public void customerInfoNoCustomerId() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "人物详情信息，不填customerId";

        logger.info("\n\n" + caseName + "\n");

        String userId = "";

        try {

            String faceUrl = defence.wanghuanFaceUrlNew;
            userId = defence.genRandom();

            String age = "20";
            String sex = "MALE";

//            社区人员注册
            String customerId = defence.customerReg(faceUrl, userId).getJSONObject("data").getString("customer_id");

//            人物详情信息
            JSONObject data = defence.customerInfo(userId, "").getJSONObject("data").getJSONObject("info");

            checkUtil.checkKeyValue("人物详细信息-", data, "customer_id", customerId, true);
            checkUtil.checkKeyValue("人物详细信息-", data, "user_id", userId, true);
            checkUtil.checkKeyValue("人物详细信息-", data, "age", age, true);
            checkUtil.checkKeyValue("人物详细信息-", data, "sex", sex, true);

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
//            删除社区人员
            defence.customerDeleteTry(userId);
            defence.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    @Test
    public void customerInfoNoUserId() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "人物详情信息，不传userId";

        logger.info("\n\n" + caseName + "\n");

        String userId = "";

        try {

            String faceUrl = defence.wanghuanFaceUrlNew;
            userId = ciCaseName + "-" + defence.genRandom7();

            String age = "20";
            String sex = "MALE";

//            社区人员注册
            String customerId = defence.customerReg(faceUrl, userId).getJSONObject("data").getString("customer_id");

//            人物详情信息
            JSONObject data = defence.customerInfo("", customerId).getJSONObject("data").getJSONObject("info");

            checkUtil.checkKeyValue("人物详细信息-", data, "customer_id", customerId, true);
            checkUtil.checkKeyValue("人物详细信息-", data, "user_id", userId, true);
            checkUtil.checkKeyValue("人物详细信息-", data, "age", age, true);
            checkUtil.checkKeyValue("人物详细信息-", data, "sex", sex, true);


        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
//            删除社区人员
            defence.customerDeleteTry(userId);
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

    @DataProvider(name = "CUSTOMER_REG")
    public Object[][] customerReg() {
        return new Object[][]{
//                        faceUrl,userId,name,phone,type,cardKey,age,sex,address,birthday
                new Object[]{
//                        userId相同，其他均不同
                        "userId", defence.nanhaiFaceUrlNew, "userId", defence.genRandom7(), defence.genPhoneNum(), "RESIDENT",
                        defence.genRandom(), "20", "MALE", "address", "birthday", StatusCode.BAD_REQUEST
                },

                new Object[]{
//                        phone+name相同，其他均不同
                        "phone+name", defence.nanhaiFaceUrlNew, defence.genRandom(), "name", "phone", "RESIDENT",
                        defence.genRandom(), "20", "MALE", "address", "birthday", StatusCode.BAD_REQUEST
                },

                new Object[]{
//                        cardKey相同，其他均不同
                        "cardKey", defence.nanhaiFaceUrlNew, defence.genRandom(), defence.genRandom7(), defence.genPhoneNum(), "RESIDENT",
                        "cardKey", "20", "MALE", "address", "birthday", StatusCode.BAD_REQUEST
                },

                new Object[]{
//                        faceUrl相同，其他的参数不同
                        "faceUrl", defence.nalaFaceUrlNew, defence.genRandom(), defence.genRandom7(), defence.genPhoneNum(), "RESIDENT",
                        defence.genRandom(), "20", "MALE", "address", "birthday", StatusCode.BAD_REQUEST
                }
        };
    }

    @DataProvider(name = "CUSTOMER_DELETE")
    public Object[][] customerDelete() {
        return new Object[][]{
//                      village_id，user_id
                new Object[]{
//                       villageId不存在，userId存在
                        1, "userId"
                },

                new Object[]{
//                        villageId存在，userId不存在
                        VILLAGE_ID, "notExist"
                },
        };
    }

    @DataProvider(name = "CUSTOMER_BLACK_REG_USERID_NEWUSER")
    public Object[][] customerDeleteUserIdNewuser() {
        return new Object[][]{
//                      village_id，user_id
                new Object[]{
//                      与社区人员是同一个face
                        "faceUrl"
                },

                new Object[]{
//                      与社区人员是不同face
                        "faceUrl"
                },
        };
    }

    @DataProvider(name = "CUSTOMER_BLACK_REG_NEWUSER")
    public Object[][] customerDeleteNewuser() {
        return new Object[][]{
//                      village_id，user_id
                new Object[]{
//                      与社区人员是同一个face
                        "faceUrl", StatusCode.BAD_REQUEST
                },

                new Object[]{
//                      与社区人员是不同face
                        "faceUrl", StatusCode.SUCCESS
                },
        };
    }

    //    结构化检索(分页查询)
    @DataProvider(name = "CUSTOMER_SEARCH_LIST_NOT_NULL")
    public Object[] customerSearchListNotNull() {
        return new Object[]{
                "[list]-id",
//                "[list]-customer_id",
//                "[list]-pic_url",
                "[list]-timestamp",
                "[list]-village_id", "[list]-village_name", "[list]-device_id", "[list]-device_name"
        };
    }


    @DataProvider(name = "GOOD_THRESHOLD")
    public Object[] goodThreshold() {

        return new Object[]{

                "1", "100", "100000",
                "2147483647"//int的最大值
        };
    }

    @DataProvider(name = "BAD_THRESHOLD")
    public Object[][] baddThreshold() {

        return new Object[][]{

                new Object[]{

                        "-1", "[threshold] should grander than 0"
                },

                new Object[]{

                        "0", "[threshold] should grander than 0"
                },

                new Object[]{
                        "2147483648", "请求JSON转换出错"//int的最大值+1
                },

                new Object[]{

                        "9223372036854775807", "请求JSON转换出错"//long的最大值
                },

                new Object[]{

                        "fdjkf", "请求JSON转换出错"
                },
        };
    }
}

