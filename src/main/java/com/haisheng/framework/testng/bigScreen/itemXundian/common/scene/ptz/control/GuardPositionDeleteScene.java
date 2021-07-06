package com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.ptz.control;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 28.5. 删除看守位
 *
 * @author wangmin
 * @date 2021-06-29 14:11:44
 */
@Builder
public class GuardPositionDeleteScene extends BaseScene {
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
     * 版本 v4.1.2
     */
    private final String terminalDeviceId;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("appId", appId);
        object.put("terminal_device_id", terminalDeviceId);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol/ptz/control/guard-position/delete";
    }
}