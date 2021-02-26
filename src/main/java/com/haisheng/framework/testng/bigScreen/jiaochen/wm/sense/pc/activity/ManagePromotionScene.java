package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.activity;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 活动管理-推广活动
 */
@Builder
public class ManagePromotionScene extends BaseScene {
    private final Long  id;
    @Override
    public JSONObject getRequest() {
        JSONObject object = new JSONObject();
        object.put("id",id );
        return object;
    }
    @Override
    public String getPath() {
        return "/jiaochen/pc/activity/manage/promotion";
    }

    @Override
    public String getIpPort() {
        return null;
    }
}
