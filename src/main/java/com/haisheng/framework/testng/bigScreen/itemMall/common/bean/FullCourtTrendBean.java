package com.haisheng.framework.testng.bigScreen.itemMall.common.bean;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

@Data
public class FullCourtTrendBean implements Serializable {
    @JSONField(name = "yesterday")
    private Integer yesterday;

    @JSONField(name = "today")
    private Integer today;

    @JSONField(name = "last_week")
    private Integer lastWeek;

    @JSONField(name = "x_value")
    private String time;
}
