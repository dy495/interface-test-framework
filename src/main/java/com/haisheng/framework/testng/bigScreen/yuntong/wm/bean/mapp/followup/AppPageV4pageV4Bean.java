package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.mapp.followup;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 3.1. app跟进列表（池）v4（2020-05-06）
 *
 * @author wangmin
 * @date 2021-06-01 18:39:24
 */
@Data
public class AppPageV4pageV4Bean implements Serializable {
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
     * 描述 跟进id
     * 版本 v4.0
     */
    @JSONField(name = "id")
    private Long id;

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
     * 描述 卡片title
     * 版本 v3.0
     */
    @JSONField(name = "title")
    private String title;

    /**
     * 描述 客户名称
     * 版本 v4.0
     */
    @JSONField(name = "customer_name")
    private String customerName;

    /**
     * 描述 客户手机号
     * 版本 v4.0
     */
    @JSONField(name = "customer_phone")
    private String customerPhone;

    /**
     * 描述 评价星级
     * 版本 v4.0
     */
    @JSONField(name = "evaluate_level")
    private Integer evaluateLevel;

    /**
     * 描述 是否跟进
     * 版本 v4.0
     */
    @JSONField(name = "is_follow")
    private Boolean isFollow;

}