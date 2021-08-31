package com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.mapp;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

@Builder
public class AppPatrolDeviceLiveScene extends BaseScene {
    private final String deviceId;
    private final Integer shopId;

    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("device_id", deviceId);
        object.put("shop_id", shopId);
        return object;
    }

    @Override
    public String getPath() {
        return "/store/m-app/auth/patrol/device-live";
    }
}
