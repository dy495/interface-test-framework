package com.haisheng.framework.testng.bigScreen.itemPorsche.scene.pc;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.casedaily.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 交车车主类型占比接口
 *
 * @author wangmin
 */
@Builder
public class Analysis2PotentialTrendScene extends BaseScene {

    @Builder.Default
    private final String dimension = "RECETION_CITY";
    private final String cycleType;
    private final String saleId;

    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("cycle_type", cycleType);
        object.put("dimension", dimension);
        object.put("sale_id", saleId);
        return object;
    }

    @Override
    public String getPath() {
        return "/porsche/analysis2/potential/trend";
    }

    @Override
    public String getIpPort() {
        return null;
    }
}
