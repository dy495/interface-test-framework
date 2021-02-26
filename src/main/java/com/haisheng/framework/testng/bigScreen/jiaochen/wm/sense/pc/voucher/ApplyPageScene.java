package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.voucher;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 卡券申请分页
 */
@Builder
public class ApplyPageScene extends BaseScene {
    @Builder.Default
    private Integer page = 1;
    @Builder.Default
    private Integer size = 10;
    private final String name;
    private final String sendTime;
    private final String applyName;
    private final String status;
    private final String applyGroup;
    private final String costCenter;
    private final String applyTime;
    private final String applyItem;

    /**
     * 2.0
     */
    private final Integer state;

    @Override
    public JSONObject getRequest() {
        JSONObject object = new JSONObject();
        object.put("page", page);
        object.put("name", name);
        object.put("send_time", sendTime);
        object.put("apply_name", applyName);
        object.put("apply_group", applyGroup);
        object.put("cost_center", costCenter);
        object.put("apply_time", applyTime);
        object.put("applyItem", applyItem);
        object.put("size", size);
        if (status != null) {
            object.put("status", status);
        } else {
            object.put("status", state);
        }
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/voucher/apply/page";
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
