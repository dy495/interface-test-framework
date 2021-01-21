package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.youzan;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author wangmin
 * @date 2021/1/19 19:34
 * 订单状态
 */
public enum OrderStatusEnum {

    /**
     * 等待买家付款
     */
    WAIT_BUYER_PAY ("等待买家付款"),
    /**
     * 订单支付成功
     */
    TRADE_PAID("订单已支付"),
    /**
     * ，包含待成团、待接单等等。即：买家已付款，等待成团或等待接单
     */
    WAIT_CONFIRM("待确认"),
    /**
     * 买家已付款
     */
    WAIT_SELLER_SEND_GOODS("等待卖家发货"),

    /**
     * 卖家已发货
     */
    WAIT_BUYER_CONFIRM_GOODS ("等待买家确认收货") ,
    TRADE_SUCCESS("买家已签收以及订单成功"),
    TRADE_CLOSED("交易关闭");

    private String value;

    OrderStatusEnum(String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static OrderStatusEnum findByName(String value) {
        Optional<OrderStatusEnum> any = Arrays.stream(values()).filter(t -> t.getValue().equals(value)).findAny();
        return any.orElse(null);
    }

}
