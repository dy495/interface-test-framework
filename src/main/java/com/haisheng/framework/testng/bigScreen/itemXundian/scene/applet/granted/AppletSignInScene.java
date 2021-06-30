package com.haisheng.framework.testng.bigScreen.itemXundian.scene.applet.granted;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.casedaily.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 小程序签到
 */
@Builder
public class AppletSignInScene extends BaseScene {

    @Override
    public JSONObject getRequestBody() {
        return new JSONObject();
    }

    @Override
    public String getPath() {
        return "/patrol-applet/granted/member-center/sign-in";
    }

}
