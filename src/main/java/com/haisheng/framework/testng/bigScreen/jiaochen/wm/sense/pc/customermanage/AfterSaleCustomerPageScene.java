package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.customermanage;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.base.scene.BaseScene;
import lombok.Builder;

/**
 * 客户管理 -> 售后客户列表
 */
@Builder
public class AfterSaleCustomerPageScene extends BaseScene {
    private final Long shopId;
    private final Long brandId;
    private final String orderStartTime;
    private final String orderEndTime;
    private final String customerName;
    private final String customerPhone;
    private final String createDate;
    private final String vehicleChassisCode;
    private final String createStartTime;
    private final String createEndTime;
    @Builder.Default
    private Integer page = 1;
    @Builder.Default
    private Integer size = 10;

    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("shop_id", shopId);
        object.put("brand_id", brandId);
        object.put("customer_name", customerName);
        object.put("customer_phone", customerPhone);
        object.put("create_date", createDate);
        object.put("vehicle_chassis_code", vehicleChassisCode);
        object.put("order_start_time", orderStartTime);
        object.put("order_end_time", orderEndTime);
        object.put("create_start_time", createStartTime);
        object.put("create_end_time", createEndTime);
        object.put("page", page);
        object.put("size", size);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/customer-manage/after-sale-customer/page";
    }


    @Override
    public void setSize(Integer size) {
        this.size = size;
    }

    @Override
    public void setPage(Integer page) {
        this.page = page;
    }
}
