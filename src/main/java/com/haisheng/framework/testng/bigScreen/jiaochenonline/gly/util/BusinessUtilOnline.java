package com.haisheng.framework.testng.bigScreen.jiaochenonline.gly.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.util.BasicUtil;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumAppletToken;
import com.haisheng.framework.testng.bigScreen.jiaochen.gly.util.PublicParameter;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumAccount;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.activity.ManagePageBean;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.activity.ManageRegisterPageBean;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.activity.ActivityApprovalStatusEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.activity.ActivityStatusEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.activity.RegisterInfoEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing.VoucherStatusEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing.VoucherTypeEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.generate.voucher.VoucherGenerator;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.activity.*;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.activity.*;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.brand.BrandPageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.file.FileUploadScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.userange.SubjectListScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.vouchermanage.VoucherDetailScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.vouchermanage.VoucherFormVoucherPageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.util.SceneUtil;
import com.haisheng.framework.util.DateTimeUtil;
import com.haisheng.framework.util.ImageUtil;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.*;

public class BusinessUtilOnline extends BasicUtil {
    private final VisitorProxy visitor;
    private final SceneUtil user;

    public BusinessUtilOnline(VisitorProxy visitor) {
        super(visitor);
        this.visitor = visitor;
        this.user = new SceneUtil(visitor);
    }

