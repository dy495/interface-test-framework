package com.haisheng.framework.testng.bigScreen.itemMall.common.scene.shop;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 3.5. 删除门店
 *
 * @author wangmin
 * @date 2021-07-28 16:58:57
 */
@Builder
public class ShopDeleteScene extends BaseScene {
    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final String referer;

    /**
     * 描述 门店ID
     * 是否必填 true
     * 版本 -
     */
    private final Integer shopId;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("shopId", shopId);
        return object;
    }

    @Override
    public String getPath() {
        return "/mall/shop/delete";
    }
}