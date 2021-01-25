package com.haisheng.framework.testng.bigScreen.jiaochen.gly.util;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.agency.Visitor;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.activity.RegisterInfoEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing.VoucherStatusEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.activity.FissionVoucherAddScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.activity.RecruitAddScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.file.FileUpload;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.util.SupporterUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.voucher.VoucherGenerator;
import com.haisheng.framework.util.DateTimeUtil;
import com.haisheng.framework.util.ImageUtil;

import java.text.SimpleDateFormat;
import java.util.*;

public class BusinessUtil {
    public Visitor visitor;
    public BusinessUtil(Visitor visitor){
        this.visitor=visitor;
    }
    /**
     * @description :创建裂变活动-分享者奖励
     * @date :2021/1/22
     **/
    public JSONObject getShareVoucher(Long id, int type, Integer num, Integer expireType, String voucherStart, String voucherEnd, Integer voucherEffectiveDays) {
        JSONObject shareVoucher =new JSONObject();
        shareVoucher.put("id",id);
        shareVoucher.put("type", type);
        shareVoucher.put("num",num);
        JSONObject voucherValid=new JSONObject();
        voucherValid.put("expire_type",expireType);
        voucherValid.put("voucher_start",voucherStart);
        voucherValid.put("voucher_end",voucherEnd);
        shareVoucher.put("voucher_effective_days",voucherEffectiveDays);
        shareVoucher.put("voucher_valid",voucherValid);
        return shareVoucher;
    }

    /**
     * @description :创建裂变活动-被邀请者奖励
     * @date :2021/1/22
     **/
    public JSONObject getinvitedVoucher(Long id,int type,Integer num,Integer expireType,String voucherStart,String voucherEnd,Integer voucherEffectiveDays) {
        JSONObject invitedVoucher =new JSONObject();
        invitedVoucher.put("id",id);
        invitedVoucher.put("type", type);
        invitedVoucher.put("num",num);
        JSONObject voucherValid=new JSONObject();
        voucherValid.put("expire_type",expireType);
        voucherValid.put("voucher_start",voucherStart);
        voucherValid.put("voucher_end",voucherEnd);
        invitedVoucher.put("voucher_effective_days",voucherEffectiveDays);
        invitedVoucher.put("voucher_valid",voucherValid);
        return invitedVoucher;
    }

    /**
     * @description :创建招募活动-报名所需信息
     * @date :2021/1/24
     **/
    public JSONArray getRegisterInformationList(List<Boolean> isShow,List<Boolean> isRequired) {
        JSONArray array=new JSONArray();
        for(int i=0;i<RegisterInfoEnum.values().length;i++){
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("type",RegisterInfoEnum.values()[i].getId());
            jsonObject.put("name",RegisterInfoEnum.values()[i].getName());
            jsonObject.put("is_show", isShow.get(i));
            jsonObject.put("is_required",isRequired.get(i));
            array.add(jsonObject);
        }
        return array;
    }

    /**
     * @description :创建招募活动-卡券奖励
     * @date :2021/1/24
     **/
    public JSONArray getRewardVouchers(Long id,int type,int num) {
        JSONArray rewardVouchers =new JSONArray();
        JSONObject object=new JSONObject();
        JSONObject voucherValid=new JSONObject();
        object.put("id",id);
        object.put("type",type);
        object.put("num",num);
        rewardVouchers.add(voucherValid);
        return rewardVouchers;
    }

    /**
     * @description :创建招募活动-奖励有效期
     * @date :2021/1/24
     **/
    public JSONObject getVoucherValid(int expireType,String voucherStart,String voucherEnd,int voucherEffectiveDays) {
        JSONObject voucherValid=new JSONObject();
        voucherValid.put("expire_type",expireType);
        voucherValid.put("voucher_start",voucherStart);
        voucherValid.put("voucher_end",voucherEnd);
        voucherValid.put("voucher_effective_days",voucherEffectiveDays);
        return voucherValid;
    }



    /**
     *一个简单的测试写法
     */
//    public Integer createActivity(){
//        JSONObject invitedVoucher=getinvitedVoucher("");
//        JSONObject shareVoucher=getShareVoucher();
//        IScene scene=  FissionVoucherAdd.builder().invitedVoucher(invitedVoucher).shareVoucher(shareVoucher).build();
//        JSONObject response=visitor.invokeApi(scene);
//        return  response.getInteger("id");
//    }

    /**
     * 获取当前时间
     */
    public String nowTimeFormat(){
        // 格式化时间
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
        // 获取当前时间
        Date date = new Date();
        // 输出已经格式化的现在时间（24小时制）
        String nowTime=sdf.format(date);
        return nowTime;
    }

    /**
     * 获取当前时间+10天
     */

    public String futureTimeFormat()  {
        //当前时间+10天的日期
       return DateTimeUtil.addDayFormat(new Date(),10);
    }
    /**
     *活动管理-创建裂变活动
     */
    public void createVoucherActivity(){
        List<String>picList=new ArrayList<>();
        SupporterUtil supporterUtil=new SupporterUtil(visitor);
        PublicParameter pp=new PublicParameter();
        BusinessUtil businessUtil =new BusinessUtil(visitor);
        picList.add(supporterUtil.getPicPath());
        //获取优惠券ID
        Long voucherId= new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
        // 创建被邀请者和分享者的信息字段
        JSONObject invitedVoucher=getinvitedVoucher(voucherId,1,1,2,"","",3);
        JSONObject shareVoucher=getShareVoucher(pp.packageId,2,1,2,"","",3);
        IScene scene= FissionVoucherAddScene.builder()
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

   /**
    *活动管理-创建招募活动
    */
   public void createRecruitActivity(){
       List<String>picList=new ArrayList<>();
       SupporterUtil supporterUtil=new SupporterUtil(visitor);
       PublicParameter pp=new PublicParameter();
       BusinessUtil businessUtil =new BusinessUtil(visitor);
       picList.add(supporterUtil.getPicPath());

       //获取优惠券ID
       Long voucherId= new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();

       //填写报名所需要信息
       List<Boolean> isShow=new ArrayList<>();
       isShow.add(true);
       isShow.add(true);
       isShow.add(true);
       isShow.add(true);
       List<Boolean> isRequired=new ArrayList<>();
       isRequired.add(true);
       isRequired.add(true);
       isRequired.add(true);
       isRequired.add(true);
       JSONArray registerInformationList=getRegisterInformationList(isShow,isRequired);

       //报名成功奖励
       JSONArray registerObject=getRewardVouchers(voucherId,1,1);

       //卡券有效期
       JSONObject voucherValid=getVoucherValid(1,"","",10);

       //创建招募活动
       IScene scene= RecruitAddScene.builder()
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
               .rewardVouchers(registerObject)
               .voucherValid(voucherValid)
               .rewardReceiveType(0)
               .isNeedApproval(true)
               .build();
       visitor.invokeApi(scene);
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



}
