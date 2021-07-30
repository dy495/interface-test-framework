package com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.checkriskalarm;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 13.3. 处理巡店风控事件告警
 *
 * @author wangmin
 * @date 2021-07-30 10:55:36
 */
@Builder
public class CheckRiskAlarmHandleScene extends BaseScene {
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

    /**
     * 描述 事件处理状态 （READY_CONFIRM待告警确认 RISK_CONFIRM告警已确认 NOT_HANDLE无需处理）
     * 是否必填 false
     * 版本 -
     */
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