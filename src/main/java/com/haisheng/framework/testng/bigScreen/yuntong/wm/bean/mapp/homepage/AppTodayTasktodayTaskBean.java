package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.mapp.homepage;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 4.1. 今日任务 (池) v1.0
 *
 * @author wangmin
 * @date 2021-06-01 18:39:24
 */
@Data
public class AppTodayTasktodayTaskBean implements Serializable {
    /**
     * 描述 预约分子
     * 版本 v1.0
     */
    @JSONField(name = "surplus_appointment")
    private Long surplusAppointment;

    /**
     * 描述 预约分母
     * 版本 v1.0
     */
    @JSONField(name = "all_appointment")
    private Long allAppointment;

    /**
     * 描述 售后接待分子
     * 版本 v1.0
     */
    @JSONField(name = "after_surplus_reception")
    private Long afterSurplusReception;

    /**
     * 描述 售后接待分母
     * 版本 v1.0
     */
    @JSONField(name = "after_all_reception")
    private Long afterAllReception;

    /**
     * 描述 售前接待分子
     * 版本 v1.0
     */
    @JSONField(name = "pre_surplus_reception")
    private Long preSurplusReception;

    /**
     * 描述 售前接待分母
     * 版本 v1.0
     */
    @JSONField(name = "pre_all_reception")
    private Long preAllReception;

    /**
     * 描述 接待分子
     * 版本 v1.0
     */
    @JSONField(name = "surplus_reception")
    private Long surplusReception;

    /**
     * 描述 接待分母
     * 版本 v1.0
     */
    @JSONField(name = "all_reception")
    private Long allReception;

    /**
     * 描述 跟进分子
     * 版本 v2.0
     */
    @JSONField(name = "surplus_follow")
    private Long surplusFollow;

    /**
     * 描述 跟进分母
     * 版本 v2.0
     */
    @JSONField(name = "all_follow")
    private Long allFollow;

}