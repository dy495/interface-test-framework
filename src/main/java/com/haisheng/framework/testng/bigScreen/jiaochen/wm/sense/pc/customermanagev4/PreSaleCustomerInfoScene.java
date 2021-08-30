package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.customermanagev4;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 7.1. 售前客户详情(池)v4.0 （2021-05-06 更新）
 *
 * @author wangmin
 * @date 2021-08-30 14:26:54
 */
@Builder
public class PreSaleCustomerInfoScene extends BaseScene {
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
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("shop_id", shopId);
        object.put("customer_id", customerId);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/pc/customer-manage-v4/pre-sale-customer/info";
    }
}