package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.packagemanager;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 套餐审批
 */
@Builder
public class AuditPackageStatusScene extends BaseScene {

    /**
     * 卡券id
     */
    private final Long id;

    /**
     * 审批是否通过
     */
    private final String status;

    @Override
    public JSONObject getJSONObject() {
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("status", status);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/package-manage/audit-package-status";
    }
}
