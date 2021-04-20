package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.Integral;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author wangmin
 * @date 2021/1/8 2:27 PM

 */
public enum ExchangeRuleTypeEnum {
    /**
     *
     */
    YEAR("有效期"),
    DESCRIPTION("说明");

    private String desc;

    ExchangeRuleTypeEnum(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public static ExchangeRuleTypeEnum findByType(String type) {
        Preconditions.checkArgument(StringUtils.isNotEmpty(type), "规则类型不存在");
        Optional<ExchangeRuleTypeEnum> any = Arrays.stream(values()).filter(v -> v.name().equals(type)).findAny();
        Preconditions.checkArgument(any.isPresent(), "规则类型不存在");
        return any.get();
    }
}
