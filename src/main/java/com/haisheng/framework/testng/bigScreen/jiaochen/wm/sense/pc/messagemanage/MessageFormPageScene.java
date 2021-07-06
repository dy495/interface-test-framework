package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.messagemanage;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 消息管理 -> 消息表单
 */
@Builder
public class MessageFormPageScene extends BaseScene {
    private final String messageType;
    private final String sendAccount;
    private final String startTime;
    private final String endTime;
    private final String customerName;
    private final Long shopId;
    @Builder.Default
    private Integer page = 1;
    @Builder.Default
    private Integer size = 10;

    @Override
    public JSONObject getRequestBody() {
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
    public void setPage(Integer page) {
        this.page = page;
    }

    @Override
    public void setSize(Integer size) {
        this.size = size;
    }
}
