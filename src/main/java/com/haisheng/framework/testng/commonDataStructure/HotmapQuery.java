package com.haisheng.framework.testng.commonDataStructure;

public class HotmapQuery {

    public String regionId;
    public String startTime;
    public String endTime;
    public boolean isRealTime;

    public HotmapQuery(String regionId, String startTime, String endTime, boolean isRealTime) {
        this.regionId = regionId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isRealTime = isRealTime;
    }

}
