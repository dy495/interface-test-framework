package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.granted;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.casedaily.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 我的卡券列表
 */
@Builder
public class AppletVoucherListScene extends BaseScene {
    private final Integer id;
    private final Integer status;
    private final String type;
    private final Integer size;

    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        JSONObject lastValue = new JSONObject();
        lastValue.put("id", id);
        lastValue.put("status", status);
        object.put("last_value", lastValue);
        object.put("type", type);
        object.put("size", size);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/applet/granted/voucher/list";
    }
}
