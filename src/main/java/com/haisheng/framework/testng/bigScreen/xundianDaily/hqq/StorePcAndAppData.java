package com.haisheng.framework.testng.bigScreen.xundianDaily.hqq;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumChecklistUser;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumJobName;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumTestProduce;
import com.haisheng.framework.testng.bigScreen.crm.wm.scene.IScene;
import com.haisheng.framework.testng.bigScreen.xundianDaily.StoreScenarioUtil;
import com.haisheng.framework.testng.bigScreen.xundianDaily.enumerator.EnumAppPageType;
import com.haisheng.framework.testng.bigScreen.xundianDaily.enumerator.EnumCycleType;
import com.haisheng.framework.testng.bigScreen.xundianDaily.hqq.sence.app.*;
import com.haisheng.framework.testng.bigScreen.xundianDaily.hqq.sence.pc.*;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.util.CommonUtil;
import com.haisheng.framework.util.DateTimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 有问题处请@wangmin
 */
public class StorePcAndAppData extends TestCaseCommon implements TestCaseStd {
    public static final Logger log = LoggerFactory.getLogger(StorePcAndAppData.class);
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
        commonConfig.shopId = EnumTestProduce.MENDIAN_DAILY.getShopId();
        commonConfig.checklistQaOwner = "青青";
        beforeClassInit(commonConfig);
        logger.debug("store " + md);
        md.login("yuexiu@test.com", "f5b3e737510f31b88eb2d4b5d0cd2fb4");
//        md.login("2842729999@qq.com", "d1487f65671ae61d513764093af222d1");
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

    @Test(description = "权限下全部门店今日到访人数<=权限全部门店的趋势图中今日各时刻中人数之和") //bug 6526
    public void homePage_data_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene scene = CardList.builder().pageType(EnumAppPageType.HOME_BELOW.name()).build();
            JSONObject total = md.invokeApi(scene).getJSONArray("list").getJSONObject(0).getJSONObject("result");
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

    @Test(description = "权限下全部门店今日到访人次==权限下全部门店趋势图今日各时段中人次之和") //bug 6527 已解决
    public void homePage_data_2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene scene = CardList.builder().pageType(EnumAppPageType.HOME_BELOW.name()).build();
            JSONObject total = md.invokeApi(scene).getJSONArray("list").getJSONObject(0).getJSONObject("result");
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

    @Test(description = "权限下全部门店昨日到访人数<=权限下全部门店昨日各时段中人数之和") //ok
    public void homePage_data_3() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene scene = CardList.builder().pageType(EnumAppPageType.HOME_BELOW.name()).build();
            JSONObject total = md.invokeApi(scene).getJSONArray("list").getJSONObject(0).getJSONObject("result");
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

    @Test(description = "权限下全部门店昨日到访人次==权限下全部门店昨日各时段中人次之和") //bug 6529 已解决
    public void homePage_data_4() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene scene = CardList.builder().pageType(EnumAppPageType.HOME_BELOW.name()).build();

