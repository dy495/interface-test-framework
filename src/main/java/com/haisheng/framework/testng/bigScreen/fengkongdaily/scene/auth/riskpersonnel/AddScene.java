package com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.riskpersonnel;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.casedaily.wm.base.scene.BaseScene;
import lombok.Builder;

import java.util.List;

/**
 * 7.3. 特殊人员新增
 *
 * @author wangmin
 * @date 2021-04-01 14:22:36
 */
@Builder
public class AddScene extends BaseScene {
    /**
     * 描述 人物id
     * 是否必填 true
     * 版本 v1.0
     */
    private final String customerId;

    /**
     * 描述 人物id
     * 是否必填 true
     * 版本 v1.0
     */
    private final List<String> customerIds;

    /**
     * 描述 门店id
     * 是否必填 false
     * 版本 v1.0
     */
    private final Long shopId;

    /**
     * 描述 姓名
     * 是否必填 false
     * 版本 v1.0
     */
    private final String name;

    /**
     * 描述 添加原因
     * 是否必填 false
     * 版本 v1.0
     */
    private final String addReason;

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
        object.put("customer_ids", customerIds);
        object.put("shop_id", shopId);
        object.put("name", name);
        object.put("add_reason", addReason);
        object.put("type", type);
        return object;
    }

    @Override
    public String getPath() {
        return "/risk-control/auth/risk-personnel/add";
    }
}