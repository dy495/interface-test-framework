package com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.pc.integralcenter;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 37.20. 确认发货
 *
 * @author wangmin
 * @date 2021-07-30 10:55:36
 */
@Builder
public class IntegralCenterConfirmShipmentScene extends BaseScene {
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
     * 描述 订单ID
     * 是否必填 false
     * 版本 v2.0
     */
    private final Long id;

    /**
     * 描述 物流单号
     * 是否必填 true
     * 版本 v2.0
     */
    private final String oddNumbers;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("appId", appId);
        object.put("id", id);
        object.put("odd_numbers", oddNumbers);
        return object;
    }

    @Override
    public String getPath() {
        return "/shop/pc/integral-center/confirm_shipment";
    }
}