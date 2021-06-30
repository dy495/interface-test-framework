package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.activity;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.base.scene.BaseScene;
import lombok.Builder;

import java.util.List;

/**
 * 活动管理-活动审批
 */
@Builder
public class ManageApprovalScene extends BaseScene {
    private final Integer status;
    private final List<Long> ids;

    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("status", status);
        object.put("ids", ids);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/activity/manage/approval";
    }
}
