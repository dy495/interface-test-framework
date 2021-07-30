package com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.shop.device;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 22.8. 云台控制
 *
 * @author wangmin
 * @date 2021-07-30 10:55:36
 */
@Builder
public class DeviceControlScene extends BaseScene {
    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final String referer;

    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final String appId;

    /**
     * 描述 设备id
     * 是否必填 true
     * 版本 -
     */
    private final Long deviceId;

    /**
     * 描述 操作命令 0-上，1-下，2-左，3-右，8-放大，9-缩小
     * 是否必填 true
     * 版本 -
     */
    private final Integer command;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("appId", appId);
        object.put("device_id", deviceId);
        object.put("command", command);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol/shop/device/control";
    }
}