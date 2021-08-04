package com.haisheng.framework.testng.bigScreen.itemBasic.enumerator;

import lombok.Getter;

import java.util.Arrays;

/**
 * 小程序token枚举
 * 命名格式：项目_名字_环境
 * 若有小号：项目_名字_SMALL_环境
 * 若失效统一修改此类，统一管理
 */
public enum EnumAppletToken {
    BSJ_WM_DAILY("AxWi53xBTv/EKpEkNdrJBg==", true, "15321527989"),

    BSJ_XMF_DAILY("b1PO9+C+Bqo742yJc2bPYw==", true, "15037286013"),

    BSJ_GLY_DAILY("fhZYUT+W895nJw5UeUX4rw==", true, "13373166806"),

    BSJ_XMF_ONLINE("sht4pAFOJ1qo4qC3AyzZGQ==", false, "15037286013"),

    BSJ_WM_ONLINE("+Lxwl2EDyIVPo3SD2sFuvw==", false, "15321527989"),

    BSJ_WM_SMALL_ONLINE("29c11pEn61vmjuKxyX5WZw==", false, "15321527989"),

    JC_WM_DAILY("+4QvEng6s0rnqlfzyztttg==", true, "15321527989"),

    JC_WM_ONLINE("ozWh4QcCow9uvHXvVGoQAg==", false, "15321527989"),

    JC_LXQ_DAILY("/EyTUCJ03NrqyYF9iYtWig==", false, "13436941018"),

    JC_LXQ_ONLINE("sdPfrCq3BAurovfyqBA95g==", false, "13436941018"),

    JC_GLY_DAILY("HBpN0dfUrLK/Nak8Zjmg9Q==", true, "13373166806"),

    JC_GLY_ONLINE("Lle2O9LilbiQEcbGAFHE2A==", false, "13373166806"),

    INS_WM_DAILY("mdBX0R2xzhXwswuUFUDwAw==", true, "13373166806"),

    INS_WM_ONLINE("TnJd0oOQ8wznvndM+0hV6A==", false, "15321527989"),

    INS_ZT_DAILY("oivuHuPuOxBVkx1QiOXq/A==", true, "13604609869"),

    INS_ZT_ONLINE("Com3dkGhBuuZgzAir8SRDw==", false, "13604609869"),
    ;

    EnumAppletToken(String token, boolean isDaily, String phone) {
        this.token = token;
        this.isDaily = isDaily;
        this.phone = phone;
    }

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
