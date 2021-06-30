package com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.pc.activity;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.casedaily.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 29.23. 活动报名数据 （谢）（2020-12-23）
 *
 * @author wangmin
 * @date 2021-05-18 17:04:36
 */
@Builder
public class ManageRegisterDataScene extends BaseScene {
    /**
     * 描述 活动id
     * 是否必填 true
     * 版本 v2.0
     */
    private final Long activityId;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("activity_id", activityId);
        return object;
    }

    @Override
    public String getPath() {
        return "/yt/pc/activity/manage/register/data";
    }
}