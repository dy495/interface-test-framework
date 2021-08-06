package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.mapp.presalesreception;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 9.14. 接待详情（杨）v4.0 （2021-05-08）
 *
 * @author wangmin
 * @date 2021-08-06 16:38:23
 */
@Builder
public class AppPreSalesReceptionDetailV4Scene extends BaseScene {
    /**
     * 描述 接待id
     * 是否必填 true
     * 版本 v3.0
     */
    private final Long id;

    /**
     * 描述 顾客Id
     * 是否必填 true
     * 版本 v4.0
     */
    private final Long customerId;

    /**
     * 描述 门店id
     * 是否必填 true
     * 版本 v3.0
     */
    private final Long shopId;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("customer_id", customerId);
        object.put("shop_id", shopId);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/m-app/pre-sales-reception/detail-v4";
    }
}