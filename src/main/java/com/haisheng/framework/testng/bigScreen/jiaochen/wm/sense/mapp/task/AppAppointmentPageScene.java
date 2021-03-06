package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.mapp.task;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * @author wangmin
 * @date 2021/1/29 15:11
 */
@Builder
public class AppAppointmentPageScene extends BaseScene {
    @Builder.Default
    private final Integer size = 10;
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
        return "/car-platform/m-app/task/appointment/page";
    }
}
