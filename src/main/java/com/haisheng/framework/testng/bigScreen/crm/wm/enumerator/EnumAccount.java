package com.haisheng.framework.testng.bigScreen.crm.wm.enumerator;

import com.haisheng.framework.util.MD5Util;
import lombok.Getter;

/**
 * 人员账号
 * 账号、密码、uid、环境
 *
 * @author wangmin
 * @date 2020/7/24 16:54
 */
public enum EnumAccount {
    /**
     * 销售顾问
     */
    XSGW("xsgwtemp", "e10adc3949ba59abbe56e057f20f883e", "uid_41786c76", "daily"),

    /**
     * 销售顾问
     */
    ZDHCS("zdhcs", "e10adc3949ba59abbe56e057f20f883e", "", "daily"),

    /**
     * 总经理
     */
    ZJL("zjl", "e10adc3949ba59abbe56e057f20f883e", "uid_d3d35577", "daily"),

    /**
     * 前台
     */
    QT("qt", "e10adc3949ba59abbe56e057f20f883e", "uid_05e8599f", "daily"),

    /**
     * 服务总监
     */
    FWZJ("fwzj", "e10adc3949ba59abbe56e057f20f883e", "uid_d3fcde5c", "daily"),

    /**
     * 管理员
     */
    BAOSHIJIE("baoshijie", "e10adc3949ba59abbe56e057f20f883e", "uid_827f10a3", "daily"),


    /**
     * 保时捷线上销售顾问
     */
    XSGW_ONLINE("11", new MD5Util().getMD5("11"), "uid_c01f9419", "online"),

    /**
     * 保时捷线上总经
     */
    ZJL_ONLINE("zjl", new MD5Util().getMD5("zjl"), "uid_a6452755", "online");

    EnumAccount(String username, String password, String uid, String environment) {
        this.username = username;
        this.password = password;
        this.uid = uid;
        this.environment = environment;
    }

    @Getter
    private final String username;

    @Getter
    private final String password;

    @Getter
    private final String uid;

    @Getter
    private final String environment;
}


