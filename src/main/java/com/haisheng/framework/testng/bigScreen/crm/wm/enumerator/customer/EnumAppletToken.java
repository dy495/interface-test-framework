package com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.customer;

import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumProduce;
import lombok.Getter;

/**
 * 小程序token枚举
 * 命名格式：项目_名字_环境
 * 若有小号：项目_名字_SMALL_环境
 * 若失效统一修改此类，统一管理
 */
public enum EnumAppletToken {
    BSJ_WM_DAILY(EnumProduce.BSJ.name(), "gtrOgq5Rxy2fF79cR+s7xw==", "w", true, "oQwmJ5Lm1mlt4HXbxYuZi8L7CW90"),

    BSJ_WM_SMALL_DAILY(EnumProduce.BSJ.name(), "secrarR6b4DrZ/7lkmB4LQ==", "纯牛奶", true, ""),

    BSJ_XMF_DAILY(EnumProduce.BSJ.name(), "Yyq04ubFzxKP8GzWC1yhzw==", "@@@", true, ""),

    BSJ_GLY_DAILY(EnumProduce.BSJ.name(), "v6CrjxBj/3TGYzE52mY6qQ==", "Max", true, ""),

    BSJ_XMF_ONLINE(EnumProduce.BSJ.name(), "cw4sTi87+ZWGuDqdsfZXSA==", "@@@", false, ""),

    BSJ_WM_ONLINE(EnumProduce.BSJ.name(), "TIMWRzr8Z90Z1irPhNtklQ==", "w", false, ""),

    BSJ_WM_SMALL_ONLINE(EnumProduce.BSJ.name(), "dtp9Ydtr21uRLuK4+H7XgQ==", "纯牛奶", false, ""),

    JC_WM_DAILY(EnumProduce.JC.name(), "Bjqq43gwyVsmUWYOi+AW5w==", "", true, ""),

    JC_WM_ONLINE(EnumProduce.JC.name(), "FzYbYDjtpl1OaSUnw8YjsA==", "", false, ""),

    JC_XMF_DAILY(EnumProduce.JC.name(), "3QQYlO1DtjV5mwp2hP/cwg==", "", false, ""),
    JC_XMF_ONLINE(EnumProduce.JC.name(), "zu+0zdqBw70D0R9WQK9C+A==", "", false, ""),
    ;

    EnumAppletToken(String produce, String token, String wechatName, boolean isDaily, String wechatId) {
        this.produce = produce;
        this.token = token;
        this.wechatName = wechatName;
        this.isDaily = isDaily;
        this.wechatId = wechatId;
    }

    @Getter
    private final String produce;
    @Getter
    private final String token;
    @Getter
    private final String wechatName;
    @Getter
    private final boolean isDaily;
    @Getter
    private final String wechatId;

}
