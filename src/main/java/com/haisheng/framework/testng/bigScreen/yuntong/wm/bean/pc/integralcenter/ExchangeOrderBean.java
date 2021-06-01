package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.pc.integralcenter;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 37.17. 积分兑换订单 (张小龙) v2.0
 *
 * @author wangmin
 * @date 2021-06-01 18:10:55
 */
@Data
public class ExchangeOrderBean implements Serializable {
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
     * 描述 订单记录唯一id
     * 版本 v2.0
     */
    @JSONField(name = "id")
    private Long id;

    /**
     * 描述 订单id
     * 版本 v2.0
     */
    @JSONField(name = "order_id")
    private String orderId;

    /**
     * 描述 商品名称
     * 版本 v2.0
     */
    @JSONField(name = "goods_name")
    private String goodsName;

    /**
     * 描述 商品分类
     * 版本 v2.0
     */
    @JSONField(name = "goods_type")
    private String goodsType;

    /**
     * 描述 会员名称
     * 版本 v2.0
     */
    @JSONField(name = "member_name")
    private String memberName;

    /**
     * 描述 会员手机号
     * 版本 v2.0
     */
    @JSONField(name = "member_phone")
    private String memberPhone;

    /**
     * 描述 兑换数量
     * 版本 v2.0
     */
    @JSONField(name = "num")
    private Integer num;

    /**
     * 描述 兑换时间
     * 版本 v2.0
     */
    @JSONField(name = "exchange_time")
    private String exchangeTime;

    /**
     * 描述 订单状态 UNPAID("未支付"),WAITING("待发货"),CANCELED("已取消"),FINISHED("已完成"),LOSE("交易失败")
     * 版本 v2.0
     */
    @JSONField(name = "order_status")
    private String orderStatus;

    /**
     * 描述 订单状态 UNPAID("未支付"),WAITING("待发货"),CANCELED("已取消"),FINISHED("已完成"),LOSE("交易失败")
     * 版本 v2.0
     */
    @JSONField(name = "order_status_name")
    private String orderStatusName;

    /**
     * 描述 收货地址
     * 版本 v2.0
     */
    @JSONField(name = "address")
    private String address;

    /**
     * 描述 收货人姓名
     * 版本 -
     */
    @JSONField(name = "customer_name")
    private String customerName;

    /**
     * 描述 收货人电话
     * 版本 v2.0
     */
    @JSONField(name = "customer_phone")
    private String customerPhone;

    /**
     * 描述 是否显示确认发货
     * 版本 v2.0
     */
    @JSONField(name = "is_confirm_shipment")
    private Boolean isConfirmShipment;

    /**
     * 描述 是否显示取消按钮
     * 版本 v2.0
     */
    @JSONField(name = "is_cancel")
    private Boolean isCancel;

}