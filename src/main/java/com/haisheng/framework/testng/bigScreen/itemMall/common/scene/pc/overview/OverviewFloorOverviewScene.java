package com.haisheng.framework.testng.bigScreen.itemMall.common.scene.pc.overview;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 9.2. 楼层客流总览
 *
 * @author wangmin
 * @date 2021-07-28 16:58:57
 */
@Builder
public class OverviewFloorOverviewScene extends BaseScene {
    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final String referer;

    /**
     * 描述 按时间查询类型(DAY天，WEEK周，MONTH月，CYCLE自定义)
     * 是否必填 false
     * 版本 -
     */
    private final String timeType;

    /**
     * 描述 日期/周/月份/自定义周期
     * 是否必填 false
     * 版本 -
     */
    private final String time;

    /**
     * 描述 楼层id
     * 是否必填 false
     * 版本 -
     */
    private final Long floorId;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("time_type", timeType);
        object.put("time", time);
        object.put("floor_id", floorId);
        return object;
    }

    @Override
    public String getPath() {
        return "/mall/pc/overview/floor-overview";
    }
}