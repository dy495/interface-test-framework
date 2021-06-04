package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.app.departmentdata;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 4.1. 数据总览（谢）
 *
 * @author wangmin
 * @date 2021-06-02 17:56:15
 */
@Data
public class AppOverviewBean implements Serializable {
    /**
     * 描述 接待次数
     * 版本 v1.0
     */
    @JSONField(name = "count")
    private Integer count;

    /**
     * 描述 接待总时长（单位min）
     * 版本 v1.0
     */
    @JSONField(name = "total_duration")
    private Integer totalDuration;

    /**
     * 描述 接待平均时长（单位min）
     * 版本 v1.0
     */
    @JSONField(name = "average_duration")
    private Integer averageDuration;

    /**
     * 描述 接待平均分
     * 版本 v1.0
     */
    @JSONField(name = "average_score")
    private Integer averageScore;

    /**
     * 描述 当前月超过的百分比（0~100）
     * 版本 v1.0
     */
    @JSONField(name = "rank")
    private Integer rank;

}