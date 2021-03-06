package com.haisheng.framework.testng.bigScreen.jiaochen.gly.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumAppletToken;
import com.haisheng.framework.testng.bigScreen.jiaochen.ScenarioUtil;
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
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.userange.UseRangeSubjectListScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.vouchermanage.VoucherDetailScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.vouchermanage.VoucherFormVoucherPageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.util.SceneUtil;
import com.haisheng.framework.util.DateTimeUtil;
import com.haisheng.framework.util.ImageUtil;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.*;

public class BusinessUtil extends SceneUtil {
    private final VisitorProxy visitor;

    public BusinessUtil(VisitorProxy visitor) {
        super(visitor);
        this.visitor = visitor;
    }

    ScenarioUtil jc = new ScenarioUtil();
    PublicParameter pp = new PublicParameter();

    /**
     * ??????????????????-???????????????
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
     * ??????????????????-??????????????????
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
     * @description :??????????????????-??????????????????
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
     * @description :??????????????????-????????????????????????
     * @date :2021/1/24
     **/
    public JSONArray getRegisterInformationNullList(List<Boolean> isShow, List<Boolean> isRequired) {
        return new JSONArray();
    }

    /**
     * @description :??????????????????-????????????
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
     * @description :??????????????????-????????????
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
     * ????????????4??????
     */
    public int randomNumber() {
        return (int) (Math.random() * 10000);
    }

    /**
     * ??????????????????-???????????????
     *
     * @param expireType           ????????????????????? 1???????????????2???????????????
     * @param voucherStart         ???????????????????????? ????????????????????????1?????????????????????
     * @param voucherEnd           ???????????????????????? ????????????????????????1?????????????????????
     * @param voucherEffectiveDays ?????????????????? ????????????????????????2????????????????????????
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
     * ??????????????????
     */
    public String getStartDate() {
        // ???????????????
        return DateTimeUtil.getFormat(new Date(), "yyyy-MM-dd HH:mm");
    }

    /**
     * ??????????????????+10???
     */
    public String getEndDate() {
        Date endDate = DateTimeUtil.addDay(new Date(), 10);
        return DateTimeUtil.getFormat(endDate, "yyyy-MM-dd HH:mm");
    }

    /**
     * ??????????????????
     */
    public String getDateTime(int day) {
        Date endDate = DateTimeUtil.addDay(new Date(), day);
        return DateTimeUtil.getFormat(endDate, "yyyy-MM-dd HH:mm");
    }

    /**
     * ??????????????????
     */
    public int getVoucherAllowUseInventory(Long voucherId) {
        int allowUseInventory = getVoucherPage(voucherId).getAllowUseInventory();
        return (int) Math.min((allowUseInventory == 1 ? allowUseInventory : allowUseInventory - 1), 30L);
    }

    /**
     * ??????????????????
     */
    public Long getVoucherAllowUseInventoryNum(Long voucherId) {
        int allowUseInventory = getVoucherPage(voucherId).getAllowUseInventory();
        return (long) allowUseInventory;
    }

    /**
     * ???????????????????????????
     */
    public Long getVoucherId() {
        return new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
    }

