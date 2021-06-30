package com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.cashier;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.base.scene.BaseScene;
import lombok.Builder;

/**
 * 6.5. 风控事件处理
 *
 * @author wangmin
 * @date 2021-04-01 14:22:36
 */
@Builder
public class RiskEventHandleScene extends BaseScene {
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
     * 描述 注册人物列表
     * 是否必填 false
     * 版本 -
     */
    private final JSONArray customerIds;


    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("result", result);
        object.put("remarks", remarks);
        object.put("customer_ids", customerIds);
        return object;
    }

    @Override
    public String getPath() {
        return "/risk-control/auth/cashier/risk-event/handle";
    }
}