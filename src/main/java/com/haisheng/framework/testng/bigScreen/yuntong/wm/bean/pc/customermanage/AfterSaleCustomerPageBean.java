package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.pc.customermanage;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 13.15. 售后客户列表 (杨) v3.0
 *
 * @author wangmin
 * @date 2021-06-01 18:10:54
 */
@Data
public class AfterSaleCustomerPageBean implements Serializable {
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
     * 描述 服务单号
     * 版本 v1.0
     */
    @JSONField(name = "service_order_id")
    private String serviceOrderId;

    /**
     * 描述 工单类型
     * 版本 v1.0
     */
    @JSONField(name = "work_order_type")
    private String workOrderType;

    /**
     * 描述 维修类型
     * 版本 v1.0
     */
    @JSONField(name = "repair_type")
    private String repairType;

    /**
     * 描述 维修类型
     * 版本 v1.0
     */
    @JSONField(name = "repair_type_name")
    private String repairTypeName;

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
    @JSONField(name = "repair_customer_name")
    private String repairCustomerName;

    /**
     * 描述 送人手联系方式
     * 版本 v1.0
     */
    @JSONField(name = "repair_customer_phone")
    private String repairCustomerPhone;

    /**
     * 描述 送修人性别
     * 版本 v1.0
     */
    @JSONField(name = "sex")
    private String sex;

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
     * 描述 交车时间
     * 版本 v1.0
     */
    @JSONField(name = "deliver_date")
    private String deliverDate;

    /**
     * 描述 导入时间
     * 版本 v1.0
     */
    @JSONField(name = "import_date")
    private String importDate;

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
     * 描述 车型
     * 版本 v3.0
     */
    @JSONField(name = "car_model_name")
    private String carModelName;

    /**
     * 描述 车系
     * 版本 v3.0
     */
    @JSONField(name = "car_style_name")
    private String carStyleName;

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

    /**
     * 描述 创建时间 (测试用)
     * 版本 v3.0
     */
    @JSONField(name = "create_date")
    private String createDate;

}