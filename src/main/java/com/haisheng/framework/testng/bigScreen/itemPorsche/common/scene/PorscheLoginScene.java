package com.haisheng.framework.testng.bigScreen.itemPorsche.common.scene;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

@Builder
public class PorscheLoginScene extends BaseScene {
    private final String username;
    private final String password;

    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("type", 0);
        object.put("username", username);
        object.put("password", password);
        return object;
    }

    @Override
    public String getPath() {
        return "/porsche-login";
    }
}
