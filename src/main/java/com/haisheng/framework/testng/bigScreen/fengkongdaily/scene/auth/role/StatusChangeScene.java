package com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.role;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 10.4. 角色状态变更 （杨航）
 *
 * @author wangmin
 * @date 2021-04-01 14:22:36
 */
@Builder
public class StatusChangeScene extends BaseScene {
    /**
     * 描述 角色id
     * 是否必填 true
     * 版本 v1.0
     */
    private final Integer id;

    /**
     * 描述 角色状态¬
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
        return "/risk-control/auth/role/status/change";
    }
}