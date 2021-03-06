package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.voucher;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

@Builder
public class ApplyApprovalScene extends BaseScene {
    @Builder.Default
    private Integer page = 1;
    @Builder.Default
    private Integer size = 10;
    private final Long id;
    private final String status;

    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("page", page);
        object.put("id", id);
        object.put("status", status);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/pc/voucher/apply/approval";
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
