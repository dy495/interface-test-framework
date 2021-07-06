package com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.schedulerule;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 43.4. 删除定检规则
 *
 * @author wangmin
 * @date 2021-06-29 14:11:45
 */
@Builder
public class DeleteScene extends BaseScene {
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
     * 描述 定检规则id
     * 是否必填 true
     * 版本 -
     */
    private final JSONArray ruleIds;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("appId", appId);
        object.put("rule_ids", ruleIds);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol/schedule-rule/delete";
    }
}