package com.haisheng.framework.testng.bigScreen.yuntong.wm.testcase;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.AtomicDouble;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumChecklistUser;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumJobName;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumTestProduce;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.EnumAccount;
import com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.pc.manage.*;
import com.haisheng.framework.testng.bigScreen.yuntong.wm.util.BusinessUtil;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.util.CommonUtil;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.util.*;

/**
 * 会听数据
 *
 * @author wangmin
 * @date 2021/1/29 11:17
 */
public class ShopDataCenterCase extends TestCaseCommon implements TestCaseStd {
    private static final EnumTestProduce PRODUCE = EnumTestProduce.YT_DAILY_CONTROL;
    private static final EnumAccount ALL_AUTHORITY = EnumAccount.ALL_YT_DAILY;
    public VisitorProxy visitor = new VisitorProxy(PRODUCE);
    public BusinessUtil util = new BusinessUtil(visitor);
    private static final String startDate = "2021-06-23";
    private static final String endDate = "2021-06-23";

    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before class initial");
        //替换checklist的相关信息
        CommonConfig commonConfig = new CommonConfig();
        commonConfig.dingHook = DingWebhook.CAR_OPEN_MANAGEMENT_PLATFORM_GRP;
        commonConfig.checklistAppId = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_CRM_DAILY_SERVICE;
        commonConfig.checklistQaOwner = EnumChecklistUser.WM.getName();
        //替换jenkins-job的相关信息
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.JIAOCHEN_DAILY_TEST.getJobName());
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, PRODUCE.getDesc() + commonConfig.checklistQaOwner);
        //放入shopId
        commonConfig.product = PRODUCE.getAbbreviation();
        commonConfig.referer = PRODUCE.getReferer();
        commonConfig.shopId = PRODUCE.getShopId();
        commonConfig.roleId = ALL_AUTHORITY.getRoleId();
        beforeClassInit(commonConfig);
    }

    @AfterClass
    @Override
    public void clean() {
        afterClassClean();
    }

    @BeforeMethod
    @Override
    public void createFreshCase(Method method) {
        visitor.setProduct(EnumTestProduce.YT_DAILY_SSO);
        util.loginApp(ALL_AUTHORITY);
        logger.debug("beforeMethod");
        caseResult = getFreshCaseResult(method);
        logger.debug("case: " + caseResult);
        visitor.setProduct(EnumTestProduce.YT_DAILY_CAR);
    }

    @Test(description = "【星级评分趋势】中的星级=【星级评分详情】中各话术环节各星级相加/（列表条数＊５）")
    public void data_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene evaluateV4ScoreTrendScene = EvaluateV4ScoreTrendScene.builder().receptionStart(startDate).receptionEnd(endDate).evaluateType(5).build();
            int score = evaluateV4ScoreTrendScene.invoke(visitor).getJSONArray("list").getJSONObject(0).getInteger("score");
            IScene evaluateV4PageScene = EvaluateV4PageScene.builder().receptionStart(startDate).receptionEnd(endDate).evaluateType(5).build();
            List<JSONObject> list = util.toJavaObjectList(evaluateV4PageScene, JSONObject.class);
            List<Integer> scoreList = new ArrayList<>();
            list.forEach(e -> {
                scoreList.add(e.getInteger("link1"));
                scoreList.add(e.getInteger("link2"));
                scoreList.add(e.getInteger("link3"));
                scoreList.add(e.getInteger("link4"));
                scoreList.add(e.getInteger("link5"));
            });
            int scoreSum = scoreList.stream().mapToInt(e -> e).sum();
            int mathResult = CommonUtil.getCeilIntRatio(scoreSum, list.size() * 5);
            CommonUtil.valueView(score, mathResult);
            Preconditions.checkArgument(score == mathResult, "【星级评分趋势】中的星级：" + score + " 【星级评分详情】中各话术环节各星级相加/（列表条数＊５）:" + mathResult);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("【星级评分趋势】中的星级=【星级评分详情】中各话术环节各星级相加/（列表条数＊５）");
        }
    }

    @Test(description = "【星级比例】全部环节中的x星级比例=【星级评分详情】总分中x星级条数/列表条数", dataProvider = "starType")
    public void data_2(String type, Integer scoreValue, String name) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene evaluateV4ScoreTrendScene = EvaluateV4ScoreRateScene.builder().receptionStart(startDate).receptionEnd(endDate).evaluateType(5).build();
            List<JSONObject> evaluateV4ScoreRateList = util.toJavaObjectList(evaluateV4ScoreTrendScene, JSONObject.class, "list");
            String percent = evaluateV4ScoreRateList.stream().filter(e -> e.getString("type_name").equals("全部环节")).map(e -> e.getString(type)).findFirst().orElse("0%");
            IScene evaluateV4PageScene = EvaluateV4PageScene.builder().receptionStart(startDate).receptionEnd(endDate).evaluateType(5).build();
            List<JSONObject> list = util.toJavaObjectList(evaluateV4PageScene, JSONObject.class);
            int count = (int) list.stream().filter(e -> e.getInteger("score").equals(scoreValue)).count();
            String mathResult = CommonUtil.getIntPercent(count, list.size()) + "%";
            CommonUtil.valueView(percent, mathResult);
            Preconditions.checkArgument(percent.equals(mathResult), "【星级比例】全部环节中" + name + "比例：" + percent + " 【星级评分详情】总分中" + name + "星级条数/列表条数:" + mathResult);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("【星级比例】全部环节中的x星级比例=【星级评分详情】总分中x星级条数/列表条数");
        }
    }

    @DataProvider(name = "starType")
    public Object[] getStarData() {
        return new Object[][]{
                {"one", 1, "一星"},
                {"two", 2, "二星"},
                {"three", 3, "三星"},
                {"four", 4, "四星"},
                {"five", 5, "五星"}
        };
    }

    @Test(description = "【星级比例】欢迎接待中的x星级比例=【星级评分详情】欢迎接待中x星级条数/列表条数", dataProvider = "starType")
    public void data_3(String type, Integer scoreValue, String name) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene evaluateV4ScoreTrendScene = EvaluateV4ScoreRateScene.builder().receptionStart(startDate).receptionEnd(endDate).evaluateType(5).build();
            List<JSONObject> evaluateV4ScoreRateList = util.toJavaObjectList(evaluateV4ScoreTrendScene, JSONObject.class, "list");
            String percent = evaluateV4ScoreRateList.stream().filter(e -> e.getString("type_name").equals("欢迎接待")).map(e -> e.getString(type)).findFirst().orElse("0%");
            IScene evaluateV4PageScene = EvaluateV4PageScene.builder().receptionStart(startDate).receptionEnd(endDate).evaluateType(5).build();
            List<JSONObject> list = util.toJavaObjectList(evaluateV4PageScene, JSONObject.class);
            int count = (int) list.stream().filter(e -> e.getInteger("link1").equals(scoreValue)).count();
            String mathResult = CommonUtil.getIntPercent(count, list.size()) + "%";
            CommonUtil.valueView(percent, mathResult);
            Preconditions.checkArgument(percent.equals(mathResult), "【星级比例】欢迎接待中" + name + "比例：" + percent + " 【星级评分详情】欢迎接待中" + name + "星级条数/列表条数:" + mathResult);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("【星级比例】欢迎接待中的x星级比例=【星级评分详情】欢迎接待中x星级条数/列表条数");
        }
    }

    //bug
    @Test(description = "【星级比例】欢迎接待中的x星级比例=【星级评分详情】欢迎接待中x星级条数/列表条数", dataProvider = "starType")
    public void data_4(String type, Integer scoreValue, String name) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String[][] strings = {{"欢迎接待", "link1"}, {"新车推荐", "link2"}, {"试乘试驾", "link3"}, {"车辆提案", "link4"}, {"个性需求", "link5"}};
            Arrays.stream(strings).forEach(string -> {
                IScene evaluateV4ScoreTrendScene = EvaluateV4ScoreRateScene.builder().receptionStart(startDate).receptionEnd(endDate).evaluateType(5).build();
                List<JSONObject> evaluateV4ScoreRateList = util.toJavaObjectList(evaluateV4ScoreTrendScene, JSONObject.class, "list");
                String percent = evaluateV4ScoreRateList.stream().filter(e -> e.getString("type_name").equals(string[0])).map(e -> e.getString(type)).findFirst().orElse("0%");
                IScene evaluateV4PageScene = EvaluateV4PageScene.builder().receptionStart(startDate).receptionEnd(endDate).evaluateType(5).build();
                List<JSONObject> list = util.toJavaObjectList(evaluateV4PageScene, JSONObject.class);
                int count = (int) list.stream().filter(e -> e.getInteger(string[1]).equals(scoreValue)).count();
                String mathResult = CommonUtil.getIntPercent(count, list.size()) + "%";
                CommonUtil.valueView(percent, mathResult);
                Preconditions.checkArgument(percent.equals(mathResult), "【星级比例】" + string[0] + "中" + name + "比例：" + percent + " 【星级评分详情】" + string[0] + "中" + name + "条数/列表条数:" + mathResult);
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("【星级比例】欢迎接待中的x星级比例=【星级评分详情】欢迎接待中x星级条数/列表条数");
        }
    }

    @Test(description = "【星级评分详情】总分=各环节星级相加/5")
    public void data_5() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene evaluateV4PageScene = EvaluateV4PageScene.builder().evaluateType(5).build();
            List<JSONObject> list = util.toJavaObjectList(evaluateV4PageScene, JSONObject.class);
            list.forEach(e -> {
                int total = e.getInteger("total");
                int link1 = e.getInteger("link1");
                int link2 = e.getInteger("link2");
                int link3 = e.getInteger("link3");
                int link4 = e.getInteger("link4");
                int link5 = e.getInteger("link5");
                int mathResult = CommonUtil.getIntRatio(link1 + link2 + link3 + link4 + link5, 5);
                CommonUtil.valueView(total, mathResult);
                Preconditions.checkArgument(total == mathResult, "【星级评分详情】总分：" + total + " 各环节星级相加/5：" + mathResult);
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("【星级评分详情】总分=各环节星级相加/5");
        }
    }

    @Test(description = "【星级评分趋势】星级=【星级评分详情】总分的星级=【星级比例】全部环节中为100%的星级")
    public void data_6() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene evaluateV4ScoreTrendScene = EvaluateV4ScoreTrendScene.builder().receptionStart(startDate).receptionEnd(endDate).evaluateType(5).build();
            List<JSONObject> trendList = util.toJavaObjectList(evaluateV4ScoreTrendScene, JSONObject.class, "list");
            int score = trendList.stream().filter(e -> startDate.contains(e.getString("day"))).map(e -> e.getInteger("score")).findFirst().orElse(0);
            IScene evaluateV4PageScene = EvaluateV4PageScene.builder().receptionStart(startDate).receptionEnd(endDate).evaluateType(5).build();
            List<JSONObject> pageList = util.toJavaObjectList(evaluateV4PageScene, JSONObject.class);
            int scoreTotal = pageList.stream().mapToInt(e -> e.getInteger("total")).sum();
            int mathResult = CommonUtil.getIntRatio(scoreTotal, pageList.size());
