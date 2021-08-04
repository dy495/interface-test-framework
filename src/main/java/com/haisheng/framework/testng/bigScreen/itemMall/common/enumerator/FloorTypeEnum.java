package com.haisheng.framework.testng.bigScreen.itemMall.common.enumerator;

import lombok.Getter;

public enum FloorTypeEnum {
    VISIT("VISIT","楼层到访"),
    FLOOR_ENTER("FLOOR_ENTER","爬楼");

    @Getter
    private String floorType;
    @Getter
    private String floorTypeName;
    FloorTypeEnum(String floorType,String floorTypeName){
        this.floorType=floorType;
        this.floorTypeName=floorTypeName;

    }
}
