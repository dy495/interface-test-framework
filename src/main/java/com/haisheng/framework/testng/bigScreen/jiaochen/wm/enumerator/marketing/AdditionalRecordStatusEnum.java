package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing;

import lombok.Getter;

/**
 * @author wangmin
 * @date 2021/1/22 17:48
 */
public enum AdditionalRecordStatusEnum {

    /**
     * 待审核
     */
    WAITING(0, "审核中"),

    /**
     * 已撤回
     */
    RECALL(1, "已撤回"),

    /**
     * 审核未通过
     */
    REJECT(2, "已拒绝"),

    /**
     * 进行中
     */
    WORKING(3, "已通过"),
    ;

    AdditionalRecordStatusEnum(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    @Getter
    private final Integer id;

    @Getter
    private final String name;
}
