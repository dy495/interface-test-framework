package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.voucher;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.scene.BaseScene;
import lombok.Builder;

/**
 * 卡券申请分页
 */
@Builder
public class ApplyPage extends BaseScene {
    @Builder.Default
    private final Integer page = 1;
    @Builder.Default
    private final Integer size = 10;
    private final String name;
    private final String sendTime;
    private final String applyName;
    private final String status;
    private final String applyGroup;
    private final String costCenter;
    private final String applyTime;
    private final String applyItem;

    @Override
    public JSONObject getJSONObject() {
        JSONObject object = new JSONObject();
        object.put("page", page);
        object.put("name", name);
        object.put("send_time", sendTime);
        object.put("apply_name", applyName);
        object.put("status", status);
        object.put("apply_group", applyGroup);
        object.put("cost_center", costCenter);
        object.put("apply_time", applyTime);
        object.put("applyItem", applyItem);
        object.put("size", size);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/voucher/apply/page";
    }
}
