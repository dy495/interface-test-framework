package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing;

import lombok.Getter;

/**
 * @author wangmin
 * @date 2021/1/20 13:36
 * @desc 卡券审核状态
 */
public enum ApplyStatusEnum {
    AUDITING("审核中"),
    AGREE("已通过"),
    REFUSAL("已拒绝"),
    CANCEL("已取消"),
    ;

    ApplyStatusEnum(String name) {
        this.name = name;
    }

    @Getter
    private final String name;

}
