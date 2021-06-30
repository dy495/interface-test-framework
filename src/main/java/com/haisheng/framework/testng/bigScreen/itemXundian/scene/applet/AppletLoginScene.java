package com.haisheng.framework.testng.bigScreen.itemXundian.scene.applet;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.casedaily.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 7.1. 微信小程序登录（谢）car_platform_path: /jiaochen/applet-login
 *
 * @author wangmin
 * @date 2021-03-30 15:23:58
 */
@Builder
public class AppletLoginScene extends BaseScene {
    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final String referer;

    /**
     * 描述 小程序登录授权code
     * 是否必填 true
     * 版本 v1.0
     */
    private final String code;


    @Override
    public JSONObject getRequestBody(){
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("code", code);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol-applet/login";
    }
}