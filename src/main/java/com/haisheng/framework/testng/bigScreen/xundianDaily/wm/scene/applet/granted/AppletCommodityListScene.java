package com.haisheng.framework.testng.bigScreen.xundianDaily.wm.scene.applet.granted;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 小程序积分商城
 */
@Builder
public class AppletCommodityListScene extends BaseScene {
    private final Integer size;
    private final JSONObject lastValue;
    private final String integralSort;
    private final Boolean status;

    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("size", size);
        object.put("last_value", lastValue);
        object.put("integral_sort", integralSort);
        object.put("status", status);
        return object;
    }

    @Override
    public String getPath() {
        return "/shop/applet/granted/integral-mall/commodity-list";
    }

}