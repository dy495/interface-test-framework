package com.haisheng.framework.testng.bigScreen.xundian.scene.applet;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 7.2. 获取小程序验证码（谢）car_platform_path: /jiaochen/applet/verification-code
 *
 * @author wangmin
 * @date 2021-03-30 15:23:58
 */
@Builder
public class AppletVerificationCodeScene extends BaseScene {
    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final String referer;

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
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("phone", phone);
        object.put("type", type);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol-applet/verification-code";
    }
}