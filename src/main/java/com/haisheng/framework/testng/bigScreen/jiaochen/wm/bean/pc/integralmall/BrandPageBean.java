package com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.integralmall;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 23.9. 商品品牌分页 (张小龙) v2.0
 *
 * @author wangmin
 * @date 2021-04-02 14:47:46
 */
@Data
public class BrandPageBean implements Serializable {
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
     * 描述 品牌id
     * 版本 v2.0
     */
    @JSONField(name = "id")
    private Long id;

    /**
     * 描述 品牌logo
     * 版本 v2.0
     */
    @JSONField(name = "brand_pic")
    private String brandPic;

    /**
     * 描述 品牌名称
     * 版本 v2.0
     */
    @JSONField(name = "brand_name")
    private String brandName;

    /**
     * 描述 品牌描述
     * 版本 v2.0
     */
    @JSONField(name = "brand_description")
    private String brandDescription;

    /**
     * 描述 商品数量
     * 版本 v2.0
     */
    @JSONField(name = "num")
    private Integer num;

    /**
     * 描述 品牌状态
     * 版本 v2.0
     */
    @JSONField(name = "brand_status")
    private Boolean brandStatus;

    /**
     * 描述 品牌状态名称
     * 版本 v2.0
     */
    @JSONField(name = "brand_status_name")
    private String brandStatusName;

    /**
     * 描述 上次修改时间
     * 版本 v2.0
     */
    @JSONField(name = "last_modify_time")
    private String lastModifyTime;

    /**
     * 描述 修改人
     * 版本 v2.0
     */
    @JSONField(name = "modify_sale_name")
    private String modifySaleName;

    /**
     * 描述 是否可以编辑
     * 版本 v2.0
     */
    @JSONField(name = "is_edit")
    private Boolean isEdit;

    /**
     * 描述 是否可以删除
     * 版本 v2.0
     */
    @JSONField(name = "is_delete")
    private Boolean isDelete;

}