package com.haisheng.framework.testng.bigScreen.xundianDaily.wm.bean;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 人次人数数据
 */
@Data
public class RealTimeShopPvUvBean implements Serializable {

    /**
     * 时间段
     */
    @JSONField(name = "time")
    private String time;

    /**
     * 今日人次
     */
    @JSONField(name = "today_pv")
    private Integer todayPv;

    /**
     * 今日人数
     */
    @JSONField(name = "today_uv")
    private Integer todayUv;

    /**
     * 昨日人次
     */
    @JSONField(name = "yesterday_pv")
    private Integer yesterdayPv;

    /**
     * 昨日人数
     */
    @JSONField(name = "yesterday_uv")
    private Integer yesterdayUv;

}
