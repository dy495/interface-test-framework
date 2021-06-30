package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.granted;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.base.scene.BaseScene;
import lombok.Builder;

/**
 * 7.4. 提交预约 （谢）v3.0（2021-03-29）
 *
 * @author wangmin
 * @date 2021-03-31 13:03:22
 */
@Builder
public class AppletAppointmentSubmitScene extends BaseScene {
    /**
     * 描述 预约类型 详见字典表《预约类型》v3.0（2021-03-12）
     * 是否必填 true
     * 版本 v2.0
     */
    private final String type;

    /**
     * 描述 门店id
     * 是否必填 true
     * 版本 v1.0
     */
    private final Long shopId;

    /**
     * 描述 服务顾问id
     * 是否必填 true
     * 版本 v1.0
     */
    private final String staffId;

    /**
     * 描述 预约时间段id
     * 是否必填 true
     * 版本 v1.0
     */
    private final Long timeId;

    /**
     * 描述 预约车辆id type为保养维修时不能为空
     * 是否必填 false
     * 版本 v1.0
     */
    private final Long carId;

    /**
     * 描述 预约车系id type为试驾时不能为空
     * 是否必填 false
     * 版本 v3.0
     */
    private final Long carStyleId;

    /**
     * 描述 预约人姓名
     * 是否必填 true
     * 版本 v1.0
     */
    private final String appointmentName;

    /**
     * 描述 预约人电话 不填默认为绑定电话
     * 是否必填 true
     * 版本 v1.0
     */
    private final String appointmentPhone;

    /**
     * 描述 故障描述
     * 是否必填 false
     * 版本 v2.0
     */
    private final String faultDescription;

    /**
     * 描述 信息id businessType 为 08,09 时传入
     * 是否必填 false
     * 版本 -
     */
    private final Long informationId;


    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("type", type);
        object.put("shop_id", shopId);
        object.put("staff_id", staffId);
        object.put("time_id", timeId);
        object.put("car_id", carId);
        object.put("car_style_id", carStyleId);
        object.put("appointment_name", appointmentName);
        object.put("appointment_phone", appointmentPhone);
        object.put("fault_description", faultDescription);
        object.put("information_id", informationId);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/applet/granted/appointment/submit";
    }
}