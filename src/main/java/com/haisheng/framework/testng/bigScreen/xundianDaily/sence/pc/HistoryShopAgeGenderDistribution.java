package com.haisheng.framework.testng.bigScreen.xundianDaily.sence.pc;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.scene.BaseScene;
import lombok.Builder;

@Builder
public class HistoryShopAgeGenderDistribution extends BaseScene {
    private final String shopId;
    private final String cycleType;

    @Override
    public JSONObject getJSONObject() {
        JSONObject object = new JSONObject();
        object.put("shop_id", shopId);
        object.put("cycle_type", cycleType);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol/history/shop/age-gender/distribution";
    }
}
