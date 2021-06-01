package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.mapp.task;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 9.6. app接待分页（谢）（2020-12-15）
 *
 * @author wangmin
 * @date 2021-06-01 18:10:55
 */
@Data
public class AppReceptionPageBean implements Serializable {
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
     * 描述 接待id
     * 版本 v1.0
     */
    @JSONField(name = "id")
    private Long id;

    /**
     * 描述 接待门店id
     * 版本 v1.0
     */
    @JSONField(name = "shop_id")
    private Long shopId;

    /**
     * 描述 接待顾客名称
     * 版本 v1.0
     */
    @JSONField(name = "customer_name")
    private String customerName;

    /**
     * 描述 接待车辆车牌号
     * 版本 v1.0
     */
    @JSONField(name = "plate_number")
    private String plateNumber;

    /**
     * 描述 接待车辆品牌
     * 版本 v1.0
     */
    @JSONField(name = "brand_name")
    private String brandName;

    /**
     * 描述 接待车辆车系名称
     * 版本 v1.0
     */
    @JSONField(name = "car_style_name")
    private String carStyleName;

    /**
     * 描述 接待车辆品牌logo图片地址
     * 版本 v1.0
     */
    @JSONField(name = "car_logo_url")
    private String carLogoUrl;

    /**
     * 描述 接待顾客电话
     * 版本 v1.0
     */
    @JSONField(name = "customer_phone")
    private String customerPhone;

    /**
     * 描述 顾客到达时间
     * 版本 v1.0
     */
    @JSONField(name = "arrive_time")
    private String arriveTime;

    /**
     * 描述 接待时间
     * 版本 v1.0
     */
    @JSONField(name = "reception_time")
    private String receptionTime;

    /**
     * 描述 接待员工id
     * 版本 v1.0
     */
    @JSONField(name = "reception_sale_id")
    private String receptionSaleId;

    /**
     * 描述 接待是否超时
     * 版本 v1.0
     */
    @JSONField(name = "is_overtime")
    private Boolean isOvertime;

    /**
     * 描述 接待状态 IN_SERVICE（"接待中"）
     * 版本 v1.0
     */
    @JSONField(name = "reception_status")
    private String receptionStatus;

    /**
     * 描述 预约客户标识，空则不展示
     * 版本 v2.0
     */
    @JSONField(name = "label")
    private String label;

    /**
     * 描述 是否可变更接待
     * 版本 v2.0
     */
    @JSONField(name = "is_can_change_receptor")
    private Boolean isCanChangeReceptor;

    /**
     * 描述 推荐项目列表
     * 版本 v2.0
     */
    @JSONField(name = "recommended_items")
    private JSONArray recommendedItems;

}