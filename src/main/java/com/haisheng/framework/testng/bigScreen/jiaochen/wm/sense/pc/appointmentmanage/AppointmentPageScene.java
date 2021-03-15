package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.appointmentmanage;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 预约管理 -> 预约记录
 */
@Builder
public class AppointmentPageScene extends BaseScene {
    private final String plateNumber;
    private final String customerManager;
    private final String appointmentDate;
    private final Long shopId;
    private final String customerName;
    private final Integer appointmentStatus;
    private final String createDate;
    private final String confirmTime;
    private final String customerPhone;
    private final Boolean isOvertime;
    @Builder.Default
    private Integer page = 1;
    @Builder.Default
    private Integer size = 10;

    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("plate_number", plateNumber);
        object.put("customer_manager", customerManager);
        object.put("appointment_date", appointmentDate);
        object.put("shop_id", shopId);
        object.put("customer_name", customerName);
        object.put("appointment_status", appointmentStatus);
        object.put("create_date", createDate);
        object.put("confirm_time", confirmTime);
        object.put("customer_phone", customerPhone);
        object.put("is_overtime", isOvertime);
        object.put("page", page);
        object.put("size", size);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/appointment-manage/appointment-record/appointment-page";
    }

    @Override
    public void setSize(Integer size) {
        this.size = size;
    }

    @Override
    public void setPage(Integer page) {
        this.page = page;
    }
}
