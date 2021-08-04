package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.granted;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 3.1. 新增车辆 (池) v1.0
 *
 * @author wangmin
 * @date 2021-03-31 13:03:22
 */
@Builder
public class AppletCarCreateScene extends BaseScene {
    /**
     * 描述 车型id
     * 是否必填 false
     * 版本 1.0
     */
    private final Long modelId;

    /**
     * 描述 车牌号
     * 是否必填 false
     * 版本 1.0
     */
    private final String plateNumber;


    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("model_id", modelId);
        object.put("plate_number", plateNumber);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/applet/granted/car/create";
    }
}