package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.activity;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 34.23. 活动报名数据 （谢）（2020-12-23）
 *
 * @author wangmin
 * @date 2021-07-27 18:26:28
 */
@Builder
public class RegisterDataScene extends BaseScene {
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
        return "/car-platform/pc/activity/manage/register/data";
    }
}