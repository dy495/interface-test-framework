package com.haisheng.framework.testng.bigScreen.itemcms.common.scene;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

@Builder
public class DataRegionDeviceScene extends BaseScene {
    private final Long regionId;
    private final String deviceId;

    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("device_id", deviceId);
        object.put("region_id", regionId);
        return object;
    }

    @Override
    public String getPath() {
        return "/admin/data/regionDevice/";
    }
}
