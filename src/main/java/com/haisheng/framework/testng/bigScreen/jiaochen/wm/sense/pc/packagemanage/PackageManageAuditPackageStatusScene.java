package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.packagemanage;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 18.4. 套餐审核 v2.0 （张小龙） 2020-01-28
 *
 * @author wangmin
 * @date 2021-08-30 14:26:55
 */
@Builder
public class PackageManageAuditPackageStatusScene extends BaseScene {
    /**
     * 描述 套餐id
     * 是否必填 false
     * 版本 v2.0
     */
    private final Long id;

    /**
     * 描述 套餐审核状态 AUDITING(0, "审核中"), AGREE(1,"已通过"),REFUSAL(2,"已拒绝"),
     * 是否必填 false
     * 版本 v2.0
     */
    private final String status;

    /**
     * 描述 确认购买状态 AGREE(1,"已通过")
     * 是否必填 false
     * 版本 -
     */
    private final String auditStatus;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("status", status);
        object.put("audit_status", auditStatus);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/pc/package-manage/audit-package-status";
    }
}