package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.pc.customermanage;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 13.17. 售后客户详情(杨)v3.0
 *
 * @author wangmin
 * @date 2021-06-01 18:10:54
 */
@Data
public class AfterSaleCustomerInfoBean implements Serializable {
    /**
     * 描述 归属门店
     * 版本 v1.0
     */
    @JSONField(name = "shop_name")
    private String shopName;

    /**
     * 描述 归属门店id
     * 版本 v1.0
     */
    @JSONField(name = "shop_id")
    private Long shopId;

    /**
     * 描述 品牌名称
     * 版本 v1.0
     */
    @JSONField(name = "brand_name")
    private String brandName;

    /**
     * 描述 是否注册
     * 版本 v1.0
     */
    @JSONField(name = "registration_status_name")
    private String registrationStatusName;

    /**
     * 描述 上次接待日期
     * 版本 v1.0
     */
    @JSONField(name = "last_reception_date")
    private String lastReceptionDate;

    /**
     * 描述 开单时间
     * 版本 v1.0
     */
    @JSONField(name = "start_order_date")
    private String startOrderDate;

    /**
     * 描述 车牌号码
     * 版本 v1.0
     */
    @JSONField(name = "plate_number")
    private String plateNumber;

    /**
     * 描述 底盘号
     * 版本 v1.0
     */
    @JSONField(name = "vehicle_chassis_code")
    private String vehicleChassisCode;

    /**
     * 描述 送修人姓名
     * 版本 v1.0
     */
    @JSONField(name = "customer_name")
    private String customerName;

    /**
     * 描述 送人手联系方式
     * 版本 v1.0
     */
    @JSONField(name = "customer_phone")
    private String customerPhone;

    /**
     * 描述 送修人性别
     * 版本 v1.0
     */
    @JSONField(name = "sex")
    private String sex;

    /**
     * 描述 送修人性别
     * 版本 v1.0
     */
    @JSONField(name = "sex_id")
    private Integer sexId;

    /**
     * 描述 销售顾问id
     * 版本 v1.0
     */
    @JSONField(name = "sale_id")
    private String saleId;

    /**
     * 描述 销售故名称
     * 版本 v1.0
     */
    @JSONField(name = "sale_name")
    private String saleName;

    /**
     * 描述 最新里程数
     * 版本 v1.0
     */
    @JSONField(name = "newest_miles")
    private Integer newestMiles;

    /**
     * 描述 频次
     * 版本 v1.0
     */
    @JSONField(name = "repair_times")
    private Integer repairTimes;

    /**
     * 描述 总消费
     * 版本 v1.0
     */
    @JSONField(name = "total_price")
    private Double totalPrice;

    /**
     * 描述 车辆id
     * 版本 v1.0
     */
    @JSONField(name = "car_id")
    private Long carId;

    /**
     * 描述 来源类型
     * 版本 v1.0
     */
    @JSONField(name = "source_type_name")
    private String sourceTypeName;

    /**
     * 描述 唯一标识
     * 版本 v1.0
     */
    @JSONField(name = "id")
    private String id;

    /**
     * 描述 客户意向车型Id
     * 版本 v3.0
     */
    @JSONField(name = "car_model_id")
    private Long carModelId;

    /**
     * 描述 客户意向车系Id
     * 版本 v3.0
     */
    @JSONField(name = "car_style_id")
    private Long carStyleId;

    /**
     * 描述 客户Id
     * 版本 v3.0
     */
    @JSONField(name = "customer_id")
    private Long customerId;

    /**
     * 描述 购车日期
     * 版本 v3.0
     */
    @JSONField(name = "buy_car_time")
    private String buyCarTime;

}