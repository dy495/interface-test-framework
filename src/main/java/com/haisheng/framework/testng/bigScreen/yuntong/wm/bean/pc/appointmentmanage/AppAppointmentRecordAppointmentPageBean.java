package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.pc.appointmentmanage;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 16.1. 预约记录分页（谢）v3.0（2021-03-22）
 *
 * @author wangmin
 * @date 2021-06-01 18:10:54
 */
@Data
public class AppAppointmentRecordAppointmentPageBean implements Serializable {
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
     * 描述 预约id
     * 版本 v1.0
     */
    @JSONField(name = "id")
    private Long id;

    /**
     * 描述 预约门店id
     * 版本 v1.0
     */
    @JSONField(name = "shop_id")
    private Long shopId;

    /**
     * 描述 预约门店名称
     * 版本 v1.0
     */
    @JSONField(name = "shop_name")
    private String shopName;

    /**
     * 描述 预约车辆品牌
     * 版本 v1.0
     */
    @JSONField(name = "brand_name")
    private String brandName;

    /**
     * 描述 预约车辆车系名称
     * 版本 v1.0
     */
    @JSONField(name = "car_style_name")
    private String carStyleName;

    /**
     * 描述 预约车辆车牌号
     * 版本 v1.0
     */
    @JSONField(name = "plate_number")
    private String plateNumber;

    /**
     * 描述 预约顾客名称
     * 版本 v1.0
     */
    @JSONField(name = "customer_name")
    private String customerName;

    /**
     * 描述 预约顾客电话
     * 版本 v1.0
     */
    @JSONField(name = "customer_phone")
    private String customerPhone;

    /**
     * 描述 预约服务顾问
     * 版本 v1.0
     */
    @JSONField(name = "customer_manager")
    private String customerManager;

    /**
     * 描述 预约创建日期
     * 版本 v1.0
     */
    @JSONField(name = "create_date")
    private String createDate;

    /**
     * 描述 预约状态
     * 版本 v1.0
     */
    @JSONField(name = "appointment_status")
    private Integer appointmentStatus;

    /**
     * 描述 预约状态描述
     * 版本 v1.0
     */
    @JSONField(name = "appointment_status_name")
    private String appointmentStatusName;

    /**
     * 描述 预约提醒时间
     * 版本 v1.0
     */
    @JSONField(name = "remind_time")
    private String remindTime;

    /**
     * 描述 预约确认时间
     * 版本 v1.0
     */
    @JSONField(name = "confirm_time")
    private String confirmTime;

    /**
     * 描述 预约确认账号
     * 版本 v1.0
     */
    @JSONField(name = "confirm_account")
    private String confirmAccount;

    /**
     * 描述 预约是否超时
     * 版本 v1.0
     */
    @JSONField(name = "is_overtime")
    private Boolean isOvertime;

    /**
     * 描述 预约接待时间
     * 版本 v1.0
     */
    @JSONField(name = "reception_time")
    private String receptionTime;

    /**
     * 描述 预约接待账号
     * 版本 v1.0
     */
    @JSONField(name = "reception_account")
    private String receptionAccount;

    /**
     * 描述 预约取消时间
     * 版本 v1.0
     */
    @JSONField(name = "cancel_time")
    private String cancelTime;

    /**
     * 描述 预约取消账号
     * 版本 v1.0
     */
    @JSONField(name = "cancel_account")
    private String cancelAccount;

    /**
     * 描述 预约价格
     * 版本 v1.0
     */
    @JSONField(name = "appointment_price")
    private Double appointmentPrice;

    /**
     * 描述 是否可确认
     * 版本 v1.0
     */
    @JSONField(name = "is_can_confirm")
    private Boolean isCanConfirm;

    /**
     * 描述 是否可取消
     * 版本 v1.0
     */
    @JSONField(name = "is_can_cancel")
    private Boolean isCanCancel;

    /**
     * 描述 是否可调整时间
     * 版本 v1.0
     */
    @JSONField(name = "is_can_adjust")
    private Boolean isCanAdjust;

    /**
     * 描述 是否可接待
     * 版本 v2.0
     */
    @JSONField(name = "is_can_reception")
    private Boolean isCanReception;

    /**
     * 描述 预约类型 MAINTAIN、REPAIR
     * 版本 v2.0
     */
    @JSONField(name = "type")
    private String type;

    /**
     * 描述 预约类型描述 保养、维修
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
     * 描述 是否展示故障描述按钮
     * 版本 v2.1
     */
    @JSONField(name = "is_show_fault_desc")
    private Boolean isShowFaultDesc;

}