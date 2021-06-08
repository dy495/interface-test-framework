package com.haisheng.framework.testng.bigScreen.jiaochen.wm.testcase;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumAppletToken;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumChecklistUser;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumJobName;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumTestProduce;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.app.AppAppointmentPage;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.app.AppReceptionReceptorList;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.applet.AppletCommodity;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.applet.AppletIntegralRecord;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.applet.AppletShippingAddress;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.*;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.appointmentmanage.AppointmentRecordAppointmentPageBean;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.manage.EvaluatePageBean;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.presalesreception.PreSalesReceptionPageBean;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.EnumAccount;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.EnumDesc;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.Integral.ChangeStockTypeEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.Integral.CommodityTypeEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.Integral.IntegralExchangeStatusEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.Integral.SortTypeEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.appointment.AppointmentConfirmStatusEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.appointment.AppointmentTypeEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.appointment.MaintainStatusEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.appointment.RepairStatusEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.evaluate.EvaluateTypeEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing.AppletCodeBusinessTypeEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing.VoucherSourceEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing.VoucherStatusEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.message.AppletMessageTypeEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.reception.after.ReceptionStatusEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.generate.voucher.VoucherGenerator;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.granted.*;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.mapp.AppFollowUpCompleteScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.mapp.presalesreception.AppFinishReceptionScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.mapp.presalesreception.AppReceptorChangeScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.mapp.task.*;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.appointmentmanage.AppointmentPageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.integralcenter.*;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.manage.EvaluatePageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.presalesreception.PreSalesReceptionPageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.receptionmanage.ReceptionPageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.receptionmanage.ReceptorChangeScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.util.SupporterUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.util.UserUtil;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
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
import java.util.stream.Collectors;

/**
 * 小程序用例
 *
 * @author wangmin
 * @date 2021/1/29 11:17
 */
public class AppletManagerCase extends TestCaseCommon implements TestCaseStd {
    private static final EnumTestProduce PRODUCE = EnumTestProduce.JC_DAILY;
    private static final EnumAccount ALL_AUTHORITY = EnumAccount.ALL_AUTHORITY_DAILY;
    private static final EnumAppletToken APPLET_USER_ONE = EnumAppletToken.JC_WM_DAILY;
    public VisitorProxy visitor = new VisitorProxy(PRODUCE);
    public UserUtil user = new UserUtil(visitor);
    public SupporterUtil util = new SupporterUtil(visitor);

    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before class initial");
        CommonConfig commonConfig = new CommonConfig();
        //替换checklist的相关信息
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

