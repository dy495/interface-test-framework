package com.haisheng.framework.testng.bigScreen.xundianDaily.hqq.sence.pc;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.scene.BaseScene;
import lombok.Builder;

import java.util.List;

@Builder
public class PassengerFlow extends BaseScene {
    private final String districtCode;
    private final List<String> shopType;
    private final String shopName;
    private final String shopManager;
    @Builder.Default
    private final Integer page = 1;
    @Builder.Default
    private final Integer size = 10;

    @Override

    public JSONObject getJSONObject() {
        JSONObject object = new JSONObject();
        object.put("district_code", districtCode);
        object.put("shop_type", shopType);
        object.put("shop_name", shopName);
        object.put("shop_manager", shopManager);
        object.put("page", page);
        object.put("size", size);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol/shop/page/passenger-flow";
    }
}
