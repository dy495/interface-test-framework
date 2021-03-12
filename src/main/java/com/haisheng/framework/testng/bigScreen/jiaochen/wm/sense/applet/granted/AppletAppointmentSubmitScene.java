package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.granted;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * /jiaochen/applet/granted/appointment/submit的接口
 *
 * @author wangmin
 * @date 2021-03-12 17:53:03
 */
@Builder
public class AppletAppointmentSubmitScene extends BaseScene {
    /**
     * 描述 预约类型 MAINTAIN：保养，REPAIR：维修
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
     * 描述 预约车辆id
     * 是否必填 true
     * 版本 v1.0
     */
    private final Long carId;

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
     * 是否必填 true
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
    public JSONObject getRequestBody(){
        JSONObject object = new JSONObject();
        object.put("type", type);
        object.put("shop_id", shopId);
        object.put("staff_id", staffId);
        object.put("time_id", timeId);
        object.put("car_id", carId);
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