package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.mapp.task;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 6.1. app预约分页（谢）v3.0（2021-03-16）
 *
 * @author wangmin
 * @date 2021-06-01 18:10:55
 */
@Data
public class AppAppointmentPageBean implements Serializable {
    /**
     * 描述 总数 首次查询或刷新时返回
     * 版本 v1.0
     */
    @JSONField(name = "total")
    private Long total;

    /**
     * 描述 本次查询最后一条数据主键
     * 版本 v1.0
     */
    @JSONField(name = "last_value")
    private JSONObject lastValue;

    /**
     * 描述 展示列（部分接口返回列按权限展示时需要）
     * 版本 v4.0
     */
    @JSONField(name = "key_list")
    private JSONArray keyList;

    /**
     * 描述 key名称（展示列名称）
     * 版本 v4.0
     */
    @JSONField(name = "key_name")
    private String keyName;

    /**
     * 描述 key值（实际取值key）
     * 版本 v4.0
     */
    @JSONField(name = "key_value")
    private String keyValue;

    /**
     * 描述 返回的结果list
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
     * 描述 预约车辆品牌
     * 版本 v1.0
     */
    @JSONField(name = "brand_name")
    private String brandName;

    /**
     * 描述 预约车辆车牌号
     * 版本 v1.0
     */
    @JSONField(name = "plate_number")
    private String plateNumber;

    /**
     * 描述 预约车辆车系名称
     * 版本 v1.0
     */
    @JSONField(name = "car_style_name")
    private String carStyleName;

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
     * 描述 预约是否超时
     * 版本 v1.0
     */
    @JSONField(name = "is_overtime")
    private Boolean isOvertime;

    /**
     * 描述 预约车辆品牌logo图片地址
     * 版本 v1.0
     */
    @JSONField(name = "car_logo_url")
    private String carLogoUrl;

    /**
     * 描述 预约到达时间
     * 版本 v1.0
     */
    @JSONField(name = "arrive_time")
    private String arriveTime;

    /**
     * 描述 预约申请时间
     * 版本 v2.0
     */
    @JSONField(name = "apply_time")
    private String applyTime;

    /**
     * 描述 预约是否可确认
     * 版本 v1.0
     */
    @JSONField(name = "is_can_confirm")
    private Boolean isCanConfirm;

    /**
     * 描述 预约是否可取消
     * 版本 v1.0
     */
    @JSONField(name = "is_can_cancel")
    private Boolean isCanCancel;

    /**
     * 描述 预约是否可调整时间
     * 版本 v1.0
     */
    @JSONField(name = "is_can_adjust")
    private Boolean isCanAdjust;

    /**
     * 描述 预约类型（MAINTAIN|REPAIR|TEST_DRIVE）
     * 版本 v2.0
     */
    @JSONField(name = "type")
    private String type;

    /**
     * 描述 预约类型描述（保养|维修|试驾）
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
     * 描述 预约是否可接待
     * 版本 v2.0
     */
    @JSONField(name = "is_can_reception")
    private Boolean isCanReception;

}