    /**
     * ??????????????????
     *
     * @param voucherId ??????????????????
     * @return IScene
     */
    public IScene createFissionActivityScene(Long voucherId) {
        SceneUtil supporterUtil = new SceneUtil(visitor);
        PublicParameter pp = new PublicParameter();
        List<String> picList = new ArrayList<>();
        picList.add(getPicPath());
        IScene scene;
        int AllowUseInventory = getVoucherAllowUseInventory(voucherId);
        if (AllowUseInventory > 2) {
            // ?????????????????????????????????????????????
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
            //????????????
            Long voucherId3 = supporterUtil.createVoucherId(1000, VoucherTypeEnum.COUPON);
            //?????????????????????
            String voucherName = supporterUtil.getVoucherName(voucherId3);
            //????????????
            supporterUtil.applyVoucher(voucherName, "1");
            //????????????
            // ?????????????????????????????????????????????
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
     * ??????????????????
     */
    public Long createFissionActivity(Long voucherId) {
        return createFissionActivityScene(voucherId).visitor(visitor).execute().getLong("id");
    }

    /**
     * ??????????????????--?????????????????????
     *
     * @return ??????id
     */
    public Long createRecruitActivityApproval() {
        Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
        return createRecruitActivity(voucherId, true, 0, true);
    }

    /**
     * ??????????????????--????????????????????????
     *
     * @return ??????id
     */
    public Long createRecruitActivityNotApproval() {
        Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
        return createRecruitActivity(voucherId, true, 0, false);
    }

    /**
     * ????????????-??????????????????--?????????????????????
     *
     * @param voucherId         ??????????????????
     * @param successReward     ??????????????????
     * @param rewardReceiveType ?????????????????? 0??????????????????1???????????????
     * @param isNeedApproval    ???????????????????????????
     */
    public Long createRecruitActivity(Long voucherId, boolean successReward, int rewardReceiveType, boolean isNeedApproval) {
        IScene scene = createRecruitActivityScene(voucherId, successReward, rewardReceiveType, isNeedApproval);
        Long id = scene.visitor(visitor).execute().getLong("id");
        Preconditions.checkNotNull(id, "????????????");
        return id;
    }

    /**
     * ????????????-??????????????????--????????????????????????
     * 2021-3-17
     */
    public Long createRecruitActivity() {
        Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
        IScene scene = createRecruitActivityScene(voucherId, true, 0, true, getEndDate(), getEndDate());
        Long id = scene.visitor(visitor).execute().getLong("id");
        Preconditions.checkNotNull(id, "????????????");
        return id;
    }

    /**
     * ????????????-??????????????????
     *
     * @param voucherId         ??????????????????
     * @param successReward     ??????????????????
     * @param rewardReceiveType ?????????????????? 0??????????????????1???????????????
     * @param isNeedApproval    ???????????????????????????
     */
    public IScene createRecruitActivityScene(Long voucherId, boolean successReward, int rewardReceiveType, boolean isNeedApproval) {
        List<String> picList = new ArrayList<>();
        picList.add(0, getPicPath());
        //???????????????????????????
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
        //??????????????????
        int AllowUseInventory = getVoucherAllowUseInventory(voucherId);
        ManageRecruitAddScene.ManageRecruitAddSceneBuilder builder;
        if (AllowUseInventory > 0) {
            //??????????????????
            JSONArray registerObject = getRewardVouchers(voucherId, 1, Math.toIntExact(AllowUseInventory));
            //???????????????
            JSONObject voucherValid = getVoucherValid(2, null, null, 10);
            //??????????????????-?????????--????????????
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
                    .subjectType(getSubjectType())
                    .subjectId(getSubjectDesc(getSubjectType()))
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
            //????????????
            Long voucherId3 = createVoucherId(1000, VoucherTypeEnum.COUPON);
            //?????????????????????
            String voucherName = getVoucherName(voucherId3);
            //????????????
            applyVoucher(voucherName, "1");
            //??????????????????
            JSONArray registerObject = getRewardVouchers(voucherId3, 1, getVoucherAllowUseInventory(voucherId));
            //???????????????
            JSONObject voucherValid = getVoucherValid(2, null, null, 10);
            //??????????????????-?????????--????????????
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
                    .subjectType(getSubjectType())
                    .subjectId(getSubjectDesc(getSubjectType()))
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
     * ????????????-??????????????????
     *
     * @param voucherId         ??????????????????
     * @param successReward     ??????????????????
     * @param rewardReceiveType ?????????????????? 0??????????????????1???????????????
     * @param isNeedApproval    ???????????????????????????
     */
    public IScene createRecruitActivityScene(Long voucherId, boolean successReward, int rewardReceiveType, boolean isNeedApproval, String startTime, String endTime) {
        List<String> picList = new ArrayList<>();
        SceneUtil supporterUtil = new SceneUtil(visitor);
        PublicParameter pp = new PublicParameter();
        picList.add(0, supporterUtil.getPicPath());
        //???????????????????????????
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
        //??????????????????
        JSONArray registerObject = getRewardVouchers(voucherId, 1, getVoucherAllowUseInventory(voucherId));
        //???????????????
        JSONObject voucherValid = getVoucherValid(2, null, null, 10);
        //??????????????????-?????????--????????????
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
     * ????????????-??????????????????
     *
     * @param voucherId         ??????????????????
     * @param successReward     ??????????????????
     * @param rewardReceiveType ?????????????????? 0??????????????????1???????????????
     * @param isNeedApproval    ???????????????????????????
     */
    public IScene createRecruitActivityScene(Long voucherId, boolean successReward, int rewardReceiveType, boolean isNeedApproval, Boolean type) {
        List<String> picList = new ArrayList<>();
        SceneUtil supporterUtil = new SceneUtil(visitor);
        PublicParameter pp = new PublicParameter();
        picList.add(0, supporterUtil.getPicPath());
        //???????????????????????????
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
        //??????????????????
        JSONArray registerObject = getRewardVouchers(voucherId, 1, getVoucherAllowUseInventory(voucherId));
        //???????????????
        JSONObject voucherValid = getVoucherValid(2, null, null, 10);
        //??????????????????-?????????--????????????
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
     * ??????????????????
     */
    public String activityEditScene(Long id) {
        //??????????????????
        Long voucherId = getVoucherId();
        List<String> picList = new ArrayList<>();
        SceneUtil supporterUtil = new SceneUtil(visitor);
        PublicParameter pp = new PublicParameter();
        picList.add(getPicturePath());
        //???????????????????????????
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
        //??????????????????
        JSONArray registerObject = getRewardVouchers(voucherId, 1, getVoucherAllowUseInventory(voucherId));
        //???????????????
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
        return scene.visitor(visitor).getResponse().getMessage();
    }

    /**
     * ??????????????????
     */
    public IScene fissionActivityEditScene(Long activityId) {
        Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
        SceneUtil supporterUtil = new SceneUtil(visitor);
        PublicParameter pp = new PublicParameter();
        List<String> picList = new ArrayList<>();
        picList.add(getPicturePath());
        // ?????????????????????????????????????????????
        JSONObject invitedVoucher = getInvitedVoucher(voucherId, 1, String.valueOf(getVoucherAllowUseInventory(voucherId)), 2, "", "", 1);
        JSONObject shareVoucher = getShareVoucher(voucherId, 1, String.valueOf(getVoucherAllowUseInventory(voucherId)), 2, "", "", 1);
        //??????????????????
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
     * ??????????????????
     *
     * @return ????????????
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
     * ??????????????????
     *
     * @return ????????????
     */
    public String getPicPath() {
        String path = "src/main/java/com/haisheng/framework/testng/bigScreen/jiaochen/wm/multimedia/picture/banner-1.jpg";
        String picture = new ImageUtil().getImageBinary(path);
        IScene scene = FileUploadScene.builder().isPermanent(false).permanentPicType(0).pic(picture).ratio(1.5).build();
        return scene.visitor(visitor).execute().getString("pic_path");
    }

    /**
     * ????????????????????????
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
     * ????????????????????????
     */
    public String getPrice(Long id) {
        IScene scene = VoucherDetailScene.builder().id(id).build();
        return scene.visitor(visitor).execute().getString("par_value");
    }


    /**
     * ????????????????????????
     */
    public String getCost(Long id) {
        IScene scene = VoucherDetailScene.builder().id(id).build();
        return scene.visitor(visitor).execute().getString("cost");
    }


//--------------------------------???????????????????????????--------------------------------------------

    /**
     * ??????????????????????????????????????????ID???---????????????
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
     * ??????????????????????????????????????????ID???---????????????
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
     * ????????????????????????????????????????????????ID???
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
     * ????????????-????????????????????????????????????????????????ID???
     */
    public ManagePageBean getFissionActivityWorking() {
        //????????????
        ManagePageBean managePageBean = getActivity(ActivityStatusEnum.PASSED, 1);
        if (managePageBean != null) {
            return managePageBean;
        }
        //?????????????????????
        Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
        createFissionActivity(voucherId);
        return getFissionActivityWorking();
    }

    /**
     * ????????????-??????????????????????????????????????????ID???
     */
    public ManagePageBean getRecruitActivityWorking() {
        //????????????
        ManagePageBean managePageBean = getActivity(ActivityStatusEnum.PASSED, 2);
        if (managePageBean != null) {
            return managePageBean;
        }
        //????????????
        Long id = createRecruitActivityApproval();
        //????????????
        getApprovalPassed(id);
        return getRecruitActivityWorking();
    }

    /**
     * ????????????-???????????????????????????????????????????????????-????????????????????????
     */
    public ManagePageBean getRecruitActivityWorkingApproval() {
        List<ManagePageBean> list = getRecruitActivityWorkingApprovalList();
        return list.stream().filter(e -> e.getWaitingAuditNum() >= 1).findFirst().orElse(null);
    }

    /**
     * ?????????????????????????????????
     *
     * @return ????????????
     */
    public List<ManagePageBean> getRecruitActivityWorkingApprovalList() {
        IScene scene = ActivityManagePageScene.builder().status(ActivityStatusEnum.PASSED.getId()).build();
        List<ManagePageBean> managePageBeanList = toJavaObjectList(scene, ManagePageBean.class, "activity_type", 2);
        if (managePageBeanList.size() != 0) {
            return managePageBeanList;
        }
        Long id = createRecruitActivityApproval();
        getApprovalPassed(id);
        return getRecruitActivityWorkingApprovalList();
    }


    /**
     * ?????????????????????
     *
     * @param activityId ??????id
     */
    public void appletSignUpActivity(Long activityId) {
        activityRegisterApplet(activityId, "13373166806", "?????????", 2, "1513814362@qq.com", "22", "???", "??????");
    }

    /**
     * ????????????????????????????????????????????????ID???
     */
    public ManagePageBean getActivityReject() {
        //????????????
        ManagePageBean managePageBean = getActivity(ActivityStatusEnum.REJECT, null);
        if (managePageBean != null) {
            return managePageBean;
        }
        //??????????????????????????????
        Long id = createRecruitActivityApproval();
        getApprovalReject(id);
        return getActivityReject();
    }

    /**
     * ????????????????????????????????????????????????ID???--????????????
     */
    public ManagePageBean getRecruitActivityReject() {
        //????????????
        ManagePageBean managePageBean = getActivity(ActivityStatusEnum.REJECT, 2);
        if (managePageBean != null) {
            return managePageBean;
        }
        Long id = createRecruitActivityApproval();
        getApprovalReject(id);
        return getRecruitActivityReject();
    }

    /**
     * ????????????????????????????????????????????????ID???
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
     * ??????????????????????????????????????????ID???--????????????
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
     * ??????????????????????????????????????????ID???--????????????
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
     * ????????????????????????????????????????????????ID???
     */
    public ManagePageBean getActivityCancel() {
        ManagePageBean managePageBean = getActivity(ActivityStatusEnum.CANCELED, null);
        if (managePageBean != null) {
            return managePageBean;
        }
        //????????????
        Long id = createRecruitActivityApproval();
        //????????????
        getApprovalReject(id);
        //????????????
        getCancelActivity(id);
        return getActivityCancel();
    }

    /**
     * ????????????????????????????????????????????????ID???--????????????
     */
    public ManagePageBean getRecruitActivityCancel() {
        ManagePageBean managePageBean = getActivity(ActivityStatusEnum.CANCELED, 2);
        if (managePageBean != null) {
            return managePageBean;
        }
        //????????????-??????????????????-????????????
        Long id = createRecruitActivityApproval();
        //????????????
        getApprovalReject(id);
        //????????????
        getCancelActivity(id);
        return getRecruitActivityCancel();
    }

    /**
     * ????????????????????????????????????????????????ID???--????????????
     */
    public ManagePageBean getFissionActivityCancel() {
        ManagePageBean managePageBean = getActivity(ActivityStatusEnum.CANCELED, 1);
        if (managePageBean != null) {
            return managePageBean;
        }
        Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
        Long id = createFissionActivity(voucherId);
        //????????????
        getApprovalReject(id);
        //????????????
        getCancelActivity(id);
        return getFissionActivityCancel();
    }

    /**
     * ??????????????????????????????????????????ID???----????????????
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
     * ??????????????????????????????????????????ID???----????????????
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
     * ????????????-??????????????????????????????????????????ID???---????????????
     * 2021-3-17
     */
    public ManagePageBean getRecruitActivityWaitingStar() {
        ManagePageBean managePageBean = getActivity(ActivityStatusEnum.WAITING_START, 2);
        if (managePageBean != null) {
            return managePageBean;
        }
        //????????????
        Long id = createRecruitActivity();
        //????????????
        getApprovalPassed(id);
        return getRecruitActivityWaitingStar();
    }

    /**
     * ??????????????????????????????????????????ID???---????????????
     * 2021-3-17
     */
    public ManagePageBean getFissionActivityWaitingStar() {
        ManagePageBean managePageBean = getActivity(ActivityStatusEnum.WAITING_START, 1);
        if (managePageBean != null) {
            return managePageBean;
        }
        //????????????
        Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
        Long id = createFissionActivityWaitingStarScene(voucherId);
        //????????????
        getApprovalPassed(id);
        return getFissionActivityWaitingStar();
    }

    /**
     * ????????????-??????????????????????????????????????????ID???---????????????
     * 2021-3-17
     */
    public ManagePageBean getRecruitActivityFinish() {
        return getActivity(ActivityStatusEnum.FINISH, 2);
    }

    /**
     * ????????????-??????????????????????????????????????????ID???---????????????
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
     * ????????????-??????????????????????????????????????????ID???---????????????
     * 2021-3-17
     */
    public ManagePageBean geContentMarketingFinish() {
        return getActivity(ActivityStatusEnum.FINISH, 3);
    }

    /**
     * ????????????-??????????????????????????????????????????ID???
     * 2021-3-17
     */
    public ManagePageBean getContentMarketingOffLine() {
        ManagePageBean managePageBean = getActivity(ActivityStatusEnum.OFFLINE, 3);
        if (managePageBean != null) {
            return managePageBean;
        }
        //????????????
        Long id = getContentMarketingNotStar();
        //????????????
        getApprovalPassed(id);
        //????????????
        getContentMarketingOffLine(id);
        return getContentMarketingOffLine();
    }

    /**
     * ????????????-??????????????????????????????????????????ID???
     * 2021-3-17
     */
    public ManagePageBean getFissionActivityOffLine() {
        ManagePageBean managePageBean = getActivity(ActivityStatusEnum.OFFLINE, 1);
        if (managePageBean != null) {
            return managePageBean;
        }
        //????????????
        Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
        Long id = createFissionActivity(voucherId);
        //????????????
        getApprovalPassed(id);
        //????????????
        getContentMarketingOffLine(id);
        return getFissionActivityOffLine();
    }

    /**
     * ????????????-??????????????????????????????????????????ID???
     * 2021-3-17
     */
    public ManagePageBean getRecruitActivityOffLine() {
        ManagePageBean managePageBean = getActivity(ActivityStatusEnum.OFFLINE, 2);
        if (managePageBean != null) {
            return managePageBean;
        }
        Long id = createRecruitActivityApproval();
        //????????????
        getApprovalPassed(id);
        //????????????
        getContentMarketingOffLine(id);
        return getRecruitActivityOffLine();
    }

    /**
     * ??????????????????????????????????????????ID???---????????????
     */
    public ManagePageBean getContentMarketingWaitingApproval() {
        return getActivity(ActivityStatusEnum.PENDING, 3);
    }

    /**
     * ??????????????????????????????????????????ID???---????????????
     * 2021-3-17
     */
    public ManagePageBean getContentMarketingRevoke() {
        return getActivity(ActivityStatusEnum.REVOKE, 3);
    }

    /**
     * ????????????????????????????????????????????????ID???--????????????
     */
    public ManagePageBean getContentMarketingReject() {
        //????????????
        ManagePageBean managePageBean = getActivity(ActivityStatusEnum.REJECT, 3);
        if (managePageBean != null) {
            return managePageBean;
        }
        Long id = getContentMarketingAdd();
        getApprovalReject(id);
        return getContentMarketingReject();
    }

    /**
     * ????????????????????????????????????????????????ID???--????????????
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
     * ????????????-????????????????????????????????????????????????ID???
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
     * ????????????-??????????????????????????????????????????ID???
     * 2021-3-17
     */
    public ManagePageBean getContentMarketingWaitingStar() {
        ManagePageBean managePageBean = getActivity(ActivityStatusEnum.WAITING_START, 3);
        if (managePageBean != null) {
            return managePageBean;
        }
        Long id = getContentMarketingNotStar();
        //????????????
        getApprovalPassed(id);
        return getContentMarketingWaitingStar();
    }

    /**
     * ????????????
     *
     * @param status       ????????????
     * @param activityType ????????????1????????? 2????????? 3?????????
     * @return ??????
     */
    private ManagePageBean getActivity(@NotNull ActivityStatusEnum status, Integer activityType) {
        IScene scene = ActivityManagePageScene.builder().status(status.getId()).build();
        return activityType == null ? toFirstJavaObject(scene, ManagePageBean.class) : toJavaObject(scene, ManagePageBean.class, "activity_type", activityType);
    }

    /**
     * ????????????????????????
     */
    public int getActivityStatus(Long id) {
        IScene scene = ActivityManagePageScene.builder().build();
        return toJavaObject(scene, JSONObject.class, "id", id).getInteger("status");
    }

    /**
     * ??????????????????????????????
     */
    public int getActivityApprovalStatus(Long id) {
        IScene scene = ActivityManagePageScene.builder().build();
        return toJavaObject(scene, JSONObject.class, "id", id).getInteger("approval_status");
    }

    /**
     * ????????????-?????????????????????????????????ids
     */
    public List<Long> registerApproval(Long activityId, String type) {
        List<Long> list = new ArrayList<>();
        IScene scene = ManageRegisterPageScene.builder().activityId(activityId).build();
        if (type.equals("1")) {
            list.add(toJavaObject(scene, JSONObject.class, "status_name", "?????????").getLong("id"));
        } else {
            toJavaObjectList(scene, JSONObject.class, "status_name", "?????????").stream().map(e -> e.getLong("id")).forEach(list::add);
        }
        return list;
    }

    /**
     * ?????????????????????????????????ids?????????
     */
    public ManageRegisterPageBean registerAppletRegister(Long activityId) {
        IScene scene = ManageRegisterPageScene.builder().status(1).activityId(activityId).build();
        return toFirstJavaObject(scene, ManageRegisterPageBean.class);
    }

    /**
     * ??????????????????????????????????????????ID???
     */
    public ManagePageBean getActivityWaitingApproval() {
        ManagePageBean managePageBean = getActivity(ActivityStatusEnum.PENDING, null);
        if (managePageBean != null) {
            return managePageBean;
        }
        createRecruitActivityApproval();
        return getActivityWaitingApproval();
    }

    /**
     * ????????????-?????????????????????????????????ids
     */
    public List<Long> registerApproval(Long activityId) {
        //??????????????????????????????????????????
        JSONObject response = getRegisterPage(activityId);
        int pages = response.getInteger("pages");
        List<Long> idArray = new ArrayList<>();
        for (int page = 1; page <= pages; page++) {
            IScene scene = ManageRegisterPageScene.builder().page(page).size(10).activityId(activityId).build();
            JSONArray list = scene.visitor(visitor).execute().getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                String statusName = list.getJSONObject(i).getString("status_name");
                if (statusName.equals("?????????")) {
                    //????????????ID
                    Long ids = list.getJSONObject(i).getLong("id");
                    idArray.add(ids);
                }
            }
        }
        return idArray;
    }

    /**
     * ?????????????????????????????????ids?????????
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


    // ---------------------------------?????????????????????-----------------------------

    /**
     * ????????????-????????????
     */
    public String getDelActivity(Long id) {
        IScene scene = ManageDeleteScene.builder().id(id).build();
        return scene.visitor(visitor).getResponse().getMessage();
    }

    /**
     * ????????????-??????????????????
     */
    public String getRevokeActivity(Long id) {
        return ManageRevokeScene.builder().id(id).build().visitor(visitor).getResponse().getMessage();
    }

    /**
     * ????????????-????????????
     */
    public String getCancelActivity(Long id) {
        return ManageCancelScene.builder().id(id).build().visitor(visitor).getResponse().getMessage();
    }

    /**
     * ????????????-????????????
     */
    public String getPromotionActivity(Long id) {
        return ManagePromotionScene.builder().id(id).build().visitor(visitor).execute().getString("applet_code_url");
    }

    /**
     * ????????????-????????????????????????
     */
    public String getApprovalPassed(Long... id) {
        List<Long> ids = Arrays.asList(id);
        System.err.println("---------" + ids);
        return ManageApprovalScene.builder().ids(ids).status(ActivityApprovalStatusEnum.PASSED.getId()).build().visitor(visitor).getResponse().getMessage();
    }


    /**
     * ????????????-?????????????????????????????????
     */
    public String getApprovalReject(Long... id) {
        List<Long> ids = Arrays.asList(id);
        return ManageApprovalScene.builder().ids(ids).status(ActivityApprovalStatusEnum.REJECT.getId()).build().visitor(visitor).getResponse().getMessage();
    }

    /**
     * ????????????-??????????????????????????????
     */
    public String getRegisterApprovalPassed(Long activityId, Long... id) {
        List<Long> ids = Arrays.asList(id);
        IScene scene = ManageRegisterApprovalScene.builder().activityId(activityId).ids(ids).status(101).build();
        return scene.visitor(visitor).getResponse().getMessage();
    }

    /**
     * ????????????-???????????????????????????????????????
     */
    public String getRegisterApprovalReject(Long activityId, Long... id) {
        List<Long> ids = Arrays.asList(id);
        IScene scene = ManageRegisterApprovalScene.builder().activityId(activityId).ids(ids).status(201).build();
        return scene.visitor(visitor).getResponse().getMessage();
    }

    /**
     * ?????????-????????????-????????????
     */
    public void activityCancelScene(Long id) {
        AppointmentActivityCancelScene.builder().id(id).build().visitor(visitor).execute();
    }

    /**
     * ????????????ID?????????????????????
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
     * ??????????????????-?????????
     * 2021-3-17
     */
    public JSONObject appointmentActivityTitleNew() {
        IScene scene = AppletArticleListScene.builder().lastValue(null).size(10).build();
        return scene.visitor(visitor).execute();
    }

    /**
     * ??????????????????-????????????ID?????????????????????
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
     * ??????????????????-????????????ID??????????????????????????????id
     */
    public Long appointmentActivityId(Long activityId) {
        JSONObject lastValue = null;
        JSONArray list;
        Long id = 0L;
        //????????????????????????
        loginPc(pp.phone1, pp.password);
        String title = getRecruitActivityDetailDate1(activityId).getString("title");
        visitor.setToken(EnumAppletToken.JC_GLY_DAILY.getToken());
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
     * ??????????????????
     */
    public JSONObject changeRecordPage(Long activityId) {
        IScene scene = ManageChangeRecordScene.builder().page(1).size(10).id(activityId).build();
        return scene.visitor(visitor).execute();
    }

    /**
     * ?????????????????????-??????????????????????????????????????????
     */
    public JSONArray getRecruitActivityDetail(Long activityId) {
        return ManageDetailScene.builder().id(activityId).build().visitor(visitor).execute().getJSONObject("recruit_activity_info").getJSONArray("reward_vouchers");
    }

    /**
     * ??????????????????????????????
     */
    public JSONObject getRecruitActivityDetailDate(Long activityId) {
        return ManageDetailScene.builder().id(activityId).build().visitor(visitor).execute().getJSONObject("recruit_activity_info");
    }

    /**
     * ??????????????????????????????
     */
    public JSONObject getRecruitActivityDetailDate1(Long activityId) {
        return ManageDetailScene.builder().id(activityId).build().visitor(visitor).execute();
    }

    /**
     * ????????????????????????????????????
     */
    public JSONObject getFissionActivityDetailDate1(Long activityId) {
        IScene scene = ManageDetailScene.builder().id(activityId).build();
        return scene.visitor(visitor).execute();
    }

    /**
     * ???????????????????????????-???????????????
     */
    public JSONObject getFissionActivityDetailData(Long activityId) {
        IScene scene = ManageDetailScene.builder().id(activityId).build();
        return scene.visitor(visitor).execute().getJSONObject("fission_voucher_info");
    }

    /**
     * ???????????????????????????-??????????????????????????????????????????
     */
    public JSONObject getFissionActivityDetail(Long activityId) {
        IScene scene = ManageDetailScene.builder().id(activityId).build();
        return scene.visitor(visitor).execute().getJSONObject("fission_voucher_info").getJSONObject("reward_vouchers");
    }

    /**
     * ????????????-????????????data???
     */
    public JSONObject getRegisterData(Long activityId) {
        return ManageRegisterDataScene.builder().activityId(activityId).build().visitor(visitor).execute();
    }

    /**
     * ????????????-????????????data???
     */
    public JSONObject getRegisterPage(Long activityId) {
        IScene scene = ManageRegisterPageScene.builder().page(1).size(10).activityId(activityId).build();
        return scene.visitor(visitor).execute();
    }

    /**
     * ??????????????????-data
     */
    public JSONObject getActivityApprovalDate() {
        IScene scene = ActivityManageDateScene.builder().build();
        return scene.visitor(visitor).execute();
    }

    /**
     * ???????????????????????????--data
     */
    public JSONObject getActivityManagePage(int status) {
        IScene scene = ActivityManagePageScene.builder().page(1).size(10).approvalStatus(status).build();
        return scene.visitor(visitor).execute();
    }


    /**
     * ??????????????????????????????
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
     * ???????????????-??????-????????????-????????????
     */
    public int getAppletArticleList() {
        //???????????????????????????
        JSONObject lastValue = null;
        JSONArray list;
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
     * ?????????????????????????????????????????????????????????
     */
    public List<String> phoneSameArrayCheck(Long activityId) {
        List<String> phoneSameArray = new ArrayList<>();
        //????????????????????????
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
     * ???????????????????????????
     */
    public void activityRegisterApplet(Long id, String phone, String name, int registerCount, String eMail, String age, String gender, String others) {
        Long activityId = 0L;
        //???????????????????????????????????????????????????
        loginPc(pp.phone1, pp.password);
        JSONArray registerItems = new JSONArray();
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
        //????????????????????????
        String title = getRecruitActivityDetailDate1(id).getString("title");
        System.err.println("----------title:" + title);
        //??????
        ActivityManageTopScene.builder().id(id).build().visitor(visitor).execute();
        visitor.setToken(EnumAppletToken.JC_GLY_DAILY.getToken());
        //???????????????????????????
        JSONObject lastValue = null;
        JSONArray list;
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
        ArticleActivityRegisterScene.builder().id(activityId).registerItems(registerItems).build().visitor(visitor).execute();
    }

    /**
     * ???????????????????????????
     */
    public void activityRegisterApplet(Long id, String phone, String name, int registerCount, String eMail, String age, String gender, String others, String activityName) {
        JSONArray registerItems = new JSONArray();
        //???????????????????????????????????????????????????
        loginPc(pp.phone1, pp.password);
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
        visitor.setToken(EnumAppletToken.JC_GLY_DAILY.getToken());
        //???????????????????????????
        JSONObject lastValue = null;
        JSONArray list;
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
     * ???????????????????????????
     */
    public void activityRegisterApplet1(Long id, String phone, String name, int registerCount, String eMail, String age, String gender, String others, String activityName) {
        JSONArray registerItems = new JSONArray();
        //???????????????????????????????????????????????????
        loginPc(pp.phone1, pp.password);
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
        visitor.setToken(EnumAppletToken.JC_LXQ_DAILY.getToken());
        //???????????????????????????
        JSONObject lastValue = null;
        JSONArray list;
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
     * ???????????????????????????---?????????????????????
     */
    public void activityRegisterApplet(Long id) {
        JSONObject lastValue = null;
        JSONArray list;
        JSONArray registerItems = new JSONArray();
        Long activityId = 0L;
        //??????PC
        loginPc(pp.phone1, pp.password);
        //????????????????????????
        String title = getRecruitActivityDetailDate1(id).getString("title");
        visitor.setToken(EnumAppletToken.JC_GLY_DAILY.getToken());
        //???????????????????????????
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
     * V2.0?????????-????????????-????????????--????????????
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
     * ??????shop_id???shop_name??????
     */
    public String shopNameTransformId(String shopName) {
        String shopId = "";
        JSONObject response = jc.shopListPage();
        JSONArray list = response.getJSONArray("list");
        for (int i = 0; i < list.size(); i++) {
            String name = list.getJSONObject(i).getString("shop_name");
            if (shopName.equals(name)) {
                shopId = list.getJSONObject(i).getString("shop_id");
            } else if (shopName.equals("????????????")) {
                shopId = list.getJSONObject(0).getString("shop_id");
            }
        }
        return shopId;
    }


    /**
     * ???????????????????????????????????????????????????
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
     * ?????????id???name??????
     */
    public String authNameTransformId(String shopName, String type) {
        String shopId = "";
        JSONObject response = jc.authListPage(type, "-1");
        JSONArray list = response.getJSONArray("list");
        for (int i = 0; i < list.size(); i++) {
            String name = list.getJSONObject(i).getString("name");
            if (shopName.equals(name)) {
                shopId = list.getJSONObject(i).getString("id");
            } else if (shopName.equals("????????????")) {
                shopId = list.getJSONObject(0).getString("id");
            } else if (i == list.size() - 1 && shopId.equals("")) {
                shopId = list.getJSONObject(0).getString("id");
            }
        }
        return shopId;
    }


    /**
     * ?????????????????????????????????????????????????????????
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
     * ???????????????????????????
     */
    public Long voucherWorking() {
        //?????????????????????ID
        Long activityId = 0L;
        //???????????????
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
     * ????????????????????????
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
     * ????????????????????????????????????????????????????????????
     */
    public String enumMapSendChannelList(String sendChannelName) {
        JSONArray list = jc.enumMap().getJSONArray("SEND_CHANNEL_LIST");
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
     * ?????????????????????????????????ID
     */
    public String brandPageExchange(String brandNme) {
        JSONObject response = BrandPageScene.builder().build().visitor(visitor).execute();
        int pages = response.getInteger("pages");
        String brandId = "";
        for (int page = 1; page <= pages; page++) {
            JSONArray list = BrandPageScene.builder().page(page).build().visitor(visitor).execute().getJSONArray("list");
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
     * ?????????????????????????????????????????????
     */
    public String articleVoucher(Long activityId) {
        //?????????????????????????????????ID
        Long id = appointmentActivityId(activityId);
        //??????????????????????????????
        IScene scene2 = ArticleVoucherList.builder().id(id).build();
        return scene2.visitor(visitor).execute().getJSONArray("list").getJSONObject(0).getString("is_received");
    }

    /**
     * ????????????????????????????????????
     */
    public JSONObject articleVoucherData(Long activityId) {
        //????????????????????????
        Long id = appointmentActivityId(activityId);
        //??????????????????????????????
        IScene scene2 = ArticleVoucherList.builder().id(id).build();
        return scene2.visitor(visitor).execute();
    }

    /**
     * ?????????????????????
     */
    public String getSubjectList(String subjectName) {
        //?????????????????????
        String subjectKey = "";
        //???????????????????????????
        JSONArray list = UseRangeSubjectListScene.builder().build().visitor(visitor).execute().getJSONArray("list");
        for (int i = 0; i < list.size(); i++) {
            String subjectValue = list.getJSONObject(i).getString("subject_value");
            if (subjectValue.equals(subjectName)) {
                subjectKey = list.getJSONObject(i).getString("subject_key");
            }
        }
        return subjectKey;
    }


    //------------------------------------------??????3.1???????????????---------------------------------------------

    /**
     * ??????????????????
     * ???????????????:0-??????,1-??????,2-??????,3-??????,4-??????,5-??????,6-??????,7-??????
     */
    public IScene getContentMarketingAddScene(int participationType, List<String> chooseLabels, int labelNum, int actionPoint) {
        SceneUtil supporterUtil = new SceneUtil(visitor);
        List<String> picList = new ArrayList<>();
        picList.add(0, getPicPath());
        String[][] label = {{"CAR_WELFARE", "?????????"}, {"CAR_INFORMATION", "?????????"}, {"CAR_LIFE", "?????????"}, {"CAR_ACVITITY", "?????????"}, {"CAR_KNOWLEDGE", "?????????"}};
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
     * ???????????????????????????????????????ID
     */
    public Long getContentMarketingAdd() {
        SceneUtil supporterUtil = new SceneUtil(visitor);
        List<String> picList = new ArrayList<>();
        picList.add(0, getPicPath());
        String[][] label = {{"CAR_WELFARE", "?????????"}, {"CAR_INFORMATION", "?????????"}, {"CAR_LIFE", "?????????"}, {"CAR_ACVITITY", "?????????"}, {"CAR_KNOWLEDGE", "?????????"}};
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
        Preconditions.checkNotNull(activityId, "????????????");
        return activityId;
    }

    /**
     * ????????????????????????????????????????????????ID
     */
    public Long getContentMarketingNotStar() {
        SceneUtil supporterUtil = new SceneUtil(visitor);
        List<String> picList = new ArrayList<>();
        picList.add(0, getPicPath());
        String[][] label = {{"CAR_WELFARE", "?????????"}, {"CAR_INFORMATION", "?????????"}, {"CAR_LIFE", "?????????"}, {"CAR_ACVITITY", "?????????"}, {"CAR_KNOWLEDGE", "?????????"}};
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
        Preconditions.checkNotNull(activityId, "????????????");
        return activityId;
    }

    /**
     * ??????????????????????????????
     */
    public String getContentMarketingRecover(Long id) {
        IScene scene = ManageRecoveryScene.builder().id(id).build();
        return scene.visitor(visitor).getResponse().getMessage();
    }

    /**
     * ???????????????????????????????????????ID
     */
    public String getContentMarketingEdit(Long id, String title, String rule) {
        SceneUtil supporterUtil = new SceneUtil(visitor);
        List<String> picList = new ArrayList<>();
        picList.add(0, getPicPath());
        String[][] label = {{"CAR_WELFARE", "?????????"}, {"CAR_INFORMATION", "?????????"}, {"CAR_LIFE", "?????????"}, {"CAR_ACVITITY", "?????????"}, {"CAR_KNOWLEDGE", "?????????"}};
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
     * ????????????
     */
    public String getContentMarketingOffLine(Long id) {
        IScene scene = ManageOfflineScene.builder().id(id).build();
        return scene.visitor(visitor).getResponse().getMessage();
    }

    /**
     * ????????????
     */
    public String getContentMarketingOnline(Long id) {
        IScene scene = ManageOnlineScene.builder().id(id).build();
        return scene.visitor(visitor).getResponse().getMessage();
    }


    /**
     * ????????????-????????????????????????
     */
    public Long createFissionActivityWaitingStarScene(Long voucherId) {
        SceneUtil supporterUtil = new SceneUtil(visitor);
        PublicParameter pp = new PublicParameter();
        List<String> picList = new ArrayList<>();
        picList.add(supporterUtil.getPicPath());
        int AllowUseInventory = getVoucherAllowUseInventory(voucherId);
        Long activityId;
        if (AllowUseInventory > 6) {
            // ?????????????????????????????????????????????
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
            //????????????
            Long voucherId3 = supporterUtil.createVoucherId(1000, VoucherTypeEnum.COUPON);
            //?????????????????????
            String voucherName = supporterUtil.getVoucherName(voucherId3);
            //????????????
            supporterUtil.applyVoucher(voucherName, "1");
            //????????????
            // ?????????????????????????????????????????????
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
        Preconditions.checkNotNull(activityId, "????????????");
        return activityId;
    }
}
