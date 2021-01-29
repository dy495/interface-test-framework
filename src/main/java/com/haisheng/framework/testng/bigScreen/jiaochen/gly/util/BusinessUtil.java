package com.haisheng.framework.testng.bigScreen.jiaochen.gly.util;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.agency.Visitor;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.EnumDesc;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.activity.ActivityApprovalStatusEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.activity.ActivityStatusEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.activity.ActivityTypeEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.activity.RegisterInfoEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing.VoucherStatusEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.activity.AppletArticleList;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.activity.ArticleActivityRegisterScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.activity.*;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.file.FileUpload;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.vouchermanage.VoucherDetailScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.vouchermanage.VoucherPageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.util.SupporterUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.voucher.VoucherGenerator;
import com.haisheng.framework.util.DateTimeUtil;
import com.haisheng.framework.util.ImageUtil;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class BusinessUtil {
    public Visitor visitor;

    public BusinessUtil(Visitor visitor) {
        this.visitor = visitor;
    }

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

    /**
     * 创建裂变活动-被邀请者奖励
     **/
    public JSONObject getInvitedVoucher(Long id, int type, String num, Integer expireType, String voucherStart, String voucherEnd, Integer voucherEffectiveDays) {
        JSONObject invitedVoucher = new JSONObject();
        invitedVoucher.put("id", id);
        invitedVoucher.put("type", type);
        invitedVoucher.put("num", num);
        JSONObject voucherValid = new JSONObject();
        if (expireType == 1) {
            voucherValid.put("voucher_start", voucherStart);
            voucherValid.put("voucher_end", voucherEnd);
        } else {
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
     * @description :创建招募活动-卡券奖励
     * @date :2021/1/24
     **/
    public JSONArray getRewardVouchers(Long id, int type, int num) {
        JSONArray rewardVouchers = new JSONArray();
        JSONObject voucherValid = new JSONObject();
        voucherValid.put("id", id);
        voucherValid.put("type", type);
        voucherValid.put("num", num);
        rewardVouchers.add(voucherValid);
        return rewardVouchers;
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
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern("yyyy-MM-dd");
        // 获取当前时间
        Date date = new Date();

        String nowTime = sdf.format(date);
        return nowTime;
    }

    /**
     * 获取当前时间+10天
     */
    public String getEndDate() {
        //当前时间+10天的日期
        return DateTimeUtil.addDayFormat(new Date(), 10);
    }

    /**
     * 判断剩余库存
     */
    private int getVoucherSurplusInventory(Long voucherId) {
        SupporterUtil su=new SupporterUtil(visitor);
        Long surplusInventory = su.getVoucherPage(voucherId).getSurplusInventory();
        return (int) (surplusInventory == 1 ? surplusInventory : surplusInventory - 1);

    }

    /**
     * 获取优惠券
     */
    public Long getVoucherId(){

        Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();

        return voucherId;
    }

    /**
     * 构建裂变活动
     *
     * @param voucherId 包含卡券信息
     * @return IScene
     */
    public IScene createFissionActivityScene(Long voucherId) {
        SupporterUtil supporterUtil = new SupporterUtil(visitor);
        List<String> picList = new ArrayList<>();
        picList.add(supporterUtil.getPicPath());
        // 创建被邀请者和分享者的信息字段
        JSONObject invitedVoucher = getInvitedVoucher(voucherId, 1, String.valueOf(getVoucherSurplusInventory(voucherId)), 2, "", "", 3);
        JSONObject shareVoucher = getShareVoucher(voucherId, 2, String.valueOf(getVoucherSurplusInventory(voucherId)), 2, "", "", 3);
        return FissionVoucherAddScene.builder()
                .type(0)
                .participationLimitType(0)
                .receiveLimitType(0)
                .title(ActivityTypeEnum.FISSION_VOUCHER.getName())
                .rule(EnumDesc.ACTIVITY_DESC.getDesc())
                .startDate(getStartDate())
                .endDate(getEndDate())
                .subjectType(supporterUtil.getSubjectType())
                .subjectId(supporterUtil.getSubjectDesc(supporterUtil.getSubjectType()))
                .label("RED_PAPER")
                .picList(picList)
                .shareNum("3")
                .shareVoucher(shareVoucher)
                .invitedVoucher(invitedVoucher)
                .build();
    }

    /**
     * 创建裂变活动
     */
    public Long createFissionActivity(Long voucherId){
        IScene scene=createFissionActivityScene(voucherId);
        Long activityId=visitor.invokeApi(scene).getLong("id");
        return activityId;
    }

    /**
     * 创建招募活动--需要审批的活动
     *
     * @return 活动id
     */
    public Long createRecruitActivityApproval() {
        Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
        return createRecruitActivity(voucherId,true,0,true);
    }

    /**
     * 创建招募活动--不需要审批的活动
     *
     * @return 活动id
     */
    public Long createRecruitActivityNotApproval() {
        Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
        return createRecruitActivity(voucherId,true,0,false);
    }

    /**
     * 活动管理-创建招募活动--需要审批的活动
     *
     * @param voucherId         奖励卡券信息
     * @param award             是否包含奖励
     * @param rewardReceiveType 奖励领取方式 0：自动发放，1：主动领取
     * @param isNeedApproval    报名后是否需要审批
     */
    public Long createRecruitActivity(Long voucherId, boolean award, int rewardReceiveType, boolean isNeedApproval) {
        IScene scene = createRecruitActivityScene(voucherId, award, rewardReceiveType, isNeedApproval);
        return visitor.invokeApi(scene).getLong("id");
    }

    /**
     * 活动管理-创建招募活动
     *
     * @param voucherId         奖励卡券信息
     * @param award             是否包含奖励
     * @param rewardReceiveType 奖励领取方式 0：自动发放，1：主动领取
     * @param isNeedApproval    报名后是否需要审批
     */
    public IScene createRecruitActivityScene(Long voucherId, boolean award, int rewardReceiveType, boolean isNeedApproval) {
        List<String> picList = new ArrayList<>();
        SupporterUtil supporterUtil = new SupporterUtil(visitor);
        picList.add(supporterUtil.getPicPath());
        //填写报名所需要信息
        List<Boolean> isShow = new ArrayList<>();
        isShow.add(true);
        isShow.add(true);
        isShow.add(true);
        isShow.add(true);
        List<Boolean> isRequired = new ArrayList<>();
        isRequired.add(true);
        isRequired.add(true);
        isRequired.add(true);
        isRequired.add(true);
        JSONArray registerInformationList = getRegisterInformationList(isShow, isRequired);
        //报名成功奖励
        JSONArray registerObject = getRewardVouchers(voucherId, 1, getVoucherSurplusInventory(voucherId));
        //卡券有效期
        JSONObject voucherValid = getVoucherValid(1, "", "", 10);
        //创建招募活动-共有的--基础信息
        ManageRecruitAddScene.ManageRecruitAddSceneBuilder builder = ManageRecruitAddScene.builder()
                .type(1)
                .participationLimitType(0)
                .receiveLimitType(0)
                .title(ActivityTypeEnum.FISSION_VOUCHER.getName())
                .rule(EnumDesc.ACTIVITY_DESC.getDesc())
                .startDate(getStartDate())
                .endDate(getEndDate())
                .subjectType(supporterUtil.getSubjectType())
                .subjectId(supporterUtil.getSubjectDesc(supporterUtil.getSubjectType()))
                .label("RED_PAPER")
                .picList(picList)
                .applyStart(getStartDate())
                .applyEnd(getEndDate())
                .isLimitQuota(true)
                .quota(2)
                .address(EnumDesc.MESSAGE_TITLE.getDesc())
                .registerInformationList(registerInformationList)
                .successReward(true)
                .rewardReceiveType(rewardReceiveType)
                .isNeedApproval(isNeedApproval);
        if (award) {
            builder.rewardVouchers(registerObject)
                    .voucherValid(voucherValid);
        }
        return builder.build();
    }

    /**
     * 获取图片地址
     *
     * @return 图片地址
     */
    public String getPicPath() {
        String path = "src/main/java/com/haisheng/framework/testng/bigScreen/jiaochen/wm/multimedia/picture/卡券图.jpg";
        String picture = new ImageUtil().getImageBinary(path);
        IScene scene = FileUpload.builder().isPermanent(false).pic(picture).ratio(1.5).build();
        return visitor.invokeApi(scene).getString("pic_path");
    }

    /**
     * 获取优惠券的库存
     */
    public String getSurplusInventory(Long id) {
        IScene scene1 = VoucherPageScene.builder().page(1).size(10).build();
        int pages = visitor.invokeApi(scene1).getInteger("pages");
        String surplusInventory = "";
        for (int page = 1; page <= pages; page++) {
            IScene scene2 = VoucherPageScene.builder().page(page).size(10).build();
            JSONArray list = visitor.invokeApi(scene2).getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                Long voucherId = list.getJSONObject(i).getLong("voucher_id");
                if (voucherId.equals(id)) {
                    surplusInventory = list.getJSONObject(i).getString("surplus_inventory");
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
        IScene scene1 = VoucherDetailScene.builder().id(id).build();
        int pages = visitor.invokeApi(scene1).getInteger("pages");
        String parValue = "";
        for (int page = 1; page <= pages; page++) {
            IScene scene2 = VoucherPageScene.builder().page(page).size(10).build();
            JSONArray list = visitor.invokeApi(scene2).getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                Long voucherId = list.getJSONObject(i).getLong("voucher_id");
                if (voucherId.equals(id)) {
                    parValue = list.getJSONObject(i).getString("par_value");
                    break;
                }
            }
        }
        return parValue;
    }
/**
 * --------------------------------活动列表中活动状态--------------------------------------------
 */
    /**
     * 查询列表中的状态为【待审核的ID】
     */
    public List<Long> getActivityWaitingApproval() {
        List<Long> ids =new ArrayList<>();
        //活动列表
        IScene scene = ActivityManageListScene.builder().page(1).size(10).build();
        int pages = visitor.invokeApi(scene).getInteger("pages");
        for (int page = 1; page <= pages; page++) {
            IScene scene1 = ActivityManageListScene.builder().page(page).size(10).build();
            JSONArray list = visitor.invokeApi(scene1).getJSONArray("list");
//          id=list.stream().map(e->(JSONObject)e).filter(e->e.getString("status").equals("待审核"))
//                    .map(e->e.getLong("id")).findFirst().orElse(null);
//          if(id==null){
//            id=  createRecruitActivity();
//          }
            for (int i = 0; i < list.size(); i++) {
                String status = list.getJSONObject(i).getString("status");
                if (status.equals(ActivityStatusEnum.PENDING.getStatusName())) {
                    Long id = list.getJSONObject(i).getLong("id");
                    ids.add(id);
                }
            }
        }
        if (ids.size() == 0) {
            createRecruitActivityApproval();
            getActivityWaitingApproval();
        }
        return ids;
    }

    /**
     * 查询活动列表中的状态为【进行中的ID】
     */
    public List<Long> getActivityWorking() {
        List<Long> ids =new ArrayList<>();

        //活动列表
        IScene scene = ActivityManageListScene.builder().page(1).size(10).build();
        int pages = visitor.invokeApi(scene).getInteger("pages");
        for (int page = 1; page <= pages; page++) {
            IScene scene1 = ActivityManageListScene.builder().page(page).size(10).build();
            JSONArray list = visitor.invokeApi(scene1).getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                String status = list.getJSONObject(i).getString("status");
                if (status.equals(ActivityStatusEnum.PASSED.getStatusName())) {
                    Long id = list.getJSONObject(i).getLong("id");
                    ids.add(id);
                }
            }
        }
        //创建活动并审批
        if (ids.size() == 0) {
            Long id1 = createRecruitActivityApproval();
            getApprovalPassed(id1);
            getActivityWorking();

        }
        return ids;
    }

    /**
     * 裂变活动-查询活动列表中的状态为【进行中的ID】
     */
    public List<Long> getFissionActivityWorking() {
        List<Long> ids = new ArrayList<>();
        //活动列表
        IScene scene = ActivityManageListScene.builder().page(1).size(10).build();
        int pages = visitor.invokeApi(scene).getInteger("pages");
        for (int page = 1; page <= pages; page++) {
            IScene scene1 = ActivityManageListScene.builder().page(page).size(10).build();
            JSONArray list = visitor.invokeApi(scene1).getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                String status = list.getJSONObject(i).getString("status");
                int activityType = list.getJSONObject(i).getInteger("activity_type");
                if (status.equals(ActivityStatusEnum.PASSED.getStatusName()) && activityType == 1) {
                   Long id = list.getJSONObject(i).getLong("id");
                   ids.add(id);
                }
            }
        }
        //创建活动并审批
        if (ids.size() == 0) {
           Long id1 = createRecruitActivityApproval();
            getApprovalPassed(id1);
            getFissionActivityWorking();

        }
        return ids;
    }

    /**
     * 招募活动-查询列表中的状态为【进行中的ID】
     */
    public List<Long> getRecruitActivityWorking() {
        List<Long> ids = new ArrayList<>();
        //活动列表
        IScene scene = ActivityManageListScene.builder().page(1).size(10).build();
        int pages = visitor.invokeApi(scene).getInteger("pages");
        for (int page = 1; page <= pages; page++) {
            IScene scene1 = ActivityManageListScene.builder().page(page).size(10).build();
            JSONArray list = visitor.invokeApi(scene1).getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                String status = list.getJSONObject(i).getString("status");
                int activityType = list.getJSONObject(i).getInteger("activity_type");
                if (status.equals(ActivityStatusEnum.PASSED.getStatusName()) && activityType == 2) {
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
            getRecruitActivityWorking();

        }
        return ids;
    }

    /**
     * 招募活动-查询列表中的状态为【进行中】的活动-存在待审批的人数
     */
    public List<Long> getRecruitActivityWorkingApproval() {
        List<Long> ids = new ArrayList<>();
        //活动列表
        IScene scene = ActivityManageListScene.builder().page(1).size(10).build();
        int pages = visitor.invokeApi(scene).getInteger("pages");
        for (int page = 1; page <= pages; page++) {
            IScene scene1 = ActivityManageListScene.builder().page(page).size(10).build();
            JSONArray list = visitor.invokeApi(scene1).getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                String status = list.getJSONObject(i).getString("status");
                int activityType = list.getJSONObject(i).getInteger("activity_type");
                int waitingAuditNum=list.getJSONObject(i).getInteger("waiting_audit_num");
                if (status.equals(ActivityStatusEnum.PASSED.getStatusName()) && activityType == 2&&waitingAuditNum>=1) {
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
            //小程序报名
            activityRegisterApplet(id1,"13373166806","Max",3,"1513814362@qq.com");
            getRecruitActivityWorkingApproval();
        }
        return ids;
    }

    /**
     * 查询列表中的状态为【审核未通过的ID】
     */
    public List<Long> getActivityReject() {
        List<Long> ids = new ArrayList<>();
        //活动列表
        IScene scene = ActivityManageListScene.builder().page(1).size(10).build();
        int pages = visitor.invokeApi(scene).getInteger("pages");
        for (int page = 1; page <= pages; page++) {
            IScene scene1 = ActivityManageListScene.builder().page(page).size(10).build();
            JSONArray list = visitor.invokeApi(scene1).getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                String status = list.getJSONObject(i).getString("status");
                if (status.equals(ActivityStatusEnum.REJECT.getStatusName())) {
                    Long id = list.getJSONObject(i).getLong("id");
                    ids.add(id);
                }
            }
        }
        //创建活动并审批不通过
        if (ids.size() > 0) {
            Long id = createRecruitActivityApproval();
            getApprovalReject(id);
            getActivityReject();

        }
        return ids;
    }

    /**
     * 查询列表中的状态为【待审批的ID】
     */
    public List<Long> getActivityWait() {

        List<Long> ids =new ArrayList<>();
        //活动列表
        IScene scene = ActivityManageListScene.builder().page(1).size(10).build();
        int pages = visitor.invokeApi(scene).getInteger("pages");
        for (int page = 1; page <= pages; page++) {
            IScene scene1 = ActivityManageListScene.builder().page(page).size(10).build();
            JSONArray list = visitor.invokeApi(scene1).getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                String status = list.getJSONObject(i).getString("status");
                if (status.equals(ActivityStatusEnum.PENDING.getStatusName())) {
                    Long id = list.getJSONObject(i).getLong("id");
                    ids.add(id);
                }
            }
        }
        //创建活动
        if (ids.size() == 0) {
            createRecruitActivityApproval();
            getActivityWait();
        }
        return ids;
    }

    /**
     * 查询列表中的状态为【审核已取消的ID】
     */
    public Long getActivityCancel() {
        Long id = 0L;
        //活动列表
        IScene scene = ActivityManageListScene.builder().page(1).size(10).build();
        int pages = visitor.invokeApi(scene).getInteger("pages");
        for (int page = 1; page <= pages; page++) {
            IScene scene1 = ActivityManageListScene.builder().page(page).size(10).build();
            JSONArray list = visitor.invokeApi(scene1).getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                String status = list.getJSONObject(i).getString("status");
                if (status.equals(ActivityStatusEnum.CANCELED.getStatusName())) {
                    id = list.getJSONObject(i).getLong("id");
                }
            }
        }
        //创建活动-审批通过活动-取消活动
        if (id == 0L) {
            //创建活动
            id = createRecruitActivityApproval();
            //审批通过
            getApprovalReject(id);
            //取消活动
            getCancelActivity(id);
            getActivityCancel();
        }
        return id;
    }


    /**
     * 获取活动的状态
     */
    public int getActivityStatus(Long id) {
        IScene scene = ActivityManageListScene.builder().page(1).size(10).build();
        int pages = visitor.invokeApi(scene).getInteger("pages");
        int status = 0;
        for (int page = 1; page <= pages; page++) {
            IScene scene2 = ActivityManageListScene.builder().page(page).size(10).build();
            JSONArray list = visitor.invokeApi(scene2).getJSONArray("list");
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
     * 活动管理-活动报名列表中待审批的ids
     */
    public List<Long> registerApproval(Long activityId) {
        //报名列表的中【待审批】的活动
        JSONObject response = getRegisterPage(activityId);
        int pages=response.getInteger("pages");
        List<Long> idArray = new ArrayList<>();
        for(int page=1;page<=pages;page++){
            IScene scene = ManageRegisterPageScene.builder().page(page).size(10).activityId(activityId).build();
            JSONArray  list = visitor.invokeApi(scene).getJSONArray("list");
            for(int i=0;i<list.size();i++){
                String  statusName=list.getJSONObject(i).getString("status_name");
                if (list.size() > 0&&statusName.equals(ActivityApprovalStatusEnum.PENDING.getStatusName())) {
                    //报名列表ID
                    Long ids= response.getJSONArray("list").getJSONObject(i).getLong("id");
                    idArray.add(ids);
                }
            }
        }
        return idArray;
    }

    /**
     * 报名审批列表【待审批】ids的合集
     */
    public List<Long> RegisterAppletIds(Long activityId){
        List<Long> ids=new ArrayList<>();
        IScene scene = ManageRegisterPageScene.builder().page(1).size(10).status(ActivityApprovalStatusEnum.PASSED.getId()).activityId(activityId).build();
        JSONObject response = visitor.invokeApi(scene);
        int pages=response.getInteger("pages");
        for (int page=1;page<=pages;page++){
            IScene scene1 = ManageRegisterPageScene.builder().page(page).size(10).status(ActivityApprovalStatusEnum.PASSED.getId()).activityId(activityId).build();
            JSONArray list = visitor.invokeApi(scene1).getJSONArray("list");
            for(int i=0;i<list.size();i++){
                Long id=list.getJSONObject(i).getLong("id");
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
        String message = visitor.invokeApi(scene, false).getString("message");
        return message;
    }

    /**
     * 活动管理-撤回活动申请
     */
    public String getRevokeActivity(Long id) {
        IScene scene = ManageRevokeScene.builder().id(id).build();
        String message = visitor.invokeApi(scene, false).getString("message");
        return message;
    }

    /**
     * 活动管理-取消活动
     */
    public String getCancelActivity(Long id) {
        IScene scene = ManageCancelScene.builder().id(id).build();
        String message = visitor.invokeApi(scene, false).getString("message");
        return message;
    }

    /**
     * 活动管理-推广活动
     */
    public String getPromotionActivity(Long id) {
        IScene scene = ManagePromotionScene.builder().id(id).build();
        String appletCodeUrl = visitor.invokeApi(scene).getString("applet_code_url");
        return appletCodeUrl;
    }


    /**
     * 活动管理-活动审批【通过】
     */
    public String getApprovalPassed(Long... id) {
        List<Long> ids = Arrays.asList(id);
        IScene scene = ManageApprovalScene.builder().ids(ids).status(ActivityApprovalStatusEnum.PASSED.getId()).build();
        String message = visitor.invokeApi(scene,false).getString("message");
        return message;
    }


    /**
     * 活动管理-活动审批【审核不通过】
     */
    public String getApprovalReject(Long... id) {
        List<Long> ids = Arrays.asList(id);
        IScene scene = ManageApprovalScene.builder().ids(ids).status(ActivityApprovalStatusEnum.REJECT.getId()).build();
        String message = visitor.invokeApi(scene,false).getString("message");
        return message;
    }

    /**
     * 活动管理-活动报名审批【通过】
     */
    public String getRegisterApprovalPassed(Long... id) {
        List<Long> ids = Arrays.asList(id);
        IScene scene = ManageRegisterApprovalScene.builder().ids(ids).status(ActivityApprovalStatusEnum.PASSED.getId()).build();
        String message = visitor.invokeApi(scene,false).getString("message");
        return message;
    }


    /**
     * 活动管理-活动报名审批【审核不通过】
     */
    public String getRegisterApprovalReject(Long... id) {
        List<Long> ids = Arrays.asList(id);
        IScene scene = ManageRegisterApprovalScene.builder().ids(ids).status(ActivityApprovalStatusEnum.REJECT.getId()).build();
        String message = visitor.invokeApi(scene,false).getString("message");
        return message;
    }


/**
 * ---------------------------------------------获取也买你返回值---------------------------------------------
 */


    /**
     * 调整记录列表
     */
    public JSONObject changeRecordPage(Long activityId) {
        IScene scene = ManageChangeRecordScene.builder().page(1).size(10).id(activityId).build();
        JSONObject response = visitor.invokeApi(scene);
        return response;
    }

    /**
     * 招募活动详情页-获取返回值在【活动奖励】内部
     */
    public JSONObject getRecruitActivityDetail(Long activityId) {
        IScene scene = ManageDetailScene.builder().id(activityId).build();
        JSONObject response = visitor.invokeApi(scene).getJSONObject("recruit_activity_info").getJSONObject("reward_vouchers");
        return response;
    }

    /**
     * 招募活动详情页返回值
     */
    public JSONObject getRecruitActivityDetailDate(Long activityId) {
        IScene scene = ManageDetailScene.builder().id(activityId).build();
        JSONObject response = visitor.invokeApi(scene).getJSONObject("recruit_activity_info");
        return response;
    }

    /**
     * 裂变活动详情页-获取返回值在【活动奖励】内部
     */
    public JSONObject getFissionActivityDetail(Long activityId) {
        IScene scene = ManageDetailScene.builder().id(activityId).build();
        JSONObject response = visitor.invokeApi(scene).getJSONObject("fission_voucher_info").getJSONObject("reward_vouchers");
        return response;
    }

    /**
     * 报名数据-返回值（data）
     */
    public JSONObject getRegisterData(Long activityId) {
        IScene scene = ManageRegisterDataScene.builder().activityId(activityId).build();
        JSONObject response = visitor.invokeApi(scene);
        return response;
    }

    /**
     * 报名列表-返回值（data）
     */
    public JSONObject getRegisterPage(Long activityId) {
        IScene scene = ManageRegisterPageScene.builder().page(1).size(10).activityId(activityId).build();
        JSONObject response = visitor.invokeApi(scene);
        return response;
    }

    /**
     * 活动审批数据-data
     */
    public JSONObject getActivityApprovalDate() {
        IScene scene = ActivityManageDate.builder().build();
        JSONObject response = visitor.invokeApi(scene);
        return response;
    }
    /**
     *活动审批列表返回值--data
     */
    public JSONObject getActivityManagePage(int status) {
        IScene scene = ActivityManageListScene.builder().page(1).size(10).status(status).build();
        JSONObject response = visitor.invokeApi(scene);
        return response;
    }


    /**
     * 获取招募活动的返回值
     */
    public JSONObject getActivityRespond(Long id) {
        IScene scene = ActivityManageListScene.builder().page(1).size(10).build();
        int pages = visitor.invokeApi(scene).getInteger("pages");
        JSONObject respondOne = null;
        for (int page = 1; page <= pages; page++) {
            IScene scene2 = ActivityManageListScene.builder().page(page).size(10).build();
            JSONArray list = visitor.invokeApi(scene2).getJSONArray("list");
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
     * 获取小程序-首页-文章列表-更多的返回值   AppletArticleList
     */
    public JSONObject getAppletArticleList(){
        IScene scene= AppletArticleList.builder().lastValue(1).size(10).build();
        JSONObject response=visitor.invokeApi(scene);
        return response;
    }



    /**
     * 判断客户报名是否重复，获取重复的手机号
     */
    public List<String> phoneSameArrayCheck(Long activityId){
        List<String> phoneSameArray=null;
        //报名列表的返回值
        JSONObject pageRes = getRegisterPage(activityId);
        int pages=pageRes.getInteger("pages");
        for (int page=1;page<=pages;page++){
            IScene scene=ManageRegisterPageScene.builder().page(page).size(10).activityId(activityId).build();
            JSONArray list=visitor.invokeApi(scene).getJSONArray("list");
            for(int i=0;i<list.size();i++){
                String customerPhone=list.getJSONObject(i).getString("customer_phone");
                IScene scene1=ManageRegisterPageScene.builder().page(page).size(10).activityId(activityId).build();
                JSONArray list1=visitor.invokeApi(scene1).getJSONArray("list");
                    for(int j=0;j<list1.size();j++){
                        String customerPhone1=list1.getJSONObject(i).getString("customer_phone");
                        if (customerPhone.equals(customerPhone1)){
                            phoneSameArray.add(customerPhone);
                        }
                    }
            }
        }
        return phoneSameArray;
    }



    /**
     *小程序报名招募活动
     */
    public void activityRegisterApplet(Long id,String phone,String name,int registerCount,String eMail){
        JSONArray registerItems=new JSONArray();
        //在活动详情中获得招募活动的报名信息
        JSONObject response=getRecruitActivityDetailDate(id);
        JSONArray registerInformationList=response.getJSONArray("register_information_list");
        for(int i=0;i<registerInformationList.size();i++){
            int type=registerInformationList.getJSONObject(i).getInteger("type");
            if(type==RegisterInfoEnum.PHONE.getId()){
                JSONObject jsonObjectPhone=new JSONObject();
                jsonObjectPhone.put("type",type);
                jsonObjectPhone.put("value",phone);
                registerItems.add(jsonObjectPhone);
            }else if(type==RegisterInfoEnum.NAME.getId()){
                JSONObject jsonObjectName=new JSONObject();
                jsonObjectName.put("type",type);
                jsonObjectName.put("value",name);
                registerItems.add(jsonObjectName);
            }else if(type==RegisterInfoEnum.REGISTER_COUNT.getId()){
                JSONObject jsonObjectRegisterCount=new JSONObject();
                jsonObjectRegisterCount.put("type",type);
                jsonObjectRegisterCount.put("value",registerCount);
                registerItems.add(jsonObjectRegisterCount);
            }else if(type==RegisterInfoEnum.EMAIL.getId()){
                JSONObject jsonObjectEMail=new JSONObject();
                jsonObjectEMail.put("type",type);
                jsonObjectEMail.put("value",eMail);
                registerItems.add(jsonObjectEMail);
            }
        }
        IScene scene= ArticleActivityRegisterScene.builder().id(id).registerItems(registerItems).build();
        visitor.invokeApi(scene);
    }



}
