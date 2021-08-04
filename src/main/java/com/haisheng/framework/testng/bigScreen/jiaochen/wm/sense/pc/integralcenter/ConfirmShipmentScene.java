package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.integralcenter;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 发货
 */
@Builder
public class ConfirmShipmentScene extends BaseScene {
    private final String oddNumbers;
    private final Long id;

    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("odd_numbers", oddNumbers);
        object.put("id", id);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/pc/integral-center/confirm_shipment";
    }
}
