package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.common;

/**
 * @author wangmin
 * @date  2020/11/19  11:42
 */
public enum AuthCodeEnum {
    /**
     * 预约应答权限
     */
    APPOINTMENT_REPLY("BUSINESS_JIAOCHEN_PC_00213"),
    /**
     * 售后接待权限
     */
    AFTER_SALE_RECEPTION("BUSINESS_JIAOCHEN_PC_00211"),

    /**
     * 售前接待权限
     */
    PRE_SALE_RECEPTION("BUSINESS_JIAOCHEN_PC_31"),

    /**
     * 接收保养分配权限
     */
    MAINTAIN_DISTRIBUTION("BUSINESS_JIAOCHEN_PC_00212"),

    /**
     * 通用主体
     */
    GROUP_ENTITY("BUSINESS_JIAOCHEN_PC_00311"),

    /**
     * 区域主体
     */
    REGION_ENTITY("BUSINESS_JIAOCHEN_PC_00312"),

    /**
     * 品牌主体
     */
    BRAND_ENTITY("BUSINESS_JIAOCHEN_PC_00313"),

    /**
     * 门店主体
     */
    SHOP_ENTITY("BUSINESS_JIAOCHEN_PC_00314"),

    /**
     * 核销人员
     */
    VERIFY_PEOPLE("BUSINESS_JIAOCHEN_PC_94"),

    /**
     * 全部数据权限
     */
    ALL_DATA("BUSINESS_JIAOCHEN_PC_00111");

    AuthCodeEnum(String code) {
        this.code = code;
    }

    private String code;

    public String getCode() {
        return code;
    }
}
