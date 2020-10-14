package com.haisheng.framework.testng.bigScreen.crm.wm.scene.app;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumAddress;
import com.haisheng.framework.testng.bigScreen.crm.wm.scene.BaseScene;
import lombok.Builder;

/**
 * 登录接口
 * 测试使用
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
    public JSONObject getJSONObject() {
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
        return EnumAddress.PORSCHE.getAddress();
    }
}
