package com.haisheng.framework.testng.bigScreen.itemYuntong.casedaily.mc.otherScene.H5;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

@Builder
public class CustomerForClientSubmitScene extends BaseScene {
    /**
     * url附带的接待id
     * Required : true
     **/
    private final long receptionId;
    /**
     * 客户名称
     * Required : false
     **/
    private final String customerName;
    /**
     * 客户手机号
     * Required : false
     **/
    private final String customerPhone;
    /**
     * 客户性别
     * Required : false
     **/
    private final int sexId;
    /**
     * 验证码
     * Required : false
     **/
    private final String verificationCode;
    /**
     * 意向车型Id
     * Required : false
     **/
    private final String intentionCarModelId;
    /**
     * 预计购车时间
     * Required : false
     **/
    private final String estimateBuyCarTime;
    /**
     * 是否授权
     * Required : false
     **/
    private final boolean isEmpower;

    @Override
    protected JSONObject getRequestBody() {
        JSONObject obj = new JSONObject();
        obj.put("reception_id", receptionId);
        obj.put("customer_name", customerName);
        obj.put("customer_phone", customerPhone);
        obj.put("sex_id", sexId);
        obj.put("verification_code", verificationCode);
        obj.put("intention_car_model_id", intentionCarModelId);
        obj.put("estimate_buy_car_time", estimateBuyCarTime);
        obj.put("is_empower", isEmpower);
        return obj;
    }

    @Override
    public String getPath() {
        return "/car-platform/h5/pre-sales-reception/customer-for-client/submit";
    }
}
