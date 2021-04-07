package com.haisheng.framework.testng.bigScreen.xundianDaily.wm.scene.pc.vouchermanage;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 领取记录
 */
@Builder
public class SendRecordScene extends BaseScene {
    private final String voucherName;
    private final String sender;
    @Builder.Default
    private Integer page = 1;
    @Builder.Default
    private Integer size = 10;

    /**
     * 2.0
     */
    private final Long voucherId;
    private final String receiver;
    private final String receivePhone;
    private final String useStatus;
    private final Long startTime;
    private final Long endTime;
    private final String useStartTime;
    private final String useEndTime;
    private final String customerLabel;

    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("voucher_name", voucherName);
        object.put("sender", sender);
        object.put("page", page);
        object.put("size", size);
        object.put("voucher_id", voucherId);
        object.put("receiver", receiver);
        object.put("receive_phone", receivePhone);
        object.put("use_status", useStatus);
        object.put("start_time", startTime);
        object.put("end_time", endTime);
        object.put("use_start_time", useStartTime);
        object.put("use-end-time", useEndTime);
        object.put("customer_label", customerLabel);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol/pc/voucher-manage/send-record";
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
