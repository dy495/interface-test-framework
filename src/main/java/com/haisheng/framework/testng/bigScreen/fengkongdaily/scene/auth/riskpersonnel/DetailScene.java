package com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.riskpersonnel;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 7.4. 特殊人员详情
 *
 * @author wangmin
 * @date 2021-04-01 14:22:36
 */
@Builder
public class DetailScene extends BaseScene {
    /**
     * 描述 人物id
     * 是否必填 true
     * 版本 v1.0
     */
    private final String customerId;

    /**
     * 描述 类型 (枚举值: BLACK or WHITE or FOCUS)
     * 是否必填 true
     * 版本 v1.0
     */
    private final String type;


    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("customer_id", customerId);
        object.put("type", type);
        return object;
    }

    @Override
    public String getPath() {
        return "/risk-control/auth/risk-personnel/detail";
    }
}