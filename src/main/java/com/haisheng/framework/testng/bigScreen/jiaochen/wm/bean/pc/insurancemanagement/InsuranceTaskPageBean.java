package com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.insurancemanagement;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 17.13. 投保任务（池）（2021-03-05）
 *
 * @author wangmin
 * @date 2021-04-02 14:47:46
 */
@Data
public class InsuranceTaskPageBean implements Serializable {
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
     * 描述 归属门店
     * 版本 v3.0
     */
    @JSONField(name = "shop_name")
    private String shopName;

    /**
     * 描述 任务时间
     * 版本 v3.0
     */
    @JSONField(name = "task_date")
    private String taskDate;

    /**
     * 描述 客户名称
     * 版本 v3.0
     */
    @JSONField(name = "customer_name")
    private String customerName;

    /**
     * 描述 联系电话
     * 版本 v3.0
     */
    @JSONField(name = "customer_phone")
    private String customerPhone;

    /**
     * 描述 车辆品牌
     * 版本 v3.0
     */
    @JSONField(name = "car_brand_name")
    private String carBrandName;

    /**
     * 描述 车型
     * 版本 v3.0
     */
    @JSONField(name = "car_model_name")
    private String carModelName;

    /**
     * 描述 购车日期
     * 版本 v3.0
     */
    @JSONField(name = "buy_car_date")
    private String buyCarDate;

    /**
     * 描述 是否超时
     * 版本 v3.0
     */
    @JSONField(name = "is_over_time")
    private Boolean isOverTime;

    /**
     * 描述 车牌号码
     * 版本 v3.0
     */
    @JSONField(name = "follow_date")
    private String followDate;

    /**
     * 描述 跟进人员
     * 版本 v3.0
     */
    @JSONField(name = "follow_sales_name")
    private String followSalesName;

    /**
     * 描述 跟进账号
     * 版本 v3.0
     */
    @JSONField(name = "follow_sales_phone")
    private String followSalesPhone;

    /**
     * 描述 推荐类型
     * 版本 v3.0
     */
    @JSONField(name = "recommend_type_name")
    private String recommendTypeName;

    /**
     * 描述 唯一标识id
     * 版本 -
     */
    @JSONField(name = "id")
    private Long id;

}