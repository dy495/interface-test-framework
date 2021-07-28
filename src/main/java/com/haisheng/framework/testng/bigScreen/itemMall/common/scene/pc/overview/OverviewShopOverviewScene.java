package com.haisheng.framework.testng.bigScreen.itemMall.common.scene.pc.overview;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 9.3. 门店详情总览
 *
 * @author wangmin
 * @date 2021-07-28 16:58:57
 */
@Builder
public class OverviewShopOverviewScene extends BaseScene {
    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final String referer;

    /**
     * 描述 时间段
     * 是否必填 false
     * 版本 -
     */
    private final String time;

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
        object.put("time", time);
        object.put("shop_id", shopId);
        return object;
    }

    @Override
    public String getPath() {
        return "/mall/pc/overview/shop-overview";
    }
}