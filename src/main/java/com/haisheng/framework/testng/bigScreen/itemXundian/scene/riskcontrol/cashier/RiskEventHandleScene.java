package com.haisheng.framework.testng.bigScreen.itemXundian.scene.riskcontrol.cashier;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.base.scene.BaseScene;
import lombok.Builder;

/**
 * 49.5. 风控事件处理
 *
 * @author wangmin
 * @date 2021-06-29 14:11:45
 */
@Builder
public class RiskEventHandleScene extends BaseScene {
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
     * 描述 id
     * 是否必填 true
     * 版本 -
     */
    private final Long id;

    /**
     * 描述 结果 1订单无异常，0订单异常
     * 是否必填 true
     * 版本 -
     */
    private final Integer result;

    /**
     * 描述 备注
     * 是否必填 true
     * 版本 -
     */
    private final String remarks;

    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final JSONArray customerIds;

    /**
     * 描述 名单类型
     * 是否必填 false
     * 版本 -
     */
    private final String type;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("appId", appId);
        object.put("id", id);
        object.put("result", result);
        object.put("remarks", remarks);
        object.put("customer_ids", customerIds);
        object.put("type", type);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol/risk-control/cashier/risk-event/handle";
    }
}