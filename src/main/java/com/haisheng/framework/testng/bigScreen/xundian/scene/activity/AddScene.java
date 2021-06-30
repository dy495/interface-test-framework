package com.haisheng.framework.testng.bigScreen.xundian.scene.activity;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 33.2. 添加活动列表
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
     * 描述 门店id
     * 是否必填 true
     * 版本 -
     */
    private final Long shopId;

    /**
     * 描述 活动描述
     * 是否必填 true
     * 版本 -
     */
    private final String activityDescription;

    /**
     * 描述 活动类型
     * 是否必填 true
     * 版本 -
     */
    private final String activityType;

    /**
     * 描述 开始时间
     * 是否必填 true
     * 版本 -
     */
    private final String startDate;

    /**
     * 描述 结束
     * 是否必填 true
     * 版本 -
     */
    private final String endDate;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("appId", appId);
        object.put("shop_id", shopId);
        object.put("activity_description", activityDescription);
        object.put("activity_type", activityType);
        object.put("start_date", startDate);
        object.put("end_date", endDate);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol/activity/add";
    }
}