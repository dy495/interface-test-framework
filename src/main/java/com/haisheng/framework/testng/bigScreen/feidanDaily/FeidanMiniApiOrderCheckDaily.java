package com.haisheng.framework.testng.bigScreen.feidanDaily;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.model.bean.Case;
import com.haisheng.framework.util.CheckUtil;
import com.haisheng.framework.util.DateTimeUtil;
import com.haisheng.framework.util.StatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.*;

/**
 * @ClassName
 * @Deacription TODO
 * @Author Shine
 * @Date 2020/5/6 12:17
 * @Version 1.0
 */
public class FeidanMiniApiOrderCheckDaily {

    public Logger logger = LoggerFactory.getLogger(this.getClass());
    public String failReason = "";
    public String response = "";
    public boolean FAIL = false;
    public Case aCase = new Case();

    Feidan feidan = new Feidan();
    DateTimeUtil dateTimeUtil = new DateTimeUtil();
    CheckUtil checkUtil = new CheckUtil();

    String natureCustomer = "NATURE";
    String channelCustomer = "CHANNEL";

    //  -----------------------------------------渠道------------------------------------------
    String wudongChannelIdStr = "5";
    String wudongChannelNameStr = "测试FREEZE";
    int wudongChannelInt = 5;
    String wudongOwnerPhone = "16600000000";

    String lianjiaChannelStr = "1";
    int lianjiaChannelInt = 1;
    String lianjiaChannelName = "链家";
    String lianjiaOwnerPhone = "16600000001";

//  ------------------------------------------业务员-----------------------------------------------------

    String lianjiaToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiLmtYvor5XjgJDli7_liqjjgJEiLCJ1aWQiOjIxMzYsImxvZ2luVGltZSI6MT" +
            "U3ODk5OTY2NjU3NH0.kQsEw_wGVmPQ4My1p-FNZ556FJC7W177g7jfjFarTu4";
    String lianjiaFreezeStaffIdStr = "2136";
    int lianjiaFreezeStaffIdInt = 2136;
    String lianjiaFreezeStaffName = "链家业务员";
    String lianjiaFreezeStaffPhone = "14112345678";

    String lianjiaStaffIdStr = "2136";
    int lianjiaStaffIdInt = 2136;
    String lianjiaStaffName = "链家业务员";
    String lianjiaStaffPhone = "17711111024";


    String wudongToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiLjgJDli7_li" +
            "qjjgJExIiwidWlkIjoyMDk4LCJsb2dpblRpbWUiOjE1Nzg1NzQ2MjM4NDB9.exDJ6avJKJd3ezQkYc4fmUkHvXaukqfgjThkpoYgnAw";

    String wudongStaffIdStr = "2098";
    int wudongStaffIdInt = 2098;


//    -------------------------------------------置业顾问-----------------------------------------------------

    String zhangIdStr = "8";
    String zhangName = "张钧甯";
    String zhangPhone = "19111311116";

    long lianjiaReportTime = 1547014265000L;//2019-01-09 14:11:05
    long noChannelReportTime = 1547034265000L;//2019-01-09 19:44:25


    //long firstAppearTime = 1582684439509L; //原
    long firstAppearTime = 1583900897877L;
    String defaultRuleId = "837";
    String ahead1hRuleId = "996";
    String ahead24hRuleId = "842";
    String ahead7dayRuleId = "844";
    String ahead30dayRuleId = "846";
    String aheadMaxRuleId = "1003";

    String protect1DayRuleId = "840";

    int pageSize = 10000;

//    --------------------------------------------------------正常单------------------------------------------


    /**
     * 顾客到场-自助扫码(选自助)，置业顾问：安生
     * 自然登记一人，截至目前的自然登记人数+1
     * 成单后删除置业顾问，订单详情和列表中置业顾问不变
     */
    @Test
    public void A_SR() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "顾客到场-自助扫码（选自助）-创单（选择无渠道）,规则为提前报备时长：0min";

        logger.info("\n\n" + caseName + "\n");

