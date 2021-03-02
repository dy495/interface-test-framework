package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.Integral;

/**
 * 订单状态枚举
 *
 * @author wangmin
 * @date 2020/12/17 11:05 AM
 */
public enum OrderStatusEnum {
    /**
     * 未支付
     */
    UNPAID("未支付"),
    /**
     * 待发货
     */
    WAITING("待发货"),
    /**
     * 已取消
     */
    CANCELED("已取消"),
    /**
     * 已发货
     */
    SEND("待收货"),
    /**
     * 已完成
     */
    FINISHED("已完成"),
    /**
     * 交易失败
     */
    LOSE("交易失败");

    private String name;

    OrderStatusEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
