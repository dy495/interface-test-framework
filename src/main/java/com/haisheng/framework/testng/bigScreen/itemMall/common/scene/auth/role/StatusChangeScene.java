package com.haisheng.framework.testng.bigScreen.itemMall.common.scene.auth.role;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 2.4. 角色状态变更 （杨航）
 *
 * @author wangmin
 * @date 2021-08-26 16:28:32
 */
@Builder
public class StatusChangeScene extends BaseScene {
    /**
     * 描述 角色id
     * 是否必填 true
     * 版本 v1.0
     */
    private final String id;

    /**
     * 描述 角色状态¬
     * 是否必填 true
     * 版本 v1.0
     */
    private final String status;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("status", status);
        return object;
    }

    @Override
    public String getPath() {
        return "/account-platform/auth/role/status/change";
    }
}