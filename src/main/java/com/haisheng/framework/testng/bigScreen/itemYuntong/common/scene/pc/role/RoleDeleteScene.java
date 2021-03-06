package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.role;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 34.4. 删除角色 （杨航）
 *
 * @author wangmin
 * @date 2021-05-18 17:04:36
 */
@Builder
public class RoleDeleteScene extends BaseScene {
    /**
     * 描述 角色id
     * 是否必填 true
     * 版本 v1.0
     */
    private final Integer id;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("id", id);
        return object;
    }

    @Override
    public String getPath() {
        return "/account-platform/auth/role/delete";
    }
}