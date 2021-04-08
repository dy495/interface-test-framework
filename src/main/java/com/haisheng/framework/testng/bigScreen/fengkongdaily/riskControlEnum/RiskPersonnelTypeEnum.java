package com.haisheng.framework.testng.bigScreen.fengkongdaily.riskControlEnum;

public enum RiskPersonnelTypeEnum {
    /**
     * 黑名单风控
     */
    BLACK("黑名单","BLACK"),
    /**
     * 白名单
     */
    WHITE("白名单","WHITE"),
    /**
     * 重点观察人员
     */
    FOCUS("重点观察人员","FOCUS");

    private final String name;

    private final String type;

    RiskPersonnelTypeEnum(String name, String type) {
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
