package com.haisheng.framework.testng.bigScreen.itemXundian.scene.checkrisk.tasks;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.base.scene.BaseScene;
import lombok.Builder;

/**
 * 38.1. 获取门店巡店记录列表
 *
 * @author wangmin
 * @date 2021-06-29 14:11:44
 */
@Builder
public class ListScene extends BaseScene {
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
     * 描述 触发规则
     * 是否必填 false
     * 版本 -
     */
    private final String triggerRule;

    /**
     * 描述 触发日期
     * 是否必填 false
     * 版本 -
     */
    private final String triggerTime;

    /**
     * 描述 事件状态
     * 是否必填 false
     * 版本 -
     */
    private final String eventState;
    private final Integer page;
    private final Integer  size;
    private final Long shopId;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("appId", appId);
        object.put("trigger_rule", triggerRule);
        object.put("trigger_time", triggerTime);
        object.put("event_state", eventState);
        object.put("page", page);
        object.put("size", size);
        object.put("shop_id", shopId);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol/check-risk/tasks/list";
    }
}