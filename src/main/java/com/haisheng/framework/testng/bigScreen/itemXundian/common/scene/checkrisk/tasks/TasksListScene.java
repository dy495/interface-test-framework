package com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.checkrisk.tasks;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 17.1. 获取门店巡店记录列表
 *
 * @author wangmin
 * @date 2021-07-30 10:55:36
 */
@Builder
public class TasksListScene extends BaseScene {
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
     * 描述 触发规则
     * 是否必填 false
     * 版本 -
     */
    private final String triggerRule;

    /**
     * 描述 触发日期开始
     * 是否必填 false
     * 版本 -
     */
    private final String startTime;

    /**
     * 描述 触发日期结束
     * 是否必填 false
     * 版本 -
     */
    private final String endTime;

    /**
     * 描述 事件状态
     * 是否必填 false
     * 版本 -
     */
    private final String eventState;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("appId", appId);
        object.put("page", page);
        object.put("size", size);
        object.put("trigger_rule", triggerRule);
        object.put("start_time", startTime);
        object.put("end_time", endTime);
        object.put("event_state", eventState);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol/check-risk/tasks/list";
    }
}