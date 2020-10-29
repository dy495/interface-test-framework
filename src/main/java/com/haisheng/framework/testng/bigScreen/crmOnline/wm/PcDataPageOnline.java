package com.haisheng.framework.testng.bigScreen.crmOnline.wm;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.crm.wm.container.EnumContainer;
import com.haisheng.framework.testng.bigScreen.crm.wm.container.Factory;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.*;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.customer.EnumCarStyle;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.other.EnumFindType;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.sale.EnumAccount;
import com.haisheng.framework.testng.bigScreen.crm.wm.scene.IScene;
import com.haisheng.framework.testng.bigScreen.crm.wm.scene.app.CustomerInfoScene;
import com.haisheng.framework.testng.bigScreen.crm.wm.scene.pc.*;
import com.haisheng.framework.testng.bigScreen.crm.wm.sql.Sql;
import com.haisheng.framework.testng.bigScreen.crm.wm.util.UserUtil;
import com.haisheng.framework.testng.bigScreen.crmOnline.CrmScenarioUtilOnline;
import com.haisheng.framework.testng.bigScreen.crmOnline.commonDsOnline.PublicMethodOnline;
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
import java.util.Date;
import java.util.List;
import java.util.Map;

public class PcDataPageOnline extends TestCaseCommon implements TestCaseStd {
    PublicMethodOnline method = new PublicMethodOnline();
    CrmScenarioUtilOnline crm = CrmScenarioUtilOnline.getInstance();
    private static final EnumAccount zjl = EnumAccount.ZJL_ONLINE;
    private static final String shopId = EnumShopId.WIN_SENSE_SHOP_ONLINE.getShopId();
    private int s;
    private int v;

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
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, EnumTestProduce.CRM_ONLINE.getName());
        //替换钉钉推送
        commonConfig.dingHook = EnumDingTalkWebHook.ONLINE_CAR_CAR_OPEN_MANAGEMENT_PLATFORM_GRP.getWebHook();
        //放入shopId
        commonConfig.shopId = EnumShopId.WIN_SENSE_SHOP_ONLINE.getShopId();
        beforeClassInit(commonConfig);
        logger.debug("crm: " + crm);
        UserUtil.login(zjl);
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

    //    --------------------------------------------------4个数据展示------------------------------------------------------

    @Test(description = "店面数据分析--【各时间段】相同时间段内：【不选销售顾问】累计接待>=各个销售顾问累计接待之和")
    public void shopPanel_data_2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            compareFourData("service", "接待");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("店面数据分析--【各时间段】相同时间段内：【不选销售顾问】累计接待>=各个销售顾问累计接待之和");
        }
    }

    @Test(description = "店面数据分析--【各时间段】相同时间段内：【不选销售顾问】累计试驾=各个销售顾问累计试驾之和")
    public void shopPanel_data_3() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            compareFourData("test_drive", "试驾");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("店面数据分析--【各时间段】相同时间段内：【不选销售顾问】累计试驾=各个销售顾问累计试驾之和");
        }
    }

    @Test(description = "店面数据分析--【各时间段】相同时间段内：【不选销售顾问】累计成交=各个销售顾问累计成交之和")
    public void shopPanel_data_4() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            compareFourData("deal", "成交");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("店面数据分析--【各时间段】相同时间段内：【不选销售顾问】累计成交=各个销售顾问累计成交之和");
        }
    }

    @Test(description = "店面数据分析--【各时间段】相同时间段内：【不选销售顾问】累计交车=各个销售顾问累计交车之和")
    public void shopPanel_data_5() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            compareFourData("delivery", "交车");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("店面数据分析--【各时间段】相同时间段内：【不选销售顾问】累计交车=各个销售顾问累计交车之和");
        }
    }

    /**
     * 四项数据比较
     *
     * @param type 比较的字段
     * @param str  名称
     */
    private void compareFourData(final String type, String str) {
        for (EnumFindType e : EnumFindType.values()) {
            int gwSum = 0;
            int zjlNum = 0;
            List<Map<String, String>> list = method.getSaleList("销售顾问");
            for (Map<String, String> map : list) {
                CommonUtil.valueView(map.get("userName") + e.getName());
                IScene scene = Analysis2ShopPanelScene.builder().cycleType(e.getType()).saleId(map.get("userId")).build();
                if (!map.get("userName").contains("总经理")) {
                    int num = crm.invokeApi(scene).getInteger(type);
                    CommonUtil.valueView(num);
                    gwSum += num;
                    CommonUtil.valueView("各顾问之和：" + gwSum);
                } else {
                    zjlNum = crm.invokeApi(scene).getInteger(type);
                    CommonUtil.valueView("总经理：" + zjlNum);
                }
                CommonUtil.logger(map.get("userName"));
            }
            CommonUtil.valueView(gwSum, zjlNum);
            Preconditions.checkArgument(zjlNum >= gwSum, e.getName() + "【不选销售顾问】累计" + str + "：" + zjlNum + " 各个销售顾问累计" + str + "：" + gwSum);
            CommonUtil.logger(e.getName());
        }
    }

//    ----------------------------------------------------接待时长--------------------------------------------------------

    @Test(description = "店面数据分析--客户接待时长分析，【各时间段】相同时间段内：【不选销售顾问】10分钟内组数=各个销售顾问10分钟内组数之和")
    public void shopPanel_data_6() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            compareReceptionTime("10分钟以内");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
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
            appendFailreason(e.toString());
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
            appendFailreason(e.toString());
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
            appendFailreason(e.toString());
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
            appendFailreason(e.toString());
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
            List<Map<String, String>> list = method.getSaleList("销售顾问");
            for (Map<String, String> map : list) {
                CommonUtil.valueView(map.get("userName") + e.getName());
                IScene scene = Analysis2ShopReceptTimeScene.builder().cycleType(e.getType()).saleId(map.get("userId")).build();
                if (!map.get("userName").contains("总经理")) {
                    JSONArray jsonArray = crm.invokeApi(scene).getJSONArray("list");
                    for (int i = 0; i < jsonArray.size(); i++) {
                        if (jsonArray.getJSONObject(i).getString("time").equals(time)) {
                            int num = jsonArray.getJSONObject(i).getInteger("value");
                            CommonUtil.valueView(num);
                            gwSum += num;
                            CommonUtil.valueView("各顾问之和：" + gwSum);
                        }
                    }
                } else {
                    JSONArray jsonArray = crm.invokeApi(scene).getJSONArray("list");
                    for (int i = 0; i < jsonArray.size(); i++) {
                        if (jsonArray.getJSONObject(i).getString("time").equals(time)) {
                            zjlNum = jsonArray.getJSONObject(i).getInteger("value");
                            CommonUtil.valueView("总经理：" + zjlNum);
                        }
                    }
                }
                CommonUtil.logger(map.get("userName"));
            }
            CommonUtil.valueView(zjlNum, gwSum);
            Preconditions.checkArgument(zjlNum >= gwSum, e.getName() + "【不选销售顾问】" + time + "组数：" + zjlNum + " 各个销售顾问" + time + "组数之和：" + gwSum);
        }
    }

