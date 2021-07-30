package com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.member.consumption;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 40.2. 订单详情 lj
 *
 * @author wangmin
 * @date 2021-07-30 10:55:36
 */
@Builder
public class ConsumptionDetailScene extends BaseScene {
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
     * 描述 订单编号
     * 是否必填 false
     * 版本 -
     */
    private final String orderNumber;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("appId", appId);
        object.put("order_number", orderNumber);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol/member/consumption/detail";
    }
}