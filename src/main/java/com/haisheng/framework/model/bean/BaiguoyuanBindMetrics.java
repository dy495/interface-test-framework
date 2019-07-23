package com.haisheng.framework.model.bean;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

@Data
public class BaiguoyuanBindMetrics {

    private String date;
    private String metrics;
    private float accuracy;
    private String video;
    private String shopId;

}
