package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.commodity;

import com.google.common.base.Preconditions;

import java.util.Arrays;
import java.util.Optional;

/**
 * 发卷状态
 * @author wangmin
 * @date 2021/1/12 20:42
 */
public enum VoucherSendStatusEnum {

    /**
     * 已发
     */
    SENT("已发"),

    /**
     * 未发
     */
    NOT_SENT("未发");



    private final String name;

    VoucherSendStatusEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }



    public static VoucherSendStatusEnum findById(String type) {
        Optional<VoucherSendStatusEnum> any = Arrays.stream(values()).filter(t -> t.name().equals(type))
                .findAny();
        Preconditions.checkArgument(any.isPresent(), "发卷状态不存在");
        return any.get();
    }
}
