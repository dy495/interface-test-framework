package com.haisheng.framework.testng.bigScreen.itemMall.common.scene.pc.region;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 6.4. 区域到访趋势折线图
 *
 * @author wangmin
 * @date 2021-09-01 17:31:17
 */
@Builder
public class RegionRegionTrendScene extends BaseScene {
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

    /**
     * 描述 区域id
     * 是否必填 false
     * 版本 -
     */
    private final String regionId;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("mall_id", mallId);
        object.put("type", type);
        object.put("start_time", startTime);
        object.put("end_time", endTime);
        object.put("region_id", regionId);
        return object;
    }

    @Override
    public String getPath() {
        return "/mall/pc/region/trend";
    }
}