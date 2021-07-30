package com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.checkriskalarm;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 13.2. 告警异常图片列表
 *
 * @author wangmin
 * @date 2021-07-30 10:55:36
 */
@Builder
public class CheckRiskAlarmErrorPicListScene extends BaseScene {
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
        object.put("page", page);
        object.put("size", size);
        object.put("event_id", eventId);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol/check-risk-alarm/error-pic-list";
    }
}