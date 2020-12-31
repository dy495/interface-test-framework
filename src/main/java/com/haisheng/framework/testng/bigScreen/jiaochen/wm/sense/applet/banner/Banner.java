package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.banner;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.scene.BaseScene;
import lombok.Builder;

@Builder
public class Banner extends BaseScene {
    @Override
    public JSONObject getJSONObject() {
        return new JSONObject();
    }

    @Override
    public String getPath() {
        return "/jiaochen/applet/banner";
    }
}
