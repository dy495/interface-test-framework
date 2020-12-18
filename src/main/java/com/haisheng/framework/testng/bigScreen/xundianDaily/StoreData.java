package com.haisheng.framework.testng.bigScreen.xundianDaily;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumChecklistUser;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumJobName;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumShopId;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumTestProduce;
import com.haisheng.framework.testng.bigScreen.crm.wm.scene.IScene;
import com.haisheng.framework.testng.bigScreen.xundianDaily.enumerator.EnumAppPageType;
import com.haisheng.framework.testng.bigScreen.xundianDaily.sence.app.*;
import com.haisheng.framework.testng.bigScreen.xundianDaily.sence.pc.HistoryDayTrendPvUv;
import com.haisheng.framework.testng.bigScreen.xundianDaily.sence.pc.PassengerFlow;
import com.haisheng.framework.testng.bigScreen.xundianDaily.sence.pc.RealTimeShopPvUv;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.util.CommonUtil;
import com.haisheng.framework.util.DateTimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * 有问题处请@wangmin
 */
public class StoreData extends TestCaseCommon implements TestCaseStd {
    public static final Logger log = LoggerFactory.getLogger(StoreData.class);
    public static final int size = 100;
    StoreScenarioUtil md = StoreScenarioUtil.getInstance();

    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before classs initial");
        CommonConfig commonConfig = new CommonConfig();
        //replace checklist app id and conf id
        commonConfig.checklistAppId = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_MENDIAN_DAILY_SERVICE;
        commonConfig.checklistQaOwner = EnumChecklistUser.QQ.getName();
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.XUNDIAN_DAILY_TEST.getJobName());

        //replace product name for ding push
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, EnumTestProduce.MENDIAN_DAILY.getName());
        commonConfig.dingHook = DingWebhook.DAILY_STORE_MANAGEMENT_PLATFORM_GRP;
        commonConfig.pushRd = new String[]{"15898182672", "18513118484", "18810332354", "15084928847"};
        commonConfig.shopId = EnumShopId.XUNDIAN_DAILY.getShopId();
        beforeClassInit(commonConfig);
        logger.debug("store " + md);
        md.login("yuexiu@test.com", "f5b3e737510f31b88eb2d4b5d0cd2fb4");
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

    @Test(description = "权限下全部门店今日到访人数<=权限全部门店的趋势图中今日各时刻中人数之和")
    public void homePage_data_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene scene = CardList.builder().pageType(EnumAppPageType.HOME_BELOW.name()).build();
            JSONObject total = md.invokeApi(scene).getJSONArray("list").getJSONObject(0);
            int todayUv = total.getJSONObject("total_number").getInteger("today_uv");
            JSONArray trendList = total.getJSONArray("trend_list");
            int sumTodayUv = trendList.stream().map(e -> (JSONObject) e).mapToInt(jsonObject -> jsonObject.getInteger("today_uv") == null ? 0 : jsonObject.getInteger("today_uv")).sum();
            CommonUtil.valueView(todayUv, sumTodayUv);
            Preconditions.checkArgument(todayUv <= sumTodayUv, "全部门店今日到访人数：" + todayUv + " 全部门店趋势图今日各时段中人数之和" + sumTodayUv);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("权限下全部门店今日到访人数<=权限全部门店的趋势图中今日各时刻中人数之和");
        }
    }

    @Test(description = "权限下全部门店今日到访人次==权限下全部门店趋势图今日各时段中人次之和")
    public void homePage_data_2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene scene = CardList.builder().pageType(EnumAppPageType.HOME_BELOW.name()).build();
            JSONObject total = md.invokeApi(scene).getJSONArray("list").getJSONObject(0);
            int todayPv = total.getJSONObject("total_number").getInteger("today_pv");
            JSONArray trendList = total.getJSONArray("trend_list");
            int sumTodayPv = trendList.stream().map(e -> (JSONObject) e).mapToInt(jsonObject -> jsonObject.getInteger("today_pv") == null ? 0 : jsonObject.getInteger("today_pv")).sum();
            CommonUtil.valueView(todayPv, sumTodayPv);
            Preconditions.checkArgument(todayPv == sumTodayPv, "全部门店今日到访人次：" + todayPv + " 全部门店趋势图今日各时段中人次之和" + sumTodayPv);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("权限下全部门店今日到访人次==权限下全部门店趋势图今日各时段中人次之和");
        }
    }

    @Test(description = "权限下全部门店昨日到访人数<=权限下全部门店昨日各时段中人数之和")
    public void homePage_data_3() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene scene = CardList.builder().pageType(EnumAppPageType.HOME_BELOW.name()).build();
            JSONObject total = md.invokeApi(scene).getJSONArray("list").getJSONObject(0);
            int yesterdayUv = total.getJSONObject("total_number").getInteger("yesterday_uv");
            JSONArray trendList = total.getJSONArray("trend_list");
            int sumYesterdayUv = trendList.stream().map(e -> (JSONObject) e).mapToInt(jsonObject -> jsonObject.getInteger("yesterday_uv") == null ? 0 : jsonObject.getInteger("yesterday_uv")).sum();
            CommonUtil.valueView(yesterdayUv, sumYesterdayUv);
            Preconditions.checkArgument(yesterdayUv <= sumYesterdayUv, "全部门店昨日到访人数：" + yesterdayUv + " 全部门店昨日各时段中人数之和" + sumYesterdayUv);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("权限下全部门店昨日到访人数<=权限下全部门店昨日各时段中人数之和");
        }
    }

    @Test(description = "权限下全部门店昨日到访人次==权限下全部门店昨日各时段中人次之和")
    public void homePage_data_4() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene scene = CardList.builder().pageType(EnumAppPageType.HOME_BELOW.name()).build();
            JSONObject total = md.invokeApi(scene).getJSONArray("list").getJSONObject(0);
            int yesterdayPv = total.getJSONObject("total_number").getInteger("yesterday_pv");
            JSONArray trendList = total.getJSONArray("trend_list");
            int sumYesterdayPv = trendList.stream().map(e -> (JSONObject) e).mapToInt(jsonObject -> jsonObject.getInteger("yesterday_pv") == null ? 0 : jsonObject.getInteger("yesterday_pv")).sum();
            CommonUtil.valueView(yesterdayPv, sumYesterdayPv);
            Preconditions.checkArgument(yesterdayPv <= sumYesterdayPv, "全部门店今日到访人次：" + yesterdayPv + " 全部门店趋势图今日各时段中人次之和" + sumYesterdayPv);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("权限下全部门店昨日到访人次==权限下全部门店昨日各时段中人次之和");
        }
    }

    @Test(description = "app权限下【首页】全部门店昨日到访人数==pc权限下【客流分析】各个门店历史客流中截止日期得人数相加的和")
    public void homePage_data_5() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String date = DateTimeUtil.addDayFormat(new Date(), -1);
            IScene scene = CardList.builder().pageType(EnumAppPageType.HOME_BELOW.name()).build();
            JSONObject totalNumber = md.invokeApi(scene).getJSONArray("list").getJSONObject(0).getJSONObject("total_number");
            //首页-昨日到访总人数
            int yesterdayPv = totalNumber.getInteger("yesterday_uv");
            //pc各门店到访人数之和
            List<String> shopIds = getPcShopIds();
            int sumYesterdayPv = shopIds.stream().map(shopId -> HistoryDayTrendPvUv.builder().shopId(shopId).day(date).build()).mapToInt(trendScene -> getTypeSum(trendScene, "yesterday_uv")).sum();
            CommonUtil.valueView(yesterdayPv, sumYesterdayPv);
            Preconditions.checkArgument(yesterdayPv == sumYesterdayPv, "");
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("app权限下【首页】全部门店昨日到访人数==pc权限下【客流分析】各个门店历史客流中截止日期得人数相加的和");
        }
    }

    @Test(description = "app权限下【首页】全部门店昨日到访人次==pc【客流分析】各个门店历史客流中截止日期得人次之和")
    public void homePage_data_6() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String date = DateTimeUtil.addDayFormat(new Date(), -1);
            IScene scene = CardList.builder().pageType(EnumAppPageType.HOME_BELOW.name()).build();
            JSONObject totalNumber = md.invokeApi(scene).getJSONArray("list").getJSONObject(0).getJSONObject("total_number");
            //首页-昨日到访总人次
            int yesterdayPv = totalNumber.getInteger("yesterday_pv");
            //pc各门店到访人次之和
            List<String> shopIds = getPcShopIds();
            int sumYesterdayPv = shopIds.stream().map(shopId -> HistoryDayTrendPvUv.builder().shopId(shopId).day(date).build()).mapToInt(trendScene -> getTypeSum(trendScene, "yesterday_pv")).sum();
            CommonUtil.valueView(yesterdayPv, sumYesterdayPv);
            Preconditions.checkArgument(yesterdayPv == sumYesterdayPv, "");
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("app权限下【首页】全部门店昨日到访人次==pc【客流分析】各个门店历史客流中截止日期得人次之和");
        }
    }

    @Test(description = "app权限下【首页】全部门店今日到访人数==pc【客流分析】各个门店今日到访人数之和")
    public void homePage_data_7() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene scene = CardList.builder().pageType(EnumAppPageType.HOME_BELOW.name()).build();
            JSONObject totalNumber = md.invokeApi(scene).getJSONArray("list").getJSONObject(0).getJSONObject("total_number");
            //首页-今日到访总人数
            int todayUv = totalNumber.getInteger("today_uv");
            //pc各门店到访人数之和
            List<String> shopIds = getPcShopIds();
            int sumTodayUv = shopIds.stream().map(shopId -> RealTimeShopPvUv.builder().shopId(shopId).build()).mapToInt(realTimeScene -> getTypeSum(realTimeScene, "today_uv")).sum();
            CommonUtil.valueView(todayUv, sumTodayUv);
            Preconditions.checkArgument(todayUv == sumTodayUv, "");
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("app权限下【首页】全部门店今日到访人数==pc【客流分析】各个门店今日到访人数之和");
        }
    }

    @Test(description = "app权限下【首页】全部门店今日到访人次==pc【客流分析】各个门店今日到访人次之和")
    public void homePage_data_8() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene scene = CardList.builder().pageType(EnumAppPageType.HOME_BELOW.name()).build();
            JSONObject totalNumber = md.invokeApi(scene).getJSONArray("list").getJSONObject(0).getJSONObject("total_number");
            //首页-今日到访总人次
            int todayPv = totalNumber.getInteger("today_pv");
            //pc各门店到访人次之和
            List<String> shopIds = getPcShopIds();
            int sumTodayUv = shopIds.stream().map(shopId -> RealTimeShopPvUv.builder().shopId(shopId).build()).mapToInt(realTimeScene -> getTypeSum(realTimeScene, "today_pv")).sum();
            CommonUtil.valueView(todayPv, sumTodayUv);
            Preconditions.checkArgument(todayPv == sumTodayUv, "");
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("app权限下【首页】全部门店今日到访人次==pc【客流分析】各个门店今日到访人次之和");
        }
    }

    @Test(description = "app权限下【首页】全部门店昨日各时段中人数之和==pc【客流分析】各个门店昨日各时段中人数之和")
    public void homePage_data_9() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //app昨日到访人数之和
            String date = DateTimeUtil.addDayFormat(new Date(), -1);
            List<String> appShopIds = getAppShopIds();
            int appSum = appShopIds.stream().map(shopId -> HistoryPvUv.builder().shopId(shopId).day(date).build()).mapToInt(scene -> getTypeSum(scene, "uv")).sum();
            //pc各门店到访人数之和
            List<String> shopIds = getPcShopIds();
            int pcSum = shopIds.stream().map(shopId -> HistoryDayTrendPvUv.builder().shopId(shopId).day(date).build()).mapToInt(realTimeScene -> getTypeSum(realTimeScene, "yesterday_uv")).sum();
            CommonUtil.valueView(appSum, pcSum);
            Preconditions.checkArgument(appSum == pcSum, "");
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("app权限下【首页】全部门店昨日各时段中人数之和==pc【客流分析】各个门店昨日各时段中人数之和");
        }
    }

    @Test(description = "app权限下【首页】全部门店昨日各时段中人次之和==pc【客流分析】各个门店昨日各时段中人次之和")
    public void homePage_data_10() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //app昨日到访人次之和
            String date = DateTimeUtil.addDayFormat(new Date(), -1);
            List<String> appShopIds = getAppShopIds();
            int appSum = appShopIds.stream().map(shopId -> HistoryPvUv.builder().shopId(shopId).day(date).build()).mapToInt(scene -> getTypeSum(scene, "pv")).sum();
            //pc各门店到访人次之和
            List<String> shopIds = getPcShopIds();
            int pcSum = shopIds.stream().map(shopId -> HistoryDayTrendPvUv.builder().shopId(shopId).day(date).build()).mapToInt(realTimeScene -> getTypeSum(realTimeScene, "yesterday_pv")).sum();
            CommonUtil.valueView(appSum, pcSum);
            Preconditions.checkArgument(appSum == pcSum, "app昨日到访人次之和：" + appSum + " pc【客流分析】各个门店昨日各时段中人数之和：" + pcSum);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("app权限下【首页】全部门店昨日各时段中人次之和==pc【客流分析】各个门店昨日各时段中人次之和");
        }
    }

    @Test(description = "女性占比+男性占比==100%")
    public void passengerFlow_data_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            List<String> shopIds = getAppShopIds();
            shopIds.forEach(shopId -> {
                IScene scene = AgeGenderDistribution.builder().shopId(shopId).build();
                double percent = getTypeSum(scene, "gender", "gender_ratio");
                CommonUtil.valueView(percent);
                Preconditions.checkArgument(percent == (double) 100 || percent == (double) 0, "");
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("女性占比+男性占比==100%");
        }
    }

    @Test(description = "年龄段占比相加==100%")
    public void passengerFlow_data_2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            List<String> shopIds = getAppShopIds();
            shopIds.forEach(shopId -> {
                IScene scene = AgeGenderDistribution.builder().shopId(shopId).build();
                double percent = getTypeSum(scene, "age", "age_ratio");
                CommonUtil.valueView(percent);
                Preconditions.checkArgument(percent == (double) 100 || percent == (double) 0, "");
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("女性占比+男性占比==100%");
        }
    }

    @Test(description = "会员占比+非会员占比==100%")
    public void passengerFlow_data_3() {
        //todo
    }

    @Test(description = "今日到访人数<=今日各时刻中人数之和")
    public void passengerFlow_data_4() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            List<String> shopIds = getAppShopIds();
            shopIds.forEach(shopId -> {
                IScene scene = RealHourTotal.builder().shopId(shopId).build();
                JSONArray list = md.invokeApi(scene).getJSONArray("list");
                //今日到访人数
                int value = list.stream().map(e -> (JSONObject) e).filter(object -> object.getString("type").equals("pv"))
                        .map(s -> s.getInteger("value")).collect(Collectors.toList()).get(0);
                IScene scene1 = RealHourPvUv.builder().shopId(shopId).build();
                //今日各时刻中人数之和
                int sumValue = getTypeSum(scene1, "pv");
                CommonUtil.valueView(value, sumValue);
                Preconditions.checkArgument(value <= sumValue, "");
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("今日到访人数<=今日各时刻中人数之和");
        }
    }

    @Test(description = "今日到访人次==今日各时段中人次之和")
    public void passengerFlow_data_5() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            List<String> shopIds = getAppShopIds();
            shopIds.forEach(shopId -> {
                IScene scene = RealHourTotal.builder().shopId(shopId).build();
                JSONArray list = md.invokeApi(scene).getJSONArray("list");
                //今日到访人次
                int value = list.stream().map(e -> (JSONObject) e).filter(object -> object.getString("type").equals("uv"))
                        .map(s -> s.getInteger("value")).collect(Collectors.toList()).get(0);
                //今日各时段中人次之和
                IScene scene1 = RealHourPvUv.builder().shopId(shopId).build();
                int sumValue = getTypeSum(scene1, "uv");
                CommonUtil.valueView(value, sumValue);
                Preconditions.checkArgument(value == sumValue, "");
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("今日到访人次==今日各时段中人次之和");
        }
    }

    @Test(description = "昨日到访人数<=昨日各时段中人数之和")
    public void passengerFlow_data_6() {
        //todo
    }


    /**
     * 获取shopIds
     *
     * @return shopId集合
     */
    public List<String> getPcShopIds() {
        List<String> shopIds = new ArrayList<>();
        PassengerFlow.PassengerFlowBuilder builder = PassengerFlow.builder();
        int total = md.invokeApi(builder.build()).getInteger("total");
        int s = CommonUtil.getTurningPage(total, size);
        for (int i = 1; i < s; i++) {
            JSONArray array = md.invokeApi(builder.page(i).size(size).build()).getJSONArray("list");
            array.forEach(e -> {
                JSONObject jsonObject = (JSONObject) e;
                shopIds.add(String.valueOf(jsonObject.getInteger("id")));
            });
        }
        return shopIds;
    }


    /**
     * 获取shopIds
     *
     * @return shopId集合
     */
    public List<String> getAppShopIds() {
        Integer lastValue = 0;
        List<String> shopIds = new ArrayList<>();
        JSONArray list;
        do {
            IScene scene = CardList.builder().pageType(EnumAppPageType.DATA_LIST_PAGE.name()).lastValue(lastValue).size(20).build();
            JSONObject response = md.invokeApi(scene);
            lastValue = response.getInteger("last_value");
            list = response.getJSONArray("list");
            list.forEach(e -> {
                JSONObject jsonObject = (JSONObject) e;
                String shopId = String.valueOf(jsonObject.getJSONObject("result").getInteger("shop_id"));
                shopIds.add(shopId);
            });
            log.info("shopIds is:{}", shopIds);
        } while (list.size() == 20);
        return shopIds;
    }

    /**
     * 获取某个类型数据总和
     *
     * @param scene 某一接口
     * @param type  某个类型
     * @return 和值
     */
    public int getTypeSum(IScene scene, String type) {
        JSONArray list = md.invokeApi(scene).getJSONArray("list");
        return list.stream().map(e -> (JSONObject) e).mapToInt(jsonObject -> jsonObject.getInteger(type) == null ? 0 : jsonObject.getInteger(type)).sum();
    }

    public double getTypeSum(IScene scene, String key, String type) {
        AtomicReference<Double> percent = new AtomicReference<>((double) 0);
        JSONArray age = md.invokeApi(scene).getJSONArray(key);
        if (!age.isEmpty()) {
            age.forEach(e -> {
                JSONObject jsonObject = (JSONObject) e;
                String ageRatio = jsonObject.getString(type);
                String result = ageRatio.replace(ageRatio.substring(ageRatio.length() - 1), "");
                double num = Double.parseDouble(result);
                percent.updateAndGet(v -> v + num);
            });
        }
        return percent.get();
    }

}
