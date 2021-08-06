package com.haisheng.framework.testng.bigScreen.itemMall.common.scene.shop;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 3.8. 搜索门店详情
 *
 * @author wangmin
 * @date 2021-08-06 17:47:04
 */
@Builder
public class ShopSearchInfoScene extends BaseScene {
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
    private final Integer id;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("id", id);
        return object;
    }

    @Override
    public String getPath() {
        return "/mall/shop/search-info";
    }
}