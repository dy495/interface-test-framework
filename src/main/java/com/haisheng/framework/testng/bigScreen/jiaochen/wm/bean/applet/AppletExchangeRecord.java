package com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.applet;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 小程序积分兑换记录
 */
@Data
public class AppletExchangeRecord implements Serializable {

    /**
     * 订单状态
     */
    @JSONField(name = "exchange_status_name")
    private String exchangeStatusName;

    /**
     * 商品数量
     */
    @JSONField(name = "num")
    private Integer num;

    /**
     * 商品名称
     */
    @JSONField(name = "name")
    private String name;

    /**
     * 订单状态
     */
    @JSONField(name = "exchange_status")
    private String exchangeStatus;

    /**
     * 订单号
     */
    @JSONField(name = "order_id")
    private String orderId;

    /**
     * 商品id
     */
    @JSONField(name = "commodity_id")
    private Long commodityId;

    /**
     * 商品积分
     */
    @JSONField(name = "integral")
    private Integer integral;

}
