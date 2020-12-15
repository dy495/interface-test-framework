package com.haisheng.framework.testng.bigScreen.crm.wm.scene.pc;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.scene.BaseScene;
import lombok.Builder;

/**
 * 发送站内消息接口
 *
 * @author wangmin
 */
@Builder
public class MessageAddScene extends BaseScene {

    private final String title;
    private final String content;
    private final String sendTime;
    private final String[] customerTypes;
    private final int[] carTypes;
    private final int[] customerLevel;
    private final String[] customerProperty;
    private final String appointmentType;
    private final Integer activityId;

    @Override
    public JSONObject getJSONObject() {
        JSONObject object = new JSONObject();
        object.put("title", title);
        object.put("content", content);
        object.put("send_time", sendTime);
        object.put("customer_types", customerTypes);
        object.put("car_types", carTypes);
        object.put("customer_level", customerLevel);
        object.put("customer_property", customerProperty);
        object.put("appointment_type", appointmentType);
        object.put("activity_id", activityId);
        return object;
    }

    @Override
    public String getPath() {
        return "/porsche/message/add";
    }

    @Override
    public String getIpPort() {
        return null;
    }
}
