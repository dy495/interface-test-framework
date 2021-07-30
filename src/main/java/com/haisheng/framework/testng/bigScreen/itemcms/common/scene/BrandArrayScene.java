package com.haisheng.framework.testng.bigScreen.itemcms.common.scene;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

@Builder
public class BrandArrayScene extends BaseScene {
    private final String appId;

    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("app_id", appId);
        return object;
    }

    @Override
    public String getPath() {
        return "/admin/data/brand/array";
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    @Override
    public void setSize(Integer size) {
        this.size = size;
    }
}
