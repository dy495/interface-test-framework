package com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.pc.integralmall;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 26.24. 商品管理列表
 *
 * @author wangmin
 * @date 2021-05-18 17:04:35
 */
@Builder
public class GoodsManagePageScene extends BaseScene {
    /**
     * 描述 页码 大于0
     * 是否必填 true
     * 版本 v1.0
     */
    private final Integer page;

    /**
     * 描述 页大小 范围为[1,100]
     * 是否必填 true
     * 版本 v1.0
     */
    private final Integer size;

    /**
     * 描述 商品名称
     * 是否必填 false
     * 版本 v2.0
     */
    private final String goodsName;

    /**
     * 描述 商品品牌
     * 是否必填 false
     * 版本 v2.0
     */
    private final Long goodsBrand;

    /**
     * 描述 商品状态
     * 是否必填 false
     * 版本 v2.0
     */
    private final String goodsStatus;

    /**
     * 描述 一级品类
     * 是否必填 false
     * 版本 v2.0
     */
    private final Long firstCategory;

    /**
     * 描述 二级品类
     * 是否必填 false
     * 版本 v2.0
     */
    private final Long secondCategory;

    /**
     * 描述 三级品类
     * 是否必填 false
     * 版本 v2.0
     */
    private final Long thirdCategory;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("page", page);
        object.put("size", size);
        object.put("goods_name", goodsName);
        object.put("goods_brand", goodsBrand);
        object.put("goods_status", goodsStatus);
        object.put("first_category", firstCategory);
        object.put("second_category", secondCategory);
        object.put("third_category", thirdCategory);
        return object;
    }

    @Override
    public String getPath() {
        return "/yt/pc/integral-mall/goods-manage-page";
    }
}