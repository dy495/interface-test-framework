package com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.mapp;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 门店详情-实时客流-趋势图
 */
@Builder
public class AppRealHourPvUvScene extends BaseScene {
    private final String shopId;

    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("shop_id", shopId);
        return object;
    }

    @Override
    public String getPath() {
        return "/store/m-app/auth/shop/real-hour/pv-uv";
    }
}
