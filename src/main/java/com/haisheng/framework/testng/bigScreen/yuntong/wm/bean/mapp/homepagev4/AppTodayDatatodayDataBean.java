package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.mapp.homepagev4;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 5.2. 今日数据 (池) v4.0
 *
 * @author wangmin
 * @date 2021-06-01 18:39:24
 */
@Data
public class AppTodayDatatodayDataBean implements Serializable {
    /**
     * 描述 总数 首次查询或刷新时返回
     * 版本 v1.0
     */
    @JSONField(name = "total")
    private Long total;

    /**
     * 描述 本次查询最后一条数据主键
     * 版本 v1.0
     */
    @JSONField(name = "last_value")
    private JSONObject lastValue;

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
     * 描述 返回的结果list
     * 版本 v1.0
     */
    @JSONField(name = "list")
    private JSONArray list;

    /**
     * 描述 唯一表示
     * 版本 v1.0
     */
    @JSONField(name = "id")
    private String id;

    /**
     * 描述 名称
     * 版本 v1.0
     */
    @JSONField(name = "name")
    private String name;

    /**
     * 描述 销售待处理接待
     * 版本 v1.0
     */
    @JSONField(name = "pre_pending_appointment")
    private String prePendingAppointment;

    /**
     * 描述 销售待处理接待
     * 版本 v1.0
     */
    @JSONField(name = "pre_pending_reception")
    private String prePendingReception;

    /**
     * 描述 销售待处理跟进
     * 版本 v2.0
     */
    @JSONField(name = "pre_pending_follow")
    private String prePendingFollow;

    /**
     * 描述 售后待处理接待
     * 版本 v1.0
     */
    @JSONField(name = "after_pending_appointment")
    private String afterPendingAppointment;

    /**
     * 描述 售后待处理接待
     * 版本 v1.0
     */
    @JSONField(name = "after_pending_reception")
    private String afterPendingReception;

    /**
     * 描述 售后待处理跟进
     * 版本 v2.0
     */
    @JSONField(name = "after_pending_follow")
    private String afterPendingFollow;

}