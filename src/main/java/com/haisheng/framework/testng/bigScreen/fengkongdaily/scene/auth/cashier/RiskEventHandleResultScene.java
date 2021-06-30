package com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.cashier;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.casedaily.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 6.6. 风控事件详情
 *
 * @author wangmin
 * @date 2021-04-01 14:22:36
 */
@Builder
public class RiskEventHandleResultScene extends BaseScene {
    /**
     * 描述 风控事件Id
     * 是否必填 false
     * 版本 
     */
    private final Long id;


    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("id", id);
        return object;
    }

    @Override
    public String getPath() {
        return "/risk-control/auth/cashier/risk-event/handle-result";
    }
}