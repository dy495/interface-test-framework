package com.haisheng.framework.testng.bigScreen.feidanDaily;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.model.bean.Case;
import com.haisheng.framework.util.DateTimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.*;

public class FeidanWithoutCodeDaily {

    public Logger logger = LoggerFactory.getLogger(this.getClass());
    public String failReason = "";
    public String response = "";
    public boolean FAIL = false;
    public Case aCase = new Case();
    public String CURRENT_CASE = "";

    Feidan feidan = new Feidan();
    DateTimeUtil dateTimeUtil = new DateTimeUtil();

    String natureCustomer = "NATURE";
    String channelCustomer = "CHANNEL";

    //  -----------------------------------------渠道------------------------------------------
    String wudongChannelIdStr = "5";
    String wudongChannelNameStr = "测试FREEZE";
    String wudongChannelowner_id = "uid_e07e08c7";
    int wudongChannelInt = 5;
    String wudongOwnerPhone = "16600000000";

    int maiTianChannelInt = 2;

    String lianjiaChannelStr = "1";
    String lianjiaChannelName = "链家";
    String lianjiaOwnerPhone = "16600000001";
    String lianjiaChannelowner_id= "uid_58ee7ff2";

//  ------------------------------------------业务员-----------------------------------------------------

    String lianjiaToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiLmtYvor5XjgJDli7_liqjjgJEiLCJ1aWQiOjIxMzYsImxvZ2luVGltZSI6MT" +
            "U3ODk5OTY2NjU3NH0.kQsEw_wGVmPQ4My1p-FNZ556FJC7W177g7jfjFarTu4";
    String lianjiaFreezeStaffIdStr = "2124"; //2136
    int lianjiaFreezeStaffIdInt = 2124; //2136

    String lianjiaStaffIdStr = "2124"; //2136
    String lianjiaStaffName = "链家业务员";
    String lianjiaStaffPhone = "17711111024";


    String wudongToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiLjgJDli7_li" +
            "qjjgJExIiwidWlkIjoyMDk4LCJsb2dpblRpbWUiOjE1Nzg1NzQ2MjM4NDB9.exDJ6avJKJd3ezQkYc4fmUkHvXaukqfgjThkpoYgnAw";

    String wudongStaffIdStr = "2098"; //17722222221
    int wudongStaffIdInt = 2098;

    String maitianStaffName = "喵喵喵";
    String maitianStaffPhone = "14422110039";

//    -------------------------------------------置业顾问-----------------------------------------------------

    String anShengIdStr = "15";
    String anShengName = "安生";
    String anShengPhone = "16622222222";

    String zhangName = "张钧甯";
    String zhangPhone = "19111311116";

    long wudongReportTime = 1547024264000L;//2019-01-09 16:57:44
    long lianjiaReportTime = 1547014265000L;//2019-01-09 14:11:05
    long noChannelReportTime = 1547034265000L;//2019-01-09 19:44:25

    //long firstAppearTime = 1582684439509L; //原
    //long firstAppearTime = 1583900897877L;
    //long firstAppearTime = 1584936946964L;
    long firstAppearTime = 1609733247344L;

    String defaultRuleId = "837";
    String ahead1hRuleId = "996";
    String ahead24hRuleId = "842";
    String ahead7dayRuleId = "844";
    String ahead30dayRuleId = "846";
    String aheadMaxRuleId = "1003";

    String protect1DayRuleId = "840";
    String protect10DayRuleId = "2720";
    String protect30DayRuleId = "2721";
    String protect100DayRuleId = "2719";
    String protect365DayRuleId = "2722";
    String protect10000DayRuleId = "2723";

    int pageSize = 10000;

//    --------------------------------------------------------正常单------------------------------------------

