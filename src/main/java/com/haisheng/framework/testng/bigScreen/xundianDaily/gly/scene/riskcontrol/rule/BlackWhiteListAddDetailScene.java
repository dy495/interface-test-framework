package com.haisheng.framework.testng.bigScreen.xundianDaily.gly.scene.riskcontrol.rule;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 48.4. 黑白名单-新增页面顾客详情(黑白名单共用)
 *
 * @author wangmin
 * @date 2021-06-29 14:11:45
 */
@Builder
public class BlackWhiteListAddDetailScene extends BaseScene {
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
     * 描述 No comments found.
     * 是否必填 true
     * 版本 -
     */
    private final String type;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("appId", appId);
        object.put("customer_id", customerId);
        object.put("type", type);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol/risk-control/rule/black-white-list/add/detail";
    }
}