package com.haisheng.framework.testng.bigScreen.itemMall.common.scene.visittrend.history;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 7.4. 进出口/停车场到访趋势折线图
 *
 * @author wangmin
 * @date 2021-07-28 16:58:57
 */
@Builder
public class RegionTrendScene extends BaseScene {
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
     * 描述 到访趋势类型 进出口='EXIT' 停车场='PARKING'
     * 是否必填 false
     * 版本 -
     */
    private final String region;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("date", date);
        object.put("type", type);
        object.put("region", region);
        return object;
    }

    @Override
    public String getPath() {
        return "/mall/visit-trend/history/hour/region/trend";
    }
}