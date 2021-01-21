package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.Integral;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author wangmin
 * @date 2020/12/28 2:20 PM
 * @desc 积分品类
 */
public enum IntegralCategoryTypeEnum {
    /**
     * 一级品类
     */
    FIRST_CATEGORY("一级品类"),
    SECOND_CATEGORY("二级品类"),
    THIRD_CATEGORY("三级品类");

    private String desc;

    IntegralCategoryTypeEnum(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public static IntegralCategoryTypeEnum findByType(String type) {
        Preconditions.checkArgument(StringUtils.isNotEmpty(type), "商品品类不存在");
        Optional<IntegralCategoryTypeEnum> any = Arrays.stream(values()).filter(v -> v.name().equals(type)).findAny();
        Preconditions.checkArgument(any.isPresent(), "商品品类不存在");
        return any.get();
    }
}
