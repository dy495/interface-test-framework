package com.haisheng.framework.testng.bigScreen.crm.wm.scene.app;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.scene.BaseScene;
import lombok.Builder;

/**
 * 客户呃购车信息接口
 *
 * @author wangmin
 */
@Builder
public class CustomerBuyCarListScene extends BaseScene {
    private final String customerId;

    @Override
    public JSONObject getJSONObject() {
        JSONObject object = new JSONObject();
        object.put("customer_id", customerId);
        return object;
    }

    @Override
    public String getPath() {
        return "/porsche/app/customer/buy-car-list";
    }

    @Override
    public String getIpPort() {
        return null;
    }
}
