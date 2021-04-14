package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.common;

/**
 * @author wangmin
 * @date 2020/11/19  11:42
 */
public enum AuthCodeEnum {
    /**
     * 预约应答权限
     */
    APPOINTMENT_REPLY("BUSINESS_JIAOCHEN_PC_00213"),

    /**
     * 预约应答权限
     */
    APPOINTMENT_OVERTIME_REMIND("BUSINESS_JIAOCHEN_PC_00214"),

    /**
     * 售后接待权限
     */
    AFTER_SALE_RECEPTION("BUSINESS_JIAOCHEN_PC_00211"),

    /**
     * 售后评价差评接收
     */
    AFTER_EVALUATE("BUSINESS_JIAOCHEN_PC_2111"),

    /**
     * 售前评价差评接收
     */
    PRE_EVALUATE("BUSINESS_JIAOCHEN_PC_2211"),

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
     * 核销
     */
    WRITE_OFF("BUSINESS_JIAOCHEN_PC_712"),

    /**
     * 全部数据权限
     */
    ALL_DATA("BUSINESS_JIAOCHEN_PC_00111"),

    /**
     * 导出权限
     */
    EXPORT_FILE("BUSINESS_JIAOCHEN_PC_000212"),

    /**
     * 变更售后接待人员权限
     */
    CHANGE_AFTER_RECEPTIONIST("BUSINESS_JIAOCHEN_PC_0002112"),
    /**
     * 客户积分变更权限
     */
    CUSTOMER_INTEGRAL_CHANGE("BUSINESS_JIAOCHEN_PC_571");

    AuthCodeEnum(String code) {
        this.code = code;
    }

    private String code;

    public String getCode() {
        return code;
    }
}
