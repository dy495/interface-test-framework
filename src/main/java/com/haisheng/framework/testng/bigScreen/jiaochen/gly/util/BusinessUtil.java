package com.haisheng.framework.testng.bigScreen.jiaochen.gly.util;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.agency.Visitor;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.activity.ActivityApprovalStatusEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.activity.ActivityStatusEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.activity.RegisterInfoEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing.VoucherStatusEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.activity.*;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.file.FileUpload;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.vouchermanage.VoucherDetailScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.vouchermanage.VoucherPage;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.util.SupporterUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.voucher.VoucherGenerator;
import com.haisheng.framework.util.DateTimeUtil;
import com.haisheng.framework.util.ImageUtil;

import java.text.SimpleDateFormat;
import java.util.*;

public class BusinessUtil {
    public Visitor visitor;

    public BusinessUtil(Visitor visitor) {
        this.visitor = visitor;
    }

    /**
     * @description :创建裂变活动-分享者奖励
     * @date :2021/1/22
     **/
    public JSONObject getShareVoucher(Long id, int type, String num, Integer expireType, String voucherStart, String voucherEnd, Integer voucherEffectiveDays) {
        JSONObject shareVoucher = new JSONObject();
        shareVoucher.put("id", id);
        shareVoucher.put("type", type);
        shareVoucher.put("num", num);
        JSONObject voucherValid = new JSONObject();
        voucherValid.put("expire_type", expireType);
        voucherValid.put("voucher_start", voucherStart);
        voucherValid.put("voucher_end", voucherEnd);
        shareVoucher.put("voucher_effective_days", voucherEffectiveDays);
        shareVoucher.put("voucher_valid", voucherValid);
        return shareVoucher;
    }

