package com.haisheng.framework.testng.bigScreen.jiaochen.wm.testcase;

import com.haisheng.framework.testng.bigScreen.crm.wm.base.agency.Visitor;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.*;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.customer.EnumAppletToken;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.app.AppletReceptionPage;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.EnumAccount;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.EnumDesc;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.appointment.AppointmentConfirmStatusEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.appointment.AppointmentTypeEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing.VoucherStatusEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.app.FollowUpCompleteScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.app.tack.AppAppointmentHandleScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.app.tack.AppAppointmentReceptionScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.app.tack.AppReceptionFinishReceptionScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.app.tack.AppReceptionReceptorChangePageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.granted.EvaluateSubmitScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.appointmentmanager.AppointmentPageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.receptionmanager.ReceptionPageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.util.SupporterUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.util.UserUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.voucher.VoucherGenerator;
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

/**
 * 小程序用例
 *
 * @author wangmin
 * @date 2021/1/29 11:17
 */
public class AppletManager extends TestCaseCommon implements TestCaseStd {
    private static final EnumTestProduce product = EnumTestProduce.JIAOCHEN_DAILY;
    private static final EnumAccount ADMINISTRATOR = EnumAccount.ADMINISTRATOR_DAILY;
    private static final EnumAccount MARKETING = EnumAccount.MARKETING_DAILY;
    private static final EnumAppletToken APPLET_USER_ONE = EnumAppletToken.JC_WM_DAILY;
    private static final Integer SIZE = 100;
    public Visitor visitor = new Visitor(product);
    public SupporterUtil util = new SupporterUtil(visitor);
    public UserUtil user = new UserUtil(visitor);
    CommonConfig commonConfig = new CommonConfig();

    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before class initial");
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

    @Test(description = "")
    public void appointmentManager() {
        Integer shopId = util.getShopId();
        Integer appointmentNumber = util.appointmentNumber(new Date());
        IScene appointmentPageScene = AppointmentPageScene.builder().build();
        int appointmentPageTotal = visitor.invokeApi(appointmentPageScene).getInteger("total");
        //预约保养
        commonConfig.roleId = "1";
        user.loginApplet(APPLET_USER_ONE);
        int appointmentNum = util.getAppletAppointmentNum();
        String date = DateTimeUtil.getFormat(new Date());
        Integer appointmentId = util.appointment(AppointmentTypeEnum.MAINTAIN, date);
        int newAppointmentNum = util.getAppletAppointmentNum();
        Integer newAppointmentNumber = util.appointmentNumber(new Date());
        int newAppointmentPageTotal = visitor.invokeApi(appointmentPageScene).getInteger("total");
        CommonUtil.checkResult("我的预约列表数", appointmentNum + 1, newAppointmentNum);
        CommonUtil.checkResult("预约看板分子数", appointmentNumber + 1, newAppointmentNumber);
        CommonUtil.checkResult("预约记录列表数", appointmentPageTotal + 1, newAppointmentPageTotal);
        //确认预约
        user.loginApp(ADMINISTRATOR);
        Integer appAppointmentNum = util.getAppointmentPageNum();
        IScene appointmentHandleScene = AppAppointmentHandleScene.builder().id(appointmentId).shopId(shopId).type(10).build();
        visitor.invokeApi(appointmentHandleScene);
        Integer newAppAppointmentNum = util.getAppointmentPageNum();
        String appointmentStatusName = util.getAppointmentPageById(appointmentId).getAppointmentStatusName();
        CommonUtil.checkResult("app预约记录数", appAppointmentNum, newAppAppointmentNum);
        CommonUtil.checkResult("确认预约后预约状态", AppointmentConfirmStatusEnum.AGREE.getStatusName(), appointmentStatusName);
        //点接待
        int appReceptionPageNum = util.getReceptionPageNum();
        int pcReceptionPageNum = visitor.invokeApi(ReceptionPageScene.builder().build()).getInteger("total");
        IScene appointmentReceptionScene = AppAppointmentReceptionScene.builder().id(appointmentId).build();
        visitor.invokeApi(appointmentReceptionScene);
        int newAppReceptionPageNum = util.getReceptionPageNum();
        int newPcReceptionPageNum = visitor.invokeApi(ReceptionPageScene.builder().build()).getInteger("total");
        CommonUtil.checkResult("app接待页列表数", appReceptionPageNum + 1, newAppReceptionPageNum);
        CommonUtil.checkResult("pc接待列表数", pcReceptionPageNum + 1, newPcReceptionPageNum);
        //变更接待
        Integer receptionId = util.getReceptionPageList().stream().filter(e -> e.getCustomerPhone().equals(MARKETING.getPhone())).map(AppletReceptionPage::getId).findFirst().orElse(null);
        String uid = util.getReceptorId().getUid();
        IScene receptionReceptorChangePageScene = AppReceptionReceptorChangePageScene.builder().id(appointmentId).shopId(shopId).receptorId(uid).build();
        visitor.invokeApi(receptionReceptorChangePageScene);
        //购买套餐
        Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
        String voucherName = util.getVoucherName(voucherId);
        util.receptionBuyTemporaryPackage(receptionId, voucherName, 1);
        //登录此人app完成接待
        commonConfig.shopId = String.valueOf(shopId);
        user.loginPc(MARKETING);
        IScene receptionFinishReceptionScene = AppReceptionFinishReceptionScene.builder().id(receptionId).shopId(shopId).build();
        visitor.invokeApi(receptionFinishReceptionScene);
        //评价
        user.loginApplet(APPLET_USER_ONE);
        IScene evaluateSubmitScene = EvaluateSubmitScene.builder().id(appointmentId).shopId(shopId).type(1).score(4).describe(EnumDesc.MESSAGE_DESC.getDesc()).build();
        visitor.invokeApi(evaluateSubmitScene);
        //跟进
        commonConfig.shopId = String.valueOf(shopId);
        user.loginPc(MARKETING);
        Integer followId = util.getFollowUpPageList().get(0).getId();
        IScene followUpCompleteScene = FollowUpCompleteScene.builder().id(followId).shopId(shopId).remark(EnumDesc.MESSAGE_DESC.getDesc()).build();
        visitor.invokeApi(followUpCompleteScene);
    }
}
