package com.haisheng.framework.testng.bigScreen.itemYuntong.casedaily.wm;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.AtomicDouble;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumChecklistUser;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumJobName;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumTestProduce;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.dataprovider.DataClass;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.EnumAccount;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.manage.*;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.util.SceneUtil;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.util.CommonUtil;
import com.haisheng.framework.util.DateTimeUtil;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.util.*;

/**
 * 门店数据中心测试用例
 *
 * @author wangmin
 * @date 2021/1/29 11:17
 */
public class ShopDataCenterCase extends TestCaseCommon implements TestCaseStd {
    private static final EnumTestProduce PRODUCE = EnumTestProduce.YT_DAILY_CAR;
    private static final EnumAccount ALL_AUTHORITY = EnumAccount.YT_ALL_DAILY;
    public VisitorProxy visitor = new VisitorProxy(PRODUCE);
    public SceneUtil util = new SceneUtil(visitor);
    private static final String startDate = DateTimeUtil.addDayFormat(new Date(), -2);
    private static final String endDate = DateTimeUtil.addDayFormat(new Date(), -2);

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
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.YUNTONG_DAILY_TEST.getJobName());
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, PRODUCE.getDesc() + commonConfig.checklistQaOwner);
        //放入shopId
        commonConfig.product = PRODUCE.getAbbreviation();
        commonConfig.referer = PRODUCE.getReferer();
        commonConfig.shopId = PRODUCE.getShopId();
        commonConfig.roleId = ALL_AUTHORITY.getRoleId();
        beforeClassInit(commonConfig);
        util.loginApp(ALL_AUTHORITY);
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

    @Test(description = "【星级评分趋势】中的星级=【星级评分详情】中各话术环节各星级相加/（列表条数＊５）")
    public void evaluate_data_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene evaluateV4ScoreTrendScene = EvaluateV4ScoreTrendScene.builder().receptionStart(startDate).receptionEnd(endDate).evaluateType(5).build();
            JSONObject obj = evaluateV4ScoreTrendScene.invoke(visitor).getJSONArray("list").getJSONObject(0);
            int score = obj.getInteger("score") == null ? 0 : obj.getInteger("score");
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

    @Test(description = "【星级比例】全部环节中的x星级比例=【星级评分详情】总分中x星级条数/列表条数", dataProvider = "starType", dataProviderClass = DataClass.class)
    public void evaluate_data_2(String type, Integer scoreValue, String name) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene evaluateV4ScoreTrendScene = EvaluateV4ScoreRateScene.builder().receptionStart(startDate).receptionEnd(endDate).evaluateType(5).build();
            List<JSONObject> evaluateV4ScoreRateList = util.toJavaObjectList(evaluateV4ScoreTrendScene, JSONObject.class, "list");
            String percent = evaluateV4ScoreRateList.stream().filter(e -> e.getString("type_name").equals("全部环节")).findFirst().map(e -> e.getString(type)).orElse("0%");
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

    @Test(description = "【星级比例】欢迎接待中的x星级比例=【星级评分详情】欢迎接待中x星级条数/列表条数", dataProvider = "starType", dataProviderClass = DataClass.class)
    public void evaluate_data_3(String type, Integer scoreValue, String name) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene evaluateV4ScoreTrendScene = EvaluateV4ScoreRateScene.builder().receptionStart(startDate).receptionEnd(endDate).evaluateType(5).build();
            List<JSONObject> evaluateV4ScoreRateList = util.toJavaObjectList(evaluateV4ScoreTrendScene, JSONObject.class, "list");
            String percent = evaluateV4ScoreRateList.stream().filter(e -> e.getString("type_name").equals("欢迎接待")).findFirst().map(e -> e.getString(type)).orElse("0%");
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

    @Test(description = "【星级比例】欢迎接待中的x星级比例=【星级评分详情】欢迎接待中x星级条数/列表条数", dataProvider = "starType", dataProviderClass = DataClass.class)
    public void evaluate_data_4(String type, Integer scoreValue, String name) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String[][] strings = {{"欢迎接待", "link1"}, {"新车推荐", "link2"}, {"试乘试驾", "link3"}, {"车辆提案", "link4"}, {"个性需求", "link5"}};
            Arrays.stream(strings).forEach(string -> {
                IScene evaluateV4ScoreTrendScene = EvaluateV4ScoreRateScene.builder().receptionStart(startDate).receptionEnd(endDate).evaluateType(5).build();
                List<JSONObject> evaluateV4ScoreRateList = util.toJavaObjectList(evaluateV4ScoreTrendScene, JSONObject.class, "list");
                String percent = evaluateV4ScoreRateList.stream().filter(e -> e.getString("type_name").equals(string[0])).findFirst().map(e -> e.getString(type)).orElse("0%");
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
    public void evaluate_data_5() {
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
                int mathResult = CommonUtil.getRoundIntRatio(link1 + link2 + link3 + link4 + link5, 5);
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
    public void evaluate_data_6() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene evaluateV4ScoreTrendScene = EvaluateV4ScoreTrendScene.builder().receptionStart(startDate).receptionEnd(endDate).evaluateType(5).build();
            List<JSONObject> trendList = util.toJavaObjectList(evaluateV4ScoreTrendScene, JSONObject.class, "list");
            int score = trendList.stream().filter(e -> startDate.contains(e.getString("day"))).findFirst().map(e -> e.getInteger("score")).orElse(0);
            IScene evaluateV4PageScene = EvaluateV4PageScene.builder().receptionStart(startDate).receptionEnd(endDate).evaluateType(5).build();
            List<JSONObject> pageList = util.toJavaObjectList(evaluateV4PageScene, JSONObject.class);
            int scoreTotal = pageList.stream().mapToInt(e -> e.getInteger("total")).sum();
            int mathResult = CommonUtil.getRoundIntRatio(scoreTotal, pageList.size());
            CommonUtil.valueView(score, mathResult);
            Preconditions.checkArgument(score == mathResult, "【星级评分趋势】星级：" + score, " 【星级评分详情】总分的平均分：" + mathResult);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("【星级评分趋势】星级=【星级评分详情】总分的星级=【星级比例】全部环节中为100%的星级");
        }
    }

    @Test(description = "各个环节中各星级比例相加为100％或者0%")
    public void evaluate_data_7() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String[] strings = {"全部环节", "欢迎接待", "新车推荐", "试乘试驾", "车辆提案", "个性需求"};
            Arrays.stream(strings).forEach(string -> {
                AtomicDouble rateScore = new AtomicDouble();
                IScene evaluateV4ScoreRateScene = EvaluateV4ScoreRateScene.builder().receptionStart(startDate).receptionEnd(endDate).evaluateType(5).build();
                List<JSONObject> rateList = util.toJavaObjectList(evaluateV4ScoreRateScene, JSONObject.class, "list");
                rateList.stream().filter(e -> e.getString("type_name").equals(string)).forEach(e -> {
                    double a = parseScore(e.getString("one"));
                    double b = parseScore(e.getString("two"));
                    double c = parseScore(e.getString("three"));
                    double d = parseScore(e.getString("four"));
                    double f = parseScore(e.getString("five"));
                    rateScore.addAndGet(a + b + c + d + f);
                });
                CommonUtil.valueView(rateScore);
                Preconditions.checkArgument(rateScore.get() == 100 || rateScore.get() == 0, string + " 环节中各星级比例相加为100％或者0，实际为：" + rateScore);
            });

        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("各个环节中各星级比例相加为100％或者0%");
        }
    }

    private double parseScore(String score) {
        return score == null ? 0 : Integer.parseInt(score.replace("%", ""));
    }

    @Test(description = "各个环节中各星级比例相加为100％")
    public void evaluate_data_8() {
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
                int mathResult = CommonUtil.getRoundIntRatio(scoreSum, evaluateV4DetailList.size());
                CommonUtil.valueView(total, mathResult);
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("各个环节中各星级比例相加为100％");
        }
    }
}
