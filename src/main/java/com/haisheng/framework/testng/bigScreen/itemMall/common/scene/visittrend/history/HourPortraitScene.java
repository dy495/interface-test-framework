package com.haisheng.framework.testng.bigScreen.itemMall.common.scene.visittrend.history;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 7.3. 饼状趋势图
 *
 * @author wangmin
 * @date 2021-07-28 16:58:57
 */
@Builder
public class HourPortraitScene extends BaseScene {
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
    private final String date;

    /**
     * 描述 到访趋势类型 进出口='EXIT' 停车场='PARKING' 楼层出入口='FLOOR'
     * 是否必填 false
     * 版本 -
     */
    private final String scene;

    private final Long floorId;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("date", date);
        object.put("scene", scene);
        object.put("floor_id", floorId);
        return object;
    }

    @Override
    public String getPath() {
        return "/mall/visit-trend/history/hour/portrait";
    }
}