package com.haisheng.framework.testng.bigScreen.xundian.scene.applet.grant;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 7.3. 授权其他手机号（xie）（2021-01-21）car_platform_path: /jiaochen/applet/grant/phone
 *
 * @author wangmin
 * @date 2021-03-30 15:23:58
 */
@Builder
public class AppletPhoneScene extends BaseScene {
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
     * 描述 验证码
     * 是否必填 true
     * 版本 v1.0
     */
    private final String verificationCode;


    @Override
    public JSONObject getRequestBody(){
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("phone", phone);
        object.put("verification_code", verificationCode);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol-applet/grant/phone";
    }
}