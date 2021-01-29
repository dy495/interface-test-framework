package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.activity;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

import java.util.List;

/**
 * 活动管理-活动管理列表
 */
@Builder
public class ActivityManageDate extends BaseScene {
    @Override
    public JSONObject getJSONObject() {
        JSONObject object = new JSONObject();

        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/activity/manage/data";
    }

    @Override
    public String getIpPort() {
        return null;
    }
}
