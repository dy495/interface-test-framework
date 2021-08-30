package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.activity;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 30.23. 活动报名数据 （谢）（2020-12-23）
 *
 * @author wangmin
 * @date 2021-08-30 14:26:55
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