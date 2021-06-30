package com.haisheng.framework.testng.bigScreen.xundianDaily.gly.scene.history.shop;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 8.1. 客群漏斗
 *
 * @author wangmin
 * @date 2021-06-29 14:11:44
 */
@Builder
public class ConversionScene extends BaseScene {
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
     * 描述 月
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
        return "/patrol/history/shop/conversion";
    }
}