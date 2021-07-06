package com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.riskcontrol.rule;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 48.5. 黑白名单-新增黑白名单(黑白名单共用)
 *
 * @author wangmin
 * @date 2021-06-29 14:11:45
 */
@Builder
public class BlackWhiteListAddScene extends BaseScene {
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
     * 描述 人物id
     * 是否必填 true
     * 版本 门店 v4.1
     */
    private final String customerId;

    /**
     * 描述 人物id
     * 是否必填 true
     * 版本 门店 v4.1
     */
    private final JSONArray customerIds;

    /**
     * 描述 门店id
     * 是否必填 false
     * 版本 门店 v4.1
     */
    private final Long shopId;

    /**
     * 描述 姓名
     * 是否必填 false
     * 版本 门店 v4.1
     */
    private final String name;

    /**
     * 描述 添加原因
     * 是否必填 false
     * 版本 门店 v4.1
     */
    private final String addReason;

    /**
     * 描述 类型 (枚举值: BLACK or WHITE)
     * 是否必填 true
     * 版本 门店v4.1
     */
    private final String type;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("appId", appId);
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
        return "/patrol/risk-control/rule/black-white-list/add";
    }
}