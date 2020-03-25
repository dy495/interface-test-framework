package com.haisheng.framework.testng.bigScreen;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.arronlong.httpclientutil.HttpClientUtil;
import com.arronlong.httpclientutil.builder.HCB;
import com.arronlong.httpclientutil.common.HttpConfig;
import com.arronlong.httpclientutil.common.HttpHeader;
import com.arronlong.httpclientutil.exception.HttpProcessException;
import com.google.common.collect.ImmutableMap;
import com.haisheng.framework.model.bean.Case;
import com.haisheng.framework.testng.CommonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.CommonDataStructure.DingWebhook;
import com.haisheng.framework.util.*;
import org.apache.commons.lang.text.StrSubstitutor;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.*;
import java.time.LocalDate;
import java.util.Random;
import java.util.UUID;

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
    String wudongChannelNameStr = "测试【勿动】";
    int wudongChannelInt = 5;
    String wudongOwnerPhone = "16600000000";


    String maiTianChannelStr = "2";
    String maiTianChannelNameStr = "麦田";
    int maiTianChannelInt = 2;

    String lianjiaChannelStr = "1";
    int lianjiaChannelInt = 1;
    String lianjiaChannelName = "链家";
    String lianjiaOwnerPhone = "16600000001";

//  ------------------------------------------业务员-----------------------------------------------------

    String lianjiaToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiLmtYvor5XjgJDli7_liqjjgJEiLCJ1aWQiOjIxMzYsImxvZ2luVGltZSI6MT" +
            "U3ODk5OTY2NjU3NH0.kQsEw_wGVmPQ4My1p-FNZ556FJC7W177g7jfjFarTu4";
    String lianjiaFreezeStaffIdStr = "2136";
    int lianjiaFreezeStaffIdInt = 2136;
    String lianjiaFreezeStaffName = "链家-【勿动】";
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

    String normalOrderType = "NORMAL";
    String riskOrderType = "RISK";

    String defaultRuleId = "837";
    String ahead1hRuleId = "996";
    String ahead24hRuleId = "842";
    String ahead7dayRuleId = "844";
    String ahead30dayRuleId = "846";
    String aheadMaxRuleId = "1003";


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
            String smsCode = "202593";

            String customerName = caseName + "-" + getNamePro();
            customerReportH5(wudongStaffIdStr, customerName, customerPhone, "MALE", wudongToken);
            String report2 = customerReportH5NoCheckCode(wudongStaffIdStr, customerName, customerPhone, "MALE", wudongToken);
            checkCode(report2, StatusCode.BAD_REQUEST, "重复报备");

            checkMessage("重复报备", report2, "报备失败！当前顾客信息已报备完成，请勿重复报备");

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, caseDesc);
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

            String customerName = caseName + "-" + getNamePro();

//            保护渠道报备
            newCustomer(maiTianChannelInt, maitianStaffName, maitianStaffPhone, zhangName, zhangPhone, customerPhone, customerName, "MALE");

//            其他渠道报备

            String report2 = customerReportH5NoCheckCode(wudongStaffIdStr, customerName, customerPhone, "MALE", wudongToken);
            checkCode(report2, StatusCode.BAD_REQUEST, "保护期内其他渠道报备");

            checkMessage("报备保护", report2, "报备失败！当前顾客信息处于(麦田)渠道报备保护期内，请勿重复报备");

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, caseDesc);
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

            String customerName = caseName + "-" + getNamePro();

//            保护渠道报备
            newCustomer(maiTianChannelInt, maitianStaffName, maitianStaffPhone, zhangName, zhangPhone, customerPhone, customerName, "MALE");

//            现场注册
            newCustomer(-1, "", "", zhangName, zhangPhone, customerPhone, customerName, "MALE");

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, caseDesc);
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

            String customerName = caseName + "-" + getNamePro();

//            保护渠道报备
            newCustomer(maiTianChannelInt, maitianStaffName, maitianStaffPhone, zhangName, zhangPhone, customerPhone, customerName, "MALE");

            selfRegister(customerName, customerPhone, selfCode, "", "", "MALE");

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, caseDesc);
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
            String customerName = caseName + "-" + getNamePro();

            customerReportH5(wudongStaffIdStr, customerName, customerPhone, "MALE", wudongToken);

            newCustomer(maiTianChannelInt, maitianStaffName, maitianStaffPhone, zhangName, zhangPhone, customerPhone, customerName, "MALE");

            String report2 = customerReportH5NoCheckCode(lianjiaStaffIdStr, customerName, customerPhone, "MALE", lianjiaToken);

            checkCode(report2, StatusCode.BAD_REQUEST, "");

            checkMessage("报备保护", report2, "报备失败！当前顾客信息处于(麦田)渠道报备保护期内，请勿重复报备");

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, caseDesc);
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
            String customerName = caseName + "-" + getNamePro();

            customerReportH5(wudongStaffIdStr, customerName, customerPhone, "MALE", wudongToken);

            newCustomer(maiTianChannelInt, maitianStaffName, maitianStaffPhone, zhangName, zhangPhone, customerPhone, customerName, "MALE");

            newCustomer(-1, "", "", zhangName, zhangPhone, customerPhone, customerName, "MALE");

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, caseDesc);
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
            String customerName = caseName + "-" + getNamePro();
            customerReportH5(wudongStaffIdStr, customerName, customerPhone, "MALE", wudongToken);

            newCustomer(maiTianChannelInt, maitianStaffName, maitianStaffPhone, zhangName, zhangPhone, customerPhone, customerName, "MALE");

            selfRegister(customerName, customerPhone, selfCode, "", "", "MALE");

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, caseDesc);
        }
    }

    /**
     * 其他渠道补全手机号为保护渠道报备的顾客手机号
     */
    @Test
    public void InProtectOthersCompleteIn() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "保护渠道报备-保护期内其他渠道补全手机号为保护渠道报备的顾客手机号";

        logger.info("\n\n" + caseName + "\n");

        try {
            // 报备
            String customerPhone = "14422110014";
            String customerPhoneHide = "144****0014";

            String customerName = caseName + "-" + getNamePro();

//            保护渠道报备（麦田）
            newCustomer(maiTianChannelInt, maitianStaffName, maitianStaffPhone, zhangName, zhangPhone, customerPhone, customerName, "MALE");

//            报备隐藏手机号(勿动)
            customerReportH5(wudongStaffIdStr, customerName, customerPhoneHide, "MALE", wudongToken);

//            补全手机号
            JSONArray list = customerList(customerName, wudongChannelIdStr, "", 1, 10).getJSONArray("list");

            String cid = list.getJSONObject(0).getString("cid");

            String res = customerEditPCNoCheckCode(cid, customerName, customerPhone, zhangName, zhangPhone);

            checkCode(res, StatusCode.BAD_REQUEST, "保护期内其他渠道修改手机号为当前顾客");

            checkMessage("报备保护", res, "修改顾客信息失败！该手机号已被其他拥有报备保护的渠道报备");

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, caseDesc);
        }
    }

    /**
     * 其他渠道补全手机号为保护渠道报备的顾客手机号
     */
    @Test
    public void InProtectOthersChangeIn() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "保护渠道报备-保护期内其他渠道修改手机号为保护渠道报备的顾客手机号";

        logger.info("\n\n" + caseName + "\n");

        try {
            // 报备
            String customerPhoneB = "13322110014";
            String customerPhone = "14422110014";

            String customerName = caseName + "-" + getNamePro();

//            保护渠道报备（麦田）
            newCustomer(maiTianChannelInt, maitianStaffName, maitianStaffPhone, zhangName, zhangPhone, customerPhone, customerName, "MALE");

//            报备原手机号(勿动)
            customerReportH5(wudongStaffIdStr, customerName, customerPhoneB, "MALE", wudongToken);

//            更改手机号
            JSONArray list = customerList(customerName, wudongChannelIdStr, "", 1, 10).getJSONArray("list");

            String cid = list.getJSONObject(0).getString("cid");

            String res = customerEditPCNoCheckCode(cid, customerName, customerPhone, zhangName, zhangPhone);

            checkCode(res, StatusCode.BAD_REQUEST, "保护期内其他渠道修改手机号为当前顾客");

            checkMessage("报备保护", res, "修改顾客信息失败！该手机号已被其他拥有报备保护的渠道报备");

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, caseDesc);
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
            String customerName = "麦田FREEZE";

            String customerNameA = "麦田FREEZE" + "-" + getNamePro();

