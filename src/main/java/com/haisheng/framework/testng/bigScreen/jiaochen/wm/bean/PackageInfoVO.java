package com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

@Data
public class PackageInfoVO implements Serializable {
    @JSONField(name = "package_name")
    private String packageName;
    @JSONField(name = "id")
    private Long id;
    @JSONField(name = "price")
    private String price;
}
