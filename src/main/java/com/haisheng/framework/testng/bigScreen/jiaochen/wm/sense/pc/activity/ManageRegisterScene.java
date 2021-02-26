package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.activity;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 活动管理-活动报名列表
 */
@Builder
public class ManageRegisterScene extends BaseScene {
    private final Integer status;
    private final Integer page;
    private final Integer size;
    private final Long activityId;

    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("page", page);
        object.put("size", size);
        object.put("status", status);
        object.put("activity_id", activityId);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/activity/manage/register/page";
    }

    @Override
    public String getIpPort() {
        return null;
    }
}
