package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.customermanage;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 6.10. 售前客户详情(杨)v3.0
 *
 * @author wangmin
 * @date 2021-03-31 12:32:56
 */
@Builder
public class PreSaleCustomerInfoScene extends BaseScene {
    /**
     * 描述 门店Id
     * 是否必填 true
     * 版本 v3.0
     */
    private final Long shopId;

    /**
     * 描述 客户Id
     * 是否必填 true
     * 版本 v3.0
     */
    private final Long customerId;


    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("shopId", shopId);
        object.put("customerId", customerId);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/pc/customer-manage/pre-sale-customer/info";
    }
}