package com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.cashier;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.base.scene.BaseScene;
import lombok.Builder;

/**
 * 5.2. 收银追溯
 *
 * @author wangmin
 * @date 2021-04-01 14:22:36
 */
@Builder
public class TraceBackScene extends BaseScene {
    /**
     * 描述 页码 大于0
     * 是否必填 true
     * 版本 v1.0
     */
    private final Integer page;

    /**
     * 描述 页大小 范围为[1,100]
     * 是否必填 true
     * 版本 v1.0
     */
    private final Integer size;

    /**
     * 描述 是否导出
     * 是否必填 false
     * 版本 v1.0
     */
    private final Boolean isExport;

    /**
     * 描述 门店id
     * 是否必填 true
     * 版本 v1.0
     */
    private final Long shopId;

    /**
     * 描述 小票日期
     * 是否必填 false
     * 版本 v1.0
     */
    private final String date;

    /**
     * 描述 订单id
     * 是否必填 false
     * 版本 v1.0
     */
    private final String orderId;


    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("page", page);
        object.put("size", size);
        object.put("is_export", isExport);
        object.put("shop_id", shopId);
        object.put("date", date);
        object.put("order_id", orderId);
        return object;
    }

    @Override
    public String getPath() {
        return "/risk-control/auth/cashier/trace-back";
    }
}