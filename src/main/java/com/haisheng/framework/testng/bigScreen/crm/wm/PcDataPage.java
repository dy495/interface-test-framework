package com.haisheng.framework.testng.bigScreen.crm.wm;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.openservices.shade.com.alibaba.fastjson.JSON;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.crm.CrmScenarioUtil;
import com.haisheng.framework.testng.bigScreen.crm.commonDs.PublicMethod;
import com.haisheng.framework.testng.bigScreen.crm.wm.bean.*;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.*;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.customer.EnumCarStyle;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.customer.EnumChannelName;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.other.EnumFindType;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.sale.EnumAccount;
import com.haisheng.framework.testng.bigScreen.crm.wm.scene.IScene;
import com.haisheng.framework.testng.bigScreen.crm.wm.scene.app.CustomerInfoScene;
import com.haisheng.framework.testng.bigScreen.crm.wm.scene.app.CustomerPageScene;
import com.haisheng.framework.testng.bigScreen.crm.wm.scene.pc.*;
import com.haisheng.framework.testng.bigScreen.crm.wm.util.UserUtil;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.util.CommonUtil;
import com.haisheng.framework.util.DateTimeUtil;
import factory.Factory;
import org.springframework.util.StringUtils;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import sql.Sql;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 门店数据中心测试用例
 */
public class PcDataPage extends TestCaseCommon implements TestCaseStd {
    public static final Factory ONE_PIECE_FACTORY = new Factory.Builder().container(EnumContainer.ONE_PIECE.getContainer()).build();
    PublicMethod method = new PublicMethod();
    CrmScenarioUtil crm = CrmScenarioUtil.getInstance();
    private static final EnumAccount zjl = EnumAccount.ZJL_DAILY;
    private static final String shopId = EnumTestProduce.CRM_DAILY.getShopId();
    private static final int size = 100;
    private int s;
    private int v;

    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before class initial");
        CommonConfig commonConfig = new CommonConfig();
        //替换checklist的相关信息
        commonConfig.checklistAppId = EnumChecklistAppId.DB_APP_ID_SCREEN_SERVICE.getId();
        commonConfig.checklistConfId = EnumChecklistConfId.DB_SERVICE_ID_CRM_DAILY_SERVICE.getId();
        commonConfig.checklistQaOwner = EnumChecklistUser.WM.getName();
        commonConfig.produce = EnumProduce.BSJ.name();
        commonConfig.referer = EnumTestProduce.CRM_DAILY.getReferer();
        //替换jenkins-job的相关信息
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.CRM_DAILY_TEST.getJobName());
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, EnumTestProduce.CRM_DAILY.getName() + commonConfig.checklistQaOwner);
        //替换钉钉推送
        commonConfig.dingHook = EnumDingTalkWebHook.CAR_OPEN_MANAGEMENT_PLATFORM_GRP.getWebHook();
        //放入shopId
        commonConfig.shopId = EnumTestProduce.CRM_DAILY.getShopId();
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
            collectMessage(e);
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
            collectMessage(e);
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
            appendFailReason(e.toString());
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
            collectMessage(e);
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
        List<Map<String, String>> list = method.getSaleListByRoleName("销售顾问");
        Arrays.stream(EnumFindType.values()).forEach(e -> {
            int gwSum = list.stream().filter(map -> !map.get("userName").contains("总经理")).map(map -> getTypeValue(e, map, type)).collect(Collectors.toList()).stream().mapToInt(a -> a).sum();
            int zjlNum = list.stream().filter(map -> map.get("userName").contains("总经理")).map(map -> getTypeValue(e, map, type)).collect(Collectors.toList()).get(0);
            CommonUtil.valueView("各顾问之和：" + gwSum, "总经理：" + zjlNum);
            Preconditions.checkArgument(zjlNum >= gwSum, e.getName() + "【不选销售顾问】累计" + str + "：" + zjlNum + " 各个销售顾问累计" + str + "：" + gwSum);
            CommonUtil.logger(e.getName());
        });
    }

    private int getTypeValue(EnumFindType findType, Map<String, String> map, String type) {
        IScene scene = Analysis2ShopPanelScene.builder().cycleType(findType.getType()).saleId(map.get("userId")).build();
        return crm.invokeApi(scene).getInteger(type);
    }

//    ----------------------------------------------------接待时长--------------------------------------------------------

    @Test(description = "店面数据分析--客户接待时长分析，【各时间段】相同时间段内：【不选销售顾问】10分钟内组数=各个销售顾问10分钟内组数之和")
    public void shopPanel_data_6() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            compareReceptionTime("10分钟以内");
        } catch (Exception | AssertionError e) {
            collectMessage(e);
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
            collectMessage(e);
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
            collectMessage(e);
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
            collectMessage(e);
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
            collectMessage(e);
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
        List<Map<String, String>> list = method.getSaleListByRoleName("销售顾问");
        Arrays.stream(EnumFindType.values()).forEach(e -> {
            int gwSum = list.stream().filter(map -> !map.get("userName").contains("总经理")).map(map -> getTimeValue(e, map, time)).collect(Collectors.toList()).stream().mapToInt(map -> map).sum();
            int zjlNum = list.stream().filter(map -> map.get("userName").contains("总经理")).map(map -> getTimeValue(e, map, time)).collect(Collectors.toList()).get(0);
            CommonUtil.valueView("售顾问之和：" + gwSum, "总经理：" + zjlNum);
            Preconditions.checkArgument(zjlNum >= gwSum, e.getName() + "【不选销售顾问】" + time + "组数：" + zjlNum + " 各个销售顾问" + time + "组数之和：" + gwSum);
        });
    }

    private int getTimeValue(EnumFindType findType, Map<String, String> map, String time) {
        IScene scene = Analysis2ShopReceptTimeScene.builder().cycleType(findType.getType()).saleId(map.get("userId")).build();
        JSONArray jsonArray = crm.invokeApi(scene).getJSONArray("list");
        return jsonArray.stream().map(e -> (JSONObject) e).filter(object -> object.getString("time").equals(time))
                .map(object -> object.getInteger("value")).collect(Collectors.toList()).get(0);
    }

//    ----------------------------------------------------业务漏斗--------------------------------------------------------

    @Test(description = "店面数据分析--业务漏斗，【各时间段+各销售】线索=创建线索+接待线索")
    public void shopPanel_data_11() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            compareBusinessFunnelData("business", "CLUE");
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("店面数据分析--业务漏斗，【各时间段+各销售】线索=创建线索+接待线索");
        }
    }

    @Test(description = "店面数据分析--业务漏斗，【各时间段+各销售】商机=FU+PU")
    public void shopPanel_data_12() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            List<Map<String, String>> list = method.getSaleListByRoleName("销售顾问");
            Arrays.stream(EnumFindType.values()).forEach(e -> list.forEach(map -> {
                CommonUtil.valueView(map.get("userName") + e.getName());
                IScene scene = Analysis2ShopSaleFunnelScene.builder().cycleType(e.getType()).saleId(map.get("userId")).build();
                JSONArray jsonArray = crm.invokeApi(scene).getJSONObject("business").getJSONArray("list");
                List<JSONObject> object = jsonArray.stream().map(o -> (JSONObject) o).filter(o -> o.getString("type").equals("RECEIVE")).collect(Collectors.toList());
                List<List<Integer>> resultLists = object.stream().map(this::getResultList).collect(Collectors.toList());
                resultLists.forEach(resultList -> {
                    Preconditions.checkArgument(resultList.get(0).equals(resultList.get(1)), map.get("userName") + e.getName() + "商机总数：" + resultList.get(0) + " FU+PU和：" + resultList.get(1));
                    CommonUtil.logger(map.get("userName"));
                });
            }));
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("店面数据分析--业务漏斗，【各时间段+各销售】商机=FU+PU");
        }
    }

    private List<Integer> getResultList(JSONObject object) {
        List<Integer> list = new ArrayList<>();
        int totalNum = object.getInteger("value");
        JSONArray detail = object.getJSONArray("detail");
        int sum = detail.stream().map(o -> (JSONObject) o).filter(o -> o.getString("label").equals("FU") || o.getString("label").equals("PU"))
                .map(o -> o.getInteger("value")).collect(Collectors.toList()).stream().mapToInt(o -> o).sum();
        CommonUtil.valueView("总数：" + totalNum, "FU+PU和：" + sum);
        list.add(totalNum);
        list.add(sum);
        return list;
    }

    @Test(description = "店面数据分析--业务漏斗，【各时间段+各销售】试驾=FU+PU+BB")
    public void shopPanel_data_13() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            compareBusinessFunnelData("business", "TEST_DRIVE");
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            saveData("店面数据分析--业务漏斗，【各时间段+各销售】试驾=FU+PU+BB");
        }
    }

    @Test(description = "店面数据分析--业务漏斗，【各时间段+各销售】订单FU+PU+BB")
    public void shopPanel_data_14() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            compareBusinessFunnelData("business", "ORDER");
        } catch (Exception | AssertionError e) {
            collectMessage(e);
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
    private void compareBusinessFunnelData(final String funnelType, final String type) {
        List<Map<String, String>> list = method.getSaleListByRoleName("销售顾问");
        Arrays.stream(EnumFindType.values()).forEach(e -> list.forEach(map -> {
            CommonUtil.valueView(map.get("userName") + e.getName());
            IScene scene = Analysis2ShopSaleFunnelScene.builder().cycleType(e.getType()).saleId(map.get("userId")).build();
            JSONArray jsonArray = crm.invokeApi(scene).getJSONObject(funnelType).getJSONArray("list");
            for (int i = 0; i < jsonArray.size(); i++) {
                if (jsonArray.getJSONObject(i).getString("type").equals(type)) {
                    int totalNum = jsonArray.getJSONObject(i).getInteger("value");
                    JSONArray detail = jsonArray.getJSONObject(i).getJSONArray("detail");
                    int sum = detail.stream().map(o -> (JSONObject) o).map(o -> o.getInteger("value")).collect(Collectors.toList()).stream().mapToInt(o -> o).sum();
                    CommonUtil.valueView("总数：" + totalNum, "各项和：" + sum);
                    Preconditions.checkArgument(totalNum == sum, map.get("userName") + e.getName() + "查询结果总数：" + totalNum + " 后几项之和：" + sum);
                    CommonUtil.logger(map.get("userName"));
                }
            }
        }));
    }

    @Test(description = "店面数据分析--业务漏斗，【各时间段各销售】留档率=商机FU/该时间段内每一天【销售总监-app-我的接待】今日新客接待之和", enabled = false)
    public void shopPanel_data_15() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            List<Map<String, String>> list = method.getSaleListByRoleName("销售顾问");
            for (EnumFindType e : EnumFindType.values()) {
                for (Map<String, String> map : list) {
                    CommonUtil.valueView(map.get("userName") + e.getName());
                    IScene scene = Analysis2ShopSaleFunnelScene.builder().cycleType(e.getType()).saleId(map.get("userId")).build();
                    JSONObject business = crm.invokeApi(scene).getJSONObject("business");
                    String enterPercentage = business.getString("enter_percentage");
                    JSONArray jsonArray = business.getJSONArray("list");
                    int receiveValue = jsonArray.stream().map(o -> (JSONObject) o).filter(o -> o.getString("type").equals("RECEIVE"))
                            .map(o -> o.getJSONArray("detail")).collect(Collectors.toList()).stream().map(detail -> detail.stream()
                                    .map(object -> (JSONObject) object).filter(object -> object.getString("label").equals("FU"))
                                    .map(object -> object.getInteger("value")).collect(Collectors.toList()).get(0)).collect(Collectors.toList()).get(0);
                    int clueValue = jsonArray.stream().map(o -> (JSONObject) o).filter(o -> o.getString("type").equals("CLUE"))
                            .map(o -> o.getJSONArray("detail")).collect(Collectors.toList()).stream().map(detail -> detail.stream()
                                    .map(object -> (JSONObject) object).filter(object -> object.getString("label").equals("接待线索"))
                                    .map(object -> object.getInteger("value")).collect(Collectors.toList()).get(0)).collect(Collectors.toList()).get(0);
                    CommonUtil.valueView("接待线索：" + clueValue, "FU：" + receiveValue);
                    String result = CommonUtil.getPercent(receiveValue, clueValue);
                    CommonUtil.valueView("FU/接待线索：" + result, "留资率：" + enterPercentage);
                    Preconditions.checkArgument(result.equals(enterPercentage), map.get("userName") + e.getName() + " 留资率：" + enterPercentage + " FU/接待线索：" + result);
                    CommonUtil.logger(map.get("userName"));
                }
            }
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("店面数据分析--业务漏斗，【各时间段各销售】留档率=商机FU/该时间段内每一天【销售总监-app-我的接待】今日新客接待之和");
        }
    }

    @Test(description = "店面数据分析--业务漏斗，【各时间段+各销售】试驾率=（试驾的：FU+PU+BB）/（商机的：FU+PU）")
    public void shopPanel_data_16() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            compareFunnelPercent("test_drive_percentage", "TEST_DRIVE");
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("店面数据分析--业务漏斗，【各时间段+各销售】试驾率=（试驾的：FU+PU+BB）/（商机的：FU+PU）");
        }
    }

    @Test(description = "店面数据分析--业务漏斗，【各时间段+各销售】成交率=（订单的：FU+PU+BB）/（商机的：FU+PU）")
    public void shopPanel_data_17() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            compareFunnelPercent("deal_percentage", "ORDER");
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("店面数据分析--业务漏斗，【各时间段+各销售】成交率=（订单的：FU+PU+BB）/（商机的：FU+PU）");
        }
    }

    @Test(description = "店面数据分析--业务漏斗，【各时间段+各销售】交车率=交车/订单")
    public void shopPanel_data_18() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            List<Map<String, String>> list = method.getSaleListByRoleName("销售顾问");
            Arrays.stream(EnumFindType.values()).forEach(e -> list.forEach(arr -> {
                CommonUtil.valueView(arr.get("userName"));
                JSONObject business = crm.saleFunnel(e.getType(), "", arr.get("userId")).getJSONObject("business");
                String deliverPercentage = business.getString("deliver_percentage");
                JSONArray businessList = business.getJSONArray("list");
                int orderNum = businessList.stream().map(o -> (JSONObject) o).filter(o -> o.getString("type").equals("ORDER")).map(o -> o.getInteger("value")).collect(Collectors.toList()).get(0);
                int dealNum = businessList.stream().map(o -> (JSONObject) o).filter(o -> o.getString("type").equals("DEAL")).map(o -> o.getInteger("value")).collect(Collectors.toList()).get(0);
                String result = CommonUtil.getPercent(dealNum, orderNum, 4);
                CommonUtil.valueView("交车/订单：" + result, "交车率：" + deliverPercentage);
                Preconditions.checkArgument(deliverPercentage.equals(result), arr.get("userName") + e.getName() + "的交车/订单结果：" + result + " 交车率：" + deliverPercentage);
                CommonUtil.logger(arr.get("userName"));
            }));
        } catch (Exception | AssertionError e) {
            collectMessage(e);
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
    private void compareFunnelPercent(String percentType, final String type) {
        List<Map<String, String>> list = method.getSaleListByRoleName("销售顾问");
        Arrays.stream(EnumFindType.values()).forEach(e -> list.forEach(arr -> {
            CommonUtil.valueView(arr.get("userName") + e.getName());
            JSONObject business = crm.saleFunnel(e.getType(), "", arr.get("userId")).getJSONObject("business");
            String percent = business.getString(percentType);
            JSONArray businessList = business.getJSONArray("list");
            int aNum = businessList.stream().map(o -> (JSONObject) o).filter(o -> o.getString("type").equals(type)).map(o -> o.getInteger("value")).collect(Collectors.toList()).get(0);
            List<JSONArray> details = businessList.stream().map(o -> (JSONObject) o).filter(o -> o.getString("type").equals("RECEIVE")).map(o -> o.getJSONArray("detail")).collect(Collectors.toList());
            int bNum = details.stream().mapToInt(detail -> detail.stream().map(object -> (JSONObject) object).filter(object -> object.getString("label").equals("FU") || object.getString("label").equals("PU"))
                    .map(object -> object.getInteger("value")).collect(Collectors.toList()).stream().mapToInt(object -> object).sum()).sum();
            String result = CommonUtil.getPercent(aNum, bNum, 4);
            CommonUtil.valueView("界面百分比：" + percent, "计算率：" + result);
            Preconditions.checkArgument(result.equals(percent), arr.get("userName") + e.getName() + type + "的FU+PU+BB加和除以" + "RECEIVE" + "的FU+PU加和：" + result + " 界面显示百分比：" + percent);
            CommonUtil.logger(arr.get("userName"));
        }));
    }

    //两个数据无关系
    @Test(description = "店面数据分析--业务漏斗,接待线索>=商机的FU", enabled = false)
    public void shopPanel_data_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            for (EnumFindType e : EnumFindType.values()) {
                List<Map<String, String>> list = method.getSaleListByRoleName("销售顾问");
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
            appendFailReason(e.toString());
        } finally {
            saveData("店面数据分析--业务漏斗,接待线索>=商机的FU");
        }
    }

