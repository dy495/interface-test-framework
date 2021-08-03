package com.haisheng.framework.testng.bigScreen.itemCms.common.scene;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 添加区域设备
 * 需要先把设备添加到平面中
 *
 * @author wangmin
 * @date 2021/08/03
 */
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
