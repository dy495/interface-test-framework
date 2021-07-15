package com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.checkrisk.events;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 45.2. 事件处理
 *
 * @author wangmin
 * @date 2021-07-14 14:30:22
 */
@Builder
public class HandEventScene extends BaseScene {
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
     * 描述 触发规则名称
     * 是否必填 false
     * 版本 -
     */
    private final String triggerRule;

    /**
     * 描述 门店id
     * 是否必填 false
     * 版本 -
     */
    private final Long shopId;

    /**
     * 描述 设备id
     * 是否必填 false
     * 版本 -
     */
    private final String deviceId;

    /**
     * 描述 照片url
     * 是否必填 false
     * 版本 -
     */
    private final String picUrl;

    /**
     * 描述 触发时间
     * 是否必填 false
     * 版本 -
     */
    private final Long time;

    /**
     * 描述 事件状态
     * 是否必填 false
     * 版本 -
     */
    private final String eventStatus;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("appId", appId);
        object.put("trigger_rule", triggerRule);
        object.put("shop_id", shopId);
        object.put("device_id", deviceId);
        object.put("pic_url", picUrl);
        object.put("time", time);
        object.put("event_status", eventStatus);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol/check-risk/events/hand-event";
    }
}