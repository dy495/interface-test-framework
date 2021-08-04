package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.packagemanage;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 套餐管理 -> 手机号查询客户信息
 *
 * @author wangmin
 * @date 2021/1/25 18:00
 */
@Builder
public class SearchCustomerScene extends BaseScene {
    private final String customerPhone;

    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("customer_phone", customerPhone);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/pc/package-manage/search-customer";
    }
}