//    ----------------------------------------------------业务漏斗--------------------------------------------------------

    @Test(description = "店面数据分析--业务漏斗，【各时间段+各销售】线索=创建线索+接待线索")
    public void shopPanel_data_11() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            compareBusinessFunnelData("CLUE", "business");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("店面数据分析--业务漏斗，【各时间段+各销售】线索=创建线索+接待线索");
        }
    }

    @Test(description = "店面数据分析--业务漏斗，【各时间段+各销售】商机=FU+PU")
    public void shopPanel_data_12() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            for (EnumFindType e : EnumFindType.values()) {
                List<Map<String, String>> list = method.getSaleList("销售顾问");
                for (Map<String, String> map : list) {
                    CommonUtil.valueView(map.get("userName") + e.getName());
                    IScene scene = Analysis2ShopSaleFunnelScene.builder().cycleType(e.getType()).saleId(map.get("userId")).build();
                    JSONArray jsonArray = crm.invokeApi(scene).getJSONObject("business").getJSONArray("list");
                    for (int i = 0; i < jsonArray.size(); i++) {
                        if (jsonArray.getJSONObject(i).getString("type").equals("RECEIVE")) {
                            int totalNum = jsonArray.getJSONObject(i).getInteger("value");
                            int sum = 0;
                            JSONArray detail = jsonArray.getJSONObject(i).getJSONArray("detail");
                            for (int j = 0; j < detail.size(); j++) {
                                if (detail.getJSONObject(j).getString("label").equals("FU")
                                        || detail.getJSONObject(j).getString("label").equals("PU")) {
                                    int value = detail.getJSONObject(j).getInteger("value");
                                    sum += value;

                                }
                            }
                            CommonUtil.valueView("总数：" + totalNum, "FU+PU和：" + sum);
                            Preconditions.checkArgument(totalNum == sum, map.get("userName") + e.getName() + "商机总数：" + totalNum + " FU+PU和：" + sum);
                            CommonUtil.logger(map.get("userName"));
                        }
                    }
                }
            }
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("店面数据分析--业务漏斗，【各时间段+各销售】商机=FU+PU");
        }
    }

    @Test(description = "店面数据分析--业务漏斗，【各时间段+各销售】试驾=FU+PU+BB")
    public void shopPanel_data_13() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            compareBusinessFunnelData("TEST_DRIVE", "business");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("店面数据分析--业务漏斗，【各时间段+各销售】试驾=FU+PU+BB");
        }
    }

    @Test(description = "店面数据分析--业务漏斗，【各时间段+各销售】订单FU+PU+BB")
    public void shopPanel_data_14() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            compareBusinessFunnelData("ORDER", "business");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("店面数据分析--业务漏斗，【各时间段+各销售】订单FU+PU+BB");
        }
    }

    /**
     * 漏斗数据比较
     *
     * @param type       类型
     * @param funnelType 漏斗类型
     */
    private void compareBusinessFunnelData(final String type, final String funnelType) {
        for (EnumFindType e : EnumFindType.values()) {
            List<Map<String, String>> list = method.getSaleList("销售顾问");
            for (Map<String, String> map : list) {
                CommonUtil.valueView(map.get("userName") + e.getName());
                IScene scene = Analysis2ShopSaleFunnelScene.builder().cycleType(e.getType()).saleId(map.get("userId")).build();
                JSONArray jsonArray = crm.invokeApi(scene).getJSONObject(funnelType).getJSONArray("list");
                for (int i = 0; i < jsonArray.size(); i++) {
                    if (jsonArray.getJSONObject(i).getString("type").equals(type)) {
                        int totalNum = jsonArray.getJSONObject(i).getInteger("value");
                        int sum = 0;
                        JSONArray detail = jsonArray.getJSONObject(i).getJSONArray("detail");
                        for (int j = 0; j < detail.size(); j++) {
                            int value = detail.getJSONObject(j).getInteger("value");
                            sum += value;
                        }
                        CommonUtil.valueView("总数：" + totalNum, "各项和：" + sum);
                        Preconditions.checkArgument(totalNum == sum, map.get("userName") + e.getName() + "查询结果总数：" + totalNum + " 后几项之和：" + sum);
                        CommonUtil.logger(map.get("userName"));
                    }
                }
            }
        }
    }

    @Test(description = "店面数据分析--业务漏斗，【各时间段各销售】留档率=商机FU/该时间段内每一天【销售总监-app-我的接待】今日新客接待之和", enabled = false)
    public void shopPanel_data_15() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            for (EnumFindType e : EnumFindType.values()) {
                List<Map<String, String>> list = method.getSaleList("销售顾问");
                for (Map<String, String> map : list) {
                    int clueValue = 0;
                    int receiveValue = 0;
                    CommonUtil.valueView(map.get("userName") + e.getName());
                    IScene scene = Analysis2ShopSaleFunnelScene.builder().cycleType(e.getType()).saleId(map.get("userId")).build();
                    JSONObject business = crm.invokeApi(scene).getJSONObject("business");
                    String enterPercentage = business.getString("enter_percentage");
                    JSONArray jsonArray = business.getJSONArray("list");
                    for (int i = 0; i < jsonArray.size(); i++) {
                        if (jsonArray.getJSONObject(i).getString("type").equals("RECEIVE")) {
                            JSONArray detail = jsonArray.getJSONObject(i).getJSONArray("detail");
                            for (int j = 0; j < detail.size(); j++) {
                                if (detail.getJSONObject(j).getString("label").equals("FU")) {
                                    receiveValue = detail.getJSONObject(j).getInteger("value");
                                }
                            }
                        }
                        if (jsonArray.getJSONObject(i).getString("type").equals("CLUE")) {
                            JSONArray detail = jsonArray.getJSONObject(i).getJSONArray("detail");
                            for (int j = 0; j < detail.size(); j++) {
                                if (detail.getJSONObject(j).getString("label").equals("接待线索")) {
                                    clueValue = detail.getJSONObject(j).getInteger("value");
                                }
                            }
                        }
                    }
                    CommonUtil.valueView("接待线索：" + clueValue, "FU：" + receiveValue);
                    String result = CommonUtil.getPercent(receiveValue, clueValue, 4);
                    CommonUtil.valueView("FU/接待线索：" + result, "留资率：" + enterPercentage);
                    Preconditions.checkArgument(result.equals(enterPercentage), map.get("userName") + e.getName() + " 留资率：" + enterPercentage + " FU/接待线索：" + result);
                    CommonUtil.logger(map.get("userName"));
                }
            }
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("店面数据分析--业务漏斗，【各时间段各销售】留档率=商机FU/该时间段内每一天【销售总监-app-我的接待】今日新客接待之和");
        }
    }

    @Test(description = "店面数据分析--业务漏斗，【各时间段+各销售】试驾率=（试驾的：FU+PU+BB）/（商机的：FU+PU）")
    public void shopPanel_data_16() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            compareFunnelPercent("TEST_DRIVE", "test_drive_percentage");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("店面数据分析--业务漏斗，【各时间段+各销售】试驾率=（试驾的：FU+PU+BB）/（商机的：FU+PU）");
        }
    }

    @Test(description = "店面数据分析--业务漏斗，【各时间段+各销售】成交率=（订单的：FU+PU+BB）/（商机的：FU+PU）")
    public void shopPanel_data_17() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            compareFunnelPercent("ORDER", "deal_percentage");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("店面数据分析--业务漏斗，【各时间段+各销售】成交率=（订单的：FU+PU+BB）/（商机的：FU+PU）");
        }
    }

    @Test(description = "店面数据分析--业务漏斗，【各时间段+各销售】交车率=交车/订单")
    public void shopPanel_data_18() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            for (EnumFindType e : EnumFindType.values()) {
                List<Map<String, String>> list = method.getSaleList("销售顾问");
                list.forEach(arr -> {
                    int orderNum = 0;
                    int dealNum = 0;
                    CommonUtil.valueView(arr.get("userName"));
                    JSONObject business = crm.saleFunnel(e.getType(), "", arr.get("userId")).getJSONObject("business");
                    String deliverPercentage = business.getString("deliver_percentage");
                    JSONArray businessList = business.getJSONArray("list");
                    for (int i = 0; i < businessList.size(); i++) {
                        if (businessList.getJSONObject(i).getString("type").equals("ORDER")) {
                            orderNum = businessList.getJSONObject(i).getInteger("value");
                        }
                        if (businessList.getJSONObject(i).getString("type").equals("DEAL")) {
                            dealNum = businessList.getJSONObject(i).getInteger("value");
                        }
                    }
                    String result = CommonUtil.getPercent(dealNum, orderNum, 4);
                    CommonUtil.valueView("交车/订单：" + result, "交车率：" + deliverPercentage);
                    Preconditions.checkArgument(deliverPercentage.equals(result), arr.get("userName") + e.getName() + "的交车/订单结果：" + result + " 交车率：" + deliverPercentage);
                    CommonUtil.logger(arr.get("userName"));
                });
            }
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("店面数据分析--业务漏斗，【各时间段+各销售】交车率=交车/订单");
        }
    }

    /**
     * 漏斗实际百分比计算比较
     *
     * @param type        计算项
     * @param percentType 百分比项
     */
    private void compareFunnelPercent(final String type, String percentType) {
        for (EnumFindType e : EnumFindType.values()) {
            List<Map<String, String>> list = method.getSaleList("销售顾问");
            list.forEach(arr -> {
                int aNum = 0;
                int bNum = 0;
                CommonUtil.valueView(arr.get("userName") + e.getName());
                JSONObject business = crm.saleFunnel(e.getType(), "", arr.get("userId")).getJSONObject("business");
                String percent = business.getString(percentType);
                JSONArray businessList = business.getJSONArray("list");
                for (int i = 0; i < businessList.size(); i++) {
                    if (businessList.getJSONObject(i).getString("type").equals(type)) {
                        aNum = businessList.getJSONObject(i).getInteger("value");
                    }
                    if (businessList.getJSONObject(i).getString("type").equals("RECEIVE")) {
                        JSONArray detailList = businessList.getJSONObject(i).getJSONArray("detail");
                        for (int j = 0; j < detailList.size(); j++) {
                            if (detailList.getJSONObject(j).getString("label").equals("FU")
                                    || detailList.getJSONObject(j).getString("label").equals("PU")) {
                                bNum += detailList.getJSONObject(j).getInteger("value");
                            }
                        }
                    }
                }
                String result = CommonUtil.getPercent(aNum, bNum, 4);
                CommonUtil.valueView("界面百分比：" + percent, "计算率：" + result);
                Preconditions.checkArgument(result.equals(percent), arr.get("userName") + e.getName() + type + "的FU+PU+BB加和除以" + "RECEIVE" + "的FU+PU加和：" + result + " 界面显示百分比：" + percent);
                CommonUtil.logger(arr.get("userName"));
            });
        }
    }

    @Test(description = "店面数据分析--业务漏斗,接待线索>=商机的FU")
    public void shopPanel_data_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            for (EnumFindType e : EnumFindType.values()) {
                List<Map<String, String>> list = method.getSaleList("销售顾问");
                for (Map<String, String> map : list) {
                    CommonUtil.valueView(map.get("userName") + e.getName());
                    class Inner {
                        int getValue(String type, String label) {
                            int value = 0;
                            IScene scene = Analysis2ShopSaleFunnelScene.builder().saleId(map.get("userId")).cycleType(e.getType()).build();
                            JSONArray array = crm.invokeApi(scene).getJSONObject("business").getJSONArray("list");
                            for (int i = 0; i < array.size(); i++) {
                                if (array.getJSONObject(i).getString("type").equals(type)) {
                                    JSONArray detailList = array.getJSONObject(i).getJSONArray("detail");
                                    for (int j = 0; j < detailList.size(); j++) {
                                        if (detailList.getJSONObject(j).getString("label").equals(label)) {
                                            value = detailList.getJSONObject(j).getInteger("value");
                                        }
                                    }
                                }
                            }
                            return value;
                        }
                    }
                    int receptionNum = new Inner().getValue("CLUE", "接待线索");
                    int fuNum = new Inner().getValue("RECEIVE", "FU");
                    CommonUtil.valueView(receptionNum, fuNum);
                    Preconditions.checkArgument(receptionNum >= fuNum, map.get("userName") + e.getName() + "接待线索：" + receptionNum + " 商机的FU：" + fuNum);
                    CommonUtil.logger(map.get("userName"));
                }
            }
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("店面数据分析--业务漏斗,接待线索>=商机的FU");
        }
    }

//    ----------------------------------------------------车系漏斗--------------------------------------------------------

    @Test(description = "店面数据分析--车系漏斗，【各时间段+各销售】线索=创建线索+接待线索")
    public void shopPanel_data_38() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            compareBusinessFunnelData("CLUE", "car_type");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("店面数据分析--车系漏斗，【各时间段+各销售】线索=创建线索+接待线索");
        }
    }

    @Test(description = "店面数据分析--车系漏斗，【各时间段+各销售】接待=各车型数量之和（意向车型）")
    public void shopPanel_data_42() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            compareBusinessFunnelData("RECEIVE", "car_type");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("店面数据分析--车系漏斗，【各时间段+各销售】接待=各车型数量之和（意向车型）");
        }
    }

    @Test(description = "店面数据分析--车系漏斗，【各时间段+各销售】试驾=各车型数量之和（审核通过&&没取消的试驾车型）")
    public void shopPanel_data_43() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            compareBusinessFunnelData("TEST_DRIVE", "car_type");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("店面数据分析--车系漏斗，【各时间段+各销售】试驾=各车型数量之和（审核通过&&没取消的试驾车型）");
        }
    }

    @Test(description = "店面数据分析--车系漏斗，【各时间段+各销售】订单=各车型数量之和")
    public void shopPanel_data_44() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            compareBusinessFunnelData("ORDER", "car_type");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("店面数据分析--车系漏斗，【各时间段+各销售】订单=各车型数量之和");
        }
    }

    @Test(description = "店面数据分析--车系漏斗，【各时间段+各销售】交车=各车型数量之和（购买车型）")
    public void shopPanel_data_45() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            compareBusinessFunnelData("DEAL", "car_type");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("店面数据分析--车系漏斗，【各时间段+各销售】交车=各车型数量之和（购买车型）");
        }
    }

