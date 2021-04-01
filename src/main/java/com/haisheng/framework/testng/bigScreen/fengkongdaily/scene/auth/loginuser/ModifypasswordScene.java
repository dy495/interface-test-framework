package com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.loginuser;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 14.3. 修改密码
 *
 * @author wangmin
 * @date 2021-04-01 14:22:36
 */
@Builder
public class ModifypasswordScene extends BaseScene {
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
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("old_password", oldPassword);
        object.put("new_password", newPassword);
        return object;
    }

    @Override
    public String getPath() {
        return "/risk-control/auth/login-user/modifyPassword";
    }
}