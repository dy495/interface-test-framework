package com.haisheng.framework.testng.bigScreen.itemCms.common.scene;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 添加楼层设备
 *
 * @author wangmin
 * @date 2021-07-29
 */
@Builder
public class DataLayoutDeviceScene extends BaseScene {
    private final Long layoutId;
    private final String deviceId;

    @Override
    protected JSONObject getRequestBody() {
        return new JSONObject();
    }

    @Override
    public String getPath() {
        return "/admin/data/layoutDevice/" + deviceId + "/" + layoutId + "";
    }
}
