package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 15.1. 微信小程序登录（谢）
 *
 * @author wangmin
 * @date 2021-03-31 13:03:22
 */
@Builder
public class AppletAppletLoginScene extends BaseScene {
    /**
     * 描述 小程序登录授权code
     * 是否必填 true
     * 版本 v1.0
     */
    private final String code;


    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("code", code);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/applet-login";
    }
}