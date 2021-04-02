package com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.consultmanagement;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 8.4. 在线专家咨询列表 （池）(2021-03-08)
 *
 * @author wangmin
 * @date 2021-04-02 14:47:46
 */
@Data
public class OnlineExpertsPageListBean implements Serializable {
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
     * 描述 咨询时间
     * 版本 v3.0
     */
    @JSONField(name = "consult_date")
    private String consultDate;

    /**
     * 描述 联系人
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
    @JSONField(name = "brand_name")
    private String brandName;

    /**
     * 描述 车型
     * 版本 v3.0
     */
    @JSONField(name = "model_name")
    private String modelName;

    /**
     * 描述 是否超时
     * 版本 v3.0
     */
    @JSONField(name = "is_over_time")
    private String isOverTime;

    /**
     * 描述 跟进时间
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
    @JSONField(name = "follow_login_name")
    private String followLoginName;

    /**
     * 描述 唯一id
     * 版本 v3.0
     */
    @JSONField(name = "id")
    private Long id;

}