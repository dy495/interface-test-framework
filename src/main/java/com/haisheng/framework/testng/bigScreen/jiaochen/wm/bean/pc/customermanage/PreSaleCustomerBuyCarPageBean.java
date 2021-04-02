package com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.customermanage;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 11.8. 售前客户购车记录 (杨)v3.0
 *
 * @author wangmin
 * @date 2021-04-02 14:47:46
 */
@Data
public class PreSaleCustomerBuyCarPageBean implements Serializable {
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
     * 描述 门店名称
     * 版本 v2.0
     */
    @JSONField(name = "shop_name")
    private String shopName;

    /**
     * 描述 品牌名称
     * 版本 v2.0
     */
    @JSONField(name = "brand_name")
    private String brandName;

    /**
     * 描述 车主类型
     * 版本 v2.0
     */
    @JSONField(name = "owner_type_name")
    private String ownerTypeName;

    /**
     * 描述 客户名称
     * 版本 v2.0
     */
    @JSONField(name = "customer_name")
    private String customerName;

    /**
     * 描述 客户手机号
     * 版本 v2.0
     */
    @JSONField(name = "customer_phone")
    private String customerPhone;

    /**
     * 描述 性别
     * 版本 v2.0
     */
    @JSONField(name = "sex")
    private String sex;

    /**
     * 描述 车型
     * 版本 v2.0
     */
    @JSONField(name = "intention_car_model_name")
    private String intentionCarModelName;

    /**
     * 描述 车系
     * 版本 v2.0
     */
    @JSONField(name = "intention_car_style_name")
    private String intentionCarStyleName;

    /**
     * 描述 购车日期
     * 版本 v2.0
     */
    @JSONField(name = "buy_car_date")
    private String buyCarDate;

    /**
     * 描述 底盘号
     * 版本 v3.0
     */
    @JSONField(name = "vehicle_chassis_code")
    private String vehicleChassisCode;

    /**
     * 描述 销售名称
     * 版本 v3.0
     */
    @JSONField(name = "pre_sale_name")
    private String preSaleName;

    /**
     * 描述 销售账号
     * 版本 v3.0
     */
    @JSONField(name = "pre_sale_account")
    private String preSaleAccount;

}