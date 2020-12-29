package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator;

import lombok.Getter;

public enum EnumApplyStatusName {
    /**
     * 审核中
     */
    AUDITING(0, "审核中"),
    /**
     * 同意
     */
    AGREE(1, "已通过"),
    /**
     * 拒绝
     */
    REFUSAL(2, "已拒绝"),
    /**
     * 取消
     */
    CANCEL(3, "已取消");


    EnumApplyStatusName(int status, String name) {
        this.status = status;
        this.name = name;
    }

    @Getter
    private final int status;
    @Getter
    private final String name;

}
