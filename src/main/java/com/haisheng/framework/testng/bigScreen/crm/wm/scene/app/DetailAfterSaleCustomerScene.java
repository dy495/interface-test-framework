package com.haisheng.framework.testng.bigScreen.crm.wm.scene.app;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 售后客户信息接口
 *
 * @author wangmin
 */
@Builder
public class DetailAfterSaleCustomerScene extends BaseScene {
    private final String afterRecordId;

    @Override
    public JSONObject getJSONObject() {
        JSONObject object = new JSONObject();
        object.put("after_record_id", afterRecordId);
        return object;
    }

    @Override
    public String getPath() {
        return "/porsche/app/after_sale/detail_after_sale_customer";
    }

    @Override
    public String getIpPort() {
        return null;
    }
}
