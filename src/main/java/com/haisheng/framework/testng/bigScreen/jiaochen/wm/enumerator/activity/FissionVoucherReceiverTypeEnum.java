package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.activity;

/**
 * @author : wangmin
 * @date :  2021/1/20  15:03
 */

public enum FissionVoucherReceiverTypeEnum {

    /**
     * 分享者
     */
    SHARER,
    /**
     * 被邀请者
     */
    INVITEES;

    public static FissionVoucherReceiverTypeEnum findById(int id) {
        if (SHARER.ordinal() == id) {
            return SHARER;
        }
        if (INVITEES.ordinal() == id) {
            return INVITEES;
        }
        throw new IllegalArgumentException("卡券接收人类型不存在");
    }
}
