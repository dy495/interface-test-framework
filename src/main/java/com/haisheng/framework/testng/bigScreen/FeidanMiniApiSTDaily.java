package com.haisheng.framework.testng.bigScreen;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.arronlong.httpclientutil.common.HttpConfig;
import com.google.common.base.Preconditions;
import com.haisheng.framework.model.bean.Case;
import com.haisheng.framework.testng.CommonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.*;

import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

/**
 * @author : huachengyu
 * @date :  2019/11/21  14:55
 */

public class FeidanMiniApiSTDaily {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private String failReason = "";
    private String response = "";
    private boolean FAIL = false;
    private Case aCase = new Case();

    Feidan feidan = new Feidan();
    StringUtil stringUtil = new StringUtil();
    DateTimeUtil dateTimeUtil = new DateTimeUtil();
    CheckUtil checkUtil = new CheckUtil();
    private QADbUtil qaDbUtil = new QADbUtil();
    private int APP_ID = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
    private int CONFIG_ID = ChecklistDbInfo.DB_SERVICE_ID_FEIDAN_DAILY_SERVICE;

    private String CI_CMD = "curl -X POST http://qarobot:qarobot@192.168.50.2:8080/job/feidan-daily-test/buildWithParameters?case_name=";

    private String DEBUG = System.getProperty("DEBUG", "true");

    private String authorization = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiLotornp4DmtYvor5XotKblj7ciLCJ1aWQiOiJ1aWRfZWY2ZDJkZTUiLCJsb2dpblRpbWUiOjE1NzQyNDE5NDIxNjV9.lR3Emp8iFv5xMZYryi0Dzp94kmNT47hzk2uQP9DbqUU";

    private HttpConfig config;

    DateTimeUtil dt = new DateTimeUtil();

    String natureCustomer = "NATURE";
    String channelCustomer = "CHANNEL";

    //  -----------------------------------------渠道------------------------------------------
    String wudongChannelIdStr = "5";
    String wudongChannelNameStr = "测试FREEZE";
    int wudongChannelInt = 5;
    String wudongOwnerPhone = "16600000000";


    String maiTianChannelStr = "2";
    String maiTianChannelNameStr = "麦田";
    int maiTianChannelInt = 2;

    String lianjiaChannelStr = "1";
    int lianjiaChannelInt = 1;
    String lianjiaChannelName = "链家";
    String lianjiaOwnerPhone = "16600000001";


    String channel1dayIdStr = "848";
    int channel1dayInt = 848;
    String staffName1day = "呵呵";
    String staffPhone1day = "12398654976";
    int staffId1day = 3378;

//  ------------------------------------------业务员-----------------------------------------------------

    String lianjiaToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiLmtYvor5XjgJDli7_liqjjgJEiLCJ1aWQiOjIxMzYsImxvZ2luVGltZSI6MT" +
            "U3ODk5OTY2NjU3NH0.kQsEw_wGVmPQ4My1p-FNZ556FJC7W177g7jfjFarTu4";
    String lianjiaFreezeStaffIdStr = "2136";
    int lianjiaFreezeStaffIdInt = 2136;
    String lianjiaFreezeStaffName = "链家-FREEZE";
    String lianjiaFreezeStaffPhone = "14112345678";

    String lianjiaStaffIdStr = "2136";
    int lianjiaStaffIdInt = 2136;
    String lianjiaStaffName = "链家业务员";
    String lianjiaStaffPhone = "17711111024";


    String wudongToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiLjgJDli7_li" +
            "qjjgJExIiwidWlkIjoyMDk4LCJsb2dpblRpbWUiOjE1Nzg1NzQ2MjM4NDB9.exDJ6avJKJd3ezQkYc4fmUkHvXaukqfgjThkpoYgnAw";

    String wudongStaffIdStr = "2098";
    int wudongStaffIdInt = 2098;

    String maiTianStaffStr = "2";
    int maiTianStaffInt = 2;
    int maitianStaffIdInt = 2449;
    String maitianStaffIdStr = "2449";
    String maitianStaffName = "喵喵喵";
    String maitianStaffPhone = "14422110039";


//    -------------------------------------------置业顾问-----------------------------------------------------

    String anShengIdStr = "15";
    String anShengName = "安生";
    String anShengPhone = "16622222222";

    String zhangIdStr = "8";
    int zhangIdInt = 8;
    String zhangName = "张钧甯";
    String zhangPhone = "19111311116";

    long wudongReportTime = 1547024264000L;//2019-01-09 16:57:44
    long lianjiaReportTime = 1547014265000L;//2019-01-09 14:11:05
    long noChannelReportTime = 1547034265000L;//2019-01-09 19:44:25

    long firstAppearTime = 1582684439509L;

    String defaultRuleId = "837";
    String ahead1hRuleId = "996";


    String protect1DayRuleId = "840";


    int pageSize = 10000;

//    ------------------------------------------------------非创单验证（其他逻辑）-------------------------------------

    /**
     * 同一业务员报备同一顾客两次（全号）
     */
    @Test
    public void dupReport() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "重复报备";

        logger.info("\n\n" + caseName + "\n");

