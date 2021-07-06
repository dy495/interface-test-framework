package com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.history.shop;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 8.8. 历史数据-区域图片
 *
 * @author wangmin
 * @date 2021-06-29 14:11:44
 */
@Builder
public class DayRegionMapScene extends BaseScene {
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
     * 描述 日期类型（NATURE_DAY or NATURE_WEEK）自然周需要传递一周的开始日期
     * 是否必填 false
     * 版本 -
     */
    private final String dateType;

    /**
     * 描述 门店id
     * 是否必填 true
     * 版本 -
     */
    private final Long shopId;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("appId", appId);
        object.put("day", day);
        object.put("date_type", dateType);
        object.put("shop_id", shopId);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol/history/shop/day/region-map";
    }
}