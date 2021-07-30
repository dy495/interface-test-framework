package com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.riskcontrol.rule;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 48.3. 风控规则列表
 *
 * @author wangmin
 * @date 2021-07-30 10:55:36
 */
@Builder
public class RulePageScene extends BaseScene {
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
     * 描述 当前页
     * 是否必填 true
     * 版本 -
     */
    private final Integer page;

    /**
     * 描述 当前页的数量
     * 是否必填 true
     * 版本 -
     */
    private final Integer size;

    /**
     * 描述 风控规则名称
     * 是否必填 false
     * 版本 -
     */
    private final String name;

    /**
     * 描述 规则类型
     * 是否必填 false
     * 版本 -
     */
    private final String type;

    /**
     * 描述 门店类型
     * 是否必填 false
     * 版本 -
     */
    private final String shopType;

    /**
     * 描述 状态 1开启 0关闭
     * 是否必填 false
     * 版本 -
     */
    private final Integer status;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("appId", appId);
        object.put("page", page);
        object.put("size", size);
        object.put("name", name);
        object.put("type", type);
        object.put("shop_type", shopType);
        object.put("status", status);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol/risk-control/rule/page";
    }
}