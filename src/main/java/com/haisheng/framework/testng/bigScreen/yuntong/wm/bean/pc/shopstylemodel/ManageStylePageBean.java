package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.pc.shopstylemodel;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 11.4. 试驾车系配置分页（谢）V3.0（2021-03-22）
 *
 * @author wangmin
 * @date 2021-06-01 18:10:54
 */
@Data
public class ManageStylePageBean implements Serializable {
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
     * 版本 v3.0
     */
    @JSONField(name = "id")
    private Long id;

    /**
     * 描述 品牌
     * 版本 v3.0
     */
    @JSONField(name = "brand_name")
    private String brandName;

    /**
     * 描述 生产商
     * 版本 v3.0
     */
    @JSONField(name = "manufacturer")
    private String manufacturer;

    /**
     * 描述 车系
     * 版本 v3.0
     */
    @JSONField(name = "style_name")
    private String styleName;

    /**
     * 描述 保养预约状态 ENABLE：开启，DISABLE：关闭
     * 版本 v3.0
     */
    @JSONField(name = "status")
    private String status;

}