package com.haisheng.framework.testng.bigScreen.crm.wm.scene.app;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.model.experiment.enumerator.EnumAddress;
import com.haisheng.framework.testng.bigScreen.crm.wm.scene.base.BaseScene;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 顾客完成接待接口
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
public
class CustomerFinishReception extends BaseScene {
    //是 客户id
    private long customerId;
    //是 客户名称
    @Builder.Default
    private String name = "String";
    //是 顾客类型
    @Builder.Default
    private String subjectType = "默认值";
    // 是 顾客称呼
    private String call;
    //是 区域编码
    private String districtCode;
    //否 详细地址
    private String address;
    //否 生日
    private String birthday;
    //否 身份证号
    private String idNumber;
    //是 标识
    private long id;
    //是 手机号码
    private String phone;
    //是 手机号类型
    private Integer type;
    //是 意向车型
    private long intentionCarModel;
    //是 预计购车时间
    private String expectedBuyDay;
    //否 试驾车型
    private String testDriveCarModel;
    //否 付款方式
    private Integer payType;
    //否 评估车型
    private long assessCarModel;
    //否 顾客来源
    private Integer sourceChannel;
    //否 对比车型
    private long compareCarModel;
    //否 现有车型
    private long ownCarModel;
    //否 来访人数
    private Integer visitCountType;
    //否 是否报价
    private Integer isOffer;
    //否 购车类型
    private long buyCarType;
    //否 是否二手车评估
    private Integer isAssessed;

    @Override
    public JSONObject invokeApi() {
        JSONObject object = new JSONObject();
        object.put("customer_id", customerId);
        object.put("name", name);
        object.put("subject_type", subjectType);
        object.put("call", call);
        object.put("address", address);
        object.put("birthday", birthday);
        object.put("id_number", idNumber);
        object.put("id", id);
        JSONArray phoneList = new JSONArray();
        JSONObject s = new JSONObject();
        s.put("phone", phone);
        s.put("phone_order", 0);
        phoneList.add(s);
        object.put("phone_list", phoneList);
        object.put("type", type);
        object.put("intention_car_model", intentionCarModel);
        object.put("expected_buy_day", expectedBuyDay);
        object.put("test_drive_car_model", testDriveCarModel);
        object.put("pay_type", payType);
        object.put("assess_car_model", assessCarModel);
        object.put("source_channel", sourceChannel);
        object.put("compare_car_model", compareCarModel);
        object.put("own_car_model", ownCarModel);
        object.put("visit_count_type", visitCountType);
        object.put("is_offer", isOffer);
        object.put("buy_car_type", buyCarType);
        object.put("is_assessed", isAssessed);
        return execute(object, true);
    }

    @Override
    public String getPath() {
        return EnumAddress.PORSCHE.getAddress();
    }

    @Override
    public String getIpPort() {
        return "/porsche/app/customer/finishReception";
    }
}
