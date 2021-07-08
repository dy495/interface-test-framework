package com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.pc.shop;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 28.7. 查看门店洗车二维码(池)
 *
 * @author wangmin
 * @date 2021-05-18 17:04:36
 */
@Builder
public class ShowShopExtensionScene extends BaseScene {
    /**
     * 描述 门店id
     * 是否必填 false
     * 版本 v2.0
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
        return "/account-platform/auth/shop/show-shop-extension";
    }
}