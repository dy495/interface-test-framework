package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.loginuser;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

@Builder
public class LoginApp extends BaseScene {
    private final String phone;
    private final String verificationCode;

    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("phone", phone);
        object.put("verification_code", verificationCode);
        return object;
    }

    @Override
    public String getPath() {
        return "/account-platform/login-m-app";
    }
}
