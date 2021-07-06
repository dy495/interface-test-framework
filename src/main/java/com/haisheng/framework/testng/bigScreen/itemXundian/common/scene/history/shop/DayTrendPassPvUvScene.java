package com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.history.shop;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 8.7. 获取到店趋势数据 天级别
 *
 * @author wangmin
 * @date 2021-06-29 14:11:44
 */
@Builder
public class DayTrendPassPvUvScene extends BaseScene {
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
        object.put("day", day);
        object.put("shop_id", shopId);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol/history/shop/day/trend-pass-pv-uv";
    }
}