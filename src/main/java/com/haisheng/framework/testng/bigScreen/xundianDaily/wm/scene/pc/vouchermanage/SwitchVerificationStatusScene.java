package com.haisheng.framework.testng.bigScreen.xundianDaily.wm.scene.pc.vouchermanage;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

@Builder
public class SwitchVerificationStatusScene extends BaseScene {
    private final Long id;
    private final Boolean status;

    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("status", status);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol/pc/voucher-manage/switch-verification-status";
    }
}
