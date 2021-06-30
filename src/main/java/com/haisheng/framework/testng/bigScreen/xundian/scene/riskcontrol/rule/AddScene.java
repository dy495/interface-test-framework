package com.haisheng.framework.testng.bigScreen.xundian.scene.riskcontrol.rule;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 51.2. 新增风控规则
 *
 * @author wangmin
 * @date 2021-06-29 14:11:45
 */
@Builder
public class AddScene extends BaseScene {
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
     * 描述 风控规则名称
     * 是否必填 true
     * 版本 -
     */
    private final String name;

    /**
     * 描述 规则详细
     * 是否必填 true
     * 版本 -
     */
    private final JSONObject rule;

    /**
     * 描述 门店类型
     * 是否必填 true
     * 版本 -
     */
    private final String shopType;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("appId", appId);
        object.put("name", name);
        object.put("rule", rule);
        object.put("shop_type", shopType);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol/risk-control/rule/add";
    }
}