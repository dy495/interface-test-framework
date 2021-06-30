package com.haisheng.framework.testng.bigScreen.itemPorsche.scene.app;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.casedaily.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 登录接口
 *
 * @author wangmin
 */
@Builder
public class LoginScene extends BaseScene {
    @Builder.Default
    private final int type = 0;
    private final String username;
    private final String password;

    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("type", type);
        object.put("username", username);
        object.put("password", password);
        return object;
    }

    @Override
    public String getPath() {
        return "/porsche-login";
    }

    @Override
    public String getIpPort() {
        return null;
    }
}
