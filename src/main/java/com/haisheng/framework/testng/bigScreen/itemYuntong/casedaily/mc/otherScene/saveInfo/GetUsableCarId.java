package com.haisheng.framework.testng.bigScreen.itemYuntong.casedaily.mc.otherScene.saveInfo;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

@Builder
public class GetUsableCarId extends BaseScene {
    @Override
    protected JSONObject getRequestBody() {
        JSONObject obj = new JSONObject();
        return obj;
    }

    @Override
    public String getPath() {
        return "/car-platform/m-app/retention/plate-number-list";
    }
}
