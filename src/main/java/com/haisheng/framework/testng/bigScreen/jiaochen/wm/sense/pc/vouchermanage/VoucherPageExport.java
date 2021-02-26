package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.vouchermanage;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

import java.util.List;

@Builder
public class VoucherPageExport extends BaseScene {
    private final String voucherStatus;
    private final String voucherType;
    private final String voucherName;
    private final String subjectName;
    private final String creatorName;
    private final String creatorAccount;
    private final String exportType;
    private final List<Long> ids;
    @Builder.Default
    private Integer page = 1;
    @Builder.Default
    private Integer size = 10;

    @Override
    public JSONObject getRequest() {
        JSONObject object = new JSONObject();
        object.put("voucher_status", voucherStatus);
        object.put("voucher_type", voucherType);
        object.put("voucher_name", voucherName);
        object.put("subject_name", subjectName);
        object.put("creator_name", creatorName);
        object.put("creator_account", creatorAccount);
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
