package com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.history.shop;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 5.4. 获取到店趋势数据
 *
 * @author wangmin
 * @date 2021-07-30 10:55:36
 */
@Builder
public class ShopTrendPvUvScene extends BaseScene {
    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final String referer;

    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final String appId;

    /**
     * 描述 时间范围
     * 是否必填 false
     * 版本 -
     */
    private final String cycleType;

    /**
     * 描述 月份
     * 是否必填 false
     * 版本 -
     */
    private final String month;

    /**
     * 描述 日
     * 是否必填 false
     * 版本 -
     */
    private final String day;

    /**
     * 描述 门店id
     * 是否必填 false
     * 版本 -
     */
    private final Long shopId;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("appId", appId);
        object.put("cycle_type", cycleType);
        object.put("month", month);
        object.put("day", day);
        object.put("shop_id", shopId);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol/history/shop/trend-pv-uv";
    }
}