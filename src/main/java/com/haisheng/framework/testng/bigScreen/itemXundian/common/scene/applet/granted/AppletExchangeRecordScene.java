package com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.applet.granted;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

@Builder
public class AppletExchangeRecordScene extends BaseScene {
    private final Integer lastValue;
    private final Integer size;
    private final Boolean status;

    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("last_value", lastValue);
        object.put("size", size);
        object.put("status", status);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol-applet/granted/integral-mall/exchange-record";
    }
}
