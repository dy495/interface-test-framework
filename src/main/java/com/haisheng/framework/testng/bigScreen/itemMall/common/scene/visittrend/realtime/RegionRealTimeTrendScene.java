package com.haisheng.framework.testng.bigScreen.itemMall.common.scene.visittrend.realtime;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 1.5. 实时进出口/停车场/楼层到访趋势折线图
 *
 * @author wangmin
 * @date 2021-08-06 17:47:04
 */
@Builder
public class RegionRealTimeTrendScene extends BaseScene {
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
     * 描述 到访趋势类型 人数='UV' 人次='PV'
     * 是否必填 false
     * 版本 -
     */
    private final String type;

    /**
     * 描述 到访趋势类型 进出口='EXIT' 停车场='PARKING' 楼层='FLOOR'
     * 是否必填 false
     * 版本 -
     */
    private final String region;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("mall_id", mallId);
        object.put("type", type);
        object.put("region", region);
        return object;
    }

    @Override
    public String getPath() {
        return "/mall/visit-trend/real-time/region/trend";
    }
}