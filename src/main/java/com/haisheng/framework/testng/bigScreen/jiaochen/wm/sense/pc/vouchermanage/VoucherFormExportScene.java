package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.vouchermanage;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

@Builder
public class VoucherFormExportScene extends BaseScene {
    private final String voucherStatus;
    private final String voucherName;
    private final String voucherType;
    private final String subjectName;
    private final String creatorName;
    private final String creatorAccount;
    private final String exportType;
    private final String ids;
    @Builder.Default
    private Integer page = 1;
    @Builder.Default
    private Integer size = 10;

    @Override
    public JSONObject getJSONObject() {
        JSONObject object = new JSONObject();
        object.put("voucher_status", voucherStatus);
        object.put("voucher_type", voucherType);
        object.put("subject_name", subjectName);
        object.put("voucher_name", voucherName);
        object.put("creator_name", creatorName);
        object.put("creator_account", creatorAccount);
        object.put("export_type", exportType);
        object.put("page", page);
        object.put("size", size);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/voucher-manage/voucher-form/export";
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