    /**
     * H5报备-顾客到场-创单（选择H5渠道）-更改置业顾问2次，置业顾问张钧甯
     * 根据不同的规则
     */
    @Test(dataProvider = "NORMAL")
    public void _H5ARule(String caseNamePro, String ruleId, long reportTime) {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName + caseNamePro;

        String caseDesc = "H5报备-顾客到场-创单（选择H5渠道）规则为提前报备时长：" + caseNamePro;

        logger.info("\n\n" + caseName + "\n");

        try {

            feidan.channelEdit(wudongChannelIdStr, wudongChannelNameStr, wudongChannelowner_id, wudongOwnerPhone, ruleId);

            // 报备
            String customerPhone = "14422110014";
            String smsCode = "202593";

            String customerName = caseName + "-" + feidan.getNamePro();
            feidan.customerReportH5(wudongStaffIdStr, customerName, customerPhone, "MALE", wudongToken);

//            更改报备时间
            feidan.updateReportTimeChannel(customerPhone, customerName, wudongChannelInt, wudongStaffIdInt, reportTime);

//            刷证
            feidan.witnessUpload(feidan.genCardId(), customerName);

            JSONArray list = feidan.orderList(-1, customerName, 10).getJSONArray("list");
            String orderId = list.getJSONObject(0).getString("order_id");

            String faceUrl = "witness/2224020000000100015/1c32c393-21c2-48b2-afeb-11c197436194";

//            创单
            feidan.createOrderNOCode(customerPhone, orderId, faceUrl, 5);

//            校验
            String adviserName = "-";
            String channelName = "测试FREEZE";
            String channelStaffName = "FREEZE1";
            String orderStatusTips = "正常";
            String firstAppear = firstAppearTime + "";

            String customerType = "渠道访客";
            String visitor = channelCustomer;

            JSONObject orderLinkData = feidan.orderLinkList(orderId);
            JSONObject orderDetail = feidan.orderDetail(orderId);

//            订单详情
            feidan.checkDetail(orderId, customerName, customerPhone, adviserName, channelName, channelStaffName, orderStatusTips,
                    faceUrl, firstAppear, reportTime + "", orderDetail);

//        订单详情，列表，关键环节中信息一致性
            feidan.detailListLinkConsist(orderId, customerPhone);

//        订单环节风险/正常
            feidan.checkNormalOrderLink(orderId, orderLinkData);

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            feidan.channelEditFinally(wudongChannelIdStr, wudongChannelNameStr, wudongChannelowner_id, wudongOwnerPhone, defaultRuleId);
            feidan.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    /**
     * PC（有渠道）-顾客到场,置业顾问是张钧甯
     * 选PC报备渠道
     */
    @Test
    public void _PCTAAhead1hR() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "PC（渠道报备）-顾客到场-创单（选择PC报备渠道）,规则为提前报备时长：1h";

        logger.info("\n\n" + caseName + "\n");

        try {

            feidan.channelEdit(lianjiaChannelStr, lianjiaChannelName, lianjiaChannelowner_id, lianjiaOwnerPhone, ahead1hRuleId);

            // PC报备
            String customerPhone = "14422110015";
            String smsCode = "805931";
            String customerName = caseName + "-" + feidan.getNamePro();
            String adviserName = zhangName;
            String adviserPhone = zhangPhone;
            int channelId = 1;
            int channelStaffId = 2124;
            String channelStaffName = lianjiaStaffName;
            String channelStaffPhone = lianjiaStaffPhone;
            feidan.newCustomer(channelId, channelStaffName, channelStaffPhone, adviserName, adviserPhone, customerPhone, customerName, "MALE");
            long reportTime = firstAppearTime - (60 + 1) * 60 * 1000L;
            feidan.updateReportTimeChannel(customerPhone, customerName, channelId, channelStaffId, reportTime);

//            刷证
            feidan.witnessUpload(feidan.genCardId(), customerName);

            JSONArray list = feidan.orderList(-1, customerName, 10).getJSONArray("list");
            String orderId = list.getJSONObject(0).getString("order_id");

//            创单
            String faceUrl = "witness/2224020000000100015/1c32c393-21c2-48b2-afeb-11c197436194";
            feidan.createOrderNOCode(customerPhone, orderId, faceUrl, channelId);

//            校验
            String channelName = "链家";
            String orderStatusTips = "正常";
            String firstAppear = firstAppearTime + "";

            String customerType = "自然访客";
            String visitor = natureCustomer;

            JSONObject orderLinkData = feidan.orderLinkList(orderId);
            JSONObject orderDetail = feidan.orderDetail(orderId);

//            订单详情
            feidan.checkDetail(orderId, customerName, customerPhone, adviserName, channelName, channelStaffName, orderStatusTips,
                    faceUrl, firstAppear, reportTime + "", orderDetail);

//        订单详情，列表，关键环节中信息一致性
            feidan.detailListLinkConsist(orderId, customerPhone);

//        订单环节风险/正常
            feidan.checkNormalOrderLink(orderId, orderLinkData);

//            场内轨迹
            feidan.checkFirstVisitAndTrace(orderId, orderLinkData, true);

//            审核
            feidan.orderAudit(orderId, visitor);

//            校验风控单
            feidan.checkReport(orderId, orderStatusTips, orderLinkData.getJSONArray("list").size() + 1, customerType, orderDetail);

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            feidan.channelEditFinally(lianjiaChannelStr, lianjiaChannelName, lianjiaChannelowner_id, lianjiaOwnerPhone, defaultRuleId);
            feidan.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    /**
     * PC（无渠道）-顾客到场-创单（选择无渠道），置业顾问是张钧甯
     * 选无渠道
     * 成单后是否在顾客列表中消失
     */
    @Test
    public void _PCFA() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "PC（无渠道）-顾客到场-创单（选择无渠道）,规则为提前报备时长：0min";

        logger.info("\n\n" + caseName + "\n");

        try {

            // PC报备
            String customerPhone = "14422110017";
            String smsCode = "133345";
            String customerName = caseName + "-" + feidan.getNamePro();
            String adviserName = zhangName;
            String adviserPhone = zhangPhone;
            int channelId = -1;

            feidan.newCustomer(channelId, "", "", adviserName, adviserPhone, customerPhone, customerName, "MALE");

//            更改置业顾问2次
            JSONArray list = feidan.customerList(customerName, "", "", 1, 10).getJSONArray("list");

            String cid = list.getJSONObject(0).getString("cid");

            feidan.customerEditPC(cid, customerName, customerPhone, anShengName, anShengPhone);
            feidan.customerEditPC(cid, customerName, customerPhone, zhangName, zhangPhone);

            feidan.updateReportTime_PCF(customerPhone, customerName, noChannelReportTime);

//            刷证
            feidan.witnessUpload(feidan.genCardId(), customerName);

            list = feidan.orderList(-1, customerName, 10).getJSONArray("list");
            String orderId = list.getJSONObject(0).getString("order_id");

//            创单
            String faceUrl = "witness/2224020000000100015/1c32c393-21c2-48b2-afeb-11c197436194";
            feidan.createOrderNOCode(customerPhone, orderId, faceUrl, channelId);

            //成单后是否在顾客列表中消失
            list = feidan.customerList(customerName, "", "", 1, 10).getJSONArray("list");
            if (list.size() != 0) {
                throw new Exception("成单后该顾客没有在顾客列表中消失。customerName =" + customerName);
            }

//            校验
            String channelName = "-";
            String channelStaffName = "-";
            String orderStatusTips = "正常";
            String firstAppear = firstAppearTime + "";
            String reportTime = "-";
            String orderType = "正常";
            String customerType = "自然访客";
            String visitor = natureCustomer;

            JSONObject orderLinkData = feidan.orderLinkList(orderId);
            JSONObject orderDetail = feidan.orderDetail(orderId);

//            订单详情
            feidan.checkDetail(orderId, customerName, customerPhone, adviserName, channelName, channelStaffName, orderStatusTips,
                    faceUrl, firstAppear, reportTime, orderDetail);

//        订单详情，列表，关键环节中信息一致性
            feidan.detailListLinkConsist(orderId, customerPhone);

//        订单环节风险/正常
            feidan.checkNormalOrderLink(orderId, orderLinkData);

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
     * 自助扫码(选自助)-顾客到场，置业顾问：安生
     */
    @Test
    public void _SA() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "自助扫码（选自助）顾客到场--创单（选择无渠道）,规则为提前报备时长：0min";

        logger.info("\n\n" + caseName + "\n");

        try {
            String customerPhone = "18210113587";
            String selfCode = "805805";
            String smsCode = "805805";
            String customerName = caseName + "-" + feidan.getNamePro();

//            自助扫码
            feidan.selfRegister(customerName, customerPhone, selfCode, anShengIdStr, "dd", "MALE");

//            更改置业顾问2次
            JSONArray list = feidan.customerList(customerName, "", "", 1, 10).getJSONArray("list");

            String cid = list.getJSONObject(0).getString("cid");

            feidan.customerEditPC(cid, customerName, customerPhone, anShengName, anShengPhone);
            feidan.customerEditPC(cid, customerName, customerPhone, zhangName, zhangPhone);

//            更改登记时间
            feidan.updateReportTime_S(customerPhone, customerName, noChannelReportTime);

//            刷证
            feidan.witnessUpload(feidan.genCardId(), customerName);

            list = feidan.orderList(-1, customerName, 10).getJSONArray("list");
            String orderId = list.getJSONObject(0).getString("order_id");

//            创单
            String faceUrl = "witness/2224020000000100015/1c32c393-21c2-48b2-afeb-11c197436194";
            feidan.createOrderNOCode(customerPhone, orderId, faceUrl, -1);

//            校验
            String adviserName = zhangName;
            String channelName = "-";
            String channelStaffName = "-";
            String orderStatusTips = "正常";
            String firstAppear = firstAppearTime + "";
            String reportTime = "-";

            String customerType = "自然访客";

            JSONObject orderLinkData = feidan.orderLinkList(orderId);
            JSONObject orderDetail = feidan.orderDetail(orderId);

//            订单详情
            feidan.checkDetail(orderId, customerName, customerPhone, adviserName, channelName, channelStaffName, orderStatusTips,
                    faceUrl, firstAppear, reportTime, orderDetail);

//        订单详情，列表，关键环节中信息一致性
            feidan.detailListLinkConsist(orderId, customerPhone);

//        订单环节风险/正常
            feidan.checkNormalOrderLink(orderId, orderLinkData);

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
     * H5报备-顾客到场-自助扫码(选H5)
     */
    @Test
    public void _H5AS() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "H5报备-顾客到场-自助扫码-创单（选择H5报备渠道）,规则为提前报备时长：0min";

        logger.info("\n\n" + caseName + "\n");

        try {
            String customerPhone = "18210113587";
            String selfCode = "805805";
            String smsCode = "805805";
            String customerName = caseName + "-" + feidan.getNamePro();

//            H5报备
            feidan.customerReportH5(wudongStaffIdStr, customerName, customerPhone, "MALE", wudongToken);

//            更改报备时间
            feidan.updateReportTimeChannel(customerPhone, customerName, 5, 2098, wudongReportTime);

//            自助扫码
            feidan.selfRegister(customerName, customerPhone, selfCode, anShengIdStr, "dd", "MALE");

//            刷证
            feidan.witnessUpload(feidan.genCardId(), customerName);

            JSONArray list = feidan.orderList(-1, customerName, 10).getJSONArray("list");
            String orderId = list.getJSONObject(0).getString("order_id");

//            创单
            String faceUrl = "witness/2224020000000100015/1c32c393-21c2-48b2-afeb-11c197436194";
            feidan.createOrderNOCode(customerPhone, orderId, faceUrl, 5);

//            校验
            String adviserName = "-";
            String channelName = "测试FREEZE";
            String channelStaffName = "FREEZE1";
            String orderStatusTips = "正常";
            String firstAppear = firstAppearTime + "";
            String reportTime = wudongReportTime + "";

            String orderType = "正常";
            String customerType = "渠道访客";
            String visitor = channelCustomer;

            JSONObject orderLinkData = feidan.orderLinkList(orderId);
            JSONObject orderDetail = feidan.orderDetail(orderId);

//            订单详情
            feidan.checkDetail(orderId, customerName, customerPhone, adviserName, channelName, channelStaffName, orderStatusTips,
                    faceUrl, firstAppear, reportTime + "", orderDetail);

//        订单详情，列表，关键环节中信息一致性
            feidan.detailListLinkConsist(orderId, customerPhone);

//        订单环节风险/正常
            feidan.checkNormalOrderLink(orderId, orderLinkData);

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
     * H5报备-顾客到场-自助扫码(选H5)
     */
    @Test
    public void _H5APCF() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "H5报备-顾客到场-PC（无渠道）-创单（选择H5报备渠道）,规则为默认规则";

        logger.info("\n\n" + caseName + "\n");

        try {

            String customerPhone = "14422110017";
            String smsCode = "133345";
            String customerName = caseName + "-" + feidan.getNamePro();
            int channelId = -1;

//            H5报备
            feidan.customerReportH5(wudongStaffIdStr, customerName, customerPhone, "MALE", wudongToken);

//            更改报备时间
            feidan.updateReportTimeChannel(customerPhone, customerName, 5, 2098, wudongReportTime);

//            pc无渠道登记
            feidan.newCustomer(channelId, "", "", "", "", customerPhone, customerName, "MALE");

//            刷证
            feidan.witnessUpload(feidan.genCardId(), customerName);

            JSONArray list = feidan.orderList(-1, customerName, 10).getJSONArray("list");
            String orderId = list.getJSONObject(0).getString("order_id");

//            创单
            String faceUrl = "witness/2224020000000100015/1c32c393-21c2-48b2-afeb-11c197436194";
            feidan.createOrderNOCode(customerPhone, orderId, faceUrl, 5);

//            校验
            String adviserName = "-";
            String channelName = "测试FREEZE";
            String channelStaffName = "FREEZE1";
            String orderStatusTips = "正常";
            String firstAppear = firstAppearTime + "";
            String reportTime = wudongReportTime + "";

            String orderType = "正常";
            String customerType = "渠道访客";
            String visitor = channelCustomer;

            JSONObject orderLinkData = feidan.orderLinkList(orderId);
            JSONObject orderDetail = feidan.orderDetail(orderId);

//            订单详情
            feidan.checkDetail(orderId, customerName, customerPhone, adviserName, channelName, channelStaffName, orderStatusTips,
                    faceUrl, firstAppear, reportTime + "", orderDetail);

//        订单详情，列表，关键环节中信息一致性
            feidan.detailListLinkConsist(orderId, customerPhone);

//        订单环节风险/正常
            feidan.checkNormalOrderLink(orderId, orderLinkData);

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

//    -------------------------------------------------异常单-------------------------------------------------------

    /**
     * 顾客到场-H5，无置业顾问
     * 选H5
     */
    @Test(dataProvider = "RISK_1")
    public void A_H5Rule(String caseNamePro, String ruleId, String aheadTime, long reportTime) {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName + "-" + caseNamePro;

        String caseDesc = "顾客到场-H5报备-创单（选择H5报备渠道）,规则为提前报备时长为：" + caseNamePro;

        logger.info("\n\n" + caseName + "\n");

        try {

            feidan.channelEdit(wudongChannelIdStr, wudongChannelNameStr, wudongChannelowner_id, wudongOwnerPhone, ruleId);

            // 报备
            String customerPhone = "18210113587";
            String smsCode = "805805";

            String customerName = caseName + "-" + feidan.getNamePro();

            feidan.customerReportH5(wudongStaffIdStr, customerName, customerPhone, "MALE", wudongToken);

            if (reportTime > System.currentTimeMillis()) {
                reportTime = System.currentTimeMillis();
            }

            feidan.updateReportTimeChannel(customerPhone, customerName, 5, 2098, reportTime);

//            刷证
            feidan.witnessUpload(feidan.genCardId(), customerName);

            JSONArray list = feidan.orderList(-1, customerName, 10).getJSONArray("list");
            String orderId = list.getJSONObject(0).getString("order_id");

            String faceUrl = "witness/2224020000000100015/1c32c393-21c2-48b2-afeb-11c197436194";

//            创单
            feidan.createOrderNOCode(customerPhone, orderId, faceUrl, 5);

//            校验
            String adviserName = "-";
            String channelName = "测试FREEZE";
            String channelStaffName = "FREEZE1";
            String orderStatusTips = "风险";
            String firstAppear = firstAppearTime + "";
            int riskNum = 2;
            int riskNumA = 3;

            String customerType = "渠道访客";
            String visitor = channelCustomer;

            JSONObject orderLinkData = feidan.orderLinkList(orderId);
            JSONObject orderDetail = feidan.orderDetail(orderId);

//            订单详情
            feidan.checkDetail(orderId, customerName, customerPhone, adviserName, channelName, channelStaffName, orderStatusTips,
                    faceUrl, firstAppear, reportTime + "", orderDetail);

//        订单详情，列表，关键环节中信息一致性
            feidan.detailListLinkConsist(orderId, customerPhone);

//        订单环节风险/正常
            feidan.checkOrderRiskLinkMess(orderId, orderLinkData, "RISK_STATUS_CHANGE", "订单风险状态:未知->风险", "存在2个异常环节");
            feidan.checkOrderRiskLinkMess(orderId, orderLinkData, "RISK_RULE", "报备时间需大于" + aheadTime, "该顾客的风控规则为提前报备时间:" + aheadTime);
            feidan.checkOrderRiskLinkNum(orderId, orderLinkData, riskNum);

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            feidan.channelEditFinally(wudongChannelIdStr, wudongChannelNameStr, wudongChannelowner_id, wudongOwnerPhone, defaultRuleId);
            feidan.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    /**
     * H5报备-顾客到场-自助扫码(选H5)
     */
    @Test
    public void H5_SA() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "H5报备-顾客到场-自助扫码-创单（选择H5报备渠道）,规则为默认规则";

        logger.info("\n\n" + caseName + "\n");

        try {
            String customerPhone = "18210113587";
            String selfCode = "805805";
            String smsCode = "805805";
            String customerName = caseName + "-" + feidan.getNamePro();

//            H5报备
            feidan.customerReportH5(wudongStaffIdStr, customerName, customerPhone, "MALE", wudongToken);

//            更改报备时间
            feidan.updateReportTimeChannel(customerPhone, customerName, 5, 2098, wudongReportTime);

//            自助扫码
            feidan.selfRegister(customerName, customerPhone, selfCode, anShengIdStr, "dd", "MALE");
            feidan.updateReportTime_S(customerPhone, customerName, noChannelReportTime);

//            刷证
            feidan.witnessUpload(feidan.genCardId(), customerName);

            JSONArray list = feidan.orderList(-1, customerName, 10).getJSONArray("list");
            String orderId = list.getJSONObject(0).getString("order_id");

//            创单
            String faceUrl = "witness/2224020000000100015/1c32c393-21c2-48b2-afeb-11c197436194";
            feidan.createOrderNOCode(customerPhone, orderId, faceUrl, -1);

//            校验
            String adviserName = "安生";
            String channelName = "-";
            String channelStaffName = "-";
            String orderStatusTips = "风险";
            String firstAppear = firstAppearTime + "";
            String reportTime = "-";

            int riskNum = 3;
            int riskNumA = 4;

            String customerType = "自然访客";
            String visitor = natureCustomer;

            JSONObject orderLinkData = feidan.orderLinkList(orderId);
            JSONObject orderDetail = feidan.orderDetail(orderId);

//            订单详情
            feidan.checkDetail(orderId, customerName, customerPhone, adviserName, channelName, channelStaffName, orderStatusTips,
                    faceUrl, firstAppear, reportTime + "", orderDetail);

//        订单详情，列表，关键环节中信息一致性
            feidan.detailListLinkConsist(orderId, customerPhone);

//        订单环节风险/正常
            feidan.checkOrderRiskLinkMess(orderId, orderLinkData, "RISK_STATUS_CHANGE", "订单风险状态:未知->风险", "存在3个异常环节");
            feidan.checkOrderRiskLinkMess(orderId, orderLinkData, "CUSTOMER_CONFIRM_INFO", "顾客在确认信息时表明无渠道介绍", "该顾客成为自然访客");
            feidan.checkOrderRiskLinkMess(orderId, orderLinkData, "CHANNEL_REPORT", "测试FREEZE-FREEZE1", "异常提示:多个渠道报备同一顾客");
            feidan.checkOrderRiskLinkNum(orderId, orderLinkData, riskNum);

//        场内轨迹
            feidan.checkFirstVisitAndTrace(orderId, orderLinkData, true);

//            审核
            feidan.orderAudit(orderId, visitor);

//            校验风控单
            feidan.checkReport(orderId, orderStatusTips, riskNumA, customerType, orderDetail);
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
     * H5报备-顾客到场-H5报备(不同渠道)，无置业顾问
     * 选后者
     */
    @Test(dataProvider = "RISK_2")
    public void H5A_H5Rule(String caseNamePro, String ruleId, String aheadTime, long reportTime1, long reportTime2) {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName + "-" + caseNamePro;

        String caseDesc = "H5报备（渠道A）-顾客到场-H5报备(渠道B)--创单（选择渠道B）,规则为提前报备时长为：" + caseNamePro;

        logger.info("\n\n" + caseName + "\n");

        try {
            feidan.channelEdit(lianjiaChannelStr, lianjiaChannelName, lianjiaChannelowner_id, lianjiaOwnerPhone, ruleId);

            feidan.channelEdit(wudongChannelIdStr, wudongChannelNameStr, wudongChannelowner_id, wudongOwnerPhone, ruleId);

            String customerPhone = "14422110004";
            String customerName = caseName + "-" + feidan.getNamePro();

//            H5报备（链家）
            feidan.customerReportH5(lianjiaFreezeStaffIdStr, customerName, customerPhone, "MALE", lianjiaToken);

//            更改报备时间

            feidan.updateReportTimeChannel(customerPhone, customerName, 1, lianjiaFreezeStaffIdInt, reportTime1);

//            H5报备（测试FREEZE）
            feidan.customerReportH5(wudongStaffIdStr, customerName, customerPhone, "MALE", wudongToken);
            feidan.updateReportTimeChannel(customerPhone, customerName, wudongChannelInt, wudongStaffIdInt, reportTime2);

//            刷证
            feidan.witnessUpload(feidan.genCardId(), customerName);

            JSONArray list = feidan.orderList(-1, customerName, 10).getJSONArray("list");
            String orderId = list.getJSONObject(0).getString("order_id");

//            创单
            String faceUrl = "witness/2224020000000100015/1c32c393-21c2-48b2-afeb-11c197436194";
            String smsCode = "704542";
            feidan.createOrderNOCode(customerPhone, orderId, faceUrl, wudongChannelInt);

//            校验
            String adviserName = "-";
            String channelName = "测试FREEZE";
            String channelStaffName = "FREEZE1";
            String orderStatusTips = "风险";
            String firstAppear = firstAppearTime + "";

            int riskNum = 3;
            int riskNumA = 4;

            String customerType = "渠道访客";
            String visitor = channelCustomer;

            JSONObject orderLinkData = feidan.orderLinkList(orderId);
            JSONObject orderDetail = feidan.orderDetail(orderId);

//            订单详情
            feidan.checkDetail(orderId, customerName, customerPhone, adviserName, channelName, channelStaffName, orderStatusTips,
                    faceUrl, firstAppear, reportTime2 + "", orderDetail);

//        订单详情，列表，关键环节中信息一致性
            feidan.detailListLinkConsist(orderId, customerPhone);

//        订单环节风险/正常
            feidan.checkOrderRiskLinkMess(orderId, orderLinkData, "RISK_STATUS_CHANGE", "订单风险状态:未知->风险", "存在3个异常环节");
            feidan.checkOrderRiskLinkMess(orderId, orderLinkData, "RISK_RULE", "报备时间需大于" + aheadTime, "该顾客的风控规则为提前报备时间:" + aheadTime);
            feidan.checkOrderRiskLinkMess(orderId, orderLinkData, "CHANNEL_REPORT", "链家-链家业务员", "异常提示:多个渠道报备同一顾客");
            feidan.checkOrderRiskLinkNum(orderId, orderLinkData, riskNum);

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            feidan.channelEditFinally(lianjiaChannelStr, lianjiaChannelName, lianjiaChannelowner_id, lianjiaOwnerPhone, defaultRuleId);
            feidan.channelEditFinally(wudongChannelIdStr, wudongChannelNameStr, wudongChannelowner_id, wudongOwnerPhone, defaultRuleId);
            feidan.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    /**
     * H5报备-顾客到场-PC报备，置业顾问：张钧甯
     * 选PC
     */

    @Test(dataProvider = "RISK_2_2")
    public void H5A_PCT(String caseNamePro, String ruleId, String aheadTime, long reportTime1, long reportTime2) {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName + "-" + caseNamePro;

        String caseDesc = "H5报备-顾客到场-PC报备-创单（选择PC报备渠道）,规则为默认规则";

        logger.info("\n\n" + caseName + "\n");

        try {

            feidan.channelEdit(lianjiaChannelStr, lianjiaChannelName, lianjiaChannelowner_id, lianjiaOwnerPhone, ruleId);

            feidan.channelEdit(wudongChannelIdStr, wudongChannelNameStr, wudongChannelowner_id, wudongOwnerPhone, ruleId);

            String customerPhone = "14422110005";
            String customerName = ciCaseName + "-" + feidan.getNamePro();

            // PC报备
            int channelId = 1;
            int channelStaffId = 2124;

            String channelStaffName = lianjiaStaffName;
            String channelStaffPhone = lianjiaStaffPhone;
            String adviserName = zhangName;
            String adviserPhone = zhangPhone;

            feidan.newCustomer(channelId, channelStaffName, channelStaffPhone, adviserName, adviserPhone, customerPhone, customerName, "MALE");

//            更改PC报备时间
            feidan.updateReportTimeChannel(customerPhone, customerName, channelId, channelStaffId, reportTime2);

//            H5报备
            feidan.customerReportH5("2098", customerName, customerPhone, "MALE", wudongToken);

//            更改报备时间
            feidan.updateReportTimeChannel(customerPhone, customerName, 5, 2098, reportTime1);

//            刷证
            feidan.witnessUpload(feidan.genCardId(), customerName);

            JSONArray list = feidan.orderList(-1, customerName, 10).getJSONArray("list");
            String orderId = list.getJSONObject(0).getString("order_id");

//            创单
            String faceUrl = "witness/2224020000000100015/1c32c393-21c2-48b2-afeb-11c197436194";
            String smsCode = "127230";
            feidan.createOrderNOCode(customerPhone, orderId, faceUrl, channelId);

//            校验
            String channelName = "链家";
            String orderStatusTips = "风险";
            String firstAppear = firstAppearTime + "";

            int riskNum = 3;
            int riskNumA = 4;

            String customerType = "渠道访客";
            String visitor = channelCustomer;

            JSONObject orderLinkData = feidan.orderLinkList(orderId);
            JSONObject orderDetail = feidan.orderDetail(orderId);

//            订单详情
            feidan.checkDetail(orderId, customerName, customerPhone, adviserName, channelName, channelStaffName, orderStatusTips,
                    faceUrl, firstAppear, reportTime2 + "", orderDetail);

//        订单详情，列表，关键环节中信息一致性
            feidan.detailListLinkConsist(orderId, customerPhone);

//        订单环节风险/正常
            feidan.checkOrderRiskLinkMess(orderId, orderLinkData, "RISK_STATUS_CHANGE", "订单风险状态:未知->风险", "存在3个异常环节");
            feidan.checkOrderRiskLinkMess(orderId, orderLinkData, "CHANNEL_REPORT", "测试FREEZE-FREEZE1", "异常提示:多个渠道报备同一顾客");
            feidan.checkOrderRiskLinkMess(orderId, orderLinkData, "RISK_RULE", "报备时间需大于0h0min", "该顾客的风控规则为提前报备时间:0h0min");
            feidan.checkOrderRiskLinkNum(orderId, orderLinkData, riskNum);

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            feidan.channelEditFinally(lianjiaChannelStr, lianjiaChannelName, lianjiaChannelowner_id, lianjiaOwnerPhone, defaultRuleId);
            feidan.channelEditFinally(wudongChannelIdStr, wudongChannelNameStr, wudongChannelowner_id, wudongOwnerPhone, defaultRuleId);
            feidan.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    /**
     * H5报备-顾客到场-自助扫码，置业顾问：安生
     * 选自助
     */
    @Test(dataProvider = "NORMAL")
    public void H5A_SRule(String caseNamePro, String ruleId, long reportTime) {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName + "-" + caseNamePro;

        String caseDesc = "H5报备-顾客到场-自助扫码-创单（选择无渠道）,规则为默认规则";

        logger.info("\n\n" + caseName + "\n");

        try {
            String customerPhone = "18210113587";
            String selfCode = "805805";
            String smsCode = "805805";
            String customerName = caseName + "-" + feidan.getNamePro();

            feidan.channelEdit(wudongChannelIdStr, wudongChannelNameStr, wudongChannelowner_id, wudongOwnerPhone, ruleId);

//            H5报备
            feidan.customerReportH5(wudongStaffIdStr, customerName, customerPhone, "MALE", wudongToken);

//            更改报备时间
            feidan.updateReportTimeChannel(customerPhone, customerName, 5, 2098, reportTime);

//            自助扫码
            feidan.selfRegister(customerName, customerPhone, selfCode, anShengIdStr, "dd", "MALE");

//            刷证
            feidan.witnessUpload(feidan.genCardId(), customerName);

            JSONArray list = feidan.orderList(-1, customerName, 10).getJSONArray("list");
            String orderId = list.getJSONObject(0).getString("order_id");

//            创单
            String faceUrl = "witness/2224020000000100015/1c32c393-21c2-48b2-afeb-11c197436194";
            feidan.createOrderNOCode(customerPhone, orderId, faceUrl, -1);

//            校验
            String adviserName = "安生";
            String channelName = "-";
            String channelStaffName = "-";
            String orderStatusTips = "风险";
            String firstAppear = firstAppearTime + "";

            int riskNum = 3;
            int riskNumA = 4;

            String customerType = "自然访客";
            String visitor = natureCustomer;

            JSONObject orderLinkData = feidan.orderLinkList(orderId);
            JSONObject orderDetail = feidan.orderDetail(orderId);

//            订单详情
            feidan.checkDetail(orderId, customerName, customerPhone, adviserName, channelName, channelStaffName, orderStatusTips,
                    faceUrl, firstAppear, "-", orderDetail);

//        订单详情，列表，关键环节中信息一致性
            feidan.detailListLinkConsist(orderId, customerPhone);

//        订单环节风险/正常
            feidan.checkOrderRiskLinkMess(orderId, orderLinkData, "RISK_STATUS_CHANGE", "订单风险状态:未知->风险", "存在3个异常环节");
            feidan.checkOrderRiskLinkMess(orderId, orderLinkData, "CUSTOMER_CONFIRM_INFO", "顾客在确认信息时表明无渠道介绍", "该顾客成为自然访客");
            feidan.checkOrderRiskLinkMess(orderId, orderLinkData, "CHANNEL_REPORT", "测试FREEZE-FREEZE1", "异常提示:多个渠道报备同一顾客");
            feidan.checkOrderRiskLinkNum(orderId, orderLinkData, riskNum);

        } catch (AssertionError e) {
            failReason = e.toString();

            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();

            aCase.setFailReason(failReason);
        } finally {
            feidan.channelEditFinally(wudongChannelIdStr, wudongChannelNameStr, wudongChannelowner_id, wudongOwnerPhone, defaultRuleId);
            feidan.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    /**
     * H5报备-顾客到场-PC报备-自助扫码，置业顾问：张钧甯
     * 选PC
     */
    @Test
    public void H5A_PCTS() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "H5报备-顾客到场-PC报备-自助扫码-创单（选择PC报备渠道）,规则为默认规则";

        logger.info("\n\n" + caseName + "\n");

        try {
            String customerPhone = "18210113587";
            String selfCode = "805805";
            String smsCode = "805805";
            String customerName = caseName + "-" + feidan.getNamePro();

//            H5报备
            feidan.customerReportH5(wudongStaffIdStr, customerName, customerPhone, "MALE", wudongToken);

//            更改报备时间
            feidan.updateReportTimeChannel(customerPhone, customerName, 5, 2098, wudongReportTime);

//            PC报备
            int channelId = 1;
            int channelStaffId = 2124;
            String adviserName = zhangName;
            String adviserPhone = zhangPhone;
            String channelStaffName = lianjiaStaffName;
            String channelStaffPhone = lianjiaStaffPhone;

            feidan.newCustomer(channelId, channelStaffName, channelStaffPhone, adviserName, adviserPhone, customerPhone, customerName, "MALE");
            long repTimeLianJia = System.currentTimeMillis();
            feidan.updateReportTimeChannel(customerPhone, customerName, channelId, channelStaffId, repTimeLianJia);

//            自助扫码
            feidan.selfRegister(customerName, customerPhone, selfCode, anShengIdStr, "dd", "MALE");

//            刷证
            feidan.witnessUpload(feidan.genCardId(), customerName);

            JSONArray list = feidan.orderList(-1, customerName, 10).getJSONArray("list");
            String orderId = list.getJSONObject(0).getString("order_id");

//            创单
            String faceUrl = "witness/2224020000000100015/1c32c393-21c2-48b2-afeb-11c197436194";
            feidan.createOrderNOCode(customerPhone, orderId, faceUrl, channelId);

//            校验
            String channelName = "链家";
            String orderStatusTips = "风险";
            String firstAppear = firstAppearTime + "";
            String reportTime = repTimeLianJia + "";

            int riskNum = 3;
            int riskNumA = 4;

            String customerType = "渠道访客";
            String visitor = channelCustomer;

            JSONObject orderLinkData = feidan.orderLinkList(orderId);
            JSONObject orderDetail = feidan.orderDetail(orderId);

//            订单详情
            feidan.checkDetail(orderId, customerName, customerPhone, adviserName, channelName, channelStaffName, orderStatusTips,
                    faceUrl, firstAppear, reportTime, orderDetail);

//        订单详情，列表，关键环节中信息一致性
            feidan.detailListLinkConsist(orderId, customerPhone);

//        订单环节风险/正常
            feidan.checkOrderRiskLinkMess(orderId, orderLinkData, "RISK_STATUS_CHANGE", "订单风险状态:未知->风险", "存在3个异常环节");
            feidan.checkOrderRiskLinkMess(orderId, orderLinkData, "CHANNEL_REPORT", "测试FREEZE-FREEZE1", "异常提示:多个渠道报备同一顾客");
            feidan.checkOrderRiskLinkMess(orderId, orderLinkData, "RISK_RULE", "报备时间需大于0h0min", "该顾客的风控规则为提前报备时间:0h0min");
            feidan.checkOrderRiskLinkNum(orderId, orderLinkData, riskNum);

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
     * H5报备-顾客到场-PC报备-自助扫码，置业顾问：安生
     * 选自助扫码
     */
    @Test
    public void H5APCT_S() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "H5报备-顾客到场-PC报备-自助扫码-创单（选择无渠道）,规则为默认规则";

        logger.info("\n\n" + caseName + "\n");

        try {
            String customerPhone = "18210113587";
            String selfCode = "805805";
            String smsCode = "805805";
            String customerName = caseName + "-" + feidan.getNamePro();

//            H5报备
            feidan.customerReportH5(wudongStaffIdStr, customerName, customerPhone, "MALE", wudongToken);

//            更改报备时间
            feidan.updateReportTimeChannel(customerPhone, customerName, 5, 2098, wudongReportTime);

//            PC报备
            String adviserName = anShengName;
            String adviserPhone = anShengPhone;
            int channelId = 1;
            int channelStaffId = 2124;
            String channelStaffName = lianjiaStaffName;
            String channelStaffPhone = lianjiaStaffPhone;

            feidan.newCustomer(channelId, channelStaffName, channelStaffPhone, adviserName, adviserPhone, customerPhone, customerName, "MALE");
            long repTimeLianJia = System.currentTimeMillis();
            feidan.updateReportTimeChannel(customerPhone, customerName, channelId, channelStaffId, repTimeLianJia);

//            自助扫码
            feidan.selfRegister(customerName, customerPhone, selfCode, anShengIdStr, "dd", "MALE");

//            刷证
            feidan.witnessUpload(feidan.genCardId(), customerName);

            JSONArray list = feidan.orderList(-1, customerName, 10).getJSONArray("list");
            String orderId = list.getJSONObject(0).getString("order_id");

//            创单
            String faceUrl = "witness/2224020000000100015/1c32c393-21c2-48b2-afeb-11c197436194";
            feidan.createOrderNOCode(customerPhone, orderId, faceUrl, -1);

//            校验
            String channelName = "-";
            channelStaffName = "-";
            String orderStatusTips = "风险";
            String firstAppear = firstAppearTime + "";
            String reportTime = "-";

            int riskNum = 4;
            int riskNumA = 5;

            String customerType = "自然访客";
            String visitor = natureCustomer;

            JSONObject orderLinkData = feidan.orderLinkList(orderId);
            JSONObject orderDetail = feidan.orderDetail(orderId);

//            订单详情
            feidan.checkDetail(orderId, customerName, customerPhone, adviserName, channelName, channelStaffName, orderStatusTips,
                    faceUrl, firstAppear, reportTime, orderDetail);

//        订单详情，列表，关键环节中信息一致性
            feidan.detailListLinkConsist(orderId, customerPhone);

//        订单环节风险/正常
            feidan.checkOrderRiskLinkMess(orderId, orderLinkData, "RISK_STATUS_CHANGE", "订单风险状态:未知->风险", "存在4个异常环节");
            feidan.checkOrderRiskLinkMess(orderId, orderLinkData, "CUSTOMER_CONFIRM_INFO", "顾客在确认信息时表明无渠道介绍", "该顾客成为自然访客");
            feidan.checkOrderRiskLinkMess(orderId, orderLinkData, "CHANNEL_REPORT", "链家-链家业务员", "异常提示:多个渠道报备同一顾客");
            feidan.checkOrderRiskLinkMess(orderId, orderLinkData, "CHANNEL_REPORT", "测试FREEZE-FREEZE1", "异常提示:多个渠道报备同一顾客");
            feidan.checkOrderRiskLinkNum(orderId, orderLinkData, riskNum);

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
     * H5报备-PC报备-顾客到场-自助扫码，置业顾问：安生
     * 选自助扫码
     */
    @Test
    public void H5PCTA_S() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "H5报备-PC报备-顾客到场-自助扫码-创单（选择无渠道）,规则为默认规则";

        logger.info("\n\n" + caseName + "\n");

        try {
            String customerPhone = "18210113587";
            String selfCode = "805805";
            String smsCode = "805805";
            String customerName = caseName + "-" + feidan.getNamePro();

//            H5报备
            feidan.customerReportH5(wudongStaffIdStr, customerName, customerPhone, "MALE", wudongToken);

//            更改报备时间
            feidan.updateReportTimeChannel(customerPhone, customerName, 5, 2098, wudongReportTime);

//            PC报备
            String adviserName = anShengName;
            String adviserPhone = anShengPhone;
            int channelId = 1;
            int channelStaffId = 2124;
            String channelStaffName = lianjiaStaffName;
            String channelStaffPhone = lianjiaStaffPhone;

            feidan.newCustomer(channelId, channelStaffName, channelStaffPhone, adviserName, adviserPhone, customerPhone, customerName, "MALE");

//            更改报备时间
            feidan.updateReportTimeChannel(customerPhone, customerName, channelId, channelStaffId, lianjiaReportTime);

//            自助扫码
            feidan.selfRegister(customerName, customerPhone, selfCode, anShengIdStr, "dd", "MALE");

//            刷证
            feidan.witnessUpload(feidan.genCardId(), customerName);

            JSONArray list = feidan.orderList(-1, customerName, 10).getJSONArray("list");
            String orderId = list.getJSONObject(0).getString("order_id");

//            创单
            String faceUrl = "witness/2224020000000100015/1c32c393-21c2-48b2-afeb-11c197436194";
            feidan.createOrderNOCode(customerPhone, orderId, faceUrl, -1);

//            校验
            String channelName = "-";
            channelStaffName = "-";
            String orderStatusTips = "风险";
            String firstAppear = firstAppearTime + "";
            String reportTime = "-";

            int riskNum = 4;
            int riskNumA = 5;

            String customerType = "自然访客";
            String visitor = natureCustomer;

            JSONObject orderLinkData = feidan.orderLinkList(orderId);
            JSONObject orderDetail = feidan.orderDetail(orderId);

//            订单详情
            feidan.checkDetail(orderId, customerName, customerPhone, adviserName, channelName, channelStaffName, orderStatusTips,
                    faceUrl, firstAppear, reportTime, orderDetail);

//        订单详情，列表，关键环节中信息一致性
            feidan.detailListLinkConsist(orderId, customerPhone);

//        订单环节风险/正常
            feidan.checkOrderRiskLinkMess(orderId, orderLinkData, "RISK_STATUS_CHANGE", "订单风险状态:未知->风险", "存在4个异常环节");
            feidan.checkOrderRiskLinkMess(orderId, orderLinkData, "CUSTOMER_CONFIRM_INFO", "顾客在确认信息时表明无渠道介绍", "该顾客成为自然访客");
            feidan.checkOrderRiskLinkMess(orderId, orderLinkData, "CHANNEL_REPORT", "链家-链家业务员", "异常提示:多个渠道报备同一顾客");
            feidan.checkOrderRiskLinkMess(orderId, orderLinkData, "CHANNEL_REPORT", "测试FREEZE-FREEZE1", "异常提示:多个渠道报备同一顾客");
            feidan.checkOrderRiskLinkNum(orderId, orderLinkData, riskNum);

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
     * H5报备-顾客到场-H5
     * 选前者
     */
    @Test(dataProvider = "RISK_2_2")
    public void _H5AH5(String caseNamePro, String ruleId, String aheadTime, long reportTime1, long reportTime2) {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName + "-" + caseNamePro;

        String caseDesc = "H5（渠道A）-顾客到场-H5（渠道B）-创单（选择渠道A）,规则为默认规则";

        logger.info("\n\n" + caseName + "\n");

        try {
            String customerPhone = "18210113587";
            String smsCode = "805805";
            String customerName = caseName + "-" + feidan.getNamePro();

            feidan.channelEdit(lianjiaChannelStr, lianjiaChannelName, lianjiaChannelowner_id, lianjiaOwnerPhone, ruleId);

            feidan.channelEdit(wudongChannelIdStr, wudongChannelNameStr, wudongChannelowner_id, wudongOwnerPhone, ruleId);

//            H5报备(勿动)
            feidan.customerReportH5(wudongStaffIdStr, customerName, customerPhone, "MALE", wudongToken);

//            更改报备时间
            feidan.updateReportTimeChannel(customerPhone, customerName, 5, 2098, reportTime1);

//            H5报备（链家）
            feidan.customerReportH5(lianjiaFreezeStaffIdStr, customerName, customerPhone, "MALE", lianjiaToken);
            feidan.updateReportTimeChannel(customerPhone, customerName, lianjiaFreezeStaffIdInt, lianjiaFreezeStaffIdInt, reportTime2);

//            刷证
            feidan.witnessUpload(feidan.genCardId(), customerName);

            JSONArray list = feidan.orderList(-1, customerName, 10).getJSONArray("list");
            String orderId = list.getJSONObject(0).getString("order_id");

//            根据手机号搜索报备信息
            String[] descs = {"测试FREEZE-FREEZE1", "链家-链家业务员"};
            feidan.checkReportInfo(orderId, customerPhone, descs);

//            创单
            String faceUrl = "witness/2224020000000100015/1c32c393-21c2-48b2-afeb-11c197436194";
            feidan.createOrderNOCode(customerPhone, orderId, faceUrl, 5);

//            校验
            String adviserName = "-";
            String channelName = "测试FREEZE";
            String channelStaffName = "FREEZE1";
            String orderStatusTips = "风险";
            String firstAppear = firstAppearTime + "";

            int riskNum = 2;
            int riskNumA = 3;

            String customerType = "渠道访客";
            String visitor = channelCustomer;

            JSONObject orderLinkData = feidan.orderLinkList(orderId);
            JSONObject orderDetail = feidan.orderDetail(orderId);

//            订单详情
            feidan.checkDetail(orderId, customerName, customerPhone, adviserName, channelName, channelStaffName, orderStatusTips,
                    faceUrl, firstAppear, reportTime1 + "", orderDetail);

//        订单详情，列表，关键环节中信息一致性
            feidan.detailListLinkConsist(orderId, customerPhone);

//        订单环节风险/正常
            feidan.checkOrderRiskLinkMess(orderId, orderLinkData, "RISK_STATUS_CHANGE", "订单风险状态:未知->风险", "存在2个异常环节");
            feidan.checkOrderRiskLinkMess(orderId, orderLinkData, "CHANNEL_REPORT", "链家-链家业务员", "异常提示:多个渠道报备同一顾客");
            feidan.checkOrderRiskLinkNum(orderId, orderLinkData, riskNum);

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            feidan.channelEditFinally(lianjiaChannelStr, lianjiaChannelName, lianjiaChannelowner_id, lianjiaOwnerPhone, defaultRuleId);
            feidan.channelEditFinally(wudongChannelIdStr, wudongChannelNameStr, wudongChannelowner_id, wudongOwnerPhone, defaultRuleId);
            feidan.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);

        }
    }

    /**
     * H5报备-顾客到场-PC报备
     * 选H5
     */
    @Test(dataProvider = "RISK_2_2")
    public void _H5APCT(String caseNamePro, String ruleId, String aheadTime, long reportTime1, long reportTime2) {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName + "-" + caseNamePro;

        String caseDesc = "H5报备-顾客到场-PC报备-创单（选择H5报备渠道）,规则为默认规则";

        logger.info("\n\n" + caseName + "\n");

        try {
            String customerPhone = "18210113587";
            String smsCode = "805805";

            String customerName = caseName + "-" + feidan.getNamePro();

            feidan.channelEdit(lianjiaChannelStr, lianjiaChannelName, lianjiaChannelowner_id, lianjiaOwnerPhone, ruleId);

            feidan.channelEdit(wudongChannelIdStr, wudongChannelNameStr, wudongChannelowner_id, wudongOwnerPhone, ruleId);

            // PC报备
            String adviserName = zhangName;
            String adviserPhone = zhangPhone;
            int channelId = 1;
            int channelStaffId = 2124;
            String channelStaffName = lianjiaStaffName;
            String channelStaffPhone = lianjiaStaffPhone;

            feidan.newCustomer(channelId, channelStaffName, channelStaffPhone, adviserName, adviserPhone, customerPhone, customerName, "MALE");
            feidan.updateReportTimeChannel(customerPhone, customerName, channelId, channelStaffId, reportTime2);

//            H5报备
            feidan.customerReportH5("2098", customerName, customerPhone, "MALE", wudongToken);

//            更改报备时间
            feidan.updateReportTimeChannel(customerPhone, customerName, 5, 2098, reportTime1);

//            刷证
            feidan.witnessUpload(feidan.genCardId(), customerName);

            JSONArray list = feidan.orderList(-1, customerName, 10).getJSONArray("list");
            String orderId = list.getJSONObject(0).getString("order_id");

//            创单
            String faceUrl = "witness/2224020000000100015/1c32c393-21c2-48b2-afeb-11c197436194";
            feidan.createOrderNOCode(customerPhone, orderId, faceUrl, 5);

//            校验
            adviserName = "-";
            String channelName = "测试FREEZE";
            channelStaffName = "FREEZE1";
            String orderStatusTips = "风险";
            String firstAppear = firstAppearTime + "";

            int riskNum = 2;
            int riskNumA = 3;

            String customerType = "渠道访客";
            String visitor = channelCustomer;

            JSONObject orderLinkData = feidan.orderLinkList(orderId);
            JSONObject orderDetail = feidan.orderDetail(orderId);

//            订单详情
            feidan.checkDetail(orderId, customerName, customerPhone, adviserName, channelName, channelStaffName, orderStatusTips,
                    faceUrl, firstAppear, reportTime1 + "", orderDetail);

//        订单详情，列表，关键环节中信息一致性
            feidan.detailListLinkConsist(orderId, customerPhone);

//        订单环节风险/正常
            feidan.checkOrderRiskLinkMess(orderId, orderLinkData, "RISK_STATUS_CHANGE", "订单风险状态:未知->风险", "存在2个异常环节");
            feidan.checkOrderRiskLinkMess(orderId, orderLinkData, "CHANNEL_REPORT", "链家-链家业务员", "异常提示:多个渠道报备同一顾客");
            feidan.checkOrderRiskLinkNum(orderId, orderLinkData, riskNum);

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            feidan.channelEditFinally(lianjiaChannelStr, lianjiaChannelName, lianjiaChannelowner_id, lianjiaOwnerPhone, defaultRuleId);
            feidan.channelEditFinally(wudongChannelIdStr, wudongChannelNameStr, wudongChannelowner_id, wudongOwnerPhone, defaultRuleId);
            feidan.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    /**
     * H5报备-PC报备-顾客到场
     * 选H5报备渠道
     */
    @Test(dataProvider = "RISK_2_2")
    public void _H5PCTA(String caseNamePro, String ruleId, String aheadTime, long reportTime1, long reportTime2) {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName + "-" + caseNamePro;

        String caseDesc = "H5报备-PC报备-顾客到场-创单（选择H5报备渠道）,规则为默认规则";

        logger.info("\n\n" + caseName + "\n");

        try {
            String customerPhone = "14422110012";
            String smsCode = "748338";
            String customerName = caseName + "-" + feidan.getNamePro();

            feidan.channelEdit(lianjiaChannelStr, lianjiaChannelName, lianjiaChannelowner_id, lianjiaOwnerPhone, ruleId);

            feidan.channelEdit(wudongChannelIdStr, wudongChannelNameStr, wudongChannelowner_id, wudongOwnerPhone, ruleId);

//            H5报备
            feidan.customerReportH5(wudongStaffIdStr, customerName, customerPhone, "MALE", wudongToken);
            feidan.updateReportTimeChannel(customerPhone, customerName, 5, wudongStaffIdInt, reportTime1);

            // PC报备
            String adviserName = zhangName;
            String adviserPhone = zhangPhone;
            int channelId = 1;
            int channelStaffId = 2124;
            String channelStaffName = lianjiaStaffName;
            String channelStaffPhone = lianjiaStaffPhone;

            feidan.newCustomer(channelId, channelStaffName, channelStaffPhone, adviserName, adviserPhone, customerPhone, customerName, "MALE");
            feidan.updateReportTimeChannel(customerPhone, customerName, 1, channelStaffId, reportTime2);

//            刷证
            feidan.witnessUpload(feidan.genCardId(), customerName);

            JSONArray list = feidan.orderList(-1, customerName, 10).getJSONArray("list");
            String orderId = list.getJSONObject(0).getString("order_id");

//            创单
            String faceUrl = "witness/2224020000000100015/1c32c393-21c2-48b2-afeb-11c197436194";
            feidan.createOrderNOCode(customerPhone, orderId, faceUrl, 5);

//            校验
            adviserName = "-";
            String channelName = "测试FREEZE";
            channelStaffName = "FREEZE1";
            String orderStatusTips = "风险";
            String firstAppear = firstAppearTime + "";

            int riskNum = 2;
            int riskNumA = 3;

            String customerType = "渠道访客";
            String visitor = channelCustomer;

            JSONObject orderLinkData = feidan.orderLinkList(orderId);
            JSONObject orderDetail = feidan.orderDetail(orderId);

//            订单详情
            feidan.checkDetail(orderId, customerName, customerPhone, adviserName, channelName, channelStaffName, orderStatusTips,
                    faceUrl, firstAppear, reportTime1 + "", orderDetail);

//        订单详情，列表，关键环节中信息一致性
            feidan.detailListLinkConsist(orderId, customerPhone);

//        订单环节风险/正常
            feidan.checkOrderRiskLinkMess(orderId, orderLinkData, "RISK_STATUS_CHANGE", "订单风险状态:未知->风险", "存在2个异常环节");
            feidan.checkOrderRiskLinkMess(orderId, orderLinkData, "CHANNEL_REPORT", "链家-链家业务员", "异常提示:多个渠道报备同一顾客");
            feidan.checkOrderRiskLinkNum(orderId, orderLinkData, riskNum);

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            feidan.channelEditFinally(lianjiaChannelStr, lianjiaChannelName, lianjiaChannelowner_id, lianjiaOwnerPhone, defaultRuleId);

            feidan.channelEditFinally(wudongChannelIdStr, wudongChannelNameStr, wudongChannelowner_id, wudongOwnerPhone, defaultRuleId);
            feidan.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    /**
     * H5报备-顾客到场
     * 成单时选无渠道
     */
    @Test(dataProvider = "NORMAL")
    public void H5A_NoChnanelRule(String caseNamePro, String ruleId, long reportTime) {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName + "-" + caseNamePro;

        String caseDesc = "H5报备-顾客到场-创单（选择无渠道）,规则为默认规则";

        logger.info("\n\n" + caseName + "\n");

        try {
            String customerPhone = "14422110013";
            String smsCode = "105793";
            String customerName = caseName + "-" + feidan.getNamePro();

            feidan.channelEdit(wudongChannelIdStr, wudongChannelNameStr, wudongChannelowner_id, wudongOwnerPhone, ruleId);
//            H5报备
            feidan.customerReportH5(wudongStaffIdStr, customerName, customerPhone, "MALE", wudongToken);
            feidan.updateReportTimeChannel(customerPhone, customerName, 5, wudongStaffIdInt, reportTime);

//            刷证
            feidan.witnessUpload(feidan.genCardId(), customerName);

            JSONArray list = feidan.orderList(-1, customerName, 10).getJSONArray("list");
            String orderId = list.getJSONObject(0).getString("order_id");

//            创单
            String faceUrl = "witness/2224020000000100015/1c32c393-21c2-48b2-afeb-11c197436194";
            feidan.createOrderNOCode(customerPhone, orderId, faceUrl, -1);

//            校验
            String adviserName = "-";
            String channelName = "-";
            String channelStaffName = "-";
            String orderStatusTips = "风险";
            String firstAppear = firstAppearTime + "";

            int riskNum = 3;
            int riskNumA = 4;

            String customerType = "自然访客";
            String visitor = natureCustomer;

            JSONObject orderLinkData = feidan.orderLinkList(orderId);
            JSONObject orderDetail = feidan.orderDetail(orderId);

//            订单详情
            feidan.checkDetail(orderId, customerName, customerPhone, adviserName, channelName, channelStaffName, orderStatusTips,
                    faceUrl, firstAppear, "-", orderDetail);

//        订单详情，列表，关键环节中信息一致性
            feidan.detailListLinkConsist(orderId, customerPhone);

//        订单环节风险/正常
            feidan.checkOrderRiskLinkMess(orderId, orderLinkData, "RISK_STATUS_CHANGE", "订单风险状态:未知->风险", "存在3个异常环节");
            feidan.checkOrderRiskLinkMess(orderId, orderLinkData, "CUSTOMER_CONFIRM_INFO", "顾客在确认信息时表明无渠道介绍", "该顾客成为自然访客");
            feidan.checkOrderRiskLinkMess(orderId, orderLinkData, "CHANNEL_REPORT", "测试FREEZE-FREEZE1", "异常提示:多个渠道报备同一顾客");
            feidan.checkOrderRiskLinkNum(orderId, orderLinkData, riskNum);

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            feidan.channelEditFinally(wudongChannelIdStr, wudongChannelNameStr, wudongChannelowner_id, wudongOwnerPhone, defaultRuleId);
            feidan.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

//    -------------------------------------------顾客信息修改---------------------------------------------------------

//    ------------------------------------------更改手机号---------------------------------------------------

    /**
     * H5-顾客到场，没有置业顾问，更改手机号
     * 选H5
     */
    @Test
    public void _H5AChngPhoneR() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "H5报备-顾客到场-更改手机号-创单（选择H5报备渠道）,规则为默认规则";

        logger.info("\n\n" + caseName + "\n");

        try {
            // 报备
            String customerPhoneB = "18210113588";
            String customerPhoneA = "18210113587";
            String smsCode = "805805";

            feidan.channelEdit(wudongChannelIdStr, wudongChannelNameStr, wudongChannelowner_id, wudongOwnerPhone, defaultRuleId);
            String customerName = caseName + "-" + feidan.getNamePro();

            feidan.customerReportH5(wudongStaffIdStr, customerName, customerPhoneB, "MALE", wudongToken);

            JSONArray list = feidan.customerList(customerPhoneB, wudongChannelIdStr, "", 1, 10).getJSONArray("list");

            String cid = list.getJSONObject(0).getString("cid");

//            更改手机号
            feidan.customerEditPC(cid, customerName, customerPhoneA, "", "");

            feidan.updateReportTimeChannel(customerPhoneA, customerName, wudongChannelInt, wudongStaffIdInt, wudongReportTime);

//            刷证
            feidan.witnessUpload(feidan.genCardId(), customerName);

            list = feidan.orderList(-1, "", 10).getJSONArray("list");
            String orderId = list.getJSONObject(0).getString("order_id");

            String faceUrl = "witness/2224020000000100015/1c32c393-21c2-48b2-afeb-11c197436194";

//            创单
            feidan.createOrder(customerPhoneA, orderId, faceUrl, 5, smsCode);

//            校验
            String adviserName = "-";
            String channelName = "测试FREEZE";
            String channelStaffName = "FREEZE1";
            String orderStatusTips = "风险";
            String firstAppear = firstAppearTime + "";
            String reportTime = wudongReportTime + "";

            int riskNum = 2;
            int riskNumA = 3;

            String customerType = "渠道访客";
            String visitor = channelCustomer;

            JSONObject orderLinkData = feidan.orderLinkList(orderId);
            JSONObject orderDetail = feidan.orderDetail(orderId);

//            订单详情
            feidan.checkDetail(orderId, customerName, customerPhoneA, adviserName, channelName, channelStaffName, orderStatusTips,
                    faceUrl, firstAppear, reportTime, orderDetail);

//        订单详情，列表，关键环节中信息一致性
            feidan.detailListLinkConsist(orderId, customerPhoneA);

//        订单环节风险/正常
            feidan.checkOrderRiskLinkMess(orderId, orderLinkData, "RISK_STATUS_CHANGE", "订单风险状态:未知->风险", "存在2个异常环节");
            feidan.checkOrderRiskLinkMess(orderId, orderLinkData, "INFO_CHANGE", "18210113588更改为18210113587", "顾客手机号被修改");
            feidan.checkOrderRiskLinkNum(orderId, orderLinkData, riskNum);

//        场内轨迹
            feidan.checkFirstVisitAndTrace(orderId, orderLinkData, true);

//            审核
            feidan.orderAudit(orderId, visitor);

//            校验风控单
            feidan.checkReport(orderId, orderStatusTips, riskNumA, customerType, orderDetail);

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            feidan.channelEditFinally(wudongChannelIdStr, wudongChannelNameStr, wudongChannelowner_id, wudongOwnerPhone, defaultRuleId);
            feidan.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

//    -------------------------------更改置业顾问3次-----------------------------------------

    /**
     * PC（有渠道）-顾客到场,置业顾问是张钧甯,更改置业顾问3次
     * 选PC报备渠道
     */
    @Test
    public void _PCTAChngAdviser3R() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "PC（有渠道）-顾客到场-更改置业顾问3次-创单（选择PC报备渠道）,规则为默认规则";

        logger.info("\n\n" + caseName + "\n");

        try {
            // PC报备
            String customerPhone = "14422110015";
            String smsCode = "805931";
            String customerName = caseName + "-" + feidan.getNamePro();
            String adviserName = zhangName;
            String adviserPhone = zhangPhone;
            int channelId = 1;
            String channelStaffName = lianjiaStaffName;
            String channelStaffPhone = lianjiaStaffPhone;
            int channelStaffId = 2124;

            feidan.channelEdit(lianjiaChannelStr, lianjiaChannelName, lianjiaChannelowner_id, lianjiaOwnerPhone, defaultRuleId);

            feidan.newCustomer(channelId, channelStaffName, channelStaffPhone, adviserName, adviserPhone, customerPhone, customerName, "MALE");

            JSONArray list = feidan.customerList(customerPhone, lianjiaChannelStr, "8", 1, 10).getJSONArray("list");

            String cid = list.getJSONObject(0).getString("cid");

            feidan.customerEditPC(cid, customerName, customerPhone, anShengName, anShengPhone);
            feidan.customerEditPC(cid, customerName, customerPhone, zhangName, zhangPhone);
            feidan.customerEditPC(cid, customerName, customerPhone, anShengName, anShengPhone);

            feidan.updateReportTimeChannel(customerPhone, customerName, channelId, channelStaffId, lianjiaReportTime);

//            刷证
            feidan.witnessUpload(feidan.genCardId(), customerName);

            list = feidan.orderList(-1, "", 10).getJSONArray("list");
            String orderId = list.getJSONObject(0).getString("order_id");

//            创单
            String faceUrl = "witness/2224020000000100015/1c32c393-21c2-48b2-afeb-11c197436194";
            feidan.createOrderNOCode(customerPhone, orderId, faceUrl, channelId);

//            校验
            String channelName = "链家";
            String orderStatusTips = "风险";
            String firstAppear = firstAppearTime + "";
            adviserName = anShengName;

            int riskNum = 2;
            int riskNumA = 3;

            String customerType = "渠道访客";
            String visitor = channelCustomer;

            JSONObject orderLinkData = feidan.orderLinkList(orderId);
            JSONObject orderDetail = feidan.orderDetail(orderId);

//            订单详情
            feidan.checkDetail(orderId, customerName, customerPhone, adviserName, channelName, channelStaffName, orderStatusTips,
                    faceUrl, firstAppear, lianjiaReportTime + "", orderDetail);

//        订单详情，列表，关键环节中信息一致性
            feidan.detailListLinkConsist(orderId, customerPhone);

//        订单环节风险/正常
            feidan.checkOrderRiskLinkMess(orderId, orderLinkData, "RISK_STATUS_CHANGE", "订单风险状态:未知->风险", "存在2个异常环节");
            feidan.checkOrderRiskLinkMess(orderId, orderLinkData, "INFO_CHANGE", "张钧甯更改为安生", "异常提示:顾客置业顾问被多次更换");
            feidan.checkOrderRiskLinkNum(orderId, orderLinkData, riskNum);

//        场内轨迹
            feidan.checkFirstVisitAndTrace(orderId, orderLinkData, true);

//            审核
            feidan.orderAudit(orderId, visitor);

//            校验风控单
            feidan.checkReport(orderId, orderStatusTips, riskNumA, customerType, orderDetail);

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            feidan.channelEditFinally(lianjiaChannelStr, lianjiaChannelName, lianjiaChannelowner_id, lianjiaOwnerPhone, defaultRuleId);
            feidan.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    //    -------------------------------更改姓名1次-----------------------------------------

    /**
     * 顾客到场-PC(无渠道)，置业顾问是张钧甯
     * 选无渠道
     */
    @Test
    public void _PCFAChngNameR() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "顾客到场-PC（无渠道）-更改姓名-创单（选择无渠道）,规则为默认规则";

        logger.info("\n\n" + caseName + "\n");

        try {
            // PC报备
            String customerPhone = "14422110017";
            String smsCode = "133345";
            String customerNameOLd = caseName + "-" + feidan.getNamePro() + "-old";
            String customerNameNew = caseName + "-" + feidan.getNamePro() + "-new";
            String adviserName = zhangName;
            String adviserPhone = zhangPhone;
            int channelId = -1;

            feidan.newCustomer(channelId, "", "", adviserName, adviserPhone, customerPhone, customerNameOLd, "MALE");

            JSONArray list = feidan.customerList(customerPhone, "", "", 1, 10).getJSONArray("list");

            String cid = list.getJSONObject(0).getString("cid");

            feidan.customerEditPC(cid, customerNameNew, customerPhone, zhangName, zhangPhone);

            feidan.updateReportTime_PCF(customerPhone, customerNameNew, noChannelReportTime);

//            刷证
            feidan.witnessUpload(feidan.genCardId(), customerNameNew);

            list = feidan.orderList(-1, "", 10).getJSONArray("list");
            String orderId = list.getJSONObject(0).getString("order_id");

//            创单
            String faceUrl = "witness/2224020000000100015/1c32c393-21c2-48b2-afeb-11c197436194";
            feidan.createOrderNOCode(customerPhone, orderId, faceUrl, channelId);

//            校验
            String channelName = "-";
            String channelStaffName = "-";
            String orderStatusTips = "风险";
            String firstAppear = firstAppearTime + "";
            String reportTime = "-";

            int riskNum = 2;
            int riskNumA = 3;

            String customerType = "自然访客";
            String visitor = natureCustomer;

            JSONObject orderLinkData = feidan.orderLinkList(orderId);
            JSONObject orderDetail = feidan.orderDetail(orderId);

//            订单详情
            feidan.checkDetail(orderId, customerNameNew, customerPhone, adviserName, channelName, channelStaffName, orderStatusTips,
                    faceUrl, firstAppear, reportTime, orderDetail);

//        订单详情，列表，关键环节中信息一致性
            feidan.detailListLinkConsist(orderId, customerPhone);

//        订单环节风险/正常
            feidan.checkOrderRiskLinkMess(orderId, orderLinkData, "RISK_STATUS_CHANGE", "订单风险状态:未知->风险", "存在2个异常环节");
            feidan.checkOrderRiskLinkMess(orderId, orderLinkData, "INFO_CHANGE", customerNameOLd + "更改为" + customerNameNew, "顾客姓名被修改");
            feidan.checkOrderRiskLinkNum(orderId, orderLinkData, riskNum);

//        场内轨迹
            feidan.checkFirstVisitAndTrace(orderId, orderLinkData, true);

//            审核
            feidan.orderAudit(orderId, visitor);

//            校验风控单
            feidan.checkReport(orderId, orderStatusTips, riskNumA, customerType, orderDetail);

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

//    --------------------------------------------------------隐藏手机号报备------------------------------------------

    /**
     * H5报备-顾客到场-同一渠道隐藏手机号报备-创单（选择H5渠道），置业顾问无
     * 选H5
     */
    @Test
    public void c1Hide1Evident1() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        logger.info("\n\n" + caseName + "\n");

        String caseDesc = "渠道A报备全号-到场-渠道A报备隐藏手机号-刷证-创单（选择渠道A）,规则为默认规则";

        try {
            // 报备
            String customerPhone = "14422110014";
            String smsCode = "202593";

            String customerName = caseName + "-" + feidan.getNamePro();
            feidan.customerReportH5(wudongStaffIdStr, customerName, customerPhone, "MALE", wudongToken);
            feidan.customerReportH5(wudongStaffIdStr, customerName, "144****0014", "MALE", wudongToken);

//            更改报备时间
            feidan.updateReportTimeChannel(customerPhone, customerName, wudongChannelInt, wudongStaffIdInt, wudongReportTime);

//            刷证
            feidan.witnessUpload(feidan.genCardId(), customerName);

            JSONArray list = feidan.orderList(-1, customerName, 10).getJSONArray("list");
            String orderId = list.getJSONObject(0).getString("order_id");

            String faceUrl = "witness/2224020000000100015/1c32c393-21c2-48b2-afeb-11c197436194";

//            创单
            feidan.createOrderNOCode(customerPhone, orderId, faceUrl, 5);

//            校验
            String adviserName = "-";
            String channelName = "测试FREEZE";
            String channelStaffName = "FREEZE1";
            String orderStatusTips = "风险";
            String firstAppear = firstAppearTime + "";
            String reportTime = wudongReportTime + "";

            int riskNum = 2;
            int riskNumA = 3;

            String customerType = "渠道访客";
            String visitor = channelCustomer;

            JSONObject orderLinkData = feidan.orderLinkList(orderId);
            JSONObject orderDetail = feidan.orderDetail(orderId);

//            订单详情
            feidan.checkDetail(orderId, customerName, customerPhone, adviserName, channelName, channelStaffName, orderStatusTips,
                    faceUrl, firstAppear, reportTime, orderDetail);

//        订单详情，列表，关键环节中信息一致性
            feidan.detailListLinkConsist(orderId, customerPhone);

//        订单环节风险/正常
            feidan.checkOrderRiskLinkMess(orderId, orderLinkData, "RISK_STATUS_CHANGE", "订单风险状态:未知->风险", "存在2个异常环节");
            feidan.checkOrderRiskLinkMess(orderId, orderLinkData, "RISK_REPORT", "测试FREEZE-FREEZE1\n" +
                    "报备号码:144****0014", "异常提示:顾客手机号与报备手机号码部分匹配");
            feidan.checkOrderRiskLinkNum(orderId, orderLinkData, riskNum);

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
     * 渠道一H5报备-顾客到场-渠道一隐藏手机号报备-渠道二隐藏手机号报备-创单（选择H5渠道）置业顾问无
     * 选H5
     */
    @Test
    public void c2Hide2Evident1() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        logger.info("\n\n" + caseName + "\n");

        String caseDesc = "渠道A报备全号-到场-渠道A、B报备隐藏手机号-刷证-创单（选择渠道A）,规则为默认规则";

        try {
            // 报备
            String customerPhone = "14422110014";
            String smsCode = "202593";

            String customerName = caseName + "-" + feidan.getNamePro();
            feidan.customerReportH5(wudongStaffIdStr, customerName, customerPhone, "MALE", wudongToken);
            feidan.customerReportH5(lianjiaFreezeStaffIdStr, customerName, "144****0014", "MALE", lianjiaToken);
            feidan.customerReportH5(wudongStaffIdStr, customerName, "144****0014", "MALE", wudongToken);

//            更改报备时间
            feidan.updateReportTimeChannel(customerPhone, customerName, wudongChannelInt, wudongStaffIdInt, wudongReportTime);

//            刷证
            feidan.witnessUpload(feidan.genCardId(), customerName);

            JSONArray list = feidan.orderList(-1, customerName, 10).getJSONArray("list");

            String orderId = list.getJSONObject(0).getString("order_id");

//            根据手机号搜索报备信息
            String[] descs = {"测试FREEZE-FREEZE1"};
            feidan.checkReportInfo(orderId, customerPhone, descs);

//            创单
            String faceUrl = "witness/2224020000000100015/1c32c393-21c2-48b2-afeb-11c197436194";

            feidan.createOrderNOCode(customerPhone, orderId, faceUrl, 5);

//            校验
            String adviserName = "-";
            String channelName = "测试FREEZE";
            String channelStaffName = "FREEZE1";
            String orderStatusTips = "风险";
            String firstAppear = firstAppearTime + "";
            String reportTime = wudongReportTime + "";

            int riskNum = 3;
            int riskNumA = 4;

            String customerType = "渠道访客";
            String visitor = channelCustomer;

            JSONObject orderLinkData = feidan.orderLinkList(orderId);
            JSONObject orderDetail = feidan.orderDetail(orderId);

//            订单详情
            feidan.checkDetail(orderId, customerName, customerPhone, adviserName, channelName, channelStaffName, orderStatusTips,
                    faceUrl, firstAppear, reportTime, orderDetail);

//        订单详情，列表，关键环节中信息一致性
            feidan.detailListLinkConsist(orderId, customerPhone);

//        订单环节风险/正常
            feidan.checkOrderRiskLinkMess(orderId, orderLinkData, "RISK_STATUS_CHANGE", "订单风险状态:未知->风险", "存在3个异常环节");
            feidan.checkOrderRiskLinkMess(orderId, orderLinkData, "RISK_REPORT", "链家-链家业务员\n" +
                    "报备号码:144****0014", "异常提示:顾客手机号与报备手机号码部分匹配");
            feidan.checkOrderRiskLinkMess(orderId, orderLinkData, "RISK_REPORT", "测试FREEZE-FREEZE1\n" +
                    "报备号码:144****0014", "异常提示:顾客手机号与报备手机号码部分匹配");
            feidan.checkOrderRiskLinkNum(orderId, orderLinkData, riskNum);

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
     * 顾客到场-H5，无置业顾问
     * 选H5
     */
    @Test
    public void deleteUnusedRule() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "删除顾客引用后删除规则";

        logger.info("\n\n" + caseName + "\n");

        try {

//            新建规则
            String name = feidan.getNamePro();

            feidan.addRiskRule(name, "60", "");

            JSONObject data = feidan.riskRuleList();

            JSONArray list = data.getJSONArray("list");

            JSONObject ruleData = list.getJSONObject(list.size() - 1);

            String ruleName = ruleData.getString("name");
            if (!ruleName.equals(name)) {
                throw new Exception("期待最后一条规则为【" + ruleName + "】，实际为【" + name + "】");
            }

            String id = ruleData.getString("id");

//            编辑渠道规则
            feidan.channelEdit(wudongChannelIdStr, "测试FREEZE", wudongChannelowner_id, feidan.genPhoneNum(), id);

//            报备
            String customerPhone = "18210113587";
            String smsCode = "805805";

            String customerName = caseName + "-" + feidan.getNamePro();

            feidan.customerReportH5(wudongStaffIdStr, customerName, customerPhone, "MALE", wudongToken);

            long repTime = System.currentTimeMillis() - 59 * 60 * 1000;
            feidan.updateReportTimeChannel(customerPhone, customerName, 5, 2098, repTime);

//            刷证
            feidan.witnessUpload(feidan.genCardId(), customerName);

            list = feidan.orderList(-1, customerName, 10).getJSONArray("list");
            String orderId = list.getJSONObject(0).getString("order_id");

            String faceUrl = "witness/2224020000000100015/1c32c393-21c2-48b2-afeb-11c197436194";

//            创单
            feidan.createOrderNOCode(customerPhone, orderId, faceUrl, 5);

            //引用后删除规则
            String s = feidan.deleteRiskRuleNoCheckCode(id);
            feidan.checkMessage("新建风控规则", s, "规则已被渠道引用, 不可删除");

//            删除引用
            feidan.channelEdit(wudongChannelIdStr, "测试FREEZE", wudongChannelowner_id, feidan.genPhoneNum(), defaultRuleId);

//            删除引用后删除规则
            feidan.deleteRiskRule(id);

//            校验
            String adviserName = "-";
            String channelName = "测试FREEZE";
            String channelStaffName = "FREEZE1";
            String orderStatusTips = "风险";
            String firstAppear = firstAppearTime + "";
            String reportTime = repTime + "";
            int riskNum = 2;
            int riskNumA = 3;

            String customerType = "渠道访客";
            String visitor = channelCustomer;

            JSONObject orderLinkData = feidan.orderLinkList(orderId);
            JSONObject orderDetail = feidan.orderDetail(orderId);

//            订单详情
            feidan.checkDetail(orderId, customerName, customerPhone, adviserName, channelName, channelStaffName, orderStatusTips,
                    faceUrl, firstAppear, reportTime, orderDetail);

//        订单详情，列表，关键环节中信息一致性
            feidan.detailListLinkConsist(orderId, customerPhone);

//        订单环节风险/正常
            feidan.checkOrderRiskLinkMess(orderId, orderLinkData, "RISK_STATUS_CHANGE", "订单风险状态:未知->风险", "存在2个异常环节");
            feidan.checkOrderRiskLinkMess(orderId, orderLinkData, "RISK_RULE", "报备时间需大于1h0min", "该顾客的风控规则为提前报备时间:1h0min");
            feidan.checkOrderRiskLinkNum(orderId, orderLinkData, riskNum);

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


    @DataProvider(name = "NORMAL")
    public Object[][] normalCase() {
        return new Object[][]{
//                caseName,ruleId,reportTime
                new Object[]{
                        "0min", defaultRuleId, firstAppearTime - 61 * 1000
                },
                new Object[]{
                        "60min", ahead1hRuleId, firstAppearTime - 61 * 60 * 1000
                },
                new Object[]{
                        "1day", ahead24hRuleId, firstAppearTime - (24 * 60 + 1) * 60 * 1000
                },
                new Object[]{
                        "7day", ahead7dayRuleId, firstAppearTime - (7 * 24 * 60 + 1) * 60 * 1000
                },
                new Object[]{
                        "30day", ahead30dayRuleId, firstAppearTime - 2592060000L
                },
                new Object[]{
                        "aheadMax", aheadMaxRuleId, firstAppearTime - 15600060000L
                }
        };
    }

    @DataProvider(name = "RISK_1")
    public Object[][] riskCase1Channel() {
        return new Object[][]{
//                caseName,ruleId,aheadTime,reportTime
                new Object[]{
                        "0min", defaultRuleId, "0h0min", firstAppearTime
                },
                new Object[]{
                        "60min", ahead1hRuleId, "1h0min", firstAppearTime - 60 * 60 * 1000
                },
                new Object[]{
                        "60min", ahead1hRuleId, "1h0min", firstAppearTime + 61 * 60 * 1000
                },
                new Object[]{
                        "1day", ahead24hRuleId, "24h0min", firstAppearTime - (24 * 60) * 60 * 1000
                },
                new Object[]{
                        "1day", ahead24hRuleId, "24h0min", System.currentTimeMillis()
                },
                new Object[]{
                        "7day", ahead7dayRuleId, "168h0min", firstAppearTime - (7 * 24 * 60) * 60 * 1000
                },
                new Object[]{
                        "7day", ahead7dayRuleId, "168h0min", System.currentTimeMillis()
                },
                new Object[]{
                        "30day", ahead30dayRuleId, "720h0min", firstAppearTime - 2592000000L
                },
                new Object[]{
                        "30day", ahead30dayRuleId, "720h0min", System.currentTimeMillis()
                },
                new Object[]{
                        "max", aheadMaxRuleId, "4333h20min", firstAppearTime - 15600000000L
                }
        };
    }

    @DataProvider(name = "RISK_2")
    public Object[][] riskCase2Channel() {
        return new Object[][]{
//                caseName,ruleId,AreportTime,BreportTime
                new Object[]{
                        "0min", defaultRuleId, "0h0min", firstAppearTime - 1 * 60000, firstAppearTime + 600 * 1000
                },
                new Object[]{
                        "60min", ahead1hRuleId, "1h0min", firstAppearTime - 61 * 60000, firstAppearTime - 60 * 60000
                },
                new Object[]{
                        "1day", ahead24hRuleId, "24h0min", firstAppearTime - (24 * 60 + 1) * 60000, firstAppearTime - (24 * 60) * 60000
                },
                new Object[]{
                        "7day", ahead7dayRuleId, "168h0min", firstAppearTime - (7 * 24 * 60 + 1) * 60000, firstAppearTime - (7 * 24 * 60) * 60000
                },
                new Object[]{
                        "30day", ahead30dayRuleId, "720h0min", firstAppearTime - 2592060000L, firstAppearTime - 2592000000L
                },
                new Object[]{
                        "max", aheadMaxRuleId, "4333h20min", firstAppearTime - 1560000000L, firstAppearTime - 1560006000L
                }
        };
    }

    @DataProvider(name = "RISK_2_2")
    public Object[][] riskCase2Channel2() {
        return new Object[][]{
//                caseName,ruleId,AreportTime,BreportTime
                new Object[]{
                        "0min", defaultRuleId, "0h0min", firstAppearTime - 1 * 60000, firstAppearTime + 600 * 1000
                }
        };
    }

    @DataProvider(name = "RULE_ID")
    public Object[] ruleId() {
        return new Object[]{
                "0min", "60min", "1day", "7day", "30day", "max"
        };
    }

    @DataProvider(name = "PROTECT_RULE")
    public Object[] protectRule() {
        return new Object[][]{
//                ruleId,name,time,code

                new Object[]{
                        protect1DayRuleId, "protect1Day", 1 * 24 * 60 * 60 * 1000L, "0day", 60 * 60 * 1000L, 1001
                },
                new Object[]{
                        protect1DayRuleId, "protect1Day", 1 * 24 * 60 * 60 * 1000L, "L1day", 1 * 24 * 60 * 60 * 1000L - 2000, 1001
                },
                new Object[]{
                        protect1DayRuleId, "protect1Day", 1 * 24 * 60 * 60 * 1000L, "M1day", 1 * 24 * 60 * 60 * 1000L + 2000, 1000
                },
                new Object[]{
                        protect1DayRuleId, "protect1Day", 1 * 24 * 60 * 60 * 1000L, "2day", 2 * 24 * 60 * 60 * 1000L, 1000
                },

                new Object[]{
                        protect10DayRuleId, "protect10Day", 10 * 24 * 60 * 60 * 1000L, "1day", 1 * 24 * 60 * 60 * 1000L, 1001
                },
                new Object[]{
                        protect10DayRuleId, "protect10Day", 10 * 24 * 60 * 60 * 1000L, "9day", 9 * 24 * 60 * 60 * 1000L, 1001
                },
                new Object[]{
                        protect10DayRuleId, "protect10Day", 10 * 24 * 60 * 60 * 1000L, "L10day", 10 * 24 * 60 * 60 * 1000L - 2000, 1001
                },
                new Object[]{
                        protect10DayRuleId, "protect10Day", 10 * 24 * 60 * 60 * 1000L, "M10day", 10 * 24 * 60 * 60 * 1000L + 2000, 1000
                },
                new Object[]{
                        protect10DayRuleId, "protect10Day", 10 * 24 * 60 * 60 * 1000L, "11day", 11 * 24 * 60 * 60 * 1000L, 1000
                },
                new Object[]{
                        protect10DayRuleId, "protect10Day", 10 * 24 * 60 * 60 * 1000L, "20day", 20 * 24 * 60 * 60 * 1000L, 1000
                },


                new Object[]{
                        protect30DayRuleId, "protect30Day", 30 * 24 * 60 * 60 * 1000L, "1day", 1 * 24 * 60 * 60 * 1000L, 1001
                },
                new Object[]{
                        protect30DayRuleId, "protect30Day", 30 * 24 * 60 * 60 * 1000L, "29day", 29 * 24 * 60 * 60 * 1000L, 1001
                },
                new Object[]{
                        protect30DayRuleId, "protect30Day", 30 * 24 * 60 * 60 * 1000L, "L30day", 30 * 24 * 60 * 60 * 1000L - 20000, 1001
                },
                new Object[]{
                        protect30DayRuleId, "protect30Day", 30 * 24 * 60 * 60 * 1000L, "M30day", 30 * 24 * 60 * 60 * 1000L + 2000, 1000
                },
                new Object[]{
                        protect30DayRuleId, "protect30Day", 30 * 24 * 60 * 60 * 1000L, "31day", 31 * 24 * 60 * 60 * 1000L, 1000
                },
                new Object[]{
                        protect30DayRuleId, "protect30Day", 30 * 24 * 60 * 60 * 1000L, "40day", 40 * 24 * 60 * 60 * 1000L, 1000
                },

                new Object[]{
                        protect100DayRuleId, "protect100Day", 100 * 24 * 60 * 60 * 1000L, "1day", 1 * 24 * 60 * 60 * 1000L, 1001
                },
                new Object[]{
                        protect100DayRuleId, "protect100Day", 100 * 24 * 60 * 60 * 1000L, "50day", 50 * 24 * 60 * 60 * 1000L, 1001
                },
                new Object[]{
                        protect100DayRuleId, "protect100Day", 100 * 24 * 60 * 60 * 1000L, "99day", 99 * 24 * 60 * 60 * 1000L, 1001
                },
                new Object[]{
                        protect100DayRuleId, "protect100Day", 100 * 24 * 60 * 60 * 1000L, "L100day", 100 * 24 * 60 * 60 * 1000L - 2000, 1001
                },
                new Object[]{
                        protect100DayRuleId, "protect100Day", 100 * 24 * 60 * 60 * 1000L, "M100day", 100 * 24 * 60 * 60 * 1000L + 2000, 1000
                },
                new Object[]{
                        protect100DayRuleId, "protect100Day", 100 * 24 * 60 * 60 * 1000L, "101day", 101 * 24 * 60 * 60 * 1000L, 1000
                },
                new Object[]{
                        protect100DayRuleId, "protect100Day", 100 * 24 * 60 * 60 * 1000L, "200day", 200 * 24 * 60 * 60 * 1000L, 1000
                },

                new Object[]{
                        protect365DayRuleId, "protect365Day", 365 * 24 * 60 * 60 * 1000L, "1day", 1 * 24 * 60 * 60 * 1000L, 1001
                },
                new Object[]{
                        protect365DayRuleId, "protect365Day", 365 * 24 * 60 * 60 * 1000L, "364day", 364 * 24 * 60 * 60 * 1000L, 1001
                },
                new Object[]{
                        protect365DayRuleId, "protect365Day", 365 * 24 * 60 * 60 * 1000L, "L365day", 365 * 24 * 60 * 60 * 1000L - 2000, 1001
                },
                new Object[]{
                        protect365DayRuleId, "protect365Day", 365 * 24 * 60 * 60 * 1000L, "M365day", 365 * 24 * 60 * 60 * 1000L + 2000, 1000
                },
                new Object[]{
                        protect365DayRuleId, "protect365Day", 365 * 24 * 60 * 60 * 1000L, "366day", 366 * 24 * 60 * 60 * 1000L, 1000
                },
                new Object[]{
                        protect365DayRuleId, "protect365Day", 365 * 24 * 60 * 60 * 1000L, "400day", 400 * 24 * 60 * 60 * 1000L, 1000
                },

                new Object[]{
                        protect10000DayRuleId, "protect10000Day", 10000 * 24 * 60 * 60 * 1000L, "1day", 1 * 24 * 60 * 60 * 1000L, 1001
                },
                new Object[]{
                        protect10000DayRuleId, "protect10000Day", 10000 * 24 * 60 * 60 * 1000L, "9999day", 9999 * 24 * 60 * 60 * 1000L, 1001
                },
                new Object[]{
                        protect10000DayRuleId, "protect10000Day", 10000 * 24 * 60 * 60 * 1000L, "L10000day", 10000 * 24 * 60 * 60 * 1000L - 2000, 1001
                },
                new Object[]{
                        protect10000DayRuleId, "protect10000Day", 10000 * 24 * 60 * 60 * 1000L, "M10000day", 10000 * 24 * 60 * 60 * 1000L + 2000, 1000
                },
                new Object[]{
                        protect10000DayRuleId, "protect10000Day", 10000 * 24 * 60 * 60 * 1000L, "10001day", 10001 * 24 * 60 * 60 * 1000L, 1000
                },
                new Object[]{
                        protect10000DayRuleId, "protect10000Day", 10000 * 24 * 60 * 60 * 1000L, "10010day", 10010 * 24 * 60 * 60 * 1000L, 1000
                },
        };
    }





}

