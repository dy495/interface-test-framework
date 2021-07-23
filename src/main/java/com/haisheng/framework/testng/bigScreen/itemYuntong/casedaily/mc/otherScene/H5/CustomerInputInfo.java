package com.haisheng.framework.testng.bigScreen.itemYuntong.casedaily.mc.otherScene.H5;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

@Builder
public class CustomerInputInfo extends BaseScene {
    /**
     * url附带的接待id
     * Required : true
     **/
    private final int reception_id;
    /**
     * 客户名称
     * Required : false
     **/
    private final int customer_name;
    /**
     * 客户手机号
     * Required : false
     **/
    private final int customer_phone;
    /**
     * 客户性别
     * Required : false
     **/
    private final int sex_id;
    /**
     * 验证码
     * Required : false
     **/
    private final int verification_code;
    /**
     * 意向车型Id
     * Required : false
     **/
    private final int intention_car_model_id;
    /**
     * 预计购车时间
     * Required : false
     **/
    private final int estimate_buy_car_time;
    /**
     * 是否授权
     * Required : false
     **/
    private final int is_empower;

    @Override
    protected JSONObject getRequestBody() {
        JSONObject obj = new JSONObject();
        obj.put("reception_id",reception_id);
        obj.put("customer_name",customer_name);
        obj.put("customer_phone",customer_phone);
        obj.put("sex_id",sex_id);
        obj.put("verification_code",verification_code);
        obj.put("intention_car_model_id",intention_car_model_id);
        obj.put("estimate_buy_car_time",estimate_buy_car_time);
        obj.put("is_empower",is_empower);
        return obj;
    }

    @Override
    public String getPath() {
        return "/car-platform/h5/pre-sales-reception/customer-for-client/submit";
    }
}
