package com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.customer;

import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumProduce;
import lombok.Getter;

import java.util.Arrays;

/**
 * 小程序token枚举
 * 命名格式：项目_名字_环境
 * 若有小号：项目_名字_SMALL_环境
 * 若失效统一修改此类，统一管理
 */
public enum EnumAppletToken {
    BSJ_WM_DAILY(EnumProduce.BSJ.name(), "pR9YaWL+SUN3c4hpgP9ymg==", "w", true, "oQwmJ5Lm1mlt4HXbxYuZi8L7CW90", "15321527989"),

    BSJ_WM_SMALL_DAILY(EnumProduce.BSJ.name(), "usxKib/bBsV+ahtyvx+/RA==", "纯牛奶", true, "", "15321527989"),

    BSJ_XMF_DAILY(EnumProduce.BSJ.name(), "xYbesUkHQTXI0Zf5Cbp/FQ==", "@@@", true, "", "15037286013"),

    BSJ_GLY_DAILY(EnumProduce.BSJ.name(), "ykcw+P7/BrfMSFfxHQkfdg==", "Max", true, "", "13373166806"),

    BSJ_XMF_ONLINE(EnumProduce.BSJ.name(), "cMzFau3/cRA7nTf/r3X3GQ==", "@@@", false, "", "15037286013"),

    BSJ_WM_ONLINE(EnumProduce.BSJ.name(), "6nJhC4e34NB7jcqUVOonRQ==", "w", false, "", "15321527989"),

    BSJ_WM_SMALL_ONLINE(EnumProduce.BSJ.name(), "Fhh9NQmWUe+iNT+hOlivvg==", "纯牛奶", false, "", "15321527989"),

    JC_WM_DAILY(EnumProduce.JC.name(), "eVvKxoI0LSCdMQJoN3Qg7g==", "", true, "", "15321527989"),

    JC_WM_ONLINE(EnumProduce.JC.name(), "aTcP39Q3mJAUauGwmq2VMg==", "", false, "", "15321527989"),

    JC_XMF_DAILY(EnumProduce.JC.name(), "hZiYL+y2QvANm4IfJFka9A==", "", false, "", "15037286013"),

    JC_XMF_ONLINE(EnumProduce.JC.name(), "cu7N+XqQ0AFa5QHXmqUm2A==", "", false, "", "15037286013"),

    JC_LXQ_DAILY(EnumProduce.JC.name(), "31Z9jgBlSsTIqRTYxOWaKw==", "", false, "", "13436941018"),

    JC_GLY_DAILY(EnumProduce.JC.name(), "neY6Yoiafw8h0MmK3VXdtQ==", "Max", true, "", "13373166806"),
    JC_GLY_ONLINE(EnumProduce.JC.name(), "cjEW0H1AT+uU35ELU+UFbQ==", "Max", false, "", "13373166806"),
    ;

    EnumAppletToken(String product, String token, String wechatName, boolean isDaily, String wechatId, String phone) {
        this.product = product;
        this.token = token;
        this.wechatName = wechatName;
        this.isDaily = isDaily;
        this.wechatId = wechatId;
        this.phone = phone;
    }

    @Getter
    private final String product;
    @Getter
    private final String token;
    @Getter
    private final String wechatName;
    @Getter
    private final boolean isDaily;
    @Getter
    private final String wechatId;
    @Getter
    private final String phone;

    public static String getPhoneByToken(String token) {
        return Arrays.stream(EnumAppletToken.values()).filter(e -> e.getToken().equals(token)).map(EnumAppletToken::getPhone).findFirst().orElse(JC_GLY_ONLINE.getPhone());
    }
}
