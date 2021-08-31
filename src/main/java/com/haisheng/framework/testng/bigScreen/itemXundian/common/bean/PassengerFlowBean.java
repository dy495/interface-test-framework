package com.haisheng.framework.testng.bigScreen.itemXundian.common.bean;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

@Data
public class PassengerFlowBean implements Serializable {

    @JSONField(name = "id")
    private String id;

    @JSONField(name = "name")
    private String name;

}
