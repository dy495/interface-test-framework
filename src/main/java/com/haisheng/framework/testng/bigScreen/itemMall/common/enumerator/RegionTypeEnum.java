package com.haisheng.framework.testng.bigScreen.itemMall.common.enumerator;

import lombok.Getter;

public enum RegionTypeEnum {
    EXIT("EXIT","停车场"),
    PARKING("PARKING","楼层出入口"),
    FLOOR("FLOOR","楼层");

    @Getter
    private String region;
    @Getter
    private String regionName;

    RegionTypeEnum(String region,String regionName){
        this.region=region;
        this.regionName=regionName;
    }
}
