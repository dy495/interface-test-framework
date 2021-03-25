package com.haisheng.framework.testng.bigScreen.jiaochenonline.gly;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.*;
import com.haisheng.framework.testng.bigScreen.jiaochen.ScenarioUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.gly.util.PublicParameter;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.EnumAccount;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing.VoucherStatusEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.generate.voucher.VoucherGenerator;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.activity.ArticleVoucherReceiveScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.activity.FissionVoucherAddScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.activity.ManageRecruitAddScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.activity.ManageRegisterPageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.util.SupporterUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.util.UserUtil;
import com.haisheng.framework.testng.bigScreen.jiaochenonline.gly.util.BusinessUtilOnline;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ActivityTypeOnLine extends TestCaseCommon implements TestCaseStd {
    ScenarioUtil jc = new ScenarioUtil();
    private static final EnumTestProduce product = EnumTestProduce.JC_ONLINE;
    private static final EnumAccount ADMINISTRATOR=EnumAccount.ALL_JC_ONLINE;
    public VisitorProxy visitor = new VisitorProxy(product);
    //    BusinessUtil businessUtil = new BusinessUtil(visitor);
    BusinessUtilOnline businessUtil=new BusinessUtilOnline(visitor);
    SupporterUtil supporterUtil = new SupporterUtil(visitor);
    PublicParameter pp = new PublicParameter();
    UserUtil user = new UserUtil(visitor);
    CommonConfig commonConfig = new CommonConfig();

    /**
     * @description: initial test class level config, such as appid/uid/ak/dinghook/push_rd_name
     */
    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before class initial");
        jc.changeIpPort(EnumTestProduce.JC_ONLINE.getAddress());
        //替换checklist的相关信息
        commonConfig.checklistAppId = EnumChecklistAppId.DB_APP_ID_SCREEN_SERVICE.getId();
        commonConfig.checklistConfId = EnumChecklistConfId.DB_SERVICE_ID_CRM_ONLINE_SERVICE.getId();
        commonConfig.checklistQaOwner = EnumChecklistUser.WM.getName();
        commonConfig.product = product.getAbbreviation();
        //替换jenkins-job的相关信息
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.JIAOCHEN_ONLINE_TEST.getJobName());
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, product.getDesc() + commonConfig.checklistQaOwner);
        //替换钉钉推送
        commonConfig.dingHook = EnumDingTalkWebHook.ONLINE_CAR_CAR_OPEN_MANAGEMENT_PLATFORM_GRP.getWebHook();
        //放入shopId
        commonConfig.roleId = ADMINISTRATOR.getRoleId();
        commonConfig.referer = product.getReferer();
        commonConfig.shopId = product.getShopId();
        beforeClassInit(commonConfig);
        logger.debug("jc: " + jc);
    }

    @AfterClass
    @Override
    public void clean() {
        afterClassClean();
    }

    /**
     * @deprecated :get a fresh case ds to save case result, such as result/response
     */
    @BeforeMethod
    @Override
    public void createFreshCase(Method method) {
        logger.debug("beforeMethod");
        caseResult = getFreshCaseResult(method);
        logger.debug("case: " + caseResult);
        user.loginPc(ADMINISTRATOR);
    }

    /**
     * 裂变活动-参与客户不限制，领取次数不限制，分享人数=2
     * @return
     */
    @Test(description = "裂变活动-参与客户不限制，领取次数不限制，分享人数=2")
    public void activityType1(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
//            获取优惠券ID
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
//            Long voucherId=49L;
            List<String> picList = new ArrayList<>();
            picList.add(supporterUtil.getPicPath());
            // 创建被邀请者和分享者的信息字段
            JSONObject invitedVoucher = businessUtil.getInvitedVoucher(voucherId, 1, String.valueOf(businessUtil.getVoucherSurplusInventory(voucherId)), 2, "", "", 3);
            JSONObject shareVoucher = businessUtil.getShareVoucher(voucherId, 1, String.valueOf(businessUtil.getVoucherSurplusInventory(voucherId)), 2, "", "", 3);
            IScene scene =FissionVoucherAddScene.builder()
                    .type(1)
                    .participationLimitType(0)
                    .receiveLimitType(0)
                    .title("裂变-分享人数=2")
                    .rule(pp.rule)
                    .startDate(businessUtil.getStartDate())
                    .endDate(businessUtil.getEndDate())
                    .subjectType(supporterUtil.getSubjectType())
                    .subjectId(supporterUtil.getSubjectDesc(supporterUtil.getSubjectType()))
                    .label("RED_PAPER")
                    .picList(picList)
                    .shareNum("2")
                    .shareVoucher(shareVoucher)
                    .invitedVoucher(invitedVoucher)
                    .build();
            Long activityId = visitor.invokeApi(scene).getLong("id");
            //审批通过招募活动
            businessUtil.getApprovalPassed(activityId);
            Preconditions.checkArgument(activityId>0,"裂变活动1创建失败");

        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("裂变活动-参与客户不限制，领取次数不限制，分享人数=2");
        }

    }

    /**
     * 裂变活动-参与客户不限制，领取次数限制1，分享人数=1
     * @return
     */
    @Test(description = "裂变活动-参与客户不限制，领取次数限制1，分享人数=1")
    public void activityType2(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //获取优惠券ID
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
//            Long voucherId=49L;
            List<String> picList = new ArrayList<>();
            picList.add(supporterUtil.getPicPath());
            // 创建被邀请者和分享者的信息字段
            JSONObject invitedVoucher = businessUtil.getInvitedVoucher(voucherId, 1, String.valueOf(businessUtil.getVoucherSurplusInventory(voucherId)), 2, "", "", 3);
            JSONObject shareVoucher = businessUtil.getShareVoucher(voucherId, 1, String.valueOf(businessUtil.getVoucherSurplusInventory(voucherId)), 2, "", "", 3);
            IScene scene =FissionVoucherAddScene.builder()
                    .type(1)
                    .participationLimitType(0)
                    .receiveLimitType(1)
                    .receiveLimitTimes("1")
                    .title("裂变-领取次数=1-分享人数=1")
                    .rule(pp.rule)
                    .startDate(businessUtil.getStartDate())
                    .endDate(businessUtil.getEndDate())
                    .subjectType(supporterUtil.getSubjectType())
                    .subjectId(supporterUtil.getSubjectDesc(supporterUtil.getSubjectType()))
                    .label("RED_PAPER")
                    .picList(picList)
                    .shareNum("1")
                    .shareVoucher(shareVoucher)
                    .invitedVoucher(invitedVoucher)
                    .build();
            Long activityId = visitor.invokeApi(scene).getLong("id");
            //审批通过招募活动
            businessUtil.getApprovalPassed(activityId);
            Preconditions.checkArgument(activityId>0,"裂变活动2创建失败");

        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("裂变活动-参与客户不限制，领取次数限制1，分享人数=1");
        }

    }
    /**
     * 裂变活动-参与客户不限制，每人每天领取限制1，分享人数=1
     * @return
     */
    @Test(description = "裂变活动-参与客户不限制，每天领取限制1，分享人数=1")
    public void activityType3(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //获取优惠券ID
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
//            Long voucherId=49L;
            List<String> picList = new ArrayList<>();
            picList.add(supporterUtil.getPicPath());
            // 创建被邀请者和分享者的信息字段
            JSONObject invitedVoucher = businessUtil.getInvitedVoucher(voucherId, 1, String.valueOf(businessUtil.getVoucherSurplusInventory(voucherId)), 2, "", "", 3);
            JSONObject shareVoucher = businessUtil.getShareVoucher(voucherId, 1, String.valueOf(businessUtil.getVoucherSurplusInventory(voucherId)), 2, "", "", 3);
            IScene scene =FissionVoucherAddScene.builder()
                    .type(1)
                    .participationLimitType(0)
                    .receiveLimitType(2)
                    .receiveLimitTimes("1")
                    .title("裂变-每天次数=1-分享人数=1")
                    .rule(pp.rule)
                    .startDate(businessUtil.getStartDate())
                    .endDate(businessUtil.getEndDate())
                    .subjectType(supporterUtil.getSubjectType())
                    .subjectId(supporterUtil.getSubjectDesc(supporterUtil.getSubjectType()))
                    .label("RED_PAPER")
                    .picList(picList)
                    .shareNum("1")
                    .shareVoucher(shareVoucher)
                    .invitedVoucher(invitedVoucher)
                    .build();
            Long activityId = visitor.invokeApi(scene).getLong("id");
            //审批通过招募活动
            businessUtil.getApprovalPassed(activityId);
            Preconditions.checkArgument(activityId>0,"裂变活动3创建失败");

        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("裂变活动-参与客户不限制，每人每天领取限制1，分享人数=1");
        }
    }

    /**
     * 裂变活动-领取次数不限制，分享人数=2，参与客户限制为普通、VIP、小程序、销售、售后
     * @return
     */
    @Test(description = "裂变活动-领取次数不限制，分享人数=2，参与客户限制为普通、VIP、小程序、销售、售后")
    public void activityType4(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //参与客户限制
            List<Integer> labels = new ArrayList<>();
            String[][] label = {{"1", "普通会员"}, {"100", "VIP会员"}, {"1000", "小程序客户"}, {"2000", "销售客户"}, {"3000", "售后客户"}};
            //获取优惠券ID
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
//            Long voucherId=49L;
            List<String> picList = new ArrayList<>();
            picList.add(supporterUtil.getPicPath());
            // 创建被邀请者和分享者的信息字段
            JSONObject invitedVoucher = businessUtil.getInvitedVoucher(voucherId, 1, String.valueOf(businessUtil.getVoucherSurplusInventory(voucherId)), 2, "", "", 3);
            JSONObject shareVoucher = businessUtil.getShareVoucher(voucherId, 1, String.valueOf(businessUtil.getVoucherSurplusInventory(voucherId)), 2, "", "", 3);
            for(int i=0;i<label.length;i++){
                labels.add(Integer.valueOf(label[i][0]));
                IScene scene =FissionVoucherAddScene.builder()
                        .type(1)
                        .participationLimitType(1)
                        .chooseLabels(labels)
                        .receiveLimitType(0)
                        .title("裂变-分享人数2-限制为"+label[i][1])
                        .rule(pp.rule)
                        .startDate(businessUtil.getStartDate())
                        .endDate(businessUtil.getEndDate())
                        .subjectType(supporterUtil.getSubjectType())
                        .subjectId(supporterUtil.getSubjectDesc(supporterUtil.getSubjectType()))
                        .label("RED_PAPER")
                        .picList(picList)
                        .shareNum("1")
                        .shareVoucher(shareVoucher)
                        .invitedVoucher(invitedVoucher)
                        .build();
                Long activityId = visitor.invokeApi(scene).getLong("id");
                //审批通过招募活动
                businessUtil.getApprovalPassed(activityId);
                Preconditions.checkArgument(activityId>0,"裂变活动4创建失败");

            }
        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("裂变活动-领取次数不限制，分享人数=2，参与客户限制为普通、VIP、小程序、销售、售后");
        }
    }


    /**
     * 招募活动-参与客户限制为普通、VIP、小程序、销售、售后
     * @return
     */
    @Test(description = "招募活动-参与客户限制为普通、VIP、小程序、销售、售后")
    public void activityType5(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
//            Long voucherId=49L;
            //客户限制
            List<Integer> labels = new ArrayList<>();
            String[][] label = {{"1", "普通会员"}, {"100", "VIP会员"}, {"1000", "小程序客户"}, {"2000", "销售客户"}, {"3000", "售后客户"}};
            List<String> picList = new ArrayList<>();
            SupporterUtil supporterUtil = new SupporterUtil(visitor);
            PublicParameter pp = new PublicParameter();
            picList.add(0, supporterUtil.getPicPath());
            //填写报名所需要信息
            List<Boolean> isShow = new ArrayList<>();
            isShow.add(false);
            isShow.add(false);
            isShow.add(false);
            isShow.add(false);
            isShow.add(false);
            isShow.add(false);
            isShow.add(false);
            List<Boolean> isRequired = new ArrayList<>();
            isRequired.add(false);
            isRequired.add(false);
            isRequired.add(false);
            isRequired.add(false);
            isRequired.add(false);
            isRequired.add(false);
            isRequired.add(false);
            JSONArray registerInformationList = this.businessUtil.getRegisterInformationList(isShow, isRequired);
            //报名成功奖励
            JSONArray registerObject = businessUtil.getRewardVouchers(voucherId, 1, businessUtil.getVoucherSurplusInventory(voucherId));
            //卡券有效期
            JSONObject voucherValid = businessUtil.getVoucherValid(2, null, null, 10);
            //创建招募活动-共有的--基础信息
            for(int i=0;i<label.length;i++){
                labels.add(Integer.valueOf(label[i][0]));
                ManageRecruitAddScene.ManageRecruitAddSceneBuilder builder = ManageRecruitAddScene.builder()
                        .type(2)
                        .participationLimitType(1)
                        .chooseLabels(labels)
                        .title("招募-参与客户限制：" +label[i][1])
                        .startDate(businessUtil.getStartDate())
                        .endDate(businessUtil.getEndDate())
                        .applyStart(businessUtil.getStartDate())
                        .applyEnd(businessUtil.getEndDate())
                        .isLimitQuota(true)
                        .quota(10)
                        .subjectType(supporterUtil.getSubjectType())
                        .subjectId(supporterUtil.getSubjectDesc(supporterUtil.getSubjectType()))
                        .label("BARGAIN")
                        .picList(picList)
                        .rule(pp.rule)
                        .registerInformationList(registerInformationList)
                        .successReward(true)
                        .rewardReceiveType(0)
                        .isNeedApproval(true);
                if (true) {
                    builder.rewardVouchers(registerObject)
                            .voucherValid(voucherValid);
                }
                IScene scene = builder.build();
                Long activityId = visitor.invokeApi(scene).getLong("id");
                //审批通过招募活动
                businessUtil.getApprovalPassed(activityId);
                Preconditions.checkArgument(activityId>0,"招募活动5创建失败");
            }

        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("招募活动-参与客户限制为普通、VIP、小程序、销售、售后");
        }
    }

    /**
     * 招募活动-客户类型不限制，报名成功奖励：有奖励，自动发放，需要审批
     * @return
     */
    @Test(description = "招募活动-客户类型不限制，报名成功奖励：有奖励，自动发放，需要审批")
    public void activityType6(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
//            Long voucherId=49L;
            //客户限制
            List<Integer> labels = new ArrayList<>();
            List<String> picList = new ArrayList<>();
            SupporterUtil supporterUtil = new SupporterUtil(visitor);
            PublicParameter pp = new PublicParameter();
            picList.add(0, supporterUtil.getPicPath());
            //填写报名所需要信息
            List<Boolean> isShow = new ArrayList<>();
            isShow.add(true);
            isShow.add(true);
            isShow.add(true);
            isShow.add(true);
            isShow.add(true);
            isShow.add(true);
            isShow.add(true);
            List<Boolean> isRequired = new ArrayList<>();
            isRequired.add(true);
            isRequired.add(true);
            isRequired.add(true);
            isRequired.add(true);
            isRequired.add(true);
            isRequired.add(true);
            isRequired.add(true);
            isRequired.add(true);
            JSONArray registerInformationList = this.businessUtil.getRegisterInformationList(isShow, isRequired);
            //报名成功奖励
            JSONArray registerObject = businessUtil.getRewardVouchers(voucherId, 1, businessUtil.getVoucherSurplusInventory(voucherId));
            //卡券有效期
            JSONObject voucherValid = businessUtil.getVoucherValid(2, null, null, 10);
            //创建招募活动-共有的--基础信息
                ManageRecruitAddScene.ManageRecruitAddSceneBuilder builder = ManageRecruitAddScene.builder()
                        .type(2)
                        .participationLimitType(0)
                        .chooseLabels(labels)
                        .title("招募-有奖励-自动发放-需审批" )
                        .startDate(businessUtil.getStartDate())
                        .endDate(businessUtil.getEndDate())
                        .applyStart(businessUtil.getStartDate())
                        .applyEnd(businessUtil.getEndDate())
                        .isLimitQuota(true)
                        .quota(10)
                        .subjectType(supporterUtil.getSubjectType())
                        .subjectId(supporterUtil.getSubjectDesc(supporterUtil.getSubjectType()))
                        .label("BARGAIN")
                        .picList(picList)
                        .rule(pp.rule)
                        .registerInformationList(registerInformationList)
                        .successReward(true)
                        .rewardReceiveType(0)
                        .isNeedApproval(true);
                if (true) {
                    builder.rewardVouchers(registerObject)
                            .voucherValid(voucherValid);
                }
                IScene scene = builder.build();
                Long activityId = visitor.invokeApi(scene).getLong("id");
                //审批通过招募活动
                 businessUtil.getApprovalPassed(activityId);
                Preconditions.checkArgument(activityId>0,"招募活动6创建失败");

        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("招募活动-客户类型不限制，报名成功奖励：有奖励，自动发放，需要审批");
        }
    }

    /**
     * 招募活动-客户类型不限制，报名成功奖励：有奖励，自动发放，不需要审批
     * @return
     */
    @Test(description = "招募活动-客户类型不限制，报名成功奖励：有奖励，自动发放，不需要审批")
    public void activityType7(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
//            Long voucherId=49L;
            //客户限制
            List<Integer> labels = new ArrayList<>();
            List<String> picList = new ArrayList<>();
            SupporterUtil supporterUtil = new SupporterUtil(visitor);
            PublicParameter pp = new PublicParameter();
            picList.add(0, supporterUtil.getPicPath());
            //填写报名所需要信息
            List<Boolean> isShow = new ArrayList<>();
            isShow.add(true);
            isShow.add(true);
            isShow.add(true);
            isShow.add(true);
            isShow.add(true);
            isShow.add(true);
            isShow.add(true);
            List<Boolean> isRequired = new ArrayList<>();
            isRequired.add(true);
            isRequired.add(true);
            isRequired.add(true);
            isRequired.add(true);
            isRequired.add(true);
            isRequired.add(true);
            isRequired.add(true);
            JSONArray registerInformationList = this.businessUtil.getRegisterInformationList(isShow, isRequired);
            //报名成功奖励
            JSONArray registerObject = businessUtil.getRewardVouchers(voucherId, 1, businessUtil.getVoucherSurplusInventory(voucherId));
            //卡券有效期
            JSONObject voucherValid = businessUtil.getVoucherValid(2, null, null, 10);
            //创建招募活动-共有的--基础信息
            ManageRecruitAddScene.ManageRecruitAddSceneBuilder builder = ManageRecruitAddScene.builder()
                    .type(2)
                    .participationLimitType(0)
                    .chooseLabels(labels)
                    .title("招募-有奖励-自动发放-不需审批" )
                    .startDate(businessUtil.getStartDate())
                    .endDate(businessUtil.getEndDate())
                    .applyStart(businessUtil.getStartDate())
                    .applyEnd(businessUtil.getEndDate())
                    .isLimitQuota(true)
                    .quota(10)
                    .subjectType(supporterUtil.getSubjectType())
                    .subjectId(supporterUtil.getSubjectDesc(supporterUtil.getSubjectType()))
                    .label("BARGAIN")
                    .picList(picList)
                    .rule(pp.rule)
                    .registerInformationList(registerInformationList)
                    .successReward(true)
                    .rewardReceiveType(0)
                    .isNeedApproval(false);
            if (true) {
                builder.rewardVouchers(registerObject)
                        .voucherValid(voucherValid);
            }
            IScene scene = builder.build();
            Long activityId = visitor.invokeApi(scene).getLong("id");
            //审批通过招募活动
            businessUtil.getApprovalPassed(activityId);
            Preconditions.checkArgument(activityId>0,"招募活动7创建失败");

        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("招募活动-客户类型不限制，报名成功奖励：有奖励，自动发放，不需要审批");
        }
    }

    /**
     * 招募活动-客户类型不限制，报名成功奖励：有奖励，用户领取，需要审批
     * @return
     */
    @Test(description = "招募活动-客户类型不限制，报名成功奖励：有奖励，用户领取，需要审批")
    public void activityType8(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
//            Long voucherId=49L;
            //客户限制
            List<Integer> labels = new ArrayList<>();
            List<String> picList = new ArrayList<>();
            SupporterUtil supporterUtil = new SupporterUtil(visitor);
            PublicParameter pp = new PublicParameter();
            picList.add(0, supporterUtil.getPicPath());
            //填写报名所需要信息
            List<Boolean> isShow = new ArrayList<>();
            isShow.add(true);
            isShow.add(true);
            isShow.add(true);
            isShow.add(true);
            isShow.add(true);
            isShow.add(true);
            isShow.add(true);
            List<Boolean> isRequired = new ArrayList<>();
            isRequired.add(true);
            isRequired.add(true);
            isRequired.add(true);
            isRequired.add(true);
            isRequired.add(true);
            isRequired.add(true);
            isRequired.add(true);
            JSONArray registerInformationList = this.businessUtil.getRegisterInformationList(isShow, isRequired);
            //报名成功奖励
            JSONArray registerObject = businessUtil.getRewardVouchers(voucherId, 1, businessUtil.getVoucherSurplusInventory(voucherId));
            //卡券有效期
            JSONObject voucherValid = businessUtil.getVoucherValid(2, null, null, 10);
            //创建招募活动-共有的--基础信息
            ManageRecruitAddScene.ManageRecruitAddSceneBuilder builder = ManageRecruitAddScene.builder()
                    .type(2)
                    .participationLimitType(0)
                    .chooseLabels(labels)
                    .title("招募-有奖励-用户领取-需审批" )
                    .startDate(businessUtil.getStartDate())
                    .endDate(businessUtil.getEndDate())
                    .applyStart(businessUtil.getStartDate())
                    .applyEnd(businessUtil.getEndDate())
                    .isLimitQuota(true)
                    .quota(10)
                    .subjectType(supporterUtil.getSubjectType())
                    .subjectId(supporterUtil.getSubjectDesc(supporterUtil.getSubjectType()))
                    .label("BARGAIN")
                    .picList(picList)
                    .rule(pp.rule)
                    .registerInformationList(registerInformationList)
                    .successReward(true)
                    .rewardReceiveType(1)
                    .isNeedApproval(true);
            if (true) {
                builder.rewardVouchers(registerObject)
                        .voucherValid(voucherValid);
            }
            IScene scene = builder.build();
            Long activityId = visitor.invokeApi(scene).getLong("id");
            //审批通过招募活动
            businessUtil.getApprovalPassed(activityId);
            Preconditions.checkArgument(activityId>0,"招募活动7创建失败");

        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("招募活动-客户类型不限制，报名成功奖励：有奖励，用户领取，需要审批");
        }
    }

    /**
     * 招募活动-客户类型不限制，报名成功奖励：有奖励，用户领取，不需要审批
     * @return
     */
    @Test(description = "招募活动-客户类型不限制，报名成功奖励：有奖励，用户领取，不需要审批")
    public void activityType9(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
//            Long voucherId=49L;
            //客户限制
            List<Integer> labels = new ArrayList<>();
            List<String> picList = new ArrayList<>();
            SupporterUtil supporterUtil = new SupporterUtil(visitor);
            PublicParameter pp = new PublicParameter();
            picList.add(0, supporterUtil.getPicPath());
            //填写报名所需要信息
            List<Boolean> isShow = new ArrayList<>();
            isShow.add(true);
            isShow.add(true);
            isShow.add(true);
            isShow.add(true);
            isShow.add(true);
            isShow.add(true);
            isShow.add(true);
            List<Boolean> isRequired = new ArrayList<>();
            isRequired.add(true);
            isRequired.add(true);
            isRequired.add(true);
            isRequired.add(true);
            isRequired.add(true);
            isRequired.add(true);
            isRequired.add(true);
            JSONArray registerInformationList = this.businessUtil.getRegisterInformationList(isShow, isRequired);
            //报名成功奖励
            JSONArray registerObject = businessUtil.getRewardVouchers(voucherId, 1, businessUtil.getVoucherSurplusInventory(voucherId));
            //卡券有效期
            JSONObject voucherValid = businessUtil.getVoucherValid(2, null, null, 10);
            //创建招募活动-共有的--基础信息
            ManageRecruitAddScene.ManageRecruitAddSceneBuilder builder = ManageRecruitAddScene.builder()
                    .type(2)
                    .participationLimitType(0)
                    .chooseLabels(labels)
                    .title("招募-有奖励-用户领取-不需审批" )
                    .startDate(businessUtil.getStartDate())
                    .endDate(businessUtil.getEndDate())
                    .applyStart(businessUtil.getStartDate())
                    .applyEnd(businessUtil.getEndDate())
                    .isLimitQuota(true)
                    .quota(10)
                    .subjectType(supporterUtil.getSubjectType())
                    .subjectId(supporterUtil.getSubjectDesc(supporterUtil.getSubjectType()))
                    .label("BARGAIN")
                    .picList(picList)
                    .rule(pp.rule)
                    .registerInformationList(registerInformationList)
                    .successReward(true)
                    .rewardReceiveType(1)
                    .isNeedApproval(false);
            if (true) {
                builder.rewardVouchers(registerObject)
                        .voucherValid(voucherValid);
            }
            IScene scene = builder.build();
            Long activityId = visitor.invokeApi(scene).getLong("id");
            //审批通过招募活动
            businessUtil.getApprovalPassed(activityId);
            Preconditions.checkArgument(activityId>0,"招募活动9创建失败");

        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("招募活动-客户类型不限制，报名成功奖励：有奖励，用户领取，不需要审批");
        }
    }

    /**
     * 招募活动-客户类型不限制，报名成功无奖励，用户领取，需要审批
     * @return
     */
    @Test(description = "招募活动-客户类型不限制，报名成功无奖励，用户领取，需要审批")
    public void activityType10(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
//            Long voucherId=49L;
            //客户限制
            List<Integer> labels = new ArrayList<>();
            List<String> picList = new ArrayList<>();
            SupporterUtil supporterUtil = new SupporterUtil(visitor);
            PublicParameter pp = new PublicParameter();
            picList.add(0, supporterUtil.getPicPath());
            //填写报名所需要信息
            List<Boolean> isShow = new ArrayList<>();
            isShow.add(true);
            isShow.add(true);
            isShow.add(true);
            isShow.add(true);
            isShow.add(true);
            isShow.add(true);
            isShow.add(true);
            List<Boolean> isRequired = new ArrayList<>();
            isRequired.add(true);
            isRequired.add(true);
            isRequired.add(true);
            isRequired.add(true);
            isRequired.add(true);
            isRequired.add(true);
            isRequired.add(true);
            JSONArray registerInformationList = this.businessUtil.getRegisterInformationList(isShow, isRequired);
            //报名成功奖励
            JSONArray registerObject = businessUtil.getRewardVouchers(voucherId, 1, businessUtil.getVoucherSurplusInventory(voucherId));
            //卡券有效期
            JSONObject voucherValid = businessUtil.getVoucherValid(2, null, null, 10);
            //创建招募活动-共有的--基础信息
            ManageRecruitAddScene.ManageRecruitAddSceneBuilder builder = ManageRecruitAddScene.builder()
                    .type(2)
                    .participationLimitType(0)
                    .chooseLabels(labels)
                    .title("招募-无奖励-用户领取-需审批" )
                    .startDate(businessUtil.getStartDate())
                    .endDate(businessUtil.getEndDate())
                    .applyStart(businessUtil.getStartDate())
                    .applyEnd(businessUtil.getEndDate())
                    .isLimitQuota(true)
                    .quota(10)
                    .subjectType(supporterUtil.getSubjectType())
                    .subjectId(supporterUtil.getSubjectDesc(supporterUtil.getSubjectType()))
                    .label("BARGAIN")
                    .picList(picList)
                    .rule(pp.rule)
                    .registerInformationList(registerInformationList)
                    .successReward(false)
                    .rewardReceiveType(1)
                    .isNeedApproval(true);
            if (false) {
                builder.rewardVouchers(registerObject)
                        .voucherValid(voucherValid);
            }
            IScene scene = builder.build();
            Long activityId = visitor.invokeApi(scene).getLong("id");
            //审批通过招募活动
            businessUtil.getApprovalPassed(activityId);
            Preconditions.checkArgument(activityId>0,"招募活动10创建失败");

        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("招募活动-客户类型不限制，报名成功无奖励，用户领取，需要审批");
        }
    }

    /**
     * 招募活动-客户类型不限制，报名成功无奖励，用户领取，不需要审批
     * @return
     */
    @Test(description = "招募活动-客户类型不限制，报名成功无奖励，用户领取，不需要审批")
    public void activityType11(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
//            Long voucherId=49L;
            //客户限制
            List<Integer> labels = new ArrayList<>();
            List<String> picList = new ArrayList<>();
            SupporterUtil supporterUtil = new SupporterUtil(visitor);
            PublicParameter pp = new PublicParameter();
            picList.add(0, supporterUtil.getPicPath());
            //填写报名所需要信息
            List<Boolean> isShow = new ArrayList<>();
            isShow.add(true);
            isShow.add(true);
            isShow.add(true);
            isShow.add(true);
            isShow.add(true);
            isShow.add(true);
            isShow.add(true);
            List<Boolean> isRequired = new ArrayList<>();
            isRequired.add(true);
            isRequired.add(true);
            isRequired.add(true);
            isRequired.add(true);
            isRequired.add(true);
            isRequired.add(true);
            isRequired.add(true);
            JSONArray registerInformationList = this.businessUtil.getRegisterInformationList(isShow, isRequired);
            //报名成功奖励
            JSONArray registerObject = businessUtil.getRewardVouchers(voucherId, 1, businessUtil.getVoucherSurplusInventory(voucherId));
            //卡券有效期
            JSONObject voucherValid = businessUtil.getVoucherValid(2, null, null, 10);
            //创建招募活动-共有的--基础信息
            ManageRecruitAddScene.ManageRecruitAddSceneBuilder builder = ManageRecruitAddScene.builder()
                    .type(2)
                    .participationLimitType(0)
                    .chooseLabels(labels)
                    .title("招募-无奖励-用户领取-不需审批" )
                    .startDate(businessUtil.getStartDate())
                    .endDate(businessUtil.getEndDate())
                    .applyStart(businessUtil.getStartDate())
                    .applyEnd(businessUtil.getEndDate())
                    .isLimitQuota(true)
                    .quota(10)
                    .subjectType(supporterUtil.getSubjectType())
                    .subjectId(supporterUtil.getSubjectDesc(supporterUtil.getSubjectType()))
                    .label("BARGAIN")
                    .picList(picList)
                    .rule(pp.rule)
                    .registerInformationList(registerInformationList)
                    .successReward(false)
                    .rewardReceiveType(1)
                    .isNeedApproval(false);
            if (false) {
                builder.rewardVouchers(registerObject)
                        .voucherValid(voucherValid);
            }
            IScene scene = builder.build();
            Long activityId = visitor.invokeApi(scene).getLong("id");
            //审批通过招募活动
            businessUtil.getApprovalPassed(activityId);
            Preconditions.checkArgument(activityId>0,"招募活动11创建失败");

        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("招募活动-客户类型不限制，报名成功无奖励，用户领取，不需要审批");
        }
    }



    /**
     * 创建裂变活动-客户标签
     */
    @Test(description ="创建裂变活动-客户标签" )
    public void activityType12() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
