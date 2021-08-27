package com.haisheng.framework.testng.bigScreen.itemMall.common.scene.auth.loginuser;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 6.3. 修改密码
 *
 * @author wangmin
 * @date 2021-08-26 16:28:32
 */
@Builder
public class LoginUserModifypasswordScene extends BaseScene {
    /**
     * 描述 旧密码
     * 是否必填 false
     * 版本 -
     */
    private final String oldPassword;

    /**
     * 描述 新密码
     * 是否必填 false
     * 版本 -
     */
    private final String newPassword;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("old_password", oldPassword);
        object.put("new_password", newPassword);
        return object;
    }

    @Override
    public String getPath() {
        return "/account-platform/auth/login-user/modifyPassword";
    }
}