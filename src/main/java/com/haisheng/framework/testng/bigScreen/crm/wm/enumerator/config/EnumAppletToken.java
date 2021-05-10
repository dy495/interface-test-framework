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
    BSJ_WM_DAILY(EnumTestProduce.PORSCHE_DAILY.getAbbreviation(), "AxWi53xBTv/EKpEkNdrJBg==", true, "15321527989"),

    BSJ_XMF_DAILY(EnumTestProduce.PORSCHE_DAILY.getAbbreviation(), "b1PO9+C+Bqo742yJc2bPYw==", true, "15037286013"),

    BSJ_GLY_DAILY(EnumTestProduce.PORSCHE_DAILY.getAbbreviation(), "Dkj9ox+0FtVN20Ka1z89HA==", true, "13373166806"),

    BSJ_XMF_ONLINE(EnumTestProduce.PORSCHE_DAILY.getAbbreviation(), "cMzFau3/cRA7nTf/r3X3GQ==", false, "15037286013"),

    BSJ_WM_ONLINE(EnumTestProduce.PORSCHE_DAILY.getAbbreviation(), "+Lxwl2EDyIVPo3SD2sFuvw==", false, "15321527989"),

    BSJ_WM_SMALL_ONLINE(EnumTestProduce.PORSCHE_DAILY.getAbbreviation(), "29c11pEn61vmjuKxyX5WZw==", false, "15321527989"),

    JC_WM_DAILY(EnumTestProduce.JC_DAILY.getAbbreviation(), "jVXDdP1MPHXpNPrzl5LRLg==", true, "15321527989"),

    JC_WM_ONLINE(EnumTestProduce.JC_DAILY.getAbbreviation(), "aTcP39Q3mJAUauGwmq2VMg==", false, "15321527989"),

    JC_XMF_DAILY(EnumTestProduce.JC_DAILY.getAbbreviation(), "LH4AyoNJqHnlX51e7pKwDQ==", false, "15037286013"),

    JC_XMF_ONLINE(EnumTestProduce.JC_DAILY.getAbbreviation(), "doZhCo6nCMQq1HBzaToqww==", false, "15037286013"),

    JC_LXQ_DAILY(EnumTestProduce.JC_DAILY.getAbbreviation(), "ORwlXpsC2yWsZ7f+tPMV9A==", false, "13436941018"),

    JC_LXQ_ONLINE(EnumTestProduce.JC_DAILY.getAbbreviation(), "ORwlXpsC2yWsZ7f+tPMV9A==", false, "13436941018"),

    JC_GLY_DAILY(EnumTestProduce.JC_DAILY.getAbbreviation(), "vkspHVxuqSlNsXYMNFlmaA==", true, "13373166806"),

    JC_GLY_ONLINE(EnumTestProduce.JC_DAILY.getAbbreviation(), "fyjmSYPLARzumAnIbPrt5g==", false, "13373166806"),

    INS_WM_DAILY(EnumTestProduce.INS_DAILY.getAbbreviation(), "n+KZJeG41Z0Nu11800aOEw==", true, "15321527989"),

    INS_WM_ONLINE(EnumTestProduce.INS_ONLINE.getAbbreviation(), "TnJd0oOQ8wznvndM+0hV6A==", false, "15321527989"),

    INS_ZT_DAILY(EnumTestProduce.INS_DAILY.getAbbreviation(), "sck5BA9NaaCU6ar//hPJaA==", true, "13604609869"),

    INS_ZT_ONLINE(EnumTestProduce.INS_ONLINE.getAbbreviation(), "Com3dkGhBuuZgzAir8SRDw==", false, "13604609869"),
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
