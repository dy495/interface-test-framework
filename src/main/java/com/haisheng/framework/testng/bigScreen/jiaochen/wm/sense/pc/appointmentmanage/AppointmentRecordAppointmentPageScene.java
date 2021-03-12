package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.appointmentmanage;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * /jiaochen/pc/appointment-manage/appointment-record/appointment-page的接口
 *
 * @author wangmin
 * @date 2021-03-12 17:23:18
 */
@Builder
public class AppointmentRecordAppointmentPageScene extends BaseScene {
    /**
     * 描述 页码 大于0
     * 是否必填 true
     * 版本 v1.0
     */
    private final Integer page;

    /**
     * 描述 页大小 范围为[1,100]
     * 是否必填 true
     * 版本 v1.0
     */
    private final Integer size;

    /**
     * 描述 预约类型 MAINTAIN：保养，REPAIR：维修
     * 是否必填 false
     * 版本 v2.0
     */
    private final String type;

    /**
     * 描述 车牌号
     * 是否必填 false
     * 版本 v1.0
     */
    private final String plateNumber;

    /**
     * 描述 客户经理id 通过权限员工列表获取下拉列表 auth_type 为 MAINTAIN_DISTRIBUTION
     * 是否必填 false
     * 版本 v2.0
     */
    private final String serviceSaleId;

    /**
     * 描述 预约日期范围查询开始日期
     * 是否必填 false
     * 版本 v1.0
     */
    private final String appointmentStart;

    /**
     * 描述 预约日期范围查询结束日期
     * 是否必填 false
     * 版本 v1.0
     */
    private final String appointmentEnd;

    /**
     * 描述 归属门店id
     * 是否必填 false
     * 版本 v1.0
     */
    private final Long shopId;

    /**
     * 描述 客户名称
     * 是否必填 false
     * 版本 v1.0
     */
    private final String customerName;

    /**
     * 描述 预约确认状态 通过通用枚举接口获取，枚举key为MAINTAIN_CONFIRM_STATUS
     * 是否必填 false
     * 版本 v1.0
     */
    private final Integer confirmStatus;

    /**
     * 描述 创建日期范围查询开始日期
     * 是否必填 false
     * 版本 v1.0
     */
    private final String createStart;

    /**
     * 描述 创建日期范围查询结束日期
     * 是否必填 false
     * 版本 v1.0
     */
    private final String createEnd;

    /**
     * 描述 确认日期范围查询开始日期
     * 是否必填 false
     * 版本 v1.0
     */
    private final String confirmStart;

    /**
     * 描述 确认日期范围查询结束日期
     * 是否必填 false
     * 版本 v1.0
     */
    private final String confirmEnd;

    /**
     * 描述 联系方式
     * 是否必填 false
     * 版本 v1.0
     */
    private final String customerPhone;

    /**
     * 描述 是否超时
     * 是否必填 false
     * 版本 v1.0
     */
    private final Boolean isOvertime;


    @Override
    public JSONObject getRequestBody(){
        JSONObject object = new JSONObject();
        object.put("page", page);
        object.put("size", size);
        object.put("type", type);
        object.put("plate_number", plateNumber);
        object.put("service_sale_id", serviceSaleId);
        object.put("appointment_start", appointmentStart);
        object.put("appointment_end", appointmentEnd);
        object.put("shop_id", shopId);
        object.put("customer_name", customerName);
        object.put("confirm_status", confirmStatus);
        object.put("create_start", createStart);
        object.put("create_end", createEnd);
        object.put("confirm_start", confirmStart);
        object.put("confirm_end", confirmEnd);
        object.put("customer_phone", customerPhone);
        object.put("is_overtime", isOvertime);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/appointment-manage/appointment-record/appointment-page";
    }
}