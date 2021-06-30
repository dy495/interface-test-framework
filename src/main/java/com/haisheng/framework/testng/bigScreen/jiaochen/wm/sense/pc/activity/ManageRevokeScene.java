package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.activity;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.casedaily.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 活动管理-取消活动
 */
@Builder
public class ManageRevokeScene extends BaseScene {
    private final Long id;

    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("id", id);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/activity/manage/revoke";
    }


}
