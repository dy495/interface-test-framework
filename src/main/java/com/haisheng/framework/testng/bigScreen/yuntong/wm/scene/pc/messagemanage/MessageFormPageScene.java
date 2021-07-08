package com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.pc.messagemanage;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 14.2. 消息分页 （张）
 *
 * @author wangmin
 * @date 2021-05-18 17:04:35
 */
@Builder
public class MessageFormPageScene extends BaseScene {
    /**
     * 描述 页码 大于0
     * 是否必填 true
     * 版本 v1.0
     */
    private final Integer page;

    /**
     * 描述 页大小 范围为[1,100]
     * 是否必填 true
     * 版本 v1.0
     */
    private final Integer size;

    /**
     * 描述 消息发送门店id
     * 是否必填 false
     * 版本 v1.0
     */
    private final Long shopId;

    /**
     * 描述 消息类型 通过通用枚举接口获取key为MESSAGE_TYPE_LIST
     * 是否必填 false
     * 版本 v1.0
     */
    private final String messageType;

    /**
     * 描述 推送时间范围查询开始日期
     * 是否必填 false
     * 版本 v1.0
     */
    private final String startTime;

    /**
     * 描述 推送时间范围查询结束日期
     * 是否必填 false
     * 版本 v1.0
     */
    private final String endTime;

    /**
     * 描述 发送账号
     * 是否必填 false
     * 版本 v1.0
     */
    private final String sendAccount;

    /**
     * 描述 客户名称
     * 是否必填 false
     * 版本 v1.0
     */
    private final String customerName;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("page", page);
        object.put("size", size);
        object.put("shop_id", shopId);
        object.put("message_type", messageType);
        object.put("start_time", startTime);
        object.put("end_time", endTime);
        object.put("send_account", sendAccount);
        object.put("customer_name", customerName);
        return object;
    }

    @Override
    public String getPath() {
        return "/account-platform/auth/message-manage/message-form/page";
    }
}