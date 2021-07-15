package com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.shop.collection;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 5.3. 获取门店收藏列表对应设备列表的树状结构
 *
 * @author wangmin
 * @date 2021-07-14 14:30:21
 */
@Builder
public class ShopDeviceListScene extends BaseScene {
    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final String referer;

    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final String appId;

    /**
     * 描述 门店名称
     * 是否必填 false
     * 版本 -
     */
    private final String shopName;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("appId", appId);
        object.put("shop_name", shopName);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol/shop/collection/shop-device/list";
    }
}