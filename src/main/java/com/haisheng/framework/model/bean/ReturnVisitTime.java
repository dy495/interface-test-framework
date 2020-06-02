package com.haisheng.framework.model.bean;


import lombok.Data;

import java.sql.Timestamp;

@Data
public class ReturnVisitTime {

    private int shopId = 22728;
    private long customerId;
    private String returnVisitDate;
    private int intervalFromGenerate = 0;

}
