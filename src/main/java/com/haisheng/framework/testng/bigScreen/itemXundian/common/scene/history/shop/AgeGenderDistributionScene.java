package com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.history.shop;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

@Builder
public class AgeGenderDistributionScene extends BaseScene {
    private final String shopId;
    private final String cycleType;
    private final String month;

    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("shop_id", shopId);
        object.put("cycle_type", cycleType);
        object.put("month", month);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol/history/shop/age-gender/distribution";
    }
}
