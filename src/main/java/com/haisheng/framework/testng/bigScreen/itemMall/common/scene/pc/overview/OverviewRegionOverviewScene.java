package com.haisheng.framework.testng.bigScreen.itemMall.common.scene.pc.overview;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 7.4. 场景区域详情概览
 *
 * @author wangmin
 * @date 2021-09-01 17:31:17
 */
@Builder
public class OverviewRegionOverviewScene extends BaseScene {
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
     * 描述 查询区域id
     * 是否必填 false
     * 版本 -
     */
    private final String regionId;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("mall_id", mallId);
        object.put("start_time", startTime);
        object.put("end_time", endTime);
        object.put("region_id", regionId);
        return object;
    }

    @Override
    public String getPath() {
        return "/mall/pc/overview/region-overview";
    }
}