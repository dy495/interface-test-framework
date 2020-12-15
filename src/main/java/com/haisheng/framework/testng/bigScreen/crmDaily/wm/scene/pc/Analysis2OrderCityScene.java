package com.haisheng.framework.testng.bigScreen.crmDaily.wm.scene.pc;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crmDaily.wm.scene.BaseScene;
import lombok.Builder;

/**
 * 市订车量接口
 *
 * @author wangmin
 */
@Builder
public class Analysis2OrderCityScene extends BaseScene {

    private final String month;
    private final String cycleType;
    private final String carType;
    private final long adCode;

    @Override
    public JSONObject getJSONObject() {
        JSONObject object = new JSONObject();
        object.put("month", month);
        object.put("cycle_type", cycleType);
        object.put("car_type", carType);
        object.put("adcode", adCode);
        return object;
    }

    @Override
    public String getPath() {
        return "/porsche/analysis2/order/city";
    }

    @Override
    public String getIpPort() {
        return null;
    }
}
