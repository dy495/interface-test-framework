package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.messagemanage;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.scene.BaseScene;
import lombok.Builder;

import java.util.List;

/**
 * 消息管理 -> 推送消息
 */
@Builder
public class PushMessage extends BaseScene {
    private final String pushTarget;
    private final List<Long> shopList;
    private final List<String> telList;
    private final String  messageName;
    private final String messageContent;
    private final Integer type;
    private final List<Long> voucherOrPackageList;
    private final String beginUseTime;
    private final String endUseTime;
    private final Integer useDays;
    private final Long sendTime;
    private final Boolean ifSendImmediately;
    private final Long activityId;

    @Override
    public JSONObject getJSONObject() {
        JSONObject object = new JSONObject();
        object.put("push_target", pushTarget);
        object.put("shop_list", shopList);
        object.put("tel_list", telList);
        object.put("message_name", messageName);
        object.put("message_content", messageContent);
        object.put("type", type);
        object.put("voucher_or_package_list", voucherOrPackageList);
        object.put("begin_use_time", beginUseTime);
        object.put("end_use_time", endUseTime);
        object.put("use_days", useDays);
        object.put("send_time", sendTime);
        object.put("if_send_immediately", ifSendImmediately);
        object.put("activity_id", activityId);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/message-manage/push-message";
    }

    @Override
    public String getIpPort() {
        return null;
    }
}
