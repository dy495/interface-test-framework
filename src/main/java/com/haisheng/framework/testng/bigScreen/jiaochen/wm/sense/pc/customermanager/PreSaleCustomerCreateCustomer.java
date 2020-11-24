package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.customermanager;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.scene.BaseScene;
import lombok.Builder;

/**
 * 客户管理 -> 新建
 */
@Builder
public class PreSaleCustomerCreateCustomer extends BaseScene {
    private final String customerName;
    private final String customerPhone;
    private final Integer sex;
    private final Integer customerType;

    @Override
    public JSONObject getJSONObject() {
        JSONObject object = new JSONObject();
        object.put("customer_name", customerName);
        object.put("customer_phone", customerPhone);
        object.put("sex", sex);
        object.put("customer_type", customerType);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/customer-manage/pre-sale-customer/create-customer";
    }

    @Override
    public String getIpPort() {
        return null;
    }
}
