package com.haisheng.framework.testng.bigScreen.crmDaily.wm.scene.pc;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crmDaily.wm.scene.BaseScene;
import lombok.Builder;

/**
 * 车主年龄/性别占比接口
 *
 * @author wangmin
 */
@Builder
public class Analysis2DealGenderAgeScene extends BaseScene {

    private final String month;
    private final String cycleType;
    private final String carType;

    @Override
    public JSONObject getJSONObject() {
        JSONObject object = new JSONObject();
        object.put("month", month);
        object.put("cycle_type", cycleType);
        object.put("car_type", carType);
        return object;
    }

    @Override
    public String getPath() {
        return "/porsche/analysis2/deal/gender-age";
    }

    @Override
    public String getIpPort() {
        return null;
    }
}
