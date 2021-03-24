package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.activity;

import com.google.common.base.Preconditions;

/**
 * @author : wangmin
 * @date :  2021/1/20  15:09
 */
public enum RewardTypeEnum {
    /**
     * 卡券
     */
    VOUCHER(1, "卡券");


    private Integer id;

    private String typeName;

    RewardTypeEnum(Integer id, String typeName) {
        this.id = id;
        this.typeName = typeName;
    }

    public Integer getId() {
        return id;
    }

    public String getTypeName() {
        return typeName;
    }

    public static RewardTypeEnum findById(Integer id) {
        Preconditions.checkArgument(VOUCHER.getId().equals(id), "奖励类型不存在");
        return VOUCHER;
    }
}
