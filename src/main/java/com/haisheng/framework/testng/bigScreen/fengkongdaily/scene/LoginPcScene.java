package com.haisheng.framework.testng.bigScreen.fengkongdaily.scene;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.casedaily.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 13.1. 用户登录
 *
 * @author wangmin
 * @date 2021-04-01 14:22:36
 */
@Builder
public class LoginPcScene extends BaseScene {
    /**
     * 描述 登录方式（0：账号密码，1：验证码 ,2:用户名密码）
     * 是否必填 false
     * 版本 -
     */
    private final Integer type;

    /**
     * 描述 请求名称
     * 是否必填 false
     * 版本 -
     */
    private final String username;

    /**
     * 描述 密码
     * 是否必填 false
     * 版本 -
     */
    private final String password;

    /**
     * 描述 手机号
     * 是否必填 false
     * 版本 -
     */
    private final String phone;

    /**
     * 描述 验证码
     * 是否必填 false
     * 版本 -
     */
    private final String verificationCode;


    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("type", type);
        object.put("username", username);
        object.put("password", password);
        object.put("phone", phone);
        object.put("verification_code", verificationCode);
        return object;
    }

    @Override
    public String getPath() {
        return "/risk-control/login-pc";
    }
}