package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.mapp.customermanager;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 1.1. app销售客户列表（杨）v4.0（2021-05-06）
 *
 * @author wangmin
 * @date 2021-06-01 18:39:24
 */
@Data
public class AppPreCustomerpreCustomerBean implements Serializable {
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
     * 描述 顾客Id
     * 版本 v4.0
     */
    @JSONField(name = "customer_id")
    private Long customerId;

    /**
     * 描述 门店Id
     * 版本 v4.0
     */
    @JSONField(name = "shop_id")
    private Long shopId;

    /**
     * 描述 头像
     * 版本 v4.0
     */
    @JSONField(name = "face_url")
    private String faceUrl;

    /**
     * 描述 客户名称
     * 版本 v4.0
     */
    @JSONField(name = "customer_name")
    private String customerName;

    /**
     * 描述 意向车型
     * 版本 v4.0
     */
    @JSONField(name = "intention_car_model_Name")
    private Long intentionCarModelName;

    /**
     * 描述 电话号码
     * 版本 v4.0
     */
    @JSONField(name = "customer_phone")
    private String customerPhone;

    /**
     * 描述 所属销售
     * 版本 v4.0
     */
    @JSONField(name = "sales_name")
    private String salesName;

    /**
     * 描述 接待销售
     * 版本 v4.0
     */
    @JSONField(name = "reception_sales_name")
    private String receptionSalesName;

    /**
     * 描述 接待日期
     * 版本 v4.0
     */
    @JSONField(name = "reception_date")
    private String receptionDate;

    /**
     * 描述 预计购车日期
     * 版本 v4.0
     */
    @JSONField(name = "estimate_buy_car_date")
    private String estimateBuyCarDate;

    /**
     * 描述 顾客归属标签
     * 版本 v4.0
     */
    @JSONField(name = "customer_belong_labels")
    private JSONArray customerBelongLabels;

}