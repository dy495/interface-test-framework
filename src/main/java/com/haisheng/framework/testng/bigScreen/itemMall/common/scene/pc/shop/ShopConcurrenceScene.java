package com.haisheng.framework.testng.bigScreen.itemMall.common.scene.pc.shop;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 2.3. 竞合分析
 *
 * @author wangmin
 * @date 2021-07-28 16:58:57
 */
@Builder
public class ShopConcurrenceScene extends BaseScene {
    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final String referer;

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
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final Long shopId;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("start_time", startTime);
        object.put("end_time", endTime);
        object.put("shop_id", shopId);
        return object;
    }

    @Override
    public String getPath() {
        return "/mall/pc/shop/concurrence";
    }
}