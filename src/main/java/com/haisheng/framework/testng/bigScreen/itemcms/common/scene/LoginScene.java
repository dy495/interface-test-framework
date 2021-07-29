package com.haisheng.framework.testng.bigScreen.itemcms.common.scene;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

@Builder
public class LoginScene extends BaseScene {
    private final String email;
    private final String password;

    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("email", email);
        object.put("password", password);
        return object;
    }

    @Override
    public String getPath() {
        return "/administrator/login";
    }
}
