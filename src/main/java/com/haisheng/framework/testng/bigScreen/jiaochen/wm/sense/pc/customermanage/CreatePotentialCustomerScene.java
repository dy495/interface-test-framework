package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.customermanage;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

@Builder
public class CreatePotentialCustomerScene extends BaseScene {
    private final String customerName;
    private final String customerPhone;
    private final Integer sex;
    private final Integer customerType;
    private final String carModelId;

    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("customer_name", customerName);
        object.put("customer_phone", customerPhone);
        object.put("sex", sex);
        object.put("customer_type", customerType);
        object.put("car_model_id", carModelId);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/customer-manage/pre-sale-customer/create-potential-customer";
    }
}
