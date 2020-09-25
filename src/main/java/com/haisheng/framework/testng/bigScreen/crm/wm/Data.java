package com.haisheng.framework.testng.bigScreen.crm.wm;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.crm.CrmScenarioUtil;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.*;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.other.EnumFindType;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.sale.EnumAccount;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.util.CommonUtil;
import org.jooq.util.derby.sys.Sys;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

public class Data extends TestCaseCommon implements TestCaseStd {
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
     * 数据比较处理
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
            compareFunnelData("CLUE");
            Preconditions.checkArgument(totalNum.equals(sumNum), "线索" + totalNum + "!=创建线索+接待线索" + sumNum);
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
            compareFunnelData("RECEIVE");
            Preconditions.checkArgument(totalNum.equals(sumNum), "接待数" + totalNum + "!=首次+邀约+再次" + sumNum);
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
            compareFunnelData("TEST_DRIVE");
            Preconditions.checkArgument(totalNum.equals(sumNum), "试驾" + totalNum + "!=首次+邀约+再次" + sumNum);
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
            compareFunnelData("ORDER");
            Preconditions.checkArgument(totalNum.equals(sumNum), "订单" + totalNum + "!=首次+邀约+再次" + sumNum);
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("店面数据分析--销售顾问漏斗，订单=首次+邀约+再次");
        }
    }

    /**
     * 销售顾问漏斗
     *
     * @param type 类型
     */
    private void compareFunnelData(final String type) throws Exception {
        for (EnumFindType e : EnumFindType.values()) {
            int totalNum = 0;
            int sumNum = 0;
            JSONArray list = crm.saleFunnel(e.getType(), "", "").getJSONObject("business").getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                if (list.getJSONObject(i).getString("type").equals(type)) {
                    totalNum = list.getJSONObject(i).getInteger("value");
                    JSONArray detail = list.getJSONObject(i).getJSONArray("detail");
                    for (int j = 0; j < detail.size(); j++) {
                        int value = detail.getJSONObject(j).getInteger("value");
                        sumNum += value;
                    }
                }
            }
            CommonUtil.valueView(totalNum, sumNum);
            this.totalNum = totalNum;
            this.sumNum = sumNum;
        }
    }

    @Test(description = "店面数据分析--销售顾问漏斗，留资率=接待/线索")
    public void shopPanel_data_15() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            for (EnumFindType e : EnumFindType.values()) {
                int clueNum = 0;
                int receiveNum = 0;
                JSONObject business = crm.saleFunnel(e.getType(), "", "").getJSONObject("business");
                String enterPercentage = business.getString("enter_percentage");
                JSONArray list = business.getJSONArray("list");
                for (int i = 0; i < list.size(); i++) {
                    if (list.getJSONObject(i).getString("type").equals("CLUE")) {
                        clueNum = list.getJSONObject(i).getInteger("value");
                    }
                    if (list.getJSONObject(i).getString("type").equals("RECEIVE")) {
                        receiveNum = list.getJSONObject(i).getInteger("value");
                    }
                }
                String result = CommonUtil.getPercent(receiveNum, clueNum);
                CommonUtil.valueView(result, enterPercentage);
                Preconditions.checkArgument(result.equals(enterPercentage));
                CommonUtil.log("分割线");
            }
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
            for (EnumFindType e : EnumFindType.values()) {
                int clueNum = 0;
                int testDriveNum = 0;
                JSONObject business = crm.saleFunnel(e.getType(), "", "").getJSONObject("business");
                String testDrivePercentage = business.getString("test_drive_percentage");
                JSONArray list = business.getJSONArray("list");
                for (int i = 0; i < list.size(); i++) {
                    if (list.getJSONObject(i).getString("type").equals("CLUE")) {
                        clueNum = list.getJSONObject(i).getInteger("value");
                    }
                    if (list.getJSONObject(i).getString("type").equals("TEST_DRIVE")) {
                        testDriveNum = list.getJSONObject(i).getInteger("value");
                    }
                }
                String result = CommonUtil.getPercent(testDriveNum, clueNum);
                CommonUtil.valueView(result, testDrivePercentage);
                Preconditions.checkArgument(result.equals(testDrivePercentage));
                CommonUtil.log("分割线");
            }
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
            for (EnumFindType e : EnumFindType.values()) {
                int clueNum = 0;
                int orderNum = 0;
                JSONObject business = crm.saleFunnel(e.getType(), "", "").getJSONObject("business");
                String dealPercentage = business.getString("deal_percentage");
                JSONArray list = business.getJSONArray("list");
                for (int i = 0; i < list.size(); i++) {
                    if (list.getJSONObject(i).getString("type").equals("CLUE")) {
                        clueNum = list.getJSONObject(i).getInteger("value");
                    }
                    if (list.getJSONObject(i).getString("type").equals("ORDER")) {
                        orderNum = list.getJSONObject(i).getInteger("value");
                    }
                }
                String result = CommonUtil.getPercent(orderNum, clueNum);
                CommonUtil.valueView(result, dealPercentage);
                Preconditions.checkArgument(result.equals(dealPercentage));
                CommonUtil.log("分割线");
            }
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("店面数据分析--销售顾问漏斗，成交率=订单/线索");
        }
    }
}
