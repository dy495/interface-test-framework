package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.manager;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.scene.BaseScene;
import lombok.Builder;

/**
 * 保养配置分页
 */
@Builder
public class CarModelPage extends BaseScene {
    private final Integer page;
    private final Integer size;
    private final String brandName;
    private final String manufacturer;
    private final String carModel;
    private final String year;

    @Override
    public JSONObject getJSONObject() {
        JSONObject object = new JSONObject();
        object.put("page", page);
        object.put("size", size);
        object.put("brand_name", brandName);
        object.put("manufacturer", manufacturer);
        object.put("car_model", carModel);
        object.put("year", year);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/manage/maintain/car-model/page";
    }

    @Override
    public String getIpPort() {
        return null;
    }
}