//            Long voucherId=49L;
            List<Integer> chooseLabels = new ArrayList<>();
            chooseLabels.add(1000);
            chooseLabels.add(1);
            chooseLabels.add(100);
            chooseLabels.add(2000);
            chooseLabels.add(3000);
            String[][] label = {{"PREFERENTIAL", "优惠"}, {"BARGAIN", "特价"}, {"WELFARE", "福利"}, {"RED_PAPER", "红包"}, {"GIFT", "礼品"}, {"SELL_WELL", "热销"}, {"RECOMMEND", "推荐"}};
            for (int i = 0; i < label.length; i++) {
                System.err.println(label.length + "-------" + 1);
                SupporterUtil supporterUtil = new SupporterUtil(visitor);
                PublicParameter pp = new PublicParameter();
                List<String> picList = new ArrayList<>();
                picList.add(supporterUtil.getPicPath());
                // 创建被邀请者和分享者的信息字段
                JSONObject invitedVoucher = businessUtil.getInvitedVoucher(voucherId, 1, String.valueOf(businessUtil.getVoucherSurplusInventory(voucherId)), 2, "", "", 3);
                JSONObject shareVoucher = businessUtil.getShareVoucher(voucherId, 1, String.valueOf(businessUtil.getVoucherSurplusInventory(voucherId)), 2, "", "", 3);
                IScene scene = FissionVoucherAddScene.builder()
                        .type(1)
                        .participationLimitType(1)
                        .chooseLabels(chooseLabels)
                        .receiveLimitType(0)
                        .title("裂变活动标签为-" + label[i][1] + "-" + (int) (Math.random() * 10000))
                        .rule(pp.rule)
                        .startDate(businessUtil.getStartDate())
                        .endDate(businessUtil.getEndDate())
                        .subjectType(supporterUtil.getSubjectType())
                        .subjectId(supporterUtil.getSubjectDesc(supporterUtil.getSubjectType()))
                        .label(label[i][0])
                        .picList(picList)
                        .shareNum("3")
                        .shareVoucher(shareVoucher)
                        .invitedVoucher(invitedVoucher)
                        .build();
                Long activityId = visitor.invokeApi(scene).getLong("id");
                //审批通过招募活动
                businessUtil.getApprovalPassed(activityId);
                Preconditions.checkArgument(activityId > 0, "裂变活动12创建失败");
            }

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("创建裂变活动-参与客户限制为【部分】，部分中的标签选择全部的");
        }
    }


    /**
     * 招募活动，报名信息全为非必填项
     */
    @Test(description = "招募活动，报名信息全为非必填项")
    public void activityType13() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
