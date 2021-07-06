package com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.pc;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

@Builder
public class PatrolLoginScene extends BaseScene {
    private final String username;
    private final String password;
    private final Integer type;

    @Override
    public JSONObject getRequestBody() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", username);
        jsonObject.put("password", password);
        jsonObject.put("type", type);
        return jsonObject;
    }

    @Override
    public String getPath() {
        return "/patrol-login";
    }
}
