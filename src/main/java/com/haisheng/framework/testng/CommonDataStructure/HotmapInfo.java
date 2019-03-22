package com.haisheng.framework.testng.CommonDataStructure;


import lombok.Data;

@Data
public class HotmapInfo {
    private String requestId;
    private String time; //current time to the end of hour
    private String generateTime; //current get data time
    private String width;
    private String height;
    private String startX;
    private String startY;
    private String thermalMapData;
    private int[] thermalMapPoint;
    private String maxValue;

}
