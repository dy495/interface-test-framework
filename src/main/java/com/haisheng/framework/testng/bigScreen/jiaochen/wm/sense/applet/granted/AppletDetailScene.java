package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.granted;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

@Builder
public class AppletDetailScene extends BaseScene {

    @Override
    public JSONObject getRequestBody() {
        return new JSONObject();
    }

    @Override
    public String getPath() {
        return "/car-platform/applet/granted/user-info/detail";
    }
}
