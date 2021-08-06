package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.mapp.customermanager;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 1.2. app销售客户详情（杨）v4.0（2021-05-06)
 *
 * @author wangmin
 * @date 2021-08-06 16:38:23
 */
@Builder
public class AppCustomerManagerPreCustomerInfoScene extends BaseScene {
    /**
     * 描述 门店Id
     * 是否必填 true
     * 版本 v4.0
     */
    private final Long shopId;

    /**
     * 描述 顾客Id
     * 是否必填 true
     * 版本 v4.0
     */
    private final Long customerId;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("shop_id", shopId);
        object.put("customer_id", customerId);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/m-app/customer-manager/pre-customer-info";
    }
}