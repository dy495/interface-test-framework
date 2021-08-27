package com.haisheng.framework.testng.bigScreen.itemMall.common.scene.auth;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 1.1. 账号权限集合
 *
 * @author wangmin
 * @date 2021-08-26 16:28:31
 */
@Builder
public class AuthTreeScene extends BaseScene {
    /**
     * 描述 父角色Id
     * 是否必填 false
     * 版本 -
     */
    private final Integer parentRole;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("parent_role", parentRole);
        return object;
    }

    @Override
    public String getPath() {
        return "/account-platform/auth/tree";
    }
}