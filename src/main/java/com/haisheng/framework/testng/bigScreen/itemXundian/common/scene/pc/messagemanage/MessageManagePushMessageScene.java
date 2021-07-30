package com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.pc.messagemanage;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 45.1. 消息推送
 *
 * @author wangmin
 * @date 2021-07-30 10:55:36
 */
@Builder
public class MessageManagePushMessageScene extends BaseScene {
    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final String referer;

    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final String appId;

    /**
     * 描述 推送目标 0：全部客户，1：门店客户，2：个人客户
     * 是否必填 true
     * 版本 v2.0
     */
    private final Integer pushTarget;

    /**
     * 描述 门店列表 推送目标为门店客户时必填
     * 是否必填 false
     * 版本 v1.0
     */
    private final JSONArray shopList;

    /**
     * 描述 客户手机号列表 推送目标个人客户时时必填
     * 是否必填 false
     * 版本 v1.0
     */
    private final JSONArray telList;

    /**
     * 描述 消息名称
     * 是否必填 true
     * 版本 v1.0
     */
    private final String messageName;

    /**
     * 描述 消息内容
     * 是否必填 true
     * 版本 v1.0
     */
    private final String messageContent;

    /**
     * 描述 推送优惠类型 0：卡券，1：套餐
     * 是否必填 false
     * 版本 v1.0
     */
    private final Integer type;

    /**
     * 描述 推送卡券或套餐 选择对应推送优惠类型时必填
     * 是否必填 false
     * 版本 v1.0
     */
    private final JSONArray voucherOrPackageList;

    /**
     * 描述 卡券或套餐可用开始日期 选择对应推送优惠类型时 可用日期范围和可用有效天数二选一
     * 是否必填 false
     * 版本 v1.0
     */
    private final String beginUseTime;

    /**
     * 描述 卡券或套餐可用结束日期 选择对应推送优惠类型时 可用日期范围和可用有效天数二选一
     * 是否必填 false
     * 版本 v1.0
     */
    private final String endUseTime;

    /**
     * 描述 卡券或套餐领用可用有效天数 选择对应推送优惠类型时 可用有效天数和可用日期范围二选一
     * 是否必填 false
     * 版本 v1.0
     */
    private final Integer useDays;

    /**
     * 描述 是否立即发送
     * 是否必填 true
     * 版本 v1.0
     */
    private final Boolean ifSendImmediately;

    /**
     * 描述 定时发送时间 选择非立即发送时必填
     * 是否必填 false
     * 版本 v1.0
     */
    private final Long sendTime;

    /**
     * 描述 跳转活动id
     * 是否必填 false
     * 版本 v1.0
     */
    private final Long activityId;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("appId", appId);
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
        object.put("if_send_immediately", ifSendImmediately);
        object.put("send_time", sendTime);
        object.put("activity_id", activityId);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol/pc/message-manage/push-message";
    }
}