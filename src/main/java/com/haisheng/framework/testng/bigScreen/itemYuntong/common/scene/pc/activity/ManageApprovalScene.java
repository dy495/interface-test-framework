package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.activity;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 34.20. 活动审批（2020-12-23）
 *
 * @author wangmin
 * @date 2021-07-27 18:26:28
 */
@Builder
public class ManageApprovalScene extends BaseScene {
    /**
     * 描述 审批状态 枚举见字典表《活动审批状态》
     * 是否必填 true
     * 版本 v2.0
     */
    private final Integer status;

    /**
     * 描述 活动id列表
     * 是否必填 true
     * 版本 v2.0
     */
    private final JSONArray ids;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("status", status);
        object.put("ids", ids);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/pc/activity/manage/approval";
    }
}