package com.haisheng.framework.testng.bigScreen.itemMall.common.scene.visittrend.history;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 7.2. 客群画像
 *
 * @author wangmin
 * @date 2021-08-06 17:47:04
 */
@Builder
public class CycleCustomersPortraitScene extends BaseScene {
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


    private final String startTime;
    private final String endTime;

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
    private final long floorId;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("mall_id", mallId);
        object.put("start_time", startTime);
        object.put("end_time", endTime);
        object.put("scene", scene);
        object.put("floor_id", floorId);
        return object;
    }

    @Override
    public String getPath() {
        return "/mall/visit-trend/history/cycle/customers/portrait";
    }
}