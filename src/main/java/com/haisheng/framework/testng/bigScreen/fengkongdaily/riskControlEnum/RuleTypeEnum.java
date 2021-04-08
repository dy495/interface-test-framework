package com.haisheng.framework.testng.bigScreen.fengkongdaily.riskControlEnum;

public enum RuleTypeEnum {
    /**
     * 单一客户订单数量监控--同一客户在设定的连续天数内的下单数量上限,既一人多单
     */
    RISK_SINGLE_MEMBER_ORDER_QUANTITY("一人多单","RISK_SINGLE_MEMBER_ORDER_QUANTITY"),

    /**
     * 无人支付订单监控--识别订单发生时间，结算位置无人存在
     */
    UNMANNED_ORDER("无人支付订单监控","UNMANNED_ORDER"),

    /**
     * 员工支付订单监控--识别订单发生时间，结算位置仅有员工存在，锁定结算动作员工，跨店统计员
     */
    EMPLOYEE_ORDER("员工支付订单监控","EMPLOYEE_ORDER"),

    /**
     * 单一顾客多台车支付监控(一人多车)--同一客户为多台车支付的上限,既一人多车
     */
    RISK_SINGLE_MEMBER_CAR_QUANTITY("一人多车","RISK_SINGLE_MEMBER_CAR_QUANTITY"),


    /**
     * 单一车辆被多人支付监控(一车多人)--同一车辆被多人支付的上限,既车多人
     */
    RISK_SINGLE_CAR_TRANSACTION_QUANTITY("一车多人","RISK_SINGLE_CAR_TRANSACTION_QUANTITY");

    private final String name;

    private final String type;

    RuleTypeEnum(String name, String type) {
        this.type = type;
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public String getType() {
        return type;
    }
}
