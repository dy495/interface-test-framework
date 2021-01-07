package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.vouchermanage;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.scene.BaseScene;
import lombok.Builder;

/**
 * 卡券管理 -> 卡券表单
 */
@Builder
public class VoucherFormPage extends BaseScene {
    private final String voucherStatus;
    private final String subjectName;
    private final String voucherName;
    private final String creator;
    private final Boolean isDiff;
    private final Boolean isSelfVerification;
    @Builder.Default
    private final Integer page = 1;
    @Builder.Default
    private final Integer size = 10;

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
}