        try {
            // 报备
            String customerPhone = "14422110014";

            String customerName = caseName + "-" + feidan.getNamePro();
            feidan.customerReportH5(wudongStaffIdStr, customerName, customerPhone, "MALE", wudongToken);
            String report2 = feidan.customerReportH5NoCheckCode(wudongStaffIdStr, customerName, customerPhone, "MALE", wudongToken);
            feidan.checkCode(report2, StatusCode.BAD_REQUEST, "重复报备");

            feidan.checkMessage("重复报备", report2, "报备失败！当前顾客信息已报备完成，请勿重复报备");

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            feidan.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

//    --------------------------------------------规则页报备保护期验证-----------------------------------------------------------------------

    /**
     * 保护渠道报备 -> 其他渠道报备
     */
    @Test
    public void inProtectOthersFail() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "保护渠道报备 -> 保护期内其他渠道报备";

        logger.info("\n\n" + caseName + "\n");

        try {
            // 报备
            String customerPhone = "14422110180";

            String customerName = "麦田FREEZE";

//            其他渠道报备(保护渠道为麦田,麦田报备期为10000天)
            String report2 = feidan.customerReportH5NoCheckCode(wudongStaffIdStr, customerName, customerPhone, "MALE", wudongToken);
            feidan.checkCode(report2, StatusCode.BAD_REQUEST, "保护期内其他渠道报备");

            feidan.checkMessage("报备保护", report2, "报备失败！当前顾客信息处于(麦田)渠道报备保护期内，请勿重复报备");

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            feidan.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    /**
     * 保护渠道报备 -> 顾客现场登记（PC无渠道）
     */
    @Test
    public void inProtectPCF() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "保护渠道报备 -> 保护期内顾客现场登记（PC无渠道）";

        logger.info("\n\n" + caseName + "\n");

        try {
            // 报备
            String customerPhone = "14422110180";

            String customerName = caseName + "-" + feidan.getNamePro();

//            保护渠道报备
            feidan.newCustomer(maiTianChannelInt, maitianStaffName, maitianStaffPhone, zhangName, zhangPhone, customerPhone, customerName, "MALE");

//            现场注册
            feidan.newCustomer(-1, "", "", zhangName, zhangPhone, customerPhone, customerName, "MALE");

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            feidan.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    /**
     * 保护渠道报备 -> 顾客现场登记（自助报备）
     */
    @Test
    public void inProtectSelf() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "保护渠道报备 -> 保护期内顾客现场登记（自助报备）";

        logger.info("\n\n" + caseName + "\n");

        try {
            // 报备
            String customerPhone = "18210113587";
            String selfCode = "805805";

            String customerName = caseName + "-" + feidan.getNamePro();

//            保护渠道报备
            feidan.newCustomer(maiTianChannelInt, maitianStaffName, maitianStaffPhone, zhangName, zhangPhone, customerPhone, customerName, "MALE");

//            顾客现场登记
            feidan.selfRegister(customerName, customerPhone, selfCode, "", "", "MALE");

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            feidan.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    /**
     * 其他渠道报备 -> 保护渠道报备 -> 其他渠道报备
     */
    @Test
    public void othersInProtectOthers() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "其他渠道报备 -> 保护渠道报备 -> 保护期内其他渠道报备";

        logger.info("\n\n" + caseName + "\n");


        try {
            // 报备
            String customerPhone = "14422110014";
            String customerName = caseName + "-" + feidan.getNamePro();

//            勿动报备
            feidan.customerReportH5(wudongStaffIdStr, customerName, customerPhone, "MALE", wudongToken);

//            麦田报备（保护期为10000天）
            feidan.newCustomer(maiTianChannelInt, maitianStaffName, maitianStaffPhone, zhangName, zhangPhone, customerPhone, customerName, "MALE");

//            链家渠道报备
            String report2 = feidan.customerReportH5NoCheckCode(lianjiaStaffIdStr, customerName, customerPhone, "MALE", lianjiaToken);

            feidan.checkCode(report2, StatusCode.BAD_REQUEST, "");

            feidan.checkMessage("报备保护", report2, "报备失败！当前顾客信息处于(麦田)渠道报备保护期内，请勿重复报备");

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            feidan.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    /**
     * 其他渠道报备 -> 保护渠道报备 -> PC(无渠道)
     */
    @Test
    public void othersInProtectPCF() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "其他渠道报备 -> 保护渠道报备 -> 保护期内PC(无渠道)登记";

        logger.info("\n\n" + caseName + "\n");

        try {
            // 报备
            String customerPhone = "14422110014";
            String customerName = caseName + "-" + feidan.getNamePro();

//            其他渠道报备
            feidan.customerReportH5(wudongStaffIdStr, customerName, customerPhone, "MALE", wudongToken);

//            保护渠道报备
            feidan.newCustomer(maiTianChannelInt, maitianStaffName, maitianStaffPhone, zhangName, zhangPhone, customerPhone, customerName, "MALE");

//            现场登记
            feidan.newCustomer(-1, "", "", zhangName, zhangPhone, customerPhone, customerName, "MALE");

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            feidan.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    /**
     * 其他渠道报备 -> 保护渠道报备 -> 自助扫码
     */
    @Test
    public void othersInProtectSELF() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "其他渠道报备 -> 保护渠道报备 -> 保护期内自助扫码";
        logger.info("\n\n" + caseName + "\n");

        try {
            // 报备
            String customerPhone = "18210113587";
            String selfCode = "805805";
            String customerName = caseName + "-" + feidan.getNamePro();

//            其他渠道报备
            feidan.customerReportH5(wudongStaffIdStr, customerName, customerPhone, "MALE", wudongToken);

//            保护渠道报备
            feidan.newCustomer(maiTianChannelInt, maitianStaffName, maitianStaffPhone, zhangName, zhangPhone, customerPhone, customerName, "MALE");

//            自助扫码
            feidan.selfRegister(customerName, customerPhone, selfCode, "", "", "MALE");

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            feidan.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    /**
     * 其他渠道补全手机号为保护渠道报备的顾客手机号
     */
    @Test
    public void InProtectOthersComplete() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "保护渠道报备-保护期内其他渠道补全手机号为保护渠道报备的顾客手机号";

        logger.info("\n\n" + caseName + "\n");

        try {
            // 报备
            String customerPhone = "14422110180";

            String customerName = "麦田FREEZE";

//            补全手机号
            String cid = "REGISTER-39d84b8a-7f75-450b-861a-6edf8024a5d0";//测试FREEZE下的该顾客

            String res = feidan.customerEditPCNoCheckCode(cid, customerName, customerPhone, zhangName, zhangPhone);

            feidan.checkCode(res, StatusCode.BAD_REQUEST, "保护期内其他渠道补全手机号为当前顾客");

            feidan.checkMessage("报备保护", res, "修改顾客信息失败！该手机号已被其他拥有报备保护的渠道报备");

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            feidan.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    /**
     * 其他渠道修改手机号为保护渠道报备的顾客手机号
     */
    @Test
    public void InProtectOthersChange() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "保护渠道报备-保护期内其他渠道修改手机号为保护渠道报备的顾客手机号";

        logger.info("\n\n" + caseName + "\n");

        try {
            // 报备
            String customerPhone = "14422110180";

            String customerName = "麦田FREEZE";

//            更改手机号
            String cid = "REGISTER-739230f9-2485-43b8-974a-7037352c667a";//链家

            String res = feidan.customerEditPCNoCheckCode(cid, customerName, customerPhone, zhangName, zhangPhone);

            feidan.checkCode(res, StatusCode.BAD_REQUEST, "保护期内其他渠道修改手机号为当前顾客");

            feidan.checkMessage("报备保护", res, "修改顾客信息失败！该手机号已被其他拥有报备保护的渠道报备");

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            feidan.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    /**
     * 保护渠道报备 -> 其他渠道报备
     */
    @Test
    public void outProtectOthersSuccess() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        logger.info("\n\n" + caseName + "\n");

        String caseDesc = "保护渠道报备 -> 保护期外其他渠道报备";

        try {
            String customerPhone = "14422110180";

            String customerName = caseName + "-" + feidan.getNamePro();

//            保护渠道报备
            feidan.newCustomer(channel1dayInt, staffName1day, staffPhone1day, zhangName, zhangPhone, customerPhone, customerName, "MALE");

//            修改保护时间
            long protectTime = System.currentTimeMillis() - 25 * 60 * 60 * 1000;
            feidan.updateProtectTime(customerPhone, customerName, channel1dayInt, staffId1day, protectTime);

//            其他渠道报备
            feidan.customerReportH5(wudongStaffIdStr, customerName, customerPhone, "MALE", wudongToken);

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            feidan.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    /**
     * 其他渠道补全手机号为保护渠道报备的顾客手机号
     */
    @Test
    public void OutProtectOthersComplete() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "保护期外其他渠道补全手机号为保护渠道报备的顾客手机号";

        logger.info("\n\n" + caseName + "\n");

        try {

            String customerPhone = "14422110180";
            String customerPhoneHide = "144****0180";
            String customerName = caseName + "-" + feidan.getNamePro();

//            保护渠道报备
            feidan.newCustomer(channel1dayInt, staffName1day, staffPhone1day, zhangName, zhangPhone, customerPhone, customerName, "MALE");

//            修改保护时间
            long protectTime = System.currentTimeMillis() - 25 * 60 * 60 * 1000;
            feidan.updateProtectTime(customerPhone, customerName, channel1dayInt, staffId1day, protectTime);

//            其他渠道报备
            feidan.customerReportH5(wudongStaffIdStr, customerName, customerPhoneHide, "MALE", wudongToken);

//            其他渠道补全
            JSONArray list = feidan.customerList(customerName, wudongChannelIdStr, "", 1, 10).getJSONArray("list");

            String cid = list.getJSONObject(0).getString("cid");

            feidan.customerEditPC(cid, customerName, customerPhone, anShengName, anShengPhone);

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            feidan.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    /**
     * 其他渠道补全手机号为保护渠道报备的顾客手机号
     */
    @Test
    public void OutProtectOthersChange() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "保护期外其他渠道修改手机号为保护渠道报备的顾客手机号";

        logger.info("\n\n" + caseName + "\n");

        try {
            // 报备
            String customerPhone = "14422110014";
            String customerPhoneB = "13422110014";

            String customerName = caseName + "-" + feidan.getNamePro();

//            保护渠道报备
            feidan.newCustomer(channel1dayInt, staffName1day, staffPhone1day, zhangName, zhangPhone, customerPhone, customerName, "MALE");

//            修改保护时间
            long protectTime = System.currentTimeMillis() - 25 * 60 * 60 * 1000;
            feidan.updateProtectTime(customerPhone, customerName, channel1dayInt, staffId1day, protectTime);

//            其他渠道报备
            feidan.customerReportH5(wudongStaffIdStr, customerName, customerPhoneB, "MALE", wudongToken);

//            其他渠道更改手机号
            JSONArray list = feidan.customerList(customerPhoneB, wudongChannelIdStr, "", 1, 10).getJSONArray("list");

            String cid = list.getJSONObject(0).getString("cid");

            feidan.customerEditPC(cid, customerName, customerPhone, anShengName, anShengPhone);

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            feidan.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    /**
     * 保护渠道报备带*手机号
     */
    @Test
    public void protectHidePhone() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "保护渠道报备隐藏手机号";

        logger.info("\n\n" + caseName + "\n");

        try {
            // 报备
            String customerPhone = "144****0014";

            String customerName = caseName + "-" + feidan.getNamePro();

            feidan.channelEdit(wudongChannelIdStr, wudongChannelNameStr, "test", "12301010101", protect1DayRuleId);

//            报备隐藏手机号(勿动)
            feidan.customerReportH5(wudongStaffIdStr, customerName, customerPhone, "MALE", wudongToken);

            feidan.customerReportH5(lianjiaStaffIdStr, customerName, customerPhone, "MALE", lianjiaToken);

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            feidan.channelEditFinally(wudongChannelIdStr, wudongChannelNameStr, "test", "12301010101", defaultRuleId);
            feidan.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    /**
     * 补全手机号时修改客户姓名
     */
    @Test(dataProvider = "H5_REPORT")
    public void CompAndModifyName(String name, String customerPhoneHide, String customerPhone) {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "补全手机号时修改客户姓名";

        logger.info("\n\n" + caseName + "\n");

        try {

            // 报备
            String customerNameB = name;
            String customerNameA = name + "-" + feidan.getNamePro();

//            报备隐藏手机号(勿动)
            feidan.customerReportH5(wudongStaffIdStr, customerNameB, customerPhoneHide, "MALE", wudongToken);

//            补全
            JSONObject customer = feidan.customerListH5(1, 10, wudongToken).getJSONArray("list").getJSONObject(0);
            String cid = customer.getString("cid");
            String customerNameRes = customer.getString("customer_name");
            if (!customerNameB.equals(customerNameRes)) {
                throw new Exception("H5业务员顾客列表中的第一个顾客【" + customerNameRes + "】不是刚报备的顾客【" + customerNameB + "】");
            }

            feidan.customerEditH5(cid, customerNameA, customerPhone, wudongToken);

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            feidan.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    /**
     * 补全后的号码与原号码非*部分不匹配
     */
    @Test
    public void CompchngLast4() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "补全后的号码与原号码非*部分不匹配";

        logger.info("\n\n" + caseName + "\n");

        try {
            // 报备
            String customerPhoneHide = "144****0014";
            String customerPhone = "14422110010";

            String customerNameB = caseName;
            String customerNameA = caseName + "-" + feidan.getNamePro();

//            报备隐藏手机号(勿动)
            feidan.customerReportH5(wudongStaffIdStr, customerNameB, customerPhoneHide, "MALE", wudongToken);

//            补全
            JSONObject customer = feidan.customerListH5(1, 10, wudongToken).getJSONArray("list").getJSONObject(0);
            String cid = customer.getString("cid");
            String customerNameRes = customer.getString("customer_name");
            if (!customerNameB.equals(customerNameRes)) {
                throw new Exception("H5业务员顾客列表中的第一个顾客【" + customerNameRes + "】不是刚报备的顾客【" + customerNameB + "】");
            }

            String editH5NoCode = feidan.customerEditH5NoCode(cid, customerNameA, customerPhone, wudongToken);
            feidan.checkCode(editH5NoCode, StatusCode.BAD_REQUEST, "");

            feidan.checkMessage("H5补全手机号", editH5NoCode, "补全号码时必须保证前三后四相同");

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            feidan.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    @Test(dataProvider = "INVALID_NUM_AHEAD")
    public void ruleAheadInvalidNumber(String number) {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "提前报备时间为【" + number + "】";

        logger.info("\n\n" + caseName + "\n");

        try {

            String addRiskRule = feidan.addRiskRuleNoCheckCode("test", number, "10");

            feidan.checkCode(addRiskRule, StatusCode.BAD_REQUEST, "提前报备时间为【" + number + "】");

            feidan.checkMessage("新建风控规则", addRiskRule, "提前报备时间不能超过半年");

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            feidan.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    @Test(dataProvider = "VALID_NUM_AHEAD")
    public void ruleAheadValidNumber(int number) {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "提前报备时间为【" + number + "】";

        logger.info("\n\n" + caseName + "\n");

        try {

            String ruleName = feidan.getNamePro();

            feidan.addRiskRule(ruleName, number + "", "10");

            JSONObject data = feidan.riskRuleList();

            JSONArray list = data.getJSONArray("list");

            JSONObject ruleData = list.getJSONObject(list.size() - 1);

            String name = ruleData.getString("name");
            if (!ruleName.equals(name)) {
                throw new Exception("期待最后一条规则为【" + ruleName + "】，实际为【" + name + "】");
            }

            String id = ruleData.getString("id");

            feidan.deleteRiskRule(id);

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            feidan.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    @Test(dataProvider = "INVALID_NUM_PROTECT")
    public void ruleProtectInvalidNum(String number) {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "报备保护期为【" + number + "】";

        logger.info("\n\n" + caseName + "\n");

        try {

            String addRiskRule = feidan.addRiskRuleNoCheckCode("test", "60", number);

            feidan.checkCode(addRiskRule, StatusCode.BAD_REQUEST, "");

            feidan.checkMessage("新建风控规则", addRiskRule, "报备保护期不能超过10000天");

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            feidan.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    @Test(dataProvider = "VALID_NUM_PROTECT")
    public void ruleProtectValidNumber(int number) {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "报备保护期为【" + number + "】";

        logger.info("\n\n" + caseName + "\n");

        try {

            String ruleName = feidan.getNamePro();

            feidan.addRiskRule(ruleName, number + "", "10");

            JSONObject data = feidan.riskRuleList();

            JSONArray list = data.getJSONArray("list");

            JSONObject ruleData = list.getJSONObject(list.size() - 1);

            String name = ruleData.getString("name");
            if (!ruleName.equals(name)) {
                throw new Exception("期待最后一条规则为【" + ruleName + "】，实际为【" + name + "】");
            }

            String id = ruleData.getString("id");

            feidan.deleteRiskRule(id);

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            feidan.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    @Test(dataProvider = "INVALID_NAME")
    public void ruleNameInvalid(String name) {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "新建风控规则名称为【" + name + "】";

        logger.info("\n\n" + caseName + "\n");

        try {

            String addRiskRule = feidan.addRiskRuleNoCheckCode(name, "60", "60");

            feidan.checkCode(addRiskRule, StatusCode.BAD_REQUEST, "新建风控规则");

            if ("".equals(name.trim())) {
                feidan.checkMessage("新建风控规则", addRiskRule, "规则名称不可为空");
            } else if ("默认规则".equals(name)) {
                feidan.checkMessage("新建风控规则", addRiskRule, "规则名称不允许重复");
            } else {
                feidan.checkMessage("新建风控规则", addRiskRule, "规则名称最长为20个字符");
            }

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            feidan.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    @Test(dataProvider = "VALID_NAME")
    public void ruleNameValid(String name) {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "新建风控规则名称为【" + name + "】";

        logger.info("\n\n" + caseName + "\n");

        try {
            feidan.addRiskRule(name, "60", "60");

            JSONObject data = feidan.riskRuleList();

            JSONArray list = data.getJSONArray("list");

            JSONObject ruleData = list.getJSONObject(list.size() - 1);

            String ruleName = ruleData.getString("name");
            if (!ruleName.equals(name)) {
                throw new Exception("期待最后一条规则为【" + ruleName + "】，实际为【" + name + "】");
            }

            String id = ruleData.getString("id");

            feidan.deleteRiskRule(id);
        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            feidan.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    @Test(dataProvider = "INVALID_DELETE_RULE")
    public void romoveRuleInvalid(String id) {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "非法删除规则";

        logger.info("\n\n" + caseName + "\n");

        try {

            String s = feidan.deleteRiskRuleNoCheckCode(id);

            feidan.checkCode(s, StatusCode.BAD_REQUEST, caseDesc);

            if (defaultRuleId.equals(id)) {
                feidan.checkMessage("新建风控规则", s, "只允许删除自定义规则");
            } else {
                feidan.checkMessage("新建风控规则", s, "规则已被渠道引用, 不可删除");
            }

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            feidan.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

//    ----------------------------------------------新建顾客验证---------------------------------------------

    @Test(dataProvider = "NEW_CUSTOMER_BAD")
    public void newCustomerBad(String message, int channelId, String channelStaffName, String channelStaffPhone, String adviserName,
                               String adviserPhone, String phone, String customerName, String gender) {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "新建顾客-单个新建";

        logger.info("\n\n" + caseName + "\n");

        try {

            String s = feidan.newCustomerNoCheckCode(channelId, channelStaffName, channelStaffPhone, adviserName, adviserPhone, phone, customerName, gender);
            feidan.checkCode(s, StatusCode.BAD_REQUEST, message);

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            feidan.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    @Test
    public void newCustomerNewStaff() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "新建顾客-不存在的业务员和置业顾问";

        logger.info("\n\n" + caseName + "\n");

        try {

            String channelStaffName = feidan.getNamePro();
            String channelStaffPhone = feidan.genPhoneNum();

            String adviserName = feidan.getNamePro();
            String adviserPhone = feidan.genPhoneNum();

            String customerName = feidan.getNamePro();
            String customerPhone = feidan.genPhoneNum();

            feidan.newCustomer(lianjiaChannelInt, channelStaffName, channelStaffPhone, adviserName, adviserPhone, customerPhone, customerName, "MALE");

            JSONObject channelStaff = feidan.channelStaffList(channelStaffName, lianjiaChannelStr, 1, 10).getJSONArray("list").getJSONObject(0);

            String phoneRes = channelStaff.getString("phone");
            if (!channelStaffPhone.equals(phoneRes)) {
                throw new Exception("没有找到业务员【" + channelStaffName + "】");
            }

            JSONObject staff = feidan.adviserList(adviserName, 1, 10).getJSONArray("list").getJSONObject(0);

            String adviserPhoneRes = staff.getString("phone");
            if (!adviserPhone.equals(adviserPhoneRes)) {
                throw new Exception("没有找到置业顾问【" + adviserName + "】");
            }

            String id = staff.getString("id");

            feidan.adviserDelete(id);

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            feidan.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    @Test
    public void newCustomerXML() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "新建顾客-xml文件";

        logger.info("\n\n" + caseName + "\n");

        try {

            String dirPath = "src/main/java/com/haisheng/framework/testng/bigScreen/newCustomerFile";
            dirPath = dirPath.replace("/", File.separator);
            File file = new File(dirPath);
            File[] files = file.listFiles();

            String xmlPath = "";

            for (int i = 0; i < files.length; i++) {
                xmlPath = dirPath + File.separator + files[i].getName();

                String res1 = feidan.importFile(xmlPath);

                feidan.checkCode(res1, StatusCode.BAD_REQUEST, files[i].getName() + ">>>");
            }

            xmlPath = "src/main/java/com/haisheng/framework/testng/bigScreen/Feidan.java";
            String res = feidan.importFile(xmlPath);
            feidan.checkCode(res, StatusCode.BAD_REQUEST, "上传java文件");
            feidan.checkMessage("上传java文件", res, "暂不支持当前文件格式");

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            feidan.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    @Test
    public void visitor2Staff() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "到访人物转员工";

        logger.info("\n\n" + caseName + "\n");

        try {
            String customerType = "NEW";
            String deviceId = "6878238614619136";
            String startTime = LocalDate.now().minusDays(7).toString();
            String endTime = LocalDate.now().toString();

            JSONArray newB = feidan.catchList(customerType, deviceId, startTime, endTime, 1, 1).getJSONArray("list");

            String customerId = "";
            if (newB.size() > 0) {
                JSONObject onePerson = newB.getJSONObject(0);
                customerId = onePerson.getString("customer_id");

                feidan.visitor2Staff(customerId);

                Integer pages = feidan.catchList(customerType, deviceId, startTime, endTime, 1, 1).getInteger("pages");

                for (int i = 1; i <= pages; i++) {
                    JSONObject newA = feidan.catchList(customerType, deviceId, startTime, endTime, i, 50);
                    JSONArray list = newA.getJSONArray("list");
                    for (int j = 0; j < list.size(); j++) {
                        JSONObject single = list.getJSONObject(j);
                        if (customerId.equals(single.getString("customer_id"))) {
                            throw new Exception("转员工失败，有部分同一customerId的访客未转成员工。customerId=" + customerId);
                        }
                    }
                }

                pages = feidan.catchList("STAFF", deviceId, startTime, endTime, 1, 1).getInteger("pages");
                boolean isExist = false;
                for (int i = 1; i < pages; i++) {

                    JSONObject staff = feidan.catchList("STAFF", deviceId, startTime, endTime, i, 50);
                    JSONArray list = staff.getJSONArray("list");
                    for (int j = 0; j < list.size(); j++) {
                        JSONObject single = list.getJSONObject(j);
                        if (customerId.equals(single.getString("customer_id"))) {
                            isExist = true;
                            break;
                        }
                    }
                }

                if (!isExist) {
                    throw new Exception("转员工失败，员工列表中不存在该顾客，customerId=" + customerId);
                }
            }

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            feidan.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

//    -------------------------------------------置业顾问-------------------------------------------------

    @Test
    public void advisernewPicEditNoPic() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "新建顾问（有头像）-编辑顾问（无头像）-删除顾问";

        logger.info("\n\n" + caseName + "\n");

        try {

            String dirPath = "src/main/java/com/haisheng/framework/testng/bigScreen/feidanImages";

            String imagePath = dirPath + "/" + "Cris.jpg";
            imagePath = imagePath.replace("/", File.separator);
            JSONObject uploadImage = feidan.uploadImage(imagePath, "shopStaff");
            String phoneNum = feidan.genPhoneNum();
            String staffName = feidan.getNamePro();

//            新建置业顾问
            feidan.addAdviser(staffName, phoneNum, uploadImage.getString("face_url"));

//            查询列表
            feidan.checkAdviserList(staffName, phoneNum, true);

//            编辑
            JSONObject staff = feidan.adviserList(phoneNum, 1, 1).getJSONArray("list").getJSONObject(0);
            String id = staff.getString("id");

            staffName = feidan.getNamePro();
            phoneNum = feidan.genPhoneNum();

            feidan.adviserEdit(id, staffName, phoneNum, "");

//            查询
            feidan.checkAdviserList(staffName, phoneNum, false);

//            删除
            feidan.adviserDelete(id);
        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            feidan.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    @Test
    public void adviserNewNoPicEditPic() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "新建顾问（无头像）-编辑顾问（有头像）-删除顾问";

        logger.info("\n\n" + caseName + "\n");

        try {

            String phoneNum = feidan.genPhoneNum();
            String staffName = feidan.getNamePro();

//            新建置业顾问
            feidan.addAdviser(staffName, phoneNum, "");

//            查询列表
            feidan.checkAdviserList(staffName, phoneNum, false);

//            编辑
            String dirPath = "src/main/java/com/haisheng/framework/testng/bigScreen/feidanImages";

            String imagePath = dirPath + "/" + "Cris.jpg";
            imagePath = imagePath.replace("/", File.separator);
            JSONObject uploadImage = feidan.uploadImage(imagePath, "shopStaff");

            JSONObject staff = feidan.adviserList(phoneNum, 1, 1).getJSONArray("list").getJSONObject(0);
            String id = staff.getString("id");

            staffName = feidan.getNamePro();
            phoneNum = feidan.genPhoneNum();

            feidan.adviserEdit(id, staffName, phoneNum, uploadImage.getString("face_url"));

//            查询
            feidan.checkAdviserList(staffName, phoneNum, true);

//            删除
            feidan.adviserDelete(id);
        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            feidan.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }


//    ---------------------------------------------渠道相关---------------------------------------------

    @Test
    public void channelCheck() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "新建渠道-列表-编辑-列表";

        logger.info("\n\n" + caseName + "\n");

        try {

            String phoneNum = feidan.genPhoneNum();
            String phoneNumNew = feidan.genPhoneNum();
            String channelName = feidan.getNamePro();
            String channelNameNew = feidan.getNamePro();

            String owner = "owner";

//            新建渠道
            feidan.addChannel(channelName, owner, phoneNum, defaultRuleId);

//            渠道列表
            JSONArray channelList = feidan.channelList(1, 10).getJSONArray("list");
            boolean isExist = false;
            boolean isExistA = false;
            for (int i = 0; i < channelList.size(); i++) {
                JSONObject single = channelList.getJSONObject(i);
                if (phoneNum.equals(single.getString("phone"))) {
                    isExist = true;

                    checkUtil.checkKeyValue("渠道列表", single, "channel_name", channelName, true);
                    checkUtil.checkKeyValue("渠道列表", single, "register_time", "register_time", false);
                    checkUtil.checkKeyValue("渠道列表", single, "rule_name", "默认规则", true);
                    checkUtil.checkKeyValue("渠道列表", single, "owner_principal", owner, true);
                    checkUtil.checkKeyValue("渠道列表", single, "total_customers", "", false);


                    String channelId = single.getString("channel_id");

                    feidan.channelEdit(channelId, channelNameNew, owner, phoneNumNew, ahead1hRuleId);

                    JSONArray channelListA = feidan.channelList(1, 10).getJSONArray("list");
                    for (int j = 0; j < channelListA.size(); j++) {

                        JSONObject singleA = channelListA.getJSONObject(j);
                        if (channelId.equals(singleA.getString("channel_id"))) {
                            isExistA = true;

                            checkUtil.checkKeyValue("渠道列表", singleA, "channel_name", channelNameNew, true);
                            checkUtil.checkKeyValue("渠道列表", singleA, "register_time", "register_time", false);
                            checkUtil.checkKeyValue("渠道列表", singleA, "rule_name", "ahead60min", true);
                            checkUtil.checkKeyValue("渠道列表", singleA, "owner_principal", owner, true);
                            checkUtil.checkKeyValue("渠道列表", singleA, "phone", phoneNumNew, true);
                            checkUtil.checkKeyValue("渠道列表", singleA, "total_customers", "", false);
                            break;
                        }
                    }
                    break;
                }
            }

            if (!isExist) {
                throw new Exception("新建后渠道列表中没有该渠道，渠道名称为【" + channelName + "】");
            }

            if (!isExistA) {
                throw new Exception("编辑后渠道列表中没有该渠道，渠道名称为【" + channelNameNew + "】");
            }

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            feidan.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    @Test
    public void channelStaffCheck() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;
        String caseDesc = "新建业务员-列表-查询-列表";

        try {

            String phoneNum = feidan.genPhoneNum();
            String staffName = feidan.getNamePro();

//            新建业务员
            feidan.addChannelStaff(wudongChannelIdStr, staffName, phoneNum);

//            查询列表
            feidan.checkChannelStaffList(wudongChannelIdStr, staffName, phoneNum, false);

//            编辑
            String dirPath = "src/main/java/com/haisheng/framework/testng/bigScreen/feidanImages";

            String imagePath = dirPath + "/" + "Cris.jpg";
            imagePath = imagePath.replace("/", File.separator);
            JSONObject uploadImage = feidan.uploadImage(imagePath, "channelStaff");

            JSONObject staff = feidan.channelStaffList(wudongChannelIdStr, 1, 10).getJSONArray("list").getJSONObject(0);
            String id = staff.getString("id");

            staffName = feidan.getNamePro();
            phoneNum = feidan.genPhoneNum();

            feidan.editChannelStaff(id, wudongChannelIdStr, staffName, phoneNum, uploadImage.getString("face_url"));

//            查询
            feidan.checkChannelStaffList(wudongChannelIdStr, staffName, phoneNum, true);

//            删除
            feidan.changeChannelStaffState(id);

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);

        } finally {
            feidan.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    @Test(dataProvider = "BAD_CHANNEL_STAFF")
    public void chanStaffSamePhoneToStaff(String caseDesc, String phoneNum, String message) {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        try {

            String staffName = feidan.getNamePro();

//            新建业务员
            String res = feidan.addChannelStaffNoCode(wudongChannelIdStr, staffName, phoneNum);
            feidan.checkCode(res, StatusCode.BAD_REQUEST, caseDesc);
            feidan.checkMessage(caseDesc, res, message);

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);

        } finally {
            feidan.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    @Test
    public void samePhoneOtherChanDisable() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "新建业务员（与其他渠道的已禁用业务员手机号相同）";

        try {

            String staffName = feidan.getNamePro();

//            新建业务员(渠道一)
            String phoneNum = feidan.genPhoneNum();
            feidan.addChannelStaff(wudongChannelIdStr, staffName, phoneNum);

//            禁用
            JSONArray channelStaffList = feidan.channelStaffList(phoneNum, wudongChannelIdStr, 1, 10).getJSONArray("list");
            String id = channelStaffList.getJSONObject(0).getString("id");

            feidan.changeState(id);

//            新建业务员（渠道二）
            feidan.addChannelStaff(lianjiaChannelStr, staffName, phoneNum);

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);

        } finally {
            feidan.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    @Test
    public void disableThenReport() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "禁用业务员后，PC报备时选该业务员";

        try {

//            PC报备(选择该被禁用业务员)
            String newCustomer = feidan.newCustomerNoCheckCode(wudongChannelInt, "禁用FREEZE", "17794123828",
                    zhangName, zhangPhone, feidan.genPhoneNum(), feidan.getNamePro(), "MALE");

            feidan.checkCode(newCustomer, StatusCode.BAD_REQUEST, caseDesc);

            feidan.checkMessage(caseDesc, newCustomer, "当前手机号17794123828在本渠道被禁用，请先启用、修改业务员信息即可");
        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);

        } finally {
            feidan.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    @Test
    public void initAnatherSamePhoneStaff() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "启用其他渠道已启用的业务员";

        try {

            String id = "80";//测试FREEZE下的启用FREEZE
            String res = feidan.changeStateNocode(id);

            feidan.checkCode(res, StatusCode.BAD_REQUEST, caseDesc);

            feidan.checkMessage(caseDesc, res, "该手机号18210113588当前使用者为渠道'链家'的链家-苏菲玛索,请先修改其手机号或删除/禁用其账号后，再启用此员工");

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            feidan.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    @Test(dataProvider = "BAD_ADVISER")
    public void adviserSamePhoneToChanStaff(String caseDesc, String phoneNum, String message) {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        try {

            String staffName = feidan.getNamePro();

//            新建置业顾问
            String addStaff = feidan.addAdviserNoCode(staffName, phoneNum, "");

            feidan.checkCode(addStaff, StatusCode.BAD_REQUEST, caseDesc);

            feidan.checkMessage(caseDesc, addStaff, message);
        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);

        } finally {
            feidan.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    @Test
    public void witnessUploadOcr() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;
        String caseDesc = "刷新验证码-确认登录-上传-刷新-用刷新之前的验证码登录-用刷新后的验证码登录-用刷新后的验证码登录";

        try {

            //        获取正确验证码
            String refreshCode = feidan.refreshQrcodeNoCheckCode();
            feidan.checkCode(refreshCode, StatusCode.SUCCESS, "刷新OCR验证码");

            String codeB = JSON.parseObject(refreshCode).getJSONObject("data").getString("code");

//        确认
            String confirmCode = feidan.confirmQrcodeNoCheckCode(codeB);

            feidan.checkCode(confirmCode, StatusCode.SUCCESS, "确认验证码");

            String token = JSON.parseObject(confirmCode).getJSONObject("data").getString("token");

            long time = System.currentTimeMillis();

//        上传身份信息
            String idCardPath = "src/main/java/com/haisheng/framework/testng/bigScreen/checkOrderFile/idCard.jpg";
            idCardPath = idCardPath.replace("/", File.separator);
            String facePath = "src/main/java/com/haisheng/framework/testng/bigScreen/checkOrderFile/share.jpg";
            facePath = facePath.replace("/", File.separator);

            ImageUtil imageUtil = new ImageUtil();
            String imageBinary = imageUtil.getImageBinary(idCardPath);
            imageBinary = stringUtil.trimStr(imageBinary);
            String faceBinary = imageUtil.getImageBinary(facePath);
            faceBinary = stringUtil.trimStr(faceBinary);

            String ocrPicUpload = feidan.ocrPicUpload(token, imageBinary, faceBinary);
            feidan.checkCode(ocrPicUpload, StatusCode.SUCCESS, "案场OCR上传证件");

//        刷新
            refreshCode = feidan.refreshQrcodeNoCheckCode();
            feidan.checkCode(refreshCode, StatusCode.SUCCESS, "刷新OCR验证码");

            String codeA = JSON.parseObject(refreshCode).getJSONObject("data").getString("code");

//        原code确认
            String confirm = feidan.confirmQrcodeNoCheckCode(codeB);
            feidan.checkCode(confirm, StatusCode.BAD_REQUEST, "OCR确认-刷新之前的");

//        现code确认
            confirm = feidan.confirmQrcodeNoCheckCode(codeA);
            feidan.checkCode(confirm, StatusCode.SUCCESS, "OCR确认-刷新之后的");

//            现code再次确认
            confirm = feidan.confirmQrcodeNoCheckCode(codeA);
            feidan.checkCode(confirm, StatusCode.SUCCESS, "再次OCR确认-刷新之后的");

            JSONArray list = feidan.orderList("廖祥茹", 10).getJSONArray("list");
            if (list.size() > 1) {
                throw new Exception("OCR刷证后，刷证环节没有出现在原有订单中!customerName=廖祥茹");
            } else if (list.size() == 0) {
                throw new Exception("不存在该顾客的订单!customerName=廖祥茹");
            } else {
                JSONObject order = list.getJSONObject(0);
                String orderId = order.getString("order_id");

                boolean isExist = false;

                JSONArray linkList = feidan.orderLinkList(orderId).getJSONArray("list");
                for (int i = 0; i < linkList.size(); i++) {
                    JSONObject single = linkList.getJSONObject(i);
                    String linkKey = single.getString("link_key");
                    if ("WITNESS_RESULT".equals(linkKey)) {
                        long linkTime = single.getLongValue("link_time");
                        if (linkTime > time) {
                            isExist = true;
                            break;
                        }
                    }
                }

                if (!isExist) {
                    throw new Exception("OCR刷证后订单中没有该刷证环节!customerName=廖祥茹");
                }
            }
        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            feidan.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    @Test(dataProvider = "ORDER_LIST_CHECK")
    public void orderListCheck(String channelId, String status, String isAudited, String namePhone, int pageSize) {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;
        String caseDesc = "";

        try {

            JSONArray list = feidan.orderList(channelId, status, isAudited, namePhone, 10).getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                JSONObject single = list.getJSONObject(i);

                String channelName = single.getString("channel_name");
                if ("1".equals(channelId)) {
                    if (!"链家".equals(channelName)) {
                        throw new Exception("搜索条件为渠道=链家时，搜索结果中出现【" + channelName + "】渠道的订单");
                    }
                } else if ("5".equals(channelId)) {
                    if (!"测试FREEZE".equals(channelName)) {
                        throw new Exception("搜索条件为渠道=测试FREEZE时，搜索结果中出现【" + channelName + "】渠道的订单");
                    }
                }

                String statusRes = single.getString("status");
                if (!"".equals(status)) {
                    if (!status.equals(statusRes)) {
                        throw new Exception("搜索条件为status=" + status + "时，搜索结果中出现status=" + statusRes + "的订单");
                    }
                }

                String isAuditedRes = single.getString("is_audited");
                if (!"".equals(isAudited)) {
                    if (!isAudited.equals(isAuditedRes)) {
                        throw new Exception("搜索条件为is_audited=" + isAudited + "时，搜索结果中出现is_audited=" + isAuditedRes + "的订单");
                    }
                }

                String customerNameRes = single.getString("customer_name");
                if (!"".equals(namePhone)) {
                    if (!customerNameRes.contains(namePhone)) {
                        throw new Exception("搜索条件为namePhone=" + namePhone + "时，搜索结果中出现namePhone=" + customerNameRes + "的订单");
                    }
                }
            }

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            feidan.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    @Test
    public void activityBadName() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;
        String caseDesc = "新建活动-活动名称为特殊字符";

        try {

            String name = "~!@#$%^&*()_+~！@#￥%……&*（）——+·";
            String type = "OTHER";
            String contrastS = "2020-03-04";
            String contrastE = "2020-03-04";
            String start = "2020-03-13";
            String end = "2020-03-13";
            String influenceS = "2020-03-20";
            String influenceE = "2020-03-20";
            String s = feidan.addActivity(name, type, contrastS, contrastE, start, end, influenceS, influenceE);
            feidan.checkCode(s, StatusCode.BAD_REQUEST, caseDesc);
            feidan.checkMessage(caseDesc, s, "参数校验未通过：活动名称不允许使用特殊字符");
        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            feidan.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    @Test
    public void activityCheck() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;
        String caseDesc = "新建活动-删除活动";

        try {

            String name = caseName;
            String type = "OTHER";
            String contrastS = "2020-03-04";
            String contrastE = "2020-03-04";
            String start = "2020-03-13";
            String end = "2020-03-13";
            String influenceS = "2020-03-20";
            String influenceE = "2020-03-20";

//            新建活动
            String s = feidan.addActivity(name, type, contrastS, contrastE, start, end, influenceS, influenceE);
            feidan.checkCode(s, StatusCode.SUCCESS, caseDesc);

//            活动列表
            JSONArray list = feidan.listActivity(name, 1, 1).getJSONArray("list");
            String id = list.getJSONObject(0).getString("id");

//            删除活动
            feidan.deleteActivity(id);

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            feidan.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    @Test
    public void deviceCheck() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;
        String caseDesc = "异常设备信息标红";

        try {

            feidan.checkDevice();

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            feidan.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    @Test
    public void visitorSearchType() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;
        String caseDesc = "到访人物查询-查询条件是顾客身份";

        try {

            JSONObject typeList = feidan.personTypeList();

            JSONArray list = typeList.getJSONArray("list");

            for (int i = 0; i < list.size(); i++) {
                JSONObject single = list.getJSONObject(i);
                String personType = single.getString("person_type");

                JSONArray catchList = feidan.catchList(personType, "", "", "", 1, 21).getJSONArray("list");

                for (int j = 0; j < catchList.size(); j++) {
                    JSONObject single1 = catchList.getJSONObject(j);
                    String personType1 = single1.getString("person_type");
                    Preconditions.checkArgument(personType.equals(personType1), "到访人物列表，搜索条件是person_type=" + personType +
                            "，搜索结果中出现person_type=" + personType1 + "的顾客");
                }
            }
        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            feidan.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    @Test
    public void visitorSearchDevice() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;
        String caseDesc = "到访人物查询-查询条件是摄像头名称";

        try {

            JSONArray devices = feidan.fetchDeviceList().getJSONArray("list");

            for (int i = 0; i < devices.size(); i++) {
                JSONObject single = devices.getJSONObject(i);
                String deviceId = single.getString("device_id");
                String deviceName = single.getString("device_name");

                JSONArray catchList = feidan.catchList("", deviceId, "", "", 1, 21).getJSONArray("list");

                for (int j = 0; j < catchList.size(); j++) {
                    JSONObject single1 = catchList.getJSONObject(j);
                    String cameraName = single1.getString("camera_name");
                    Preconditions.checkArgument(deviceName.equals(cameraName), "到访人物列表，搜索条件是camera_name=" + deviceName +
                            "，搜索结果中出现camera_name=" + cameraName + "的顾客");
                }
            }
        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            feidan.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    @Test(dataProvider = "CATCH_DATE")
    public void visitorSearchDate(String start, String end) {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;
        String caseDesc = "到访人物查询-查询条件是日期";

        try {

            JSONArray catchList = feidan.catchList("", "", start, end, 1, 21).getJSONArray("list");

            if (start.equals(end)) {

                SimpleDateFormat df1 = new SimpleDateFormat("HH:mm");
                if (df1.format(new Date()).compareTo("08:00") < 0) {
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Preconditions.checkArgument(catchList.size() == 0, "查询时间=" + df.format(new Date()) + "，列表不为空");
                }
            } else {

                for (int i = 0; i < catchList.size(); i++) {
                    JSONObject single = catchList.getJSONObject(i);

                    String shootTime = dateTimeUtil.timestampToDate("yyyy-MM-dd", single.getLongValue("shoot_time"));
                    if (shootTime.compareTo(start) < 0 || shootTime.compareTo(end) > 0) {
                        throw new Exception("查询时间是startTime=" + start + "，endTime=" + end + "，查询结果中出现" + shootTime + "到访的顾客");
                    }

                }

            }
        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            feidan.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }


    @Test
    public void selfRegHot() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;
        String caseDesc = "自主注册-购房需求多选、不选";

        try {

            String customerPhone = "18210113587";
            String smsCode = "805805";

            for (int i = 0; i < 4; i++) {
                String customerName = caseName + "-" + feidan.getNamePro();
                feidan.selfRegisterHot(customerName, customerPhone, smsCode, anShengIdStr, i, "MALE");
            }

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            feidan.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

//    下一期迭代再改

    @Test
    public void reSelf() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;
        String caseDesc = "重复自主注册-重复PC无渠道登记";

        try {

            String customerName = "登记FREEZE";
            String customerPhone = "18210113587";
            String smsCode = "805805";

            String res = feidan.selfRegisterHotNoCode(customerName, customerPhone, smsCode, anShengIdStr, 0, "MALE");
            feidan.checkCode(res, StatusCode.BAD_REQUEST, "自主注册-再次注册");
            feidan.checkMessage("自主注册-再次注册", res, "登记失败！当前顾客信息已登记完成，请勿重复登记");

            String res1 = feidan.newCustomerNoCheckCode(-1, "", "", "", "", customerPhone, customerName, "MALE");
            feidan.checkCode(res1, StatusCode.BAD_REQUEST, "自主注册-PC登记（无渠道）");
            feidan.checkMessage("自主注册-PC无渠道登记", res1, "登记失败！当前顾客信息已登记完成，请勿重复登记");

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            feidan.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    /**
     * 获取登录信息 如果上述初始化方法（initHttpConfig）使用的authorization 过期，请先调用此方法获取
     *
     * @ 异常
     */
    @BeforeClass
    public void login() {
        feidan.login();
    }

    @AfterClass
    public void clean() {
        feidan.clean();
    }

    @BeforeMethod
    public void initialVars() {
        failReason = "";
        response = "";
        aCase = new Case();
    }

    @DataProvider(name = "RULE_ID")
    public Object[] ruleId() {
        return new Object[]{
                "0min", "60min", "1day", "7day", "30day", "max"
        };
    }

    @DataProvider(name = "INVALID_NUM_AHEAD")
    public Object[] invalidNumAhead() {
        return new Object[]{
                "260001"
        };
    }

    @DataProvider(name = "INVALID_NUM_PROTECT")
    public Object[] invalidNumProtect() {
        return new Object[]{
                "10001"
        };
    }

    @DataProvider(name = "VALID_NUM_AHEAD")
    public Object[] validNumAhead() {
        return new Object[]{
                0, 1, 60, 1440, 10080, 43200, 10000
        };
    }

    @DataProvider(name = "VALID_NUM_PROTECT")
    public Object[] validNumProtect() {
        return new Object[]{
                0, 1, 60, 1440, 10080, 43200, 10001, 260000
        };
    }

    @DataProvider(name = "INVALID_NAME")
    public Object[] invalidName() {
        return new Object[]{
                "qwer@tyui&opas.dfgh#？",
                "qwer tyui opas dfg  h",
                "默认规则"
        };
    }

    @DataProvider(name = "VALID_NAME")
    public Object[] validName() {
        return new Object[]{
                "正常一点-飞单V3.0",
        };
    }

    @DataProvider(name = "INVALID_DELETE_RULE")
    public Object[] invalidDeleteRule() {
        return new Object[]{
                defaultRuleId,
                protect1DayRuleId
        };
    }

    @DataProvider(name = "NEW_CUSTOMER_BAD")
    public Object[][] newCUstomerBad() {
        return new Object[][]{
//channelId, channelStaffName, channelStaffPhone, adviserName, adviserPhone, customerPhone, customerName, "MALE"
                new Object[]{
                        "顾客姓名为空，", lianjiaChannelInt, lianjiaStaffName, lianjiaStaffPhone, zhangName, zhangPhone, "12300000001", "", "MALE"
                },
                new Object[]{
                        "顾客手机号为空，", lianjiaChannelInt, lianjiaStaffName, lianjiaStaffPhone, zhangName, zhangPhone, "", "name", "MALE"
                },
                new Object[]{
                        "顾客性别为空，", lianjiaChannelInt, lianjiaStaffName, lianjiaStaffPhone, zhangName, zhangPhone, "12300000001", "name", ""
                },
                new Object[]{
                        "置业顾问手机号为空，", lianjiaChannelInt, lianjiaStaffName, lianjiaStaffPhone, zhangName, "", "12300000001", "name", "MALE"
                },
                new Object[]{
                        "置业顾问隐藏手机号，", lianjiaChannelInt, lianjiaStaffName, lianjiaStaffPhone, zhangName, "123****0000", "12300000001", "name", "MALE"
                },
                new Object[]{
                        "置业顾问手机号存在，姓名不同，", lianjiaChannelInt, lianjiaStaffName, lianjiaStaffPhone, "name", zhangPhone, "12300000001", "name", "MALE"
                },
                new Object[]{
                        "业务员手机号为空，", lianjiaChannelInt, lianjiaStaffName, "", zhangName, zhangPhone, "12300000001", "name", "MALE"
                },
                new Object[]{
                        "业务员隐藏手机号，", lianjiaChannelInt, lianjiaStaffName, "123****1111", zhangName, zhangPhone, "12300000001", "name", "MALE"
                },
                new Object[]{
                        "业务员姓名为空，", lianjiaChannelInt, "", lianjiaStaffPhone, zhangName, zhangPhone, "12300000001", "name", "MALE"
                },
                new Object[]{
                        "业务员手机号存在，姓名不同，", lianjiaChannelInt, "name", lianjiaStaffPhone, zhangName, zhangPhone, "12300000001", "name", "MALE"
                },
                new Object[]{
                        "有渠道，无业务员信息，", lianjiaChannelInt, "", "", zhangName, zhangPhone, "12300000001", "name", "MALE"
                },
        };
    }

    @DataProvider(name = "H5_REPORT")
    public Object[][] report() {
        return new Object[][]{
//channelId, channelStaffName, channelStaffPhone, adviserName, adviserPhone, customerPhone, customerName, "MALE"
                new Object[]{
                        "adviser", "166****2222", "16622222222"
                },
                new Object[]{
                        "channelStaff", "176****8107", "17610248107"
                }
        };
    }

    @DataProvider(name = "ORDER_LIST_CHECK")
    public Object[][] orderListCheck1() {
        return new Object[][]{
//String channelId, int status, boolean isAudited, String namePhone, int pageSize
                new Object[]{
                        "5", "1", "true", "廖祥茹", 10
                },
                new Object[]{
                        "5", "2", "false", "", 10
                },
                new Object[]{
                        "1", "3", "true", "", 10
                },
        };
    }

    @DataProvider(name = "BAD_CHANNEL_STAFF")
    public Object[][] badChannelStaff() {
        return new Object[][]{
//String channelId, int status, boolean isAudited, String namePhone, int pageSize
                new Object[]{
                        "新建业务员（与置业顾问手机号相同）", "16622222222", "业务员手机号已被员工占用，请重新填写或更改员工信息"
                },
                new Object[]{
                        "新建业务员（与本渠道已启用业务员手机号相同）", "17610248107", "当前手机号17610248107已被使用"
                },
                new Object[]{
                        "新建业务员（与本渠道已禁用业务员手机号相同）", "17794123828", "当前手机号17794123828在本渠道被禁用，请先启用、修改业务员信息即可"
                },
                new Object[]{
                        "新建业务员（与其他渠道已启用业务员手机号相同）", "17711111024", "当前手机号17711111024已被使用"
                }
        };
    }

    @DataProvider(name = "BAD_ADVISER")
    public Object[][] badAdviser() {
        return new Object[][]{
//String channelId, int status, boolean isAudited, String namePhone, int pageSize
                new Object[]{
                        "新建置业顾问（与已启用业务员手机号相同）", "17610248107", "当前手机号已被使用"
                },
                new Object[]{
                        "新建置业顾问（与置业顾问手机号相同）", "16622222222", "当前手机号已被使用"
                }
        };
    }

    @DataProvider(name = "CATCH_DATE")
    public Object[][] catchDate() {
        return new Object[][]{
//start,end
                new Object[]{
                        LocalDate.now().toString(), LocalDate.now().toString()
                },
                new Object[]{
                        LocalDate.now().minusDays(7).toString(), LocalDate.now().toString()
                },
                new Object[]{
                        LocalDate.now().minusDays(30).toString(), LocalDate.now().toString()
                },
                new Object[]{
                        LocalDate.now().minusDays(365).toString(), LocalDate.now().toString()
                },
        };
    }
}

