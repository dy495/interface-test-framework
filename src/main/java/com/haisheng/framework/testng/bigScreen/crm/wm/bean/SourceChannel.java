package com.haisheng.framework.testng.bigScreen.crm.wm.bean;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

@Data
public class SourceChannel implements Serializable {
    @JSONField(name = "channel_name")
    private String channelName;
    @JSONField(name = "percent")
    private String percent;
    @JSONField(name = "value")
    private Integer value;
    @JSONField(name = "percentage")
    private Double percentage;
}
