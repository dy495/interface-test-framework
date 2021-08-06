package com.haisheng.framework.testng.bigScreen.itemMall.common.scene.shop;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 3.9. 门店导航
 *
 * @author wangmin
 * @date 2021-08-06 17:47:04
 */
@Builder
public class ShopShopNavigationScene extends BaseScene {
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
     * 是否必填 false
     * 版本 -
     */
    private final String name;

    /**
     * 描述 楼层id
     * 是否必填 false
     * 版本 -
     */
    private final Long floorId;

    /**
     * 描述 排序类型
     * 是否必填 false
     * 版本 -
     */
    private final String sortType;

    /**
     * 描述 排序方式
     * 是否必填 false
     * 版本 -
     */
    private final Integer sort;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("page", page);
        object.put("size", size);
        object.put("name", name);
        object.put("floor_id", floorId);
        object.put("sort_type", sortType);
        object.put("sort", sort);
        return object;
    }

    @Override
    public String getPath() {
        return "/mall/shop/shop-navigation";
    }
}