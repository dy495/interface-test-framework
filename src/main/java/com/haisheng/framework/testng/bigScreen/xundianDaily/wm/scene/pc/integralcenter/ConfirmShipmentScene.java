package com.haisheng.framework.testng.bigScreen.xundianDaily.wm.scene.pc.integralcenter;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 42.20. 确认发货
 *
 * @author wangmin
 * @date 2021-03-30 14:00:03
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
    public JSONObject getRequestBody(){
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("odd_numbers", oddNumbers);
        return object;
    }

    @Override
    public String getPath() {
        return "/shop/pc/integral-center/confirm_shipment";
    }
}