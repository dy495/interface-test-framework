package com.haisheng.framework.testng.bigScreen.crm.wm.scene.app;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.scene.BaseScene;
import lombok.Builder;

@Builder
public class ReturnVisitTaskPageScene extends BaseScene {
    private final String belongSaleId;
    @Builder.Default
    private final Integer page = 1;
    @Builder.Default
    private final Integer size = 100;

    @Override
    public JSONObject getJSONObject() {
        JSONObject object = new JSONObject();
        object.put("page", page);
        object.put("size", size);
        object.put("belong_sale_id", belongSaleId);
        return object;
    }

    @Override
    public String getPath() {
        return "/porsche/app/return-visit-task/page";
    }
}