    /**
     * @description :创建裂变活动-被邀请者奖励
     * @date :2021/1/22
     **/
    public JSONObject getinvitedVoucher(Long id, int type, String num, Integer expireType, String voucherStart, String voucherEnd, Integer voucherEffectiveDays) {
        JSONObject invitedVoucher = new JSONObject();
        invitedVoucher.put("id", id);
        invitedVoucher.put("type", type);
        invitedVoucher.put("num", num);
        JSONObject voucherValid = new JSONObject();
        voucherValid.put("expire_type", expireType);
        voucherValid.put("voucher_start", voucherStart);
        voucherValid.put("voucher_end", voucherEnd);
        invitedVoucher.put("voucher_effective_days", voucherEffectiveDays);
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
     * @description :创建招募活动-奖励有效期
     * @date :2021/1/24
     **/
    public JSONObject getVoucherValid(int expireType, String voucherStart, String voucherEnd, int voucherEffectiveDays) {
        JSONObject voucherValid = new JSONObject();
        voucherValid.put("expire_type", expireType);
        voucherValid.put("voucher_start", voucherStart);
        voucherValid.put("voucher_end", voucherEnd);
        voucherValid.put("voucher_effective_days", voucherEffectiveDays);
        return voucherValid;
    }


    /**
     * 获取当前时间
     */
    public String nowTimeFormat() {
        // 格式化时间
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
        // 获取当前时间
        Date date = new Date();
        // 输出已经格式化的现在时间（24小时制）
        String nowTime = sdf.format(date);
        return nowTime;
    }

    /**
     * 获取当前时间+10天
     */

    public String futureTimeFormat() {
        //当前时间+10天的日期
        return DateTimeUtil.addDayFormat(new Date(), 10);
    }

    /**
     * 活动管理-创建裂变活动
     */
    public void createVoucherActivity() {
        List<String> picList = new ArrayList<>();
        SupporterUtil supporterUtil = new SupporterUtil(visitor);
        PublicParameter pp = new PublicParameter();
        BusinessUtil businessUtil = new BusinessUtil(visitor);
        picList.add(supporterUtil.getPicPath());
        //获取优惠券ID
        Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
        // 创建被邀请者和分享者的信息字段
        JSONObject invitedVoucher = getinvitedVoucher(voucherId, 1, "1", 2, "", "", 3);
        JSONObject shareVoucher = getShareVoucher(pp.packageId, 2, "1", 2, "", "", 3);
        IScene scene = FissionVoucherAddScene.builder()
                .type(0).participationLimitType(0)
                .receiveLimitType(0)
                .title(pp.fissionVoucherName)
                .rule(pp.rule)
                .startDate(businessUtil.nowTimeFormat())
                .endDate(futureTimeFormat())
                .subjectType(supporterUtil.getSubjectType())
                .subjectId(supporterUtil.getSubjectDesc(supporterUtil.getSubjectType()))
                .label("RED_PAPER")
                .picList(picList)
                .shareNum("3")
                .shareVoucher(shareVoucher)
                .invitedVoucher(invitedVoucher)
                .build();
        visitor.invokeApi(scene);

//        FissionVoucherAddScene.FissionVoucherAddSceneBuilder  builder=FissionVoucherAddScene.builder().invitedVoucher().chooseLabels();
//
//        //liebian
//        builder.chooseLabels();

//        //huodog
//        builder.endDate();
//        visitor.invokeApi(builder.build());
    }

    public Long createRecruitActivity(){
        Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
       return createRecruitActivity(voucherId,true,0,true);
    }

    /**
     * 活动管理-创建招募活动
     */
    public Long createRecruitActivity(Long voucherId, boolean award, int rewardReceiveType, boolean isNeedApproval) {
        List<String> picList = new ArrayList<>();
        SupporterUtil supporterUtil = new SupporterUtil(visitor);
        PublicParameter pp = new PublicParameter();
        BusinessUtil businessUtil = new BusinessUtil(visitor);
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
        JSONArray registerObject = getRewardVouchers(voucherId, 1, 10);

        //卡券有效期
        JSONObject voucherValid = getVoucherValid(1, "", "", 10);

        //创建招募活动-共有的--基础信息
        ManageRecruitAddScene.ManageRecruitAddSceneBuilder builder = ManageRecruitAddScene.builder()
                .type(1)
                .participationLimitType(0)
                .receiveLimitType(0)
                .title(pp.RecruitName)
                .rule(pp.rule)
                .startDate(businessUtil.nowTimeFormat())
                .endDate(futureTimeFormat())
                .subjectType(supporterUtil.getSubjectType())
                .subjectId(supporterUtil.getSubjectDesc(supporterUtil.getSubjectType()))
                .label("RED_PAPER")
                .picList(picList)
                .applyStart(nowTimeFormat())
                .applyEnd(futureTimeFormat())
                .isLimitQuota(true)
                .quota(2)
                .address(pp.address)
                .registerInformationList(registerInformationList)
                .successReward(true)
                .rewardReceiveType(rewardReceiveType)
                .isNeedApproval(isNeedApproval);
        if (award) {
            //增加有奖励的字段
            builder.rewardVouchers(registerObject)
                    .voucherValid(voucherValid);
        }
        return visitor.invokeApi(builder.build()).getLong("id");
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
        IScene scene1 = VoucherPage.builder().page(1).size(10).build();
        int pages = visitor.invokeApi(scene1).getInteger("pages");
        String surplusInventory = "";
        for (int page = 1; page <= pages; page++) {
            IScene scene2 = VoucherPage.builder().page(page).size(10).build();
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
            IScene scene2 = VoucherPage.builder().page(page).size(10).build();
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
     * 查询列表中的状态为【待审核的ID】
     */
    public Long getActivityWaitingApproval() {
        Long id = 0L;
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
            for (int i = 0; i <list.size(); i++) {
                String status = list.getJSONObject(i).getString("status");
                if (status.equals(ActivityStatusEnum.PENDING.getStatusName())) {
                    id = list.getJSONObject(i).getLong("id");
                }
            }
        }
        if(id==0){
            id= createRecruitActivity();
        }
        return id;
    }

    /**
     * 查询列表中的状态为【进行中的ID】
     */
    public Long getActivityWorking() {
        Long id = 0L;
        //活动列表
        IScene scene = ActivityManageListScene.builder().page(1).size(10).build();
        int pages = visitor.invokeApi(scene).getInteger("pages");
        for (int page = 1; page <= pages; page++) {
            IScene scene1 = ActivityManageListScene.builder().page(page).size(10).build();
            JSONArray list = visitor.invokeApi(scene1).getJSONArray("list");
            for (int i = 0; i <list.size(); i++) {
                String status = list.getJSONObject(i).getString("status");
                if (status.equals(ActivityStatusEnum.PASSED.getStatusName())) {
                    id = list.getJSONObject(i).getLong("id");
                }
            }
        }
        //创建活动并审批
        if(id==0){
            id= createRecruitActivity();
            getApprovalPassed(id);

        }
        return id;
    }

    /**
     *  裂变活动-查询列表中的状态为【进行中的ID】
     */
    public Long getFissionActivityWorking() {
        Long id = 0L;
        //活动列表
        IScene scene = ActivityManageListScene.builder().page(1).size(10).build();
        int pages = visitor.invokeApi(scene).getInteger("pages");
        for (int page = 1; page <= pages; page++) {
            IScene scene1 = ActivityManageListScene.builder().page(page).size(10).build();
            JSONArray list = visitor.invokeApi(scene1).getJSONArray("list");
            for (int i = 0; i <list.size(); i++) {
                String status = list.getJSONObject(i).getString("status");
                int activityType=list.getJSONObject(i).getInteger("activity_type");
                if (status.equals(ActivityStatusEnum.PASSED.getStatusName())&&activityType==1) {
                    id = list.getJSONObject(i).getLong("id");
                }
            }
        }
        //创建活动并审批
        if(id==0){
            id= createRecruitActivity();
            getApprovalPassed(id);

        }
        return id;
    }

    /**
     * 招募活动-查询列表中的状态为【进行中的ID】
     */
    public Long getRecruitActivityWorking() {
        Long id = 0L;
        //活动列表
        IScene scene = ActivityManageListScene.builder().page(1).size(10).build();
        int pages = visitor.invokeApi(scene).getInteger("pages");
        for (int page = 1; page <= pages; page++) {
            IScene scene1 = ActivityManageListScene.builder().page(page).size(10).build();
            JSONArray list = visitor.invokeApi(scene1).getJSONArray("list");
            for (int i = 0; i <list.size(); i++) {
                String status = list.getJSONObject(i).getString("status");
                int activityType=list.getJSONObject(i).getInteger("activity_type");
                if (status.equals(ActivityStatusEnum.PASSED.getStatusName())&&activityType==2) {
                    id = list.getJSONObject(i).getLong("id");
                }
            }
        }
        //创建活动并审批
        if(id==0){
            //创建活动
            id= createRecruitActivity();
            //todo 小程序报名活动

            //审批活动
            getApprovalPassed(id);

        }
        return id;
    }

    /**
     * 查询列表中的状态为【审核未通过的ID】
     */
    public Long getActivityReject() {
        Long id = 0L;
        //活动列表
        IScene scene = ActivityManageListScene.builder().page(1).size(10).build();
        int pages = visitor.invokeApi(scene).getInteger("pages");
        for (int page = 1; page <= pages; page++) {
            IScene scene1 = ActivityManageListScene.builder().page(page).size(10).build();
            JSONArray list = visitor.invokeApi(scene1).getJSONArray("list");
            for (int i = 0; i <list.size(); i++) {
                String status = list.getJSONObject(i).getString("status");
                if (status.equals(ActivityStatusEnum.REJECT.getStatusName())) {
                    id = list.getJSONObject(i).getLong("id");
                }
            }
        }
        //创建活动并审批不通过
        if(id==0){
            id= createRecruitActivity();
            getApprovalReject(id);

        }
        return id;
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
            for (int i = 0; i <list.size(); i++) {
                String status = list.getJSONObject(i).getString("status");
                if (status.equals(ActivityStatusEnum.CANCELED.getStatusName())) {
                    id = list.getJSONObject(i).getLong("id");
                }
            }
        }
        //创建活动-审批通过活动-取消活动
        if(id==0L){
            //创建活动
            id= createRecruitActivity();
            //审批通过
            getApprovalReject(id);
            //取消活动
            getCancelActivity(id);
        }
        return id;
    }



    /**
     * 获取活动的状态
     */
    public int getActivityStatus(Long id){
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
     * 获取招募活动的返回值
     */
    public JSONObject getActivityRespond(Long id){
        IScene scene = ActivityManageListScene.builder().page(1).size(10).build();
        int pages = visitor.invokeApi(scene).getInteger("pages");
        JSONObject respondOne = null;
        for (int page = 1; page <= pages; page++) {
            IScene scene2 = ActivityManageListScene.builder().page(page).size(10).build();
            JSONArray list = visitor.invokeApi(scene2).getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                Long activityId = list.getJSONObject(i).getLong("id");
                if (activityId.equals(id)) {
                    respondOne=list.getJSONObject(i);
                }
            }
        }
        return respondOne;
    }


    /**
     * 活动管理-删除活动
     */
    public String getDelActivity(Long id){
        IScene scene= ManageDeleteScene.builder().id(id).build();
        String message=visitor.invokeApi(scene, false).getString("message");
        return message;
    }

    /**
     * 活动管理-撤回活动申请
     */
    public String getRevokeActivity(Long id){
        IScene scene= ManageRevokeScene.builder().id(id).build();
        String message=visitor.invokeApi(scene, false).getString("message");
        return message;
    }

    /**
     * 活动管理-取消活动
     */
    public String getCancelActivity(Long id){
        IScene scene= ManageCancelScene.builder().id(id).build();
        String message=visitor.invokeApi(scene, false).getString("message");
        return message;
    }

    /**
     * 活动管理-推广活动
     */
    public String getPromotionActivity(Long id){
        IScene scene= ManagePromotionScene.builder().id(id).build();
        String appletCodeUrl=visitor.invokeApi(scene).getString("applet_code_url");
        return appletCodeUrl;
    }



    /**
     * 活动管理-审批活动【通过】
     */
    public String getApprovalPassed(Long id){
        List<Long> idArray=new ArrayList<>();
        idArray.add(id);
        IScene scene= ManageApprovalScene.builder().ids(idArray).status(ActivityApprovalStatusEnum.PASSED.getId()).build();
        return visitor.invokeApi(scene,false).getString("message");
    }

    /**
     * 活动管理-审批活动【审核不通过】
     */
    public String getApprovalReject(Long id){
        List<Long> idArray=new ArrayList<>();
        idArray.add(id);
        IScene scene= ManageApprovalScene.builder().ids(idArray).status(ActivityApprovalStatusEnum.REJECT.getId()).build();
        return visitor.invokeApi(scene,false).getString("message");
    }


    /**
     * 活动管理-活动报名审批--通过列表中的第一个
     */
    public String registerApproval(Long activityId){
        //活动审批列表的中【待审批】的活动
        IScene scene=ManageRegisterScene.builder().page(1).size(10).activityId(activityId).status(ActivityStatusEnum.PENDING.getId()).build();
        JSONObject response=visitor.invokeApi(scene);
        JSONArray list=response.getJSONArray("list");
        List<Long> idArray=new ArrayList<>();
        String message="";
        if(list.size()>0){
            //报名列表ID
            Long ids=response.getJSONArray("list").getJSONObject(0).getLong("ids");
            idArray.add(ids);
            IScene scene1=ManageRegisterApprovalScene.builder().ids(idArray).activityId(activityId).status(101).build();
            message=visitor.invokeApi(scene1,false).getString("message");
        }else{
            message="当前的报名列表中没有【待审批】的活动";
        }
        return message;

    }
    /**
     * 调整记录列表
     */
    public JSONObject changeRecordPage(Long activityId){
        IScene scene =ManageChangeRecordScene.builder().page(1).size(10).id(activityId).build();
        JSONObject response=visitor.invokeApi(scene);
        return response;
    }

    /**
     * 招募活动详情页-获取返回值在【活动奖励】内部
     */
    public JSONObject getRecruitActivityDetail(Long activityId){
        IScene scene=ManageDetailScene.builder().id(activityId).build();
        JSONObject response=visitor.invokeApi(scene).getJSONObject("recruit_activity_info").getJSONObject("reward_vouchers");
        return response;
    }

    /**
     * 裂变活动详情页-获取返回值在【活动奖励】内部
     */
    public JSONObject getFissionActivityDetail(Long activityId){
        IScene scene=ManageDetailScene.builder().id(activityId).build();
        JSONObject response=visitor.invokeApi(scene).getJSONObject("fission_voucher_info").getJSONObject("reward_vouchers");
        return response;
    }

    /**
     * 报名数据-返回值（data）
     */
    public JSONObject getRegisterData(Long activityId){
        IScene scene=ManageRegisterDataScene.builder().activityId(activityId).build();
        JSONObject response=visitor.invokeApi(scene);
        return response;
    }

    /**
     * 报名列表-返回值（data）
     */
    public JSONObject getRegisterPage(Long activityId){
        IScene scene=ManageRegisterPageScene.builder().page(1).size(10).activityId(activityId).build();
        JSONObject response=visitor.invokeApi(scene);
        return response;
    }


}
