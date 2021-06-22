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
import com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.app.departmentdata.AppCapabilityModelBean;
import com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.app.departmentdata.AppLinkDataCarouselBean;
import com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.app.departmentdata.AppOverviewBean;
import com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.app.departmentdata.AppReceptionAverageScoreTrendBean;
import com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.app.personaldata.AppPersonalOverviewBean;
import com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.app.personaldata.AppReceptionLinkScoreBean;
import com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.app.personaldata.AppReceptionScoreTrendBean;
import com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.app.voicerecord.AppDepartmentPageBean;
import com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.app.voicerecord.AppDetailBean;
import com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.app.voicerecord.AppPersonalPageBean;
import com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.pc.manage.VoiceDetailBean;
import com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.pc.manage.VoiceEvaluationPageBean;
import com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.app.departmentdata.*;
import com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.app.personaldata.AppPersonalOverviewScene;
import com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.app.personaldata.AppReceptionLinkScoreScene;
import com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.app.voicerecord.AppDepartmentPageScene;
import com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.app.voicerecord.AppDetailScene;
import com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.app.voicerecord.AppPersonalPageScene;
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
import org.jetbrains.annotations.NotNull;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

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
    }

    @Test
    public void department_data_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            visitor.setProduct(EnumTestProduce.YT_DAILY_HT);
            Integer dataCycleType = 0;
            String startDate = DateTimeUtil.addDayFormat(new Date(), -1);
            String endDate = DateTimeUtil.getFormat(new Date());
            IScene scene = AppOverviewScene.builder().dataCycleType(dataCycleType).startDate(startDate).endDate(endDate).build();
            AppOverviewBean appOverviewBean = util.toJavaObject(scene, AppOverviewBean.class);
            int count = appOverviewBean.getCount();
            int totalDuration = appOverviewBean.getTotalDuration();
            int averageDuration = appOverviewBean.getAverageDuration();
            int mathResult = CommonUtil.getIntRatio(totalDuration, count, 0);
            Preconditions.checkArgument(averageDuration == mathResult, "app平均接待时长为：" + averageDuration + "app总时长除以接待次数：" + mathResult);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("app平均接待时长=APP总接待时长/接待次数");
        }
    }

    @Test
    public void department_data_2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Integer dataCycleType = 0;
            String startDate = DateTimeUtil.addDayFormat(new Date(), -1);
            String endDate = DateTimeUtil.getFormat(new Date());
            IScene appOverviewScene = AppOverviewScene.builder().dataCycleType(dataCycleType).startDate(startDate).endDate(endDate).build();
            int averageData = util.toJavaObject(appOverviewScene, AppOverviewBean.class).getAverageScore();
            IScene evaluationPageScene = VoiceEvaluationPageScene.builder().receptionStart(startDate).receptionEnd(endDate).build();
            List<VoiceEvaluationPageBean> evaluationPageBeanList = util.toJavaObjectList(evaluationPageScene, VoiceEvaluationPageBean.class);
            List<VoiceEvaluationPageBean> newList = evaluationPageBeanList.stream().filter(e -> e.getEvaluateScore() != null & e.getEvaluateScore() != 0).collect(Collectors.toList());
            int evaluateScoreSum = newList.stream().mapToInt(VoiceEvaluationPageBean::getEvaluateScore).sum();
            int mathResult = CommonUtil.getIntRatio(evaluateScoreSum, newList.size(), 0);
            Preconditions.checkArgument(averageData == mathResult, "app接待接待分数为：" + averageData + "pc语音接待评鉴得分平均值为：" + mathResult);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("app接待平均分=PC【语音接待评鉴】中相同时间段的首次到店的接待评分的平均分");
        }
    }

    @Test
    public void department_data_3() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Integer dataCycleType = 0;
            String startDate = DateTimeUtil.addDayFormat(new Date(), -1);
            String endDate = DateTimeUtil.getFormat(new Date());
            IScene appOverviewScene = AppOverviewScene.builder().dataCycleType(dataCycleType).startDate(startDate).endDate(endDate).build();
            int averageData = util.toJavaObject(appOverviewScene, AppOverviewBean.class).getAverageScore();
            List<AppDepartmentPageBean> appDepartmentPageBeanList = util.getAppDepartmentPageList(startDate, endDate);
            List<AppDepartmentPageBean> newList = appDepartmentPageBeanList.stream().filter(e -> e.getTotalScore() != null && e.getTotalScore() != 0).collect(Collectors.toList());
            long totalScoreSum = newList.stream().mapToLong(AppDepartmentPageBean::getTotalScore).sum();
            int mathResult = CommonUtil.getIntRatio(Math.toIntExact(totalScoreSum), newList.size(), 0);
            Preconditions.checkArgument(averageData == mathResult, "app接待接待分数为：" + averageData + "app员工接待评鉴平均分为：" + mathResult);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("app接待平均分=APP部门接待评鉴中的【员工接待评鉴】列表中（总得分不为空的的数据）的平均分");
        }
    }

    @Test
    public void department_data_4() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Integer dataCycleType = 0;
            String startDate = DateTimeUtil.addDayFormat(new Date(), -1);
            String endDate = DateTimeUtil.getFormat(new Date());
            IScene appOverviewScene = AppOverviewScene.builder().dataCycleType(dataCycleType).startDate(startDate).endDate(endDate).build();
            int averageData = util.toJavaObject(appOverviewScene, AppOverviewBean.class).getAverageScore();
            IScene scene = AppCapabilityModelScene.builder().startDate(startDate).endDate(endDate).build();
            int scoreSum = util.toJavaObjectList(scene, AppCapabilityModelBean.class).stream().mapToInt(AppCapabilityModelBean::getScore).sum();
            int mathResult = CommonUtil.getIntRatio(scoreSum, 5, 0);
            Preconditions.checkArgument(averageData == mathResult, "app接待接待平均分数为：" + averageData + "app销售接待能力模型各话术之和平均值：" + mathResult);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("app接待平均分=APP【销售接待能力模型】中各话术环节的数值之和/5");
        }
    }

    @Test
    public void department_data_5() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Integer dataCycleType = 0;
            String startDate = DateTimeUtil.addDayFormat(new Date(), -1);
            String endDate = DateTimeUtil.getFormat(new Date());
            IScene appOverviewScene = AppOverviewScene.builder().dataCycleType(dataCycleType).startDate(startDate).endDate(endDate).build();
            int averageData = util.toJavaObject(appOverviewScene, AppOverviewBean.class).getAverageScore();
            List<AppPersonalPageBean> personalPageBeanList = new ArrayList<>();
            util.getAppDepartmentPageList(startDate, endDate).stream().map(a -> util.getAppPersonalPageList(a.getId(), startDate, endDate)).forEach(personalPageBeanList::addAll);
            List<AppPersonalPageBean> newList = personalPageBeanList.stream().filter(e -> util.getAverageScoreByReceptionDetail(e.getId()) != null && util.getAverageScoreByReceptionDetail(e.getId()) != 0).collect(Collectors.toList());
            int scoreSum = newList.stream().mapToInt(e -> util.getAverageScoreByReceptionDetail(e.getId())).sum();
            int mathResult = CommonUtil.getIntRatio(scoreSum, newList.size(), 0);
            Preconditions.checkArgument(averageData == mathResult, "app接待平均分为：" + averageData + "【话术环节数据统计（全部环节）】部门平均分（全员工全流程接待分值之和/参与评分的接待次数）：" + mathResult);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("app接待平均分=APP【话术环节数据统计（全部环节）】部门平均分（全员工全流程接待分值之和/参与评分的接待次数）");
        }
    }

    @Test
    public void department_data_6() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Integer dataCycleType = 0;
            String startDate = DateTimeUtil.addDayFormat(new Date(), -1);
            String endDate = DateTimeUtil.getFormat(new Date());
            IScene appOverviewScene = AppOverviewScene.builder().dataCycleType(dataCycleType).startDate(startDate).endDate(endDate).build();
            int totalDuration = util.toJavaObject(appOverviewScene, AppOverviewBean.class).getTotalDuration();
            IScene scene = VoiceEvaluationPageScene.builder().receptionStart(startDate).receptionEnd(endDate).build();
            List<VoiceEvaluationPageBean> evaluationPageBeanList = util.toJavaObjectList(scene, VoiceEvaluationPageBean.class);
            int receptionTimeSum = evaluationPageBeanList.stream().map(VoiceEvaluationPageBean::getReceptionTime).mapToInt(DateTimeUtil::timeToSecond).sum();
            int mathResult = CommonUtil.getIntRatio(receptionTimeSum, 60, 0);
            Preconditions.checkArgument(totalDuration == mathResult, "app总接待时长：" + totalDuration + "PC【语音接待评鉴】所有接待时长之和：" + mathResult);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("app总接待时长=PC【语音接待评鉴】所有接待时长之和");
        }
    }

    @Test
    public void department_data_7() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Integer dataCycleType = 0;
            String startDate = DateTimeUtil.addDayFormat(new Date(), -1);
            String endDate = DateTimeUtil.getFormat(new Date());
            IScene appOverviewScene = AppOverviewScene.builder().dataCycleType(dataCycleType).startDate(startDate).endDate(endDate).build();
            int totalDuration = util.toJavaObject(appOverviewScene, AppOverviewBean.class).getTotalDuration();
            List<AppDepartmentPageBean> departmentPageBeanList = util.getAppDepartmentPageList(startDate, endDate);
            long receptionDurationSum = departmentPageBeanList.stream().mapToLong(AppDepartmentPageBean::getReceptionDuration).sum();
            Preconditions.checkArgument(totalDuration == receptionDurationSum, "app总接待时长：" + totalDuration + "APP部门接待评鉴中的【员工接待评鉴】列表中接待时长之和：" + receptionDurationSum);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("app总接待时长=APP部门接待评鉴中的【员工接待评鉴】列表中接待时长之和");
        }
    }

    @Test
    public void department_data_8() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Integer dataCycleType = 0;
            String startDate = DateTimeUtil.addDayFormat(new Date(), -1);
            String endDate = DateTimeUtil.getFormat(new Date());
            IScene appOverviewScene = AppOverviewScene.builder().dataCycleType(dataCycleType).startDate(startDate).endDate(endDate).build();
            int count = util.toJavaObject(appOverviewScene, AppOverviewBean.class).getCount();
            int total = VoiceEvaluationPageScene.builder().receptionStart(startDate).receptionEnd(endDate).build().invoke(visitor).getInteger("total");
            //todo 语音缓存记录中上传失败
            Preconditions.checkArgument(count == total, "app接待次数：" + count + "PC【语音接待评鉴】中相同时间段的首次到店的条数：" + total);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("app接待次数=PC【语音接待评鉴】中相同时间段的首次到店的条数");
        }
    }

    @Test
    public void department_data_9() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Integer dataCycleType = 0;
            String startDate = DateTimeUtil.addDayFormat(new Date(), -1);
            String endDate = DateTimeUtil.getFormat(new Date());
            IScene appOverviewScene = AppOverviewScene.builder().dataCycleType(dataCycleType).startDate(startDate).endDate(endDate).build();
            int count = util.toJavaObject(appOverviewScene, AppOverviewBean.class).getCount();
            int receptionRecordSum = util.getAppDepartmentPageList(startDate, endDate).stream().map(e -> util.getAppPersonalPageList(e.getId(), startDate, endDate).size()).mapToInt(e -> e).sum();
            Preconditions.checkArgument(count == receptionRecordSum, "app接待次数：" + count + "APP员工接待列表中【接待评分详情】列表中接待条数之和：" + receptionRecordSum);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("app接待次数=APP员工接待列表中【接待评分详情】列表中接待条数之和");
        }
    }

    @Test
    public void department_data_10() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Integer dataCycleType = 0;
            String startDate = DateTimeUtil.addDayFormat(new Date(), -1);
            String endDate = DateTimeUtil.getFormat(new Date());
            IScene appOverviewScene = AppOverviewScene.builder().dataCycleType(dataCycleType).startDate(startDate).endDate(endDate).build();
            int count = util.toJavaObject(appOverviewScene, AppOverviewBean.class).getCount();
            int receptionTimesSum = util.getAppDepartmentPageList(startDate, endDate).stream().mapToInt(AppDepartmentPageBean::getReceptionTimes).sum();
            Preconditions.checkArgument(count == receptionTimesSum, "app接待次数：" + count + "APP【员工接待评鉴】接待次数之和：" + receptionTimesSum);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("app接待次数=APP【员工接待评鉴】接待次数之和");
        }
    }

    @Test
    public void department_data_11() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Integer dataCycleType = 0;
            String startDate = DateTimeUtil.addDayFormat(new Date(), -1);
            String endDate = DateTimeUtil.getFormat(new Date());
            IScene scene = AppCapabilityModelScene.builder().dataCycleType(dataCycleType).startDate(startDate).endDate(endDate).build();
            int scoreSumA = util.toJavaObjectList(scene, AppCapabilityModelBean.class).stream().mapToInt(AppCapabilityModelBean::getScore).sum();
            int mathResultA = CommonUtil.getIntRatio(scoreSumA, 5, 0);
            List<AppPersonalPageBean> personalPageBeanList = new ArrayList<>();
            util.getAppDepartmentPageList(startDate, endDate).stream().map(a -> util.getAppPersonalPageList(a.getId(), startDate, endDate)).forEach(personalPageBeanList::addAll);
            List<AppPersonalPageBean> newList = personalPageBeanList.stream().filter(e -> util.getAverageScoreByReceptionDetail(e.getId()) != null && util.getAverageScoreByReceptionDetail(e.getId()) != 0).collect(Collectors.toList());
            int scoreSum = newList.stream().mapToInt(e -> util.getAverageScoreByReceptionDetail(e.getId())).sum();
            int mathResult = CommonUtil.getIntRatio(scoreSum, newList.size(), 0);
            Preconditions.checkArgument(mathResultA == mathResult, "app【我的能力模型】各话术环节分数相加/5：" + mathResultA + "APP【话术环节数据统计（全部环节）】部门平均分（全员工全流程接待分值之和/参与评分的接待次数）：" + mathResult);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("app【我的能力模型】各话术环节分数相加/5=APP【话术环节数据统计（全部环节）】部门平均分（全员工全流程接待分值之和/参与评分的接待次数）");
        }
    }

    @Test
    public void department_data_12() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Map<Integer, Integer> map = new HashMap<>();
            Map<Integer, Integer> sumMap = new HashMap<>();
            Integer dataCycleType = 0;
            String startDate = DateTimeUtil.addDayFormat(new Date(), -1);
            String endDate = DateTimeUtil.getFormat(new Date());
            IScene scene = AppCapabilityModelScene.builder().dataCycleType(dataCycleType).startDate(startDate).endDate(endDate).build();
            util.toJavaObjectList(scene, AppCapabilityModelBean.class).forEach(e -> map.put(e.getType(), e.getScore()));
            List<AppPersonalPageBean> personalPageBeanList = new ArrayList<>();
            util.getAppDepartmentPageList(startDate, endDate).stream().map(a -> util.getAppPersonalPageList(a.getId(), startDate, endDate)).forEach(personalPageBeanList::addAll);
            List<AppPersonalPageBean> newList = personalPageBeanList.stream().filter(e -> util.getAverageScoreByReceptionDetail(e.getId()) != null && util.getAverageScoreByReceptionDetail(e.getId()) != 0).collect(Collectors.toList());
            map.forEach((key, value) -> sumMap.put(key, newList.stream().mapToInt(e -> util.getScoreByType(e.getId(), key)).sum()));
            sumMap.forEach((key, value) -> {
                int score = map.entrySet().stream().filter(e -> e.getKey().equals(key)).map(Map.Entry::getValue).findFirst().orElse(0);
                int mathResult = CommonUtil.getIntRatio(sumMap.get(key), newList.size(), 0);
                Preconditions.checkArgument(score == mathResult, "能力模型中话术" + key + "的值为：" + score + "全员工全流程接待分值之和/参与评分的接待次数值为：" + mathResult);
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("app【我的能力模型】各话术环节分数相加/5=APP【话术环节数据统计（全部环节）】部门平均分（全员工全流程接待分值之和/参与评分的接待次数）");
        }
    }

    @Test
    public void department_data_13() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Integer dataCycleType = 0;
            String startDate = DateTimeUtil.addDayFormat(new Date(), -1);
            String endDate = DateTimeUtil.getFormat(new Date());
            IScene scene = AppLinkDataCarouselScene.builder().dataCycleType(dataCycleType).startDate(startDate).endDate(endDate).build();
            List<AppLinkDataCarouselBean> dataCarouselBeanList = util.toJavaObjectList(scene, AppLinkDataCarouselBean.class);
            int qualifiedRatio = dataCarouselBeanList.stream().filter(e -> e.getType().equals(1)).map(AppLinkDataCarouselBean::getQualifiedRatio).findFirst().orElse(0);
            List<AppPersonalPageBean> list = new ArrayList<>();
            util.getAppDepartmentPageList(startDate, endDate).stream().map(e -> util.getAppPersonalPageList(e.getId(), startDate, endDate)).forEach(list::addAll);
            List<Integer> averageScoreList = list.stream().filter(e -> util.getAverageScoreByReceptionDetail(e.getId()) != null && util.getAverageScoreByReceptionDetail(e.getId()) != 0).map(e -> util.getAverageScoreByReceptionDetail(e.getId())).collect(Collectors.toList());
            int pass = (int) averageScoreList.stream().filter(e -> e >= 60).count();
            int mathResult = CommonUtil.getIntRatio(pass, averageScoreList.size(), 0);
            Preconditions.checkArgument(qualifiedRatio == mathResult, "接待合格率为：" + qualifiedRatio + "员工接待分数超过60的比例为：" + mathResult);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("【销售能力管理中】的合格率=部门接待评鉴中【员工接待评鉴】列表中的总得分大于60的员工所占的比例");
        }
    }

    @Test
    public void department_data_14() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Integer dataCycleType = 0;
            String startDate = DateTimeUtil.addDayFormat(new Date(), -1);
            String endDate = DateTimeUtil.getFormat(new Date());
            IScene scene = AppCapabilityModelScene.builder().startDate(startDate).endDate(endDate).dataCycleType(dataCycleType).build();
            int scoreSum = util.toJavaObjectList(scene, AppCapabilityModelBean.class).stream().mapToInt(AppCapabilityModelBean::getScore).sum();
            int mathResult = CommonUtil.getIntRatio(scoreSum, 5, 0);
            List<AppPersonalPageBean> list = new ArrayList<>();
            util.getAppDepartmentPageList(startDate, endDate).stream().map(e -> util.getAppPersonalPageList(e.getId(), startDate, endDate)).forEach(list::addAll);
            List<Integer> averageScoreList = list.stream().filter(e -> util.getAverageScoreByReceptionDetail(e.getId()) != null && util.getAverageScoreByReceptionDetail(e.getId()) != 0).map(e -> util.getAverageScoreByReceptionDetail(e.getId())).collect(Collectors.toList());
            int averageScoreSum = averageScoreList.stream().mapToInt(e -> e).sum();
            int newMathResult = CommonUtil.getIntRatio(averageScoreSum, averageScoreList.size(), 0);
            Preconditions.checkArgument(mathResult == newMathResult, "【我的能力模型】各话术环节分数相加/5：" + mathResult + "部门接待评鉴中【员工接待评鉴】接待详情中的首次接待平均分：" + mathResult);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("【我的能力模型】各话术环节分数相加/5=部门接待评鉴中【员工接待评鉴】接待详情中的首次接待平均分");
        }
    }

    @Test
    public void department_data_15() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Map<Integer, Integer> map = new HashMap<>();
            Integer dataCycleType = 0;
            String startDate = DateTimeUtil.addDayFormat(new Date(), -1);
            String endDate = DateTimeUtil.getFormat(new Date());
            IScene receptionLinkScoreScene = AppReceptionLinkScoreScene.builder().startDate(startDate).endDate(endDate).dataCycleType(dataCycleType).build();
            util.toJavaObjectList(receptionLinkScoreScene, AppReceptionLinkScoreBean.class).forEach(e -> map.put(e.getType(), e.getDepartmentAverageScore()));
            List<AppPersonalPageBean> list = new ArrayList<>();
            util.getAppDepartmentPageList(startDate, endDate).stream().map(e -> util.getAppPersonalPageList(e.getId(), startDate, endDate)).forEach(list::addAll);
            List<AppPersonalPageBean> newList = list.stream().filter(e -> util.getAverageScoreByReceptionDetail(e.getId()) != null && util.getAverageScoreByReceptionDetail(e.getId()) != 0).collect(Collectors.toList());
            IScene linkDataCarouselScene = AppLinkDataCarouselScene.builder().startDate(startDate).endDate(endDate).dataCycleType(dataCycleType).build();
            List<AppLinkDataCarouselBean> linkDataCarouselBeanList = util.toJavaObjectList(linkDataCarouselScene, AppLinkDataCarouselBean.class);
            map.forEach((key, value) -> {
                int averageRatio = linkDataCarouselBeanList.stream().filter(e -> e.getType().equals(key)).map(AppLinkDataCarouselBean::getAverageRatio).findFirst().orElse(0);
                int passCount = (int) newList.stream().filter(e -> util.getScoreByType(e.getId(), key) > value).count();
                int mathResult = CommonUtil.getIntRatio(passCount, newList.size(), 0);
                Preconditions.checkArgument(averageRatio == mathResult, "平均分达成率：" + averageRatio + "【员工接待评鉴列表】超过部门平均分的接待次数/参与评分的总接待次数：" + mathResult);
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("平均分达成率=【员工接待评鉴列表】超过部门平均分的接待次数/参与评分的总接待次数*100%");
        }
    }

    @Test
    public void department_data_16() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Map<Integer, Integer> map = new HashMap<>();
            Integer dataCycleType = 0;
            String startDate = DateTimeUtil.addDayFormat(new Date(), -1);
            String endDate = DateTimeUtil.getFormat(new Date());
            IScene receptionLinkScoreScene = AppReceptionLinkScoreScene.builder().startDate(startDate).endDate(endDate).dataCycleType(dataCycleType).build();
            util.toJavaObjectList(receptionLinkScoreScene, AppReceptionLinkScoreBean.class).forEach(e -> map.put(e.getType(), e.getDepartmentAverageScore()));
            List<AppPersonalPageBean> list = new ArrayList<>();
            util.getAppDepartmentPageList(startDate, endDate).stream().map(e -> util.getAppPersonalPageList(e.getId(), startDate, endDate)).forEach(list::addAll);
            List<AppPersonalPageBean> newList = list.stream().filter(e -> util.getAverageScoreByReceptionDetail(e.getId()) != null && util.getAverageScoreByReceptionDetail(e.getId()) != 0).collect(Collectors.toList());
            IScene linkDataCarouselScene = AppLinkDataCarouselScene.builder().startDate(startDate).endDate(endDate).dataCycleType(dataCycleType).build();
            List<AppLinkDataCarouselBean> linkDataCarouselBeanList = util.toJavaObjectList(linkDataCarouselScene, AppLinkDataCarouselBean.class);
            map.forEach((key, value) -> {
                int qualifiedRatio = linkDataCarouselBeanList.stream().filter(e -> e.getType().equals(key)).map(AppLinkDataCarouselBean::getQualifiedRatio).findFirst().orElse(0);
                int passCount = (int) newList.stream().filter(e -> util.getScoreByType(e.getId(), key) > 60).count();
                int mathResult = CommonUtil.getIntRatio(passCount, newList.size(), 0);
                Preconditions.checkArgument(qualifiedRatio == mathResult, "某一环节的接待合格率：" + qualifiedRatio + "【员工接待评鉴列表】超过60分的接待次数/参与评分的总接待次数：" + mathResult);
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("某一环节的接待合格率=【员工接待评鉴列表】超过60分的接待次数/参与评分的总接待次数*100%");
        }
    }

    @Test
    public void department_data_17() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Integer dataCycleType = 0;
            String startDate = DateTimeUtil.addDayFormat(new Date(), -1);
            String endDate = DateTimeUtil.getFormat(new Date());
            IScene scene = AppReceptionScoreTrendScene.builder().dataCycleType(dataCycleType).startDate(startDate).endDate(endDate).build();
            AppReceptionScoreTrendBean appReceptionScoreTrend = util.toFirstJavaObject(scene, AppReceptionScoreTrendBean.class);
            int sum = appReceptionScoreTrend.getOne() + appReceptionScoreTrend.getTwo() + appReceptionScoreTrend.getThree() + appReceptionScoreTrend.getFour() + appReceptionScoreTrend.getFive();
            int mathResult = CommonUtil.getIntRatio(sum, 5, 0);
            IScene scene1 = AppReceptionAverageScoreTrendScene.builder().dataCycleType(dataCycleType).startDate(startDate).endDate(endDate).build();
            AppReceptionAverageScoreTrendBean appReceptionAverageScoreTrend = util.toFirstJavaObject(scene1, AppReceptionAverageScoreTrendBean.class);
            int totalAverageScore = appReceptionAverageScoreTrend.getTotalAverageScore();
            Preconditions.checkArgument(totalAverageScore == mathResult, "（筛选某一天）【部门总平均分趋势】分数：" + totalAverageScore + "【各环节得分趋势】各话术环节平均分之和/5：" + mathResult);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("（筛选某一天）【部门总平均分趋势】分数=【各环节得分趋势】各话术环节平均分之和/5");
        }
    }

    @Test
    public void department_data_18() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Map<Integer, Integer> map = new HashMap<>();
            Integer dataCycleType = 0;
            String startDate = DateTimeUtil.addDayFormat(new Date(), -1);
            String endDate = DateTimeUtil.getFormat(new Date());
            IScene receptionScoreTrendScene = AppReceptionScoreTrendScene.builder().dataCycleType(dataCycleType).startDate(startDate).endDate(endDate).build();
            AppReceptionScoreTrendBean appReceptionScoreTrend = util.toFirstJavaObject(receptionScoreTrendScene, AppReceptionScoreTrendBean.class);
            IScene capabilityModelScene = AppCapabilityModelScene.builder().dataCycleType(dataCycleType).startDate(startDate).endDate(endDate).build();
            List<AppCapabilityModelBean> capabilityModelBeanList = util.toJavaObjectList(capabilityModelScene, AppCapabilityModelBean.class);
            capabilityModelBeanList.forEach(e -> map.put(e.getType(), e.getScore()));
            Preconditions.checkArgument(appReceptionScoreTrend.getOne().equals(map.get(100)), "【我的能力模型】各环节得分：" + map.get(100) + "【各环节得分趋势】中的各环节得分：" + appReceptionScoreTrend.getOne());
            Preconditions.checkArgument(appReceptionScoreTrend.getTwo().equals(map.get(200)), "【我的能力模型】各环节得分：" + map.get(200) + "【各环节得分趋势】中的各环节得分：" + appReceptionScoreTrend.getTwo());
            Preconditions.checkArgument(appReceptionScoreTrend.getThree().equals(map.get(300)), "【我的能力模型】各环节得分：" + map.get(300) + "【各环节得分趋势】中的各环节得分：" + appReceptionScoreTrend.getThree());
            Preconditions.checkArgument(appReceptionScoreTrend.getFour().equals(map.get(400)), "【我的能力模型】各环节得分：" + map.get(400) + "【各环节得分趋势】中的各环节得分：" + appReceptionScoreTrend.getFour());
            Preconditions.checkArgument(appReceptionScoreTrend.getFive().equals(map.get(500)), "【我的能力模型】各环节得分：" + map.get(500) + "【各环节得分趋势】中的各环节得分：" + appReceptionScoreTrend.getFive());
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("（筛选为某一天）【各环节得分趋势】中的各环节得分=【我的能力模型】各环节得分");
        }
    }

    @Test
    public void department_data_19() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Map<Integer, Integer> map = new HashMap<>();
            Integer dataCycleType = 0;
            String startDate = DateTimeUtil.addDayFormat(new Date(), -1);
            String endDate = DateTimeUtil.getFormat(new Date());
            IScene receptionScoreTrendScene = AppReceptionScoreTrendScene.builder().dataCycleType(dataCycleType).startDate(startDate).endDate(endDate).build();
            AppReceptionScoreTrendBean appReceptionScoreTrend = util.toFirstJavaObject(receptionScoreTrendScene, AppReceptionScoreTrendBean.class);
            IScene voiceEvaluationPageScene = VoiceEvaluationPageScene.builder().receptionStart(startDate).receptionEnd(endDate).build();
            List<VoiceEvaluationPageBean> evaluationPageBeanList = util.toJavaObjectList(voiceEvaluationPageScene, VoiceEvaluationPageBean.class);
            List<VoiceEvaluationPageBean> newList = evaluationPageBeanList.stream().filter(e -> e.getEvaluateScore() != null && e.getEvaluateScore() != 0).collect(Collectors.toList());
            List<JSONArray> scoresList = newList.stream().map(e -> util.toJavaObject(VoiceDetailScene.builder().id(e.getId()).build(), VoiceDetailBean.class)).map(VoiceDetailBean::getScores).collect(Collectors.toList());
            scoresList.forEach(e -> e.stream().map(a -> (JSONObject) a).forEach(a -> map.put(a.getInteger("type"), (getScore(map, a.getInteger("type")) + a.getInteger("score")) / newList.size())));
            Preconditions.checkArgument(appReceptionScoreTrend.getOne().equals(map.get(100)), "PC【语音接待评鉴】中相同时间段的首次到店的的接待详情中的各环节的平均分：" + map.get(100) + "【各环节得分趋势】中的各环节得分：" + appReceptionScoreTrend.getOne());
            Preconditions.checkArgument(appReceptionScoreTrend.getTwo().equals(map.get(200)), "PC【语音接待评鉴】中相同时间段的首次到店的的接待详情中的各环节的平均分：" + map.get(200) + "【各环节得分趋势】中的各环节得分：" + appReceptionScoreTrend.getTwo());
            Preconditions.checkArgument(appReceptionScoreTrend.getThree().equals(map.get(300)), "PC【语音接待评鉴】中相同时间段的首次到店的的接待详情中的各环节的平均分：" + map.get(300) + "【各环节得分趋势】中的各环节得分：" + appReceptionScoreTrend.getThree());
            Preconditions.checkArgument(appReceptionScoreTrend.getFour().equals(map.get(400)), "PC【语音接待评鉴】中相同时间段的首次到店的的接待详情中的各环节的平均分：" + map.get(400) + "【各环节得分趋势】中的各环节得分：" + appReceptionScoreTrend.getFour());
            Preconditions.checkArgument(appReceptionScoreTrend.getFive().equals(map.get(500)), "PC【语音接待评鉴】中相同时间段的首次到店的的接待详情中的各环节的平均分：" + map.get(500) + "【各环节得分趋势】中的各环节得分：" + appReceptionScoreTrend.getFive());
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("（筛选为某一天）【各环节得分趋势】中的各环节得分＝PC【语音接待评鉴】中相同时间段的首次到店的的接待详情中的各环节的平均分");
        }
    }

    private int getScore(@NotNull Map<Integer, Integer> map, Integer type) {
        return map.get(type) == null ? 0 : map.get(type);
    }

    @Test
    public void department_data_20() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Integer dataCycleType = 0;
            String startDate = DateTimeUtil.addDayFormat(new Date(), -1);
            String endDate = DateTimeUtil.getFormat(new Date());
            IScene receptionAverageScoreTrendScene = AppReceptionAverageScoreTrendScene.builder().dataCycleType(dataCycleType).startDate(startDate).endDate(endDate).build();
            AppReceptionAverageScoreTrendBean appReceptionAverageScoreTrendBean = util.toFirstJavaObject(receptionAverageScoreTrendScene, AppReceptionAverageScoreTrendBean.class);
            int totalAverageScore = appReceptionAverageScoreTrendBean.getTotalAverageScore();
            IScene capabilityModelScene = AppCapabilityModelScene.builder().dataCycleType(dataCycleType).startDate(startDate).endDate(endDate).build();
            int scoreSum = util.toJavaObjectList(capabilityModelScene, AppCapabilityModelBean.class).stream().mapToInt(AppCapabilityModelBean::getScore).sum();
            int mathResult = CommonUtil.getIntRatio(scoreSum, 5, 0);
            Preconditions.checkArgument(totalAverageScore == mathResult, "（筛选某一天）【部门总平均分趋势】平均分：" + totalAverageScore + "【我的能力模型】各话术环节平均分之和/5：" + mathResult);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("（筛选某一天）【部门总平均分趋势】平均分=【我的能力模型】各话术环节平均分之和/5");
        }
    }

    @Test
    public void department_data_21() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Integer dataCycleType = 0;
            String startDate = DateTimeUtil.addDayFormat(new Date(), -1);
            String endDate = DateTimeUtil.getFormat(new Date());
            IScene receptionAverageScoreTrendScene = AppReceptionAverageScoreTrendScene.builder().dataCycleType(dataCycleType).startDate(startDate).endDate(endDate).build();
            AppReceptionAverageScoreTrendBean appReceptionAverageScoreTrendBean = util.toFirstJavaObject(receptionAverageScoreTrendScene, AppReceptionAverageScoreTrendBean.class);
            int totalAverageScore = appReceptionAverageScoreTrendBean.getTotalAverageScore();
            IScene scene = VoiceEvaluationPageScene.builder().receptionStart(startDate).receptionEnd(endDate).build();
            List<VoiceEvaluationPageBean> evaluationPageBeanList = util.toJavaObjectList(scene, VoiceEvaluationPageBean.class);
            List<VoiceEvaluationPageBean> newList = evaluationPageBeanList.stream().filter(e -> e.getEvaluateScore() != null && e.getEvaluateScore() != 0).collect(Collectors.toList());
            int evaluateScoreSum = newList.stream().mapToInt(VoiceEvaluationPageBean::getEvaluateScore).sum();
            int mathResult = CommonUtil.getIntRatio(evaluateScoreSum, newList.size(), 0);
            Preconditions.checkArgument(totalAverageScore == mathResult, "（筛选某一天）【部门总平均分趋势】平均分：" + totalAverageScore + "PC【语音接待评鉴】中相同时间段的首次到店的接待评分之和平均分：" + mathResult);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("（筛选某一天）【部门总平均分趋势】平均分＝PC【语音接待评鉴】中相同时间段的首次到店的接待评分之和平均分");
        }
    }

    @Test
    public void department_data_22() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Integer dataCycleType = 0;
            String startDate = DateTimeUtil.addDayFormat(new Date(), -1);
            String endDate = DateTimeUtil.getFormat(new Date());
            IScene receptionAverageScoreTrendScene = AppReceptionAverageScoreTrendScene.builder().dataCycleType(dataCycleType).startDate(startDate).endDate(endDate).build();
            AppReceptionAverageScoreTrendBean appReceptionAverageScoreTrendBean = util.toFirstJavaObject(receptionAverageScoreTrendScene, AppReceptionAverageScoreTrendBean.class);
            int totalAverageScore = appReceptionAverageScoreTrendBean.getTotalAverageScore();
            IScene appOverviewScene = AppOverviewScene.builder().startDate(startDate).endDate(endDate).build();
            AppOverviewBean appOverviewBean = util.toJavaObject(appOverviewScene, AppOverviewBean.class);
            int averageScore = appOverviewBean.getAverageScore();
            Preconditions.checkArgument(totalAverageScore == averageScore, "（筛选某一天）【部门总平均分趋势】分数：" + totalAverageScore + "【部门接待评鉴】中的接待平均分：" + averageScore);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("（筛选某一天）【部门总平均分趋势】分数=【部门接待评鉴】中的接待平均分");
        }
    }

    @Test
    public void department_data_23() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Integer dataCycleType = 0;
            String startDate = DateTimeUtil.addDayFormat(new Date(), -1);
            String endDate = DateTimeUtil.getFormat(new Date());
            //todo 登录员工A账号
            String uid = "";
            IScene appOverviewScene = AppPersonalOverviewScene.builder().dataCycleType(dataCycleType).startDate(startDate).endDate(endDate).salesId(uid).build();
            AppPersonalOverviewBean overviewBean = util.toFirstJavaObject(appOverviewScene, AppPersonalOverviewBean.class);
            int averageDuration = overviewBean.getAverageDuration();
            //todo 登录部门账号
            IScene appDepartmentPageScene = AppDepartmentPageScene.builder().startDate(startDate).endDate(endDate).build();
            List<AppDepartmentPageBean> departmentPageBeanList = util.toJavaObjectList(appDepartmentPageScene, AppDepartmentPageBean.class);
            int mathResult = departmentPageBeanList.stream().filter(e -> e.getId().equals(uid)).map(e -> CommonUtil.getIntRatio(Math.toIntExact(e.getTotalScore()), e.getReceptionTimes(), 0)).collect(Collectors.toList()).get(0);
            Preconditions.checkArgument(averageDuration == mathResult, "（同一个员工）【员工接待评鉴】中的平均接待时长：" + averageDuration + "部门接待评鉴中的【员工接待评鉴】中的接待时长／接待次数：" + mathResult);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("（同一个员工）【员工接待评鉴】中的平均接待时长＝部门接待评鉴中的【员工接待评鉴】中的接待时长／接待次数");
        }
    }

    @Test
    public void personal_data_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String startDate = DateTimeUtil.addDayFormat(new Date(), -1);
            String endDate = DateTimeUtil.getFormat(new Date());
            //todo 登录员工A账号
            String uid = "";
            IScene appPersonalPageScene = AppPersonalPageScene.builder().startDate(startDate).endDate(endDate).salesId(uid).build();
            AppPersonalPageBean personalPageBean = util.toJavaObject(appPersonalPageScene, AppPersonalPageBean.class);
            IScene appDetailScene = AppDetailScene.builder().id(personalPageBean.getId()).build();
            AppDetailBean appDetailBean = util.toJavaObject(appDetailScene, AppDetailBean.class);
            int scoreSum = appDetailBean.getScores().stream().map(e -> (JSONObject) e).mapToInt(e -> e.getInteger("score")).sum();
            int mathResult = CommonUtil.getIntRatio(scoreSum, 5, 0);
            int averageScore = appDetailBean.getAverageScore();
            Preconditions.checkArgument(averageScore == mathResult, "本次接待得分：" + averageScore + "各环节的接待得分之和/5：" + mathResult);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("本次接待得分=各环节的接待得分之和/5");
        }
    }

    @Test
    public void personal_data_2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String startDate = DateTimeUtil.addDayFormat(new Date(), -1);
            String endDate = DateTimeUtil.getFormat(new Date());
            //todo 登录员工A账号
            String uid = "";
            List<AppPersonalPageBean> list = util.getAppPersonalPageList(uid, startDate, endDate);
            AppPersonalPageBean appPersonalPageBean = list.stream().filter(e -> util.getAverageScoreByReceptionDetail(e.getId()) != null && util.getAverageScoreByReceptionDetail(e.getId()) > 0).findFirst().orElse(null);
            assert appPersonalPageBean != null;
            IScene scene = AppDetailScene.builder().id(appPersonalPageBean.getId()).build();
            int departmentAverageScore = util.toJavaObject(scene, AppDetailBean.class).getDepartmentAverageScore();
            //todo 登录部门账号
            List<AppPersonalPageBean> personalPageBeanList = new ArrayList<>();
            util.getAppDepartmentPageList(startDate, endDate).stream().map(a -> util.getAppPersonalPageList(a.getId(), startDate, endDate)).forEach(personalPageBeanList::addAll);
            List<AppPersonalPageBean> newList = personalPageBeanList.stream().filter(e -> util.getAverageScoreByReceptionDetail(e.getId()) != null && util.getAverageScoreByReceptionDetail(e.getId()) != 0).collect(Collectors.toList());
            int scoreSum = newList.stream().mapToInt(e -> util.getScoreSum(e.getId())).sum();
            int mathResult = CommonUtil.getIntRatio(scoreSum, 5 * newList.size(), 0);
            Preconditions.checkArgument(departmentAverageScore == mathResult, "部门平均分：" + departmentAverageScore + "部门的全部员工全流程接待分值之和/参与评分的接待次数：" + mathResult);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("部门平均分=此部门的全部员工全流程接待分值之和/参与评分的接待次数");
        }
    }

    @Test
    public void personal_data_25() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String startDate = DateTimeUtil.addDayFormat(new Date(), -1);
            String endDate = DateTimeUtil.getFormat(new Date());
            List<AppPersonalPageBean> personalPageBeanList = new ArrayList<>();
            util.getAppDepartmentPageList(startDate, endDate).stream().map(a -> util.getAppPersonalPageList(a.getId(), startDate, endDate)).forEach(personalPageBeanList::addAll);
            List<AppPersonalPageBean> newList = personalPageBeanList.stream().filter(e -> util.getAverageScoreByReceptionDetail(e.getId()) != null && util.getAverageScoreByReceptionDetail(e.getId()) != 0).collect(Collectors.toList());
            int scoreSum = newList.stream().mapToInt(e -> util.getScoreSum(e.getId())).sum();
            int mathResult = CommonUtil.getIntRatio(scoreSum, 5 * newList.size(), 0);
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
            String startDate = DateTimeUtil.addDayFormat(new Date(), -1);
            String endDate = DateTimeUtil.getFormat(new Date());
            List<AppPersonalPageBean> personalPageBeanList = new ArrayList<>();
            util.getAppDepartmentPageList(startDate, endDate).stream().map(a -> util.getAppPersonalPageList(a.getId(), startDate, endDate)).forEach(personalPageBeanList::addAll);
            List<AppPersonalPageBean> newList = personalPageBeanList.stream().filter(e -> util.getAverageScoreByReceptionDetail(e.getId()) != null && util.getAverageScoreByReceptionDetail(e.getId()) != 0).collect(Collectors.toList());
            int scoreSum = newList.stream().mapToInt(e -> util.getScoreSum(e.getId())).sum();
            int mathResult = CommonUtil.getIntRatio(scoreSum, 5 * newList.size(), 0);
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
            int mathResult = CommonUtil.getIntRatio(scoreSum, 5, 0);
            Preconditions.checkArgument(averageData == mathResult, "接待平均分为：" + averageData + "能力模型平均分计算结果：" + mathResult);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("app我的能力模型各项数值=各环节得分的平均分");
        }
    }
}
