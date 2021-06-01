package com.haisheng.framework.testng.bigScreen.yuntong.wm.testcase;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumChecklistUser;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumJobName;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumTestProduce;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.EnumAccount;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.util.UserUtil;
import com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.app.personaldata.AppCapabilityModelScene;
import com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.app.personaldata.AppOverviewScene;
import com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.app.personaldata.AppReceptionLinkScoreScene;
import com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.pc.manage.EvaluateV4ScoreTrendScene;
import com.haisheng.framework.testng.bigScreen.yuntong.wm.util.BusinessUtil;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.util.CommonUtil;
import com.haisheng.framework.util.DateTimeUtil;
import org.jetbrains.annotations.NotNull;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 业务管理测试用例
 *
 * @author wangmin
 * @date 2021/1/29 11:17
 */
public class ReceptionCase extends TestCaseCommon implements TestCaseStd {
    private static final EnumTestProduce PRODUCE = EnumTestProduce.YT_DAILY;
    private static final EnumAccount ALL_AUTHORITY = EnumAccount.ALL_AUTHORITY_DAILY;
    public VisitorProxy visitor = new VisitorProxy(PRODUCE);
    public UserUtil user = new UserUtil(visitor);
    public BusinessUtil util = new BusinessUtil(visitor);

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
        user.loginPc(ALL_AUTHORITY);
        logger.debug("beforeMethod");
        caseResult = getFreshCaseResult(method);
        logger.debug("case: " + caseResult);
    }


    @Test
    public void receptionCriticism_data_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Integer dataCycleType = 0;
            String startDate = "";
            String endDate = "";
            String saleId = "";
            JSONObject responseData = AppOverviewScene.builder().dataCycleType(dataCycleType).startDate(startDate).endDate(endDate).salesId(saleId).build().invoke(visitor);
            int count = responseData.getInteger("count");
            int totalDuration = responseData.getInteger("total_duration");
            int averageDuration = responseData.getInteger("average_duration");
            int mathResult = CommonUtil.getIntValue(totalDuration, count, 0);
            Preconditions.checkArgument(averageDuration == mathResult, "平均时长为：" + averageDuration + "总时长除以接待次数：" + mathResult);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("app-平均接待时长=总接待时长/接待次数");
        }
    }

    @Test
    public void receptionCriticism_data_2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Integer dataCycleType = 0;
            String startDate = "";
            String endDate = "";
            String saleId = "";
            Map<Integer, Integer> map = new HashMap<>();
            JSONArray capabilityModelList = AppCapabilityModelScene.builder().dataCycleType(dataCycleType).startDate(startDate).endDate(endDate).salesId(saleId).build().invoke(visitor).getJSONArray("list");
            capabilityModelList.stream().map(e -> (JSONObject) e).forEach(e -> map.put(e.getInteger("type"), e.getInteger("score")));
            JSONArray list = AppReceptionLinkScoreScene.builder().dataCycleType(dataCycleType).startDate(startDate).endDate(endDate).salesId(saleId).build().invoke(visitor).getJSONArray("list");
            list.stream().map(e -> (JSONObject) e).forEach(e -> {
                int value = getValueByKey(map, e.getInteger("type"));
                int type = e.getInteger("type");
                int personAverageScore = e.getInteger("person_average_score");
                Preconditions.checkArgument(personAverageScore == value, "我的能力模型" + type + "得分为：" + value + "   各环节得分" + type + "得分为：" + personAverageScore);
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("app-我的能力模型各项数值=各环节得分的平均分");
        }
    }

    public <T> Integer getValueByKey(@NotNull Map<Integer, Integer> map, T type) {
        return map.keySet().stream().filter(e -> e.equals(type)).map(map::get).collect(Collectors.toList()).get(0);
    }

    @Test
    public void receptionCriticism_data_3() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Integer dataCycleType = 0;
            String startDate = "";
            String endDate = "";
            String saleId = "";
            int averageData = AppOverviewScene.builder().dataCycleType(dataCycleType).startDate(startDate).endDate(endDate).salesId(saleId).build().invoke(visitor).getInteger("average_score");
            JSONArray capabilityModelList = AppCapabilityModelScene.builder().dataCycleType(dataCycleType).startDate(startDate).endDate(endDate).salesId(saleId).build().invoke(visitor).getJSONArray("list");
            int scoreSum = capabilityModelList.stream().map(e -> (JSONObject) e).mapToInt(e -> e.getInteger("score")).sum();
            int mathResult = CommonUtil.getIntValue(scoreSum, 5, 0);
            Preconditions.checkArgument(averageData == mathResult, "接待平均分为：" + averageData + "能力模型平均分计算结果：" + mathResult);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("app-我的能力模型各项数值=各环节得分的平均分");
        }
    }


    @Test
    public void receptionCriticism_data_50() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String receptionStartDate = DateTimeUtil.addDayFormat(new Date(), -1);
            String receptionEndDate = DateTimeUtil.getFormat(new Date());
            IScene scoreTrendScene = EvaluateV4ScoreTrendScene.builder().receptionStart(receptionStartDate).receptionEnd(receptionEndDate).build();
//            EvaluateV4DetailScene.builder().



        } catch (Exception | AssertionError e) {
            collectMessage(e);
        }
    }


}
