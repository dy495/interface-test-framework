package com.haisheng.framework.testng.bigScreen.itemMall.common.scene.shop;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 3.4. 搜索门店
 *
 * @author wangmin
 * @date 2021-08-06 17:47:04
 */
@Builder
public class ShopSearchScene extends BaseScene {
    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final String referer;

    /**
     * 描述 当前页
     * 是否必填 true
     * 版本 -
     */
    private final Integer page;

    /**
     * 描述 当前页的数量
     * 是否必填 true
     * 版本 -
     */
    private final Integer size;

    /**
     * 描述 门店名称
     * 是否必填 true
     * 版本 -
     */
    private final String shopName;

    /**
     * 描述 联系人
     * 是否必填 true
     * 版本 -
     */
    private final String managerName;

    /**
     * 描述 城市
     * 是否必填 true
     * 版本 -
     */
    private final String city;

    /**
     * 描述 状态
     * 是否必填 true
     * 版本 -
     */
    private final Boolean isShow;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("page", page);
        object.put("size", size);
        object.put("shop_name", shopName);
        object.put("manager_name", managerName);
        object.put("city", city);
        object.put("is_show", isShow);
        return object;
    }

    @Override
    public String getPath() {
        return "/mall/shop/search";
    }
}