//            其他渠道报备
            customerReportH5(wudongStaffIdStr, customerName, customerPhone, "MALE", wudongToken);

            JSONArray list = customerList(customerName, wudongChannelIdStr, "", 1, 10).getJSONArray("list");

            String cid = list.getJSONObject(0).getString("cid");

            customerEditPC(cid, customerNameA, customerPhone, anShengName, anShengPhone);

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, caseDesc);
        }
    }

    /**
     * 其他渠道补全手机号为保护渠道报备的顾客手机号
     */
    @Test
    public void OutProtectOthersCompleteAfter() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "保护期外其他渠道补全手机号为保护渠道报备的顾客手机号";

        logger.info("\n\n" + caseName + "\n");

        try {

            String customerPhone = "14422110180";
            String customerPhoneHide = "144****0180";
            String customerName = "麦田FREEZE";

            String customerNameA = "麦田FREEZE" + "-" + getNamePro();

//            其他渠道报备
            customerReportH5(wudongStaffIdStr, customerName, customerPhoneHide, "MALE", wudongToken);

            JSONArray list = customerList(customerName, wudongChannelIdStr, "", 1, 10).getJSONArray("list");

            String cid = list.getJSONObject(0).getString("cid");

            customerEditPC(cid, customerName, customerPhone, anShengName, anShengPhone);

            customerEditPC(cid, customerNameA, customerPhone, anShengName, anShengPhone);

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, caseDesc);
        }
    }

    /**
     * 其他渠道补全手机号为保护渠道报备的顾客手机号
     */
    @Test
    public void OutProtectOthersChangeAfter() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "保护期外其他渠道补全手机号为保护渠道报备的顾客手机号";

        logger.info("\n\n" + caseName + "\n");

        try {
            // 报备
            String customerPhone = "14422110014";
            String customerPhoneB = "13422110014";

            String customerName = "麦田【勿动】";

            String customerNameA = "麦田【勿动】" + "-" + getNamePro();

            customerReportH5(wudongStaffIdStr, customerName, customerPhoneB, "MALE", wudongToken);

//            更改手机号
            JSONArray list = customerList(customerPhoneB, wudongChannelIdStr, "", 1, 10).getJSONArray("list");

            String cid = list.getJSONObject(0).getString("cid");

            customerEditPC(cid, customerName, customerPhone, anShengName, anShengPhone);

            customerEditPC(cid, customerNameA, customerPhone, anShengName, anShengPhone);

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, caseDesc);
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

            String customerName = caseName + "-" + getNamePro();

            channelEdit(wudongChannelIdStr, wudongChannelNameStr, "test", "12301010101", protect1DayRuleId);

//            报备隐藏手机号(勿动)
            customerReportH5(wudongStaffIdStr, customerName, customerPhone, "MALE", wudongToken);

            customerReportH5(lianjiaStaffIdStr, customerName, customerPhone, "MALE", lianjiaToken);

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            channelEditFinally(wudongChannelIdStr, wudongChannelNameStr, "test", "12301010101", defaultRuleId);
            saveData(aCase, ciCaseName, caseName, caseDesc);
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
            String customerNameA = name + "-" + getNamePro();

//            报备隐藏手机号(勿动)
            customerReportH5(wudongStaffIdStr, customerNameB, customerPhoneHide, "MALE", wudongToken);

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
            channelEditFinally(wudongChannelIdStr, wudongChannelNameStr, "test", "12301010101", defaultRuleId);
            saveData(aCase, ciCaseName, caseName, caseDesc);
        }
    }


    //    @Test
    public void test() throws Exception {
        feidan.customerListH5(1, 10, wudongToken);
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
            String customerNameA = caseName + "-" + getNamePro();

//            报备隐藏手机号(勿动)
            customerReportH5(wudongStaffIdStr, customerNameB, customerPhoneHide, "MALE", wudongToken);

//            补全
            JSONObject customer = feidan.customerListH5(1, 10, wudongToken).getJSONArray("list").getJSONObject(0);
            String cid = customer.getString("cid");
            String customerNameRes = customer.getString("customer_name");
            if (!customerNameB.equals(customerNameRes)) {
                throw new Exception("H5业务员顾客列表中的第一个顾客【" + customerNameRes + "】不是刚报备的顾客【" + customerNameB + "】");
            }

            String editH5NoCode = feidan.customerEditH5NoCode(cid, customerNameA, customerPhone, wudongToken);
            feidan.checkCode(editH5NoCode, StatusCode.FORBIDDEN, "");

            feidan.checkMessage("H5补全手机号", editH5NoCode, "补全号码时必须保证前三后四相同");

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            channelEditFinally(wudongChannelIdStr, wudongChannelNameStr, "test", "12301010101", defaultRuleId);
            saveData(aCase, ciCaseName, caseName, caseDesc);
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

            String addRiskRule = addRiskRuleNoCheckCode("test", number, "10");

            checkCode(addRiskRule, StatusCode.BAD_REQUEST, "提前报备时间为【" + number + "】");

            checkMessage("新建风控规则", addRiskRule, "提前报备时间不能超过半年");

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, caseDesc);
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

            String ruleName = getNamePro();

            addRiskRule(ruleName, number + "", "10");

            JSONObject data = riskRuleList();

            JSONArray list = data.getJSONArray("list");

            JSONObject ruleData = list.getJSONObject(list.size() - 1);

            String name = ruleData.getString("name");
            if (!ruleName.equals(name)) {
                throw new Exception("期待最后一条规则为【" + ruleName + "】，实际为【" + name + "】");
            }

            String id = ruleData.getString("id");

            deleteRiskRule(id);

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, caseDesc);
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

            String addRiskRule = addRiskRuleNoCheckCode("test", "60", number);

            checkCode(addRiskRule, StatusCode.BAD_REQUEST, "");

            checkMessage("新建风控规则", addRiskRule, "报备保护期不能超过10000天");

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, caseDesc);
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

            String ruleName = getNamePro();

            addRiskRule(ruleName, number + "", "10");

            JSONObject data = riskRuleList();

            JSONArray list = data.getJSONArray("list");

            JSONObject ruleData = list.getJSONObject(list.size() - 1);

            String name = ruleData.getString("name");
            if (!ruleName.equals(name)) {
                throw new Exception("期待最后一条规则为【" + ruleName + "】，实际为【" + name + "】");
            }

            String id = ruleData.getString("id");

            deleteRiskRule(id);

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, caseDesc);
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

            String addRiskRule = addRiskRuleNoCheckCode(name, "60", "60");

            checkCode(addRiskRule, StatusCode.BAD_REQUEST, "新建风控规则");

            if ("".equals(name.trim())) {
                checkMessage("新建风控规则", addRiskRule, "规则名称不可为空");
            } else if ("默认规则".equals(name)) {
                checkMessage("新建风控规则", addRiskRule, "规则名称不允许重复");
            } else {
                checkMessage("新建风控规则", addRiskRule, "规则名称最长为20个字符");
            }

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, caseDesc);
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
            addRiskRule(name, "60", "60");

            JSONObject data = riskRuleList();

            JSONArray list = data.getJSONArray("list");

            JSONObject ruleData = list.getJSONObject(list.size() - 1);

            String ruleName = ruleData.getString("name");
            if (!ruleName.equals(name)) {
                throw new Exception("期待最后一条规则为【" + ruleName + "】，实际为【" + name + "】");
            }

            String id = ruleData.getString("id");

            deleteRiskRule(id);
        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, caseDesc);
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

            String s = deleteRiskRuleNoCheckCode(id);

            checkCode(s, StatusCode.BAD_REQUEST, caseDesc);

            if (defaultRuleId.equals(id)) {
                checkMessage("新建风控规则", s, "只允许删除自定义规则");
            } else {
                checkMessage("新建风控规则", s, "规则已被渠道引用, 不可删除");
            }

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, caseDesc);
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

            String s = newCustomerNoCheckCode(channelId, channelStaffName, channelStaffPhone, adviserName, adviserPhone, phone, customerName, gender);
            checkCode(s, StatusCode.BAD_REQUEST, message);

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, caseDesc);
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

            String channelStaffName = getNamePro();
            String channelStaffPhone = genPhoneNum();

            String adviserName = getNamePro();
            String adviserPhone = genPhoneNum();

            String customerName = getNamePro();
            String customerPhone = genPhoneNum();

            newCustomer(lianjiaChannelInt, channelStaffName, channelStaffPhone, adviserName, adviserPhone, customerPhone, customerName, "MALE");

            JSONObject channelStaff = channelStaffList(channelStaffName, lianjiaChannelStr, 1, 10).getJSONArray("list").getJSONObject(0);

            String phoneRes = channelStaff.getString("phone");
            if (!channelStaffPhone.equals(phoneRes)) {
                throw new Exception("没有找到业务员【" + channelStaffName + "】");
            }

            JSONObject staff = staffList(adviserName, 1, 10).getJSONArray("list").getJSONObject(0);

            String adviserPhoneRes = staff.getString("phone");
            if (!adviserPhone.equals(adviserPhoneRes)) {
                throw new Exception("没有找到置业顾问【" + adviserName + "】");
            }

            String id = staff.getString("id");

            staffDelete(id);

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, caseDesc);
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

                importFile(xmlPath);

                checkCode(this.response, StatusCode.BAD_REQUEST, files[i].getName() + ">>>");
            }

            xmlPath = "src/main/java/com/haisheng/framework/testng/bigScreen/Feidan.java";
            importFile(xmlPath);
            checkCode(this.response, StatusCode.BAD_REQUEST, "上传java文件");
            checkMessage("上传java文件", this.response, "暂不支持当前文件格式");

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, caseDesc);
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

            JSONArray newB = catchList(customerType, deviceId, startTime, endTime, 1, 1).getJSONArray("list");

            String customerId = "";
            if (newB.size() > 0) {
                JSONObject onePerson = newB.getJSONObject(0);
                customerId = onePerson.getString("customer_id");

                visitor2Staff(customerId);

                Integer pages = catchList(customerType, deviceId, startTime, endTime, 1, 1).getInteger("pages");

                for (int i = 1; i <= pages; i++) {
                    JSONObject newA = catchList(customerType, deviceId, startTime, endTime, i, 50);
                    JSONArray list = newA.getJSONArray("list");
                    for (int j = 0; j < list.size(); j++) {
                        JSONObject single = list.getJSONObject(j);
                        if (customerId.equals(single.getString("customer_id"))) {
                            throw new Exception("转员工失败，有部分同一customerId的访客未转成员工。customerId=" + customerId);
                        }
                    }
                }

                pages = catchList("STAFF", deviceId, startTime, endTime, 1, 1).getInteger("pages");
                boolean isExist = false;
                for (int i = 1; i < pages; i++) {

                    JSONObject staff = catchList("STAFF", deviceId, startTime, endTime, i, 50);
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
            saveData(aCase, ciCaseName, caseName, caseDesc);
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
            JSONObject uploadImage = uploadImage(imagePath);
            String phoneNum = genPhoneNum();
            String staffName = getNamePro();

//            新建置业顾问
            addStaff(staffName, phoneNum, uploadImage.getString("face_url"));

//            查询列表
            checkAdviserList(staffName, phoneNum, true);

//            编辑
            JSONObject staff = staffList(phoneNum, 1, 1).getJSONArray("list").getJSONObject(0);
            String id = staff.getString("id");

            staffName = getNamePro();
            phoneNum = genPhoneNum();

            staffEdit(id, staffName, phoneNum, "");

//            查询
            checkAdviserList(staffName, phoneNum, false);

//            删除
            staffDelete(id);
        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, caseDesc);
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

            String phoneNum = genPhoneNum();
            String staffName = getNamePro();

//            新建置业顾问
            addStaff(staffName, phoneNum, "");

//            查询列表
            checkAdviserList(staffName, phoneNum, false);

//            编辑
            String dirPath = "src/main/java/com/haisheng/framework/testng/bigScreen/feidanImages";

            String imagePath = dirPath + "/" + "Cris.jpg";
            imagePath = imagePath.replace("/", File.separator);
            JSONObject uploadImage = uploadImage(imagePath);

            JSONObject staff = staffList(phoneNum, 1, 1).getJSONArray("list").getJSONObject(0);
            String id = staff.getString("id");

            staffName = getNamePro();
            phoneNum = genPhoneNum();

            staffEdit(id, staffName, phoneNum, uploadImage.getString("face_url"));

//            查询
            checkAdviserList(staffName, phoneNum, true);

//            删除
            staffDelete(id);
        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, caseDesc);
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

            String phoneNum = genPhoneNum();
            String phoneNumNew = genPhoneNum();
            String channelName = getNamePro();
            String channelNameNew = getNamePro();

            String owner = "owner";

