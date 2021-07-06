package com.haisheng.framework.testng.bigScreen.itemPorsche.common.scene.pc;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

@Builder
public class CustomerListScene extends BaseScene {
    private final String customerName;
    private final String customerPhone;
    private final String customerLevel;
    private final String startTime;
    private final String endTime;
    private final String belongsSaleId;
    @Builder.Default
    private final Integer page = 1;
    @Builder.Default
    private final Integer size = 10;

    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("customer_name", customerName);
        object.put("customer_phone", customerPhone);
        object.put("customer_level", customerLevel);
        object.put("start_time", startTime);
        object.put("end_time", endTime);
        object.put("page", page);
        object.put("size", size);
        object.put("belongs_sale_id", belongsSaleId);
        return object;
    }

    @Override
    public String getPath() {
        return "/porsche/customer/list";
    }
}
