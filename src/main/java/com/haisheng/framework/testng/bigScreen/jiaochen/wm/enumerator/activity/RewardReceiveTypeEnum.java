package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.activity;

/**
 * @author : wangmin
 * @date :  2020/12/23  17:13
 */
public enum RewardReceiveTypeEnum {

    /**
     * 自动发放
     */
    AUTO,
    /**
     * 主动领取
     */
    ACTIVE;

    public static RewardReceiveTypeEnum findById(int id) {
        if (AUTO.ordinal() == id) {
            return AUTO;
        }
        if (ACTIVE.ordinal() == id) {
            return ACTIVE;
        }
        throw new IllegalArgumentException("奖励发放类型不存在");
    }
}
