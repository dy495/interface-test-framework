package com.haisheng.framework.testng.bigScreen.crmOnline.wm;

import com.alibaba.fastjson.JSONArray;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.*;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.other.EnumFindType;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.sale.EnumAccount;
import com.haisheng.framework.testng.bigScreen.crm.wm.scene.IScene;
import com.haisheng.framework.testng.bigScreen.crm.wm.scene.pc.Analysis2ShopReceptTimeScene;
import com.haisheng.framework.testng.bigScreen.crm.wm.util.UserUtil;
import com.haisheng.framework.testng.bigScreen.crmOnline.CrmScenarioUtilOnline;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.util.CommonUtil;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PcDataPagePorsche extends TestCaseCommon implements TestCaseStd {

    CrmScenarioUtilOnline crm = CrmScenarioUtilOnline.getInstance();
    private static final EnumAccount zj = EnumAccount.ZJ_ONLINE_PORSCHE;
    private static final String shopId = EnumShopId.PORSCHE_ONLINE.getShopId();

    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before class initial");
        CommonConfig commonConfig = new CommonConfig();
        //替换checklist的相关信息
        commonConfig.checklistAppId = EnumChecklistAppId.DB_APP_ID_SCREEN_SERVICE.getId();
        commonConfig.checklistConfId = EnumChecklistConfId.DB_SERVICE_ID_CRM_ONLINE_SERVICE.getId();
        commonConfig.checklistQaOwner = EnumChecklistUser.WM.getName();
        //替换jenkins-job的相关信息
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.CRM_ONLINE_TEST.getJobName());
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, EnumTestProduce.CRM_ONLINE_PORSCHE.getName());
        //替换钉钉推送
        commonConfig.dingHook = EnumDingTalkWebHook.ONLINE_CAR_CAR_OPEN_MANAGEMENT_PLATFORM_GRP.getWebHook();
        //放入shopId
        commonConfig.shopId = shopId;
        beforeClassInit(commonConfig);
        logger.debug("crm: " + crm);
        UserUtil.login(zj);
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

    @Test(description = "店面数据分析--客户接待时长分析，【各时间段】相同时间段内：【不选销售顾问】10分钟内组数=各个销售顾问10分钟内组数之和")
    public void shopPanel_data_6() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            compareReceptionTime("10分钟以内");
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            saveData("店面数据分析--客户接待时长分析，【各时间段】相同时间段内：【不选销售顾问】10分钟内组数=各个销售顾问10分钟内组数之和");
        }
    }

    @Test(description = "店面数据分析--客户接待时长分析，【各时间段】相同时间段内：【不选销售顾问】10～30分钟组数=各个销售顾问10～30分钟组数之和")
    public void shopPanel_data_7() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            compareReceptionTime("10-30分钟");
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            saveData("店面数据分析--客户接待时长分析，【各时间段】相同时间段内：【不选销售顾问】10～30分钟组数=各个销售顾问10～30分钟组数之和");
        }
    }

    @Test(description = "店面数据分析--客户接待时长分析，【各时间段】相同时间段内：【不选销售顾问】30～60分钟组数=各个销售顾问30～60分钟组数之和")
    public void shopPanel_data_8() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            compareReceptionTime("30-60分钟");
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            saveData("店面数据分析--客户接待时长分析，【各时间段】相同时间段内：【不选销售顾问】30～60分钟组数=各个销售顾问30～60分钟组数之和");
        }
    }

    @Test(description = "店面数据分析--客户接待时长分析，【各时间段】相同时间段内：【不选销售顾问】60～120分钟组数=各个销售顾问60～120分钟组数之和")
    public void shopPanel_data_9() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            compareReceptionTime("60-120分钟");
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            saveData("店面数据分析--客户接待时长分析，【各时间段】相同时间段内：【不选销售顾问】60～120分钟组数=各个销售顾问60～120分钟组数之和");
        }
    }

    @Test(description = "店面数据分析--客户接待时长分析，【各时间段】相同时间段内：【不选销售顾问】大于120分钟组数=各个销售顾问大于120分钟组数之和")
    public void shopPanel_data_10() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            compareReceptionTime("120分钟以上");
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            saveData("店面数据分析--客户接待时长分析，【各时间段】相同时间段内：【不选销售顾问】大于120分钟组数=各个销售顾问大于120分钟组数之和");
        }
    }

    /**
     * 客户接待时长分析
     *
     * @param time 时间段
     */
    private void compareReceptionTime(final String time) {
        for (EnumFindType e : EnumFindType.values()) {
            int zjlNum = 0;
            int gwSum = 0;
            List<Map<String, String>> list = getSaleList("销售顾问");
            for (Map<String, String> map : list) {
                CommonUtil.valueView(map.get("userName") + e.getName());
                IScene scene = Analysis2ShopReceptTimeScene.builder().cycleType(e.getType()).saleId(map.get("userId")).build();
                if (!map.get("userName").contains("销售总监")) {
                    JSONArray jsonArray = crm.invokeApi(scene).getJSONArray("list");
                    for (int i = 0; i < jsonArray.size(); i++) {
                        if (jsonArray.getJSONObject(i).getString("time").equals(time)) {
                            int num = jsonArray.getJSONObject(i).getInteger("value");
                            CommonUtil.valueView(map.get("userName") + "：" + num);
                            gwSum += num;
                            CommonUtil.valueView("各顾问之和：" + gwSum);
                        }
                    }
                } else {
                    JSONArray jsonArray = crm.invokeApi(scene).getJSONArray("list");
                    for (int i = 0; i < jsonArray.size(); i++) {
                        if (jsonArray.getJSONObject(i).getString("time").equals(time)) {
                            zjlNum = jsonArray.getJSONObject(i).getInteger("value");
                            CommonUtil.valueView("销售总监" + zjlNum);
                        }
                    }
                }
                CommonUtil.logger(map.get("userName"));
            }
            CommonUtil.valueView(zjlNum, gwSum);
            Preconditions.checkArgument(zjlNum >= gwSum, e.getName() + "【不选销售顾问】" + time + "组数：" + zjlNum + " 各个销售顾问" + time + "组数之和：" + gwSum);
        }
    }

    /**
     * 获取人员列表
     *
     * @return list
     */
    public List<Map<String, String>> getSaleList(String roleName) {
        List<Map<String, String>> array = new ArrayList<>();
        int total = crm.userUserPage(1, 10).getInteger("total");
        int s = CommonUtil.getTurningPage(total, 100);
        for (int i = 1; i < s; i++) {
            JSONArray list = crm.userUserPage(i, 100).getJSONArray("list");
            for (int j = 0; j < list.size(); j++) {
                if (list.getJSONObject(j).getString("role_name").equals(roleName)) {
                    Map<String, String> map = new HashMap<>(16);
                    String userId = list.getJSONObject(j).getString("user_id");
                    String userName = list.getJSONObject(j).getString("user_name");
                    String account = list.getJSONObject(j).getString("user_login_name");
                    map.put("userId", userId);
                    map.put("userName", userName);
                    map.put("account", account);
                    array.add(map);
                }
            }
        }
        Map<String, String> map = new HashMap<>(16);
        map.put("userId", "");
        map.put("userName", "销售总监(演示)");
        map.put("account", "xszj");
        array.add(map);
        return array;
    }
}
