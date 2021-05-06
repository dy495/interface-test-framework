package com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.app;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 预约列表
 *
 * @author wangmin
 * @date 2021/1/29 15:20
 */
@Data
public class AppAppointmentPage implements Serializable {
    @JSONField(name = "id")
    private Integer id;
    @JSONField(name = "shop_id")
    private String shopId;
    @JSONField(name = "plate_number")
    private String platNumber;
    @JSONField(name = "customer_phone")
    private String customerPhone;

    /**
     * 预约到达时间
     */
    @JSONField(name = "arrive_time")
    private String arriveTime;
    @JSONField(name = "type")
    private String type;
    @JSONField(name = "type_name")
    private String typeName;
    @JSONField(name = "is_can_confirm")
    private Boolean isCanConfirm;
    @JSONField(name = "is_can_reception")
    private Boolean isCanReception;
    @JSONField(name = "is_can_adjust")
    private Boolean isCanAdjust;
    @JSONField(name = "is_overtime")
    private Boolean isOvertime;
    @JSONField(name = "is_can_cancel")
    private Boolean isCanCancel;
}
