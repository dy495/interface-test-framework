package com.haisheng.framework.testng.bigScreen.itemXundian.scene.member.consumption;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.casedaily.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 10.2. 订单详情 lj
 *
 * @author wangmin
 * @date 2021-06-29 14:11:44
 */
@Builder
public class DetailScene extends BaseScene {
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