package com.haisheng.framework.testng.bigScreen.fengkongdaily.riskControlEnum;

public enum RuleEnum {
    /**
     * 黑名单风控
     */
    BLACK_LIST("黑名单风控","BLACK_LIST"),
    /**
     * 重点观察人员风控
     */
    FOCUS_LIST("重点观察人员风控","FOCUS_LIST"),
    /**
     * 收银风控
     */
    CASHIER("收银风控","CASHIER");

    private final String name;

    private final String type;

    RuleEnum(String name, String type) {
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
