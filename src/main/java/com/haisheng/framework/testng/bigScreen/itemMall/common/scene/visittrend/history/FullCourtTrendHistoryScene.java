package com.haisheng.framework.testng.bigScreen.itemMall.common.scene.visittrend.history;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 7.1. 全场到访趋势折线图
 *
 * @author wangmin
 * @date 2021-07-28 16:58:57
 */
@Builder
public class FullCourtTrendHistoryScene extends BaseScene {
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
     * 描述 到访趋势类型 人数='UV' 人次='PV'
     * 是否必填 false
     * 版本 -
     */
    private final String type;

    /**
     * 描述 到访趋势类型 场馆='VENUE' 楼层='FLOOR'
     * 是否必填 false
     * 版本 -
     */
    private final String scene;

    /**
     * 描述 楼层Id sence = 'FLOOR' 则需要传入floor_id
     * 是否必填 false
     * 版本 -
     */
    private final Long floorId;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("date", date);
        object.put("type", type);
        object.put("scene", scene);
        object.put("floor_id", floorId);
        return object;
    }

    @Override
    public String getPath() {
        return "/mall/visit-trend/history/hour/full-court/trend";
    }
}