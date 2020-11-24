package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.messagemanage;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.scene.BaseScene;
import lombok.Builder;

import java.util.List;

@Builder
public class PushMessage extends BaseScene {
    private final Integer messageType;
    private final String messageName;
    private final Long customerId;
    private final Boolean ifAllSelect;
    private final String messageContent;
    private final Integer type;
    private final List<String> voucherOrPackageList;
    private final String beginUseTime;
    private final String endUseTime;
    private final Integer useDays;
    private final Long sendTime;
    private final Boolean ifSendImmediately;
    private final Long activityId;

    @Override
    public JSONObject getJSONObject() {
        JSONObject object = new JSONObject();
        object.put("message_type", messageType);
        object.put("message_name", messageName);
        object.put("customer_id", customerId);
        object.put("if_all_select", ifAllSelect);
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
