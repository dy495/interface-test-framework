package com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.applet;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 小程序邮寄地址
 */
@Data
public class AppletShippingAddress implements Serializable {
    @JSONField(name = "address")
    private String address;
    @JSONField(name = "phone")
    private String phone;
    @JSONField(name = "district_code")
    private String districtCode;
    @JSONField(name = "name")
    private String name;
    @JSONField(name = "id")
    private Integer id;
    @JSONField(name = "postal_code")
    private String postalCode;
    @JSONField(name = "district_code_name")
    private String districtCodeName;
}
