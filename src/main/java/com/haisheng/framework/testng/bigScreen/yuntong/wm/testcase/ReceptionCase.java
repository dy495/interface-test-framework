package com.haisheng.framework.testng.bigScreen.yuntong.wm.testcase;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.itemPorsche.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemPorsche.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.itemPorsche.enumerator.config.EnumChecklistUser;
import com.haisheng.framework.testng.bigScreen.itemPorsche.enumerator.config.EnumJobName;
import com.haisheng.framework.testng.bigScreen.itemPorsche.enumerator.config.EnumTestProduce;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.EnumAccount;
import com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.app.departmentdata.AppCapabilityModelBean;
import com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.app.departmentdata.AppLinkDataCarouselBean;
import com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.app.departmentdata.AppOverviewBean;
import com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.app.departmentdata.AppReceptionAverageScoreTrendBean;
import com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.app.personaldata.AppPersonalCapabilityModelBean;
import com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.app.personaldata.AppPersonalOverviewBean;
import com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.app.personaldata.AppReceptionLinkScoreBean;
import com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.app.personaldata.AppReceptionScoreTrendBean;
import com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.app.voicerecord.AppDepartmentPageBean;
import com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.app.voicerecord.AppDetailBean;
import com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.app.voicerecord.AppPersonalPageBean;
import com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.pc.manage.VoiceDetailBean;
import com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.pc.manage.VoiceEvaluationPageBean;
import com.haisheng.framework.testng.bigScreen.yuntong.wm.enumerate.EnumDataCycleType;
import com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.app.departmentdata.*;
import com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.app.personaldata.AppPersonalCapabilityModelScene;
import com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.app.personaldata.AppReceptionLinkScoreScene;
import com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.app.voicerecord.AppDetailScene;
import com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.pc.manage.VoiceDetailScene;
import com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.pc.manage.VoiceEvaluationPageScene;
import com.haisheng.framework.testng.bigScreen.yuntong.wm.util.BusinessUtil;
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
import java.util.stream.Collectors;

/**
 * 会听数据
 *
 * @author wangmin
 * @date 2021/1/29 11:17
 */
