package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.mapp.followup;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 2.1. app评价跟进列表（谢）（2020-12-15）
 *
 * @author wangmin
 * @date 2021-06-01 18:10:55
 */
@Data
public class AppPageBean implements Serializable {
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
     * 描述 评价记录id
     * 版本 v2.0
     */
    @JSONField(name = "id")
    private Long id;

    /**
     * 描述 评价时间
     * 版本 v2.0
     */
    @JSONField(name = "time")
    private Long time;

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
     * 描述 门店id
     * 版本 v2.0
     */
    @JSONField(name = "shop_id")
    private Long shopId;

    /**
     * 描述 车牌号
     * 版本 v2.0
     */
    @JSONField(name = "plate_number")
    private String plateNumber;

    /**
     * 描述 品牌车系
     * 版本 v2.0
     */
    @JSONField(name = "car_style")
    private String carStyle;

    /**
     * 描述 客户名称
     * 版本 v2.0
     */
    @JSONField(name = "customer_name")
    private String customerName;

    /**
     * 描述 客户手机号
     * 版本 v2.0
     */
    @JSONField(name = "customer_phone")
    private String customerPhone;

    /**
     * 描述 评价时间
     * 版本 v2.0
     */
    @JSONField(name = "evaluate_time")
    private String evaluateTime;

    /**
     * 描述 预约车辆品牌logo图片地址
     * 版本 v1.0
     */
    @JSONField(name = "car_logo_url")
    private String carLogoUrl;

    /**
     * 描述 评价分数
     * 版本 v2.0
     */
    @JSONField(name = "score")
    private Integer score;

    /**
     * 描述 评价内容
     * 版本 v2.0
     */
    @JSONField(name = "suggestion")
    private String suggestion;

    /**
     * 描述 评价标签
     * 版本 v2.0
     */
    @JSONField(name = "labels")
    private JSONArray labels;

    /**
     * 描述 是否超时
     * 版本 v2.0
     */
    @JSONField(name = "is_overtime")
    private Boolean isOvertime;

    /**
     * 描述 故障描述
     * 版本 v3.0
     */
    @JSONField(name = "failure_description")
    private String failureDescription;

    /**
     * 描述 评价.售前或售后
     * 版本 -
     */
    @JSONField(name = "title")
    private String title;

}