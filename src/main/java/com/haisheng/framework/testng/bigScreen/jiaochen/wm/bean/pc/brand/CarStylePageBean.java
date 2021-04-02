package com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.brand;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 28.8. 品牌车系分页（谢）
 *
 * @author wangmin
 * @date 2021-04-02 14:47:46
 */
@Data
public class CarStylePageBean implements Serializable {
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
     * 描述 车系id
     * 版本 v1.0
     */
    @JSONField(name = "id")
    private Long id;

    /**
     * 描述 车系名称
     * 版本 v1.0
     */
    @JSONField(name = "name")
    private String name;

    /**
     * 描述 生产商
     * 版本 v1.0
     */
    @JSONField(name = "manufacturer")
    private String manufacturer;

    /**
     * 描述 品牌id
     * 版本 v2.0
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
     * 描述 车系上线时间
     * 版本 v1.0
     */
    @JSONField(name = "online_time")
    private String onlineTime;

}