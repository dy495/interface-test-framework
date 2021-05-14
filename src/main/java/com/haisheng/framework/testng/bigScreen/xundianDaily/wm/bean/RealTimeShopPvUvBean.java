package com.haisheng.framework.testng.bigScreen.xundianDaily.wm.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.sun.org.glassfish.gmbal.Description;
import lombok.Data;

import java.io.Serializable;

/**
 * 人次人数数据
 */
@Data
public class RealTimeShopPvUvBean implements Serializable {
    @Description("时间段")
    @JSONField(name = "time")
    private String time;

    @Description("今日人次")
    @JSONField(name = "today_pv")
    private Integer todayPv;

    @Description("今日人数")
    @JSONField(name = "today_uv")
    private Integer todayUv;

    @JSONField(name = "yesterday_pv")
    private Integer yesterdayPv;

    @JSONField(name = "yesterday_uv")
    private Integer yesterdayUv;

}
