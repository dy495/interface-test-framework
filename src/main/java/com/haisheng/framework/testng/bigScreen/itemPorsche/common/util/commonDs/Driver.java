package com.haisheng.framework.testng.bigScreen.itemPorsche.common.util.commonDs;


import lombok.Builder;

@Builder
public class Driver {

    public Long customerId;
    public Long receiptId;
    public String name;
    public String phone;
    public String signDate;
    public String signTime;
    public int auditStatus;
}
