package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.receptionmanager;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 接待管理 -> 开始接待
 */
@Builder
public class StartReception extends BaseScene {
    private final String customerId;
    private final String plateNumber;
    private final String customerName;
    private final String customerPhone;

    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("plate_number", plateNumber);
        object.put("customer_id", customerId);
        object.put("customer_name", customerName);
        object.put("customer_phone", customerPhone);
        return object;
    }

    @Override
    public String getPath() {
        return "/pc/reception-manage/start-reception";
    }
}
