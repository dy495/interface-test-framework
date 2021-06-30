package com.haisheng.framework.testng.bigScreen.xundianDaily.gly.scene.checkrisk.tasks;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 38.2. 获取门店触发规则列表
 *
 * @author wangmin
 * @date 2021-06-29 14:11:44
 */
@Builder
public class TriggerRuleListScene extends BaseScene {
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
        return "/patrol/check-risk/tasks/trigger-rule-list";
    }
}