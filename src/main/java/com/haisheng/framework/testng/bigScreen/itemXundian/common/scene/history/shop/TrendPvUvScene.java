package com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.history.shop;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 门店详情-历史客流-到店趋势图
 */
@Builder
public class TrendPvUvScene extends BaseScene {
    private final String shopId;
    private final String month;
    private final String day;
    private final String cycleType;

    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("shop_id", shopId);
        object.put("month", month);
        object.put("day", day);
        object.put("cycle_type", cycleType);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol/history/shop/day/trend-pv-uv";
    }
}
