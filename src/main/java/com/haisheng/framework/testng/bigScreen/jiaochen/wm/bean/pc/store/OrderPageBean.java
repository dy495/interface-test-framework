package com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.store;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 18.8. 商城订单 v2.0(池)
 *
 * @author wangmin
 * @date 2021-04-02 14:47:46
 */
@Data
public class OrderPageBean implements Serializable {
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
     * 描述 订单编号
     * 版本 v2.0
     */
    @JSONField(name = "order_number")
    private String orderNumber;

    /**
     * 描述 商品名称
     * 版本 v2.0
     */
    @JSONField(name = "commodity_name")
    private String commodityName;

    /**
     * 描述 支付时间
     * 版本 v2.0
     */
    @JSONField(name = "pay_time")
    private String payTime;

    /**
     * 描述 订单状态
     * 版本 v2.0
     */
    @JSONField(name = "order_status")
    private String orderStatus;

    /**
     * 描述 订单状态
     * 版本 v2.0
     */
    @JSONField(name = "order_status_name")
    private String orderStatusName;

    /**
     * 描述 商品规格
     * 版本 v2.0
     */
    @JSONField(name = "commodity_specification")
    private String commoditySpecification;

    /**
     * 描述 金额
     * 版本 v2.0
     */
    @JSONField(name = "order_money")
    private Double orderMoney;

    /**
     * 描述 购买数量
     * 版本 v2.0
     */
    @JSONField(name = "purchase_number")
    private Integer purchaseNumber;

    /**
     * 描述 配送方式
     * 版本 v2.0
     */
    @JSONField(name = "distribution_manner")
    private String distributionManner;

    /**
     * 描述 收货人
     * 版本 v2.0
     */
    @JSONField(name = "consignee")
    private String consignee;

    /**
     * 描述 收货地址
     * 版本 v2.0
     */
    @JSONField(name = "consignee_address")
    private String consigneeAddress;

    /**
     * 描述 联系方式
     * 版本 v2.0
     */
    @JSONField(name = "customer_phone")
    private String customerPhone;

    /**
     * 描述 分销员手机号
     * 版本 v2.0
     */
    @JSONField(name = "sales_phone")
    private String salesPhone;

    /**
     * 描述 分销员
     * 版本 v2.0
     */
    @JSONField(name = "sales_name")
    private String salesName;

    /**
     * 描述 分销门店
     * 版本 v2.0
     */
    @JSONField(name = "sales_shop_name")
    private String salesShopName;

    /**
     * 描述 分销佣金
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
     * 描述 快递单号
     * 版本 v2.0
     */
    @JSONField(name = "express_number")
    private String expressNumber;

    /**
     * 描述 唯一标识
     * 版本 v2.0
     */
    @JSONField(name = "id")
    private Long id;

    /**
     * 描述 发卷状态 SENT 已发 NOT_SENT 未发
     * 版本 v2.0
     */
    @JSONField(name = "volume_status")
    private String volumeStatus;

    /**
     * 描述 发卷状态 SENT 已发 NOT_SENT 未发
     * 版本 v2.0
     */
    @JSONField(name = "volume_status_name")
    private String volumeStatusName;

    /**
     * 描述 备注
     * 版本 v2.0
     */
    @JSONField(name = "remark")
    private String remark;

    /**
     * 描述 订单绑定手机号
     * 版本 -
     */
    @JSONField(name = "phone")
    private String phone;

}