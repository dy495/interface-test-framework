package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.granted;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 5.5. 获取门店id（池）(下拉)（2021-03-23）
 *
 * @author wangmin
 * @date 2021-03-31 13:03:22
 */
@Builder
public class AppletCommonShopListScene extends BaseScene {
    /**
     * 描述 车型id
     * 是否必填 false
     * 版本 v3.0
     */
    private final Long carModelId;
    private final JSONArray coordinate;

    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("car_model_id", carModelId);
        object.put("coordinate", coordinate);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/applet/granted/common/shop-list";
    }
}