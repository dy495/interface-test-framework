package com.haisheng.framework.testng.bigScreen.itemXundian.scene.history.shop;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.casedaily.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 8.15. 获取到店趋势数据 天级别-多店
 *
 * @author wangmin
 * @date 2021-06-29 14:11:44
 */
@Builder
public class AllDayQueryEntrysPvUvScene extends BaseScene {
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
     * 描述 winsense门店id
     * 是否必填 false
     * 版本 -
     */
    private final Long winsenseShopId;

    /**
     * 描述 业务方门店id（优先使用业务方门店id，其次使用winsense门店id，如果两个id都没有则报错）
     * 是否必填 false
     * 版本 -
     */
    private final String ownerShopId;

    /**
     * 描述 按时间查询类型(HOUR(小时);DAY(天);WEEK(周);MONTH(月);QUARTER(季度);YEAR(年))
     * 是否必填 false
     * 版本 -
     */
    private final String timeType;

    /**
     * 描述 如果查询某一天的数据，必须对应time_type为HOUR（返回的是以小时为单位的数据） 如果查询多天的数据，必须对应time_type为DAY（返回的是以天为单位的数据）
     * 是否必填 false
     * 版本 -
     */
    private final JSONArray day;

    /**
     * 描述 按周查的该周的开始日期List（必须对应time_type为WEEK,返回的是以周为单位的数据）
     * 是否必填 false
     * 版本 -
     */
    private final JSONArray week;

    /**
     * 描述 按月查的月份List（必须对应time_type为MONTH,返回的是以月为单位的数据）
     * 是否必填 false
     * 版本 -
     */
    private final JSONArray month;

    /**
     * 描述 按季度查的季度开始日期List（必须对应time_type为QUARTER,返回的是以季度为单位的数据）
     * 是否必填 false
     * 版本 -
     */
    private final JSONArray quarter;

    /**
     * 描述 按年查的年的开始日期List（必须对应time_type为YEAR,返回的是以年为单位的数据）
     * 是否必填 false
     * 版本 -
     */
    private final JSONArray year;

    /**
     * 描述 是否查询门店列表
     * 是否必填 false
     * 版本 -
     */
    private final Integer isQueryShopList;

    /**
     * 描述 版本号
     * 是否必填 false
     * 版本 -
     */
    private final String version;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("appId", appId);
        object.put("winsense_shop_id", winsenseShopId);
        object.put("owner_shop_id", ownerShopId);
        object.put("time_type", timeType);
        object.put("day", day);
        object.put("week", week);
        object.put("month", month);
        object.put("quarter", quarter);
        object.put("year", year);
        object.put("is_query_shop_list", isQueryShopList);
        object.put("version", version);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol/history/shop/all/day/QUERY_ENTRYS_PV_UV";
    }
}