package com.haisheng.framework.model.experiment.enumerator;

import lombok.Getter;

/**
 * @author wangmin
 * @date 2020/7/24 16:54
 */
public enum EnumAccount {
    /**
     * 销售顾问
     */
    XSGWTEMP("xsgwtemp", "e10adc3949ba59abbe56e057f20f883e"),

    /**
     * 销售顾问
     */
    ZDH("zdh", "e10adc3949ba59abbe56e057f20f883e"),

    /**
     * 销售顾问
     */
    ZDHCS("zdhcs", "e10adc3949ba59abbe56e057f20f883e"),

    /**
     * 销售总监
     */
    XSZJ("xszj", "e10adc3949ba59abbe56e057f20f883e"),

    /**
     * 前台
     */
    QT("qt", "e10adc3949ba59abbe56e057f20f883e"),

    /**
     * 管理员
     */
    BAOSHIJIE("baoshijie", "e10adc3949ba59abbe56e057f20f883e");

    EnumAccount(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Getter
    private final String username;

    @Getter
    private final String password;

}
