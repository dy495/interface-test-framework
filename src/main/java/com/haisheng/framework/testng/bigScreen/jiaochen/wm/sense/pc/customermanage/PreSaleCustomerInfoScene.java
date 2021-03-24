package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.customermanage;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 10.9. 售前客户详情(杨)v3.0的接口
 *
 * @author wangmin
 * @date 2021-03-24 14:32:26
 */
@Builder
public class PreSaleCustomerInfoScene extends BaseScene {
    /**
     * 描述 客户Id
     * 是否必填 true
     * 版本 v1.0
     */
    private final Long customerId;


    @Override
    public JSONObject getRequestBody(){
        JSONObject object = new JSONObject();
        object.put("customer_id", customerId);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/customer-manage/pre-sale-customer/info";
    }
}