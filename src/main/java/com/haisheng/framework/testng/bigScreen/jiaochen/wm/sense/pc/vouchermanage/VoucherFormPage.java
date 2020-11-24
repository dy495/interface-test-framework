package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.vouchermanage;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.scene.BaseScene;
import lombok.Builder;

/**
 * 预约管理 -> 预约记录
 */
@Builder
public class VoucherFormPage extends BaseScene {
    private final String voucherStatus;
    private final String subjectName;
    private final String voucherName;
    private final String creator;
    private final Boolean isDiff;
    private final Boolean isSelfVerification;
    private final Integer page;
    private final Integer size;

    @Override
    public JSONObject getJSONObject() {
        JSONObject object = new JSONObject();
        object.put("voucher_status", voucherStatus);
        object.put("subject_name", subjectName);
        object.put("voucher_name", voucherName);
        object.put("creator", creator);
        object.put("is_diff", isDiff);
        object.put("is_self_verification", isSelfVerification);
        object.put("page", page);
        object.put("size", size);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/voucher-manage/voucher-form/page";
    }

    @Override
    public String getIpPort() {
        return null;
    }
}
