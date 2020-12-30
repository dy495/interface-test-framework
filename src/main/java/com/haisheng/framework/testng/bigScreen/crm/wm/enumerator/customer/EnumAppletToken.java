package com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.customer;

import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumProduce;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 小程序token枚举
 * 命名格式：项目_名字_环境
 * 若有小号：项目_名字_SMALL_环境
 * 若失效统一修改此类，统一管理
 */
public enum EnumAppletToken {
    BSJ_WM_DAILY(EnumProduce.BSJ.name(), "pR9YaWL+SUN3c4hpgP9ymg==", "w", true, "oQwmJ5Lm1mlt4HXbxYuZi8L7CW90", "15321527989"),

    BSJ_WM_SMALL_DAILY(EnumProduce.BSJ.name(), "usxKib/bBsV+ahtyvx+/RA==", "纯牛奶", true, "", "15321527989"),

    BSJ_XMF_DAILY(EnumProduce.BSJ.name(), "xYbesUkHQTXI0Zf5Cbp/FQ==", "@@@", true, "", ""),

    BSJ_GLY_DAILY(EnumProduce.BSJ.name(), "YfRMJg382YOSCOMovQs0CQ==", "Max", true, "", "13373166806"),

    BSJ_XMF_ONLINE(EnumProduce.BSJ.name(), "E++thmPRX4y1TgxMjvpe9Q==", "@@@", false, "", ""),

    BSJ_WM_ONLINE(EnumProduce.BSJ.name(), "/l2SriTdlX3tII0n/bbwtw==", "w", false, "", "15321527989"),

    BSJ_WM_SMALL_ONLINE(EnumProduce.BSJ.name(), "4It447PUdHX0+ZOwM6EOuA==", "纯牛奶", false, "", "15321527989"),

    JC_WM_DAILY(EnumProduce.JC.name(), "4X5Z/xEJq06LP5uBJIBgpQ==", "", true, "", "15321527989"),

    JC_WM_ONLINE(EnumProduce.JC.name(), "1/T/yuq+K0z8JvDer+1fIw==", "", false, "", "15321527989"),

    JC_XMF_DAILY(EnumProduce.JC.name(), "iZjgzpm0ltTWoM9UQQmkAQ==", "", false, "", ""),
    JC_XMF_ONLINE(EnumProduce.JC.name(), "cu7N+XqQ0AFa5QHXmqUm2A==", "", false, "", ""),

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

    public static String getPhoneByToken(String token) {
        List<String> phoneList = Arrays.stream(EnumAppletToken.values()).filter(e -> e.getToken().equals(token)).map(EnumAppletToken::getPhone).collect(Collectors.toList());
        return phoneList.size() == 0 ? JC_GLY_ONLINE.getPhone() : phoneList.get(0);
    }
}
