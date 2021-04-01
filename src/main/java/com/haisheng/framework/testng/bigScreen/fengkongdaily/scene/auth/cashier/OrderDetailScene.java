package com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.cashier;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 5.4. 收银追溯查看小票详情
 *
 * @author wangmin
 * @date 2021-04-01 14:22:36
 */
@Builder
public class OrderDetailScene extends BaseScene {
    /**
     * 描述 门店id
     * 是否必填 true
     * 版本 v1.0
     */
    private final Long shopId;

    /**
     * 描述 订单id
     * 是否必填 false
     * 版本 v1.0
     */
    private final String orderId;


    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("shop_id", shopId);
        object.put("order_id", orderId);
        return object;
    }

    @Override
    public String getPath() {
        return "/risk-control/auth/cashier/order-detail";
    }
}