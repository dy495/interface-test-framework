package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.customermanager;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

@Builder
public class RepairPageScene extends BaseScene {
    @Builder.Default
    private final Integer size = 10;
    @Builder.Default
    private final Integer page = 1;
    private final String carId;
    private final String shopId;

    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("size", size);
        object.put("page", page);
        object.put("car_id", carId);
        object.put("shop_id", shopId);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/customer-manage/after-sale-customer/repair-page";
    }
}