//    ----------------------------------------------------接待时长--------------------------------------------------------

    @Test(description = "店面数据分析--【日】累计接待<=【周】累计接待")
    public void shopPanel_data_47() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            List<Map<String, String>> list = method.getSaleList("销售顾问");
            list.forEach(arr -> {
                CommonUtil.valueView(arr.get("userName"));
                getData(arr.get("userId"), EnumFindType.DAY, EnumFindType.WEEK, "service");
                Preconditions.checkArgument(s <= v, arr.get("userName") + "日接待数量为：" + s + "周接待数量为：" + v);
            });
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("店面数据分析--【日】累计接待<=【周】累计接待");
        }
    }

    @Test(description = "店面数据分析--【周】累计接待<=【月】累计接待")
    public void shopPanel_data_48() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            List<Map<String, String>> list = method.getSaleList("销售顾问");
            list.forEach(arr -> {
                CommonUtil.valueView(arr.get("userName"));
                getData(arr.get("userId"), EnumFindType.WEEK, EnumFindType.MONTH, "service");
                Preconditions.checkArgument(s <= v, arr.get("userName") + "周接待数量为：" + s + "月接待数量为：" + v);
            });
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("店面数据分析--【周】累计接待<=【月】累计接待");
        }
    }

    @Test(description = "店面数据分析--【月】累计接待<=【季】累计接待")
    public void shopPanel_data_49() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            List<Map<String, String>> list = method.getSaleList("销售顾问");
            list.forEach(arr -> {
                CommonUtil.valueView(arr.get("userName"));
                getData(arr.get("userId"), EnumFindType.MONTH, EnumFindType.QUARTER, "service");
                Preconditions.checkArgument(s <= v, arr.get("userName") + "月接待数量为：" + s + "季接待数量为：" + v);
            });
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("店面数据分析--【月】累计接待<=【季】累计接待");
        }
    }

    @Test(description = "店面数据分析--【季】累计接待<=【年】累计接待")
    public void shopPanel_data_50() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            List<Map<String, String>> list = method.getSaleList("销售顾问");
            list.forEach(arr -> {
                CommonUtil.valueView(arr.get("userName"));
                getData(arr.get("userId"), EnumFindType.QUARTER, EnumFindType.YEAR, "service");
                Preconditions.checkArgument(s <= v, arr.get("userName") + "季接待数量为：" + s + "年接待数量为：" + v);
            });
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("店面数据分析--【季】累计接待<=【年】累计接待");
        }
    }

    @Test(description = "店面数据分析--【日】累计试驾<=【周】累计试驾")
    public void shopPanel_data_51() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            List<Map<String, String>> list = method.getSaleList("销售顾问");
            list.forEach(arr -> {
                CommonUtil.valueView(arr.get("userName"));
                getData(arr.get("userId"), EnumFindType.DAY, EnumFindType.WEEK, "test_drive");
                Preconditions.checkArgument(s <= v, arr.get("userName") + "日试驾数量为：" + s + "周试驾数量为：" + v);
            });
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("店面数据分析--【日】累计试驾<=【周】累计试驾");
        }
    }

    @Test(description = "店面数据分析--【周】累计试驾<=【月】累计试驾")
    public void shopPanel_data_52() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            List<Map<String, String>> list = method.getSaleList("销售顾问");
            list.forEach(arr -> {
                CommonUtil.valueView(arr.get("userName"));
                getData(arr.get("userId"), EnumFindType.WEEK, EnumFindType.MONTH, "test_drive");
                Preconditions.checkArgument(s <= v, arr.get("userName") + "周试驾数量为：" + s + "月试驾数量为：" + v);
            });
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("店面数据分析--【周】累计试驾<=【月】累计试驾");
        }
    }

    @Test(description = "店面数据分析--【月】累计试驾<=【季】累计试驾")
    public void shopPanel_data_53() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            List<Map<String, String>> list = method.getSaleList("销售顾问");
            list.forEach(arr -> {
                CommonUtil.valueView(arr.get("userName"));
                getData(arr.get("userId"), EnumFindType.MONTH, EnumFindType.QUARTER, "test_drive");
                Preconditions.checkArgument(s <= v, arr.get("userName") + "月试驾数量为：" + s + "季试驾数量为：" + v);
            });
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("店面数据分析--【月】累计试驾<=【季】累计试驾");
        }
    }

    @Test(description = "店面数据分析--【季】累计试驾<=【年】累计试驾")
    public void shopPanel_data_54() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            List<Map<String, String>> list = method.getSaleList("销售顾问");
            list.forEach(arr -> {
                CommonUtil.valueView(arr.get("userName"));
                getData(arr.get("userId"), EnumFindType.QUARTER, EnumFindType.YEAR, "test_drive");
                Preconditions.checkArgument(s <= v, arr.get("userName") + "季试驾数量为：" + s + "年试驾数量为：" + v);
            });
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("店面数据分析--【季】累计试驾<=【年】累计试驾");
        }
    }

    @Test(description = "店面数据分析--【日】累计成交<=【周】累计成交")
    public void shopPanel_data_55() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            List<Map<String, String>> list = method.getSaleList("销售顾问");
            list.forEach(arr -> {
                CommonUtil.valueView(arr.get("userName"));
                getData(arr.get("userId"), EnumFindType.DAY, EnumFindType.WEEK, "deal");
                Preconditions.checkArgument(s <= v, arr.get("userName") + "日成交数量为：" + s + "周成交数量为：" + v);
            });
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("店面数据分析--【日】累计成交<=【周】累计成交");
        }
    }

    @Test(description = "店面数据分析--【周】累计成交<=【月】累计成交")
    public void shopPanel_data_56() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            List<Map<String, String>> list = method.getSaleList("销售顾问");
            list.forEach(arr -> {
                CommonUtil.valueView(arr.get("userName"));
                getData(arr.get("userId"), EnumFindType.WEEK, EnumFindType.MONTH, "deal");
                Preconditions.checkArgument(s <= v, arr.get("userName") + "周成交数量为：" + s + "月成交数量为：" + v);
            });
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("店面数据分析--【周】累计成交<=【月】累计成交");
        }
    }

    @Test(description = "店面数据分析--【月】累计成交<=【季】累计成交")
    public void shopPanel_data_57() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            List<Map<String, String>> list = method.getSaleList("销售顾问");
            list.forEach(arr -> {
                CommonUtil.valueView(arr.get("userName"));
                getData(arr.get("userId"), EnumFindType.MONTH, EnumFindType.QUARTER, "deal");
                Preconditions.checkArgument(s <= v, arr.get("userName") + "月成交数量为：" + s + "季成交数量为：" + v);
            });
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("店面数据分析--【月】累计成交<=【季】累计成交");
        }
    }

    @Test(description = "店面数据分析--【季】累计成交<=【年】累计成交")
    public void shopPanel_data_58() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            List<Map<String, String>> list = method.getSaleList("销售顾问");
            list.forEach(arr -> {
                CommonUtil.valueView(arr.get("userName"));
                getData(arr.get("userId"), EnumFindType.QUARTER, EnumFindType.YEAR, "deal");
                Preconditions.checkArgument(s <= v, arr.get("userName") + "季成交数量为：" + s + "年成交数量为：" + v);
            });
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("店面数据分析--【季】累计成交<=【年】累计成交");
        }
    }

    @Test(description = "店面数据分析--【日】累计交车<=【周】累计交车")
    public void shopPanel_data_59() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            List<Map<String, String>> list = method.getSaleList("销售顾问");
            list.forEach(arr -> {
                CommonUtil.valueView(arr.get("userName"));
                getData(arr.get("userId"), EnumFindType.DAY, EnumFindType.WEEK, "delivery");
                Preconditions.checkArgument(s <= v, arr.get("userName") + "日交车数量为：" + s + "周成交数量为：" + v);
            });
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("店面数据分析--【日】累计交车<=【周】累计交车");
        }
    }

    @Test(description = "店面数据分析--【周】累计交车<=【月】累计交车")
    public void shopPanel_data_60() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            List<Map<String, String>> list = method.getSaleList("销售顾问");
            list.forEach(arr -> {
                CommonUtil.valueView(arr.get("userName"));
                getData(arr.get("userId"), EnumFindType.WEEK, EnumFindType.MONTH, "delivery");
                Preconditions.checkArgument(s <= v, arr.get("userName") + "周交车数量为：" + s + "月成交数量为：" + v);
            });
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("店面数据分析--【周】累计交车<=【月】累计交车");
        }
    }

    @Test(description = "店面数据分析--【月】累计交车<=【季】累计交车")
    public void shopPanel_data_61() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            List<Map<String, String>> list = method.getSaleList("销售顾问");
            list.forEach(arr -> {
                CommonUtil.valueView(arr.get("userName"));
                getData(arr.get("userId"), EnumFindType.MONTH, EnumFindType.QUARTER, "delivery");
                Preconditions.checkArgument(s <= v, arr.get("userName") + "月交车数量为：" + s + "季成交数量为：" + v);
            });
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("店面数据分析--【月】累计交车<=【季】累计交车");
        }
    }

    @Test(description = "店面数据分析--【季】累计交车<=【年】累计交车")
    public void shopPanel_data_62() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            List<Map<String, String>> list = method.getSaleList("销售顾问");
            list.forEach(arr -> {
                CommonUtil.valueView(arr.get("userName"));
                getData(arr.get("userId"), EnumFindType.QUARTER, EnumFindType.YEAR, "delivery");
                Preconditions.checkArgument(s <= v, arr.get("userName") + "季交车数量为：" + s + "年成交数量为：" + v);
            });
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("店面数据分析--【季】累计交车<=【年】累计交车");
        }
    }

    private void getData(String userId, EnumFindType enumFindType1, EnumFindType enumFindType2, String type) {
        IScene scene = Analysis2ShopPanelScene.builder().cycleType(enumFindType1.getType()).saleId(userId).build();
        int s = crm.invokeApi(scene).getInteger(type);
        IScene scene1 = Analysis2ShopPanelScene.builder().cycleType(enumFindType2.getType()).saleId(userId).build();
        int v = crm.invokeApi(scene1).getInteger(type);
        this.s = s;
        this.v = v;
        CommonUtil.valueView(s, v);
        CommonUtil.log("分割线");
    }

