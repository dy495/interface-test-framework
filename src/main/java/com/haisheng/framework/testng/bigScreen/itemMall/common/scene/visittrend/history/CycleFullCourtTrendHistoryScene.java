package com.haisheng.framework.testng.bigScreen.itemMall.common.scene.visittrend.history;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

@Builder
public class CycleFullCourtTrendHistoryScene extends  BaseScene {
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
    private final String mallId;


    private final String endTime;

    /**
     * 描述 到访趋势类型 人数='UV' 人次='PV'
     * 是否必填 false
     * 版本 -
     */
    private final String type;


    private final String startTime;




    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("mall_id", mallId);
        object.put("end_time", endTime);
        object.put("type", type);
        object.put("start_time", startTime);
        return object;
    }

    @Override
    public String getPath() {
        return "/mall/visit-trend/history/cycle/full-court/trend";
    }

}
