package com.haisheng.framework.testng.bigScreen.xundian.casedaily.hqq.sence.app;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 门店详情-实时客流-总数
 */
@Builder
public class RealHourTotal extends BaseScene {
    private final String shopId;

    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("shop_id", shopId);
        return object;
    }

    @Override
    public String getPath() {
        return "/store/m-app/auth/shop/real-hour/total";
    }
}
