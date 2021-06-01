package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.mapp.homepagev4;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 5.1. 今日任务 (池) v4.0
 *
 * @author wangmin
 * @date 2021-06-01 18:10:55
 */
@Data
public class AppTodayTaskBean implements Serializable {
    /**
     * 描述 展示列（部分接口返回列按权限展示时需要）
     * 版本 v4.0
     */
    @JSONField(name = "key_list")
    private JSONArray keyList;

    /**
     * 描述 key名称（展示列名称）
     * 版本 v4.0
     */
    @JSONField(name = "key_name")
    private String keyName;

    /**
     * 描述 key值（实际取值key）
     * 版本 v4.0
     */
    @JSONField(name = "key_value")
    private String keyValue;

    /**
     * 描述 销售预约
     * 版本 v4.0
     */
    @JSONField(name = "pre_appointment")
    private String preAppointment;

    /**
     * 描述 销售接待
     * 版本 v4.0
     */
    @JSONField(name = "pre_reception")
    private String preReception;

    /**
     * 描述 销售跟进
     * 版本 v4.0
     */
    @JSONField(name = "pre_follow")
    private String preFollow;

    /**
     * 描述 售后预约
     * 版本 v4.0
     */
    @JSONField(name = "after_appointment")
    private String afterAppointment;

    /**
     * 描述 售后接待
     * 版本 v4.0
     */
    @JSONField(name = "after_reception")
    private String afterReception;

    /**
     * 描述 售后跟进
     * 版本 v4.0
     */
    @JSONField(name = "after_follow")
    private String afterFollow;

}