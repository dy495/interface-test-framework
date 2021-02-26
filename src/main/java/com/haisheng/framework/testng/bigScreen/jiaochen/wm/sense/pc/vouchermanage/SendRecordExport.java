package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.vouchermanage;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

import java.util.List;

@Builder
public class SendRecordExport extends BaseScene {
    private final Long id;
    private final String receiver;
    private final String receivePhone;
    private final String useStatus;
    private final String useStartTime;
    private final String useEndTime;
    private final String customerLabel;
    private final String voucherName;
    private final String sender;
    private final String startTime;
    private final String endTime;
    private final String exportType;
    private final List<Long> ids;
    @Builder.Default
    private Integer page = 1;
    @Builder.Default
    private Integer size = 10;

    @Override
    public JSONObject getRequest() {
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("receiver", receiver);
        object.put("receive_phone", receivePhone);
        object.put("use_status", useStatus);
        object.put("use_start_time", useStartTime);
        object.put("use-end-time", useEndTime);
        object.put("customer_label", customerLabel);
        object.put("voucher_name", voucherName);
        object.put("sender", sender);
        object.put("start_time", startTime);
        object.put("end_time", endTime);
        object.put("export_type", exportType);
        object.put("ids", ids);
        object.put("page", page);
        object.put("size", size);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/voucher-manage/voucher-form/page";
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
