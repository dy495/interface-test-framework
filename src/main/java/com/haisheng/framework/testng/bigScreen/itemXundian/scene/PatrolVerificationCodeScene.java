package com.haisheng.framework.testng.bigScreen.itemXundian.scene;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.base.scene.BaseScene;
import lombok.Builder;

/**
 * 35.1. 获取验证码
 *
 * @author wangmin
 * @date 2021-06-29 14:11:44
 */
@Builder
public class PatrolVerificationCodeScene extends BaseScene {
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
     * 描述 手机号
     * 是否必填 true
     * 版本 -
     */
    private final String phone;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("appId", appId);
        object.put("phone", phone);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol-verification-code";
    }
}