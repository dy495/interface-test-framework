package com.haisheng.framework.testng.bigScreen.itemPorsche.common.scene.applet;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 小程序预约保养接口
 *
 * @author wangmin
 */
@Builder
public class AppointmentRepairScene extends BaseScene {
    private final String myCarId;
    private final String customerName;
    private final String customerGender;
    private final String customerPhoneNumber;
    private final int timeRangeId;


    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("my_car_id", myCarId);
        object.put("customer_name", customerName);
        object.put("customer_gender", customerGender);
        object.put("customer_phone_number", customerPhoneNumber);
        object.put("time_range_id", timeRangeId);
        return object;
    }

    @Override
    public String getPath() {
        return "/WeChat-applet/porsche/a/appointment/repair";
    }


}
