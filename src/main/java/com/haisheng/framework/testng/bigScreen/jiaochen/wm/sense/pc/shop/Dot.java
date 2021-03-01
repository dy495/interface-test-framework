package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.shop;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 新建门店
 */
@Builder
public class Dot extends BaseScene {
    private final String terminalDeviceId;
    private final String command;
    private final Integer keepTime;
    private final String appId;
    private final String service;
    private final String source;

    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        JSONObject data = new JSONObject();
        data.put("terminal_device_id", terminalDeviceId);
        data.put("command", command);
        data.put("keep_time", keepTime);
        JSONObject system = new JSONObject();
        system.put("app_id", appId);
        system.put("service", service);
        system.put("source", source);
        object.put("data", data);
        object.put("system", system);
        return object;
    }

    @Override
    public String getPath() {
        return "/os/device/REQUEST_TERMINAL_DEVICE_SERVICE/v1.0";
    }
}
