package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.mapp.task;

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
        return "/jiaochen/m-app/task/reception/page";
    }
}
