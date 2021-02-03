package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.granted;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 自助核销卡券
 */
@Builder
public class VoucherVerificationScene extends BaseScene {

    /**
     * 小程序我的卡券对应的id
     */
    private final String id;

    /**
     * 核销码
     */
    private final String verificationCode;

    @Override
    public JSONObject getJSONObject() {
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("verification_code", verificationCode);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/applet/granted/voucher/verification";
    }
}
