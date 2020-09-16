package com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.sale;

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
    XSGW_DAILY("xsgwtemp", "e10adc3949ba59abbe56e057f20f883e", "uid_41786c76", "daily", "销售顾问temp"),

    /**
     * 销售顾问
     */
    XS_DAILY("xs", "e10adc3949ba59abbe56e057f20f883e", "", "daily", "xs"),

    /**
     * 总经理
     */
    ZJL_DAILY("zjl", "e10adc3949ba59abbe56e057f20f883e", "uid_d3d35577", "daily", ""),

    /**
     * 前台
     */
    QT_DAILY("qt", "e10adc3949ba59abbe56e057f20f883e", "uid_05e8599f", "daily", ""),

    /**
     * 服务总监
     */
    FWZJ_DAILY("fwzj", "e10adc3949ba59abbe56e057f20f883e", "uid_d3fcde5c", "daily", ""),

    /**
     * 管理员
     */
    BAOSHIJIE_DAILY("baoshijie", "e10adc3949ba59abbe56e057f20f883e", "uid_827f10a3", "daily", ""),


    /**
     * 保时捷线上销售顾问
     */
    XSGW_ONLINE("11", new MD5Util().getMD5("ys123456"), "uid_c01f9419", "online", "11"),

    /**
     * 销售顾问
     */
    XS_ONLINE("xs", new MD5Util().getMD5("ys123456"), "", "online", "xs"),

    /**
     * 保养顾问
     */
    BYGW_ONLINE("55", new MD5Util().getMD5("ys123456"), "uid_b4c2c8e2", "online", "保养顾问"),

    /**
     * 保时捷线上总经
     */
    ZJL_ONLINE("zjl", new MD5Util().getMD5("ys123456"), "uid_a6452755", "online", "总经理");

    EnumAccount(String account, String password, String uid, String environment, String username) {
        this.account = account;
        this.password = password;
        this.uid = uid;
        this.environment = environment;
        this.username = username;
    }

    @Getter
    private final String account;

    @Getter
    private final String password;

    @Getter
    private final String uid;

    @Getter
    private final String environment;

    @Getter
    private final String username;
}


