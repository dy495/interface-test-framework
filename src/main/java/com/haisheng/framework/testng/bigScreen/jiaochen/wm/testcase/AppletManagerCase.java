package com.haisheng.framework.testng.bigScreen.jiaochen.wm.testcase;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.*;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.customer.EnumAppletToken;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.app.AppReceptionReceptorList;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.applet.AppletIntegralRecord;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.applet.AppletShippingAddress;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.*;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.EnumAccount;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.EnumDesc;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.Integral.ChangeStockTypeEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.Integral.CommodityTypeEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.Integral.IntegralExchangeStatusEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.appointment.AppointmentConfirmStatusEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.appointment.AppointmentTypeEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing.AppletCodeBusinessTypeEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing.VoucherSourceEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing.VoucherStatusEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.reception.after.ReceptionStatusEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.generate.voucher.VoucherGenerator;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.granted.*;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.mapp.AppFollowUpCompleteScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.mapp.task.AppAppointmentHandleScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.mapp.task.AppAppointmentReceptionScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.mapp.task.AppReceptionFinishReceptionScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.mapp.task.AppReceptionReceptorChangePageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.appointmentmanage.AppointmentPageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.integralcenter.*;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.receptionmanage.ReceptionPageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.receptionmanage.ReceptorChangeScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.vouchermanage.AddVoucherScene;
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
import java.util.stream.Collectors;

/**
 * 小程序用例
 *
 * @author wangmin
 * @date 2021/1/29 11:17
 */
