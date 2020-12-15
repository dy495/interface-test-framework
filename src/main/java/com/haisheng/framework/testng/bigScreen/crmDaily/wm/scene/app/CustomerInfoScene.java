package com.haisheng.framework.testng.bigScreen.crmDaily.wm.scene.app;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crmDaily.wm.scene.BaseScene;
import lombok.Builder;

/**
 * 客户信息查询接口
 *
 * @author wangmin
 */
@Builder
public class CustomerInfoScene extends BaseScene {
    private final String customerId;

    @Override
    public JSONObject getJSONObject() {
        JSONObject object = new JSONObject();
        object.put("customer_id", customerId);
        return object;
    }

    @Override
    public String getPath() {
        return "/porsche/app/customer/info";
    }

    @Override
    public String getIpPort() {
        return null;
    }
}
