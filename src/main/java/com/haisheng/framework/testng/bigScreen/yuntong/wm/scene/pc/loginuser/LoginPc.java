package com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.pc.loginuser;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

@Builder
public class LoginPc extends BaseScene {
    private final String phone;
    private final String verificationCode;

    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("phone", phone);
        object.put("verification_code", verificationCode);
        object.put("type", "1");
        return object;
    }

    // 这是我粘来的

    @Override
    public String getPath() {
        return "/account-platform/login-pc";
    }
}