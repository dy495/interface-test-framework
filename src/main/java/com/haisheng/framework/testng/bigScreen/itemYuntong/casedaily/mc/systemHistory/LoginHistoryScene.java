package com.haisheng.framework.testng.bigScreen.itemYuntong.casedaily.mc.systemHistory;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

@Builder
public class LoginHistoryScene extends BaseScene {
    private final int page;
    private final int size;

    @Override
    protected JSONObject getRequestBody() {
        JSONObject obj = new JSONObject();
        obj.put("page",page);
        obj.put("size",size);
        return obj;
    }

    @Override
    public String getPath() {
        return "/car-platform/pc/record/login-record/page";
    }
}