    //ok
    @Test(description = "预约保养->确认预约->点接待->变更接待->完成接待->评价->跟进")
    public void appointmentManager_maintain() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Date appointmentBoardDate = DateTimeUtil.addDay(new Date(), 0);
            String appointmentDate = DateTimeUtil.addDayFormat(new Date(), 0);
            AppointmentTypeEnum appointmentTypeEnum = AppointmentTypeEnum.MAINTAIN;
            Long shopId = util.getShopId();
            IScene appointmentPageScene = AppointmentPageScene.builder().type(appointmentTypeEnum.name()).build();
            int appointmentPageTotal = appointmentPageScene.invoke(visitor).getInteger("total");
            int appointmentNumber = util.appointmentNumber(appointmentBoardDate, appointmentTypeEnum.name());
            user.loginApp(ALL_AUTHORITY);
            int appAppointmentNum = util.getAppointmentPageNum();
            //预约保养
            user.loginApplet(APPLET_USER_ONE);
            int appointmentNum = util.getAppletAppointmentNum();
            Long appointmentId = util.appointment(appointmentTypeEnum, appointmentDate);
            int newAppointmentNum = util.getAppletAppointmentNum();
            CommonUtil.checkResult("applet我的预约列表数", appointmentNum + 1, newAppointmentNum);
            user.loginApp(ALL_AUTHORITY);
            int newAppAppointmentNum = util.getAppointmentPageNum();
            CommonUtil.checkResult("app我的预约列表数", appAppointmentNum + 1, newAppAppointmentNum);
            IScene appAppointmentPageScene = AppAppointmentPageScene.builder().build();
            AppAppointmentPage appAppointmentPage = util.toFirstJavaObject(appAppointmentPageScene, AppAppointmentPage.class);
            CommonUtil.checkResult("app预约类型", appointmentTypeEnum.getValue(), appAppointmentPage.getTypeName());
            CommonUtil.checkResult("app预约类型", appointmentTypeEnum.name(), appAppointmentPage.getType());
            CommonUtil.checkResult("app是否可确认", true, appAppointmentPage.getIsCanConfirm());
            CommonUtil.checkResult("app是否可接待", true, appAppointmentPage.getIsCanReception());
            CommonUtil.checkResult("app是否可取消", true, appAppointmentPage.getIsCanCancel());
            CommonUtil.checkResult("app是否可调整时间", true, appAppointmentPage.getIsCanAdjust());
            user.loginPc(ALL_AUTHORITY);
            int newAppointmentNumber = util.appointmentNumber(appointmentBoardDate, appointmentTypeEnum.name());
            CommonUtil.checkResult("pc预约看板分子数", appointmentNumber + 1, newAppointmentNumber);
            int newAppointmentPageTotal = appointmentPageScene.invoke(visitor).getInteger("total");
            CommonUtil.checkResult("pc预约记录列表数", appointmentPageTotal + 1, newAppointmentPageTotal);
            AppointmentRecordAppointmentPageBean appointmentPage = util.getAppointmentPageById(appointmentId, appointmentTypeEnum.name());
            CommonUtil.checkResult("预约类型", appointmentTypeEnum.getValue(), appointmentPage.getTypeName());
            CommonUtil.checkResult("预约状态", AppointmentConfirmStatusEnum.WAITING.getStatusName(), appointmentPage.getAppointmentStatusName());
            CommonUtil.checkResult("是否可确认", true, appointmentPage.getIsCanConfirm());
            CommonUtil.checkResult("是否可接待", true, appointmentPage.getIsCanReception());
            CommonUtil.checkResult("是否可取消", true, appointmentPage.getIsCanCancel());
            CommonUtil.checkResult("是否可调整时间", true, appointmentPage.getIsCanAdjust());
            //确认预约
            user.loginApp(ALL_AUTHORITY);
            int makeSureAppointmentNum = util.getAppointmentPageNum();
            AppAppointmentHandleScene.builder().id(appointmentId).shopId(shopId).type(10).build().invoke(visitor);
            Integer newMakeSureAppAppointmentNum = util.getAppointmentPageNum();
            CommonUtil.checkResult("app预约记录数", makeSureAppointmentNum, newMakeSureAppAppointmentNum);
            user.loginPc(ALL_AUTHORITY);
            AppointmentRecordAppointmentPageBean newAppointmentPage = util.getAppointmentPageById(appointmentId, appointmentTypeEnum.name());
            CommonUtil.checkResult("预约状态", AppointmentConfirmStatusEnum.AGREE.getStatusName(), newAppointmentPage.getAppointmentStatusName());
            CommonUtil.checkResult("是否可确认", false, newAppointmentPage.getIsCanConfirm());
            CommonUtil.checkResult("是否可接待", true, newAppointmentPage.getIsCanReception());
            CommonUtil.checkResult("是否可取消", true, newAppointmentPage.getIsCanCancel());
            CommonUtil.checkResult("是否可调整时间", true, newAppointmentPage.getIsCanAdjust());
            //点接待
            user.loginApp(ALL_AUTHORITY);
            int appReceptionPageNum = util.getReceptionPageNum();
            user.loginPc(ALL_AUTHORITY);
            IScene receptionPageScene = ReceptionPageScene.builder().build();
            int pcReceptionPageNum = receptionPageScene.invoke(visitor).getInteger("total");
            AppAppointmentReceptionScene.builder().id(appointmentId).build().invoke(visitor);
            int newAppReceptionPageNum = util.getReceptionPageNum();
            CommonUtil.checkResult("app接待页列表数", appReceptionPageNum + 1, newAppReceptionPageNum);
            user.loginPc(ALL_AUTHORITY);
            int newPcReceptionPageNum = receptionPageScene.invoke(visitor).getInteger("total");
            CommonUtil.checkResult("pc接待列表数", pcReceptionPageNum + 1, newPcReceptionPageNum);
            ReceptionPage receptionPage = util.getFirstReceptionPage();
            CommonUtil.checkResult("接待状态", ReceptionStatusEnum.IN_RECEPTION.getStatusName(), receptionPage.getReceptionStatusName());
            CommonUtil.checkResult("接待类型", "预约", receptionPage.getReceptionTypeName());
            CommonUtil.checkResult("接待人", ALL_AUTHORITY.getName(), receptionPage.getReceptionSaleName());
            CommonUtil.checkResult("注册状态", "已注册", receptionPage.getRegistrationStatusName());
            //变更接待
            Long receptionId = receptionPage.getId();
            user.loginApp(ALL_AUTHORITY);
            AppReceptionReceptorList receptorList = util.getReceptorList();
            String uid = receptorList.getUid();
            AppReceptionReceptorChangeScene.builder().id(receptionId).receptorId(uid).shopId(shopId).build().invoke(visitor);
            user.loginPc(ALL_AUTHORITY);
            ReceptionPage newReceptionPage = util.getReceptionPageById(receptionId);
            CommonUtil.checkResult("变更接待后接待人员", receptorList.getName(), newReceptionPage.getReceptionSaleName());
            user.loginPc(ALL_AUTHORITY);
            ReceptorChangeScene.builder().id(receptionId).receptorId(util.getReceptorList(ALL_AUTHORITY).getUid()).shopId(shopId).build().invoke(visitor);
            //登录此人app完成接待
            user.loginApp(ALL_AUTHORITY);
            int finishReceptionNum = util.getReceptionPageNum();
            AppReceptionFinishReceptionScene.builder().id(receptionId).shopId(shopId).build().invoke(visitor);
            int newFinishReceptionNum = util.getReceptionPageNum();
            CommonUtil.checkResult("完成接待后，app接待列表数", finishReceptionNum - 1, newFinishReceptionNum);
            user.loginPc(ALL_AUTHORITY);
            ReceptionPage finishReceptionPage = util.getReceptionPageById(receptionId);
            CommonUtil.checkResult("完成接待后pc接待列表接待状态", MaintainStatusEnum.TO_BE_EVALUATED.getStatusName(), finishReceptionPage.getReceptionStatusName());
            //小程序评价状态
            user.loginApplet(APPLET_USER_ONE);
            IScene appletAppointmentListScene = AppletAppointmentListScene.builder().build();
            JSONObject object = appletAppointmentListScene.invoke(visitor).getJSONArray("list").getJSONObject(0);
            String statusName = object.getString("status_name");
            CommonUtil.checkResult("完成接待后applet预约状态", MaintainStatusEnum.TO_BE_EVALUATED.getStatusName(), statusName);
            //消息状态
            IScene appletMessageListScene = AppletMessageListScene.builder().build();
            JSONObject messageObject = appletMessageListScene.invoke(visitor).getJSONArray("list").getJSONObject(0);
            String messageTypeName = messageObject.getString("message_type_name");
            Integer messageType = messageObject.getInteger("message_type");
            Boolean isRead = messageObject.getBoolean("is_read");
            String title = messageObject.getString("title");
            Long messageId = messageObject.getLong("id");
            CommonUtil.checkResult("完成接待后applet消息类型", AppletMessageTypeEnum.MAINTAIN_EVALUATE_TIP.getServiceType().getId(), messageType);
            CommonUtil.checkResult("完成接待后applet消息类型", AppletMessageTypeEnum.MAINTAIN_EVALUATE_TIP.getTypeName(), messageTypeName);
            CommonUtil.checkResult("完成接待后applet消息是否已读", false, isRead);
            CommonUtil.checkResult("完成接待后applet消息标题", AppletMessageTypeEnum.MAINTAIN_EVALUATE_TIP.getServiceType().getMsgTitle(), title);
            IScene appletMessageDetailScene = AppletMessageDetailScene.builder().id(messageId).build();
            JSONObject messageDetailObject = appletMessageDetailScene.invoke(visitor);
            Boolean isCanEvaluate = messageDetailObject.getBoolean("is_can_evaluate");
            String content = messageDetailObject.getString("content");
            CommonUtil.checkResult("完成接待后applet消息中可评价状态", true, isCanEvaluate);
            Preconditions.checkArgument(content.contains("已经完成服务了，请您对我们的服务进行评价！"), "消息内容应不包含已经完成服务了，请您对我们的服务进行评价！");
            //评价
            user.loginPc(ALL_AUTHORITY);
            IScene evaluatePageScene = EvaluatePageScene.builder().evaluateType(EvaluateTypeEnum.MAINTAIN.getId()).build();
            Long total = evaluatePageScene.invoke(visitor).getLong("total");
            user.loginApplet(APPLET_USER_ONE);
            AppletEvaluateSubmitScene.builder().id(receptionId).shopId(shopId).type(1).score(4).isAnonymous(true).describe(EnumDesc.DESC_BETWEEN_40_50.getDesc()).suggestion(EnumDesc.DESC_BETWEEN_40_50.getDesc()).build().invoke(visitor);
            //评价完成后
            JSONObject newMessageDetailObject = appletMessageDetailScene.invoke(visitor);
            Boolean newIsCanEvaluate = newMessageDetailObject.getBoolean("is_can_evaluate");
            CommonUtil.checkResult("完成评价后applet消息中可评价状态", false, newIsCanEvaluate);
            String newStatusName = appletAppointmentListScene.invoke(visitor).getJSONArray("list").getJSONObject(0).getString("status_name");
            CommonUtil.checkResult("完成评价后applet预约状态", MaintainStatusEnum.EVALUATED.getStatusName(), newStatusName);
            user.loginPc(ALL_AUTHORITY);
            JSONObject evaluatePageObject = evaluatePageScene.invoke(visitor);
            Long newTotal = evaluatePageObject.getLong("total");
            EvaluatePageBean evaluatePageBean = util.toFirstJavaObject(evaluatePageScene, EvaluatePageBean.class);
            CommonUtil.checkResult("评价列表数", total + 1, newTotal);
            CommonUtil.checkResult("评价描述", EnumDesc.DESC_BETWEEN_40_50.getDesc(), evaluatePageBean.getDescribe());
            CommonUtil.checkResult("评价内容", EnumDesc.DESC_BETWEEN_40_50.getDesc(), evaluatePageBean.getSuggestion());
            CommonUtil.checkResult("评价星星", 4, evaluatePageBean.getScore());
            //跟进
            user.loginApp(ALL_AUTHORITY);
            Integer followId = util.getFollowUpPageList().get(0).getId();
            AppFollowUpCompleteScene.builder().id(followId).shopId(shopId).remark(EnumDesc.DESC_BETWEEN_40_50.getDesc()).build().invoke(visitor);
            user.loginPc(ALL_AUTHORITY);
            EvaluatePageBean followEvaluatePage = util.toFirstJavaObject(evaluatePageScene, EvaluatePageBean.class);
            CommonUtil.checkResult("跟进后跟进备注", EnumDesc.DESC_BETWEEN_40_50.getDesc(), followEvaluatePage.getFollowUpRemark());
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("预约保养->确认预约->点接待->变更接待->完成接待->评价->跟进");
        }
    }

    //ok
    @Test(description = "预约维修->确认预约->点接待->变更接待->完成接待->评价->跟进")
    public void appointmentManager_repair() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String appointmentDate = DateTimeUtil.addDayFormat(new Date(), 0);
            AppointmentTypeEnum appointmentTypeEnum = AppointmentTypeEnum.REPAIR;
            AppletMessageTypeEnum appletMessageTypeEnum = AppletMessageTypeEnum.REPAIR_EVALUATE_TIP;
            Long shopId = util.getShopId();
            IScene appointmentPageScene = AppointmentPageScene.builder().type(appointmentTypeEnum.name()).build();
            int appointmentPageTotal = appointmentPageScene.invoke(visitor).getInteger("total");
            user.loginApp(ALL_AUTHORITY);
            int appAppointmentNum = util.getAppointmentPageNum();
            //预约维修
            user.loginApplet(APPLET_USER_ONE);
            int appointmentNum = util.getAppletAppointmentNum();
            Long appointmentId = util.appointment(appointmentTypeEnum, appointmentDate);
            int newAppointmentNum = util.getAppletAppointmentNum();
            CommonUtil.checkResult("applet我的预约列表数", appointmentNum + 1, newAppointmentNum);
            user.loginApp(ALL_AUTHORITY);
            int newAppAppointmentNum = util.getAppointmentPageNum();
            CommonUtil.checkResult("app我的预约列表数", appAppointmentNum + 1, newAppAppointmentNum);
            IScene appAppointmentPageScene = AppAppointmentPageScene.builder().build();
            AppAppointmentPage appAppointmentPage = util.toFirstJavaObject(appAppointmentPageScene, AppAppointmentPage.class);
            CommonUtil.checkResult("app预约类型", appointmentTypeEnum.getValue(), appAppointmentPage.getTypeName());
            CommonUtil.checkResult("app预约类型", appointmentTypeEnum.name(), appAppointmentPage.getType());
            CommonUtil.checkResult("app是否可确认", true, appAppointmentPage.getIsCanConfirm());
            CommonUtil.checkResult("app是否可接待", true, appAppointmentPage.getIsCanReception());
            CommonUtil.checkResult("app是否可取消", true, appAppointmentPage.getIsCanCancel());
            CommonUtil.checkResult("app是否可调整时间", true, appAppointmentPage.getIsCanAdjust());
            user.loginPc(ALL_AUTHORITY);
            int newAppointmentPageTotal = appointmentPageScene.invoke(visitor).getInteger("total");
            CommonUtil.checkResult("pc预约记录列表数", appointmentPageTotal + 1, newAppointmentPageTotal);
            AppointmentRecordAppointmentPageBean appointmentPage = util.getAppointmentPageById(appointmentId, appointmentTypeEnum.name());
            CommonUtil.checkResult("预约类型", appointmentTypeEnum.getValue(), appointmentPage.getTypeName());
            CommonUtil.checkResult("预约状态", AppointmentConfirmStatusEnum.WAITING.getStatusName(), appointmentPage.getAppointmentStatusName());
            CommonUtil.checkResult("是否可确认", true, appointmentPage.getIsCanConfirm());
            CommonUtil.checkResult("是否可接待", true, appointmentPage.getIsCanReception());
            CommonUtil.checkResult("是否可取消", true, appointmentPage.getIsCanCancel());
            CommonUtil.checkResult("是否可调整时间", true, appointmentPage.getIsCanAdjust());
            CommonUtil.checkResult("故障描述", EnumDesc.DESC_BETWEEN_15_20.getDesc(), appointmentPage.getFaultDescription());
            //确认预约
            user.loginApp(ALL_AUTHORITY);
            int makeSureAppointmentNum = util.getAppointmentPageNum();
            AppAppointmentHandleScene.builder().id(appointmentId).shopId(shopId).type(10).build().invoke(visitor);
            Integer newMakeSureAppAppointmentNum = util.getAppointmentPageNum();
            CommonUtil.checkResult("app预约记录数", makeSureAppointmentNum, newMakeSureAppAppointmentNum);
            user.loginPc(ALL_AUTHORITY);
            AppointmentRecordAppointmentPageBean newAppointmentPage = util.getAppointmentPageById(appointmentId, appointmentTypeEnum.name());
            CommonUtil.checkResult("预约状态", AppointmentConfirmStatusEnum.AGREE.getStatusName(), newAppointmentPage.getAppointmentStatusName());
            CommonUtil.checkResult("是否可确认", false, newAppointmentPage.getIsCanConfirm());
            CommonUtil.checkResult("是否可接待", true, newAppointmentPage.getIsCanReception());
            CommonUtil.checkResult("是否可取消", true, newAppointmentPage.getIsCanCancel());
            CommonUtil.checkResult("是否可调整时间", true, newAppointmentPage.getIsCanAdjust());
            //点接待
            user.loginApp(ALL_AUTHORITY);
            int appReceptionPageNum = util.getReceptionPageNum();
            user.loginPc(ALL_AUTHORITY);
            IScene receptionPageScene = ReceptionPageScene.builder().build();
            int pcReceptionPageNum = receptionPageScene.invoke(visitor).getInteger("total");
            AppAppointmentReceptionScene.builder().id(appointmentId).build().invoke(visitor);
            int newAppReceptionPageNum = util.getReceptionPageNum();
            CommonUtil.checkResult("app接待页列表数", appReceptionPageNum + 1, newAppReceptionPageNum);
            user.loginPc(ALL_AUTHORITY);
            int newPcReceptionPageNum = receptionPageScene.invoke(visitor).getInteger("total");
            CommonUtil.checkResult("pc接待列表数", pcReceptionPageNum + 1, newPcReceptionPageNum);
            ReceptionPage receptionPage = util.getFirstReceptionPage();
            CommonUtil.checkResult("接待状态", ReceptionStatusEnum.IN_RECEPTION.getStatusName(), receptionPage.getReceptionStatusName());
            CommonUtil.checkResult("接待类型", "预约", receptionPage.getReceptionTypeName());
            CommonUtil.checkResult("接待人", ALL_AUTHORITY.getName(), receptionPage.getReceptionSaleName());
            CommonUtil.checkResult("注册状态", "已注册", receptionPage.getRegistrationStatusName());
            //变更接待
            Long receptionId = receptionPage.getId();
            user.loginApp(ALL_AUTHORITY);
            AppReceptionReceptorList receptorList = util.getReceptorList();
            String uid = receptorList.getUid();
            AppReceptionReceptorChangeScene.builder().id(receptionId).receptorId(uid).shopId(shopId).build().invoke(visitor);
            user.loginPc(ALL_AUTHORITY);
            ReceptionPage newReceptionPage = util.getReceptionPageById(receptionId);
            CommonUtil.checkResult("变更接待后接待人员", receptorList.getName(), newReceptionPage.getReceptionSaleName());
            user.loginPc(ALL_AUTHORITY);
            ReceptorChangeScene.builder().id(receptionId).receptorId(util.getReceptorList(ALL_AUTHORITY).getUid()).shopId(shopId).build().invoke(visitor);
            //登录此人app完成接待
            user.loginApp(ALL_AUTHORITY);
            int finishReceptionNum = util.getReceptionPageNum();
            AppReceptionFinishReceptionScene.builder().id(receptionId).shopId(shopId).build().invoke(visitor);
            int newFinishReceptionNum = util.getReceptionPageNum();
            CommonUtil.checkResult("完成接待后，app接待列表数", finishReceptionNum - 1, newFinishReceptionNum);
            user.loginPc(ALL_AUTHORITY);
            ReceptionPage finishReceptionPage = util.getReceptionPageById(receptionId);
            CommonUtil.checkResult("完成接待后pc接待列表接待状态", RepairStatusEnum.TO_BE_EVALUATED.getStatusName(), finishReceptionPage.getReceptionStatusName());
            //小程序评价状态
            user.loginApplet(APPLET_USER_ONE);
            IScene appletAppointmentListScene = AppletAppointmentListScene.builder().build();
            JSONObject object = appletAppointmentListScene.invoke(visitor).getJSONArray("list").getJSONObject(0);
            String statusName = object.getString("status_name");
            CommonUtil.checkResult("完成接待后applet预约状态", RepairStatusEnum.TO_BE_EVALUATED.getStatusName(), statusName);
            //消息状态
            IScene appletMessageListScene = AppletMessageListScene.builder().build();
            JSONObject messageObject = appletMessageListScene.invoke(visitor).getJSONArray("list").getJSONObject(0);
            String messageTypeName = messageObject.getString("message_type_name");
            Integer messageType = messageObject.getInteger("message_type");
            Boolean isRead = messageObject.getBoolean("is_read");
            String title = messageObject.getString("title");
            Long messageId = messageObject.getLong("id");
            CommonUtil.checkResult("完成接待后applet消息类型", appletMessageTypeEnum.getServiceType().getId(), messageType);
            CommonUtil.checkResult("完成接待后applet消息类型", appletMessageTypeEnum.getTypeName(), messageTypeName);
            CommonUtil.checkResult("完成接待后applet消息是否已读", false, isRead);
            CommonUtil.checkResult("完成接待后applet消息标题", appletMessageTypeEnum.getServiceType().getMsgTitle(), title);
            IScene appletMessageDetailScene = AppletMessageDetailScene.builder().id(messageId).build();
            JSONObject messageDetailObject = appletMessageDetailScene.invoke(visitor);
            Boolean isCanEvaluate = messageDetailObject.getBoolean("is_can_evaluate");
            String content = messageDetailObject.getString("content");
            CommonUtil.checkResult("完成接待后applet消息中可评价状态", true, isCanEvaluate);
            Preconditions.checkArgument(content.contains("已经完成服务了，请您对我们的服务进行评价！"), "消息内容应不包含已经完成服务了，请您对我们的服务进行评价！");
            //评价
            user.loginPc(ALL_AUTHORITY);
            IScene evaluatePageScene = EvaluatePageScene.builder().evaluateType(appletMessageTypeEnum.getServiceType().getId()).build();
            Long total = evaluatePageScene.invoke(visitor).getLong("total");
            user.loginApplet(APPLET_USER_ONE);
            AppletEvaluateSubmitScene.builder().id(receptionId).shopId(shopId).type(1).score(4).isAnonymous(true).describe(EnumDesc.DESC_BETWEEN_40_50.getDesc()).suggestion(EnumDesc.DESC_BETWEEN_40_50.getDesc()).build().invoke(visitor);
            //评价完成后
            JSONObject newMessageDetailObject = appletMessageDetailScene.invoke(visitor);
            Boolean newIsCanEvaluate = newMessageDetailObject.getBoolean("is_can_evaluate");
            CommonUtil.checkResult("完成评价后applet消息中可评价状态", false, newIsCanEvaluate);
            String newStatusName = appletAppointmentListScene.invoke(visitor).getJSONArray("list").getJSONObject(0).getString("status_name");
            CommonUtil.checkResult("完成评价后applet预约状态", RepairStatusEnum.EVALUATED.getStatusName(), newStatusName);
            user.loginPc(ALL_AUTHORITY);
            JSONObject evaluatePageObject = evaluatePageScene.invoke(visitor);
            Long newTotal = evaluatePageObject.getLong("total");
            EvaluatePageBean evaluatePageBean = util.toFirstJavaObject(evaluatePageScene, EvaluatePageBean.class);
            CommonUtil.checkResult("评价列表数", total + 1, newTotal);
            CommonUtil.checkResult("评价描述", EnumDesc.DESC_BETWEEN_40_50.getDesc(), evaluatePageBean.getDescribe());
            CommonUtil.checkResult("评价内容", EnumDesc.DESC_BETWEEN_40_50.getDesc(), evaluatePageBean.getSuggestion());
            CommonUtil.checkResult("评价星星", 4, evaluatePageBean.getScore());
            //跟进
            user.loginApp(ALL_AUTHORITY);
            Integer followId = util.getFollowUpPageList().get(0).getId();
            AppFollowUpCompleteScene.builder().id(followId).shopId(shopId).remark(EnumDesc.DESC_BETWEEN_40_50.getDesc()).build().invoke(visitor);
            user.loginPc(ALL_AUTHORITY);
            EvaluatePageBean followEvaluatePage = util.toFirstJavaObject(evaluatePageScene, EvaluatePageBean.class);
            CommonUtil.checkResult("跟进后跟进备注", EnumDesc.DESC_BETWEEN_40_50.getDesc(), followEvaluatePage.getFollowUpRemark());
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("预约维修->确认预约->点接待->变更接待->完成接待->评价->跟进");
        }
    }

    //ok
    @Test(description = "预约试驾->确认预约->点接待->变更接待->完成接待->评价->跟进")
    public void appointmentManager_testDriver() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String appointmentDate = DateTimeUtil.addDayFormat(new Date(), 0);
            AppointmentTypeEnum appointmentTypeEnum = AppointmentTypeEnum.TEST_DRIVE;
            AppletMessageTypeEnum appletMessageTypeEnum = AppletMessageTypeEnum.RECEPTION_EVALUATE_TIP;
            Long shopId = util.getShopId();
            IScene appointmentPageScene = AppointmentPageScene.builder().type(appointmentTypeEnum.name()).build();
            int appointmentPageTotal = appointmentPageScene.invoke(visitor).getInteger("total");
            user.loginApp(ALL_AUTHORITY);
            int appAppointmentNum = util.getAppointmentPageNum();
            //预约试驾
            user.loginApplet(APPLET_USER_ONE);
            int appointmentNum = util.getAppletAppointmentNum();
            Long appointmentId = util.appointment(appointmentTypeEnum, appointmentDate);
            int newAppointmentNum = util.getAppletAppointmentNum();
            CommonUtil.checkResult("applet我的预约列表数", appointmentNum + 1, newAppointmentNum);
            user.loginApp(ALL_AUTHORITY);
            int newAppAppointmentNum = util.getAppointmentPageNum();
            CommonUtil.checkResult("app我的预约列表数", appAppointmentNum + 1, newAppAppointmentNum);
            IScene appAppointmentPageScene = AppAppointmentPageScene.builder().build();
            AppAppointmentPage appAppointmentPage = util.toFirstJavaObject(appAppointmentPageScene, AppAppointmentPage.class);
            CommonUtil.checkResult("app预约类型", appointmentTypeEnum.getValue(), appAppointmentPage.getTypeName());
            CommonUtil.checkResult("app预约类型", appointmentTypeEnum.name(), appAppointmentPage.getType());
            CommonUtil.checkResult("app是否可确认", true, appAppointmentPage.getIsCanConfirm());
            CommonUtil.checkResult("app是否可接待", true, appAppointmentPage.getIsCanReception());
            CommonUtil.checkResult("app是否可取消", true, appAppointmentPage.getIsCanCancel());
            CommonUtil.checkResult("app是否可调整时间", true, appAppointmentPage.getIsCanAdjust());
            user.loginPc(ALL_AUTHORITY);
            int newAppointmentPageTotal = appointmentPageScene.invoke(visitor).getInteger("total");
            CommonUtil.checkResult("pc预约记录列表数", appointmentPageTotal + 1, newAppointmentPageTotal);
            AppointmentRecordAppointmentPageBean appointmentPage = util.getAppointmentPageById(appointmentId, appointmentTypeEnum.name());
            CommonUtil.checkResult("预约类型", appointmentTypeEnum.getValue(), appointmentPage.getTypeName());
            CommonUtil.checkResult("预约状态", AppointmentConfirmStatusEnum.WAITING.getStatusName(), appointmentPage.getAppointmentStatusName());
            CommonUtil.checkResult("是否可确认", true, appointmentPage.getIsCanConfirm());
            CommonUtil.checkResult("是否可接待", true, appointmentPage.getIsCanReception());
            CommonUtil.checkResult("是否可取消", true, appointmentPage.getIsCanCancel());
            CommonUtil.checkResult("是否可调整时间", true, appointmentPage.getIsCanAdjust());
            //确认预约
            user.loginApp(ALL_AUTHORITY);
            int makeSureAppointmentNum = util.getAppointmentPageNum();
            AppAppointmentHandleScene.builder().id(appointmentId).shopId(shopId).type(10).build().invoke(visitor);
            Integer newMakeSureAppAppointmentNum = util.getAppointmentPageNum();
            CommonUtil.checkResult("app预约记录数", makeSureAppointmentNum, newMakeSureAppAppointmentNum);
            user.loginPc(ALL_AUTHORITY);
            AppointmentRecordAppointmentPageBean newAppointmentPage = util.getAppointmentPageById(appointmentId, appointmentTypeEnum.name());
            CommonUtil.checkResult("预约状态", AppointmentConfirmStatusEnum.AGREE.getStatusName(), newAppointmentPage.getAppointmentStatusName());
            CommonUtil.checkResult("是否可确认", false, newAppointmentPage.getIsCanConfirm());
            CommonUtil.checkResult("是否可接待", true, newAppointmentPage.getIsCanReception());
            CommonUtil.checkResult("是否可取消", true, newAppointmentPage.getIsCanCancel());
            CommonUtil.checkResult("是否可调整时间", true, newAppointmentPage.getIsCanAdjust());
            //点接待
            user.loginApp(ALL_AUTHORITY);
            int appReceptionPageNum = util.getPreSalesReceptionPageNum();
            user.loginPc(ALL_AUTHORITY);
            IScene receptionPageScene = PreSalesReceptionPageScene.builder().build();
            int pcReceptionPageNum = receptionPageScene.invoke(visitor).getInteger("total");
            AppAppointmentReceptionScene.builder().id(appointmentId).build().invoke(visitor);
            int newAppReceptionPageNum = util.getPreSalesReceptionPageNum();
            CommonUtil.checkResult("app接待页列表数", appReceptionPageNum + 1, newAppReceptionPageNum);
            user.loginPc(ALL_AUTHORITY);
            int newPcReceptionPageNum = receptionPageScene.invoke(visitor).getInteger("total");
            CommonUtil.checkResult("pc接待列表数", pcReceptionPageNum + 1, newPcReceptionPageNum);
            PreSalesReceptionPageBean receptionPage = util.getFirstPreSalesReceptionPage();
            CommonUtil.checkResult("接待类型", "预约", receptionPage.getReceptionTypeName());
            CommonUtil.checkResult("接待人", ALL_AUTHORITY.getName(), receptionPage.getReceptionSaleName());
            //变更接待
            Long receptionId = receptionPage.getId();
            user.loginApp(ALL_AUTHORITY);
            AppReceptionReceptorList receptorList = util.getPreSalesReceptorList();
            String uid = receptorList.getUid();
            AppReceptorChangeScene.builder().id(receptionId).receptorId(uid).shopId(shopId).build().invoke(visitor);
            user.loginPc(ALL_AUTHORITY);
            PreSalesReceptionPageBean newReceptionPage = util.getPreSalesReceptionPageById(receptionId);
            CommonUtil.checkResult("变更接待后接待人员", receptorList.getName(), newReceptionPage.getReceptionSaleName());
            user.loginPc(ALL_AUTHORITY);
            AppReceptorChangeScene.builder().id(receptionId).receptorId(util.getReceptorList(ALL_AUTHORITY).getUid()).shopId(shopId).build().invoke(visitor);
            //登录此人app完成接待
            user.loginApp(ALL_AUTHORITY);
            int finishReceptionNum = util.getPreSalesReceptionPageNum();
            util.editCustomerInfo();
            AppFinishReceptionScene.builder().id(receptionId).shopId(shopId).build().invoke(visitor);
            int newFinishReceptionNum = util.getPreSalesReceptionPageNum();
            CommonUtil.checkResult("完成接待后，app接待列表数", finishReceptionNum - 1, newFinishReceptionNum);
            user.loginPc(ALL_AUTHORITY);
            //小程序评价状态
            user.loginApplet(APPLET_USER_ONE);
            IScene appletAppointmentListScene = AppletAppointmentListScene.builder().build();
            JSONObject object = appletAppointmentListScene.invoke(visitor).getJSONArray("list").getJSONObject(0);
            String statusName = object.getString("status_name");
            CommonUtil.checkResult("完成接待后applet预约状态", MaintainStatusEnum.TO_BE_EVALUATED.getStatusName(), statusName);
            //消息状态
            IScene appletMessageListScene = AppletMessageListScene.builder().build();
            JSONObject messageObject = appletMessageListScene.invoke(visitor).getJSONArray("list").getJSONObject(0);
            String messageTypeName = messageObject.getString("message_type_name");
            Integer messageType = messageObject.getInteger("message_type");
            Boolean isRead = messageObject.getBoolean("is_read");
            String title = messageObject.getString("title");
            Long messageId = messageObject.getLong("id");
            CommonUtil.checkResult("完成接待后applet消息类型", 3, messageType);
            CommonUtil.checkResult("完成接待后applet消息类型", appletMessageTypeEnum.getTypeName(), messageTypeName);
            CommonUtil.checkResult("完成接待后applet消息是否已读", false, isRead);
            CommonUtil.checkResult("完成接待后applet消息标题", appletMessageTypeEnum.getServiceType().getMsgTitle(), title);
            IScene appletMessageDetailScene = AppletMessageDetailScene.builder().id(messageId).build();
            JSONObject messageDetailObject = appletMessageDetailScene.invoke(visitor);
            Boolean isCanEvaluate = messageDetailObject.getBoolean("is_can_evaluate");
            String content = messageDetailObject.getString("content");
            CommonUtil.checkResult("完成接待后applet消息中可评价状态", true, isCanEvaluate);
            Preconditions.checkArgument(content.contains("为您服务是我们的荣幸，请您对本次销售接待服务进行评价！"), "消息内容应不包含已经完成服务了，请您对我们的服务进行评价！");
            //评价
            user.loginPc(ALL_AUTHORITY);
            IScene evaluatePageScene = EvaluatePageScene.builder().evaluateType(appletMessageTypeEnum.getServiceType().getId()).build();
            Long total = evaluatePageScene.invoke(visitor).getLong("total");
            user.loginApplet(APPLET_USER_ONE);
            AppletEvaluateSubmitScene.builder().id(receptionId).shopId(shopId).type(4).score(4).isAnonymous(true).describe(EnumDesc.DESC_BETWEEN_40_50.getDesc()).suggestion(EnumDesc.DESC_BETWEEN_40_50.getDesc()).build().invoke(visitor);
            //评价完成后
            JSONObject newMessageDetailObject = appletMessageDetailScene.invoke(visitor);
            Boolean newIsCanEvaluate = newMessageDetailObject.getBoolean("is_can_evaluate");
            CommonUtil.checkResult("完成评价后applet消息中可评价状态", false, newIsCanEvaluate);
            String newStatusName = appletAppointmentListScene.invoke(visitor).getJSONArray("list").getJSONObject(0).getString("status_name");
            CommonUtil.checkResult("完成评价后applet预约状态", MaintainStatusEnum.EVALUATED.getStatusName(), newStatusName);
            user.loginPc(ALL_AUTHORITY);
            JSONObject evaluatePageObject = evaluatePageScene.invoke(visitor);
            Long newTotal = evaluatePageObject.getLong("total");
            EvaluatePageBean evaluatePageBean = util.toFirstJavaObject(evaluatePageScene, EvaluatePageBean.class);
            CommonUtil.checkResult("评价列表数", total + 1, newTotal);
            CommonUtil.checkResult("评价描述", EnumDesc.DESC_BETWEEN_40_50.getDesc(), evaluatePageBean.getDescribe());
            CommonUtil.checkResult("评价内容", EnumDesc.DESC_BETWEEN_40_50.getDesc(), evaluatePageBean.getSuggestion());
            CommonUtil.checkResult("评价星星", 4, evaluatePageBean.getScore());
            //跟进
            user.loginApp(ALL_AUTHORITY);
            Integer followId = util.getFollowUpPageList().get(0).getId();
            AppFollowUpCompleteScene.builder().id(followId).shopId(shopId).remark(EnumDesc.DESC_BETWEEN_40_50.getDesc()).build().invoke(visitor);
            user.loginPc(ALL_AUTHORITY);
            EvaluatePageBean followEvaluatePage = util.toFirstJavaObject(evaluatePageScene, EvaluatePageBean.class);
            CommonUtil.checkResult("跟进后跟进备注", EnumDesc.DESC_BETWEEN_40_50.getDesc(), followEvaluatePage.getFollowUpRemark());
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("预约试驾->确认预约->点接待->变更接待->完成接待->评价->跟进");
        }
    }

    //ok
    @Test(description = "小程序--积分总数=积分明细所有项加和")
    public void integralMall_data_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            user.loginApplet(APPLET_USER_ONE);
            IScene homePageScene = AppletHomePageScene.builder().build();
            Integer integral = homePageScene.invoke(visitor).getInteger("integral");
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

    //ok
    @Test(description = "小程序--积分商城商品数量=pc进行中的积分兑换数量")
    public void integralMall_data_2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            user.loginApplet(APPLET_USER_ONE);
            int appletCommodityListNum = util.getAppletCommodityListNum();
            user.loginPc(ALL_AUTHORITY);
            int total = ExchangePageScene.builder().status(IntegralExchangeStatusEnum.WORKING.name()).build().invoke(visitor).getInteger("total");
            CommonUtil.checkResultPlus("小程序积分商城商品数量", appletCommodityListNum, "pc进行中的积分兑换数量", total);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("小程序--积分商城商品数量=pc进行中的积分兑换数量");
        }
    }

    //ok
    @Test(description = "小程序--积分兑换--兑换库存不足的实体商品，提示：商品库存不足")
    public void integralMall_system_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene exchangePageScene = ExchangePageScene.builder().status(IntegralExchangeStatusEnum.WORKING.name()).exchangeType(CommodityTypeEnum.REAL.name()).build();
            ExchangePage a = util.toJavaObjectList(exchangePageScene, ExchangePage.class).stream().filter(e -> e.getExchangedAndSurplus().split("/")[1].equals("0")).findFirst().orElse(null);
            ExchangePage exchangePage = a == null ? util.createExchangeRealGoods(0) : a;
            //修改为可兑换多次
            util.modifyExchangeGoodsLimit(exchangePage.getId(), exchangePage.getExchangeType(), false);
            user.loginApplet(APPLET_USER_ONE);
            int specificationId = AppletCommodityDetailScene.builder().id(exchangePage.getId()).build().invoke(visitor).getJSONArray("specification_compose_list").getJSONObject(0).getInteger("id");
            AppletShippingAddress appletShippingAddress = AppletShippingAddressListScene.builder().build().invoke(visitor).getJSONArray("list").stream().map(e -> (JSONObject) e).map(e -> JSONObject.toJavaObject(e, AppletShippingAddress.class)).collect(Collectors.toList()).get(0);
            //兑换积分
            String message = AppletSubmitOrderScene.builder().commodityId(exchangePage.getId()).specificationId(specificationId).smsNotify(false).commodityNum("1").districtCode(appletShippingAddress.getDistrictCode()).address(appletShippingAddress.getAddress()).receiver(appletShippingAddress.getName()).receivePhone(appletShippingAddress.getPhone()).build().invoke(visitor, false).getString("message");
            String err = "商品库存不足";
            CommonUtil.checkResult("兑换库存不足的商品", err, message);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("小程序--积分兑换--兑换库存不足的实体商品，提示：商品库存不足");
        }
    }

    //ok
    @Test(description = "小程序--积分兑换--兑换无库存的虚拟商品，提示：商品库存不足")
    public void integralMall_system_3() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //找一个库存为大于0并且包含卡券剩余库存等于0的积分商品
            IScene exchangePageScene = ExchangePageScene.builder().status(IntegralExchangeStatusEnum.WORKING.name()).exchangeType(CommodityTypeEnum.FICTITIOUS.name()).build();
            ExchangePage a = util.toJavaObjectList(exchangePageScene, ExchangePage.class).stream().filter(e -> Integer.parseInt(e.getExchangedAndSurplus().split("/")[1]) == 0 && util.getExchangeGoodsContainVoucher(e.getId()).getAllowUseInventory() > 0).findFirst().orElse(null);
            ExchangePage exchangePage;
            if (a == null) {
                Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
                exchangePage = util.createExchangeFictitiousGoods(voucherId);
                EditExchangeStockScene.builder().id(exchangePage.getId()).changeStockType(ChangeStockTypeEnum.MINUS.name()).num("1").goodsName(exchangePage.getGoodsName()).type(exchangePage.getExchangeType()).build().invoke(visitor);
            } else {
                exchangePage = a;
            }
            user.loginApplet(APPLET_USER_ONE);
            Long specificationId = AppletCommodityDetailScene.builder().id(exchangePage.getId()).build().invoke(visitor).getLong("id");
            //兑换积分
            String message = AppletIntegralExchangeScene.builder().id(specificationId).build().invoke(visitor, false).getString("message");
            String err = "商品库存不足";
            CommonUtil.checkResult("兑换无库存的虚拟商品", err, message);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("小程序--积分兑换--兑换无库存的虚拟商品，提示：商品库存不足");
        }
    }

    //ok
    @Test(description = "小程序--积分兑换--兑换无库存的虚拟商品")
    public void integralMall_system_4() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Long voucherId;
            ExchangePage exchangePage;
            IScene exchangePageScene = ExchangePageScene.builder().status(IntegralExchangeStatusEnum.WORKING.name()).exchangeType(CommodityTypeEnum.FICTITIOUS.name()).build();
            //找一个库存为大于0并且包含卡券剩余库存大于0的积分商品
            ExchangePage a = util.toJavaObjectList(exchangePageScene, ExchangePage.class).stream().filter(e -> Integer.parseInt(e.getExchangedAndSurplus().split("/")[1]) > 0 && util.getExchangeGoodsContainVoucher(e.getId()).getAllowUseInventory() > 0 && util.getExchangeGoodsContainVoucher(e.getId()).getVoucherStatus().equals(VoucherStatusEnum.WORKING.name())).findFirst().orElse(null);
            if (a == null) {
                //如果没有找一个库存为0并且包含卡券剩余库存大于0的积分商品
                ExchangePage b = util.toJavaObjectList(exchangePageScene, ExchangePage.class).stream().filter(e -> Integer.parseInt(e.getExchangedAndSurplus().split("/")[1]) == 0 && util.getExchangeGoodsContainVoucher(e.getId()).getAllowUseInventory() > 0 && util.getExchangeGoodsContainVoucher(e.getId()).getVoucherStatus().equals(VoucherStatusEnum.WORKING.name())).findFirst().orElse(null);
                if (b == null) {
                    //如果没有创建一个库存为1并且包含卡券剩余库存>0的积分商品
                    voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
                    exchangePage = util.createExchangeFictitiousGoods(voucherId);
                } else {
                    //如果有给此积分商品增加一个库存
                    exchangePage = b;
                    voucherId = util.getExchangeGoodsContainVoucher(exchangePage.getId()).getVoucherId();
                    EditExchangeStockScene.builder().changeStockType(ChangeStockTypeEnum.ADD.name()).num("1").id(exchangePage.getId()).goodsName(exchangePage.getGoodsName()).type(CommodityTypeEnum.FICTITIOUS.name()).build().invoke(visitor);
                }
            } else {
                //如果有直接使用
                exchangePage = a;
                voucherId = util.getExchangeGoodsContainVoucher(exchangePage.getId()).getVoucherId();
            }
            //编辑为不限兑换次数
            util.modifyExchangeGoodsLimit(exchangePage.getId(), exchangePage.getExchangeType(), false);
            VoucherPage voucherPage = util.getVoucherPage(voucherId);
            user.loginApplet(APPLET_USER_ONE);
            int appletVoucherNum = util.getAppletVoucherNum();
            //兑换积分
            Long specificationId = AppletCommodityDetailScene.builder().id(exchangePage.getId()).build().invoke(visitor).getLong("id");
            AppletIntegralExchangeScene.builder().id(specificationId).build().invoke(visitor);
            int newAppletVoucherNum = util.getAppletVoucherNum();
            CommonUtil.checkResult(voucherPage.getVoucherName() + "兑换后我的卡券列表数量", appletVoucherNum + 1, newAppletVoucherNum);
            user.loginPc(ALL_AUTHORITY);
            VoucherPage newVoucherPage = util.getVoucherPage(voucherId);
            CommonUtil.checkResult(voucherPage.getVoucherName() + "剩余库存", voucherPage.getSurplusInventory() - 1, newVoucherPage.getSurplusInventory());
            CommonUtil.checkResult(voucherPage.getVoucherName() + "已领取", voucherPage.getCumulativeDelivery() + 1, newVoucherPage.getCumulativeDelivery());
            VoucherSendRecord voucherSendRecord = util.getVoucherSendRecordList(voucherId).get(0);
            CommonUtil.checkResult(voucherPage.getVoucherName() + "领取记录领取渠道", VoucherSourceEnum.INTEGRAL_PURCHASE.getName(), voucherSendRecord.getSendChannelName());
            CommonUtil.checkResult(voucherPage.getVoucherName() + "领取人手机号", APPLET_USER_ONE.getPhone(), voucherSendRecord.getCustomerPhone());
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("小程序--积分兑换--兑换无库存的虚拟商品");
        }
    }

    //ok
    @Test(description = "小程序--积分商城倒序排序，积分依次减少")
    public void integralMall_system_5() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            user.loginApplet(APPLET_USER_ONE);
            List<AppletCommodity> appletCommodityList = util.getAppletCommodityList(SortTypeEnum.DOWN.name(), false);
            for (int i = 0; i < appletCommodityList.size(); i++) {
                if (i != appletCommodityList.size() - 1) {
                    Integer firstPrice = appletCommodityList.get(i).getPresentIntegralPrice();
                    Integer secondPrice = appletCommodityList.get(i + 1).getPresentIntegralPrice();
                    CommonUtil.valueView("第" + i + "个" + firstPrice, "第" + (i + 1) + "个" + secondPrice);
                    Preconditions.checkArgument(firstPrice >= secondPrice, "第" + i + "个商品需要积分：" + firstPrice + " 第" + (i + 1) + "个商品需要积分：" + secondPrice);
                }
            }
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("小程序--积分商城倒序排序，积分依次减少");
        }
    }

    //ok
    @Test(description = "小程序--积分商城，我可兑换的商品所需积分均小于我现有积分")
    public void integralMall_system_6() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            user.loginApplet(APPLET_USER_ONE);
            List<AppletCommodity> appletCommodityList = util.getAppletCommodityList(SortTypeEnum.DOWN.name(), true);
            Integer score = AppletUserInfoDetailScene.builder().build().invoke(visitor).getInteger("score");
            appletCommodityList.forEach(e -> {
                CommonUtil.valueView(score, e.getPresentIntegralPrice());
                Preconditions.checkArgument(e.getPresentIntegralPrice() <= score, "我的总积分：" + score + e.getCommodityName() + "所需积分" + e.getPresentIntegralPrice());
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("小程序--积分商城，我可兑换的商品所需积分均小于我现有积分");
        }
    }

    //ok
    @Test(description = "小程序--签到--积分增加&积分明细记录增加类型")
    public void integralCenter_system_5() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene integralExchangeRulesScene = IntegralExchangeRulesScene.builder().build();
            int allSend = util.toJavaObjectList(integralExchangeRulesScene, JSONObject.class).stream().filter(e -> e.getString("rule_name").equals(AppletCodeBusinessTypeEnum.SIGN_IN.getTypeName())).map(e -> e.getInteger("all_send")).findFirst().orElse(0);
            user.loginApplet(APPLET_USER_ONE);
            JSONObject response = AppletSignInDetailScene.builder().build().invoke(visitor);
            int signInScore = response.getInteger("sign_in_score");
            int signInScoreCount = response.getInteger("sign_in_score_count");
            int signInTime = response.getInteger("sign_in_time");
            boolean isSignIn = response.getBoolean("is_sign_in");
            CommonUtil.checkResult("小程序是否已签到", false, isSignIn);
            int integralRecordNum = util.getAppletIntegralRecordNum();

            String gainTime = DateTimeUtil.getFormat(new Date(), "yyyy-MM-dd HH:mm");
            String monthType = DateTimeUtil.getFormat(new Date(), "yyyy-MM");
            //签到
            AppletSignInScene.builder().build().invoke(visitor);
            //签到完成后数据校验
            JSONObject newResponse = AppletSignInDetailScene.builder().build().invoke(visitor);
            CommonUtil.checkResult("小程序签到完成累计签到积分", signInScoreCount + signInScore, newResponse.getInteger("sign_in_score_count"));
            CommonUtil.checkResult("小程序签到完成累计签到次数", signInTime + 1, newResponse.getInteger("sign_in_time"));
            int newIntegralRecordNum = util.getAppletIntegralRecordNum();
            CommonUtil.checkResult("小程序签到完成积分明细列表数", integralRecordNum + 1, newIntegralRecordNum);
            AppletIntegralRecord integralRecord = util.getAppletIntegralRecordList().get(0);
            Preconditions.checkArgument(integralRecord.getGainTime().contains(gainTime), "小程序签到完成积分明细获取时间：" + integralRecord.getGainTime());
            CommonUtil.checkResult("小程序签到完成积分明细获取月份", monthType, integralRecord.getMonthType());
            CommonUtil.checkResult("小程序签到完成积分明细内容", "签到获得" + signInScore + "积分", integralRecord.getName());
            CommonUtil.checkResult("小程序签到完成积分明细类型", ChangeStockTypeEnum.ADD.name(), integralRecord.getChangeType());
            CommonUtil.checkResult("小程序签到完成积分明细积分数", signInScore, Integer.parseInt(integralRecord.getIntegral()));
            user.loginPc(ALL_AUTHORITY);
            IScene exchangeDetailedScene = ExchangeDetailedScene.builder().build();
            ExchangeDetailed exchangeDetailed = util.toJavaObjectList(exchangeDetailedScene, ExchangeDetailed.class).get(0);
            Preconditions.checkArgument(exchangeDetailed.getOperateTime().contains(gainTime), "pc积分明细获取时间：" + exchangeDetailed.getOperateTime());
            CommonUtil.checkResult("pc积分明细内容", "签到获得" + signInScore + "积分", exchangeDetailed.getChangeReason());
            CommonUtil.checkResult("pc积分明细类型", ChangeStockTypeEnum.ADD.name(), exchangeDetailed.getExchangeType());
            CommonUtil.checkResult("pc积分明细类型", ChangeStockTypeEnum.ADD.getDescription(), exchangeDetailed.getExchangeTypeName());
            CommonUtil.checkResult("pc积分明细积分数", signInScore, exchangeDetailed.getStockDetail());
            int newAllSend = util.toJavaObjectList(integralExchangeRulesScene, JSONObject.class).stream().filter(e -> e.getString("rule_name").equals(AppletCodeBusinessTypeEnum.SIGN_IN.getTypeName())).map(e -> e.getInteger("all_send")).findFirst().orElse(0);
            CommonUtil.checkResult("pc积分规则中签到已发放积分", allSend + signInScore, newAllSend);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("小程序--签到--积分增加&积分明细记录增加类型");
        }
    }
}
