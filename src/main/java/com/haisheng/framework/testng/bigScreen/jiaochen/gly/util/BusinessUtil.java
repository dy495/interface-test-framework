package com.haisheng.framework.testng.bigScreen.jiaochen.gly.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumAppletToken;
import com.haisheng.framework.testng.bigScreen.jiaochen.ScenarioUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.activity.ActivityApprovalStatusEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.activity.ActivityStatusEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.activity.RegisterInfoEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing.VoucherStatusEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing.VoucherTypeEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.generate.voucher.VoucherGenerator;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.activity.*;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.activity.*;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.file.FileUploadScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.vouchermanage.VoucherDetailScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.vouchermanage.VoucherFormVoucherPageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.util.SceneUtil;
import com.haisheng.framework.util.DateTimeUtil;
import com.haisheng.framework.util.ImageUtil;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class BusinessUtil {
    private final VisitorProxy visitor;
    private final SceneUtil user;

    public BusinessUtil(VisitorProxy visitor) {
        this.visitor = visitor;
        this.user = new SceneUtil(visitor);
    }

    ScenarioUtil jc = new ScenarioUtil();
    PublicParameter pp = new PublicParameter();
    public String shopId = "-1";


    /**
     * @description :创建裂变活动-分享者奖励
     * @date :2021/1/22
     **/
    /**
     * 创建裂变活动-分享者奖励
     **/
    public JSONObject getShareVoucher(Long id, int type, String num, Integer expireType, String voucherStart, String voucherEnd, Integer voucherEffectiveDays) {
        JSONObject shareVoucher = new JSONObject();
        shareVoucher.put("id", id);
        shareVoucher.put("type", type);
        shareVoucher.put("num", num);
        JSONObject voucherValid = new JSONObject();
        voucherValid.put("expire_type", expireType);
        if (expireType == 1) {
            voucherValid.put("voucher_start", voucherStart);
            voucherValid.put("voucher_end", voucherEnd);
        } else {
            voucherValid.put("voucher_effective_days", voucherEffectiveDays);
        }
        shareVoucher.put("voucher_valid", voucherValid);
        return shareVoucher;
    }

    public void ss() {
        System.err.println(pp.fissionVoucherName);
    }

    /**
     * 创建裂变活动-被邀请者奖励
     **/
    public JSONObject getInvitedVoucher(Long id, int type, String num, Integer expireType, String voucherStart, String voucherEnd, Integer voucherEffectiveDays) {
        JSONObject invitedVoucher = new JSONObject();
        invitedVoucher.put("id", id);
        invitedVoucher.put("type", type);
        invitedVoucher.put("num", num);
        JSONObject voucherValid = new JSONObject();
        voucherValid.put("expire_type", expireType);
        if (expireType == 1) {
            voucherValid.put("voucher_start", voucherStart);
            voucherValid.put("voucher_end", voucherEnd);
        } else if (expireType == 2) {
            voucherValid.put("voucher_effective_days", voucherEffectiveDays);
        }
        invitedVoucher.put("voucher_valid", voucherValid);
        return invitedVoucher;
    }

    /**
     * @description :创建招募活动-报名所需信息
     * @date :2021/1/24
     **/
    public JSONArray getRegisterInformationList(List<Boolean> isShow, List<Boolean> isRequired) {
        JSONArray array = new JSONArray();
        for (int i = 0; i < RegisterInfoEnum.values().length; i++) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("type", RegisterInfoEnum.values()[i].getId());
            jsonObject.put("name", RegisterInfoEnum.values()[i].getName());
            jsonObject.put("is_show", isShow.get(i));
            jsonObject.put("is_required", isRequired.get(i));
            array.add(jsonObject);
        }
        return array;
    }

    /**
     * @description :创建招募活动-报名所需信息为空
     * @date :2021/1/24
     **/
    public JSONArray getRegisterInformationNullList(List<Boolean> isShow, List<Boolean> isRequired) {
        JSONArray array = new JSONArray();
        return array;
    }

    /**
     * @description :创建招募活动-卡券奖励
     * @date :2021/1/24
     **/
    public JSONArray getRewardVouchers(Long id, int type, int num) {
        JSONArray rewardVouchers = new JSONArray();
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("type", type);
        object.put("num", num);
        rewardVouchers.add(object);
        return rewardVouchers;
    }

    /**
     * @description :创建招募活动-卡券奖励
     * @date :2021/1/24
     **/
    public JSONArray getRewardVouchers(Long id, int type, String num) {
        JSONArray rewardVouchers = new JSONArray();
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("type", type);
        object.put("num", num);
        rewardVouchers.add(object);
        return rewardVouchers;
    }

    /**
     * 获取随机4位数
     */
    public int randomNumber() {
        int number = (int) (Math.random() * 10000);
        return number;
    }

    /**
     * 创建招募活动-奖励有效期
     *
     * @param expireType           卡券有效期类型 1：时间段，2：有效天数
     * @param voucherStart         卡券有效开始日期 卡券有效期类型为1（时间段）必填
     * @param voucherEnd           卡券有效结束日期 卡券有效期类型为1（时间段）必填
     * @param voucherEffectiveDays 卡券有效天数 卡券有效期类型为2（有效天数）必填
     **/
    public JSONObject getVoucherValid(int expireType, String voucherStart, String voucherEnd, int voucherEffectiveDays) {
        JSONObject voucherValid = new JSONObject();
        voucherValid.put("expire_type", expireType);
        if (expireType == 1) {
            voucherValid.put("voucher_start", voucherStart);
            voucherValid.put("voucher_end", voucherEnd);
        } else {
            voucherValid.put("voucher_effective_days", voucherEffectiveDays);
        }
        return voucherValid;
    }


    /**
     * 获取当前时间
     */
    public String getStartDate() {
        // 格式化时间
        return DateTimeUtil.getFormat(new Date(), "yyyy-MM-dd HH:mm");
    }

    /**
     * 获取当前时间+10天
     */
    public String getEndDate() {
        Date endDate = DateTimeUtil.addDay(new Date(), 10);
        return DateTimeUtil.getFormat(endDate, "yyyy-MM-dd HH:mm");
    }

    /**
     * 获取当前时间
     */
    public String getDateTime(int day) {
        Date endDate = DateTimeUtil.addDay(new Date(), day);
        return DateTimeUtil.getFormat(endDate, "yyyy-MM-dd HH:mm");
    }


    /**
     * 配置可用库存
     */
    public int getVoucherAllowUseInventory(Long voucherId) {
        SceneUtil su = new SceneUtil(visitor);
        int allowUseInventory = su.getVoucherPage(voucherId).getAllowUseInventory();
        return (int) Math.min((allowUseInventory == 1 ? allowUseInventory : allowUseInventory - 1), 30L);

    }

    /**
     * 判断可用库存
     */
    public Long getVoucherAllowUseInventoryNum(Long voucherId) {
        SceneUtil su = new SceneUtil(visitor);
        int allowUseInventory = su.getVoucherPage(voucherId).getAllowUseInventory();
        return (long) allowUseInventory;

    }


    /**
     * 获取进行中的优惠券
     */
    public Long getVoucherId() {
        return new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
    }

    /**
     * 获取进行中的优惠券合集
     */
    public List<Long> getWaitingWorkingVoucherIds() {
        List<Long> voucherIds = new ArrayList<>();
        IScene scene = VoucherFormVoucherPageScene.builder().page(1).size(10).build();
        JSONObject response = scene.visitor(visitor).execute();
        int pages = response.getInteger("pages");
        for (int page = 1; page <= pages; page++) {
            IScene scene1 = VoucherFormVoucherPageScene.builder().page(page).size(10).build();
            JSONArray list = scene1.visitor(visitor).execute().getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                Long voucherId = list.getJSONObject(i).getLong("id");
                String voucherStatusName = list.getJSONObject(i).getString("voucher_status_name");
                if (voucherStatusName.equals(VoucherStatusEnum.WORKING.getName())) {
                    voucherIds.add(voucherId);
                }
            }
        }
        return voucherIds;
    }

    /**
     * 获取待审批的优惠券合集
     */
    public List<Long> getVoucherIds() {
        SceneUtil su = new SceneUtil(visitor);
        List<Long> voucherIds = new ArrayList<>();
        IScene scene = VoucherFormVoucherPageScene.builder().page(1).size(10).build();
        JSONObject response = scene.visitor(visitor).execute();
        int pages = response.getInteger("pages");
        for (int page = 1; page <= pages; page++) {
            IScene scene1 = VoucherFormVoucherPageScene.builder().page(page).size(10).build();
            JSONArray list = scene1.visitor(visitor).execute().getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                Long voucherId = list.getJSONObject(i).getLong("id");
                String voucherStatus = list.getJSONObject(i).getString("audit_status_name");
                String invalidStatusName = list.getJSONObject(i).getString("invalid_status_name");
                String voucherName = list.getJSONObject(i).getString("voucher_name");
                System.err.println(voucherId + "---------" + voucherStatus + "---------" + invalidStatusName);
                if (voucherStatus.equals("审核中") && invalidStatusName.equals("待审核")) {
                    voucherIds.add(voucherId);
                }
            }
        }
        return voucherIds;
    }

    /**
     * 构建裂变活动
     *
     * @param voucherId 包含卡券信息
     * @return IScene
     */
    public IScene createFissionActivityScene(Long voucherId) {
        SceneUtil supporterUtil = new SceneUtil(visitor);
        PublicParameter pp = new PublicParameter();
        List<String> picList = new ArrayList<>();
        picList.add(getPicPath());
        IScene scene = null;
        int AllowUseInventory = getVoucherAllowUseInventory(voucherId);
        if (AllowUseInventory > 2) {
            // 创建被邀请者和分享者的信息字段
            JSONObject invitedVoucher = getInvitedVoucher(voucherId, 1, String.valueOf(Math.min(getVoucherAllowUseInventory(voucherId), 1)), 2, "", "", 3);
            JSONObject shareVoucher = getShareVoucher(voucherId, 1, String.valueOf(Math.min(getVoucherAllowUseInventory(voucherId), 1)), 2, "", "", 3);
            scene = FissionVoucherAddScene.builder()
                    .type(1)
                    .participationLimitType(0)
                    .receiveLimitType(0)
                    .title(pp.fissionVoucherName)
                    .rule(pp.rule)
                    .startDate(getStartDate())
                    .endDate(getEndDate())
                    .subjectType(supporterUtil.getSubjectType())
                    .subjectId(supporterUtil.getSubjectDesc(supporterUtil.getSubjectType()))
                    .label("CAR_WELFARE")
                    .picList(picList)
                    .shareNum("3")
                    .shareVoucher(shareVoucher)
                    .invitedVoucher(invitedVoucher)
                    .isCustomShareInfo(false)
                    .build();
        } else {
            //创建卡券
            Long voucherId3 = supporterUtil.createVoucherId(1000, VoucherTypeEnum.COUPON);
            //获取卡券的名字
            String voucherName = supporterUtil.getVoucherName(voucherId3);
            //审批通过
            supporterUtil.applyVoucher(voucherName, "1");
            //创建活动
            // 创建被邀请者和分享者的信息字段
            JSONObject invitedVoucher = getInvitedVoucher(voucherId3, 1, String.valueOf(Math.min(getVoucherAllowUseInventory(voucherId), 1)), 2, "", "", 3);
            JSONObject shareVoucher = getShareVoucher(voucherId3, 1, String.valueOf(Math.min(getVoucherAllowUseInventory(voucherId), 1)), 2, "", "", 3);
            scene = FissionVoucherAddScene.builder()
                    .type(1)
                    .participationLimitType(0)
                    .receiveLimitType(0)
                    .title(pp.fissionVoucherName)
                    .rule(pp.rule)
                    .startDate(getStartDate())
                    .endDate(getEndDate())
                    .subjectType(supporterUtil.getSubjectType())
                    .subjectId(supporterUtil.getSubjectDesc(supporterUtil.getSubjectType()))
                    .label("CAR_WELFARE")
                    .picList(picList)
                    .shareNum("3")
                    .shareVoucher(shareVoucher)
                    .invitedVoucher(invitedVoucher)
                    .isCustomShareInfo(false)
                    .build();

        }

        return scene;

    }

    /**
     * 创建裂变活动
     */
    public Long createFissionActivity(Long voucherId) {
        IScene scene = createFissionActivityScene(voucherId);
        Long activityId = scene.visitor(visitor).execute().getLong("id");
        return activityId;
    }

    /**
     * 创建招募活动--需要审批的活动
     *
     * @return 活动id
     */
    public Long createRecruitActivityApproval() {
        Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
        return createRecruitActivity(voucherId, true, 0, true);
    }

    /**
     * 创建招募活动--不需要审批的活动
     *
     * @return 活动id
     */
    public Long createRecruitActivityNotApproval() {
        Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
        return createRecruitActivity(voucherId, true, 0, false);
    }

    /**
     * 活动管理-创建招募活动--需要审批的活动
     *
     * @param voucherId         奖励卡券信息
     * @param successReward     是否包含奖励
     * @param rewardReceiveType 奖励领取方式 0：自动发放，1：主动领取
     * @param isNeedApproval    报名后是否需要审批
     */
    public Long createRecruitActivity(Long voucherId, boolean successReward, int rewardReceiveType, boolean isNeedApproval) {
        IScene scene = createRecruitActivityScene(voucherId, successReward, rewardReceiveType, isNeedApproval);
        return scene.visitor(visitor).execute().getLong("id");
    }

    /**
     * 活动管理-创建招募活动--创建未开始的活动
     * 2021-3-17
     */
    public Long createRecruitActivity() {
        Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
        IScene scene = createRecruitActivityScene(voucherId, true, 0, true, getEndDate(), getEndDate());
        return scene.visitor(visitor).execute().getLong("id");
    }

    /**
     * 活动管理-创建招募活动
     *
     * @param voucherId         奖励卡券信息
     * @param successReward     是否包含奖励
     * @param rewardReceiveType 奖励领取方式 0：自动发放，1：主动领取
     * @param isNeedApproval    报名后是否需要审批
     */
    public IScene createRecruitActivityScene(Long voucherId, boolean successReward, int rewardReceiveType, boolean isNeedApproval) {
        List<String> picList = new ArrayList<>();
        SceneUtil supporterUtil = new SceneUtil(visitor);
        picList.add(0, getPicPath());
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
        JSONArray registerInformationList = this.getRegisterInformationList(isShow, isRequired);
        //判断可用库存
        int AllowUseInventory = getVoucherAllowUseInventory(voucherId);
        ManageRecruitAddScene.ManageRecruitAddSceneBuilder builder = null;
        if (AllowUseInventory > 0) {
            //报名成功奖励
            JSONArray registerObject = getRewardVouchers(voucherId, 1, Math.toIntExact(AllowUseInventory));
            //卡券有效期
            JSONObject voucherValid = getVoucherValid(2, null, null, 10);
            //创建招募活动-共有的--基础信息
            builder = ManageRecruitAddScene.builder()
                    .type(2)
                    .participationLimitType(0)
                    .title(pp.RecruitName)
                    .startDate(getStartDate())
                    .endDate(getEndDate())
                    .applyStart(getStartDate())
                    .applyEnd(getEndDate())
                    .isLimitQuota(true)
                    .quota(10)
                    .subjectType(supporterUtil.getSubjectType())
                    .subjectId(supporterUtil.getSubjectDesc(supporterUtil.getSubjectType()))
                    .label("CAR_WELFARE")
                    .picList(picList)
                    .rule(pp.rule)
                    .registerInformationList(registerInformationList)
                    .successReward(successReward)
                    .rewardReceiveType(rewardReceiveType)
                    .isNeedApproval(isNeedApproval);
            if (successReward) {
                builder.rewardVouchers(registerObject)
                        .voucherValid(voucherValid);

            }
        } else {
            //创建卡券
            Long voucherId3 = supporterUtil.createVoucherId(1000, VoucherTypeEnum.COUPON);
            //获取卡券的名字
            String voucherName = supporterUtil.getVoucherName(voucherId3);
            //审批通过
            supporterUtil.applyVoucher(voucherName, "1");
            //报名成功奖励
            JSONArray registerObject = getRewardVouchers(voucherId3, 1, getVoucherAllowUseInventory(voucherId));
            //卡券有效期
            JSONObject voucherValid = getVoucherValid(2, null, null, 10);
            //创建招募活动-共有的--基础信息
            builder = ManageRecruitAddScene.builder()
                    .type(2)
                    .participationLimitType(0)
                    .title(pp.RecruitName)
                    .startDate(getStartDate())
                    .endDate(getEndDate())
                    .applyStart(getStartDate())
                    .applyEnd(getEndDate())
                    .isLimitQuota(true)
                    .quota(10)
                    .subjectType(supporterUtil.getSubjectType())
                    .subjectId(supporterUtil.getSubjectDesc(supporterUtil.getSubjectType()))
                    .label("CAR_WELFARE")
                    .picList(picList)
                    .rule(pp.rule)
                    .registerInformationList(registerInformationList)
                    .successReward(successReward)
                    .rewardReceiveType(rewardReceiveType)
                    .isNeedApproval(isNeedApproval);
            if (successReward) {
                builder.rewardVouchers(registerObject)
                        .voucherValid(voucherValid);

            }
        }

        return builder.build();
    }

    /**
     * 活动管理-创建招募活动
     *
     * @param voucherId         奖励卡券信息
     * @param successReward     是否包含奖励
     * @param rewardReceiveType 奖励领取方式 0：自动发放，1：主动领取
     * @param isNeedApproval    报名后是否需要审批
     */
    public IScene createRecruitActivityScene(Long voucherId, boolean successReward, int rewardReceiveType, boolean isNeedApproval, String startTime, String endTime) {
        List<String> picList = new ArrayList<>();
        SceneUtil supporterUtil = new SceneUtil(visitor);
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
        JSONArray registerInformationList = this.getRegisterInformationList(isShow, isRequired);
        //报名成功奖励
        JSONArray registerObject = getRewardVouchers(voucherId, 1, getVoucherAllowUseInventory(voucherId));
        //卡券有效期
        JSONObject voucherValid = getVoucherValid(2, null, null, 10);
        //创建招募活动-共有的--基础信息
        ManageRecruitAddScene.ManageRecruitAddSceneBuilder builder = ManageRecruitAddScene.builder()
                .type(2)
                .participationLimitType(0)
                .title(pp.RecruitName)
                .startDate(startTime)
                .endDate(endTime)
                .applyStart(getStartDate())
                .applyEnd(getEndDate())
                .isLimitQuota(true)
                .quota(10)
                .subjectType(supporterUtil.getSubjectType())
                .subjectId(supporterUtil.getSubjectDesc(supporterUtil.getSubjectType()))
                .label("CAR_WELFARE")
                .picList(picList)
                .rule(pp.rule)
                .registerInformationList(registerInformationList)
                .successReward(successReward)
                .rewardReceiveType(rewardReceiveType)
                .isNeedApproval(isNeedApproval);
        if (successReward) {
            builder.rewardVouchers(registerObject)
                    .voucherValid(voucherValid);

        }
        return builder.build();
    }

    /**
     * 活动管理-创建招募活动
     *
     * @param voucherId         奖励卡券信息
     * @param successReward     是否包含奖励
     * @param rewardReceiveType 奖励领取方式 0：自动发放，1：主动领取
     * @param isNeedApproval    报名后是否需要审批
     */
    public IScene createRecruitActivityScene(Long voucherId, boolean successReward, int rewardReceiveType, boolean isNeedApproval, Boolean type) {
        List<String> picList = new ArrayList<>();
        SceneUtil supporterUtil = new SceneUtil(visitor);
        PublicParameter pp = new PublicParameter();
        picList.add(0, supporterUtil.getPicPath());
        //填写报名所需要信息
        List<Boolean> isShow = new ArrayList<>();
        isShow.add(type);
        isShow.add(type);
        isShow.add(type);
        isShow.add(type);
        isShow.add(type);
        isShow.add(type);
        isShow.add(type);
        List<Boolean> isRequired = new ArrayList<>();
        isRequired.add(type);
        isRequired.add(type);
        isRequired.add(type);
        isRequired.add(type);
        isRequired.add(type);
        isRequired.add(type);
        isRequired.add(type);
        JSONArray registerInformationList = this.getRegisterInformationList(isShow, isRequired);
        //报名成功奖励
        JSONArray registerObject = getRewardVouchers(voucherId, 1, getVoucherAllowUseInventory(voucherId));
        //卡券有效期
        JSONObject voucherValid = getVoucherValid(2, null, null, 10);
        //创建招募活动-共有的--基础信息
        ManageRecruitAddScene.ManageRecruitAddSceneBuilder builder = ManageRecruitAddScene.builder()
                .type(2)
                .participationLimitType(0)
                .title(pp.RecruitName)
                .startDate(getStartDate())
                .endDate(getEndDate())
                .applyStart(getStartDate())
                .applyEnd(getEndDate())
                .isLimitQuota(true)
                .quota(10)
                .subjectType(supporterUtil.getSubjectType())
                .subjectId(supporterUtil.getSubjectDesc(supporterUtil.getSubjectType()))
                .label("CAR_WELFARE")
                .picList(picList)
                .rule(pp.rule)
                .registerInformationList(registerInformationList)
                .successReward(successReward)
                .rewardReceiveType(rewardReceiveType)
                .isNeedApproval(isNeedApproval);
        if (successReward) {
            builder.rewardVouchers(registerObject)
                    .voucherValid(voucherValid);

        }
        return builder.build();
    }

    /**
     * 编辑招募活动
     */
    public String activityEditScene(Long id) {
        //获取一个卡券
        Long voucherId = getVoucherId();
        List<String> picList = new ArrayList<>();
        SceneUtil supporterUtil = new SceneUtil(visitor);
        PublicParameter pp = new PublicParameter();
        picList.add(getPicturePath());
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
        JSONArray registerInformationList = getRegisterInformationList(isShow, isRequired);
        //报名成功奖励
        JSONArray registerObject = getRewardVouchers(voucherId, 1, getVoucherAllowUseInventory(voucherId));
        //卡券有效期
        JSONObject voucherValid = getVoucherValid(2, "", "", 10);
        IScene scene = ManageRecruitEditScene.builder()
                .type(2)
                .id(id)
                .title(pp.editTitle)
                .rule(pp.EditRule)
                .participationLimitType(0)
                .rule(pp.EditRule)
                .startDate(getStartDate())
                .endDate(getEndDate())
                .subjectType(supporterUtil.getSubjectType())
                .subjectId(supporterUtil.getSubjectDesc(supporterUtil.getSubjectType()))
                .label("CAR_WELFARE")
                .picList(picList)
                .applyStart(getStartDate())
                .applyEnd(getEndDate())
                .isLimitQuota(true)
                .quota(2)
                .address(pp.address)
                .registerInformationList(registerInformationList)
                .successReward(true)
                .rewardReceiveType(0)
                .isNeedApproval(true)
                .rewardVouchers(registerObject)
                .voucherValid(voucherValid)
                .build();
        String message = scene.visitor(visitor).getResponse().getMessage();
        return message;
    }

    /**
     * 编辑裂变活动
     */
    public IScene fissionActivityEditScene(Long activityId) {
        Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
        SceneUtil supporterUtil = new SceneUtil(visitor);
        PublicParameter pp = new PublicParameter();
        List<String> picList = new ArrayList<>();
        picList.add(getPicturePath());
        // 创建被邀请者和分享者的信息字段
        JSONObject invitedVoucher = getInvitedVoucher(voucherId, 1, String.valueOf(getVoucherAllowUseInventory(voucherId)), 2, "", "", 1);
        JSONObject shareVoucher = getShareVoucher(voucherId, 1, String.valueOf(getVoucherAllowUseInventory(voucherId)), 2, "", "", 1);
        //编辑裂变活动
        return FissionVoucherEditScene.builder()
                .id(activityId)
                .type(1)
                .participationLimitType(0)
                .receiveLimitType(0)
                .title(pp.fissionVoucherNameEdit)
                .rule(pp.EditFissionRule)
                .startDate(getStartDate())
                .endDate(getEndDate())
                .subjectType(supporterUtil.getSubjectType())
                .subjectId(supporterUtil.getSubjectDesc(supporterUtil.getSubjectType()))
                .label("CAR_WELFARE")
                .picList(picList)
                .shareNum("3")
                .shareVoucher(shareVoucher)
                .invitedVoucher(invitedVoucher)
                .build();

    }

    /**
     * 获取图片地址
     *
     * @return 图片地址
     */
    public String getPicturePath() {
        String path = "src/main/java/com/haisheng/framework/testng/bigScreen/jiaochen/wm/multimedia/picture/banner-1.jpg";
        return getPicPath(path);
    }

    public String getPicPath(String picPath) {
        return getPicPath(picPath, "3:2");
    }

    public String getPicPath(String picPath, String ratioStr) {
        String picture = new ImageUtil().getImageBinary(picPath);
        String[] strings = ratioStr.split(":");
        double ratio = BigDecimal.valueOf(Double.parseDouble(strings[0]) / Double.parseDouble(strings[1])).divide(new BigDecimal(1), 4, BigDecimal.ROUND_HALF_UP).doubleValue();
        IScene scene = FileUploadScene.builder().isPermanent(false).permanentPicType(0).pic(picture).ratioStr(ratioStr).ratio(ratio).build();
        return scene.visitor(visitor).execute().getString("pic_path");
    }


    /**
     * 获取图片地址
     *
     * @return 图片地址
     */
    public String getPicPath() {
        String path = "src/main/java/com/haisheng/framework/testng/bigScreen/jiaochen/wm/multimedia/picture/banner-1.jpg";
        String picture = new ImageUtil().getImageBinary(path);
        IScene scene = FileUploadScene.builder().isPermanent(false).permanentPicType(0).pic(picture).ratio(1.5).build();
        return scene.visitor(visitor).execute().getString("pic_path");
    }

    /**
     * 获取优惠券的库存
     */
    public String getSurplusInventory(Long id) {
        IScene scene1 = VoucherFormVoucherPageScene.builder().page(1).size(10).build();
        int pages = scene1.visitor(visitor).execute().getInteger("pages");
        String surplusInventory = "";
        for (int page = 1; page <= pages; page++) {
            IScene scene2 = VoucherFormVoucherPageScene.builder().page(page).size(10).build();
            JSONArray list = scene2.visitor(visitor).execute().getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                Long voucherId = list.getJSONObject(i).getLong("voucher_id");
                if (voucherId.equals(id)) {
                    surplusInventory = list.getJSONObject(i).getString("allow_use_inventory");
                    break;
                }
            }
        }
        return surplusInventory;
    }

    /**
     * 获取优惠券的面值
     */
    public String getPrice(Long id) {
        IScene scene = VoucherDetailScene.builder().id(id).build();
        String parValue = scene.visitor(visitor).execute().getString("par_value");
        return parValue;
    }


    /**
     * 获取优惠券的成本
     */
    public String getCost(Long id) {
        IScene scene = VoucherDetailScene.builder().id(id).build();
        String cost = scene.visitor(visitor).execute().getString("cost");
        return cost;
    }


