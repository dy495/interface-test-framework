package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.customermanager;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 客户管理 -> 新建销售客户
 */
@Builder
public class CreateCustomerScene extends BaseScene {
    private final String customerName;
    private final String customerPhone;
    private final Integer sex;
    private final Integer customerType;
    private final Integer carModelId;
    private final String salesId;
    private final String purchaseCarDate;

    @Override
    public JSONObject getRequest() {
        JSONObject object = new JSONObject();
        object.put("customer_name", customerName);
        object.put("customer_phone", customerPhone);
        object.put("sex", sex);
        object.put("customer_type", customerType);
        object.put("salesId", salesId);
        object.put("purchase_car_date", purchaseCarDate);
        object.put("car_model_id", carModelId);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/customer-manage/pre-sale-customer/create-customer";
    }
}
