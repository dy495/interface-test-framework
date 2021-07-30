package com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.checkriskalarm;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 13.1. 告警详情
 *
 * @author wangmin
 * @date 2021-07-30 10:55:36
 */
@Builder
public class CheckRiskAlarmAlarmDetailScene extends BaseScene {
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


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("appId", appId);
        object.put("event_id", eventId);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol/check-risk-alarm/alarm-detail";
    }
}