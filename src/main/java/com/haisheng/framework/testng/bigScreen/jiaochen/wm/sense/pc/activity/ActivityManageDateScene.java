package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.activity;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 活动管理-活动管理列表--gly
 */
@Builder
public class ActivityManageDateScene extends BaseScene {
    @Override
    public JSONObject getRequestBody() {
        return new JSONObject();
    }

    @Override
    public String getPath() {
        return "/car-platform/pc/activity/manage/data";
    }
}
