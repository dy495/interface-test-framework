package com.haisheng.framework.testng.bigScreen.itemXundian.scene.riskcontrol.cashier;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.casedaily.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 50.4. 收银追溯查看小票详情
 *
 * @author wangmin
 * @date 2021-06-29 14:11:45
 */
@Builder
public class OrderDetailScene extends BaseScene {
    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final String referer;

    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final String appId;

    /**
     * 描述 门店id
     * 是否必填 true
     * 版本 -
     */
    private final Long shopId;

    /**
     * 描述 订单id
     * 是否必填 false
     * 版本 -
     */
    private final String orderId;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("appId", appId);
        object.put("shop_id", shopId);
        object.put("order_id", orderId);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol/risk-control/cashier/order-detail";
    }
}