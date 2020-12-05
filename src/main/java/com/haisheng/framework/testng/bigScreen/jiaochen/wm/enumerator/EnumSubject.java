package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator;

import lombok.Getter;

/**
 * 主体类型
 */
public enum EnumSubject {

    CURRENT(0, "通用", "集团管理"),
    BRAND(1, "品牌", "品牌管理"),
    REGION(2, "区域", "区域管理"),
    STORE(3, "门店", "门店管理"),
    ;

    EnumSubject(int status, String subject, String name) {
        this.status = status;
        this.subject = subject;
        this.name = name;
    }

    @Getter
    private final int status;
    @Getter
    private final String subject;
    @Getter
    private final String name;
}