/**
 * --------------------------------活动列表中活动状态--------------------------------------------
 */
    /**
     * 查询列表中的状态为【待审核的ID】
     */
    public List<Long> getActivityWaitingApproval() {
        List<Long> ids = new ArrayList<>();
        //活动列表
        IScene scene = ActivityManagePageScene.builder().page(1).size(10).build();
        int pages = scene.visitor(visitor).execute().getInteger("pages");
        for (int page = 1; page <= pages; page++) {
            IScene scene1 = ActivityManagePageScene.builder().page(page).size(10).build();
            JSONArray list = scene1.visitor(visitor).execute().getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                int status = list.getJSONObject(i).getInteger("status");

                if (status == ActivityStatusEnum.PENDING.getId()) {
                    Long id = list.getJSONObject(i).getLong("id");
                    ids.add(id);
                }
            }
        }
        if (ids.size() == 0) {
            Long id1 = createRecruitActivityApproval();
            ids.add(id1);
        }
        return ids;
    }

    /**
     * 查询列表中的状态为【待审核的ID】---招募活动
     */
    public List<Long> getRecruitActivityWaitingApproval() {
        List<Long> ids = new ArrayList<>();
        //活动列表
        IScene scene = ActivityManagePageScene.builder().page(1).size(10).build();
        int pages = scene.visitor(visitor).execute().getInteger("pages");
        for (int page = 1; page <= pages; page++) {
            IScene scene1 = ActivityManagePageScene.builder().page(page).size(10).build();
            JSONArray list = scene1.visitor(visitor).execute().getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                int status = list.getJSONObject(i).getInteger("status");
                int activityType = list.getJSONObject(i).getInteger("activity_type");
                if (status == ActivityStatusEnum.PENDING.getId() && activityType == 2) {
                    Long id = list.getJSONObject(i).getLong("id");
                    ids.add(id);
                }
            }
        }
        if (ids.size() == 0) {
            Long id1 = createRecruitActivityApproval();
            ids.add(id1);
        }
        return ids;
    }

    /**
     * 查询列表中的状态为【待审核的ID】---裂变活动
     */
    public List<Long> getFissionActivityWaitingApproval() {
        List<Long> ids = new ArrayList<>();
        //活动列表
        IScene scene = ActivityManagePageScene.builder().page(1).size(10).build();
        int pages = scene.visitor(visitor).execute().getInteger("pages");
        for (int page = 1; page <= pages; page++) {
            IScene scene1 = ActivityManagePageScene.builder().page(page).size(10).build();
            JSONArray list = scene1.visitor(visitor).execute().getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                int status = list.getJSONObject(i).getInteger("status");
                int activityType = list.getJSONObject(i).getInteger("activity_type");
                if (status == ActivityStatusEnum.PENDING.getId() && activityType == 1) {
                    Long id = list.getJSONObject(i).getLong("id");
                    ids.add(id);
                }
            }
        }
        if (ids.size() == 0) {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            Long id1 = createFissionActivity(voucherId);
            ids.add(id1);
        }
        return ids;
    }

    /**
     * 查询活动列表中的状态为【进行中的ID】
     */
    public List<Long> getActivityWorking() {
        List<Long> ids = new ArrayList<>();

        //活动列表
        IScene scene = ActivityManagePageScene.builder().page(1).size(10).build();
        int pages = scene.visitor(visitor).execute().getInteger("pages");
        for (int page = 1; page <= pages; page++) {
            IScene scene1 = ActivityManagePageScene.builder().page(page).size(10).build();
            JSONArray list = scene1.visitor(visitor).execute().getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                int status = list.getJSONObject(i).getInteger("status");
                if (status == ActivityStatusEnum.PASSED.getId()) {
                    Long id = list.getJSONObject(i).getLong("id");
                    ids.add(id);
                }
            }
        }
        //创建活动并审批
        if (ids.size() == 0) {
            Long id1 = createRecruitActivityApproval();
            getApprovalPassed(id1);
            ids.add(id1);

        }
        return ids;
    }

    /**
     * 裂变活动-查询活动列表中的状态为【进行中的ID】
     */
    public List<Long> getFissionActivityWorking() {
        List<Long> ids = new ArrayList<>();
        //活动列表
        IScene scene = ActivityManagePageScene.builder().page(1).size(10).build();
        int pages = scene.visitor(visitor).execute().getInteger("pages");
        for (int page = 1; page <= pages; page++) {
            IScene scene1 = ActivityManagePageScene.builder().page(page).size(10).build();
            JSONArray list = scene1.visitor(visitor).execute().getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                int status = list.getJSONObject(i).getInteger("status");
                int activityType = list.getJSONObject(i).getInteger("activity_type");
                if (status == ActivityStatusEnum.PASSED.getId() && activityType == 1) {
                    Long id = list.getJSONObject(i).getLong("id");
                    ids.add(id);
                }
            }
        }
        //创建活动并审批
        if (ids.size() == 0) {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            Long id1 = createFissionActivity(voucherId);
            //审批活动
            getApprovalPassed(id1);
            ids.add(id1);

        }
        return ids;
    }

    /**
     * 招募活动-查询列表中的状态为【进行中的ID】
     */
    public List<Long> getRecruitActivityWorking() {
        List<Long> ids = new ArrayList<>();
        //活动列表
        IScene scene = ActivityManagePageScene.builder().page(1).size(10).build();
        int pages = scene.visitor(visitor).execute().getInteger("pages") > 10 ? 10 : scene.visitor(visitor).execute().getInteger("pages");
        System.err.println("pages:" + pages);
        for (int page = 1; page <= pages; page++) {
            System.err.println("------------" + page);
            IScene scene1 = ActivityManagePageScene.builder().page(page).size(10).build();
            JSONArray list = scene1.visitor(visitor).execute().getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                int status = list.getJSONObject(i).getInteger("status");
                int activityType = list.getJSONObject(i).getInteger("activity_type");
                if (status == ActivityStatusEnum.PASSED.getId() && activityType == 2) {
                    Long id = list.getJSONObject(i).getLong("id");
                    ids.add(id);
                }
            }
        }
        //创建活动并审批
        if (ids.size() == 0) {
            //创建活动
            Long id1 = createRecruitActivityApproval();
            //审批活动
            getApprovalPassed(id1);
            ids.add(id1);

        }
        return ids;
    }

    /**
     * 招募活动-查询列表中的状态为【进行中】的活动-存在待审批的人数
     */
    public List<Long> getRecruitActivityWorkingApproval() {
        List<Long> ids = new ArrayList<>();
        //活动列表
        IScene scene = ActivityManagePageScene.builder().page(1).size(10).build();
        int pages = scene.visitor(visitor).execute().getInteger("pages");
        for (int page = 1; page <= pages; page++) {
            IScene scene1 = ActivityManagePageScene.builder().page(page).size(10).build();
            JSONArray list = scene1.visitor(visitor).execute().getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                String statusName = list.getJSONObject(i).getString("status_name");
                int activityType = list.getJSONObject(i).getInteger("activity_type");
                if (activityType == 2 && statusName.equals("进行中")) {
                    int waitingAuditNum = list.getJSONObject(i).getInteger("waiting_audit_num");
                    if (waitingAuditNum >= 1) {
                        Long id = list.getJSONObject(i).getLong("id");
                        ids.add(id);
                    }
                }
            }
        }
        //创建活动并审批
        if (ids.isEmpty()) {
            //创建活动
            Long id1 = createRecruitActivityApproval();
            //审批活动
            getApprovalPassed(id1);
            //小程序报名
            user.loginApplet(EnumAppletToken.JC_GLY_ONLINE);
            activityRegisterApplet(id1, "13373166806", "郭丽雅", 2, "1513814362@qq.com", "22", "女", "其他");
            ids.add(id1);
            //登录PC
            jc.pcLogin(pp.phone1, pp.password);
        }
        return ids;

    }


    /**
     * 查询列表中的状态为【审核未通过的ID】
     */
    public List<Long> getActivityReject() {
        List<Long> ids = new ArrayList<>();
        //活动列表
        IScene scene = ActivityManagePageScene.builder().page(1).size(10).build();
        int pages = scene.visitor(visitor).execute().getInteger("pages");
        for (int page = 1; page <= pages; page++) {
            IScene scene1 = ActivityManagePageScene.builder().page(page).size(10).build();
            JSONArray list = scene1.visitor(visitor).execute().getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                int status = list.getJSONObject(i).getInteger("status");
                if (status == ActivityStatusEnum.REJECT.getId()) {
                    Long id = list.getJSONObject(i).getLong("id");
                    System.err.println(status + "-----" + id);
                    ids.add(id);
                }
            }
        }
        //创建活动并审批不通过
        if (ids.size() == 0) {
            Long id1 = createRecruitActivityApproval();
            getApprovalReject(id1);
            ids.add(id1);
        }
        return ids;
    }

    /**
     * 查询列表中的状态为【审核未通过的ID】--招募活动
     */
    public List<Long> getRecruitActivityReject() {
        List<Long> ids = new ArrayList<>();
        //活动列表
        IScene scene = ActivityManagePageScene.builder().page(1).size(10).build();
        int pages = scene.visitor(visitor).execute().getInteger("pages");
        for (int page = 1; page <= pages; page++) {
            IScene scene1 = ActivityManagePageScene.builder().page(page).size(10).build();
            JSONArray list = scene1.visitor(visitor).execute().getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                int status = list.getJSONObject(i).getInteger("status");
                int activityType = list.getJSONObject(i).getInteger("activity_type");
                if (status == ActivityStatusEnum.REJECT.getId() && activityType == 2) {
                    Long id = list.getJSONObject(i).getLong("id");
                    System.err.println(status + "-----" + id);
                    ids.add(id);
                }
            }
        }
        //创建活动并审批不通过
        if (ids.size() == 0) {
            Long id1 = createRecruitActivityApproval();
            getApprovalReject(id1);
            ids.add(id1);
        }
        return ids;
    }

    /**
     * 查询列表中的状态为【审核未通过的ID】
     */
    public List<Long> getFissionActivityReject() {
        List<Long> ids = new ArrayList<>();
        //活动列表
        IScene scene = ActivityManagePageScene.builder().page(1).size(10).build();
        int pages = scene.visitor(visitor).execute().getInteger("pages");
        for (int page = 1; page <= pages; page++) {
            IScene scene1 = ActivityManagePageScene.builder().page(page).size(10).build();
            JSONArray list = scene1.visitor(visitor).execute().getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                int status = list.getJSONObject(i).getInteger("status");
                int activityType = list.getJSONObject(i).getInteger("activity_type");
                if (status == ActivityStatusEnum.REJECT.getId() && activityType == 1) {
                    Long id = list.getJSONObject(i).getLong("id");
                    System.err.println(status + "-----" + id);
                    ids.add(id);
                }
            }
        }
        //创建活动并审批不通过
        if (ids.size() == 0) {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            Long id1 = createFissionActivity(voucherId);
            getApprovalReject(id1);
            ids.add(id1);
        }
        return ids;
    }


    /**
     * 查询列表中的状态为【待审批的ID】--裂变活动
     */
    public List<Long> getFissionActivityWait() {
        List<Long> ids = new ArrayList<>();
        //活动列表
        IScene activityManageListScene = ActivityManagePageScene.builder().page(1).size(10).build();
        int pages = activityManageListScene.visitor(visitor).execute().getInteger("pages");
        for (int page = 1; page <= pages; page++) {
            IScene scene1 = ActivityManagePageScene.builder().page(page).size(10).build();
            JSONArray list = scene1.visitor(visitor).execute().getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                int status = list.getJSONObject(i).getInteger("status");
                int activityType = list.getJSONObject(i).getInteger("activity_type");
                if (status == ActivityStatusEnum.PENDING.getId() && activityType == 1) {
                    Long id = list.getJSONObject(i).getLong("id");
                    ids.add(id);
                }
            }
        }

        if (ids.size() == 0) {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            Long id1 = createFissionActivity(voucherId);
            ids.add(id1);
        }
        return ids;
    }

    /**
     * 查询列表中的状态为【待审批的ID】--招募活动
     */
    public List<Long> getActivityWait() {
        List<Long> ids = new ArrayList<>();
        //活动列表
        IScene activityManageListScene = ActivityManagePageScene.builder().page(1).size(10).build();
        int pages = activityManageListScene.visitor(visitor).execute().getInteger("pages");
        for (int page = 1; page <= pages; page++) {
            IScene scene1 = ActivityManagePageScene.builder().page(page).size(10).build();
            JSONArray list = scene1.visitor(visitor).execute().getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                int status = list.getJSONObject(i).getInteger("status");
                int activityType = list.getJSONObject(i).getInteger("activity_type");
                if (status == ActivityStatusEnum.PENDING.getId() && activityType == 2) {
                    Long id = list.getJSONObject(i).getLong("id");
                    ids.add(id);
                }
            }
        }

        if (ids.size() == 0) {
            Long id1 = createRecruitActivityApproval();
            ids.add(id1);
        }
        return ids;
    }

    /**
     * 查询列表中的状态为【审核已取消的ID】
     */
    public List<Long> getActivityCancel() {
        List<Long> ids = new ArrayList<>();
        //活动列表
        IScene scene = ActivityManagePageScene.builder().page(1).size(10).build();
        int pages = scene.visitor(visitor).execute().getInteger("pages");
        for (int page = 1; page <= pages; page++) {
            IScene scene1 = ActivityManagePageScene.builder().page(page).size(10).build();
            JSONArray list = scene1.visitor(visitor).execute().getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                int status = list.getJSONObject(i).getInteger("status");
                if (status == ActivityStatusEnum.CANCELED.getId()) {
                    Long id = list.getJSONObject(i).getLong("id");
                    ids.add(id);
                }
            }
        }
        //创建活动-审批通过活动-取消活动
        if (ids.size() == 0) {
            //创建活动
            Long id = createRecruitActivityApproval();
            //审批通过
            getApprovalReject(id);
            //取消活动
            getCancelActivity(id);
            ids.add(id);
        }
        return ids;
    }

    /**
     * 查询列表中的状态为【审核已取消的ID】--招募活动
     */
    public List<Long> getRecruitActivityCancel() {
        List<Long> ids = new ArrayList<>();
        //活动列表
        IScene scene = ActivityManagePageScene.builder().page(1).size(10).build();
        int pages = scene.visitor(visitor).execute().getInteger("pages");
        for (int page = 1; page <= pages; page++) {
            IScene scene1 = ActivityManagePageScene.builder().page(page).size(10).build();
            JSONArray list = scene1.visitor(visitor).execute().getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                int status = list.getJSONObject(i).getInteger("status");
                int activityType = list.getJSONObject(i).getInteger("activity_type");
                if (status == ActivityStatusEnum.CANCELED.getId() && activityType == 2) {
                    Long id = list.getJSONObject(i).getLong("id");
                    ids.add(id);
                }
            }
        }
        //创建活动-审批通过活动-取消活动
        if (ids.size() == 0) {
            //创建活动
            Long id = createRecruitActivityApproval();
            //审批通过
            getApprovalReject(id);
            //取消活动
            getCancelActivity(id);
            ids.add(id);
        }
        return ids;
    }

    /**
     * 查询列表中的状态为【审核已取消的ID】--裂变活动
     */
    public List<Long> getFissionActivityCancel() {
        List<Long> ids = new ArrayList<>();
        //活动列表
        IScene scene = ActivityManagePageScene.builder().page(1).size(10).build();
        int pages = scene.visitor(visitor).execute().getInteger("pages");
        for (int page = 1; page <= pages; page++) {
            IScene scene1 = ActivityManagePageScene.builder().page(page).size(10).build();
            JSONArray list = scene1.visitor(visitor).execute().getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                int status = list.getJSONObject(i).getInteger("status");
                int activityType = list.getJSONObject(i).getInteger("activity_type");
                if (status == ActivityStatusEnum.CANCELED.getId() && activityType == 1) {
                    Long id = list.getJSONObject(i).getLong("id");
                    ids.add(id);
                }
            }
        }
        //创建活动-审批通过活动-取消活动
        if (ids.size() == 0) {
            //创建活动
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            Long id = createFissionActivity(voucherId);
            //审批通过
            getApprovalReject(id);
            //取消活动
            getCancelActivity(id);
            ids.add(id);
        }
        return ids;
    }

    /**
     * 查询列表中的状态为【已撤销的ID】
     * 2021-3-17
     */
    public List<Long> getActivityRevoke() {
        List<Long> ids = new ArrayList<>();
        //活动列表
        IScene activityManageListScene = ActivityManagePageScene.builder().page(1).size(10).build();
        int pages = activityManageListScene.visitor(visitor).execute().getInteger("pages");
        for (int page = 1; page <= pages; page++) {
            IScene scene1 = ActivityManagePageScene.builder().page(page).size(10).build();
            JSONArray list = scene1.visitor(visitor).execute().getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                int status = list.getJSONObject(i).getInteger("status");
                if (status == ActivityStatusEnum.REVOKE.getId()) {
                    Long id = list.getJSONObject(i).getLong("id");
                    ids.add(id);
                }
            }
        }

        if (ids.size() == 0) {
            Long id1 = createRecruitActivityApproval();
            getRevokeActivity(id1);
            ids.add(id1);
        }
        return ids;
    }

    /**
     * 查询列表中的状态为【已撤销的ID】----招募活动
     * 2021-3-17
     */
    public List<Long> getRecruitActivityRevoke() {
        List<Long> ids = new ArrayList<>();
        //活动列表
        IScene activityManageListScene = ActivityManagePageScene.builder().page(1).size(10).build();
        int pages = activityManageListScene.visitor(visitor).execute().getInteger("pages");
        for (int page = 1; page <= pages; page++) {
            IScene scene1 = ActivityManagePageScene.builder().page(page).size(10).build();
            JSONArray list = scene1.visitor(visitor).execute().getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                int status = list.getJSONObject(i).getInteger("status");
                int activityType = list.getJSONObject(i).getInteger("activity_type");
                if (status == ActivityStatusEnum.REVOKE.getId() && activityType == 2) {
                    Long id = list.getJSONObject(i).getLong("id");
                    System.err.println(status + "-------" + id);
                    ids.add(id);
                }
            }
        }
        if (ids.size() == 0) {
            Long id1 = createRecruitActivityApproval();
            getRevokeActivity(id1);
            ids.add(id1);
        }
        return ids;
    }

    /**
     * 查询列表中的状态为【已撤销的ID】----裂变活动
     * 2021-3-17
     */
    public List<Long> getFissionActivityRevoke() {
        List<Long> ids = new ArrayList<>();
        //活动列表
        IScene activityManageListScene = ActivityManagePageScene.builder().page(1).size(10).build();
        int pages = activityManageListScene.visitor(visitor).execute().getInteger("pages");
        for (int page = 1; page <= pages; page++) {
            IScene scene1 = ActivityManagePageScene.builder().page(page).size(10).build();
            JSONArray list = scene1.visitor(visitor).execute().getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                int status = list.getJSONObject(i).getInteger("status");
                int activityType = list.getJSONObject(i).getInteger("activity_type");
                if (status == ActivityStatusEnum.REVOKE.getId() && activityType == 1) {
                    Long id = list.getJSONObject(i).getLong("id");
                    System.err.println(status + "-------" + id);
                    ids.add(id);
                }
            }
        }
        if (ids.size() == 0) {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            Long id1 = createFissionActivity(voucherId);
            getRevokeActivity(id1);
            ids.add(id1);
        }
        return ids;
    }

    /**
     * 招募活动-查询列表中的状态为【未开始的ID】
     * 2021-3-17
     */
    public List<Long> getActivityWaitingStar() {
        List<Long> ids = new ArrayList<>();
        //活动列表
        IScene scene = ActivityManagePageScene.builder().page(1).size(10).build();
        int pages = scene.visitor(visitor).execute().getInteger("pages");
        for (int page = 1; page <= pages; page++) {
            IScene scene1 = ActivityManagePageScene.builder().page(page).size(10).build();
            JSONArray list = scene1.visitor(visitor).execute().getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                int status = list.getJSONObject(i).getInteger("status");
                int activityType = list.getJSONObject(i).getInteger("activity_type");
                if (status == ActivityStatusEnum.WAITING_START.getId() && activityType == 2) {
                    Long id = list.getJSONObject(i).getLong("id");
                    ids.add(id);
                }
            }
        }
        //创建活动并审批
        if (ids.size() == 0) {
            //创建活动
            Long id1 = createRecruitActivity();
            //审批活动
            getApprovalPassed(id1);
            ids.add(id1);

        }
        return ids;
    }

    /**
     * 招募活动-查询列表中的状态为【未开始的ID】---招募活动
     * 2021-3-17
     */
    public List<Long> getRecruitActivityWaitingStar() {
        List<Long> ids = new ArrayList<>();
        //活动列表
        IScene scene = ActivityManagePageScene.builder().page(1).size(10).build();
        int pages = scene.visitor(visitor).execute().getInteger("pages") > 10 ? 10 : scene.visitor(visitor).execute().getInteger("pages");
        for (int page = 1; page <= pages; page++) {
            IScene scene1 = ActivityManagePageScene.builder().page(page).size(10).build();
            JSONArray list = scene1.visitor(visitor).execute().getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                int status = list.getJSONObject(i).getInteger("status");
                int activityType = list.getJSONObject(i).getInteger("activity_type");
                if (status == ActivityStatusEnum.WAITING_START.getId() && activityType == 2) {
                    Long id = list.getJSONObject(i).getLong("id");
                    ids.add(id);
                }
            }
        }
        //创建活动并审批
        if (ids.size() == 0) {
            //创建活动
            Long id1 = createRecruitActivity();
            //审批活动
            getApprovalPassed(id1);
            ids.add(id1);

        }
        return ids;
    }

    /**
     * 招募活动-查询列表中的状态为【未开始的ID】---裂变活动
     * 2021-3-17
     */
    public List<Long> getFissionActivityWaitingStar() {
        List<Long> ids = new ArrayList<>();
        //活动列表
        IScene scene = ActivityManagePageScene.builder().page(1).size(10).build();
        int pages = scene.visitor(visitor).execute().getInteger("pages");
        for (int page = 1; page <= pages; page++) {
            IScene scene1 = ActivityManagePageScene.builder().page(page).size(10).build();
            JSONArray list = scene1.visitor(visitor).execute().getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                int status = list.getJSONObject(i).getInteger("status");
                int activityType = list.getJSONObject(i).getInteger("activity_type");
                if (status == ActivityStatusEnum.WAITING_START.getId() && activityType == 1) {
                    Long id = list.getJSONObject(i).getLong("id");
                    ids.add(id);
                }
            }
        }
        //创建活动并审批
        if (ids.size() == 0) {
            //创建活动
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            Long id1 = createFissionActivityWaitingStarScene(voucherId);
            //审批活动
            getApprovalPassed(id1);
            ids.add(id1);

        }
        return ids;
    }

    /**
     * 招募活动-查询列表中的状态为【已过期的ID】---招募活动
     * 2021-3-17
     */
    public List<Long> getRecruitActivityFinish() {
        List<Long> ids = new ArrayList<>();
        //活动列表
        IScene scene = ActivityManagePageScene.builder().page(1).size(10).build();
        int pages = scene.visitor(visitor).execute().getInteger("pages");
        for (int page = 1; page <= pages; page++) {
            IScene scene1 = ActivityManagePageScene.builder().page(page).size(10).build();
            JSONArray list = scene1.visitor(visitor).execute().getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                int status = list.getJSONObject(i).getInteger("status");
                int activityType = list.getJSONObject(i).getInteger("activity_type");
                if (status == ActivityStatusEnum.FINISH.getId() && activityType == 2) {
                    Long id = list.getJSONObject(i).getLong("id");
                    ids.add(id);
                }
            }
        }
        return ids;
    }

    /**
     * 招募活动-查询列表中的状态为【已过期的ID】---裂变活动
     * 2021-3-17
     */
    public List<Long> getFissionActivityFinish() {
        List<Long> ids = new ArrayList<>();
        //活动列表
        IScene scene = ActivityManagePageScene.builder().page(1).size(10).build();
        int pages = scene.visitor(visitor).execute().getInteger("pages");
        for (int page = 1; page <= pages; page++) {
            IScene scene1 = ActivityManagePageScene.builder().page(page).size(10).build();
            JSONArray list = scene1.visitor(visitor).execute().getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                int status = list.getJSONObject(i).getInteger("status");
                int activityType = list.getJSONObject(i).getInteger("activity_type");
                if (status == ActivityStatusEnum.FINISH.getId() && activityType == 1) {
                    Long id = list.getJSONObject(i).getLong("id");
                    ids.add(id);
                }
            }
        }
        return ids;
    }


    /**
     * 获取活动的的状态
     */
    public int getActivityStatus(Long id) {
        IScene scene = ActivityManagePageScene.builder().page(1).size(10).build();
        int pages = scene.visitor(visitor).execute().getInteger("pages");
        int status = 0;
        for (int page = 1; page <= pages; page++) {
            IScene scene2 = ActivityManagePageScene.builder().page(page).size(10).build();
            JSONArray list = scene2.visitor(visitor).execute().getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                Long activityId = list.getJSONObject(i).getLong("id");
                if (activityId.equals(id)) {
                    status = list.getJSONObject(i).getInteger("status");
                }
            }
        }
        return status;
    }

    /**
     * 获取活动审批的的状态
     */
    public int getActivityApprovalStatus(Long id) {
        IScene scene = ActivityManagePageScene.builder().page(1).size(10).build();
        int pages = scene.visitor(visitor).execute().getInteger("pages");
        int status = 0;
        for (int page = 1; page <= pages; page++) {
            IScene scene2 = ActivityManagePageScene.builder().page(page).size(10).build();
            JSONArray list = scene2.visitor(visitor).execute().getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                Long activityId = list.getJSONObject(i).getLong("id");
                if (activityId.equals(id)) {
                    status = list.getJSONObject(i).getInteger("approval_status");
                }
            }
        }
        return status;
    }

    /**
     * 活动管理-活动报名列表中待审批的ids
     */
    public List<Long> registerApproval(Long activityId) {
        //报名列表的中【待审批】的活动
        JSONObject response = getRegisterPage(activityId);
        int pages = response.getInteger("pages");
        List<Long> idArray = new ArrayList<>();
        for (int page = 1; page <= pages; page++) {
            IScene scene = ManageRegisterPageScene.builder().page(page).size(10).activityId(activityId).build();
            JSONArray list = scene.visitor(visitor).execute().getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                String statusName = list.getJSONObject(i).getString("status_name");
                if (statusName.equals("待审批")) {
                    //报名列表ID
                    Long ids = list.getJSONObject(i).getLong("id");
                    idArray.add(ids);
                }
            }
        }
        return idArray;
    }

    /**
     * 报名审批列表【待审批】ids的合集
     */
    public List<Long> RegisterAppletIds(Long activityId) {
        List<Long> ids = new ArrayList<>();
        IScene scene = ManageRegisterPageScene.builder().page(1).size(100).status(1).activityId(activityId).build();
        JSONObject response = scene.visitor(visitor).execute();
        int pages = response.getInteger("pages");
        for (int page = 1; page <= pages; page++) {
            IScene scene1 = ManageRegisterPageScene.builder().page(page).size(10).status(1).activityId(activityId).build();
            JSONArray list = scene1.visitor(visitor).execute().getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                Long id = list.getJSONObject(i).getLong("id");
                ids.add(id);
            }
        }
        return ids;
    }


    /**
     * ---------------------------------对于活动的操作------------------------------
     */

    /**
     * 活动管理-删除活动
     */
    public String getDelActivity(Long id) {
        IScene scene = ManageDeleteScene.builder().id(id).build();
        String message = scene.visitor(visitor).getResponse().getMessage();
        return message;
    }

    /**
     * 活动管理-撤回活动申请
     */
    public String getRevokeActivity(Long id) {
        IScene scene = ManageRevokeScene.builder().id(id).build();
        String message = scene.visitor(visitor).getResponse().getMessage();
        return message;
    }

    /**
     * 活动管理-取消活动
     */
    public String getCancelActivity(Long id) {
        IScene scene = ManageCancelScene.builder().id(id).build();
        String message = scene.visitor(visitor).getResponse().getMessage();
        return message;
    }

    /**
     * 活动管理-推广活动
     */
    public String getPromotionActivity(Long id) {
        IScene scene = ManagePromotionScene.builder().id(id).build();
        String appletCodeUrl = scene.visitor(visitor).execute().getString("applet_code_url");
        return appletCodeUrl;
    }


    /**
     * 活动管理-活动审批【通过】
     */
    public String getApprovalPassed(Long... id) {
        List<Long> ids = Arrays.asList(id);
        System.err.println("---------" + ids);
        IScene scene = ManageApprovalScene.builder().ids(ids).status(ActivityApprovalStatusEnum.PASSED.getId()).build();
        String message = scene.visitor(visitor).getResponse().getMessage();
        return message;
    }


    /**
     * 活动管理-活动审批【审核不通过】
     */
    public String getApprovalReject(Long... id) {
        List<Long> ids = Arrays.asList(id);
        IScene scene = ManageApprovalScene.builder().ids(ids).status(ActivityApprovalStatusEnum.REJECT.getId()).build();
        String message = scene.visitor(visitor).getResponse().getMessage();
        return message;
    }

    /**
     * 活动管理-活动报名审批【通过】
     */
    public String getRegisterApprovalPassed(Long activityId, Long... id) {
        List<Long> ids = Arrays.asList(id);
        IScene scene = ManageRegisterApprovalScene.builder().activityId(activityId).ids(ids).status(101).build();
        String message = scene.visitor(visitor).getResponse().getMessage();
        return message;
    }


    /**
     * 活动管理-活动报名审批【审核不通过】
     */
    public String getRegisterApprovalReject(Long activityId, Long... id) {
        List<Long> ids = Arrays.asList(id);
        IScene scene = ManageRegisterApprovalScene.builder().activityId(activityId).ids(ids).status(201).build();
        String message = scene.visitor(visitor).getResponse().getMessage();
        return message;
    }

    /**
     * 小程序-活动报名-取消报名
     */
    public void activityCancelScene(Long id) {
        IScene scene = AppointmentActivityCancelScene.builder().id(id).build();
        scene.visitor(visitor).execute();
    }

    /**
     * 根据活动ID返回活动的状态
     */
    public String appointmentActivityStatus(Long activityId) {
        Integer lastValue = null;
        JSONArray list = null;
        String status = "";
        do {
            IScene scene = AppointmentActivityListScene.builder().lastValue(lastValue).size(10).build();
            JSONObject response1 = scene.visitor(visitor).execute();
            lastValue = response1.getInteger("last_value");
            list = response1.getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                Long id = list.getJSONObject(i).getLong("id");
                System.out.println(i + "---------" + id);
                if (activityId.equals(id)) {
                    status = list.getJSONObject(i).getString("status_name");
                }

            }
        } while (list.size() == 10);
        return status;
    }

    /**
     * 更多活动列表-小程序
     * 2021-3-17
     */
    public JSONObject appointmentActivityTitleNew() {
        IScene scene = AppletArticleListScene.builder().lastValue(null).size(10).build();
        JSONObject response = scene.visitor(visitor).execute();
        return response;
    }

    /**
     * 更多活动列表-根据活动ID返回活动的标题
     */
    public String appointmentActivityTitleNew(Long activityId) {
        JSONObject lastValue = null;
        JSONArray list;
        String title = "";
        do {
            IScene scene = AppletArticleListScene.builder().lastValue(lastValue).size(10).build();
            JSONObject response = scene.visitor(visitor).execute();
            lastValue = response.getJSONObject("last_value");
            list = response.getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                Long itemId = list.getJSONObject(i).getLong("itemId");
                if (activityId.equals(itemId)) {
                    title = list.getJSONObject(i).getString("title");
                    System.err.println("-----title----" + title);
                }
            }
        } while (list.size() == 10);

        return title;
    }

    /**
     * 更多活动列表-根据活动ID返回活动的小程序活动id
     */
    public Long appointmentActivityId(Long activityId) {
        JSONObject lastValue = null;
        JSONArray list;
        Long id = 0L;
        //获取此活动的名称
        jc.pcLogin(pp.phone1, pp.password);
        String title = getRecruitActivityDetailDate1(activityId).getString("title");
        user.loginApplet(EnumAppletToken.JC_GLY_DAILY);
        do {
            IScene scene = AppletArticleListScene.builder().lastValue(lastValue).size(10).build();
            JSONObject response = scene.visitor(visitor).execute();
            lastValue = response.getJSONObject("last_value");
            list = response.getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                String title1 = list.getJSONObject(i).getString("title");
                if (title.equals(title1)) {
                    id = list.getJSONObject(i).getLong("id");
                }
            }
        } while (list.size() == 10);

        return id;
    }

