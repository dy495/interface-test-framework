package com.haisheng.framework.testng.bigScreen.jiaochen.wm.testcase;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.*;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.customer.EnumAppletToken;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.generate.activity.ActivityGenerator;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.VoucherSendRecord;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.EnumAccount;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.EnumVP;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.activity.ActivityApprovalStatusEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.activity.ActivityStatusEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing.VoucherSourceEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing.VoucherStatusEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.activity.ManageApprovalScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.activity.ManageRegisterApprovalScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.activity.ManageRegisterScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.vouchermanage.SendRecordScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.util.SupporterUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.util.UserUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.generate.voucher.VoucherGenerator;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.util.CommonUtil;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 卡券领取渠道校验
 *
 * @author wangmin
 * @date 2021/1/26 16:44
 */
public class SendVoucherCase extends TestCaseCommon implements TestCaseStd {
    private static final EnumTestProduce PRODUCT = EnumTestProduce.JIAOCHEN_DAILY;
    private static final EnumAccount ALL_AUTHORITY = EnumAccount.ALL_AUTHORITY_DAILY;
    private static final EnumAccount MARKETING = EnumAccount.MARKETING_DAILY;
    private static final EnumAppletToken APPLET_USER_ONE = EnumAppletToken.JC_WM_DAILY;
    public VisitorProxy visitor = new VisitorProxy(PRODUCT);
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
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, PRODUCT.getDesc() + commonConfig.checklistQaOwner);
        //替换钉钉推送
        commonConfig.dingHook = EnumDingTalkWebHook.CAR_OPEN_MANAGEMENT_PLATFORM_GRP.getWebHook();
        //放入shopId
        commonConfig.product = PRODUCT.getAbbreviation();
        commonConfig.referer = PRODUCT.getReferer();
        commonConfig.shopId = PRODUCT.getShopId();
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

    @Test(description = "优惠券管理--接待时购买固定套餐")
    public void sendVoucher_data_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            Long packageId = util.editPackage(voucherId, 1);
            util.receptionBuyFixedPackage(packageId, 1);
            util.makeSureBuyPackage(packageId);
            checkVoucherRecord(voucherId, VoucherSourceEnum.PURCHASE);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        }
    }

    @Test(description = "优惠券管理--接待时赠送固定套餐")
    public void sendVoucher_data_2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            Long packageId = util.editPackage(voucherId, 1);
            util.receptionBuyFixedPackage(packageId, 0);
            checkVoucherRecord(voucherId, VoucherSourceEnum.PRESENT);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        }
    }

    @Test(description = "优惠券管理--接待时购买临时套餐")
    public void sendVoucher_data_3() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            String voucherName = util.getVoucherName(voucherId);
            util.receptionBuyTemporaryPackage(voucherName, 1);
            util.makeSureBuyPackage(EnumVP.TEMPORARY.getPackageName());
            checkVoucherRecord(voucherId, VoucherSourceEnum.PURCHASE);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        }
    }

    @Test(description = "优惠券管理--接待时赠送临时套餐")
    public void sendVoucher_data_4() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            String voucherName = util.getVoucherName(voucherId);
            util.receptionBuyTemporaryPackage(voucherName, 0);
            checkVoucherRecord(voucherId, VoucherSourceEnum.PRESENT);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        }
    }

    @Test(description = "优惠券管理--套餐页购买固定套餐")
    public void sendVoucher_data_5() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            Long packageId = util.editPackage(voucherId, 1);
            util.buyFixedPackage(packageId, 1);
            util.makeSureBuyPackage(packageId);
            checkVoucherRecord(voucherId, VoucherSourceEnum.PURCHASE);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        }
    }

    @Test(description = "优惠券管理--套餐页赠送固定套餐")
    public void sendVoucher_data_6() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            Long packageId = util.editPackage(voucherId, 1);
            util.buyFixedPackage(packageId, 0);
            checkVoucherRecord(voucherId, VoucherSourceEnum.PRESENT);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        }
    }

    @Test(description = "优惠券管理--套餐页购买临时套餐")
    public void sendVoucher_data_7() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            String voucherName = util.getVoucherName(voucherId);
            util.buyTemporaryPackage(voucherName, 1);
            util.makeSureBuyPackage(EnumVP.TEMPORARY.getPackageName());
            checkVoucherRecord(voucherId, VoucherSourceEnum.PURCHASE);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        }
    }

    @Test(description = "优惠券管理--套餐页赠送临时套餐")
    public void sendVoucher_data_8() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            String voucherName = util.getVoucherName(voucherId);
            util.buyTemporaryPackage(voucherName, 0);
            checkVoucherRecord(voucherId, VoucherSourceEnum.PRESENT);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        }
    }

    @Test(description = "优惠券管理--招募活动报名即获取优惠券")
    public void sendVoucher_data_9() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            //创建
            Long activityId = util.createRecruitActivity(voucherId, true, 0, false);
            //审批
            List<Long> ids = new ArrayList<>();
            ids.add(activityId);
            IScene scene = ManageApprovalScene.builder().ids(ids).status(201).build();
            visitor.invokeApi(scene);
            //活动报名
            user.loginApplet(APPLET_USER_ONE);
            util.activityRegister(activityId);
            checkVoucherRecord(voucherId, VoucherSourceEnum.ACTIVITY);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        }
    }

    @Test(description = "优惠券管理--招募活动报名成功获取优惠券")
    public void sendVoucher_data_10() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取一个进行中的卡券
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            //创建一个活动
            IScene scene = util.createRecruitActivityScene(voucherId, true, 0, false);
            Long activityId = new ActivityGenerator.Builder().createScene(scene).visitor(visitor).activityStatusEnum(ActivityStatusEnum.PENDING).buildActivity().getActivityId();
            //审批
            util.approvalActivity(201, activityId);
            //活动报名
            user.loginApplet(APPLET_USER_ONE);
            util.activityRegister(activityId);
            //审批通过
            user.loginPc(ALL_AUTHORITY);
            IScene manageRegisterScene = ManageRegisterScene.builder().status(ActivityApprovalStatusEnum.PENDING.getId()).activityId(activityId).build();
            JSONArray list = visitor.invokeApi(manageRegisterScene).getJSONArray("list");
            Long id = Objects.requireNonNull(list.stream().map(e -> (JSONObject) e).filter(e -> e.getString("customer_phone").equals(MARKETING.getPhone())).findFirst().orElse(null)).getLong("id");
            List<Long> ids = new ArrayList<>();
            ids.add(id);
            IScene manageRegisterApprovalScene = ManageRegisterApprovalScene.builder().activityId(activityId).ids(ids).status(101).build();
            visitor.invokeApi(manageRegisterApprovalScene);
            checkVoucherRecord(voucherId, VoucherSourceEnum.ACTIVITY);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        }
    }

    /**
     * 拉新做不了
     */
    @Test(description = "优惠券管理--裂变活动拉新成功获取优惠券", enabled = false)
    public void sendVoucher_data_11() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取一个进行中的卡券
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
//            IScene scene = util.createFissionActivityScene(voucherId);
//            Long activityId = new ActivityGenerator.Builder().visitor(visitor).createScene(scene).activityStatusEnum(ActivityStatusEnum.PASSED).buildActivity().getActivityId();
//            //拉新
//            user.loginApplet(APPLET_USER_ONE);
//            util.activityRegister(activityId);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        }
    }

    @Test(description = "优惠券管理--消息推送优惠券")
    public void sendVoucher_data_12() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            util.pushMessage(0, true, voucherId);
            checkVoucherRecord(voucherId, VoucherSourceEnum.MESSAGE);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        }
    }

    public void checkVoucherRecord(Long voucherId, VoucherSourceEnum voucherSourceEnum) {
        String voucherName = util.getPackageName(voucherId);
        IScene scene = SendRecordScene.builder().voucherId(voucherId).build();
        VoucherSendRecord voucherSendRecord = util.collectBean(scene, VoucherSendRecord.class).get(0);
        String sendChannelName = voucherSendRecord.getSendChannelName();
        CommonUtil.checkResult(voucherName + " 发出渠道", voucherSourceEnum.getName(), sendChannelName);
    }
}