public class ReceptionCase extends TestCaseCommon implements TestCaseStd {
    private static final EnumTestProduce PRODUCE = EnumTestProduce.YT_DAILY_HT;
    private static final EnumAccount ALL_AUTHORITY = EnumAccount.ALL_YT_DAILY;
    public VisitorProxy visitor = new VisitorProxy(PRODUCE);
    public BusinessUtil util = new BusinessUtil(visitor);
    private static final String startDate = DateTimeUtil.addDayFormat(new Date(), -4);
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
        visitor.setProduct(EnumTestProduce.YT_DAILY_ZH);
        util.loginApp(ALL_AUTHORITY);
        logger.debug("beforeMethod");
        caseResult = getFreshCaseResult(method);
        logger.debug("case: " + caseResult);
        visitor.setProduct(EnumTestProduce.YT_DAILY_HT);
    }

    //ok
    @Test
    public void department_data_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene scene = AppOverviewScene.builder().dataCycleType(dataCycleType).startDate(startDate).endDate(endDate).build();
            AppOverviewBean appOverviewBean = util.toJavaObject(scene, AppOverviewBean.class);
            int count = appOverviewBean.getCount();
            int totalDuration = appOverviewBean.getTotalDuration();
            int averageDuration = appOverviewBean.getAverageDuration();
            int mathResult = CommonUtil.getIntRatio(totalDuration, count);
            CommonUtil.valueView(averageDuration, mathResult);
            Preconditions.checkArgument(averageDuration == mathResult, "app平均接待时长为：" + averageDuration + "app总时长/接待次数：" + mathResult);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("app平均接待时长=APP总接待时长/接待次数");
        }
    }

    //ok
    @Test
    public void department_data_2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene appOverviewScene = AppOverviewScene.builder().dataCycleType(dataCycleType).startDate(startDate).endDate(endDate).build();
            int totalDuration = util.toJavaObject(appOverviewScene, AppOverviewBean.class).getTotalDuration();
            IScene scene = VoiceEvaluationPageScene.builder().receptionStart(startDate).receptionEnd(endDate).build();
            List<VoiceEvaluationPageBean> evaluationPageBeanList = util.toJavaObjectList(scene, VoiceEvaluationPageBean.class);
            int receptionTimeSum = evaluationPageBeanList.stream().map(VoiceEvaluationPageBean::getReceptionDuration).mapToInt(DateTimeUtil::timeToSecond).sum();
            int mathResult = CommonUtil.getIntRatio(receptionTimeSum, 60);
            CommonUtil.valueView(totalDuration, mathResult);
            Preconditions.checkArgument(totalDuration - 5 <= mathResult || mathResult <= totalDuration + 5, "app总接待时长：" + totalDuration + " PC【语音接待评鉴】中相同时间段的客户的接待时长之和：" + mathResult);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("app总接待时长=PC【语音接待评鉴】所有接待时长之和");
        }
    }

    //ok
    @Test
    public void department_data_3() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene appOverviewScene = AppOverviewScene.builder().dataCycleType(dataCycleType).startDate(startDate).endDate(endDate).build();
            int totalDuration = util.toJavaObject(appOverviewScene, AppOverviewBean.class).getTotalDuration();
            List<AppDepartmentPageBean> departmentPageBeanList = util.getAppDepartmentPageList(dataCycleType, startDate, endDate);
            long receptionDurationSum = departmentPageBeanList.stream().mapToLong(AppDepartmentPageBean::getReceptionDuration).sum();
            CommonUtil.valueView(totalDuration, receptionDurationSum);
            Preconditions.checkArgument(totalDuration <= receptionDurationSum || totalDuration >= receptionDurationSum - 5, "app总接待时长：" + totalDuration + " APP部门接待评鉴中的【员工接待评鉴】列表中接待时长之和：" + receptionDurationSum);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("app总接待时长=APP部门接待评鉴中的【员工接待评鉴】列表中接待时长之和");
        }
    }

    //ok
    @Test
    public void department_data_4() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene appOverviewScene = AppOverviewScene.builder().dataCycleType(dataCycleType).startDate(startDate).endDate(endDate).build();
            int count = util.toJavaObject(appOverviewScene, AppOverviewBean.class).getCount();
            int total = VoiceEvaluationPageScene.builder().receptionStart(startDate).receptionEnd(endDate).build().invoke(visitor).getInteger("total");
            CommonUtil.valueView(count, total);
            Preconditions.checkArgument(count == total, "app接待次数：" + count + " PC【语音接待评鉴】列表数：" + total);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("app接待次数=PC【语音接待评鉴】列表数：");
        }
    }

    //ok
    @Test
    public void department_data_5() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene appOverviewScene = AppOverviewScene.builder().dataCycleType(dataCycleType).startDate(startDate).endDate(endDate).build();
            int count = util.toJavaObject(appOverviewScene, AppOverviewBean.class).getCount();
            int receptionTimesSum = util.getAppDepartmentPageList(dataCycleType, startDate, endDate).stream().mapToInt(AppDepartmentPageBean::getReceptionTimes).sum();
            CommonUtil.valueView(count, receptionTimesSum);
            Preconditions.checkArgument(count == receptionTimesSum, "app接待次数：" + count + " APP【员工接待评鉴】列表所有员工接待次数之和：" + receptionTimesSum);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("app接待次数=APP【员工接待评鉴】列表所有员工接待次数之和");
        }
    }

    @Test
    public void department_data_6() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene appOverviewScene = AppOverviewScene.builder().dataCycleType(dataCycleType).startDate(startDate).endDate(endDate).build();
            int count = util.toJavaObject(appOverviewScene, AppOverviewBean.class).getCount();
            int receptionRecordSum = util.getAppDepartmentPageList(dataCycleType, startDate, endDate).stream().map(e -> util.getAppPersonalPageList(dataCycleType, e.getSaleId(), startDate, endDate).size()).mapToInt(e -> e).sum();
            CommonUtil.valueView(count, receptionRecordSum);
            Preconditions.checkArgument(count == receptionRecordSum, "app接待次数：" + count + " APP各个员工的【接待评分详情】列表之和：" + receptionRecordSum);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("app接待次数=APP各个员工的【接待评分详情】列表之和：");
        }
    }

    //ok
    @Test
    public void department_data_7() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene appOverviewScene = AppOverviewScene.builder().dataCycleType(dataCycleType).startDate(startDate).endDate(endDate).build();
            int averageData = util.toJavaObject(appOverviewScene, AppOverviewBean.class).getAverageScore();
            IScene evaluationPageScene = VoiceEvaluationPageScene.builder().enterStatus(1).evaluateStatus(500).receptionStart(startDate).receptionEnd(endDate).build();
            List<VoiceEvaluationPageBean> evaluationPageBeanList = util.toJavaObjectList(evaluationPageScene, VoiceEvaluationPageBean.class);
            int evaluateScoreSum = evaluationPageBeanList.stream().mapToInt(VoiceEvaluationPageBean::getEvaluateScore).sum();
            int mathResult = CommonUtil.getIntRatio(evaluateScoreSum, evaluationPageBeanList.size());
            CommonUtil.valueView(averageData, evaluateScoreSum, mathResult);
            Preconditions.checkArgument(averageData == mathResult, "app接待平均分数为：" + averageData + " PC【语音接待评鉴】列表中首次进店&评分完成的得分平均数：" + mathResult);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("app接待平均分=PC【语音接待评鉴】列表中首次进店&评分完成的得分平均数");
        }
    }

    //ok
    @Test
    public void department_data_8() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene appOverviewScene = AppOverviewScene.builder().dataCycleType(dataCycleType).startDate(startDate).endDate(endDate).build();
            int averageData = util.toJavaObject(appOverviewScene, AppOverviewBean.class).getAverageScore();
            IScene scene = AppCapabilityModelScene.builder().dataCycleType(dataCycleType).startDate(startDate).endDate(endDate).build();
            int scoreSum = util.toJavaObjectList(scene, AppCapabilityModelBean.class, "list").stream().mapToInt(AppCapabilityModelBean::getScore).sum();
            int mathResult = CommonUtil.getIntRatio(scoreSum, 5);
            CommonUtil.valueView(averageData, mathResult);
            Preconditions.checkArgument(averageData == mathResult, "app接待平均分数为：" + averageData + " APP【销售接待能力模型】中各话术数值之和/5：" + mathResult);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("app接待平均分=APP【销售接待能力模型】中各话术数值之和/5");
        }
    }

    //数据有问题
    @Test
    public void department_data_9() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene appOverviewScene = AppOverviewScene.builder().dataCycleType(dataCycleType).startDate(startDate).endDate(endDate).build();
            int averageData = util.toJavaObject(appOverviewScene, AppOverviewBean.class).getAverageScore();
            List<AppDepartmentPageBean> appDepartmentPageBeanList = util.getAppDepartmentPageList(dataCycleType, startDate, endDate);
            List<AppPersonalOverviewBean> personalOverviewBeanList = appDepartmentPageBeanList.stream().map(e -> util.getAppPersonalOverview(dataCycleType, e.getSaleId(), startDate, endDate)).collect(Collectors.toList());
            long totalScoreSum = personalOverviewBeanList.stream().mapToLong(AppPersonalOverviewBean::getAverageScore).sum();
            int mathResult = CommonUtil.getIntRatio(Math.toIntExact(totalScoreSum), personalOverviewBeanList.size());
            CommonUtil.valueView(averageData, totalScoreSum, mathResult);
            Preconditions.checkArgument(averageData == mathResult, "app接待平均分数为：" + averageData + " APP各个员工的接待平均分之和/总人数：" + mathResult);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("app接待平均分=APP各个员工的接待平均分之和/总人数");
        }
    }

    //数据有问题
    @Test
    public void department_data_10() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene appOverviewScene = AppOverviewScene.builder().dataCycleType(dataCycleType).startDate(startDate).endDate(endDate).build();
            int averageData = util.toJavaObject(appOverviewScene, AppOverviewBean.class).getAverageScore();
            List<AppDepartmentPageBean> appDepartmentPageBeanList = util.getAppDepartmentPageList(dataCycleType, startDate, endDate);
            List<AppPersonalPageBean> personalPageList = new ArrayList<>();
            appDepartmentPageBeanList.forEach(e -> personalPageList.addAll(util.getAppPersonalPageList(dataCycleType, e.getSaleId(), startDate, endDate)
                    .stream().filter(personalPage -> util.getAppVoiceRecordDetail(personalPage.getId()).getEnterStatusName().equals("首次进店")).collect(Collectors.toList())));
            int scoreSum = personalPageList.stream().mapToInt(e -> util.getAppVoiceRecordDetailScoresSum(e.getId())).sum();
            int mathResult = CommonUtil.getIntRatio(scoreSum, personalPageList.size());
            CommonUtil.valueView(averageData, scoreSum, mathResult);
            Preconditions.checkArgument(averageData == mathResult, "app接待平均分数为：" + averageData + " 全员工全流程接待分值之和/参与评分的接待次数：" + mathResult);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("app接待平均分=全员工全流程接待分值之和/参与评分的接待次数");
        }
    }

    //ok
    @Test
    public void department_data_11() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene appOverviewScene = AppOverviewScene.builder().dataCycleType(dataCycleType).startDate(startDate).endDate(endDate).build();
            int averageData = util.toJavaObject(appOverviewScene, AppOverviewBean.class).getAverageScore();
            IScene scene = AppCapabilityModelScene.builder().dataCycleType(dataCycleType).startDate(startDate).endDate(endDate).build();
            int scoreSum = util.toJavaObjectList(scene, AppCapabilityModelBean.class, "list").stream().mapToInt(AppCapabilityModelBean::getScore).sum();
            int mathResult = CommonUtil.getIntRatio(scoreSum, 5);
            CommonUtil.valueView(averageData, mathResult);
            Preconditions.checkArgument(averageData == mathResult, "app接待平均分数为：" + averageData + " APP【销售接待能力模型】中各话术数值之和/5：" + mathResult);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("app接待平均分=APP【销售接待能力模型】中各话术数值之和/5");
        }
    }

    //平均分有误
    @Test
    public void department_data_12() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene scene = AppCapabilityModelScene.builder().dataCycleType(dataCycleType).startDate(startDate).endDate(endDate).build();
            int scoreSum = util.toJavaObjectList(scene, AppCapabilityModelBean.class, "list").stream().mapToInt(AppCapabilityModelBean::getScore).sum();
            int mathResult = CommonUtil.getIntRatio(scoreSum, 5);
            List<AppDepartmentPageBean> appDepartmentPageBeanList = util.getAppDepartmentPageList(dataCycleType, startDate, endDate);
            List<AppPersonalOverviewBean> personalOverviewList = appDepartmentPageBeanList.stream().map(e -> util.getAppPersonalOverview(dataCycleType, e.getSaleId(), startDate, endDate)).collect(Collectors.toList());
            long totalScoreSum = personalOverviewList.stream().mapToLong(AppPersonalOverviewBean::getAverageScore).sum();
            int mathResult2 = CommonUtil.getIntRatio(Math.toIntExact(totalScoreSum), personalOverviewList.size());
            CommonUtil.valueView(scoreSum, mathResult, totalScoreSum, mathResult2);
            Preconditions.checkArgument(mathResult == mathResult2, "app【我的能力模型】各话术环节分数相加/5：" + mathResult + " APP【话术环节数据统计（全部环节）】部门平均分（全员工全流程接待分值之和/参与评分的接待次数：" + mathResult2);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("app【我的能力模型】各话术环节分数相加/5=APP【话术环节数据统计（全部环节）】部门平均分（全员工全流程接待分值之和/参与评分的接待次数）");
        }
    }

    //ok 3天内数据一致，本月数据一致
    @Test
    public void department_data_13() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene capabilityModelScene = AppCapabilityModelScene.builder().dataCycleType(dataCycleType).startDate(startDate).endDate(endDate).build();
            JSONArray modelList = capabilityModelScene.invoke(visitor).getJSONArray("list");

            IScene linkDataCarouselScene = AppLinkDataCarouselScene.builder().dataCycleType(dataCycleType).startDate(startDate).endDate(endDate).build();
            JSONArray linkList = linkDataCarouselScene.invoke(visitor).getJSONArray("list");
            modelList.stream().map(model -> (JSONObject) model).forEach(model -> linkList.stream().map(link -> (JSONObject) link)
                    .filter(link -> link.getInteger("type").equals(model.getInteger("type")))
                    .forEach(link -> {
                        int score = model.getInteger("score");
                        int averageScore = link.getInteger("average_score");
                        CommonUtil.valueView(score, averageScore);
                        Preconditions.checkArgument(averageScore == score, "欢迎接待中" + link.getString("type_name") + "的部门平均分：" + averageScore + " 能力模型中：" + score);
                        CommonUtil.logger(link.getString("type_name"));
                    }));
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("app【销售能力管理中】的合格率=部门接待评鉴中【员工接待评鉴】列表中的总得分大于60的员工所占的比例");
        }
    }

    //ok
    @Test
    public void department_data_14() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene scene = AppLinkDataCarouselScene.builder().dataCycleType(dataCycleType).startDate(startDate).endDate(endDate).build();
            scene.invoke(visitor).getJSONArray("list").stream().map(e -> (JSONObject) e).forEach(e -> Preconditions.checkArgument(e.getInteger("qualified_score") == 60, e.getString("type_name") + "的合格线为" + e.getInteger("qualified_score") + "分"));
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("合格线=后台配置60分");
        }
    }

    //ok
    @Test
    public void department_data_15() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene scene = AppLinkDataCarouselScene.builder().dataCycleType(dataCycleType).startDate(startDate).endDate(endDate).build();
            List<AppLinkDataCarouselBean> linkDataCarouselList = util.toJavaObjectList(scene, AppLinkDataCarouselBean.class, "list");
            int qualifiedRatio = linkDataCarouselList.stream().filter(e -> e.getTypeName().equals("全部环节")).map(AppLinkDataCarouselBean::getQualifiedRatio).findFirst().orElse(0);
            //员工评鉴列表
            List<AppDepartmentPageBean> departmentPageList = util.getAppDepartmentPageList(dataCycleType, startDate, endDate);
            //员工接待列表
            List<AppPersonalPageBean> personalPageList = new ArrayList<>();
            departmentPageList.stream().map(e -> util.getAppPersonalPageList(dataCycleType, e.getSaleId(), startDate, endDate)).forEach(personalPageList::addAll);
            //员工解答详情中首次接待的条数
            List<AppDetailBean> detailList = new ArrayList<>();
            personalPageList.stream().map(e -> util.getAppVoiceRecordDetail(e.getId())).filter(e -> e.getEnterStatusName().equals("首次进店") && e.getAverageScore() != null && e.getAverageScore() != 0).forEach(detailList::add);
            //接待详情中分数超过60的
            int passCount = (int) detailList.stream().filter(e -> e.getAverageScore() != null && e.getAverageScore() >= 60).count();
            int mathResult = CommonUtil.getIntPercent(passCount, detailList.size());
            CommonUtil.valueView(qualifiedRatio, mathResult);
            Preconditions.checkArgument(qualifiedRatio == mathResult, "app【销售能力管理中】的合格率：" + qualifiedRatio + " APP接待分数>=60分的次数/接待详情中有得分的总接待次数：" + mathResult);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("app【销售能力管理中】的合格率=接待分数>=60分的次数/APP接待详情中有得分的总接待次数");
        }
    }

    //有问题
    @Test
    public void department_data_16() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene scene = AppCapabilityModelScene.builder().dataCycleType(dataCycleType).startDate(startDate).endDate(endDate).build();
            int scoreSum = util.toJavaObjectList(scene, AppCapabilityModelBean.class, "list").stream().mapToInt(AppCapabilityModelBean::getScore).sum();
            int mathResult = CommonUtil.getIntRatio(scoreSum, 5);
            List<AppDepartmentPageBean> departmentPageList = util.getAppDepartmentPageList(dataCycleType, startDate, endDate);
            List<AppPersonalPageBean> personalPageList = new ArrayList<>();
            departmentPageList.stream().map(e -> util.getAppPersonalPageList(dataCycleType, e.getSaleId(), startDate, endDate)).forEach(personalPageList::addAll);
            personalPageList.stream().map(e -> util.getAppVoiceRecordDetail(e.getId()))
                    .filter(e -> e.getEnterStatusName().equals("首次进店"))
                    .filter(e -> e.getDepartmentAverageScore() != null)
                    .forEach(e -> Preconditions.checkArgument(mathResult == e.getDepartmentAverageScore(), "各话术环节分数相加/5：" + mathResult + " APP接待详情中的部门平均分：" + e.getDepartmentAverageScore()));
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("各话术环节分数相加/5=APP接待详情中的部门平均分");
        }
    }

    //有问题
    @Test
    public void department_data_17() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene scene = AppLinkDataCarouselScene.builder().dataCycleType(dataCycleType).startDate(startDate).endDate(endDate).build();
            List<AppLinkDataCarouselBean> linkDataCarouselList = util.toJavaObjectList(scene, AppLinkDataCarouselBean.class, "list");
            AppLinkDataCarouselBean linkDataCarousel = linkDataCarouselList.stream().filter(e -> e.getTypeName().equals("全部环节")).findFirst().orElse(null);
            assert linkDataCarousel != null;
            int averageScore = linkDataCarousel.getAverageScore() == null ? 0 : linkDataCarousel.getAverageScore();
            int averageRatio = linkDataCarousel.getAverageRatio() == null ? 0 : linkDataCarousel.getAverageRatio();
            //员工评鉴列表
            List<AppDepartmentPageBean> departmentPageList = util.getAppDepartmentPageList(dataCycleType, startDate, endDate);
            //员工接待列表
            List<AppPersonalPageBean> personalPageList = new ArrayList<>();
            departmentPageList.stream().map(e -> util.getAppPersonalPageList(dataCycleType, e.getSaleId(), startDate, endDate)).forEach(personalPageList::addAll);
            //员工解答详情中首次接待的条数
            List<AppDetailBean> detailList = new ArrayList<>();
            personalPageList.stream().map(e -> util.getAppVoiceRecordDetail(e.getId())).filter(e -> e.getEnterStatusName().equals("首次进店") && e.getAverageScore() != null && e.getAverageScore() != 0).forEach(detailList::add);
            //接待详情中分数超过60的
            int passCount = (int) detailList.stream().filter(e -> e.getAverageScore() != null && e.getAverageScore() >= averageScore).count();
            int mathResult = CommonUtil.getIntPercent(passCount, detailList.size());
            CommonUtil.valueView(averageRatio, mathResult);
            Preconditions.checkArgument(averageRatio == mathResult, "app【销售能力管理中】的合格率：" + averageRatio + " APP接待分数>=60分的次数/接待详情中有得分的总接待次数：" + mathResult);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("app【销售能力管理中】的合格率=APP接待分数>=60分的次数/接待详情中有得分的总接待次数");
        }
    }

    //有问题
    @Test(dataProvider = "type")
    public void department_data_18(int type) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene scene = AppLinkDataCarouselScene.builder().dataCycleType(dataCycleType).startDate(startDate).endDate(endDate).build();
            List<AppLinkDataCarouselBean> linkDataCarouselList = util.toJavaObjectList(scene, AppLinkDataCarouselBean.class, "list");
            int qualifiedRatio = linkDataCarouselList.stream().filter(e -> e.getType().equals(type)).map(AppLinkDataCarouselBean::getQualifiedRatio).findFirst().orElse(0);
            //员工评鉴列表
            List<AppDepartmentPageBean> departmentPageList = util.getAppDepartmentPageList(dataCycleType, startDate, endDate);
            //员工接待列表
            List<AppPersonalPageBean> personalPageList = new ArrayList<>();
            departmentPageList.stream().map(e -> util.getAppPersonalPageList(dataCycleType, e.getSaleId(), startDate, endDate)).forEach(personalPageList::addAll);
            //员工解答详情中首次接待的条数
            List<AppDetailBean> detailList = new ArrayList<>();
            personalPageList.stream().map(e -> util.getAppVoiceRecordDetail(e.getId())).filter(e -> e.getEnterStatusName().equals("首次进店")).forEach(detailList::add);
            List<Integer> scoreList = new ArrayList<>();
            detailList.forEach(e -> e.getScores().stream()
                    .map(object -> (JSONObject) object)
                    .filter(object -> object.getInteger("type").equals(type))
                    .filter(object -> object.getInteger("score") != null)
                    .filter(object -> object.getInteger("score") != 0)
                    .forEach(object -> scoreList.add(object.getInteger("score"))));
            int scoreSum = scoreList.stream().mapToInt(e -> e).sum();
            int mathResult = CommonUtil.getIntRatio(scoreSum, scoreList.size());
            CommonUtil.valueView(qualifiedRatio, mathResult);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("某一环节的接待合格率=【员工接待评鉴列表】超过60分的接待次数/参与评分的总接待次数*100%");
        }
    }

    @DataProvider(name = "type")
    public Object[] getType() {
        return new Integer[]{
                100, 200, 300, 400, 500
        };
    }

    //ok
    @Test
    public void department_data_19() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene receptionAverageScoreTrendScene = AppReceptionAverageScoreTrendScene.builder().dataCycleType(dataCycleType).startDate(startDate).endDate(endDate).build();
            AppReceptionAverageScoreTrendBean receptionAverageScoreTrend = util.toFirstJavaObject(receptionAverageScoreTrendScene, AppReceptionAverageScoreTrendBean.class);
            int totalAverageScore = receptionAverageScoreTrend == null ? 0 : receptionAverageScoreTrend.getTotalAverageScore();
            IScene receptionScoreTrendScene = AppReceptionScoreTrendScene.builder().dataCycleType(dataCycleType).startDate(startDate).endDate(endDate).build();
            AppReceptionScoreTrendBean receptionScoreTrend = util.toFirstJavaObject(receptionScoreTrendScene, AppReceptionScoreTrendBean.class);
            int scoreSum = receptionScoreTrend == null ? 0 : receptionScoreTrend.getOne() + receptionScoreTrend.getTwo() + receptionScoreTrend.getThree() + receptionScoreTrend.getFour() + receptionScoreTrend.getFive();
            int mathResult = CommonUtil.getIntRatio(scoreSum, 5);
            CommonUtil.valueView(totalAverageScore, mathResult);
            Preconditions.checkArgument(mathResult <= totalAverageScore + 1 || mathResult >= totalAverageScore - 1, "筛选某一天）【部门总平均分趋势】分数：" + totalAverageScore + " 【各环节得分趋势】各话术环节平均分之和/5：" + mathResult);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("筛选某一天）【部门总平均分趋势】分数=【各环节得分趋势】各话术环节平均分之和/5");
        }
    }

    @Test(dataProvider = "type")
    public void department_data_20(int type) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String startDate = DateTimeUtil.addDayFormat(new Date(), -1, "yyyy-MM-dd");
            IScene receptionScoreTrendScene = AppReceptionScoreTrendScene.builder().dataCycleType(dataCycleType).startDate(startDate).endDate(startDate).build();
            JSONArray array = receptionScoreTrendScene.invoke(visitor).getJSONArray("list");
            int trendScore = array.size() == 0 ? 0 : array.getJSONObject(0).getInteger(String.valueOf(type));
            IScene capabilityModelScene = AppCapabilityModelScene.builder().dataCycleType(dataCycleType).startDate(startDate).endDate(endDate).build();
            List<AppCapabilityModelBean> capabilityModelList = util.toJavaObjectList(capabilityModelScene, AppCapabilityModelBean.class, "list");
            int modelScore = capabilityModelList.stream().filter(e -> e.getType().equals(type)).map(AppCapabilityModelBean::getScore).findFirst().orElse(0);
            CommonUtil.valueView(trendScore, modelScore);
            Preconditions.checkArgument(trendScore == modelScore, "【各环节得分趋势】中的各环节得分：" + trendScore + " 【我的能力模型】各环节得分：" + modelScore);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("（筛选为某一天）【各环节得分趋势】中的各环节得分＝【我的能力模型】各环节得分");
        }
    }

    @Test(dataProvider = "type")
    public void department_data_21(int type) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String startDate = DateTimeUtil.addDayFormat(new Date(), -1, "yyyy-MM-dd");
            IScene receptionScoreTrendScene = AppReceptionScoreTrendScene.builder().dataCycleType(dataCycleType).startDate(startDate).endDate(startDate).build();
            JSONArray array = receptionScoreTrendScene.invoke(visitor).getJSONArray("list");
            int trendScore = array.size() == 0 ? 0 : array.getJSONObject(0).getInteger(String.valueOf(type));
            IScene voiceEvaluationPageScene = VoiceEvaluationPageScene.builder().enterStatus(1).evaluateStatus(500).receptionStart(startDate).receptionEnd(startDate).build();
            List<VoiceEvaluationPageBean> voiceEvaluationPageList = util.toJavaObjectList(voiceEvaluationPageScene, VoiceEvaluationPageBean.class);
            int scoreSum = voiceEvaluationPageList.stream().map(e -> VoiceDetailScene.builder().id(e.getId()).build().invoke(visitor).getJSONArray("scores").getJSONObject(0))
                    .filter(e -> e.getInteger("type").equals(type)).filter(e -> e.getInteger("score") != 0).mapToInt(e -> e.getInteger("score")).sum();
            int mathResult = CommonUtil.getIntRatio(scoreSum, voiceEvaluationPageList.size());
            CommonUtil.valueView(trendScore, mathResult);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("（筛选为某一天）【各环节得分趋势】中的各环节得分=PC【语音接待评鉴】中相同时间段的首次到店的的接待详情中的各环节的平均分");
        }
    }

    //ok
    @Test
    public void department_data_22() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String startDate = DateTimeUtil.addDayFormat(new Date(), -1, "yyyy-MM-dd");
            IScene receptionAverageScoreTrendScene = AppReceptionAverageScoreTrendScene.builder().startDate(startDate).endDate(startDate).dataCycleType(dataCycleType).build();
            AppReceptionAverageScoreTrendBean receptionAverageScoreTrend = util.toFirstJavaObject(receptionAverageScoreTrendScene, AppReceptionAverageScoreTrendBean.class);
            int totalAverageScore = receptionAverageScoreTrend == null ? 0 : receptionAverageScoreTrend.getTotalAverageScore();
            IScene scene = AppCapabilityModelScene.builder().dataCycleType(dataCycleType).startDate(startDate).endDate(endDate).build();
            int scoreSum = util.toJavaObjectList(scene, AppCapabilityModelBean.class, "list").stream().mapToInt(AppCapabilityModelBean::getScore).sum();
            int mathResult = CommonUtil.getIntRatio(scoreSum, 5);
            CommonUtil.valueView(totalAverageScore, mathResult);
            Preconditions.checkArgument(scoreSum >= mathResult + 1 || scoreSum <= mathResult - 1, "【部门总平均分趋势】平均分：" + scoreSum + " 【我的能力模型】各话术环节平均分之和/5：" + mathResult);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("（筛选某一天）【部门总平均分趋势】平均分＝【我的能力模型】各话术环节平均分之和/5");
        }
    }

    //ok
    @Test
    public void department_data_23() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String startDate = DateTimeUtil.addDayFormat(new Date(), -1, "yyyy-MM-dd");
            IScene receptionAverageScoreTrendScene = AppReceptionAverageScoreTrendScene.builder().startDate(startDate).endDate(startDate).dataCycleType(dataCycleType).build();
            AppReceptionAverageScoreTrendBean receptionAverageScoreTrend = util.toFirstJavaObject(receptionAverageScoreTrendScene, AppReceptionAverageScoreTrendBean.class);
            int totalAverageScore = receptionAverageScoreTrend == null ? 0 : receptionAverageScoreTrend.getTotalAverageScore();
            IScene voiceEvaluationPageScene = VoiceEvaluationPageScene.builder().receptionStart(startDate).receptionEnd(startDate).evaluateStatus(500).enterStatus(1).build();
            List<VoiceEvaluationPageBean> list = util.toJavaObjectList(voiceEvaluationPageScene, VoiceEvaluationPageBean.class);
            int evaluateScoreSum = list.stream().mapToInt(VoiceEvaluationPageBean::getEvaluateScore).sum();
            int mathResult = CommonUtil.getIntRatio(evaluateScoreSum, list.size());
            CommonUtil.valueView(totalAverageScore, mathResult);
            Preconditions.checkArgument(totalAverageScore <= mathResult + 1 || totalAverageScore >= mathResult - 1, "【部门总平均分趋势】分数：" + totalAverageScore + " PC【语音接待评鉴】首次到店的接待评分之和/接待次数：" + mathResult);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("（筛选某一天）【部门总平均分趋势】分数=【部门接待评鉴】中的接待平均分");
        }
    }

    //ok
    @Test
    public void department_data_24() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String startDate = DateTimeUtil.addDayFormat(new Date(), -1, "yyyy-MM-dd");
            IScene receptionAverageScoreTrendScene = AppReceptionAverageScoreTrendScene.builder().startDate(startDate).endDate(startDate).dataCycleType(dataCycleType).build();
            AppReceptionAverageScoreTrendBean receptionAverageScoreTrend = util.toFirstJavaObject(receptionAverageScoreTrendScene, AppReceptionAverageScoreTrendBean.class);
            int totalAverageScore = receptionAverageScoreTrend == null ? 0 : receptionAverageScoreTrend.getTotalAverageScore();
            IScene scene = AppOverviewScene.builder().dataCycleType(dataCycleType).startDate(startDate).endDate(endDate).build();
            AppOverviewBean overview = util.toJavaObject(scene, AppOverviewBean.class);
            int averageScore = overview.getAverageScore();
            CommonUtil.valueView(totalAverageScore, averageScore);
            Preconditions.checkArgument(totalAverageScore <= averageScore + 1 || totalAverageScore >= averageScore - 1, "【部门总平均分趋势】分数：" + totalAverageScore + " 【部门接待评鉴】中的接待平均分：" + averageScore);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("（同一个员工）【员工接待评鉴】中的平均接待时长＝【部门接待评鉴】中的接待平均分");
        }
    }

    //ok
    @Test
    public void department_data_25() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene scene = AppOverviewScene.builder().dataCycleType(dataCycleType).startDate(startDate).endDate(endDate).build();
            AppOverviewBean overview = util.toJavaObject(scene, AppOverviewBean.class);
            int count = overview.getCount();
            List<AppDepartmentPageBean> departmentPageList = util.getAppDepartmentPageList(dataCycleType, startDate, endDate);
            List<AppPersonalPageBean> list = new ArrayList<>();
            departmentPageList.stream().map(e -> util.getAppPersonalPageList(dataCycleType, e.getSaleId(), startDate, endDate)).forEach(list::addAll);
            int listSize = list.size();
            CommonUtil.valueView(count, listSize);
            Preconditions.checkArgument(count == list.size(), "【部门接待评鉴】中的接待次数：" + count + " 各接待销售记录中的【接待评分详情】中的列表条数之和：" + list.size());
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("【部门接待评鉴】中的接待次数=各接待销售记录中的【接待评分详情】中的列表条数之和");
        }
    }

    //ok
    @Test
    public void department_data_26() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            List<AppDepartmentPageBean> departmentPageList = util.getAppDepartmentPageList(dataCycleType, startDate, endDate);
            departmentPageList.forEach(e -> {
                int count = util.getAppPersonalOverview(dataCycleType, e.getSaleId(), startDate, endDate).getCount();
                int listSize = util.getAppPersonalPageList(dataCycleType, e.getSaleId(), startDate, endDate).size();
                CommonUtil.valueView(count, listSize);
                Preconditions.checkArgument(count == listSize, e.getName() + "的接待次数：" + count + " 【员工接待评鉴】【接待评分详情中】的列表数：" + listSize);
                CommonUtil.logger(e.getName());
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("【员工接待评鉴】每个员工的接待次数=【员工接待评鉴】【接待评分详情中】的列表数");
        }
    }

    //ok
    @Test
    public void department_data_27() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            List<AppDepartmentPageBean> departmentPageList = util.getAppDepartmentPageList(dataCycleType, startDate, endDate);
            departmentPageList.forEach(e -> {
                AppPersonalOverviewBean personalOverview = util.getAppPersonalOverview(dataCycleType, e.getSaleId(), startDate, endDate);
                int averageDuration = personalOverview.getAverageDuration();
                int mathResult = CommonUtil.getIntRatio(personalOverview.getTotalDuration(), personalOverview.getCount());
                CommonUtil.valueView(averageDuration, mathResult);
                Preconditions.checkArgument(averageDuration == mathResult, e.getName() + "的接待平均市场：" + averageDuration + " 【员工接待评鉴】中的接待时长／接待次数：" + mathResult);
                CommonUtil.logger(e.getName());
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("（同一个员工）【员工接待评鉴】中的平均接待时长＝【员工接待评鉴】中的接待时长/接待次数");
        }
    }

    //ok
    @Test
    public void personal_data_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            List<AppDepartmentPageBean> departmentPageList = util.getAppDepartmentPageList(dataCycleType, startDate, endDate);
            departmentPageList.forEach(e -> {
                AppPersonalOverviewBean personalOverview = util.getAppPersonalOverview(dataCycleType, e.getSaleId(), startDate, endDate);
                int averageScore = personalOverview.getAverageScore();
                IScene scene = AppPersonalCapabilityModelScene.builder().dataCycleType(dataCycleType).salesId(e.getSaleId()).startDate(startDate).endDate(endDate).build();
                int scoreSum = util.toJavaObjectList(scene, AppPersonalCapabilityModelBean.class, "list").stream().mapToInt(AppPersonalCapabilityModelBean::getScore).sum();
                int mathResult = CommonUtil.getIntRatio(scoreSum, 5);
                CommonUtil.valueView(e.getName(), averageScore, mathResult);
                Preconditions.checkArgument(averageScore <= mathResult + 1 || averageScore >= mathResult - 1, e.getName() + " 接待平均分：" + averageScore + " 【我的能力模型】各话术环节分数相加/5：" + mathResult);
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("每个员工的接待平均分=【我的能力模型】各话术环节分数相加/5");
        }
    }


    //ok
    @Test()
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
                int mathResult = CommonUtil.getIntRatio(personAverageScoreSum, 5);
                CommonUtil.valueView(e.getName(), averageScore, mathResult);
                Preconditions.checkArgument(averageScore <= mathResult + 1 || averageScore >= mathResult - 1, e.getName() + " 接待平均分：" + averageScore + " 【各环节得分】个人总平均分之和/5：" + mathResult);
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("接待平均分=【各环节得分】个人总平均分之和/5");
        }
    }

    //ok
    @Test(dataProvider = "type")
    public void personal_data_3(int type) {
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
                Preconditions.checkArgument(modelScore == personAverageScore, "【我的能力模型】" + type + "环节的分值：" + modelScore + "【各环节得分】平均分的分值");
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("【我的能力模型】各话术环节的分值＝【各环节得分】平均分的分值");
        }
    }

    //ok
    @Test(dataProvider = "type")
    public void personal_data_5(int type) {
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
                int mathResult = CommonUtil.getIntRatio(scoreSum, list.size());
                CommonUtil.valueView(e.getName(), modelScore, mathResult);
                Preconditions.checkArgument(modelScore <= mathResult + 1 || modelScore >= mathResult - 1, "【我的能力模型】" + type + "的分值：" + modelScore + " PC【语音接待评鉴】接待详情中的各环节的平均分：" + mathResult);
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("【我的能力模型】各话术环节的分值＝PC【语音接待评鉴】接待详情中的各环节的平均分");
        }
    }

    @Test
    public void personal_data_6() {
        logger.logCaseStart(caseResult.getCaseName());
        try {


        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("（筛选为某一天）【个人总平均分趋势】中分数=【各环节得分】各环节总平均分相加/5");
        }
    }

    @Test
    public void personal_data_7() {
        logger.logCaseStart(caseResult.getCaseName());
        try {


        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("（筛选为某一天）【各环节得分趋势】中的各环节得分=【各环节得分】各环节平均分");
        }
    }

    @Test
    public void personal_data_25() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            int dataCycleType = 0;
            String startDate = DateTimeUtil.addDayFormat(new Date(), -1);
            String endDate = DateTimeUtil.getFormat(new Date());
            List<AppPersonalPageBean> personalPageBeanList = new ArrayList<>();
            util.getAppDepartmentPageList(dataCycleType, startDate, endDate).stream().map(a -> util.getAppPersonalPageList(dataCycleType, a.getId(), startDate, endDate)).forEach(personalPageBeanList::addAll);
            List<AppPersonalPageBean> newList = personalPageBeanList.stream().filter(e -> util.getAppAverageScoreByReceptionDetail(e.getId()) != null && util.getAppAverageScoreByReceptionDetail(e.getId()) != 0).collect(Collectors.toList());
            int scoreSum = newList.stream().mapToInt(e -> util.getAppDetailScoreSum(e.getId())).sum();
            int mathResult = CommonUtil.getIntRatio(scoreSum, 5 * newList.size());
            IScene appDetailScene = AppDetailScene.builder().id(newList.get(0).getId()).build();
            int departmentAverageScore = util.toJavaObject(appDetailScene, AppDetailBean.class).getDepartmentAverageScore();
            Preconditions.checkArgument(departmentAverageScore == mathResult, "部门平均分：" + departmentAverageScore + "部门的全部员工全流程接待分值之和/参与评分的接待次数：" + mathResult);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("部门平均分=此部门的全部员工全流程接待分值之和/参与评分的接待次数");
        }
    }

    @Test
    public void personal_data_26() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            int dataCycleType = 0;
            String startDate = DateTimeUtil.addDayFormat(new Date(), -1);
            String endDate = DateTimeUtil.getFormat(new Date());
            List<AppPersonalPageBean> personalPageBeanList = new ArrayList<>();
            util.getAppDepartmentPageList(dataCycleType, startDate, endDate).stream().map(a -> util.getAppPersonalPageList(dataCycleType, a.getId(), startDate, endDate)).forEach(personalPageBeanList::addAll);
            List<AppPersonalPageBean> newList = personalPageBeanList.stream().filter(e -> util.getAppAverageScoreByReceptionDetail(e.getId()) != null && util.getAppAverageScoreByReceptionDetail(e.getId()) != 0).collect(Collectors.toList());
            int scoreSum = newList.stream().mapToInt(e -> util.getAppDetailScoreSum(e.getId())).sum();
            int mathResult = CommonUtil.getIntRatio(scoreSum, 5 * newList.size());
            IScene appDetailScene = AppDetailScene.builder().id(newList.get(0).getId()).build();
            int departmentAverageScore = util.toJavaObject(appDetailScene, AppDetailBean.class).getDepartmentAverageScore();
            Preconditions.checkArgument(departmentAverageScore == mathResult, "部门平均分：" + departmentAverageScore + "部门的全部员工全流程接待分值之和/参与评分的接待次数：" + mathResult);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("部门平均分=此部门的全部员工全流程接待分值之和/参与评分的接待次数");
        }
    }

    @Test
    public void receptionCriticism_data_12() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Integer dataCycleType = 0;
            String startDate = DateTimeUtil.addDayFormat(new Date(), -1);
            String endDate = DateTimeUtil.getFormat(new Date());
            String saleId = "";
            Map<Integer, Integer> map = new HashMap<>();
            JSONArray capabilityModelList = AppCapabilityModelScene.builder().dataCycleType(dataCycleType).startDate(startDate).endDate(endDate).build().invoke(visitor).getJSONArray("list");
            capabilityModelList.stream().map(e -> (JSONObject) e).forEach(e -> map.put(e.getInteger("type"), e.getInteger("score")));
            JSONArray list = AppReceptionLinkScoreScene.builder().dataCycleType(dataCycleType).startDate(startDate).endDate(endDate).salesId(saleId).build().invoke(visitor).getJSONArray("list");
            list.stream().map(e -> (JSONObject) e).forEach(e -> {
                int value = util.getValueByKey(map, e.getInteger("type"));
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

    @Test
    public void receptionCriticism_data_13() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Integer dataCycleType = 0;
            String startDate = "";
            String endDate = "";
            int averageData = AppOverviewScene.builder().dataCycleType(dataCycleType).startDate(startDate).endDate(endDate).build().invoke(visitor).getInteger("average_score");
            JSONArray capabilityModelList = AppCapabilityModelScene.builder().dataCycleType(dataCycleType).startDate(startDate).endDate(endDate).build().invoke(visitor).getJSONArray("list");
            int scoreSum = capabilityModelList.stream().map(e -> (JSONObject) e).mapToInt(e -> e.getInteger("score")).sum();
            int mathResult = CommonUtil.getIntRatio(scoreSum, 5);
            Preconditions.checkArgument(averageData == mathResult, "接待平均分为：" + averageData + "能力模型平均分计算结果：" + mathResult);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("app我的能力模型各项数值=各环节得分的平均分");
        }
    }
}
