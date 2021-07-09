package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.role;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * @author wangmin
 * @date 2021-05-18 17:04:36
 */
@Builder
public class RoleListScene extends BaseScene {


    @Override
    protected JSONObject getRequestBody() {
        return new JSONObject();
    }

    @Override
    public String getPath() {
        return "/account-platform/auth/role/list";
    }
}