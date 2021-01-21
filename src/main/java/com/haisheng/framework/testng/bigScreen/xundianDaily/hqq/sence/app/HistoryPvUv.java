package com.haisheng.framework.testng.bigScreen.xundianDaily.hqq.sence.app;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 门店详情-历史客流-到店趋势图
 */
@Builder
public class HistoryPvUv extends BaseScene {
    private final String shopId;
    private final String month;
    private final String day;
    private final String cycleType;

    @Override
    public JSONObject getJSONObject() {
        JSONObject object = new JSONObject();
        object.put("shop_id", shopId);
        object.put("month", month);
        object.put("day", day);
        object.put("cycle_type", cycleType);
        return object;
    }

    @Override
    public String getPath() {
        return "/store/m-app/auth/shop/history/pv-uv";
    }
}
