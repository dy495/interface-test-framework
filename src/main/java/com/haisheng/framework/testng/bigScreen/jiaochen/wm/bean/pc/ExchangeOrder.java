package com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 积分订单页
 *
 * @author wangmin
 * @date 2021/3/1 19:06
 */
@Data
public class ExchangeOrder implements Serializable {
    /**
     * 商品名称
     */
    @JSONField(name = "goods_name")
    private String goodsName;

    /**
     * 收货地址
     */
    @JSONField(name = "address")
    private String address;


    /**
     * 会员手机号
     */
    @JSONField(name = "member_phone")
    private String memberPhone;


    /**
     * 兑换数量
     */
    @JSONField(name = "num")
    private Integer num;

    /**
     * 收货人电话
     */
    @JSONField(name = "customer_phone")
    private String customerPhone;

    /**
     * 兑换类型
     */
    @JSONField(name = "exchange_type_name")
    private String exchangeTypeName;

    /**
     * 会员名称
     */
    @JSONField(name = "member_name")
    private String memberName;


    /**
     * 可否取消
     */
    @JSONField(name = "is_cancel")
    private Boolean isCancel;

    /**
     * 兑换时间
     */
    @JSONField(name = "exchange_time")
    private String exchangeTime;

    /**
     * 订单状态
     */
    @JSONField(name = "order_status")
    private String orderStatus;

    /**
     * 订单状态
     */
    @JSONField(name = "order_status_name")
    private String orderStatusName;

    /**
     * 可否发货
     */
    @JSONField(name = "is_confirm_shipment")
    private Boolean isConfirmShipment;

    /**
     * 收货人姓名
     */
    @JSONField(name = "customer_name")
    private String customerName;

    /**
     * 商品分类
     */
    @JSONField(name = "goods_type")
    private String goodsType;

    /**
     * 订单号
     */
    @JSONField(name = "order_id")
    private String orderId;

    @JSONField(name = "id")
    private Long id;

}
