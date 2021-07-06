package com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.checkriskalarm;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 47.2. 告警异常图片列表
 *
 * @author wangmin
 * @date 2021-06-29 14:11:45
 */
@Builder
public class ErrorPicListScene extends BaseScene {
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
        return "/patrol/check-risk-alarm/error-pic-list";
    }
}