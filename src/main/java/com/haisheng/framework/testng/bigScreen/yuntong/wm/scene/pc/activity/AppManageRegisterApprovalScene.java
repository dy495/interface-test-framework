package com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.pc.activity;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 29.25. 活动报名审批（2020-12-23）
 *
 * @author wangmin
 * @date 2021-05-18 17:04:36
 */
@Builder
public class AppManageRegisterApprovalScene extends BaseScene {
    /**
     * 描述 活动id
     * 是否必填 true
     * 版本 v2.0
     */
    private final Long activityId;

    /**
     * 描述 审批状态 101：通过，201：拒绝
     * 是否必填 true
     * 版本 v2.0
     */
    private final Integer status;

    /**
     * 描述 报名id列表
     * 是否必填 true
     * 版本 v2.0
     */
    private final JSONArray ids;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("activity_id", activityId);
        object.put("status", status);
        object.put("ids", ids);
        return object;
    }

    @Override
    public String getPath() {
        return "/account-platform/auth/activity/manage/register/approval";
    }
}