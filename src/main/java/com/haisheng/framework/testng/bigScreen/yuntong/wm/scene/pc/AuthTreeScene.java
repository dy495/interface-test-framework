package com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.pc;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * @author wangmin
 * @date 2021-05-18 17:04:35
 */
@Builder
public class AuthTreeScene extends BaseScene {

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