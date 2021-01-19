package com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

@Data
public class VoucherApply implements Serializable {
    @JSONField(name = "status_name")
    private String statusName;
    @JSONField(name = "name")
    private String name;
    @JSONField(name = "id")
    private Long id;
}
