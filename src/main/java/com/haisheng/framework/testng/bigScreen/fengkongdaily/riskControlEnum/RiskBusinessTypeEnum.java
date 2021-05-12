package com.haisheng.framework.testng.bigScreen.fengkongdaily.riskControlEnum;

public enum RiskBusinessTypeEnum {
    /**
     *
     */
    FIRST_INSPECTION("首次检查","FIRST_INSPECTION"),
    INSURANCE_CONTRACT_CUSTOMERS("保险合同客户","INSURANCE_CONTRACT_CUSTOMERS"),
    PDI_FACTORY("PDI-厂家","PDI_FACTORY"),
    INSURANCE_DIRECT_INDEMNITY("保险直赔","INSURANCE_DIRECT_INDEMNITY"),
    ROUTINE_MAINTENANCE("常规维修","ROUTINE_MAINTENANCE"),
    RESCUE("救援","RESCUE"),
    INSURANCE_MARKETING("保险营销","INSURANCE_MARKETING"),
    GROUP_INTERNAL_MAINTENANCE("集团内部维修","GROUP_INTERNAL_MAINTENANCE"),
    KEY_ACCOUNT_CONTRACT("大客户合同","KEY_ACCOUNT_CONTRACT"),
    ZERO_FEE("零费","ZERO_FEE"),
    WARRANTY_CLAIM("保修索赔","WARRANTY_CLAIM"),
    INSURANCE_OWN_EXPENSE("保险自费","INSURANCE_OWN_EXPENSE"),
    REGULAR_MAINTENANCE("定期保养","REGULAR_MAINTENANCE"),
    FIRST_MAINTENANCE("首保","FIRST_MAINTENANCE"),
    SHOP_MAINTENANCE("本店内修","SHOP_MAINTENANCE"),
    PDI_INNER("PDI-内部","PDI_INNER"),
    INTERNAL_FRICTION("内耗","INTERNAL_FRICTION"),
    SHEET_METAL_SPRAY_OWN_EXPENSE("自费钣喷","SHEET_METAL_SPRAY_OWN_EXPENSE"),
    FREE_INSPECTION("免费检测","FREE_INSPECTION"),
    REWORK_MAINTENANCE("返修","REWORK_MAINTENANCE"),
    DECORATE("装饰","DECORATE"),

    ;

    private String name;
    private String type;

    public String getName() {
        return name;
    }
    public String getType() {
        return type;
    }


    RiskBusinessTypeEnum(String name,String type) {
        this.name = name;
        this.type = type;
    }

}
