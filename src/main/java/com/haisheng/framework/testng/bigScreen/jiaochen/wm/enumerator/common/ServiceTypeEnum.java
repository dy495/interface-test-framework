package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.common;

/**
 * @author : xiezhidong
 * @date :  2021/3/12  14:42
 */
public enum ServiceTypeEnum {
    /**
     * 销售
     */
    PRE_SALES(1, "销售"),
    AFTER_SALE(10, "售后");


    private Integer id;


    private String typeName;

    ServiceTypeEnum(Integer id, String typeName) {
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
