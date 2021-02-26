package com.haisheng.framework.testng.bigScreen.xundianDaily.hqq.sence.pc;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

@Builder
public class HistoryShopConversion extends BaseScene {
    private final String shopId;
    private final String cycleType;
    private final String month;

    @Override
    public JSONObject getRequest() {
        JSONObject object = new JSONObject();
        object.put("shop_id", shopId);
        object.put("cycle_type", cycleType);
        object.put("month", month);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol/history/shop/conversion";
    }
}
