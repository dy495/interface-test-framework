package com.haisheng.framework.testng.bigScreen.xundianDaily.gly.scene.riskcontrol.cashier;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 50.2. 收银追溯
 *
 * @author wangmin
 * @date 2021-06-29 14:11:45
 */
@Builder
public class TraceBackScene extends BaseScene {
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
     * 描述 当前页
     * 是否必填 true
     * 版本 -
     */
    private final Integer page;

    /**
     * 描述 当前页的数量
     * 是否必填 true
     * 版本 -
     */
    private final Integer size;

    /**
     * 描述 门店id
     * 是否必填 true
     * 版本 -
     */
    private final Long shopId;

    /**
     * 描述 小票日期
     * 是否必填 false
     * 版本 -
     */
    private final String date;

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
        object.put("page", page);
        object.put("size", size);
        object.put("shop_id", shopId);
        object.put("date", date);
        object.put("order_id", orderId);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol/risk-control/cashier/trace-back";
    }
}