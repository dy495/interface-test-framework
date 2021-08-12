package com.haisheng.framework.testng.bigScreen.itemMall.common.scene.pc;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

@Builder
public class LoginPcMall extends BaseScene {

    private final String type;
    private final String password;
    private final String username;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("type", type);
        object.put("password", password);
        object.put("username", username);
        return object;
    }

    @Override
    public String getPath() {
        return "/account-platform/login-pc";
    }
}
