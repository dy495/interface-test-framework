package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.granted;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * @author wangmin
 * @date 2021/1/29 12:09
 */
@Builder
public class MaintainShopListScene extends BaseScene {
    private final String carId;

    @Override
    public JSONObject getRequest() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("car_id", carId);
        return jsonObject;
    }

    @Override
    public String getPath() {
        return "/jiaochen/applet/granted/maintain/shop/list";
    }
}
