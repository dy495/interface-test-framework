package com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.pc.activity;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.casedaily.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 29.20. 活动审批（2020-12-23）
 *
 * @author wangmin
 * @date 2021-05-18 17:04:36
 */
@Builder
public class AppManageApprovalScene extends BaseScene {
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
        return "/yt/pc/activity/manage/approval";
    }
}