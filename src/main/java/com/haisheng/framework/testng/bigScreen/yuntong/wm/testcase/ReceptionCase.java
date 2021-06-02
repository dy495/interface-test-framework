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
import com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.app.departmentdata.AppOverviewBean;
import com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.app.personaldata.AppCapabilityModelBean;
import com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.app.voicerecord.AppDepartmentPageBean;
import com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.app.voicerecord.AppPersonalPageBean;
import com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.pc.manage.VoiceEvaluationPageBean;
import com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.app.departmentdata.AppOverviewScene;
import com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.app.personaldata.AppCapabilityModelScene;
import com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.app.personaldata.AppReceptionLinkScoreScene;
import com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.pc.manage.EvaluateV4ScoreTrendScene;
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
 * 业务管理测试用例
 *
 * @author wangmin
 * @date 2021/1/29 11:17
 */
public class ReceptionCase extends TestCaseCommon implements TestCaseStd {
    private static final EnumTestProduce PRODUCE = EnumTestProduce.YT_DAILY;
    private static final EnumAccount ALL_AUTHORITY = EnumAccount.ALL_AUTHORITY_DAILY;
    public VisitorProxy visitor = VisitorProxy.getInstance(PRODUCE);
    public BusinessUtil UTIL = BusinessUtil.getInstance(visitor);

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
//        user.loginPc(ALL_AUTHORITY);
        logger.debug("beforeMethod");
        caseResult = getFreshCaseResult(method);
        logger.debug("case: " + caseResult);
    }

    @Test
    public void department_data_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Integer dataCycleType = 0;
            String startDate = DateTimeUtil.addDayFormat(new Date(), -1);
            String endDate = DateTimeUtil.getFormat(new Date());
            IScene scene = AppOverviewScene.builder().dataCycleType(dataCycleType).startDate(startDate).endDate(endDate).build();
            AppOverviewBean appOverviewBean = UTIL.toJavaObject(scene, AppOverviewBean.class);
            int count = appOverviewBean.getCount();
            int totalDuration = appOverviewBean.getTotalDuration();
            int averageDuration = appOverviewBean.getAverageDuration();
            int mathResult = CommonUtil.getIntValue(totalDuration, count, 0);
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
            int averageData = UTIL.toJavaObject(appOverviewScene, AppOverviewBean.class).getAverageScore();
            IScene evaluationPageScene = VoiceEvaluationPageScene.builder().receptionStart(startDate).receptionEnd(endDate).build();
            List<VoiceEvaluationPageBean> evaluationPageBeanList = UTIL.toJavaObjectList(evaluationPageScene, VoiceEvaluationPageBean.class);
            List<VoiceEvaluationPageBean> newList = evaluationPageBeanList.stream().filter(e -> e.getEvaluateScore() != null & e.getEvaluateScore() != 0).collect(Collectors.toList());
            int evaluateScoreSum = newList.stream().mapToInt(VoiceEvaluationPageBean::getEvaluateScore).sum();
            int mathResult = CommonUtil.getIntValue(evaluateScoreSum, newList.size(), 0);
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
            int averageData = UTIL.toJavaObject(appOverviewScene, AppOverviewBean.class).getAverageScore();
            List<AppDepartmentPageBean> appDepartmentPageBeanList = UTIL.getAppDepartmentPageList(startDate, endDate);
            List<AppDepartmentPageBean> newList = appDepartmentPageBeanList.stream().filter(e -> e.getTotalScore() != null && e.getTotalScore() != 0).collect(Collectors.toList());
            long totalScoreSum = newList.stream().mapToLong(AppDepartmentPageBean::getTotalScore).sum();
            int mathResult = CommonUtil.getIntValue(Math.toIntExact(totalScoreSum), newList.size(), 0);
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
            int averageData = UTIL.toJavaObject(appOverviewScene, AppOverviewBean.class).getAverageScore();
            IScene scene = AppCapabilityModelScene.builder().startDate(startDate).endDate(endDate).build();
            int scoreSum = UTIL.toJavaObjectList(scene, AppCapabilityModelBean.class).stream().mapToInt(AppCapabilityModelBean::getScore).sum();
            int mathResult = CommonUtil.getIntValue(scoreSum, 5, 0);
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
            int averageData = UTIL.toJavaObject(appOverviewScene, AppOverviewBean.class).getAverageScore();
            List<AppPersonalPageBean> personalPageBeanList = new ArrayList<>();
            UTIL.getAppDepartmentPageList(startDate, endDate).stream().map(a -> UTIL.getAppPersonalPageList(a.getId(), startDate, endDate)).forEach(personalPageBeanList::addAll);
            List<AppPersonalPageBean> newList = personalPageBeanList.stream().filter(e -> UTIL.getAverageScoreByReceptionDetail(e.getId()) != null && UTIL.getAverageScoreByReceptionDetail(e.getId()) != 0).collect(Collectors.toList());
            int scoreSum = newList.stream().mapToInt(e -> UTIL.getAverageScoreByReceptionDetail(e.getId())).sum();
            int mathResult = CommonUtil.getIntValue(scoreSum, newList.size(), 0);
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
            int totalDuration = UTIL.toJavaObject(appOverviewScene, AppOverviewBean.class).getTotalDuration();
            IScene scene = VoiceEvaluationPageScene.builder().receptionStart(startDate).receptionEnd(endDate).build();
            List<VoiceEvaluationPageBean> evaluationPageBeanList = UTIL.toJavaObjectList(scene, VoiceEvaluationPageBean.class);
            int receptionTimeSum = evaluationPageBeanList.stream().map(VoiceEvaluationPageBean::getReceptionTime).mapToInt(DateTimeUtil::timeToSecond).sum();
            int mathResult = CommonUtil.getIntValue(receptionTimeSum, 60, 0);
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
            int totalDuration = UTIL.toJavaObject(appOverviewScene, AppOverviewBean.class).getTotalDuration();
            List<AppDepartmentPageBean> departmentPageBeanList = UTIL.getAppDepartmentPageList(startDate, endDate);
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
            int count = UTIL.toJavaObject(appOverviewScene, AppOverviewBean.class).getCount();
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
            int count = UTIL.toJavaObject(appOverviewScene, AppOverviewBean.class).getCount();
            int receptionRecordSum = UTIL.getAppDepartmentPageList(startDate, endDate).stream().map(e -> UTIL.getAppPersonalPageList(e.getId(), startDate, endDate).size()).mapToInt(e -> e).sum();
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
            int count = UTIL.toJavaObject(appOverviewScene, AppOverviewBean.class).getCount();
            int receptionTimesSum = UTIL.getAppDepartmentPageList(startDate, endDate).stream().mapToInt(AppDepartmentPageBean::getReceptionTimes).sum();
            Preconditions.checkArgument(count == receptionTimesSum, "app接待次数：" + count + "APP【员工接待评鉴】接待次数之和：" + receptionTimesSum);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("app接待次数=APP【员工接待评鉴】接待次数之和");
        }
    }

    @Test
    public void capabilityModel_data_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("app【我的能力模型】各话术环节分数相加/5=接待平均分");
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
    public void receptionCriticism_data_13() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Integer dataCycleType = 0;
            String startDate = "";
            String endDate = "";
            int averageData = AppOverviewScene.builder().dataCycleType(dataCycleType).startDate(startDate).endDate(endDate).build().invoke(visitor).getInteger("average_score");
            JSONArray capabilityModelList = AppCapabilityModelScene.builder().dataCycleType(dataCycleType).startDate(startDate).endDate(endDate).build().invoke(visitor).getJSONArray("list");
            int scoreSum = capabilityModelList.stream().map(e -> (JSONObject) e).mapToInt(e -> e.getInteger("score")).sum();
            int mathResult = CommonUtil.getIntValue(scoreSum, 5, 0);
            Preconditions.checkArgument(averageData == mathResult, "接待平均分为：" + averageData + "能力模型平均分计算结果：" + mathResult);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("app我的能力模型各项数值=各环节得分的平均分");
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
