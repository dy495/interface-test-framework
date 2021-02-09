package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Optional;

/**
 * 卡券使用状态
 *
 * @author wangmin
 * @date 2020/11/23 2:31 PM
 */
public enum VoucherUseStatusEnum {
    NORMAL("未使用"),
    IS_USED("已使用"),
    NEAR_EXPIRE("快过期"),
    EXPIRED("已过期"),
    ;
    private final String name;

    VoucherUseStatusEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static VoucherUseStatusEnum findByName(String name) {
        Preconditions.checkArgument(StringUtils.isNotEmpty(name), "使用状态不存在");
        Optional<VoucherUseStatusEnum> any = Arrays.stream(values()).filter(v -> v.name().equals(name)).findAny();
        Preconditions.checkArgument(any.isPresent(), "使用状态不存在");
        return any.get();
    }
}
