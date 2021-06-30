package com.haisheng.framework.testng.bigScreen.itemPorsche.scene.pc;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.base.scene.BaseScene;
import lombok.Builder;

/**
 * 全国成交量接口
 *
 * @author wangmin
 */
@Builder
public class Analysis2DealWholeCountryScene extends BaseScene {

    private final String month;
    private final String cycleType;
    private final String carType;

    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("cycle_type", cycleType);
        object.put("month", month);
        object.put("car_type", carType);
        return object;
    }

    @Override
    public String getPath() {
        return "/porsche/analysis2/deal/whole-country";
    }

    @Override
    public String getIpPort() {
        return null;
    }
}
