package com.haisheng.framework.testng.bigScreen.xundianDaily.gly.scene.checkrisk;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 42.3. 巡店风控事件统计-事件排序列表 (list{{"已处理事件"，"HANDLE"}，{"待处理事件"，"PENDING"}})
 *
 * @author wangmin
 * @date 2021-06-29 14:11:44
 */
@Builder
public class EventSortListScene extends BaseScene {
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
        return "/patrol/check-risk/event-sort-list";
    }
}