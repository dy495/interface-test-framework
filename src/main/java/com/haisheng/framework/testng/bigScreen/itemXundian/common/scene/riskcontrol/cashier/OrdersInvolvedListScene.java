package com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.riskcontrol.cashier;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 46.3. 风控事件涉及订单列表
 *
 * @author wangmin
 * @date 2021-07-30 10:55:36
 */
@Builder
public class OrdersInvolvedListScene extends BaseScene {
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
     * 描述 id
     * 是否必填 true
     * 版本 -
     */
    private final Long id;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("appId", appId);
        object.put("page", page);
        object.put("size", size);
        object.put("id", id);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol/risk-control/cashier/risk-event/orders-involved/list";
    }
}