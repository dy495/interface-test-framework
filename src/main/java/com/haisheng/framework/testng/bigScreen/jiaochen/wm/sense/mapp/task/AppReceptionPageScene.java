package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.mapp.task;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 售后接待列表
 *
 * @author wangmin
 * @date 2021/1/29 15:11
 */
@Builder
public class AppReceptionPageScene extends BaseScene {
    private final Integer size;
    private final Integer lastValue;

    @Override
    public JSONObject getRequestBody() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("size", size);
        jsonObject.put("last_value", lastValue);
        return jsonObject;
    }

    @Override
    public String getPath() {
        return "/car-platform/m-app/task/reception/page";
    }
}
