package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.activity;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 活动管理-删除活动
 */
@Builder
public class DeleteScene extends BaseScene {
    private final Long  id;
    @Override
    public JSONObject getJSONObject() {
        JSONObject object = new JSONObject();
        object.put("id",id );
        return object;
    }
    @Override
    public String getPath() {
        return "/jiaochen/pc/activity/manage/delete";
    }

    @Override
    public String getIpPort() {
        return null;
    }
}
