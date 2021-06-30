package com.haisheng.framework.testng.bigScreen.xundianDaily.gly.scene.pc.integralmall;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 55.29. 编辑商品
 *
 * @author wangmin
 * @date 2021-06-29 14:11:45
 */
@Builder
public class EditGoodsScene extends BaseScene {
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
     * 描述 商品id
     * 是否必填 false
     * 版本 -
     */
    private final Long id;

    /**
     * 描述 商品名称
     * 是否必填 true
     * 版本 v2.0
     */
    private final String goodsName;

    /**
     * 描述 商品说明
     * 是否必填 false
     * 版本 v2.0
     */
    private final String goodsDescription;

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

    /**
     * 描述 商品品牌
     * 是否必填 false
     * 版本 v2.0
     */
    private final Long goodsBrand;

    /**
     * 描述 商品图片列表
     * 是否必填 false
     * 版本 v2.0
     */
    private final JSONArray goodsPicList;

    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final JSONArray goodsPicTempFullUrlList;

    /**
     * 描述 市场价
     * 是否必填 false
     * 版本 v2.0
     */
    private final Double price;

    /**
     * 描述 所选规格
     * 是否必填 false
     * 版本 v2.0
     */
    private final JSONArray selectSpecifications;

    /**
     * 描述 生成商品规格列表
     * 是否必填 false
     * 版本 v2.0
     */
    private final JSONArray goodsSpecificationsList;

    /**
     * 描述 商品详情
     * 是否必填 false
     * 版本 v2.0
     */
    private final String goodsDetail;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("appId", appId);
        object.put("id", id);
        object.put("goods_name", goodsName);
        object.put("goods_description", goodsDescription);
        object.put("first_category", firstCategory);
        object.put("second_category", secondCategory);
        object.put("third_category", thirdCategory);
        object.put("goods_brand", goodsBrand);
        object.put("goods_pic_list", goodsPicList);
        object.put("goods_pic_temp_full_url_list", goodsPicTempFullUrlList);
        object.put("price", price);
        object.put("select_specifications", selectSpecifications);
        object.put("goods_specifications_list", goodsSpecificationsList);
        object.put("goods_detail", goodsDetail);
        return object;
    }

    @Override
    public String getPath() {
        return "/shop/pc/integral-mall/edit-goods";
    }
}