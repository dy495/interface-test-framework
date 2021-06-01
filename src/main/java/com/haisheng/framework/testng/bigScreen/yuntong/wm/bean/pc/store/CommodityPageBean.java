package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.pc.store;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 20.1. 特惠商品 v2.0(池)
 *
 * @author wangmin
 * @date 2021-06-01 18:10:54
 */
@Data
public class CommodityPageBean implements Serializable {
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
     * 描述 商品id
     * 版本 v2.0
     */
    @JSONField(name = "id")
    private Long id;

    /**
     * 描述 商品名称
     * 版本 v2.0
     */
    @JSONField(name = "commodity_name")
    private String commodityName;

    /**
     * 描述 商品规格
     * 版本 v2.0
     */
    @JSONField(name = "commodity_specification")
    private String commoditySpecification;

    /**
     * 描述 配送方式
     * 版本 v2.0
     */
    @JSONField(name = "distribution_manner")
    private String distributionManner;

    /**
     * 描述 卡卷名称
     * 版本 v2.0
     */
    @JSONField(name = "volume_name")
    private String volumeName;

    /**
     * 描述 有效期
     * 版本 v2.0
     */
    @JSONField(name = "period_of_validity")
    private String periodOfValidity;

    /**
     * 描述 商品数量
     * 版本 v2.0
     */
    @JSONField(name = "commodity_amount")
    private String commodityAmount;

    /**
     * 描述 归属
     * 版本 v2.0
     */
    @JSONField(name = "affiliation")
    private String affiliation;

    /**
     * 描述 主体类型
     * 版本 v2.0
     */
    @JSONField(name = "subject_type")
    private String subjectType;

    /**
     * 描述 主体id
     * 版本 v2.0
     */
    @JSONField(name = "subject_id")
    private Long subjectId;

    /**
     * 描述 商品单价
     * 版本 v2.0
     */
    @JSONField(name = "price")
    private Double price;

    /**
     * 描述 商品佣金
     * 版本 v2.0
     */
    @JSONField(name = "commission")
    private Double commission;

    /**
     * 描述 邀请奖励金
     * 版本 v2.0
     */
    @JSONField(name = "invitation_payment")
    private Double invitationPayment;

    /**
     * 描述 商品状态
     * 版本 v2.0
     */
    @JSONField(name = "status")
    private String status;

    /**
     * 描述 商品状态
     * 版本 v2.0
     */
    @JSONField(name = "status_name")
    private String statusName;

    /**
     * 描述 创建时间
     * 版本 v2.0
     */
    @JSONField(name = "create_date")
    private String createDate;

    /**
     * 描述 No comments found.
     * 版本 -
     */
    @JSONField(name = "subject_type_name")
    private String subjectTypeName;

}