//    ----------------------------------------------------车系漏斗--------------------------------------------------------

    @Test(description = "店面数据分析--车系漏斗，【各时间段+各销售】线索=创建线索+接待线索")
    public void shopPanel_data_38() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            compareBusinessFunnelData("car_type", "CLUE");
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            saveData("店面数据分析--车系漏斗，【各时间段+各销售】线索=创建线索+接待线索");
        }
    }

    @Test(description = "店面数据分析--车系漏斗，【各时间段+各销售】接待=各车型数量之和（意向车型）")
    public void shopPanel_data_42() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            compareBusinessFunnelData("car_type", "RECEIVE");
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            saveData("店面数据分析--车系漏斗，【各时间段+各销售】接待=各车型数量之和（意向车型）");
        }
    }

    @Test(description = "店面数据分析--车系漏斗，【各时间段+各销售】试驾=各车型数量之和（审核通过&&没取消的试驾车型）")
    public void shopPanel_data_43() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            compareBusinessFunnelData("car_type", "TEST_DRIVE");
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            saveData("店面数据分析--车系漏斗，【各时间段+各销售】试驾=各车型数量之和（审核通过&&没取消的试驾车型）");
        }
    }

    @Test(description = "店面数据分析--车系漏斗，【各时间段+各销售】订单=各车型数量之和")
    public void shopPanel_data_44() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            compareBusinessFunnelData("car_type", "ORDER");
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            saveData("店面数据分析--车系漏斗，【各时间段+各销售】订单=各车型数量之和");
        }
    }

    @Test(description = "店面数据分析--车系漏斗，【各时间段+各销售】交车=各车型数量之和（购买车型）")
    public void shopPanel_data_45() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            compareBusinessFunnelData("car_type", "DEAL");
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            saveData("店面数据分析--车系漏斗，【各时间段+各销售】交车=各车型数量之和（购买车型）");
        }
    }

