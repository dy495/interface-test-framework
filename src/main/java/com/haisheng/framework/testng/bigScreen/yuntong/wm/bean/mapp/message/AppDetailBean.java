package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.mapp.message;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 12.3. 获取消息详情（张小龙）（2021-03-11）v3.0 modify
 *
 * @author wangmin
 * @date 2021-06-01 18:10:55
 */
@Data
public class AppDetailBean implements Serializable {
    /**
     * 描述 消息id
     * 版本 v2.0
     */
    @JSONField(name = "id")
    private Long id;

    /**
     * 描述 消息标题
     * 版本 v2.0
     */
    @JSONField(name = "title")
    private String title;

    /**
     * 描述 消息发送时间
     * 版本 v2.0
     */
    @JSONField(name = "time")
    private String time;

    /**
     * 描述 是否已读
     * 版本 v2.0
     */
    @JSONField(name = "is_read")
    private Boolean isRead;

    /**
     * 描述 消息类型 type(MAINTAIN_OVERTIME:保养超时提醒，CONSULT_REPLY_OVERTIME：咨询回复超时提醒)
     * 版本 -
     */
    @JSONField(name = "message_type")
    private String messageType;

    /**
     * 描述 门店id
     * 版本 v2.0
     */
    @JSONField(name = "shop_id")
    private Long shopId;

    /**
     * 描述 车辆品牌
     * 版本 v2.0
     */
    @JSONField(name = "brand_name")
    private String brandName;

    /**
     * 描述 车辆车牌号
     * 版本 v2.0
     */
    @JSONField(name = "plate_number")
    private String plateNumber;

    /**
     * 描述 车辆车系名称
     * 版本 v2.0
     */
    @JSONField(name = "car_style_name")
    private String carStyleName;

    /**
     * 描述 车辆车型名称
     * 版本 v2.0
     */
    @JSONField(name = "car_model_name")
    private String carModelName;

    /**
     * 描述 顾客名称
     * 版本 v2.0
     */
    @JSONField(name = "customer_name")
    private String customerName;

    /**
     * 描述 顾客电话
     * 版本 v2.0
     */
    @JSONField(name = "customer_phone")
    private String customerPhone;

    /**
     * 描述 是否超时
     * 版本 v2.0
     */
    @JSONField(name = "is_overtime")
    private Boolean isOvertime;

    /**
     * 描述 车辆品牌logo图片地址
     * 版本 v2.0
     */
    @JSONField(name = "car_logo_url")
    private String carLogoUrl;

    /**
     * 描述 预约到店时间
     * 版本 v2.0
     */
    @JSONField(name = "appointment_arrival_time")
    private String appointmentArrivalTime;

    /**
     * 描述 预约时间
     * 版本 v2.0
     */
    @JSONField(name = "appointment_time")
    private String appointmentTime;

    /**
     * 描述 message_type为MAINTAIN_OVERTIME时（MAINTAIN、REPAIR、TEST_DRIVE） message_type为CONSULT_REPLY_OVERTIME时（销售顾问SALES、售后服务顾问AFTER_SALES、在线专家ONLINE_EXPERTS）
     * 版本 v2.0
     */
    @JSONField(name = "type")
    private String type;

    /**
     * 描述 类型描述（保养|维修）
     * 版本 v2.0
     */
    @JSONField(name = "type_name")
    private String typeName;

    /**
     * 描述 故障描述
     * 版本 v2.0
     */
    @JSONField(name = "fault_description")
    private String faultDescription;

    /**
     * 描述 客户经理
     * 版本 v2.0
     */
    @JSONField(name = "service_sale_name")
    private String serviceSaleName;

    /**
     * 描述 在线专家
     * 版本 v3.0
     */
    @JSONField(name = "online_experts_info")
    private JSONObject onlineExpertsInfo;

    /**
     * 描述 咨询时间
     * 版本 v3.0
     */
    @JSONField(name = "consult_date")
    private String consultDate;

    /**
     * 描述 系统电话
     * 版本 v3.0
     */
    @JSONField(name = "customer_phone1")
    private String customerPhone1;

    /**
     * 描述 联系电话
     * 版本 v3.0
     */
    @JSONField(name = "customer_phone2")
    private String customerPhone2;

    /**
     * 描述 车系
     * 版本 v3.0
     */
    @JSONField(name = "style_name")
    private String styleName;

    /**
     * 描述 车型
     * 版本 v3.0
     */
    @JSONField(name = "model_name")
    private String modelName;

    /**
     * 描述 咨询内容
     * 版本 v3.0
     */
    @JSONField(name = "consult_content")
    private String consultContent;

    /**
     * 描述 售后服务顾问
     * 版本 v3.0
     */
    @JSONField(name = "after_service_sales_info")
    private JSONObject afterServiceSalesInfo;

    /**
     * 描述 销售服务顾问
     * 版本 v3.0
     */
    @JSONField(name = "service_sales_info")
    private JSONObject serviceSalesInfo;

}