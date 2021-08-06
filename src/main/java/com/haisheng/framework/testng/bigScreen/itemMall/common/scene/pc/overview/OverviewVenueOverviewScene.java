package com.haisheng.framework.testng.bigScreen.itemMall.common.scene.pc.overview;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 9.1. 场馆客流总览
 *
 * @author wangmin
 * @date 2021-08-06 17:47:05
 */
@Builder
public class OverviewVenueOverviewScene extends BaseScene {
    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final String referer;

    /**
     * 描述 购物中心id
     * 是否必填 true
     * 版本 -
     */
    private final Long mallId;

    /**
     * 描述 查询开始日期
     * 是否必填 false
     * 版本 -
     */
    private final String startTime;

    /**
     * 描述 查询结束日期
     * 是否必填 false
     * 版本 -
     */
    private final String endTime;

    /**
     * 描述 查询开始日期
     * 是否必填 false
     * 版本 -
     */
    private final String date;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("mall_id", mallId);
        object.put("start_time", startTime);
        object.put("end_time", endTime);
        object.put("date", date);
        return object;
    }

    @Override
    public String getPath() {
        return "/mall/pc/overview/venue-overview";
    }
}