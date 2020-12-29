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
    BSJ_WM_DAILY(EnumProduce.BSJ.name(), "HPLfevPPf88Hvql8BkP2tA==", "w", true, "oQwmJ5Lm1mlt4HXbxYuZi8L7CW90", "15321527989"),

    BSJ_WM_SMALL_DAILY(EnumProduce.BSJ.name(), "PXi/v1x6FLDJsVHry8mFGw==", "纯牛奶", true, "", "15321527989"),

    BSJ_XMF_DAILY(EnumProduce.BSJ.name(), "Yyq04ubFzxKP8GzWC1yhzw==", "@@@", true, "", ""),

    BSJ_GLY_DAILY(EnumProduce.BSJ.name(), "YfRMJg382YOSCOMovQs0CQ==", "Max", true, "", "13373166806"),

    BSJ_XMF_ONLINE(EnumProduce.BSJ.name(), "cw4sTi87+ZWGuDqdsfZXSA==", "@@@", false, "", ""),

    BSJ_WM_ONLINE(EnumProduce.BSJ.name(), "TIMWRzr8Z90Z1irPhNtklQ==", "w", false, "", "15321527989"),

    BSJ_WM_SMALL_ONLINE(EnumProduce.BSJ.name(), "dtp9Ydtr21uRLuK4+H7XgQ==", "纯牛奶", false, "", "15321527989"),

    JC_WM_DAILY(EnumProduce.JC.name(), "l2fRw5k+rH4Mqc/BEKDemw==", "", true, "", "15321527989"),

    JC_WM_ONLINE(EnumProduce.JC.name(), "B6H0gkhiYEpVOiEfDs5Sqw==", "", false, "", "15321527989"),

    JC_XMF_DAILY(EnumProduce.JC.name(), "X7ub38LmihAlj4uUUIOWaQ==", "", false, "", ""),
    JC_XMF_ONLINE(EnumProduce.JC.name(), "zu+0zdqBw70D0R9WQK9C+A==", "", false, "", ""),

    JC_GLY_DAILY(EnumProduce.JC.name(), "Cu4VbW1A3clBjgwPzAM+KQ==", "Max", true, "", "13373166806"),
    JC_GLY_ONLINE(EnumProduce.JC.name(), "cjEW0H1AT+uU35ELU+UFbQ==", "Max", false, "", "13373166806"),
    ;

    EnumAppletToken(String produce, String token, String wechatName, boolean isDaily, String wechatId, String phone) {
        this.produce = produce;
        this.token = token;
        this.wechatName = wechatName;
        this.isDaily = isDaily;
        this.wechatId = wechatId;
        this.phone = phone;
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
    @Getter
    private final String phone;

}
