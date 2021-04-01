package com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.staff;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 12.5. 员工状态修改
 *
 * @author wangmin
 * @date 2021-04-01 14:22:36
 */
@Builder
public class StatusChangeScene extends BaseScene {
    /**
     * 描述 账号uid
     * 是否必填 true
     * 版本 v1.0
     */
    private final String id;

    /**
     * 描述 账号状态
     * 是否必填 true
     * 版本 v1.0
     */
    private final String status;


    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("status", status);
        return object;
    }

    @Override
    public String getPath() {
        return "/risk-control/auth/staff/status/change";
    }
}