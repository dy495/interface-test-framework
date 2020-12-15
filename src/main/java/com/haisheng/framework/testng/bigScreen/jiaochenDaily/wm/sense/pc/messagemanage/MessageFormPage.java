package com.haisheng.framework.testng.bigScreen.jiaochenDaily.wm.sense.pc.messagemanage;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crmDaily.wm.scene.BaseScene;
import lombok.Builder;

/**
 * 消息管理 -> 消息表单
 */
@Builder
public class MessageFormPage extends BaseScene {
    private final String messageType;
    private final String sendAccount;
    private final Long startTime;
    private final Long endTime;
    private final String customerName;
    private final Long shopId;
    private final Integer page;
    private final Integer size;

    @Override
    public JSONObject getJSONObject() {
        JSONObject object = new JSONObject();
        object.put("message_type", messageType);
        object.put("send_account", sendAccount);
        object.put("start_time", startTime);
        object.put("end_time", endTime);
        object.put("customer_name", customerName);
        object.put("shop_id", shopId);
        object.put("page", page);
        object.put("size", size);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/message-manage/message-form/page";
    }

    @Override
    public String getIpPort() {
        return null;
    }
}
