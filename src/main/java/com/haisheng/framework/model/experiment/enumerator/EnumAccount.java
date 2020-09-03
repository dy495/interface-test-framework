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
    XSGWTEMP("xsgwtemp", "e10adc3949ba59abbe56e057f20f883e", "uid_41786c76"),

    /**
     * 销售顾问
     */
    ZDH("zdh", "e10adc3949ba59abbe56e057f20f883e", "uid_8861b7fd"),

    /**
     * 销售顾问
     */
    ZDHCS("zdhcs", "e10adc3949ba59abbe56e057f20f883e", ""),

    /**
     * 销售总监
     */
    XSZJ("xszj", "e10adc3949ba59abbe56e057f20f883e", "uid_c643c797"),

    /**
     * 总经理
     */
    ZJL("zjl", "e10adc3949ba59abbe56e057f20f883e", "uid_d3d35577"),

    /**
     * 前台
     */
    QT("qt", "e10adc3949ba59abbe56e057f20f883e", "uid_05e8599f"),

    /**
     * cpxsgw
     */
    CPXSGW("cpxsgw", "e10adc3949ba59abbe56e057f20f883e", "uid_3dd67680"),

    /**
     * 管理员
     */
    BAOSHIJIE("baoshijie", "e10adc3949ba59abbe56e057f20f883e", "uid_827f10a3");

    EnumAccount(String username, String password, String uid) {
        this.username = username;
        this.password = password;
        this.uid = uid;
    }

    @Getter
    private final String username;

    @Getter
    private final String password;

    @Getter
    private final String uid;

}


