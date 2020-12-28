package com.haisheng.framework.testng.bigScreen.crm.wm.bean;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

@Data
public class ResponseData implements Serializable {
    @JSONField(name = "id")
    public Long id;
    @JSONField(name = "creator")
    public String creator;
}