//            AtomicInteger rateScore = new AtomicInteger();
//            IScene evaluateV4ScoreRateScene = EvaluateV4ScoreRateScene.builder().receptionStart(startDate).receptionEnd(endDate).evaluateType(5).build();
//            List<JSONObject> rateList = util.toJavaObjectList(evaluateV4ScoreRateScene, JSONObject.class, "list");
//            rateList.stream().filter(e -> e.getString("type_name").equals("全部环节")).forEach(e -> {
//                double a = (double) Integer.parseInt(e.getString("one").replace("%", "")) / 100;
//                double b = (double) 2 * Integer.parseInt(e.getString("two").replace("%", "")) / 100;
//                double c = (double) 3 * Integer.parseInt(e.getString("three").replace("%", "")) / 100;
//                double d = (double) 4 * Integer.parseInt(e.getString("four").replace("%", "")) / 100;
//                double f = (double) 5 * Integer.parseInt(e.getString("five").replace("%", "")) / 100;
//                rateScore.addAndGet((int) (a + b + c + d + f));
//            });
            CommonUtil.valueView(score, mathResult);
            Preconditions.checkArgument(score == mathResult, "【星级评分趋势】星级：" + score, " 【星级评分详情】总分的平均分：" + mathResult);

        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("【星级评分趋势】星级=【星级评分详情】总分的星级=【星级比例】全部环节中为100%的星级");
        }
    }

    @Test(description = "各个环节中各星级比例相加为100％")
    public void data_7() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String[] strings = {"全部环节", "欢迎接待", "新车推荐", "试乘试驾", "车辆提案", "个性需求"};
            Arrays.stream(strings).forEach(string -> {
                AtomicDouble rateScore = new AtomicDouble();
                IScene evaluateV4ScoreRateScene = EvaluateV4ScoreRateScene.builder().receptionStart(startDate).receptionEnd(endDate).evaluateType(5).build();
                List<JSONObject> rateList = util.toJavaObjectList(evaluateV4ScoreRateScene, JSONObject.class, "list");
                rateList.stream().filter(e -> e.getString("type_name").equals(string)).forEach(e -> {
                    double a = Integer.parseInt(e.getString("one").replace("%", ""));
                    double b = Integer.parseInt(e.getString("two").replace("%", ""));
                    double c = Integer.parseInt(e.getString("three").replace("%", ""));
                    double d = Integer.parseInt(e.getString("four").replace("%", ""));
                    double f = Integer.parseInt(e.getString("five").replace("%", ""));
                    rateScore.addAndGet(a + b + c + d + f);
                });
                CommonUtil.valueView(rateScore);
                Preconditions.checkArgument(rateScore.get() == 100, string + "环节中各星级比例相加为100％:" + rateScore);
            });

        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("各个环节中各星级比例相加为100％");
        }
    }

    @Test(description = "各个环节中各星级比例相加为100％")
    public void data_8() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene evaluateV4PageScene = EvaluateV4PageScene.builder().receptionStart(startDate).evaluateEnd(endDate).evaluateType(5).build();
            List<JSONObject> evaluateV4PageList = util.toJavaObjectList(evaluateV4PageScene, JSONObject.class);
            evaluateV4PageList.forEach(evaluateV4Page -> {
                int total = evaluateV4Page.getInteger("total");
                long id = evaluateV4Page.getLong("id");
                IScene evaluateV4DetailScene = EvaluateV4DetailScene.builder().id(id).build();
                List<JSONObject> evaluateV4DetailList = util.toJavaObjectList(evaluateV4DetailScene, JSONObject.class, "info");
                int scoreSum = evaluateV4DetailList.stream().mapToInt(e -> e.getInteger("score")).sum();
                int mathResult = CommonUtil.getIntRatio(scoreSum, evaluateV4DetailList.size());
                CommonUtil.valueView(total, mathResult);
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("各个环节中各星级比例相加为100％");
        }
    }
}
