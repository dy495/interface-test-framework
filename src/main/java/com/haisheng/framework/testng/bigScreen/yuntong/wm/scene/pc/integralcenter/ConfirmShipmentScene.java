package com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.pc.integralcenter;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.base.scene.BaseScene;
import lombok.Builder;

/**
 * 38.20. 确认发货 (张小龙) v2.0
 *
 * @author wangmin
 * @date 2021-05-18 17:04:36
 */
@Builder
public class ConfirmShipmentScene extends BaseScene {
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
        object.put("id", id);
        object.put("odd_numbers", oddNumbers);
        return object;
    }

    @Override
    public String getPath() {
        return "/yt/pc/integral-center/confirm_shipment";
    }
}