package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.granted;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * @author wangmin
 * @date 2021/1/29 11:46
 */
@Builder
public class AppointmentTimeListScene extends BaseScene {
    private final String type;
    private final Integer shopId;
    private final Integer carId;
    private final String day;

    @Override
    public JSONObject getJSONObject() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", type);
        jsonObject.put("shop_id", shopId);
        jsonObject.put("car_id", carId);
        jsonObject.put("day", day);
        return jsonObject;
    }

    @Override
    public String getPath() {
        return "/jiaochen/applet/granted/appointment/time/list";
    }
}
