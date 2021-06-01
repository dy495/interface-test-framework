package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.pc.manage;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 18.1. 评价记录分页 （谢）v3.0（2021-03-12）
 *
 * @author wangmin
 * @date 2021-06-01 18:10:54
 */
@Data
public class EvaluatePageBean implements Serializable {
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
     * 描述 评价id
     * 版本 v2.0
     */
    @JSONField(name = "id")
    private Long id;

    /**
     * 描述 归属门店id
     * 版本 v2.0
     */
    @JSONField(name = "shop_id")
    private Long shopId;

    /**
     * 描述 归属门店名称
     * 版本 v2.0
     */
    @JSONField(name = "shop_name")
    private String shopName;

    /**
     * 描述 评价时间
     * 版本 v2.0
     */
    @JSONField(name = "evaluate_time")
    private String evaluateTime;

    /**
     * 描述 来源项生成时间（任务时间）
     * 版本 v2.0
     */
    @JSONField(name = "source_create_time")
    private String sourceCreateTime;

    /**
     * 描述 客户名称
     * 版本 v2.0
     */
    @JSONField(name = "customer_name")
    private String customerName;

    /**
     * 描述 客户联系方式
     * 版本 v2.0
     */
    @JSONField(name = "customer_phone")
    private String customerPhone;

    /**
     * 描述 车牌号
     * 版本 v2.0
     */
    @JSONField(name = "plate_number")
    private String plateNumber;

    /**
     * 描述 车系
     * 版本 v2.0
     */
    @JSONField(name = "car_style_name")
    private String carStyleName;

    /**
     * 描述 客户经理
     * 版本 v2.0
     */
    @JSONField(name = "service_sale_name")
    private String serviceSaleName;

    /**
     * 描述 品牌
     * 版本 v2.0
     */
    @JSONField(name = "brand_name")
    private String brandName;

    /**
     * 描述 开单手机号
     * 版本 v2.0
     */
    @JSONField(name = "order_phone")
    private String orderPhone;

    /**
     * 描述 评价星级 （1-5）
     * 版本 v2.0
     */
    @JSONField(name = "score")
    private Integer score;

    /**
     * 描述 意见建议 （留言）
     * 版本 v2.0
     */
    @JSONField(name = "suggestion")
    private String suggestion;

    /**
     * 描述 跟进备注
     * 版本 v2.0
     */
    @JSONField(name = "follow_up_remark")
    private String followUpRemark;

    /**
     * 描述 跟进时间
     * 版本 v2.0
     */
    @JSONField(name = "follow_up_time")
    private String followUpTime;

    /**
     * 描述 是否可跟进
     * 版本 v2.0
     */
    @JSONField(name = "is_can_follow_up")
    private Boolean isCanFollowUp;

    /**
     * 描述 分数提示语
     * 版本 v2.0
     */
    @JSONField(name = "describe")
    private String describe;

    /**
     * 描述 选中标签
     * 版本 v2.0
     */
    @JSONField(name = "labels")
    private JSONArray labels;

}