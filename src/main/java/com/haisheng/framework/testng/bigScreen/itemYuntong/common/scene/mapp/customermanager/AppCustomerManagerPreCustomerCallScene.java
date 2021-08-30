package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.mapp.customermanager;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 3.2. app销售客户列表-一键外呼（华成裕）v7
 *
 * @author wangmin
 * @date 2021-08-30 14:35:38
 */
@Builder
public class AppCustomerManagerPreCustomerCallScene extends BaseScene {
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
        return "/car-platform/m-app/customer-manager/pre-customer-call";
    }
}