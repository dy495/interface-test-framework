package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.operation;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

import java.util.List;

@Builder
public class ApprovalScene extends BaseScene {
    private final List<Long> registerIds;
    private final String status;

    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("register_ids", registerIds);
        object.put("status", status);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/operation/approval";
    }
}
