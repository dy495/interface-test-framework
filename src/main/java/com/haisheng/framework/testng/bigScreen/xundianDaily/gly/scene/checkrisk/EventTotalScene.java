package com.haisheng.framework.testng.bigScreen.xundianDaily.gly.scene.checkrisk;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 42.1. 巡店风控事件统计
 *
 * @author wangmin
 * @date 2021-06-29 14:11:44
 */
@Builder
public class EventTotalScene extends BaseScene {
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
     * 描述 门店id
     * 是否必填 false
     * 版本 -
     */
    private final String shopName;

    /**
     * 描述 事件排序类型
     * 是否必填 false
     * 版本 -
     */
    private final String eventType;
    private final Integer page;
    private final Integer  size;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("appId", appId);
        object.put("shop_name", shopName);
        object.put("event_type", eventType);
        object.put("page", page);
        object.put("size", size);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol/check-risk/event-total";
    }
}