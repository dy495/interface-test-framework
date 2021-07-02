package com.haisheng.framework.testng.bigScreen.itemXundian.scene.checkriskalarm;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.base.scene.BaseScene;
import lombok.Builder;

/**
 * 47.3. 处理巡店风控事件告警
 *
 * @author wangmin
 * @date 2021-06-29 14:11:45
 */
@Builder
public class HandleScene extends BaseScene {
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
     * 描述 事件id
     * 是否必填 false
     * 版本 -
     */
    private final Long eventId;
    private final String eventStatus;

    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("appId", appId);
        object.put("event_id", eventId);
        object.put("event_status", eventStatus);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol/check-risk-alarm/handle";
    }
}