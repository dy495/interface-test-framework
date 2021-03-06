package com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.brand;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 28.15. 品牌车系车型分页（谢）
 *
 * @author wangmin
 * @date 2021-04-02 14:47:46
 */
@Data
public class CarStyleCarModelPageBean implements Serializable {
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
     * 描述 车型id
     * 版本 v1.0
     */
    @JSONField(name = "id")
    private Long id;

    /**
     * 描述 车型名
     * 版本 v1.0
     */
    @JSONField(name = "name")
    private String name;

    /**
     * 描述 车型年款
     * 版本 v1.0
     */
    @JSONField(name = "year")
    private String year;

    /**
     * 描述 车型状态
     * 版本 v1.0
     */
    @JSONField(name = "status")
    private String status;

    /**
     * 描述 车型是否在售
     * 版本 v1.0
     */
    @JSONField(name = "on_sale")
    private Boolean onSale;

    /**
     * 描述 品牌id
     * 版本 v1.0
     */
    @JSONField(name = "brand_id")
    private Long brandId;

    /**
     * 描述 品牌名称
     * 版本 v1.0
     */
    @JSONField(name = "brand_name")
    private String brandName;

    /**
     * 描述 车系id
     * 版本 v1.0
     */
    @JSONField(name = "style_id")
    private Long styleId;

    /**
     * 描述 车系名称
     * 版本 v1.0
     */
    @JSONField(name = "style_name")
    private String styleName;

}