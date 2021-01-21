package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.reception;

/**
 * @author wangmin
 * @date  2020/7/4  14:18
 */
public enum CustomerSaleTypeEnum {
    /**
     * 售前
     */
    PRE_SALES("销售"," "),
    AFTER_SALES("售后","尊敬的保时捷会员"),
    ALL("全部","");

    private String name;

    private String smsCallName;

    CustomerSaleTypeEnum(String name, String smsCallName) {
        this.name = name;
        this.smsCallName = smsCallName;
    }

    public String getName() {
        return name;
    }

    public String getSmsCallName() {
        return smsCallName;
    }

    public static CustomerSaleTypeEnum findByName(String typeName) {
        if (PRE_SALES.name().equals(typeName)) {
            return PRE_SALES;
        }
        if (AFTER_SALES.name().equals(typeName)) {
            return AFTER_SALES;
        }
        if (ALL.name().equals(typeName)){
            return ALL;
        }
        throw new IllegalArgumentException("顾客售前/售后类型不存在");
    }


}
