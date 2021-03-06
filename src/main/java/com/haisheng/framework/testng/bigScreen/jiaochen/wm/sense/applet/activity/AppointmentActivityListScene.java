package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.activity;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 小程序-我的报名中列表
 */
@Builder
public class AppointmentActivityListScene extends BaseScene {
    private final int size;
    private final Integer lastValue;

    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("size", size);
        object.put("last_value",lastValue);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/applet/granted/appointment/activity/list";
    }


}
