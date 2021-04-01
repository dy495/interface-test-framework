package com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.shop;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 11.3. 门店详情
 *
 * @author wangmin
 * @date 2021-04-01 14:22:36
 */
@Builder
public class DetailScene extends BaseScene {
    /**
     * 描述 门店Id
     * 是否必填 false
     * 版本 v1.0
     */
    private final Long shopId;


    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("shop_id", shopId);
        return object;
    }

    @Override
    public String getPath() {
        return "/risk-control/auth/shop/detail";
    }
}