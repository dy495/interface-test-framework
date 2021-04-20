package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.message;

/**
 * @author wangmin
 * @date 2021/3/9 5:19 PM
 * @desc
 */
public enum AfterCustomerTypeEnum {
    /**
     * 自售新车6个月未回
     */
    SOLD_WITHOUT_RECOVERED("新车自售未回"),

    /**
     * 一年内有入厂记录
     */
    MANAGE_CUSTOMER("管理内客户"),

    /**
     * 车龄5年以上
     */
    HIGH_DRIVE_CUSTOMER("保有高车龄客户"),

    /**
     * 11个月未回
     */
    LOSS_WARN_CUSTOMER("预警流失客户"),

    /**
     * 12个月未回厂
     */
    LOSS_CUSTOMER("流失客户");

    private String desc;

    AfterCustomerTypeEnum(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
