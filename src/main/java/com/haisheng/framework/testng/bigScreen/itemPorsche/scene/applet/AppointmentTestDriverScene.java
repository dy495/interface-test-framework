package com.haisheng.framework.testng.bigScreen.itemPorsche.scene.applet;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.casedaily.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 小程序预约试驾接口
 *
 * @author wangmin
 */
@Builder
public class AppointmentTestDriverScene extends BaseScene {
    private final String customerName;
    private final String customerGender;
    private final String customerPhoneNumber;
    private final int carStyle;
    private final int carModel;
    private final String appointmentDate;

    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("customer_name", customerName);
        object.put("customer_gender", customerGender);
        object.put("customer_phone_number", customerPhoneNumber);
        object.put("car_style", carStyle);
        object.put("car_model", carModel);
        object.put("appointment_date", appointmentDate);
        return object;
    }

    @Override
    public String getPath() {
        return "/WeChat-applet/porsche/a/appointment/test-drive";
    }

    @Override
    public String getIpPort() {
        return null;
    }
}
