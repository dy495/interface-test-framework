package com.haisheng.framework.testng.bigScreen.xundianDaily.wm.scene.applet.granted;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 我的套餐列表
 */
@Builder
public class AppletPackageListScene extends BaseScene {
    private final Long lastValue;
    private final String type;
    private final Integer size;

    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("last_value", lastValue);
        object.put("type", type);
        object.put("size", size);
        return object;
    }

    @Override
    public String getPath() {
        return "/shop/applet/granted/package/list";
    }
}