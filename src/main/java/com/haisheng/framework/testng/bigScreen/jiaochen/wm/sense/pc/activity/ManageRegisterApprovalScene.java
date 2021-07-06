package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.activity;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

import java.util.List;

/**
 * 活动管理-活动报名审批
 */
@Builder
public class ManageRegisterApprovalScene extends BaseScene {
    private final Long activityId;
    private final Integer status;
    private final List<Long> ids;

    @Override
    public JSONObject getRequestBody() {
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


}
