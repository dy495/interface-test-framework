package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.activity;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 小程序-我的报名中取消报名
 */
@Builder
public class AppointmentActivityCancelScene extends BaseScene {
    private final Long id;

    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("id", id);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/applet/granted/appointment/activity/cancel";
    }



}
