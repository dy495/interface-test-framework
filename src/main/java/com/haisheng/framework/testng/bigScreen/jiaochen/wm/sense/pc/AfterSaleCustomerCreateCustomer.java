package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.scene.BaseScene;
import lombok.Builder;

/**
 * 客户管理 -> 新建售后客户
 */
@Builder
public class AfterSaleCustomerCreateCustomer extends BaseScene {
    private final String customerName;
    private final String customerPhone;
    private final Integer sex;
    private final Integer customerType;
    private final String saleId;
    private final String plateNumber;

    @Override
    public JSONObject getJSONObject() {
        JSONObject object = new JSONObject();
        object.put("customer_name", customerName);
        object.put("customer_phone", customerPhone);
        object.put("sex", sex);
        object.put("customer_type", customerType);
        object.put("sale_id", saleId);
        object.put("plate_number", plateNumber);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/customer-manage/after-sale-customer/create-customer";
    }

    @Override
    public String getIpPort() {
        return null;
    }
}
