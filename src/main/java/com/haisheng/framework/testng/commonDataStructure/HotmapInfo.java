package com.haisheng.framework.testng.commonDataStructure;


import lombok.Data;

import java.util.concurrent.ConcurrentHashMap;

@Data
public class HotmapInfo {
    private String requestId;
    private String time; //current time to the end of hour
    private String generateTime; //current get data time
    private int width;
    private int height;
    private int startX;
    private int startY;
    private String thermalMapData;
//    private int[] thermalMapPoint;
    private int maxValue;
    private ConcurrentHashMap<Axis, Integer> axisHm = new ConcurrentHashMap<>();

}
