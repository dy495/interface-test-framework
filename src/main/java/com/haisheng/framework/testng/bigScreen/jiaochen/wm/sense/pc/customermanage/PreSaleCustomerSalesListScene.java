package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.customermanage;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * /jiaochen/pc/customer-manage/pre-sale-customer/sales-list的接口
 *
 * @author wangmin
 * @date 2021-03-12 17:23:18
 */
@Builder
public class PreSaleCustomerSalesListScene extends BaseScene {
    /**
     * 描述 门店id
     * 是否必填 false
     * 版本 v2.0
     */
    private final Long shopId;


    @Override
    public JSONObject getRequestBody(){
        JSONObject object = new JSONObject();
        object.put("shop_id", shopId);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/customer-manage/pre-sale-customer/sales-list";
    }
}