package com.haisheng.framework.testng.defence;

import ai.winsense.model.ApiResponse;
import com.alibaba.fastjson.JSON;
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

    private String liaoFaceUrl = "https://retail-huabei2.oss-cn-beijing.aliyuncs.com/BUSINESS_RISK_DAILY/qa_test/AI/liao.jpg?OSSAccessKeyId=LTAILRdMUAwTZdPh&Expires=1903004987&Signature=TYljFO4ipdEJvj1QDKSnjcVjbpA%3D";
    private String liaoMaskFaceUrl = "https://retail-huabei2.oss-cn-beijing.aliyuncs.com/BUSINESS_RISK_DAILY/qa_test/AI/liaoMask.jpg?OSSAccessKeyId=LTAILRdMUAwTZdPh&Expires=1903005006&Signature=x%2B2GjT%2BedL82HhL6n6%2FOUMxfpvU%3D";
    private String xueqingFaceUrl = "https://retail-huabei2.oss-cn-beijing.aliyuncs.com/BUSINESS_RISK_DAILY/qa_test/AI/xueqing.jpg?OSSAccessKeyId=LTAILRdMUAwTZdPh&Expires=1903005023&Signature=Hv9x9LsKtFJCGjV6e%2F1RXfuB02s%3D";
    private String xueqingMaskFaceUrl = "https://retail-huabei2.oss-cn-beijing.aliyuncs.com/BUSINESS_RISK_DAILY/qa_test/AI/xueqingMask.jpg?OSSAccessKeyId=LTAILRdMUAwTZdPh&Expires=1903005047&Signature=oBUSxN8rLPxtcj3JDIHnHoOfmgM%3D";
    private String yuFaceUrl = "https://retail-huabei2.oss-cn-beijing.aliyuncs.com/BUSINESS_RISK_DAILY/qa_test/AI/yu_7.jpg?OSSAccessKeyId=LTAILRdMUAwTZdPh&Expires=1903005104&Signature=ASaweFXsYZsmrVRXC2MLUAwqArA%3D";
    private String yuMaskFaceUrl = "https://retail-huabei2.oss-cn-beijing.aliyuncs.com/BUSINESS_RISK_DAILY/qa_test/AI/yuMask.jpg?OSSAccessKeyId=LTAILRdMUAwTZdPh&Expires=1903005085&Signature=GMfI5sVHwhBs2QXNX1whHoMJFp0%3D";
    private String hangFaceUrl = "https://retail-huabei2.oss-cn-beijing.aliyuncs.com/BUSINESS_RISK_DAILY/qa_test/AI/yang_4.jpg?OSSAccessKeyId=LTAILRdMUAwTZdPh&Expires=1903005065&Signature=cv0C8aHoOmWimkWYPRGjua2jwhQ%3D";
    private String hangGoodFaceUrl = "http://retail-huabei2.oss-cn-beijing.aliyuncs.com/BUSINESS_RISK_DAILY/qa_test/soho_staff/%E6%9D%A8%E8%88%AA.jpg?OSSAccessKeyId=LTAIlYpjA39n18Yr&Expires=1587977038&Signature=2ajWe69Wl%2FSUi2PuRnKKzuWv0mU%3D";
    private String hangMaskFaceUrl = "https://retail-huabei2.oss-cn-beijing.aliyuncs.com/BUSINESS_RISK_DAILY/qa_test/AI/hangMask.jpg?OSSAccessKeyId=LTAILRdMUAwTZdPh&Expires=1903004952&Signature=oUof5bUV%2BHBJk%2BAYyW5XW%2BkJCgo%3D";

    private String boundaryDeviceId = "153";
    private String blackDeviced = "150";
    private String NumDeviced = "155";

    public final long VILLAGE_ID = 8;

