package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.shop;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 33.7. 查看门店洗车二维码(池)
 *
 * @author wangmin
 * @date 2021-03-31 12:47:27
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
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("shop_id", shopId);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/pc/shop/show-shop-extension";
    }
}