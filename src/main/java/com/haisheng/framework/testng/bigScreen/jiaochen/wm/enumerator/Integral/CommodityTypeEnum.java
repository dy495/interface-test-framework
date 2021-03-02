package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.Integral;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Optional;

/**
 * 商品类型
 *
 * @author wangmin
 * @date 2020/12/15 4:38 PM
 */
public enum CommodityTypeEnum {
    /**
     * 虚拟商品
     */
    FICTITIOUS("虚拟商品"),

    /**
     * 真实商品
     */
    REAL("实物");

    private String name;

    CommodityTypeEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static CommodityTypeEnum findByType(String type) {
        Preconditions.checkArgument(StringUtils.isNotEmpty(type), "商品类型不存在");
        Optional<CommodityTypeEnum> any = Arrays.stream(values()).filter(v -> v.name().equals(type)).findAny();
        Preconditions.checkArgument(any.isPresent(), "商品类型不存在");
        return any.get();
    }
}
