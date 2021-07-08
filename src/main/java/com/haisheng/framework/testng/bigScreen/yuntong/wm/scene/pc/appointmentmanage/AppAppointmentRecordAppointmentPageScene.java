package com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.pc.appointmentmanage;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 16.1. 预约记录分页（谢）v3.0（2021-03-22）
 *
 * @author wangmin
 * @date 2021-05-18 17:04:35
 */
@Builder
public class AppAppointmentRecordAppointmentPageScene extends BaseScene {
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
     * 描述 预约类型 见字典表《预约类型》v3.0开始必填
     * 是否必填 true
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
     * 描述 预约申请日期范围查询开始日期
     * 是否必填 false
     * 版本 v1.0
     */
    private final String createStart;

    /**
     * 描述 预约申请日期范围查询结束日期
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

    /**
     * 描述 接待日期范围查询开始日期
     * 是否必填 false
     * 版本 v3.0
     */
    private final String receptionStart;

    /**
     * 描述 预约日期范围查询结束日期
     * 是否必填 false
     * 版本 v3.0
     */
    private final String receptionEnd;

    /**
     * 描述 取消日期范围查询开始日期
     * 是否必填 false
     * 版本 v3.0
     */
    private final String cancelStart;

    /**
     * 描述 取消日期范围查询结束日期
     * 是否必填 false
     * 版本 v3.0
     */
    private final String cancelEnd;

    /**
     * 描述 预约日期范围查询结束日期
     * 是否必填 false
     * 版本 v3.0
     */
    private final String cancelAccount;


    @Override
    protected JSONObject getRequestBody() {
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
        object.put("reception_start", receptionStart);
        object.put("reception_end", receptionEnd);
        object.put("cancel_start", cancelStart);
        object.put("cancel_end", cancelEnd);
        object.put("cancel_account", cancelAccount);
        return object;
    }

    @Override
    public String getPath() {
        return "/account-platform/auth/appointment-manage/appointment-record/appointment-page";
    }
}