package com.haisheng.framework.testng.bigScreen.fengkongdaily.scene;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.base.scene.BaseScene;
import lombok.Builder;

@Builder
public class BusinessTypeList extends BaseScene {
    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();

        return object;
    }

    @Override
    public String getPath() {
        return "/business/risk/BUSINESS_TYPE_LIST/v1.0";
    }
}
