package com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.customer;

import lombok.Getter;

/**
 * @author wangmin
 * @date 2020/8/10 21:14
 */
public enum EnumAppletCode {

    WM("w", "mvc+oq2YJNx2kvNdoo+iFg==", "oQwmJ5Lm1mlt4HXbxYuZi8L7CW90"),

    WM_SMALL("纯牛奶", "wq1v/03v0bNq45HAK1X1Vw==", ""),

    XMF("@@@", "HNzwWcPqjsREdLWRsSZ9fA==", ""),

    XMFONLINE("@@@", "zagLk0XwiZZo9+f36Yx9Lw==", ""),

    BB("北北", "DJi5GeKsEMIMe3H4XEeqmw==", ""),    //请勿使用

    LXQ("泡芙", "sTyWUt/pmhwGAV1sf0YWrQ==", ""),

    GLY("Max", "ro6uJhVfsijLjKbx/qLlGw==", ""),

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