//    ----------------------------------------------------车系漏斗--------------------------------------------------------

    @Test(description = "店面数据分析--车系漏斗--总经理线索>=各个销售之和")
    public void shopPanel_data_19() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            compareCarTypeFunnelData("CLUE");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("店面数据分析--车系漏斗--总经理线索>=各个销售之和");
        }
    }

    @Test(description = "店面数据分析--车系漏斗--总经理商机>=各个销售之和")
    public void shopPanel_data_20() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            compareCarTypeFunnelData("RECEIVE");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("店面数据分析--车系漏斗--总经理商机>=各个销售之和");
        }
    }

    @Test(description = "店面数据分析--车系漏斗--总经理试驾>=各个销售之和")
    public void shopPanel_data_21() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            compareCarTypeFunnelData("TEST_DRIVE");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("店面数据分析--车系漏斗--总经理试驾>=各个销售之和");
        }
    }

    @Test(description = "店面数据分析--车系漏斗--总经理订单>=各个销售之和")
    public void shopPanel_data_22() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            compareCarTypeFunnelData("ORDER");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("店面数据分析--车系漏斗--总经理订单>=各个销售之和");
        }
    }

    @Test(description = "店面数据分析--车系漏斗--总经理交车>=各个销售之和")
    public void shopPanel_data_23() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            compareCarTypeFunnelData("DEAL");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("店面数据分析--车系漏斗--总经理交车>=各个销售之和");
        }
    }

    /**
     * 销售顾问车系漏斗数据比较
     *
     * @param type 比较类型
     */
    private void compareCarTypeFunnelData(String type) {
        for (EnumFindType e : EnumFindType.values()) {
            int y = 0;
            JSONArray zjlList = crm.saleFunnel(e.getType(), "", "").getJSONObject("car_type").getJSONArray("list");
            for (int k = 0; k < zjlList.size(); k++) {
                if (zjlList.getJSONObject(k).getString("type").equals(type)) {
                    y = zjlList.getJSONObject(k).getInteger("value");
                }
            }
            int total = crm.userUserPage(1, 10).getInteger("total");
            int s = CommonUtil.getTurningPage(total, 100);
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

//    -----------------------------------------------两个漏斗之间数据比较---------------------------------------------------

    @Test(description = "店面数据分析--【业务漏斗】线索=【车型漏斗】线索")
    public void shopPanel_data_24() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            compareTwoFunnelData("CLUE");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("店面数据分析--【业务漏斗】线索=【车型漏斗】线索");
        }
    }

    @Test(description = "店面数据分析--【业务漏斗】交车==【车型漏斗】交车")
    public void shopPanel_data_25() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            compareTwoFunnelData("DEAL");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("店面数据分析--【业务漏斗】交车==【车型漏斗】交车");
        }
    }

    @Test(description = "店面數據分析--【业务漏斗】创建线索=【车型漏斗】创建线索,【业务漏斗】接待线索=【车型漏斗】接待线索")
    public void shopPanel_data_29() {
        logger.logCaseStart(caseResult.getCaseName());
        String type = "CLUE";
        try {
            for (EnumFindType e : EnumFindType.values()) {
                List<Map<String, String>> array = method.getSaleList("销售顾问");
                array.forEach(arr -> {
                    CommonUtil.valueView(arr.get("userName"));
                    JSONObject data = crm.shopSaleFunnel(e.getType(), "", arr.get("userId"));
                    JSONArray businessList = data.getJSONObject("business").getJSONArray("list");
                    JSONArray carTypeList = data.getJSONObject("car_type").getJSONArray("list");

                    class Inner {
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
                    int[] business = new Inner().getValue(businessList);
                    int[] carType = new Inner().getValue(carTypeList);
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
            saveData("店面數據分析--【业务漏斗】创建线索=【车型漏斗】创建线索,【业务漏斗】接待线索=【车型漏斗】接待线索");
        }
    }

    /**
     * 比较两个漏斗之间数据
     *
     * @param type 类型
     */
    private void compareTwoFunnelData(String type) {
        for (EnumFindType e : EnumFindType.values()) {
            List<Map<String, String>> array = method.getSaleList("销售顾问");
            array.forEach(arr -> {
                CommonUtil.valueView(arr.get("userName") + e.getName());
                JSONObject data = crm.shopSaleFunnel(e.getType(), "", arr.get("userId"));
                JSONArray businessList = data.getJSONObject("business").getJSONArray("list");
                JSONArray carTypeList = data.getJSONObject("car_type").getJSONArray("list");
                class Inner {
                    int getValue(JSONArray array) {
                        int value = 0;
                        for (int i = 0; i < array.size(); i++) {
                            if (array.getJSONObject(i).getString("type").equals(type)) {
                                value = array.getJSONObject(i).getInteger("value");
                            }
                        }
                        return value;
                    }
                }
                int businessValue = new Inner().getValue(businessList);
                int carTypeValue = new Inner().getValue(carTypeList);
                CommonUtil.valueView("业务漏斗：" + businessValue, "车系漏斗：" + carTypeValue);
                Preconditions.checkArgument(businessValue >= carTypeValue, arr.get("userName") +
                        " " + e.getName() + " 【业务漏斗】" + type + "数据为：" + businessValue + ",【车型漏斗】" + type + "数据为：" + carTypeValue);
                CommonUtil.logger(arr.get("userName"));
            });
        }
    }

    @Test(description = "店面数据分析--人工接待>=【业务漏斗】商机", enabled = false)
    public void shopPanel_data_26() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            for (EnumFindType e : EnumFindType.values()) {
                List<Map<String, String>> list = method.getSaleList("销售顾问");
                for (Map<String, String> map : list) {
                    CommonUtil.valueView(map.get("userName") + e.getName());
                    IScene scene = Analysis2ShopPanelScene.builder().saleId(map.get("userId")).cycleType(e.getType()).build();
                    int service = crm.invokeApi(scene).getInteger("service");
                    int funnelService = 0;
                    IScene scene1 = Analysis2ShopSaleFunnelScene.builder().saleId(map.get("userId")).cycleType(e.getType()).build();
                    JSONArray businessList = crm.invokeApi(scene1).getJSONObject("business").getJSONArray("list");
                    for (int i = 0; i < businessList.size(); i++) {
                        if (businessList.getJSONObject(i).getString("type").equals("RECEIVE")) {
                            funnelService = businessList.getJSONObject(i).getInteger("value");
                        }
                    }
                    CommonUtil.valueView("人共接待：" + service, "漏斗商机：" + funnelService);
                    Preconditions.checkArgument(service >= funnelService, "人共接待数：" + service + "漏斗接商机数：" + funnelService);
                    CommonUtil.logger(map.get("userName"));
                }
            }
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("店面数据分析--人工接待>=【业务漏斗】商机");
        }
    }

    @Test(description = "店面数据分析--人工接待=商机的FU+PU+BB", enabled = false)
    public void shopPanel_data_27() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            for (EnumFindType e : EnumFindType.values()) {
                List<Map<String, String>> list = method.getSaleList("销售顾问");
                for (Map<String, String> map : list) {
                    CommonUtil.valueView(map.get("userName") + e.getName());
                    IScene scene = Analysis2ShopPanelScene.builder().saleId(map.get("userId")).cycleType(e.getType()).build();
                    int service = crm.invokeApi(scene).getInteger("service");
                    int funnelService = 0;
                    IScene scene1 = Analysis2ShopSaleFunnelScene.builder().saleId(map.get("userId")).cycleType(e.getType()).build();
                    JSONArray businessList = crm.invokeApi(scene1).getJSONObject("business").getJSONArray("list");
                    for (int i = 0; i < businessList.size(); i++) {
                        if (businessList.getJSONObject(i).getString("type").equals("RECEIVE")) {
                            JSONArray detail = businessList.getJSONObject(i).getJSONArray("detail");
                            for (int j = 0; j < detail.size(); j++) {
                                int value = detail.getJSONObject(j).getInteger("value");
                                funnelService += value;
                            }
                        }
                    }
                    CommonUtil.valueView("人工接待：" + service, "漏斗商机：" + funnelService);
                    Preconditions.checkArgument(service >= funnelService, map.get("userName") + e.getName() + "人共接待数：" + service + "漏斗接商机数：" + funnelService);
                    CommonUtil.logger(map.get("userName"));
                }
            }
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("店面数据分析--人工接待=商机的FU+PU+BB");
        }
    }

//    -----------------------------------------------客户接待时长分析---------------------------------------------------

    @Test(description = "10分钟内组数=【前一日】【销售总监-PC-接待列表】离店时间-接待时间10分钟以内的数量")
    public void shopPanel_data_30() {
        logger.logCaseStart(caseResult.getCaseName());
        String date = DateTimeUtil.addDayFormat(new Date(), -1);
        try {
            List<Map<String, String>> array = method.getSaleList("销售顾问");
            array.forEach(arr -> {
                String userName = arr.get("userName");
                CommonUtil.valueView(userName);
                String sql;
                if (userName.contains("总经理")) {
                    sql = Sql.instance().select()
                            .from("t_porsche_reception_data")
                            .where("reception_date", "=", date)
                            .and("reception_duration", "<", 10)
                            .and("shop_id", "=", shopId)
                            .end().getSql();
                } else {
                    sql = Sql.instance().select()
                            .from("t_porsche_reception_data")
                            .where("reception_date", "=", date)
                            .and("reception_duration", "<", 10)
                            .and("reception_sale", "=", userName)
                            .and("shop_id", "=", shopId)
                            .end().getSql();
                }
                long count = new Factory.Builder().container(EnumContainer.ONE_PIECE.getContainer()).build().create(sql).size();
                String userId = arr.get("userId");
                int value = 0;
                JSONArray list = crm.receptTime("DAY", "", userId).getJSONArray("list");
                for (int i = 0; i < list.size(); i++) {
                    if (list.getJSONObject(i).getString("time").equals("10分钟以内")) {
                        value = list.getJSONObject(i).getInteger("value");
                    }
                }
                CommonUtil.valueView(count, value);
                Preconditions.checkArgument(count >= value, userName + "昨日接待时长10分钟以内的数量为：" + count + "店面数据分析中接待时长10分钟以内的数量为" + value);
                CommonUtil.logger(userName);
            });
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("10分钟内组数=【前一日】【销售总监-PC-接待列表】离店时间-接待时间<10分钟的数量");
        }
    }

    @Test(description = "10～30分钟组数=【前一日】【销售总监-PC-接待列表】10分钟<=离店时间-接待时间<30分钟的数量")
    public void shopPanel_data_31() {
        logger.logCaseStart(caseResult.getCaseName());
        String date = DateTimeUtil.addDayFormat(new Date(), -1);
        try {
            List<Map<String, String>> array = method.getSaleList("销售顾问");
            array.forEach(arr -> {
                String userName = arr.get("userName");
                CommonUtil.valueView(userName);
                String sql;
                if (userName.contains("总经理")) {
                    sql = Sql.instance().select()
                            .from("t_porsche_reception_data")
                            .where("reception_date", "=", date)
                            .and("reception_duration", "<", 30)
                            .and("reception_duration", ">=", 10)
                            .and("shop_id", "=", shopId)
                            .end().getSql();
                } else {
                    sql = Sql.instance().select()
                            .from("t_porsche_reception_data")
                            .where("reception_date", "=", date)
                            .and("reception_duration", "<", 30)
                            .and("reception_duration", ">=", 10)
                            .and("reception_sale", "=", userName)
                            .and("shop_id", "=", shopId)
                            .end().getSql();
                }
                long count = new Factory.Builder().container(EnumContainer.ONE_PIECE.getContainer()).build().create(sql).size();
                String userId = arr.get("userId");
                int value = 0;
                JSONArray list = crm.receptTime("DAY", "", userId).getJSONArray("list");
                for (int i = 0; i < list.size(); i++) {
                    if (list.getJSONObject(i).getString("time").equals("10-30分钟")) {
                        value = list.getJSONObject(i).getInteger("value");
                    }
                }
                CommonUtil.valueView(count, value);
                Preconditions.checkArgument(count >= value, userName + "昨日接待时长10～30分钟之内的数量为：" + count + "店面数据分析中接待时长10～30分钟的数量为" + value);
                CommonUtil.logger(userName);
            });
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("10～30分钟组数=【前一日】【销售总监-PC-接待列表】10分钟<=离店时间-接待时间<30分钟的数量");
        }
    }

    @Test(description = "30～60分钟内组数=【前一日】【销售总监-PC-接待列表】30分钟<=离店时间-接待时间<60分钟的数量")
    public void shopPanel_data_32() {
        logger.logCaseStart(caseResult.getCaseName());
        String date = DateTimeUtil.addDayFormat(new Date(), -1);
        try {
            List<Map<String, String>> array = method.getSaleList("销售顾问");
            array.forEach(arr -> {
                String userName = arr.get("userName");
                CommonUtil.valueView(userName);
                String sql;
                if (userName.contains("总经理")) {
                    sql = Sql.instance().select()
                            .from("t_porsche_reception_data")
                            .where("reception_date", "=", date)
                            .and("reception_duration", "<", 60)
                            .and("reception_duration", ">=", 30)
                            .and("shop_id", "=", shopId)
                            .end().getSql();
                } else {
                    sql = Sql.instance().select()
                            .from("t_porsche_reception_data")
                            .where("reception_date", "=", date)
                            .and("reception_duration", "<", 60)
                            .and("reception_duration", ">=", 30)
                            .and("reception_sale", "=", userName)
                            .and("shop_id", "=", shopId)
                            .end().getSql();
                }
                String userId = arr.get("userId");
                long count = new Factory.Builder().container(EnumContainer.ONE_PIECE.getContainer()).build().create(sql).size();
                int value = 0;
                JSONArray list = crm.receptTime("DAY", "", userId).getJSONArray("list");
                for (int i = 0; i < list.size(); i++) {
                    if (list.getJSONObject(i).getString("time").equals("30-60分钟")) {
                        value = list.getJSONObject(i).getInteger("value");
                    }
                }
                CommonUtil.valueView(count, value);
                Preconditions.checkArgument(count >= value, userName + "昨日接待时长30～60分钟之内的数量为：" + count + "该店面数据分析中接待时长30～60分钟的数量为" + value);
                CommonUtil.log(userName + "跑完");
            });
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("30～60分钟内组数=【前一日】【销售总监-PC-接待列表】30分钟<=离店时间-接待时间<60分钟的数量");
        }
    }

    @Test(description = "60～120分钟内组数=【前一日】【销售总监-PC-接待列表】60分钟<=离店时间-接待时间<120分钟的数量")
    public void shopPanel_data_33() {
        logger.logCaseStart(caseResult.getCaseName());
        String date = DateTimeUtil.addDayFormat(new Date(), -1);
        try {
            List<Map<String, String>> array = method.getSaleList("销售顾问");
            array.forEach(arr -> {
                String userName = arr.get("userName");
                CommonUtil.valueView(userName);
                String sql;
                if (userName.contains("总经理")) {
                    sql = Sql.instance().select()
                            .from("t_porsche_reception_data")
                            .where("reception_date", "=", date)
                            .and("reception_duration", "<", 120)
                            .and("reception_duration", ">=", 60)
                            .and("shop_id", "=", shopId)
                            .end().getSql();
                } else {
                    sql = Sql.instance().select()
                            .from("t_porsche_reception_data")
                            .where("reception_date", "=", date)
                            .and("reception_duration", "<", 120)
                            .and("reception_duration", ">=", 60)
                            .and("reception_sale", "=", userName)
                            .and("shop_id", "=", shopId)
                            .end().getSql();
                }
                String userId = arr.get("userId");
                long count = new Factory.Builder().container(EnumContainer.ONE_PIECE.getContainer()).build().create(sql).size();
                int value = 0;
                JSONArray list = crm.receptTime("DAY", "", userId).getJSONArray("list");
                for (int i = 0; i < list.size(); i++) {
                    if (list.getJSONObject(i).getString("time").equals("60-120分钟")) {
                        value = list.getJSONObject(i).getInteger("value");
                    }
                }
                CommonUtil.valueView(count, value);
                Preconditions.checkArgument(count >= value, userName + "昨日接待时长60～120分钟之内的数量为：" + count + "店面数据分析中接待时长60～120分钟的数量为" + value);
                CommonUtil.log(userName + "跑完");
            });
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("60～120分钟内组数=【前一日】【销售总监-PC-接待列表】60分钟<=离店时间-接待时间<120分钟的数量");
        }
    }

    @Test(description = "大于120分钟组数=【前一日】【销售总监-PC-接待列表】离店时间-接待时间>=120分钟的数量")
    public void shopPanel_data_34() {
        logger.logCaseStart(caseResult.getCaseName());
        String date = DateTimeUtil.addDayFormat(new Date(), -1);
        try {
            List<Map<String, String>> array = method.getSaleList("销售顾问");
            array.forEach(arr -> {
                String userName = arr.get("userName");
                CommonUtil.valueView(userName);
                String sql;
                if (userName.contains("总经理")) {
                    sql = Sql.instance().select()
                            .from("t_porsche_reception_data")
                            .where("reception_date", "=", date)
                            .and("reception_duration", ">=", 120)
                            .and("shop_id", "=", shopId)
                            .end().getSql();
                } else {
                    sql = Sql.instance().select()
                            .from("t_porsche_reception_data")
                            .where("reception_date", "=", date)
                            .and("reception_duration", ">=", 120)
                            .and("reception_sale", "=", userName)
                            .and("shop_id", "=", shopId)
                            .end().getSql();
                }
                String userId = arr.get("userId");
                long count = new Factory.Builder().container(EnumContainer.ONE_PIECE.getContainer()).build().create(sql).size();
                int value = 0;
                JSONArray list = crm.receptTime("DAY", "", userId).getJSONArray("list");
                for (int i = 0; i < list.size(); i++) {
                    if (list.getJSONObject(i).getString("time").equals("120分钟以上")) {
                        value = list.getJSONObject(i).getInteger("value");
                    }
                }
                CommonUtil.valueView(count, value);
                Preconditions.checkArgument(count >= value, userName + "昨日接待时长120分钟以上的数量为：" + count + "店面数据分析中接待时长120分钟以上的数量为" + value);
                CommonUtil.log(userName + "跑完");
            });
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("大于120分钟组数=【前一日】【销售总监-PC-接待列表】离店时间-接待时间>=120分钟的数量");
        }
    }

    @Test(description = "店面数据分析--智能接待=【进店批次分析页】所选时间段列表数")
    public void shopPanel_data_46() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            for (EnumFindType e : EnumFindType.values()) {
                IScene scene = Analysis2BatchListScene.builder().cycleType(e.getType()).build();
                int total = crm.invokeApi(scene).getInteger("total");
                IScene scene1 = Analysis2ShopPanelScene.builder().cycleType(e.getType()).build();
                int bath = crm.invokeApi(scene1).getInteger("batch");
                CommonUtil.valueView(total, bath);
                Preconditions.checkArgument(total == bath, e.getName() + "智能接待组数为：" + bath + "进店批次列表数为：" + total);
            }
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("店面数据分析--智能接待=【进店批次分析页】所选时间段列表数");
        }
    }

