package com.haisheng.framework.testng.bigScreen.xundianDaily.gly.scene.riskcontrol.cashier;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 49.1. 收银风控事件列表
 *
 * @author wangmin
 * @date 2021-06-29 14:11:45
 */
@Builder
public class RiskEventPageScene extends BaseScene {
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
     * 描述 风控事件
     * 是否必填 false
     * 版本 -
     */
    private final String eventName;

    /**
     * 描述 小票号
     * 是否必填 false
     * 版本 -
     */
    private final String orderId;

    /**
     * 描述 收银日期
     * 是否必填 false
     * 版本 -
     */
    private final String orderDate;

    /**
     * 描述 会员姓名
     * 是否必填 false
     * 版本 -
     */
    private final String memberName;

    /**
     * 描述 处理结果
     * 是否必填 false
     * 版本 -
     */
    private final String handleResult;

    /**
     * 描述 当前状态
     * 是否必填 false
     * 版本 -
     */
    private final String currentState;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("appId", appId);
        object.put("page", page);
        object.put("size", size);
        object.put("shop_id", shopId);
        object.put("event_name", eventName);
        object.put("order_id", orderId);
        object.put("order_date", orderDate);
        object.put("member_name", memberName);
        object.put("handle_result", handleResult);
        object.put("current_state", currentState);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol/risk-control/cashier/risk-event/page";
    }
}