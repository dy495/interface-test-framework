package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.loginuser;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

@Builder
public class LoginPc extends BaseScene {
    private final String phone;
    private final String verificationCode;
    private final Integer type;

    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("phone", phone);
        object.put("verification_code", verificationCode);
        object.put("type", type);
        return object;
    }

    @Override
    public String getPath() {
        return "/account-platform/login-pc";
    }
}
