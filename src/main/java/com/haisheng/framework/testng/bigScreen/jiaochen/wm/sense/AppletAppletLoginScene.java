package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * /jiaochen/applet-login的接口
 *
 * @author wangmin
 * @date 2021-03-12 18:06:01
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
    public JSONObject getRequestBody(){
        JSONObject object = new JSONObject();
        object.put("code", code);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/applet-login";
    }
}