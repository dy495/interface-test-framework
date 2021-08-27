package com.haisheng.framework.testng.bigScreen.itemMall.common.scene.auth.shop;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 3.1. 权限门店列表
 *
 * @author wangmin
 * @date 2021-08-26 16:28:32
 */
@Builder
public class ShopListScene extends BaseScene {
    /**
     * 描述 区县编码
     * 是否必填 false
     * 版本 v1.0
     */
    private final String shopName;

    /**
     * 描述 区县编码
     * 是否必填 false
     * 版本 v1.0
     */
    private final String districtCode;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("shop_name", shopName);
        object.put("district_code", districtCode);
        return object;
    }

    @Override
    public String getPath() {
        return "/account-platform/auth/shop/list";
    }
}