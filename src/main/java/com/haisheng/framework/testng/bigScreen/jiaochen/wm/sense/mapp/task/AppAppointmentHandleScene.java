package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.mapp.task;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 确认预约
 *
 * @author wangmin
 * @date 2021/1/29 15:11
 */
@Builder
public class AppAppointmentHandleScene extends BaseScene {
    private final Long id;
    private final Long shopId;
    private final Integer type;

    @Override
    public JSONObject getRequestBody() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", id);
        jsonObject.put("shop_id", shopId);
        jsonObject.put("type", type);
        return jsonObject;
    }

    @Override
    public String getPath() {
        return "/jiaochen/m-app/task/appointment/handle";
    }
}
