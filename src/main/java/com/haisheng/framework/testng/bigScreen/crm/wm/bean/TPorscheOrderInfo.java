package com.haisheng.framework.testng.bigScreen.crm.wm.bean;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * 每日订单表
 */
@Data
public class TPorscheOrderInfo {
    @JSONField(name = "auto_increment")
    private Integer id;
    private String shopId;
    private String customerId;
    @JSONField(name = "customer_name")
    private String customerName;
    @JSONField(name = "id_number")
    private String idNumber;
    @JSONField(name = "birthday")
    private String birthday;
    @JSONField(name = "district_name")
    private String address;
    @JSONField(name = "gender")
    private String gender;
    @JSONField(name = "age")
    private String age;
    @JSONField(name = "phones")
    private JSONArray phones;
    private String phone;
    @JSONField(name = "subject_type_name")
    private String subjectTypeName;
    @JSONField(name = "belongs_sale_name")
    private String saleName;
    private String saleId;
    @JSONField(name = "car_style_name")
    private String carStyle;
    @JSONField(name = "car_model_name")
    private String carModel;
    @JSONField(name = "order_date")
    private String orderDate;
    @JSONField(name = "plate_type_name")
    private String plateTypeName;
    @JSONField(name = "defray_type_name")
    private String defrayTypeName;
    @JSONField(name = "source_channel_name")
    private String sourceChannelName;
    @JSONField(name = "pay_type_name")
    private String payTypeName;
    @JSONField(name = "plate_number")
    private String plateNumber;
    @JSONField(name = "vehicle_chassis_code")
    private String vehicleChassisCode;
    private java.sql.Timestamp gmtCreate;
}
