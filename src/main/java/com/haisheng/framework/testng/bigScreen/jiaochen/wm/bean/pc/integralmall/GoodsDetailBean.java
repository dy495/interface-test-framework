package com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.integralmall;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 23.28. 商品详情 (张小龙) v2.0
 *
 * @author wangmin
 * @date 2021-04-02 14:47:46
 */
@Data
public class GoodsDetailBean implements Serializable {
    /**
     * 描述 商品id
     * 版本 -
     */
    @JSONField(name = "id")
    private Long id;

    /**
     * 描述 商品名称
     * 版本 v2.0
     */
    @JSONField(name = "goods_name")
    private String goodsName;

    /**
     * 描述 商品说明
     * 版本 v2.0
     */
    @JSONField(name = "goods_description")
    private String goodsDescription;

    /**
     * 描述 一级品类
     * 版本 v2.0
     */
    @JSONField(name = "first_category")
    private Long firstCategory;

    /**
     * 描述 一级品类名称
     * 版本 v2.0
     */
    @JSONField(name = "first_category_name")
    private String firstCategoryName;

    /**
     * 描述 二级品类
     * 版本 v2.0
     */
    @JSONField(name = "second_category")
    private Long secondCategory;

    /**
     * 描述 三级品类
     * 版本 v2.0
     */
    @JSONField(name = "third_category")
    private Long thirdCategory;

    /**
     * 描述 商品品牌
     * 版本 v2.0
     */
    @JSONField(name = "goods_brand")
    private Long goodsBrand;

    /**
     * 描述 商品图片列表
     * 版本 v2.0
     */
    @JSONField(name = "goods_pic_list")
    private JSONArray goodsPicList;

    /**
     * 描述 市场价
     * 版本 v2.0
     */
    @JSONField(name = "price")
    private String price;

    /**
     * 描述 所选规格
     * 版本 v2.0
     */
    @JSONField(name = "select_specifications")
    private JSONArray selectSpecifications;

    /**
     * 描述 规格类型
     * 版本 v2.0
     */
    @JSONField(name = "specifications_id")
    private Long specificationsId;

    /**
     * 描述 规格类型
     * 版本 v2.0
     */
    @JSONField(name = "specifications_name")
    private String specificationsName;

    /**
     * 描述 规格列表
     * 版本 v2.0
     */
    @JSONField(name = "specifications_list")
    private JSONArray specificationsList;

    /**
     * 描述 规格参数详情id
     * 版本 v2.0
     */
    @JSONField(name = "specifications_detail_id")
    private Long specificationsDetailId;

    /**
     * 描述 规格参数详情名称
     * 版本 v2.0
     */
    @JSONField(name = "specifications_detail_name")
    private String specificationsDetailName;

    /**
     * 描述 生成商品规格列表
     * 版本 v2.0
     */
    @JSONField(name = "goods_specifications_list")
    private JSONArray goodsSpecificationsList;

    /**
     * 描述 第一品类
     * 版本 v2.0
     */
    @JSONField(name = "first_specifications")
    private Long firstSpecifications;

    /**
     * 描述 第一品类名称
     * 版本 v2.0
     */
    @JSONField(name = "first_specifications_name")
    private String firstSpecificationsName;

    /**
     * 描述 第二品类
     * 版本 v2.0
     */
    @JSONField(name = "second_specifications")
    private Long secondSpecifications;

    /**
     * 描述 第一品类名称
     * 版本 v2.0
     */
    @JSONField(name = "second_specifications_name")
    private String secondSpecificationsName;

    /**
     * 描述 头图
     * 版本 v2.0
     */
    @JSONField(name = "head_pic")
    private String headPic;

    /**
     * 描述 图片oss地址
     * 版本 -
     */
    @JSONField(name = "oss_key")
    private String ossKey;

    /**
     * 描述 商品详情
     * 版本 v2.0
     */
    @JSONField(name = "goods_detail")
    private String goodsDetail;

}