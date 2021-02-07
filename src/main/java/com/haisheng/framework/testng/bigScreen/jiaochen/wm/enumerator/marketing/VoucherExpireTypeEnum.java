package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing;

/**
 * @author wangmin
 * @date  2021/1/20  15:28
 */
public enum VoucherExpireTypeEnum {
    /**
     * 时间段
     */
    TIME_RANGE(1, "时间段"),
    EXPIRE_DAYS(2, "有效天数");


    private final Integer id;

    private final String typeName;

    VoucherExpireTypeEnum(Integer id, String typeName) {
        this.id = id;
        this.typeName = typeName;
    }

    public Integer getId() {
        return id;
    }

    public String getTypeName() {
        return typeName;
    }

}