//    -----------------------------------------------存量客户分析---------------------------------------------------

    @Test(description = "存量客户分析页--【各时间段+各车型筛选】个人车主百分比+公司车主百分比=100% 或 0%")
    public void stockCustomer_data_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            for (EnumFindType a : EnumFindType.values()) {
                for (EnumCarStyle b : EnumCarStyle.values()) {
                    CommonUtil.valueView(a.getName(), b.getName());
                    double percentSum = 0;
                    IScene scene = Analysis2DealCarOwnerScene.builder().carType(b.getStyleId()).cycleType(a.getType()).build();
                    JSONArray ratioList = crm.invokeApi(scene).getJSONArray("ratio_list");
                    for (int i = 0; i < ratioList.size(); i++) {
                        double percent = ratioList.getJSONObject(i).getDouble("percent");
                        String name = ratioList.getJSONObject(i).getString("name");
                        CommonUtil.valueView(name + "：" + percent);
                        percentSum += percent;
                    }
                    CommonUtil.valueView(percentSum);
                    Preconditions.checkArgument(percentSum == 1 || percentSum == 0, b.getName() + a.getName() + "个人车主百分比+公司车主百分比=" + percentSum * 100 + "%");
                    CommonUtil.log("分割线");
                }
            }
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("存量客户分析页--【各时间段+各车型筛选】个人车主百分比+公司车主百分比=100% 或 0%");
        }
    }

    @Test(description = "存量客户分析页--【各时间段+各车型筛选】车主年龄分析 各年龄段之和=100%或 0%")
    public void stockCustomer_data_2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            for (EnumCarStyle b : EnumCarStyle.values()) {
                for (EnumFindType a : EnumFindType.values()) {
                    CommonUtil.valueView(b.getName(), a.getName());
                    double percentageSum = 0;
                    IScene scene = Analysis2DealGenderAgeScene.builder().carType(b.getStyleId()).cycleType(a.getType()).build();
                    JSONArray list = crm.invokeApi(scene).getJSONObject("age").getJSONArray("list");
                    for (int i = 0; i < list.size(); i++) {
                        double percentage = list.getJSONObject(i).getDouble("percentage");
                        String age = list.getJSONObject(i).getString("age");
                        CommonUtil.valueView(age + ":" + percentage);
                        percentageSum += percentage;
                    }
                    CommonUtil.valueView(percentageSum);
                    Preconditions.checkArgument((percentageSum >= 0.99 && percentageSum <= 1.01) || percentageSum == 0, b.getName() + a.getName() + "车主年龄分析 各年龄段之和=" + percentageSum * 100 + "%");
                    CommonUtil.log("分割线");
                }
            }
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("存量客户分析页--【各时间段+各车型筛选】车主年龄分析 各年龄段之和=100%或 0%");
        }
    }

    @Test(description = "存量客户分析--【各时间段+各车型筛选】车主性别分析 性别之和=100%或 0%")
    public void stockCustomer_data_3() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            for (EnumCarStyle b : EnumCarStyle.values()) {
                for (EnumFindType a : EnumFindType.values()) {
                    CommonUtil.valueView(b.getName(), a.getName());
                    double percentageSum = 0;
                    IScene scene = Analysis2DealGenderAgeScene.builder().carType(b.getStyleId()).cycleType(a.getType()).build();
                    JSONArray list = crm.invokeApi(scene).getJSONObject("gender").getJSONArray("list");
                    for (int i = 0; i < list.size(); i++) {
                        double percentage = list.getJSONObject(i).getDouble("percentage");
                        String gender = list.getJSONObject(i).getString("gender");
                        CommonUtil.valueView(gender + ":" + percentage);
                        percentageSum += percentage;
                    }
                    CommonUtil.valueView(percentageSum);
                    Preconditions.checkArgument(percentageSum == 1.0 || percentageSum == 0.0, b.getName() + a.getName() + "车主性别分析 性别之和=" + percentageSum * 100 + "%");
                    CommonUtil.log("分割线");
                }
            }
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("存量客户分析--【各时间段+各车型筛选】车主性别分析 性别之和=100%或 0%");
        }
    }

    @Test(description = "存量客户分析--【各时间段+各车型筛选】苏州各区成交量之和<=江苏成交量")
    public void stockCustomer_data_7() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            for (EnumFindType a : EnumFindType.values()) {
                for (EnumCarStyle b : EnumCarStyle.values()) {
                    CommonUtil.valueView(a.getName(), b.getName());
                    int cityValueNum = 0;
                    int provinceValue = 0;
                    IScene scene = Analysis2DealCityScene.builder().adCode(320500).carType(b.getStyleId()).cycleType(a.getType()).build();
                    JSONArray list = crm.invokeApi(scene).getJSONArray("list");
                    for (int i = 0; i < list.size(); i++) {
                        cityValueNum += list.getJSONObject(i).getInteger("value");
                    }
                    IScene scene1 = Analysis2DealWholeCountryScene.builder().cycleType(a.getType()).carType(b.getStyleId()).build();
                    JSONArray list1 = crm.invokeApi(scene1).getJSONArray("list");
                    for (int i = 0; i < list1.size(); i++) {
                        if (list1.getJSONObject(i).getString("province").equals("江苏省")) {
                            provinceValue = list1.getJSONObject(i).getInteger("value");
                        }
                    }
                    CommonUtil.valueView(cityValueNum, provinceValue);
                    Preconditions.checkArgument(cityValueNum <= provinceValue, "苏州各区成交量为：" + cityValueNum + "江苏省成交量为：" + provinceValue);
                    CommonUtil.log("分割线");
                }
            }
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("存量客户分析--【各时间段+各车型筛选】苏州各区成交量之和<=江苏成交量");
        }
    }

    @Test(description = "存量客户分析--【各时间段】全国各省成交量，某个省的百分比=该省成交量/各省成交量之和")
    public void stockCustomer_data_4() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            for (EnumCarStyle b : EnumCarStyle.values()) {
                for (EnumFindType a : EnumFindType.values()) {
                    CommonUtil.valueView(b.getName(), a.getName());
                    int totalValue = 0;
                    IScene scene = Analysis2DealWholeCountryScene.builder().carType(b.getStyleId()).cycleType(a.getType()).build();
                    JSONArray list = crm.invokeApi(scene).getJSONArray("list");
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
                        Preconditions.checkArgument(result == percentage, b.getName() + a.getName() + district + "接口返回占比：" + percentage + "，计算占比：" + result);
                    }
                    CommonUtil.log("分割线");
                }
            }
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("存量客户分析--【各时间段】全国各省成交量，某个省的百分比=该省成交量/各省成交量之和");
        }
    }

    @Test(description = "存量客户分析--【各时间段】全国各省成交量，各省百分比之和=100%")
    public void stockCustomer_data_8() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            for (EnumCarStyle b : EnumCarStyle.values()) {
                for (EnumFindType a : EnumFindType.values()) {
                    CommonUtil.valueView(b.getName(), a.getName());
                    double percentageNum = 0;
                    IScene scene = Analysis2DealWholeCountryScene.builder().carType(b.getStyleId()).cycleType(a.getType()).build();
                    JSONArray list = crm.invokeApi(scene).getJSONArray("list");
                    for (int i = 0; i < list.size(); i++) {
                        String percentage_str = list.getJSONObject(i).getString("percentage_str");
                        String result = percentage_str.substring(0, percentage_str.length() - 1);
                        CommonUtil.valueView(result);
                        percentageNum += Double.parseDouble(result);
                    }
                    CommonUtil.valueView(percentageNum);
                    Preconditions.checkArgument((percentageNum >= 99 && percentageNum <= 101) || percentageNum == 0, b.getName() + a.getName() + "全国各省占比百分比之和为：" + percentageNum + "%");
                    CommonUtil.log("分割线");
                }
            }
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("存量客户分析--【各时间段】全国各省成交量，各省百分比之和=100%");
        }
    }

    @Test(description = "存量客户分析--【各时间段筛选】苏州各区成交量，某个区的百分比=该区成交量/各区成交量之和")
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
                        Preconditions.checkArgument(result == percentage, b.getName() + a.getName() + district + "接口返回占比：" + percentage + "，计算占比：" + result);
                    }
                    CommonUtil.log("分割线");
                }
            }
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("存量客户分析--【各时间段筛选】苏州各区成交量，某个区的百分比=该区成交量/各区成交量之和");
        }
    }

    @Test(description = "存量客户分析--【各时间段筛选】苏州各区成交量，各区百分比之和=100%")
    public void stockCustomer_data_9() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            for (EnumFindType a : EnumFindType.values()) {
                for (EnumCarStyle b : EnumCarStyle.values()) {
                    CommonUtil.valueView(a.getName(), b.getName());
                    double percentageNum = 0;
                    JSONArray list = crm.city(a.getType(), "", b.getStyleId(), 320500).getJSONArray("list");
                    for (int i = 0; i < list.size(); i++) {
                        String percentageStr = list.getJSONObject(i).getString("percentage_str");
                        String percent = percentageStr.substring(0, percentageStr.length() - 1);
                        CommonUtil.valueView(percent);
                        percentageNum += Double.parseDouble(percent);
                    }
                    CommonUtil.valueView(percentageNum);
                    Preconditions.checkArgument((percentageNum >= 99 && percentageNum <= 101) || percentageNum == 0, "苏州各区百分比之和为：" + percentageNum + "%");
                    CommonUtil.log("分割线");
                }
            }
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("存量客户分析--【各时间段筛选】苏州各区成交量，各区百分比之和=100%");
        }
    }

    @Test(description = "存量客户分析--个人车主数量<=【app-销售总监-展厅客户-购车档案】客户类型为个人&交车日期在该时间段内的购车档案数量")
    public void stockCustomer_data_10() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String date = DateTimeUtil.addDayFormat(new Date(), -1);
            int pcCustomerNum = 0;
            IScene scene = Analysis2DealCarOwnerScene.builder().cycleType(EnumFindType.DAY.getType()).build();
            JSONArray ratioList = crm.invokeApi(scene).getJSONArray("ratio_list");
            for (int i = 0; i < ratioList.size(); i++) {
                if (ratioList.getJSONObject(i).getString("name").equals("个人车主")) {
                    pcCustomerNum = ratioList.getJSONObject(i).getInteger("value");
                }
            }
            String sql = Sql.instance().select()
                    .from("t_porsche_deliver_info")
                    .where("subject_type_name", "=", "个人")
                    .and("deliver_date", "=", date)
                    .and("shop_id", "=", shopId)
                    .end().getSql();
            int appCustomerNum = new Factory.Builder().container(EnumContainer.ONE_PIECE.getContainer()).build().create(sql).size();
            CommonUtil.valueView(pcCustomerNum, appCustomerNum);
            Preconditions.checkArgument(appCustomerNum >= pcCustomerNum, "昨日个人车主数为：" + pcCustomerNum + "昨日app个人客户交车数量为：" + appCustomerNum);
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("存量客户分析--个人车主数量<=【app-销售总监-展厅客户-购车档案】客户类型为个人&交车日期在该时间段内的购车档案数量");
        }
    }

    @Test(description = "存量客户分析--公司车主数量<=【app-销售总监-展厅客户-购车档案】客户类型为公司&交车日期在该时间段内的购车档案数量")
    public void stockCustomer_data_11() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String date = DateTimeUtil.addDayFormat(new Date(), -1);
            int pcCustomerNum = 0;
            IScene scene = Analysis2DealCarOwnerScene.builder().cycleType(EnumFindType.DAY.getType()).build();
            JSONArray ratioList = crm.invokeApi(scene).getJSONArray("ratio_list");
            for (int i = 0; i < ratioList.size(); i++) {
                if (ratioList.getJSONObject(i).getString("name").equals("公司车主")) {
                    pcCustomerNum = ratioList.getJSONObject(i).getInteger("value");
                }
            }
            String sql = Sql.instance().select()
                    .from("t_porsche_deliver_info")
                    .where("subject_type_name", "=", "公司")
                    .and("deliver_date", "=", date)
                    .and("shop_id", "=", shopId)
                    .end().getSql();
            List<Map<String, Object>> list = new Factory.Builder().container(EnumContainer.ONE_PIECE.getContainer()).build().create(sql);
            int appCustomerNum = list.size();
            CommonUtil.valueView(pcCustomerNum, appCustomerNum);
            Preconditions.checkArgument(appCustomerNum >= pcCustomerNum, "昨日公司车主数为：" + pcCustomerNum + "昨日app公司客户交车数量为：" + appCustomerNum);
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("存量客户分析--公司车主数量<=【app-销售总监-展厅客户-购车档案】客户类型为公司&交车日期在该时间段内的购车档案数量");
        }
    }

    @Test(description = "存量客户分析--【各时间段+车系筛选】个人车主数量<=【app-销售总监-展厅客户-购车档案】客户类型为个人&交车日期在该时间段内&购买车系为筛选车系的购车档案数量")
    public void stockCustomer_data_12() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String date = DateTimeUtil.addDayFormat(new Date(), -1);
            for (EnumCarStyle e : EnumCarStyle.values()) {
                int pcCustomerNum = 0;
                CommonUtil.valueView(e.getName());
                IScene scene = Analysis2DealCarOwnerScene.builder().cycleType(EnumFindType.DAY.getType()).carType(e.getStyleId()).build();
                JSONArray ratioList = crm.invokeApi(scene).getJSONArray("ratio_list");
                for (int i = 0; i < ratioList.size(); i++) {
                    if (ratioList.getJSONObject(i).getString("name").equals("个人车主")) {
                        pcCustomerNum = ratioList.getJSONObject(i).getInteger("value");
                    }
                }
                String sql;
                if (e.getStyleId() == null) {
                    sql = Sql.instance().select()
                            .from("t_porsche_deliver_info")
                            .where("subject_type_name", "=", "个人")
                            .and("deliver_date", "=", date)
                            .and("shop_id", "=", shopId)
                            .end().getSql();
                } else {
                    sql = Sql.instance().select()
                            .from("t_porsche_deliver_info")
                            .where("subject_type_name", "=", "个人")
                            .and("car_style", "=", e.getStyleId())
                            .and("deliver_date", "=", date)
                            .and("shop_id", "=", shopId)
                            .end().getSql();
                }
                int appCustomerNum = new Factory.Builder().container(EnumContainer.ONE_PIECE.getContainer()).build().create(sql).size();
                CommonUtil.valueView(pcCustomerNum, appCustomerNum);
                Preconditions.checkArgument(appCustomerNum >= pcCustomerNum, "昨日" + e.getName() + "个人车主数为：" + pcCustomerNum + "昨日app该车系个人客户交车数量为：" + appCustomerNum);
                CommonUtil.logger(e.getName());
            }
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("存量客户分析--【各时间段+车系筛选】个人车主数量<=【app-销售总监-展厅客户-购车档案】客户类型为个人&交车日期在该时间段内&购买车系为筛选车系的购车档案数量");
        }
    }

    @Test(description = "存量客户分析--【各时间段+车系筛选】公司车主数量<=【app-销售总监-展厅客户-购车档案】客户类型为公司&交车日期在该时间段内&购买车系为筛选车系的购车档案数量")
    public void stockCustomer_data_13() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            for (EnumCarStyle e : EnumCarStyle.values()) {
                int pcCustomerNum = 0;
                int appCustomerNum = 0;
                if (e.getStyleId() == null) {
                    continue;
                }
                CommonUtil.valueView(e.getName());
                IScene scene = Analysis2DealCarOwnerScene.builder().cycleType(EnumFindType.DAY.getType()).carType(e.getStyleId()).build();
                JSONArray ratioList = crm.invokeApi(scene).getJSONArray("ratio_list");
                for (int i = 0; i < ratioList.size(); i++) {
                    if (ratioList.getJSONObject(i).getString("name").equals("公司车主")) {
                        pcCustomerNum = ratioList.getJSONObject(i).getInteger("value");
                    }
                }
                String date = DateTimeUtil.addDayFormat(new Date(), -1);
                JSONArray list = crm.deliverCarAppList("", 1, 100, date, date).getJSONArray("list");
                for (int i = 0; i < list.size(); i++) {
                    if (list.getJSONObject(i).getString("car_style").equals(e.getStyleId())) {
                        int customerId = list.getJSONObject(i).getInteger("customer_id");
                        IScene scene1 = CustomerInfoScene.builder().customerId(String.valueOf(customerId)).build();
                        if (crm.invokeApi(scene1).getString("subject_type").equals("CORPORATION")) {
                            appCustomerNum++;
                        }
                    }
                }
                CommonUtil.valueView(pcCustomerNum, appCustomerNum);
                Preconditions.checkArgument(pcCustomerNum == appCustomerNum, "昨日" + e.getName() + "个人车主数为：" + pcCustomerNum + "昨日app该车系个人客户交车数量为：" + appCustomerNum);
                CommonUtil.log("分割线");
            }
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("存量客户分析--【各时间段+车系筛选】公司车主数量<=【app-销售总监-展厅客户-购车档案】客户类型为公司&交车日期在该时间段内&购买车系为筛选车系的购车档案数量");
        }
    }

    @Test(description = "存量客户分析--【各时间段+车系筛选】全国各省成交量=【app-销售总监-展厅客户-购车档案】交车日期在该时间段内&购买车系为筛选车系的购车档案数量")
    public void stockCustomer_data_14() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            for (EnumCarStyle e : EnumCarStyle.values()) {
                CommonUtil.valueView(e.getName());
                IScene scene = Analysis2DealWholeCountryScene.builder().carType(e.getStyleId()).cycleType(EnumFindType.DAY.getType()).build();
                JSONArray list = crm.invokeApi(scene).getJSONArray("list");
                for (int i = 0; i < list.size(); i++) {
                    String province = list.getJSONObject(i).getString("province");
                    int pcCustomerNum = list.getJSONObject(i).getInteger("value");
                    String date = DateTimeUtil.addDayFormat(new Date(), -1);
                    String sql;
                    if (e.getStyleId() == null) {
                        sql = Sql.instance().select()
                                .from("t_porsche_deliver_info")
                                .where("deliver_date", "=", date)
                                .and("address", "like", "%" + province + "%")
                                .and("shop_id", "=", shopId)
                                .end().getSql();
                    } else {
                        sql = Sql.instance().select()
                                .from("t_porsche_deliver_info")
                                .where("deliver_date", "=", date)
                                .and("address", "like", "%" + province + "%")
                                .and("car_style", "=", e.getStyleId())
                                .and("shop_id", "=", shopId)
                                .end().getSql();
                    }
                    int count = new Factory.Builder().container(EnumContainer.ONE_PIECE.getContainer()).build().create(sql).size();
                    CommonUtil.valueView(pcCustomerNum, count);
                    Preconditions.checkArgument(count >= pcCustomerNum, "昨日" + province + e.getName() + "交车数为：" + pcCustomerNum + "昨日app该省此车系交车数量为：" + count);
                    CommonUtil.logger(province);
                }
                CommonUtil.logger(e.getName());
            }
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("存量客户分析--【各时间段+车系筛选】全国各省成交量=【app-销售总监-展厅客户-购车档案】交车日期在该时间段内&购买车系为筛选车系的购车档案数量");
        }
    }

