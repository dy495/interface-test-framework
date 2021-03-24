package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.customermanage;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 10.11. 售前变更所属销售 (杨)v3.0的接口
 *
 * @author wangmin
 * @date 2021-03-24 14:32:26
 */
@Builder
public class PreSaleCustomerChangeBelongSaleScene extends BaseScene {
    /**
     * 描述 客户Id
     * 是否必填 true
     * 版本 v3.0
     */
    private final Long customerId;

    /**
     * 描述 客户Id
     * 是否必填 true
     * 版本 v3.0
     */
    private final String saleId;


    @Override
    public JSONObject getRequestBody(){
        JSONObject object = new JSONObject();
        object.put("customer_id", customerId);
        object.put("sale_id", saleId);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/customer-manage/pre-sale-customer/change-belong-sale";
    }
}