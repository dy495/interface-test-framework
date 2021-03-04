package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.activity;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 小程序-我的报名中列表
 */
@Builder
public class AppointmentActivityListScene extends BaseScene {
    private final int size;
    private final JSONObject lastValue;

    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("size", size);
        object.put("last_value", lastValue);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/applet/granted/appointment/activity/list";
    }

    @Override
    public String getIpPort() {
        return null;
    }
}