//            Long voucherId=49L;
            List<Integer> labels = new ArrayList<>();
            labels.add(1000);
            labels.add(1);
            labels.add(100);
            labels.add(2000);
            labels.add(3000);
            List<String> picList = new ArrayList<>();
            SupporterUtil supporterUtil = new SupporterUtil(visitor);
            PublicParameter pp = new PublicParameter();
            picList.add(0, supporterUtil.getPicPath());
            //填写报名所需要信息
            List<Boolean> isShow = new ArrayList<>();
            isShow.add(true);
            isShow.add(true);
            isShow.add(true);
            isShow.add(true);
            isShow.add(true);
            isShow.add(true);
            isShow.add(true);
            List<Boolean> isRequired = new ArrayList<>();
            isRequired.add(false);
            isRequired.add(false);
            isRequired.add(false);
            isRequired.add(false);
            isRequired.add(false);
            isRequired.add(false);
            isRequired.add(false);
            JSONArray registerInformationList = this.businessUtil.getRegisterInformationList(isShow, isRequired);
            //报名成功奖励
            JSONArray registerObject = businessUtil.getRewardVouchers(voucherId, 1, businessUtil.getVoucherSurplusInventory(voucherId));
            //卡券有效期
            JSONObject voucherValid = businessUtil.getVoucherValid(2, null, null, 10);
            //创建招募活动-共有的--基础信息
            ManageRecruitAddScene.ManageRecruitAddSceneBuilder builder = ManageRecruitAddScene.builder()
                    .type(2)
                    .participationLimitType(0)
                    .title("招募-报名信息非必填" + (int) (Math.random() * 10000))
                    .startDate(businessUtil.getStartDate())
                    .endDate(businessUtil.getEndDate())
                    .applyStart(businessUtil.getStartDate())
                    .applyEnd(businessUtil.getEndDate())
                    .isLimitQuota(true)
                    .quota(10)
                    .subjectType(supporterUtil.getSubjectType())
                    .subjectId(supporterUtil.getSubjectDesc(supporterUtil.getSubjectType()))
                    .label("BARGAIN")
                    .picList(picList)
                    .rule(pp.rule)
                    .registerInformationList(registerInformationList)
                    .successReward(true)
                    .rewardReceiveType(0)
                    .isNeedApproval(true);
            if (true) {
                builder.rewardVouchers(registerObject)
                        .voucherValid(voucherValid);
            }
            IScene scene = builder.build();
            Long activityId = visitor.invokeApi(scene).getLong("id");
            //审批通过招募活动
            businessUtil.getApprovalPassed(activityId);
            //小程序报名活动(报名信息不填写)
