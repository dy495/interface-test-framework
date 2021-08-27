package com.haisheng.framework.testng.bigScreen.itemMall.common.scene.auth.shop;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 3.3. 门店详情
 *
 * @author wangmin
 * @date 2021-08-26 16:28:32
 */
@Builder
public class ShopDetailScene extends BaseScene {
    /**
     * 描述 门店Id
     * 是否必填 false
     * 版本 v1.0
     */
    private final Long shopId;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("shop_id", shopId);
        return object;
    }

    @Override
    public String getPath() {
        return "/account-platform/auth/shop/detail";
    }
}