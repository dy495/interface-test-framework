package com.haisheng.framework.testng.bigScreen.jiaochenonline.wm.testcase;

import com.haisheng.framework.testng.bigScreen.crm.wm.base.agency.Visitor;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.*;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.customer.EnumAppletToken;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.app.AppReceptionReceptorList;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.applet.AppletIntegralRecord;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.AppointmentPage;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.EvaluatePage;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.ReceptionPage;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.EnumAccount;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.EnumDesc;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.appointment.AppointmentConfirmStatusEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.appointment.AppointmentTypeEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.reception.after.ReceptionStatusEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.app.FollowUpCompleteScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.app.tack.AppAppointmentHandleScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.app.tack.AppAppointmentReceptionScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.app.tack.AppReceptionFinishReceptionScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.app.tack.AppReceptionReceptorChangePageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.granted.EvaluateSubmitScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.granted.HomePageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.appointmentmanager.AppointmentPageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.receptionmanager.ReceptionPageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.receptionmanager.ReceptorChangeScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.util.SupporterUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.util.UserUtil;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.util.CommonUtil;
import com.haisheng.framework.util.DateTimeUtil;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 小程序用例
 *
 * @author wangmin
 * @date 2021/1/29 11:17
 */
public class AppletManagerCaseOnline extends TestCaseCommon implements TestCaseStd {
    private static final EnumTestProduce product = EnumTestProduce.JIAOCHEN_ONLINE;
    private static final EnumAccount ADMINISTRATOR = EnumAccount.ADMINISTRATOR_ONLINE;
    //小程序用户
    private static final EnumAppletToken APPLET_USER_ONE = EnumAppletToken.JC_WM_ONLINE;
    //访问者
    public Visitor visitor = new Visitor(product);
    //登录工具
    public UserUtil user = new UserUtil(visitor);
    //封装方法
    public SupporterUtil util = new SupporterUtil(visitor);

    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before class initial");
        CommonConfig commonConfig = new CommonConfig();
        //替换checklist的相关信息
        commonConfig.checklistAppId = EnumChecklistAppId.DB_APP_ID_SCREEN_SERVICE.getId();
        commonConfig.checklistConfId = EnumChecklistConfId.DB_SERVICE_ID_CRM_DAILY_SERVICE.getId();
        commonConfig.checklistQaOwner = EnumChecklistUser.WM.getName();
        commonConfig.product = product.getAbbreviation();
        commonConfig.referer = product.getReferer();
        //替换jenkins-job的相关信息
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.JIAOCHEN_DAILY_TEST.getJobName());
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, product.getDesc() + commonConfig.checklistQaOwner);
        //替换钉钉推送
        commonConfig.dingHook = EnumDingTalkWebHook.CAR_OPEN_MANAGEMENT_PLATFORM_GRP.getWebHook();
        //放入shopId
        commonConfig.roleId = "603";
        commonConfig.referer = "https://servicewechat.com/wxbd41de85739a00c7/0/page-frame.html";
        commonConfig.shopId = product.getShopId();
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
        user.loginPc(ADMINISTRATOR);
        logger.debug("beforeMethod");
        caseResult = getFreshCaseResult(method);
        logger.debug("case: " + caseResult);
    }

    //ok
    @Test(description = "预约保养->确认预约->点接待->变更接待->完成接待->评价->跟进")
    public void appointmentManager_maintain() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            int i = 0;
            Integer shopId = util.getShopId();
            IScene appointmentPageScene = AppointmentPageScene.builder().build();
            int appointmentPageTotal = visitor.invokeApi(appointmentPageScene).getInteger("total");
            int appointmentNumber = util.appointmentNumber(DateTimeUtil.addDay(new Date(), i));
            user.loginApp(ADMINISTRATOR);
            int appAppointmentNum = util.getAppointmentPageNum();
            //预约保养
            user.loginApplet(APPLET_USER_ONE);
            int appointmentNum = util.getAppletAppointmentNum();
            int appointmentId = util.appointment(AppointmentTypeEnum.MAINTAIN, DateTimeUtil.addDayFormat(new Date(), i));
            int newAppointmentNum = util.getAppletAppointmentNum();
            CommonUtil.checkResult("applet我的预约列表数", appointmentNum + 1, newAppointmentNum);
            user.loginApp(ADMINISTRATOR);
            int newAppAppointmentNum = util.getAppointmentPageNum();
            CommonUtil.checkResult("app我的预约列表数", appAppointmentNum + 1, newAppAppointmentNum);
            user.loginPc(ADMINISTRATOR);
            int newAppointmentNumber = util.appointmentNumber(DateTimeUtil.addDay(new Date(), i));
            CommonUtil.checkResult("pc预约看板分子数", appointmentNumber + 1, newAppointmentNumber);
            int newAppointmentPageTotal = visitor.invokeApi(appointmentPageScene).getInteger("total");
            CommonUtil.checkResult("pc预约记录列表数", appointmentPageTotal + 1, newAppointmentPageTotal);
            AppointmentPage appointmentPage = util.getAppointmentPageById(appointmentId);
            CommonUtil.checkResult("预约类型", AppointmentTypeEnum.MAINTAIN.getValue(), appointmentPage.getTypeName());
            CommonUtil.checkResult("预约状态", AppointmentConfirmStatusEnum.WAITING.getStatusName(), appointmentPage.getAppointmentStatusName());
            CommonUtil.checkResult("是否可确认", true, appointmentPage.getIsCanConfirm());
            CommonUtil.checkResult("是否可接待", true, appointmentPage.getIsCanReception());
            CommonUtil.checkResult("是否可取消", true, appointmentPage.getIsCanCancel());
            CommonUtil.checkResult("是否可调整时间", true, appointmentPage.getIsCanAdjust());
            //确认预约
            user.loginApp(ADMINISTRATOR);
            int makeSureAppointmentNum = util.getAppointmentPageNum();
            IScene appointmentHandleScene = AppAppointmentHandleScene.builder().id(appointmentId).shopId(shopId).type(10).build();
            visitor.invokeApi(appointmentHandleScene);
            Integer newMakeSureAppAppointmentNum = util.getAppointmentPageNum();
            CommonUtil.checkResult("app预约记录数", makeSureAppointmentNum, newMakeSureAppAppointmentNum);
            user.loginPc(ADMINISTRATOR);
            AppointmentPage newAppointmentPage = util.getAppointmentPageById(appointmentId);
            CommonUtil.checkResult("预约状态", AppointmentConfirmStatusEnum.AGREE.getStatusName(), newAppointmentPage.getAppointmentStatusName());
            CommonUtil.checkResult("是否可确认", false, newAppointmentPage.getIsCanConfirm());
            CommonUtil.checkResult("是否可接待", true, newAppointmentPage.getIsCanReception());
            CommonUtil.checkResult("是否可取消", false, newAppointmentPage.getIsCanCancel());
            CommonUtil.checkResult("是否可调整时间", true, newAppointmentPage.getIsCanAdjust());
            //点接待
            user.loginApp(ADMINISTRATOR);
            int appReceptionPageNum = util.getReceptionPageNum();
            user.loginPc(ADMINISTRATOR);
            int pcReceptionPageNum = visitor.invokeApi(ReceptionPageScene.builder().build()).getInteger("total");
            IScene appointmentReceptionScene = AppAppointmentReceptionScene.builder().id(appointmentId).build();
            visitor.invokeApi(appointmentReceptionScene);
            int newAppReceptionPageNum = util.getReceptionPageNum();
            CommonUtil.checkResult("app接待页列表数", appReceptionPageNum + 1, newAppReceptionPageNum);
            user.loginPc(ADMINISTRATOR);
            int newPcReceptionPageNum = visitor.invokeApi(ReceptionPageScene.builder().build()).getInteger("total");
            CommonUtil.checkResult("pc接待列表数", pcReceptionPageNum + 1, newPcReceptionPageNum);
            ReceptionPage receptionPage = util.getFirstReceptionPage();
            CommonUtil.checkResult("接待状态", ReceptionStatusEnum.IN_RECEPTION.getStatusName(), receptionPage.getReceptionStatusName());
            CommonUtil.checkResult("接待类型", "预约", receptionPage.getReceptionTypeName());
            CommonUtil.checkResult("接待时间", DateTimeUtil.getFormat(new Date(), "yyyy-MM-dd HH:mm"), receptionPage.getReceptionTime());
            CommonUtil.checkResult("接待人", ADMINISTRATOR.getName(), receptionPage.getReceptionSaleName());
            CommonUtil.checkResult("注册状态", "已注册", receptionPage.getRegistrationStatusName());
            //变更接待
            int receptionId = receptionPage.getId();
            user.loginApp(ADMINISTRATOR);
            AppReceptionReceptorList receptorList = util.getReceptorList();
            String uid = receptorList.getUid();
            IScene receptionReceptorChangePageScene = AppReceptionReceptorChangePageScene.builder().id(receptionId).receptorId(uid).shopId(shopId).build();
            visitor.invokeApi(receptionReceptorChangePageScene);
            user.loginPc(ADMINISTRATOR);
            ReceptionPage newReceptionPage = util.getReceptionPageById(receptionId);
            CommonUtil.checkResult("变更接待后接待人员", receptorList.getName(), newReceptionPage.getReceptionSaleName());
            user.loginPc(ADMINISTRATOR);
            IScene receptorChangeScene = ReceptorChangeScene.builder().id(receptionId).receptorId(util.getReceptorList(ADMINISTRATOR).getUid()).shopId(shopId).build();
            visitor.invokeApi(receptorChangeScene);
            //登录此人app完成接待
            user.loginApp(ADMINISTRATOR);
            int finishReceptionNum = util.getReceptionPageNum();
            IScene receptionFinishReceptionScene = AppReceptionFinishReceptionScene.builder().id(receptionId).shopId(shopId).build();
            visitor.invokeApi(receptionFinishReceptionScene);
            int newFinishReceptionNum = util.getReceptionPageNum();
            CommonUtil.checkResult("完成接待后，app接待列表数", finishReceptionNum - 1, newFinishReceptionNum);
            user.loginPc(ADMINISTRATOR);
            ReceptionPage finishReceptionPage = util.getReceptionPageById(receptionId);
            CommonUtil.checkResult("完成接待后pc接待列表接待状态", ReceptionStatusEnum.FINISH.getStatusName(), finishReceptionPage.getReceptionStatusName());
            CommonUtil.checkResult("完成接待后pc完成时间", DateTimeUtil.getFormat(new Date(), "yyyy-MM-dd HH:mm"), finishReceptionPage.getFinishTime());
            //评价
            user.loginPc(ADMINISTRATOR);
            List<EvaluatePage> evaluatePageList = util.getEvaluatePageList();
            user.loginApplet(APPLET_USER_ONE);
            IScene evaluateSubmitScene = EvaluateSubmitScene.builder().id(appointmentId).shopId(shopId).type(1).score(4).describe(EnumDesc.MESSAGE_DESC.getDesc()).suggestion(EnumDesc.MESSAGE_DESC.getDesc()).isAnonymous(true).build();
            visitor.invokeApi(evaluateSubmitScene);
            user.loginPc(ADMINISTRATOR);
            List<EvaluatePage> newEvaluatePageList = util.getEvaluatePageList();
            CommonUtil.checkResult("评价列表数", evaluatePageList.size() + 1, newEvaluatePageList.size());
            CommonUtil.checkResult("评价描述", EnumDesc.MESSAGE_DESC.getDesc(), newEvaluatePageList.get(0).getDescribe());
            CommonUtil.checkResult("评价内容", EnumDesc.MESSAGE_DESC.getDesc(), newEvaluatePageList.get(0).getSuggestion());
            CommonUtil.checkResult("评价星星", 4, newEvaluatePageList.get(0).getScore());
            //跟进
            user.loginApp(ADMINISTRATOR);
            Integer followId = util.getFollowUpPageList().get(0).getId();
            IScene followUpCompleteScene = FollowUpCompleteScene.builder().id(followId).shopId(shopId).remark(EnumDesc.MESSAGE_DESC.getDesc()).build();
            visitor.invokeApi(followUpCompleteScene);
            user.loginPc(ADMINISTRATOR);
            List<EvaluatePage> followEvaluatePage = util.getEvaluatePageList();
            CommonUtil.checkResult("跟进后跟进备注", EnumDesc.MESSAGE_DESC.getDesc(), followEvaluatePage.get(0).getFollowUpRemark());
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("预约保养->确认预约->点接待->变更接待->完成接待->评价->跟进");
        }

    }

    @Test(description = "预约维修->确认预约->点接待->变更接待->完成接待->评价->跟进")
    public void appointmentManager_repair() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            int i = 0;
            Integer shopId = util.getShopId();
            IScene appointmentPageScene = AppointmentPageScene.builder().build();
            int appointmentPageTotal = visitor.invokeApi(appointmentPageScene).getInteger("total");
            int appointmentNumber = util.appointmentNumber(DateTimeUtil.addDay(new Date(), i));
            user.loginApp(ADMINISTRATOR);
            int appAppointmentNum = util.getAppointmentPageNum();
            //预约维修
            user.loginApplet(APPLET_USER_ONE);
            int appointmentNum = util.getAppletAppointmentNum();
            int appointmentId = util.appointment(AppointmentTypeEnum.REPAIR, DateTimeUtil.addDayFormat(new Date(), i));
            int newAppointmentNum = util.getAppletAppointmentNum();
            CommonUtil.checkResult("applet我的预约列表数", appointmentNum + 1, newAppointmentNum);
            user.loginApp(ADMINISTRATOR);
            int newAppAppointmentNum = util.getAppointmentPageNum();
            CommonUtil.checkResult("app我的预约列表数", appAppointmentNum + 1, newAppAppointmentNum);
            user.loginPc(ADMINISTRATOR);
            int newAppointmentNumber = util.appointmentNumber(DateTimeUtil.addDay(new Date(), i));
            CommonUtil.checkResult("pc预约看板分子数", appointmentNumber + 1, newAppointmentNumber);
            int newAppointmentPageTotal = visitor.invokeApi(appointmentPageScene).getInteger("total");
            CommonUtil.checkResult("pc预约记录列表数", appointmentPageTotal + 1, newAppointmentPageTotal);
            AppointmentPage appointmentPage = util.getAppointmentPageById(appointmentId);
            CommonUtil.checkResult("预约类型", AppointmentTypeEnum.REPAIR.getValue(), appointmentPage.getTypeName());
            CommonUtil.checkResult("预约状态", AppointmentConfirmStatusEnum.WAITING.getStatusName(), appointmentPage.getAppointmentStatusName());
            CommonUtil.checkResult("是否可确认", true, appointmentPage.getIsCanConfirm());
            CommonUtil.checkResult("是否可接待", true, appointmentPage.getIsCanReception());
            CommonUtil.checkResult("是否可取消", true, appointmentPage.getIsCanCancel());
            CommonUtil.checkResult("是否可调整时间", true, appointmentPage.getIsCanAdjust());
            //确认预约
            user.loginApp(ADMINISTRATOR);
            int makeSureAppointmentNum = util.getAppointmentPageNum();
            IScene appointmentHandleScene = AppAppointmentHandleScene.builder().id(appointmentId).shopId(shopId).type(10).build();
            visitor.invokeApi(appointmentHandleScene);
            Integer newMakeSureAppAppointmentNum = util.getAppointmentPageNum();
            CommonUtil.checkResult("app预约记录数", makeSureAppointmentNum, newMakeSureAppAppointmentNum);
            user.loginPc(ADMINISTRATOR);
            AppointmentPage newAppointmentPage = util.getAppointmentPageById(appointmentId);
            CommonUtil.checkResult("预约状态", AppointmentConfirmStatusEnum.AGREE.getStatusName(), newAppointmentPage.getAppointmentStatusName());
            CommonUtil.checkResult("是否可确认", false, newAppointmentPage.getIsCanConfirm());
            CommonUtil.checkResult("是否可接待", true, newAppointmentPage.getIsCanReception());
            CommonUtil.checkResult("是否可取消", false, newAppointmentPage.getIsCanCancel());
            CommonUtil.checkResult("是否可调整时间", true, newAppointmentPage.getIsCanAdjust());
            //点接待
            user.loginApp(ADMINISTRATOR);
            int appReceptionPageNum = util.getReceptionPageNum();
            user.loginPc(ADMINISTRATOR);
            int pcReceptionPageNum = visitor.invokeApi(ReceptionPageScene.builder().build()).getInteger("total");
            IScene appointmentReceptionScene = AppAppointmentReceptionScene.builder().id(appointmentId).build();
            visitor.invokeApi(appointmentReceptionScene);
            int newAppReceptionPageNum = util.getReceptionPageNum();
            CommonUtil.checkResult("app接待页列表数", appReceptionPageNum + 1, newAppReceptionPageNum);
            user.loginPc(ADMINISTRATOR);
            int newPcReceptionPageNum = visitor.invokeApi(ReceptionPageScene.builder().build()).getInteger("total");
            CommonUtil.checkResult("pc接待列表数", pcReceptionPageNum + 1, newPcReceptionPageNum);
            ReceptionPage receptionPage = util.getFirstReceptionPage();
            CommonUtil.checkResult("接待状态", ReceptionStatusEnum.IN_RECEPTION.getStatusName(), receptionPage.getReceptionStatusName());
            CommonUtil.checkResult("接待类型", "预约", receptionPage.getReceptionTypeName());
            CommonUtil.checkResult("接待时间", DateTimeUtil.getFormat(new Date(), "yyyy-MM-dd HH:mm"), receptionPage.getReceptionTime());
            CommonUtil.checkResult("接待人", ADMINISTRATOR.getName(), receptionPage.getReceptionSaleName());
            CommonUtil.checkResult("注册状态", "已注册", receptionPage.getRegistrationStatusName());
            //变更接待
            int receptionId = receptionPage.getId();
            user.loginApp(ADMINISTRATOR);
            AppReceptionReceptorList receptorList = util.getReceptorList();
            String uid = receptorList.getUid();
            IScene receptionReceptorChangePageScene = AppReceptionReceptorChangePageScene.builder().id(receptionId).receptorId(uid).shopId(shopId).build();
            visitor.invokeApi(receptionReceptorChangePageScene);
            user.loginPc(ADMINISTRATOR);
            ReceptionPage newReceptionPage = util.getReceptionPageById(receptionId);
            CommonUtil.checkResult("变更接待后接待人员", receptorList.getName(), newReceptionPage.getReceptionSaleName());
            user.loginPc(ADMINISTRATOR);
            IScene receptorChangeScene = ReceptorChangeScene.builder().id(receptionId).receptorId(util.getReceptorList(ADMINISTRATOR).getUid()).shopId(shopId).build();
            visitor.invokeApi(receptorChangeScene);
            //登录此人app完成接待
            user.loginApp(ADMINISTRATOR);
            int finishReceptionNum = util.getReceptionPageNum();
            IScene receptionFinishReceptionScene = AppReceptionFinishReceptionScene.builder().id(receptionId).shopId(shopId).build();
            visitor.invokeApi(receptionFinishReceptionScene);
            int newFinishReceptionNum = util.getReceptionPageNum();
            CommonUtil.checkResult("完成接待后，app接待列表数", finishReceptionNum - 1, newFinishReceptionNum);
            user.loginPc(ADMINISTRATOR);
            ReceptionPage finishReceptionPage = util.getReceptionPageById(receptionId);
            CommonUtil.checkResult("完成接待后pc接待列表接待状态", ReceptionStatusEnum.FINISH.getStatusName(), finishReceptionPage.getReceptionStatusName());
            CommonUtil.checkResult("完成接待后pc完成时间", DateTimeUtil.getFormat(new Date(), "yyyy-MM-dd HH:mm"), finishReceptionPage.getFinishTime());
            //评价
            user.loginPc(ADMINISTRATOR);
            List<EvaluatePage> evaluatePageList = util.getEvaluatePageList();
            user.loginApplet(APPLET_USER_ONE);
            IScene evaluateSubmitScene = EvaluateSubmitScene.builder().id(appointmentId).shopId(shopId).type(2).score(4).describe(EnumDesc.MESSAGE_DESC.getDesc()).suggestion(EnumDesc.MESSAGE_DESC.getDesc()).isAnonymous(true).build();
            visitor.invokeApi(evaluateSubmitScene);
            user.loginPc(ADMINISTRATOR);
            List<EvaluatePage> newEvaluatePageList = util.getEvaluatePageList();
            CommonUtil.checkResult("评价列表数", evaluatePageList.size() + 1, newEvaluatePageList.size());
            CommonUtil.checkResult("评价描述", EnumDesc.MESSAGE_DESC.getDesc(), newEvaluatePageList.get(0).getDescribe());
            CommonUtil.checkResult("评价内容", EnumDesc.MESSAGE_DESC.getDesc(), newEvaluatePageList.get(0).getSuggestion());
            CommonUtil.checkResult("评价星星", 4, newEvaluatePageList.get(0).getScore());
            //跟进
            user.loginApp(ADMINISTRATOR);
            Integer followId = util.getFollowUpPageList().get(0).getId();
            IScene followUpCompleteScene = FollowUpCompleteScene.builder().id(followId).shopId(shopId).remark(EnumDesc.MESSAGE_DESC.getDesc()).build();
            visitor.invokeApi(followUpCompleteScene);
            user.loginPc(ADMINISTRATOR);
            List<EvaluatePage> followEvaluatePage = util.getEvaluatePageList();
            CommonUtil.checkResult("跟进后跟进备注", EnumDesc.MESSAGE_DESC.getDesc(), followEvaluatePage.get(0).getFollowUpRemark());
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("预约维修->确认预约->点接待->变更接待->完成接待->评价->跟进");
        }
    }

    //ok
    @Test(description = "小程序--积分总数=积分明细所有项加和")
    public void integralMall_data_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            user.loginApplet(APPLET_USER_ONE);
            IScene homePageScene = HomePageScene.builder().build();
            Integer integral = visitor.invokeApi(homePageScene).getInteger("integral");
            AtomicInteger integralSum = new AtomicInteger();
            List<AppletIntegralRecord> appletIntegralRecordList = util.getAppletIntegralRecordList();
            appletIntegralRecordList.forEach(e -> {
                String changeType = e.getChangeType();
                String integralDetail = e.getIntegral();
                integralSum.set(changeType.equals("ADD") ? integralSum.addAndGet(Integer.parseInt(integralDetail)) : integralSum.addAndGet(-Integer.parseInt(integralDetail)));
            });
            CommonUtil.checkResultPlus("积分总数", integral, "积分明细积分相加", integralSum.get());
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("小程序--积分总数=积分明细所有项加和");
        }
    }
}
