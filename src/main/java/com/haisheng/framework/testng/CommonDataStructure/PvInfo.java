package com.haisheng.framework.testng.CommonDataStructure;


import lombok.Data;
import java.util.concurrent.ConcurrentHashMap;

@Data
public class PvInfo {
    private String startTime;
    private String endTime;
    private String requestId; //请求id
    private String uid;
    private int total; //enter num + leave num

    //region_id -> num
    private ConcurrentHashMap<String, Integer> stayHm;
    //enter unit -> num
    private ConcurrentHashMap<RegionEntranceUnit, Integer> unitHm;

}
