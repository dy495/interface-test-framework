package com.haisheng.framework.testng.bigScreen.crm.wm.scene.pc;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.scene.BaseScene;
import lombok.Builder;

/**
 * 订单客户分析车主年龄接口
 * 测试使用
 *
 * @author wangmin
 */
@Builder
public class Analysis2OrderWholeCountryScene extends BaseScene {

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
        return "/porsche/analysis2/order/whole-country";
    }

    @Override
    public String getIpPort() {
        return null;
    }
}
