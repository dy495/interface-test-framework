package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.customermanager;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.scene.BaseScene;
import lombok.Builder;

@Builder
public class CustomerType extends BaseScene {

    @Override
    public JSONObject getJSONObject() {
        return new JSONObject();
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/customer-manage/pre-sale-customer/customer-type";
    }
}
