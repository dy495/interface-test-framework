package com.haisheng.framework.testng.commonDataStructure;


import lombok.Data;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

@Data
public class AlarmSummary {

    private String alarmSumLink;
    private String product;
    private String rd;

    private String rgnLink;
    private List<String> passRate; //[85%,15,100];通过率、失败数、总数
}