public class AppletManagerCase extends TestCaseCommon implements TestCaseStd {
    private static final EnumTestProduce PRODUCE = EnumTestProduce.JIAOCHEN_DAILY;
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
        commonConfig.checklistAppId = EnumChecklistAppId.DB_APP_ID_SCREEN_SERVICE.getId();
        commonConfig.checklistConfId = EnumChecklistConfId.DB_SERVICE_ID_CRM_DAILY_SERVICE.getId();
        commonConfig.checklistQaOwner = EnumChecklistUser.WM.getName();
        //替换jenkins-job的相关信息
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.JIAOCHEN_DAILY_TEST.getJobName());
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, PRODUCE.getDesc() + commonConfig.checklistQaOwner);
        //替换钉钉推送
        commonConfig.dingHook = EnumDingTalkWebHook.CAR_OPEN_MANAGEMENT_PLATFORM_GRP.getWebHook();
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
    @Test(description = "预约保养->确认预约->点接待->变更接待->完成接待->评价->跟进", enabled = false)
    public void appointmentManager_maintain() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            int i = 0;
            Integer shopId = util.getShopId();
            IScene appointmentPageScene = AppointmentPageScene.builder().build();
            int appointmentPageTotal = visitor.invokeApi(appointmentPageScene).getInteger("total");
            int appointmentNumber = util.appointmentNumber(DateTimeUtil.addDay(new Date(), i));
            user.loginApp(ALL_AUTHORITY);
            int appAppointmentNum = util.getAppointmentPageNum();
            //预约保养
            user.loginApplet(APPLET_USER_ONE);
            int appointmentNum = util.getAppletAppointmentNum();
            int appointmentId = util.appointment(AppointmentTypeEnum.MAINTAIN, DateTimeUtil.addDayFormat(new Date(), i));
            int newAppointmentNum = util.getAppletAppointmentNum();
            CommonUtil.checkResult("applet我的预约列表数", appointmentNum + 1, newAppointmentNum);
            user.loginApp(ALL_AUTHORITY);
            int newAppAppointmentNum = util.getAppointmentPageNum();
            CommonUtil.checkResult("app我的预约列表数", appAppointmentNum + 1, newAppAppointmentNum);
            user.loginPc(ALL_AUTHORITY);
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
            user.loginApp(ALL_AUTHORITY);
            int makeSureAppointmentNum = util.getAppointmentPageNum();
            IScene appointmentHandleScene = AppAppointmentHandleScene.builder().id(appointmentId).shopId(shopId).type(10).build();
            visitor.invokeApi(appointmentHandleScene);
            Integer newMakeSureAppAppointmentNum = util.getAppointmentPageNum();
            CommonUtil.checkResult("app预约记录数", makeSureAppointmentNum, newMakeSureAppAppointmentNum);
            user.loginPc(ALL_AUTHORITY);
            AppointmentPage newAppointmentPage = util.getAppointmentPageById(appointmentId);
            CommonUtil.checkResult("预约状态", AppointmentConfirmStatusEnum.AGREE.getStatusName(), newAppointmentPage.getAppointmentStatusName());
            CommonUtil.checkResult("是否可确认", false, newAppointmentPage.getIsCanConfirm());
            CommonUtil.checkResult("是否可接待", true, newAppointmentPage.getIsCanReception());
            CommonUtil.checkResult("是否可取消", false, newAppointmentPage.getIsCanCancel());
            CommonUtil.checkResult("是否可调整时间", true, newAppointmentPage.getIsCanAdjust());
            //点接待
            user.loginApp(ALL_AUTHORITY);
            int appReceptionPageNum = util.getReceptionPageNum();
            user.loginPc(ALL_AUTHORITY);
            int pcReceptionPageNum = visitor.invokeApi(ReceptionPageScene.builder().build()).getInteger("total");
            IScene appointmentReceptionScene = AppAppointmentReceptionScene.builder().id(appointmentId).build();
            visitor.invokeApi(appointmentReceptionScene);
            int newAppReceptionPageNum = util.getReceptionPageNum();
            CommonUtil.checkResult("app接待页列表数", appReceptionPageNum + 1, newAppReceptionPageNum);
            user.loginPc(ALL_AUTHORITY);
            int newPcReceptionPageNum = visitor.invokeApi(ReceptionPageScene.builder().build()).getInteger("total");
            CommonUtil.checkResult("pc接待列表数", pcReceptionPageNum + 1, newPcReceptionPageNum);
            ReceptionPage receptionPage = util.getFirstReceptionPage();
            CommonUtil.checkResult("接待状态", ReceptionStatusEnum.IN_RECEPTION.getStatusName(), receptionPage.getReceptionStatusName());
            CommonUtil.checkResult("接待类型", "预约", receptionPage.getReceptionTypeName());
            CommonUtil.checkResult("接待时间", DateTimeUtil.getFormat(new Date(), "yyyy-MM-dd HH:mm"), receptionPage.getReceptionTime());
            CommonUtil.checkResult("接待人", ALL_AUTHORITY.getName(), receptionPage.getReceptionSaleName());
            CommonUtil.checkResult("注册状态", "已注册", receptionPage.getRegistrationStatusName());
            //变更接待
            int receptionId = receptionPage.getId();
            user.loginApp(ALL_AUTHORITY);
            AppReceptionReceptorList receptorList = util.getReceptorList();
            String uid = receptorList.getUid();
            IScene receptionReceptorChangePageScene = AppReceptionReceptorChangePageScene.builder().id(receptionId).receptorId(uid).shopId(shopId).build();
            visitor.invokeApi(receptionReceptorChangePageScene);
            user.loginPc(ALL_AUTHORITY);
            ReceptionPage newReceptionPage = util.getReceptionPageById(receptionId);
            CommonUtil.checkResult("变更接待后接待人员", receptorList.getName(), newReceptionPage.getReceptionSaleName());
            user.loginPc(ALL_AUTHORITY);
            IScene receptorChangeScene = ReceptorChangeScene.builder().id(receptionId).receptorId(util.getReceptorList(ALL_AUTHORITY).getUid()).shopId(shopId).build();
            visitor.invokeApi(receptorChangeScene);
            //登录此人app完成接待
            user.loginApp(ALL_AUTHORITY);
            int finishReceptionNum = util.getReceptionPageNum();
            IScene receptionFinishReceptionScene = AppReceptionFinishReceptionScene.builder().id(receptionId).shopId(shopId).build();
            visitor.invokeApi(receptionFinishReceptionScene);
            int newFinishReceptionNum = util.getReceptionPageNum();
            CommonUtil.checkResult("完成接待后，app接待列表数", finishReceptionNum - 1, newFinishReceptionNum);
            user.loginPc(ALL_AUTHORITY);
            ReceptionPage finishReceptionPage = util.getReceptionPageById(receptionId);
            CommonUtil.checkResult("完成接待后pc接待列表接待状态", ReceptionStatusEnum.FINISH.getStatusName(), finishReceptionPage.getReceptionStatusName());
            CommonUtil.checkResult("完成接待后pc完成时间", DateTimeUtil.getFormat(new Date(), "yyyy-MM-dd HH:mm"), finishReceptionPage.getFinishTime());
            //评价
            user.loginPc(ALL_AUTHORITY);
            List<EvaluatePage> evaluatePageList = util.getEvaluatePageList();
            user.loginApplet(APPLET_USER_ONE);
            AppletEvaluateSubmitScene.builder().id(appointmentId).shopId(shopId).type(1).score(4).isAnonymous(true).describe(EnumDesc.MESSAGE_DESC.getDesc()).suggestion(EnumDesc.MESSAGE_DESC.getDesc()).build().execute(visitor, true);
            user.loginPc(ALL_AUTHORITY);
            List<EvaluatePage> newEvaluatePageList = util.getEvaluatePageList();
            CommonUtil.checkResult("评价列表数", evaluatePageList.size() + 1, newEvaluatePageList.size());
            CommonUtil.checkResult("评价描述", EnumDesc.MESSAGE_DESC.getDesc(), newEvaluatePageList.get(0).getDescribe());
            CommonUtil.checkResult("评价内容", EnumDesc.MESSAGE_DESC.getDesc(), newEvaluatePageList.get(0).getSuggestion());
            CommonUtil.checkResult("评价星星", 4, newEvaluatePageList.get(0).getScore());
            //跟进
            user.loginApp(ALL_AUTHORITY);
            Integer followId = util.getFollowUpPageList().get(0).getId();
            AppFollowUpCompleteScene.builder().id(followId).shopId(shopId).remark(EnumDesc.MESSAGE_DESC.getDesc()).build().execute(visitor, true);
            user.loginPc(ALL_AUTHORITY);
            List<EvaluatePage> followEvaluatePage = util.getEvaluatePageList();
            CommonUtil.checkResult("跟进后跟进备注", EnumDesc.MESSAGE_DESC.getDesc(), followEvaluatePage.get(0).getFollowUpRemark());
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("预约保养->确认预约->点接待->变更接待->完成接待->评价->跟进");
        }
    }

    //ok
    @Test(description = "预约维修->确认预约->点接待->变更接待->完成接待->评价->跟进", enabled = false)
    public void appointmentManager_repair() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            int i = 0;
            Integer shopId = util.getShopId();
            IScene appointmentPageScene = AppointmentPageScene.builder().build();
            int appointmentPageTotal = visitor.invokeApi(appointmentPageScene).getInteger("total");
            int appointmentNumber = util.appointmentNumber(DateTimeUtil.addDay(new Date(), i));
            user.loginApp(ALL_AUTHORITY);
            int appAppointmentNum = util.getAppointmentPageNum();
            //预约维修
            user.loginApplet(APPLET_USER_ONE);
            int appointmentNum = util.getAppletAppointmentNum();
            int appointmentId = util.appointment(AppointmentTypeEnum.REPAIR, DateTimeUtil.addDayFormat(new Date(), i));
            int newAppointmentNum = util.getAppletAppointmentNum();
            CommonUtil.checkResult("applet我的预约列表数", appointmentNum + 1, newAppointmentNum);
            user.loginApp(ALL_AUTHORITY);
            int newAppAppointmentNum = util.getAppointmentPageNum();
            CommonUtil.checkResult("app我的预约列表数", appAppointmentNum + 1, newAppAppointmentNum);
            user.loginPc(ALL_AUTHORITY);
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
            user.loginApp(ALL_AUTHORITY);
            int makeSureAppointmentNum = util.getAppointmentPageNum();
            IScene appointmentHandleScene = AppAppointmentHandleScene.builder().id(appointmentId).shopId(shopId).type(10).build();
            visitor.invokeApi(appointmentHandleScene);
            Integer newMakeSureAppAppointmentNum = util.getAppointmentPageNum();
            CommonUtil.checkResult("app预约记录数", makeSureAppointmentNum, newMakeSureAppAppointmentNum);
            user.loginPc(ALL_AUTHORITY);
            AppointmentPage newAppointmentPage = util.getAppointmentPageById(appointmentId);
            CommonUtil.checkResult("预约状态", AppointmentConfirmStatusEnum.AGREE.getStatusName(), newAppointmentPage.getAppointmentStatusName());
            CommonUtil.checkResult("是否可确认", false, newAppointmentPage.getIsCanConfirm());
            CommonUtil.checkResult("是否可接待", true, newAppointmentPage.getIsCanReception());
            CommonUtil.checkResult("是否可取消", false, newAppointmentPage.getIsCanCancel());
            CommonUtil.checkResult("是否可调整时间", true, newAppointmentPage.getIsCanAdjust());
            //点接待
            user.loginApp(ALL_AUTHORITY);
            int appReceptionPageNum = util.getReceptionPageNum();
            user.loginPc(ALL_AUTHORITY);
            int pcReceptionPageNum = visitor.invokeApi(ReceptionPageScene.builder().build()).getInteger("total");
            IScene appointmentReceptionScene = AppAppointmentReceptionScene.builder().id(appointmentId).build();
            visitor.invokeApi(appointmentReceptionScene);
            int newAppReceptionPageNum = util.getReceptionPageNum();
            CommonUtil.checkResult("app接待页列表数", appReceptionPageNum + 1, newAppReceptionPageNum);
            user.loginPc(ALL_AUTHORITY);
            int newPcReceptionPageNum = visitor.invokeApi(ReceptionPageScene.builder().build()).getInteger("total");
            CommonUtil.checkResult("pc接待列表数", pcReceptionPageNum + 1, newPcReceptionPageNum);
            ReceptionPage receptionPage = util.getFirstReceptionPage();
            CommonUtil.checkResult("接待状态", ReceptionStatusEnum.IN_RECEPTION.getStatusName(), receptionPage.getReceptionStatusName());
            CommonUtil.checkResult("接待类型", "预约", receptionPage.getReceptionTypeName());
            CommonUtil.checkResult("接待时间", DateTimeUtil.getFormat(new Date(), "yyyy-MM-dd HH:mm"), receptionPage.getReceptionTime());
            CommonUtil.checkResult("接待人", ALL_AUTHORITY.getName(), receptionPage.getReceptionSaleName());
            CommonUtil.checkResult("注册状态", "已注册", receptionPage.getRegistrationStatusName());
            //变更接待
            int receptionId = receptionPage.getId();
            user.loginApp(ALL_AUTHORITY);
            AppReceptionReceptorList receptorList = util.getReceptorList();
            String uid = receptorList.getUid();
            IScene receptionReceptorChangePageScene = AppReceptionReceptorChangePageScene.builder().id(receptionId).receptorId(uid).shopId(shopId).build();
            visitor.invokeApi(receptionReceptorChangePageScene);
            user.loginPc(ALL_AUTHORITY);
            ReceptionPage newReceptionPage = util.getReceptionPageById(receptionId);
            CommonUtil.checkResult("变更接待后接待人员", receptorList.getName(), newReceptionPage.getReceptionSaleName());
            user.loginPc(ALL_AUTHORITY);
            ReceptorChangeScene.builder().id(receptionId).receptorId(util.getReceptorList(ALL_AUTHORITY).getUid()).shopId(shopId).build().execute(visitor, true);
            //登录此人app完成接待
            user.loginApp(ALL_AUTHORITY);
            int finishReceptionNum = util.getReceptionPageNum();
            IScene receptionFinishReceptionScene = AppReceptionFinishReceptionScene.builder().id(receptionId).shopId(shopId).build();
            visitor.invokeApi(receptionFinishReceptionScene);
            int newFinishReceptionNum = util.getReceptionPageNum();
            CommonUtil.checkResult("完成接待后，app接待列表数", finishReceptionNum - 1, newFinishReceptionNum);
            user.loginPc(ALL_AUTHORITY);
            ReceptionPage finishReceptionPage = util.getReceptionPageById(receptionId);
            CommonUtil.checkResult("完成接待后pc接待列表接待状态", ReceptionStatusEnum.FINISH.getStatusName(), finishReceptionPage.getReceptionStatusName());
            CommonUtil.checkResult("完成接待后pc完成时间", DateTimeUtil.getFormat(new Date(), "yyyy-MM-dd HH:mm"), finishReceptionPage.getFinishTime());
            //评价
            user.loginPc(ALL_AUTHORITY);
            List<EvaluatePage> evaluatePageList = util.getEvaluatePageList();
            user.loginApplet(APPLET_USER_ONE);
            AppletEvaluateSubmitScene.builder().id(appointmentId).shopId(shopId).type(2).score(4).isAnonymous(true).describe(EnumDesc.MESSAGE_DESC.getDesc()).suggestion(EnumDesc.MESSAGE_DESC.getDesc()).build().execute(visitor, true);
            user.loginPc(ALL_AUTHORITY);
            List<EvaluatePage> newEvaluatePageList = util.getEvaluatePageList();
            CommonUtil.checkResult("评价列表数", evaluatePageList.size() + 1, newEvaluatePageList.size());
            CommonUtil.checkResult("评价描述", EnumDesc.MESSAGE_DESC.getDesc(), newEvaluatePageList.get(0).getDescribe());
            CommonUtil.checkResult("评价内容", EnumDesc.MESSAGE_DESC.getDesc(), newEvaluatePageList.get(0).getSuggestion());
            CommonUtil.checkResult("评价星星", 4, newEvaluatePageList.get(0).getScore());
            //跟进
            user.loginApp(ALL_AUTHORITY);
            Integer followId = util.getFollowUpPageList().get(0).getId();
            AppFollowUpCompleteScene.builder().id(followId).shopId(shopId).remark(EnumDesc.MESSAGE_DESC.getDesc()).build().execute(visitor, true);
            user.loginPc(ALL_AUTHORITY);
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
            IScene homePageScene = AppletHomePageScene.builder().build();
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

    //ok
    @Test(description = "小程序--积分兑换--兑换库存不足的实体商品，提示：商品库存不足")
    public void integralMall_system_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene exchangePageScene = ExchangePageScene.builder().status(IntegralExchangeStatusEnum.WORKING.name()).exchangeType(CommodityTypeEnum.REAL.name()).build();
            ExchangePage a = util.collectBean(exchangePageScene, ExchangePage.class).stream().filter(e -> e.getExchangedAndSurplus().split("/")[1].equals("0")).findFirst().orElse(null);
            ExchangePage exchangePage = a == null ? util.createExchangeRealGoods(0) : a;
            //修改为可兑换多次
            util.modifyExchangeGoodsLimit(exchangePage.getId(), exchangePage.getExchangeType(), false);
            user.loginApplet(APPLET_USER_ONE);
            int specificationId = AppletCommodityDetailScene.builder().id(exchangePage.getId()).build().execute(visitor, true).getJSONArray("specification_compose_list").getJSONObject(0).getInteger("id");
            AppletShippingAddress appletShippingAddress = AppletShippingAddressListScene.builder().build().execute(visitor, true).getJSONArray("list").stream().map(e -> (JSONObject) e).map(e -> JSONObject.toJavaObject(e, AppletShippingAddress.class)).collect(Collectors.toList()).get(0);
            //兑换积分
            String message = AppletSubmitOrderScene.builder().commodityId(exchangePage.getId()).specificationId(specificationId).smsNotify(false).commodityNum("1").districtCode(appletShippingAddress.getDistrictCode()).address(appletShippingAddress.getAddress()).receiver(appletShippingAddress.getName()).receivePhone(appletShippingAddress.getPhone()).build().execute(visitor, false).getString("message");
            String err = "商品库存不足";
            CommonUtil.checkResult("兑换库存不足的商品", err, message);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("小程序--积分兑换--兑换库存不足的实体商品，提示：商品库存不足");
        }
    }

    //ok
    @Test(description = "小程序--积分兑换--兑换卡券无库存的虚拟商品")
    public void integralMall_system_2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //找一个库存为大于0并且包含卡券剩余库存等于0的积分商品
            IScene exchangePageScene = ExchangePageScene.builder().status(IntegralExchangeStatusEnum.WORKING.name()).exchangeType(CommodityTypeEnum.FICTITIOUS.name()).build();
            ExchangePage a = util.collectBean(exchangePageScene, ExchangePage.class).stream().filter(e -> Integer.parseInt(e.getExchangedAndSurplus().split("/")[1]) > 0 && util.getExchangeGoodsContainVoucher(e.getId()).getSurplusInventory() == 0).findFirst().orElse(null);
            ExchangePage exchangePage;
            if (a == null) {
                Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.SELL_OUT).buildVoucher().getVoucherId();
                AddVoucherScene.builder().id(voucherId).addNumber(1).build().execute(visitor, true);
                String voucherName = util.getVoucherName(voucherId);
                util.applyVoucher(voucherName, "1");
                exchangePage = util.createExchangeFictitiousGoods(voucherId);
                util.pushMessage(0, true, voucherId);
            } else {
                exchangePage = a;
            }
            user.loginApplet(APPLET_USER_ONE);
            Long specificationId = AppletCommodityDetailScene.builder().id(exchangePage.getId()).build().execute(visitor, true).getLong("id");
            //兑换积分
            String message = AppletIntegralExchangeScene.builder().id(specificationId).build().execute(visitor, false).getString("message");
            String err = "很遗憾，优惠券已经被抢光了～更多活动敬请期待";
            CommonUtil.checkResult("兑换卡券无库存的虚拟商品", err, message);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("小程序--积分兑换--兑换卡券无库存的虚拟商品");
        }
    }

    //ok
    @Test(description = "小程序--积分兑换--兑换无库存的虚拟商品，提示：商品库存不足")
    public void integralMall_system_3() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //找一个库存为大于0并且包含卡券剩余库存等于0的积分商品
            IScene exchangePageScene = ExchangePageScene.builder().status(IntegralExchangeStatusEnum.WORKING.name()).exchangeType(CommodityTypeEnum.FICTITIOUS.name()).build();
            ExchangePage a = util.collectBean(exchangePageScene, ExchangePage.class).stream().filter(e -> Integer.parseInt(e.getExchangedAndSurplus().split("/")[1]) == 0 && util.getExchangeGoodsContainVoucher(e.getId()).getSurplusInventory() > 0).findFirst().orElse(null);
            ExchangePage exchangePage;
            if (a == null) {
                Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
                exchangePage = util.createExchangeFictitiousGoods(voucherId);
                EditExchangeStockScene.builder().id(exchangePage.getId()).changeStockType(ChangeStockTypeEnum.MINUS.name()).num("1").goodsName(exchangePage.getGoodsName()).type(exchangePage.getExchangeType()).build().execute(visitor, true);
            } else {
                exchangePage = a;
            }
            user.loginApplet(APPLET_USER_ONE);
            Long specificationId = AppletCommodityDetailScene.builder().id(exchangePage.getId()).build().execute(visitor, true).getLong("id");
            //兑换积分
            String message = AppletIntegralExchangeScene.builder().id(specificationId).build().execute(visitor, false).getString("message");
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
            ExchangePage a = util.collectBean(exchangePageScene, ExchangePage.class).stream().filter(e -> Integer.parseInt(e.getExchangedAndSurplus().split("/")[1]) > 0 && util.getExchangeGoodsContainVoucher(e.getId()).getSurplusInventory() > 0 && util.getExchangeGoodsContainVoucher(e.getId()).getVoucherStatus().equals(VoucherStatusEnum.WORKING.name())).findFirst().orElse(null);
            if (a == null) {
                //如果没有找一个库存为0并且包含卡券剩余库存大于0的积分商品
                ExchangePage b = util.collectBean(exchangePageScene, ExchangePage.class).stream().filter(e -> Integer.parseInt(e.getExchangedAndSurplus().split("/")[1]) == 0 && util.getExchangeGoodsContainVoucher(e.getId()).getSurplusInventory() > 0 && util.getExchangeGoodsContainVoucher(e.getId()).getVoucherStatus().equals(VoucherStatusEnum.WORKING.name())).findFirst().orElse(null);
                if (b == null) {
                    //如果没有创建一个库存为1并且包含卡券剩余库存>0的积分商品
                    voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
                    exchangePage = util.createExchangeFictitiousGoods(voucherId);
                } else {
                    //如果有给此积分商品增加一个库存
                    exchangePage = b;
                    voucherId = util.getExchangeGoodsContainVoucher(exchangePage.getId()).getVoucherId();
                    EditExchangeStockScene.builder().changeStockType(ChangeStockTypeEnum.ADD.name()).num("1").id(exchangePage.getId()).goodsName(exchangePage.getGoodsName()).type(CommodityTypeEnum.FICTITIOUS.name()).build().execute(visitor, true);
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
            Long specificationId = AppletCommodityDetailScene.builder().id(exchangePage.getId()).build().execute(visitor, true).getLong("id");
            AppletIntegralExchangeScene.builder().id(specificationId).build().execute(visitor, true);
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
    @Test(description = "小程序--签到--积分增加&积分明细记录增加类型")
    public void integralCenter_system_5() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene integralExchangeRulesScene = IntegralExchangeRulesScene.builder().build();
            int allSend = util.collectBean(integralExchangeRulesScene, JSONObject.class).stream().filter(e -> e.getString("rule_name").equals(AppletCodeBusinessTypeEnum.SIGN_IN.getTypeName())).map(e -> e.getInteger("all_send")).findFirst().orElse(0);
            user.loginApplet(APPLET_USER_ONE);
            JSONObject response = AppletSignInDetailScene.builder().build().execute(visitor, true);
            int signInScore = response.getInteger("sign_in_score");
            int signInScoreCount = response.getInteger("sign_in_score_count");
            int signInTime = response.getInteger("sign_in_time");
            boolean isSignIn = response.getBoolean("is_sign_in");
            CommonUtil.checkResult("小程序是否已签到", false, isSignIn);
            int integralRecordNum = util.getAppletIntegralRecordNum();
            //签到
            String gainTime = DateTimeUtil.getFormat(new Date(), "yyyy-MM-dd HH:mm:ss");
            String monthType = DateTimeUtil.getFormat(new Date(), "yyyy-MM");
            AppletSignInScene.builder().build().execute(visitor, true);
            JSONObject newResponse = AppletSignInDetailScene.builder().build().execute(visitor, true);
            CommonUtil.checkResult("小程序签到完成累计签到积分", signInScoreCount + signInScore, newResponse.getInteger("sign_in_score_count"));
            CommonUtil.checkResult("小程序签到完成累计签到次数", signInTime + 1, newResponse.getInteger("sign_in_time"));
            int newIntegralRecordNum = util.getAppletIntegralRecordNum();
            CommonUtil.checkResult("小程序签到完成积分明细列表数", integralRecordNum + 1, newIntegralRecordNum);
            AppletIntegralRecord integralRecord = util.getAppletIntegralRecordList().get(0);
            CommonUtil.checkResult("小程序签到完成积分明细获取时间", gainTime, integralRecord.getGainTime());
            CommonUtil.checkResult("小程序签到完成积分明细获取月份", monthType, integralRecord.getMonthType());
            CommonUtil.checkResult("小程序签到完成积分明细内容", "签到获得" + signInScore + "积分", integralRecord.getName());
            CommonUtil.checkResult("小程序签到完成积分明细类型", ChangeStockTypeEnum.ADD.name(), integralRecord.getChangeType());
            CommonUtil.checkResult("小程序签到完成积分明细积分数", signInScore, Integer.parseInt(integralRecord.getIntegral()));
            user.loginPc(ALL_AUTHORITY);
            IScene exchangeDetailedScene = ExchangeDetailedScene.builder().build();
            ExchangeDetailed exchangeDetailed = util.collectBean(exchangeDetailedScene, ExchangeDetailed.class).get(0);
            CommonUtil.checkResult("pc积分明细获取时间", gainTime, exchangeDetailed.getOperateTime());
            CommonUtil.checkResult("pc积分明细内容", "签到获得" + signInScore + "积分", exchangeDetailed.getChangeReason());
            CommonUtil.checkResult("pc积分明细类型", ChangeStockTypeEnum.ADD.name(), exchangeDetailed.getExchangeType());
            CommonUtil.checkResult("pc积分明细类型", ChangeStockTypeEnum.ADD.getDescription(), exchangeDetailed.getExchangeTypeName());
            CommonUtil.checkResult("pc积分明细积分数", signInScore, exchangeDetailed.getStockDetail());
            int newAllSend = util.collectBean(integralExchangeRulesScene, JSONObject.class).stream().filter(e -> e.getString("rule_name").equals(AppletCodeBusinessTypeEnum.SIGN_IN.getTypeName())).map(e -> e.getInteger("all_send")).findFirst().orElse(0);
            CommonUtil.checkResult("pc积分规则中签到已发放积分", allSend + signInScore, newAllSend);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("小程序--签到--积分增加&积分明细记录增加类型");
        }
    }
}