    PublicParameter pp = new PublicParameter();
    public String shopId = "-1";

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
        return new JSONArray();
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
        return (int) (Math.random() * 10000);
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
     * 判断可用库存
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
     * 构建裂变活动
     *
     * @param voucherId 包含卡券信息
     * @return IScene
     */
    public IScene createFissionActivityScene(Long voucherId) {
        SceneUtil supporterUtil = new SceneUtil(visitor);
        PublicParameter pp = new PublicParameter();
        List<String> picList = new ArrayList<>();
        picList.add(supporterUtil.getPicPath());
        IScene scene;
        int AllowUseInventory = getVoucherAllowUseInventory(voucherId);
        if (AllowUseInventory > 6) {
            // 创建被邀请者和分享者的信息字段
            JSONObject invitedVoucher = getInvitedVoucher(voucherId, 1, String.valueOf(Math.min(getVoucherAllowUseInventory(voucherId), 2)), 2, "", "", 3);
            JSONObject shareVoucher = getShareVoucher(voucherId, 1, String.valueOf(Math.min(getVoucherAllowUseInventory(voucherId), 2)), 2, "", "", 3);
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
            JSONObject invitedVoucher = getInvitedVoucher(voucherId3, 1, String.valueOf(Math.min(getVoucherAllowUseInventory(voucherId), 2)), 2, "", "", 3);
            JSONObject shareVoucher = getShareVoucher(voucherId3, 1, String.valueOf(Math.min(getVoucherAllowUseInventory(voucherId), 2)), 2, "", "", 3);
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
        return scene.visitor(visitor).execute().getLong("id");
    }

    /**
     * 编辑裂变活动
     */
    public IScene fissionActivityEditScene(Long activityId) {
        Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
        SceneUtil supporterUtil = new SceneUtil(visitor);
        PublicParameter pp = new PublicParameter();
        List<String> picList = new ArrayList<>();
        picList.add(supporterUtil.getPicPath());
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
        Long id = scene.visitor(visitor).execute().getLong("id");
        Preconditions.checkNotNull(id, "创建失败");
        return id;
    }

    /**
     * 活动管理-创建招募活动--创建未开始的活动
     * 2021-3-17
     */
    public Long createRecruitActivity() {
        Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
        IScene scene = createRecruitActivityScene(voucherId, true, 0, true, getEndDate(), getEndDate());
        Long id = scene.visitor(visitor).execute().getLong("id");
        Preconditions.checkNotNull(id, "创建失败");
        return id;
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
        ManageRecruitAddScene.ManageRecruitAddSceneBuilder builder;
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
        JSONArray registerObject = getRewardVouchers(voucherId, 1, Math.min(getVoucherAllowUseInventory(voucherId), 2));
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
     * 编辑活动
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
        JSONArray registerObject = getRewardVouchers(voucherId, 1, Math.min(getVoucherAllowUseInventory(voucherId), 2));
        //卡券有效期
        JSONObject voucherValid = getVoucherValid(2, "", "", 10);
        IScene scene = ManageRecruitEditScene.builder()
                .type(2)
                .id(id)
                .title(pp.editTitle)
                .rule(pp.EditRule)
                .participationLimitType(0)
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
        return scene.visitor(visitor).getResponse().getMessage();
    }

    /**
     * 获取图片地址
     *
     * @return 图片地址
     */
    public String getPicPath() {
        String path = "src/main/java/com/haisheng/framework/testng/bigScreen/jiaochen/wm/multimedia/picture/卡券图.jpg";
        String picture = new ImageUtil().getImageBinary(path);
        IScene scene = FileUploadScene.builder().isPermanent(false).permanentPicType(0).pic(picture).ratio(1.5).build();
        return scene.visitor(visitor).execute().getString("pic_path");
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
        return scene.visitor(visitor).execute().getString("par_value");
    }

    /**
     * 获取优惠券的成本
     */
    public String getCost(Long id) {
        IScene scene = VoucherDetailScene.builder().id(id).build();
        return scene.visitor(visitor).execute().getString("cost");
    }

    //--------------------------------活动列表中活动状态--------------------------------------------

    /**
     * 查询列表中的状态为【待审核的ID】---招募活动
     */
    public ManagePageBean getRecruitActivityWaitingApproval() {
        ManagePageBean managePageBean = getActivity(ActivityStatusEnum.PENDING, 2);
        if (managePageBean != null) {
            return managePageBean;
        }
        createRecruitActivityApproval();
        return getRecruitActivityWaitingApproval();
    }

    /**
     * 查询列表中的状态为【待审核的ID】---裂变活动
     */
    public ManagePageBean getFissionActivityWaitingApproval() {
        ManagePageBean managePageBean = getActivity(ActivityStatusEnum.PENDING, 1);
        if (managePageBean != null) {
            return managePageBean;
        }
        Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
        createFissionActivity(voucherId);
        return getFissionActivityWaitingApproval();
    }

    /**
     * 查询活动列表中的状态为【进行中的ID】
     */
    public ManagePageBean getActivityWorking() {
        ManagePageBean managePageBean = getActivity(ActivityStatusEnum.PASSED, null);
        if (managePageBean != null) {
            return managePageBean;
        }
        Long id = createRecruitActivityApproval();
        getApprovalPassed(id);
        return getActivityWorking();
    }

    /**
     * 裂变活动-查询活动列表中的状态为【进行中的ID】
     */
    public ManagePageBean getFissionActivityWorking() {
        //活动列表
        ManagePageBean managePageBean = getActivity(ActivityStatusEnum.PASSED, 1);
        if (managePageBean != null) {
            return managePageBean;
        }
        //创建活动并审批
        Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
        createFissionActivity(voucherId);
        return getFissionActivityWorking();
    }

    /**
     * 招募活动-查询列表中的状态为【进行中的ID】
     */
    public ManagePageBean getRecruitActivityWorking() {
        //活动列表
        ManagePageBean managePageBean = getActivity(ActivityStatusEnum.PASSED, 2);
        if (managePageBean != null) {
            return managePageBean;
        }
        //创建活动
        Long id = createRecruitActivityApproval();
        //审批活动
        getApprovalPassed(id);
        return getRecruitActivityWorking();
    }

    /**
     * 招募活动-查询列表中的状态为【进行中】的活动-存在待审批的人数
     */
    public ManagePageBean getRecruitActivityWorkingApproval() {
        IScene scene = ActivityManagePageScene.builder().status(ActivityStatusEnum.PASSED.getId()).build();
        List<ManagePageBean> managePageBeanList = toJavaObjectList(scene, ManagePageBean.class, "activity_type", 2);
        ManagePageBean managePageBean = managePageBeanList.stream().filter(e -> e.getWaitingAuditNum() >= 1).findFirst().orElse(null);
        if (managePageBean != null) {
            return managePageBean;
        }
        //创建活动
        Long id = createRecruitActivityApproval();
        //审批活动
        getApprovalPassed(id);
        //小程序报名
        user.loginApplet(EnumAppletToken.JC_GLY_ONLINE);
        activityRegisterApplet(id, "13373166806", "郭丽雅", 2, "1513814362@qq.com", "22", "女", "其他");
        return getRecruitActivityWorkingApproval();
    }

    /**
     * 查询列表中的状态为【审核未通过的ID】
     */
    public ManagePageBean getActivityReject() {
        //活动列表
        ManagePageBean managePageBean = getActivity(ActivityStatusEnum.REJECT, null);
        if (managePageBean != null) {
            return managePageBean;
        }
        //创建活动并审批不通过
        Long id = createRecruitActivityApproval();
        getApprovalReject(id);
        return getActivityReject();
    }

    /**
     * 查询列表中的状态为【审核未通过的ID】--招募活动
     */
    public ManagePageBean getRecruitActivityReject() {
        //活动列表
        ManagePageBean managePageBean = getActivity(ActivityStatusEnum.REJECT, 2);
        if (managePageBean != null) {
            return managePageBean;
        }
        Long id = createRecruitActivityApproval();
        getApprovalReject(id);
        return getRecruitActivityReject();
    }

    /**
     * 查询列表中的状态为【审核未通过的ID】
     */
    public ManagePageBean getFissionActivityReject() {
        ManagePageBean managePageBean = getActivity(ActivityStatusEnum.REJECT, 1);
        if (managePageBean != null) {
            return managePageBean;
        }
        Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
        Long id = createFissionActivity(voucherId);
        getApprovalReject(id);
        return getFissionActivityReject();
    }

    /**
     * 查询列表中的状态为【待审批的ID】--裂变活动
     */
    public ManagePageBean getFissionActivityWait() {
        ManagePageBean managePageBean = getActivity(ActivityStatusEnum.PENDING, 1);
        if (managePageBean != null) {
            return managePageBean;
        }
        Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
        createFissionActivity(voucherId);
        return getFissionActivityWait();
    }

    /**
     * 查询列表中的状态为【待审批的ID】--招募活动
     */
    public ManagePageBean getActivityWait() {
        ManagePageBean managePageBean = getActivity(ActivityStatusEnum.PENDING, 2);
        if (managePageBean != null) {
            return managePageBean;
        }
        createRecruitActivityApproval();
        return getActivityWait();
    }

    /**
     * 查询列表中的状态为【审核已取消的ID】
     */
    public ManagePageBean getActivityCancel() {
        ManagePageBean managePageBean = getActivity(ActivityStatusEnum.CANCELED, null);
        if (managePageBean != null) {
            return managePageBean;
        }
        //创建活动
        Long id = createRecruitActivityApproval();
        //审批通过
        getApprovalReject(id);
        //取消活动
        getCancelActivity(id);
        return getActivityCancel();
    }

    /**
     * 查询列表中的状态为【审核已取消的ID】--招募活动
     */
    public ManagePageBean getRecruitActivityCancel() {
        ManagePageBean managePageBean = getActivity(ActivityStatusEnum.CANCELED, 2);
        if (managePageBean != null) {
            return managePageBean;
        }
        //创建活动-审批通过活动-取消活动
        Long id = createRecruitActivityApproval();
        //审批通过
        getApprovalReject(id);
        //取消活动
        getCancelActivity(id);
        return getRecruitActivityCancel();
    }

    /**
     * 查询列表中的状态为【审核已取消的ID】--裂变活动
     */
    public ManagePageBean getFissionActivityCancel() {
        ManagePageBean managePageBean = getActivity(ActivityStatusEnum.CANCELED, 1);
        if (managePageBean != null) {
            return managePageBean;
        }
        Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
        Long id = createFissionActivity(voucherId);
        //审批通过
        getApprovalReject(id);
        //取消活动
        getCancelActivity(id);
        return getFissionActivityCancel();
    }

    /**
     * 查询列表中的状态为【已撤销的ID】----招募活动
     * 2021-3-17
     */
    public ManagePageBean getRecruitActivityRevoke() {
        ManagePageBean managePageBean = getActivity(ActivityStatusEnum.REVOKE, 2);
        if (managePageBean != null) {
            return managePageBean;
        }
        Long id = createRecruitActivityApproval();
        getRevokeActivity(id);
        return getRecruitActivityRevoke();
    }

    /**
     * 查询列表中的状态为【已撤销的ID】----裂变活动
     * 2021-3-17
     */
    public ManagePageBean getFissionActivityRevoke() {
        ManagePageBean managePageBean = getActivity(ActivityStatusEnum.REVOKE, 1);
        if (managePageBean != null) {
            return managePageBean;
        }
        Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
        Long id = createFissionActivity(voucherId);
        getRevokeActivity(id);
        return getFissionActivityRevoke();
    }

    /**
     * 招募活动-查询列表中的状态为【未开始的ID】---招募活动
     * 2021-3-17
     */
    public ManagePageBean getRecruitActivityWaitingStar() {
        ManagePageBean managePageBean = getActivity(ActivityStatusEnum.WAITING_START, 2);
        if (managePageBean != null) {
            return managePageBean;
        }
        //创建活动
        Long id = createRecruitActivity();
        //审批活动
        getApprovalPassed(id);
        return getRecruitActivityWaitingStar();
    }

    /**
     * 查询列表中的状态为【未开始的ID】---裂变活动
     * 2021-3-17
     */
    public ManagePageBean getFissionActivityWaitingStar() {
        ManagePageBean managePageBean = getActivity(ActivityStatusEnum.WAITING_START, 1);
        if (managePageBean != null) {
            return managePageBean;
        }
        //创建活动
        Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
        Long id = createFissionActivityWaitingStarScene(voucherId);
        //审批活动
        getApprovalPassed(id);
        return getFissionActivityWaitingStar();
    }

    /**
     * 招募活动-查询列表中的状态为【已过期的ID】---招募活动
     * 2021-3-17
     */
    public ManagePageBean getRecruitActivityFinish() {
        return getActivity(ActivityStatusEnum.FINISH, 2);
    }

    /**
     * 招募活动-查询列表中的状态为【已过期的ID】---裂变活动
     * 2021-3-17
     */
    public ManagePageBean getFissionActivityFinish() {
        return getActivity(ActivityStatusEnum.FINISH, 1);
    }

    public ManagePageBean getActivityManagerPage(Long id) {
        IScene scene = ActivityManagePageScene.builder().build();
        return toJavaObject(scene, ManagePageBean.class, "id", id);
    }

    /**
     * 招募活动-查询列表中的状态为【已过期的ID】---招募活动
     * 2021-3-17
     */
    public ManagePageBean geContentMarketingFinish() {
        return getActivity(ActivityStatusEnum.FINISH, 3);
    }

    /**
     * 内容营销-查询列表中的状态为【未开始的ID】
     * 2021-3-17
     */
    public ManagePageBean getContentMarketingOffLine() {
        ManagePageBean managePageBean = getActivity(ActivityStatusEnum.OFFLINE, 3);
        if (managePageBean != null) {
            return managePageBean;
        }
        //创建活动
        Long id = getContentMarketingNotStar();
        //审批活动
        getApprovalPassed(id);
        //活动下架
        getContentMarketingOffLine(id);
        return getContentMarketingOffLine();
    }

    /**
     * 裂变活动-查询列表中的状态为【已下架的ID】
     * 2021-3-17
     */
    public ManagePageBean getFissionActivityOffLine() {
        ManagePageBean managePageBean = getActivity(ActivityStatusEnum.OFFLINE, 1);
        if (managePageBean != null) {
            return managePageBean;
        }
        //创建活动
        Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
        Long id = createFissionActivity(voucherId);
        //审批活动
        getApprovalPassed(id);
        //活动下架
        getContentMarketingOffLine(id);
        return getFissionActivityOffLine();
    }

    /**
     * 招募活动-查询列表中的状态为【已下架的ID】
     * 2021-3-17
     */
    public ManagePageBean getRecruitActivityOffLine() {
        ManagePageBean managePageBean = getActivity(ActivityStatusEnum.OFFLINE, 2);
        if (managePageBean != null) {
            return managePageBean;
        }
        Long id = createRecruitActivityApproval();
        //审批活动
        getApprovalPassed(id);
        //活动下架
        getContentMarketingOffLine(id);
        return getRecruitActivityOffLine();
    }

    /**
     * 查询列表中的状态为【待审核的ID】---内容营销
     */
    public ManagePageBean getContentMarketingWaitingApproval() {
        return getActivity(ActivityStatusEnum.PENDING, 3);
    }

    /**
     * 查询列表中的状态为【已撤销的ID】---内容营销
     * 2021-3-17
     */
    public ManagePageBean getContentMarketingRevoke() {
        return getActivity(ActivityStatusEnum.REVOKE, 3);
    }

    /**
     * 查询列表中的状态为【审核未通过的ID】--内容营销
     */
    public ManagePageBean getContentMarketingReject() {
        //活动列表
        ManagePageBean managePageBean = getActivity(ActivityStatusEnum.REJECT, 3);
        if (managePageBean != null) {
            return managePageBean;
        }
        Long id = getContentMarketingAdd();
        getApprovalReject(id);
        return getContentMarketingReject();
    }

    /**
     * 查询列表中的状态为【审核已取消的ID】--招募活动
     */
    public ManagePageBean getContentMarketingCancel() {
        ManagePageBean managePageBean = getActivity(ActivityStatusEnum.CANCELED, 3);
        if (managePageBean != null) {
            return managePageBean;
        }
        Long id = getContentMarketingAdd();
        getApprovalReject(id);
        getCancelActivity(id);
        return getContentMarketingCancel();
    }

    /**
     * 裂变活动-查询活动列表中的状态为【进行中的ID】
     */
    public ManagePageBean getContentMarketingWorking() {
        ManagePageBean managePageBean = getActivity(ActivityStatusEnum.PASSED, 3);
        if (managePageBean != null) {
            return managePageBean;
        }
        Long id = getContentMarketingAdd();
        getApprovalPassed(id);
        return getContentMarketingWorking();
    }

    /**
     * 内容营销-查询列表中的状态为【未开始的ID】
     * 2021-3-17
     */
    public ManagePageBean getContentMarketingWaitingStar() {
        ManagePageBean managePageBean = getActivity(ActivityStatusEnum.WAITING_START, 3);
        if (managePageBean != null) {
            return managePageBean;
        }
        Long id = getContentMarketingNotStar();
        //审批活动
        getApprovalPassed(id);
        return getContentMarketingWaitingStar();
    }

    /**
     * 查询活动
     *
     * @param status       活动状态
     * @param activityType 活动类型1：裂变 2：招募 3：营销
     * @return 结果
     */
    private ManagePageBean getActivity(@NotNull ActivityStatusEnum status, Integer activityType) {
        IScene scene = ActivityManagePageScene.builder().status(status.getId()).build();
        return activityType == null ? toFirstJavaObject(scene, ManagePageBean.class) : toJavaObject(scene, ManagePageBean.class, "activity_type", activityType);
    }

    //---------------------------------------------活动状态---------------------------------------------

    /**
     * 获取活动的的状态
     */
    public int getActivityStatus(Long id) {
        IScene scene = ActivityManagePageScene.builder().build();
        return toJavaObject(scene, JSONObject.class, "id", id).getInteger("status");
    }

    /**
     * 获取活动审批的的状态
     */
    public int getActivityApprovalStatus(Long id) {
        IScene scene = ActivityManagePageScene.builder().build();
        return toJavaObject(scene, JSONObject.class, "id", id).getInteger("approval_status");
    }

    /**
     * 活动管理-活动报名列表中待审批的ids
     */
    public List<Long> registerApproval(Long activityId, String type) {
        List<Long> list = new ArrayList<>();
        IScene scene = ManageRegisterPageScene.builder().activityId(activityId).build();
        if (type.equals("1")) {
            list.add(toJavaObject(scene, JSONObject.class, "status_name", "待审批").getLong("id"));
        } else {
            toJavaObjectList(scene, JSONObject.class, "status_name", "待审批").stream().map(e -> e.getLong("id")).forEach(list::add);
        }
        return list;
    }

    /**
     * 报名审批列表【待审批】ids的合集
     */
    public ManageRegisterPageBean registerAppletRegister(Long activityId) {
        IScene scene = ManageRegisterPageScene.builder().status(1).activityId(activityId).build();
        return toFirstJavaObject(scene, ManageRegisterPageBean.class);
    }

    /**
     * 活动管理-删除活动
     */
    public String getDelActivity(Long id) {
        IScene scene = ManageDeleteScene.builder().id(id).build();
        return scene.visitor(visitor).getResponse().getMessage();
    }

    /**
     * 活动管理-撤回活动申请
     */
    public String getRevokeActivity(Long id) {
        IScene scene = ManageRevokeScene.builder().id(id).build();
        return scene.visitor(visitor).getResponse().getMessage();
    }

    /**
     * 活动管理-取消活动
     */
    public String getCancelActivity(Long id) {
        IScene scene = ManageCancelScene.builder().id(id).build();
        return scene.visitor(visitor).getResponse().getMessage();
    }

    /**
     * 活动管理-推广活动
     */
    public String getPromotionActivity(Long id) {
        IScene scene = ManagePromotionScene.builder().id(id).build();
        return scene.visitor(visitor).execute().getString("applet_code_url");
    }

    /**
     * 活动管理-活动审批【通过】
     */
    public String getApprovalPassed(Long... id) {
        List<Long> ids = Arrays.asList(id);
        IScene scene = ManageApprovalScene.builder().ids(ids).status(ActivityApprovalStatusEnum.PASSED.getId()).build();
        return scene.visitor(visitor).getResponse().getMessage();
    }

    /**
     * 活动管理-活动审批【审核不通过】
     */
    public String getApprovalReject(Long... id) {
        List<Long> ids = Arrays.asList(id);
        IScene scene = ManageApprovalScene.builder().ids(ids).status(ActivityApprovalStatusEnum.REJECT.getId()).build();
        return scene.visitor(visitor).getResponse().getMessage();
    }

    /**
     * 活动管理-活动报名审批【通过】
     */
    public String getRegisterApprovalPassed(Long activityId, Long... id) {
        List<Long> ids = Arrays.asList(id);
        IScene scene = ManageRegisterApprovalScene.builder().activityId(activityId).ids(ids).status(101).build();
        return scene.visitor(visitor).getResponse().getMessage();
    }

    /**
     * 活动管理-活动报名审批【审核不通过】
     */
    public String getRegisterApprovalReject(Long activityId, Long... id) {
        List<Long> ids = Arrays.asList(id);
        IScene scene = ManageRegisterApprovalScene.builder().activityId(activityId).ids(ids).status(201).build();
        return scene.visitor(visitor).getResponse().getMessage();
    }

    /**
     * 小程序-活动报名-取消报名
     */
    public void activityCancelScene(Long id) {
        AppointmentActivityCancelScene.builder().id(id).build().visitor(visitor).execute();
    }

    /**
     * 根据活动ID返回活动的状态
     */
    public String appointmentActivityStatus(Long activityId) {
        Integer lastValue = null;
        JSONArray list;
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
        return AppletArticleListScene.builder().lastValue(null).size(10).build().visitor(visitor).execute();
    }

    /**
     * 更多活动列表-根据活动ID返回活动的小程序活动id
     */
    public Long appointmentActivityId(Long activityId) {
        JSONObject lastValue = null;
        JSONArray list;
        Long id = 0L;
        //获取此活动的名称
        user.loginPc(pp.phone, pp.password);
        String title = getRecruitActivityDetailDate1(activityId).getString("title");
        user.loginApplet(EnumAppletToken.JC_GLY_ONLINE);
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

    //---------------------------------------------获取页面返回值---------------------------------------------


    /**
     * 调整记录列表
     */
    public JSONObject changeRecordPage(Long activityId) {
        return ManageChangeRecordScene.builder().page(1).size(10).id(activityId).build().visitor(visitor).execute();
    }

    /**
     * 招募活动详情页-获取返回值在【活动奖励】内部
     */
    public JSONArray getRecruitActivityDetail(Long activityId) {
        IScene scene = ManageDetailScene.builder().id(activityId).build();
        return scene.visitor(visitor).execute().getJSONObject("recruit_activity_info").getJSONArray("reward_vouchers");
    }

    /**
     * 招募活动详情页返回值
     */
    public JSONObject getRecruitActivityDetailDate(Long activityId) {
        IScene scene = ManageDetailScene.builder().id(activityId).build();
        return scene.visitor(visitor).execute().getJSONObject("recruit_activity_info");
    }

    /**
     * 招募活动详情页返回值
     */
    public JSONObject getRecruitActivityDetailDate1(Long activityId) {
        IScene scene = ManageDetailScene.builder().id(activityId).build();
        return scene.visitor(visitor).execute();
    }

    /**
     * 裂变活动详情页-获取返回值在【活动奖励】内部
     */
    public JSONObject getFissionActivityDetail(Long activityId) {
        IScene scene = ManageDetailScene.builder().id(activityId).build();
        return scene.visitor(visitor).execute().getJSONObject("fission_voucher_info").getJSONObject("reward_vouchers");
    }

    /**
     * 招募活动裂变详情页返回值
     */
    public JSONObject getFissionActivityDetailDate1(Long activityId) {
        return ManageDetailScene.builder().id(activityId).build().visitor(visitor).execute();
    }

    /**
     * 裂变活动裂变详情页-获取返回值在【活动奖励】内部
     */
    public JSONObject getFissionActivityDetailData(Long activityId) {
        IScene scene = ManageDetailScene.builder().id(activityId).build();
        return scene.visitor(visitor).execute().getJSONObject("fission_voucher_info");
    }

    /**
     * 报名数据-返回值（data）
     */
    public JSONObject getRegisterData(Long activityId) {
        return ManageRegisterDataScene.builder().activityId(activityId).build().visitor(visitor).execute();
    }

    /**
     * 报名列表-返回值（data）
     */
    public JSONObject getRegisterPage(Long activityId) {
        return ManageRegisterPageScene.builder().page(1).size(10).activityId(activityId).build().visitor(visitor).execute();
    }

    /**
     * 活动审批数据-data
     */
    public JSONObject getActivityApprovalDate() {
        return ActivityManageDateScene.builder().build().visitor(visitor).execute();
    }

    /**
     * 活动审批列表返回值--data
     */
    public JSONObject getActivityManagePage(int status) {
        return ActivityManagePageScene.builder().page(1).size(10).approvalStatus(status).build().visitor(visitor).execute();
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
     * 获取小程序-首页-文章列表-更多的返回值
     */
    public int getAppletArticleList() {
        //获取小程序推荐列表
        JSONObject lastValue = null;
        JSONArray list;
        int num = 0;
        do {
            IScene scene = AppletArticleListScene.builder().lastValue(lastValue).size(10).build();
            JSONObject response1 = scene.visitor(visitor).execute();
            lastValue = response1.getJSONObject("last_value");
            System.err.println(lastValue);
            list = response1.getJSONArray("list");
            num += list.size();
        } while (list.size() == 10);
        return num;
    }


    /**
     * 判断客户报名是否重复，获取重复的手机号
     */
    public List<String> phoneSameArrayCheck(Long activityId) {
        List<String> phoneSameArray = new ArrayList<>();
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
        user.loginPc(EnumAccount.JC_ONLINE_YS);
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
        user.loginApplet(EnumAppletToken.JC_GLY_ONLINE);
        //获取小程序推荐列表
        JSONObject lastValue = null;
        JSONArray list;
        do {
            IScene scene = AppletArticleListScene.builder().lastValue(lastValue).size(10).build();
            JSONObject response1 = scene.visitor(visitor).execute();
            lastValue = response1.getJSONObject("last_value");
            list = response1.getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                String title1 = list.getJSONObject(i).getString("title");
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
    public void activityRegisterApplet1(Long id, String phone, String name, int registerCount, String eMail, String age, String gender, String others) {
        JSONArray registerItems = new JSONArray();
        Long activityId = 0L;
        //在活动详情中获得招募活动的报名信息
        user.loginPc(EnumAccount.JC_ONLINE_YS);
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
        user.loginApplet(EnumAppletToken.JC_LXQ_ONLINE);
        //获取小程序推荐列表
        JSONObject lastValue = null;
        JSONArray list;
        do {
            IScene scene = AppletArticleListScene.builder().lastValue(lastValue).size(10).build();
            JSONObject response1 = scene.visitor(visitor).execute();
            lastValue = response1.getJSONObject("last_value");
            list = response1.getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                String title1 = list.getJSONObject(i).getString("title");
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
     * 小程序报名招募活动---不填写个人信息
     */
    public void activityRegisterApplet(Long id) {
        JSONObject lastValue = null;
        JSONArray list;
        JSONArray registerItems = new JSONArray();
        Long activityId = 0L;
        //获取此活动的名称
        user.loginPc(pp.phone, pp.password);
        String title = getRecruitActivityDetailDate1(id).getString("title");
        user.loginApplet(EnumAppletToken.JC_GLY_ONLINE);
        //获取小程序推荐列表
        do {
            IScene scene = AppletArticleListScene.builder().lastValue(lastValue).size(10).build();
            JSONObject response1 = scene.visitor(visitor).execute();
            lastValue = response1.getJSONObject("last_value");
            list = response1.getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                String title1 = list.getJSONObject(i).getString("title");
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
     * V2.0小程序-我的报名-报名列表--报名条数
     */
    public int appointmentActivityList() {
        Integer lastValue = null;
        JSONArray list;
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
     * 获取活动的的状态
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
     * 通过品牌名称获取品牌的ID
     */
    public String brandPageExchange(String brandNme) {
        IScene scene = BrandPageScene.builder().name(brandNme).build();
        return toJavaObject(scene, JSONObject.class, "name", brandNme).getString("id");
    }

    /**
     * 判断小程序中小喇叭中卡券的状态
     */
    public String articleVoucher(Long activityId) {
        //获取此活动对应的小程序ID
        Long id = appointmentActivityId(activityId);
        //查看小喇叭中的优惠券
        IScene scene2 = ArticleVoucherList.builder().id(id).build();
        return scene2.visitor(visitor).execute().getJSONArray("list").getJSONObject(0).getString("is_received");
    }

    /**
     * 判断小程序中小喇叭的状态
     */
    public JSONObject articleVoucherData(Long activityId) {
        //获取此活动对应的小程序ID
        Long id = appointmentActivityId(activityId);
        //查看小喇叭中的优惠券
        IScene scene2 = ArticleVoucherList.builder().id(id).build();
        return scene2.visitor(visitor).execute();
    }

    /**
     * 主体类型的查询
     */
    public String getSubjectList(String subjectName) {
        //定义主体的字段
        String subjectKey = "";
        //获取主题状态的列表
        JSONArray list = SubjectListScene.builder().build().visitor(visitor).execute().getJSONArray("list");
        for (int i = 0; i < list.size(); i++) {
            String subjectValue = list.getJSONObject(i).getString("subject_value");
            if (subjectValue.equals(subjectName)) {
                subjectKey = list.getJSONObject(i).getString("subject_key");
            }
        }
        return subjectKey;
    }

    /**
     * 构建招募活动
     * 标签的状态:0-优惠,1-特价,2-福利,3-红包,4-礼品,5-礼品,6-热销,7-推荐
     */
    public IScene getContentMarketingAddScene(int participationType, List<String> chooseLabels, int labelNum, int actionPoint) {
        SceneUtil supporterUtil = new SceneUtil(visitor);
        List<String> picList = new ArrayList<>();
        picList.add(0, getPicPath());
        String[][] label = {{"CAR_WELFARE", "车福利"}, {"CAR_INFORMATION", "车资讯"}, {"CAR_LIFE", "车生活"}, {"CAR_ACVITITY", "车活动"}, {"CAR_KNOWLEDGE", "车知识"}};
        return ManageContentMarketingAddScene.builder()
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
        return scene.visitor(visitor).execute().getLong("id");
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
        return scene.visitor(visitor).execute().getLong("id");
    }

    /**
     * 取消的活动，进行恢复
     */
    public String getContentMarketingRecover(Long id) {
        IScene scene = ManageRecoveryScene.builder().id(id).build();
        return scene.visitor(visitor).getResponse().getMessage();
    }

    /**
     * 编辑招募活动，返回活动ID
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
        return scene.visitor(visitor).getResponse().getMessage();
    }

    /**
     * 活动下架
     */
    public String getContentMarketingOffLine(Long id) {
        IScene scene = ManageOfflineScene.builder().id(id).build();
        return scene.visitor(visitor).getResponse().getMessage();
    }

    /**
     * 活动上架
     */
    public String getContentMarketingOnline(Long id) {
        IScene scene = ManageOnlineScene.builder().id(id).build();
        return scene.visitor(visitor).getResponse().getMessage();
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
        Long activityId;
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
        Preconditions.checkNotNull(activityId, "创建失败");
        return activityId;
    }
}
