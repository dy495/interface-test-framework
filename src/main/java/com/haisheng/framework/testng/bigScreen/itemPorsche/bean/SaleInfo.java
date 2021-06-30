package com.haisheng.framework.testng.bigScreen.itemPorsche.bean;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

@Data
public class SaleInfo implements Serializable {
    @JSONField(name = "user_id")
    private String userId;
    @JSONField(name = "user_name")
    private String userName;
    @JSONField(name = "user_login_name")
    private String account;
}
