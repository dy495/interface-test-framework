package com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.pc.vouchermanage;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 作废记录
 */
@Builder
public class VoucherInvalidPageScene extends BaseScene {
    @Builder.Default
    private Integer size = 10;
    @Builder.Default
    private Integer page = 1;
    private final Long id;
    private final String receiver;
    private final String receivePhone;
    private final String startTime;
    private final String endTime;
    private final String invalidName;
    private final String invalidPhone;
    private final String invalidStartTime;
    private final String invalidEndTime;

    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("size", size);
        object.put("page", page);
        object.put("id", id);
        object.put("receiver", receiver);
        object.put("receive_phone", receivePhone);
        object.put("start_time", startTime);
        object.put("end_time", endTime);
        object.put("invalidName", invalidName);
        object.put("invalidPhone", invalidPhone);
        object.put("invalid_start_time", invalidStartTime);
        object.put("invalid_end_time", invalidEndTime);
        return object;
    }

    @Override
    public String getPath() {
        return "/shop/pc/voucher-manage/voucher-invalid-page";
    }

    @Override
    public void setSize(Integer size) {
        this.size = size;
    }

    @Override
    public void setPage(Integer page) {
        this.page = page;
    }
}
