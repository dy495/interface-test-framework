package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.sale;

import lombok.Getter;

public enum EnumAccount {

    /**
     * 集团
     */
    GROUP(""),
    /**
     * 区域
     */
    AREA(""),
    /**
     * 门店
     */
    SHOP(""),
    /**
     * 个人
     */
    PERSON("");

    EnumAccount(String account) {
        this.account = account;
    }

    @Getter
    private final String account;

}
