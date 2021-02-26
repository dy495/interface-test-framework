package com.haisheng.framework.testng.bigScreen.xundianDaily.hqq.sence.pc;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 *
 */
@Builder
public class RealTimeShopPvUv extends BaseScene {
    private final String shopId;

    @Override
    public JSONObject getRequest() {
        JSONObject object = new JSONObject();
        object.put("shop_id", shopId);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol/real-time/shop/pv-uv";
    }
}
