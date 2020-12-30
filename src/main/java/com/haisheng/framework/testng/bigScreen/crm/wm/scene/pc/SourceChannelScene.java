package com.haisheng.framework.testng.bigScreen.crm.wm.scene.pc;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.scene.BaseScene;
import lombok.Builder;

@Builder
public class SourceChannelScene extends BaseScene {
    private final String cycleType;
    private final String month;
    private final String day;
    private final String saleId;

    @Override
    public JSONObject getJSONObject() {
        JSONObject object = new JSONObject();
        object.put("cycle_type", cycleType);
        object.put("month", month);
        object.put("day", day);
        object.put("sale_id", saleId);
        return object;
    }

    @Override
    public String getPath() {
        return "/porsche/analysis2/shop/source-channel";
    }
}
