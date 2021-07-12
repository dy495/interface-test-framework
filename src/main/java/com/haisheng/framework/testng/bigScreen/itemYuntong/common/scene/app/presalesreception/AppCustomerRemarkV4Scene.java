package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.app.presalesreception;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 4.5. app接待编辑资料
 *
 * @author wangmin
 * @date 2021-03-31 13:03:23
 */
@Builder
public class AppCustomerRemarkV4Scene extends BaseScene {
    private final String id;
    private final String customerId;
    private final String shopId;
    private final String remark;

    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("shop_id", shopId);
        object.put("id", id);
        object.put("customer_id", customerId);
        object.put("remark", remark);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/m-app/pre-sales-reception/customer/remark-v4";
    }
}