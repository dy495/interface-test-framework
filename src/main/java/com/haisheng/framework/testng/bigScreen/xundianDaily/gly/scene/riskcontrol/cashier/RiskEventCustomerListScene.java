package com.haisheng.framework.testng.bigScreen.xundianDaily.gly.scene.riskcontrol.cashier;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 49.4. 订单涉及顾客
 *
 * @author wangmin
 * @date 2021-06-29 14:11:45
 */
@Builder
public class RiskEventCustomerListScene extends BaseScene {
    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final String map;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("map", map);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol/risk-control/cashier/risk-event/customer-list";
    }
}