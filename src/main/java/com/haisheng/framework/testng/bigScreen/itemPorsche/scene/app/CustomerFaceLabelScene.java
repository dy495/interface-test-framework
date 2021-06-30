package com.haisheng.framework.testng.bigScreen.itemPorsche.scene.app;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.base.scene.BaseScene;
import lombok.Builder;

/**
 * 客户人脸以及标签信息查询接口
 *
 * @author wangmin
 */
@Builder
public class CustomerFaceLabelScene extends BaseScene {
    private final String customerId;

    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("customer_id", customerId);
        return object;
    }

    @Override
    public String getPath() {
        return "/porsche/app/customer/face-label";
    }

    @Override
    public String getIpPort() {
        return null;
    }
}
