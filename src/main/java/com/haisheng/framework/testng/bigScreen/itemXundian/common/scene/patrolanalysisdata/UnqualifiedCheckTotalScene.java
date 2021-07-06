package com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.patrolanalysisdata;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 41.3. 巡店不合格项趋势
 *
 * @author wangmin
 * @date 2021-06-29 14:11:44
 */
@Builder
public class UnqualifiedCheckTotalScene extends BaseScene {
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
     * 描述 最近多少天
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
        object.put("shop_id", shopId);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol/patrol-analysis-data/unqualified-check-total";
    }
}