package com.haisheng.framework.testng.bigScreen.itemYuntong.caseonline.wm;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumChecklistUser;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumJobName;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumTestProduce;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.bean.app.departmentdata.*;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.bean.app.personaldata.AppPersonalCapabilityModelBean;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.bean.app.personaldata.AppPersonalOverviewBean;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.bean.app.personaldata.AppReceptionLinkScoreBean;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.bean.app.personaldata.AppReceptionScoreTrendBean;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.bean.app.voicerecord.AppDepartmentPageBean;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.bean.app.voicerecord.AppDetailBean;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.bean.app.voicerecord.AppPersonalPageBean;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.bean.pc.manage.VoiceEvaluationPageBean;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.dataprovider.DataClass;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.enumerate.EnumDataCycleType;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.app.departmentdata.*;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.app.personaldata.AppPersonDataReceptionAverageScoreTrendScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.app.personaldata.AppPersonalCapabilityModelScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.app.personaldata.AppPersonalReceptionScoreTrendScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.app.personaldata.AppReceptionLinkScoreScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.app.speechtechnique.AppStandardListScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.app.voicerecord.AppDetailScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.manage.VoiceDetailScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.manage.VoiceEvaluationPageScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.speechtechnique.SpeechTechniquePageScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.util.SceneUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.EnumAccount;
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
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * app会听测试用例
 *
 * @author wangmin
 * @date 2021/1/29 11:17
 */