//    ------------------------------------------------------非创单验证（其他逻辑）-------------------------------------

    //    @Test(dataProvider = "CUSTOMER_REG")
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
            String userId1 = defence.genRandom();
            String name1 = defence.genRandom7();
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
    public void customerRegAfterDelete() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "社区人员注册-删除-再注册-再删除";

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
            String userId1 = defence.genRandom();
            String name1 = "name";
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
            String alarmCustomerId = defence.customerRegBlackUserId(userId1, "level", "label").
                    getJSONObject("data").getString("alarm_customer_id");

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
            String name1 = "name";
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
            String name1 = "name";
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
    public void blackRegNewuser() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "注册社区人员-删除-用new_user注册";

        logger.info("\n\n" + caseName + "\n");

        try {

//            注册社区人员
            String faceUrl1 = defence.kangLinFaceUrlNew;
            String userId1 = defence.genRandom();
            String name1 = "name";
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
            String userId1 = defence.genRandom();
            String name1 = "name";
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
            String userId1 = defence.genRandom();
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
            String userId1 = defence.genRandom();
            String name1 = "name";
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
            String name1 = "name";
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

            String alarmCustomerId = defence.customerRegBlackNewUser(faceUrl1, level, label).getJSONObject("data").
                    getString("alarm_customer_id");

//            再次用该new_user信息注册
            String name1 = defence.genRandom();
            String phone1 = defence.genPhoneNum();
            String type1 = defence.genRandom7();
            String cardKey1 = defence.genRandom();
            String age1 = "20";
            String sex1 = "MALE";
            String address1 = "sex1";
            defence.customerRegBlackNewUser(level, label, faceUrl1, name1, phone1, type1, cardKey1,
                    age1, sex1, address1, StatusCode.BAD_REQUEST);

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

            String userId = defence.genRandom();

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
            String name1 = "name";
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

    @DataProvider(name = "VILLAGE_LIST_NOT_NULL")
    public Object[] villageListNotNull() {
        return new Object[]{
                "[list]-village_id", "[list]-village_name"
        };
    }

    @DataProvider(name = "DEVICE_LIST_NOT_NULL")
    public Object[] deviceListNotNull() {
        return new Object[]{
                "[list]-device_id", "[list]-device_name", "[list]-device_url", "[list]-device_type",
        };
    }

    //    社区人员注册
    @DataProvider(name = "CUSTOMER_REGISTER_NOT_NULL")
    public Object[] customerRegNotNull() {
        return new Object[]{
                "user_id", "customer_id"
        };
    }

    //    注册人员黑名单
    @DataProvider(name = "CUSTOMER_REGISTER_BLACK_NOT_NULL")
    public Object[] customerRegBlackNotNull() {
        return new Object[]{
                "alarm_customer_id"
        };
    }

    //    删除人员黑名单
    @DataProvider(name = "CUSTOMER_DELETE_BLACK_NOT_NULL")
    public Object[] customerDeleteBlackNotNull() {
        return new Object[]{
                "alarm_customer_id"
        };
    }

    //    获取人员黑名单
    @DataProvider(name = "CUSTOMER_BLACK_PAGE_NOT_NULL")
    public Object[] customerBlackPageNotNull() {
        return new Object[]{
                "[list]-user_id", "[list]-face_url", "[list]-level", "[list]-label"
        };
    }

    //    获取设备周界报警配置
    @DataProvider(name = "BOUNDARY_ALARM_INFO_NOT_NULL")
    public Object[] boundaryAlarmInfoNotNull() {
        return new Object[]{
                "[boundary_axis]-x", "[boundary_axis]-y"
        };
    }

    //    告警记录(分页查询)
    @DataProvider(name = "ALARM_LOG_PAGE_NOT_NULL")
    public Object[] alarmLogPageNotNull() {
        return new Object[]{
                "[list]-id", "[list]-alarm_type", "[list]-alarm_desc", "[list]-device_id", "[list]-device_name",
                "[list]-pic_url", "[list]-opt_status", "[list]-opt_result", "[list]-operator", "[list]-opt_timestamp",
                "[list]-level"
        };
    }

    //    人脸识别记录分页查询
    @DataProvider(name = "CUSTOMER_HISTORY_CAPTURE_PAGE_NOT_NULL")
    public Object[] customerHistoryCapturePageNotNull() {
        return new Object[]{
                "[list]-id", "[list]-customer_id", "[list]-timestamp", "[list]-pic_url", "[list]-village_id",
                "[list]-village_name", "[list]-device_id", "[list]-device_name", "[list]-page", "[list]-total"
        };
    }

    //    轨迹查询(人脸搜索)
    @DataProvider(name = "CUSTOMER_FACE_TRACE_LIST_NOT_NULL")
    public Object[] customerFaceTraceListNotNull() {
        return new Object[]{
                "[list]-id", "[list]-customer_id", "[list]-timestamp", "[list]-pic_url", "[list]-village_id",
                "[list]-village_name", "[list]-device_id", "[list]-device_name", "[list]-similarity"
        };
    }

    //    结构化检索(分页查询)
    @DataProvider(name = "CUSTOMER_SEARCH_LIST_NOT_NULL")
    public Object[] customerSearchListNotNull() {
        return new Object[]{
                "[list]-id", "[list]-customer_id", "[list]-pic_url", "[list]-timestamp",
                "[list]-village_id", "[list]-village_name", "[list]-device_id", "[list]-device_name"
        };
    }

    //    人物详情信息
    @DataProvider(name = "CUSTOMER_INFO_NOT_NULL")
    public Object[] customerInfoNotNull() {
        return new Object[]{
                "customer_id"
        };
    }

    //    设备画面播放(实时/历史)
    @DataProvider(name = "DEVICE_STREAM_NOT_NULL")
    public Object[] deviceStreamNotNull() {
        return new Object[]{
                "pull_rtsp_url", "expire_time", "device_status"
        };
    }

    //    客流统计
    @DataProvider(name = "DEVICE_CUSTOMER_FLOW_STATISTIC_NOT_NULL")
    public Object[] deviceCustomerFlowStatisticNotNull() {
        return new Object[]{
                "pv", "device_status", "status_name"
        };
    }

    //    报警统计
    @DataProvider(name = "DEVICE_ALARM_STATISTIC_NOT_NULL")
    public Object[] deviceAlarmStatisticNotNull() {
        return new Object[]{
                "alarm_count", "device_status", "status_name"
        };
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
                },

                new Object[]{
//                        name相同，phone不同，其他均不同
                        "name1phone0", defence.nanhaiFaceUrlNew, defence.genRandom(), "name", defence.genPhoneNum(), "RESIDENT",
                        defence.genRandom(), "20", "MALE", "address", "birthday", StatusCode.SUCCESS
                },

                new Object[]{
//                        name不同，phone相同，其他均不同
                        "name0phone1", defence.nanhaiFaceUrlNew, defence.genRandom(), defence.genRandom7(), "phone", "RESIDENT",
                        defence.genRandom(), "20", "MALE", "address", "birthday", StatusCode.SUCCESS
                },

                new Object[]{
//                        name不同，phone不同，其他均不同
                        "name0phone0", defence.nanhaiFaceUrlNew, defence.genRandom(), defence.genRandom7(), defence.genPhoneNum(), "RESIDENT",
                        defence.genRandom(), "20", "MALE", "address", "birthday", StatusCode.SUCCESS
                },
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
}

