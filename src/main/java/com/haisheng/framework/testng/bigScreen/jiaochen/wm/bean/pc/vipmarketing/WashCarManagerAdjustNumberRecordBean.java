package com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.vipmarketing;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 2.10. 调整次数记录 (池) v1.2
 *
 * @author wangmin
 * @date 2021-04-02 14:47:46
 */
@Data
public class WashCarManagerAdjustNumberRecordBean implements Serializable {
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
    private String id;

    /**
     * 描述 调整时间
     * 版本 v2.0
     */
    @JSONField(name = "adjust_date")
    private String adjustDate;

    /**
     * 描述 调整门店
     * 版本 v2.0
     */
    @JSONField(name = "adjust_shop_name")
    private String adjustShopName;

    /**
     * 描述 客户类型
     * 版本 v2.0
     */
    @JSONField(name = "customer_vip_type_name")
    private String customerVipTypeName;

    /**
     * 描述 客户名称
     * 版本 v2.0
     */
    @JSONField(name = "customer_name")
    private String customerName;

    /**
     * 描述 联系方式
     * 版本 v2.0
     */
    @JSONField(name = "customer_phone")
    private String customerPhone;

    /**
     * 描述 调整次数
     * 版本 v2.0
     */
    @JSONField(name = "adjust_number")
    private Integer adjustNumber;

    /**
     * 描述 调整后次数
     * 版本 v2.0
     */
    @JSONField(name = "after_number")
    private Integer afterNumber;

    /**
     * 描述 备注
     * 版本 v2.0
     */
    @JSONField(name = "remark")
    private String remark;

}