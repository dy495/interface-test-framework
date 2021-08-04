package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.granted;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 我的预约分页
 *
 * @author 王敏
 * @date 2021-02-01
 */
@Builder
public class AppointmentListScene extends BaseScene {
    private final Integer size;
    private final Integer lastValue;

    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("size", size);
        object.put("last_value", lastValue);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/applet/granted/appointment/list";
    }
}
