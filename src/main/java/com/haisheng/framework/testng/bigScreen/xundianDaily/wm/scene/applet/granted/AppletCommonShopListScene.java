package com.haisheng.framework.testng.bigScreen.xundianDaily.wm.scene.applet.granted;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
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


    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("car_model_id", carModelId);
        return object;
    }

    @Override
    public String getPath() {
        return "/shop/applet/granted/common/shop-list";
    }
}