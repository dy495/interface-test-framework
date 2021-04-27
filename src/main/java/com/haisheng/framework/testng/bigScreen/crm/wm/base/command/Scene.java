package com.haisheng.framework.testng.bigScreen.crm.wm.base.command;

import com.alibaba.fastjson.JSONObject;
import lombok.Setter;
import lombok.experimental.Accessors;

public class Scene {
    private final String phone;
    private final String verificationCode;

    public Scene(Builder builder) {
        this.phone = builder.phone;
        this.verificationCode = builder.verificationCode;
    }

    public Response getResponse() {
        JSONObject object = new JSONObject();
        object.put("phone", phone);
        object.put("verification_code", verificationCode);
        Api api = new Api.Builder().header("shop_id", "-1").header("role_id", "2942").requestBody(object).contentType("application/json").method(EnumMethod.POST).build();
        return api.getMethod().getCommand().execute(api, "http://dev.dealer-jc.winsenseos.cn/jiaochen/login-pc");
    }

    @Setter
    @Accessors(chain = true, fluent = true)
    public static class Builder {
        private String phone;
        private String verificationCode;

        public Scene build() {
            return new Scene(this);
        }

    }
}
