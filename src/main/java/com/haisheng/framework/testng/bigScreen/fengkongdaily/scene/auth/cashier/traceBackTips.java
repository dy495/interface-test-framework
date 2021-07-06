package com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.cashier;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

@Builder
public class traceBackTips extends BaseScene {
    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        return object;
    }

    @Override
    public String getPath() {
        return "/risk-control/auth/cashier/trace-back/tips";
    }
}
