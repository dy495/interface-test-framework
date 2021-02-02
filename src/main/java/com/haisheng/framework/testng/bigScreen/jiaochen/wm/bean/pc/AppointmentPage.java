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
    @JSONField(name = "appointment_status_name")
    private String appointmentStatusName;
}