//            businessUtil.activityRegisterApplet(activityId,"","",1,"","","","");

            Preconditions.checkArgument(activityId > 0, "招募活动13创建失败");
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("招募活动，报名信息全为非必填项");
        }
    }

    /**
     * 招募活动，报名信息为空
     */
    @Test(description = "招募活动，报名信息为空")
    public void activityType14() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
//            Long voucherId=49L;
            List<Integer> labels = new ArrayList<>();
            labels.add(1000);
            List<String> picList = new ArrayList<>();
            SupporterUtil supporterUtil = new SupporterUtil(visitor);
            PublicParameter pp = new PublicParameter();
            picList.add(0, supporterUtil.getPicPath());
            //填写报名所需要信息
            List<Boolean> isShow = new ArrayList<>();
            isShow.add(false);
            isShow.add(false);
            isShow.add(false);
            isShow.add(false);
            isShow.add(false);
            isShow.add(false);
            isShow.add(false);
            List<Boolean> isRequired = new ArrayList<>();
            isRequired.add(false);
            isRequired.add(false);
            isRequired.add(false);
            isRequired.add(false);
            isRequired.add(false);
            isRequired.add(false);
            isRequired.add(false);

            JSONArray registerInformationList = this.businessUtil.getRegisterInformationNullList(isShow, isRequired);
            //报名成功奖励
            JSONArray registerObject = businessUtil.getRewardVouchers(voucherId, 1, businessUtil.getVoucherSurplusInventory(voucherId));
            //卡券有效期
            JSONObject voucherValid = businessUtil.getVoucherValid(2, null, null, 10);
            //创建招募活动-共有的--基础信息
            ManageRecruitAddScene.ManageRecruitAddSceneBuilder builder = ManageRecruitAddScene.builder()
                    .type(2)
                    .participationLimitType(0)
                    .title("招募-报名信息为空" + (int) (Math.random() * 10000))
                    .startDate(businessUtil.getStartDate())
                    .endDate(businessUtil.getEndDate())
                    .applyStart(businessUtil.getStartDate())
                    .applyEnd(businessUtil.getEndDate())
                    .isLimitQuota(true)
                    .quota(10)
                    .subjectType(supporterUtil.getSubjectType())
                    .subjectId(supporterUtil.getSubjectDesc(supporterUtil.getSubjectType()))
                    .label("BARGAIN")
                    .picList(picList)
                    .rule(pp.rule)
                    .registerInformationList(registerInformationList)
                    .successReward(true)
                    .rewardReceiveType(0)
                    .isNeedApproval(true);
            if (true) {
                builder.rewardVouchers(registerObject)
                        .voucherValid(voucherValid);
            }
            IScene scene = builder.build();
            Long activityId = visitor.invokeApi(scene).getLong("id");
            //审批通过招募活动
            businessUtil.getApprovalPassed(activityId);
            //小程序报名
