package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.message;

/**
 * @author : wangmin
 * @date :  2021/3/18  11:27
 */
public enum AppletPopupTypeEnum {
    /**
     * 卡券到账消息
     */
    VOUCHER_RECEIVED(1, "卡券到账", 1000),
    VOUCHER_TO_BE_RECEIVED(2, "卡券待领取", 10000);


    private Integer id;

    private String typeName;

    private Integer priority;

    AppletPopupTypeEnum(Integer id, String typeName, Integer priority) {
        this.id = id;
        this.typeName = typeName;
        this.priority = priority;
    }

    public Integer getId() {
        return id;
    }

    public String getTypeName() {
        return typeName;
    }

    public Integer getPriority() {
        return priority;
    }
}