            JSONObject total = md.invokeApi(scene).getJSONArray("list").getJSONObject(0).getJSONObject("result");
            int yesterdayPv = total.getJSONObject("total_number").getInteger("yesterday_pv");
            JSONArray trendList = total.getJSONArray("trend_list");
            int sumYesterdayPv = trendList.stream().map(e -> (JSONObject) e).mapToInt(jsonObject -> jsonObject.getInteger("yesterday_pv") == null ? 0 : jsonObject.getInteger("yesterday_pv")).sum();
            CommonUtil.valueView(yesterdayPv, sumYesterdayPv);
            Preconditions.checkArgument(yesterdayPv <= sumYesterdayPv, "全部门店昨日到访人次：" + yesterdayPv + " 全部门店趋势图昨日各时段中人次之和" + sumYesterdayPv);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("权限下全部门店昨日到访人次==权限下全部门店昨日各时段中人次之和");
        }
    }

    @Test(description = "app权限下【首页】全部门店昨日到访人数==pc权限下【客流分析】各个门店历史客流中截止日期得人数相加的和") //bug 6531
    public void homePage_data_5() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String date = DateTimeUtil.addDayFormat(new Date(), -1);
            IScene scene = CardList.builder().pageType(EnumAppPageType.HOME_BELOW.name()).build();
            JSONObject totalNumber = md.invokeApi(scene).getJSONArray("list").getJSONObject(0).getJSONObject("result").getJSONObject("total_number");
            //首页-昨日到访总人数
            int yesterdayPv = totalNumber.getInteger("yesterday_uv");
            //pc各门店到访人数之和
            List<String> shopIds = getPcShopIds();
            int sumYesterdayPv = shopIds.stream().map(shopId -> HistoryDayTrendPvUv.builder().shopId(shopId).day(date).build()).mapToInt(trendScene -> getTypeSum(trendScene, "today_uv")).sum(); //已经选了昨天的日期了，所以取那天的数据
            CommonUtil.valueView(yesterdayPv, sumYesterdayPv);
            Preconditions.checkArgument(yesterdayPv == sumYesterdayPv, "app " + yesterdayPv + " , pc " + sumYesterdayPv);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("app权限下【首页】全部门店昨日到访人数==pc权限下【客流分析】各个门店历史客流中截止日期得人数相加的和");
        }
    }

    @Test(description = "app权限下【首页】全部门店昨日到访人次==pc【客流分析】各个门店历史客流中截止日期得人次之和") //bug 6532 已解决
    public void homePage_data_6() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String date = DateTimeUtil.addDayFormat(new Date(), -1);
            IScene scene = CardList.builder().pageType(EnumAppPageType.HOME_BELOW.name()).build();
            JSONObject totalNumber = md.invokeApi(scene).getJSONArray("list").getJSONObject(0).getJSONObject("result").getJSONObject("total_number");
            //首页-昨日到访总人次
            int yesterdayPv = totalNumber.getInteger("yesterday_pv");
            //pc各门店到访人次之和
            List<String> shopIds = getPcShopIds();
            int sumYesterdayPv = shopIds.stream().map(shopId -> HistoryDayTrendPvUv.builder().shopId(shopId).day(date).build()).mapToInt(trendScene -> getTypeSum(trendScene, "today_pv")).sum();
            CommonUtil.valueView(yesterdayPv, sumYesterdayPv);
            Preconditions.checkArgument(yesterdayPv == sumYesterdayPv, "app " + yesterdayPv + " , pc " + sumYesterdayPv);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("app权限下【首页】全部门店昨日到访人次==pc【客流分析】各个门店历史客流中截止日期得人次之和");
        }
    }

    @Test(description = "app权限下【首页】全部门店今日到访人数==pc【客流分析】各个门店今日到访人数之和") //bug 6533 //已解决
    public void homePage_data_7() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene scene = CardList.builder().pageType(EnumAppPageType.HOME_BELOW.name()).build();
            JSONObject totalNumber = md.invokeApi(scene).getJSONArray("list").getJSONObject(0).getJSONObject("result").getJSONObject("total_number");
            //首页-今日到访总人数
            int todayUv = totalNumber.getInteger("today_uv");
            //pc各门店到访人数之和
            List<String> shopIds = getPcShopIds();
            int sumTodayUv = shopIds.stream().map(shopId -> RealTimeShopPvUv.builder().shopId(shopId).build()).mapToInt(realTimeScene -> getTypeSum(realTimeScene, "today_uv")).sum();
            CommonUtil.valueView(todayUv, sumTodayUv);
            Preconditions.checkArgument(todayUv == sumTodayUv, "app " + todayUv + " , pc " + sumTodayUv);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("app权限下【首页】全部门店今日到访人数==pc【客流分析】各个门店今日到访人数之和");
        }
    }

    @Test(description = "app权限下【首页】全部门店今日到访人次==pc【客流分析】各个门店今日到访人次之和") // bug 6534
    public void homePage_data_8() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene scene = CardList.builder().pageType(EnumAppPageType.HOME_BELOW.name()).build();
            JSONObject totalNumber = md.invokeApi(scene).getJSONArray("list").getJSONObject(0).getJSONObject("result").getJSONObject("total_number");
            //首页-今日到访总人次
            int todayPv = totalNumber.getInteger("today_pv");
            //pc各门店到访人次之和
            List<String> shopIds = getPcShopIds();
            int sumTodayUv = shopIds.stream().map(shopId -> RealTimeShopPvUv.builder().shopId(shopId).build()).mapToInt(realTimeScene -> getTypeSum(realTimeScene, "today_pv")).sum();
            CommonUtil.valueView(todayPv, sumTodayUv);
            Preconditions.checkArgument(todayPv == sumTodayUv, "app " + todayPv + " , pc" + sumTodayUv);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("app权限下【首页】全部门店今日到访人次==pc【客流分析】各个门店今日到访人次之和");
        }
    }

    @Test(description = "app权限下【首页】全部门店昨日各时段中人数之和==pc【客流分析】各个门店昨日各时段中人数之和") //ok
    public void homePage_data_9() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //app昨日到访人数之和
            String date = DateTimeUtil.addDayFormat(new Date(), -1);
            List<String> appShopIds = getAppShopIds();
            int appSum = appShopIds.stream().map(shopId -> HistoryPvUv.builder().shopId(shopId).day(date).build()).mapToInt(scene -> getTypeSum(scene, "uv")).sum();
            //pc各门店到访人数之和
            List<String> shopIds = getPcShopIds();
            int pcSum = shopIds.stream().map(shopId -> HistoryDayTrendPvUv.builder().shopId(shopId).day(date).build()).mapToInt(realTimeScene -> getTypeSum(realTimeScene, "today_uv")).sum();
            CommonUtil.valueView(appSum, pcSum);
            Preconditions.checkArgument(appSum == pcSum, "app权限下【首页】全部门店昨日各时段中人数之和" + appSum + "!==pc【客流分析】各个门店昨日各时段中人数之和" + pcSum);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("app权限下【首页】全部门店昨日各时段中人数之和==pc【客流分析】各个门店昨日各时段中人数之和");
        }
    }

    @Test(description = "app权限下【首页】全部门店昨日各时段中人次之和==pc【客流分析】各个门店昨日各时段中人次之和") //ok
    public void homePage_data_10() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //app昨日到访人次之和
            String date = DateTimeUtil.addDayFormat(new Date(), -1);
            List<String> appShopIds = getAppShopIds();
            int appSum = appShopIds.stream().map(shopId -> HistoryPvUv.builder().shopId(shopId).day(date).build()).mapToInt(scene -> getTypeSum(scene, "pv")).sum();
            //pc各门店到访人次之和
            List<String> shopIds = getPcShopIds();
            int pcSum = shopIds.stream().map(shopId -> HistoryDayTrendPvUv.builder().shopId(shopId).day(date).build()).mapToInt(realTimeScene -> getTypeSum(realTimeScene, "today_pv")).sum();
            CommonUtil.valueView(appSum, pcSum);
            Preconditions.checkArgument(appSum == pcSum, "app昨日到访人次之和：" + appSum + " pc【客流分析】各个门店昨日各时段中人次之和：" + pcSum);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("app权限下【首页】全部门店昨日各时段中人次之和==pc【客流分析】各个门店昨日各时段中人次之和");
        }
    }

    @Test(description = "实时客流--女性占比+男性占比==100%")
    public void passengerFlow_data_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            List<String> shopIds = getAppShopIds();
            shopIds.forEach(shopId -> {
                IScene scene = RealHourAgeGenderDistribution.builder().shopId(shopId).build();
                double percent = getTypeSum(scene, "gender", "gender_ratio");
                CommonUtil.valueView(percent);
                Preconditions.checkArgument(percent == (double) 100 || percent == (double) 0, "实际" + percent);
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("实时客流--女性占比+男性占比==100%");
        }
    }

    @Test(description = "实时客流--年龄段占比相加==100%")
    public void passengerFlow_data_2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            List<String> shopIds = getAppShopIds();
            shopIds.forEach(shopId -> {
                IScene scene = RealHourAgeGenderDistribution.builder().shopId(shopId).build();
                double percent = getTypeSum(scene, "age", "age_ratio");
                CommonUtil.valueView(percent);
                Preconditions.checkArgument(percent == (double) 100 || percent == (double) 0, "实际" + percent);
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("实时客流--女性占比+男性占比==100%");
        }
    }

    @Ignore
    @Test(description = "实时客流--会员占比+非会员占比==100%")
    public void passengerFlow_data_3() {
        //todo 请黄青青补充完整
    }

    @Test(description = "实时客流--今日到访人数<=今日各时刻中人数之和") //ok
    public void passengerFlow_data_4() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            List<String> shopIds = getAppShopIds();
            shopIds.forEach(shopId -> {
                IScene scene = RealHourTotal.builder().shopId(shopId).build();
                JSONArray list = md.invokeApi(scene).getJSONArray("list");
                //今日到访人数
                int value = list.stream().map(e -> (JSONObject) e).filter(object -> object.getString("type").equals("uv")).map(s -> s.getInteger("value")).collect(Collectors.toList()).get(0);
                IScene scene1 = RealHourPvUv.builder().shopId(shopId).build();
                //今日各时刻中人次之和
                int sumValue = getTypeSum(scene1, "uv");
                CommonUtil.valueView(value, sumValue);
                Preconditions.checkArgument(value <= sumValue, "今日到访人数" + value + "，各时刻人次和" + sumValue);
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("实时客流--今日到访人数<=今日各时刻中人数之和");
        }
    }

    @Test(description = "实时客流--今日到访人次==今日各时段中人次之和") //ok //bug 6524
    public void passengerFlow_data_5() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            List<String> shopIds = getAppShopIds();
            shopIds.forEach(shopId -> {
                IScene scene = RealHourTotal.builder().shopId(shopId).build();
                JSONArray list = md.invokeApi(scene).getJSONArray("list");
                //今日到访人次
                int value = list.stream().map(e -> (JSONObject) e).filter(object -> object.getString("type").equals("pv")).map(s -> s.getInteger("value")).collect(Collectors.toList()).get(0);
                //今日各时段中人次之和
                IScene scene1 = RealHourPvUv.builder().shopId(shopId).build();
                int sumValue = getTypeSum(scene1, "pv");
                CommonUtil.valueView(value, sumValue);
                Preconditions.checkArgument(value == sumValue, "今日到访人次" + value + ", 今日各时段中人次之和" + sumValue);
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("实时客流--今日到访人次==今日各时段中人次之和");
        }
    }

    @Test(description = "实时客流--昨日到访人次==昨日各时段中人次之和")
    public void passengerFlow_data_6() {
        //todo 请黄青青补充完整
    }

    @Test(description = "实时客流--昨日到访人次==昨日各时段中人次之和")
    public void passengerFlow_data_7() {
        //todo 请黄青青补充完整
    }

    @Test(description = "实时客流--app昨日到访人数==app历史客流中截止日期得人数")
    public void passengerFlow_data_8() {
        //todo 请黄青青补充完整
    }

    @Test(description = "实时客流--app昨日到访人数==pc历史客流中截止日期得人数")
    public void passengerFlow_data_9() {
        //todo 请黄青青补充完整
    }

    @Test(description = "实时客流--app昨日到访人次==app历史客流中截止日期得人次")
    public void passengerFlow_data_10() {
        //todo 请黄青青补充完整
    }

    @Test(description = "实时客流--app昨日到访人次==pc历史客流中截止日期得人次")
    public void passengerFlow_data_11() {
        //todo 请黄青青补充完整
    }

    @Test(description = "实时客流--app今日到访人数==pc【实时客流】今日到访人数") //ok
    public void passengerFlow_data_12() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            List<String> shopIds = getAppShopIds();
            shopIds.forEach(shopId -> {
                //app今日到访
                IScene scene = RealHourTotal.builder().shopId(shopId).build();
                JSONArray list = md.invokeApi(scene).getJSONArray("list");
                Integer appUv = list.stream().map(e -> (JSONObject) e).filter(object -> object.getString("type").equals("uv")).map(object -> object.getInteger("value")).collect(Collectors.toList()).get(0);
                IScene scene1 = RealTimeShopTotal.builder().shopId(shopId).build();
                JSONArray list1 = md.invokeApi(scene1).getJSONArray("list");
                Integer pcUv = list1.stream().map(e -> (JSONObject) e).filter(object -> object.getString("type").equals("uv")).map(object -> object.getInteger("value")).collect(Collectors.toList()).get(0);
                CommonUtil.valueView(appUv, pcUv);
                Preconditions.checkArgument(appUv.equals(pcUv), "app" + appUv + ",pc" + pcUv);
                CommonUtil.logger(shopId);
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("实时客流--app今日到访人数==pc【实时客流】今日到访人数");
        }
    }

    @Test(description = "实时客流--app今日到访人次==pc【实时客流】今日到访人次")//ok
    public void passengerFlow_data_13() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            List<String> shopIds = getAppShopIds();
            shopIds.forEach(shopId -> {
                //app今日到访
                IScene scene = RealHourTotal.builder().shopId(shopId).build();
                JSONArray list = md.invokeApi(scene).getJSONArray("list");
                Integer appPv = list.stream().map(e -> (JSONObject) e).filter(object -> object.getString("type").equals("pv")).map(object -> object.getInteger("value")).collect(Collectors.toList()).get(0);
                IScene scene1 = RealTimeShopTotal.builder().shopId(shopId).build();
                JSONArray list1 = md.invokeApi(scene1).getJSONArray("list");
                Integer pcPv = list1.stream().map(e -> (JSONObject) e).filter(object -> object.getString("type").equals("pv")).map(object -> object.getInteger("value")).collect(Collectors.toList()).get(0);
                CommonUtil.valueView(appPv, pcPv);
                Preconditions.checkArgument(appPv.equals(pcPv), "app" + appPv + ", pc" + pcPv);
                CommonUtil.logger(shopId);
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("实时客流--app今日到访人数==pc【实时客流】今日到访人数");
        }
    }

    @Test(description = "实时客流--app昨日各时段中人数之和==pc【实时客流】昨日各时段中人数之和") //6417
    public void passengerFlow_data_14() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String date = DateTimeUtil.addDayFormat(new Date(), -1);
            List<String> shopIds = getAppShopIds();
            shopIds.forEach(shopId -> {
                IScene scene = HistoryPvUv.builder().day(date).shopId(shopId).build();
                int appUvSum = getTypeSum(scene, "uv");
                IScene scene1 = RealTimeShopPvUv.builder().shopId(shopId).build();
                int pcUvSum = getTypeSum(scene1, "yesterday_uv");
                CommonUtil.valueView(appUvSum, pcUvSum);
                Preconditions.checkArgument(appUvSum == pcUvSum, "app" + appUvSum + ", pc" + pcUvSum);
                CommonUtil.logger(shopId);
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("实时客流--app昨日各时段中人数之和==pc【实时客流】昨日各时段中人数之和");
        }
    }

    //@Test(description = "实时客流--app昨日各时段中人次之和==pc【实时客流】昨日各时段中人次之和") // 没ok
    public void passengerFlow_data_15() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String date = DateTimeUtil.addDayFormat(new Date(), -1);
            List<String> shopIds = getAppShopIds();
            shopIds.forEach(shopId -> {
                IScene scene = HistoryPvUv.builder().day(date).shopId(shopId).build();
                int appPvSum = getTypeSum(scene, "pv");
                IScene scene1 = RealTimeShopPvUv.builder().shopId(shopId).build();
                int pcPvSum = getTypeSum(scene1, "yesterday_pv");
                CommonUtil.valueView(appPvSum, pcPvSum);
                Preconditions.checkArgument(appPvSum == pcPvSum, "app " + appPvSum + " ,pc" + pcPvSum);
                CommonUtil.logger(shopId);
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("实时客流--app昨日各时段中人次之和==pc【实时客流】昨日各时段中人次之和");
        }
    }

    @Test(description = "历史客流--最近7天、14天、30天、60天女性占比+男性占比==100%") //ok
    public void passengerFlow_data_16() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Arrays.stream(EnumCycleType.values()).forEach(enumCycleType -> {
                List<String> shopIds = getAppShopIds();
                shopIds.forEach(shopId -> {
                    IScene scene = HistoryAgeGenderDistribution.builder().shopId(shopId).cycleType(enumCycleType.name()).build();
                    double percent = getTypeSum(scene, "gender", "gender_ratio_number");
                    CommonUtil.valueView(percent);
                    Preconditions.checkArgument(percent == (double) 100 || percent == (double) 0, shopId + " " + enumCycleType.name() + "占比和=" + percent);
                    CommonUtil.logger(shopId);
                });
                CommonUtil.logger(enumCycleType.name());
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("历史客流--最近7天、14天、30天、60天女性占比+男性占比==100%");
        }
    }

    @Test(description = "历史客流--最近7天、14天、30天、60年龄段占比相加==100%") //ok
    public void passengerFlow_data_17() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Arrays.stream(EnumCycleType.values()).forEach(enumCycleType -> {
                List<String> shopIds = getAppShopIds();
                shopIds.forEach(shopId -> {
                    IScene scene = HistoryAgeGenderDistribution.builder().shopId(shopId).cycleType(enumCycleType.name()).build();
                    double percent = getTypeSum(scene, "age", "age_ratio_number");
                    CommonUtil.valueView(percent);
                    Preconditions.checkArgument(percent == (double) 100 || percent == (double) 0, shopId + " " + enumCycleType.name() + "占比和=" + percent);
                    CommonUtil.logger(shopId);
                });
                CommonUtil.logger(enumCycleType.name());
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("历史客流--最近7天、14天、30天、60年龄段占比相加==100%");
        }
    }

    @Test(description = "历史客流--会员占比+非会员占比==100%")
    public void passengerFlow_data_18() {
        //todo 请黄青青补充完整
    }

    @Test(description = "历史客流--到店人次的和==客群漏斗中的到店人次")
    public void passengerFlow_data_19() {
        //todo 请黄青青补充完整
    }

    @Test(description = "历史客流--到店人数的和>=客群漏斗中的到店人数")
    public void passengerFlow_data_20() {
        //todo 请黄青青补充完整
    }

    @Test(description = "历史客流--日均客流==人数相加/天数")
    public void passengerFlow_data_21() {
        //todo 请黄青青补充完整
    }

    @Test(description = "历史客流--自然日--女性占比+男性占比==100%") //ok
    public void passengerFlow_data_22() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            List<String> dayList = getDayList();
            List<String> shopIds = getAppShopIds();
            shopIds.forEach(shopId -> dayList.forEach(day -> {
                IScene scene = HistoryAgeGenderDistribution.builder().shopId(shopId).day(day).build();
                if (hasType(scene, "gender")) {
                    double percent = getTypeSum(scene, "gender", "gender_ratio_number");
                    CommonUtil.valueView(percent);
                    Preconditions.checkArgument(percent == (double) 100 || percent == (double) 0, "门店" + shopId + " " + dayList + " 百分比为" + percent);
                    CommonUtil.logger(shopId + day);
                }
            }));
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("历史客流--自然日女性占比+男性占比==100%");
        }
    }

    @Test(description = "历史客流--自然日--年龄段占比相加==100%")
    public void passengerFlow_data_23() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            List<String> dayList = getDayList();
            List<String> shopIds = getAppShopIds();
            shopIds.forEach(shopId -> dayList.forEach(day -> {
                IScene scene = HistoryAgeGenderDistribution.builder().shopId(shopId).day(day).build();
                if (hasType(scene, "age")) {
                    double percent = getTypeSum(scene, "age", "age_ratio_number");
                    CommonUtil.valueView(percent);
                    Preconditions.checkArgument(percent == (double) 100 || percent == (double) 0, "门店" + shopId + " " + dayList + " 百分比为" + percent);
                    CommonUtil.logger(shopId + day);
                }
            }));
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("历史客流--自然日--年龄段占比相加==100%");
        }
    }

    @Test(description = "历史客流--自然月--女性占比+男性占比==100%") //ok
    public void passengerFlow_data_24() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            List<String> shopIds = getAppShopIds();
            List<String> monthList = getMonthList();
            shopIds.forEach(shopId -> monthList.forEach(month -> {
                IScene scene = HistoryAgeGenderDistribution.builder().shopId(shopId).month(month).build();
                if (hasType(scene, "gender")) {
                    double percent = getTypeSum(scene, "gender", "gender_ratio_number");
                    CommonUtil.valueView(percent);
                    Preconditions.checkArgument(percent == (double) 100 || percent == (double) 0, "门店" + shopId + " " + monthList + " 百分比为" + percent);
                    CommonUtil.logger(shopId + month);
                }

            }));
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("历史客流--自然月--女性占比+男性占比==100%");
        }
    }

    @Test(description = "历史客流--自然月--年龄段占比相加==100%") //ok
    public void passengerFlow_data_25() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            List<String> shopIds = getAppShopIds();
            List<String> monthList = getMonthList();
            shopIds.forEach(shopId -> monthList.forEach(month -> {
                IScene scene = HistoryAgeGenderDistribution.builder().shopId(shopId).month(month).build();
                if (hasType(scene, "age")) {
                    double percent = getTypeSum(scene, "age", "age_ratio_number");
                    CommonUtil.valueView(percent);
                    Preconditions.checkArgument(percent == (double) 100 || percent == (double) 0, "门店" + shopId + " " + monthList + " 百分比为" + percent);
                    CommonUtil.logger(shopId + month);
                }
            }));
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("历史客流--自然月--年龄段占比相加==100%");
        }
    }

    @Test(description = "历史客流--吸引率==兴趣客群/过店客群")
    public void passengerFlow_data_26() {
        //todo 请黄青青补充完整
    }

    @Test(description = "历史客流--进店率==进店客群/兴趣客群")
    public void passengerFlow_data_27() {
        //todo 请黄青青补充完整
    }

    @Test(description = "历史客流--转化率==交易客群/进店客群")
    public void passengerFlow_data_28() {
        //todo 请黄青青补充完整
    }

    @Ignore //统计方式不一致，注释掉
    @Test(description = "历史客流--选择同一时间段（月），男性占比==pc客群漏斗中进店客群男性占比") //bug 6483
    public void passengerFlow_data_29() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            List<String> shopIds = getPcShopIds();
            List<String> monthList = getMonthList();
            shopIds.forEach(shopId -> monthList.forEach(month -> {
                IScene appScene = HistoryAgeGenderDistribution.builder().shopId(shopId).month(month).build();
                JSONArray gender = md.invokeApi(appScene).getJSONArray("gender");
//                String appGenderRatio = gender.stream().map(e -> (JSONObject) e).filter(object -> object.getString("gender_type").equals("男性"))
//                        .map(s -> s.getString("gender_ratio_number")).collect(Collectors.toList()).get(0);
//                double appPercent = percentToDouble(appGenderRatio);
                double appPercent = gender.getJSONObject(0).getDouble("gender_ratio_number");

                IScene pcScene = HistoryShopAgeGenderDistribution.builder().month(month).shopId(shopId).build();
                JSONArray list = md.invokeApi(pcScene).getJSONObject("enter").getJSONArray("list");
                double pcPercent = getTypeSum(list, "male_percent");
                CommonUtil.valueView(appPercent, pcPercent);
                Preconditions.checkArgument(pcPercent == appPercent, shopId + " app" + appPercent + "， pc" + pcPercent);
                CommonUtil.logger(shopId + month);
            }));
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("历史客流--选择同一时间段，男性占比==pc客群漏斗进店客群中男性占比");
        }
    }

    @Ignore //统计方式不一致，注释掉
    @Test(description = "历史客流--选择同一时间段（月），女性占比==pc客群漏斗中进店客群女性占比") //bug 6483
    public void passengerFlow_data_30() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            List<String> shopIds = getPcShopIds();
            List<String> monthList = getMonthList();
            shopIds.forEach(shopId -> monthList.forEach(month -> {
                IScene appScene = HistoryAgeGenderDistribution.builder().shopId(shopId).month(month).build();
                JSONArray gender = md.invokeApi(appScene).getJSONArray("gender");

//                String appGenderRatio = gender.stream().map(e -> (JSONObject) e).filter(object -> object.getString("gender_type").equals("男性"))
//                        .map(s -> s.getString("gender_ratio_number")).collect(Collectors.toList()).get(0);
//                double appPercent = percentToDouble(appGenderRatio);
                double appPercent = gender.getJSONObject(1).getDouble("gender_ratio_number");
                IScene pcScene = HistoryShopAgeGenderDistribution.builder().month(month).shopId(shopId).build();
                JSONArray list = md.invokeApi(pcScene).getJSONObject("enter").getJSONArray("list");
                double pcPercent = getTypeSum(list, "female_percent");
                CommonUtil.valueView(appPercent, pcPercent);
                Preconditions.checkArgument(pcPercent == appPercent, shopId + " app" + appPercent + ", pc" + pcPercent);
                CommonUtil.logger(shopId + month);
            }));
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("历史客流--选择同一时间段，女性占比==pc客群漏斗进店客群中女性占比");
        }
    }

    @Ignore //统计方式不一致，注释掉
    @Test(description = "历史客流--选择同一时间段（月），年龄段占比==pc客群漏斗中年龄段占比") //bug 6483
    public void passengerFlow_data_31() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            List<String> shopIds = getPcShopIds();
            List<String> monthList = getMonthList();
            shopIds.forEach(shopId -> monthList.forEach(month -> {
                IScene appScene = HistoryAgeGenderDistribution.builder().shopId(shopId).month(month).build();
                JSONArray age = md.invokeApi(appScene).getJSONArray("age");
                //List<Double> appPercentList = gender.stream().map(e -> (JSONObject) e).map(s -> percentToDouble(s.getString("age_ratio_number"))).collect(Collectors.toList());
                List<Double> appPercentList = new ArrayList<>();
                for (int i = 0; i < age.size(); i++) {
                    appPercentList.add(age.getJSONObject(i).getDouble("age_ratio_number"));
                }
                IScene pcScene = HistoryShopAgeGenderDistribution.builder().month(month).shopId(shopId).build();
                JSONArray list = md.invokeApi(pcScene).getJSONObject("enter").getJSONArray("list");
                List<Double> pcPercentList = list.stream().map(e -> (JSONObject) e).map(object -> percentToDouble(object.getString("age_group_percent"))).collect(Collectors.toList());
                CommonUtil.valueView(pcPercentList);
                CommonUtil.valueView(appPercentList, pcPercentList);
                Preconditions.checkArgument(appPercentList.equals(pcPercentList), "app" + appPercentList + "， pc" + pcPercentList);
                CommonUtil.logger(shopId + " " + month);
            }));
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("历史客流--选择同一时间段，年龄段占比==pc客群漏斗中年龄段占比");
        }
    }

    //@Test(description = "历史客流--选择同一时间段，app转化率&吸引率&进店率==pc【客群漏斗】的转化率&吸引率&进店率") //没ok
    public void passengerFlow_data_32() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            List<String> shopIds = getPcShopIds();
            List<String> monthList = getMonthList();
            shopIds.forEach(shopId -> monthList.forEach(month -> {
                IScene appScene = HistoryConversion.builder().month(month).shopId(shopId).build();
                JSONObject appData = md.invokeApi(appScene);
                String appTransactionPercentage = appData.getString("transaction_percentage");
                String appEnterPercentage = appData.getString("enter_percentage");
                String appInterestPercentage = appData.getString("interest_percentage");
                CommonUtil.valueView(appTransactionPercentage, appEnterPercentage, appInterestPercentage);
                IScene pcScene = HistoryShopConversion.builder().shopId(shopId).month(month).build();
                JSONObject pcData = md.invokeApi(pcScene);
                String pcTransactionPercentage = pcData.getString("transaction_percentage");
                String pceEnterPercentage = pcData.getString("enter_percentage");
                String pcInterestPercentage = pcData.getString("interest_percentage");
                CommonUtil.valueView(pcTransactionPercentage, pceEnterPercentage, pcInterestPercentage);
                Preconditions.checkArgument(appEnterPercentage.equals(pceEnterPercentage), "");
                Preconditions.checkArgument(appInterestPercentage.equals(pcInterestPercentage), "");
                Preconditions.checkArgument(appTransactionPercentage.equals(pcTransactionPercentage), "");
                CommonUtil.logger(shopId + " " + month);
            }));
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("历史客流--选择同一时间段，app转化率&吸引率&进店率==pc【客群漏斗】的转化率&吸引率&进店率");
        }
    }


    @Ignore //统计方式不一致，注释掉
    @Test(description = "历史客流--选择同一时间段（7/14/30/60），男性占比==pc客群漏斗中进店客群男性占比") //同 bug 6483
    public void passengerFlow_data_33() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Arrays.stream(EnumCycleType.values()).forEach(enumCycleType -> {
                List<String> shopIds = getPcShopIds();

                shopIds.forEach(shopId -> {
                    IScene appScene = HistoryAgeGenderDistribution.builder().shopId(shopId).cycleType(enumCycleType.name()).build();
                    JSONArray gender = md.invokeApi(appScene).getJSONArray("gender");
                    double appPercent = gender.getJSONObject(0).getDouble("gender_ratio_number");

                    IScene pcScene = HistoryShopAgeGenderDistribution.builder().cycleType(enumCycleType.name()).shopId(shopId).build();
                    JSONArray list = md.invokeApi(pcScene).getJSONObject("enter").getJSONArray("list");
                    double pcPercent = getTypeSum(list, "male_percent");
                    CommonUtil.valueView(appPercent, pcPercent);
                    Preconditions.checkArgument(pcPercent == appPercent, shopId + " app" + appPercent + "， pc" + pcPercent);
                    CommonUtil.logger(shopId + enumCycleType.name());
                });
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("历史客流--选择同一时间段，男性占比==pc客群漏斗进店客群中男性占比");
        }
    }

    @Ignore //统计方式不一致，注释掉
    @Test(description = "历史客流--选择同一时间段（7/14/30/60），女性占比==pc客群漏斗中进店客群女性占比")//同 bug 6483
    public void passengerFlow_data_34() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            Arrays.stream(EnumCycleType.values()).forEach(enumCycleType -> {
                List<String> shopIds = getPcShopIds();

                shopIds.forEach(shopId -> {
                    IScene appScene = HistoryAgeGenderDistribution.builder().shopId(shopId).cycleType(enumCycleType.name()).build();
                    JSONArray gender = md.invokeApi(appScene).getJSONArray("gender");
                    double appPercent = gender.getJSONObject(0).getDouble("gender_ratio_number");

                    IScene pcScene = HistoryShopAgeGenderDistribution.builder().cycleType(enumCycleType.name()).shopId(shopId).build();
                    JSONArray list = md.invokeApi(pcScene).getJSONObject("enter").getJSONArray("list");
                    double pcPercent = getTypeSum(list, "female_percent");
                    CommonUtil.valueView(appPercent, pcPercent);
                    Preconditions.checkArgument(pcPercent == appPercent, shopId + " app" + appPercent + "， pc" + pcPercent);
                    CommonUtil.logger(shopId + enumCycleType.name());
                });
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("历史客流--选择同一时间段，女性占比==pc客群漏斗进店客群中女性占比");
        }
    }

    @Ignore //统计方式不一致，注释掉
    @Test(description = "历史客流--选择同一时间段（7/14/30/60），年龄段占比==pc客群漏斗中年龄段占比") //bug 6488
    public void passengerFlow_data_35() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Arrays.stream(EnumCycleType.values()).forEach(enumCycleType -> {
                List<String> shopIds = getPcShopIds();

                shopIds.forEach(shopId -> {
                    IScene appScene = HistoryAgeGenderDistribution.builder().shopId(shopId).cycleType(enumCycleType.name()).build();
                    JSONArray age = md.invokeApi(appScene).getJSONArray("age");
                    List<Double> appPercentList = new ArrayList<>();
                    for (int i = 0; i < age.size(); i++) {
                        appPercentList.add(age.getJSONObject(i).getDouble("age_ratio_number"));
                    }

                    IScene pcScene = HistoryShopAgeGenderDistribution.builder().cycleType(enumCycleType.name()).shopId(shopId).build();
                    JSONArray list = md.invokeApi(pcScene).getJSONObject("enter").getJSONArray("list");
                    List<Double> pcPercentList = list.stream().map(e -> (JSONObject) e).map(object -> percentToDouble(object.getString("age_group_percent"))).collect(Collectors.toList());
                    CommonUtil.valueView(pcPercentList);
                    CommonUtil.valueView(appPercentList, pcPercentList);
                    Preconditions.checkArgument(appPercentList.equals(pcPercentList), "app" + appPercentList + "， pc" + pcPercentList);
                    CommonUtil.logger(shopId + " " + enumCycleType.name());
                });
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("历史客流--选择同一时间段，年龄段占比==pc客群漏斗中年龄段占比");
        }
    }


    public List<String> getMonthList() {
        String date = DateTimeUtil.getFormat(new Date(), "yyyy-MM");
        String year = date.substring(0, 4);
        String month = date.substring(5, 7);
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            int y = Integer.parseInt(month) - i;
            String newDate = y > 9 ? year + "-" + y : year + "-0" + y;
            list.add(newDate);

        }
        return list;
    }

    public List<String> getDayList() {
        List<String> list = new ArrayList<>();
        for (int i = 1; i < 50; i++) {
            String date = DateTimeUtil.addDayFormat(new Date(), -i);
            list.add(date);
        }
        return list;
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
            shopIds.addAll(array.stream().map(e -> (JSONObject) e).map(jsonObject -> String.valueOf(jsonObject.getInteger("id"))).collect(Collectors.toList()));
        }
        log.info("shopIds is:{}", shopIds);
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
            shopIds.addAll(list.stream().map(e -> (JSONObject) e).map(jsonObject -> String.valueOf(jsonObject.getJSONObject("result").getInteger("shop_id"))).collect(Collectors.toList()));
            log.info("shopIds is:{}", shopIds);
        } while (list.size() == 20);
        return shopIds;
    }

    /**
     * 获取某个类型数据总和
     *
     * @param scene 某一接口
     * @param type  某个字段
     * @return 和值(int)
     */
    public int getTypeSum(IScene scene, String type) {
        JSONArray list = md.invokeApi(scene).getJSONArray("list");
        return list.stream().map(e -> (JSONObject) e).mapToInt(jsonObject -> jsonObject.getInteger(type) == null ? 0 : jsonObject.getInteger(type)).sum();
    }

    /**
     * 获取某个类型数据总和
     *
     * @param jsonArray 某个jsonArray
     * @param type      某个字段
     * @return 和值(double)
     */
    public double getTypeSum(JSONArray jsonArray, String type) {
        return jsonArray.isEmpty() ? 0 : jsonArray.stream().map(e -> (JSONObject) e).map(jsonObject -> percentToDouble(jsonObject.getString(type)))
                .collect(Collectors.toList()).stream().mapToDouble(e -> e).sum();
    }

    /**
     * 获取某个类型数据总和
     *
     * @param scene 某一接口
     * @param key   某个jsonArray
     * @param type  某个字段
     * @return 和值(double)
     */
    public double getTypeSum(IScene scene, String key, String type) {
        JSONArray list = md.invokeApi(scene).getJSONArray(key);
        return list.isEmpty() ? 0 : list.stream().map(e -> (JSONObject) e).map(jsonObject -> percentToDouble(jsonObject.getString(type) == null ? "0%" : jsonObject.getString(type)))
                .collect(Collectors.toList()).stream().mapToDouble(e -> e).sum();
    }

    public boolean hasType(IScene scene, String key) {
        return md.invokeApi(scene).containsKey(key);

    }

    /**
     * 百分比转double
     *
     * @param percent 百分比
     * @return 值(double)
     */
    private double percentToDouble(String percent) {
        String per = StringUtils.isEmpty(percent) || !percent.contains("%") ? "0%" : percent;
        String result = per.replace(per.substring(per.length() - 1), "");
        return Double.parseDouble(result);
    }
}
