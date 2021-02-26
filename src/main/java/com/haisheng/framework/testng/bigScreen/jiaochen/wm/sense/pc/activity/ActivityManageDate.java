package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.activity;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 活动管理-活动管理列表--gly
 */
@Builder
public class ActivityManageDate extends BaseScene {
    @Override
    public JSONObject getRequest() {
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
