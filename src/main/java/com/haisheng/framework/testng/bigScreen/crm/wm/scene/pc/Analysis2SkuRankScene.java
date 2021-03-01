package com.haisheng.framework.testng.bigScreen.crm.wm.scene.pc;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * @author wangmin
 */
@Builder
public class Analysis2SkuRankScene extends BaseScene {
    private final String month;
    private final String cycleType;

    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("cycle_type", cycleType);
        object.put("month", month);
        return object;
    }

    @Override
    public String getPath() {
        return "/porsche/analysis2/sku/rank";
    }

    @Override
    public String getIpPort() {
        return null;
    }
}
