package com.haisheng.framework.testng.bigScreen.itemXundian.scene.history.shop;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.base.scene.BaseScene;
import lombok.Builder;

/**
 * 8.13. 到店趋势数据-多店
 *
 * @author wangmin
 * @date 2021-06-29 14:11:44
 */
@Builder
public class AllTrendPvUvScene extends BaseScene {
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


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("appId", appId);
        object.put("cycle_type", cycleType);
        object.put("month", month);
        object.put("day", day);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol/history/shop/all/trend-pv-uv";
    }
}