package com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.checkrisk.events;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 44.1. 事件反馈列表
 *
 * @author wangmin
 * @date 2021-06-29 14:11:45
 */
@Builder
public class FeedListScene extends BaseScene {
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
        return "/patrol/check-risk/events/feed-list";
    }
}