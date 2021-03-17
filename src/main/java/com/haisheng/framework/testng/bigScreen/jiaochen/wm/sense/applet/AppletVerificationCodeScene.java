package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * /jiaochen/applet/verification-code的接口
 *
 * @author wangmin
 * @date 2021-03-12 18:03:09
 */
@Builder
public class AppletVerificationCodeScene extends BaseScene {
    /**
     * 描述 手机号
     * 是否必填 true
     * 版本 v1.0
     */
    private final String phone;

    /**
     * 描述 验证码类型 默认为LOGIN
     * 是否必填 true
     * 版本 v1.0
     */
    private final String type;


    @Override
    public JSONObject getRequestBody(){
        JSONObject object = new JSONObject();
        object.put("phone", phone);
        object.put("type", type);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/applet/verification-code";
    }
}