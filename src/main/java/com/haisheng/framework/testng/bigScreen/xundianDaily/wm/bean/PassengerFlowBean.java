package com.haisheng.framework.testng.bigScreen.xundianDaily.wm.bean;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

@Data
public class PassengerFlowBean implements Serializable {

    @JSONField(name = "id")
    private Long id;

}
