package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.pc.packagemanage;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 17.1. 套餐表单 v1.0
 *
 * @author wangmin
 * @date 2021-06-01 18:10:54
 */
@Data
public class PackageFormPageBean implements Serializable {
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
     * 描述 套餐id
     * 版本 v1.0
     */
    @JSONField(name = "id")
    private Long id;

    /**
     * 描述 套餐id
     * 版本 v1.0
     */
    @JSONField(name = "package_id")
    private Long packageId;

    /**
     * 描述 门店id
     * 版本 v1.0
     */
    @JSONField(name = "shop_id")
    private Long shopId;

    /**
     * 描述 门店名称
     * 版本 v1.0
     */
    @JSONField(name = "shop_name")
    private String shopName;

    /**
     * 描述 品牌名称
     * 版本 v1.0
     */
    @JSONField(name = "brand_name")
    private String brandName;

    /**
     * 描述 套餐名称
     * 版本 v1.0
     */
    @JSONField(name = "package_name")
    private String packageName;

    /**
     * 描述 创建时间
     * 版本 v1.0
     */
    @JSONField(name = "create_time")
    private String createTime;

    /**
     * 描述 套餐价格
     * 版本 v1.0
     */
    @JSONField(name = "price")
    private String price;

    /**
     * 描述 套餐价格
     * 版本 v1.0
     */
    @JSONField(name = "package_price")
    private String packagePrice;

    /**
     * 描述 卡券数量
     * 版本 v1.0
     */
    @JSONField(name = "voucher_number")
    private Integer voucherNumber;

    /**
     * 描述 有效期
     * 版本 v1.0
     */
    @JSONField(name = "validity")
    private Integer validity;

    /**
     * 描述 售出
     * 版本 v1.0
     */
    @JSONField(name = "sold_number")
    private Integer soldNumber;

    /**
     * 描述 赠送
     * 版本 v1.0
     */
    @JSONField(name = "give_number")
    private Integer giveNumber;

    /**
     * 描述 创建者
     * 版本 v1.0
     */
    @JSONField(name = "creator")
    private String creator;

    /**
     * 描述 状态
     * 版本 v1.0
     */
    @JSONField(name = "status")
    private Boolean status;

    /**
     * 描述 审核状态 AUDITING(0,"审核中"),AGREE(1,"已通过"),REFUSAL(2,"已拒绝")
     * 版本 v2.0
     */
    @JSONField(name = "audit_status")
    private String auditStatus;

    /**
     * 描述 审核状态名称
     * 版本 v2.0
     */
    @JSONField(name = "audit_status_name")
    private String auditStatusName;

    /**
     * 描述 客户使用有效期
     * 版本 v2.0
     */
    @JSONField(name = "customer_use_validity")
    private String customerUseValidity;

}