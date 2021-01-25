package com.haisheng.framework.testng.bigScreen.jiaochen.gly.util;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.agency.Visitor;

public class BusinessUtil {
    public Visitor visitor;
    public BusinessUtil(Visitor visitor){
        this.visitor=visitor;
    }
    /**
     * @description :创建裂变活动-分享者奖励
     * @date :2021/1/22
     **/
    public JSONObject getShareVoucher(Long id, String type, Integer num, Integer expireType, String voucherStart, String voucherEnd, Integer voucherEffectiveDays) {
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
    public JSONObject getinvitedVoucher(Long id,String type,Integer num,Integer expireType,String voucherStart,String voucherEnd,Integer voucherEffectiveDays) {
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
    public JSONArray getRegisterInformationList(int type,String name,Boolean isShow,Boolean isRequired) {
        JSONArray registerInformationList =new JSONArray();
        JSONObject object=new JSONObject();
        object.put("type",type);
        object.put("name",name);
        object.put("is_show",isShow);
        object.put("is_required",isRequired);
        registerInformationList.add(object);
        return registerInformationList;
    }

    /**
     * @description :创建招募活动-卡券奖励
     * @date :2021/1/24
     **/
    public JSONArray getRewardVouchers(Long id,int type,int num,int expireType,String voucherStart,String voucherEnd,int voucherEffectiveDays) {
        JSONArray rewardVouchers =new JSONArray();
        JSONObject object=new JSONObject();
        JSONObject voucherValid=new JSONObject();
        object.put("id",id);
        object.put("type",type);
        object.put("num",num);
        object.put("voucher_valid",voucherValid);
        voucherValid.put("expire_type",expireType);
        voucherValid.put("voucher_start",voucherStart);
        voucherValid.put("voucher_end",voucherEnd);
        voucherValid.put("voucher_effective_days",voucherEffectiveDays);
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
//     return    response.getInteger("id");
//    }

}
