package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.mapp.presalesreception;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 7.13. 客户接待记录列表（杨）v4.0 （2021-05-18）
 *
 * @author wangmin
 * @date 2021-06-01 18:39:24
 */
@Data
public class AppListV4listV4Bean implements Serializable {
    /**
     * 描述 数据列表
     * 版本 v1.0
     */
    @JSONField(name = "list")
    private JSONArray list;

    /**
     * 描述 车辆id
     * 版本 v4.0
     */
    @JSONField(name = "id")
    private Long id;

    /**
     * 描述 顾客Id
     * 版本 v4.0
     */
    @JSONField(name = "customer_id")
    private Long customerId;

    /**
     * 描述 记录创建时间
     * 版本 v4.0
     */
    @JSONField(name = "record_create_date")
    private String recordCreateDate;

    /**
     * 描述 接待类型
     * 版本 v4.0
     */
    @JSONField(name = "type")
    private String type;

    /**
     * 描述 销售门店
     * 版本 v3.0
     */
    @JSONField(name = "shop_name")
    private String shopName;

    /**
     * 描述 销售顾问
     * 版本 v3.0
     */
    @JSONField(name = "sale_name")
    private String saleName;

    /**
     * 描述 购车时间
     * 版本 v3.0
     */
    @JSONField(name = "buy_car_time")
    private String buyCarTime;

    /**
     * 描述 购买车型名
     * 版本 v3.0
     */
    @JSONField(name = "car_model_name")
    private String carModelName;

    /**
     * 描述 购买车型id
     * 版本 v3.0
     */
    @JSONField(name = "car_model_id")
    private Long carModelId;

}