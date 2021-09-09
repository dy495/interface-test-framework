package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.mapp.homepagev4;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

@Builder
public class AppTodayDataScene extends BaseScene {
    private final String type;
    private final Integer size;

    @Override
    protected JSONObject getRequestBody() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", type);
        jsonObject.put("size", size);
        return jsonObject;
    }

    @Override
    public String getPath() {
        return "/car-platform/m-app/home-page-v4/today-data";
    }
}
