package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.customermanager;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 客户管理 -> 销售客户列表
 */
@Builder
public class PreSaleCustomerPageScene extends BaseScene {
    private final String customerName;
    private final String customerPhone;
    private final String customerType;
    private final String saleName;
    private final String sex;
    private final String startTime;
    private final String endTime;
    @Builder.Default
    private final Integer page = 1;
    @Builder.Default
    private final Integer size = 10;

    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("customer_name", customerName);
        object.put("customer_phone", customerPhone);
        object.put("sale_name", saleName);
        object.put("customer_type", customerType);
        object.put("page", page);
        object.put("size", size);
        object.put("sex", sex);
        object.put("startTime", startTime);
        object.put("endTime", endTime);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/customer-manage/pre-sale-customer/page";
    }
}
