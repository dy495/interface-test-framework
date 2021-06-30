package com.haisheng.framework.testng.bigScreen.itemXundian.scene.schedulerule;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.base.scene.BaseScene;
import lombok.Builder;

/**
 * 43.1. 新建定检规则
 *
 * @author wangmin
 * @date 2021-06-29 14:11:44
 */
@Builder
public class AddScene extends BaseScene {
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
     * 描述 定检规则名称
     * 是否必填 true
     * 版本 -
     */
    private final String name;

    /**
     * 描述 开始时间（需要整点小时）
     * 是否必填 true
     * 版本 -
     */
    private final String startTime;

    /**
     * 描述 结束时间（需要整点小时)
     * 是否必填 true
     * 版本 -
     */
    private final String endTime;

    /**
     * 描述 间隔小时
     * 是否必填 true
     * 版本 -
     */
    private final Integer intervalHour;

    /**
     * 描述 门店idList
     * 是否必填 true
     * 版本 -
     */
    private final JSONArray shopList;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("appId", appId);
        object.put("name", name);
        object.put("start_time", startTime);
        object.put("end_time", endTime);
        object.put("interval_hour", intervalHour);
        object.put("shop_list", shopList);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol/schedule-rule/add";
    }
}