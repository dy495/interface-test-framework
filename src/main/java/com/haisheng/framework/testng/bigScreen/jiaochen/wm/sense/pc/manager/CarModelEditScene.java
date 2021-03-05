package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.manager;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 修改价格
 */
@Builder
public class CarModelEditScene extends BaseScene {
    private final Integer id;
    private final Double price;
    private final String status;

    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("price", price);
        object.put("status", status);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/manage/maintain/car-model/edit";
    }
}