/**
 * ---------------------------------------------获取也买你返回值---------------------------------------------
 */


    /**
     * 调整记录列表
     */
    public JSONObject changeRecordPage(Long activityId) {
        IScene scene = ManageChangeRecordScene.builder().page(1).size(10).id(activityId).build();
        JSONObject response = scene.visitor(visitor).execute();
        return response;
    }

    /**
     * 招募活动详情页-获取返回值在【活动奖励】内部
     */
    public JSONArray getRecruitActivityDetail(Long activityId) {
        IScene scene = ManageDetailScene.builder().id(activityId).build();
        JSONArray response = scene.visitor(visitor).execute().getJSONObject("recruit_activity_info").getJSONArray("reward_vouchers");
        return response;
    }

    /**
     * 招募活动详情页返回值
     */
    public JSONObject getRecruitActivityDetailDate(Long activityId) {
        IScene scene = ManageDetailScene.builder().id(activityId).build();
        JSONObject response = scene.visitor(visitor).execute().getJSONObject("recruit_activity_info");
        return response;
    }

    /**
     * 招募活动详情页返回值
     */
    public JSONObject getRecruitActivityDetailDate1(Long activityId) {
        IScene scene = ManageDetailScene.builder().id(activityId).build();
        JSONObject response = scene.visitor(visitor).execute();
        return response;
    }

    /**
     * 招募活动裂变详情页返回值
     */
    public JSONObject getFissionActivityDetailDate1(Long activityId) {
        IScene scene = ManageDetailScene.builder().id(activityId).build();
        JSONObject response = scene.visitor(visitor).execute();
        return response;
    }

    /**
     * 裂变活动裂变详情页-获取返回值
     */
    public JSONObject getFissionActivityDetailData(Long activityId) {
        IScene scene = ManageDetailScene.builder().id(activityId).build();
        JSONObject response = scene.visitor(visitor).execute().getJSONObject("fission_voucher_info");
        return response;
    }

    /**
     * 裂变活动裂变详情页-获取返回值在【活动奖励】内部
     */
    public JSONObject getFissionActivityDetail(Long activityId) {
        IScene scene = ManageDetailScene.builder().id(activityId).build();
        JSONObject response = scene.visitor(visitor).execute().getJSONObject("fission_voucher_info").getJSONObject("reward_vouchers");
        return response;
    }


    /**
     * 报名数据-返回值（data）
     */
    public JSONObject getRegisterData(Long activityId) {
        IScene scene = ManageRegisterDataScene.builder().activityId(activityId).build();
        JSONObject response = scene.visitor(visitor).execute();
        return response;
    }

    /**
     * 报名列表-返回值（data）
     */
    public JSONObject getRegisterPage(Long activityId) {
        IScene scene = ManageRegisterPageScene.builder().page(1).size(10).activityId(activityId).build();
        JSONObject response = scene.visitor(visitor).execute();
        return response;
    }

    /**
     * 活动审批数据-data
     */
    public JSONObject getActivityApprovalDate() {
        IScene scene = ActivityManageDateScene.builder().build();
        JSONObject response = scene.visitor(visitor).execute();
        return response;
    }

    /**
     * 活动审批列表返回值--data
     */
    public JSONObject getActivityManagePage(int status) {
        IScene scene = ActivityManagePageScene.builder().page(1).size(10).approvalStatus(status).build();
        JSONObject response = scene.visitor(visitor).execute();
        return response;
    }


    /**
     * 获取招募活动的返回值
     */
    public JSONObject getActivityRespond(Long id) {
        IScene scene = ActivityManagePageScene.builder().page(1).size(10).build();
        int pages = scene.visitor(visitor).execute().getInteger("pages");
        JSONObject respondOne = null;
        for (int page = 1; page <= pages; page++) {
            IScene scene2 = ActivityManagePageScene.builder().page(page).size(10).build();
            JSONArray list = scene2.visitor(visitor).execute().getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                Long activityId = list.getJSONObject(i).getLong("id");
                if (activityId.equals(id)) {
                    respondOne = list.getJSONObject(i);
                }
            }
        }
        return respondOne;
    }

    /**
     * 获取小程序-首页-文章列表-文章条数
     */
    public int getAppletArticleList() {
        //获取小程序推荐列表
        JSONObject lastValue = null;
        JSONArray list = null;
        JSONObject object = null;
        int num = 0;
        do {
            IScene scene = AppletArticleListScene.builder().lastValue(lastValue).size(10).build();
            JSONObject response1 = scene.visitor(visitor).execute();
            lastValue = response1.getJSONObject("last_value");
            list = response1.getJSONArray("list");
            num += list.size();
        } while (list.size() == 10);
        return num;
    }


    /**
     * 判断客户报名是否重复，获取重复的手机号
     */
    public List<String> phoneSameArrayCheck(Long activityId) {
        List<String> phoneSameArray = null;
        //报名列表的返回值
        JSONObject pageRes = getRegisterPage(activityId);
        int pages = pageRes.getInteger("pages");
        for (int page = 1; page <= pages; page++) {
            IScene scene = ManageRegisterPageScene.builder().page(page).size(10).activityId(activityId).build();
            JSONArray list = scene.visitor(visitor).execute().getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                String customerPhone = list.getJSONObject(i).getString("customer_phone");
                IScene scene1 = ManageRegisterPageScene.builder().page(page).size(10).activityId(activityId).build();
                JSONArray list1 = scene1.visitor(visitor).execute().getJSONArray("list");
                for (int j = 0; j < list1.size(); j++) {
                    String customerPhone1 = list1.getJSONObject(i).getString("customer_phone");
                    if (customerPhone.equals(customerPhone1)) {
                        phoneSameArray.add(customerPhone);
                    }
                }
            }
        }
        return phoneSameArray;
    }


    /**
     * 小程序报名招募活动
     */
    public void activityRegisterApplet(Long id, String phone, String name, int registerCount, String eMail, String age, String gender, String others) {
        JSONArray registerItems = new JSONArray();
        Long activityId = 0L;
        //在活动详情中获得招募活动的报名信息
        jc.pcLogin(pp.phone1, pp.password);
        JSONObject response = getRecruitActivityDetailDate(id);
        JSONArray registerInformationList = response.getJSONArray("register_information_list");
        for (int i = 0; i < registerInformationList.size(); i++) {
            int type = registerInformationList.getJSONObject(i).getInteger("type");
            if (type == RegisterInfoEnum.PHONE.getId()) {
                JSONObject jsonObjectPhone = new JSONObject();
                jsonObjectPhone.put("type", type);
                jsonObjectPhone.put("value", phone);
                registerItems.add(jsonObjectPhone);
            } else if (type == RegisterInfoEnum.NAME.getId()) {
                JSONObject jsonObjectName = new JSONObject();
                jsonObjectName.put("type", type);
                jsonObjectName.put("value", name);
                registerItems.add(jsonObjectName);
            } else if (type == RegisterInfoEnum.EMAIL.getId()) {
                JSONObject jsonObjectEMail = new JSONObject();
                jsonObjectEMail.put("type", type);
                jsonObjectEMail.put("value", eMail);
                registerItems.add(jsonObjectEMail);
            } else if (type == RegisterInfoEnum.GENDER.getId()) {
                JSONObject jsonObjectEMail = new JSONObject();
                jsonObjectEMail.put("type", type);
                jsonObjectEMail.put("value", gender);
                registerItems.add(jsonObjectEMail);
            } else if (type == RegisterInfoEnum.AGE.getId()) {
                JSONObject jsonObjectEMail = new JSONObject();
                jsonObjectEMail.put("type", type);
                jsonObjectEMail.put("value", age);
                registerItems.add(jsonObjectEMail);
            } else if (type == RegisterInfoEnum.REGISTER_COUNT.getId()) {
                JSONObject jsonObjectEMail = new JSONObject();
                jsonObjectEMail.put("type", type);
                jsonObjectEMail.put("value", registerCount);
                registerItems.add(jsonObjectEMail);
            } else if (type == RegisterInfoEnum.OTHERS.getId()) {
                JSONObject jsonObjectEMail = new JSONObject();
                jsonObjectEMail.put("type", type);
                jsonObjectEMail.put("value", others);
                registerItems.add(jsonObjectEMail);
            }
        }
        //获取此活动的名称
        String title = getRecruitActivityDetailDate1(id).getString("title");
        System.err.println("----------title:" + title);
        user.loginApplet(EnumAppletToken.JC_GLY_DAILY);
        //获取小程序推荐列表
        JSONObject lastValue = null;
        JSONArray list = null;
        do {
            IScene scene = AppletArticleListScene.builder().lastValue(lastValue).size(10).build();
            JSONObject response1 = scene.visitor(visitor).execute();
            lastValue = response1.getJSONObject("last_value");
            list = response1.getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                String title1 = list.getJSONObject(i).getString("title");
                System.out.println("------------" + title1);
                if (title.equals(title1)) {
                    activityId = list.getJSONObject(i).getLong("id");
                    System.err.println(title + "----------" + activityId);
                }
            }
        } while (list.size() == 10);
        IScene scene = ArticleActivityRegisterScene.builder().id(activityId).registerItems(registerItems).build();
        scene.visitor(visitor).execute();

    }

    /**
     * 小程序报名招募活动
     */
    public void activityRegisterApplet(Long id, String phone, String name, int registerCount, String eMail, String age, String gender, String others, String activityName) {
        JSONArray registerItems = new JSONArray();
        //在活动详情中获得招募活动的报名信息
        jc.pcLogin(pp.phone1, pp.password);
        JSONObject response = getRecruitActivityDetailDate(id);
        JSONArray registerInformationList = response.getJSONArray("register_information_list");
        for (int i = 0; i < registerInformationList.size(); i++) {
            int type = registerInformationList.getJSONObject(i).getInteger("type");
            if (type == RegisterInfoEnum.PHONE.getId()) {
                JSONObject jsonObjectPhone = new JSONObject();
                jsonObjectPhone.put("type", type);
                jsonObjectPhone.put("value", phone);
                registerItems.add(jsonObjectPhone);
            } else if (type == RegisterInfoEnum.NAME.getId()) {
                JSONObject jsonObjectName = new JSONObject();
                jsonObjectName.put("type", type);
                jsonObjectName.put("value", name);
                registerItems.add(jsonObjectName);
            } else if (type == RegisterInfoEnum.EMAIL.getId()) {
                JSONObject jsonObjectEMail = new JSONObject();
                jsonObjectEMail.put("type", type);
                jsonObjectEMail.put("value", eMail);
                registerItems.add(jsonObjectEMail);
            } else if (type == RegisterInfoEnum.GENDER.getId()) {
                JSONObject jsonObjectEMail = new JSONObject();
                jsonObjectEMail.put("type", type);
                jsonObjectEMail.put("value", gender);
                registerItems.add(jsonObjectEMail);
            } else if (type == RegisterInfoEnum.AGE.getId()) {
                JSONObject jsonObjectEMail = new JSONObject();
                jsonObjectEMail.put("type", type);
                jsonObjectEMail.put("value", age);
                registerItems.add(jsonObjectEMail);
            } else if (type == RegisterInfoEnum.REGISTER_COUNT.getId()) {
                JSONObject jsonObjectEMail = new JSONObject();
                jsonObjectEMail.put("type", type);
                jsonObjectEMail.put("value", registerCount);
                registerItems.add(jsonObjectEMail);
            } else if (type == RegisterInfoEnum.OTHERS.getId()) {
                JSONObject jsonObjectEMail = new JSONObject();
                jsonObjectEMail.put("type", type);
                jsonObjectEMail.put("value", others);
                registerItems.add(jsonObjectEMail);
            }
        }
        user.loginApplet(EnumAppletToken.JC_GLY_DAILY);
        //获取小程序推荐列表
        JSONObject lastValue = null;
        JSONArray list = null;
        Long activityId = 0L;
        do {
            IScene scene = AppletArticleListScene.builder().lastValue(lastValue).size(10).build();
            JSONObject response1 = scene.visitor(visitor).execute();
            lastValue = response1.getJSONObject("last_value");
            list = response1.getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                String title1 = list.getJSONObject(i).getString("title");
                System.out.println("------------" + title1);
                if (activityName.equals(title1)) {
                    activityId = list.getJSONObject(i).getLong("id");
                    System.err.println(activityName + "----------" + activityId);
                }
            }
        } while (list.size() == 10);
        System.err.println("-----activityId------" + activityId);
        IScene scene = ArticleActivityRegisterScene.builder().id(activityId).registerItems(registerItems).build();
        scene.visitor(visitor).execute();

    }

    /**
     * 小程序报名招募活动
     */
    public void activityRegisterApplet1(Long id, String phone, String name, int registerCount, String eMail, String age, String gender, String others, String activityName) {
        JSONArray registerItems = new JSONArray();
        //在活动详情中获得招募活动的报名信息
        jc.pcLogin(pp.phone1, pp.password);
        JSONObject response = getRecruitActivityDetailDate(id);
        JSONArray registerInformationList = response.getJSONArray("register_information_list");
        for (int i = 0; i < registerInformationList.size(); i++) {
            int type = registerInformationList.getJSONObject(i).getInteger("type");
            if (type == RegisterInfoEnum.PHONE.getId()) {
                JSONObject jsonObjectPhone = new JSONObject();
                jsonObjectPhone.put("type", type);
                jsonObjectPhone.put("value", phone);
                registerItems.add(jsonObjectPhone);
            } else if (type == RegisterInfoEnum.NAME.getId()) {
                JSONObject jsonObjectName = new JSONObject();
                jsonObjectName.put("type", type);
                jsonObjectName.put("value", name);
                registerItems.add(jsonObjectName);
            } else if (type == RegisterInfoEnum.EMAIL.getId()) {
                JSONObject jsonObjectEMail = new JSONObject();
                jsonObjectEMail.put("type", type);
                jsonObjectEMail.put("value", eMail);
                registerItems.add(jsonObjectEMail);
            } else if (type == RegisterInfoEnum.GENDER.getId()) {
                JSONObject jsonObjectEMail = new JSONObject();
                jsonObjectEMail.put("type", type);
                jsonObjectEMail.put("value", gender);
                registerItems.add(jsonObjectEMail);
            } else if (type == RegisterInfoEnum.AGE.getId()) {
                JSONObject jsonObjectEMail = new JSONObject();
                jsonObjectEMail.put("type", type);
                jsonObjectEMail.put("value", age);
                registerItems.add(jsonObjectEMail);
            } else if (type == RegisterInfoEnum.REGISTER_COUNT.getId()) {
                JSONObject jsonObjectEMail = new JSONObject();
                jsonObjectEMail.put("type", type);
                jsonObjectEMail.put("value", registerCount);
                registerItems.add(jsonObjectEMail);
            } else if (type == RegisterInfoEnum.OTHERS.getId()) {
                JSONObject jsonObjectEMail = new JSONObject();
                jsonObjectEMail.put("type", type);
                jsonObjectEMail.put("value", others);
                registerItems.add(jsonObjectEMail);
            }
        }
        user.loginApplet(EnumAppletToken.JC_LXQ_DAILY);
        //获取小程序推荐列表
        JSONObject lastValue = null;
        JSONArray list = null;
        Long activityId = 0L;
        do {
            IScene scene = AppletArticleListScene.builder().lastValue(lastValue).size(10).build();
            JSONObject response1 = scene.visitor(visitor).execute();
            lastValue = response1.getJSONObject("last_value");
            list = response1.getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                String title1 = list.getJSONObject(i).getString("title");
                System.out.println("------------" + title1);
                if (activityName.equals(title1)) {
                    activityId = list.getJSONObject(i).getLong("id");
                    System.err.println(activityName + "----------" + activityId);
                }
            }
        } while (list.size() == 10);
        System.err.println("-----activityId------" + activityId);
        IScene scene = ArticleActivityRegisterScene.builder().id(activityId).registerItems(registerItems).build();
        scene.visitor(visitor).execute();

    }

    /**
     * 小程序报名招募活动---不填写个人信息
     */
    public void activityRegisterApplet(Long id) {
        JSONObject lastValue = null;
        JSONArray list = null;
        JSONArray registerItems = new JSONArray();
        Long activityId = 0L;
        //登录PC
        jc.pcLogin(pp.phone1, pp.password);
        //获取此活动的名称
        String title = getRecruitActivityDetailDate1(id).getString("title");
        user.loginApplet(EnumAppletToken.JC_GLY_DAILY);
        //获取小程序推荐列表
        do {
            IScene scene = AppletArticleListScene.builder().lastValue(lastValue).size(10).build();
            JSONObject response1 = scene.visitor(visitor).execute();
            lastValue = response1.getJSONObject("last_value");
            list = response1.getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                String title1 = list.getJSONObject(i).getString("title");
                System.out.println("----------title1:" + title1);
                if (title.equals(title1)) {
                    activityId = list.getJSONObject(i).getLong("id");
                }
            }
        } while (list.size() == 10);
        IScene scene = ArticleActivityRegisterScene.builder().id(activityId).registerItems(registerItems).build();
        scene.visitor(visitor).execute();
    }

    /**
     * V2.0小程序-我的报名-报名列表--报名条数
     */
    public int appointmentActivityList() {
        Integer lastValue = null;
        JSONArray list = null;
        int num = 0;
        do {
            IScene scene = AppointmentActivityListScene.builder().lastValue(lastValue).size(10).build();
            JSONObject response1 = scene.visitor(visitor).execute();
            lastValue = response1.getInteger("last_value");
            list = response1.getJSONArray("list");
            num += list.size();
        } while (list.size() == 10);
        return num;
    }

    /**
     * 获取卡券列表未使用条数
     */
    public Integer voucherNotUseNum() {
        JSONArray list = jc.appletVoucherList(null, "USED", 100).getJSONArray("list");
        int num = 0;
        for (int i = 0; i < list.size(); i++) {
            String status = list.getJSONObject(i).getString("status_name");
            System.out.println("=======" + status);
            if (!status.equals("已使用") && !status.equals("已过期")) {
                num++;
            }
        }
        return num;
    }

    /**
     * 获取卡券列表未使用优惠券ID    ---不通
     */
    public List<Long> voucherNotUseId() {
        //登录小程序
        user.loginApplet(EnumAppletToken.JC_GLY_DAILY);
        JSONArray list = jc.appletVoucherList(null, "USED", 100).getJSONArray("list");
        List<Long> voucherCodes = new ArrayList<>();
        List<Long> ids = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Long voucherCode = list.getJSONObject(i).getLong("voucher_code");
            String status = list.getJSONObject(i).getString("status_name");
            if (!status.equals("已使用") && !status.equals("已过期")) {
                voucherCodes.add(voucherCode);
            }
        }
        //登录PC
        jc.pcLogin(pp.phone1, pp.password);
        //获取PC中对应的优惠券

        return ids;
    }

    /**
     * 优惠券转移
     */
    public void voucherTransfer(List<Long> ids) {
        int num = voucherNotUseNum();
        if (num > 0) {
            ScenarioUtil su = new ScenarioUtil();
            su.pcTransfer("13373166806", "17611474518", ids);
        }

    }

    /**
     * @description :比较日期大小的方法
     * @date :2020/12/14
     **/
    public void dataCompare() throws ParseException {
        String flag = "2020-12-13 12:33";
        String aaa = flag.substring(0, 9);
        //获取当前时间---df.format(date)
        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        //当前时间+-3天
        Calendar rightNow = Calendar.getInstance();
        int year = rightNow.get(Calendar.YEAR);    //获取年
        int month = rightNow.get(Calendar.MONTH) + 1;   //获取月份，0表示1月份
        int day = rightNow.get(Calendar.DAY_OF_MONTH);    //获取当前天数
        int time = rightNow.get(Calendar.HOUR_OF_DAY);       //获取当前小时
        int min = rightNow.get(Calendar.MINUTE);          //获取当前分钟
        String startTime = year + "-" + month + "-" + (day - 3) + " " + time + ":" + min;
        String endTime = year + "-" + month + "-" + (day + 3) + " " + time + ":" + min;
        System.out.println("--------创建时间：" + "--------开始时间：" + startTime + "--------开始时间：" + endTime);
        Date startDate = df.parse(startTime);
        Date endDate = df.parse(endTime);
        Date createDate = df.parse("2020-12-14 12:56");
        System.out.println("--------创建时间：" + createDate + "--------开始时间：" + startDate + "--------开始时间：" + endDate);
        System.out.println(createDate.compareTo(startDate));
//        Preconditions.checkArgument(createDate.compareTo(startDate)>=0&&createDate.compareTo(endDate)<=0, "列表中创建时间："+createDate+"筛选栏开始时间："+startTime+"筛选栏开始时间："+endTime+"创建时间不在开始系欸结束之间之间");

    }

    /**
     * 门店shop_id与shop_name转化
     */
    public String shopNameTransformId(String shopName) {
        String shopId = "";
        JSONObject response = jc.shopListPage();
        JSONArray list = response.getJSONArray("list");
        for (int i = 0; i < list.size(); i++) {
            String name = list.getJSONObject(i).getString("shop_name");
            if (shopName.equals(name)) {
                shopId = list.getJSONObject(i).getString("shop_id");
            } else if (shopName.equals("集团管理")) {
                shopId = list.getJSONObject(0).getString("shop_id");
            }
        }
        return shopId;
    }


    /**
     * 遍历门店的名字，判断是否存在此门店
     */
    public String getShopNameExist(String name) {
        String nameBack = "";
        JSONArray list = jc.shopListPage().getJSONArray("list");
        for (int i = 0; i < list.size(); i++) {
            String shopName = list.getJSONObject(i).getString("shop_name");
            if (name.equals(shopName)) {
                nameBack = shopName;
            } else if (i == list.size() - 1 && nameBack.equals("")) {
                nameBack = list.getJSONObject(0).getString("shop_name");
            }
        }
        return nameBack;
    }

    /**
     * 接待人id与name转化
     */
    public String authNameTransformId(String shopName, String type) {
        String shopId = "";
        JSONObject response = jc.authListPage(type, "-1");
        JSONArray list = response.getJSONArray("list");
        for (int i = 0; i < list.size(); i++) {
            String name = list.getJSONObject(i).getString("name");
            if (shopName.equals(name)) {
                shopId = list.getJSONObject(i).getString("id");
            } else if (shopName.equals("集团管理")) {
                shopId = list.getJSONObject(0).getString("id");
            } else if (i == list.size() - 1 && shopId.equals("")) {
                shopId = list.getJSONObject(0).getString("id");
            }
        }
        return shopId;
    }


    /**
     * 遍历接待人的名字，判断是否存在此接待人
     */
    public String getAuthNameExist(String name, String type) {
        String nameBack = "";
        JSONArray list = jc.authListPage(type, "-1").getJSONArray("list");
        for (int i = 0; i < list.size(); i++) {
            String shopName = list.getJSONObject(i).getString("name");
            if (name.equals(shopName)) {
                nameBack = shopName;
            } else if (i == list.size() - 1 && nameBack.isEmpty()) {
                nameBack = list.getJSONObject(0).getString("name");
            }
        }
        return nameBack;
    }

    /**
     * 获取进行中的优惠券
     */
    public Long voucherWorking() {
        //进行中的优惠券ID
        Long activityId = 0L;
        //优惠券列表
        JSONObject response = jc.voucherPageFilterManage("1", "10", "", "");
        int pages = response.getInteger("pages");
        for (int page = 1; page <= pages; page++) {
            JSONArray list = jc.voucherPageFilterManage(String.valueOf(page), "10", "", "").getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                String status = list.getJSONObject(i).getString("voucher_status_name");
                int surplusInventory = list.getJSONObject(i).getInteger("surplus_inventory");
                if (status.equals(VoucherStatusEnum.WORKING.getName()) && surplusInventory > 0) {
                    activityId = list.getJSONObject(i).getLong("voucher_id");
                }
            }
        }
        return activityId;
    }


    /**
     * 获取活动的的名称
     * 2021-3-17
     */
    public String getActivityTitle(Long id) {
        IScene scene = ActivityManagePageScene.builder().page(1).size(10).build();
        int pages = scene.visitor(visitor).execute().getInteger("pages");
        String title = "";
        for (int page = 1; page <= pages; page++) {
            IScene scene2 = ActivityManagePageScene.builder().page(page).size(10).build();
            JSONArray list = scene2.visitor(visitor).execute().getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                Long activityId = list.getJSONObject(i).getLong("id");
                if (activityId.equals(id)) {
                    title = list.getJSONObject(i).getString("title");
                }
            }
        }
        return title;
    }

    /**
     * 获取领取渠道的相关的字段，以及字段的转化
     */
    public String enumMapSendChannelList(String sendChannelName) {
        JSONArray list = jc.enummap().getJSONArray("SEND_CHANNEL_LIST");
        String key = "";
        for (int i = 0; i < list.size(); i++) {
            String value = list.getJSONObject(i).getString("value");
            if (value.equals(sendChannelName)) {
                key = list.getJSONObject(i).getString("key");
                System.err.println(key);
            }
        }
        return key;
    }

    /**
     * 通过品牌名称获取品牌的ID
     */
    public String brandPageExchange(String brandNme) {
        JSONObject response = jc.brandListFilterManage3("", "1", "10", "");
        int pages = response.getInteger("pages");
        String brandId = "";
        for (int page = 1; page <= pages; page++) {
            JSONArray list = jc.brandListFilterManage3("", String.valueOf(page), "10", "").getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                String name = list.getJSONObject(i).getString("name");
                if (name.equals(brandNme)) {
                    brandId = list.getJSONObject(i).getString("id");
                    System.err.println(brandId);
                }
            }
        }
        return brandId;
    }

    /**
     * 判断小程序中小喇叭中卡券的状态
     */
    public String articleVoucher(Long activityId) {
        //获取此活动对应的小程序ID
        Long id = appointmentActivityId(activityId);
        //查看小喇叭中的优惠券
        IScene scene2 = ArticleVoucherList.builder().id(id).build();
        String isReceived = scene2.visitor(visitor).execute().getJSONArray("list").getJSONObject(0).getString("is_received");
        return isReceived;
    }

    /**
     * 判断小程序中小喇叭的状态
     */
    public JSONObject articleVoucherData(Long activityId) {
        //获取此活动对应的
        Long id = appointmentActivityId(activityId);
        //查看小喇叭中的优惠券
        IScene scene2 = ArticleVoucherList.builder().id(id).build();
        JSONObject object = scene2.visitor(visitor).execute();
        return object;
    }

    /**
     * 主体类型的查询
     */
    public String getSubjectList(String subjectName) {
        //定义主体的字段
        String subjectKey = "";
        //获取主题状态的列表
        JSONArray list = jc.subjectList().getJSONArray("list");
        for (int i = 0; i < list.size(); i++) {
            String subjectValue = list.getJSONObject(i).getString("subject_value");
            if (subjectValue.equals(subjectName)) {
                subjectKey = list.getJSONObject(i).getString("subject_key");
            }
        }
        return subjectKey;
    }


    /**
     * ------------------------------------------矫辰3.1新增的方法---------------------------------------------
     */

    /**
     * 构建招募活动
     * 标签的状态:0-优惠,1-特价,2-福利,3-红包,4-礼品,5-礼品,6-热销,7-推荐
     */
    public IScene getContentMarketingAddScene(int participationType, List<String> chooseLabels, int labelNum, int actionPoint) {
        SceneUtil supporterUtil = new SceneUtil(visitor);
        List<String> picList = new ArrayList<>();
        picList.add(0, getPicPath());
        String[][] label = {{"CAR_WELFARE", "车福利"}, {"CAR_INFORMATION", "车资讯"}, {"CAR_LIFE", "车生活"}, {"CAR_ACVITITY", "车活动"}, {"CAR_KNOWLEDGE", "车知识"}};
        IScene scene = ManageContentMarketingAddScene.builder()
                .type(3)
                .participationLimitType(participationType)
                .chooseLabels(chooseLabels)
                .title(pp.contentMarketingName)
                .rule(pp.rule)
                .startDate(getStartDate())
                .endDate(getEndDate())
                .subjectType(supporterUtil.getSubjectType())
                .label(label[labelNum][0])
                .picList(picList)
                .actionPoint(actionPoint)
                .build();
        return scene;

    }

    /**
     * 创建内容营销活动，返回活动ID
     */
    public Long getContentMarketingAdd() {
        SceneUtil supporterUtil = new SceneUtil(visitor);
        List<String> picList = new ArrayList<>();
        picList.add(0, getPicPath());
        String[][] label = {{"CAR_WELFARE", "车福利"}, {"CAR_INFORMATION", "车资讯"}, {"CAR_LIFE", "车生活"}, {"CAR_ACVITITY", "车活动"}, {"CAR_KNOWLEDGE", "车知识"}};
        IScene scene = ManageContentMarketingAddScene.builder()
                .type(3)
                .participationLimitType(0)
                .title(pp.contentMarketingName)
                .rule(pp.rule)
                .startDate(getStartDate())
                .endDate(getEndDate())
                .subjectType(supporterUtil.getSubjectType())
                .label(label[0][0])
                .picList(picList)
                .actionPoint(1)
                .build();
        Long activityId = scene.visitor(visitor).execute().getLong("id");
        return activityId;
    }

    /**
     * 创建【未开始】招募活动，返回活动ID
     */
    public Long getContentMarketingNotStar() {
        SceneUtil supporterUtil = new SceneUtil(visitor);
        List<String> picList = new ArrayList<>();
        picList.add(0, getPicPath());
        String[][] label = {{"CAR_WELFARE", "车福利"}, {"CAR_INFORMATION", "车资讯"}, {"CAR_LIFE", "车生活"}, {"CAR_ACVITITY", "车活动"}, {"CAR_KNOWLEDGE", "车知识"}};
        IScene scene = ManageContentMarketingAddScene.builder()
                .type(3)
                .participationLimitType(0)
                .title(pp.contentMarketingName)
                .rule(pp.rule)
                .startDate(getEndDate())
                .endDate(getEndDate())
                .subjectType(supporterUtil.getSubjectType())
                .label(label[0][0])
                .picList(picList)
                .actionPoint(1)
                .build();
        Long activityId = scene.visitor(visitor).execute().getLong("id");
        return activityId;
    }

    /**
     * 查询列表中的状态为【待审核的ID】---内容营销
     */
    public List<Long> getContentMarketingWaitingApproval() {
        List<Long> ids = new ArrayList<>();
        //活动列表
        IScene scene = ActivityManagePageScene.builder().page(1).size(10).build();
        int pages = scene.visitor(visitor).execute().getInteger("pages");
        for (int page = 1; page <= pages; page++) {
            IScene scene1 = ActivityManagePageScene.builder().page(page).size(10).build();
            JSONArray list = scene1.visitor(visitor).execute().getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                int status = list.getJSONObject(i).getInteger("status");
                int activityType = list.getJSONObject(i).getInteger("activity_type");
                if (status == ActivityStatusEnum.PENDING.getId() && activityType == 3) {
                    Long id = list.getJSONObject(i).getLong("id");
                    ids.add(id);
                }
            }
        }
        if (ids.size() == 0) {
            Long id1 = getContentMarketingAdd();
            ids.add(id1);
        }
        return ids;
    }

    /**
     * 查询列表中的状态为【已撤销的ID】---内容营销
     * 2021-3-17
     */
    public List<Long> getContentMarketingRevoke() {
        List<Long> ids = new ArrayList<>();
        //活动列表
        IScene activityManageListScene = ActivityManagePageScene.builder().page(1).size(10).build();
        int pages = activityManageListScene.visitor(visitor).execute().getInteger("pages");
        for (int page = 1; page <= pages; page++) {
            IScene scene1 = ActivityManagePageScene.builder().page(page).size(10).build();
            JSONArray list = scene1.visitor(visitor).execute().getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                int status = list.getJSONObject(i).getInteger("status");
                int activityType = list.getJSONObject(i).getInteger("activity_type");
                if (status == ActivityStatusEnum.REVOKE.getId() && activityType == 3) {
                    Long id = list.getJSONObject(i).getLong("id");
                    System.err.println(status + "-------" + id);
                    ids.add(id);
                }
            }
        }
        if (ids.size() == 0) {
            Long id1 = getContentMarketingAdd();
            getRevokeActivity(id1);
            ids.add(id1);
        }
        return ids;
    }

    /**
     * 查询列表中的状态为【审核未通过的ID】--内容营销
     */
    public List<Long> getContentMarketingReject() {
        List<Long> ids = new ArrayList<>();
        //活动列表
        IScene scene = ActivityManagePageScene.builder().page(1).size(10).build();
        int pages = scene.visitor(visitor).execute().getInteger("pages");
        for (int page = 1; page <= pages; page++) {
            IScene scene1 = ActivityManagePageScene.builder().page(page).size(10).build();
            JSONArray list = scene1.visitor(visitor).execute().getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                int status = list.getJSONObject(i).getInteger("status");
                int activityType = list.getJSONObject(i).getInteger("activity_type");
                if (status == ActivityStatusEnum.REJECT.getId() && activityType == 3) {
                    Long id = list.getJSONObject(i).getLong("id");
                    System.err.println(status + "-----" + id);
                    ids.add(id);
                }
            }
        }
        //创建活动并审批不通过
        if (ids.size() == 0) {
            Long id1 = getContentMarketingAdd();
            getApprovalReject(id1);
            ids.add(id1);
        }
        return ids;
    }

    /**
     * 查询列表中的状态为【审核已取消的ID】--招募活动
     */
    public List<Long> getContentMarketingCancel() {
        List<Long> ids = new ArrayList<>();
        //活动列表
        IScene scene = ActivityManagePageScene.builder().page(1).size(10).build();
        int pages = scene.visitor(visitor).execute().getInteger("pages");
        for (int page = 1; page <= pages; page++) {
            IScene scene1 = ActivityManagePageScene.builder().page(page).size(10).build();
            JSONArray list = scene1.visitor(visitor).execute().getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                int status = list.getJSONObject(i).getInteger("status");
                int activityType = list.getJSONObject(i).getInteger("activity_type");
                if (status == ActivityStatusEnum.CANCELED.getId() && activityType == 3) {
                    Long id = list.getJSONObject(i).getLong("id");
                    ids.add(id);
                }
            }
        }
        //创建活动-审批通过活动-取消活动
        if (ids.size() == 0) {
            //创建活动
            Long id = getContentMarketingAdd();
            //审批通过
            getApprovalReject(id);
            //取消活动
            getCancelActivity(id);
            ids.add(id);
        }
        return ids;
    }

    /**
     * 取消的活动，进行恢复
     */
    public String getContentMarketingRecover(Long id) {
        IScene scene = ManageRecoveryScene.builder().id(id).build();
        String message = scene.visitor(visitor).getResponse().getMessage();
        return message;
    }


    /**
     * 裂变活动-查询活动列表中的状态为【进行中的ID】
     */
    public List<Long> getContentMarketingWorking() {
        List<Long> ids = new ArrayList<>();
        //活动列表
        IScene scene = ActivityManagePageScene.builder().page(1).size(10).build();
        int pages = scene.visitor(visitor).execute().getInteger("pages");
        for (int page = 1; page <= pages; page++) {
            IScene scene1 = ActivityManagePageScene.builder().page(page).size(10).build();
            JSONArray list = scene1.visitor(visitor).execute().getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                int status = list.getJSONObject(i).getInteger("status");
                int activityType = list.getJSONObject(i).getInteger("activity_type");
                if (status == ActivityStatusEnum.PASSED.getId() && activityType == 3) {
                    Long id = list.getJSONObject(i).getLong("id");
                    ids.add(id);
                }
            }
        }
        //创建活动并审批
        if (ids.size() == 0) {
            Long id1 = getContentMarketingAdd();
            //审批通过
            getApprovalPassed(id1);
            ids.add(id1);

        }
        return ids;
    }

    /**
     * 编辑内容营销活动，返回活动ID
     */
    public String getContentMarketingEdit(Long id, String title, String rule) {
        SceneUtil supporterUtil = new SceneUtil(visitor);
        List<String> picList = new ArrayList<>();
        picList.add(0, getPicPath());
        String[][] label = {{"CAR_WELFARE", "车福利"}, {"CAR_INFORMATION", "车资讯"}, {"CAR_LIFE", "车生活"}, {"CAR_ACVITITY", "车活动"}, {"CAR_KNOWLEDGE", "车知识"}};
        IScene scene = ManageContentMarketingEditScene.builder()
                .id(id)
                .type(3)
                .participationLimitType(0)
                .title(title)
                .rule(rule)
                .startDate(getStartDate())
                .endDate(getEndDate())
                .subjectType(supporterUtil.getSubjectType())
                .label(label[0][0])
                .picList(picList)
                .actionPoint(1)
                .build();
        String message = scene.visitor(visitor).getResponse().getMessage();
        return message;
    }

    /**
     * 活动下架
     */
    public String getContentMarketingOffLine(Long id) {
        IScene scene = ManageOfflineScene.builder().id(id).build();
        String message = scene.visitor(visitor).getResponse().getMessage();
        return message;
    }

    /**
     * 活动上架
     */
    public String getContentMarketingOnline(Long id) {
        IScene scene = ManageOnlineScene.builder().id(id).build();
        String message = scene.visitor(visitor).getResponse().getMessage();
        return message;
    }


    /**
     * 内容营销-查询列表中的状态为【未开始的ID】
     * 2021-3-17
     */
    public List<Long> getContentMarketingWaitingStar() {
        List<Long> ids = new ArrayList<>();
        //活动列表
        IScene scene = ActivityManagePageScene.builder().page(1).size(10).build();
        int pages = scene.visitor(visitor).execute().getInteger("pages") > 10 ? 10 : scene.visitor(visitor).execute().getInteger("pages");
        for (int page = 1; page <= pages; page++) {
            IScene scene1 = ActivityManagePageScene.builder().page(page).size(10).build();
            JSONArray list = scene1.visitor(visitor).execute().getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                int status = list.getJSONObject(i).getInteger("status");
                int activityType = list.getJSONObject(i).getInteger("activity_type");
                if (status == ActivityStatusEnum.WAITING_START.getId() && activityType == 3) {
                    Long id = list.getJSONObject(i).getLong("id");
                    ids.add(id);
                }
            }
        }
        //创建活动并审批
        if (ids.size() == 0) {
            //创建活动
            Long id1 = getContentMarketingNotStar();
            //审批活动
            getApprovalPassed(id1);
            ids.add(id1);

        }
        return ids;
    }

    /**
     * 招募活动-查询列表中的状态为【已过期的ID】---招募活动
     * 2021-3-17
     */
    public List<Long> geContentMarketingFinish() {
        List<Long> ids = new ArrayList<>();
        //活动列表
        IScene scene = ActivityManagePageScene.builder().page(1).size(10).build();
        int pages = scene.visitor(visitor).execute().getInteger("pages");
        for (int page = 1; page <= pages; page++) {
            IScene scene1 = ActivityManagePageScene.builder().page(page).size(10).build();
            JSONArray list = scene1.visitor(visitor).execute().getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                int status = list.getJSONObject(i).getInteger("status");
                int activityType = list.getJSONObject(i).getInteger("activity_type");
                if (status == ActivityStatusEnum.FINISH.getId() && activityType == 3) {
                    Long id = list.getJSONObject(i).getLong("id");
                    ids.add(id);
                }
            }
        }
        return ids;
    }

    /**
     * 内容营销-查询列表中的状态为【未开始的ID】
     * 2021-3-17
     */
    public List<Long> getContentMarketingOffLine() {
        List<Long> ids = new ArrayList<>();
        //活动列表
        IScene scene = ActivityManagePageScene.builder().page(1).size(10).build();
        int pages = scene.visitor(visitor).execute().getInteger("pages") > 10 ? 10 : scene.visitor(visitor).execute().getInteger("pages");
        for (int page = 1; page <= pages; page++) {
            IScene scene1 = ActivityManagePageScene.builder().page(page).size(10).build();
            JSONArray list = scene1.visitor(visitor).execute().getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                int status = list.getJSONObject(i).getInteger("status");
                int activityType = list.getJSONObject(i).getInteger("activity_type");
                if (status == 701 && activityType == 3) {
                    Long id = list.getJSONObject(i).getLong("id");
                    ids.add(id);
                }
            }
        }
        //创建活动并审批
        if (ids.size() == 0) {
            //创建活动
            Long id1 = getContentMarketingNotStar();
            //审批活动
            getApprovalPassed(id1);
            //活动下架
            getContentMarketingOffLine(id1);
            ids.add(id1);

        }
        return ids;
    }

    /**
     * 裂变活动-查询列表中的状态为【已下架的ID】
     * 2021-3-17
     */
    public List<Long> getFissionActivityOffLine() {
        List<Long> ids = new ArrayList<>();
        //活动列表
        IScene scene = ActivityManagePageScene.builder().page(1).size(10).build();
        int pages = scene.visitor(visitor).execute().getInteger("pages") > 10 ? 10 : scene.visitor(visitor).execute().getInteger("pages");
        for (int page = 1; page <= pages; page++) {
            IScene scene1 = ActivityManagePageScene.builder().page(page).size(10).build();
            JSONArray list = scene1.visitor(visitor).execute().getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                int status = list.getJSONObject(i).getInteger("status");
                int activityType = list.getJSONObject(i).getInteger("activity_type");
                if (status == ActivityStatusEnum.OFFLINE.getId() && activityType == 1) {
                    Long id = list.getJSONObject(i).getLong("id");
                    ids.add(id);
                }
            }
        }
        //创建活动并审批
        if (ids.size() == 0) {
            //创建活动
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            Long id1 = createFissionActivity(voucherId);
            //审批活动
            getApprovalPassed(id1);
            //活动下架
            getContentMarketingOffLine(id1);
            ids.add(id1);

        }
        return ids;
    }

    /**
     * 招募活动-查询列表中的状态为【已下架的ID】
     * 2021-3-17
     */
    public List<Long> getRecruitActivityOffLine() {
        List<Long> ids = new ArrayList<>();
        //活动列表
        IScene scene = ActivityManagePageScene.builder().page(1).size(10).build();
        int pages = scene.visitor(visitor).execute().getInteger("pages") > 10 ? 10 : scene.visitor(visitor).execute().getInteger("pages");
        for (int page = 1; page <= pages; page++) {
            IScene scene1 = ActivityManagePageScene.builder().page(page).size(10).build();
            JSONArray list = scene1.visitor(visitor).execute().getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                int status = list.getJSONObject(i).getInteger("status");
                int activityType = list.getJSONObject(i).getInteger("activity_type");
                if (status == ActivityStatusEnum.OFFLINE.getId() && activityType == 2) {
                    Long id = list.getJSONObject(i).getLong("id");
                    ids.add(id);
                }
            }
        }
        //创建活动并审批
        if (ids.size() == 0) {
            //创建活动
            Long id1 = createRecruitActivityApproval();
            //审批活动
            getApprovalPassed(id1);
            //活动下架
            getContentMarketingOffLine(id1);
            ids.add(id1);
        }
        return ids;
    }

    /**
     * 裂变活动-创建未开始的活动
     */
    public Long createFissionActivityWaitingStarScene(Long voucherId) {
        SceneUtil supporterUtil = new SceneUtil(visitor);
        PublicParameter pp = new PublicParameter();
        List<String> picList = new ArrayList<>();
        picList.add(supporterUtil.getPicPath());
        int AllowUseInventory = getVoucherAllowUseInventory(voucherId);
        Long activityId = 0L;
        if (AllowUseInventory > 6) {
            // 创建被邀请者和分享者的信息字段
            JSONObject invitedVoucher = getInvitedVoucher(voucherId, 1, String.valueOf(Math.min(getVoucherAllowUseInventory(voucherId), 2)), 2, "", "", 3);
            JSONObject shareVoucher = getShareVoucher(voucherId, 1, String.valueOf(Math.min(getVoucherAllowUseInventory(voucherId), 2)), 2, "", "", 3);
            IScene scene = FissionVoucherAddScene.builder()
                    .type(1)
                    .participationLimitType(0)
                    .receiveLimitType(0)
                    .title(pp.fissionVoucherName)
                    .rule(pp.rule)
                    .startDate(getEndDate())
                    .endDate(getEndDate())
                    .subjectType(supporterUtil.getSubjectType())
                    .subjectId(supporterUtil.getSubjectDesc(supporterUtil.getSubjectType()))
                    .label("CAR_WELFARE")
                    .picList(picList)
                    .shareNum("3")
                    .shareVoucher(shareVoucher)
                    .invitedVoucher(invitedVoucher)
                    .isCustomShareInfo(false)
                    .build();
            activityId = scene.visitor(visitor).execute().getLong("id");
        } else {
            //创建卡券
            Long voucherId3 = supporterUtil.createVoucherId(1000, VoucherTypeEnum.COUPON);
            //获取卡券的名字
            String voucherName = supporterUtil.getVoucherName(voucherId3);
            //审批通过
            supporterUtil.applyVoucher(voucherName, "1");
            //创建活动
            // 创建被邀请者和分享者的信息字段
            JSONObject invitedVoucher = getInvitedVoucher(voucherId3, 1, String.valueOf(Math.min(getVoucherAllowUseInventory(voucherId), 2)), 2, "", "", 3);
            JSONObject shareVoucher = getShareVoucher(voucherId3, 1, String.valueOf(Math.min(getVoucherAllowUseInventory(voucherId), 2)), 2, "", "", 3);
            IScene scene = FissionVoucherAddScene.builder()
                    .type(1)
                    .participationLimitType(0)
                    .receiveLimitType(0)
                    .title(pp.fissionVoucherName)
                    .rule(pp.rule)
                    .startDate(getEndDate())
                    .endDate(getEndDate())
                    .subjectType(supporterUtil.getSubjectType())
                    .subjectId(supporterUtil.getSubjectDesc(supporterUtil.getSubjectType()))
                    .label("CAR_WELFARE")
                    .picList(picList)
                    .shareNum("3")
                    .shareVoucher(shareVoucher)
                    .invitedVoucher(invitedVoucher)
                    .isCustomShareInfo(false)
                    .build();
            activityId = scene.visitor(visitor).execute().getLong("id");
        }
        return activityId;
    }


}
