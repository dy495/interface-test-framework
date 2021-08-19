package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.loginuser;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumTestProduct;
import lombok.Builder;

@Builder
public class LoginPc extends BaseScene {
    private final String phone;
    private final String verificationCode;
    private final Integer type;

    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("phone", phone);
        object.put("verification_code", verificationCode);
        object.put("type", type);
        return object;
    }

    @Override
    public String getPath() {
        return "/account-platform/login-pc";
    }

    @Override
    public String getIpPort() {
        return getVisitor().isDaily() ? EnumTestProduct.JC_DAILY_ZH.getIp() : EnumTestProduct.JC_ONLINE_ZH.getIp();
    }
}
