package com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.sale;

import com.haisheng.framework.util.MD5Util;
import lombok.Getter;
import org.springframework.util.StringUtils;

/**
 * 人员账号
 * 账号、密码、uid、环境、名称
 *
 * @author wangmin
 * @date 2020/7/24 16:54
 */
public enum EnumAccount {

    /**
     * 销售顾问
     */
    XS_TEMP_DAILY("xsgwtemp", getPassword("123456"), "uid_41786c76", "daily", "销售顾问temp"),

    /**
     * 销售顾问
     */
    XS_DAILY("wm", getPassword("123456"), "", "daily", "wm"),

    /**
     * 保养顾问
     */
    FW_BAOYANG_DAILY("baoyangr", getPassword("123456"), "", "daily", "baoyangr"),

    /**
     * 总经理
     */
    ZJL_DAILY("zjl", getPassword("123456"), "uid_d3d35577", "daily", "总经理123456"),

    /**
     * 前台
     */
    QT_DAILY("qt", getPassword("123456"), "uid_05e8599f", "daily", ""),

    /**
     * 保时捷线上销售顾问
     */
    XS_11_ONLINE("11", getPassword("ys123456"), "uid_c01f9419", "online", "11"),

    /**
     * 保时捷线上销售顾问
     */
    XS_22_ONLINE("22", getPassword("ys123456"), "uid_da55f38d", "online", "22"),

    /**
     * 销售顾问
     */
    XS_CS_ONLINE("cs", getPassword("ys123456"), "", "online", "cs"),

    /**
     * 前台
     */
    QT_ONLINE("qt", getPassword("ys123456"), "uid_8ef9ee4c", "online", "销售前台"),

    /**
     * 保养顾问
     */
    FW_55_ONLINE("55", getPassword("ys123456"), "uid_b4c2c8e2", "online", "保养顾问"),

    /**
     * 保时捷线上总经
     */
    ZJL_ONLINE("zjl", getPassword("ys123456"), "uid_a6452755", "online", "总经理"),

    /**
     * 客户使用保时捷线上，不要轻易使用
     */
    ZJ_ONLINE_PORSCHE("xszj", getPassword("123456"), "", "online", "");

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

    private static String getPassword(String s) {
        if (StringUtils.isEmpty(s)) {
            throw new RuntimeException("password is null");
        }
        return new MD5Util().getMD5(s);
    }
}


