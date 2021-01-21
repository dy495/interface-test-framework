package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.audit;

import com.google.common.base.Preconditions;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author wangmin
 * @date 2020-07-06 16:30
 */
public enum AuditStatusEnum {
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

    private Integer code;
    private String name;

    AuditStatusEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }


    public static AuditStatusEnum findByCode(Integer code) {
        Preconditions.checkArgument(code != null, "审核状态不存在");
        Optional<AuditStatusEnum> any = Arrays.stream(values()).filter(e -> e.getCode().equals(code))
                .findAny();
        Preconditions.checkArgument(any.isPresent(), "审核状态不存在");
        return any.get();
    }
}
