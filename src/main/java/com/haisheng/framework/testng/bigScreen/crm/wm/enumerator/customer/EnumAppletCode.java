package com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.customer;

import lombok.Getter;

/**
 * @author wangmin
 * @date 2020/8/10 21:14
 */
public enum EnumAppletCode {

    WM("w", "gtrOgq5Rxy2fF79cR+s7xw==", "oQwmJ5Lm1mlt4HXbxYuZi8L7CW90"),

    WM_SMALL("纯牛奶", "S6yhfjH4cUtWbNqGRmGp4Q==", ""),

    XMF("@@@", "SwKzoo8aH3HfvzJx1iIqsQ==", ""),

    XMFONLINE("@@@", "aM6oYGul72g1w3F4I2BoFg==", ""),

    BB("北北", "DJi5GeKsEMIMe3H4XEeqmw==", ""),    //请勿使用

    LXQ("泡芙", "sTyWUt/pmhwGAV1sf0YWrQ==", ""),

    GLY("Max", "v6CrjxBj/3TGYzE52mY6qQ==", ""),

    WM_ONLINE("w", "nDQrQZlv4wGTNvNy7Qy78Q==", ""),

    WM_SMALL_ONLINE("纯牛奶", "e7KgTxVJJTGokmjSk2exQA==", "");

    EnumAppletCode(String weChatName, String code, String weChatId) {
        this.code = code;
        this.weChatName = weChatName;
        this.weChatId = weChatId;
    }

    @Getter
    private final String code;
    @Getter
    private final String weChatName;
    @Getter
    private final String weChatId;
}