public class AppVoiceCase extends TestCaseCommon implements TestCaseStd {
    private static final EnumTestProduce PRODUCE = EnumTestProduce.YT_ONLINE_CONTROL;
    private static final EnumAccount ALL_AUTHORITY = EnumAccount.YT_ALL_ONLINE;
    public VisitorProxy visitor = new VisitorProxy(PRODUCE);
    public SceneUtil util = new SceneUtil(visitor);
    private static final String startDate = DateTimeUtil.addDayFormat(new Date(), -1);
    private static final String endDate = DateTimeUtil.addDayFormat(new Date(), -1);
    private static final Integer dataCycleType = EnumDataCycleType.CUSTOM.getId();

    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before class initial");
        //替换checklist的相关信息
        CommonConfig commonConfig = new CommonConfig();
        commonConfig.dingHook = DingWebhook.CAR_OPEN_MANAGEMENT_PLATFORM_GRP;
        commonConfig.checklistAppId = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_CRM_ONLINE_SERVICE;
        commonConfig.checklistQaOwner = EnumChecklistUser.WM.getName();
        //替换jenkins-job的相关信息
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.YUNTONG_ONLINE_TEST.getJobName());
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

    //ok
    @Test(description = "APP【部门接待评鉴】平均接待时长=APP【部门接待评鉴】总接待时长/接待次数")
    public void department_data_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene scene = AppOverviewScene.builder().dataCycleType(dataCycleType).startDate(startDate).endDate(endDate).build();
            AppOverviewBean appOverviewBean = util.toJavaObject(scene, AppOverviewBean.class);
            int count = appOverviewBean.getCount();
            int totalDuration = appOverviewBean.getTotalDuration();
            int averageDuration = appOverviewBean.getAverageDuration();
            int mathResult = CommonUtil.getCeilIntRatio(totalDuration, count);
            CommonUtil.valueView(averageDuration, mathResult);
            Preconditions.checkArgument(averageDuration == mathResult, "APP【部门接待评鉴】平均接待时长：" + averageDuration + " APP【部门接待评鉴】总接待时长/接待次数：" + mathResult);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("APP【部门接待评鉴】平均接待时长=APP【部门接待评鉴】总接待时长/接待次数");
        }
    }

    //bug
    @Test(description = "APP【部门接待评鉴】总接待时长=PC【语音接待评鉴】所有接待时长之和")
    public void department_data_2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene appOverviewScene = AppOverviewScene.builder().dataCycleType(dataCycleType).startDate(startDate).endDate(endDate).build();
            int totalDuration = util.toJavaObject(appOverviewScene, AppOverviewBean.class).getTotalDuration();
            IScene scene = VoiceEvaluationPageScene.builder().receptionStart(startDate).receptionEnd(endDate).build();
            List<VoiceEvaluationPageBean> evaluationPageBeanList = util.toJavaObjectList(scene, VoiceEvaluationPageBean.class);
            int receptionTimeSum = evaluationPageBeanList.stream().map(VoiceEvaluationPageBean::getReceptionDuration).mapToInt(DateTimeUtil::timeToSecond).sum();
            int mathResult = CommonUtil.getCeilIntRatio(receptionTimeSum, 60);
            CommonUtil.valueView(totalDuration, mathResult);
            Preconditions.checkArgument(totalDuration >= mathResult - 5 && totalDuration <= mathResult + 5, "APP【部门接待评鉴】总接待时长：" + totalDuration + " PC【语音接待评鉴】所有接待时长之和：" + mathResult);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("APP【部门接待评鉴】总接待时长=PC【语音接待评鉴】所有接待时长之和");
        }
    }

    //ok
    @Test(description = "APP【部门接待评鉴】总接待时长=APP【员工接待评鉴】列表中接待时长之和")
    public void department_data_3() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene appOverviewScene = AppOverviewScene.builder().dataCycleType(dataCycleType).startDate(startDate).endDate(endDate).build();
            int totalDuration = util.toJavaObject(appOverviewScene, AppOverviewBean.class).getTotalDuration();
            List<AppDepartmentPageBean> departmentPageBeanList = util.getAppDepartmentPageList(dataCycleType, startDate, endDate);
            long receptionDurationSum = departmentPageBeanList.stream().mapToLong(AppDepartmentPageBean::getReceptionDuration).sum();
            CommonUtil.valueView(totalDuration, receptionDurationSum);
            Preconditions.checkArgument(totalDuration <= receptionDurationSum + 5 && totalDuration >= receptionDurationSum - 5, "APP【部门接待评鉴】总接待时长：" + totalDuration + " APP【员工接待评鉴】列表中接待时长之和：" + receptionDurationSum);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("APP【部门接待评鉴】总接待时长=APP【员工接待评鉴】列表中接待时长之和");
        }
    }

    //bug
    @Test(description = "APP【部门接待评鉴】接待次数=PC【语音接待评鉴】列表数")
    public void department_data_4() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene appOverviewScene = AppOverviewScene.builder().dataCycleType(dataCycleType).startDate(startDate).endDate(endDate).build();
            int count = util.toJavaObject(appOverviewScene, AppOverviewBean.class).getCount();
            int total = VoiceEvaluationPageScene.builder().receptionStart(startDate).receptionEnd(endDate).build().invoke(visitor).getInteger("total");
            CommonUtil.valueView(count, total);
            Preconditions.checkArgument(count == total, "APP【部门接待评鉴】接待次数：" + count + " PC【语音接待评鉴】列表数：" + total);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("APP【部门接待评鉴】接待次数=PC【语音接待评鉴】列表数");
        }
    }

    //ok
    @Test(description = "APP【部门接待评鉴】接待次数=APP【员工接待评鉴】列表中接待次数之和")
    public void department_data_5() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene appOverviewScene = AppOverviewScene.builder().dataCycleType(dataCycleType).startDate(startDate).endDate(endDate).build();
            int count = util.toJavaObject(appOverviewScene, AppOverviewBean.class).getCount();
            int receptionTimesSum = util.getAppDepartmentPageList(dataCycleType, startDate, endDate).stream().mapToInt(AppDepartmentPageBean::getReceptionTimes).sum();
            CommonUtil.valueView(count, receptionTimesSum);
            Preconditions.checkArgument(count == receptionTimesSum, "APP【部门接待评鉴】接待次数：" + count + " APP【员工接待评鉴】列表中接待次数之和：" + receptionTimesSum);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("APP【部门接待评鉴】接待次数=APP【员工接待评鉴】列表中接待次数之和");
        }
    }

    //bug
    @Test(description = "APP【部门接待评鉴】接待次数=APP每个员工的【接待评分详情】列表之和")
    public void department_data_6() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene appOverviewScene = AppOverviewScene.builder().dataCycleType(dataCycleType).startDate(startDate).endDate(endDate).build();
            int count = util.toJavaObject(appOverviewScene, AppOverviewBean.class).getCount();
            int receptionRecordSum = util.getAppDepartmentPageList(dataCycleType, startDate, endDate).stream().map(e -> util.getAppPersonalPageList(dataCycleType, e.getSaleId(), startDate, endDate).size()).mapToInt(e -> e).sum();
            CommonUtil.valueView(count, receptionRecordSum);
            Preconditions.checkArgument(count == receptionRecordSum, "APP【部门接待评鉴】接待次数：" + count + " APP每个员工的【接待评分详情】列表之和：" + receptionRecordSum);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("APP【部门接待评鉴】接待次数=APP每个员工的【接待评分详情】列表之和");
        }
    }

    //ok
    @Test(description = "APP【部门接待评鉴】接待平均分=PC【语音接待评鉴】列表中首次进店&评分完成的得分平均数")
    public void department_data_7() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene appOverviewScene = AppOverviewScene.builder().dataCycleType(dataCycleType).startDate(startDate).endDate(endDate).build();
            int averageData = util.toJavaObject(appOverviewScene, AppOverviewBean.class).getAverageScore();
            IScene evaluationPageScene = VoiceEvaluationPageScene.builder().enterStatus(1).evaluateStatus(500).receptionStart(startDate).receptionEnd(endDate).build();
            List<VoiceEvaluationPageBean> evaluationPageBeanList = util.toJavaObjectList(evaluationPageScene, VoiceEvaluationPageBean.class);
            int evaluateScoreSum = evaluationPageBeanList.stream().mapToInt(VoiceEvaluationPageBean::getEvaluateScore).sum();
            int mathResult = CommonUtil.getCeilIntRatio(evaluateScoreSum, evaluationPageBeanList.size());
            CommonUtil.valueView(averageData, mathResult);
            Preconditions.checkArgument(averageData == mathResult, "APP【部门接待评鉴】接待平均分：" + averageData + " PC【语音接待评鉴】列表中首次进店&评分完成的得分平均数：" + mathResult);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("APP【部门接待评鉴】接待平均分=PC【语音接待评鉴】列表中首次进店&评分完成的得分平均数");
        }
    }

    //ok
    @Test(description = "APP【部门接待评鉴】接待平均分=APP【销售接待能力模型】中各话术数值之和/5")
    public void department_data_8() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene appOverviewScene = AppOverviewScene.builder().dataCycleType(dataCycleType).startDate(startDate).endDate(endDate).build();
            int averageData = util.toJavaObject(appOverviewScene, AppOverviewBean.class).getAverageScore();
            IScene scene = AppCapabilityModelScene.builder().dataCycleType(dataCycleType).startDate(startDate).endDate(endDate).build();
            int scoreSum = util.toJavaObjectList(scene, AppCapabilityModelBean.class, "list").stream().mapToInt(AppCapabilityModelBean::getScore).sum();
            int mathResult = CommonUtil.getCeilIntRatio(scoreSum, 5);
            CommonUtil.valueView(averageData, mathResult);
            Preconditions.checkArgument(averageData <= mathResult + 1 && averageData >= mathResult - 1, "APP【部门接待评鉴】接待平均分数为：" + averageData + " APP【销售接待能力模型】中各话术数值之和/5：" + mathResult);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("APP【部门接待评鉴】接待平均分=APP【销售接待能力模型】中各话术数值之和/5");
        }
    }

    //数据有问题（分母刨去分数为0的人）
    @Test(description = "APP【部门接待评鉴】接待平均分=APP每个【员工接待评鉴】接待平均分之和/总人数")
    public void department_data_9() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene appOverviewScene = AppOverviewScene.builder().dataCycleType(dataCycleType).startDate(startDate).endDate(endDate).build();
            int averageData = util.toJavaObject(appOverviewScene, AppOverviewBean.class).getAverageScore();
            List<AppDepartmentPageBean> appDepartmentPageBeanList = util.getAppDepartmentPageList(dataCycleType, startDate, endDate);
            List<AppPersonalOverviewBean> personalOverviewBeanList = appDepartmentPageBeanList.stream().map(e -> util.getAppPersonalOverview(dataCycleType, e.getSaleId(), startDate, endDate)).filter(e -> e.getAverageScore() != 0).collect(Collectors.toList());
            long totalScoreSum = personalOverviewBeanList.stream().mapToLong(AppPersonalOverviewBean::getAverageScore).sum();
            int mathResult = CommonUtil.getCeilIntRatio(Math.toIntExact(totalScoreSum), personalOverviewBeanList.size());
            CommonUtil.valueView(averageData, mathResult);
            Preconditions.checkArgument(averageData <= mathResult + 1 && averageData >= mathResult - 1, "APP【部门接待评鉴】接待平均分：" + averageData + " APP每个【员工接待评鉴】接待平均分之和/总人数：" + mathResult);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("APP【部门接待评鉴】接待平均分=APP每个【员工接待评鉴】接待平均分之和/总人数");
        }
    }

    //ok
    @Test(description = "APP【部门接待评鉴】接待平均分=APP每个【员工接待详情】本次接待得分加和（首次进店&本次得分！=0）/接待次数")
    public void department_data_10() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene appOverviewScene = AppOverviewScene.builder().dataCycleType(dataCycleType).startDate(startDate).endDate(endDate).build();
            int averageData = util.toJavaObject(appOverviewScene, AppOverviewBean.class).getAverageScore();
            List<AppDepartmentPageBean> appDepartmentPageList = util.getAppDepartmentPageList(dataCycleType, startDate, endDate);
            List<AppDetailBean> appDetailList = new ArrayList<>();
            appDepartmentPageList.stream().map(e -> util.getAppPersonalPageList(dataCycleType, e.getSaleId(), startDate, endDate))
                    .forEach(e -> e.stream().map(personalPage -> util.getAppVoiceRecordDetail(personalPage.getId()))
                            .filter(appDetail -> appDetail.getEnterStatusName().equals("首次进店"))
                            .filter(appDetail -> appDetail.getAverageScore() != 0).forEach(appDetailList::add));
            //总平均分总和
            int averageScoreSum = appDetailList.stream().mapToInt(AppDetailBean::getAverageScore).sum();
            int mathResult = CommonUtil.getCeilIntRatio(averageScoreSum, appDetailList.size());
            //各接待环节加分总和
            int scoreSum = appDetailList.stream().mapToInt(e -> e.getScores().stream().map(obj -> (JSONObject) obj).mapToInt(obj -> obj.getInteger("score")).sum()).sum();
            int averageScore = CommonUtil.getCeilIntRatio(scoreSum, 5);
            int mathResult2 = CommonUtil.getCeilIntRatio(averageScore, appDetailList.size());
            CommonUtil.valueView(averageData, mathResult, mathResult2);
            Preconditions.checkArgument(averageData <= mathResult + 1 && averageData >= mathResult - 1, "APP【部门接待评鉴】接待平均分：" + averageData + " APP每个员工【接待详情】本次接待得分加和/接待次数：" + mathResult);
            Preconditions.checkArgument(averageData <= mathResult2 + 1 && averageData >= mathResult2 - 1, "APP【部门接待评鉴】接待平均分：" + averageData + " APP每个员工【接待详情】各项得分加和/5/接待次数：" + mathResult2);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("APP【部门接待评鉴】接待平均分=APP每个【员工接待详情】本次接待得分加和（首次进店&本次得分！=0）/接待次数");
        }
    }

    //ok
    @Test(description = "APP【部门接待评鉴】接待平均分=APP【销售接待能力模型】中各话术数值之和/5")
    public void department_data_11() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene appOverviewScene = AppOverviewScene.builder().dataCycleType(dataCycleType).startDate(startDate).endDate(endDate).build();
            int averageData = util.toJavaObject(appOverviewScene, AppOverviewBean.class).getAverageScore();
            IScene scene = AppCapabilityModelScene.builder().dataCycleType(dataCycleType).startDate(startDate).endDate(endDate).build();
            int scoreSum = util.toJavaObjectList(scene, AppCapabilityModelBean.class, "list").stream().mapToInt(AppCapabilityModelBean::getScore).sum();
            int mathResult = CommonUtil.getCeilIntRatio(scoreSum, 5);
            CommonUtil.valueView(averageData, mathResult);
            Preconditions.checkArgument(averageData <= mathResult + 1 && averageData >= mathResult - 1, "APP【部门接待评鉴】接待平均分：" + averageData + " APP【销售接待能力模型】中各话术数值之和/5：" + mathResult);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("APP【部门接待评鉴】接待平均分=APP【销售接待能力模型】中各话术数值之和/5");
        }
    }

    //ok
    @Test(description = "APP【销售接待能力模型】各话术环节分数相加/5=APP每个【员工接待评鉴】接待平均分之和（不为0）/人数")
    public void department_data_12() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene scene = AppCapabilityModelScene.builder().dataCycleType(dataCycleType).startDate(startDate).endDate(endDate).build();
            int scoreSum = util.toJavaObjectList(scene, AppCapabilityModelBean.class, "list").stream().mapToInt(AppCapabilityModelBean::getScore).sum();
            int mathResult = CommonUtil.getCeilIntRatio(scoreSum, 5);
            List<AppDepartmentPageBean> appDepartmentPageBeanList = util.getAppDepartmentPageList(dataCycleType, startDate, endDate);
            List<AppPersonalOverviewBean> personalOverviewList = appDepartmentPageBeanList.stream().map(e -> util.getAppPersonalOverview(dataCycleType, e.getSaleId(), startDate, endDate))
                    .filter(e -> e.getAverageScore() != 0).collect(Collectors.toList());
            long totalScoreSum = personalOverviewList.stream().mapToLong(AppPersonalOverviewBean::getAverageScore).sum();
            int mathResult2 = CommonUtil.getCeilIntRatio(Math.toIntExact(totalScoreSum), personalOverviewList.size());
            CommonUtil.valueView(mathResult, mathResult2);
            Preconditions.checkArgument(mathResult <= mathResult2 + 1 && mathResult >= mathResult2 - 1, "APP【销售接待能力模型】各话术环节分数相加/5：" + mathResult + " APP每个【员工接待评鉴】接待平均分之和/人数：" + mathResult2);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("APP【销售接待能力模型】各话术环节分数相加/5=APP每个【员工接待评鉴】接待平均分之和（不为0）/人数");
        }
    }

    //ok
    @Test(description = "APP【销售接待能力模型】的各环节平均分=APP【环节平均分】中各环节的部门平均分", dataProvider = "receptionType", dataProviderClass = DataClass.class)
    public void department_data_13(String name, int type) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene capabilityModelScene = AppCapabilityModelScene.builder().dataCycleType(dataCycleType).startDate(startDate).endDate(endDate).build();
            JSONArray modelList = capabilityModelScene.invoke(visitor).getJSONArray("list");
            int modelScore = modelList.stream().map(obj -> (JSONObject) obj).filter(obj -> obj.getInteger("type").equals(type)).map(e -> e.getInteger("score")).findFirst().orElse(0);
            IScene linkDataCarouselScene = AppLinkDataCarouselScene.builder().dataCycleType(dataCycleType).startDate(startDate).endDate(endDate).build();
            JSONArray linkList = linkDataCarouselScene.invoke(visitor).getJSONArray("list");
            int linkScore = linkList.size() == 0 ? 0 : linkList.stream().map(obj -> (JSONObject) obj).filter(obj -> obj.getInteger("type").equals(type)).map(obj -> obj.getInteger("average_score")).findFirst().orElse(0);
            CommonUtil.valueView(modelScore, linkScore);
            Preconditions.checkArgument(modelScore == linkScore, "APP【销售接待能力模型】的" + name + "环节平均分：" + modelScore + " APP【环节平均分】中" + name + "环节的部门平均分" + linkScore);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("APP【销售接待能力模型】的各环节平均分=APP【环节平均分】中各环节的部门平均分");
        }
    }

    //有问题
    @Test(description = "APP【销售接待能力模型】各话术环节分数相加/5=APP每个员工【接待详情】中的部门平均分")
    public void department_data_14() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene scene = AppCapabilityModelScene.builder().dataCycleType(dataCycleType).startDate(startDate).endDate(endDate).build();
            int scoreSum = util.toJavaObjectList(scene, AppCapabilityModelBean.class, "list").stream().mapToInt(AppCapabilityModelBean::getScore).sum();
            int mathResult = CommonUtil.getCeilIntRatio(scoreSum, 5);
            List<AppDepartmentPageBean> departmentPageList = util.getAppDepartmentPageList(dataCycleType, startDate, endDate);
            List<AppPersonalPageBean> personalPageList = new ArrayList<>();
            departmentPageList.stream().map(e -> util.getAppPersonalPageList(dataCycleType, e.getSaleId(), startDate, endDate)).forEach(personalPageList::addAll);
            personalPageList.stream().map(e -> util.getAppVoiceRecordDetail(e.getId()))
                    .filter(e -> e.getEnterStatusName().equals("首次进店"))
                    .filter(e -> e.getDepartmentAverageScore() != null)
                    .forEach(e -> Preconditions.checkArgument(mathResult == e.getDepartmentAverageScore(), "APP【销售接待能力模型】各话术环节分数相加/5：" + mathResult + " APP每个员工【接待详情】中的部门平均分：" + e.getDepartmentAverageScore()));
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("APP【销售接待能力模型】各话术环节分数相加/5=APP每个员工【接待详情】中的部门平均分");
        }
    }

    //ok
    @Test(description = "APP【环节平均分】中各环节的合格线=后台配置60分", dataProvider = "receptionType", dataProviderClass = DataClass.class)
    public void department_data_15(String name, int type) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray array = AppLinkDataCarouselScene.builder().dataCycleType(dataCycleType).startDate(startDate).endDate(endDate).build().invoke(visitor).getJSONArray("list");
            array.stream().map(e -> (JSONObject) e).filter(e -> e.getInteger("type").equals(type))
                    .forEach(e -> Preconditions.checkArgument(e.getInteger("qualified_score") == 60, "APP【环节平均分】中" + name + "环节的合格线为：" + e.getInteger("qualified_score")));
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("APP【环节平均分】中各环节的合格线=后台配置60分");
        }
    }

    //ok
    @Test(description = "APP【环节平均分】中全部环节的合格率=APP【接待详情】中本次接待分数>=60分的次数/接待次数（首次接待&本次接待！=0）")
    public void department_data_16() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene scene = AppLinkDataCarouselScene.builder().dataCycleType(dataCycleType).startDate(startDate).endDate(endDate).build();
            List<AppLinkDataCarouselBean> linkDataCarouselList = util.toJavaObjectList(scene, AppLinkDataCarouselBean.class, "list");
            int qualifiedRatio = linkDataCarouselList.size() == 0 ? 0 : linkDataCarouselList.stream().filter(e -> e.getTypeName().equals("全部环节")).map(AppLinkDataCarouselBean::getQualifiedRatio).findFirst().orElse(0);
            //员工评鉴列表
            List<AppDepartmentPageBean> departmentPageList = util.getAppDepartmentPageList(dataCycleType, startDate, endDate);
            //员工接待列表
            List<AppPersonalPageBean> personalPageList = new ArrayList<>();
            departmentPageList.stream().map(e -> util.getAppPersonalPageList(dataCycleType, e.getSaleId(), startDate, endDate)).forEach(personalPageList::addAll);
            //员工接待详情中首次接待的条数
            List<AppDetailBean> detailList = new ArrayList<>();
            personalPageList.stream().map(e -> util.getAppVoiceRecordDetail(e.getId())).filter(e -> e.getEnterStatusName().equals("首次进店") && e.getAverageScore() != 0).forEach(detailList::add);
            //接待详情中分数超过60的
            int passCount = (int) detailList.stream().filter(e -> e.getAverageScore() != null && e.getAverageScore() >= 60).count();
            int mathResult = CommonUtil.getIntPercent(passCount, detailList.size());
            CommonUtil.valueView(qualifiedRatio, mathResult);
            Preconditions.checkArgument(qualifiedRatio <= mathResult + 1 && qualifiedRatio >= mathResult - 1, "APP【销售能力管理中】的合格率：" + qualifiedRatio + " APP【接待详情】中本次接待分数>=60分的次数/接待次数：" + mathResult);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("APP【销售能力管理中】的合格率=APP【接待详情】中本次接待分数>=60分的次数/接待次数（首次接待&本次接待！=0）");
        }
    }

    //ok
    @Test(description = "APP【环节平均分】中各环节的接待合格率=APP【接待详情】中各环节接待分数>=60分的次数/接待次数（首次接待&本次接待！=0）", dataProvider = "receptionType", dataProviderClass = DataClass.class)
    public void department_data_17(String name, int type) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene scene = AppLinkDataCarouselScene.builder().dataCycleType(dataCycleType).startDate(startDate).endDate(endDate).build();
            List<AppLinkDataCarouselBean> linkDataCarouselList = util.toJavaObjectList(scene, AppLinkDataCarouselBean.class, "list");
            AppLinkDataCarouselBean linkDataCarousel = linkDataCarouselList.stream().filter(e -> e.getType().equals(type)).findFirst().orElse(null);
            //接待合格率
            int qualifiedRatio = linkDataCarousel == null ? 0 : linkDataCarousel.getQualifiedRatio();
            //员工评鉴列表
            List<AppDepartmentPageBean> departmentPageList = util.getAppDepartmentPageList(dataCycleType, startDate, endDate);
            //员工接待列表
            List<AppPersonalPageBean> personalPageList = new ArrayList<>();
            departmentPageList.stream().map(e -> util.getAppPersonalPageList(dataCycleType, e.getSaleId(), startDate, endDate)).forEach(personalPageList::addAll);
            //员工解答详情中首次接待的条数
            List<AppDetailBean> detailList = new ArrayList<>();
            personalPageList.stream().map(e -> util.getAppVoiceRecordDetail(e.getId())).filter(e -> e.getEnterStatusName().equals("首次进店") && e.getAverageScore() != 0).forEach(detailList::add);
            //接待详情中分数超过60的
            AtomicInteger i = new AtomicInteger(0);
            detailList.stream().map(AppDetailBean::getScores).forEach(array -> array.stream().map(e -> (JSONObject) e).filter(e -> e.getInteger("type").equals(type)).filter(e -> e.getInteger("score") >= 60).forEach(e -> i.getAndAdd(1)));
            int mathResult = CommonUtil.getIntPercent(i.get(), detailList.size());
            CommonUtil.valueView(qualifiedRatio, mathResult);
            Preconditions.checkArgument(qualifiedRatio == mathResult, "APP【环节平均分】中" + name + "环节的接待合格率：" + qualifiedRatio + " APP【接待详情】中" + name + "环节接待分数>=60分的次数/接待次数：" + mathResult);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("APP【环节平均分】中各环节的接待合格率=APP【接待详情】中各环节接待分数>=60分的次数/接待次数（首次接待&本次接待！=0）");
        }
    }

    //有问题
    @Test(description = "APP【环节平均分】中各环节的得分达成率=APP【接待详情】中各环节的得分>部门平均分的次数/接待次数（首次接待&本次接待！=0）", dataProvider = "receptionType", dataProviderClass = DataClass.class)
    public void department_data_18(String name, int type) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene scene = AppLinkDataCarouselScene.builder().dataCycleType(dataCycleType).startDate(startDate).endDate(endDate).build();
            List<AppLinkDataCarouselBean> linkDataCarouselList = util.toJavaObjectList(scene, AppLinkDataCarouselBean.class, "list");
            AppLinkDataCarouselBean linkDataCarousel = linkDataCarouselList.stream().filter(e -> e.getType().equals(type)).findFirst().orElse(null);
            //平均分
            int averageScore = linkDataCarousel == null ? 0 : linkDataCarousel.getAverageScore();
            //平均分达成率
            int averageRatio = linkDataCarousel == null ? 0 : linkDataCarousel.getAverageRatio();
            //员工评鉴列表
            List<AppDepartmentPageBean> departmentPageList = util.getAppDepartmentPageList(dataCycleType, startDate, endDate);
            //员工接待列表
            List<AppPersonalPageBean> personalPageList = new ArrayList<>();
            departmentPageList.stream().map(e -> util.getAppPersonalPageList(dataCycleType, e.getSaleId(), startDate, endDate)).forEach(personalPageList::addAll);
            //员工解答详情中首次接待的条数
            List<AppDetailBean> detailList = new ArrayList<>();
            personalPageList.stream().map(e -> util.getAppVoiceRecordDetail(e.getId())).filter(e -> e.getEnterStatusName().equals("首次进店") && e.getAverageScore() != 0).forEach(detailList::add);
            //接待详情中分数超过平均分的次数
            int passCount = (int) detailList.stream().filter(e -> e.getAverageScore() != null && e.getAverageScore() >= averageScore).count();
            int mathResult = CommonUtil.getIntPercent(passCount, detailList.size());
            CommonUtil.valueView(passCount, averageRatio, mathResult);
            Preconditions.checkArgument(averageRatio == mathResult, "APP【环节平均分】中" + name + "环节的平均分达成率：" + averageRatio + " APP【接待详情】中" + name + "环节的平均分>部门平均分的次数/接待次数：" + mathResult);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("APP【环节平均分】中各环节的平均分达成率=APP【接待详情】中各环节的得分>部门平均分的次数/接待次数（首次接待&本次接待！=0）");
        }
    }

    //ok
    @Test(description = "APP【部门总平均分趋势】分数=APP【各环节得分趋势】各话术环节平均分之和/5")
    public void department_data_19() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene receptionAverageScoreTrendScene = AppReceptionAverageScoreTrendScene.builder().dataCycleType(dataCycleType).startDate(startDate).endDate(endDate).build();
            AppReceptionAverageScoreTrendBean receptionAverageScoreTrend = util.toFirstJavaObject(receptionAverageScoreTrendScene, AppReceptionAverageScoreTrendBean.class);
            int totalAverageScore = receptionAverageScoreTrend == null ? 0 : receptionAverageScoreTrend.getTotalAverageScore();
            IScene receptionScoreTrendScene = AppReceptionScoreTrendScene.builder().dataCycleType(dataCycleType).startDate(startDate).endDate(endDate).build();
            AppReceptionScoreTrendBean receptionScoreTrend = util.toFirstJavaObject(receptionScoreTrendScene, AppReceptionScoreTrendBean.class);
            int scoreSum = receptionScoreTrend == null ? 0 : receptionScoreTrend.getOne() + receptionScoreTrend.getTwo() + receptionScoreTrend.getThree() + receptionScoreTrend.getFour() + receptionScoreTrend.getFive();
            int mathResult = CommonUtil.getCeilIntRatio(scoreSum, 5);
            CommonUtil.valueView(totalAverageScore, mathResult);
            Preconditions.checkArgument(mathResult <= totalAverageScore + 1 && mathResult >= totalAverageScore - 1, "APP【部门总平均分趋势】分数：" + totalAverageScore + " APP【各环节得分趋势】各话术环节平均分之和/5：" + mathResult);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("APP【部门总平均分趋势】分数=APP【各环节得分趋势】各话术环节平均分之和/5");
        }
    }

    //bug
    @Test(description = "APP【各环节得分趋势】中的各环节得分＝APP【销售接待能力模型】各环节得分", dataProvider = "receptionType", dataProviderClass = DataClass.class)
    public void department_data_20(String name, int type) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene receptionScoreTrendScene = AppReceptionScoreTrendScene.builder().dataCycleType(dataCycleType).startDate(startDate).endDate(endDate).build();
            JSONArray array = receptionScoreTrendScene.invoke(visitor).getJSONArray("list");
            int trendScore = array.size() == 0 ? 0 : array.getJSONObject(0).getInteger(String.valueOf(type));
            IScene capabilityModelScene = AppCapabilityModelScene.builder().dataCycleType(dataCycleType).startDate(startDate).endDate(endDate).build();
            List<AppCapabilityModelBean> capabilityModelList = util.toJavaObjectList(capabilityModelScene, AppCapabilityModelBean.class, "list");
            int modelScore = capabilityModelList.stream().filter(e -> e.getType().equals(type)).map(AppCapabilityModelBean::getScore).findFirst().orElse(0);
            CommonUtil.valueView(trendScore, modelScore);
            Preconditions.checkArgument(trendScore == modelScore, "APP【各环节得分趋势】中的" + name + "环节得分：" + trendScore + " APP【销售接待能力模型】" + name + "环节得分：" + modelScore);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("APP【各环节得分趋势】中的各环节得分＝APP【销售接待能力模型】各环节得分");
        }
    }

    //bug
    @Test(description = "APP【各环节得分趋势】中的各环节得分=PC【语音接待评鉴】中的各环节的平均分", dataProvider = "receptionType", dataProviderClass = DataClass.class)
    public void department_data_21(String name, int type) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene receptionScoreTrendScene = AppReceptionScoreTrendScene.builder().dataCycleType(dataCycleType).startDate(startDate).endDate(endDate).build();
            JSONArray array = receptionScoreTrendScene.invoke(visitor).getJSONArray("list");
            int trendScore = array.size() == 0 ? 0 : array.getJSONObject(0).getInteger(String.valueOf(type));
            IScene voiceEvaluationPageScene = VoiceEvaluationPageScene.builder().enterStatus(1).evaluateStatus(500).receptionStart(startDate).receptionEnd(endDate).build();
            List<JSONObject> list = new ArrayList<>();
            util.toJavaObjectList(voiceEvaluationPageScene, VoiceEvaluationPageBean.class).stream()
                    .map(e -> VoiceDetailScene.builder().id(e.getId()).build().invoke(visitor).getJSONArray("scores"))
                    .forEach(e -> e.stream().map(object -> (JSONObject) object).filter(object -> object.getInteger("type").equals(type)).filter(object -> object.getInteger("score") != 0).forEach(list::add));
            int scoreSum = list.stream().mapToInt(e -> e.getInteger("score")).sum();
            int mathResult = CommonUtil.getCeilIntRatio(scoreSum, list.size());
            CommonUtil.valueView(trendScore, mathResult);
            Preconditions.checkArgument(trendScore == mathResult, "APP【各环节得分趋势】中的" + name + "环节得分：" + trendScore + " PC【语音接待评鉴】中的" + name + "环节的平均分：" + mathResult);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("APP【各环节得分趋势】中的各环节得分=PC【语音接待评鉴】中的各环节的平均分");
        }
    }

    //ok
    @Test(description = "APP【部门总平均分趋势】平均分＝APP【销售接待能力模型】各话术环节平均分之和/5")
    public void department_data_22() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene receptionAverageScoreTrendScene = AppReceptionAverageScoreTrendScene.builder().startDate(startDate).endDate(startDate).dataCycleType(dataCycleType).build();
            AppReceptionAverageScoreTrendBean receptionAverageScoreTrend = util.toFirstJavaObject(receptionAverageScoreTrendScene, AppReceptionAverageScoreTrendBean.class);
            int totalAverageScore = receptionAverageScoreTrend == null ? 0 : receptionAverageScoreTrend.getTotalAverageScore();
            IScene scene = AppCapabilityModelScene.builder().dataCycleType(dataCycleType).startDate(startDate).endDate(endDate).build();
            int scoreSum = util.toJavaObjectList(scene, AppCapabilityModelBean.class, "list").stream().mapToInt(AppCapabilityModelBean::getScore).sum();
            int mathResult = CommonUtil.getCeilIntRatio(scoreSum, 5);
            CommonUtil.valueView(totalAverageScore, mathResult);
            Preconditions.checkArgument(scoreSum <= mathResult + 1 && scoreSum >= mathResult - 1, "APP【部门总平均分趋势】平均分：" + scoreSum + " APP【销售接待能力模型】各话术环节平均分之和/5：" + mathResult);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("APP【部门总平均分趋势】平均分＝APP【销售接待能力模型】各话术环节平均分之和/5");
        }
    }

    //ok
    @Test(description = "APP【部门总平均分趋势】分数=APP【部门接待评鉴】中的接待平均分")
    public void department_data_23() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene receptionAverageScoreTrendScene = AppReceptionAverageScoreTrendScene.builder().startDate(startDate).endDate(startDate).dataCycleType(dataCycleType).build();
            AppReceptionAverageScoreTrendBean receptionAverageScoreTrend = util.toFirstJavaObject(receptionAverageScoreTrendScene, AppReceptionAverageScoreTrendBean.class);
            int totalAverageScore = receptionAverageScoreTrend == null ? 0 : receptionAverageScoreTrend.getTotalAverageScore();
            IScene voiceEvaluationPageScene = VoiceEvaluationPageScene.builder().receptionStart(startDate).receptionEnd(startDate).evaluateStatus(500).enterStatus(1).build();
            List<VoiceEvaluationPageBean> list = util.toJavaObjectList(voiceEvaluationPageScene, VoiceEvaluationPageBean.class);
            int evaluateScoreSum = list.stream().mapToInt(VoiceEvaluationPageBean::getEvaluateScore).sum();
            int mathResult = CommonUtil.getCeilIntRatio(evaluateScoreSum, list.size());
            CommonUtil.valueView(totalAverageScore, mathResult);
            Preconditions.checkArgument(totalAverageScore <= mathResult + 1 && totalAverageScore >= mathResult - 1, "APP【部门总平均分趋势】分数：" + totalAverageScore + " APP【部门接待评鉴】中的接待平均分：" + mathResult);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("APP【部门总平均分趋势】分数=APP【部门接待评鉴】中的接待平均分");
        }
    }

    //ok
    @Test(description = "APP【员工接待评鉴】中的接待平均分＝APP【个人总平均分值趋势】中的总平均分")
    public void department_data_24() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene receptionAverageScoreTrendScene = AppReceptionAverageScoreTrendScene.builder().startDate(startDate).endDate(startDate).dataCycleType(dataCycleType).build();
            AppReceptionAverageScoreTrendBean receptionAverageScoreTrend = util.toFirstJavaObject(receptionAverageScoreTrendScene, AppReceptionAverageScoreTrendBean.class);
            int totalAverageScore = receptionAverageScoreTrend == null ? 0 : receptionAverageScoreTrend.getTotalAverageScore();
            IScene scene = AppOverviewScene.builder().dataCycleType(dataCycleType).startDate(startDate).endDate(endDate).build();
            AppOverviewBean overview = util.toJavaObject(scene, AppOverviewBean.class);
            int averageScore = overview.getAverageScore();
            CommonUtil.valueView(totalAverageScore, averageScore);
            Preconditions.checkArgument(totalAverageScore <= averageScore + 1 && totalAverageScore >= averageScore - 1, "APP【员工接待评鉴】中的接待平均分：" + totalAverageScore + " APP【个人总平均分值趋势】中的总平均分：" + averageScore);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("APP【员工接待评鉴】中的接待平均分＝APP【个人总平均分值趋势】中的总平均分");
        }
    }

    //ok
    @Test(description = "APP【员工接待评鉴】中的接待次数=APP【接待评分详情】列表条数")
    public void department_data_25() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            List<AppDepartmentPageBean> list = util.getAppDepartmentPageList(dataCycleType, startDate, endDate);
            list.forEach(e -> {
                int count = util.getAppPersonalOverview(dataCycleType, e.getSaleId(), startDate, endDate).getCount();
                int receptionCount = util.getAppPersonalPageList(dataCycleType, e.getSaleId(), startDate, endDate).size();
                Preconditions.checkArgument(count == receptionCount, e.getName() + " APP【员工接待评鉴】中的接待次数：" + count + " APP【接待评分详情】列表条数：" + receptionCount);
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("APP【部门接待评鉴】中的接待次数=APP每人【接待评分详情】列表条数之和");
        }
    }

    //ok
    @Test(description = "APP【员工接待评鉴】中的平均接待时长＝APP【员工接待评鉴】中的接待时长/接待次数")
    public void department_data_27() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            List<AppDepartmentPageBean> departmentPageList = util.getAppDepartmentPageList(dataCycleType, startDate, endDate);
            departmentPageList.forEach(e -> {
                AppPersonalOverviewBean personalOverview = util.getAppPersonalOverview(dataCycleType, e.getSaleId(), startDate, endDate);
                int averageDuration = personalOverview.getAverageDuration();
                int mathResult = CommonUtil.getCeilIntRatio(personalOverview.getTotalDuration(), personalOverview.getCount());
                CommonUtil.valueView(averageDuration, mathResult);
                Preconditions.checkArgument(averageDuration == mathResult, e.getName() + " APP【员工接待评鉴】中的平均接待时长：" + averageDuration + " APP【员工接待评鉴】中的接待时长/接待次数：" + mathResult);
                CommonUtil.logger(e.getName());
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("APP【员工接待评鉴】中的平均接待时长＝APP【员工接待评鉴】中的接待时长/接待次数");
        }
    }

    //ok
    @Test(description = "APP【员工接待评鉴】的接待平均分=APP【我的能力模型】各话术环节分数相加/5")
    public void personal_data_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            List<AppDepartmentPageBean> departmentPageList = util.getAppDepartmentPageList(dataCycleType, startDate, endDate);
            departmentPageList.forEach(e -> {
                AppPersonalOverviewBean personalOverview = util.getAppPersonalOverview(dataCycleType, e.getSaleId(), startDate, endDate);
                int averageScore = personalOverview.getAverageScore();
                IScene scene = AppPersonalCapabilityModelScene.builder().dataCycleType(dataCycleType).salesId(e.getSaleId()).startDate(startDate).endDate(endDate).build();
                int scoreSum = util.toJavaObjectList(scene, AppPersonalCapabilityModelBean.class, "list").stream().mapToInt(AppPersonalCapabilityModelBean::getScore).sum();
                int mathResult = CommonUtil.getCeilIntRatio(scoreSum, 5);
                CommonUtil.valueView(e.getName(), averageScore, mathResult);
                Preconditions.checkArgument(averageScore <= mathResult + 1 && averageScore >= mathResult - 1, e.getName() + " APP【员工接待评鉴】的接待平均分：" + averageScore + " APP【我的能力模型】各话术环节分数相加/5：" + mathResult);
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("APP【员工接待评鉴】的接待平均分=APP【我的能力模型】各话术环节分数相加/5");
        }
    }

    //ok
    @Test(description = "APP【员工接待评鉴】的接待平均分=APP【我的各环节得分】个人总平均分之和/5")
    public void personal_data_2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            List<AppDepartmentPageBean> departmentPageList = util.getAppDepartmentPageList(dataCycleType, startDate, endDate);
            departmentPageList.forEach(e -> {
                AppPersonalOverviewBean personalOverview = util.getAppPersonalOverview(dataCycleType, e.getSaleId(), startDate, endDate);
                int averageScore = personalOverview.getAverageScore();
                IScene receptionLinkScoreScene = AppReceptionLinkScoreScene.builder().dataCycleType(dataCycleType).salesId(e.getSaleId()).startDate(startDate).endDate(endDate).build();
                List<AppReceptionLinkScoreBean> receptionLinkScoreList = util.toJavaObjectList(receptionLinkScoreScene, AppReceptionLinkScoreBean.class, "list");
                int personAverageScoreSum = receptionLinkScoreList.stream().mapToInt(AppReceptionLinkScoreBean::getPersonAverageScore).sum();
                int mathResult = CommonUtil.getCeilIntRatio(personAverageScoreSum, 5);
                CommonUtil.valueView(e.getName(), averageScore, mathResult);
                Preconditions.checkArgument(averageScore <= mathResult + 1 && averageScore >= mathResult - 1, e.getName() + " APP【员工接待评鉴】的接待平均分：" + averageScore + " APP【我的各环节得分】个人总平均分之和/5：" + mathResult);
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("APP【员工接待评鉴】的接待平均分=APP【我的各环节得分】个人总平均分之和/5");
        }
    }

    //ok
    @Test(description = "APP【我的能力模型】各话术环节的分值＝APP【我的各环节得分】平均分的分值", dataProvider = "receptionType", dataProviderClass = DataClass.class)
    public void personal_data_3(String name, int type) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            List<AppDepartmentPageBean> departmentPageList = util.getAppDepartmentPageList(dataCycleType, startDate, endDate);
            departmentPageList.forEach(e -> {
                IScene personalCapabilityModelScene = AppPersonalCapabilityModelScene.builder().salesId(e.getSaleId()).dataCycleType(dataCycleType).startDate(startDate).endDate(endDate).build();
                List<AppPersonalCapabilityModelBean> personalCapabilityModelList = util.toJavaObjectList(personalCapabilityModelScene, AppPersonalCapabilityModelBean.class, "list");
                int modelScore = personalCapabilityModelList.stream().filter(a -> a.getType().equals(type)).map(AppPersonalCapabilityModelBean::getScore).findFirst().orElse(0);
                IScene receptionLinkScoreScene = AppReceptionLinkScoreScene.builder().dataCycleType(dataCycleType).salesId(e.getSaleId()).startDate(startDate).endDate(endDate).build();
                List<AppReceptionLinkScoreBean> receptionLinkScoreList = util.toJavaObjectList(receptionLinkScoreScene, AppReceptionLinkScoreBean.class, "list");
                int personAverageScore = receptionLinkScoreList.stream().filter(a -> a.getType().equals(type)).map(AppReceptionLinkScoreBean::getPersonAverageScore).findFirst().orElse(0);
                CommonUtil.valueView(e.getName(), modelScore, personAverageScore);
                Preconditions.checkArgument(modelScore == personAverageScore, "APP【我的能力模型】各话术环节的分值" + name + "环节的分值：" + modelScore + " APP【我的各环节得分】" + name + "环节的平均分分值：" + personAverageScore);
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("APP【我的能力模型】各话术环节的分值＝APP【我的各环节得分】平均分的分值");
        }
    }

    //ok
    @Test(description = "APP【我的能力模型】各话术环节的分值＝PC【语音接待评鉴】接待详情中的各环节的分值", dataProvider = "receptionType", dataProviderClass = DataClass.class)
    public void personal_data_5(String name, int type) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            List<AppDepartmentPageBean> departmentPageList = util.getAppDepartmentPageList(dataCycleType, startDate, endDate);
            departmentPageList.forEach(e -> {
                IScene personalCapabilityModelScene = AppPersonalCapabilityModelScene.builder().salesId(e.getSaleId()).dataCycleType(dataCycleType).startDate(startDate).endDate(endDate).build();
                List<AppPersonalCapabilityModelBean> personalCapabilityModelList = util.toJavaObjectList(personalCapabilityModelScene, AppPersonalCapabilityModelBean.class, "list");
                int modelScore = personalCapabilityModelList.stream().filter(a -> a.getType().equals(type)).map(AppPersonalCapabilityModelBean::getScore).findFirst().orElse(0);
                IScene evaluationPageScene = VoiceEvaluationPageScene.builder().receptorName(e.getName()).evaluateStatus(500).enterStatus(1).receptionStart(startDate).receptionEnd(endDate).build();
                List<VoiceEvaluationPageBean> voiceEvaluationPageList = util.toJavaObjectList(evaluationPageScene, VoiceEvaluationPageBean.class);
                List<Integer> list = new ArrayList<>();
                voiceEvaluationPageList.stream().filter(Objects::nonNull).map(voiceRecord -> VoiceDetailScene.builder().id(voiceRecord.getId()).build().invoke(visitor).getJSONArray("scores"))
                        .forEach(scores -> scores.stream().map(object -> (JSONObject) object).filter(object -> object.getInteger("type").equals(type))
                                .filter(object -> object.getInteger("score") != null).map(object -> object.getInteger("score")).forEach(list::add));
                int scoreSum = list.stream().mapToInt(a -> a).sum();
                int mathResult = CommonUtil.getCeilIntRatio(scoreSum, list.size());
                CommonUtil.valueView(e.getName(), modelScore, mathResult);
                Preconditions.checkArgument(modelScore <= mathResult + 1 && modelScore >= mathResult - 1, "APP【我的能力模型】" + name + "环节的分值：" + modelScore + " PC【语音接待评鉴】接待详情中" + name + "环节的平均分：" + mathResult);
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("APP【我的能力模型】各话术环节的分值＝PC【语音接待评鉴】接待详情中的各环节的分值");
        }
    }

    //ok
    @Test(description = "APP【个人总平均分趋势】中分数=APP【各环节得分】各环节总平均分相加/5")
    public void personal_data_6() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            List<AppDepartmentPageBean> list = util.getAppDepartmentPageList(dataCycleType, startDate, startDate);
            list.forEach(e -> {
                JSONObject response = AppPersonDataReceptionAverageScoreTrendScene.builder().dataCycleType(dataCycleType).startDate(startDate).endDate(startDate).salesId(e.getSaleId()).build().invoke(visitor);
                int totalAverageScore = response.getJSONArray("list").size() == 0 ? 0 : response.getJSONArray("list").getJSONObject(0).getInteger("total_average_score");
                IScene scene = AppPersonalReceptionScoreTrendScene.builder().dataCycleType(dataCycleType).startDate(startDate).endDate(startDate).salesId(e.getSaleId()).build();
                JSONObject object = scene.invoke(visitor).getJSONArray("list").getJSONObject(0);
                int a = object.getInteger("100");
                int b = object.getInteger("200");
                int c = object.getInteger("300");
                int d = object.getInteger("400");
                int f = object.getInteger("500");
                int mathResult = CommonUtil.getRoundIntRatio(a + b + c + d + f, 5);
                CommonUtil.valueView(totalAverageScore, mathResult);
                Preconditions.checkArgument(totalAverageScore == mathResult, e.getName() + "APP【个人总平均分趋势】中分数：" + totalAverageScore + " APP【各环节得分】各环节总平均分相加/5：" + mathResult);
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("APP【个人总平均分趋势】中分数=APP【各环节得分】各环节总平均分相加/5");
        }
    }

    //ok
    @Test(description = "APP【接待详情】本次接待得分=APP【接待详情】各环节的接待得分之和/5")
    public void personal_data_8() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            List<AppDepartmentPageBean> departmentPageList = util.getAppDepartmentPageList(dataCycleType, startDate, endDate);
            departmentPageList.forEach(e -> {
                List<AppPersonalPageBean> personalPageList = util.getAppPersonalPageList(dataCycleType, e.getSaleId(), startDate, endDate);
                personalPageList.forEach(personalPage -> {
                    JSONObject response = VoiceDetailScene.builder().id(personalPage.getId()).build().invoke(visitor);
                    if (!response.getString("enter_status_name").equals("再次进店")) {
                        int averageScore = response.getInteger("average_score");
                        int scoreSum = response.getJSONArray("scores").stream().map(object -> (JSONObject) object)
                                .filter(object -> object.getInteger("score") != null)
                                .mapToInt(object -> object.getInteger("score")).sum();
                        int mathResult = CommonUtil.getCeilIntRatio(scoreSum, 5);
                        CommonUtil.valueView(e.getName(), averageScore, mathResult);
                        Preconditions.checkArgument(averageScore <= mathResult + 1 && averageScore >= mathResult - 1, e.getName() + "APP【接待详情】本次接待得分：" + averageScore + " APP【接待详情】各环节的接待得分之和/5：" + mathResult);
                    }
                });
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("APP【接待详情】本次接待得分=APP【接待详情】各环节的接待得分之和/5");
        }
    }

    //ok
    @Test(description = "APP【接待详情】中各环节得分=PC【语音接待评鉴详情】中各环节得分", dataProvider = "receptionType", dataProviderClass = DataClass.class)
    public void personal_data_9(String name, int type) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            List<AppDepartmentPageBean> departmentPageList = util.getAppDepartmentPageList(dataCycleType, startDate, endDate);
            departmentPageList.forEach(e -> {
                Map<Long, Integer> appScoreMap = new HashMap<>();
                List<AppPersonalPageBean> personalPageList = util.getAppPersonalPageList(dataCycleType, e.getSaleId(), startDate, endDate);
                personalPageList.stream().filter(Objects::nonNull).forEach(personalPage -> {
                    JSONObject detail = AppDetailScene.builder().id(personalPage.getId()).build().invoke(visitor);
                    if (!detail.getString("enter_status_name").equals("再次进店") && detail.getInteger("average_score") != 0) {
                        JSONArray array = detail.getJSONArray("scores");
                        array.stream().map(obj -> (JSONObject) obj)
                                .filter(obj -> obj.getInteger("type").equals(type))
                                .forEach(obj -> appScoreMap.put(personalPage.getId(), obj.getInteger("score")));
                    }
                });
                Map<Long, Integer> pcScoreMap = new HashMap<>();
                IScene evaluationPageScene = VoiceEvaluationPageScene.builder().receptorName(e.getName()).receptionStart(startDate).receptionEnd(endDate).evaluateStatus(500).enterStatus(1).build();
                List<VoiceEvaluationPageBean> voiceEvaluationPageList = util.toJavaObjectList(evaluationPageScene, VoiceEvaluationPageBean.class);
                voiceEvaluationPageList.stream().filter(Objects::nonNull).forEach(voiceEvaluationPage -> {
                    JSONObject detail = VoiceDetailScene.builder().id(voiceEvaluationPage.getId()).build().invoke(visitor);
                    if (detail.getInteger("average_score") != null && detail.getInteger("average_score") != 0) {
                        JSONArray array = detail.getJSONArray("scores");
                        array.stream().map(obj -> (JSONObject) obj)
                                .filter(obj -> obj.getInteger("type").equals(type))
                                .forEach(obj -> pcScoreMap.put(voiceEvaluationPage.getId(), obj.getInteger("score")));
                    }
                });
                CommonUtil.valueView(e.getName(), appScoreMap, pcScoreMap);
                Preconditions.checkArgument(appScoreMap.size() == pcScoreMap.size(), e.getName() + "APP【接待详情】中平均分不为0的次数：" + appScoreMap.size() + " PC【语音接待评鉴详情】中各平均分分不为0的次数：" + pcScoreMap.size());
                appScoreMap.forEach((appKey, appValue) -> pcScoreMap.forEach((pcKey, pcValue) -> {
                    if (pcKey.equals(appKey)) {
                        Preconditions.checkArgument(pcValue.equals(appValue), " APP【接待详情】中" + name + "环节得分：" + appValue + " PC【语音接待评鉴详情】中" + name + "环节得分：" + pcValue);

                    }
                }));
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("APP【接待详情】中各环节得分=PC【语音接待评鉴详情】中各环节得分");
        }
    }

    //ok
    @Test(description = "首次到店的客户A的【接待详情】中的接待的得分中的本次接待得分=PC中【语音接待评鉴】客户A的详情中的接待得分")
    public void personal_data_10() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            List<AppDepartmentPageBean> departmentPageList = util.getAppDepartmentPageList(dataCycleType, startDate, endDate);
            departmentPageList.forEach(e -> {
                Map<Long, Integer> map = new HashMap<>();
                List<AppPersonalPageBean> personalPageList = util.getAppPersonalPageList(dataCycleType, e.getSaleId(), startDate, endDate);
                personalPageList.stream().filter(Objects::nonNull).forEach(personalPage -> {
                    JSONObject response = AppDetailScene.builder().id(personalPage.getId()).build().invoke(visitor);
                    if (!response.getString("enter_status_name").equals("再次进店") && response.getInteger("average_score") != null) {
                        map.put(personalPage.getId(), response.getInteger("average_score"));
                    }
                });
                IScene evaluationPageScene = VoiceEvaluationPageScene.builder().receptorName(e.getName()).receptionStart(startDate).receptionEnd(endDate).enterStatus(1).build();
                List<VoiceEvaluationPageBean> voiceEvaluationPageList = util.toJavaObjectList(evaluationPageScene, VoiceEvaluationPageBean.class);
                voiceEvaluationPageList.stream().filter(Objects::nonNull).forEach(voiceRecord -> {
                    for (Map.Entry<Long, Integer> entry : map.entrySet()) {
                        if (entry.getKey().equals(voiceRecord.getId())) {
                            JSONObject response = VoiceDetailScene.builder().id(voiceRecord.getId()).build().invoke(visitor);
                            if (response.getInteger("average_score") != null) {
                                int pcAverageScore = response.getInteger("average_score");
                                int appAverageScore = entry.getValue();
                                CommonUtil.valueView(e.getName(), appAverageScore, pcAverageScore);
                                Preconditions.checkArgument(appAverageScore <= pcAverageScore + 1 && appAverageScore >= pcAverageScore - 1, e.getName() + "APP【接待详情】中的接待得分：" + appAverageScore + " PC中【语音接待评鉴】详情中的接待得分：" + pcAverageScore);
                            }
                        }
                    }
                });
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("首次到店的客户A的【接待详情】中的接待的得分中的本次接待得分=PC中【语音接待评鉴】客户A的详情中的接待得分");
        }
    }

    //ok
    @Test(description = "APP【接待详情】中的话术提高项=PC【语音接待评鉴】中给您一些小建议")
    public void personal_data_11() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Map<String, AppSpeechTechniqueAdviceBean> map = new HashMap<>();
            IScene scene = AppStandardListScene.builder().build();
            JSONArray list = scene.invoke(visitor).getJSONArray("list");
            list.stream().map(object -> (JSONObject) object).forEach(object -> {
                String type = object.getString("type");
                JSONArray array = object.getJSONArray("techniques");
                array.stream().map(e -> (JSONObject) e).forEach(e -> {
                    AppSpeechTechniqueAdviceBean speechTechniqueAdvice = new AppSpeechTechniqueAdviceBean();
                    speechTechniqueAdvice.setTerms(e.getString("terms"));
                    speechTechniqueAdvice.setTitle(e.getString("title"));
                    map.put(type, speechTechniqueAdvice);
                });
            });
            IScene speechPageScene = SpeechTechniquePageScene.builder().build();
            JSONArray array = speechPageScene.invoke(visitor).getJSONArray("list");
            array.stream().map(e -> (JSONObject) e).forEach(e -> {
                for (Map.Entry<String, AppSpeechTechniqueAdviceBean> entry : map.entrySet()) {
                    if (e.getString("link_name").equals(entry.getKey()) && e.getString("label").equals(entry.getValue().getTitle())) {
                        String terms = entry.getValue().getTerms();
                        String advice = e.getString("advice");
                        CommonUtil.valueView(e.getString("link_name") + e.getString("label"), terms, advice);
                        Preconditions.checkArgument(terms.equals(advice), e.getString("link_name") + e.getString("label") + "APP【接待详情】中的话术提高项：" + terms + " PC【语音接待评鉴】中给您一些小建议：" + advice);
                    }
                }
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("APP【接待详情】中的话术提高项=PC【语音接待评鉴】中给您一些小建议");
        }
    }

    //ok
    @Test(description = "APP【接待详情】中的录音记录的时长＝PC【语音接待评鉴】中同一客户的接待详情中的录音时长")
    public void personal_data_12() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            List<AppDepartmentPageBean> departmentPageList = util.getAppDepartmentPageList(dataCycleType, startDate, endDate);
            departmentPageList.forEach(e -> {
                Map<Long, String> map = new HashMap<>();
                List<AppPersonalPageBean> personalPageList = util.getAppPersonalPageList(dataCycleType, e.getSaleId(), startDate, endDate);
                personalPageList.stream().filter(Objects::nonNull).forEach(personalPage -> {
                    JSONObject response = AppDetailScene.builder().id(personalPage.getId()).build().invoke(visitor);
                    if (!response.getString("enter_status_name").equals("再次进店") && response.getString("voice_record") != null) {
                        map.put(personalPage.getId(), response.getString("voice_record"));
                    }
                });
                IScene evaluationPageScene = VoiceEvaluationPageScene.builder().receptorName(e.getName()).receptionStart(startDate).receptionEnd(endDate).enterStatus(1).build();
                List<VoiceEvaluationPageBean> voiceEvaluationPageList = util.toJavaObjectList(evaluationPageScene, VoiceEvaluationPageBean.class);
                voiceEvaluationPageList.stream().filter(Objects::nonNull).forEach(voiceRecord -> {
                    for (Map.Entry<Long, String> entry : map.entrySet()) {
                        if (entry.getKey().equals(voiceRecord.getId())) {
                            JSONObject response = VoiceDetailScene.builder().id(voiceRecord.getId()).build().invoke(visitor);
                            if (response.getString("voice_record") != null) {
                                String pcVoiceRecord = response.getString("voice_record");
                                String appVoiceRecord = entry.getValue();
                                CommonUtil.valueView(e.getName(), appVoiceRecord, pcVoiceRecord);
                                Preconditions.checkArgument(appVoiceRecord.equals(pcVoiceRecord), e.getName() + "APP【接待详情】中的接待时长：" + appVoiceRecord + " PC中【语音接待评鉴】详情中的接待时长：" + pcVoiceRecord);
                            }
                        }
                    }
                });
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("APP【接待详情】中的录音记录的时长＝PC【语音接待评鉴】中同一客户的接待详情中的录音时长");
        }
    }

    //ok
    @Test(description = "APP【接待详情】部门平均分=此部门的全部员工全流程接待分值之和/参与评分的接待次数*5")
    public void personal_data_13() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene overviewScene = AppOverviewScene.builder().dataCycleType(dataCycleType).startDate(startDate).endDate(endDate).build();
            AppOverviewBean overview = util.toJavaObject(overviewScene, AppOverviewBean.class);
            int averageScore = overview.getAverageScore();
            Map<Long, JSONArray> map = new HashMap<>();
            List<AppDepartmentPageBean> departmentPageList = util.getAppDepartmentPageList(dataCycleType, startDate, endDate);
            departmentPageList.forEach(e -> {
                List<AppPersonalPageBean> personalPageList = util.getAppPersonalPageList(dataCycleType, e.getSaleId(), startDate, endDate);
                personalPageList.stream().filter(Objects::nonNull).forEach(personalPage -> {
                    JSONObject response = AppDetailScene.builder().id(personalPage.getId()).build().invoke(visitor);
                    if (!response.getString("enter_status_name").equals("再次进店")
                            && response.getJSONArray("scores").size() != 0) {
                        map.put(personalPage.getId(), response.getJSONArray("scores"));
                    }
                });
            });
            int i = map.values().stream().mapToInt(array -> array.stream().map(object -> (JSONObject) object).mapToInt(object -> object.getInteger("score")).sum()).sum();
            int mathResult = CommonUtil.getCeilIntRatio(i, map.size() * 5);
            CommonUtil.valueView(averageScore, mathResult);
            Preconditions.checkArgument(averageScore <= mathResult + 1 && averageScore >= mathResult - 1, "APP部门平均分：" + averageScore + " 此部门的全部员工全流程接待分值之和/参与评分的接待次数*5：" + mathResult);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("APP【接待详情】部门平均分=此部门的全部员工全流程接待分值之和/参与评分的接待次数*5");
        }
    }
}
