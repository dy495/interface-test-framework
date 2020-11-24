package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.customermanager;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.scene.BaseScene;
import lombok.Builder;

/**
 * 客户管理 -> 售后客户列表
 */
@Builder
public class AfterSaleCustomerPage extends BaseScene {
    private final String customerName;
    private final String customerPhone;
    private final String createDate;
    private final String vehicleChassisCode;
    private final Integer page;
    private final Integer size;

    @Override
    public JSONObject getJSONObject() {
        JSONObject object = new JSONObject();
        object.put("customer_name", customerName);
        object.put("customer_phone", customerPhone);
        object.put("create_date", createDate);
        object.put("vehicle_chassis_code", vehicleChassisCode);
        object.put("page", page);
        object.put("size", size);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/customer-manage/after-sale-customer/page";
    }

    @Override
    public String getIpPort() {
        return null;
    }
}
