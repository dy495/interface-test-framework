package com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.cashier;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.base.scene.BaseScene;
import lombok.Builder;

/**
 * 6.1. 收银风控事件分页
 *
 * @author wangmin
 * @date 2021-04-01 14:22:36
 */
@Builder
public class RiskEventPageScene extends BaseScene {
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
     * 描述 风控事件
     * 是否必填 false
     * 版本 v1.0
     */
    private final String eventName;

    /**
     * 描述 小票号
     * 是否必填 false
     * 版本 v1.0
     */
    private final String orderId;

    /**
     * 描述 收银日期
     * 是否必填 false
     * 版本 v1.0
     */
    private final String orderDate;

    /**
     * 描述 会员Id
     * 是否必填 false
     * 版本 v1.0
     */
    private final String memberId;

    /**
     * 描述 会员姓名
     * 是否必填 false
     * 版本 v1.0
     */
    private final String memberName;

    /**
     * 描述 触发的风控规则id
     * 是否必填 false
     * 版本 v1.0
     */
    private final Long roleId;

    /**
     * 描述 处理结果
     * 是否必填 false
     * 版本 v1.0
     */
    private final String handleResult;

    /**
     * 描述 当前状态
     * 是否必填 false
     * 版本 v1.0
     */
    private final String currentState;
    private final String evenId;


    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("page", page);
        object.put("size", size);
        object.put("is_export", isExport);
        object.put("shop_id", shopId);
        object.put("event_name", eventName);
        object.put("order_id", orderId);
        object.put("order_date", orderDate);
        object.put("member_id", memberId);
        object.put("member_name", memberName);
        object.put("role_id", roleId);
        object.put("handle_result", handleResult);
        object.put("current_state", currentState);
        object.put("even_id",evenId);
        return object;
    }

    @Override
    public String getPath() {
        return "/risk-control/auth/cashier/risk-event/page";
    }
}