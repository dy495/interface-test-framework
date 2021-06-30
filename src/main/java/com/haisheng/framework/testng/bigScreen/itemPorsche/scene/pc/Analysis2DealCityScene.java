package com.haisheng.framework.testng.bigScreen.itemPorsche.scene.pc;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.casedaily.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 市成交量接口
 *
 * @author wangmin
 */
@Builder
public class Analysis2DealCityScene extends BaseScene {

    private final String month;
    private final String cycleType;
    private final String carType;
    private final long adCode;

    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("month", month);
        object.put("cycle_type", cycleType);
        object.put("car_type", carType);
        object.put("adcode", adCode);
        return object;
    }

    @Override
    public String getPath() {
        return "/porsche/analysis2/deal/city";
    }

    @Override
    public String getIpPort() {
        return null;
    }
}
