package com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config;

import lombok.Getter;

import java.util.Arrays;

/**
 * 小程序token枚举
 * 命名格式：项目_名字_环境
 * 若有小号：项目_名字_SMALL_环境
 * 若失效统一修改此类，统一管理
 */
public enum EnumAppletToken {
    BSJ_WM_DAILY(EnumTestProduce.PORSCHE_DAILY.getAbbreviation(), "Zg0LsqAeP25MGL0ulmDtDg==", true, "15321527989"),

    BSJ_XMF_DAILY(EnumTestProduce.PORSCHE_DAILY.getAbbreviation(), "X1vn9SlgXS1ohNyW9qRCjg==", true, "15037286013"),

    BSJ_GLY_DAILY(EnumTestProduce.PORSCHE_DAILY.getAbbreviation(), "MLlSMAp5oXR7cGuCfY6+NA==", true, "13373166806"),

    BSJ_XMF_ONLINE(EnumTestProduce.PORSCHE_DAILY.getAbbreviation(), "cMzFau3/cRA7nTf/r3X3GQ==", false, "15037286013"),

    BSJ_WM_ONLINE(EnumTestProduce.PORSCHE_DAILY.getAbbreviation(), "m0XhSoGDFBdWwLt/XA2URA==", false, "15321527989"),

    BSJ_WM_SMALL_ONLINE(EnumTestProduce.PORSCHE_DAILY.getAbbreviation(), "UhqugJ3HUDI7BRKnIvxxKg==", false, "15321527989"),

    JC_WM_DAILY(EnumTestProduce.JC_DAILY.getAbbreviation(), "uhFcIKEc05Q50D9kEfLkBg==", true, "15321527989"),

    JC_WM_ONLINE(EnumTestProduce.JC_DAILY.getAbbreviation(), "aTcP39Q3mJAUauGwmq2VMg==", false, "15321527989"),

    JC_XMF_DAILY(EnumTestProduce.JC_DAILY.getAbbreviation(), "hZiYL+y2QvANm4IfJFka9A==", false, "15037286013"),

    JC_XMF_ONLINE(EnumTestProduce.JC_DAILY.getAbbreviation(), "EDCYsAK+NKACPGAEN8ToJQ==", false, "15037286013"),

    JC_LXQ_DAILY(EnumTestProduce.JC_DAILY.getAbbreviation(), "u8hGGZ9NPqOEdfO3w04QFA==", false, "13436941018"),

    JC_GLY_DAILY(EnumTestProduce.JC_DAILY.getAbbreviation(), "1nxb35B/gDlj6Q88uD5yEA==", true, "13373166806"),

    JC_GLY_ONLINE(EnumTestProduce.JC_DAILY.getAbbreviation(), "iCMVUEzq+zOmzldZM2awEQ==", false, "13373166806"),

    INS_WM_DAILY(EnumTestProduce.INS_DAILY.getAbbreviation(), "UMe3BrC5qlTNlqD9u9uPkQ==", true, "15321527989"),

    INS_WM_ONLINE(EnumTestProduce.INS_ONLINE.getAbbreviation(), "", false, "15321527989"),

    INS_ZT_DAILY(EnumTestProduce.INS_DAILY.getAbbreviation(), "DR5xZkyFawbAh4RSNPsWrg==", true, "13604609869"),

    INS_ZT_ONLINE(EnumTestProduce.INS_ONLINE.getAbbreviation(), "", false, "13604609869"),
    ;

    EnumAppletToken(String abbreviation, String token, boolean isDaily, String phone) {
        this.abbreviation = abbreviation;
        this.token = token;
        this.isDaily = isDaily;
        this.phone = phone;
    }

    @Getter
    private final String abbreviation;
    @Getter
    private final String token;
    @Getter
    private final boolean isDaily;
    @Getter
    private final String phone;

    public static String getPhoneByToken(String token) {
        return Arrays.stream(EnumAppletToken.values()).filter(e -> e.getToken().equals(token)).findFirst().map(EnumAppletToken::getPhone).orElse(JC_GLY_ONLINE.getPhone());
    }

    public static EnumAppletToken getEnumByToken(String token) {
        return Arrays.stream(EnumAppletToken.values()).filter(e -> e.getToken().equals(token)).findFirst().orElse(null);
    }
}
