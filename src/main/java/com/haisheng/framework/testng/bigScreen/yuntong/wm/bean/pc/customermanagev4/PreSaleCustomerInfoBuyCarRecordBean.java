package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.pc.customermanagev4;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 1.3. 客户详情购车记录(池)v4.0 （2021-05-06 更新）
 *
 * @author wangmin
 * @date 2021-06-01 18:10:53
 */
@Data
public class PreSaleCustomerInfoBuyCarRecordBean implements Serializable {
    /**
     * 描述 当前页
     * 版本 v1.0
     */
    @JSONField(name = "page")
    private Integer page;

    /**
     * 描述 当前页的数量
     * 版本 v1.0
     */
    @JSONField(name = "size")
    private Integer size;

    /**
     * 描述 每页的数量
     * 版本 v1.0
     */
    @JSONField(name = " page_size")
    private Integer  pageSize;

    /**
     * 描述 总数
     * 版本 v1.0
     */
    @JSONField(name = "total")
    private Long total;

    /**
     * 描述 总页数
     * 版本 v1.0
     */
    @JSONField(name = "pages")
    private Integer pages;

    /**
     * 描述 详细数据列表
     * 版本 v1.0
     */
    @JSONField(name = "list")
    private JSONArray list;

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
     * 描述 销售名称
     * 版本 v4.0
     */
    @JSONField(name = "sale_name")
    private String saleName;

    /**
     * 描述 销售门店
     * 版本 v4.0
     */
    @JSONField(name = "shop_name")
    private String shopName;

    /**
     * 描述 购车时间
     * 版本 v4.0
     */
    @JSONField(name = "buy_car_date")
    private String buyCarDate;

    /**
     * 描述 购买车型
     * 版本 v4.0
     */
    @JSONField(name = "car_model_name")
    private String carModelName;

    /**
     * 描述 车辆底盘号
     * 版本 v4.0
     */
    @JSONField(name = "vehicle_chassis_code")
    private String vehicleChassisCode;

}