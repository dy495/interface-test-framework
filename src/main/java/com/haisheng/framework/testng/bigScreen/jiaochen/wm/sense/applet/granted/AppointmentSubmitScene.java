package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.granted;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * @author wangmin
 * @date 2021/1/29 11:21
 */
@Builder
public class AppointmentSubmitScene extends BaseScene {
    private final String type;
    private final Integer shopId;
    private final String staffId;
    private final Integer timeId;
    private final Integer carId;
    private final String appointmentName;
    private final String appointmentPhone;
    private final String faultDescription;
    private final Integer informationId;

    @Override
    public JSONObject getRequest() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", type);
        jsonObject.put("shop_id", shopId);
        jsonObject.put("staff_id", staffId);
        jsonObject.put("time_id", timeId);
        jsonObject.put("car_id", carId);
        jsonObject.put("appointment_name", appointmentName);
        jsonObject.put("appointment_phone", appointmentPhone);
        jsonObject.put("fault_description", faultDescription);
        jsonObject.put("information_id", informationId);
        return jsonObject;
    }

    @Override
    public String getPath() {
        return "/jiaochen/applet/granted/appointment/submit";
    }
}
