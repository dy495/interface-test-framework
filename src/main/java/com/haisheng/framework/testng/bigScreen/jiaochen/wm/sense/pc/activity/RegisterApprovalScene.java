package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.activity;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 活动管理-活动报名审批
 */
@Builder
public class RegisterApprovalScene extends BaseScene {
    private final Integer activityId;
    private final Integer  status;
    private final JSONArray ids;

    @Override
    public JSONObject getJSONObject() {
        JSONObject object = new JSONObject();
        object.put("activity_id", activityId);
        object.put("status", status);
        object.put("ids", ids);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/activity/manage/register/approval";
    }

    @Override
    public String getIpPort() {
        return null;
    }
}