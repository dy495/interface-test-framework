package com.haisheng.framework.testng.bigScreen.itemPorsche.enumerator.sale;

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
     * 总经理
     */
    ZJL_DAILY("zjl", getPassword("123456"), "uid_e5d21cbc", true, "zjl"),

    /**
     * 销售顾问
     */
    SALE("xsgw", getPassword("123456"), "xsgw", true, "xsgw"),

    /**
     * 销售顾问wang
     */
    SALE_WANG("wm", getPassword("123456"), "", true, "wm"),

    /**
     * 保养顾问
     */
    MAINTAIN("weixiu", getPassword("123456"), "uid_a3301290", true, "weixiu"),

    /**
     * 保时捷线上销售顾问
     */
    XS_11_ONLINE("11", getPassword("ys123456"), "uid_c01f9419", false, "11"),

    /**
     * 保时捷线上销售顾问
     */
    XS_22_ONLINE("22", getPassword("ys123456"), "uid_da55f38d", false, "22"),

    /**
     * 销售顾问
     */
    XS_CS_ONLINE("cs", getPassword("ys123456"), "", false, "cs"),

    /**
     * 前台
     */
    QT_ONLINE("qt", getPassword("ys123456"), "uid_8ef9ee4c", false, "销售前台"),

    /**
     * 保养顾问
     */
    FW_55_ONLINE("55", getPassword("ys123456"), "uid_b4c2c8e2", false, "保养顾问"),

    /**
     * 保时捷线上总经
     */
    ZJL_ONLINE("zjl", getPassword("ys123456"), "uid_4f27fd2c", false, "zjl"),

    /**
     * 客户使用保时捷线上，不要轻易使用
     */
    ZJ_ONLINE_PORSCHE("xszj", getPassword("123456"), "", false, "");

    EnumAccount(String account, String password, String uid, Boolean isDaily, String username) {
        this.account = account;
        this.password = password;
        this.uid = uid;
        this.isDaily = isDaily;
        this.username = username;
    }

    @Getter
    private final String account;

    @Getter
    private final String password;

    @Getter
    private final String uid;

    @Getter
    private final Boolean isDaily;

    @Getter
    private final String username;

    private static String getPassword(String s) {
        if (StringUtils.isEmpty(s)) {
            throw new RuntimeException("password is null");
        }
        return new MD5Util().getMD5(s);
    }
}


