package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.mapp.homepagev4;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

@Builder
public class AppTodayTaskScene extends BaseScene {
    @Override
    protected JSONObject getRequestBody() {
        return new JSONObject();
    }

    @Override
    public String getPath() {
        return "/car-platform/m-app/home-page-v4/today-task";
    }
}
