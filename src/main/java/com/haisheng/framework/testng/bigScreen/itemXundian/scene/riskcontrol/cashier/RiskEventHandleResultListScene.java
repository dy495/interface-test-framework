package com.haisheng.framework.testng.bigScreen.itemXundian.scene.riskcontrol.cashier;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.base.scene.BaseScene;
import lombok.Builder;

/**
 * 49.7. 风控事件处理结果枚举
 *
 * @author wangmin
 * @date 2021-06-29 14:11:45
 */
@Builder
public class RiskEventHandleResultListScene extends BaseScene {
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


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("appId", appId);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol/risk-control/cashier/risk-event/handle-result/list";
    }
}