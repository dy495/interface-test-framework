package com.haisheng.framework.testng.bigScreen.itemMall.common.enumerator;

import lombok.Getter;

public enum SortTypeEnum {
    PASS("PASS","过店人数"),
    ENTRY("ENTRY","进店人数"),
    ENTRY_PERCENTAGE("ENTRY_PERCENTAGE","进店率"),
    VISIT_PERCENTAGE("VISIT_PERCENTAGE","光顾率");

    @Getter
    private String sortType;
    @Getter
    private String sortTypeName;

    SortTypeEnum(String sortType,String sortTypeName){
        this.sortType=sortType;
        this.sortTypeName=sortTypeName;
    }

}