//    ----------------------------------------------------接待时长--------------------------------------------------------

    @Test(description = "店面数据分析--【日】累计接待<=【周】累计接待")
    public void shopPanel_data_47() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            List<Map<String, String>> list = method.getSaleListByRoleName("销售顾问");
            list.forEach(arr -> {
                CommonUtil.valueView(arr.get("userName"));
                getData(arr.get("userId"), EnumFindType.DAY, EnumFindType.WEEK, "service");
                Preconditions.checkArgument(s <= v, arr.get("userName") + "日接待数量为：" + s + "周接待数量为：" + v);
            });
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            saveData("店面数据分析--【日】累计接待<=【周】累计接待");
        }
    }

    @Test(description = "店面数据分析--【周】累计接待<=【月】累计接待")
    public void shopPanel_data_48() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            List<Map<String, String>> list = method.getSaleListByRoleName("销售顾问");
            list.forEach(arr -> {
                CommonUtil.valueView(arr.get("userName"));
                getData(arr.get("userId"), EnumFindType.WEEK, EnumFindType.MONTH, "service");
                Preconditions.checkArgument(s <= v, arr.get("userName") + "周接待数量为：" + s + "月接待数量为：" + v);
            });
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            saveData("店面数据分析--【周】累计接待<=【月】累计接待");
        }
    }

    @Test(description = "店面数据分析--【月】累计接待<=【季】累计接待")
    public void shopPanel_data_49() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            List<Map<String, String>> list = method.getSaleListByRoleName("销售顾问");
            list.forEach(arr -> {
                CommonUtil.valueView(arr.get("userName"));
                getData(arr.get("userId"), EnumFindType.MONTH, EnumFindType.QUARTER, "service");
                Preconditions.checkArgument(s <= v, arr.get("userName") + "月接待数量为：" + s + "季接待数量为：" + v);
            });
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            saveData("店面数据分析--【月】累计接待<=【季】累计接待");
        }
    }

    @Test(description = "店面数据分析--【季】累计接待<=【年】累计接待")
    public void shopPanel_data_50() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            List<Map<String, String>> list = method.getSaleListByRoleName("销售顾问");
            list.forEach(arr -> {
                CommonUtil.valueView(arr.get("userName"));
                getData(arr.get("userId"), EnumFindType.QUARTER, EnumFindType.YEAR, "service");
                Preconditions.checkArgument(s <= v, arr.get("userName") + "季接待数量为：" + s + "年接待数量为：" + v);
            });
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            saveData("店面数据分析--【季】累计接待<=【年】累计接待");
        }
    }

    @Test(description = "店面数据分析--【日】累计试驾<=【周】累计试驾")
    public void shopPanel_data_51() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            List<Map<String, String>> list = method.getSaleListByRoleName("销售顾问");
            list.forEach(arr -> {
                CommonUtil.valueView(arr.get("userName"));
                getData(arr.get("userId"), EnumFindType.DAY, EnumFindType.WEEK, "test_drive");
                Preconditions.checkArgument(s <= v, arr.get("userName") + "日试驾数量为：" + s + "周试驾数量为：" + v);
            });
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            saveData("店面数据分析--【日】累计试驾<=【周】累计试驾");
        }
    }

    @Test(description = "店面数据分析--【周】累计试驾<=【月】累计试驾")
    public void shopPanel_data_52() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            List<Map<String, String>> list = method.getSaleListByRoleName("销售顾问");
            list.forEach(arr -> {
                CommonUtil.valueView(arr.get("userName"));
                getData(arr.get("userId"), EnumFindType.WEEK, EnumFindType.MONTH, "test_drive");
                Preconditions.checkArgument(s <= v, arr.get("userName") + "周试驾数量为：" + s + "月试驾数量为：" + v);
            });
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            saveData("店面数据分析--【周】累计试驾<=【月】累计试驾");
        }
    }

    @Test(description = "店面数据分析--【月】累计试驾<=【季】累计试驾")
    public void shopPanel_data_53() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            List<Map<String, String>> list = method.getSaleListByRoleName("销售顾问");
            list.forEach(arr -> {
                CommonUtil.valueView(arr.get("userName"));
                getData(arr.get("userId"), EnumFindType.MONTH, EnumFindType.QUARTER, "test_drive");
                Preconditions.checkArgument(s <= v, arr.get("userName") + "月试驾数量为：" + s + "季试驾数量为：" + v);
            });
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            saveData("店面数据分析--【月】累计试驾<=【季】累计试驾");
        }
    }

    @Test(description = "店面数据分析--【季】累计试驾<=【年】累计试驾")
    public void shopPanel_data_54() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            List<Map<String, String>> list = method.getSaleListByRoleName("销售顾问");
            list.forEach(arr -> {
                CommonUtil.valueView(arr.get("userName"));
                getData(arr.get("userId"), EnumFindType.QUARTER, EnumFindType.YEAR, "test_drive");
                Preconditions.checkArgument(s <= v, arr.get("userName") + "季试驾数量为：" + s + "年试驾数量为：" + v);
            });
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            saveData("店面数据分析--【季】累计试驾<=【年】累计试驾");
        }
    }

    @Test(description = "店面数据分析--【日】累计成交<=【周】累计成交")
    public void shopPanel_data_55() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            List<Map<String, String>> list = method.getSaleListByRoleName("销售顾问");
            list.forEach(arr -> {
                CommonUtil.valueView(arr.get("userName"));
                getData(arr.get("userId"), EnumFindType.DAY, EnumFindType.WEEK, "deal");
                Preconditions.checkArgument(s <= v, arr.get("userName") + "日成交数量为：" + s + "周成交数量为：" + v);
            });
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            saveData("店面数据分析--【日】累计成交<=【周】累计成交");
        }
    }

    @Test(description = "店面数据分析--【周】累计成交<=【月】累计成交")
    public void shopPanel_data_56() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            List<Map<String, String>> list = method.getSaleListByRoleName("销售顾问");
            list.forEach(arr -> {
                CommonUtil.valueView(arr.get("userName"));
                getData(arr.get("userId"), EnumFindType.WEEK, EnumFindType.MONTH, "deal");
                Preconditions.checkArgument(s <= v, arr.get("userName") + "周成交数量为：" + s + "月成交数量为：" + v);
            });
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            saveData("店面数据分析--【周】累计成交<=【月】累计成交");
        }
    }

    @Test(description = "店面数据分析--【月】累计成交<=【季】累计成交")
    public void shopPanel_data_57() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            List<Map<String, String>> list = method.getSaleListByRoleName("销售顾问");
            list.forEach(arr -> {
                CommonUtil.valueView(arr.get("userName"));
                getData(arr.get("userId"), EnumFindType.MONTH, EnumFindType.QUARTER, "deal");
                Preconditions.checkArgument(s <= v, arr.get("userName") + "月成交数量为：" + s + "季成交数量为：" + v);
            });
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            saveData("店面数据分析--【月】累计成交<=【季】累计成交");
        }
    }

    @Test(description = "店面数据分析--【季】累计成交<=【年】累计成交")
    public void shopPanel_data_58() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            List<Map<String, String>> list = method.getSaleListByRoleName("销售顾问");
            list.forEach(arr -> {
                CommonUtil.valueView(arr.get("userName"));
                getData(arr.get("userId"), EnumFindType.QUARTER, EnumFindType.YEAR, "deal");
                Preconditions.checkArgument(s <= v, arr.get("userName") + "季成交数量为：" + s + "年成交数量为：" + v);
            });
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            saveData("店面数据分析--【季】累计成交<=【年】累计成交");
        }
    }

    @Test(description = "店面数据分析--【日】累计交车<=【周】累计交车")
    public void shopPanel_data_59() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            List<Map<String, String>> list = method.getSaleListByRoleName("销售顾问");
            list.forEach(arr -> {
                CommonUtil.valueView(arr.get("userName"));
                getData(arr.get("userId"), EnumFindType.DAY, EnumFindType.WEEK, "delivery");
                Preconditions.checkArgument(s <= v, arr.get("userName") + "日交车数量为：" + s + "周成交数量为：" + v);
            });
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            saveData("店面数据分析--【日】累计交车<=【周】累计交车");
        }
    }

    @Test(description = "店面数据分析--【周】累计交车<=【月】累计交车")
    public void shopPanel_data_60() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            List<Map<String, String>> list = method.getSaleListByRoleName("销售顾问");
            list.forEach(arr -> {
                CommonUtil.valueView(arr.get("userName"));
                getData(arr.get("userId"), EnumFindType.WEEK, EnumFindType.MONTH, "delivery");
                Preconditions.checkArgument(s <= v, arr.get("userName") + "周交车数量为：" + s + "月成交数量为：" + v);
            });
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            saveData("店面数据分析--【周】累计交车<=【月】累计交车");
        }
    }

    @Test(description = "店面数据分析--【月】累计交车<=【季】累计交车")
    public void shopPanel_data_61() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            List<Map<String, String>> list = method.getSaleListByRoleName("销售顾问");
            list.forEach(arr -> {
                CommonUtil.valueView(arr.get("userName"));
                getData(arr.get("userId"), EnumFindType.MONTH, EnumFindType.QUARTER, "delivery");
                Preconditions.checkArgument(s <= v, arr.get("userName") + "月交车数量为：" + s + "季成交数量为：" + v);
            });
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            saveData("店面数据分析--【月】累计交车<=【季】累计交车");
        }
    }

    @Test(description = "店面数据分析--【季】累计交车<=【年】累计交车")
    public void shopPanel_data_62() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            List<Map<String, String>> list = method.getSaleListByRoleName("销售顾问");
            list.forEach(arr -> {
                CommonUtil.valueView(arr.get("userName"));
                getData(arr.get("userId"), EnumFindType.QUARTER, EnumFindType.YEAR, "delivery");
                Preconditions.checkArgument(s <= v, arr.get("userName") + "季交车数量为：" + s + "年成交数量为：" + v);
            });
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
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
            appendFailReason(e.toString());
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
            appendFailReason(e.toString());
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
            appendFailReason(e.toString());
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
            appendFailReason(e.toString());
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
            appendFailReason(e.toString());
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
            JSONArray zjlList = crm.saleFunnel(e.getType(), "", "").getJSONObject("car_type").getJSONArray("list");
            int y = zjlList.stream().map(a -> (JSONObject) a).filter(a -> a.getString("type").equals(type)).map(a -> a.getInteger("value")).collect(Collectors.toList()).get(0);
            int total = crm.userUserPage(1, 10).getInteger("total");
            int s = CommonUtil.getTurningPage(total, size);
            int sum = 0;
            for (int i = 1; i < s; i++) {
                JSONArray userList = crm.userUserPage(i, size).getJSONArray("list");
                for (int j = 0; j < userList.size(); j++) {
                    if (userList.getJSONObject(j).getString("role_name").equals("销售顾问")) {
                        String userId = userList.getJSONObject(j).getString("user_id");
                        CommonUtil.valueView(userList.getJSONObject(j).getString("user_name"));
                        JSONArray list = crm.saleFunnel(e.getType(), "", userId).getJSONObject("car_type").getJSONArray("list");
                        int value = list.stream().map(a -> (JSONObject) a).filter(a -> a.getString("type").equals(type)).map(a -> a.getInteger("value")).collect(Collectors.toList()).get(0);
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
            appendFailReason(e.toString());
        } finally {
            saveData("店面数据分析--【业务漏斗】线索=【车型漏斗】线索");
        }
    }

    //交车数据含有导入数据，两者无关系
    @Test(description = "店面数据分析--【业务漏斗】交车==【车型漏斗】交车", enabled = false)
    public void shopPanel_data_25() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            compareTwoFunnelData("DEAL");
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
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
                List<Map<String, String>> array = method.getSaleListByRoleName("销售顾问");
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
            appendFailReason(e.toString());
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
            List<Map<String, String>> array = method.getSaleListByRoleName("销售顾问");
            array.forEach(arr -> {
                CommonUtil.valueView(arr.get("userName") + e.getName());
                JSONObject data = crm.shopSaleFunnel(e.getType(), "", arr.get("userId"));
                JSONArray businessList = data.getJSONObject("business").getJSONArray("list");
                JSONArray carTypeList = data.getJSONObject("car_type").getJSONArray("list");
                int businessValue = businessList.stream().map(json -> (JSONObject) json).filter(json -> json.getString("type").equals(type)).map(json -> json.getInteger("value")).collect(Collectors.toList()).get(0);
                int carTypeValue = carTypeList.stream().map(json -> (JSONObject) json).filter(json -> json.getString("type").equals(type)).map(json -> json.getInteger("value")).collect(Collectors.toList()).get(0);
                CommonUtil.valueView("业务漏斗：" + businessValue, "车系漏斗：" + carTypeValue);
                Preconditions.checkArgument(businessValue >= carTypeValue, arr.get("userName") +
                        " " + e.getName() + " 【业务漏斗】" + type + "数据为：" + businessValue + ",【车型漏斗】" + type + "数据为：" + carTypeValue);
                CommonUtil.logger(arr.get("userName"));
            });
        }
    }

    @Test(description = "店面数据分析--人工接待>=【业务漏斗】商机")
    public void shopPanel_data_26() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            for (EnumFindType e : EnumFindType.values()) {
                List<Map<String, String>> list = method.getSaleListByRoleName("销售顾问");
                for (Map<String, String> map : list) {
                    CommonUtil.valueView(map.get("userName") + e.getName());
                    IScene scene = Analysis2ShopPanelScene.builder().saleId(map.get("userId")).cycleType(e.getType()).build();
                    int service = crm.invokeApi(scene).getInteger("service");
                    IScene scene1 = Analysis2ShopSaleFunnelScene.builder().saleId(map.get("userId")).cycleType(e.getType()).build();
                    JSONArray businessList = crm.invokeApi(scene1).getJSONObject("business").getJSONArray("list");
                    int funnelService = businessList.stream().map(a -> (JSONObject) a).filter(a -> a.getString("type").equals("RECEIVE"))
                            .map(a -> a.getInteger("value")).collect(Collectors.toList()).get(0);
                    CommonUtil.valueView("人共接待：" + service, "漏斗商机：" + funnelService);
                    Preconditions.checkArgument(service >= funnelService, "人共接待数：" + service + "漏斗接商机数：" + funnelService);
                    CommonUtil.logger(map.get("userName"));
                }
            }
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            saveData("店面数据分析--人工接待>=【业务漏斗】商机");
        }
    }

    @Test(description = "店面数据分析--人工接待=商机的FU+PU+BB")
    public void shopPanel_data_27() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            for (EnumFindType e : EnumFindType.values()) {
                List<Map<String, String>> list = method.getSaleListByRoleName("销售顾问");
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
            appendFailReason(e.toString());
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
            List<Map<String, String>> array = method.getSaleListByRoleName("销售顾问");
            array.forEach(arr -> {
                String userName = arr.get("userName");
                String userId = arr.get("userId");
                CommonUtil.valueView(userName);
                Sql.Builder builder = Sql.instance().select()
                        .from(TPorscheReceptionData.class)
                        .where("reception_date", "=", date)
                        .and("reception_duration", "<", 10)
                        .and("reception_start_time", "is not", null)
                        .and("shop_id", "=", shopId);
                Sql sql = StringUtils.isEmpty(userId) ? builder.end() : builder.and("reception_sale_id", "=", userId).end();
                int count = new Factory.Builder().container(EnumContainer.ONE_PIECE.getContainer()).build().create(sql).size();
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
            e.printStackTrace();
            appendFailReason(e.toString());
        } finally {
            saveData("10分钟内组数=【前一日】【销售总监-PC-接待列表】离店时间-接待时间<10分钟的数量");
        }
    }

    @Test(description = "10～30分钟组数=【前一日】【销售总监-PC-接待列表】10分钟<=离店时间-接待时间<30分钟的数量")
    public void shopPanel_data_31() {
        logger.logCaseStart(caseResult.getCaseName());
        String date = DateTimeUtil.addDayFormat(new Date(), -1);
        try {
            List<Map<String, String>> array = method.getSaleListByRoleName("销售顾问");
            array.forEach(arr -> {
                String userName = arr.get("userName");
                String userId = arr.get("userId");
                CommonUtil.valueView(userName);
                Sql.Builder builder = Sql.instance().select()
                        .from(TPorscheReceptionData.class)
                        .where("reception_date", "=", date)
                        .and("reception_duration", "<", 30)
                        .and("reception_duration", ">=", 10)
                        .and("reception_start_time", "is not", null)
                        .and("shop_id", "=", shopId);
                Sql sql = StringUtils.isEmpty(userId) ? builder.end() : builder.and("reception_sale_id", "=", userId).end();
                int count = ONE_PIECE_FACTORY.create(sql).size();
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
            appendFailReason(e.toString());
        } finally {
            saveData("10～30分钟组数=【前一日】【销售总监-PC-接待列表】10分钟<=离店时间-接待时间<30分钟的数量");
        }
    }

    @Test(description = "30～60分钟内组数=【前一日】【销售总监-PC-接待列表】30分钟<=离店时间-接待时间<60分钟的数量")
    public void shopPanel_data_32() {
        logger.logCaseStart(caseResult.getCaseName());
        String date = DateTimeUtil.addDayFormat(new Date(), -1);
        try {
            List<Map<String, String>> array = method.getSaleListByRoleName("销售顾问");
            array.forEach(arr -> {
                String userName = arr.get("userName");
                String userId = arr.get("userId");
                CommonUtil.valueView(userName);
                Sql.Builder builder = Sql.instance().select()
                        .from(TPorscheReceptionData.class)
                        .where("reception_date", "=", date)
                        .and("reception_duration", "<", 60)
                        .and("reception_duration", ">=", 30)
                        .and("reception_start_time", "is not", null)
                        .and("shop_id", "=", shopId);
                Sql sql = StringUtils.isEmpty(userId) ? builder.end() : builder.and("reception_sale_id", "=", userId).end();
                int count = ONE_PIECE_FACTORY.create(sql).size();
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
            appendFailReason(e.toString());
        } finally {
            saveData("30～60分钟内组数=【前一日】【销售总监-PC-接待列表】30分钟<=离店时间-接待时间<60分钟的数量");
        }
    }

    @Test(description = "60～120分钟内组数=【前一日】【销售总监-PC-接待列表】60分钟<=离店时间-接待时间<120分钟的数量")
    public void shopPanel_data_33() {
        logger.logCaseStart(caseResult.getCaseName());
        String date = DateTimeUtil.addDayFormat(new Date(), -1);
        try {
            List<Map<String, String>> array = method.getSaleListByRoleName("销售顾问");
            array.forEach(arr -> {
                String userName = arr.get("userName");
                String userId = arr.get("userId");
                CommonUtil.valueView(userName);
                Sql.Builder builder = Sql.instance().select()
                        .from(TPorscheReceptionData.class)
                        .where("reception_date", "=", date)
                        .and("reception_duration", "<", 120)
                        .and("reception_duration", ">=", 60)
                        .and("reception_start_time", "is not", null)
                        .and("shop_id", "=", shopId);
                Sql sql = StringUtils.isEmpty(userId) ? builder.end() : builder.and("reception_sale_id", "=", userId).end();
                int count = ONE_PIECE_FACTORY.create(sql).size();
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
            collectMessage(e);
        } finally {
            saveData("60～120分钟内组数=【前一日】【销售总监-PC-接待列表】60分钟<=离店时间-接待时间<120分钟的数量");
        }
    }

    @Test(description = "大于120分钟组数=【前一日】【销售总监-PC-接待列表】离店时间-接待时间>=120分钟的数量")
    public void shopPanel_data_34() {
        logger.logCaseStart(caseResult.getCaseName());
        String date = DateTimeUtil.addDayFormat(new Date(), -1);
        try {
            List<Map<String, String>> array = method.getSaleListByRoleName("销售顾问");
            array.forEach(arr -> {
                String userName = arr.get("userName");
                String userId = arr.get("userId");
                CommonUtil.valueView(userName);
                Sql.Builder builder = Sql.instance().select()
                        .from(TPorscheReceptionData.class)
                        .where("reception_date", "=", date)
                        .and("reception_duration", ">=", 120)
                        .and("reception_start_time", "is not", null)
                        .and("shop_id", "=", shopId);
                Sql sql = StringUtils.isEmpty(userId) ? builder.end() : builder.and("reception_sale_id", "=", userId).end();
                int count = ONE_PIECE_FACTORY.create(sql).size();
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
            appendFailReason(e.toString());
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
            appendFailReason(e.toString());
        } finally {
            saveData("店面数据分析--智能接待=【进店批次分析页】所选时间段列表数");
        }
    }

    @Test(description = "店面数据分析--【各时间段】创建线索=【销售总监-app-销售接待】今日线索-【选中时间】【销售总监-app-我的接待】今日新客接待", enabled = false)
    public void shopPanel_data_63() {
        logger.logCaseStart(caseResult.getCaseName());
        String date = DateTimeUtil.addDayFormat(new Date(), -1);
        try {
            int createClueNum = 0;
            IScene scene = Analysis2ShopSaleFunnelScene.builder().cycleType(EnumFindType.DAY.getType()).build();
            JSONArray array = crm.invokeApi(scene).getJSONObject("business").getJSONArray("list");
            for (int i = 0; i < array.size(); i++) {
                if (array.getJSONObject(i).getString("type").equals("CLUE")) {
                    JSONArray detailList = array.getJSONObject(i).getJSONArray("detail");
                    for (int j = 0; j < detailList.size(); j++) {
                        if (detailList.getJSONObject(j).getString("label").equals("创建线索")) {
                            createClueNum = detailList.getJSONObject(j).getInteger("value");
                        }
                    }
                }
            }
            //今日线索
            Sql sql = Sql.instance().select("today_clue_num", "today_new_customer_reception_num")
                    .from(TPorscheTodayData.class)
                    .where("today_date", "=", date)
                    .and("sale_name", "like", "%总经理%")
                    .and("shop_id", "=", shopId).end();
            List<TPorscheTodayData> list = ONE_PIECE_FACTORY.create(sql, TPorscheTodayData.class);
            if (list.size() > 0) {
                for (TPorscheTodayData tPorscheTodayData : list) {
                    int todayClueNum = tPorscheTodayData.getTodayClueNum();
                    int todayNewCustomerReceptionNum = tPorscheTodayData.getTodayNewCustomerReceptionNum();
                    CommonUtil.valueView(createClueNum, todayClueNum - todayNewCustomerReceptionNum);
                    Preconditions.checkArgument(createClueNum == todayClueNum - todayNewCustomerReceptionNum,
                            "创建线索：" + createClueNum + " 今日线索-今日新客接待：" + (todayClueNum - todayNewCustomerReceptionNum));
                }
            }
        } catch (Exception | AssertionError e) {
            e.printStackTrace();
            appendFailReason(e.toString());
        } finally {
            saveData("店面数据分析--【各时间段】创建线索=【销售总监-app-销售接待】今日线索-【选中时间】【销售总监-app-我的接待】今日新客接待");
        }
    }

    @Test(description = "店面数据分析--【各时间段】接待线索=【销售总监-app-销售接待】今日新客接待", enabled = false)
    public void shopPanel_data_64() {
        logger.logCaseStart(caseResult.getCaseName());
        String date = DateTimeUtil.addDayFormat(new Date(), -1);
        try {
            int receptionClueNum = 0;
            IScene scene = Analysis2ShopSaleFunnelScene.builder().cycleType(EnumFindType.DAY.getType()).build();
            JSONArray array = crm.invokeApi(scene).getJSONObject("business").getJSONArray("list");
            for (int i = 0; i < array.size(); i++) {
                if (array.getJSONObject(i).getString("type").equals("CLUE")) {
                    JSONArray detailList = array.getJSONObject(i).getJSONArray("detail");
                    for (int j = 0; j < detailList.size(); j++) {
                        if (detailList.getJSONObject(j).getString("label").equals("接待线索")) {
                            receptionClueNum = detailList.getJSONObject(j).getInteger("value");
                        }
                    }
                }
            }
            //今日新客接待
            Sql sql = Sql.instance().select("today_new_customer_reception_num")
                    .from(TPorscheTodayData.class)
                    .where("today_date", "=", date)
                    .and("sale_name", "like", "%总经理%")
                    .and("shop_id", "=", shopId).end();
            List<TPorscheTodayData> list = ONE_PIECE_FACTORY.create(sql, TPorscheTodayData.class);
            if (list.size() > 0) {
                for (TPorscheTodayData tPorscheTodayData : list) {
                    int todayNewCustomerReceptionNum = tPorscheTodayData.getTodayNewCustomerReceptionNum();
                    CommonUtil.valueView(receptionClueNum, todayNewCustomerReceptionNum);
                    Preconditions.checkArgument(receptionClueNum == todayNewCustomerReceptionNum, "接待线索：" + receptionClueNum + " 今日新客接待" + todayNewCustomerReceptionNum);
                }
            }
        } catch (Exception | AssertionError e) {
            e.printStackTrace();
            appendFailReason(e.toString());
        } finally {
            saveData("店面数据分析--【各时间段】创建线索=【销售总监-app-销售接待】今日线索-【选中时间】【销售总监-app-我的接待】今日新客接待");
        }
    }

    @Test(description = "店面数据分析--【各时间段】接待的PU+BB >=【销售总监-app-我的接待】今日老客接待")
    public void shopPanel_data_65() {
        logger.logCaseStart(caseResult.getCaseName());
        String date = DateTimeUtil.addDayFormat(new Date(), -1);
        try {
            int receive = 0;
            IScene scene = Analysis2ShopSaleFunnelScene.builder().cycleType(EnumFindType.DAY.getType()).build();
            JSONArray list = crm.invokeApi(scene).getJSONObject("business").getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                if (list.getJSONObject(i).getString("type").equals("RECEIVE")) {
                    JSONArray detailList = list.getJSONObject(i).getJSONArray("detail");
                    for (int j = 0; j < detailList.size(); j++) {
                        if (detailList.getJSONObject(j).getString("label").equals("PU")
                                || detailList.getJSONObject(j).getString("label").equals("BB")) {
                            receive = detailList.getJSONObject(j).getInteger("value");
                        }
                    }
                }
            }
            Sql sql = Sql.instance().select("today_old_customer_reception_num")
                    .from(TPorscheTodayData.class)
                    .where("today_date", "=", date)
                    .and("sale_name", "like", "%总经理%")
                    .and("shop_id", "=", shopId).end();
            List<TPorscheTodayData> entity = ONE_PIECE_FACTORY.create(sql, TPorscheTodayData.class);
            int todayNewCustomerReceptionNum = entity.get(0).getTodayOldCustomerReceptionNum();
            CommonUtil.valueView(receive, todayNewCustomerReceptionNum);
            Preconditions.checkArgument(receive >= todayNewCustomerReceptionNum, "接待的PU+BB：" + receive + "今日老客接待：" + todayNewCustomerReceptionNum);
        } catch (Exception | AssertionError e) {
            e.printStackTrace();
        } finally {
            saveData("店面数据分析--【各时间段】接待的PU+BB >=【销售总监-app-我的接待】今日老客接待");
        }
    }

    @Test(description = "店面数据分析--【各时间段+各销售】创建线索>=【该销售-app-展厅客户】创建日期在该时间范围内&&该客户接待记录为空的数量", enabled = false)
    public void shopPanel_data_66() {
        logger.logCaseStart(caseResult.getCaseName());
        String date = DateTimeUtil.addDayFormat(new Date(), -1);
        try {
            List<Map<String, String>> list = method.getSaleListByRoleName("销售顾问");
            for (Map<String, String> arr : list) {
                if (arr.get("userName").contains("总经理")) {
                    continue;
                }
                CommonUtil.valueView(arr.get("userName"));
                int createClueNum = 0;
                IScene scene = Analysis2ShopSaleFunnelScene.builder().cycleType(EnumFindType.DAY.getType()).saleId(arr.get("userId")).build();
                JSONArray array = crm.invokeApi(scene).getJSONObject("business").getJSONArray("list");
                for (int i = 0; i < array.size(); i++) {
                    if (array.getJSONObject(i).getString("type").equals("CLUE")) {
                        JSONArray detailList = array.getJSONObject(i).getJSONArray("detail");
                        for (int j = 0; j < detailList.size(); j++) {
                            if (detailList.getJSONObject(j).getString("label").equals("创建线索")) {
                                createClueNum = detailList.getJSONObject(j).getInteger("value");
                            }
                        }
                    }
                }
                int num = 0;
                for (int i = 1; i < 3; i++) {
                    IScene scene2 = CustomerPageScene.builder().page(String.valueOf(i)).size(String.valueOf(size)).build();
                    JSONArray array1 = crm.invokeApi(scene2).getJSONArray("list");
                    for (int j = 0; j < array1.size(); j++) {
                        String belongsSaleName = array1.getJSONObject(j).getString("belongs_sale_name");
                        String createDate = array1.getJSONObject(j).getString("create_date");
                        if (belongsSaleName != null && createDate.equals(date) && belongsSaleName.equals(arr.get("userName"))) {
                            CommonUtil.valueView(belongsSaleName + " 创建日期：" + createDate);
                            int customerId = array1.getJSONObject(j).getInteger("customer_id");
                            JSONArray carList = crm.receptionList(String.valueOf(customerId)).getJSONArray("list");
                            if (carList.size() == 0) {
                                num++;
                            }
                        }
                    }
                }
                CommonUtil.valueView(createClueNum, num);
                Preconditions.checkArgument(createClueNum >= num, arr.get("userName") + " pc端记录创建线索：" + createClueNum + " app查询创建未接待数量：" + num);
                CommonUtil.logger(arr.get("userName"));
            }
        } catch (Exception | AssertionError e) {
            e.printStackTrace();
            appendFailReason(e.toString());
        } finally {
            saveData("店面数据分析--【各时间段+各销售】创建线索>=【该销售-app-展厅客户】创建日期在该时间范围内&&该客户接待记录为空的数量");
        }
    }

    @Test(description = "店面数据分析--【各时间段+不选销售】创建线索=【销售总监-app-展厅客户】创建日期在该时间范围内&&该客户接待记录为空的数量 + 【销售总监-app-DCC客户】创建日期在该时间范围内&&该客户接待记录为空的数量")
    public void shopPanel_data_67() {
        logger.logCaseStart(caseResult.getCaseName());
        String date = DateTimeUtil.addDayFormat(new Date(), -1);
        try {
            //创建线索数
            int createClueNum = 0;
            IScene scene = Analysis2ShopSaleFunnelScene.builder().cycleType(EnumFindType.DAY.getType()).build();
            JSONArray list = crm.invokeApi(scene).getJSONObject("business").getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                if (list.getJSONObject(i).getString("type").equals("CLUE")) {
                    JSONArray detailList = list.getJSONObject(i).getJSONArray("detail");
                    for (int j = 0; j < detailList.size(); j++) {
                        if (detailList.getJSONObject(j).getString("label").equals("创建线索")) {
                            createClueNum = detailList.getJSONObject(j).getInteger("value");
                        }
                    }
                }
            }
            //创建日期为当天且没有接待记录的客户数量
            int num = 0;
            for (int i = 1; i < 3; i++) {
                IScene scene2 = CustomerPageScene.builder().page(String.valueOf(i)).size(String.valueOf(size)).build();
                JSONArray array = crm.invokeApi(scene2).getJSONArray("list");
                for (int j = 0; j < array.size(); j++) {
                    if (array.getJSONObject(j).getString("create_date").equals(date)) {
                        int customerId = array.getJSONObject(j).getInteger("customer_id");
                        JSONArray carList = crm.receptionList(String.valueOf(customerId)).getJSONArray("list");
                        if (carList.size() == 0) {
                            num++;
                        }
                    }
                }
            }
            CommonUtil.valueView(createClueNum, num);
            Preconditions.checkArgument(createClueNum >= num, "昨日创建线索：" + createClueNum + " 创建日期为当天且没有接待记录的客户数量：" + num);
        } catch (Exception | AssertionError e) {
            e.printStackTrace();
            appendFailReason(e.toString());
        } finally {
            saveData("店面数据分析--【各时间段+各销售】创建线索>=【该销售-app-展厅客户】创建日期在该时间范围内&&该客户接待记录为空的数量");
        }
    }

    @Test(description = "店面数据分析--【各时间段+各销售】接待线索=【该销售-app-销售接待】今日新客接待", enabled = false)
    public void shopPanel_data_68() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String date = DateTimeUtil.addDayFormat(new Date(), -1);
            List<Map<String, String>> list = method.getSaleListByRoleName("销售顾问");
            for (Map<String, String> arr : list) {
                CommonUtil.valueView(arr.get("userName"));
                int createClueNum = 0;
                IScene scene = Analysis2ShopSaleFunnelScene.builder().cycleType(EnumFindType.DAY.getType()).saleId(arr.get("userId")).build();
                JSONArray array = crm.invokeApi(scene).getJSONObject("business").getJSONArray("list");
                for (int i = 0; i < array.size(); i++) {
                    if (array.getJSONObject(i).getString("type").equals("CLUE")) {
                        JSONArray detailList = array.getJSONObject(i).getJSONArray("detail");
                        for (int j = 0; j < detailList.size(); j++) {
                            if (detailList.getJSONObject(j).getString("label").equals("接待线索")) {
                                createClueNum = detailList.getJSONObject(j).getInteger("value");
                            }
                        }
                    }
                }
                Sql sql = Sql.instance().select("today_new_customer_reception_num")
                        .from(TPorscheTodayData.class)
                        .where("shop_id", "=", shopId)
                        .and("sale_id", "=", arr.get("userId"))
                        .and("today_date", "=", date).end();
                List<TPorscheTodayData> list1 = ONE_PIECE_FACTORY.create(sql, TPorscheTodayData.class);
                if (list1.size() > 0) {
                    int count = list1.get(0).getTodayNewCustomerReceptionNum();
                    CommonUtil.valueView(createClueNum, count);
                    Preconditions.checkArgument(createClueNum == count, arr.get("userName") + "接待线索：" + createClueNum + " 该销售今日新客接待：" + count);
                    CommonUtil.logger(arr.get("userName"));
                }
            }
        } catch (Exception | AssertionError e) {
            e.printStackTrace();
            appendFailReason(e.toString());
        } finally {
            saveData("店面数据分析--【各时间段+各销售】接待线索=【该销售-app-销售接待】今日新客接待");
        }

    }

    @Test(description = "店面数据分析--【各时间段+各销售】累计订单=【各销售-app-我的接待】今日订单数量")
    public void shopPanel_data_71() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String date = DateTimeUtil.addDayFormat(new Date(), -1);
            List<Map<String, String>> list = method.getSaleListByRoleName("销售顾问");
            for (Map<String, String> map : list) {
                String userName = map.get("userName");
                CommonUtil.valueView(userName);
                int num = getFunnelData("business", "ORDER", map);
                Sql sql = Sql.instance().select("today_order_num")
                        .from(TPorscheTodayData.class)
                        .where("sale_id", "=", map.get("userId"))
                        .and("shop_id", "=", shopId)
                        .and("today_date", "=", date).end();
                List<TPorscheTodayData> list1 = ONE_PIECE_FACTORY.create(sql, TPorscheTodayData.class);
                if (list1.size() > 0) {
                    int count = list1.get(0).getTodayOrderNum();
                    CommonUtil.valueView(num, count);
                    Preconditions.checkArgument(num >= count, userName + "各车型订单：" + num + " 该销售今日订单数量：" + count);
                    CommonUtil.logger(userName);
                }
            }
        } catch (Exception | AssertionError e) {
            e.printStackTrace();
            appendFailReason(e.toString());
        } finally {
            saveData("店面数据分析--【各时间段+各销售】累计订单=【各销售-app-我的接待】今日订单数量");
        }
    }

    @Test(description = "【各时间段+各销售】累计交车=【各销售-app-我的交车】今日交车数量")
    public void shopPanel_data_72() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String date = DateTimeUtil.addDayFormat(new Date(), -1);
            List<Map<String, String>> list = method.getSaleListByRoleName("销售顾问");
            for (Map<String, String> map : list) {
                CommonUtil.valueView(map.get("userName"));
                int num = getFunnelData("business", "DEAL", map);
                Sql sql = Sql.instance().select("today_deal_num")
                        .from(TPorscheTodayData.class)
                        .where("sale_id", "=", map.get("userId"))
                        .and("shop_id", "=", shopId)
                        .and("today_date", "=", date).end();
                List<TPorscheTodayData> list1 = ONE_PIECE_FACTORY.create(sql, TPorscheTodayData.class);
                if (list1.size() > 0) {
                    int count = list1.get(0).getTodayDealNum();
                    CommonUtil.valueView(num, count);
                    Preconditions.checkArgument(num == count, map.get("userName") + "各车型订单：" + num + " 该销售今日交车数量：" + count);
                    CommonUtil.logger(map.get("userName"));
                }
            }
        } catch (Exception | AssertionError e) {
            e.printStackTrace();
            appendFailReason(e.toString());
        } finally {
            saveData("【各时间段+各销售】累计交车=【各销售-app-我的交车】今日交车数量");
        }
    }

    /**
     * 获取业务漏斗数据
     *
     * @param type 标签类型
     * @param map  用户
     */
    private int getFunnelData(String funnelType, String type, Map<String, String> map) {
        int num = 0;
        IScene scene = Analysis2ShopSaleFunnelScene.builder().cycleType(EnumFindType.DAY.getType()).saleId(map.get("userId")).build();
        JSONArray array = crm.invokeApi(scene).getJSONObject(funnelType).getJSONArray("list");
        for (int i = 0; i < array.size(); i++) {
            if (array.getJSONObject(i).getString("type").equals(type)) {
                num += array.getJSONObject(i).getInteger("value");
            }
        }
        return num;
    }

    @Test(description = "店面数据分析--【时间段＋销售顾问】车系漏斗中订单＝成交记录中订车时间为【时间段＋销售顾问】的订单数量")
    public void shopPanel_data_73() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String date = DateTimeUtil.addDayFormat(new Date(), -1);
            List<Map<String, String>> list = method.getSaleListByRoleName("销售顾问");
            for (Map<String, String> map : list) {
                String userName = map.get("userName");
                String userId = map.get("userId");
                CommonUtil.valueView(userName);
                int num = getFunnelData("car_type", "ORDER", map);
                Sql.Builder builder = Sql.instance().select()
                        .from(TPorscheOrderInfo.class)
                        .where("order_date", "=", date)
                        .and("shop_id", "=", shopId)
                        .and("car_style", "is not", null)
                        .and("car_model", "is not ", null);
                Sql sql = StringUtils.isEmpty(userId) ? builder.end() : builder.and("sale_id", "=", userId).end();
                int count = new Factory.Builder().container(EnumContainer.ONE_PIECE.getContainer()).build().create(sql).size();
                CommonUtil.valueView(num, count);
                Preconditions.checkArgument(num == count, userName + "车系漏斗中订单：" + num + "成交记录中的订单数量：" + count);
                CommonUtil.logger(userName);
            }
        } catch (Exception | AssertionError e) {
            e.printStackTrace();
            appendFailReason(e.toString());
        } finally {
            saveData("店面数据分析--【时间段＋销售顾问】车系漏斗中订单＝成交记录中订车时间为【时间段＋销售顾问】的订单数量");
        }
    }

    @Test(description = "店面数据分析--【时间段＋销售顾问】车系漏斗中交车＝成交记录中交车时间为【时间段＋销售顾问】的订单数量")
    public void shopPanel_data_74() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String date = DateTimeUtil.addDayFormat(new Date(), -1);
            List<Map<String, String>> list = method.getSaleListByRoleName("销售顾问");
            for (Map<String, String> map : list) {
                String userName = map.get("userName");
                String userId = map.get("userId");
                CommonUtil.valueView(userName);
                int num = getFunnelData("car_type", "DEAL", map);
                Sql.Builder builder = Sql.instance().select()
                        .from(TPorscheDeliverInfo.class)
                        .where("deliver_date", "=", date)
                        .and("shop_id", "=", shopId)
                        .and("car_style", "is not", null)
                        .and("car_model", "is not ", null);
                Sql sql = StringUtils.isEmpty(userId) ? builder.end() : builder.and("sale_id", "=", userId).end();
                int count = ONE_PIECE_FACTORY.create(sql).size();
                CommonUtil.valueView(num, count);
                Preconditions.checkArgument(num == count, userName + "车系漏斗中交车：" + num + "成交记录中的交车数量：" + count);
                CommonUtil.logger(userName);
            }
        } catch (Exception | AssertionError e) {
            e.printStackTrace();
            appendFailReason(e.toString());
        } finally {
            saveData("店面数据分析--【时间段＋销售顾问】车系漏斗中交车＝成交记录中交车时间为【时间段＋销售顾问】的订单数量");
        }
    }

