package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing;

import lombok.Getter;

/**
 * 卡券审核状态
 *
 * @author wangmin
 * @date 2021/1/20 13:36
 */
public enum ApplyStatusEnum {
    AUDITING(0, "审核中"),
    AGREE(1, "已通过"),
    REFUSAL(2, "审核未通过"),
    CANCEL(3, "已撤回"),
    ;

    ApplyStatusEnum(Integer id, String name) {
        this.name = name;
        this.id = id;
    }

    @Getter
    private final Integer id;
    @Getter
    private final String name;

}
