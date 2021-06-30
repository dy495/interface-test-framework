package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.mapp.task;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.casedaily.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 点接待
 *
 * @author wangmin
 * @date 2021/1/29 15:11
 */
@Builder
public class AppAppointmentReceptionScene extends BaseScene {
    private final Long id;

    @Override
    public JSONObject getRequestBody() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", id);
        return jsonObject;
    }

    @Override
    public String getPath() {
        return "/jiaochen/m-app/task/appointment/reception";
    }
}
