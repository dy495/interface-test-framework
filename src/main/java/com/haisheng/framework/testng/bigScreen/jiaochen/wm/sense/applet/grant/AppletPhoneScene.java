package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.grant;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.casedaily.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 15.3. 授权其他手机号（xie）（2021-01-21）
 *
 * @author wangmin
 * @date 2021-03-31 13:03:22
 */
@Builder
public class AppletPhoneScene extends BaseScene {
    /**
     * 描述 手机号
     * 是否必填 true
     * 版本 v1.0
     */
    private final String phone;

    /**
     * 描述 验证码
     * 是否必填 true
     * 版本 v1.0
     */
    private final String verificationCode;


    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("phone", phone);
        object.put("verification_code", verificationCode);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/applet/grant/phone";
    }
}