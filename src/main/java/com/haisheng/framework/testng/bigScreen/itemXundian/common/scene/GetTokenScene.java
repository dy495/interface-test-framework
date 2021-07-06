package com.haisheng.framework.testng.bigScreen.itemXundian.common.scene;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 35.6. 业务方获取token接口
 *
 * @author wangmin
 * @date 2021-06-29 14:11:44
 */
@Builder
public class GetTokenScene extends BaseScene {
    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final String referer;

    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final String appId;

    /**
     * 描述 登录方式（0：账号密码，1：验证码）
     * 是否必填 false
     * 版本 -
     */
    private final Integer type;

    /**
     * 描述 用户名(登录方式为0必填)
     * 是否必填 false
     * 版本 -
     */
    private final String username;

    /**
     * 描述 加密后密码（登录方式为0必填）
     * 是否必填 false
     * 版本 -
     */
    private final String password;

    /**
     * 描述 手机号（登录方式为1必填）
     * 是否必填 false
     * 版本 -
     */
    private final String phone;

    /**
     * 描述 验证码（登录方式为1必填）
     * 是否必填 false
     * 版本 -
     */
    private final String verificationCode;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("appId", appId);
        object.put("type", type);
        object.put("username", username);
        object.put("password", password);
        object.put("phone", phone);
        object.put("verification_code", verificationCode);
        return object;
    }

    @Override
    public String getPath() {
        return "/get-token";
    }
}