package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.customermanage;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.casedaily.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 6.17. 售后客户详情(杨)v3.0
 *
 * @author wangmin
 * @date 2021-03-31 12:32:56
 */
@Builder
public class AfterSaleCustomerInfoScene extends BaseScene {
    /**
     * 描述 车辆Id
     * 是否必填 true
     * 版本 v3.0
     */
    private final Long carId;

    /**
     * 描述 门店Id
     * 是否必填 true
     * 版本 v3.0
     */
    private final Long shopId;

    /**
     * 描述 客户Id
     * 是否必填 true
     * 版本 v3.0
     */
    private final Long customerId;


    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("car_id", carId);
        object.put("shop_id", shopId);
        object.put("customer_id", customerId);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/customer-manage/after-sale-customer/info";
    }
}