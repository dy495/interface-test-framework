package com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 预约记录
 *
 * @author wangmin
 * @date 2021/2/1 16:42
 */
@Data
public class AppointmentPage implements Serializable {
    @JSONField(name = "id")
    private Integer id;

    /**
     * 预约状态
     */
    @JSONField(name = "appointment_status_name")
    private String appointmentStatusName;

    /**
     * 预约顾客电话
     */
    @JSONField(name = "customer_phone")
    private String customerPhone;

    /**
     * 预约类型 MAINTAIN、REPAIR
     */
    @JSONField(name = "type")
    private String type;

    /**
     * 是否可以接待
     */
    @JSONField(name = "is_can_reception")
    private Boolean isCanReception;

    /**
     * 是否可以确认
     */
    @JSONField(name = "is_can_confirm")
    private Boolean isCanConfirm;

    /**
     * 是否可取消
     */
    @JSONField(name = "is_can_cancel")
    private Boolean isCanCancel;

    /**
     * 是否可调整时间
     */
    @JSONField(name = "is_can_adjust")
    private Boolean isCanAdjust;
}
