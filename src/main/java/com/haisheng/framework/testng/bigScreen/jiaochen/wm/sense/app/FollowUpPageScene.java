package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.app;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 接待列表
 *
 * @author wangmin
 * @date 2021/1/29 15:11
 */
@Builder
public class FollowUpPageScene extends BaseScene {
    private final Integer size;
    private final Integer id;
    private final Integer time;

    @Override
    public JSONObject getRequest() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("size", size);
        JSONObject lastValue = new JSONObject();
        lastValue.put("id", id);
        lastValue.put("time", time);
        jsonObject.put("last_value", lastValue);
        return jsonObject;
    }

    @Override
    public String getPath() {
        return "/jiaochen/m-app/follow-up/page";
    }
}
