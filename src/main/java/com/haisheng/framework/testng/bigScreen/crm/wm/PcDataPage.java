package com.haisheng.framework.testng.bigScreen.crm.wm;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.crm.CrmScenarioUtil;
import com.haisheng.framework.testng.bigScreen.crm.commonDs.PublicMethod;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.*;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.customer.EnumCarStyle;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.other.EnumFindType;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.sale.EnumAccount;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.util.CommonUtil;
import com.haisheng.framework.util.DateTimeUtil;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class PcDataPage extends TestCaseCommon implements TestCaseStd {
    CrmScenarioUtil crm = CrmScenarioUtil.getInstance();
    private static final EnumAccount zjl = EnumAccount.ZJL_DAILY;
    private Integer totalNum;
    private Integer sumNum;

    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before class initial");
        CommonConfig commonConfig = new CommonConfig();
        //替换checklist的相关信息
        commonConfig.checklistAppId = EnumChecklistAppId.DB_APP_ID_SCREEN_SERVICE.getId();
        commonConfig.checklistConfId = EnumChecklistConfId.DB_SERVICE_ID_CRM_DAILY_SERVICE.getId();
        commonConfig.checklistQaOwner = EnumChecklistUser.WM.getName();
        //替换jenkins-job的相关信息
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.CRM_DAILY_TEST.getJobName());
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, EnumTestProduce.CRM_DAILY.getName());
        //替换钉钉推送
        commonConfig.dingHook = EnumDingTalkWebHook.OPEN_MANAGEMENT_PLATFORM_GRP.getWebHook();
        //放入shopId
        commonConfig.shopId = EnumShopId.PORSCHE_SHOP.getShopId();
        beforeClassInit(commonConfig);
        logger.debug("crm: " + crm);
        CommonUtil.login(zjl);
    }

    @AfterClass
    @Override
    public void clean() {
        afterClassClean();
    }

    @BeforeMethod
    @Override
    public void createFreshCase(Method method) {
        logger.debug("beforeMethod");
        caseResult = getFreshCaseResult(method);
        logger.debug("case: " + caseResult);
    }

    @Test(description = "店面数据分析--选择时间段，累计接待>=累计试驾、累计接待>=累计成交、累计成交>=累计交车")
    public void shopPanel_data_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            for (EnumFindType e : EnumFindType.values()) {
                JSONObject response = crm.shopPannel(e.getType(), "", "");
                int serviceNum = response.getInteger("service");
                int testDriverNum = response.getInteger("test_drive");
                int dealNum = response.getInteger("deal");
                int deliveryNum = response.getInteger("delivery");
                CommonUtil.valueView(serviceNum, testDriverNum, dealNum, deliveryNum);
                Preconditions.checkArgument(serviceNum >= testDriverNum, "店面数据分析--选择时间段，累计接待:" + serviceNum + "<累计试驾:" + testDriverNum);
                Preconditions.checkArgument(serviceNum >= dealNum, "店面数据分析--选择时间段，累计接待:" + serviceNum + "<累计成交:" + dealNum);
                Preconditions.checkArgument(serviceNum >= deliveryNum, "店面数据分析--选择时间段，累计交车:" + serviceNum + "<累计交车:" + deliveryNum);
            }
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("店面数据分析--选择时间段，累计接待>=累计试驾");
        }
    }

    @Test(description = "店面数据分析--选择时间段，相同时间段内：【不选销售顾问】累计接待>=各个销售顾问累计接待之和")
    public void shopPanel_data_2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            compareData("service", "接待");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("店面数据分析--选择时间段，相同时间段内：【不选销售顾问】累计接待>=各个销售顾问累计接待之和");
        }
    }

    @Test(description = "店面数据分析--选择时间段，相同时间段内：【不选销售顾问】累计试驾>=各个销售顾问累计试驾之和")
    public void shopPanel_data_3() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            compareData("test_drive", "试驾");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("店面数据分析--选择时间段，相同时间段内：【不选销售顾问】累计试驾=各个销售顾问累计试驾之和");
        }
    }

    @Test(description = "店面数据分析--选择时间段，相同时间段内：【不选销售顾问】累计成交>=各个销售顾问累计成交之和")
    public void shopPanel_data_4() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            compareData("deal", "成交");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("店面数据分析--选择时间段，相同时间段内：【不选销售顾问】累计试驾=各个销售顾问累计试驾之和");
        }
    }

    @Test(description = "店面数据分析--选择时间段，相同时间段内：【不选销售顾问】累计成交>=各个销售顾问累计交车之和")
    public void shopPanel_data_5() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            compareData("delivery", "交车");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("店面数据分析--选择时间段，相同时间段内：【不选销售顾问】累计试驾=各个销售顾问累计交车之和");
        }
    }

    /**
     * 四项数据比较
     *
     * @param field 比较的字段
     * @param str   字段含义
     */
    private void compareData(final String field, final String str) throws Exception {
        for (EnumFindType e : EnumFindType.values()) {
            JSONObject response = crm.shopPannel(e.getType(), "", "");
            int totalNum = response.getInteger(field);
            int total = crm.userUserPage(1, 10).getInteger("total");
            int s = CommonUtil.pageTurning(total, 100);
            int everySaleNun = 0;
            for (int i = 1; i < s; i++) {
                JSONArray list = crm.userUserPage(i, 100).getJSONArray("list");
                for (int j = 0; j < list.size(); j++) {
                    //每个销售每种搜索类型的数据和
                    if (list.getJSONObject(j).getString("role_name").equals("销售顾问")) {
                        String userId = list.getJSONObject(j).getString("user_id");
                        CommonUtil.valueView(list.getJSONObject(j).getString("user_name"));
                        JSONObject response1 = crm.shopPannel(e.getType(), "", userId);
                        int receptionNum1 = response1.getInteger(field);
                        everySaleNun += receptionNum1;
                        CommonUtil.valueView(everySaleNun);
                        CommonUtil.log("分割线");
                    }
                }
            }
            CommonUtil.valueView(totalNum, everySaleNun);
            Preconditions.checkArgument(totalNum >= everySaleNun, "相同时间段内：【不选销售顾问】累计" + str + totalNum + "<各个销售顾问累计" + str + "之和" + everySaleNun);
        }
    }

    @Test(description = "店面数据分析--客户接待时长分析，相同时间段内：【不选销售顾问】10分钟内组数=各个销售顾问10分钟内组数之和")
    public void shopPanel_data_6() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            compareReceptionTime("10分钟以内");
            Preconditions.checkArgument(totalNum >= sumNum, "相同时间段内：【不选销售顾问】10分钟以内组数" + totalNum + "<各个销售顾问10分钟以内组数之和" + sumNum);

        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("店面数据分析--客户接待时长分析，相同时间段内：【不选销售顾问】10分钟内组数>=各个销售顾问10分钟内组数之和");
        }
    }

    @Test(description = "店面数据分析--客户接待时长分析，相同时间段内：【不选销售顾问】10-30分钟组数=各个销售顾问10-30分钟组数之和")
    public void shopPanel_data_7() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            compareReceptionTime("10-30分钟");
            Preconditions.checkArgument(totalNum >= sumNum, "相同时间段内：【不选销售顾问】10-30分钟组数" + totalNum + "<各个销售顾问10-30分钟组数之和" + sumNum);

        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("店面数据分析--客户接待时长分析，相同时间段内：【不选销售顾问】10-30分钟组数>=各个销售顾问10-30分钟组数之和");
        }
    }

    @Test(description = "店面数据分析--客户接待时长分析，相同时间段内：【不选销售顾问】30～60分钟组数=各个销售顾问30～60分钟组数之和")
    public void shopPanel_data_8() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            compareReceptionTime("30-60分钟");
            Preconditions.checkArgument(totalNum >= sumNum, "相同时间段内：【不选销售顾问】30-60分钟组数" + totalNum + "<各个销售顾问30-60分钟组数之和" + sumNum);
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("店面数据分析--客户接待时长分析，相同时间段内：【不选销售顾问】30-60分钟组数>=各个销售顾问30-60分钟组数之和");
        }
    }

    @Test(description = "店面数据分析--客户接待时长分析，相同时间段内：【不选销售顾问】60-120分钟组数>=各个销售顾问60-120分钟组数之和")
    public void shopPanel_data_9() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            compareReceptionTime("60-120分钟");
            Preconditions.checkArgument(totalNum >= sumNum, "相同时间段内：【不选销售顾问】60-120分钟组数" + totalNum + "<各个销售顾问60-120分钟组数之和" + sumNum);
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("店面数据分析--客户接待时长分析，相同时间段内：【不选销售顾问】60-120分钟组数>=各个销售顾问60-120分钟组数之和");
        }
    }

    @Test(description = "店面数据分析--客户接待时长分析，相同时间段内：【不选销售顾问】120分钟以上组数>=各个销售顾问120分钟以上组数之和")
    public void shopPanel_data_10() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            compareReceptionTime("120分钟以上");
            Preconditions.checkArgument(totalNum >= sumNum, "相同时间段内：【不选销售顾问】120分钟以上组数" + totalNum + "<各个销售顾问120分钟以上组数之和" + sumNum);
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("店面数据分析--客户接待时长分析，相同时间段内：【不选销售顾问】120分钟以上组数>=各个销售顾问120分钟以上组数之和");
        }
    }

    /**
     * 客户接待时长分析
     *
     * @param time 时间段
     */
    private void compareReceptionTime(final String time) throws Exception {
        for (EnumFindType e : EnumFindType.values()) {
            int totalNum = 0;
            JSONArray array = crm.receptTime(e.getType(), "", "").getJSONArray("list");
            for (int i = 0; i < array.size(); i++) {
                if (array.getJSONObject(i).getString("time").equals(time)) {
                    totalNum = array.getJSONObject(i).getInteger("value");
                }
            }
            int total = crm.userUserPage(1, 10).getInteger("total");
            int s = CommonUtil.pageTurning(total, 100);
            int everySaleNun = 0;
            for (int i = 1; i < s; i++) {
                JSONArray list = crm.userUserPage(i, 100).getJSONArray("list");
                for (int j = 0; j < list.size(); j++) {
                    if (list.getJSONObject(j).getString("role_name").equals("销售顾问")) {
                        String userId = list.getJSONObject(j).getString("user_id");
                        CommonUtil.valueView(list.getJSONObject(j).getString("user_name"));
                        //每个销售每种搜索类型的数据和
                        JSONArray list1 = crm.receptTime(e.getType(), "", userId).getJSONArray("list");
                        for (int k = 0; k < list1.size(); k++) {
                            if (list1.getJSONObject(k).getString("time").equals(time)) {
                                int value = list1.getJSONObject(k).getInteger("value");
                                everySaleNun += value;
                                CommonUtil.log("分割线");
                            }
                        }
                    }
                }
            }
            CommonUtil.valueView(totalNum, everySaleNun);
            this.totalNum = totalNum;
            sumNum = everySaleNun;
        }
    }

    @Test(description = "店面数据分析--销售顾问漏斗，线索=创建线索+接待线索")
    public void shopPanel_data_11() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            compareBusinessFunnelData("CLUE");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("店面数据分析--销售顾问漏斗，线索=创建线索+接待线索");
        }
    }

    @Test(description = "店面数据分析--销售顾问漏斗，接待=首次+邀约+再次")
    public void shopPanel_data_12() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            compareBusinessFunnelData("RECEIVE");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("店面数据分析--销售顾问漏斗，接待=首次+邀约+再次");
        }
    }

    @Test(description = "店面数据分析--销售顾问漏斗，试驾=首次+邀约+再次")
    public void shopPanel_data_13() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            compareBusinessFunnelData("TEST_DRIVE");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("店面数据分析--销售顾问漏斗，线索=创建线索+接待线索");
        }
    }

    @Test(description = "店面数据分析--销售顾问漏斗，订单=首次+邀约+再次")
    public void shopPanel_data_14() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            compareBusinessFunnelData("ORDER");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("店面数据分析--销售顾问漏斗，订单=首次+邀约+再次");
        }
    }

    /**
     * 销售顾问业务漏斗数据比较
     *
     * @param type 类型
     */
    private void compareBusinessFunnelData(final String type) throws Exception {
        for (EnumFindType e : EnumFindType.values()) {
            int total = crm.userUserPage(1, 10).getInteger("total");
            int s = CommonUtil.pageTurning(total, 100);
            for (int i = 1; i < s; i++) {
                JSONArray list = crm.userUserPage(i, 100).getJSONArray("list");
                for (int j = 0; j < list.size(); j++) {
                    if (list.getJSONObject(j).getString("role_name").equals("销售顾问")) {
                        String userId = list.getJSONObject(j).getString("user_id");
                        String userName = list.getJSONObject(j).getString("user_name");
                        CommonUtil.valueView(userName);
                        JSONArray list1 = crm.saleFunnel(e.getType(), "", userId).getJSONObject("business").getJSONArray("list");
                        for (int k = 0; k < list1.size(); k++) {
                            if (list1.getJSONObject(k).getString("type").equals(type)) {
                                int totalNum = list1.getJSONObject(k).getInteger("value");
                                int sumNum = 0;
                                JSONArray detail = list1.getJSONObject(k).getJSONArray("detail");
                                for (int u = 0; u < detail.size(); u++) {
                                    int value = detail.getJSONObject(u).getInteger("value");
                                    sumNum += value;
                                }
                                CommonUtil.valueView(totalNum, sumNum);
                                Preconditions.checkArgument(totalNum == sumNum, "问题数据：" + userName + "的" + e.getName() + "查询有问题，查询结果总数：" + totalNum + "后几项之和：" + sumNum);
                                CommonUtil.log("分割线");
                            }
                        }
                    }
                }
            }
        }
    }

    @Test(description = "店面数据分析--销售顾问漏斗，留资率=接待/线索")
    public void shopPanel_data_15() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            compareBusinessFunnelPercent("RECEIVE", "enter_percentage");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("店面数据分析--销售顾问漏斗，留资率=接待/线索");
        }
    }

    @Test(description = "店面数据分析--销售顾问漏斗，试驾率=试驾/线索")
    public void shopPanel_data_16() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            compareBusinessFunnelPercent("TEST_DRIVE", "test_drive_percentage");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("店面数据分析--销售顾问漏斗，留资率=试驾/线索");
        }
    }

    @Test(description = "店面数据分析--销售顾问漏斗，成交率=订单/线索")
    public void shopPanel_data_17() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            compareBusinessFunnelPercent("ORDER", "deal_percentage");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("店面数据分析--销售顾问漏斗，成交率=订单/线索");
        }
    }

    /**
     * 销售顾问业务漏斗百分比比较
     *
     * @param type        业务类型
     * @param percentType 业务百分比
     */
    private void compareBusinessFunnelPercent(final String type, final String percentType) throws Exception {
        for (EnumFindType e : EnumFindType.values()) {
            int clueNum = 0;
            int orderNum = 0;
            int total = crm.userUserPage(1, 10).getInteger("total");
            int s = CommonUtil.pageTurning(total, 100);
            for (int i = 1; i < s; i++) {
                JSONArray list = crm.userUserPage(i, 100).getJSONArray("list");
                for (int j = 0; j < list.size(); j++) {
                    if (list.getJSONObject(j).getString("role_name").equals("销售顾问")) {
                        String userId = list.getJSONObject(j).getString("user_id");
                        String userName = list.getJSONObject(j).getString("user_name");
                        CommonUtil.valueView(userName);
                        JSONObject business = crm.saleFunnel(e.getType(), "", userId).getJSONObject("business");
                        String dealPercentage = business.getString(percentType);
                        JSONArray list1 = business.getJSONArray("list");
                        for (int k = 0; k < list1.size(); k++) {
                            if (list1.getJSONObject(k).getString("type").equals("CLUE")) {
                                clueNum = list1.getJSONObject(k).getInteger("value");
                            }
                            if (list1.getJSONObject(k).getString("type").equals(type)) {
                                orderNum = list1.getJSONObject(k).getInteger("value");
                            }
                        }
                        String result = CommonUtil.getPercent(orderNum, clueNum);
                        CommonUtil.valueView(result, dealPercentage);
                        Preconditions.checkArgument(result.equals(dealPercentage), "问题数据：" + userName + "的" + e.getName() + "查询有问题");
                        CommonUtil.log("分割线");
                    }
                }
            }
        }
    }

    @Test(description = "销售顾问车系漏斗--接待,试驾，接待，交车=各车型数量之和（意向车型）")
    public void shopPanel_data_18() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            for (EnumFindType e : EnumFindType.values()) {
                int total = crm.userUserPage(1, 10).getInteger("total");
                int s = CommonUtil.pageTurning(total, 100);
                for (int i = 1; i < s; i++) {
                    JSONArray list = crm.userUserPage(i, 100).getJSONArray("list");
                    for (int j = 0; j < list.size(); j++) {
                        if (list.getJSONObject(j).getString("role_name").equals("销售顾问")) {
                            String userId = list.getJSONObject(j).getString("user_id");
                            CommonUtil.valueView(list.getJSONObject(j).getString("user_name"));
                            JSONArray list1 = crm.saleFunnel(e.getType(), "", userId).getJSONObject("car_type").getJSONArray("list");
                            for (int k = 0; k < list1.size(); k++) {
                                int totalValue = list1.getJSONObject(k).getInteger("value");
                                String type = list1.getJSONObject(k).getString("type");
                                CommonUtil.valueView(type);
                                JSONArray detail = list1.getJSONObject(k).getJSONArray("detail");
                                int sumNum = 0;
                                for (int u = 0; u < detail.size(); u++) {
                                    int value = detail.getJSONObject(u).getInteger("value");
                                    sumNum += value;
                                }
                                CommonUtil.valueView(totalValue, sumNum);
                                CommonUtil.log("分割线");
                                Preconditions.checkArgument(totalValue == sumNum, type + "总数：" + totalValue + "各车型" + type + "总数：" + sumNum);
                            }
                        }
                    }
                }
            }
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("销售顾问车系漏斗--接待,试驾，接待，交车=各车型数量之和（意向车型）");
        }
    }

    @Test(description = "销售顾问车系漏斗--总经理线索>=各个销售之和")
    public void shopPanel_data_19() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            compareCarTypeFunnelData("CLUE");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("销售顾问车系漏斗--总经理线索>=各个销售之和");
        }
    }

    @Test(description = "销售顾问车系漏斗--总经理商机>=各个销售之和")
    public void shopPanel_data_20() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            compareCarTypeFunnelData("RECEIVE");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("销售顾问车系漏斗--总经理商机>=各个销售之和");
        }
    }

    @Test(description = "销售顾问车系漏斗--总经理试驾>=各个销售之和")
    public void shopPanel_data_21() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            compareCarTypeFunnelData("TEST_DRIVE");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("销售顾问车系漏斗--总经理试驾>=各个销售之和");
        }
    }

    @Test(description = "销售顾问车系漏斗--总经理订单>=各个销售之和")
    public void shopPanel_data_22() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            compareCarTypeFunnelData("ORDER");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("销售顾问车系漏斗--总经理订单>=各个销售之和");
        }
    }

    @Test(description = "销售顾问车系漏斗--总经理交车>=各个销售之和")
    public void shopPanel_data_23() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            compareCarTypeFunnelData("DEAL");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("销售顾问车系漏斗--总经理交车>=各个销售之和");
        }
    }

    /**
     * 销售顾问车系漏斗数据比较
     *
     * @param type 比较类型
     */
    private void compareCarTypeFunnelData(String type) throws Exception {
        for (EnumFindType e : EnumFindType.values()) {
            int y = 0;
            JSONArray zjlList = crm.saleFunnel(e.getType(), "", "").getJSONObject("car_type").getJSONArray("list");
            for (int k = 0; k < zjlList.size(); k++) {
                if (zjlList.getJSONObject(k).getString("type").equals(type)) {
                    y = zjlList.getJSONObject(k).getInteger("value");
                }
            }
            int total = crm.userUserPage(1, 10).getInteger("total");
            int s = CommonUtil.pageTurning(total, 100);
            int sum = 0;
            for (int i = 1; i < s; i++) {
                JSONArray userList = crm.userUserPage(i, 100).getJSONArray("list");
                for (int j = 0; j < userList.size(); j++) {
                    if (userList.getJSONObject(j).getString("role_name").equals("销售顾问")) {
                        String userId = userList.getJSONObject(j).getString("user_id");
                        CommonUtil.valueView(userList.getJSONObject(j).getString("user_name"));
                        JSONArray list = crm.saleFunnel(e.getType(), "", userId).getJSONObject("car_type").getJSONArray("list");
                        int value = 0;
                        for (int a = 0; a < list.size(); a++) {
                            if (list.getJSONObject(a).getString("type").equals(type)) {
                                value = list.getJSONObject(a).getInteger("value");
                            }
                        }
                        sum += value;
                        CommonUtil.valueView(value, sum);
                        CommonUtil.log("分割线");
                    }
                }
            }
            CommonUtil.valueView(y, sum);
            Preconditions.checkArgument(y >= sum, "总经理" + type + "数量为：" + y + ",所有销售" + type + "数量为：" + sum);
        }
    }

    @Test(description = "销售顾问漏斗--【业务漏斗】线索=【车型漏斗】线索")
    public void shopPanel_data_24() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            compareTwoPanelData("CLUE");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("销售顾问车系漏斗--总经理交车>=各个销售之和");
        }
    }

    @Test(description = "销售顾问漏斗--【业务漏斗】商机>=【车型漏斗】商机")
    public void shopPanel_data_25() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            compareTwoPanelData("RECEIVE");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("销售顾问漏斗--【业务漏斗】商机=【车型漏斗】商机");
        }
    }

    @Test(description = "销售顾问漏斗--【业务漏斗】试驾>=【车型漏斗】试驾")
    public void shopPanel_data_26() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            compareTwoPanelData("TEST_DRIVE");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("销售顾问漏斗--【业务漏斗】试驾=【车型漏斗】试驾");
        }
    }

    @Test(description = "销售顾问漏斗--【业务漏斗】订车>=【车型漏斗】订车")
    public void shopPanel_data_27() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            compareTwoPanelData("ORDER");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("销售顾问漏斗--【业务漏斗】订车>=【车型漏斗】订车");
        }
    }

    @Test(description = "销售顾问漏斗--【业务漏斗】交车>=【车型漏斗】交车")
    public void shopPanel_data_28() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            compareTwoPanelData("DEAL");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("销售顾问漏斗--【业务漏斗】交车>=【车型漏斗】交车");
        }
    }

    /**
     * 比较两个漏斗之间数据
     *
     * @param type 类型
     */
    private void compareTwoPanelData(String type) {
        for (EnumFindType e : EnumFindType.values()) {
            List<Map<String, String>> array = new PublicMethod().getSaleList("销售顾问");
            array.forEach(arr -> {
                CommonUtil.valueView(arr.get("userName"));
                JSONObject data = crm.shopSaleFunnel(e.getType(), "", arr.get("userId"));
                JSONArray businessList = data.getJSONObject("business").getJSONArray("list");
                JSONArray carTypeList = data.getJSONObject("car_type").getJSONArray("list");
                class A {
                    int getValue(JSONArray array) {
                        int value = 0;
                        for (int i = 0; i < array.size(); i++) {
                            if (array.getJSONObject(i).getString("type").equals(type)) {
                                value = array.getJSONObject(i).getInteger("value");
                                CommonUtil.valueView(value);
                            }
                        }
                        return value;
                    }
                }
                int businessValue = new A().getValue(businessList);
                int carTypeValue = new A().getValue(carTypeList);
                CommonUtil.valueView(businessValue, carTypeValue);
                Preconditions.checkArgument(businessValue >= carTypeValue, arr.get("userName") +
                        " " + e.getName() + " 【业务漏斗】" + type + "数据为：" + businessValue + ",【车型漏斗】" + type + "数据为：" + carTypeValue);
                CommonUtil.log("分割线");
            });
        }
    }

    @Test(description = "销售顾问漏斗--【业务漏斗】创建线索=【车型漏斗】创建线索,【业务漏斗】接待线索=【车型漏斗】接待线索")
    public void shopPanel_data_29() {
        logger.logCaseStart(caseResult.getCaseName());
        String type = "CLUE";
        try {
            for (EnumFindType e : EnumFindType.values()) {
                List<Map<String, String>> array = new PublicMethod().getSaleList("销售顾问");
                array.forEach(arr -> {
                    CommonUtil.valueView(arr.get("userName"));
                    JSONObject data = crm.shopSaleFunnel(e.getType(), "", arr.get("userId"));
                    JSONArray businessList = data.getJSONObject("business").getJSONArray("list");
                    JSONArray carTypeList = data.getJSONObject("car_type").getJSONArray("list");
                    class A {
                        int[] getValue(JSONArray array) {
                            int[] ints = new int[2];
                            for (int i = 0; i < array.size(); i++) {
                                if (array.getJSONObject(i).getString("type").equals(type)) {
                                    JSONArray detail = array.getJSONObject(i).getJSONArray("detail");
                                    for (int x = 0; x < detail.size(); x++) {
                                        ints[x] = detail.getJSONObject(x).getInteger("value");
                                        CommonUtil.valueView(ints[x]);
                                    }
                                }
                            }
                            return ints;
                        }
                    }
                    int[] business = new A().getValue(businessList);
                    int[] carType = new A().getValue(carTypeList);
                    for (int o = 0; o < business.length; o++) {
                        Preconditions.checkArgument(business[o] == carType[o], arr.get("userName")
                                + e.getName() + "业务线索数为：" + business[o] + "车系线索为：" + carType[o]);
                    }
                    CommonUtil.log("分割线");
                });
            }
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("销售顾问漏斗--【业务漏斗】创建线索=【车型漏斗】创建线索,【业务漏斗】接待线索=【车型漏斗】接待线索");
        }
    }

    @Test(description = "10分钟内组数=【前一日】【销售总监-PC-接待列表】离店时间-接待时间<10分钟的数量")
    public void shopPanel_data_30() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            int num = getReceptionTime(0, 10);
            int value = 0;
            JSONArray list = crm.receptTime("DAY", "", "").getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                if (list.getJSONObject(i).getString("time").equals("10分钟以内")) {
                    value = list.getJSONObject(i).getInteger("value");
                }
            }
            CommonUtil.valueView(num, value);
            Preconditions.checkArgument(num == value, "昨日接待时长10分钟之内的数量为：" + num + "店面数据分析中接待时长10分钟的数量为" + value);
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("10分钟内组数=【前一日】【销售总监-PC-接待列表】离店时间-接待时间<10分钟的数量");
        }
    }

    @Test(description = "10～30分钟组数=【前一日】【销售总监-PC-接待列表】10分钟<离店时间-接待时间<30分钟的数量")
    public void shopPanel_data_31() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            int num = getReceptionTime(10, 30);
            int value = 0;
            JSONArray list = crm.receptTime("DAY", "", "").getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                if (list.getJSONObject(i).getString("time").equals("10-30分钟")) {
                    value = list.getJSONObject(i).getInteger("value");
                }
            }
            CommonUtil.valueView(num, value);
            Preconditions.checkArgument(num == value, "昨日接待时长10～30分钟之内的数量为：" + num + "店面数据分析中接待时长10～30分钟的数量为" + value);
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("10～30分钟组数=【前一日】【销售总监-PC-接待列表】10分钟<离店时间-接待时间<30分钟的数量");
        }
    }

    @Test(description = "30～60分钟内组数=【前一日】【销售总监-PC-接待列表】30分钟<离店时间-接待时间<60分钟的数量")
    public void shopPanel_data_32() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            int num = getReceptionTime(30, 60);
            int value = 0;
            JSONArray list = crm.receptTime("DAY", "", "").getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                if (list.getJSONObject(i).getString("time").equals("30-60分钟")) {
                    value = list.getJSONObject(i).getInteger("value");
                }
            }
            CommonUtil.valueView(num, value);
            Preconditions.checkArgument(num == value, "昨日接待时长30～60分钟之内的数量为：" + num + "店面数据分析中接待时长30～60分钟的数量为" + value);
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("30～60分钟内组数=【前一日】【销售总监-PC-接待列表】30分钟<离店时间-接待时间<60分钟的数量");
        }
    }

    @Test(description = "60～120分钟内组数=【前一日】【销售总监-PC-接待列表】60分钟<离店时间-接待时间<120分钟的数量")
    public void shopPanel_data_33() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            int num = getReceptionTime(60, 120);
            int value = 0;
            JSONArray list = crm.receptTime("DAY", "", "").getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                if (list.getJSONObject(i).getString("time").equals("60-120分钟")) {
                    value = list.getJSONObject(i).getInteger("value");
                }
            }
            CommonUtil.valueView(num, value);
            Preconditions.checkArgument(num == value, "昨日接待时长60～120分钟之内的数量为：" + num + "店面数据分析中接待时长60～120分钟的数量为" + value);
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("60～120分钟内组数=【前一日】【销售总监-PC-接待列表】60分钟<离店时间-接待时间<120分钟的数量");
        }
    }

    @Test(description = "大于120分钟组数=【前一日】【销售总监-PC-接待列表】离店时间-接待时间>120分钟的数量")
    public void shopPanel_data_34() {
        logger.logCaseStart(caseResult.getCaseName());
        double c = Double.POSITIVE_INFINITY;
        try {
            int num = getReceptionTime(120, (int) c);
            int value = 0;
            JSONArray list = crm.receptTime("DAY", "", "").getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                if (list.getJSONObject(i).getString("time").equals("120分钟以上")) {
                    value = list.getJSONObject(i).getInteger("value");
                }
            }
            CommonUtil.valueView(num, value);
            Preconditions.checkArgument(num == value, "昨日接待时长120分钟以上的数量为：" + num + "店面数据分析中接待时长120分钟以上的数量为" + value);
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("大于120分钟组数=【前一日】【销售总监-PC-接待列表】离店时间-接待时间>120分钟的数量");
        }
    }

    /**
     * 区间接待时长总数
     *
     * @param leftTime  左区间时间
     * @param rightTime 右区间时间
     */
    private int getReceptionTime(int leftTime, int rightTime) throws ParseException {
        String date = DateTimeUtil.addDayFormat(new Date(), -1);
        int num = 0;
        int total = crm.receptionPage("", "", "PRE_SALES", 1, 10).getInteger("total");
        int s = CommonUtil.pageTurning(total, 100);
        for (int i = 1; i < s; i++) {
            JSONArray list = crm.receptionPage("", "", "PRE_SALES", i, 100).getJSONArray("list");
            for (int j = 0; j < list.size(); j++) {
                if (list.getJSONObject(j).getString("reception_date").equals(date)) {
                    String receptionTime = list.getJSONObject(j).getString("reception_time");
                    String leaveTime = list.getJSONObject(j).getString("leave_time");
                    CommonUtil.valueView(receptionTime, leaveTime);
                    int x = new DateTimeUtil().calTimeHourDiff(receptionTime, leaveTime);
                    if (x > leftTime && x < rightTime) {
                        num++;
                    }
                    CommonUtil.log("分割线");
                }
            }
        }
        return num;
    }

    @Test(description = "存量客户分析页--个人车主百分比+公司车主百分比=100% 或 0%")
    public void stockCustomer_data_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            for (EnumFindType a : EnumFindType.values()) {
                for (EnumCarStyle b : EnumCarStyle.values()) {
                    CommonUtil.valueView(b.getName());
                    double percentSum = 0;
                    JSONArray ratioList = crm.carOwner(a.getType(), "", b.getStyleId()).getJSONArray("ratio_list");
                    for (int i = 0; i < ratioList.size(); i++) {
                        double percent = ratioList.getJSONObject(i).getDouble("percent");
                        String name = ratioList.getJSONObject(i).getString("name");
                        CommonUtil.valueView(name + ":" + percent);
                        percentSum += percent;
                    }
                    CommonUtil.valueView(percentSum);
                    Preconditions.checkArgument(percentSum == 1.0 || percentSum == 0.0, b.getName() + a.getName() + "查询" + "个人车主百分比+公司车主百分比=" + percentSum);
                    CommonUtil.log("分割线");
                }
            }
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("存量客户分析页--个人车主百分比+公司车主百分比=100% 或 0%");
        }
    }

    @Test(description = "存量客户分析页--车主年龄分析 各年龄段之和=100%或 0%")
    public void stockCustomer_data_2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            for (EnumFindType a : EnumFindType.values()) {
                for (EnumCarStyle b : EnumCarStyle.values()) {
                    CommonUtil.valueView(b.getName());
                    double percentageSum = 0;
                    JSONArray list = crm.genderAge(a.getType(), "", b.getStyleId()).getJSONObject("age").getJSONArray("list");
                    for (int i = 0; i < list.size(); i++) {
                        double percentage = list.getJSONObject(i).getDouble("percentage");
                        String age = list.getJSONObject(i).getString("age");
                        CommonUtil.valueView(age + ":" + percentage);
                        percentageSum += percentage;
                    }
                    CommonUtil.valueView(percentageSum);
                    Preconditions.checkArgument(percentageSum == 1.0 || percentageSum == 0.0, b.getName() + a.getName() + "查询" + "车主年龄分析 各年龄段之和=" + percentageSum);
                    CommonUtil.log("分割线");
                }
            }
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("存量客户分析页--个人车主百分比+公司车主百分比=100% 或 0%");
        }
    }

    @Test(description = "车主性别分析 性别之和=100%或 0%")
    public void stockCustomer_data_3() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            for (EnumFindType a : EnumFindType.values()) {
                for (EnumCarStyle b : EnumCarStyle.values()) {
                    CommonUtil.valueView(b.getName());
                    double percentageSum = 0;
                    JSONArray list = crm.genderAge(a.getType(), "", b.getStyleId()).getJSONObject("gender").getJSONArray("list");
                    for (int i = 0; i < list.size(); i++) {
                        double percentage = list.getJSONObject(i).getDouble("percentage");
                        String gender = list.getJSONObject(i).getString("gender");
                        CommonUtil.valueView(gender + ":" + percentage);
                        percentageSum += percentage;
                    }
                    CommonUtil.valueView(percentageSum);
                    Preconditions.checkArgument(percentageSum == 1.0 || percentageSum == 0.0, b.getName() + a.getName() + "查询" + "车主性别分析 性别之和=" + percentageSum);
                    CommonUtil.log("分割线");
                }
            }
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("车主性别分析 性别之和=100%或 0%");
        }
    }

    @Test(description = "某个省的百分比=该省成交量/各省成交量之和")
    public void stockCustomer_data_4() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            for (EnumFindType a : EnumFindType.values()) {
                for (EnumCarStyle b : EnumCarStyle.values()) {
                    int totalValue = 0;
                    CommonUtil.valueView(b.getName());
                    JSONArray list = crm.wholeCountry(a.getType(), "", b.getStyleId()).getJSONArray("list");
                    for (int i = 0; i < list.size(); i++) {
                        int value = list.getJSONObject(i).getInteger("value");
                        totalValue += value;
                    }
                    for (int i = 0; i < list.size(); i++) {
                        String district = list.getJSONObject(i).getString("district");
                        int value = list.getJSONObject(i).getInteger("value");
                        double percentage = list.getJSONObject(i).getDouble("percentage");
                        CommonUtil.valueView(district + "数量：" + value + "，占比：" + percentage);
                        double result = CommonUtil.getDecimal((double) value / totalValue, 2);
                        CommonUtil.valueView(district + "计算占比：" + result);
                        Preconditions.checkArgument(result == percentage, b.getName() + a.getName() + "查询" + district + "接口返回占比：" + percentage + "，计算占比：" + result);
                    }
                    CommonUtil.log("分割线");
                }
            }
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("车主性别分析 性别之和=100%或 0%");
        }
    }

    @Test(description = "某个区的百分比=该区成交量/各区成交量之和")
    public void stockCustomer_data_5() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            for (EnumFindType a : EnumFindType.values()) {
                for (EnumCarStyle b : EnumCarStyle.values()) {
                    int totalValue = 0;
                    CommonUtil.valueView(b.getName());
                    JSONArray list = crm.city(a.getType(), "", b.getStyleId(), 320500).getJSONArray("list");
                    for (int i = 0; i < list.size(); i++) {
                        int value = list.getJSONObject(i).getInteger("value");
                        totalValue += value;
                    }
                    for (int i = 0; i < list.size(); i++) {
                        String district = list.getJSONObject(i).getString("district");
                        int value = list.getJSONObject(i).getInteger("value");
                        double percentage = list.getJSONObject(i).getDouble("percentage");
                        CommonUtil.valueView(district + "数量：" + value + "，占比：" + percentage);
                        double result = CommonUtil.getDecimal((double) value / totalValue, 2);
                        CommonUtil.valueView(district + "计算占比：" + result);
                        Preconditions.checkArgument(result == percentage, b.getName() + a.getName() + "查询" + district + "接口返回占比：" + percentage + "，计算占比：" + result);
                    }
                    CommonUtil.log("分割线");
                }
            }
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("某个区的百分比=该区成交量/各区成交量之和");
        }
    }

    @Test(description = "个人车主数量=【前一日】客户名称小于等于5个字的客户订车数量")
    public void stockCustomer_data_6() {
        logger.logCaseStart(caseResult.getCaseName());
        String date = DateTimeUtil.addDayFormat(new Date(), -1);
        try {
            int personage = 0;
            int company = 0;
            JSONArray ratioLis = crm.carOwner("DAY", "", "").getJSONArray("ratio_list");
            for (int i = 0; i < ratioLis.size(); i++) {
                if (ratioLis.getJSONObject(i).getString("name").equals("个人车主")) {
                    personage = ratioLis.getJSONObject(i).getInteger("value");
                }
                if (ratioLis.getJSONObject(i).getString("name").equals("公司车主")) {
                    company = ratioLis.getJSONObject(i).getInteger("value");
                }
            }
            int receptionPerson = 0;
            int receptionCompany = 0;
            int total = crm.customerList("", "", "", date, date, 1, 10).getInteger("total");
            int s = CommonUtil.pageTurning(total, 100);
            for (int i = 1; i < s; i++) {
                JSONArray list = crm.customerList("", "", "", date, date, i, 10).getJSONArray("list");
                for (int j = 0; j < list.size(); j++) {
                    if (list.getJSONObject(j).getString("buy_car_name").equals("是")) {
                        if (list.getJSONObject(j).getString("customer_name").length() <= 5) {
                            receptionPerson++;
                        }
                        if (list.getJSONObject(j).getString("customer_name").length() >= 6) {
                            receptionCompany++;
                        }
                    }
                }
            }
            CommonUtil.valueView(personage, receptionPerson, company, receptionCompany);
            Preconditions.checkArgument(personage == receptionPerson, "昨日个人车主客户数为：" + personage + "," + "接待订车个人客户数量：" + receptionPerson);
            Preconditions.checkArgument(company == receptionCompany, "昨日公司车主客户数为：" + personage + "," + "接待订车公司客户数量：" + receptionPerson);
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("个人车主数量=【前一日】客户名称小于等于5个字的客户订车数量");
        }
    }

    @Test(description = "试乘试驾次数：= （时间段内）【APP-销售总监-我的试驾】审核通过&未取消的数量一致")
    public void exhibitionHall_data_1() {
        logger.logCaseStart(caseResult.getCaseName());
        String date = DateTimeUtil.addDayFormat(new Date(), -1);
        try {
            int driver = 0;
            int appDriver = 0;
            JSONArray list = crm.skuRank("DAY", "").getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                int value = list.getJSONObject(i).getInteger("drive");
                driver += value;
            }
            int total = crm.testDriverAppList("", date, date, 10, 1).getInteger("total");
            int s = CommonUtil.pageTurning(total, 100);
            for (int i = 1; i < s; i++) {
                JSONArray appList = crm.testDriverAppList("", date, date, 10, i).getJSONArray("list");
                for (int j = 0; j < appList.size(); j++) {
                    if (!appList.getJSONObject(j).getString("audit_status_name").equals("已取消")) {
                        appDriver++;
                    }
                }
            }
            CommonUtil.valueView(driver, appDriver);
            Preconditions.checkArgument(driver == appDriver, "昨日app记录试驾数：" + appDriver + "昨日展厅热区分析各车型试乘试驾数：" + driver);
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("试乘试驾次数：= （时间段内）【APP-销售总监-我的试驾】审核通过&未取消的数量一致");
        }
    }
}
