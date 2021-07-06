package com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.applet.granted;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 3.3. 删除我的车辆 (池) v1.0
 *
 * @author wangmin
 * @date 2021-03-31 13:03:22
 */
@Builder
public class AppletCarDeleteScene extends BaseScene {
    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final Long id;

    /**
     * 描述 车牌号码
     * 是否必填 false
     * 版本 1.0
     */
    private final String plateNumber;

    /**
     * 描述 车型id
     * 是否必填 false
     * 版本 1.0
     */
    private final Long modelId;


    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("plate_number", plateNumber);
        object.put("model_id", modelId);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol-applet/granted/car/delete";
    }
}