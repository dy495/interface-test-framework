package com.haisheng.framework.testng.bigScreen.itemXundian.scene.applet.granted;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.base.scene.BaseScene;
import lombok.Builder;

/**
 * 自助核销卡券
 */
@Builder
public class AppletVoucherVerificationScene extends BaseScene {

    /**
     * 小程序我的卡券对应的id
     */
    private final String id;

    /**
     * 核销码
     */
    private final String verificationCode;

    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("verification_code", verificationCode);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol-applet/granted/voucher/verification";
    }
}