//    --------------------------------------------------订单客户分析-------------------------------------------------------

    @Test(description = "订单客户分析--【各时间段+各车型筛选】个人车主百分比+公司车主百分比=100% 或 0%")
    public void orderCustomer_data_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            for (EnumCarStyle e : EnumCarStyle.values()) {
                for (EnumFindType a : EnumFindType.values()) {
                    double num1 = 0;
                    double num2 = 0;
                    CommonUtil.valueView(e.getName() + a.getName());
                    IScene scene = Analysis2OrderCarOwnerScene.builder().carType(e.getStyleId()).cycleType(a.getType()).build();
                    JSONArray ratioList = crm.invokeApi(scene).getJSONArray("ratio_list");
                    for (int i = 0; i < ratioList.size(); i++) {
                        if (ratioList.getJSONObject(i).getString("name").equals("个人车主")) {
                            num1 = ratioList.getJSONObject(i).getDouble("percent");
                        }
                        if (ratioList.getJSONObject(i).getString("name").equals("公司车主")) {
                            num2 = ratioList.getJSONObject(i).getDouble("percent");
                        }
                    }
                    CommonUtil.valueView(num1, num2);
                    Preconditions.checkArgument((num1 + num2) == 1 || (num1 + num2) == 0, e.getName() + a.getName() + "个人车主百分比为：" + num1 + "公司车主百分比为：" + num2);
                    CommonUtil.logger(e.getName());
                }
            }
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("订单客户分析--【各时间段+各车型筛选】个人车主百分比+公司车主百分比=100% 或 0%");
        }
    }

    @Test(description = "订单客户分析--【各时间段+各车型筛选】个人车主百分比=个人车主数量/（个人+公司车主数量）")
    public void orderCustomer_data_2() {
        logger.logCaseStart(caseResult.getCaseName());
        String percentStr = null;
        try {
            for (EnumCarStyle e : EnumCarStyle.values()) {
                for (EnumFindType a : EnumFindType.values()) {
                    double num1 = 0;
                    double num2 = 0;
                    CommonUtil.valueView(e.getName() + a.getName());
                    IScene scene = Analysis2OrderCarOwnerScene.builder().carType(e.getStyleId()).cycleType(a.getType()).build();
                    JSONArray ratioList = crm.invokeApi(scene).getJSONArray("ratio_list");
                    for (int i = 0; i < ratioList.size(); i++) {
                        if (ratioList.getJSONObject(i).getString("name").equals("个人车主")) {
                            num1 = ratioList.getJSONObject(i).getDouble("value");
                            percentStr = ratioList.getJSONObject(i).getString("percent_str");
                        }
                        if (ratioList.getJSONObject(i).getString("name").equals("公司车主")) {
                            num2 = ratioList.getJSONObject(i).getDouble("value");
                        }
                    }
                    String result = CommonUtil.getPercent(num1, num2 + num1, 3);
                    CommonUtil.valueView(percentStr, result);
                    Preconditions.checkArgument(result.equals(percentStr), e.getName() + a.getName() + "个人车主计算百分比为：" + result + "界面展示为：" + percentStr);
                    CommonUtil.logger(e.getName());
                }
            }
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("订单客户分析--【各时间段+各车型筛选】个人车主百分比=个人车主数量/（个人+公司车主数量）");
        }
    }

    @Test(description = "订单客户分析--【各时间段+各车型筛选】公司车主百分比=公司车主数量/（个人+公司车主数量）")
    public void orderCustomer_data_3() {
        logger.logCaseStart(caseResult.getCaseName());
        String percentStr = null;
        try {
            for (EnumCarStyle e : EnumCarStyle.values()) {
                for (EnumFindType a : EnumFindType.values()) {
                    double num1 = 0;
                    double num2 = 0;
                    CommonUtil.valueView(e.getName() + a.getName());
                    IScene scene = Analysis2OrderCarOwnerScene.builder().carType(e.getStyleId()).cycleType(a.getType()).build();
                    JSONArray ratioList = crm.invokeApi(scene).getJSONArray("ratio_list");
                    for (int i = 0; i < ratioList.size(); i++) {
                        if (ratioList.getJSONObject(i).getString("name").equals("公司车主")) {
                            num1 = ratioList.getJSONObject(i).getDouble("value");
                            percentStr = ratioList.getJSONObject(i).getString("percent_str");
                        }
                        if (ratioList.getJSONObject(i).getString("name").equals("个人车主")) {
                            num2 = ratioList.getJSONObject(i).getDouble("value");
                        }
                    }
                    String result = CommonUtil.getPercent(num1, num2 + num1, 3);
                    CommonUtil.valueView(percentStr, result);
                    Preconditions.checkArgument(result.equals(percentStr), e.getName() + a.getName() + "公司车主计算百分比为：" + result + "界面展示为：" + percentStr);
                    CommonUtil.logger(e.getName());
                }
            }
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("订单客户分析--【各时间段+各车型筛选】公司车主百分比=公司车主数量/（个人+公司车主数量）");
        }
    }

    @Test(description = "订单客户分析--【各时间段+各车型筛选】车主年龄分析 各年龄段之和=100%或 0%")
    public void orderCustomer_data_4() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            for (EnumCarStyle e : EnumCarStyle.values()) {
                for (EnumFindType a : EnumFindType.values()) {
                    int num = 0;
                    CommonUtil.valueView(e.getName(), a.getName());
                    IScene scene = Analysis2OrderGenderAgeScene.builder().carType(e.getStyleId()).cycleType(a.getType()).build();
                    JSONArray list = crm.invokeApi(scene).getJSONObject("age").getJSONArray("list");
                    for (int i = 0; i < list.size(); i++) {
                        double x = list.getJSONObject(i).getDouble("percentage");
                        CommonUtil.valueView((int) x);
                        num += x;
                    }
                    CommonUtil.valueView(num);
                    Preconditions.checkArgument((num <= 1.01 && num >= 0.99) || num == 0, a.getName() + e.getName() + "各年龄段百分比之和为：" + num * 100 + "%");
                    CommonUtil.logger(e.getName());
                }
            }
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("订单客户分析--【各时间段+各车型筛选】车主年龄分析 各年龄段之和=100%或 0%");
        }
    }

    @Test(description = "订单客户分析--【各时间段+各车型筛选】车主性别分析 性别之和=100%或 0%")
    public void orderCustomer_data_5() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            for (EnumCarStyle e : EnumCarStyle.values()) {
                for (EnumFindType a : EnumFindType.values()) {
                    double num = 0;
                    CommonUtil.valueView(e.getName(), a.getName());
                    IScene scene = Analysis2OrderGenderAgeScene.builder().carType(e.getStyleId()).cycleType(a.getType()).build();
                    JSONArray list = crm.invokeApi(scene).getJSONObject("gender").getJSONArray("list");
                    for (int i = 0; i < list.size(); i++) {
                        double x = list.getJSONObject(i).getDouble("percentage");
                        CommonUtil.valueView(x);
                        num += x;
                    }
                    CommonUtil.valueView(num);
                    Preconditions.checkArgument(num == 1 || num == 0, e.getName() + a.getName() + "性别百分比之和为：" + num);
                    CommonUtil.logger(e.getName());
                }
            }
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("订单客户分析--【各时间段+各车型筛选】车主性别分析 性别之和=100%或 0%");
        }
    }

    @Test(description = "订单客户分析--【各时间段】全国各省成交量，某个省的百分比=该省成交量/各省成交量之和")
    public void orderCustomer_data_6() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            for (EnumCarStyle e : EnumCarStyle.values()) {
                for (EnumFindType a : EnumFindType.values()) {
                    CommonUtil.valueView(e.getName(), a.getName());
                    IScene scene = Analysis2OrderWholeCountryScene.builder().carType(e.getStyleId()).cycleType(a.getType()).build();
                    JSONArray list = crm.invokeApi(scene).getJSONArray("list");
                    int totalNum = 0;
                    for (int i = 0; i < list.size(); i++) {
                        totalNum += list.getJSONObject(i).getInteger("value");
                    }
                    for (int i = 0; i < list.size(); i++) {
                        String percentageStr = list.getJSONObject(i).getString("percentage_str");
                        String district = list.getJSONObject(i).getString("district");
                        int num = list.getJSONObject(i).getInteger("value");
                        double percentage = list.getJSONObject(i).getDouble("percentage");
                        CommonUtil.valueView(district + "数量：" + num + "，占比：" + percentage);
                        String result = CommonUtil.getPercent(num, totalNum, 4);
                        CommonUtil.valueView(district + "计算占比：" + result);
                        Preconditions.checkArgument(result.equals(percentageStr), e.getName() + a.getName() + district + "数量除以全国数量结果为：" + result + "界面展示结果为：" + percentageStr);
                    }
                    CommonUtil.logger(e.getName());
                }
            }
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("订单客户分析--【各时间段】全国各省成交量，某个省的百分比=该省成交量/各省成交量之和");
        }
    }

    @Test(description = "订单客户分析--【各时间段】全国各省成交量，各省百分比之和=100%")
    public void orderCustomer_data_7() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            for (EnumCarStyle e : EnumCarStyle.values()) {
                for (EnumFindType a : EnumFindType.values()) {
                    CommonUtil.valueView(e.getName(), a.getName());
                    IScene scene = Analysis2OrderWholeCountryScene.builder().carType(e.getStyleId()).cycleType(a.getType()).build();
                    JSONArray list = crm.invokeApi(scene).getJSONArray("list");
                    double percentageNum = 0;
                    for (int i = 0; i < list.size(); i++) {
                        String percentage = list.getJSONObject(i).getString("percentage_str");
                        String result = percentage.substring(0, percentage.length() - 1);
                        CommonUtil.valueView(result);
                        percentageNum += Double.parseDouble(result);
                    }
                    CommonUtil.valueView(percentageNum);
                    Preconditions.checkArgument((percentageNum <= 101 && percentageNum >= 99) || percentageNum == 0, e.getName() + a.getName() + "各省成交量占比之和为：" + percentageNum + "%");
                    CommonUtil.logger(e.getName());
                }
            }
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("订单客户分析--【各时间段】全国各省成交量，各省百分比之和=100%");
        }
    }

    @Test(description = "订单客户分析--【各时间段筛选】苏州各区成交量，某个区的百分比=该区成交量/各区成交量之和")
    public void orderCustomer_data_8() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            for (EnumCarStyle b : EnumCarStyle.values()) {
                for (EnumFindType a : EnumFindType.values()) {
                    int totalValue = 0;
                    CommonUtil.valueView(a.getName() + b.getName());
                    IScene scene = Analysis2OrderCityScene.builder().adCode(320500).carType(b.getStyleId()).cycleType(a.getType()).build();
                    JSONArray list = crm.invokeApi(scene).getJSONArray("list");
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
                        Preconditions.checkArgument(result == percentage, b.getName() + a.getName() + district + "接口返回占比：" + percentage + "，计算占比：" + result);
                    }
                    CommonUtil.logger(b.getName());
                }
            }
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("订单客户分析--【各时间段筛选】苏州各区成交量，某个区的百分比=该区成交量/各区成交量之和");
        }
    }

    @Test(description = "订单客户分析--【各时间段筛选】苏州各区成交量,各区百分比之和=100%")
    public void orderCustomer_data_9() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            for (EnumCarStyle b : EnumCarStyle.values()) {
                for (EnumFindType a : EnumFindType.values()) {
                    CommonUtil.valueView(a.getName(), b.getName());
                    double percentageNum = 0;
                    IScene scene = Analysis2OrderCityScene.builder().adCode(320500).carType(b.getStyleId()).cycleType(a.getType()).build();
                    JSONArray list = crm.invokeApi(scene).getJSONArray("list");
                    for (int i = 0; i < list.size(); i++) {
                        String percentageStr = list.getJSONObject(i).getString("percentage_str");
                        String result = percentageStr.substring(0, percentageStr.length() - 1);
                        CommonUtil.valueView(result);
                        percentageNum += Double.parseDouble(result);
                    }
                    CommonUtil.valueView(percentageNum);
                    Preconditions.checkArgument((percentageNum <= 101 && percentageNum >= 99) || percentageNum == 0, b.getName() + a.getName() + "苏州各区百分比之和为：" + percentageNum + "%");
                    CommonUtil.logger(b.getName());
                }
            }
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("订单客户分析--【各时间段筛选】苏州各区成交量,各区百分比之和=100%");
        }
    }

    @Test(description = "订单客户分析--苏州各区成交量之和<=江苏成交量")
    public void orderCustomer_data_10() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            for (EnumCarStyle b : EnumCarStyle.values()) {
                for (EnumFindType a : EnumFindType.values()) {
                    CommonUtil.valueView(a.getName() + b.getName());
                    int cityValueNum = 0;
                    int provinceValue = 0;
                    IScene scene = Analysis2OrderCityScene.builder().adCode(320500).carType(b.getStyleId()).cycleType(a.getType()).build();
                    JSONArray list = crm.invokeApi(scene).getJSONArray("list");
                    for (int i = 0; i < list.size(); i++) {
                        cityValueNum += list.getJSONObject(i).getInteger("value");
                    }
                    IScene scene1 = Analysis2OrderWholeCountryScene.builder().carType(b.getStyleId()).cycleType(a.getType()).build();
                    JSONArray list1 = crm.invokeApi(scene1).getJSONArray("list");
                    for (int i = 0; i < list1.size(); i++) {
                        if (list1.getJSONObject(i).getString("province").equals("江苏省")) {
                            provinceValue = list1.getJSONObject(i).getInteger("value");
                        }
                    }
                    CommonUtil.valueView(cityValueNum, provinceValue);
                    Preconditions.checkArgument(cityValueNum <= provinceValue, "苏州各区成交量为：" + cityValueNum + "江苏省成交量为：" + provinceValue);
                    CommonUtil.log(b.getName());
                }
            }
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("订单客户分析--苏州各区成交量之和<=江苏成交量");
        }
    }

    @Test(description = "订单客户分析--订单存量图，销售数量=PC角色管理中销售顾问数量", enabled = false)
    public void orderCustomer_data_11() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            int listSize = method.getSaleList("销售顾问").size() - 1;
            for (EnumCarStyle a : EnumCarStyle.values()) {
                for (EnumFindType b : EnumFindType.values()) {
                    CommonUtil.valueView(a.getName(), b.getName());
                    IScene scene = Analysis2OrderSaleListScene.builder().carType(a.getStyleId()).cycleType(b.getType()).build();
                    JSONArray list = crm.invokeApi(scene).getJSONArray("list");
                    CommonUtil.valueView(listSize, list.size());
                    Preconditions.checkArgument(listSize == list.size(), a.getName() + b.getName() + "角色管理中销售数量为：" + listSize + "订单存量图销售数量为：" + list.size());
                    CommonUtil.log("分割线");
                }
            }
        } catch (Exception | AssertionError e) {
            e.printStackTrace();
        }
    }

    @Test(description = "订单客户分析--订单存量图，柱状图1数据>= 柱状图2数据>=柱状图3数据..")
    public void orderCustomer_data_12() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            for (EnumCarStyle a : EnumCarStyle.values()) {
                for (EnumFindType b : EnumFindType.values()) {
                    CommonUtil.valueView(a.getName() + b.getName());
                    IScene scene = Analysis2OrderSaleListScene.builder().carType(a.getStyleId()).cycleType(b.getType()).build();
                    JSONArray list = crm.invokeApi(scene).getJSONArray("list");
                    for (int i = 0; i < list.size() - 1; i++) {
                        int value1 = list.getJSONObject(i).getInteger("value");
                        int value2 = list.getJSONObject(i + 1).getInteger("value");
                        CommonUtil.valueView(value1, value2);
                        Preconditions.checkArgument(value1 >= value2, a.getName() + b.getName() + "第" + i + "名销售的订单存量为：" + value1 + "，第" + (i + 1) + "名销售的订单存量为：" + value2);
                        CommonUtil.logger(a.getName());
                    }
                }
            }
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("订单客户分析--订单存量图，柱状图1数据>= 柱状图2数据>=柱状图3数据..");
        }
    }

    @Test(description = "订单客户分析--个人车主数量<=【app-销售总监-展厅客户-购车档案】客户类型为个人&购车日期在该时间段内&交车日期为空的购车档案数量")
    public void orderCustomer_data_13() {
        logger.logCaseStart(caseResult.getCaseName());
        String date = DateTimeUtil.addDayFormat(new Date(), -1);
        try {
            for (EnumCarStyle e : EnumCarStyle.values()) {
                CommonUtil.valueView(e.getName());
                IScene scene = Analysis2OrderCarOwnerScene.builder().carType(e.getStyleId()).cycleType(EnumFindType.DAY.getType()).build();
                JSONArray ratioList = crm.invokeApi(scene).getJSONArray("ratio_list");
                int value = 0;
                for (int i = 0; i < ratioList.size(); i++) {
                    if (ratioList.getJSONObject(i).getString("name").equals("个人车主")) {
                        value = ratioList.getJSONObject(i).getInteger("value");
                    }
                }
                String sql;
                if (e.getStyleId() == null) {
                    sql = Sql.instance().select()
                            .from("t_porsche_order_info")
                            .where("order_date", "=", date)
                            .and("subject_type_name", "=", "个人")
                            .and("shop_id", "=", shopId)
                            .end().getSql();
                } else {
                    sql = Sql.instance().select()
                            .from("t_porsche_order_info")
                            .where("order_date", "=", date)
                            .and("subject_type_name", "=", "个人")
                            .and("car_style", "=", e.getStyleId())
                            .and("shop_id", "=", shopId)
                            .end().getSql();
                }
                int count = new Factory.Builder().container(EnumContainer.ONE_PIECE.getContainer()).build().create(sql).size();
                CommonUtil.valueView(value, count);
                Preconditions.checkArgument(count >= value, e.getName() + EnumFindType.DAY.getName() + "pc端个人车主数量为：" + value + " app订车数量为：" + count);
                CommonUtil.logger(e.getName());
            }
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("订单客户分析--个人车主数量<=【app-销售总监-展厅客户-购车档案】客户类型为个人&购车日期在该时间段内&交车日期为空的购车档案数量");
        }
    }

    @Test(description = "订单客户分析--公司车主数量<=【app-销售总监-展厅客户-购车档案】客户类型为公司&购车日期在该时间段内&交车日期为空的购车档案数量")
    public void orderCustomer_data_14() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            for (EnumCarStyle e : EnumCarStyle.values()) {
                CommonUtil.valueView(e.getName());
                IScene scene = Analysis2OrderCarOwnerScene.builder().carType(e.getStyleId()).cycleType(EnumFindType.DAY.getType()).build();
                JSONArray ratioList = crm.invokeApi(scene).getJSONArray("ratio_list");
                String date = DateTimeUtil.addDayFormat(new Date(), -1);
                int value = 0;
                for (int i = 0; i < ratioList.size(); i++) {
                    if (ratioList.getJSONObject(i).getString("name").equals("公司车主")) {
                        value = ratioList.getJSONObject(i).getInteger("value");
                    }
                }
                String sql;
                if (e.getStyleId() == null) {
                    sql = Sql.instance().select()
                            .from("t_porsche_order_info")
                            .where("order_date", "=", date)
                            .and("subject_type_name", "=", "公司")
                            .and("shop_id", "=", shopId)
                            .end().getSql();
                } else {
                    sql = Sql.instance().select()
                            .from("t_porsche_order_info")
                            .where("order_date", "=", date)
                            .and("subject_type_name", "=", "公司")
                            .and("car_style", "=", e.getStyleId())
                            .and("shop_id", "=", shopId)
                            .end().getSql();
                }
                int count = new Factory.Builder().container(EnumContainer.ONE_PIECE.getContainer()).build().create(sql).size();
                CommonUtil.valueView(value, count);
                Preconditions.checkArgument(count >= value, e.getName() + EnumFindType.DAY.getName() + "pc端个公司主数量为：" + value + " app接待数量为：" + count);
                CommonUtil.logger(e.getName());
            }
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("订单客户分析--公司车主数量<=【app-销售总监-展厅客户-购车档案】客户类型为公司&购车日期在该时间段内&交车日期为空的购车档案数量");
        }
    }

    @Test(description = "订单客户分析--全国各省成交量=【app-销售总监-展厅客户-购车档案】购车日期在该时间段内&交车日期为空的购车档案数量")
    public void orderCustomer_data_15() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            for (EnumCarStyle e : EnumCarStyle.values()) {
                CommonUtil.valueView(e.getName());
                IScene scene = Analysis2OrderWholeCountryScene.builder().carType(e.getStyleId()).cycleType(EnumFindType.DAY.getType()).build();
                JSONArray list = crm.invokeApi(scene).getJSONArray("list");
                for (int i = 0; i < list.size(); i++) {
                    String province = list.getJSONObject(i).getString("province");
                    int pcCustomerNum = list.getJSONObject(i).getInteger("value");
                    String date = DateTimeUtil.addDayFormat(new Date(), -1);
                    String sql;
                    if (e.getStyleId() == null) {
                        sql = Sql.instance().select()
                                .from("t_porsche_order_info")
                                .where("order_date", "=", date)
                                .and("address", "like", "%" + province + "%")
                                .and("shop_id", "=", shopId)
                                .end().getSql();
                    } else {
                        sql = Sql.instance().select()
                                .from("t_porsche_order_info")
                                .where("order_date", "=", date)
                                .and("address", "like", "%" + province + "%")
                                .and("car_style", "=", e.getStyleId())
                                .and("shop_id", "=", shopId)
                                .end().getSql();
                    }
                    int count = new Factory.Builder().container(EnumContainer.ONE_PIECE.getContainer()).build().create(sql).size();
                    CommonUtil.valueView(pcCustomerNum, count);
                    Preconditions.checkArgument(count >= pcCustomerNum, "昨日" + province + e.getName() + "交车数为：" + pcCustomerNum + " 昨日app该省此车系交车数量为：" + count);
                    CommonUtil.logger(province);
                }
                CommonUtil.logger(e.getName());
            }
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("存量客户分析--【各时间段+车系筛选】全国各省成交量=【app-销售总监-展厅客户-购车档案】交车日期在该时间段内&购买车系为筛选车系的购车档案数量");
        }
    }

    @Test(description = "个人车主数量=【前一日】客户名称小于等于5个字的客户订车数量", enabled = false)
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
            int s = CommonUtil.getTurningPage(total, 100);
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
            Preconditions.checkArgument(company == receptionCompany, "昨日公司车主客户数为：" + company + "," + "接待订车公司客户数量：" + receptionCompany);
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
            int s = CommonUtil.getTurningPage(total, 100);
            for (int i = 1; i < s; i++) {
                JSONArray appList = crm.testDriverAppList("", date, date, 100, i).getJSONArray("list");
                for (int j = 0; j < appList.size(); j++) {
                    if (appList.getJSONObject(j).getString("audit_status_name").equals("已通过")) {
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
