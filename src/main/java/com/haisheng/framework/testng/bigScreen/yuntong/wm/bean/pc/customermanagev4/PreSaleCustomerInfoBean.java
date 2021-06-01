package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.pc.customermanagev4;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 1.1. 售前客户详情(池)v4.0 （2021-05-06 更新）
 *
 * @author wangmin
 * @date 2021-06-01 18:10:53
 */
@Data
public class PreSaleCustomerInfoBean implements Serializable {
    /**
     * 描述 客户Id
     * 版本 v4.0
     */
    @JSONField(name = "customer_id")
    private Long customerId;

    /**
     * 描述 归属门店
     * 版本 v4.0
     */
    @JSONField(name = "shop_name")
    private String shopName;

    /**
     * 描述 品牌
     * 版本 v3.0
     */
    @JSONField(name = "brand_name")
    private String brandName;

    /**
     * 描述 车主类型
     * 版本 v3.0
     */
    @JSONField(name = "subject_type")
    private String subjectType;

    /**
     * 描述 车主类型
     * 版本 v3.0
     */
    @JSONField(name = "subject_type_name")
    private String subjectTypeName;

    /**
     * 描述 注册类型
     * 版本 v3.0
     */
    @JSONField(name = "registration_status")
    private Integer registrationStatus;

    /**
     * 描述 注册类型名称
     * 版本 v3.0
     */
    @JSONField(name = "registration_status_name")
    private String registrationStatusName;

    /**
     * 描述 客户名称
     * 版本 v3.0
     */
    @JSONField(name = "customer_name")
    private String customerName;

    /**
     * 描述 客户手机号
     * 版本 v3.0
     */
    @JSONField(name = "customer_phone")
    private String customerPhone;

    /**
     * 描述 性别
     * 版本 v3.0
     */
    @JSONField(name = "sex")
    private String sex;

    /**
     * 描述 No comments found.
     * 版本 -
     */
    @JSONField(name = "sex_id")
    private Integer sexId;

    /**
     * 描述 创建时间
     * 版本 v3.0
     */
    @JSONField(name = "create_date")
    private String createDate;

    /**
     * 描述 销售id
     * 版本 v3.0
     */
    @JSONField(name = "sale_id")
    private String saleId;

    /**
     * 描述 销售名称
     * 版本 v3.0
     */
    @JSONField(name = "sale_name")
    private String saleName;

    /**
     * 描述 销售账号
     * 版本 v3.0
     */
    @JSONField(name = "sale_phone")
    private String salePhone;

    /**
     * 描述 客户类型
     * 版本 v3.0
     */
    @JSONField(name = "customer_type_name")
    private String customerTypeName;

    /**
     * 描述 唯一标识
     * 版本 v3.0
     */
    @JSONField(name = "id")
    private String id;

    /**
     * 描述 客户意向车型
     * 版本 v3.0
     */
    @JSONField(name = "intention_car_model_name")
    private String intentionCarModelName;

    /**
     * 描述 客户意向车型Id
     * 版本 v3.0
     */
    @JSONField(name = "intention_car_model_id")
    private Long intentionCarModelId;

    /**
     * 描述 客户意向车系
     * 版本 v3.0
     */
    @JSONField(name = "intention_car_style_name")
    private String intentionCarStyleName;

    /**
     * 描述 客户意向车系
     * 版本 v3.0
     */
    @JSONField(name = "intention_car_style_id")
    private Long intentionCarStyleId;

    /**
     * 描述 门店id
     * 版本 v3.0
     */
    @JSONField(name = "shop_id")
    private Long shopId;

    /**
     * 描述 是否注册
     * 版本 v4.0
     */
    @JSONField(name = "is_register")
    private String isRegister;

    /**
     * 描述 是否注册
     * 版本 v4.0
     */
    @JSONField(name = "is_register_name")
    private String isRegisterName;

    /**
     * 描述 客户等级
     * 版本 v4.0
     */
    @JSONField(name = "customer_level")
    private String customerLevel;

    /**
     * 描述 到店次数
     * 版本 v4.0
     */
    @JSONField(name = "to_shop_number")
    private Integer toShopNumber;

    /**
     * 描述 最近到店时间
     * 版本 v4.0
     */
    @JSONField(name = "last_to_shop_date")
    private String lastToShopDate;

    /**
     * 描述 接待时间
     * 版本 v4.0
     */
    @JSONField(name = "reception_date")
    private String receptionDate;

    /**
     * 描述 接待顾问
     * 版本 v4.0
     */
    @JSONField(name = "reception_sales_name")
    private String receptionSalesName;

    /**
     * 描述 预计购车日期
     * 版本 v4.0
     */
    @JSONField(name = "estimate_buy_car_date")
    private String estimateBuyCarDate;

}