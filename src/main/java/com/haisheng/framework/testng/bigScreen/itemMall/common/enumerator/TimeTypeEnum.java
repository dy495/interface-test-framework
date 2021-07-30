package com.haisheng.framework.testng.bigScreen.itemMall.common.enumerator;

import lombok.Getter;

public enum TimeTypeEnum {
    DAY("DAY","按天查询"),
    WEEK("WEEK","按周查询"),
    MONTH("MONTH","按月查询"),
    CYCLE("CYCLE","自定义查询");

    @Getter
    private String timeType;
    @Getter
    private String timeTypeName;

    TimeTypeEnum(String timeType,String timeTypeName){
        this.timeType=timeType;
        this.timeTypeName=timeTypeName;

    }
}
