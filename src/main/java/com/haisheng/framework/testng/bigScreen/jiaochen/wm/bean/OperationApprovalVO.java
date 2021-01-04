package com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

@Data
public class OperationApprovalVO implements Serializable {
    @JSONField(name = "id")
    private Long id;
    @JSONField(name = "status_name")
    private String statusName;
    @JSONField(name = "customer_name")
    private String customerName;
    @JSONField(name = "phone")
    private String phone;
    @JSONField(name = "register_time")
    private String registerTime;
    @JSONField(name = "title")
    private String title;
    @JSONField(name = "passed_date")
    private String passedDate;
    @JSONField(name = "user_account_name")
    private String userAccountName;
}