//            businessUtil.activityRegisterApplet(activityId);

            Preconditions.checkArgument(activityId > 0, "招募活动14创建失败");
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("招募活动，报名信息为空");
        }
    }

    /**
     *-----------------------------------小程序活动相关代码校验------------------------------------
     */


    /**
     * 招募活动，报名成功自动发放，小程序中小喇叭中优惠券的状态为已领取
     */
    @Test(enabled = true)
    public void activityVoucherStatus1() {
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            List<Integer> labels = new ArrayList<>();
            labels.add(1000);
            labels.add(1);
            labels.add(100);
            labels.add(2000);
            labels.add(3000);
            List<String> picList = new ArrayList<>();
            SupporterUtil supporterUtil = new SupporterUtil(visitor);
            PublicParameter pp = new PublicParameter();
            picList.add(0, supporterUtil.getPicPath());
            //填写报名所需要信息
            List<Boolean> isShow = new ArrayList<>();
            isShow.add(false);
            isShow.add(false);
            isShow.add(false);
            isShow.add(false);
            isShow.add(false);
            isShow.add(false);
            isShow.add(false);
            List<Boolean> isRequired = new ArrayList<>();
            isRequired.add(false);
            isRequired.add(false);
            isRequired.add(false);
            isRequired.add(false);
            isRequired.add(false);
            isRequired.add(false);
            isRequired.add(false);
            JSONArray registerInformationList = this.businessUtil.getRegisterInformationList(isShow, isRequired);
            //报名成功奖励
            JSONArray registerObject = businessUtil.getRewardVouchers(voucherId, 1, businessUtil.getVoucherSurplusInventory(voucherId));
            //卡券有效期
            JSONObject voucherValid = businessUtil.getVoucherValid(2, null, null, 10);
            //创建招募活动-共有的--基础信息
            ManageRecruitAddScene.ManageRecruitAddSceneBuilder builder = ManageRecruitAddScene.builder()
                    .type(2)
                    .participationLimitType(0)
                    .title("招募-报名信息非必填" + (int) (Math.random() * 10000))
                    .startDate(businessUtil.getStartDate())
                    .endDate(businessUtil.getEndDate())
                    .applyStart(businessUtil.getStartDate())
                    .applyEnd(businessUtil.getEndDate())
                    .isLimitQuota(true)
                    .quota(10)
                    .subjectType(supporterUtil.getSubjectType())
                    .subjectId(supporterUtil.getSubjectDesc(supporterUtil.getSubjectType()))
                    .label("BARGAIN")
                    .picList(picList)
                    .rule(pp.rule)
                    .registerInformationList(registerInformationList)
                    .successReward(true)
                    .rewardReceiveType(0)
                    .isNeedApproval(true);
            if (true) {
                builder.rewardVouchers(registerObject)
                        .voucherValid(voucherValid);
            }
            IScene scene = builder.build();
            Long activityId = visitor.invokeApi(scene).getLong("id");
            //审批通过招募活动
            businessUtil.getApprovalPassed(activityId);
            //小程序报名活动(报名信息不填写)
            businessUtil.activityRegisterApplet(activityId,"","",1,"","","","");
            jc.pcLogin(pp.phone,pp.password);
            //获取报名管理中的信息
            IScene scene3= ManageRegisterPageScene.builder().page(1).size(10).activityId(activityId).build();
            Long registerId=visitor.invokeApi(scene3).getJSONArray("list").getJSONObject(0).getLong("id");
            System.out.println("---------registerId:"+registerId);
            //报名审批通过
            businessUtil.getRegisterApprovalPassed(activityId,registerId);
            //登录小程序
            user.loginApplet(EnumAppletToken.JC_GLY_ONLINE);
            //查看小程序中此活动对应的小喇叭中的卡券的状态
            String isReceived=businessUtil.articleVoucher(activityId);
            System.err.println("----------------"+isReceived);
            Preconditions.checkArgument(isReceived.equals("true"));
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("招募活动，报名成功自动发放，小程序中小喇叭中优惠券的状态为已领取");
        }
    }
    
    /**
     * 小程序报名审批并通过，卡券为手动领取，卡券状态为未领取，再次领取卡券，卡券的状态为，已领取
     */
    @Test(enabled = true)
    public void activityVoucherStatus2() {
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            List<Integer> labels = new ArrayList<>();
            labels.add(1000);
            labels.add(1);
            labels.add(100);
            labels.add(2000);
            labels.add(3000);
            List<String> picList = new ArrayList<>();
            SupporterUtil supporterUtil = new SupporterUtil(visitor);
            PublicParameter pp = new PublicParameter();
            picList.add(0, supporterUtil.getPicPath());
            //填写报名所需要信息
            List<Boolean> isShow = new ArrayList<>();
            isShow.add(false);
            isShow.add(false);
            isShow.add(false);
            isShow.add(false);
            isShow.add(false);
            isShow.add(false);
            isShow.add(false);
            List<Boolean> isRequired = new ArrayList<>();
            isRequired.add(false);
            isRequired.add(false);
            isRequired.add(false);
            isRequired.add(false);
            isRequired.add(false);
            isRequired.add(false);
            isRequired.add(false);
            JSONArray registerInformationList = this.businessUtil.getRegisterInformationList(isShow, isRequired);
            //报名成功奖励
            JSONArray registerObject = businessUtil.getRewardVouchers(voucherId, 1, businessUtil.getVoucherSurplusInventory(voucherId));
            //卡券有效期
            JSONObject voucherValid = businessUtil.getVoucherValid(2, null, null, 10);
            //创建招募活动-共有的--基础信息
            ManageRecruitAddScene.ManageRecruitAddSceneBuilder builder = ManageRecruitAddScene.builder()
                    .type(2)
                    .participationLimitType(0)
                    .title("招募-报名信息非必填" + (int) (Math.random() * 10000))
                    .startDate(businessUtil.getStartDate())
                    .endDate(businessUtil.getEndDate())
                    .applyStart(businessUtil.getStartDate())
                    .applyEnd(businessUtil.getEndDate())
                    .isLimitQuota(true)
                    .quota(10)
                    .subjectType(supporterUtil.getSubjectType())
                    .subjectId(supporterUtil.getSubjectDesc(supporterUtil.getSubjectType()))
                    .label("BARGAIN")
                    .picList(picList)
                    .rule(pp.rule)
                    .registerInformationList(registerInformationList)
                    .successReward(true)
                    .rewardReceiveType(1)
                    .isNeedApproval(true);
            if (true) {
                builder.rewardVouchers(registerObject)
                        .voucherValid(voucherValid);
            }
            IScene scene = builder.build();
            Long activityId = visitor.invokeApi(scene).getLong("id");
            //审批通过招募活动
            businessUtil.getApprovalPassed(activityId);
            //小程序报名活动(报名信息不填写)
            businessUtil.activityRegisterApplet(activityId,"","",1,"","","","");
            jc.pcLogin(pp.phone,pp.password);
            //获取报名管理中的信息
            IScene scene3=ManageRegisterPageScene.builder().page(1).size(10).activityId(activityId).build();
            Long registerId=visitor.invokeApi(scene3).getJSONArray("list").getJSONObject(0).getLong("id");
            System.out.println("---------registerId:"+registerId);
            //报名审批通过
            businessUtil.getRegisterApprovalPassed(activityId,registerId);
            //登录小程序
            user.loginApplet(EnumAppletToken.JC_GLY_ONLINE);
            //查看小程序中此活动对应的小喇叭中的卡券的状态
            String isReceivedBefoer=businessUtil.articleVoucher(activityId);
            System.err.println("----------------"+isReceivedBefoer);
            //小程序中手动领取优惠券
            Long id=businessUtil.appointmentActivityId(activityId);
            //获取小程序中的卡券ID
            long vId=businessUtil.articleVoucherData(activityId).getJSONArray("list").getJSONObject(0).getLong("id");
            IScene scene4= ArticleVoucherReceiveScene.builder().articleId(id).voucherId(vId).build();
            String message=visitor.invokeApi(scene4,false).getString("message");
            String isReceivedAfter=businessUtil.articleVoucher(activityId);
            System.out.println(isReceivedAfter+"--------"+message);
            Preconditions.checkArgument(isReceivedBefoer.equals("false")&&isReceivedAfter.equals("true"));
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("小程序报名审批并通过，卡券为手动领取，卡券状态为未领取，再次领取卡券，卡券的状态为，已领取");
        }
    }







}
