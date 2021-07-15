package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.app.homepagev4;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 2.2. 今日任务
 *
 * @author wangmin
 * @date 2021-05-07 19:22:48
 */
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