//            新建渠道
            addChannel(channelName, owner, phoneNum, defaultRuleId);

//            渠道列表
            JSONArray channelList = channelList(1, 10).getJSONArray("list");
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

                    channelEdit(channelId, channelNameNew, owner, phoneNumNew, ahead1hRuleId);

                    JSONArray channelListA = channelList(1, 10).getJSONArray("list");
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
            saveData(aCase, ciCaseName, caseName, caseDesc);
        }
    }

    @Test
    public void channelStaffCheck() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;
        String caseDesc = "新建业务员-列表-查询-列表";

        try {

            String phoneNum = genPhoneNum();
            String staffName = getNamePro();

//            新建业务员
            addChannelStaff(wudongChannelIdStr, staffName, phoneNum);

//            查询列表
            checkChannelStaffList(wudongChannelIdStr, staffName, phoneNum, false);

//            编辑
            String dirPath = "src/main/java/com/haisheng/framework/testng/bigScreen/feidanImages";

            String imagePath = dirPath + "/" + "Cris.jpg";
            imagePath = imagePath.replace("/", File.separator);
            JSONObject uploadImage = uploadImage(imagePath);

            JSONObject staff = channelStaffList(wudongChannelIdStr, 1, 10).getJSONArray("list").getJSONObject(0);
            String id = staff.getString("id");

            staffName = getNamePro();
            phoneNum = genPhoneNum();

            editChannelStaff(id, wudongChannelIdStr, staffName, phoneNum, uploadImage.getString("face_url"));

//            查询
            checkChannelStaffList(wudongChannelIdStr, staffName, phoneNum, true);

//            删除
            changeChannelStaffState(id);

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, caseDesc);
        }
    }

    @Test(dataProvider = "BAD_CHANNEL_STAFF")
    public void chanStaffSamePhoneToStaff(String caseDesc, String phoneNum, String message) {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        try {

            String staffName = getNamePro();

//            新建业务员
            String res = addChannelStaffNoCode(wudongChannelIdStr, staffName, phoneNum);
            checkCode(res, StatusCode.BAD_REQUEST, caseDesc);
            checkMessage(caseDesc, res, message);

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, caseDesc);
        }
    }

    @Test
    public void samePhoneOtherChanDisable() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "新建业务员（与其他渠道的已禁用业务员手机号相同）";

        try {

            String staffName = getNamePro();

//            新建业务员(渠道一)
            String phoneNum = genPhoneNum();
            addChannelStaff(wudongChannelIdStr, staffName, phoneNum);

//            禁用
            JSONArray channelStaffList = channelStaffList(phoneNum, wudongChannelIdStr, 1, 10).getJSONArray("list");
            String id = channelStaffList.getJSONObject(0).getString("id");

            changeState(id);

//            新建业务员（渠道二）
            addChannelStaff(lianjiaChannelStr, staffName, phoneNum);

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, caseDesc);
        }
    }


    @Test(dataProvider = "BAD_ADVISER")
    public void adviserSamePhoneToChanStaff(String caseDesc, String phoneNum, String message) {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        try {

            String staffName = getNamePro();

//            新建置业顾问
            String addStaff = addStaffNoCode(staffName, phoneNum, "");

            checkCode(addStaff, StatusCode.BAD_REQUEST, caseDesc);

            checkMessage(caseDesc, addStaff, message);
        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, caseDesc);
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
            String refreshCode = refreshQrcodeNoCheckCode();
            checkCode(refreshCode, StatusCode.SUCCESS, "刷新OCR验证码");

            String codeB = JSON.parseObject(refreshCode).getJSONObject("data").getString("code");

//        确认
            String confirmCode = confirmQrcodeNoCheckCode(codeB);

            checkCode(confirmCode, StatusCode.SUCCESS, "确认验证码");

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

            String ocrPicUpload = ocrPicUpload(token, imageBinary, faceBinary);
            checkCode(ocrPicUpload, StatusCode.SUCCESS, "案场OCR上传证件");

//        刷新
            refreshCode = refreshQrcodeNoCheckCode();
            checkCode(refreshCode, StatusCode.SUCCESS, "刷新OCR验证码");

            String codeA = JSON.parseObject(refreshCode).getJSONObject("data").getString("code");

//        原code确认
            String confirm = confirmQrcodeNoCheckCode(codeB);
            checkCode(confirm, StatusCode.BAD_REQUEST, "OCR确认-刷新之前的");

//        现code确认
            confirm = confirmQrcodeNoCheckCode(codeA);
            checkCode(confirm, StatusCode.SUCCESS, "OCR确认-刷新之后的");

//            现code再次确认
            confirm = confirmQrcodeNoCheckCode(codeA);
            checkCode(confirm, StatusCode.SUCCESS, "再次OCR确认-刷新之后的");

            JSONArray list = orderList("廖祥茹", 10).getJSONArray("list");
            if (list.size() > 1) {
                throw new Exception("OCR刷证后，刷证环节没有出现在原有订单中!customerName=廖祥茹");
            } else if (list.size() == 0) {
                throw new Exception("不存在该顾客的订单!customerName=廖祥茹");
            } else {
                JSONObject order = list.getJSONObject(0);
                String orderId = order.getString("order_id");

                boolean isExist = false;

                JSONArray linkList = orderLinkList(orderId).getJSONArray("list");
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
            saveData(aCase, ciCaseName, caseName, caseDesc);
        }
    }

    @Test(dataProvider = "ORDER_LIST_CHECK")
    public void orderListCheck(String channelId, String status, String isAudited, String namePhone, int pageSize) {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;
        String caseDesc = "";

        try {

            JSONArray list = orderList(channelId, status, isAudited, namePhone, 10).getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                JSONObject single = list.getJSONObject(i);

                String channelName = single.getString("channel_name");
                if ("1".equals(channelId)) {
                    if (!"链家".equals(channelName)) {
                        throw new Exception("搜索条件为渠道=链家时，搜索结果中出现【" + channelName + "】渠道的订单");
                    }
                } else if ("5".equals(channelId)) {
                    if (!"测试【勿动】".equals(channelName)) {
                        throw new Exception("搜索条件为渠道=测试【勿动】时，搜索结果中出现【" + channelName + "】渠道的订单");
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
            saveData(aCase, ciCaseName, caseName, caseDesc);
        }
    }


    public JSONObject orderList(String namePhone, int pageSize) throws Exception {

        String url = "/risk/order/list";
        String json =
                "{" +
                        "    \"shop_id\":" + getShopId() + "," +
                        "    \"customer_name\":\"" + namePhone + "\"," +
                        "    \"page\":1" + "," +
                        "    \"size\":" + pageSize +
                        "}";
        String res = httpPost(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    public JSONObject faceTraces(String showUrl) throws Exception {
        String url = "/risk/evidence/face/traces";
        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + ",\n" +
                        "    \"show_url\":\"" + showUrl + "\"" +
                        "}";


        String res = httpPost(url, json);

        return JSON.parseObject(res);
    }


    public void checkAdviserList(String name, String phone, boolean hasPic) throws Exception {

        JSONObject staff = staffList(phone, 1, 1).getJSONArray("list").getJSONObject(0);

        if (staff == null || staff.size() == 0) {
            throw new Exception("不存在该置业顾问，姓名=" + name + "，手机号=" + phone);
        } else {
            checkUtil.checkKeyValue("置业顾问列表查询", staff, "staff_name", name, true);
            checkUtil.checkKeyValue("置业顾问列表查询", staff, "phone", phone, true);

            if (hasPic) {
                checkUtil.checkNotNull("置业顾问列表查询", staff, "face_url");
            } else {
                checkUtil.checkNull("置业顾问列表查询", staff, "face_url");
            }
        }
    }

    public void checkChannelStaffList(String channelId, String name, String phone, boolean hasPic) throws Exception {

        JSONObject staff = channelStaffList(channelId, 1, 1).getJSONArray("list").getJSONObject(0);

        if (staff == null || staff.size() == 0) {
            throw new Exception("测试【勿动】渠道不存在该业务员，姓名=" + name + "，手机号=" + phone);
        } else {
            checkUtil.checkKeyValue("渠道业务员列表查询", staff, "staff_name", name, true);
            checkUtil.checkKeyValue("渠道业务员列表查询", staff, "phone", phone, true);
            checkUtil.checkNotNull("渠道业务员列表查询", staff, "total_report");

            if (hasPic) {
                checkUtil.checkNotNull("渠道业务员列表查询", staff, "face_url");
            } else {
                checkUtil.checkNull("渠道业务员列表查询", staff, "face_url");
            }
        }
    }

//    ----------------------------------------数据验证方法--------------------------------------------------------------------


    public String genPhoneNum() {
        Random random = new Random();
        String num = "177" + (random.nextInt(89999999) + 10000000);

        return num;
    }

    private void checkMessage(String function, String response, String message) throws Exception {

        String messageRes = JSON.parseObject(response).getString("message");
        if (!message.equals(messageRes)) {
            throw new Exception(function + "，提示信息与期待不符，期待=" + message + "，实际=" + messageRes);
        }
    }

    private void checkDetail(String orderId, String customerName, String phone, String adviserName, String
            channelName,
                             String channelStaffName, String orderStatusTips, String faceUrl, String firstAppearTime,
                             String reportTime, JSONObject orderDetail) throws Exception {

        String function = "订单详情-orderId=" + orderId + "，";

        if (!"-".equals(customerName)) {

            checkUtil.checkKeyValue(function, orderDetail, "customer_name", customerName, true);
        } else {
            checkUtil.checkNull(function, orderDetail, "customer_name");
        }

        if (!"-".equals(phone)) {

            checkUtil.checkKeyValue(function, orderDetail, "phone", phone, true);
        } else {
            checkUtil.checkNull(function, orderDetail, "phone");
        }

        if (!"-".equals(adviserName)) {

            checkUtil.checkKeyValue(function, orderDetail, "adviser_name", adviserName, true);
        } else {
            checkUtil.checkNull(function, orderDetail, "adviser_name");
        }

        if (!"-".equals(channelName)) {

            checkUtil.checkKeyValue(function, orderDetail, "channel_name", channelName, true);
        } else {
            checkUtil.checkNull(function, orderDetail, "channel_name");
        }

        if (!"-".equals(channelStaffName)) {

            checkUtil.checkKeyValue(function, orderDetail, "channel_staff_name", channelStaffName, true);
        } else {
            checkUtil.checkNull(function, orderDetail, "channel_staff_name");
        }

        if (!"-".equals(orderStatusTips)) {

            checkUtil.checkKeyValue(function, orderDetail, "order_status_tips", orderStatusTips, true);
        } else {
            checkUtil.checkNull(function, orderDetail, "order_status_tips");
        }

        if (!"-".equals(faceUrl)) {
            checkUtil.checkKeyValue(function, orderDetail, "face_url", faceUrl, true);
        } else {
            checkUtil.checkNull(function, orderDetail, "face_url");
        }

        if (!"-".equals(firstAppearTime)) {
            checkUtil.checkKeyValue(function, orderDetail, "first_appear_time", firstAppearTime, true);
        } else {
            checkUtil.checkNull(function, orderDetail, "first_appear_time");
        }

        if (!"-".equals(reportTime)) {
            checkUtil.checkKeyValue(function, orderDetail, "report_time", reportTime, true);
        } else {
            checkUtil.checkNull(function, orderDetail, "report_time");
        }

        checkUtil.checkKeyValue(function, orderDetail, "deal_time", "", false);
    }


    public void checkNormalOrderLink(String orderId, JSONObject data) throws Exception {

        JSONArray linkLists = data.getJSONArray("list");

        for (int i = 0; i < linkLists.size(); i++) {
            JSONObject link = linkLists.getJSONObject(i);
            Integer linkStatus = link.getInteger("link_status");
            String linkName = link.getString("link_name");
            if (linkStatus != 1) {
                throw new Exception("order_id=" + orderId + "，环节=" + linkName + "，应为正常环节，系统返回为异常!");
            }
        }
    }

    private void checkFirstVisitAndTrace(String orderId, JSONObject data, boolean expectExist) throws Exception {
        JSONArray linkLists = data.getJSONArray("list");

        boolean isExist = false;


        String functionPre = "orderId=" + orderId + "，";

        for (int i = 0; i < linkLists.size(); i++) {
            String function;
            JSONObject link = linkLists.getJSONObject(i);
            String linkKey = link.getString("link_key");
            String id = link.getString("id");

            function = functionPre + "环节id=" + id + "，";


            if ("FIRST_APPEAR".equals(linkKey) || "TRACE".equals(linkKey)) {
                isExist = true;

                checkUtil.checkNotNull(function, link, "link_point");
                checkUtil.checkNotNull(function, link.getJSONObject("link_note"), "face_url");
                checkUtil.checkNotNull(function, link, "link_time");
                checkUtil.checkNotNull(function, link, "link_time");
                checkUtil.checkNotNull(function, link, "link_name");

                Integer linkStatus = link.getInteger("link_status");
                String linkName = link.getString("link_name");
                if (linkStatus != 1) {
                    throw new Exception("order_id=" + orderId + "，环节=" + linkName + "，环节id=" + id + "，应为正常环节，系统返回为异常!");
                }
            }
        }

        if (isExist != expectExist) {
            throw new Exception("order_id=" + orderId + "，是否期待存在场内轨迹，期待：" + expectExist + ",实际：" + isExist);
        }

    }

    public String getNamePro() {

        String tmp = UUID.randomUUID() + "";

        return tmp.substring(tmp.length() - 7);
    }

    private void detailListLinkConsist(String orderId, String phone) throws Exception {

        JSONArray list = orderList(3, phone, 100).getJSONArray("list");

        for (int i = 0; i < list.size(); i++) {
            JSONObject single = list.getJSONObject(i);
            String orderIdRes = single.getString("order_id");

            if (orderId.equals(orderIdRes)) {

                JSONObject orderDetail = orderDetail(orderId);
                String function = "订单详情>>>";

                String customerName = single.getString("customer_name");
                checkUtil.checkKeyValue(function, orderDetail, "customer_name", customerName, true);

                String adviserName = single.getString("adviser_name");
                if (adviserName == null || "".equals(adviserName)) {
                    String adviserNameDetail = orderDetail.getString("adviser_name");
                    if (adviserNameDetail != null && !"".equals(adviserNameDetail)) {
                        throw new Exception("orderId=" + orderId + ",adviser_name在订单列表中是空，在订单详情中=" + adviserNameDetail);
                    }
                } else {
                    checkUtil.checkKeyValue(function, orderDetail, "adviser_name", adviserName, true);
                }

                String channelName = single.getString("channel_name");
                if (channelName == null || "".equals(channelName)) {
                    String channelNameDetail = orderDetail.getString("channel_name");
                    if (channelNameDetail != null && !"".equals(channelNameDetail)) {
                        throw new Exception("orderId=" + orderId + ",channel_name在订单列表中是空，在订单详情中=" + channelNameDetail);
                    }
                } else {
                    checkUtil.checkKeyValue(function, orderDetail, "channel_name", channelName, true);
                }

                String channelStaffName = single.getString("channel_staff_name");
                if (channelStaffName == null || "".equals(channelStaffName)) {
                    String channelStaffNameDetail = orderDetail.getString("channel_staff_name");
                    if (channelStaffNameDetail != null && !"".equals(channelStaffNameDetail)) {
                        throw new Exception("orderId=" + orderId + ",channel_staff_name在订单列表中是空，在订单详情中=" + channelStaffNameDetail);
                    }
                } else {
                    checkUtil.checkKeyValue(function, orderDetail, "channel_staff_name", channelStaffName, true);
                }

                String firstAppearTime = single.getString("first_appear_time");
                if (firstAppearTime == null || "".equals(firstAppearTime)) {
                    String firstAppearTimeDetail = orderDetail.getString("first_appear_time");
                    if (firstAppearTimeDetail != null && !"".equals(firstAppearTimeDetail)) {
                        throw new Exception("orderId=" + orderId + ",first_appear_time在订单列表中是空，在订单详情中=" + firstAppearTimeDetail);
                    }
                } else {
                    checkUtil.checkKeyValue(function, orderDetail, "first_appear_time", firstAppearTime, true);
                }

                String dealTime = single.getString("deal_time");
                if (dealTime == null || "".equals(dealTime)) {
                    String dealTimeDetail = orderDetail.getString("deal_time");
                    if (dealTimeDetail != null && !"".equals(dealTimeDetail)) {
                        throw new Exception("orderId=" + orderId + ",deal_time在订单列表中是空，在订单详情中=" + dealTimeDetail);
                    }
                } else {
                    checkUtil.checkKeyValue(function, orderDetail, "deal_time", dealTime, true);
                }

                String reportTime = single.getString("report_time");
                if (reportTime == null || "".equals(reportTime)) {
                    String reportTimeDetail = orderDetail.getString("report_time");
                    if (reportTimeDetail != null && !"".equals(reportTimeDetail)) {
                        throw new Exception("orderId=" + orderId + ",report_time在订单列表中是空，在订单详情中=" + reportTimeDetail);
                    }
                } else {
                    checkUtil.checkKeyValue(function, orderDetail, "report_time", reportTime, true);
                }

                String isAudited = single.getString("is_audited");
                if (isAudited == null || "".equals(isAudited)) {
                    String isAuditedDetail = orderDetail.getString("report_time");
                    if (isAuditedDetail != null && !"".equals(isAuditedDetail)) {
                        throw new Exception("orderId=" + orderId + ",is_audited在订单列表中是空，在订单详情中=" + isAuditedDetail);
                    }
                } else {
                    checkUtil.checkKeyValue(function, orderDetail, "is_audited", isAudited, true);
                }

                break;
            }
        }
    }

    private String getIpPort() {
        return "http://dev.store.winsenseos.cn";
    }

    private void initHttpConfig() {
        HttpClient client;
        try {
            client = HCB.custom()
                    .pool(50, 10)
                    .retry(3).build();
        } catch (HttpProcessException e) {
            failReason = "初始化http配置异常" + "\n" + e;
            return;
        }
        String userAgent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36";
        Header[] headers = HttpHeader.custom().contentType("application/json; charset=utf-8")
                .other("shop_id", getShopId().toString())
                .userAgent(userAgent)
                .authorization(authorization)
                .build();

        config = HttpConfig.custom()
                .headers(headers)
                .client(client);
    }

    private String httpPostWithCheckCode(String path, String json) throws Exception {
        initHttpConfig();
        String queryUrl = getIpPort() + path;
        config.url(queryUrl).json(json);
        logger.info("{} json param: {}", path, json);
        long start = System.currentTimeMillis();

        response = HttpClientUtil.post(config);

        logger.info("response: {}", response);

        checkCode(response, StatusCode.SUCCESS, path);

        logger.info("{} time used {} ms", path, System.currentTimeMillis() - start);
        return response;
    }

    private String httpPostNoPrintPara(String path, String json) throws Exception {
        initHttpConfig();
        String queryUrl = getIpPort() + path;
        config.url(queryUrl).json(json);
        long start = System.currentTimeMillis();


        response = HttpClientUtil.post(config);

        logger.info("response: {}", response);

        logger.info("{} time used {} ms", path, System.currentTimeMillis() - start);
        return response;
    }

    private String httpPost(String path, String json) throws Exception {
        initHttpConfig();
        String queryUrl = getIpPort() + path;
        config.url(queryUrl).json(json);
        logger.info("{} json param: {}", path, json);
        long start = System.currentTimeMillis();

        response = HttpClientUtil.post(config);

        logger.info("response: {}", response);

        logger.info("{} time used {} ms", path, System.currentTimeMillis() - start);
        return response;
    }

    private void checkCode(String response, int expect, String message) throws Exception {
        JSONObject resJo = JSON.parseObject(response);

        if (resJo.containsKey("code")) {
            int code = resJo.getInteger("code");

            if (expect != code) {
                if (code != 1000) {
                    message += resJo.getString("message");
                }
                Assert.assertEquals(code, expect, message);
            }
        } else {
            int status = resJo.getInteger("status");
            String path = resJo.getString("path");
            throw new Exception("接口调用失败，status：" + status + ",path:" + path);
        }
    }

    private void checkNotCode(String response, int expectNot, String message) throws Exception {
        JSONObject resJo = JSON.parseObject(response);

        if (resJo.containsKey("code")) {
            int code = resJo.getInteger("code");

            if (expectNot == code) {
                Assert.assertNotEquals(code, expectNot, message + resJo.getString("message"));
            }
        } else {
            int status = resJo.getInteger("status");
            String path = resJo.getString("path");
            throw new Exception("接口调用失败，status：" + status + ",path:" + path);
        }
    }

    /**
     * 获取登录信息 如果上述初始化方法（initHttpConfig）使用的authorization 过期，请先调用此方法获取
     *
     * @ 异常
     */
    @BeforeClass
    public void login() {
        qaDbUtil.openConnection();
        qaDbUtil.openConnectionRdDaily();
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        logger.info("\n\n" + caseName + "\n");

        initHttpConfig();
        String path = "/risk-login";
        String loginUrl = getIpPort() + path;
        String json = "{\"username\":\"yuexiu@test.com\",\"passwd\":\"f5b3e737510f31b88eb2d4b5d0cd2fb4\"}";
        config.url(loginUrl)
                .json(json);
        logger.info("{} json param: {}", path, json);
        long start = System.currentTimeMillis();
        try {
            response = HttpClientUtil.post(config);
            this.authorization = JSONObject.parseObject(response).getJSONObject("data").getString("token");
            logger.info("authorization: {}", this.authorization);
        } catch (Exception e) {
            aCase.setFailReason("http post 调用异常，url = " + loginUrl + "\n" + e);
            logger.error(aCase.getFailReason());
            logger.error(e.toString());
        }
        logger.info("{} time used {} ms", path, System.currentTimeMillis() - start);

        saveData(aCase, ciCaseName, caseName, "登录获取authentication");
    }

    @AfterClass
    public void clean() {
        qaDbUtil.closeConnection();
        qaDbUtil.closeConnectionRdDaily();
        dingPushFinal();
    }

    @BeforeMethod
    public void initialVars() {
        failReason = "";
        response = "";
        aCase = new Case();
    }

    private Object getShopId() {
        return "4116";
    }

    private static final String ADD_ORDER = "/risk/order/createOrder";
    private static final String ORDER_DETAIL = "/risk/order/detail";
    private static final String CUSTOMER_LIST = "/risk/customer/list";
    private static final String CUSTOMER_INSERT = "/risk/customer/insert";
    private static final String STAFF_LIST = "/risk/staff/page";
    private static final String STAFF_TYPE_LIST = "/risk/staff/type/list";
    private static String ORDER_DETAIL_JSON = "{\"order_id\":\"${orderId}\"," +
            "\"shop_id\":${shopId}}";

    private static String STAFF_TYPE_LIST_JSON = "{\"shop_id\":${shopId}}";

    private static String STAFF_LIST_JSON = "{\"shop_id\":${shopId},\"page\":\"${page}\",\"size\":\"${pageSize}\"}";

//    ----------------------------------------------接口方法--------------------------------------------------------------------

    /**
     * 3.4 顾客列表
     */
    public JSONObject customerList(String phone, String channel, String adviser, int page, int pageSize) throws
            Exception {

        String json =
                "{\n";

        if (!"".equals(phone)) {
            json += "    \"phone_or_name\":\"" + phone + "\",";
        }

        if (!"".equals(channel)) {
            json += "    \"channel_id\":" + channel + ",";
        }

        if (!"".equals(adviser)) {
            json += "    \"adviser_id\":" + adviser + ",";
        }

        json += "    \"shop_id\":" + getShopId() + "," +
                "    \"page\":" + page + "," +
                "    \"page_size\":" + pageSize +
                "}";


        String res = httpPostWithCheckCode(CUSTOMER_LIST, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    public void newCustomer(int channelId, String channelStaffName, String channelStaffPhone, String
            adviserName, String adviserPhone, String phone, String customerName, String gender) throws Exception {

        String json =
                "{\n" +
                        "    \"customer_name\":\"" + customerName + "\"," +
                        "    \"phone\":\"" + phone + "\",";
        if (!"".equals(adviserName)) {
            json += "    \"adviser_name\":\"" + adviserName + "\",";
            json += "    \"adviser_phone\":\"" + adviserPhone + "\",";
        }

        if (channelId != -1) {
            json += "    \"channel_id\":" + channelId + "," +
                    "    \"channel_staff_name\":\"" + channelStaffName + "\"," +
                    "    \"channel_staff_phone\":\"" + channelStaffPhone + "\",";
        }

        json +=
                "    \"gender\":\"" + gender + "\"," +
                        "    \"shop_id\":" + getShopId() + "" +
                        "}";

        httpPostWithCheckCode(CUSTOMER_INSERT, json);
    }

    public String newCustomerNoCheckCode(int channelId, String channelStaffName, String channelStaffPhone, String
            adviserName,
                                         String adviserPhone, String phone, String customerName, String gender) {

        String res = "";

        String json =
                "{\n" +
                        "    \"customer_name\":\"" + customerName + "\"," +
                        "    \"phone\":\"" + phone + "\",";
        if (!"".equals(adviserName)) {
            json += "    \"adviser_name\":\"" + adviserName + "\",";
            json += "    \"adviser_phone\":\"" + adviserPhone + "\",";
        }

        if (channelId != -1) {
            json += "    \"channel_id\":" + channelId + "," +
                    "    \"channel_staff_name\":\"" + channelStaffName + "\"," +
                    "    \"channel_staff_phone\":\"" + channelStaffPhone + "\",";
        }

        json +=
                "    \"gender\":\"" + gender + "\"," +
                        "    \"shop_id\":" + getShopId() + "" +
                        "}";

        try {
            res = httpPost(CUSTOMER_INSERT, json);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return res;
    }

    public JSONObject importFile(String imagePath) {
        String url = "http://dev.store.winsenseos.cn/risk/customer/file/import";
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);

        httpPost.addHeader("authorization", authorization);
        httpPost.addHeader("shop_id", getShopId() + "");
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();

        File file = new File(imagePath);
        try {
            builder.addBinaryBody(
                    "file",
                    new FileInputStream(file),
                    ContentType.IMAGE_JPEG,
                    file.getName()
            );

            HttpEntity multipart = builder.build();
            httpPost.setEntity(multipart);
            CloseableHttpResponse response = httpClient.execute(httpPost);
            HttpEntity responseEntity = response.getEntity();
            this.response = EntityUtils.toString(responseEntity, "UTF-8");

            logger.info("response: " + this.response);

        } catch (Exception e) {
            failReason = e.getMessage();
            e.printStackTrace();
        }

        return JSON.parseObject(this.response).getJSONObject("data");
    }

    public JSONObject customerEditPC(String cid, String customerName, String phone, String adviserName, String
            adviserPhone) throws Exception {
        String url = "/risk/customer/edit/" + cid;
        String json =
                "{\n" +
                        "\"customer_name\":\"" + customerName + "\"," +
                        "\"phone\":\"" + phone + "\"," +
                        "\"adviser_name\":\"" + adviserName + "\"," +
                        "\"adviser_phone\":\"" + adviserPhone + "\"," +
                        "\"shop_id\":" + getShopId() +
                        "}";

        String res = httpPostWithCheckCode(url, json);

        Thread.sleep(1000);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * 3.10 修改顾客信息
     */
    public String customerEditPCNoCheckCode(String cid, String customerName, String phone, String
            adviserName, String adviserPhone) throws Exception {
        String url = "/risk/customer/edit/" + cid;
        String json =
                "{\n" +
                        "\"customer_name\":\"" + customerName + "\"," +
                        "\"phone\":\"" + phone + "\"," +
                        "\"adviser_name\":\"" + adviserName + "\"," +
                        "\"adviser_phone\":\"" + adviserPhone + "\"," +
                        "\"shop_id\":" + getShopId() +
                        "}";

        String res = httpPost(url, json);

        return res;
    }

//    ---------------------------------------------------到访人物列表------------------------------------------------------

    public void visitor2Staff(String customerId) throws Exception {
        String url = "/risk/evidence/person-catch/toStaff";
        String json =
                "{\n" +
                        "    \"shop_id\":4116," +
                        "    \"customer_ids\":[" +
                        "        \"" + customerId + "\"" +
                        "    ]" +
                        "}";

        String res = httpPostWithCheckCode(url, json);
    }

    public JSONObject catchList(String customerType, String deviceId, String startTime, String ensTime, int page,
                                int size) throws Exception {
        String url = "/risk/evidence/person-catch/page";
        String json =
                "{\n" +
                        "\"person_type\":\"" + customerType + "\"," +
                        "\"device_id\":\"" + deviceId + "\"," +
                        "\"start_time\":\"" + startTime + "\"," +
                        "\"end_time\":\"" + ensTime + "\"," +
                        "\"page\":\"" + page + "\"," +
                        "\"size\":\"" + size + "\"," +
                        "\"shop_id\":" + getShopId() +
                        "}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    public JSONObject channelStaffList(String namePhone, String channelId, int page, int size) throws Exception {
        String url = "/risk/channel/staff/page";
        String json =
                "{\n" +
                        "\"name_phone\":\"" + namePhone + "\"," +
                        "\"channel_id\":\"" + channelId + "\"," +
                        "\"page\":\"" + page + "\"," +
                        "\"size\":\"" + size + "\"," +
                        "\"shop_id\":" + getShopId() +
                        "}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    public JSONObject changeState(String id) throws Exception {
        String url = "/risk/channel/staff/state/change/" + id;
        String json =
                "{}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    public JSONObject staffList(String namePhone, int page, int size) throws Exception {
        String url = "/risk/staff/page";
        String json =
                "{\n" +
                        "\"name_phone\":\"" + namePhone + "\"," +
                        "\"page\":\"" + page + "\"," +
                        "\"size\":\"" + size + "\"," +
                        "\"shop_id\":" + getShopId() +
                        "}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    public JSONObject addStaff(String staffName, String phone, String faceUrl) throws Exception {

        String url = "/risk/staff/add";

        String json =
                "{\n" +
                        "    \"staff_name\":\"" + staffName + "\"," +
                        "    \"phone\":\"" + phone + "\"," +
                        "    \"face_url\":\"" + faceUrl + "\"," +
                        "\"shop_id\":" + getShopId() +
                        "}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    public String addStaffNoCode(String staffName, String phone, String faceUrl) throws Exception {

        String url = "/risk/staff/add";

        String json =
                "{\n" +
                        "    \"staff_name\":\"" + staffName + "\"," +
                        "    \"phone\":\"" + phone + "\"," +
                        "    \"face_url\":\"" + faceUrl + "\"," +
                        "\"shop_id\":" + getShopId() +
                        "}";

        String res = httpPost(url, json);

        return res;
    }

    public JSONObject staffEdit(String id, String staffName, String phone, String faceUrl) throws Exception {

        String url = "/risk/staff/edit/" + id;

        String json =
                "{\n" +
                        "    \"staff_name\":\"" + staffName + "\"," +
                        "    \"phone\":\"" + phone + "\"," +
                        "    \"face_url\":\"" + faceUrl + "\"," +
                        "\"shop_id\":" + getShopId() +
                        "}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }


    public JSONObject staffDelete(String id) throws Exception {
        String url = "/risk/staff/delete/" + id;
        String json =
                "{}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

//    --------------------------------------------渠道相关-----------------------------------------

    public void addChannel(String channelName, String owner, String phone, String ruleId) throws Exception {
        String url = "/risk/channel/add";
        String json =
                "{\n" +
                        "    \"channel_name\":\"" + channelName + "\"," +
                        "    \"owner_principal\":\"" + owner + "\"," +
                        "    \"phone\":\"" + phone + "\"," +
                        "    \"rule_id\":\"" + ruleId + "\"," +
                        "\"shop_id\":" + getShopId() +
                        "}";

        String res = httpPostWithCheckCode(url, json);
    }

    public JSONObject channelList(int page, int size) throws Exception {
        String url = "/risk/channel/page";
        String json =
                "{\n" +
                        "    \"page\":\"" + page + "\"," +
                        "    \"size\":\"" + size + "\"," +
                        "\"shop_id\":" + getShopId() +
                        "}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }


    /**
     * 编辑渠道
     */
    public void channelEdit(String channelId, String channelName, String owner, String phone, String ruleId) throws
            Exception {

        String url = "/risk/channel/edit/" + channelId;
        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + "," +
                        "    \"channel_name\":\"" + channelName + "\"," +
                        "    \"owner_principal\":\"" + owner + "\"," +
                        "    \"phone\":\"" + phone + "\"," +
                        "    \"rule_id\":\"" + ruleId + "\"" +
                        "}";

        httpPostWithCheckCode(url, json);
    }

    public void channelEditFinally(String channelId, String channelName, String owner, String phone, String ruleId) {

        String url = "/risk/channel/edit/" + channelId;
        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + "," +
                        "    \"channel_name\":\"" + channelName + "\"," +
                        "    \"owner_principal\":\"" + owner + "\"," +
                        "    \"phone\":\"" + phone + "\"," +
                        "    \"rule_id\":\"" + ruleId + "\"" +
                        "}";

        try {
            httpPostWithCheckCode(url, json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public JSONObject addChannelStaff(String channelId, String staffName, String phone) throws Exception {

        String url = "/risk/channel/staff/register";

        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + "," +
                        "    \"staff_name\":\"" + staffName + "\"," +
                        "    \"channel_id\":\"" + channelId + "\"," +
                        "    \"phone\":\"" + phone + "\"" +
                        "}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    public String addChannelStaffNoCode(String channelId, String staffName, String phone) throws Exception {

        String url = "/risk/channel/staff/register";

        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + "," +
                        "    \"staff_name\":\"" + staffName + "\"," +
                        "    \"channel_id\":\"" + channelId + "\"," +
                        "    \"phone\":\"" + phone + "\"" +
                        "}";

        String res = httpPost(url, json);

        return res;
    }

    public JSONObject editChannelStaff(String id, String channelId, String staffName, String phone, String faceUrl) throws Exception {

        String url = "/risk/channel/staff/edit/" + id;

        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + "," +
                        "    \"staff_name\":\"" + staffName + "\"," +
                        "    \"channel_id\":\"" + channelId + "\"," +
                        "    \"face_url\":\"" + faceUrl + "\"," +
                        "    \"phone\":\"" + phone + "\"" +
                        "}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    public JSONObject channelStaffList(String channelId, int page, int size) throws Exception {

        String url = "/risk/channel/staff/page";

        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + "," +
                        "    \"channel_id\":\"" + channelId + "\"," +
                        "    \"page\":\"" + page + "\"," +
                        "    \"size\":\"" + size + "\"" +
                        "}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    public void changeChannelStaffState(String staffId) throws Exception {
        String json = "{}";

        httpPostWithCheckCode("/risk/channel/staff/state/change/" + staffId, json);
    }

    /**
     * 4.4 创建订单
     */
    public JSONObject createOrder(String phone, String orderId, String faceUrl, int channelId, String smsCode) throws
            Exception {

        String json =
                "{" +
                        "    \"shop_id\":" + getShopId() + "," +
                        "    \"phone\":\"" + phone + "\"," +
                        "    \"face_url\":\"" + faceUrl + "\"," +
                        "    \"order_id\":\"" + orderId + "\",";
        if (channelId != -1) {
            json += "    \"channel_id\":\"" + channelId + "\",";
        }

        json += "    \"sms_code\":\"" + smsCode + "\"" +
                "}";
        String res = httpPostWithCheckCode(ADD_ORDER, json);

        return JSON.parseObject(res);
    }

    /**
     * 4.5 订单详情
     */
    public JSONObject orderDetail(String orderId) throws Exception {
        String json = StrSubstitutor.replace(ORDER_DETAIL_JSON, ImmutableMap.builder()
                .put("shopId", getShopId())
                .put("orderId", orderId)
                .build()
        );
        String res = httpPostWithCheckCode(ORDER_DETAIL, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * 4.6 订单关键步骤接口
     */
    public JSONObject orderLinkList(String orderId) throws Exception {
        String url = "/risk/order/link/list";
        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + "," +
                        "    \"orderId\":\"" + orderId + "\"," +
                        "    \"page\":\"" + 1 + "\"," +
                        "    \"size\":\"" + 100 + "\"" +
                        "}";

        String res = httpPostWithCheckCode(url, json);//订单详情与订单跟进详情入参json一样

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * 4.8 订单列表
     */
    public JSONObject orderList(int status, String namePhone, int pageSize) throws Exception {

        String url = "/risk/order/list";
        String json =
                "{" +
                        "    \"shop_id\":" + getShopId() + "," +
                        "    \"page\":1" + ",";
        if (status != -1) {
            json += "    \"status\":\"" + status + "\",";
        }

        if (!"".equals(namePhone)) {
            json += "    \"customer_name\":\"" + namePhone + "\",";
        }

        json += "    \"size\":" + pageSize + "" +
                "}";
        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }


    public JSONObject orderList(String channelId, String status, String isAudited, String namePhone, int pageSize) throws Exception {

        String url = "/risk/order/list";
        String json =
                "{" +
                        "    \"shop_id\":" + getShopId() + "," +
                        "    \"page\":1" + ",";
        if (!"".equals(status)) {
            json += "    \"status\":\"" + status + "\",";
        }

        if (!"".equals(namePhone)) {
            json += "    \"customer_name\":\"" + namePhone + "\",";
        }

        if (!"".equals(isAudited)) {
            json += "    \"is_audited\":\"" + isAudited + "\",";
        }

        if (!"".equals(channelId)) {
            json += "    \"channel_id\":\"" + channelId + "\",";
        }

        json += "    \"size\":" + pageSize + "" +
                "}";
        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * 6.15 渠道客户报备H5
     */
    public String customerReportH5(String staffId, String customerName, String phone, String gender, String token) throws
            Exception {
        String url = "/external/channel/customer/report";
        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + "," +
                        "    \"id\":\"" + staffId + "\"," +
                        "    \"customer_name\":\"" + customerName + "\"," +
                        "    \"customer_phone\":\"" + phone + "\"," +
                        "    \"gender\":\"" + gender + "\"," +
                        "    \"token\":\"" + token + "\"" +
                        "}\n";

        String response = httpPostWithCheckCode(url, json);

        return response;
    }

    /**
     * 6.15 渠道客户报备H5
     */
    public String customerReportH5NoCheckCode(String staffId, String customerName, String phone, String
            gender, String token) throws Exception {
        String url = "/external/channel/customer/report";
        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + "," +
                        "    \"id\":\"" + staffId + "\"," +
                        "    \"customer_name\":\"" + customerName + "\"," +
                        "    \"customer_phone\":\"" + phone + "\"," +
                        "    \"gender\":\"" + gender + "\"," +
                        "    \"token\":\"" + token + "\"" +
                        "}\n";

        String response = httpPost(url, json);

        return response;
//        return JSON.parseObject(response);
    }

    /**
     * 9.6 自主注册
     */
    public JSONObject selfRegister(String customerName, String phone, String verifyCode, String adviserId, String
            hotPoints, String gender) throws Exception {
        String url = "/external/self-register/confirm";

        String json =
                "{\n" +
                        "    \"name\":\"" + customerName + "\"," +
                        "    \"gender\":\"" + gender + "\"," +
                        "    \"phone\":\"" + phone + "\"," +
                        "    \"verify_code\":\"" + verifyCode + "\",";
        if (!"".equals(adviserId)) {
            json += "    \"adviser_id\":\"" + adviserId + "\",";
        }
        if (!"".equals(hotPoints)) {
            json += "    \"hot_points\":[1],";
        }

        json += "    \"shop_id\":" + getShopId() + "}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * 16.1 新增风控规则
     */
    public String addRiskRuleNoCheckCode(String name, String aheadReportTime, String reportProtect) throws
            Exception {

        String url = "/risk/rule/add";
        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + "," +
                        "    \"name\":\"" + name + "\"," +
                        "    \"ahead_report_time\":\"" + aheadReportTime + "\"," +
                        "    \"report_protect\":\"" + reportProtect + "\"" +
                        "}";

        return httpPost(url, json);
    }

    /**
     * 16.1 新增风控规则
     */
    public void addRiskRule(String name, String aheadReportTime, String reportProtect) throws Exception {

        String url = "/risk/rule/add";
        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + "," +
                        "    \"name\":\"" + name + "\"," +
                        "    \"ahead_report_time\":\"" + aheadReportTime + "\"," +
                        "    \"report_protect\":\"" + reportProtect + "\"" +
                        "}";

        httpPostWithCheckCode(url, json);
    }

    /**
     * 16.1 风控规则列表
     */
    public JSONObject riskRuleList() throws Exception {

        String url = "/risk/rule/list";
        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + "," +
                        "    \"page\":\"" + 1 + "\"," +
                        "    \"size\":\"" + 100 + "\"" +
                        "}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");


    }

    /**
     * 16.1 删除风控规则
     */
    public void deleteRiskRule(String id) throws Exception {

        String url = "/risk/rule/delete";
        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + "," +
                        "    \"id\":\"" + id + "\"" +
                        "}";

        httpPostWithCheckCode(url, json);
    }

    public String deleteRiskRuleNoCheckCode(String id) throws Exception {

        String url = "/risk/rule/delete";
        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + "," +
                        "    \"id\":\"" + id + "\"" +
                        "}";

        String s = httpPost(url, json);
        return s;
    }

    public String refreshQrcodeNoCheckCode() throws Exception {

        String url = "/risk/shop/ocr/qrcode/refresh";
        String json =
                "{}";

        String res = httpPost(url, json);

        return res;
    }

    public String confirmQrcodeNoCheckCode(String code) throws Exception {

        String url = "/external/ocr/code/confirm";
        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + "," +
                        "    \"code\":\"" + code + "\"" +
                        "}";


        String res = httpPost(url, json);

        return res;

    }

    /**
     * 17.3 OCR验证码确认-H5
     */
    private static String OCR_PIC_UPLOAD_JSON = "{\"shop_id\":${shopId},\"token\":\"${token}\"," +
            "\"identity_card\":\"${idCard}\",\"face\":\"${face}\"}";

    public String ocrPicUpload(String token, String idCard, String face) throws Exception {

        String url = "/external/ocr/pic/upload";
        String json = StrSubstitutor.replace(OCR_PIC_UPLOAD_JSON, ImmutableMap.builder()
                .put("shopId", getShopId())
                .put("token", token)
                .put("idCard", idCard)
                .put("face", face)
                .build()
        );

        String res = httpPostNoPrintPara(url, json);

        return res;
    }

    public JSONObject uploadImage(String imagePath) {
        String url = "http://dev.store.winsenseos.cn/risk/imageUpload";
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);

        httpPost.addHeader("authorization", authorization);
        httpPost.addHeader("shop_id", String.valueOf(getShopId()));
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();

        File file = new File(imagePath);
        try {
            builder.addBinaryBody(
                    "img_file",
                    new FileInputStream(file),
                    ContentType.IMAGE_JPEG,
                    file.getName()
            );

            builder.addTextBody("path", "shopStaff", ContentType.TEXT_PLAIN);

            HttpEntity multipart = builder.build();
            httpPost.setEntity(multipart);
            CloseableHttpResponse response = httpClient.execute(httpPost);
            HttpEntity responseEntity = response.getEntity();
            this.response = EntityUtils.toString(responseEntity, "UTF-8");

            checkCode(this.response, StatusCode.SUCCESS, file.getName() + ">>>");
            logger.info("response: " + this.response);

        } catch (Exception e) {
            failReason = e.toString();
            e.printStackTrace();
        }

        return JSON.parseObject(this.response).getJSONObject("data");
    }

    private void setBasicParaToDB(Case aCase, String ciCaseName, String caseName, String caseDesc) {
        aCase.setApplicationId(APP_ID);
        aCase.setConfigId(CONFIG_ID);
        aCase.setCaseName(caseName);
        aCase.setCaseDescription(caseDesc);
        aCase.setCiCmd(CI_CMD + ciCaseName);
        aCase.setQaOwner("于海生");
        aCase.setExpect("code==1000");
        aCase.setResponse(response);

        if (!StringUtils.isEmpty(failReason) || !StringUtils.isEmpty(aCase.getFailReason())) {
            if (failReason.contains("java.lang.Exception:")) {
                failReason = failReason.replace("java.lang.Exception", "异常");
            } else if (failReason.contains("java.lang.AssertionError")) {
                failReason = failReason.replace("java.lang.AssertionError", "异常");
            }
            aCase.setFailReason(failReason);
        } else {
            aCase.setResult("PASS");
        }
    }

    private void saveData(Case aCase, String ciCaseName, String caseName, String caseDescription) {
        setBasicParaToDB(aCase, ciCaseName, caseName, caseDescription);
        qaDbUtil.saveToCaseTable(aCase);
        if (!StringUtils.isEmpty(aCase.getFailReason())) {
            logger.error(aCase.getFailReason());

            String failReason = aCase.getFailReason();

            dingPush("飞单日常-廖祥茹 \n" +
                    "验证：" + aCase.getCaseDescription() +
                    " \n\n" + failReason);
        }
    }

    private void dingPush(String msg) {
        AlarmPush alarmPush = new AlarmPush();
        if (DEBUG.trim().toLowerCase().equals("false")) {
            alarmPush.setDingWebhook(DingWebhook.QA_TEST_GRP);
//            alarmPush.setDingWebhook(DingWebhook.OPEN_MANAGEMENT_PLATFORM_GRP);
        } else {
            alarmPush.setDingWebhook(DingWebhook.QA_TEST_GRP);
        }
        alarmPush.dailyRgn(msg);
        this.FAIL = true;
        Assert.assertNull(aCase.getFailReason());
    }

    private void dingPushFinal() {
        if (DEBUG.trim().toLowerCase().equals("false") && FAIL) {
            AlarmPush alarmPush = new AlarmPush();

            alarmPush.setDingWebhook(DingWebhook.QA_TEST_GRP);
//            alarmPush.setDingWebhook(DingWebhook.OPEN_MANAGEMENT_PLATFORM_GRP);

            //15898182672 华成裕
            //18513118484 杨航
            //15011479599 谢志东
            //18600872221 蔡思明
            String[] rd = {"18513118484", //杨航
                    "15011479599", //谢志东
                    "15898182672"}; //华成裕
            alarmPush.alarmToRd(rd);
        }
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
}