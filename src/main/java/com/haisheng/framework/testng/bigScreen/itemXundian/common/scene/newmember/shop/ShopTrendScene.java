package com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.newmember.shop;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 8.5. 新增顾客-门店详情-首页趋势图
 *
 * @author wangmin
 * @date 2021-07-30 10:55:36
 */
@Builder
public class ShopTrendScene extends BaseScene {
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
        return "/patrol/new_member/shop/trend";
    }
}