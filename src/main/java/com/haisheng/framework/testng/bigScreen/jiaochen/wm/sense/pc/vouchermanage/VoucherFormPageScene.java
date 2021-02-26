package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.vouchermanage;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 卡券管理 -> 卡券表单
 */
@Builder
public class VoucherFormPageScene extends BaseScene {
    private final String voucherStatus;
    private final String subjectName;
    private final String voucherName;
    private final String creator;
    private final Boolean isDiff;
    private final Boolean isSelfVerification;
    private final Integer voucherType;
    @Builder.Default
    private Integer page = 1;
    @Builder.Default
    private Integer size = 10;

    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("voucher_status", voucherStatus);
        object.put("subject_name", subjectName);
        object.put("voucher_name", voucherName);
        object.put("creator", creator);
        object.put("is_diff", isDiff);
        object.put("is_self_verification", isSelfVerification);
        object.put("voucher_type", voucherType);
        object.put("page", page);
        object.put("size", size);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/voucher-manage/voucher-form/page";
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