        try {
            String customerPhone = "18210113587";
            String selfCode = "805805";
            String smsCode = "805805";
            String customerName = caseName + "-" + feidan.getNamePro();

            feidan.channelEdit(wudongChannelIdStr, wudongChannelNameStr, "索菲", wudongOwnerPhone, defaultRuleId);

//            登记前查数据
            JSONObject historyRuleDetailB = feidan.historyRuleDetail();
            int naturalVisitorB = historyRuleDetailB.getInteger("natural_visitor");

//            新建一个置业顾问，一会删除
            String adviserPhone = feidan.genPhoneNum();
            String adviserName = "deleteAdviser" + feidan.getNamePro();
            feidan.addAdviser(adviserName, adviserPhone, "");
            JSONArray list = feidan.adviserList(adviserPhone, 1, 1).getJSONArray("list");
            if (list.size() != 1) {
                throw new Exception("置业顾问列表中不存在该置业顾问，手机号=" + adviserPhone);
            }

            String id = list.getJSONObject(0).getString("id");

//            自助扫码
            feidan.selfRegisterHot(customerName, customerPhone, selfCode, id, 0, "MALE");

//            登记后查数据
            JSONObject historyRuleDetailB1 = feidan.historyRuleDetail();
            int naturalVisitorB1 = historyRuleDetailB1.getInteger("natural_visitor");
            if (naturalVisitorB1 - naturalVisitorB != 1) {
                throw new Exception("自然登记一人后，风控数据-截至目前的自然登记人数没有+1，customreName=" + customerName + "，phone=" + customerPhone);
            }

//            刷证
            feidan.witnessUpload(feidan.genCardId(), customerName);

            list = feidan.orderList(-1, customerName, 10).getJSONArray("list");
            String orderId = list.getJSONObject(0).getString("order_id");

//            创单
            String faceUrl = "witness/2224020000000100015/1c32c393-21c2-48b2-afeb-11c197436194";
            feidan.createOrder(customerPhone, orderId, faceUrl, -1, smsCode);

            JSONObject historyRuleDetailA = feidan.historyRuleDetail();
            int naturalVisitorA = historyRuleDetailA.getInteger("natural_visitor");

            if (naturalVisitorA - naturalVisitorB != 1) {
                throw new Exception("顾客到场-自助扫码（选自助）-创单（选择无渠道），风控数据-截至目前的自然登记人数没有+1，orderID：" + orderId);
            }

//            校验
            String channelName = "-";
            String channelStaffName = "-";
            String orderStatusTips = "正常";
            String firstAppear = firstAppearTime + "";
            String reportTime = "-";
            String customerType = "自然访客";
            String visitor = natureCustomer;

            JSONObject orderLinkData = feidan.orderLinkList(orderId);
            JSONObject orderDetail = feidan.orderDetail(orderId);

//            订单详情
            feidan.checkDetail(orderId, customerName, customerPhone, adviserName, channelName, channelStaffName, orderStatusTips,
                    faceUrl, firstAppear, reportTime + "", orderDetail);

//            删除置业顾问，然后查看订单中置业顾问
            feidan.adviserDelete(id);
            feidan.checkDetail(orderId, customerName, customerPhone, adviserName, channelName, channelStaffName, orderStatusTips,
                    faceUrl, firstAppear, reportTime + "", orderDetail);

//        订单详情，列表，关键环节中信息一致性
            feidan.detailListLinkConsist(orderId, customerName);

//        订单环节风险/正常
            feidan.checkNormalOrderLink(orderId, orderLinkData);

//        场内轨迹
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
            feidan.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    /**
     * 顾客到场-创单（选择无渠道），截至目前的自然登记人数+1
     */
    @Test
    public void A_Nochannel() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "顾客到场-创单（选择无渠道）,规则为提前报备时长：0min";

        logger.info("\n\n" + caseName + "\n");

        try {
            String customerPhone = "18210113587";
            String smsCode = "805805";
            String customerName = caseName + "-" + feidan.getNamePro();

            feidan.channelEdit(wudongChannelIdStr, wudongChannelNameStr, "索菲", wudongOwnerPhone, defaultRuleId);

//            成单前查数据
            JSONObject historyRuleDetailB = feidan.historyRuleDetail();
            int naturalVisitorB = historyRuleDetailB.getInteger("natural_visitor");

//            刷证
            feidan.witnessUpload(feidan.genCardId(), customerName);

            JSONArray list = feidan.orderList(-1, customerName, 10).getJSONArray("list");
            String orderId = list.getJSONObject(0).getString("order_id");

//            创单
            String faceUrl = "witness/2224020000000100015/1c32c393-21c2-48b2-afeb-11c197436194";
            feidan.createOrder(customerPhone, orderId, faceUrl, -1, smsCode);

//            成单后查数据
            JSONObject historyRuleDetailA = feidan.historyRuleDetail();
            int naturalVisitorA = historyRuleDetailA.getInteger("natural_visitor");

            if (naturalVisitorA - naturalVisitorB != 1) {
                throw new Exception("顾客到场-创单（选择无渠道），风控数据-截至目前的自然登记人数没有+1，orderID：" + orderId);
            }

//            校验
            String adviserName = "-";
            String channelName = "-";
            String channelStaffName = "-";
            String orderStatusTips = "正常";
            String firstAppear = firstAppearTime + "";
            String reportTime = "-";

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
     * 顾客到场-PC（无渠道）-创单（选择无渠道）
     * 选无渠道
     */
    @Test
    public void A_PCF() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "顾客到场-PC（无渠道）-创单（选择无渠道）,规则为提前报备时长：0min";

        logger.info("\n\n" + caseName + "\n");

        try {
            // PC报备
            String customerPhone = "14422110017";
            String smsCode = "133345";
            String customerName = caseName + "-" + feidan.getNamePro();
            String adviserName = zhangName;
            String adviserPhone = zhangPhone;
            int channelId = -1;

            feidan.channelEdit(wudongChannelIdStr, wudongChannelNameStr, "索菲", wudongOwnerPhone, defaultRuleId);

            feidan.newCustomer(channelId, "", "", adviserName, adviserPhone, customerPhone, customerName, "MALE");

//            刷证
            feidan.witnessUpload(feidan.genCardId(), customerName);

            JSONArray list = feidan.orderList(-1, customerName, 10).getJSONArray("list");
            String orderId = list.getJSONObject(0).getString("order_id");

//            创单
            String faceUrl = "witness/2224020000000100015/1c32c393-21c2-48b2-afeb-11c197436194";
            feidan.createOrder(customerPhone, orderId, faceUrl, channelId, smsCode);

//            校验
            String channelName = "-";
            String channelStaffName = "-";
            String orderStatusTips = "正常";
            String firstAppear = firstAppearTime + "";
            String reportTime = "-";

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
     * 顾客到场-PC（有渠道）-创单（选择PC报备渠道）,规则为默认规则
     */
    @Test(dataProvider = "RISK_1_1")
    public void A_PCT(String caseNamePro, String ruleId, String aheadTime, long reportTime) {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName + "-" + caseNamePro;

        String caseDesc = "顾客到场-PC（有渠道）-创单（选择PC报备渠道）,规则为默认规则";

        logger.info("\n\n" + caseName + "\n");

        try {

            feidan.channelEdit(lianjiaChannelStr, lianjiaChannelName, "于老师", lianjiaOwnerPhone, ruleId);

            // PC报备
            String customerPhone = "14422110001";
            String customerName = caseName + "-" + feidan.getNamePro();
            String adviserName = zhangName;
            String adviserPhone = zhangPhone;
            int channelId = 1;
            int channelStaffId = 2124;
            String channelStaffName = lianjiaStaffName;
            String channelStaffPhone = lianjiaStaffPhone;

            feidan.newCustomer(channelId, channelStaffName, channelStaffPhone, adviserName, adviserPhone, customerPhone, customerName, "MALE");

            feidan.updateReportTimeChannel(customerPhone, customerName, channelId, channelStaffId, reportTime);

//            刷证
            feidan.witnessUpload(feidan.genCardId(), customerName);

            JSONArray list = feidan.orderList(-1, customerName, 10).getJSONArray("list");
            String orderId = list.getJSONObject(0).getString("order_id");

//            创单
            String faceUrl = "witness/2224020000000100015/1c32c393-21c2-48b2-afeb-11c197436194";
            String smsCode = "209237";
            feidan.createOrder(customerPhone, orderId, faceUrl, channelId, smsCode);

//            校验
            String channelName = "链家";
            String orderStatusTips = "风险";
            String firstAppear = firstAppearTime + "";

            int riskNum = 2;

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
            feidan.channelEditFinally(lianjiaChannelStr, lianjiaChannelName, "于老师", lianjiaOwnerPhone, defaultRuleId);
            feidan.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    /**
     * 顾客到场-PC(有渠道)-成单时选择无渠道
     * 成单后自然登记人数+1
     * 输入错误验证码，创单失败
     * 输入正确验证码，创单成功
     */
    @Test(dataProvider = "RISK_1_1")
    public void APCT_Nochannel(String caseNamePro, String ruleId, String aheadTime, long reportTime) {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName + "-" + caseNamePro;

        String caseDesc = "顾客到场-PC（有渠道）-创单（选择无渠道）,规则为默认规则";

        logger.info("\n\n" + caseName + "\n");

        try {

            feidan.channelEdit(lianjiaChannelStr, lianjiaChannelName, "于老师", lianjiaOwnerPhone, ruleId);

            // PC报备
            String customerPhone = "14422110001";
            String customerName = caseName + "-" + feidan.getNamePro();
            String adviserName = zhangName;
            String adviserPhone = zhangPhone;
            int channelId = 1;
            int channelStaffId = 2124;
            String channelStaffName = lianjiaStaffName;
            String channelStaffPhone = lianjiaStaffPhone;

//            成单前查数据
            JSONObject historyRuleDetailB = feidan.historyRuleDetail();
            int naturalVisitorB = historyRuleDetailB.getInteger("natural_visitor");

            feidan.newCustomer(channelId, channelStaffName, channelStaffPhone, adviserName, adviserPhone, customerPhone, customerName, "MALE");

            feidan.updateReportTimeChannel(customerPhone, customerName, channelId, channelStaffId, reportTime);

//            刷证
            feidan.witnessUpload(feidan.genCardId(), customerName);

            JSONArray list = feidan.orderList(-1, customerName, 10).getJSONArray("list");
            String orderId = list.getJSONObject(0).getString("order_id");

//            创单
            String faceUrl = "witness/2224020000000100015/1c32c393-21c2-48b2-afeb-11c197436194";
            String smsCode = "209237";

//            输入错误验证码
            String orderNoCode = feidan.createOrderNoCode(customerPhone, orderId, faceUrl, channelId, "123456");
            feidan.checkCode(orderNoCode, StatusCode.BAD_REQUEST, "成单时输入错误验证码");
            feidan.checkMessage("成单时输入错误验证码", orderNoCode, "短信验证码错误,请重新输入");

//            输入正确验证码,成单时选择无渠道
            feidan.createOrder(customerPhone, orderId, faceUrl, -1, smsCode);

//            成单后查数据
            JSONObject historyRuleDetailA = feidan.historyRuleDetail();
            int naturalVisitorA = historyRuleDetailA.getInteger("natural_visitor");

            if (naturalVisitorA - naturalVisitorB != 1) {
                throw new Exception("顾客到场-PC（有渠道）-创单（选择无渠道），风控数据-截至目前的自然登记人数没有+1，orderID：" + orderId);
            }

//            校验
            String channelName = "链家";
            String orderStatusTips = "风险";
            String firstAppear = firstAppearTime + "";

            int riskNum = 3;

            JSONObject orderLinkData = feidan.orderLinkList(orderId);
            JSONObject orderDetail = feidan.orderDetail(orderId);

//            订单详情
            adviserName = "-";
            channelName = "-";
            channelStaffName = "-";
            feidan.checkDetail(orderId, customerName, customerPhone, adviserName, channelName, channelStaffName, orderStatusTips,
                    faceUrl, firstAppear, "-", orderDetail);

//        订单详情，列表，关键环节中信息一致性
            feidan.detailListLinkConsist(orderId, customerPhone);

//        订单环节风险/正常
            feidan.checkOrderRiskLinkMess(orderId, orderLinkData, "RISK_STATUS_CHANGE", "订单风险状态:未知->风险", "存在3个异常环节");
            feidan.checkOrderRiskLinkMess(orderId, orderLinkData, "CHANNEL_REPORT", "链家-链家业务员", "异常提示:多个渠道报备同一顾客");
            feidan.checkOrderRiskLinkMess(orderId, orderLinkData, "CUSTOMER_CONFIRM_INFO", "顾客在确认信息时表明无渠道介绍", "该顾客成为自然访客");
            feidan.checkOrderRiskLinkNum(orderId, orderLinkData, riskNum);

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            feidan.channelEditFinally(lianjiaChannelStr, lianjiaChannelName, "于老师", lianjiaOwnerPhone, defaultRuleId);
            feidan.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    /**
     * 顾客到场-PC报备-H5报备，置业顾问：张钧甯
     * 选PC报备渠道
     * 成单后更改置业顾问姓名，订单详情和列表中置业顾问不变
     */
    @Test
    public void A_PCTH5R() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "顾客到场-PC报备-H5报备-创单（选择PC报备渠道）,规则为默认规则";

        logger.info("\n\n" + caseName + "\n");

        try {
            String customerPhone = "14422110002";
            String customerName = caseName + "-" + feidan.getNamePro();

            feidan.channelEdit(wudongChannelIdStr, wudongChannelNameStr, "索菲", wudongOwnerPhone, defaultRuleId);

            // PC报备
            String adviserName = zhangName;
            String adviserPhone = zhangPhone;
            int channelId = 1;
            int channelStaffId = 2124;
            String channelStaffName = lianjiaStaffName;
            String channelStaffPhone = lianjiaStaffPhone;

            feidan.newCustomer(channelId, channelStaffName, channelStaffPhone, adviserName, adviserPhone, customerPhone, customerName, "MALE");
            long repTimeLianJia = System.currentTimeMillis();
            feidan.updateReportTimeChannel(customerPhone, customerName, channelId, channelStaffId, repTimeLianJia);

//            H5报备
            feidan.customerReportH5(wudongStaffIdStr, customerName, customerPhone, "MALE", wudongToken);
            long repTimeWuDong = System.currentTimeMillis();
            feidan.updateReportTimeChannel(customerPhone, customerName, wudongChannelInt, wudongStaffIdInt, repTimeWuDong);

//            刷证
            feidan.witnessUpload(feidan.genCardId(), customerName);

            JSONArray list = feidan.orderList(-1, customerName, 10).getJSONArray("list");
            String orderId = list.getJSONObject(0).getString("order_id");

//            创单
            String faceUrl = "witness/2224020000000100015/1c32c393-21c2-48b2-afeb-11c197436194";
            String smsCode = "384435";
            feidan.createOrder(customerPhone, orderId, faceUrl, channelId, smsCode);

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
                    faceUrl, firstAppear, reportTime + "", orderDetail);

//            成单后更改置业顾问姓名
            feidan.adviserEdit(zhangIdStr, "改名", adviserPhone, "");

            feidan.checkDetail(orderId, customerName, customerPhone, adviserName, channelName, channelStaffName, orderStatusTips,
                    faceUrl, firstAppear, reportTime + "", orderDetail);

            list = feidan.orderList(customerName, 1).getJSONArray("list");
            if (list.size() != 1) {
                throw new Exception("订单列表中姓名=" + customerName + "的顾客数量不是1，有" + list.size() + "个");
            }

//            改名后查询
            String adviserNameA = list.getJSONObject(0).getString("adviser_name");
            if (!adviserName.equals(adviserNameA)) {
                throw new Exception("成单后更改置业顾问的名字，订单列表中置业顾问的名字没有保持不变！orderId=" + orderId + "，改名前=" + adviserName + "，改名后=" + adviserNameA);
            }

            feidan.adviserEdit(zhangIdStr, adviserName, adviserPhone, "");

//        订单详情，列表，关键环节中信息一致性
            feidan.detailListLinkConsist(orderId, customerPhone);

//        订单环节风险/正常
            feidan.checkOrderRiskLinkMess(orderId, orderLinkData, "RISK_STATUS_CHANGE", "订单风险状态:未知->风险", "存在3个异常环节");
            feidan.checkOrderRiskLinkMess(orderId, orderLinkData, "CHANNEL_REPORT", "测试FREEZE-FREEZE1", "异常提示:多个渠道报备同一顾客");
            feidan.checkOrderRiskLinkMess(orderId, orderLinkData, "RISK_RULE", "报备时间需大于0h0min", "该顾客的风控规则为提前报备时间:0h0min");
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
     * 顾客到场-PC报备（无渠道）-H5报备(成单时选无渠道)
     */
    @Test(dataProvider = "RISK_1_1")
    public void A_PCFH5(String caseNamePro, String ruleId, String aheadTime, long reportTime) {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName + "-" + caseNamePro;

        String caseDesc = "顾客到场-PC无渠道-H5报备-创单（选择PC无渠道）,规则为默认规则";

        logger.info("\n\n" + caseName + "\n");

        try {
            String customerPhone = "14422110002";
            String customerName = caseName + "-" + feidan.getNamePro();

            feidan.channelEdit(wudongChannelIdStr, wudongChannelNameStr, "索菲", wudongOwnerPhone, ruleId);

            // PC报备
            int channelId = -1;
            String adviserName = zhangName;
            String adviserPhone = zhangPhone;

            feidan.newCustomer(channelId, "", "", adviserName, adviserPhone, customerPhone, customerName, "MALE");
            feidan.updateReportTime_PCF(customerPhone, customerName, reportTime);

//            H5报备
            feidan.customerReportH5(wudongStaffIdStr, customerName, customerPhone, "MALE", wudongToken);
            long repTimeWuDong = System.currentTimeMillis();
            feidan.updateReportTimeChannel(customerPhone, customerName, wudongChannelInt, wudongStaffIdInt, repTimeWuDong);

//            刷证
            feidan.witnessUpload(feidan.genCardId(), customerName);

            JSONArray list = feidan.orderList(-1, customerName, 10).getJSONArray("list");
            String orderId = list.getJSONObject(0).getString("order_id");

//            创单
            String faceUrl = "witness/2224020000000100015/1c32c393-21c2-48b2-afeb-11c197436194";
            String smsCode = "384435";
            feidan.createOrder(customerPhone, orderId, faceUrl, channelId, smsCode);

//            校验
            String channelName = "-";
            String channelStaffName = "-";
            String orderStatusTips = "风险";
            String firstAppear = firstAppearTime + "";

            int riskNum = 3;

            JSONObject orderLinkData = feidan.orderLinkList(orderId);
            JSONObject orderDetail = feidan.orderDetail(orderId);

//            订单详情
            feidan.checkDetail(orderId, customerName, customerPhone, adviserName, channelName, channelStaffName, orderStatusTips,
                    faceUrl, firstAppear, "-", orderDetail);

//        订单详情，列表，关键环节中信息一致性
            feidan.detailListLinkConsist(orderId, customerPhone);

//        订单环节风险/正常
            feidan.checkOrderRiskLinkMess(orderId, orderLinkData, "RISK_STATUS_CHANGE", "订单风险状态:未知->风险", "存在3个异常环节");
            feidan.checkOrderRiskLinkMess(orderId, orderLinkData, "CHANNEL_REPORT", "测试FREEZE-FREEZE1", "异常提示:多个渠道报备同一顾客");
            feidan.checkOrderRiskLinkMess(orderId, orderLinkData, "CUSTOMER_CONFIRM_INFO", "顾客在确认信息时表明无渠道介绍", "该顾客成为自然访客");
            feidan.checkOrderRiskLinkNum(orderId, orderLinkData, riskNum);

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            feidan.channelEditFinally(wudongChannelIdStr, wudongChannelNameStr, "索菲", wudongOwnerPhone, defaultRuleId);
            feidan.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    /**
     * 顾客到场-H5报备-自助扫码（成单时选H5报备渠道）
     * 顾客选H5
     */
    @Test
    public void A_H5S() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "顾客到场-H5报备-自助扫码-创单（选择H5报备渠道）,规则为默认规则";

        logger.info("\n\n" + caseName + "\n");

        try {
            String customerPhone = "18210113587";
            String selfCode = "805805";
            String smsCode = "805805";
            String customerName = caseName + "-" + feidan.getNamePro();

            feidan.channelEdit(wudongChannelIdStr, wudongChannelNameStr, "索菲", wudongOwnerPhone, defaultRuleId);

//            H5报备
            feidan.customerReportH5(wudongStaffIdStr, customerName, customerPhone, "MALE", wudongToken);
            long repTimeWuDong = System.currentTimeMillis();
            feidan.updateReportTimeChannel(customerPhone, customerName, wudongChannelInt, wudongStaffIdInt, repTimeWuDong);

//            自助扫码
            feidan.selfRegisterHot(customerName, customerPhone, selfCode, "", 1, "MALE");

//            刷证
            feidan.witnessUpload(feidan.genCardId(), customerName);

            JSONArray list = feidan.orderList(-1, customerName, 10).getJSONArray("list");
            String orderId = list.getJSONObject(0).getString("order_id");

//            创单
            String faceUrl = "witness/2224020000000100015/1c32c393-21c2-48b2-afeb-11c197436194";
            feidan.createOrder(customerPhone, orderId, faceUrl, wudongChannelInt, smsCode);

//            校验
            String adviserName = "-";
            String channelName = "测试FREEZE";
            String channelStaffName = "FREEZE1";
            String orderStatusTips = "风险";
            String firstAppear = firstAppearTime + "";
            String reportTime = repTimeWuDong + "";

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
     * 顾客到场-PC（无渠道）-创单（选择无渠道），置业顾问是张钧甯
     * 选无渠道
     */
    @Test
    public void witnessFailR() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "图片过期导致刷证失败的订单,规则为默认规则";

        logger.info("\n\n" + caseName + "\n");

        try {
            // PC报备
            String customerPhone = "14422110018";
            String smsCode = "721183";
            String customerName = caseName + "-" + feidan.getNamePro();
            int channelId = -1;

            feidan.channelEdit(wudongChannelIdStr, wudongChannelNameStr, "索菲", wudongOwnerPhone, defaultRuleId);

            feidan.newCustomer(channelId, "", "", "", "", customerPhone, customerName, "MALE");

//            刷证
            feidan.witnessUploadFail(feidan.genCardId(), customerName);

            JSONArray list = feidan.orderList(-1, customerName, 10).getJSONArray("list");
            String orderId = list.getJSONObject(0).getString("order_id");

//            创单
            String faceUrl = "witness/2224020000000100015/1c32c393-21c2-48b2-afeb-11c197436194";
            feidan.createOrder(customerPhone, orderId, faceUrl, channelId, smsCode);

//            校验
            String adviserName = "-";
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
            feidan.checkDetail(orderId, customerName, customerPhone, adviserName, channelName, channelStaffName, orderStatusTips,
                    faceUrl, firstAppear, reportTime, orderDetail);

//        订单详情，列表，关键环节中信息一致性
            feidan.detailListLinkConsist(orderId, customerPhone);

//        订单环节风险/正常
            feidan.checkOrderRiskLinkMess(orderId, orderLinkData, "RISK_STATUS_CHANGE", "订单风险状态:未知->风险", "存在2个异常环节");
            feidan.checkOrderRiskLinkMess(orderId, orderLinkData, "WITNESS_RESULT", "", "异常:人证比对照片未上传,请检查网络连接,请再次刷证");
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
     * 生成未知订单-判断参数非空
     * 选无渠道
     */
    @Test
    public void witnessSuccessUnknown() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "生成未知订单-判断参数非空";

        logger.info("\n\n" + caseName + "\n");

        try {
            String customerName = caseName + "-" + feidan.getNamePro();

            long now = System.currentTimeMillis();

//            刷证
            feidan.witnessUpload(feidan.genCardId(), customerName);

            JSONArray list = feidan.orderList(-1, customerName, 5).getJSONArray("list");

            long dealTime = list.getJSONObject(0).getLongValue("deal_time");
            long orderId = list.getJSONObject(0).getLongValue("order_id");

            if (dealTime - now > 6000 || dealTime < now) {
                String nowStr = dateTimeUtil.timestampToDate("yyyy年MM月dd日 HH:mm:ss", now);
                String dealTimeStr = dateTimeUtil.timestampToDate("yyyy年MM月dd日 HH:mm:ss", dealTime);
                throw new Exception("订单id=" + orderId + "，实际刷证时间=" + nowStr + "，系统显示为=" + dealTimeStr);
            }

            for (int i = 0; i < list.size(); i++) {

                JSONObject single = list.getJSONObject(i);


                Object[] objects = orderListNotNull();

                for (int j = 0; j < objects.length; j++) {

                    checkUtil.checkNotNull("未知订单列表-oderId=" + single.getString("order_id"), single, objects[j].toString());
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

//    --------------------------------------------------------隐藏手机号报备------------------------------------------

    @Test
    public void c2hide1Evident1R() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        logger.info("\n\n" + caseName + "\n");

        String caseDesc = "到场-渠道A报备全号-渠道A报备隐藏手机号-刷证-创单（选择渠道A）,规则为默认规则";

        try {
            feidan.channelEdit(wudongChannelIdStr, wudongChannelNameStr, "索菲", wudongOwnerPhone, defaultRuleId);

            // 报备
            String customerPhone = "14422110000";
            String smsCode = "680636";

            String customerName = caseName + "-" + feidan.getNamePro();

            feidan.customerReportH5(wudongStaffIdStr, customerName, customerPhone, "MALE", wudongToken);
            feidan.customerReportH5(lianjiaStaffIdStr, customerName, "144****0000", "MALE", lianjiaToken);

            long repTime = System.currentTimeMillis();
            feidan.updateReportTimeChannel(customerPhone, customerName, 5, 2098, repTime);

//            刷证
            feidan.witnessUpload(feidan.genCardId(), customerName);

            JSONArray list = feidan.orderList(-1, customerName, 10).getJSONArray("list");
            String orderId = list.getJSONObject(0).getString("order_id");

            String faceUrl = "witness/2224020000000100015/1c32c393-21c2-48b2-afeb-11c197436194";

//            创单
            feidan.createOrder(customerPhone, orderId, faceUrl, 5, smsCode);

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
                    faceUrl, firstAppear, repTime + "", orderDetail);

//        订单详情，列表，关键环节中信息一致性
            feidan.detailListLinkConsist(orderId, customerPhone);

//        订单环节风险/正常
            feidan.checkOrderRiskLinkMess(orderId, orderLinkData, "RISK_STATUS_CHANGE", "订单风险状态:未知->风险", "存在3个异常环节");
            feidan.checkOrderRiskLinkMess(orderId, orderLinkData, "RISK_RULE", "报备时间需大于0h0min", "该顾客的风控规则为提前报备时间:0h0min");
            feidan.checkOrderRiskLinkMess(orderId, orderLinkData, "RISK_REPORT", "链家-链家业务员\n" +
                    "报备号码:144****0000", "异常提示:顾客手机号与报备手机号码部分匹配");
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

    @Test
    public void c2hide1evident1_No() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        logger.info("\n\n" + caseName + "\n");

        String caseDesc = "到场-渠道A报备全号-渠道B报备隐藏手机号-刷证-创单（选择无渠道）,规则为默认规则";

        try {

            feidan.channelEdit(wudongChannelIdStr, wudongChannelNameStr, "索菲", wudongOwnerPhone, defaultRuleId);

            // 报备
            String customerPhone = "14422110000";
            String smsCode = "680636";

            String customerName = caseName + "-" + feidan.getNamePro();

            feidan.customerReportH5(wudongStaffIdStr, customerName, customerPhone, "MALE", wudongToken);
            feidan.customerReportH5(lianjiaStaffIdStr, customerName, "144****0000", "MALE", lianjiaToken);

            long repTime = System.currentTimeMillis();
            feidan.updateReportTimeChannel(customerPhone, customerName, 5, 2098, repTime);

//            刷证
            feidan.witnessUpload(feidan.genCardId(), customerName);

            JSONArray list = feidan.orderList(-1, customerName, 10).getJSONArray("list");
            String orderId = list.getJSONObject(0).getString("order_id");

            String faceUrl = "witness/2224020000000100015/1c32c393-21c2-48b2-afeb-11c197436194";

//            创单
            feidan.createOrder(customerPhone, orderId, faceUrl, -1, smsCode);

//            校验
            String adviserName = "-";
            String channelName = "-";
            String channelStaffName = "-";
            String orderStatusTips = "风险";
            String firstAppear = firstAppearTime + "";

            int riskNum = 4;
            int riskNumA = 5;

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
            feidan.checkOrderRiskLinkMess(orderId, orderLinkData, "RISK_STATUS_CHANGE", "订单风险状态:未知->风险", "存在4个异常环节");
            feidan.checkOrderRiskLinkMess(orderId, orderLinkData, "CUSTOMER_CONFIRM_INFO", "顾客在确认信息时表明无渠道介绍", "该顾客成为自然访客");
            feidan.checkOrderRiskLinkMess(orderId, orderLinkData, "CHANNEL_REPORT", "测试FREEZE-FREEZE1", "异常提示:多个渠道报备同一顾客");
            feidan.checkOrderRiskLinkMess(orderId, orderLinkData, "RISK_REPORT", "链家-链家业务员\n" +
                    "报备号码:144****0000", "异常提示:顾客手机号与报备手机号码部分匹配");
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

    @Test
    public void c2Hide2_No() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        logger.info("\n\n" + caseName + "\n");

        String caseDesc = "到场-渠道A报隐藏手机号-渠道B报备隐藏手机号-刷证-创单（选择无渠道）,规则为默认规则";

        try {

            feidan.channelEdit(wudongChannelIdStr, wudongChannelNameStr, "索菲", wudongOwnerPhone, defaultRuleId);

            // 报备
            String customerPhone = "14422110000";
            String smsCode = "680636";

            String customerName = caseName + "-" + feidan.getNamePro();

            feidan.customerReportH5(wudongStaffIdStr, customerName, "144****0000", "MALE", wudongToken);
            feidan.customerReportH5(lianjiaStaffIdStr, customerName, "144****0000", "MALE", lianjiaToken);

            long repTime = System.currentTimeMillis();
            feidan.updateReportTimeChannel(customerPhone, customerName, 5, 2098, repTime);

//            刷证
            feidan.witnessUpload(feidan.genCardId(), customerName);

            JSONArray list = feidan.orderList(-1, customerName, 10).getJSONArray("list");
            String orderId = list.getJSONObject(0).getString("order_id");

            String faceUrl = "witness/2224020000000100015/1c32c393-21c2-48b2-afeb-11c197436194";

//            创单
            feidan.createOrder(customerPhone, orderId, faceUrl, -1, smsCode);

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
            feidan.checkOrderRiskLinkMess(orderId, orderLinkData, "RISK_REPORT", "测试FREEZE-FREEZE1\n" +
                    "报备号码:144****0000", "异常提示:顾客手机号与报备手机号码部分匹配");
            feidan.checkOrderRiskLinkMess(orderId, orderLinkData, "RISK_REPORT", "链家-链家业务员\n" +
                    "报备号码:144****0000", "异常提示:顾客手机号与报备手机号码部分匹配");
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

    @Test
    public void c2Hide2Comp2_NoR() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        logger.info("\n\n" + caseName + "\n");

        String caseDesc = "渠道A报备隐-渠道A补全-渠道B报备隐藏手机号-B补全-到场-刷证-创单（选择无渠道）,规则为默认规则";

        try {

            feidan.channelEdit(wudongChannelIdStr, wudongChannelNameStr, "索菲", wudongOwnerPhone, defaultRuleId);

            // 报备
            String customerPhone = "14422110000";

            String customerName = caseName + "-" + feidan.getNamePro();

            feidan.customerReportH5(wudongStaffIdStr, customerName, "144****0000", "MALE", wudongToken);

            int channelVisitor = feidan.historyRuleDetail().getInteger("channel_visitor");

//            补全
            JSONObject customer = feidan.customerListH5(1, 10, wudongToken).getJSONArray("list").getJSONObject(0);
            String cid = customer.getString("cid");
            String customerNameRes = customer.getString("customer_name");
            if (!customerName.equals(customerNameRes)) {
                throw new Exception("H5业务员顾客列表中的第一个顾客【" + customerNameRes + "】不是刚报备的顾客【" + customerName + "】");
            }

            int channelVisitor1 = feidan.historyRuleDetail().getInteger("channel_visitor");
            if (channelVisitor != channelVisitor1) {
                throw new Exception("补全后数量错误！");
            }

            feidan.customerEditH5(cid, customerName, customerPhone, wudongToken);

//            报备
            feidan.customerReportH5(lianjiaStaffIdStr, customerName, "144****0000", "MALE", lianjiaToken);

            int channelVisitor2 = feidan.historyRuleDetail().getInteger("channel_visitor");
            if (channelVisitor2 - channelVisitor1 != 1) {
                throw new Exception("其他渠道报备后后数量错误！");
            }

//            补全
            JSONArray list = feidan.customerList(customerName, lianjiaChannelStr, "", 1, 10).getJSONArray("list");

            cid = list.getJSONObject(0).getString("cid");

            feidan.customerEditPC(cid, customerName, customerPhone, "", "");

            int channelVisitor3 = feidan.historyRuleDetail().getInteger("channel_visitor");
            if (channelVisitor2 - channelVisitor3 != 1) {
                throw new Exception("补全后数量错误！");
            }

            long repTime = System.currentTimeMillis();
            feidan.updateReportTimeChannel(customerPhone, customerName, 5, 2098, repTime);

//            刷证
            feidan.witnessUpload(feidan.genCardId(), customerName);

            list = feidan.orderList(-1, customerName, 10).getJSONArray("list");
            String orderId = list.getJSONObject(0).getString("order_id");

            String faceUrl = "witness/2224020000000100015/1c32c393-21c2-48b2-afeb-11c197436194";
            String smsCode = "680636";

//            创单
            feidan.createOrder(customerPhone, orderId, faceUrl, -1, smsCode);

            int channelVisitor4 = feidan.historyRuleDetail().getInteger("channel_visitor");
            if (channelVisitor4 != channelVisitor3) {
                throw new Exception("成单后数量错误！");
            }

//            校验
            String adviserName = "-";
            String channelName = "-";
            String channelStaffName = "-";
            String orderStatusTips = "风险";
            String firstAppear = firstAppearTime + "";

            int riskNum = 4;
            int riskNumA = 5;

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
            feidan.checkOrderRiskLinkMess(orderId, orderLinkData, "RISK_STATUS_CHANGE", "订单风险状态:未知->风险", "存在4个异常环节");
            feidan.checkOrderRiskLinkMess(orderId, orderLinkData, "CHANNEL_REPORT", "测试FREEZE-FREEZE1", "异常提示:多个渠道报备同一顾客");
            feidan.checkOrderRiskLinkMess(orderId, orderLinkData, "CHANNEL_REPORT", "链家-链家业务员", "异常提示:多个渠道报备同一顾客");
            feidan.checkOrderRiskLinkMess(orderId, orderLinkData, "CUSTOMER_CONFIRM_INFO", "顾客在确认信息时表明无渠道介绍", "该顾客成为自然访客");
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

    @DataProvider(name = "RISK_1_1")
    public Object[][] riskCase1Channel1() {
        return new Object[][]{
//                caseName,ruleId,aheadTime,reportTime
                new Object[]{
                        "0min", defaultRuleId, "0h0min", System.currentTimeMillis()
//                        "0min", defaultRuleId, "0h0min", firstAppearTime
                },
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

    @DataProvider(name = "RULE_LIST_NOT_NULL")
    public Object[] orderListNotNull() {
        return new Object[]{
                "gmt_create", "face_url_tmp", "deal_time", "face_url", "risk_link", "wit_status", "customer_name",
                "order_id", "status"
        };
    }
}
