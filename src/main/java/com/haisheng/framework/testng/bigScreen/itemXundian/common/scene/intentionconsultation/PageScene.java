package com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.intentionconsultation;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 1.1. 意向管理列表
 *
 * @author wangmin
 * @date 2021-06-29 14:11:44
 */
@Builder
public class PageScene extends BaseScene {
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
     * 描述 开始时间
     * 是否必填 false
     * 版本 -
     */
    private final String startTime;

    /**
     * 描述 结束时间
     * 是否必填 false
     * 版本 -
     */
    private final String endTime;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("appId", appId);
        object.put("page", page);
        object.put("size", size);
        object.put("start_time", startTime);
        object.put("end_time", endTime);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol/intention-consultation/page";
    }
}