//    -----------------------------------------------存量客户分析---------------------------------------------------

    @Test(description = "存量客户分析页--【各时间段+各车型筛选】个人车主百分比+公司车主百分比=100% 或 0%")
    public void stockCustomer_data_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            for (EnumFindType a : EnumFindType.values()) {
                for (EnumCarStyle b : EnumCarStyle.values()) {
                    CommonUtil.valueView(b.getName() + a.getName());
                    double percentageSum = 0;
                    IScene scene = Analysis2DealCarOwnerScene.builder().carType(b.getStyleId()).cycleType(a.getType()).build();
                    JSONArray ratioList = crm.invokeApi(scene).getJSONArray("ratio_list");
                    for (int i = 0; i < ratioList.size(); i++) {
                        String percentageStr = ratioList.getJSONObject(i).getString("percent_str");
                        double percentage = Double.parseDouble(percentageStr.substring(0, percentageStr.length() - 1));
                        String name = ratioList.getJSONObject(i).getString("name");
                        CommonUtil.valueView(name + "：" + percentage);
                        percentageSum += percentage;
                    }
                    CommonUtil.valueView(percentageSum);
                    Preconditions.checkArgument(percentageSum == 100 || percentageSum == 0, b.getName() + a.getName() + "个人车主百分比+公司车主百分比=" + percentageSum + "%");
                    CommonUtil.logger(b.getName());
                }
                CommonUtil.logger(a.getName());
            }
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            saveData("存量客户分析页--【各时间段+各车型筛选】个人车主百分比+公司车主百分比=100% 或 0%");
        }
    }

    @Test(description = "存量客户分析页--【各时间段+各车型筛选】车主年龄分析 各年龄段之和=100%或 0%")
    public void stockCustomer_data_2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            for (EnumFindType a : EnumFindType.values()) {
                for (EnumCarStyle b : EnumCarStyle.values()) {
                    CommonUtil.valueView(b.getName() + a.getName());
                    double percentageSum = 0;
                    IScene scene = Analysis2DealGenderAgeScene.builder().carType(b.getStyleId()).cycleType(a.getType()).build();
                    JSONArray list = crm.invokeApi(scene).getJSONObject("age").getJSONArray("list");
                    for (int i = 0; i < list.size(); i++) {
                        String percentageStr = list.getJSONObject(i).getString("percentage_str");
                        double percentage = Double.parseDouble(percentageStr.substring(0, percentageStr.length() - 1));
                        String age = list.getJSONObject(i).getString("age");
                        CommonUtil.valueView(age + ":" + percentage);
                        percentageSum += percentage;
                    }
                    CommonUtil.valueView(percentageSum);
                    Preconditions.checkArgument((percentageSum >= 99 && percentageSum <= 101) || percentageSum == 0, b.getName() + a.getName() + "车主年龄分析 各年龄段之和=" + percentageSum + "%");
                    CommonUtil.logger(b.getName());
                }
                CommonUtil.logger(a.getName());
            }
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            saveData("存量客户分析页--【各时间段+各车型筛选】车主年龄分析 各年龄段之和=100%或 0%");
        }
    }

    @Test(description = "存量客户分析--【各时间段+各车型筛选】车主性别分析 性别之和=100%或 0%")
    public void stockCustomer_data_3() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            for (EnumFindType a : EnumFindType.values()) {
                for (EnumCarStyle b : EnumCarStyle.values()) {
                    CommonUtil.valueView(b.getName() + a.getName());
                    double percentageSum = 0;
                    IScene scene = Analysis2DealGenderAgeScene.builder().carType(b.getStyleId()).cycleType(a.getType()).build();
                    JSONArray list = crm.invokeApi(scene).getJSONObject("gender").getJSONArray("list");
                    for (int i = 0; i < list.size(); i++) {
                        String percentageStr = list.getJSONObject(i).getString("percentage_str");
                        double percentage = Double.parseDouble(percentageStr.substring(0, percentageStr.length() - 1));
                        String gender = list.getJSONObject(i).getString("gender");
                        CommonUtil.valueView(gender + ":" + percentage);
                        percentageSum += percentage;
                    }
                    CommonUtil.valueView(percentageSum);
                    Preconditions.checkArgument(percentageSum == 100 || percentageSum == 0, b.getName() + a.getName() + "车主性别分析 性别之和=" + percentageSum + "%");
                    CommonUtil.logger(b.getName());
                }
                CommonUtil.logger(a.getName());
            }
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
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
                    CommonUtil.logger(b.getName());
                }
                CommonUtil.logger(a.getName());
            }
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            saveData("存量客户分析--【各时间段+各车型筛选】苏州各区成交量之和<=江苏成交量");
        }
    }

    @Test(description = "存量客户分析--【各时间段】全国各省成交量，某个省的百分比=该省成交量/各省成交量之和")
    public void stockCustomer_data_4() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            for (EnumFindType a : EnumFindType.values()) {
                for (EnumCarStyle b : EnumCarStyle.values()) {
                    CommonUtil.valueView(b.getName() + a.getName());
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
                        String percentageStr = list.getJSONObject(i).getString("percentage_str");
                        CommonUtil.valueView(district + "数量：" + value + "，占比：" + percentageStr);
                        String percentage = CommonUtil.getPercent(value, totalValue, 4);
                        CommonUtil.valueView(district + "计算占比：" + percentage);
                        Preconditions.checkArgument(percentageStr.equals(percentage), b.getName() + a.getName() + district + "界面显示占比：" + percentageStr + "，计算占比：" + percentage);
                    }
                    CommonUtil.logger(b.getName());
                }
                CommonUtil.logger(a.getName());
            }
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            saveData("存量客户分析--【各时间段】全国各省成交量，某个省的百分比=该省成交量/各省成交量之和");
        }
    }

    @Test(description = "存量客户分析--【各时间段】全国各省成交量，各省百分比之和=100%")
    public void stockCustomer_data_8() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            for (EnumFindType a : EnumFindType.values()) {
                for (EnumCarStyle b : EnumCarStyle.values()) {
                    CommonUtil.valueView(b.getName() + a.getName());
                    double percentageNum = 0;
                    IScene scene = Analysis2DealWholeCountryScene.builder().carType(b.getStyleId()).cycleType(a.getType()).build();
                    JSONArray list = crm.invokeApi(scene).getJSONArray("list");
                    for (int i = 0; i < list.size(); i++) {
                        String percentageStr = list.getJSONObject(i).getString("percentage_str");
                        double percentage = Double.parseDouble(percentageStr.substring(0, percentageStr.length() - 1));
                        CommonUtil.valueView(percentage);
                        percentageNum += percentage;
                    }
                    CommonUtil.valueView(percentageNum);
                    Preconditions.checkArgument((percentageNum >= 99 && percentageNum <= 101) || percentageNum == 0, b.getName() + a.getName() + "全国各省占比百分比之和为：" + percentageNum + "%");
                    CommonUtil.logger(b.getName());
                }
            }
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
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
                    CommonUtil.valueView(b.getName() + a.getName());
                    JSONArray list = crm.city(a.getType(), "", b.getStyleId(), 320500).getJSONArray("list");
                    for (int i = 0; i < list.size(); i++) {
                        int value = list.getJSONObject(i).getInteger("value");
                        totalValue += value;
                    }
                    for (int i = 0; i < list.size(); i++) {
                        String district = list.getJSONObject(i).getString("district");
                        int value = list.getJSONObject(i).getInteger("value");
                        String percentageStr = list.getJSONObject(i).getString("percentage_str");
                        CommonUtil.valueView(district + "数量：" + value + "，占比：" + percentageStr);
                        String percentage = CommonUtil.getPercent(value, totalValue, 4);
                        CommonUtil.valueView(district + "计算占比：" + percentage);
                        Preconditions.checkArgument(percentage.equals(percentageStr), b.getName() + a.getName() + district + "接口返回占比：" + percentageStr + "，计算占比：" + percentage);
                    }
                    CommonUtil.logger(b.getName());
                }
            }
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
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
                        double percentage = Double.parseDouble(percentageStr.substring(0, percentageStr.length() - 1));
                        CommonUtil.valueView(percentage);
                        percentageNum += percentage;
                    }
                    CommonUtil.valueView(percentageNum);
                    Preconditions.checkArgument((percentageNum >= 99 && percentageNum <= 101) || percentageNum == 0, "苏州各区百分比之和为：" + percentageNum + "%");
                    CommonUtil.logger(b.getName());
                }
            }
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            saveData("存量客户分析--【各时间段筛选】苏州各区成交量，各区百分比之和=100%");
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
                Sql.Builder builder = Sql.instance().select()
                        .from(TPorscheDeliverInfo.class)
                        .where("subject_type_name", "=", "个人")
                        .and("deliver_date", "=", date)
                        .and("shop_id", "=", shopId);
                Sql sql = StringUtils.isEmpty(e.getStyleId()) ? builder.end() : builder.and("car_style", "=", e.getStyleId()).end();
                int appCustomerNum = ONE_PIECE_FACTORY.create(sql).size();
                CommonUtil.valueView(pcCustomerNum, appCustomerNum);
                Preconditions.checkArgument(appCustomerNum >= pcCustomerNum, "昨日" + e.getName() + "个人车主数为：" + pcCustomerNum + "昨日app该车系个人客户交车数量为：" + appCustomerNum);
                CommonUtil.logger(e.getName());
            }
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            saveData("存量客户分析--【各时间段+车系筛选】个人车主数量<=【app-销售总监-展厅客户-购车档案】客户类型为个人&交车日期在该时间段内&购买车系为筛选车系的购车档案数量");
        }
    }

    @Test(description = "存量客户分析--【各时间段+车系筛选】公司车主数量<=【app-销售总监-展厅客户-购车档案】客户类型为公司&交车日期在该时间段内&购买车系为筛选车系的购车档案数量")
    public void stockCustomer_data_13() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String date = DateTimeUtil.addDayFormat(new Date(), -1);
            for (EnumCarStyle e : EnumCarStyle.values()) {
                int pcCustomerNum = 0;
                CommonUtil.valueView(e.getName());
                IScene scene = Analysis2DealCarOwnerScene.builder().cycleType(EnumFindType.DAY.getType()).carType(e.getStyleId()).build();
                JSONArray ratioList = crm.invokeApi(scene).getJSONArray("ratio_list");
                for (int i = 0; i < ratioList.size(); i++) {
                    if (ratioList.getJSONObject(i).getString("name").equals("公司车主")) {
                        pcCustomerNum = ratioList.getJSONObject(i).getInteger("value");
                    }
                }
                Sql.Builder builder = Sql.instance().select()
                        .from(TPorscheDeliverInfo.class)
                        .where("subject_type_name", "=", "公司")
                        .and("deliver_date", "=", date)
                        .and("shop_id", "=", shopId);
                Sql sql = StringUtils.isEmpty(e.getStyleId()) ? builder.end() : builder.and("car_style", "=", e.getStyleId()).end();
                int appCustomerNum = ONE_PIECE_FACTORY.create(sql).size();
                CommonUtil.valueView(pcCustomerNum, appCustomerNum);
                Preconditions.checkArgument(pcCustomerNum <= appCustomerNum, "昨日" + e.getName() + "公司车主数为：" + pcCustomerNum + " 昨日app该车系公司客户交车数量为：" + appCustomerNum);
                CommonUtil.logger(e.getName());
            }
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
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
                    Sql.Builder builder = Sql.instance().select()
                            .from(TPorscheDeliverInfo.class)
                            .where("deliver_date", "=", date)
                            .and("address", "like", "%" + province + "%")
                            .and("shop_id", "=", shopId);
                    Sql sql = StringUtils.isEmpty(e.getStyleId()) ? builder.end() : builder.and("car_style", "=", e.getStyleId()).end();
                    int count = ONE_PIECE_FACTORY.create(sql).size();
                    CommonUtil.valueView(pcCustomerNum, count);
                    Preconditions.checkArgument(count >= pcCustomerNum, "昨日" + province + e.getName() + "交车数为：" + pcCustomerNum + "昨日app该省此车系交车数量为：" + count);
                    CommonUtil.logger(province);
                }
                CommonUtil.logger(e.getName());
            }
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            saveData("存量客户分析--【各时间段+车系筛选】全国各省成交量=【app-销售总监-展厅客户-购车档案】交车日期在该时间段内&购买车系为筛选车系的购车档案数量");
        }
    }

    @Test(description = "存量客户分析--【时间段＋车系】个人车主数量＝＝成交记录中交车时间在【时间段＋车系＋客户类型为个人】的车主的订单数量（手机号时间段内去重）")
    public void stockCustomer_data_15() {
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
                Sql.Builder builder = Sql.instance()
                        .select("distinct(phones)")
                        .from(TPorscheDeliverInfo.class)
                        .where("deliver_date", "=", date)
                        .and("subject_type_name", "=", "个人")
                        .and("shop_id", "=", shopId);
                Sql sql = StringUtils.isEmpty(e.getStyleId()) ? builder.end() : builder.and("car_style", "=", e.getStyleId()).end();
                int count = ONE_PIECE_FACTORY.create(sql).size();
                CommonUtil.valueView(pcCustomerNum, count);
                Preconditions.checkArgument(pcCustomerNum == count, e.getName() + "个人车主数量：" + pcCustomerNum + "手机号去重数量" + count);
                CommonUtil.logger(e.getName());
            }
        } catch (Exception | AssertionError e) {
            e.printStackTrace();
            appendFailReason(e.toString());
        } finally {
            saveData("存量客户分析--【时间段＋车系】个人车主数量＝＝成交记录中交车时间在【时间段＋车系＋客户类型为个人】的车主的订单数量（手机号时间段内去重）");
        }
    }

    @Test(description = "存量客户分析--【时间段＋车系】公司车主数量＝＝成交记录中交车时间在【时间段＋车系＋客户类型为公司】的车主的订单数量（手机号时间段内去重）")
    public void stockCustomer_data_16() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String date = DateTimeUtil.addDayFormat(new Date(), -1);
            for (EnumCarStyle e : EnumCarStyle.values()) {
                int pcCustomerNum = 0;
                CommonUtil.valueView(e.getName());
                IScene scene = Analysis2DealCarOwnerScene.builder().cycleType(EnumFindType.DAY.getType()).carType(e.getStyleId()).build();
                JSONArray ratioList = crm.invokeApi(scene).getJSONArray("ratio_list");
                for (int i = 0; i < ratioList.size(); i++) {
                    if (ratioList.getJSONObject(i).getString("name").equals("公司车主")) {
                        pcCustomerNum = ratioList.getJSONObject(i).getInteger("value");
                    }
                }
                Sql.Builder builder = Sql.instance()
                        .select("distinct(phones)")
                        .from(TPorscheDeliverInfo.class)
                        .where("deliver_date", "=", date)
                        .and("subject_type_name", "=", "公司")
                        .and("shop_id", "=", shopId);
                Sql sql = StringUtils.isEmpty(e.getStyleId()) ? builder.end() : builder.and("car_style", "=", e.getStyleId()).end();
                int count = ONE_PIECE_FACTORY.create(sql).size();
                CommonUtil.valueView(pcCustomerNum, count);
                Preconditions.checkArgument(pcCustomerNum == count, e.getName() + "公司车主数量：" + pcCustomerNum + "手机号去重数量" + count);
                CommonUtil.logger(e.getName());
            }
        } catch (Exception | AssertionError e) {
            e.printStackTrace();
            appendFailReason(e.toString());
        } finally {
            saveData("存量客户分析--【时间段＋车系】公司车主数量＝＝成交记录中交车时间在【时间段＋车系＋客户类型为公司】的车主的订单数量（手机号时间段内去重）");
        }
    }

