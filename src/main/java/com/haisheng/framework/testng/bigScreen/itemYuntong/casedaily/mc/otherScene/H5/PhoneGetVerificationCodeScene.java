package com.haisheng.framework.testng.bigScreen.itemYuntong.casedaily.mc.otherScene.H5;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

@Builder
public class PhoneGetVerificationCodeScene extends BaseScene {

    private final long receptionId;
    private final String phone;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject obj = new JSONObject();
        obj.put("receptionId",receptionId);
        obj.put("phone",phone);
        return obj;
    }

    @Override
    public String getPath() {
        return "/car-platform/h5/pre-sales-reception/verification-code";
    }
}
