package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.packagemanager;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

@Builder
public class MakeSureBuyScene extends BaseScene {
    private final Long id;
    private final String auditStatus;

    @Override
    public JSONObject getJSONObject() {
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("audit_status", auditStatus);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/package-manage/make-sure-buy";
    }
}
