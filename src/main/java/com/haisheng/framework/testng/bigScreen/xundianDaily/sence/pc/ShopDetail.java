package com.haisheng.framework.testng.bigScreen.xundianDaily.sence.pc;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.scene.BaseScene;
import lombok.Builder;

import java.util.List;

@Builder
public class ShopDetail extends BaseScene {
    private final String shopId;

    @Override

    public JSONObject getJSONObject() {
        JSONObject object = new JSONObject();
        object.put("shop_id", shopId);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol/shop/detail";
    }
}
