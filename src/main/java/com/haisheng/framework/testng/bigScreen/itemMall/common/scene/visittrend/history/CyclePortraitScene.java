package com.haisheng.framework.testng.bigScreen.itemMall.common.scene.visittrend.history;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 8.3. 饼状趋势图
 *
 * @author wangmin
 * @date 2021-07-28 16:58:57
 */
@Builder
public class CyclePortraitScene extends BaseScene {
    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final String referer;

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
     * 描述 到访趋势类型 进出口='EXIT' 停车场='PARKING' 楼层出入口='FLOOR'
     * 是否必填 false
     * 版本 -
     */
    private final String region;

    /**
     * 描述 楼层Id region = FLOOR 时需要传入floor_id
     * 是否必填 false
     * 版本 -
     */
    private final String floorId;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("start_time", startTime);
        object.put("end_time", endTime);
        object.put("region", region);
        object.put("floor_id", floorId);
        return object;
    }

    @Override
    public String getPath() {
        return "/mall/visit-trend/history/cycle/portrait";
    }
}