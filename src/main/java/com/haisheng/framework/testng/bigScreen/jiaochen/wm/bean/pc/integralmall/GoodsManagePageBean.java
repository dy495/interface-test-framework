package com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.integralmall;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 23.24. 商品管理列表
 *
 * @author wangmin
 * @date 2021-04-02 14:47:46
 */
@Data
public class GoodsManagePageBean implements Serializable {
    /**
     * 描述 当前页
     * 版本 v1.0
     */
    @JSONField(name = "page")
    private Integer page;

    /**
     * 描述 当前页的数量
     * 版本 v1.0
     */
    @JSONField(name = "size")
    private Integer size;

    /**
     * 描述 每页的数量
     * 版本 v1.0
     */
    @JSONField(name = " page_size")
    private Integer  pageSize;

    /**
     * 描述 总数
     * 版本 v1.0
     */
    @JSONField(name = "total")
    private Long total;

    /**
     * 描述 总页数
     * 版本 v1.0
     */
    @JSONField(name = "pages")
    private Integer pages;

    /**
     * 描述 详细数据列表
     * 版本 v1.0
     */
    @JSONField(name = "list")
    private JSONArray list;

    /**
     * 描述 No comments found.
     * 版本 -
     */
    @JSONField(name = "id")
    private Long id;

    /**
     * 描述 商品图片
     * 版本 v2.0
     */
    @JSONField(name = "goods_pic")
    private String goodsPic;

    /**
     * 描述 商品名称
     * 版本 v2.0
     */
    @JSONField(name = "goods_name")
    private String goodsName;

    /**
     * 描述 所属品牌
     * 版本 v2.0
     */
    @JSONField(name = "belongs_brand")
    private String belongsBrand;

    /**
     * 描述 商品分类
     * 版本 v2.0
     */
    @JSONField(name = "goods_category")
    private String goodsCategory;

    /**
     * 描述 市场价
     * 版本 v2.0
     */
    @JSONField(name = "price")
    private String price;

    /**
     * 描述 商品状态
     * 版本 v2.0
     */
    @JSONField(name = "goods_status")
    private String goodsStatus;

    /**
     * 描述 商品状态名称
     * 版本 v2.0
     */
    @JSONField(name = "goods_status_name")
    private String goodsStatusName;

}