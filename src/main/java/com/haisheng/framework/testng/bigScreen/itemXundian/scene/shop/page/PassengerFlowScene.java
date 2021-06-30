package com.haisheng.framework.testng.bigScreen.itemXundian.scene.shop.page;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.casedaily.wm.base.scene.BaseScene;
import lombok.Builder;

import java.util.List;

@Builder
public class PassengerFlowScene extends BaseScene {
    private final String districtCode;
    private final List<String> shopType;
    private final String shopName;
    private final String shopManager;
    @Builder.Default
    private  Integer page = 1;
    @Builder.Default
    private  Integer size = 10;

    @Override

    public JSONObject getRequestBody() {
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

    @Override
    public void setPage(Integer page) {
        this.page = page;
    }

    @Override
    public void setSize(Integer size) {
        this.size = size;
    }
}
