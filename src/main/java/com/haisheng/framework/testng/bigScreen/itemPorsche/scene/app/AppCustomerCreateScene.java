package com.haisheng.framework.testng.bigScreen.itemPorsche.scene.app;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.casedaily.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 创建客户接口
 */
@Builder
public class AppCustomerCreateScene extends BaseScene {
    private final String customerName;
    private final String customerPhone;
    private final String customerLevel;
    private final String intentionCarModel;
    private final String intentionCarStyle;
    private final String remark;

    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("customer_name", customerName);
        object.put("customer_phone", customerPhone);
        object.put("customer_level", customerLevel);
        object.put("intention_car_model", intentionCarModel);
        object.put("intention_car_style", intentionCarStyle);
        object.put("remark", remark);
        return object;
    }

    @Override
    public String getPath() {
        return "/porsche/app/customer/create";
    }

    @Override
    public String getIpPort() {
        return null;
    }
}
