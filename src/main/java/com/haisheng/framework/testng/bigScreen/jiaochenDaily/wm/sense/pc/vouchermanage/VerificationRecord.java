package com.haisheng.framework.testng.bigScreen.jiaochenDaily.wm.sense.pc.vouchermanage;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crmDaily.wm.scene.BaseScene;
import lombok.Builder;

/**
 * 预约管理 -> 预约记录
 */
@Builder
public class VerificationRecord extends BaseScene {
    private final String voucherName;
    private final String sender;
    private final Long startTime;
    private final Long endTime;
    @Builder.Default
    private final Integer page = 1;
    @Builder.Default
    private final Integer size = 10;

    @Override
    public JSONObject getJSONObject() {
        JSONObject object = new JSONObject();
        object.put("voucher_name", voucherName);
        object.put("sender", sender);
        object.put("start_time", startTime);
        object.put("end_time", endTime);
        object.put("page", page);
        object.put("size", size);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/voucher-manage/verification-record";
    }

    @Override
    public String getIpPort() {
        return null;
    }
}