//    --------------------------------------------------订单客户分析-------------------------------------------------------

    @Test(description = "订单客户分析--【各时间段+各车型筛选】个人车主百分比+公司车主百分比=100% 或 0%")
    public void orderCustomer_data_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            for (EnumFindType a : EnumFindType.values()) {
                for (EnumCarStyle e : EnumCarStyle.values()) {
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
            appendFailReason(e.toString());
        } finally {
            saveData("订单客户分析--【各时间段+各车型筛选】个人车主百分比+公司车主百分比=100% 或 0%");
        }
    }

    @Test(description = "订单客户分析--【各时间段+各车型筛选】个人车主百分比=个人车主数量/（个人+公司车主数量）")
    public void orderCustomer_data_2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Arrays.stream(EnumFindType.values()).forEach(a -> Arrays.stream(EnumCarStyle.values()).forEach(b -> {
                CommonUtil.valueView(b.getName() + a.getName());
                IScene scene = Analysis2OrderCarOwnerScene.builder().carType(b.getStyleId()).cycleType(a.getType()).build();
                JSONArray ratioList = crm.invokeApi(scene).getJSONArray("ratio_list");
                List<Integer> num1List = getValue(ratioList, "name", "个人车主", "value").stream().map(n -> (Integer) n).collect(Collectors.toList());
                List<String> percentStrList = getValue(ratioList, "name", "个人车主", "percent_str").stream().map(n -> (String) n).collect(Collectors.toList());
                List<Integer> num2List = getValue(ratioList, "name", "公司车主", "value").stream().map(n -> (Integer) n).collect(Collectors.toList());
                for (int i = 0; i < num1List.size(); i++) {
                    String result = CommonUtil.getPercent(num1List.get(i), num1List.get(i) + num2List.get(i), 3).replace("%", "");
                    String percentStr = percentStrList.get(i).replace("%", "");
                    CommonUtil.valueView(result, percentStr);
                    Preconditions.checkArgument(compareData(result, percentStr), b.getName() + a.getName() + "个人车主计算百分比为：" + result + "界面展示为：" + percentStr);
                    CommonUtil.logger(b.getName());
                }
            }));
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("订单客户分析--【各时间段+各车型筛选】个人车主百分比=个人车主数量/（个人+公司车主数量）");
        }
    }

    @Test(description = "订单客户分析--【各时间段+各车型筛选】公司车主百分比=公司车主数量/（个人+公司车主数量）")
    public void orderCustomer_data_3() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Arrays.stream(EnumFindType.values()).forEach(a -> Arrays.stream(EnumCarStyle.values()).forEach(b -> {
                CommonUtil.valueView(b.getName() + a.getName());
                IScene scene = Analysis2OrderCarOwnerScene.builder().carType(b.getStyleId()).cycleType(a.getType()).build();
                JSONArray ratioList = crm.invokeApi(scene).getJSONArray("ratio_list");
                List<Integer> num1List = getValue(ratioList, "name", "公司车主", "value").stream().map(n -> (Integer) n).collect(Collectors.toList());
                List<String> percentStrList = getValue(ratioList, "name", "公司车主", "percent_str").stream().map(n -> (String) n).collect(Collectors.toList());
                List<Integer> num2List = getValue(ratioList, "name", "个人车主", "value").stream().map(n -> (Integer) n).collect(Collectors.toList());
                for (int i = 0; i < num1List.size(); i++) {
                    String result = CommonUtil.getPercent(num1List.get(i), num1List.get(i) + num2List.get(i), 3).replace("%", "");
                    String percentStr = percentStrList.get(i).replace("%", "");
                    CommonUtil.valueView(result, percentStr);
                    Preconditions.checkArgument(compareData(result, percentStr), b.getName() + a.getName() + "公司车主计算百分比为：" + result + "界面展示为：" + percentStr);
                    CommonUtil.logger(b.getName());
                }
            }));
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("订单客户分析--【各时间段+各车型筛选】公司车主百分比=公司车主数量/（个人+公司车主数量）");
        }
    }

    public List<Object> getValue(JSONArray list, String key, String value, String field) {
        return list.stream().map(object -> (JSONObject) object).filter(object -> object.getString(key).equals(value))
                .map(object -> object.get(field)).collect(Collectors.toList());
    }

    public boolean compareData(String a, String b) {
        return Double.parseDouble(b) <= Double.parseDouble(a) + 0.1 || Double.parseDouble(b) >= Double.parseDouble(a) - 0.1;
    }

    @Test(description = "订单客户分析--【各时间段+各车型筛选】车主年龄分析 各年龄段之和=100%或 0%")
    public void orderCustomer_data_4() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            for (EnumFindType a : EnumFindType.values()) {
                for (EnumCarStyle e : EnumCarStyle.values()) {
                    double percentageSum = 0;
                    CommonUtil.valueView(e.getName() + a.getName());
                    IScene scene = Analysis2OrderGenderAgeScene.builder().carType(e.getStyleId()).cycleType(a.getType()).build();
                    JSONArray list = crm.invokeApi(scene).getJSONObject("age").getJSONArray("list");
                    for (int i = 0; i < list.size(); i++) {
                        String percentageStr = list.getJSONObject(i).getString("percentage_str");
                        double percentage = Double.parseDouble(percentageStr.substring(0, percentageStr.length() - 1));
                        String age = list.getJSONObject(i).getString("age");
                        CommonUtil.valueView(age + ":" + percentage);
                        percentageSum += percentage;
                    }
                    CommonUtil.valueView("总百分比" + percentageSum);
                    Preconditions.checkArgument((percentageSum <= 101 && percentageSum >= 99) || percentageSum == 0, a.getName() + e.getName() + "各年龄段百分比之和为：" + percentageSum + "%");
                    CommonUtil.logger(e.getName());
                }
            }
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            saveData("订单客户分析--【各时间段+各车型筛选】车主年龄分析 各年龄段之和=100%或 0%");
        }
    }

    @Test(description = "订单客户分析--【各时间段+各车型筛选】车主性别分析 性别之和=100%或 0%")
    public void orderCustomer_data_5() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            for (EnumFindType a : EnumFindType.values()) {
                for (EnumCarStyle e : EnumCarStyle.values()) {
                    double percentageSum = 0;
                    CommonUtil.valueView(e.getName() + a.getName());
                    IScene scene = Analysis2OrderGenderAgeScene.builder().carType(e.getStyleId()).cycleType(a.getType()).build();
                    JSONArray list = crm.invokeApi(scene).getJSONObject("gender").getJSONArray("list");
                    for (int i = 0; i < list.size(); i++) {
                        String percentageStr = list.getJSONObject(i).getString("percentage_str");
                        double percentage = Double.parseDouble(percentageStr.substring(0, percentageStr.length() - 1));
                        String gender = list.getJSONObject(i).getString("gender");
                        CommonUtil.valueView(gender + ":" + percentage);
                        percentageSum += percentage;
                    }
                    CommonUtil.valueView("总百分比" + percentageSum);
                    Preconditions.checkArgument(percentageSum == 100 || percentageSum == 0, e.getName() + a.getName() + "性别百分比之和为：" + percentageSum);
                    CommonUtil.logger(e.getName());
                }
            }
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            saveData("订单客户分析--【各时间段+各车型筛选】车主性别分析 性别之和=100%或 0%");
        }
    }

    @Test(description = "订单客户分析--【各时间段】全国各省成交量，某个省的百分比=该省成交量/各省成交量之和")
    public void orderCustomer_data_6() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            for (EnumFindType a : EnumFindType.values()) {
                for (EnumCarStyle e : EnumCarStyle.values()) {
                    CommonUtil.valueView(e.getName() + a.getName());
                    IScene scene = Analysis2OrderWholeCountryScene.builder().carType(e.getStyleId()).cycleType(a.getType()).build();
                    JSONArray list = crm.invokeApi(scene).getJSONArray("list");
                    int totalValue = 0;
                    for (int i = 0; i < list.size(); i++) {
                        totalValue += list.getJSONObject(i).getInteger("value");
                    }
                    for (int i = 0; i < list.size(); i++) {
                        String district = list.getJSONObject(i).getString("district");
                        int value = list.getJSONObject(i).getInteger("value");
                        String percentageStr = list.getJSONObject(i).getString("percentage_str");
                        CommonUtil.valueView(district + "数量：" + value + "，占比：" + percentageStr);
                        String percentage = CommonUtil.getPercent(value, totalValue, 4);
                        CommonUtil.valueView(district + "计算占比：" + percentage);
                        Preconditions.checkArgument(percentageStr.equals(percentage), e.getName() + a.getName() + district + "界面显示占比：" + percentageStr + "，计算占比：" + percentage);
                    }
                    CommonUtil.logger(e.getName());
                }
            }
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            saveData("订单客户分析--【各时间段】全国各省成交量，某个省的百分比=该省成交量/各省成交量之和");
        }
    }

    @Test(description = "订单客户分析--【各时间段】全国各省成交量，各省百分比之和=100%")
    public void orderCustomer_data_7() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            for (EnumFindType a : EnumFindType.values()) {
                for (EnumCarStyle e : EnumCarStyle.values()) {
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
            appendFailReason(e.toString());
        } finally {
            saveData("订单客户分析--【各时间段】全国各省成交量，各省百分比之和=100%");
        }
    }

    @Test(description = "订单客户分析--【各时间段筛选】苏州各区成交量，某个区的百分比=该区成交量/各区成交量之和")
    public void orderCustomer_data_8() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            for (EnumFindType a : EnumFindType.values()) {
                for (EnumCarStyle b : EnumCarStyle.values()) {
                    int totalValue = 0;
                    CommonUtil.valueView(b.getName() + a.getName());
                    IScene scene = Analysis2OrderCityScene.builder().adCode(320500).carType(b.getStyleId()).cycleType(a.getType()).build();
                    JSONArray list = crm.invokeApi(scene).getJSONArray("list");
                    for (int i = 0; i < list.size(); i++) {
                        int value = list.getJSONObject(i).getInteger("value");
                        totalValue += value;
                    }
                    for (int i = 0; i < list.size(); i++) {
                        String district = list.getJSONObject(i).getString("district");
                        int value = list.getJSONObject(i).getInteger("value");
                        String percentageStr = list.getJSONObject(i).getString("percentage_str");
                        CommonUtil.valueView(district + "数量：" + value + "，占比：" + percentageStr);
                        String percentage = CommonUtil.getPercent(value, totalValue, 4);
                        CommonUtil.valueView(district + "计算占比：" + percentage);
                        Preconditions.checkArgument(percentage.equals(percentageStr), b.getName() + a.getName() + district + "接口返回占比：" + percentageStr + "，计算占比：" + percentage);
                    }
                    CommonUtil.logger(b.getName());
                }
            }
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            saveData("订单客户分析--【各时间段筛选】苏州各区成交量，某个区的百分比=该区成交量/各区成交量之和");
        }
    }

    @Test(description = "订单客户分析--【各时间段筛选】苏州各区成交量,各区百分比之和=100%")
    public void orderCustomer_data_9() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            for (EnumFindType a : EnumFindType.values()) {
                for (EnumCarStyle b : EnumCarStyle.values()) {
                    CommonUtil.valueView(b.getName() + a.getName());
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
            appendFailReason(e.toString());
        } finally {
            saveData("订单客户分析--【各时间段筛选】苏州各区成交量,各区百分比之和=100%");
        }
    }

    @Test(description = "订单客户分析--苏州各区成交量之和<=江苏成交量")
    public void orderCustomer_data_10() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            for (EnumFindType a : EnumFindType.values()) {
                for (EnumCarStyle b : EnumCarStyle.values()) {
                    CommonUtil.valueView(b.getName() + a.getName());
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
            appendFailReason(e.toString());
        } finally {
            saveData("订单客户分析--苏州各区成交量之和<=江苏成交量");
        }
    }

    @Test(description = "订单客户分析--订单存量图，销售数量=PC角色管理中销售顾问数量", enabled = false)
    public void orderCustomer_data_11() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            int listSize = method.getSaleListByRoleName("销售顾问").size() - 1;
            for (EnumFindType b : EnumFindType.values()) {
                for (EnumCarStyle a : EnumCarStyle.values()) {
                    CommonUtil.valueView(a.getName() + b.getName());
                    IScene scene = Analysis2OrderSaleListScene.builder().carType(a.getStyleId()).cycleType(b.getType()).build();
                    JSONArray list = crm.invokeApi(scene).getJSONArray("list");
                    CommonUtil.valueView(listSize, list.size());
                    Preconditions.checkArgument(listSize >= list.size(), a.getName() + b.getName() + "订单存量图销售数量为：" + list.size() + " 角色管理中销售数量为：" + listSize);
                    CommonUtil.logger(a.getName());
                }
            }
        } catch (Exception | AssertionError e) {
            e.printStackTrace();
            appendFailReason(e.toString());
        } finally {
            saveData("订单客户分析--订单存量图，销售数量=PC角色管理中销售顾问数量");
        }
    }

    @Test(description = "订单客户分析--订单存量图，柱状图1数据>= 柱状图2数据>=柱状图3数据..")
    public void orderCustomer_data_12() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            for (EnumFindType b : EnumFindType.values()) {
                for (EnumCarStyle a : EnumCarStyle.values()) {
                    CommonUtil.valueView(a.getName() + b.getName());
                    IScene scene = Analysis2OrderSaleListScene.builder().carType(a.getStyleId()).cycleType(b.getType()).build();
                    JSONArray list = crm.invokeApi(scene).getJSONArray("list");
                    for (int i = 0; i < list.size() - 1; i++) {
                        int value1 = list.getJSONObject(i).getInteger("value");
                        int value2 = list.getJSONObject(i + 1).getInteger("value");
                        CommonUtil.valueView("[" + i + "]：" + value1 + "    [" + (i + 1) + "]：" + value2);
                        Preconditions.checkArgument(value1 >= value2, a.getName() + b.getName() + "第" + i + "名销售的订单存量为：" + value1 + "，第" + (i + 1) + "名销售的订单存量为：" + value2);
                    }
                    CommonUtil.logger(a.getName());
                }
            }
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
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
                Sql.Builder builder = Sql.instance()
                        .select().from(TPorscheOrderInfo.class)
                        .where("order_date", "=", date)
                        .and("subject_type_name", "=", "个人")
                        .and("shop_id", "=", shopId);
                Sql sql = StringUtils.isEmpty(e.getStyleId()) ? builder.end() : builder.and("car_style", "=", e.getStyleId()).end();
                int count = ONE_PIECE_FACTORY.create(sql).size();
                CommonUtil.valueView(value, count);
                Preconditions.checkArgument(count >= value, e.getName() + EnumFindType.DAY.getName() + "pc端个人车主数量为：" + value + " app订车数量为：" + count);
                CommonUtil.logger(e.getName());
            }
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
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
                Sql.Builder builder = Sql.instance()
                        .select().from(TPorscheOrderInfo.class)
                        .where("order_date", "=", date)
                        .and("subject_type_name", "=", "公司")
                        .and("shop_id", "=", shopId);
                Sql sql = StringUtils.isEmpty(e.getStyleId()) ? builder.end() : builder.and("car_style", "=", e.getStyleId()).end();
                int count = ONE_PIECE_FACTORY.create(sql).size();
                CommonUtil.valueView(value, count);
                Preconditions.checkArgument(count >= value, e.getName() + EnumFindType.DAY.getName() + "pc端个公司主数量为：" + value + " app接待数量为：" + count);
                CommonUtil.logger(e.getName());
            }
        } catch (Exception | AssertionError e) {
            e.printStackTrace();
            appendFailReason(e.toString());
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
                    Sql.Builder builder = Sql.instance()
                            .select().from(TPorscheOrderInfo.class)
                            .where("order_date", "=", date)
                            .and("address", "like", "%" + province + "%")
                            .and("shop_id", "=", shopId);
                    Sql sql = StringUtils.isEmpty(e.getStyleId()) ? builder.end() : builder.and("car_style", "=", e.getStyleId()).end();
                    int count = new Factory.Builder().container(EnumContainer.ONE_PIECE.getContainer()).build().create(sql).size();
                    CommonUtil.valueView(pcCustomerNum, count);
                    Preconditions.checkArgument(count >= pcCustomerNum, "昨日" + province + e.getName() + "交车数为：" + pcCustomerNum + " 昨日app该省此车系交车数量为：" + count);
                    CommonUtil.logger(province);
                }
                CommonUtil.logger(e.getName());
            }
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            saveData("存量客户分析--【各时间段+车系筛选】全国各省成交量=【app-销售总监-展厅客户-购车档案】交车日期在该时间段内&购买车系为筛选车系的购车档案数量");
        }
    }

    @Test(description = "展厅热区管理--试乘试驾次数：= （时间段内）【APP-销售总监-我的试驾】审核通过&未取消的数量一致")
    public void exhibitionHall_data_1() {
        logger.logCaseStart(caseResult.getCaseName());
        String date = DateTimeUtil.addDayFormat(new Date(), -1);
        try {
            for (EnumCarStyle e : EnumCarStyle.values()) {
                if (StringUtils.isEmpty(e.getStyleId())) {
                    continue;
                }
                String carStyleName = e.getName();
                CommonUtil.valueView(carStyleName);
                int driver = 0;
                int appDriver = 0;
                IScene scene = Analysis2SkuRankScene.builder().cycleType(EnumFindType.DAY.getType()).build();
                JSONArray list = crm.invokeApi(scene).getJSONArray("list");
                for (int i = 0; i < list.size(); i++) {
                    if (list.getJSONObject(i).getString("sku_name").equals(carStyleName))
                        driver = list.getJSONObject(i).getInteger("drive");
                }
                int total = crm.testDriverAppList("", date, date, 10, 1).getInteger("total");
                int s = CommonUtil.getTurningPage(total, size);
                for (int i = 1; i < s; i++) {
                    JSONArray appList = crm.testDriverAppList("", date, date, size, i).getJSONArray("list");
                    for (int j = 0; j < appList.size(); j++) {
                        if (appList.getJSONObject(j).getString("audit_status_name").equals("已通过")
                                && appList.getJSONObject(j).getString("test_car_style_name").equals(carStyleName)) {
                            appDriver++;
                        }
                    }
                }
                CommonUtil.valueView(driver, appDriver);
                Preconditions.checkArgument(driver == appDriver, "昨日app记录" + carStyleName + "试驾数：" + appDriver + " 昨日展厅热区分析" + carStyleName + "试乘试驾数：" + driver);
                CommonUtil.logger(carStyleName);
            }
        } catch (Exception | AssertionError e) {
            e.printStackTrace();
            appendFailReason(e.toString());
        } finally {
            saveData("展厅热区管理--试乘试驾次数：= （时间段内）【APP-销售总监-我的试驾】审核通过&未取消的数量一致");
        }
    }

    @Test(description = "展厅热区管理--成交数量=（时间段内）【APP-销售总监-我的交车】订单为维度去重数量一致")
    public void exhibitionHall_data_2() {
        logger.logCaseStart(caseResult.getCaseName());
        String date = DateTimeUtil.addDayFormat(new Date(), -1);
        try {
            for (EnumCarStyle e : EnumCarStyle.values()) {
                if (StringUtils.isEmpty(e.getStyleId())) {
                    continue;
                }
                String carStyleName = e.getName();
                CommonUtil.valueView(carStyleName);
                int dealNum = 0;
                IScene scene = Analysis2SkuRankScene.builder().cycleType(EnumFindType.DAY.getType()).build();
                JSONArray list = crm.invokeApi(scene).getJSONArray("list");
                for (int i = 0; i < list.size(); i++) {
                    if (list.getJSONObject(i).getString("sku_name").equals(carStyleName))
                        dealNum = list.getJSONObject(i).getInteger("deal_num");
                }
                Sql sql = Sql.instance().select()
                        .from(TPorscheOrderInfo.class)
                        .where("shop_id", "=", shopId)
                        .and("car_style", "=", e.getStyleId())
                        .and("order_date", "=", date).end();
                int count = ONE_PIECE_FACTORY.create(sql).size();
                CommonUtil.valueView(dealNum, count);
                Preconditions.checkArgument(dealNum == count, "昨日app记录" + carStyleName + "成交量：" + count + " 昨日展厅热区分析" + carStyleName + "成交率数：" + dealNum);
                CommonUtil.logger(e.getName());
            }
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            saveData("展厅热区管理--成交数量： = （时间段内）【APP-销售总监-我的交车】订单为维度去重数量一致");
        }
    }

    @Test(description = "展厅热区管理--下拉框内数量=【PC-车系管理】车系数量")
    public void exhibitionHall_data_3() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray list = crm.pcCarStyleList().getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                String name = list.getJSONObject(i).getString("car_style_name");
                String result = EnumCarStyle.getValue(name);
                CommonUtil.valueView(name, result);
                Preconditions.checkArgument(name.equals(result), name + "车系不存在于" + EnumCarStyle.class.getName() + "中");
            }
        } catch (Exception | AssertionError e) {
            e.printStackTrace();
            appendFailReason(e.toString());
        } finally {
            saveData("展厅热区管理--下拉框内数量=【PC-车系管理】车系数量");
        }
    }

    @Test(description = "展厅热区管理--变更记录--变更一次，记录+1")
    public void exhibitionHall_data_4() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene scene = CarStyleChangeListScene.builder().build();
            int total = crm.invokeApi(scene).getInteger("total");
            JSONArray list = crm.regionMap(EnumFindType.DAY.getType()).getJSONArray("list");
            int regionId = 0;
            for (int i = 0; i < list.size(); i++) {
                regionId = list.getJSONObject(i).getInteger("region_id");
            }
            crm.carStyleEdit(regionId, Integer.parseInt(EnumCarStyle.PANAMERA.getStyleId()));
            int total1 = crm.invokeApi(scene).getInteger("total");
            CommonUtil.valueView(total, total1);
            Preconditions.checkArgument(total1 == total + 1, "变更之前变更记录数：" + total + "变更之后变更记录数：" + total1);
        } catch (Exception | AssertionError e) {
            e.printStackTrace();
            appendFailReason(e.toString());
        } finally {
            saveData("展厅热区管理--变更记录--变更一次，记录+1");
        }
    }

    @Test(description = "展厅热区管理--环比各时间段批次数<=【PC-接待列表】接待时间为前天的各时间段数量")
    public void potentialCustomer_data_1() {
        logger.logCaseStart(caseResult.getCaseName());
        String date = DateTimeUtil.addDayFormat(new Date(), -2);
        try {
            List<Map<String, String>> list = method.getSaleListByRoleName("销售顾问");
            list.forEach(e -> {
                IScene scene = Analysis2PotentialTrendScene.builder().saleId(e.get("userId")).cycleType(EnumFindType.DAY.getType()).build();
                JSONArray array = crm.invokeApi(scene).getJSONArray("list");
                for (int i = 0; i < array.size(); i++) {
                    String timeStrStart = array.getJSONObject(i).getString("time_str");
                    String timeStrEnd = array.size() - 1 > i ? array.getJSONObject(i + 1).getString("time_str") : "24:00";
                    String time = " " + timeStrStart + "--" + timeStrEnd;
                    CommonUtil.valueView(e.get("userName") + time);
                    //环比
                    Integer value = array.getJSONObject(i).getJSONArray("list").getInteger(2);
                    int result = value == null ? 0 : value;
                    Sql.Builder builder = Sql.instance()
                            .select().from(TPorscheReceptionData.class)
                            .where("shop_id", "=", shopId)
                            .and("reception_date", "=", date)
                            .and("reception_start_time", ">=", timeStrStart)
                            .and("reception_start_time", "<", timeStrEnd);
                    Sql sql = StringUtils.isEmpty(e.get("userId")) ? builder.end() : builder.and("reception_sale_id", "=", e.get("userId")).end();
                    int count = ONE_PIECE_FACTORY.create(sql).size();
                    CommonUtil.valueView("环比：" + result, "前天接待数：" + count);
                    if (count != 0) {
                        Preconditions.checkArgument(result != 0, e.get("userName") + time + "时间段环比数：" + result + " 前天接待数：" + count);
                    }
                    Preconditions.checkArgument(count >= result, e.get("userName") + time + "时间段环比数：" + result + " 前天接待数：" + count);
                    CommonUtil.logger(timeStrStart);
                }
                CommonUtil.logger(e.get("userName"));
            });
        } catch (Exception | AssertionError e) {
            e.printStackTrace();
            appendFailReason(e.toString());
        } finally {
            saveData("展厅热区管理--环比各时间段批次数<=【PC-接待列表】接待时间为前天的各时间段数量");
        }
    }

    @Test(description = "展厅热区管理--本期各时间段批次数<=【PC-接待列表】接待时间为昨天的各时间段数量")
    public void potentialCustomer_data_2() {
        logger.logCaseStart(caseResult.getCaseName());
        String date = DateTimeUtil.addDayFormat(new Date(), -1);
        try {
            List<Map<String, String>> list = method.getSaleListByRoleName("销售顾问");
            list.forEach(e -> {
                IScene scene = Analysis2PotentialTrendScene.builder().saleId(e.get("userId")).cycleType(EnumFindType.DAY.getType()).build();
                JSONArray array = crm.invokeApi(scene).getJSONArray("list");
                for (int i = 0; i < array.size(); i++) {
                    String timeStrStart = array.getJSONObject(i).getString("time_str");
                    String timeStrEnd = array.size() - 1 > i ? array.getJSONObject(i + 1).getString("time_str") : "24:00";
                    String time = " " + timeStrStart + "--" + timeStrEnd;
                    CommonUtil.valueView(e.get("userName") + time);
                    //环比
                    Integer value = array.getJSONObject(i).getJSONArray("list").getInteger(0);
                    int result = value == null ? 0 : value;
                    Sql.Builder builder = Sql.instance()
                            .select().from(TPorscheReceptionData.class)
                            .where("shop_id", "=", shopId)
                            .and("reception_date", "=", date)
                            .and("reception_start_time", ">=", timeStrStart)
                            .and("reception_start_time", "<", timeStrEnd);
                    Sql sql = StringUtils.isEmpty(e.get("userId")) ? builder.end() : builder.and("reception_sale_id", "=", e.get("userId")).end();
                    int count = ONE_PIECE_FACTORY.create(sql).size();
                    CommonUtil.valueView("环比：" + result, "昨天接待数：" + count);
                    if (count != 0) {
                        Preconditions.checkArgument(result != 0, e.get("userName") + time + "时间段本期数：" + result + " 昨天接待数：" + count);
                    }
                    Preconditions.checkArgument(count >= result, e.get("userName") + time + "时间段本期数：" + result + " 昨天接待数：" + count);
                    CommonUtil.logger(timeStrStart);
                }
                CommonUtil.logger(e.get("userName"));
            });
        } catch (Exception | AssertionError e) {
            e.printStackTrace();
            appendFailReason(e.toString());
        } finally {
            saveData("展厅热区管理--本期各时间段批次数<=【PC-接待列表】接待时间为昨天的各时间段数量");
        }
    }

    @Test(description = "店面数据分--渠道来源分析--各类型百分比之和=100%")
    public void sourceChannel_data_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            List<SaleInfo> list = method.getSaleList("销售顾问");
            list.forEach(a -> Arrays.stream(EnumFindType.values()).forEach(b -> {
                List<SourceChannel> sourceChannelList = getSourceChannel(a.getUserId(), b.name());
                double percent = sourceChannelList.stream().mapToDouble(SourceChannel::getPercentage).sum();
                Preconditions.checkArgument(0.09 <= percent || percent <= 1.01 || percent == (double) 0, a.getUserName() + CommonUtil.checkResult("1或者0", percent));
                CommonUtil.valueView(a.getUserName() + b.getName(), percent);
                CommonUtil.logger(a.getUserName());
            }));
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("店面数据分--渠道来源分析--各类型百分比之和=100%");
        }
    }

    @Test(description = "店面数据分--渠道来源分析--各类型百分比=该类数量/所有类型数量和")
    public void sourceChannel_data_2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            List<SaleInfo> list = method.getSaleList("销售顾问");
            list.forEach(a -> Arrays.stream(EnumFindType.values()).forEach(b -> {
                List<SourceChannel> sourceChannelList = getSourceChannel(a.getUserId(), b.name());
                int valueSum = sourceChannelList.stream().mapToInt(SourceChannel::getValue).sum();
                List<String> calculateList = sourceChannelList.stream().map(e -> CommonUtil.getPercent(e.getValue(), valueSum, 3)).collect(Collectors.toList());
                List<String> percentList = sourceChannelList.stream().map(SourceChannel::getPercent).collect(Collectors.toList());
                CommonUtil.valueView(calculateList, percentList);
                for (int i = 0; i < calculateList.size(); i++) {
                    String calculate = calculateList.get(i).replace("%", "");
                    String percent = percentList.get(i).replace("%", "");
                    Preconditions.checkArgument(compareData(calculate, percent), a.getUserName() + b.getName() + "计算结果为：" + calculateList.get(i) + "页面展示为：" + percentList.get(i));
                }
                CommonUtil.logger(a.getUserName());
            }));
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("店面数据分--渠道来源分析--各类型百分比=该类数量/所有类型数量和");
        }
    }

    @Test(description = "店面数据分--渠道来源分析--【日】各个渠道的数量=昨日接待客户档案中各个渠道的数量和")
    public void sourceChannel_data_3() {
        logger.logCaseStart(caseResult.getCaseName());
        String date = DateTimeUtil.addDayFormat(new Date(), -1);
        try {
            Sql sql = Sql.instance().select().from(TPorscheReceptionData.class)
                    .where("shop_id", "=", shopId)
                    .and("reception_date", "=", date)
                    .and("reception_sale", "is not", null).end();
            List<TPorscheReceptionData> receptionDataList = ONE_PIECE_FACTORY.create(sql, TPorscheReceptionData.class);
            CommonUtil.valueView(receptionDataList);
            List<Integer> customerIdList = receptionDataList.stream().map(TPorscheReceptionData::getCustomerId).collect(Collectors.toList());
            Arrays.stream(EnumChannelName.values()).forEach(channelName -> {
                CommonUtil.valueView(channelName.getName());
                int value = getSourceChannel(null, EnumFindType.DAY.name()).stream().filter(e -> e.getChannelName().equals(channelName.getName())).map(SourceChannel::getValue).collect(Collectors.toList()).get(0);
                int sum = (int) customerIdList.stream().filter(customerId -> getChannelName(customerId).equals(channelName.getName())).count();
                CommonUtil.valueView("页面" + channelName.getName() + "数量：" + value, channelName.getName() + "数量：" + sum);
                Preconditions.checkArgument(value == sum, channelName.getName() + "页面展示：" + value + "昨日接待客户中线上垂媒数量：" + sum);
                CommonUtil.logger(channelName.getName());
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("店面数据分--渠道来源分析--【日】各个渠道的数量=昨日接待客户档案中各个渠道的数量和");
        }
    }

    private String getChannelName(Integer customerId) {
        IScene scene = CustomerInfoScene.builder().customerId(String.valueOf(customerId)).build();
        String channelName = crm.invokeApi(scene).getString("channel_name");
        return channelName == null ? "" : channelName;
    }

    @Test(description = "店面数据分--渠道来源分析--【日】各类型数量和<=昨日接待客户总数")
    public void sourceChannel_data_8() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String date = DateTimeUtil.addDayFormat(new Date(), -1);
            Sql sql = Sql.instance().select().from(TPorscheReceptionData.class)
                    .where("shop_id", "=", shopId)
                    .and("reception_date", "=", date)
                    .and("reception_sale_id", "is not", null)
                    .end();
            List<TPorscheReceptionData> receptionDataList = ONE_PIECE_FACTORY.create(sql, TPorscheReceptionData.class);
            CommonUtil.valueView(receptionDataList);
            int pcSum = getSourceChannel(null, EnumFindType.DAY.name()).stream().mapToInt(SourceChannel::getValue).sum();
            CommonUtil.valueView(pcSum, receptionDataList.size());
            Preconditions.checkArgument(pcSum <= receptionDataList.size(), "各类型数量和：" + pcSum + "昨日接待客户总数：" + receptionDataList.size());
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("店面数据分--渠道来源分析--【日】各类型数量和<=昨日接待客户总数");
        }
    }

    private List<SourceChannel> getSourceChannel(String saleId, String cycleType) {
        IScene scene = SourceChannelScene.builder().saleId(saleId).cycleType(cycleType).build();
        JSONArray array = crm.invokeApi(scene).getJSONArray("list");
        return array.stream().map(e -> (JSONObject) e).map(e -> JSON.parseObject(JSON.toJSONString(e), SourceChannel.class)).collect(Collectors.toList());
    }
}
