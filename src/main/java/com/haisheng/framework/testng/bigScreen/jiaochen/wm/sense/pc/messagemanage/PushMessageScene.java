package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.messagemanage;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

import java.util.List;

/**
 * 消息管理 -> 推送消息
 */
@Builder
public class PushMessageScene extends BaseScene {
    private final Integer pushTarget;
    private final List<Long> shopList;
    private final List<String> telList;
    private final String messageName;
    private final String messageContent;
    private final Integer type;
    private final List<Long> voucherOrPackageList;
    private final String beginUseTime;
    private final String endUseTime;
    private final String useDays;
    private final Long sendTime;
    @Builder.Default
    private final Boolean ifSendImmediately = true;
    private final Long activityId;
    private final List<Long> customerIdList;
    private final Integer useTimeType;
    private final JSONObject searchCustomerInfo;

    @Override
    public JSONObject getRequestBody() {
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
        object.put("customer_id_list", customerIdList);
        object.put("use_time_type", useTimeType);
        object.put("search_customer_info", searchCustomerInfo);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/pc/message-manage/push-message";
    }
}
