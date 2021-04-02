package com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.rule;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

import java.util.List;

/**
 * 8.3. 新增风控规则
 *
 * @author wangmin
 * @date 2021-04-01 14:22:36
 */
@Builder
public class AddScene extends BaseScene {
    /**
     * 描述 风控规则名称
     * 是否必填 true
     * 版本 v1.0
     */
    private final String name;

    /**
     * 描述 规则详细
     * 是否必填 true
     * 版本 -
     */
    private final JSONObject rule;

    /**
     * 描述 应用门店列表
     * 是否必填 true
     * 版本 -
     */
    private final List<String> shopIds;

    /**
     * 描述 业务类型
     * 是否必填 true
     * 版本 -
     */
    private final String businessType;


    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("name", name);
        object.put("rule", rule);
        object.put("shop_ids", shopIds);
        object.put("business_type", businessType);
        return object;
    }

    @Override
    public String getPath() {
        return "/risk-control/auth/rule/add";
    }
}