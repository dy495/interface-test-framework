package com.haisheng.framework.testng.bigScreen.itemPorsche.scene.pc;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.base.scene.BaseScene;
import lombok.Builder;

/**
 * 订车车主类型占比接口
 *
 * @author wangmin
 */
@Builder
public class Analysis2OrderCarOwnerScene extends BaseScene {

    private final String month;
    private final String cycleType;
    private final String carType;

    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("month", month);
        object.put("cycle_type", cycleType);
        object.put("car_type", carType);
        return object;
    }

    @Override
    public String getPath() {
        return "/porsche/analysis2/order/car-owner";
    }

    @Override
    public String getIpPort() {
        return null;
    }
}
