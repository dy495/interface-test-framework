package com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

@Data
public class AppointmentActivity implements Serializable {
    @JSONField(name = "status_name")
    private String statusName;
    @JSONField(name = "id")
    private Long id;
    @JSONField(name = "title")
    private String title;
    @JSONField(name = "time_str")
    private String timeStr;
}
